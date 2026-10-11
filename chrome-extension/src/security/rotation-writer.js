const bytes = (value, size) => Array.isArray(value) && (size == null || value.length === size)
    && value.every(byte => Number.isInteger(byte) && byte >= 0 && byte <= 255);
const encryptedKeys = state => Object.keys(state).filter(key => /^(profile|shared):/.test(key) && state[key]?.encryptedData).sort();
export function createRotationWriter({ withActive, storage }) {
    return async ({ nonce, updates, expected }) => {
        if (typeof nonce !== 'string' || !bytes(updates?.salt, 16) || !expected) return { success: false, code: 'INVALID_DATA' };
        return withActive(nonce, async () => {
            const current = await storage.get(null), keys = encryptedKeys(current);
            if (JSON.stringify(current.salt) !== JSON.stringify(expected.salt) || JSON.stringify(keys) !== JSON.stringify(encryptedKeys(expected))
                || keys.some(key => JSON.stringify(current[key]) !== JSON.stringify(expected[key]))) return { success: false, code: 'AUTH_CANCELLED' };
            if (keys.some(key => !bytes(updates[key]?.iv, 12) || !bytes(updates[key]?.encryptedData)
                || updates[key].encryptedData.length < 16 || updates[key].encryptedData.length > 8192)) return { success: false, code: 'INVALID_DATA' };
            const safe = { salt: [...updates.salt] };
            for (const key of keys) {
                const record = { iv: [...updates[key].iv], encryptedData: [...updates[key].encryptedData] };
                safe[key] = key.startsWith('shared:') ? { ...current[key], ...record } : record;
            }
            await storage.set(safe);
            return { success: true };
        }, true);
    };
}
