/**
 * Sheet Detector UI helper for the Recipe Builder popup.
 *
 * Manages the refresh button + other-tabs picker that appear below the
 * Target Sheet URL input. Communicates with the SW via
 * GET_SHEET_ID_FROM_ACTIVE_TAB (enhanced to return active + otherTabs).
 *
 * Exported API:
 *   initSheetDetector({ inputEl, warningEl, errorEl, onDetected })
 *     Returns { runDetection }  — call to trigger a fresh SW query.
 */

/**
 * @typedef {{ sheetId: string, tabUrl: string, title: string }} SheetTab
 * @typedef {{ active: SheetTab|null, otherTabs: SheetTab[] }} DetectionResult
 */

import { t } from '../i18n-loader.js';

const SHEET_URL_REGEX = /\/spreadsheets\/d\/([a-zA-Z0-9_-]+)/;

/**
 * Build a human-readable label for a tab, stripping " - Google Sheets" suffix.
 * @param {SheetTab} tab
 * @returns {string}
 */
function tabLabel(tab) {
    return (tab.title || tab.tabUrl).replace(/ [-–] Google Sheets$/i, '').trim() || tab.tabUrl;
}

/**
 * Initialize detection UI elements inline after the sheet URL input.
 *
 * @param {object} opts
 * @param {HTMLInputElement} opts.inputEl       - #bld-sheet-url
 * @param {HTMLElement}      opts.warningEl     - #bld-sheet-warning
 * @param {HTMLElement}      opts.errorEl       - #bld-sheet-url-error
 * @param {(url: string) => void} opts.onDetected - called when user picks a tab URL
 * @returns {{ runDetection: () => Promise<void> }}
 */
export function initSheetDetector({ inputEl, warningEl, errorEl, onDetected }) {
    // ── Create refresh button ────────────────────────────────────────────────
    const refreshBtn = document.createElement('button');
    refreshBtn.type = 'button';
    refreshBtn.id = 'bld-detect-refresh';
    refreshBtn.className = 'btn-detect-refresh';
    refreshBtn.title = t('btnDetectRefreshTitle') || 'Re-detect Sheets tabs';
    refreshBtn.textContent = '↻';
    // Insert refresh button as a sibling of inputEl, wrapped in a relative container
    _wrapInputWithRefreshBtn(inputEl, refreshBtn);

    // ── Create other-tabs picker container ───────────────────────────────────
    const pickerContainer = document.createElement('div');
    pickerContainer.id = 'bld-tabs-picker';
    pickerContainer.className = 'tabs-picker hidden';
    // Insert after the error/warning elements — append right after errorEl
    errorEl.insertAdjacentElement('afterend', pickerContainer);

    refreshBtn.addEventListener('click', () => runDetection());

    // ── Core detection logic ─────────────────────────────────────────────────
    async function runDetection() {
        setLoading(true);
        try {
            /** @type {DetectionResult} */
            const result = await chrome.runtime.sendMessage({ action: 'GET_SHEET_ID_FROM_ACTIVE_TAB' });
            applyResult(result || { active: null, otherTabs: [] });
        } catch {
            applyResult({ active: null, otherTabs: [] });
        } finally {
            setLoading(false);
        }
    }

    /**
     * Apply detected result to UI.
     * - active → auto-fill input, call onDetected
     * - no active + otherTabs → show picker
     * - nothing → show no-tabs hint
     */
    function applyResult({ active, otherTabs }) {
        hidePicker();

        if (active) {
            onDetected(active.tabUrl);
            return;
        }

        if (!otherTabs || otherTabs.length === 0) {
            // No Sheets tabs open — leave input empty, let existing warning show
            return;
        }

        if (otherTabs.length === 1) {
            // Single other tab — auto-fill silently
            onDetected(otherTabs[0].tabUrl);
            return;
        }

        // Multiple tabs — show picker
        showPicker(otherTabs);
    }

    function showPicker(tabs) {
        pickerContainer.innerHTML = '';
        const label = document.createElement('span');
        label.className = 'tabs-picker-label';
        const msg = t('detectedNTabs', [String(tabs.length)])
            || `Detected ${tabs.length} Sheets tabs`;
        label.textContent = msg;
        pickerContainer.appendChild(label);

        tabs.forEach(tab => {
            const btn = document.createElement('button');
            btn.type = 'button';
            btn.className = 'tabs-picker-item';
            btn.textContent = tabLabel(tab);
            btn.title = tab.tabUrl;
            btn.addEventListener('click', () => {
                onDetected(tab.tabUrl);
                hidePicker();
            });
            pickerContainer.appendChild(btn);
        });

        pickerContainer.classList.remove('hidden');
    }

    function hidePicker() {
        pickerContainer.classList.add('hidden');
        pickerContainer.innerHTML = '';
    }

    function setLoading(loading) {
        refreshBtn.disabled = loading;
        refreshBtn.classList.toggle('loading', loading);
        refreshBtn.textContent = loading ? '…' : '↻';
    }

    return { runDetection };
}

/**
 * Wrap the input element in a relative-positioned div and append the
 * refresh button as an absolute-positioned overlay on the right side.
 * Avoids restructuring existing HTML — just wraps in-place.
 */
function _wrapInputWithRefreshBtn(inputEl, btnEl) {
    const wrapper = document.createElement('div');
    wrapper.className = 'input-with-refresh';
    inputEl.parentNode.insertBefore(wrapper, inputEl);
    wrapper.appendChild(inputEl);
    wrapper.appendChild(btnEl);
}
