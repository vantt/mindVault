import { describe, it, expect } from 'vitest';
import { createAuthCoordinator } from '../../src/security/auth-coordinator.js';
const turn = () => new Promise(resolve => setTimeout(resolve, 0));
const deferred = () => { let resolve; const promise = new Promise(done => { resolve = done; }); return { promise, resolve }; };
const prepared = () => ({ success: true, key: { k: 'synthetic-key' }, salt: Array(16).fill(1), record: { iv: Array(12).fill(2), encryptedData: Array(17).fill(3) } });
function fixture() {
    let state = {}, vault = {}, commits = 0, published, failCommit = false;
    const pending = deferred();
    const deps = { readState: async () => structuredClone(state), writeState: async value => { state = structuredClone(value); },
        readVault: async () => structuredClone(vault), prepareVault: () => pending.promise,
        commitVault: async value => { if (failCommit) throw new Error('synthetic interruption'); commits++; vault = structuredClone(value); },
        publish: async key => { published = structuredClone(key); }, clearSession: async () => { published = undefined; } };
    return { auth: createAuthCoordinator(deps), deps, pending, commits: () => commits, published: () => published,
        replace: value => { vault = structuredClone(value); }, vault: () => structuredClone(vault), state: () => state, fail: value => { failCommit = value; } };
}
describe('first Cookbook setup authority', () => {
    it('admits exactly one client and rejects stale setup after the first publication', async () => {
        const f = fixture(), active = f.auth.setup('synthetic-password'); await turn();
        expect((await f.auth.setup('second-password')).code).toBe('AUTH_BUSY');
        f.pending.resolve(prepared()); expect(await active).toEqual({ success: true });
        const existing = f.vault(); expect(f.commits()).toBe(1);
        expect((await f.auth.setup('stale-password')).code).toBe('VAULT_EXISTS');
        expect(f.vault()).toEqual(existing); expect(f.commits()).toBe(1);
    });
    it('preserves a Cookbook that appeared while derivation was pending', async () => {
        const f = fixture(), active = f.auth.setup('synthetic-password'); await turn();
        const existing = { salt: Array(16).fill(9), record: { iv: Array(12).fill(8), encryptedData: Array(32).fill(7) } };
        f.replace(existing); f.pending.resolve(prepared());
        expect((await active).code).toBe('VAULT_EXISTS'); expect(f.vault()).toEqual(existing);
        expect(f.commits()).toBe(0); expect(f.published()).toBeUndefined();
    });
    it('lock cancels pending setup without any ciphertext mutation or transferred key retention', async () => {
        const f = fixture(), active = f.auth.setup('synthetic-password'); await turn(); await f.auth.lock();
        const answer = prepared(); f.pending.resolve(answer);
        expect((await active).code).toBe('AUTH_CANCELLED'); expect(f.commits()).toBe(0);
        expect(f.vault()).toEqual({}); expect(answer.key.k).toBe('');
    });
    it('abandoned setup after restart cannot publish or overwrite a replacement claim', async () => {
        const f = fixture(), active = f.auth.setup('synthetic-password'); await turn();
        const fresh = createAuthCoordinator(f.deps); await fresh.initialize();
        f.pending.resolve(prepared()); expect((await active).code).toBe('AUTH_CANCELLED');
        expect(f.commits()).toBe(0); expect(f.published()).toBeUndefined();
    });
    it('storage failure releases setup admission for retry', async () => {
        const f = fixture(); f.fail(true); f.pending.resolve(prepared());
        expect((await f.auth.setup('synthetic-password')).code).toBe('CRYPTO_UNAVAILABLE');
        expect(f.state().inFlight).toBeUndefined(); expect(f.vault()).toEqual({});
    });
    it('invalid prepared data fails closed without a stored vault', async () => {
        const f = fixture(); f.pending.resolve({ success: true, key: { k: 'fixture-key' }, salt: [], record: {} });
        expect((await f.auth.setup('synthetic-password')).code).toBe('CRYPTO_UNAVAILABLE');
        expect(f.commits()).toBe(0); expect(f.published()).toBeUndefined(); expect(f.state().inFlight).toBeUndefined();
    });
});
