
import { Argon2Adapter } from "../adapters/infrastructure/argon2_adapter.js";

// DOM Elements
const setupSection = document.getElementById("setup-section");
const unlockSection = document.getElementById("unlock-section");
const dashboardSection = document.getElementById("dashboard-section");

const setupForm = document.getElementById("setup-form");
const unlockForm = document.getElementById("unlock-form");
const secretsForm = document.getElementById("secrets-form");
const toast = document.getElementById("toast");

// Localization Helper
function localizeHtml() {
    const objects = document.querySelectorAll('[data-i18n]');
    for (const obj of objects) {
        const msg = obj.getAttribute('data-i18n');
        obj.textContent = chrome.i18n.getMessage(msg);
    }
    
    const placeholders = document.querySelectorAll('[data-i18n-placeholder]');
    for (const obj of placeholders) {
        const msg = obj.getAttribute('data-i18n-placeholder');
        obj.placeholder = chrome.i18n.getMessage(msg);
    }
}

// Services
const argon2 = new Argon2Adapter(); // This will use the updated import path

// State
let sessionKey = null; // JWK

// Initialization
document.addEventListener("DOMContentLoaded", async () => {
    localizeHtml();
    await checkStatus();
    setupEventListeners();
});

async function checkStatus() {
    // Check if configuration exists
    const stored = await chrome.storage.sync.get(["salt", "encryptedData"]);
    
    // Check if session is active
    const session = await chrome.storage.session.get("sessionKey");
    
    if (!stored.salt) {
        // No config -> Show Setup
        showSection(setupSection);
    } else if (session.sessionKey) {
        // Config + Session -> Show Dashboard
        sessionKey = session.sessionKey;
        await loadSecrets();
        showSection(dashboardSection);
    } else {
        // Config + No Session -> Show Unlock
        showSection(unlockSection);
    }
}

function showSection(section) {
    const changePasswordSection = document.getElementById("change-password-section");
    [setupSection, unlockSection, dashboardSection, changePasswordSection].forEach(s => s.classList.add("hidden"));
    section.classList.remove("hidden");
}

