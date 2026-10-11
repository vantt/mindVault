const bytes = (value, size) => Array.isArray(value) && (size == null || value.length === size)
    && value.every(byte => Number.isInteger(byte) && byte >= 0 && byte <= 255);
export function createProfileRecordWriter({ withActive, storage }) {
    return async ({ nonce, profileKey, record, createOnly = false }) => {
        if (typeof nonce !== 'string' || !nonce || typeof profileKey !== 'string' || !profileKey.startsWith('profile:')
            || profileKey.length <= 8 || profileKey.length > 200 || !bytes(record?.iv, 12)
            || !bytes(record?.encryptedData) || record.encryptedData.length < 16 || record.encryptedData.length > 8192) {
            return { success: false, code: 'INVALID_DATA' };
        }
        // Only ciphertext bytes cross this boundary; arbitrary caller metadata is never persisted.
        const encrypted = { iv: [...record.iv], encryptedData: [...record.encryptedData] };
        return withActive(nonce, async () => {
            if (createOnly && (await storage.get(profileKey))[profileKey]) return { success: false, code: 'PROFILE_EXISTS' };
            await storage.set({ [profileKey]: encrypted });
            return { success: true };
        });
    };
}
