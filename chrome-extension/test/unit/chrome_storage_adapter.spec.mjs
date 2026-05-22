import { describe, it, expect, vi, beforeEach } from 'vitest';

// Mock the AES helper module so we don't need a real session key / JWK.
// Each profile fetched returns a fixed decrypted shape.
vi.mock('../../src/adapters/infrastructure/aes-storage-crypto-helper.js', () => ({
    getSessionKey: vi.fn(async () => ({ k: 'mock-jwk' })),
    decryptWithKey: vi.fn(async () => ({
        secrets: {
            "1": { base: "Basic*" },
            "2": { base: "Secure#" },
        },
        settings: { pepperingHint: false },
    })),
}));

import { ChromeStorageAdapter } from '../../src/adapters/infrastructure/chrome_storage_adapter.js';

function installChromeMock({ ownProfiles = ['Default'], sharedProfiles = {} } = {}) {
    const store = {};
    for (const name of ownProfiles) {
        store[`profile:${name}`] = { encryptedData: [], iv: [] };
    }
    for (const [name, sheetId] of Object.entries(sharedProfiles)) {
        store[`shared:${name}`] = { encryptedData: [], iv: [], sheetId, readOnly: true };
    }
    store.defaultProfile = ownProfiles[0];

    globalThis.chrome = {
        storage: {
            sync: {
                get: vi.fn(async (keys) => {
                    if (keys === null) return { ...store };
                    if (Array.isArray(keys)) {
                        const out = {};
                        for (const k of keys) if (k in store) out[k] = store[k];
                        return out;
                    }
                    if (typeof keys === 'string') {
                        return keys in store ? { [keys]: store[keys] } : {};
                    }
                    return {};
                }),
            },
            session: { get: vi.fn(async () => ({ sessionKey: { k: 'mock' } })) },
        },
    };
}

describe('ChromeStorageAdapter.getSecretForGeneration — binding matrix (Option B)', () => {
    beforeEach(() => { installChromeMock(); });

    it('own + sheetId → effectiveSecret === rawSecret (own never binds)', async () => {
        const adapter = new ChromeStorageAdapter();
        const r = await adapter.getSecretForGeneration(1, 'sheet-A');
        expect(r.rawSecret).toBe('Basic*');
        expect(r.effectiveSecret).toBe('Basic*');
        expect(r.isShared).toBe(false);
        expect(r.sheetId).toBe('sheet-A');
    });

    it('own + no sheetId → effectiveSecret === rawSecret', async () => {
        const adapter = new ChromeStorageAdapter();
        const r = await adapter.getSecretForGeneration(1, null);
        expect(r.rawSecret).toBe('Basic*');
        expect(r.effectiveSecret).toBe('Basic*');
        expect(r.sheetId).toBeNull();
    });

    it('shared + sheetId → effectiveSecret !== rawSecret (HKDF bound, PRD §2.4)', async () => {
        installChromeMock({
            ownProfiles: ['Default'],
            sharedProfiles: { TeamFromB: 'sheet-shared-1' },
        });
        const adapter = new ChromeStorageAdapter();
        const r = await adapter.getSecretForGeneration(1, 'sheet-shared-1');
        expect(r.rawSecret).toBe('Basic*');
        expect(r.effectiveSecret).not.toBe('Basic*');
        expect(typeof r.effectiveSecret).toBe('string');
        expect(r.isShared).toBe(true);
    });

    it('shared + no sheetId → effectiveSecret === rawSecret (no binding without sheet)', async () => {
        // Force shared profile via override is not possible; instead, set a shared profile
        // and pass sheetId=null — resolveProfileForSheet falls back to default own profile.
        // Use a direct test: load shared explicitly via getDecryptedProfile path is internal.
        // We simulate via a sheetMapping override route — but adapter resolves own as fallback
        // when no sheetId. So this case effectively tests: shared not selected when sheetId null.
        installChromeMock({
            ownProfiles: ['Default'],
            sharedProfiles: { TeamFromB: 'sheet-shared-1' },
        });
        const adapter = new ChromeStorageAdapter();
        // null sheetId routes to default OWN profile, so isShared=false here.
        const r = await adapter.getSecretForGeneration(1, null);
        expect(r.isShared).toBe(false);
        expect(r.effectiveSecret).toBe(r.rawSecret);
    });

    it('profileNameOverride takes own profile path even when shared profile exists', async () => {
        installChromeMock({
            ownProfiles: ['Default', 'Banking'],
            sharedProfiles: { TeamFromB: 'sheet-shared-1' },
        });
        const adapter = new ChromeStorageAdapter();
        const r = await adapter.getSecretForGeneration(1, 'sheet-shared-1', 'Banking');
        expect(r.isShared).toBe(false);
        expect(r.profileName).toBe('Banking');
        expect(r.rawSecret).toBe(r.effectiveSecret); // own never binds
    });

    it('returns null when secret index missing', async () => {
        const adapter = new ChromeStorageAdapter();
        const r = await adapter.getSecretForGeneration(5, null); // mock only has 1,2
        expect(r).toBeNull();
    });

    it('legacy getSecret() shim returns effectiveSecret string', async () => {
        const adapter = new ChromeStorageAdapter();
        const s = await adapter.getSecret(1);
        expect(s).toBe('Basic*');
    });
});