function setupEventListeners() {
    // Setup Flow
    setupForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        const pwd = document.getElementById("setup-password").value;
        const confirm = document.getElementById("setup-confirm").value;
        
        if (pwd !== confirm) {
            showToast(chrome.i18n.getMessage("toastPwdMismatch"), true);
            return;
        }
        
        if (pwd.length < 8) {
            showToast(chrome.i18n.getMessage("toastPwdShort"), true);
            return;
        }

        const backupConfirm = document.getElementById("setup-backup-confirm");
        if (!backupConfirm.checked) {
            showToast("Please confirm you have backed up your password.", true);
            return;
        }

        try {
            showToast("Setting up encryption...", false);
            
            // 1. Generate Salt
            const salt = crypto.getRandomValues(new Uint8Array(16));
            
            // 2. Derive Key
            // This takes time (~2s)
            sessionKey = await argon2.deriveKey(pwd, salt);
            
            // 3. Encrypt Empty Secrets
            const initialSecrets = {
                secrets: {
                    "1": { base: "" },
                    "2": { base: "" },
                    "3": { base: "" },
                    "4": { base: "" },
                    "5": { base: "" }
                }
            };
            
            await saveEncrypted(initialSecrets, sessionKey, salt);
            await chrome.storage.session.set({ sessionKey });
            
            showToast(chrome.i18n.getMessage("toastSetupComplete"));
            await loadSecrets();
            showSection(dashboardSection);
            
        } catch (err) {
            console.error(err);
            showToast("Setup failed: " + err.message, true);
        }
    });

    // Unlock Flow
    unlockForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        const pwd = document.getElementById("unlock-password").value;
        
        try {
            showToast("Unlocking...", false);
            const { salt, encryptedData, iv } = await chrome.storage.sync.get(["salt", "encryptedData", "iv"]);
            
            if (!salt) {
                showToast("Corrupted data. Reset required.", true);
                return;
            }
            
            const saltArray = new Uint8Array(salt); // Convert back from number array if needed, but storage usually handles basic types.
            // Wait, chrome.storage serializes. Uint8Array might become Object or Array.
            // chrome.storage supports Arrays. Let's assume Array.
            // If it stored as Object, we fix.
            // `crypto.getRandomValues` returns Uint8Array. 
            // chrome.storage saving Uint8Array? It might convert to array of numbers.
            // Ideally we store as Array for JSON compatibility or Base64.
            // Let's ensure we handle type conversion carefully.
            
            // Re-derive key
            const derivedKey = await argon2.deriveKey(pwd, saltArray);
            
            // Verify by attempting decrypt
            try {
                // Import derived key for decryption
                const key = await crypto.subtle.importKey(
                    "jwk",
                    derivedKey,
                    { name: "AES-GCM" },
                    false,
                    ["decrypt"]
                );

                const ivArray = new Uint8Array(iv);
                const dataArray = new Uint8Array(encryptedData);

                await crypto.subtle.decrypt(
                    { name: "AES-GCM", iv: ivArray },
                    key,
                    dataArray
                );
                
                // If successful
                sessionKey = derivedKey;
                await chrome.storage.session.set({ sessionKey });
                
                showToast(chrome.i18n.getMessage("toastUnlockSuccess"));
                await loadSecrets();
                showSection(dashboardSection);
                
            } catch (decError) {
                console.error(decError);
                showToast("Invalid Password", true);
            }
            
        } catch (err) {
            console.error(err);
            showToast("Unlock failed: " + err.message, true);
        }
    });

    // Save Secrets
    secretsForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        
        const secrets = { secrets: {}, settings: {} };
        document.querySelectorAll(".secret-input").forEach(input => {
            const index = input.dataset.index;
            secrets.secrets[index] = { base: input.value };
        });
        
        // Save Settings
        secrets.settings.pepperingHint = document.getElementById("setting-peppering-hint").checked;
        
        try {
            // Retrieve salt to persist it (or just update encryptedData/iv)
            const { salt } = await chrome.storage.sync.get("salt");
             // Convert back if needed.
             // Helper: when saving, we should convert TypedArrays to regular Arrays for storage compatibility if raw TypedArrays cause issues?
             // Actually standard chrome.storage handles it fine usually, but let's be safe.
             
            await saveEncrypted(secrets, sessionKey, new Uint8Array(salt));
            showToast(chrome.i18n.getMessage("toastSaveSuccess"));
        } catch (err) {
            console.error(err);
            showToast("Save failed", true);
        }
    });

    // Lock
    document.getElementById("btn-lock").addEventListener("click", async () => {
        await chrome.storage.session.remove("sessionKey");
        sessionKey = null;
        location.reload();
    });

    // Toggle Visibility
    document.querySelectorAll(".btn-toggle-visibility").forEach(btn => {
        btn.addEventListener("click", (e) => {
            const input = e.target.previousElementSibling;
            if (input.type === "password") {
                input.type = "text";
                e.target.textContent = "🙈";
            } else {
                input.type = "password";
                e.target.textContent = "👁";
            }
        });
    });

    // Change Password UI Toggle
    document.getElementById("btn-change-pwd").addEventListener("click", () => {
        showSection(document.getElementById("change-password-section"));
    });

    document.getElementById("btn-cancel-change").addEventListener("click", () => {
        showSection(dashboardSection);
        document.getElementById("change-password-form").reset();
    });

    // Change Password Logic
    document.getElementById("change-password-form").addEventListener("submit", async (e) => {
        e.preventDefault();
        
        const oldPwd = document.getElementById("old-password").value;
        const newPwd = document.getElementById("new-password").value;
        const confirmPwd = document.getElementById("confirm-new-password").value;
        
        if (newPwd !== confirmPwd) {
            showToast(chrome.i18n.getMessage("toastPwdMismatch"), true);
            return;
        }
        if (newPwd.length < 8) {
             showToast(chrome.i18n.getMessage("toastPwdShort"), true);
             return;
        }
        
        const backupConfirm = document.getElementById("change-backup-confirm");
        if (!backupConfirm.checked) {
             showToast("Please confirm backup", true);
             return;
        }

        try {
            showToast("Verifying and re-encrypting...", false);

            // 1. Verify OLD password by deriving key and attempting decrypt
            // We need the CURRENT salt.
            const { salt, encryptedData, iv } = await chrome.storage.sync.get(["salt", "encryptedData", "iv"]);
            const saltArray = new Uint8Array(salt);
            
            const oldKeyJWK = await argon2.deriveKey(oldPwd, saltArray);
            
            // Try decrypting current data to verify old password AND get current secrets
            let currentSecrets;
            try {
                const key = await crypto.subtle.importKey(
                    "jwk",
                    oldKeyJWK,
                    { name: "AES-GCM" },
                    false,
                    ["decrypt"]
                );

                const dataArray = new Uint8Array(encryptedData);
                const ivArray = new Uint8Array(iv);
                
                const decryptedBuffer = await crypto.subtle.decrypt(
                    { name: "AES-GCM", iv: ivArray },
                    key,
                    dataArray
                );
                
                const text = new TextDecoder().decode(decryptedBuffer);
                currentSecrets = JSON.parse(text);
                
            } catch (decError) {
                console.error(decError);
                showToast("Incorrect Current Password", true);
                return;
            }

            // 2. Generate NEW Salt and Key
            const newSalt = crypto.getRandomValues(new Uint8Array(16));
            const newKeyJWK = await argon2.deriveKey(newPwd, newSalt);

            // 3. Encrypt Secrets with NEW Key
            await saveEncrypted(currentSecrets, newKeyJWK, newSalt);
            
            // 4. Update Session
            sessionKey = newKeyJWK;
            await chrome.storage.session.set({ sessionKey });
            
            showToast(chrome.i18n.getMessage("toastPwdUpdated"));
            document.getElementById("change-password-form").reset();
            showSection(dashboardSection);

        } catch (err) {
            console.error(err);
            showToast("Update failed: " + err.message, true);
        }
    });

}

