import { bindPasswordChange } from "./options-password-change.js";
import { bindUnlock, securityRequest, securityMessage } from "../security/page-security.js";
import { initProfilesTab } from "./options-profiles-tab.js";
import { openExportWizard } from "./options-export-wizard.js";
import { openImportWizard } from "./options-import-wizard.js";
import { initSettingsTab } from "./options-settings-tab.js";
import { loadLocale, t } from "../i18n-loader.js";

let sessionKey = null;

// ── DOM refs ──
const setupSection = document.getElementById("setup-section");
const unlockSection = document.getElementById("unlock-section");
const dashboardSection = document.getElementById("dashboard-section");

// ── Localization ──
function localizeHtml() {
    document.querySelectorAll('[data-i18n]').forEach(el => {
        const msg = t(el.dataset.i18n);
        if (msg) el.textContent = msg;
    });
    document.querySelectorAll('[data-i18n-placeholder]').forEach(el => {
        const msg = t(el.dataset.i18nPlaceholder);
        if (msg) el.placeholder = msg;
    });
}

// ── Section visibility ──
function showSection(section) {
    [setupSection, unlockSection, dashboardSection].forEach(s => s.classList.add("hidden"));
    const tabBar = document.getElementById("tab-bar");
    tabBar.classList.toggle("hidden", section !== dashboardSection);
    section.classList.remove("hidden");
}

// ── Toast ──
function showToast(msg, isError = false) {
    const toast = document.getElementById("toast");
    toast.textContent = msg;
    toast.style.borderColor = isError ? "var(--red)" : "var(--green)";
    toast.style.color = isError ? "var(--red)" : "var(--green)";
    toast.classList.remove("hidden");
    setTimeout(() => toast.classList.add("hidden"), 3000);
}

// ── Tab switching ──
function initTabBar() {
    document.querySelectorAll('.tab').forEach(tab => {
        tab.addEventListener('click', () => switchTab(tab.dataset.tab));
    });
}

async function switchTab(name) {
    document.querySelectorAll('.tab').forEach(t => t.classList.toggle('active', t.dataset.tab === name));
    document.querySelectorAll('.tab-content').forEach(c => c.classList.toggle('hidden', c.id !== `tab-${name}`));
    if (name === 'profiles') {
        await initProfilesTab({ sessionKey, onExport: openExportWizard, onImport: openImportWizard, showToast });
    } else if (name === 'settings') {
        await initSettingsTab({ showToast });
    }
}

// ── Pending action handler (set by popup via session storage) ──
async function handlePendingAction() {
    const { pendingAction } = await chrome.storage.session.get('pendingAction');
    if (!pendingAction) return;
    await chrome.storage.session.remove('pendingAction');

    if (pendingAction.type === 'share') {
        const { sheetId } = pendingAction;
        const sync = await chrome.storage.sync.get(['sheetMapping', 'defaultProfile']);
        const profileName = (sync.sheetMapping || {})[sheetId] || sync.defaultProfile || 'Default';
        const profileKey = `profile:${profileName}`;
        openExportWizard(profileName, profileKey, sessionKey, () => {}, sheetId);
    }
}

// ── Status check ──
async function checkStatus() {
    const stored = await chrome.storage.sync.get(["salt", "defaultProfile", "migrationNotified"]);
    const status = await securityRequest("AUTH_STATUS");
    const session = await chrome.storage.session.get("sessionKey");

    if (!stored.salt) {
        showSection(setupSection);
    } else if (status.unlocked && session.sessionKey) {
        sessionKey = session.sessionKey;
        showSection(dashboardSection);
        await switchTab('profiles');
        // Migration notice
        if (stored.migrationNotified === false) {
            showToast(t('migrationNotice') || '✅ Migrated to v2. Secrets are now in profile "Default".');
            await chrome.storage.sync.set({ migrationNotified: true });
        }
        // Pending action from popup (e.g. "Share this sheet")
        await handlePendingAction();
    } else {
        sessionKey = null;
        document.querySelectorAll("input[type=password], .secret-input, #edit-secrets-inputs input").forEach(input => { input.value = ""; });
        document.querySelectorAll(".modal-overlay").forEach(modal => modal.classList.add("hidden"));
        showSection(unlockSection);
    }
}

// ── Event listeners ──
document.addEventListener("DOMContentLoaded", async () => {
    await loadLocale();
    localizeHtml();
    initTabBar();
    await checkStatus();

    let setupBusy = false;
    // Setup
    document.getElementById("setup-form").addEventListener("submit", async (e) => {
        e.preventDefault();
        if (setupBusy) return;
        const pwd = document.getElementById("setup-password").value;
        const confirm = document.getElementById("setup-confirm").value;
        if (pwd !== confirm) { showToast(t("toastPwdMismatch"), true); return; }
        if (pwd.length < 8) { showToast(t("toastPwdShort"), true); return; }
        if (!document.getElementById("setup-backup-confirm").checked) { showToast("Please confirm backup", true); return; }
        setupBusy = true;
        const button = document.querySelector('#setup-form button[type="submit"]'); button.disabled = true;
        try {
            showToast("Setting up…");
            const published = await securityRequest("AUTH_SETUP", { password: pwd });
            if (!published.success) { showToast(securityMessage(published), true); await checkStatus(); return; }
            document.getElementById("setup-form").reset();
            showToast(t("toastSetupComplete"));
            await checkStatus();
        } catch { sessionKey = null; showToast(securityMessage({ code: "CRYPTO_UNAVAILABLE" }), true); }
        finally { setupBusy = false; button.disabled = false; }
    });

    // Both Options and toolbar use the same durable unlock authority.
    bindUnlock({ input: document.getElementById("unlock-password"),
        button: document.querySelector('#unlock-form button[type="submit"]'),
        form: document.getElementById("unlock-form"), showError: message => showToast(message, true),
        clearError: () => {}, onUnlocked: checkStatus });

    // Lock
    document.getElementById("btn-lock").addEventListener("click", async () => {
        sessionKey = null;
        document.querySelectorAll("input[type=password], .secret-input, #edit-secrets-inputs input").forEach(input => { input.value = ""; });
        document.querySelectorAll(".modal-overlay").forEach(modal => modal.classList.add("hidden"));
        showSection(unlockSection);
        const result = await securityRequest("AUTH_LOCK");
        if (result.success) location.reload();
        else showToast(securityMessage({ code: "LOCK_UNAVAILABLE" }), true);
    });

    bindPasswordChange({ showToast, setSessionKey: key => { sessionKey = key; } });

    // Sync across tabs
    chrome.storage.onChanged.addListener((changes, area) => {
        if (area === 'session' && changes.sessionKey) checkStatus();
    });
});
