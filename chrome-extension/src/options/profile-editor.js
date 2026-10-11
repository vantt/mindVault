import { encryptWithKey, decryptWithKey } from '../adapters/infrastructure/aes-storage-crypto-helper.js';
import { readProfileSession, ownsProfileSession, commitProfile } from './profile-session.js';
import { securityMessage } from '../security/page-security.js';

// A modal and an authenticated session must both still own every async result.
export function createProfileEditor({ getDeps }) {
    const modal = document.getElementById('modal-edit-secrets');
    const inputs = [...modal.querySelectorAll('.modal-secret-input')];
    const hint = document.getElementById('modal-peppering-hint');
    const save = document.getElementById('btn-save-modal-secrets');
    let current = null;
    function clear() {
        inputs.forEach(input => { input.value = ''; input.type = 'password'; input.disabled = true; });
        hint.checked = false; hint.disabled = true; save.disabled = true;
    }
    function close() {
        current = null; clear(); modal.classList.add('hidden');
    }
    const active = work => current === work && !modal.classList.contains('hidden');
    async function open(profileKey, profileName) {
        close();
        const work = { profileKey, owner: null, ready: false };
        current = work;
        document.querySelector('#edit-secrets-title .modal-title-em').textContent = profileName;
        modal.classList.remove('hidden');
        try {
            const [owner, stored] = await Promise.all([readProfileSession(), chrome.storage.sync.get(profileKey)]);
            if (!active(work) || !owner) { if (active(work)) close(); return; }
            work.owner = owner;
            const record = stored[profileKey];
            if (!record) { close(); return; }
            const data = await decryptWithKey(record.encryptedData, record.iv, owner.key);
            if (!active(work)) return;
            if (!await ownsProfileSession(owner)) { if (active(work)) close(); return; }
            if (!active(work)) return;
            inputs.forEach(input => { input.value = data.secrets?.[input.dataset.index]?.base || ''; input.disabled = false; });
            hint.checked = !!data.settings?.pepperingHint; hint.disabled = false;
            work.ready = true; save.disabled = false;
        } catch {
            if (active(work)) { close(); getDeps().showToast(securityMessage({ code: 'INVALID_DATA' }), true); }
        }
    }
    save.addEventListener('click', async () => {
        const work = current;
        if (!work?.ready || !active(work) || save.disabled) return;
        save.disabled = true;
        const data = { secrets: {}, settings: { pepperingHint: hint.checked } };
        inputs.forEach(input => { data.secrets[input.dataset.index] = { base: input.value }; });
        try {
            if (!await ownsProfileSession(work.owner)) { if (active(work)) close(); return; }
            if (!active(work)) return;
            const record = await encryptWithKey(data, work.owner.key);
            if (!active(work)) return;
            // Background serializes this nonce check/write with lock and rotation.
            const result = await commitProfile(work.owner, work.profileKey, record);
            if (!active(work)) return;
            if (result.success) { close(); getDeps().showToast('Secrets saved'); }
            else { close(); getDeps().showToast(securityMessage(result), true); }
        } catch {
            if (active(work)) { close(); getDeps().showToast(securityMessage({ code: 'INVALID_DATA' }), true); }
        } finally {
            if (active(work)) save.disabled = false;
            Object.values(data.secrets).forEach(spice => { spice.base = ''; });
        }
    });
    modal.querySelectorAll('[data-modal="modal-edit-secrets"]').forEach(button => button.addEventListener('click', close));
    modal.querySelectorAll('.modal-btn-eye').forEach(button => button.addEventListener('click', () => {
        if (!current?.ready) return;
        const input = button.closest('.input-wrapper').querySelector('.modal-secret-input');
        input.type = input.type === 'password' ? 'text' : 'password';
        button.style.opacity = input.type === 'text' ? '0.5' : '1';
    }));
    chrome.storage.onChanged.addListener((changes, area) => {
        if (area === 'session' && (changes.sessionKey || changes.sessionNonce)) close();
    });
    window.addEventListener('pagehide', close);
    return { open, close };
}
