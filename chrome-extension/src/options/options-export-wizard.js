import { ExportImportAdapter } from "../adapters/infrastructure/export-import-adapter.js";
import { decryptWithKey } from "../adapters/infrastructure/aes-storage-crypto-helper.js";

const adapter = new ExportImportAdapter();

/** @type {{ profileName: string, profileKey: string, sessionKey: object, sheetId: string|null, onComplete: function }} */
let state = {};

export function openExportWizard(profileName, profileKey, sessionKey, onComplete, prefilledSheetId = null) {
    state = { profileName, profileKey, sessionKey, onComplete, step: 1, sheetId: prefilledSheetId || '' };
    document.querySelector('#export-modal-title .modal-title-em').textContent = profileName;
    document.getElementById('btn-close-export').onclick = () => confirmClose('modal-export');
    renderStep(1);
    document.getElementById('modal-export').classList.remove('hidden');
}

function renderStep(step) {
    state.step = step;
    updateDots('exp', step, 2);
    const content = document.getElementById('export-step-content');
    const footer = document.getElementById('export-modal-footer');

    if (step === 1) {
        content.innerHTML = `
            <p class="wizard-section-title">🔒 Lock to Google Sheet (Optional)</p>
            <p class="wizard-description">If provided, the sharing bundle will include this sheet ID, and the recipient's extension will auto-route to this profile when they open that sheet. Leave blank to share without sheet locking.</p>
            <div class="form-group">
                <label>Sheet URL or ID <span class="text-muted" style="font-weight:normal">(not required)</span></label>
                <input type="text" id="exp-sheet" value="${state.sheetId || ''}" placeholder="Paste Google Sheets URL or ID" />
                <div style="margin-top:.5rem;display:flex;gap:.5rem;align-items:center">
                    <button class="btn sm secondary" id="btn-use-current-sheet">📋 Use current sheet</button>
                    <span id="exp-sheet-preview" class="text-muted monospace" style="font-size:.83rem"></span>
                </div>
            </div>`;
        footer.innerHTML = `<span></span><div class="right"><button id="exp-next-1" class="btn sm primary" style="width:auto">Continue →</button></div>`;

        document.getElementById('btn-use-current-sheet').onclick = async () => {
            const resp = await chrome.runtime.sendMessage({ action: "GET_SHEET_ID_FROM_ACTIVE_TAB" });
            const sheetId = resp?.active?.sheetId;
            if (sheetId) {
                document.getElementById('exp-sheet').value = sheetId;
                document.getElementById('exp-sheet-preview').textContent = `✓ ${sheetId}`;
            } else {
                document.getElementById('exp-sheet-preview').textContent = "No Google Sheet tab found";
            }
        };
        document.getElementById('exp-next-1').onclick = () => {
            const raw = document.getElementById('exp-sheet').value.trim();
            if (raw) {
                const extracted = extractSheetId(raw);
                if (!extracted) { alert("Invalid Sheet URL or ID."); return; }
                state.sheetId = extracted;
                document.getElementById('exp-sheet-preview').textContent = `✓ ${extracted}`;
            } else {
                state.sheetId = null;
            }
            renderStep(2);
        };

    } else if (step === 2) {
        content.innerHTML = `
            <p class="wizard-section-title">🔑 Set Sharing Password</p>
            <p class="wizard-description">This password protects the bundle. The recipient needs it to import.</p>
            <div class="form-group">
                <label>Sharing Password</label>
                <div class="input-wrapper">
                    <input type="password" id="exp-sharing-pwd" />
                    <button type="button" class="btn-toggle-visibility">👁</button>
                </div>
            </div>
            <div class="form-group">
                <label>Confirm Password</label>
                <input type="password" id="exp-sharing-pwd-confirm" />
            </div>
            <div id="exp-step3-status"></div>`;
        footer.innerHTML = `<button id="exp-back-2" class="btn sm secondary">← Back</button><div class="right"><button id="exp-generate" class="btn sm primary" style="width:auto">Generate Bundle</button></div>`;

        document.querySelector('#export-step-content .btn-toggle-visibility').onclick = (e) => {
            const inp = document.getElementById('exp-sharing-pwd');
            inp.type = inp.type === 'password' ? 'text' : 'password';
            e.target.textContent = inp.type === 'password' ? '👁' : '🙈';
        };
        document.getElementById('exp-back-2').onclick = () => renderStep(1);
        document.getElementById('exp-generate').onclick = () => generateBundle();
    }
}

