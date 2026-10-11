import { Argon2Adapter } from "../adapters/infrastructure/argon2_adapter.js";
import { encryptWithKey, decryptWithKey } from "../adapters/infrastructure/aes-storage-crypto-helper.js";
import { t } from "../i18n-loader.js";
import { securityRequest, securityMessage } from "../security/page-security.js";

export function bindPasswordChange({ showToast, setSessionKey }) {
    const argon2 = new Argon2Adapter();
    // Change password (Settings tab)
    document.getElementById("change-password-form").addEventListener("submit", async (e) => {
        e.preventDefault();
        const oldPwd = document.getElementById("old-password").value;
        const newPwd = document.getElementById("new-password").value;
        const confirmPwd = document.getElementById("confirm-new-password").value;
        if (newPwd !== confirmPwd) { showToast(t("toastPwdMismatch"), true); return; }
        if (newPwd.length < 8) { showToast(t("toastPwdShort"), true); return; }
        if (!document.getElementById("change-backup-confirm").checked) { showToast("Please confirm backup", true); return; }
        try {
            showToast("Re-encrypting…");
            const { salt, defaultProfile } = await chrome.storage.sync.get(["salt", "defaultProfile"]);
            const verified = await securityRequest("AUTH_UNLOCK", { password: oldPwd });
            if (!verified.success) { showToast(securityMessage(verified), true); return; }
            const authority = await securityRequest("AUTH_STATUS");
            const { sessionKey: oldDerivedKey } = await chrome.storage.session.get("sessionKey");
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
            const current = await securityRequest("AUTH_STATUS");
            if (!current.unlocked || current.nonce !== authority.nonce) { showToast(securityMessage({ code: "AUTH_CANCELLED" }), true); return; }
            const expected = Object.fromEntries(Object.entries(all).filter(([key, value]) => key === "salt" || (/^(profile|shared):/.test(key) && value?.encryptedData)));
            const committed = await securityRequest("AUTH_ROTATE", { nonce: authority.nonce, updates, expected });
            if (!committed.success) { showToast(securityMessage(committed), true); return; }
            const published = await securityRequest("AUTH_PUBLISH", { password: newPwd });
            if (!published.success) throw new Error(securityMessage(published));
            const active = await chrome.storage.session.get("sessionKey");
            setSessionKey(active.sessionKey);
            showToast(t("toastPwdUpdated"));
            document.getElementById("change-password-form").reset();
            document.getElementById("change-password-form").classList.add("hidden");
            document.getElementById("pwd-summary").classList.remove("hidden");
        } catch {
            showToast(securityMessage({ code: "CRYPTO_UNAVAILABLE" }), true);
        }
    });

}