// Helpers

async function loadSecrets() {
    try {
        const { encryptedData, iv } = await chrome.storage.sync.get(["encryptedData", "iv"]);
        
        const key = await crypto.subtle.importKey(
            "jwk",
            sessionKey,
            { name: "AES-GCM" },
            false,
            ["decrypt"]
        );

        const ivArray = new Uint8Array(iv);
        const dataArray = new Uint8Array(encryptedData);

        const decrypted = await crypto.subtle.decrypt(
            { name: "AES-GCM", iv: ivArray },
            key,
            dataArray
        );

        const text = new TextDecoder().decode(decrypted);
        const json = JSON.parse(text);

        // Populate forms
        Object.keys(json.secrets).forEach(index => {
            const input = document.querySelector(`.secret-input[data-index="${index}"]`);
            if (input) {
                input.value = json.secrets[index].base;
            }
        });

        // Populate settings
        if (json.settings && json.settings.pepperingHint) {
            document.getElementById("setting-peppering-hint").checked = true;
        }

    } catch (err) {
        console.error(err);
        showToast("Failed to load secrets", true);
    }
}

async function saveEncrypted(data, keyJWK, salt) {
    const text = JSON.stringify(data);
    const encoded = new TextEncoder().encode(text);
    
    const key = await crypto.subtle.importKey(
        "jwk",
        keyJWK,
        { name: "AES-GCM" },
        false,
        ["encrypt"]
    );

    const iv = crypto.getRandomValues(new Uint8Array(12));
    
    const encrypted = await crypto.subtle.encrypt(
        { name: "AES-GCM", iv: iv },
        key,
        encoded
    );

    // Convert TypedArrays to Array for Chrome Storage compatibility (safest)
    await chrome.storage.sync.set({
        salt: Array.from(salt),
        iv: Array.from(iv),
        encryptedData: Array.from(new Uint8Array(encrypted))
    });
}

function showToast(msg, isError = false) {
    const toast = document.getElementById("toast");
    if (!toast) return;
    toast.textContent = msg;
    toast.style.borderColor = isError ? "#da3633" : "#238636";
    toast.style.color = isError ? "#da3633" : "#238636";
    toast.classList.remove("hidden");
    setTimeout(() => toast.classList.add("hidden"), 3000);
}

// Sync State across tabs (e.g. Popup unlocks -> Options unlocks)
chrome.storage.onChanged.addListener((changes, area) => {
    if (area === 'session' && changes.sessionKey) {
        checkStatus();
    }
    if (area === 'sync' && (changes.salt || changes.encryptedData)) {
        checkStatus();
    }
});
