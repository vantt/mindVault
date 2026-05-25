import { Argon2Adapter } from "../adapters/infrastructure/argon2_adapter.js";
import { initRecipeBuilder } from "./popup-recipe-builder.js";
import { loadLocale, t } from "../i18n-loader.js";

function localizeHtml() {
    document.querySelectorAll('[data-i18n]').forEach(el => {
        const msg = t(el.dataset.i18n);
        if (msg) el.textContent = msg;
    });
    document.querySelectorAll('[data-i18n-placeholder]').forEach(el => {
        const msg = t(el.dataset.i18nPlaceholder);
        if (msg) el.placeholder = msg;
    });
    // data-i18n-title="<key>" → store i18n message in `data-tooltip-html` + wire up custom rich tooltip
    document.querySelectorAll('[data-i18n-title]').forEach(el => {
        const msg = t(el.dataset.i18nTitle);
        if (msg) {
            el.dataset.tooltipHtml = msg;
            attachTooltip(el);
        }
    });
}

// ── Custom HTML tooltip (supports color spans inside hint text) ─────────────
let _tooltipEl = null;
function attachTooltip(el) {
    el.addEventListener('mouseenter', _showTooltip);
    el.addEventListener('mouseleave', _hideTooltip);
}
function _showTooltip(e) {
    if (!_tooltipEl) {
        _tooltipEl = document.createElement('div');
        _tooltipEl.className = 'tooltip-popup';
        document.body.appendChild(_tooltipEl);
    }
    _tooltipEl.innerHTML = e.currentTarget.dataset.tooltipHtml;
    // Position below the target; clamp to viewport horizontally
    const rect = e.currentTarget.getBoundingClientRect();
    _tooltipEl.style.left = '0px';
    _tooltipEl.style.top = (rect.bottom + 6) + 'px';
    _tooltipEl.classList.add('visible');
    // Adjust horizontal: keep within popup width (300px)
    const tipRect = _tooltipEl.getBoundingClientRect();
    const popupWidth = document.body.clientWidth;
    let left = rect.left;
    if (left + tipRect.width > popupWidth - 4) left = popupWidth - tipRect.width - 4;
    if (left < 4) left = 4;
    _tooltipEl.style.left = left + 'px';
}
function _hideTooltip() {
    if (_tooltipEl) _tooltipEl.classList.remove('visible');
}

