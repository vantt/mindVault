import { Argon2Adapter } from "../adapters/infrastructure/argon2_adapter.js";
import { encryptWithKey, decryptWithKey } from "../adapters/infrastructure/aes-storage-crypto-helper.js";
import { initProfilesTab } from "./options-profiles-tab.js";
import { openExportWizard } from "./options-export-wizard.js";
import { openImportWizard } from "./options-import-wizard.js";
import { initSettingsTab } from "./options-settings-tab.js";

const argon2 = new Argon2Adapter();
let sessionKey = null;

// ── DOM refs ──
const setupSection = document.getElementById("setup-section");
const unlockSection = document.getElementById("unlock-section");
const dashboardSection = document.getElementById("dashboard-section");

// ── Localization ──
function localizeHtml() {
    document.querySelectorAll('[data-i18n]').forEach(el => {
        const msg = chrome.i18n.getMessage(el.dataset.i18n);
        if (msg) el.textContent = msg;
    });
    document.querySelectorAll('[data-i18n-placeholder]').forEach(el => {
        const msg = chrome.i18n.getMessage(el.dataset.i18nPlaceholder);
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

// ── Status check ──
async function checkStatus() {
    const stored = await chrome.storage.sync.get(["salt", "defaultProfile", "migrationNotified"]);
    const session = await chrome.storage.session.get("sessionKey");

    if (!stored.salt) {
        showSection(setupSection);
    } else if (session.sessionKey) {
        sessionKey = session.sessionKey;
        showSection(dashboardSection);
        await switchTab('profiles');
        // Migration notice
        if (stored.migrationNotified === false) {
            showToast(chrome.i18n.getMessage('migrationNotice') || '✅ Migrated to v2. Secrets are now in profile "Default".');
            await chrome.storage.sync.set({ migrationNotified: true });
        }
    } else {
        showSection(unlockSection);
    }
}

// ── Event listeners ──
document.addEventListener("DOMContentLoaded", async () => {
    localizeHtml();
    initTabBar();
    await checkStatus();

    // Setup
    document.getElementById("setup-form").addEventListener("submit", async (e) => {
        e.preventDefault();
        const pwd = document.getElementById("setup-password").value;
        const confirm = document.getElementById("setup-confirm").value;
        if (pwd !== confirm) { showToast(chrome.i18n.getMessage("toastPwdMismatch"), true); return; }
        if (pwd.length < 8) { showToast(chrome.i18n.getMessage("toastPwdShort"), true); return; }
        if (!document.getElementById("setup-backup-confirm").checked) { showToast("Please confirm backup", true); return; }
        try {
            showToast("Setting up…");
            const salt = crypto.getRandomValues(new Uint8Array(16));
            sessionKey = await argon2.deriveKey(pwd, salt);
            const initial = { secrets: { "1": { base: "" }, "2": { base: "" }, "3": { base: "" }, "4": { base: "" }, "5": { base: "" } }, settings: {} };
            const encrypted = await encryptWithKey(initial, sessionKey);
            await chrome.storage.sync.set({
                salt: Array.from(salt),
                "profile:Default": encrypted,
                defaultProfile: "Default",
                sheetMapping: {}
            });
            await chrome.storage.session.set({ sessionKey });
            showToast(chrome.i18n.getMessage("toastSetupComplete"));
            showSection(dashboardSection);
            await switchTab('profiles');
        } catch (err) { showToast("Setup failed: " + err.message, true); }
    });

    // Unlock
    document.getElementById("unlock-form").addEventListener("submit", async (e) => {
        e.preventDefault();
        const pwd = document.getElementById("unlock-password").value;
        try {
            showToast("Unlocking…");
            const stored = await chrome.storage.sync.get(["salt", "defaultProfile", "encryptedData", "iv"]);
            if (!stored.salt) { showToast("Corrupted data.", true); return; }
            const derivedKey = await argon2.deriveKey(pwd, new Uint8Array(stored.salt));
            const defaultName = stored.defaultProfile || "Default";
            const profileKey = `profile:${defaultName}`;
            const { [profileKey]: profileData } = await chrome.storage.sync.get(profileKey);
            const verifyData = profileData || { encryptedData: stored.encryptedData, iv: stored.iv };
            if (!verifyData?.encryptedData) { showToast("No data found. Please reset.", true); return; }
            await decryptWithKey(verifyData.encryptedData, verifyData.iv, derivedKey); // throws if wrong pwd
            sessionKey = derivedKey;
            await chrome.storage.session.set({ sessionKey });
            showToast(chrome.i18n.getMessage("toastUnlockSuccess"));
            showSection(dashboardSection);
            await switchTab('profiles');
        } catch (err) {
            showToast(err.message.includes("OperationError") ? "Invalid Password" : err.message, true);
        }
    });

    // Lock
    document.getElementById("btn-lock").addEventListener("click", async () => {
        await chrome.storage.session.remove("sessionKey");
        sessionKey = null;
        location.reload();
    });

    // Change password (Settings tab)
    document.getElementById("change-password-form").addEventListener("submit", async (e) => {
        e.preventDefault();
        const oldPwd = document.getElementById("old-password").value;
        const newPwd = document.getElementById("new-password").value;
        const confirmPwd = document.getElementById("confirm-new-password").value;
        if (newPwd !== confirmPwd) { showToast(chrome.i18n.getMessage("toastPwdMismatch"), true); return; }
        if (newPwd.length < 8) { showToast(chrome.i18n.getMessage("toastPwdShort"), true); return; }
        if (!document.getElementById("change-backup-confirm").checked) { showToast("Please confirm backup", true); return; }
        try {
            showToast("Re-encrypting…");
            const { salt, defaultProfile } = await chrome.storage.sync.get(["salt", "defaultProfile"]);
            const oldDerivedKey = await argon2.deriveKey(oldPwd, new Uint8Array(salt));
            // Verify old password by decrypting default profile with the derived key
            const verifyKey = `profile:${defaultProfile || "Default"}`;
            const { [verifyKey]: verifyData } = await chrome.storage.sync.get(verifyKey);
            if (verifyData?.encryptedData) {
                await decryptWithKey(verifyData.encryptedData, verifyData.iv, oldDerivedKey); // throws OperationError if wrong
            }
            const newSalt = crypto.getRandomValues(new Uint8Array(16));
            const newKey = await argon2.deriveKey(newPwd, newSalt);
            // Re-encrypt ALL profiles
            const all = await chrome.storage.sync.get(null);
            const updates = { salt: Array.from(newSalt) };
            for (const [k, v] of Object.entries(all)) {
                if ((k.startsWith("profile:") || k.startsWith("shared:")) && v?.encryptedData) {
                    const decrypted = await decryptWithKey(v.encryptedData, v.iv, oldDerivedKey);
                    const reEncrypted = await encryptWithKey(decrypted, newKey);
                    updates[k] = k.startsWith("shared:") ? { ...v, ...reEncrypted } : reEncrypted;
                }
            }
            await chrome.storage.sync.set(updates);
            sessionKey = newKey;
            await chrome.storage.session.set({ sessionKey });
            showToast(chrome.i18n.getMessage("toastPwdUpdated"));
            document.getElementById("change-password-form").reset();
            document.getElementById("change-password-form").classList.add("hidden");
            document.getElementById("pwd-summary").classList.remove("hidden");
        } catch (err) {
            let msg = err.message;
            if (msg.includes("OperationError")) msg = "Incorrect current password";
            else if (msg.includes("QUOTA_BYTES")) msg = "Storage quota exceeded. Delete unused profiles first.";
            showToast(msg, true);
        }
    });

    // Sync across tabs
    chrome.storage.onChanged.addListener((changes, area) => {
        if (area === 'session' && changes.sessionKey) checkStatus();
    });
});
