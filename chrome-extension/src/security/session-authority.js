import { createSerialQueue } from './serial-queue.js';

const SESSION_MS = 10 * 60 * 1000;
export function createSessionAuthority({ session, alarms, now = Date.now, nonce = () => crypto.randomUUID() }) {
    const serial = createSerialQueue();
    async function clearOwned() {
        await session.remove(['sessionKey', 'sessionDeadline', 'sessionStartedAt', 'sessionNonce']);
        await alarms.clear('sessionTimeout');
    }
    const clear = () => serial(clearOwned);
    const start = key => serial(async () => {
        const started = now();
        await session.set({ sessionKey: key, sessionStartedAt: started,
            sessionDeadline: started + SESSION_MS, sessionNonce: nonce() });
        await alarms.create('sessionTimeout', { when: started + SESSION_MS });
    });
    const check = () => serial(async () => {
        const state = await session.get(['sessionKey', 'sessionDeadline', 'sessionStartedAt', 'sessionNonce']);
        if (!state.sessionKey) return { unlocked: false };
        if (!Number.isFinite(state.sessionDeadline) || state.sessionDeadline <= now()) {
            await clearOwned();
            return { unlocked: false, expired: true };
        }
        const existing = await alarms.get('sessionTimeout');
        if (!existing || existing.scheduledTime !== state.sessionDeadline) {
            await alarms.create('sessionTimeout', { when: state.sessionDeadline });
        }
        return { unlocked: true, expiresAt: state.sessionDeadline, nonce: state.sessionNonce };
    });
    const withActive = (expectedNonce, work, invalidateAfter = false) => serial(async () => {
        const state = await session.get(['sessionKey', 'sessionDeadline', 'sessionNonce']);
        if (!state.sessionKey || state.sessionNonce !== expectedNonce) return { success: false, code: 'VAULT_LOCKED' };
        if (!Number.isFinite(state.sessionDeadline) || state.sessionDeadline <= now()) {
            await clearOwned(); return { success: false, code: 'VAULT_LOCKED', expired: true };
        }
        const result = await work();
        if (invalidateAfter && result.success) await clearOwned();
        return result;
    });
    return { clear, start, check, withActive };
}
