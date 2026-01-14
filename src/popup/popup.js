
function localizeHtml() {
    const objects = document.querySelectorAll('[data-i18n]');
    for (const obj of objects) {
        const msg = obj.getAttribute('data-i18n');
        obj.textContent = chrome.i18n.getMessage(msg);
    }
}

import { Argon2Adapter } from "../adapters/infrastructure/argon2_adapter.js";

document.addEventListener('DOMContentLoaded', async () => {
    localizeHtml();
    
    // Services
    const argon2 = new Argon2Adapter();

    // UI Elements
    const statusSetup = document.getElementById('status-setup');
    const statusUnlocked = document.getElementById('status-unlocked');
    const statusLocked = document.getElementById('status-locked');
    const statusGenerated = document.getElementById('status-generated');
    
    const genPasswordInput = document.getElementById('gen-password');
    const genHint = document.getElementById('gen-hint');

    // Hide all helper
    const hideAll = () => {
        [statusSetup, statusUnlocked, statusLocked, statusGenerated].forEach(el => el.classList.add('hidden'));
    };

    // 1. Check if Master Password is set up (Use 'salt' as indicator)
    const { salt } = await chrome.storage.sync.get("salt");

    if (!salt) {
        // STATE: Setup Required
        hideAll();
        statusSetup.classList.remove('hidden');
    } else {
        // 2. Check Session (Locked vs Unlocked)
        const { sessionKey } = await chrome.storage.session.get("sessionKey");

        if (!sessionKey) {
            // STATE: Locked
            hideAll();
            statusLocked.classList.remove('hidden');
        } else {
            // STATE: Unlocked
            // Try to detect context immediately
            hideAll();
            
            // Default to unlocked view first
            statusUnlocked.classList.remove('hidden');
            
            const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
            
            if (tab && tab.url && tab.url.includes("docs.google.com/spreadsheets")) {
                 try {
                    const response = await chrome.tabs.sendMessage(tab.id, { action: "GET_CURRENT_CELL_PASSWORD" });
                    
                    if (response) {
                        if (response.success) {
                            // SHOW GENERATED
                            statusUnlocked.classList.add('hidden');
                            statusGenerated.classList.remove('hidden');
                            genPasswordInput.value = response.password;
                            
                            if (response.settings && response.settings.pepperingHint) {
                                 genHint.textContent = "🔑 Don't forget your pepper!";
                                 genHint.style.color = "";
                            } else {
                                 genHint.textContent = "";
                            }
                        } else if (response.error) {
                             // Context error - show in generated view or fallback?
                             // User wants to see password "if possible to detect", implying if error, maybe just basic unlocked view?
                             // But previous feedback asked to show errors.
                             // Let's show Generated View with Error for visibility IF it's a specific error (not empty).
                             if (response.error !== "Empty cell" && response.error !== "No active element") {
                                 statusUnlocked.classList.add('hidden');
                                 statusGenerated.classList.remove('hidden');
                                 genPasswordInput.value = "";
                                 const debugText = response.extractedText ? ` ("${response.extractedText}")` : "";
                                 genHint.textContent = `Error: ${response.error}${debugText}`;
                                 genHint.style.color = "#da3633";
                             }
                        }
                    }
                } catch (e) {
                    console.log("Context check failed:", e);
                    if (e.message.includes("Extension context invalidated")) {
                        statusUnlocked.classList.add('hidden');
                        statusGenerated.classList.remove('hidden');
                        genHint.textContent = "⚠️ Please reload this tab.";
                        genHint.style.color = "#da3633";
                    }
                }
            }
        }
    }
    
    // Handlers
    const openOptions = () => chrome.runtime.openOptionsPage();

    // Setup
    const btnStartSetup = document.getElementById('btn-start-setup');
    if (btnStartSetup) btnStartSetup.addEventListener('click', openOptions);

    // Unlock Logic
    const unlockInput = document.getElementById('unlock-password');
    const unlockError = document.getElementById('unlock-error');
    const btnUnlock = document.getElementById('btn-unlock');

    const handleUnlock = async () => {
        const password = unlockInput.value;
        if (!password) return;

        try {
            btnUnlock.textContent = "Unlocking...";
            unlockError.style.display = 'none';

            const { salt, encryptedData, iv } = await chrome.storage.sync.get(["salt", "encryptedData", "iv"]);
            
             if (!salt || !encryptedData || !iv) {
                unlockError.textContent = "Error: Missing data. Please reset in Options.";
                unlockError.style.display = 'block';
                btnUnlock.textContent = "Unlock";
                return;
            }

            const saltArray = new Uint8Array(salt);
            const ivArray = new Uint8Array(iv);
            const dataArray = new Uint8Array(encryptedData);

            // Derive key
            const derivedKey = await argon2.deriveKey(password, saltArray);

            // Verify
             const key = await crypto.subtle.importKey(
                "jwk",
                derivedKey,
                { name: "AES-GCM" },
                false,
                ["decrypt"]
            );

            await crypto.subtle.decrypt(
                { name: "AES-GCM", iv: ivArray },
                key,
                dataArray
            );

            // Success
            await chrome.storage.session.set({ sessionKey: derivedKey });
            
            // Reload popup state logic (simplest way is to reload window)
            window.location.reload();

        } catch (e) {
            console.error("Unlock failed", e);
            unlockError.textContent = "Invalid Password";
            unlockError.style.display = 'block';
            btnUnlock.textContent = "Unlock";
        }
    };

    if (btnUnlock) btnUnlock.addEventListener('click', handleUnlock);
    
    // Allow Enter key to unlock
    if (unlockInput) {
        unlockInput.addEventListener('keydown', (e) => {
            if (e.key === 'Enter') handleUnlock();
        });
    }

    // Lock (from Unlocked view)
    const btnLock = document.getElementById('btn-lock');
    if (btnLock) btnLock.addEventListener('click', async () => {
        await chrome.storage.session.remove("sessionKey");
        window.close();
    });
    
    // Lock (from Generated view)
    const btnLockGen = document.getElementById('btn-lock-gen');
    if (btnLockGen) btnLockGen.addEventListener('click', async () => {
         await chrome.storage.session.remove("sessionKey");
         window.close();
    });

    // Copy
    document.getElementById('btn-copy').addEventListener('click', () => {
        const password = genPasswordInput.value;
        if (!password) return;
        navigator.clipboard.writeText(password);
        const btn = document.getElementById('btn-copy');
        const originalText = btn.textContent;
        btn.textContent = "Copied!";
        setTimeout(() => btn.textContent = originalText, 1500);
    });

    // Back (from Generated -> Unlocked)
    document.getElementById('btn-back').addEventListener('click', () => {
        statusGenerated.classList.add('hidden');
        statusUnlocked.classList.remove('hidden');
    });
    
    // Settings Icon
    document.getElementById('btn-settings').addEventListener('click', openOptions);
});
