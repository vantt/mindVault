import { describe, it, expect } from 'vitest';
import { createAuthCoordinator } from '../../src/security/auth-coordinator.js';
import { createSessionAuthority } from '../../src/security/session-authority.js';

const clone = value => structuredClone(value);
const deferred = () => { let resolve; const promise = new Promise(done => { resolve = done; }); return { promise, resolve }; };
function fixture() {
    let clock = 1000, state = {}, published;
    let vault = { salt: Array(16).fill(1), record: { iv: Array(12).fill(2), encryptedData: Array(17).fill(3) } };
    let answer = { success: false, code: 'WRONG_PASSWORD' }, calls = 0;
    const deps = { readState: async () => clone(state), writeState: async next => { state = clone(next); },
        readVault: async () => deps.vaultSource(), derive: async value => deps.derivation(value),
        vaultSource: async () => clone(vault), derivation: async () => { calls++; return clone(answer); },
        publish: async key => deps.publication(key), publication: async key => { published = clone(key); }, clearSession: async () => { published = undefined; }, now: () => clock };
    return { deps, auth: createAuthCoordinator(deps), state: () => state, published: () => published,
        calls: () => calls, tick: ms => { clock += ms; }, answer: next => { answer = next; },
        mutate: () => { vault.record.encryptedData[0]++; }, vault: () => clone(vault) };
}
describe('durable authentication authority', () => {
    it('locks the fifth failed attempt for five minutes, survives reconstruction and permits boundary retry', async () => {
        const f = fixture(), before = f.vault();
        for (let i = 0; i < 4; i++) expect((await f.auth.unlock('synthetic-wrong')).attemptsRemaining).toBe(4 - i);
        expect(await f.auth.unlock('synthetic-wrong')).toMatchObject({ code: 'LOCKED_OUT', retryAfterMs: 300000 });
        const restarted = createAuthCoordinator(f.deps);
        expect((await restarted.unlock('synthetic-correct')).code).toBe('LOCKED_OUT');
        expect(f.calls()).toBe(5); expect(f.vault()).toEqual(before);
        f.tick(299999); expect((await restarted.unlock('synthetic-correct')).code).toBe('LOCKED_OUT');
        f.tick(1); f.answer({ success: true, key: { k: 'synthetic-key' } });
        expect(await restarted.unlock('synthetic-correct')).toEqual({ success: true });
        expect(f.state()).toEqual({ failures: 0, lockedUntil: 0 });
    });
    it('admits only one concurrent derivation across competing clients', async () => {
        const f = fixture(), pending = deferred(); f.deps.derivation = () => pending.promise;
        const active = f.auth.unlock('synthetic');
        await new Promise(resolve => setTimeout(resolve, 0));
        expect((await f.auth.unlock('synthetic')).code).toBe('AUTH_BUSY');
        pending.resolve({ success: false, code: 'WRONG_PASSWORD' }); await active;
        expect(f.state().failures).toBe(1);
    });
    it('reserves before work so worker death cannot reset an abandoned failure', async () => {
        const f = fixture(), pending = deferred(); f.deps.derivation = () => pending.promise;
        const active = f.auth.unlock('synthetic');
        await new Promise(resolve => setTimeout(resolve, 0));
        expect(f.state().inFlight).toBeTruthy();
        const restarted = createAuthCoordinator(f.deps); await restarted.initialize();
        expect(f.state().failures).toBe(1); expect(f.state().inFlight).toBeUndefined();
        pending.resolve({ success: true, key: { k: 'stale-key' } });
        expect((await active).code).toBe('AUTH_CANCELLED'); expect(f.published()).toBeUndefined();
    });
    it('known runtime failure does not count as a bad password or publish a key', async () => {
        const f = fixture(); f.answer({ success: false, code: 'CRYPTO_UNAVAILABLE' });
        expect((await f.auth.unlock('synthetic')).code).toBe('CRYPTO_UNAVAILABLE');
        expect(f.state().failures).toBe(0); expect(f.published()).toBeUndefined();
    });
    it('lock invalidates an in-flight successful reply and wipes transferred key ownership', async () => {
        const f = fixture(), pending = deferred(); f.deps.derivation = () => pending.promise;
        const active = f.auth.unlock('synthetic'); await new Promise(resolve => setTimeout(resolve, 0));
        await f.auth.lock(); const answer = { success: true, key: { k: 'owned-test-value' } }; pending.resolve(answer);
        expect((await active).code).toBe('AUTH_CANCELLED'); expect(f.published()).toBeUndefined(); expect(answer.key.k).toBe('');
    });
    it('rejects a successful reply when encrypted vault changed during derivation', async () => {
        const f = fixture(), pending = deferred(); f.deps.derivation = () => pending.promise;
        const active = f.auth.unlock('synthetic'); await new Promise(resolve => setTimeout(resolve, 0));
        f.mutate(); pending.resolve({ success: true, key: { k: 'old-key' } });
        expect((await active).code).toBe('AUTH_CANCELLED'); expect(f.published()).toBeUndefined();
    });
    it('failed session publication releases the claim so a genuine retry can proceed', async () => {
        const f = fixture(); f.answer({ success: true, key: { k: 'fixture-key' } });
        const publish = f.deps.publication;
        f.deps.publication = async () => { throw new Error('synthetic storage failure'); };
        expect((await f.auth.unlock('synthetic')).code).toBe('CRYPTO_UNAVAILABLE');
        expect(f.state().inFlight).toBeUndefined(); expect(f.published()).toBeUndefined();
        f.deps.publication = publish;
        expect(await f.auth.unlock('synthetic')).toEqual({ success: true });
    });
    it('invalid persistent ciphertext fails before derivation and does not consume an attempt', async () => {
        const f = fixture(); f.deps.vaultSource = async () => ({ salt: [], record: {} });
        expect((await f.auth.unlock('synthetic')).code).toBe('INVALID_DATA'); expect(f.calls()).toBe(0);
        expect(f.state()).toEqual({});
    });
});
describe('session deadline authority', () => {
    it('recreates lost alarms without extending deadline, and checks exact expiry on access', async () => {
        let clock = 1000, stored = {}, alarm;
        const session = { get: async () => clone(stored), set: async next => { stored = clone(next); },
            remove: async keys => { keys.forEach(key => delete stored[key]); } };
        const alarms = { get: async () => alarm, create: async (_, value) => { alarm = { scheduledTime: value.when }; }, clear: async () => { alarm = undefined; } };
        const owner = createSessionAuthority({ session, alarms, now: () => clock });
        await owner.start({ k: 'test-only-key' }); const original = stored.sessionDeadline; alarm = undefined; clock += 10000;
        const restarted = createSessionAuthority({ session, alarms, now: () => clock });
        expect((await restarted.check()).unlocked).toBe(true); expect(alarm.scheduledTime).toBe(original);
        clock = original; expect(await restarted.check()).toEqual({ unlocked: false, expired: true }); expect(stored).toEqual({});
    });
});
