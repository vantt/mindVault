import { encryptWithKey, decryptWithKey } from "../adapters/infrastructure/aes-storage-crypto-helper.js";

const PROFILE_COLORS = ['#58a6ff', '#e3b341', '#3fb950', '#a371f7', '#f78166'];

// ---------- Public API ----------

let _initialized = false;

export async function initProfilesTab(deps) {
    await renderAll(deps);
    if (_initialized) return;
    _initialized = true;
    document.getElementById('btn-new-profile').addEventListener('click', () => openNewProfileModal(deps));
    document.getElementById('btn-import-profile').addEventListener('click', () => deps.onImport(deps.sessionKey, () => renderAll(deps)));
    document.getElementById('btn-add-assignment').addEventListener('click', () => addSheetAssignment(deps));
    document.querySelectorAll('.modal-close[data-modal]').forEach(btn =>
        btn.addEventListener('click', () => closeModal(btn.dataset.modal))
    );
}

// ---------- Rendering ----------

async function renderAll(deps) {
    const stored = await chrome.storage.sync.get(null);
    const defaultName = stored.defaultProfile || "Default";
    const sheetMapping = stored.sheetMapping || {};

    const own = [], shared = [];
    for (const [k, v] of Object.entries(stored)) {
        if (k.startsWith("profile:")) own.push({ key: k, name: k.slice(8), data: v });
        else if (k.startsWith("shared:")) shared.push({ key: k, name: k.slice(7), data: v });
    }

    renderOwnProfiles(own, defaultName, sheetMapping, deps);
    renderSharedProfiles(shared, deps);
    renderSheetAssignments(sheetMapping, own, deps);
    renderDefaultSelector(own, defaultName, deps);
}

function renderOwnProfiles(profiles, defaultName, sheetMapping, deps) {
    const container = document.getElementById('own-profiles-list');
    container.innerHTML = '';

    if (!profiles.length) {
        container.innerHTML = '<p class="text-muted">No profiles yet. Create one above.</p>';
        return;
    }

    profiles.forEach(({ key, name, data }, idx) => {
        const isDefault = name === defaultName;
        const sheetCount = Object.values(sheetMapping).filter(n => n === name).length;
        const color = PROFILE_COLORS[idx % PROFILE_COLORS.length];

        const card = document.createElement('div');
        card.className = 'profile-card';
        card.style.setProperty('--profile-color', color);
        card.innerHTML = `
            <div class="profile-header">
                <div class="profile-header-left">
                    <span class="profile-dot" style="background:${color}"></span>
                    <span class="profile-name">${escHtml(name)}</span>
                </div>
                ${isDefault ? '<span class="profile-badge">⭐ default</span>' : ''}
            </div>
            <div class="profile-meta">${sheetCount > 0 ? `${sheetCount} ${chrome.i18n.getMessage('lblUsedBySheets') || 'sheets'}` : 'No sheet assignments'}</div>
            <div class="profile-actions">
                <button class="btn sm secondary btn-edit-secrets">✏️ ${chrome.i18n.getMessage('btnEditSecrets') || 'Edit Secrets'}</button>
                <button class="btn sm secondary btn-export">↑ ${chrome.i18n.getMessage('btnExportProfile') || 'Export'}</button>
                ${!isDefault ? `<button class="btn sm secondary btn-set-default">★ ${chrome.i18n.getMessage('btnSetDefault') || 'Set Default'}</button>` : ''}
                ${!isDefault ? `<button class="btn sm danger btn-delete">${chrome.i18n.getMessage('btnRemoveProfile') || 'Remove'}</button>` : ''}
                <span class="warn-text hidden" data-key="${key}"></span>
            </div>`;
        container.appendChild(card);

        card.querySelector('.btn-edit-secrets').onclick = () => openEditSecretsModal(key, name, deps);
        card.querySelector('.btn-export').onclick = () => deps.onExport(name, key, deps.sessionKey, () => {});
        if (!isDefault) {
            card.querySelector('.btn-set-default').onclick = () => setDefaultProfile(name, deps);
            card.querySelector('.btn-delete').onclick = () => deleteProfile(key, name, sheetCount, card, deps);
        }
    });
}

