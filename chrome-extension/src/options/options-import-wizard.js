import { ExportImportAdapter } from "../adapters/infrastructure/export-import-adapter.js";
import { encryptWithKey } from "../adapters/infrastructure/aes-storage-crypto-helper.js";

const adapter = new ExportImportAdapter();

/** @type {{ sessionKey: object, onComplete: function, step: number, bundle: object, derivedSecrets: object }} */
let state = {};

export function openImportWizard(sessionKey, onComplete) {
    state = { sessionKey, onComplete, step: 1 };
    document.getElementById('btn-close-import').onclick = () => confirmClose();
    renderStep(1);
    document.getElementById('modal-import').classList.remove('hidden');
}

function renderStep(step) {
    state.step = step;
    updateDots(step);

    const content = document.getElementById('import-step-content');
    const footer = document.getElementById('import-modal-footer');

    if (step === 1) {
        content.innerHTML = `
            <p class="wizard-section-title">📥 Import Shared Profile</p>
            <p class="wizard-description">Provide the bundle JSON from the sender. You can paste the text or upload a .json file.</p>
            <div class="form-group">
                <label>Bundle JSON</label>
                <textarea class="bundle-textarea" id="imp-bundle-input" placeholder="Paste bundle JSON here..."></textarea>
            </div>
            <div style="display:flex;gap:.5rem;margin-bottom:.75rem">
                <label class="btn sm secondary" style="cursor:pointer;width:auto">
                    ⬆️ Upload .json
                    <input type="file" accept=".json" id="imp-file-input" style="display:none" />
                </label>
            </div>
            <div id="imp-bundle-status"></div>`;
        footer.innerHTML = `<span></span><div class="right"><button id="imp-next-1" class="btn sm primary" style="width:auto">Continue →</button></div>`;

        document.getElementById('imp-file-input').onchange = (e) => {
            const file = e.target.files[0];
            if (!file) return;
            const reader = new FileReader();
            reader.onload = (ev) => {
                document.getElementById('imp-bundle-input').value = ev.target.result;
                validateBundleInput();
            };
            reader.readAsText(file);
        };

        document.getElementById('imp-bundle-input').oninput = validateBundleInput;

        document.getElementById('imp-next-1').onclick = () => {
            if (!state.bundle) { validateBundleInput(); return; }
            const validation = adapter.validateBundle(state.bundle);
            if (!validation.valid) {
                showStatus('invalid', validation.error);
                return;
            }
            renderStep(2);
        };

    } else if (step === 2) {
        const sheetId = state.bundle?.sheetId || '';
        const sheetPreview = sheetId ? (sheetId.length > 20 ? sheetId.slice(0, 20) + '...' : sheetId) : '—';
        content.innerHTML = `
            <p class="wizard-section-title">🔑 Enter Sharing Password</p>
            <p class="wizard-description">Password given by the sender.</p>
            <div class="form-group">
                <label>Sharing Password</label>
                <div class="input-wrapper">
                    <input type="password" id="imp-sharing-pwd" />
                    <button type="button" class="btn-toggle-visibility">👁</button>
                </div>
            </div>
            <div id="imp-decrypt-error"></div>
            <div class="bundle-status valid" style="margin-top:1rem">
                ✓ Bundle: <strong>${escHtml(state.bundle.profileName || '—')}</strong>
                &nbsp;·&nbsp; Sheet: <span class="monospace">${escHtml(sheetPreview)}</span>
                &nbsp;·&nbsp; Exported: ${(state.bundle.exportedAt || '').slice(0, 10)}
            </div>`;
        footer.innerHTML = `<button id="imp-back-2" class="btn sm secondary">← Back</button><div class="right"><button id="imp-decrypt" class="btn sm primary" style="width:auto">Decrypt →</button></div>`;

        content.querySelector('.btn-toggle-visibility').onclick = (e) => {
            const inp = document.getElementById('imp-sharing-pwd');
            inp.type = inp.type === 'password' ? 'text' : 'password';
            e.target.textContent = inp.type === 'password' ? '👁' : '🙈';
        };
        document.getElementById('imp-back-2').onclick = () => renderStep(1);
        document.getElementById('imp-decrypt').onclick = () => decryptBundle();

    } else if (step === 3) {
        const sheetFull = state.bundle?.sheetId || '—';
        const suggestedName = state.bundle?.profileName || 'ImportedProfile';
        content.innerHTML = `
            <p class="wizard-section-title">✏️ Name This Profile</p>
            <p class="wizard-description">Local name — only visible to you.</p>
            <div class="form-group">
                <label>Profile Name</label>
                <input type="text" id="imp-profile-name" value="${escHtml(suggestedName)}" maxlength="50" />
            </div>
            <div class="bundle-status valid">
                Locked to sheet: <span class="monospace">${escHtml(sheetFull.length > 30 ? sheetFull.slice(0, 30) + '...' : sheetFull)}</span><br>
                <span class="text-muted" style="font-size:.8rem">This profile will only work on that sheet. No manual assignment needed.</span>
            </div>`;
        footer.innerHTML = `<button id="imp-back-3" class="btn sm secondary">← Back</button><div class="right"><button id="imp-confirm" class="btn sm primary" style="width:auto">Import</button></div>`;

        document.getElementById('imp-back-3').onclick = () => renderStep(2);
        document.getElementById('imp-confirm').onclick = () => confirmImport();
    }
}

