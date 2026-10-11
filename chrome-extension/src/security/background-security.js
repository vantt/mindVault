import { createRotationWriter } from './rotation-writer.js';
import { createProfileRecordWriter } from './profile-record-writer.js';
import { createSessionGuard } from './session-guard.js';
import { createAuthCoordinator } from './auth-coordinator.js';
import { createSessionAuthority } from './session-authority.js';
import { callOffscreen, closeOffscreenIfIdle } from './offscreen-client.js';

const session = createSessionAuthority({ session: chrome.storage.session, alarms: chrome.alarms });
const auth = createAuthCoordinator({
    readState: async () => (await chrome.storage.local.get('authState')).authState,
    writeState: state => chrome.storage.local.set({ authState: state }),
    readVault: async () => {
        const stored = await chrome.storage.sync.get(['salt', 'defaultProfile', 'encryptedData', 'iv']);
        const profile = `profile:${stored.defaultProfile || 'Default'}`;
        const data = await chrome.storage.sync.get(profile);
        return { salt: stored.salt, record: data[profile] || { encryptedData: stored.encryptedData, iv: stored.iv } };
    },
    derive: values => callOffscreen('DERIVE_KEY', values),
    prepareVault: async password => {
        const salt = Array.from(crypto.getRandomValues(new Uint8Array(16)));
        return { ...await callOffscreen('DERIVE_KEY', { password, salt, create: true }), salt };
    },
    commitVault: ({ salt, record }) => chrome.storage.sync.set({ salt, 'profile:Default': record, defaultProfile: 'Default', sheetMapping: {} }),
    publish: session.start, clearSession: session.clear,
});
const rotateVault = createRotationWriter({ withActive: session.withActive, storage: chrome.storage.sync });
const saveProfile = createProfileRecordWriter({ withActive: session.withActive, storage: chrome.storage.sync });
const checkSession = createSessionGuard({ check: session.check, invalidate: lockCookbook });
const ready = auth.initialize().then(session.check).catch(() => session.clear());
export async function requireSession() {
    await ready;
    const state = await checkSession();
    if (!state.unlocked) {
        const error = new Error('Cookbook is locked'); error.code = 'VAULT_LOCKED'; throw error;
    }
    return state;
}
async function broadcastLock() {
    const tabs = await chrome.tabs.query({ url: 'https://docs.google.com/spreadsheets/*' });
    await Promise.allSettled(tabs.map(tab => chrome.tabs.sendMessage(tab.id, { action: 'VAULT_LOCKED' })));
}
export async function lockCookbook() {
    await auth.lock();
    await broadcastLock();
}
function trustedPage(sender) {
    return sender.id === chrome.runtime.id &&
        ['popup/popup.html', 'options/options.html'].some(path => sender.url === chrome.runtime.getURL(path));
}
chrome.runtime.onMessage.addListener((request, sender, reply) => {
    if (request.target === 'offscreen') return;
    if (request.action === 'OFFSCREEN_IDLE' && sender.id === chrome.runtime.id
        && sender.url === chrome.runtime.getURL('offscreen/offscreen.html')) {
        closeOffscreenIfIdle().catch(() => {});
        return;
    }
    const trusted = trustedPage(sender);
    let work;
    if (['AUTH_UNLOCK', 'AUTH_STATUS', 'AUTH_LOCK', 'AUTH_PUBLISH', 'AUTH_SETUP'].includes(request.action)) {
        if (!trusted) { reply({ success: false, code: 'FORBIDDEN' }); return; }
        if (request.action === 'AUTH_SETUP') work = ready.then(() => auth.setup(request.password));
        if (request.action === 'AUTH_UNLOCK') work = ready.then(() => auth.unlock(request.password));
        if (request.action === 'AUTH_STATUS') work = ready.then(async () => ({ success: true, ...await auth.status(), ...await checkSession() }));
        if (request.action === 'AUTH_LOCK') work = lockCookbook().then(() => ({ success: true }));
        if (request.action === 'AUTH_PUBLISH') {
            // Setup/rotation use the same authenticated derivation, never accept a caller's JWK.
            work = ready.then(() => auth.unlock(request.password));
        }
    } else if (request.action === 'AUTH_ROTATE') {
        if (!trusted || sender.url !== chrome.runtime.getURL('options/options.html')) { reply({ success: false, code: 'FORBIDDEN' }); return; }
        work = ready.then(async () => {
            const result = await rotateVault(request);
            if (result.success || result.expired) await lockCookbook();
            return result;
        });
    } else if (request.action === 'AUTH_SAVE_PROFILE') {
        if (!trusted || sender.url !== chrome.runtime.getURL('options/options.html')) { reply({ success: false, code: 'FORBIDDEN' }); return; }
        work = ready.then(async () => {
            const result = await saveProfile(request);
            if (result.expired) await lockCookbook();
            return result;
        });
    } else if (request.action === 'COPY_DISH') {
        const allowed = trusted || (sender.id === chrome.runtime.id && sender.tab?.url?.startsWith('https://docs.google.com/spreadsheets/'));
        if (!allowed || typeof request.text !== 'string' || !request.text || request.text.length > 65536) {
            reply({ success: false, code: 'FORBIDDEN' }); return;
        }
        work = requireSession().then(() => callOffscreen('COPY_DISH', { text: request.text }));
    }
    if (!work) return;
    work.then(reply, () => reply({ success: false, code: 'SECURITY_UNAVAILABLE' }));
    return true;
});
chrome.idle.setDetectionInterval(300);
chrome.idle.onStateChanged.addListener(state => {
    if (state === 'idle' || state === 'locked') lockCookbook().catch(() => {});
});
chrome.alarms.onAlarm.addListener(alarm => {
    if (alarm.name === 'sessionTimeout') session.check().then(state => {
        if (!state.unlocked) return lockCookbook();
    }).catch(() => lockCookbook());
});

chrome.storage.onChanged.addListener((changes, area) => {
    if (area === "session" && changes.sessionKey?.oldValue && !changes.sessionKey.newValue) {
        lockCookbook().catch(() => {});
    }
});
