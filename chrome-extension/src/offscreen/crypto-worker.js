import { Argon2Adapter } from '../adapters/infrastructure/argon2_adapter.js';

const adapter = new Argon2Adapter();
self.onmessage = async ({ data }) => {
    const { id, password, salt, record } = data;
    let key;
    let plaintext;
    try {
        key = await adapter.deriveKey(password, new Uint8Array(salt));
        if (record) {
            const imported = await crypto.subtle.importKey('jwk', key, 'AES-GCM', false, ['decrypt']);
            try {
                plaintext = new Uint8Array(await crypto.subtle.decrypt(
                    { name: 'AES-GCM', iv: new Uint8Array(record.iv) }, imported,
                    new Uint8Array(record.encryptedData)));
                JSON.parse(new TextDecoder().decode(plaintext));
            } catch (error) {
                self.postMessage({ id, success: false, code: error?.name === 'OperationError' ? 'WRONG_PASSWORD' : 'INVALID_DATA' });
                return;
            }
        }
        let created;
        if (data.create === true) {
            const initial = { secrets: Object.fromEntries([1, 2, 3, 4, 5].map(index => [String(index), { base: '' }])), settings: {} };
            plaintext = new TextEncoder().encode(JSON.stringify(initial));
            const imported = await crypto.subtle.importKey('jwk', key, 'AES-GCM', false, ['encrypt']);
            const iv = crypto.getRandomValues(new Uint8Array(12));
            const encrypted = await crypto.subtle.encrypt({ name: 'AES-GCM', iv }, imported, plaintext);
            created = { iv: Array.from(iv), encryptedData: Array.from(new Uint8Array(encrypted)) };
        }
        self.postMessage({ id, success: true, key, record: created });
    } catch {
        self.postMessage({ id, success: false, code: 'CRYPTO_UNAVAILABLE' });
    } finally {
        plaintext?.fill(0);
        if (key) key.k = '';
        data.password = '';
    }
};
