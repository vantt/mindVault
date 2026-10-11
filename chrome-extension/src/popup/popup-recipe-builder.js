import { initSheetDetector } from './sheet-detector.js';
import { createBuilderForm } from './builder-form.js';
import { createBuilderSheetContext } from './builder-sheet-context.js';
import { createBuilderPreview } from './builder-preview.js';

// Every edit owns a new revision, including edits that cannot produce a recipe.
export function initRecipeBuilder({ onBack }) {
    let active = false, revision = 0, lifetime = 0, loadSequence = 0, detector;
    const form = createBuilderForm(change);
    const sheet = createBuilderSheetContext(form.el);
    const preview = createBuilderPreview(form.el, () => active);
    function invalidate() {
        revision++; preview.invalidate(); detector?.invalidate();
    }
    function change(sheetEdited = false) {
        invalidate();
        if (!active) return;
        if (sheetEdited) sheet.parse(false);
        const snapshot = form.snapshot(sheet.sheetId());
        if (snapshot) preview.schedule(snapshot);
    }
    detector = initSheetDetector({
        inputEl: form.el.sheetUrl, errorEl: form.el.sheetUrlError,
        isActive: () => active,
        onStart: () => { revision++; preview.invalidate(); },
        onSettled: () => {
            const snapshot = form.snapshot(sheet.sheetId());
            if (snapshot) preview.schedule(snapshot);
        },
        onDetected: url => {
            if (!active) return;
            invalidate(); form.el.sheetUrl.value = url; sheet.parse(true);
            const snapshot = form.snapshot(sheet.sheetId());
            if (snapshot) preview.schedule(snapshot);
        },
    });
    async function loadProfiles() {
        const token = lifetime, load = ++loadSequence, initialRevision = revision;
        const current = () => active && token === lifetime && load === loadSequence;
        try {
            const all = await chrome.storage.sync.get(null);
            if (!current()) return;
            const selectedProfile = form.el.profile.value;
            const names = Object.keys(all).filter(k => k.startsWith('profile:')).map(k => k.slice(8)).sort();
            form.el.profile.replaceChildren(...names.map(name => {
                const option = document.createElement('option');
                option.value = name; option.textContent = name; return option;
            }));
            form.el.profile.value = selectedProfile;
            sheet.configure(all.sheetMapping || {}, all.defaultProfile || 'Default');
            // Metadata belongs to the open session; edits still own the form.
            const edited = initialRevision !== revision;
            invalidate(); form.randomize(); sheet.parse(false, true);
            const snapshot = form.snapshot(sheet.sheetId());
            if (snapshot) preview.schedule(snapshot);
            if (!edited) await detector.runDetection();
        } catch { if (current()) preview.fail('errBuilderPreview'); }
    }
    function reset() {
        active = true; lifetime++; invalidate(); form.reset(); sheet.configure({}, 'Default'); sheet.parse(false);
    }
    function close() { active = false; lifetime++; invalidate(); form.reset(); }
    form.el.backBtn.addEventListener('click', () => { close(); onBack(); });
    window.addEventListener('pagehide', close);
    chrome.storage.onChanged?.addListener((changes, area) => {
        if (area === 'session' && changes.sessionKey && !changes.sessionKey.newValue) close();
    });
    document.getElementById('btn-how-it-works')?.addEventListener('click', () => {
        chrome.tabs.create({ url: chrome.runtime.getURL('demo/demo.html') });
    });
    preview.invalidate();
    return { loadProfiles, reset, close };
}
