/**
 * Shared AES-GCM encrypt/decrypt helpers.
 * Operates on JSON-serializable data using a JWK session key.
 */

export async function getSessionKey() {
    const { sessionKey } = await chrome.storage.session.get("sessionKey");
    if (!sessionKey) throw new Error("Vault is locked");
    return sessionKey;
}

export async function encryptWithKey(data, keyJWK) {
    const encoded = new TextEncoder().encode(JSON.stringify(data));
    const key = await crypto.subtle.importKey(
        "jwk", keyJWK, { name: "AES-GCM" }, false, ["encrypt"]
    );
    const iv = crypto.getRandomValues(new Uint8Array(12));
    const encrypted = await crypto.subtle.encrypt({ name: "AES-GCM", iv }, key, encoded);
    return {
        encryptedData: Array.from(new Uint8Array(encrypted)),
        iv: Array.from(iv)
    };
}

export async function decryptWithKey(encryptedData, iv, keyJWK) {
    const key = await crypto.subtle.importKey(
        "jwk", keyJWK, { name: "AES-GCM" }, false, ["decrypt"]
    );
    const decrypted = await crypto.subtle.decrypt(
        { name: "AES-GCM", iv: new Uint8Array(iv) },
        key,
        new Uint8Array(encryptedData)
    );
    return JSON.parse(new TextDecoder().decode(decrypted));
}
