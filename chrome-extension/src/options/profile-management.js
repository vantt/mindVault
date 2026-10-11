import { encryptWithKey } from '../adapters/infrastructure/aes-storage-crypto-helper.js';
import { readProfileSession, commitProfile } from './profile-session.js';
import { securityMessage } from '../security/page-security.js';
import { t } from '../i18n-loader.js';

export function createProfileManagement({ getDeps, renderAll, openEdit }) {
    let generation = 0;
    const cancel = () => { generation++; closeModal('modal-new-profile'); };
    document.querySelectorAll('[data-modal="modal-new-profile"]').forEach(button => button.addEventListener('click', cancel));
    chrome.storage.onChanged.addListener((changes, area) => {
        if (area === 'session' && (changes.sessionKey || changes.sessionNonce)) cancel();
    });
    window.addEventListener('pagehide', cancel);
function openNewProfileModal(deps) {
    const token = ++generation;
    document.getElementById('new-profile-name').value = '';
    openModal('modal-new-profile');

    const btn = document.getElementById('btn-create-profile');
    btn.onclick = async () => {
        const name = document.getElementById('new-profile-name').value.trim();
        if (!name) return;
        const current = () => token === generation && !document.getElementById('modal-new-profile').classList.contains('hidden');
        await createProfile(name, getDeps(), current);
        if (!current()) return;
        closeModal('modal-new-profile');
    };
}

async function createProfile(name, deps, current) {
    const owner = await readProfileSession();
    if (!owner || !current()) return;
    const profileKey = `profile:${name}`;
    const initial = { secrets: Object.fromEntries(['1', '2', '3', '4', '5'].map(index => [index, { base: '' }])), settings: {} };
    try {
        const record = await encryptWithKey(initial, owner.key);
        if (!current()) return;
        const result = await commitProfile(owner, profileKey, record, true);
        if (!current()) return;
        if (!result.success) { deps.showToast(securityMessage(result), true); return; }
        deps.showToast(`Profile "${name}" created`);
        await renderAll(getDeps());
        if (current()) openEdit(profileKey, name);
    } catch { if (current()) deps.showToast(securityMessage({ code: 'INVALID_DATA' }), true); }
}
async function deleteProfile(key, name, sheetCount, card, deps) {
    if (sheetCount > 0) {
        const warn = card.querySelector('.warn-text');
        warn.textContent = t('errProfileAssigned') || 'Assigned to N sheet(s). Reassign before deleting.';
        warn.classList.remove('hidden');
        return;
    }
    if (!confirm(`Delete profile "${name}"? This cannot be undone.`)) return;
    await chrome.storage.sync.remove(key);
    deps.showToast(`Profile "${name}" deleted`);
    await renderAll(getDeps());
}

async function setDefaultProfile(name, deps) {
    await chrome.storage.sync.set({ defaultProfile: name });
    deps.showToast(`"${name}" is now the default profile`);
    await renderAll(getDeps());
}

async function removeSharedProfile(key, name, deps) {
    if (!confirm(`Remove imported profile "${name}"?`)) return;
    await chrome.storage.sync.remove(key);
    deps.showToast(`Profile "${name}" removed`);
    await renderAll(getDeps());
}

async function addSheetAssignment(deps) {
    const sheetId = prompt("Enter Google Sheet ID or URL:");
    if (!sheetId) return;
    const extracted = extractSheetId(sheetId.trim());
    if (!extracted) { deps.showToast("Invalid Sheet ID or URL", true); return; }
    const stored = await chrome.storage.sync.get(["sheetMapping", "defaultProfile"]);
    const mapping = stored.sheetMapping || {};
    mapping[extracted] = stored.defaultProfile || "Default";
    await chrome.storage.sync.set({ sheetMapping: mapping });
    await renderAll(getDeps());
}

async function removeSheetAssignment(sheetId, deps) {
    const stored = await chrome.storage.sync.get("sheetMapping");
    const mapping = stored.sheetMapping || {};
    delete mapping[sheetId];
    await chrome.storage.sync.set({ sheetMapping: mapping });
    await renderAll(getDeps());
}

function extractSheetId(input) {
    const urlMatch = input.match(/\/spreadsheets\/d\/([a-zA-Z0-9_-]+)/);
    if (urlMatch) return urlMatch[1];
    if (/^[a-zA-Z0-9_-]{20,}$/.test(input)) return input;
    return null;
}

function openModal(id) { document.getElementById(id)?.classList.remove('hidden'); }
function closeModal(id) { document.getElementById(id)?.classList.add('hidden'); }

    return { openNewProfileModal, deleteProfile, setDefaultProfile, removeSharedProfile, addSheetAssignment, removeSheetAssignment };
}
