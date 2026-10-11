import { readFileSync } from 'node:fs';
import { beforeEach, afterEach, it, expect, vi } from 'vitest';
const cryptoMocks = vi.hoisted(() => ({ encrypt: vi.fn(), decrypt: vi.fn() }));
vi.mock('../../src/adapters/infrastructure/aes-storage-crypto-helper.js', () => ({
    encryptWithKey: cryptoMocks.encrypt, decryptWithKey: cryptoMocks.decrypt,
}));
vi.mock('../../src/i18n-loader.js', () => ({ t: key => key }));
import { createProfileEditor } from '../../src/options/profile-editor.js';

const html = readFileSync(`${process.cwd()}/src/options/options.html`, 'utf8');
const data = name => ({ secrets: { '1': { base: name } }, settings: { pepperingHint: true } });
function deferred() { let resolve; const promise = new Promise(r => { resolve = r; }); return { promise, resolve }; }
const el = id => document.getElementById(id);
const spice = () => document.querySelector('.modal-secret-input[data-index="1"]');
let editor, listener, session, send, toast;
const record = { encryptedData: [1, 2, 3], iv: [4] };
beforeEach(() => {
    vi.clearAllMocks(); document.body.innerHTML = html;
    session = { unlocked: true, nonce: 'freshNonce', key: { k: 'syntheticFreshKey' } };
    toast = vi.fn(); cryptoMocks.decrypt.mockResolvedValue(data('CurrentSpice')); cryptoMocks.encrypt.mockResolvedValue(record);
    send = vi.fn(async request => request.action === 'AUTH_STATUS' ? session : { success: true });
    vi.stubGlobal('chrome', {
        runtime: { sendMessage: send },
        storage: {
            session: { get: async () => ({ sessionKey: session.key, sessionNonce: session.nonce }) },
            sync: { get: async key => ({ [key]: record }), set: vi.fn() },
            onChanged: { addListener: callback => { listener = callback; } },
        },
    });
    editor = createProfileEditor({ getDeps: () => ({ sessionKey: { k: 'retiredKey' }, showToast: toast }) });
});
afterEach(() => { editor.close(); vi.unstubAllGlobals(); document.body.replaceChildren(); });
function lock() {
    session = { unlocked: false }; listener({ sessionKey: { oldValue: {}, newValue: undefined } }, 'session');
}
const writes = () => send.mock.calls.filter(([request]) => request.action === 'AUTH_SAVE_PROFILE');
async function startPending() {
    const pending = deferred(); cryptoMocks.decrypt.mockReturnValue(pending.promise);
    const opened = editor.open('profile:Default', 'Default');
    await vi.waitFor(() => expect(cryptoMocks.decrypt).toHaveBeenCalled());
    return { pending, opened };
}
for (const exit of ['lock', 'close', 'cancel', 'pagehide']) {
    it(`late decryption after ${exit} cannot repopulate spice fields`, async () => {
        const { pending, opened } = await startPending();
        expect(spice().disabled).toBe(true); expect(el('btn-save-modal-secrets').disabled).toBe(true);
        if (exit === 'lock') lock();
        if (exit === 'close') document.querySelector('#modal-edit-secrets .modal-close').click();
        if (exit === 'cancel') document.querySelector('#modal-edit-secrets .btn.ghost').click();
        if (exit === 'pagehide') window.dispatchEvent(new Event('pagehide'));
        pending.resolve(data('LateSpice')); await opened;
        expect(spice().value).toBe(''); expect(el('modal-edit-secrets').classList.contains('hidden')).toBe(true);
        expect(el('btn-save-modal-secrets').disabled).toBe(true); expect(writes()).toHaveLength(0);
    });
}
it('old profile decryption cannot replace a newly opened profile', async () => {
    const { pending, opened } = await startPending();
    cryptoMocks.decrypt.mockResolvedValueOnce(data('NewProfileSpice'));
    await editor.open('profile:Other', 'Other'); pending.resolve(data('OldProfileSpice')); await opened;
    expect(spice().value).toBe('NewProfileSpice');
    expect(document.querySelector('#edit-secrets-title .modal-title-em').textContent).toBe('Other');
});
it('a rotated nonce without an earlier UI event still prevents late decrypted publication', async () => {
    const { pending, opened } = await startPending(); session = { unlocked: true, nonce: 'rotated', key: { k: 'new' } };
    pending.resolve(data('OldSpice')); await opened;
    expect(spice().value).toBe(''); expect(el('modal-edit-secrets').classList.contains('hidden')).toBe(true);
});
for (const exit of ['lock', 'close', 'reopen']) {
    it(`an encrypted save result after ${exit} cannot dispatch a ciphertext write`, async () => {
        await editor.open('profile:Default', 'Default'); const pending = deferred(); cryptoMocks.encrypt.mockReturnValue(pending.promise);
        spice().value = 'EditedSpice'; el('btn-save-modal-secrets').click();
        await vi.waitFor(() => expect(cryptoMocks.encrypt).toHaveBeenCalled());
        if (exit === 'lock') lock();
        if (exit === 'close') editor.close();
        if (exit === 'reopen') await editor.open('profile:Other', 'Other');
        pending.resolve(record); await Promise.resolve(); await Promise.resolve();
        expect(writes()).toHaveLength(0); expect(chrome.storage.sync.set).not.toHaveBeenCalled();
    });
}
it('save uses the actual current key and authoritative nonce commit, ignoring retired dependency keys', async () => {
    await editor.open('profile:Default', 'Default'); spice().value = 'EditedSpice'; el('btn-save-modal-secrets').click();
    await vi.waitFor(() => expect(writes()).toHaveLength(1));
    expect(cryptoMocks.encrypt.mock.calls[0][1]).toEqual({ k: 'syntheticFreshKey' });
    expect(writes()[0][0]).toEqual({ action: 'AUTH_SAVE_PROFILE', nonce: 'freshNonce', profileKey: 'profile:Default', record, createOnly: false });
    expect(chrome.storage.sync.set).not.toHaveBeenCalled();
    expect(spice().value).toBe(''); expect(toast).toHaveBeenCalledWith('Secrets saved');
});
it('the authority rejecting a retired save never shows Saved and closes the modal', async () => {
    await editor.open('profile:Default', 'Default');
    send.mockImplementation(async request => request.action === 'AUTH_STATUS' ? session : { success: false, code: 'VAULT_LOCKED' });
    el('btn-save-modal-secrets').click(); await vi.waitFor(() => expect(toast).toHaveBeenCalled());
    expect(toast).not.toHaveBeenCalledWith('Secrets saved'); expect(spice().value).toBe('');
    expect(chrome.storage.sync.set).not.toHaveBeenCalled();
});
it('the extracted Profiles tab wires its real card to the guarded editor', async () => {
    chrome.storage.sync.get = async key => key === null ? { 'profile:Default': record } : { [key]: record };
    const { initProfilesTab } = await import('../../src/options/options-profiles-tab.js');
    await initProfilesTab({ sessionKey: { k: 'retiredKey' }, showToast: toast });
    document.querySelector('.btn-edit-secrets').click();
    await vi.waitFor(() => expect(spice().value).toBe('CurrentSpice'));
    expect(el('btn-save-modal-secrets').disabled).toBe(false);
});