function validateBundleInput() {
    const raw = document.getElementById('imp-bundle-input')?.value.trim();
    if (!raw) { state.bundle = null; showStatus('', ''); return; }
    try {
        const parsed = JSON.parse(raw);
        const result = adapter.validateBundle(parsed);
        if (result.valid) {
            state.bundle = parsed;
            const sid = result.sheetId || '';
            showStatus('valid', `✓ Valid v2.1 bundle · Profile: ${result.profileName} · Sheet: ${sid.length > 14 ? sid.slice(0, 14) + '...' : sid}`);
        } else {
            state.bundle = null;
            showStatus('invalid', result.error);
        }
    } catch {
        state.bundle = null;
        showStatus('invalid', 'Not valid JSON');
    }
}

function showStatus(type, msg) {
    const el = document.getElementById('imp-bundle-status');
    if (!el || !msg) { if (el) el.innerHTML = ''; return; }
    el.innerHTML = `<div class="bundle-status ${type}">${escHtml(msg)}</div>`;
}

async function decryptBundle() {
    const pwd = document.getElementById('imp-sharing-pwd')?.value;
    const errEl = document.getElementById('imp-decrypt-error');
    if (!pwd) { errEl.innerHTML = '<p class="text-danger">Password is required.</p>'; return; }

    const btn = document.getElementById('imp-decrypt');
    btn.textContent = 'Decrypting…';
    btn.disabled = true;

    try {
        const secrets = await adapter.decryptBundle(state.bundle, pwd);
        // Validate structure: keys 1-5, values {base: string} max 500 chars
        const valid = secrets && typeof secrets === 'object' &&
            Object.entries(secrets).every(([k, v]) => /^[1-5]$/.test(k) && typeof v?.base === 'string' && v.base.length <= 500);
        if (!valid) throw new Error("Bundle contains invalid secret structure");
        state.derivedSecrets = secrets;
        renderStep(3);
    } catch (e) {
        errEl.innerHTML = `<p class="text-danger">${escHtml(e.message)}</p>`;
        btn.textContent = 'Decrypt →';
        btn.disabled = false;
    }
}

async function confirmImport() {
    const localName = document.getElementById('imp-profile-name')?.value.trim();
    if (!localName) { alert('Profile name is required.'); return; }

    const sharedKey = `shared:${localName}`;
    const sharedData = {
        secrets: state.derivedSecrets,
        isShared: true
    };
    const encrypted = await encryptWithKey(sharedData, state.sessionKey);

    await chrome.storage.sync.set({
        [sharedKey]: {
            ...encrypted,
            readOnly: true,
            sheetId: state.bundle.sheetId,
            meta: {
                importedFrom: state.bundle.profileName,
                importedAt: new Date().toISOString().slice(0, 10),
                bundleId: state.bundle.bundleId || ''
            }
        }
    });

    document.getElementById('modal-import').classList.add('hidden');
    state.onComplete?.();
}

function confirmClose() {
    if (state.step > 1) {
        if (!confirm('Discard import?')) return;
    }
    document.getElementById('modal-import').classList.add('hidden');
}

function updateDots(step) {
    for (let i = 1; i <= 3; i++) {
        const dot = document.getElementById(`imp-dot-${i}`);
        if (!dot) continue;
        dot.className = 'wizard-dot' + (i < step ? ' done' : i === step ? ' current' : '');
    }
    const label = document.getElementById('imp-step-label');
    if (label) label.textContent = `Step ${step} of 3`;
}

function escHtml(str) {
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}
