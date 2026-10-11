import { createSerialQueue } from './serial-queue.js';

const MAX_FAILURES = 5;
const LOCKOUT_MS = 5 * 60 * 1000;
function validBytes(value, length) {
    return Array.isArray(value) && (length == null || value.length === length)
        && value.every(byte => Number.isInteger(byte) && byte >= 0 && byte <= 255);
}
export function createAuthCoordinator({ readState, writeState, readVault, derive, publish, clearSession,
    prepareVault, commitVault, now = Date.now, nonce = () => crypto.randomUUID() }) {
    const serial = createSerialQueue();
    const runId = nonce();
    let epoch = 0;
    let ready;
    const read = async () => {
        const value = await readState() || {};
        return { ...value, failures: Number.isInteger(value.failures) ? Math.max(0, Math.min(MAX_FAILURES, value.failures)) : 0,
            lockedUntil: Number.isFinite(value.lockedUntil) ? Math.max(0, value.lockedUntil) : 0 };
    };
    const initialize = () => ready ||= serial(async () => {
        const state = await read();
        if (state.inFlight) {
            // A killed worker cannot reset its already reserved attempt.
            if (state.failures >= MAX_FAILURES) state.lockedUntil = now() + LOCKOUT_MS;
            delete state.inFlight;
            await writeState(state);
        }
    });
    async function status() {
        await initialize();
        return serial(async () => {
            const state = await read();
            return { failures: state.failures, retryAfterMs: Math.max(0, state.lockedUntil - now()), busy: !!state.inFlight };
        });
    }
    async function unlock(password) {
        if (typeof password !== 'string' || !password || password.length > 4096) return { success: false, code: 'INVALID_PASSWORD_INPUT' };
        await initialize();
        const claim = await serial(async () => {
            const state = await read();
            if (state.lockedUntil > now()) return { error: { success: false, code: 'LOCKED_OUT', retryAfterMs: state.lockedUntil - now() } };
            if (state.inFlight) return { error: { success: false, code: 'AUTH_BUSY' } };
            if (state.lockedUntil) { state.failures = 0; state.lockedUntil = 0; }
            const vault = await readVault();
            if (!validBytes(vault.salt, 16) || !validBytes(vault.record?.iv, 12)
                || !validBytes(vault.record?.encryptedData) || vault.record.encryptedData.length < 16) {
                return { error: { success: false, code: 'INVALID_DATA' } };
            }
            const id = nonce();
            const previousFailures = state.failures;
            state.failures++;
            state.inFlight = { id, runId, previousFailures };
            await writeState(state);
            return { id, epoch, vault, previousFailures };
        });
        if (claim.error) return claim.error;
        let answer;
        try { answer = await derive({ password, ...claim.vault }); }
        catch { answer = { success: false, code: 'CRYPTO_UNAVAILABLE' }; }
        password = '';
        try {
            return await serial(async () => {
                const state = await read();
                if (claim.epoch !== epoch || state.inFlight?.id !== claim.id || state.inFlight?.runId !== runId) {
                    return { success: false, code: 'AUTH_CANCELLED' };
                }
                delete state.inFlight;
                if (answer?.success && answer.key) {
                    if (JSON.stringify(await readVault()) !== JSON.stringify(claim.vault)) {
                        state.failures = claim.previousFailures;
                        await writeState(state);
                        return { success: false, code: 'AUTH_CANCELLED' };
                    }
                    await publish(answer.key);
                    if (claim.epoch !== epoch) {
                        await clearSession();
                        await writeState(state);
                        return { success: false, code: 'AUTH_CANCELLED' };
                    }
                    await writeState({ failures: 0, lockedUntil: 0 });
                    return { success: true };
                }
                if (answer?.code !== 'WRONG_PASSWORD') state.failures = claim.previousFailures;
                if (state.failures >= MAX_FAILURES) state.lockedUntil = now() + LOCKOUT_MS;
                await writeState(state);
                return { success: false, code: state.lockedUntil > now() ? 'LOCKED_OUT' : (answer?.code || 'CRYPTO_UNAVAILABLE'),
                    attemptsRemaining: Math.max(0, MAX_FAILURES - state.failures), retryAfterMs: Math.max(0, state.lockedUntil - now()) };
            });
        } catch {
            await clearSession().catch(() => {});
            await serial(async () => {
                const state = await read();
                if (state.inFlight?.id === claim.id) {
                    delete state.inFlight; state.failures = claim.previousFailures; await writeState(state);
                }
            }).catch(() => {});
            return { success: false, code: 'CRYPTO_UNAVAILABLE' };
        } finally {
            if (answer?.key) answer.key.k = '';
        }
    }
    const hasVault = vault => !!(vault?.salt || vault?.record?.encryptedData || vault?.record?.iv);
    async function setup(password) {
        if (typeof password !== 'string' || password.length < 8 || password.length > 4096) return { success: false, code: 'INVALID_PASSWORD_INPUT' };
        await initialize();
        const claim = await serial(async () => {
            const state = await read();
            if (state.inFlight) return { error: { success: false, code: 'AUTH_BUSY' } };
            if (hasVault(await readVault())) return { error: { success: false, code: 'VAULT_EXISTS' } };
            const id = nonce(); state.inFlight = { id, runId, kind: 'setup' }; await writeState(state);
            return { id, epoch };
        });
        if (claim.error) return claim.error;
        let answer;
        try { answer = await prepareVault(password); }
        catch { answer = { success: false, code: 'CRYPTO_UNAVAILABLE' }; }
        password = '';
        try {
            return await serial(async () => {
                const state = await read();
                if (claim.epoch !== epoch || state.inFlight?.id !== claim.id || state.inFlight?.runId !== runId) return { success: false, code: 'AUTH_CANCELLED' };
                delete state.inFlight;
                if (hasVault(await readVault())) { await writeState(state); return { success: false, code: 'VAULT_EXISTS' }; }
                if (!answer?.success || !answer.key?.k || !validBytes(answer.salt, 16) || !validBytes(answer.record?.iv, 12)
                    || !validBytes(answer.record?.encryptedData) || answer.record.encryptedData.length < 16) {
                    await writeState(state); return { success: false, code: 'CRYPTO_UNAVAILABLE' };
                }
                await commitVault({ salt: answer.salt, record: answer.record });
                if (claim.epoch !== epoch) { await writeState(state); return { success: false, code: 'AUTH_CANCELLED' }; }
                await publish(answer.key);
                if (claim.epoch !== epoch) { await clearSession(); await writeState(state); return { success: false, code: 'AUTH_CANCELLED' }; }
                await writeState({ failures: 0, lockedUntil: 0 });
                return { success: true };
            });
        } catch {
            await clearSession().catch(() => {});
            await serial(async () => { const state = await read(); if (state.inFlight?.id === claim.id) { delete state.inFlight; await writeState(state); } }).catch(() => {});
            return { success: false, code: 'CRYPTO_UNAVAILABLE' };
        } finally { if (answer?.key) answer.key.k = ''; }
    }
    async function lock() {
        epoch++;
        await initialize();
        return serial(async () => {
            const state = await read();
            if (state.inFlight) {
                delete state.inFlight;
                if (state.failures >= MAX_FAILURES) state.lockedUntil = now() + LOCKOUT_MS;
                await writeState(state);
            }
            await clearSession();
        });
    }
    return { initialize, status, unlock, setup, lock };
}