function renderSharedProfiles(profiles, deps) {
    const container = document.getElementById('shared-profiles-list');
    container.innerHTML = '';

    if (!profiles.length) {
        container.innerHTML = '<p class="text-muted">No imported profiles.</p>';
        return;
    }

    profiles.forEach(({ key, name, data }) => {
        const sheetDisplay = data.sheetId ? data.sheetId.slice(0, 14) + '...' : '—';
        const meta = data.meta || {};
        const card = document.createElement('div');
        card.className = 'profile-card shared';
        card.innerHTML = `
            <div class="profile-header">
                <div class="profile-header-left">
                    <span>📥</span>
                    <span class="profile-name">${escHtml(name)}</span>
                </div>
                <span class="profile-badge">🔒 read-only</span>
            </div>
            <div class="profile-meta">
                ${meta.importedFrom ? `${chrome.i18n.getMessage('lblImportedFrom') || 'Imported from'}: ${escHtml(meta.importedFrom)} · ` : ''}
                ${meta.importedAt ? `Imported: ${meta.importedAt.slice(0,10)}` : ''}
            </div>
            <div class="profile-meta monospace">${chrome.i18n.getMessage('lblSheetLocked') || 'Locked to sheet'}: ${sheetDisplay}</div>
            <div class="profile-actions">
                <button class="btn sm danger btn-remove-shared">Remove</button>
            </div>`;
        container.appendChild(card);
        card.querySelector('.btn-remove-shared').onclick = () => removeSharedProfile(key, name, deps);
    });
}

function renderSheetAssignments(sheetMapping, ownProfiles, deps) {
    const container = document.getElementById('sheet-assignments-list');
    container.innerHTML = '';

    for (const [sheetId, profileName] of Object.entries(sheetMapping)) {
        const row = document.createElement('div');
        row.className = 'sheet-row';
        const opts = ownProfiles.map(p =>
            `<option value="${escHtml(p.name)}" ${p.name === profileName ? 'selected' : ''}>${escHtml(p.name)}</option>`
        ).join('');
        row.innerHTML = `
            <span class="sheet-id" title="${escHtml(sheetId)}">${sheetId.length > 20 ? sheetId.slice(0, 20) + '...' : sheetId}</span>
            <select class="profile-select">${opts}</select>
            <button class="btn sm danger btn-rm-sheet">✕</button>`;
        container.appendChild(row);

        row.querySelector('select').onchange = async (e) => {
            const mapping = (await chrome.storage.sync.get("sheetMapping")).sheetMapping || {};
            mapping[sheetId] = e.target.value;
            await chrome.storage.sync.set({ sheetMapping: mapping });
        };
        row.querySelector('.btn-rm-sheet').onclick = () => removeSheetAssignment(sheetId, deps);
    }
}

function renderDefaultSelector(ownProfiles, defaultName, deps) {
    const sel = document.getElementById('select-default-profile');
    if (!sel) return;
    sel.innerHTML = ownProfiles.map(p =>
        `<option value="${escHtml(p.name)}" ${p.name === defaultName ? 'selected' : ''}>${escHtml(p.name)}</option>`
    ).join('');
    sel.onchange = async (e) => {
        await chrome.storage.sync.set({ defaultProfile: e.target.value });
        deps.showToast("Default profile updated");
    };
}

// ---------- New Profile Modal ----------

function openNewProfileModal(deps) {
    document.getElementById('new-profile-name').value = '';
    openModal('modal-new-profile');

    const btn = document.getElementById('btn-create-profile');
    btn.onclick = async () => {
        const name = document.getElementById('new-profile-name').value.trim();
        if (!name) return;
        await createProfile(name, deps);
        closeModal('modal-new-profile');
    };
}

// ---------- Edit Secrets Modal ----------