document.addEventListener('DOMContentLoaded', async () => {
    await loadLocale();
    localizeHtml();

    const argon2 = new Argon2Adapter();

    const statusSetup = document.getElementById('status-setup');
    const statusUnlocked = document.getElementById('status-unlocked');
    const statusLocked = document.getElementById('status-locked');
    const statusGenerated = document.getElementById('status-generated');
    const statusBuilder = document.getElementById('status-builder');
    const genPasswordInput = document.getElementById('gen-password');
    const genHint = document.getElementById('gen-hint');
    const genProfileLabel = document.getElementById('gen-profile-label');

    // ── Home contextual state elements ─────────────────────────────────────────
    const homeHint       = document.getElementById('home-hint');
    const homeNotice     = document.getElementById('home-notice');
    const homeNoticeText = document.getElementById('home-notice-text');
    const homeContext    = document.getElementById('home-sheet-context');

    // ── Global status pill (header) ─────────────────────────────────────────
    // Per-section status indicators are gone; the header pill is the single source of truth.
    const SECTION_STATUS = {
        'status-setup':     { i18nKey: 'statusSetup',    dot: 'red',   active: false },
        'status-locked':    { i18nKey: 'statusLocked',   dot: 'red',   active: false },
        'status-unlocked':  { i18nKey: 'statusUnlocked', dot: 'green', active: true  },
        'status-generated': { i18nKey: 'statusReady',    dot: 'green', active: true  },
        'status-builder':   { i18nKey: 'statusBuilder',  dot: 'green', active: true  },
    };
    const globalStatusEl   = document.getElementById('global-status');
    const globalStatusDot  = document.getElementById('global-status-dot');
    const globalStatusText = document.getElementById('global-status-text');

    const hideAll = () => [statusSetup, statusUnlocked, statusLocked, statusGenerated, statusBuilder].forEach(el => el.classList.add('hidden'));

    /** Show one section by id and sync the header status pill. */
    function showSection(id) {
        hideAll();
        document.getElementById(id).classList.remove('hidden');
        const cfg = SECTION_STATUS[id];
        if (!cfg) return;
        globalStatusText.textContent = t(cfg.i18nKey) || cfg.i18nKey;
        globalStatusDot.className = 'dot ' + cfg.dot;
        globalStatusEl.classList.toggle('active', cfg.active);
    }

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

            if (response?.success) {
                // SUCCESS ONLY → navigate to generated screen
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
            const isConnErr = e.message?.includes('Extension context invalidated')
                || e.message?.includes('Could not establish connection')
                || e.message?.includes('Receiving end does not exist');
            homeNoticeText.textContent = isConnErr
                ? (t('hintNeedsReload') || '⚠️ Reload the tab to activate the extension.')
                : `⚠️ ${e.message}`;
            homeNotice.classList.remove('hidden');
            homeHint.textContent = '';
        }
    }

    // Recipe builder controller — wired regardless of auth state (handlers safely ignored if elements absent)
    const builder = initRecipeBuilder({
        onBack: () => showSection('status-unlocked'),
    });

    const { salt } = await chrome.storage.sync.get("salt");

    if (!salt) {
        showSection('status-setup');
    } else {
        const { sessionKey } = await chrome.storage.session.get("sessionKey");

        if (!sessionKey) {
            showSection('status-locked');
        } else {
            showSection('status-unlocked');

            // Embed profile count into "Manage profiles" button label
            try {
                const all = await chrome.storage.sync.get(null);
                const ownCount = Object.keys(all).filter(k => k.startsWith("profile:")).length;
                const sharedCount = Object.keys(all).filter(k => k.startsWith("shared:")).length;
                const total = ownCount + sharedCount;
                if (total > 0) {
                    const manageBtn = document.getElementById('btn-manage-profiles');
                    if (manageBtn) {
                        const label = t('btnManageProfiles') || 'Manage profiles';
                        manageBtn.textContent = `${label} (${total})`;
                    }
                }
            } catch {}

            // Show Quick Start panel for first-time users (before auto-detect so panel is
            // visible on Home; if tryAutoDetect routes to Generated, panel is never seen anyway)
            await initQuickStart();

            // Try auto-generate from active cell (success → status-generated; errors stay on Home)
            const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
            await tryAutoDetect(tab);
        }
    }

    // Handlers
    const openOptions = () => chrome.runtime.openOptionsPage();

    document.getElementById('btn-start-setup')?.addEventListener('click', openOptions);
    document.getElementById('btn-settings').addEventListener('click', openOptions);
    document.getElementById('btn-manage-profiles')?.addEventListener('click', openOptions);

    // Share sheet — store pending action then open Options (which reads it after unlock)
    document.getElementById('btn-share-sheet')?.addEventListener('click', async () => {
        const sheetId = document.getElementById('btn-share-sheet').dataset.sheetId;
        if (!sheetId) return;
        await chrome.storage.session.set({ pendingAction: { type: 'share', sheetId } });
        chrome.runtime.openOptionsPage();
    });

    // Open recipe builder — reachable from both unlocked state and the auto-generated
    // state (which is what popup lands on when opened from a Sheets tab). Wiring both
    // buttons to the same handler keeps the button accessible regardless of flow.
    //
    // Order matters: reset() FIRST to clear form fields, then loadProfiles() which
    // runs sheet auto-detection and populates the URL field. Inverted order would
    // wipe the just-detected URL.
    const openBuilder = async () => {
        builder.reset();
        await builder.loadProfiles();
        showSection('status-builder');
    };
    document.getElementById('btn-build-recipe')?.addEventListener('click', openBuilder);
    document.getElementById('btn-build-recipe-gen')?.addEventListener('click', openBuilder);

    // Unlock
    const unlockInput = document.getElementById('unlock-password');
    const unlockError = document.getElementById('unlock-error');
    const btnUnlock = document.getElementById('btn-unlock');

    const handleUnlock = async () => {
        const password = unlockInput.value;
        if (!password) return;
        try {
            btnUnlock.textContent = "Unlocking...";
            unlockError.style.display = 'none';
            const { salt, defaultProfile } = await chrome.storage.sync.get(["salt", "defaultProfile"]);
            if (!salt) { unlockError.textContent = "Missing data. Reset in Options."; unlockError.style.display = 'block'; btnUnlock.textContent = "Unlock"; return; }

            const derivedKey = await argon2.deriveKey(password, new Uint8Array(salt));
            const profileKey = `profile:${defaultProfile || "Default"}`;
            const { [profileKey]: profileData, encryptedData, iv } = await chrome.storage.sync.get([profileKey, "encryptedData", "iv"]);
            const verifyData = profileData || { encryptedData, iv };
            if (!verifyData?.encryptedData) { unlockError.textContent = "No data found. Reset in Options."; unlockError.style.display = 'block'; btnUnlock.textContent = "Unlock"; return; }

            const key = await crypto.subtle.importKey("jwk", derivedKey, { name: "AES-GCM" }, false, ["decrypt"]);
            await crypto.subtle.decrypt({ name: "AES-GCM", iv: new Uint8Array(verifyData.iv) }, key, new Uint8Array(verifyData.encryptedData));
            await chrome.storage.session.set({ sessionKey: derivedKey });
            window.location.reload();
        } catch (e) {
            // DOMException from Web Crypto sets e.name="OperationError" but e.message="" (empty in Chrome)
        const isWrongPassword = e?.name === "OperationError" || e?.message?.includes("OperationError");
        unlockError.textContent = isWrongPassword
            ? "Invalid Password"
            : (e?.message || e?.name || "An error occurred");
            unlockError.style.display = 'block';
            btnUnlock.textContent = "Unlock";
        }
    };

    btnUnlock?.addEventListener('click', handleUnlock);
    unlockInput?.addEventListener('keydown', e => { if (e.key === 'Enter') handleUnlock(); });

    document.getElementById('btn-lock')?.addEventListener('click', async () => { await chrome.storage.session.remove("sessionKey"); window.close(); });
    document.getElementById('btn-lock-gen')?.addEventListener('click', async () => { await chrome.storage.session.remove("sessionKey"); window.close(); });

    // Copy
    document.getElementById('btn-copy').addEventListener('click', () => {
        const password = genPasswordInput.value;
        if (!password) return;
        navigator.clipboard.writeText(password);
        const btn = document.getElementById('btn-copy');
        btn.textContent = t('lblCopied') || "Copied!";
        setTimeout(() => { btn.textContent = t('btnCopy') || "Copy"; }, 1500);
    });

    // Back (from generated → unlocked) — re-run detection so Home reflects current tab state
    document.getElementById('btn-back').addEventListener('click', async () => {
        showSection('status-unlocked');
        // Re-query active tab — user may have switched tabs while popup was open
        const [activeTab] = await chrome.tabs.query({ active: true, currentWindow: true });
        await tryAutoDetect(activeTab);
    });
});
