import { t } from "../i18n-loader.js";
import { securityRequest } from "../security/page-security.js";

export function createPopupHome({ showSection, genPasswordInput, genProfileLabel, genHint, isHome }) {
    let detectionVersion = 0;
    const cancelDetection = () => { detectionVersion++; };
    const homeHint = document.getElementById("home-hint");
    const homeNotice = document.getElementById("home-notice");
    const homeNoticeText = document.getElementById("home-notice-text");
    const homeContext = document.getElementById("home-sheet-context");
    // ── Quick Start panel — shown once per device for first-time users ─────────
    async function initQuickStart() {
        const panel    = document.getElementById('quick-start-panel');
        const buildBtn = document.getElementById('btn-build-recipe');
        if (!panel || !buildBtn) return;

        let seen = false;
        try {
            const result = await chrome.storage.local.get('hasSeenQuickStart');
            seen = !!result.hasSeenQuickStart;
        } catch {
            // Storage unavailable (e.g. incognito restricted) — show panel anyway
        }
        if (seen) return;

        // Show panel + update CTA text
        panel.classList.remove('hidden');
        buildBtn.textContent = t('btnCreateFirstRecipe') || 'Create First Recipe';

        async function dismiss() {
            try { await chrome.storage.local.set({ hasSeenQuickStart: true }); } catch {}
            panel.classList.add('hidden');
            buildBtn.textContent = t('btnBuildRecipe') || 'Build Recipe';
        }

        document.getElementById('btn-qs-close')?.addEventListener('click', dismiss);
        document.getElementById('btn-qs-got-it')?.addEventListener('click', dismiss);
        document.getElementById('btn-qs-learn-more')?.addEventListener('click', async () => {
            await dismiss();
            chrome.tabs.create({ url: chrome.runtime.getURL('demo/demo.html') });
        });
    }

    const SHEET_ID_REGEX = /\/spreadsheets\/d\/([a-zA-Z0-9_-]+)/;

    // ── tryAutoDetect — called on load and from Back button (Phase 4) ──────────
    // Exposed in outer scope so Back handler can re-call it after returning Home.
    async function tryAutoDetect(tab) {
        const version = ++detectionVersion;
        const active = () => version === detectionVersion && isHome();
        if (!active()) return;
        const shareBtn = document.getElementById('btn-share-sheet');
        // Reset notice state each call
        homeNotice.classList.add('hidden');
        homeContext.classList.add('hidden');
        homeNoticeText.textContent = '';
        homeHint.textContent = t('hintClickCell') || 'Click any cell in Google Sheets containing a recipe.';

        if (!tab?.url?.includes('docs.google.com/spreadsheets')) {
            // Not on Sheets tab — hide share button, generic hint
            shareBtn?.classList.add('hidden');
            return;
        }

        // Show share button with the detected sheet ID
        const sheetMatch = tab.url.match(SHEET_ID_REGEX);
        if (sheetMatch && shareBtn) {
            shareBtn.dataset.sheetId = sheetMatch[1];
            shareBtn.classList.remove('hidden');
        }

        // Show sheet name badge
        const sheetName = (tab.title || '').replace(/ [-–] Google Sheets$/i, '').trim();
        if (sheetName) {
            const badgeMsg = t('hintSheetContext', [sheetName]);
            homeContext.textContent = badgeMsg || `📄 ${sheetName}`;
            homeContext.classList.remove('hidden');
        }

        try {
            const response = await chrome.tabs.sendMessage(tab.id, { action: 'GET_CURRENT_CELL_PASSWORD' });

            if (!active()) return;
            if (response?.success) {
                // SUCCESS ONLY → navigate to generated screen
                if (!(await securityRequest('AUTH_STATUS')).unlocked || !active()) return;
                genPasswordInput.value = response.password;
                if (response.profileName) {
                    const prefix = response.isShared ? '📥 ' : '';
                    genProfileLabel.textContent = `${t('lblProfile') || 'Profile'}: ${prefix}${response.profileName}`;
                    genProfileLabel.classList.remove('hidden');
                } else {
                    genProfileLabel.classList.add('hidden');
                }
                if (response.settings?.pepperingHint) {
                    genHint.textContent = t('hintPepperReminder') || "🔑 Don't forget your pepper!";
                } else {
                    genHint.textContent = '';
                }
                if (!(await securityRequest('AUTH_STATUS')).unlocked || !active()) return;
                showSection('status-generated');
                return;
            }

            // Any error → stay on Home, show amber notice (notice takes priority — hide generic hint)
            let msg;
            if (response?.error === 'Empty cell') {
                msg = t('hintEmptyCell') || 'Selected cell is empty or has no recipe.';
            } else if (response?.code === 'RECIPE_MISMATCH') {
                msg = t('errRecipeMismatch') || 'Recipe mismatch — wrong sheet or profile?';
            } else if (response?.error === 'Invalid recipe format') {
                msg = t('hintInvalidRecipe') || 'Invalid recipe — not a recognized recipe format.';
            } else if (response?.error?.includes('not found')) {
                msg = t('hintSecretNotFound') || 'Secret not configured — check Settings.';
            } else {
                msg = t('hintCellError') || 'Could not generate password from this cell.';
            }
            homeNoticeText.textContent = msg;
            homeNotice.classList.remove('hidden');
            homeHint.textContent = '';

        } catch (e) {
            if (!active()) return;
            const isConnErr = e.message?.includes('Extension context invalidated')
                || e.message?.includes('Could not establish connection')
                || e.message?.includes('Receiving end does not exist');
            homeNoticeText.textContent = isConnErr
                ? (t('hintNeedsReload') || '⚠️ Reload the tab to activate the extension.')
                : (t('hintCellError') || 'Could not cook this recipe.');
            homeNotice.classList.remove('hidden');
            homeHint.textContent = '';
        }
    }

    return { initQuickStart, tryAutoDetect, cancelDetection };
}