async function generateBundle() {
    const pwd = document.getElementById('exp-sharing-pwd').value;
    const confirm = document.getElementById('exp-sharing-pwd-confirm').value;
    const statusEl = document.getElementById('exp-step3-status');

    if (!pwd) { statusEl.innerHTML = '<p class="text-danger">Sharing password is required.</p>'; return; }
    if (pwd !== confirm) { statusEl.innerHTML = '<p class="text-danger">Passwords do not match.</p>'; return; }

    const btn = document.getElementById('exp-generate');
    btn.textContent = "Generating…";
    btn.disabled = true;

    try {
        // Decrypt profile secrets
        const stored = await chrome.storage.sync.get(state.profileKey);
        const profileData = stored[state.profileKey];
        if (!profileData) throw new Error("Profile data not found");
        const decrypted = await decryptWithKey(profileData.encryptedData, profileData.iv, state.sessionKey);

        // Create Full Access bundle — raw secrets, no HKDF derivation
        const bundle = await adapter.createFullAccessBundle(state.profileName, decrypted.secrets, state.sheetId, pwd);
        const bundleJson = JSON.stringify(bundle, null, 2);

        // Show result
        document.getElementById('export-step-content').innerHTML = `
            <p class="text-success" style="margin-bottom:.75rem">✅ Bundle Ready</p>
            <textarea class="bundle-textarea readonly" id="exp-bundle-output" readonly>${escHtml(bundleJson)}</textarea>
            <div style="display:flex;gap:.5rem;margin-top:.75rem">
                <button class="btn sm secondary" id="btn-copy-bundle">📋 Copy Bundle</button>
                <button class="btn sm secondary" id="btn-download-bundle">⬇️ Download .json</button>
            </div>
            <div class="warning-box">⚠️ ${chrome.i18n.getMessage('exportNextStep') || "Use this extension on the shared sheet to generate and set account passwords using the shared profile's secrets."}<br><br>⚠️ Transmit via encrypted channel only.</div>`;
        document.getElementById('export-modal-footer').innerHTML = `<span></span><div class="right"><button id="exp-done" class="btn sm primary" style="width:auto">Done</button></div>`;

        document.getElementById('btn-copy-bundle').onclick = () => {
            navigator.clipboard.writeText(bundleJson);
            document.getElementById('btn-copy-bundle').textContent = "✓ Copied";
        };
        document.getElementById('btn-download-bundle').onclick = () => {
            const blob = new Blob([bundleJson], { type: 'application/json' });
            const a = document.createElement('a');
            a.href = URL.createObjectURL(blob);
            a.download = `mindvault-bundle-${state.profileName}.json`;
            a.click();
        };
        document.getElementById('exp-done').onclick = () => {
            document.getElementById('modal-export').classList.add('hidden');
            state.onComplete?.();
        };
        updateDots('exp', 3, 2); // all done
    } catch (e) {
        btn.textContent = "Generate Bundle";
        btn.disabled = false;
        document.getElementById('exp-step3-status').innerHTML = `<p class="text-danger">Error: ${escHtml(e.message)}</p>`;
    }
}

function updateDots(prefix, step, total) {
    for (let i = 1; i <= total; i++) {
        const dot = document.getElementById(`${prefix}-dot-${i}`);
        if (!dot) continue;
        dot.className = 'wizard-dot' + (i < step ? ' done' : i === step ? ' current' : '');
    }
    const label = document.getElementById(`${prefix}-step-label`);
    if (label) label.textContent = step <= total ? `Step ${step} of ${total}` : 'Complete';
}

function confirmClose(modalId) {
    if (state.step > 1 && state.step <= 2) {
        if (!confirm("Discard export?")) return;
    }
    document.getElementById(modalId).classList.add('hidden');
}

function extractSheetId(input) {
    const urlMatch = input.match(/\/spreadsheets\/d\/([a-zA-Z0-9_-]+)/);
    if (urlMatch) return urlMatch[1];
    if (/^[a-zA-Z0-9_-]{20,}$/.test(input)) return input;
    return null;
}

function escHtml(str) {
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}
