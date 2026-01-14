
import { IStorageRepository } from "../../core/ports/interfaces.js";

export class ChromeStorageAdapter extends IStorageRepository {
    constructor() {
        super();
        this.sync = chrome.storage.sync;
        this.session = chrome.storage.session;
    }

    async isAuthenticated() {
        try {
            const { sessionKey } = await this.session.get("sessionKey");
            return !!sessionKey;
        } catch (e) {
            console.error("Auth check failed:", e);
            return false;
        }
    }

    /**
     * @param {number} index 
     * @returns {Promise<string>}
     */
    async getSecret(index) {
        // 1. Get Session Key
        const { sessionKey } = await this.session.get("sessionKey");
        if (!sessionKey) {
            throw new Error("Vault is locked");
        }

        // 2. Get Encrypted Data
        const { encryptedData, iv } = await this.sync.get(["encryptedData", "iv"]);
        if (!encryptedData || !iv) {
            // If nothing stored, maybe return undefined or throw?
            // Assuming setup is done.
            return null;
        }

        // 3. Decrypt
        // Import key
        const key = await crypto.subtle.importKey(
            "jwk",
            sessionKey,
            { name: "AES-GCM" },
            false,
            ["decrypt"]
        );

        // Decrypt
        const ivArray = new Uint8Array(iv);
        const dataArray = new Uint8Array(encryptedData);

        const decryptedBuffer = await crypto.subtle.decrypt(
            {
                name: "AES-GCM",
                iv: ivArray
            },
            key,
            dataArray
        );

        const decryptedText = new TextDecoder().decode(decryptedBuffer);
        const secrets = JSON.parse(decryptedText);

        // PRD 5.4.5: Decrypted structure
        // { "secrets": { "1": { "base": "..." }, ... } }
        
        const secretObj = secrets.secrets[index.toString()];
        if (!secretObj) return null;

        // Return base secret for now
        return secretObj.base;
    }

    /**
     * @returns {Promise<Object>}
     */
    async getSettings() {
        // Reuse decryption logic. 
        // NOTE: In production, we should cache the decrypted JSON in memory (RAM) 
        // to avoid decrypting on every single keypress or request, 
        // since sessionKey is in RAM anyway.
        // For MVP, decrypting again is fine.
        
        try {
             // 1. Get Session Key
            const { sessionKey } = await this.session.get("sessionKey");
            if (!sessionKey) return {};

            // 2. Get Encrypted Data
            const { encryptedData, iv } = await this.sync.get(["encryptedData", "iv"]);
            if (!encryptedData || !iv) return {};

            // 3. Decrypt
            const key = await crypto.subtle.importKey(
                "jwk",
                sessionKey,
                { name: "AES-GCM" },
                false,
                ["decrypt"]
            );

            const ivArray = new Uint8Array(iv);
            const dataArray = new Uint8Array(encryptedData);

            const decryptedBuffer = await crypto.subtle.decrypt(
                { name: "AES-GCM", iv: ivArray },
                key,
                dataArray
            );

            const decryptedText = new TextDecoder().decode(decryptedBuffer);
            const secrets = JSON.parse(decryptedText);
            
            return secrets.settings || {};
        } catch (e) {
            console.error(e);
            return {};
        }
    }
}