function openEditSecretsModal(profileKey, profileName, deps) {
    document.querySelector('#edit-secrets-title .modal-title-em').textContent = profileName;

    // Reset inputs to password type and clear values
    const inputs = document.querySelectorAll('#edit-secrets-inputs .modal-secret-input');
    inputs.forEach(inp => {
        inp.value = '';
        inp.type = 'password';
    });
    document.getElementById('modal-peppering-hint').checked = false;

    // Wire SVG eye toggles (idempotent — clone to strip old listeners)
    document.querySelectorAll('#edit-secrets-inputs .modal-btn-eye').forEach(btn => {
        const fresh = btn.cloneNode(true);
        btn.parentNode.replaceChild(fresh, btn);
        fresh.addEventListener('click', () => {
            const inp = fresh.closest('.input-wrapper').querySelector('.modal-secret-input');
            inp.type = inp.type === 'password' ? 'text' : 'password';
            // Swap the path to crossed-out eye when revealed
            const paths = fresh.querySelectorAll('path, circle');
            fresh.style.opacity = inp.type === 'text' ? '0.5' : '1';
        });
    });

    // Load existing secrets and settings
    chrome.storage.sync.get(profileKey).then(async stored => {
        const data = stored[profileKey];
        if (!data) return;
        try {
            const decrypted = await decryptWithKey(data.encryptedData, data.iv, deps.sessionKey);
            document.querySelectorAll('#edit-secrets-inputs .modal-secret-input').forEach(inp => {
                inp.value = decrypted.secrets?.[inp.dataset.index]?.base || '';
            });
            if (decrypted.settings?.pepperingHint) {
                document.getElementById('modal-peppering-hint').checked = true;
            }
        } catch (e) { console.error("Failed to load secrets for edit:", e); }
    });

    openModal('modal-edit-secrets');

    const saveBtn = document.getElementById('btn-save-modal-secrets');
    // Clone to strip any previous onclick
    const freshSave = saveBtn.cloneNode(true);
    saveBtn.parentNode.replaceChild(freshSave, saveBtn);
    freshSave.addEventListener('click', async () => {
        const data = { secrets: {}, settings: {} };
        document.querySelectorAll('#edit-secrets-inputs .modal-secret-input').forEach(inp => {
            data.secrets[inp.dataset.index] = { base: inp.value };
        });
        data.settings.pepperingHint = document.getElementById('modal-peppering-hint').checked;
        try {
            const encrypted = await encryptWithKey(data, deps.sessionKey);
            await chrome.storage.sync.set({ [profileKey]: encrypted });
            deps.showToast("Secrets saved");
            closeModal('modal-edit-secrets');
        } catch (e) {
            deps.showToast("Save failed: " + e.message, true);
        }
    });
}

// ---------- CRUD ----------

async function createProfile(name, deps) {
    const profileKey = `profile:${name}`;
    const initial = { secrets: { "1": { base: "" }, "2": { base: "" }, "3": { base: "" }, "4": { base: "" }, "5": { base: "" } }, settings: {} };
    const encrypted = await encryptWithKey(initial, deps.sessionKey);
    await chrome.storage.sync.set({ [profileKey]: encrypted });
    deps.showToast(`Profile "${name}" created`);
    await renderAll(deps);
    // Open edit secrets right away
    openEditSecretsModal(profileKey, name, deps);
}

async function deleteProfile(key, name, sheetCount, card, deps) {
    if (sheetCount > 0) {
        const warn = card.querySelector('.warn-text');
        warn.textContent = chrome.i18n.getMessage('errProfileAssigned') || 'Assigned to N sheet(s). Reassign before deleting.';
        warn.classList.remove('hidden');
        return;
    }
    if (!confirm(`Delete profile "${name}"? This cannot be undone.`)) return;
    await chrome.storage.sync.remove(key);
    deps.showToast(`Profile "${name}" deleted`);
    await renderAll(deps);
}

async function setDefaultProfile(name, deps) {
    await chrome.storage.sync.set({ defaultProfile: name });
    deps.showToast(`"${name}" is now the default profile`);
    await renderAll(deps);
}

async function removeSharedProfile(key, name, deps) {
    if (!confirm(`Remove imported profile "${name}"?`)) return;
    await chrome.storage.sync.remove(key);
    deps.showToast(`Profile "${name}" removed`);
    await renderAll(deps);
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
    await renderAll(deps);
}

async function removeSheetAssignment(sheetId, deps) {
    const stored = await chrome.storage.sync.get("sheetMapping");
    const mapping = stored.sheetMapping || {};
    delete mapping[sheetId];
    await chrome.storage.sync.set({ sheetMapping: mapping });
    await renderAll(deps);
}

// ---------- Helpers ----------

function extractSheetId(input) {
    const urlMatch = input.match(/\/spreadsheets\/d\/([a-zA-Z0-9_-]+)/);
    if (urlMatch) return urlMatch[1];
    if (/^[a-zA-Z0-9_-]{20,}$/.test(input)) return input;
    return null;
}

function escHtml(str) {
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}

function openModal(id) { document.getElementById(id)?.classList.remove('hidden'); }
function closeModal(id) { document.getElementById(id)?.classList.add('hidden'); }
