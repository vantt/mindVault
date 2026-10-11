import { describe, it, expect } from 'vitest';
import { createSessionAuthority } from '../../src/security/session-authority.js';
import { createProfileRecordWriter } from '../../src/security/profile-record-writer.js';
import { createRotationWriter } from '../../src/security/rotation-writer.js';
const record = value => ({ iv: Array(12).fill(value), encryptedData: Array(32).fill(value) });
function fixture() {
    let session = {}, persistent = { salt: Array(16).fill(1), 'profile:Default': record(1) }, writes = 0, sequence = 0;
    const authority = createSessionAuthority({ now: () => 1000, nonce: () => `nonce-${++sequence}`,
        session: { get: async () => structuredClone(session), set: async value => { session = structuredClone(value); },
            remove: async keys => keys.forEach(key => delete session[key]) },
        alarms: { get: async () => undefined, create: async () => {}, clear: async () => {} } });
    const storage = { get: async key => key == null ? structuredClone(persistent) : { [key]: structuredClone(persistent[key]) },
        set: async value => { writes++; Object.assign(persistent, structuredClone(value)); } };
    return { authority, writer: createProfileRecordWriter({ withActive: authority.withActive, storage }),
        rotate: createRotationWriter({ withActive: authority.withActive, storage }), current: () => session,
        persistent: () => structuredClone(persistent), writes: () => writes, mutate: () => { persistent['profile:Default'] = record(3); } };
}
describe('encrypted writes bound to current session ownership', () => {
    it('rejects a profile save using a locked or replaced session nonce', async () => {
        const f = fixture(); await f.authority.start({ k: 'fixture-key' }); const nonce = f.current().sessionNonce;
        await f.authority.clear();
        expect((await f.writer({ nonce, profileKey: 'profile:Default', record: record(2) })).code).toBe('VAULT_LOCKED');
        await f.authority.start({ k: 'new-fixture-key' });
        expect((await f.writer({ nonce, profileKey: 'profile:Default', record: record(2) })).code).toBe('VAULT_LOCKED');
        expect(f.writes()).toBe(0); expect(f.persistent()['profile:Default']).toEqual(record(1));
    });
    it('keeps existing Spices when two create-only requests name the same profile', async () => {
        const f = fixture(); await f.authority.start({ k: 'fixture-key' }); const nonce = f.current().sessionNonce;
        expect(await f.writer({ nonce, profileKey: 'profile:Other', record: record(2), createOnly: true })).toEqual({ success: true });
        expect((await f.writer({ nonce, profileKey: 'profile:Other', record: record(3), createOnly: true })).code).toBe('PROFILE_EXISTS');
        expect(f.persistent()['profile:Other']).toEqual(record(2)); expect(f.writes()).toBe(1);
    });
    it('persists only ciphertext fields, and rejects malformed ciphertext', async () => {
        const f = fixture(); await f.authority.start({ k: 'fixture-key' }); const nonce = f.current().sessionNonce;
        expect(await f.writer({ nonce, profileKey: 'profile:Default', record: { ...record(2), rawSpice: 'synthetic-do-not-persist' } })).toEqual({ success: true });
        expect(f.persistent()['profile:Default']).toEqual(record(2));
        expect((await f.writer({ nonce, profileKey: 'profile:Default', record: { iv: [], encryptedData: [] } })).code).toBe('INVALID_DATA');
        expect(f.writes()).toBe(1);
    });
    it('successful rotation atomically retires the old session before another profile write', async () => {
        const f = fixture(); await f.authority.start({ k: 'fixture-old-key' }); const nonce = f.current().sessionNonce;
        const expected = f.persistent();
        expect(await f.rotate({ nonce, expected, updates: { salt: Array(16).fill(2), 'profile:Default': record(2) } })).toEqual({ success: true });
        expect(f.current()).toEqual({});
        expect((await f.writer({ nonce, profileKey: 'profile:Default', record: record(3) })).code).toBe('VAULT_LOCKED');
        expect(f.persistent()['profile:Default']).toEqual(record(2)); expect(f.writes()).toBe(1);
    });
    it('rotation refuses to overwrite edits that arrived after its encrypted snapshot', async () => {
        const f = fixture(); await f.authority.start({ k: 'fixture-key' }); const nonce = f.current().sessionNonce;
        const expected = f.persistent(); f.mutate();
        expect((await f.rotate({ nonce, expected, updates: { salt: Array(16).fill(2), 'profile:Default': record(2) } })).code).toBe('AUTH_CANCELLED');
        expect(f.writes()).toBe(0); expect(f.persistent()['profile:Default']).toEqual(record(3));
        expect(f.current().sessionKey).toBeTruthy();
    });
});
