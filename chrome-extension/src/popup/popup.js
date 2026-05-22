import { Argon2Adapter } from "../adapters/infrastructure/argon2_adapter.js";
import { initRecipeBuilder } from "./popup-recipe-builder.js";

function localizeHtml() {
    document.querySelectorAll('[data-i18n]').forEach(el => {
        const msg = chrome.i18n.getMessage(el.dataset.i18n);
        if (msg) el.textContent = msg;
    });
    // data-i18n-title="<key>" → store i18n message in `data-tooltip-html` + wire up custom rich tooltip
    document.querySelectorAll('[data-i18n-title]').forEach(el => {
        const msg = chrome.i18n.getMessage(el.dataset.i18nTitle);
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
    const profilesSummary = document.getElementById('profiles-summary');

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
        globalStatusText.textContent = chrome.i18n.getMessage(cfg.i18nKey) || cfg.i18nKey;
        globalStatusDot.className = 'dot ' + cfg.dot;
        globalStatusEl.classList.toggle('active', cfg.active);
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

            // Show profiles summary
            try {
                const all = await chrome.storage.sync.get(null);
                const ownCount = Object.keys(all).filter(k => k.startsWith("profile:")).length;
                const sharedCount = Object.keys(all).filter(k => k.startsWith("shared:")).length;
                if (ownCount > 0 || sharedCount > 0) {
                    profilesSummary.textContent = `Profiles: ${ownCount} own · ${sharedCount} shared`;
                    profilesSummary.classList.remove('hidden');
                    profilesSummary.style.cursor = 'pointer';
                    profilesSummary.onclick = () => chrome.runtime.openOptionsPage();
                }
            } catch {}

            // Try auto-generate from active cell
            const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
            if (tab?.url?.includes("docs.google.com/spreadsheets")) {
                try {
                    const response = await chrome.tabs.sendMessage(tab.id, { action: "GET_CURRENT_CELL_PASSWORD" });
                    if (response?.success) {
                        showSection('status-generated');
                        genPasswordInput.value = response.password;
                        // Profile indicator
                        if (response.profileName) {
                            const prefix = response.isShared ? '📥 ' : '';
                            genProfileLabel.textContent = `Profile: ${prefix}${response.profileName}`;
                            genProfileLabel.classList.remove('hidden');
                        }
                        if (response.settings?.pepperingHint) {
                            genHint.textContent = "🔑 Don't forget your pepper!";
                            genHint.style.color = "";
                        }
                    } else if (response?.error) {
                        showSection('status-generated');
                        genPasswordInput.value = "";
                        if (response.error === "Empty cell") {
                            genHint.textContent = "No recipe found — select a cell with a recipe, then re-open.";
                        } else {
                            const debugText = response.extractedText ? ` ("${response.extractedText}")` : "";
                            genHint.textContent = `Error: ${response.error}${debugText}`;
                        }
                        genHint.style.color = "#da3633";
                    }
                } catch (e) {
                    // Any connection failure (content script not loaded, extension reloaded, etc.)
                    showSection('status-generated');
                    genPasswordInput.value = "";
                    const needsReload = e.message?.includes("Extension context invalidated") ||
                                        e.message?.includes("Could not establish connection") ||
                                        e.message?.includes("Receiving end does not exist");
                    genHint.textContent = needsReload
                        ? "⚠️ Reload the tab to activate the extension."
                        : `⚠️ ${e.message}`;
                    genHint.style.color = "#da3633";
                }
            }
        }
    }

    // Handlers
    const openOptions = () => chrome.runtime.openOptionsPage();

    document.getElementById('btn-start-setup')?.addEventListener('click', openOptions);
    document.getElementById('btn-settings').addEventListener('click', openOptions);

    // Open recipe builder from unlocked state
    document.getElementById('btn-build-recipe')?.addEventListener('click', async () => {
        await builder.loadProfiles();
        builder.reset();
        showSection('status-builder');
    });

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
        btn.textContent = "Copied!";
        setTimeout(() => btn.textContent = "Copy", 1500);
    });

    // Back (from generated → unlocked)
    document.getElementById('btn-back').addEventListener('click', () => showSection('status-unlocked'));
});
