import { describe, it, expect } from 'vitest';
import { createAuthCoordinator } from '../../src/security/auth-coordinator.js';
import { createSessionGuard } from '../../src/security/session-guard.js';
import { createSessionAuthority } from '../../src/security/session-authority.js';
const deferred = () => { let resolve; const promise = new Promise(done => { resolve = done; }); return { promise, resolve }; };
const turn = () => new Promise(resolve => setTimeout(resolve, 0));

describe('serialized session storage and alarms', () => {
    it('an expired read cannot erase a subsequently published session', async () => {
        const read = deferred(); let stored = { sessionKey: { k: 'old-fixture' }, sessionDeadline: 1 }, alarm, sets = 0;
        const owner = createSessionAuthority({ now: () => 1000,
            session: { get: () => read.promise, set: async state => { sets++; stored = structuredClone(state); },
                remove: async keys => keys.forEach(key => delete stored[key]) },
            alarms: { get: async () => alarm, clear: async () => { alarm = undefined; },
                create: async (_, state) => { alarm = state; } } });
        const expired = owner.check(); await turn();
        const fresh = owner.start({ k: 'fresh-fixture' }); await turn();
        expect(sets).toBe(0);
        read.resolve({ sessionKey: { k: 'old-fixture' }, sessionDeadline: 1 });
        expect(await expired).toEqual({ unlocked: false, expired: true }); await fresh;
        expect(stored.sessionKey.k).toBe('fresh-fixture'); expect(stored.sessionDeadline).toBe(601000);
        expect(alarm.when).toBe(601000);
    });
    it('expiry discovered by access invalidates a pending successful authentication', async () => {
        const pending = deferred(); let state = {}, published;
        const auth = createAuthCoordinator({ readState: async () => structuredClone(state), writeState: async value => { state = structuredClone(value); },
            readVault: async () => ({ salt: Array(16).fill(1), record: { iv: Array(12).fill(2), encryptedData: Array(17).fill(3) } }),
            derive: () => pending.promise, publish: async key => { published = key; }, clearSession: async () => { published = undefined; } });
        const active = auth.unlock('synthetic'); await turn();
        const check = createSessionGuard({ check: async () => ({ unlocked: false, expired: true }), invalidate: auth.lock });
        expect((await check()).unlocked).toBe(false);
        const transferred = { success: true, key: { k: 'fixture-only-key' } }; pending.resolve(transferred);
        expect((await active).code).toBe('AUTH_CANCELLED'); expect(published).toBeUndefined(); expect(transferred.key.k).toBe('');
    });
    it('a delayed clear cannot remove a new session alarm', async () => {
        const removal = deferred(); let stored = { sessionKey: { k: 'old-fixture' } }, alarm = { when: 5 }, sets = 0;
        const owner = createSessionAuthority({ now: () => 2000,
            session: { get: async () => structuredClone(stored), set: async state => { sets++; stored = state; },
                remove: async keys => { keys.forEach(key => delete stored[key]); await removal.promise; } },
            alarms: { get: async () => alarm, clear: async () => { alarm = undefined; },
                create: async (_, state) => { alarm = state; } } });
        const clearing = owner.clear(); await turn();
        const fresh = owner.start({ k: 'fresh-fixture' }); await turn(); expect(sets).toBe(0);
        removal.resolve(); await clearing; await fresh;
        expect(stored.sessionKey.k).toBe('fresh-fixture'); expect(alarm.when).toBe(602000);
    });
});
