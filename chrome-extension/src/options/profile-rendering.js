import { t } from '../i18n-loader.js';

export function renderSheetAssignments(sheetMapping, ownProfiles, deps, actions) {
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
        row.querySelector('.btn-rm-sheet').onclick = () => actions.removeSheetAssignment(sheetId, deps);
    }
}

export function renderDefaultSelector(ownProfiles, defaultName, deps) {
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

export function escHtml(str) {
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}
