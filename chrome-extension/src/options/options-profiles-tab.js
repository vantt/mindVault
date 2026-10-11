import { createProfileEditor } from './profile-editor.js';
import { createProfileManagement } from './profile-management.js';
import { renderSheetAssignments, renderDefaultSelector, escHtml } from './profile-rendering.js';
import { t } from "../i18n-loader.js";

const PROFILE_COLORS = ['#58a6ff', '#e3b341', '#3fb950', '#a371f7', '#f78166'];

// ---------- Public API ----------

let _initialized = false, currentDeps, editor, actions;

export async function initProfilesTab(deps) {
    currentDeps = deps;
    if (!editor) {
        editor = createProfileEditor({ getDeps: () => currentDeps });
        actions = createProfileManagement({ getDeps: () => currentDeps, renderAll, openEdit: editor.open });
    }
    await renderAll(deps);
    if (_initialized) return;
    _initialized = true;
    document.getElementById('btn-new-profile').addEventListener('click', () => actions.openNewProfileModal(currentDeps));
    document.getElementById('btn-import-profile').addEventListener('click', () => currentDeps.onImport(currentDeps.sessionKey, () => renderAll(currentDeps)));
    document.getElementById('btn-add-assignment').addEventListener('click', () => actions.addSheetAssignment(currentDeps));
    document.querySelectorAll('.modal-close[data-modal]').forEach(btn =>
        btn.addEventListener('click', () => document.getElementById(btn.dataset.modal)?.classList.add('hidden'))
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
    renderSheetAssignments(sheetMapping, own, deps, actions);
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
            <div class="profile-meta">${sheetCount > 0 ? `${sheetCount} ${t('lblUsedBySheets') || 'sheets'}` : 'No sheet assignments'}</div>
            <div class="profile-actions">
                <button class="btn sm secondary btn-edit-secrets">✏️ ${t('btnEditSecrets') || 'Edit Secrets'}</button>
                <button class="btn sm secondary btn-export">↑ ${t('btnExportProfile') || 'Export'}</button>
                <button class="btn sm secondary btn-connect-phone">📱 ${t('qrConnectPhone') || 'Connect phone app'}</button>
                ${!isDefault ? `<button class="btn sm secondary btn-set-default">★ ${t('btnSetDefault') || 'Set Default'}</button>` : ''}
                ${!isDefault ? `<button class="btn sm danger btn-delete">${t('btnRemoveProfile') || 'Remove'}</button>` : ''}
                <span class="warn-text hidden" data-key="${key}"></span>
            </div>`;
        container.appendChild(card);

        card.querySelector('.btn-edit-secrets').onclick = () => editor.open(key, name);
        card.querySelector('.btn-connect-phone').onclick = () => deps.onExport(name, key, deps.sessionKey, () => {}, null, true);
        card.querySelector('.btn-export').onclick = () => deps.onExport(name, key, deps.sessionKey, () => {});
        if (!isDefault) {
            card.querySelector('.btn-set-default').onclick = () => actions.setDefaultProfile(name, deps);
            card.querySelector('.btn-delete').onclick = () => actions.deleteProfile(key, name, sheetCount, card, deps);
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
                ${meta.importedFrom ? `${t('lblImportedFrom') || 'Imported from'}: ${escHtml(meta.importedFrom)} · ` : ''}
                ${meta.importedAt ? `Imported: ${meta.importedAt.slice(0,10)}` : ''}
            </div>
            <div class="profile-meta monospace">${t('lblSheetLocked') || 'Locked to sheet'}: ${sheetDisplay}</div>
            <div class="profile-actions">
                <button class="btn sm danger btn-remove-shared">Remove</button>
            </div>`;
        container.appendChild(card);
        card.querySelector('.btn-remove-shared').onclick = () => actions.removeSharedProfile(key, name, deps);
    });
}
