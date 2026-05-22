/**
 * Recipe Builder UI logic for the popup.
 *
 * Lets users compose a recipe string (`<hash><position><secret>[modifiers][.<tag>]`)
 * via form controls, with a debounced live password preview to verify the recipe
 * matches what the Google Sheet decoder would produce.
 *
 * Verification tag (Phase 06, Option B): when a target sheet URL is provided,
 * the builder requests a 4-char HMAC tag from the SW (`COMPUTE_RECIPE_TAG`) and
 * appends `.<tag>` to the recipe. Tag is verification-only metadata — does not
 * change password value (preserves Cookbook insight, see
 * docs/recipe-tag-design-rationale.md).
 *
 * Scope: own profiles only (shared profiles need sheetId binding handled via
 * sheetMapping, not via builder).
 */

const PREVIEW_DEBOUNCE_MS = 300;
const SHEET_URL_REGEX = /\/spreadsheets\/d\/([a-zA-Z0-9_-]+)/;

/**
 * Initialize the builder. Returns a controller with `reset()` and `loadProfiles()`.
 * @param {object} opts
 * @param {() => void} opts.onBack  - called when Back button pressed
 */
export function initRecipeBuilder({ onBack }) {
    const el = {
        sheetUrl: document.getElementById('bld-sheet-url'),
        sheetUrlError: document.getElementById('bld-sheet-url-error'),
        sheetWarning: document.getElementById('bld-sheet-warning'),
        hash: document.getElementById('bld-hash'),
        hashError: document.getElementById('bld-hash-error'),
        profile: document.getElementById('bld-profile'),
        positionGroup: document.getElementById('bld-position-group'),
        secretGroup: document.getElementById('bld-secret-group'),
        modifierRow: document.getElementById('bld-modifier-row'),
        recipeOut: document.getElementById('bld-recipe-out'),
        passwordOut: document.getElementById('bld-password-out'),
        copyBtn: document.getElementById('btn-copy-recipe'),
        backBtn: document.getElementById('btn-builder-back'),
    };

    // Mirror of parser's hash charset — ASCII alphanumeric only.
    const HASH_VALID_CHAR = /^[a-zA-Z0-9]*$/;

    const state = { position: null, secret: null, sheetId: null };
    let previewToken = 0; // latest-wins cancellation token for async SW calls
    let debounceTimer = null;

    // ── Toggle group helpers (single-select within a group) ──────────────────
    const wireToggleGroup = (groupEl, key) => {
        groupEl.querySelectorAll('button').forEach(btn => {
            btn.addEventListener('click', () => {
                const val = btn.dataset.val;
                if (state[key] === val) {
                    state[key] = null;
                    btn.classList.remove('active');
                } else {
                    state[key] = val;
                    groupEl.querySelectorAll('button').forEach(b => b.classList.toggle('active', b === btn));
                }
                onFormChange();
            });
        });
    };
    wireToggleGroup(el.positionGroup, 'position');
    wireToggleGroup(el.secretGroup, 'secret');

    // ── Inputs + checkboxes ─────────────────────────────────────────────────
    el.hash.addEventListener('input', () => { validateHash(); onFormChange(); });
    el.profile.addEventListener('change', onFormChange);
    el.sheetUrl.addEventListener('input', () => { parseSheetUrl(); onFormChange(); });
    el.modifierRow.querySelectorAll('input[type="checkbox"]').forEach(cb => {
        cb.addEventListener('change', onFormChange);
    });

    // ── Validate hash field & toggle inline error ───────────────────────────
    function validateHash() {
        const v = el.hash.value;
        const ok = HASH_VALID_CHAR.test(v);
        el.hash.classList.toggle('invalid', !ok);
        if (ok) {
            el.hashError.classList.add('hidden');
            el.hashError.textContent = '';
        } else {
            const msg = chrome.i18n.getMessage('errHashAscii') || 'Only ASCII letters and digits allowed';
            el.hashError.textContent = msg;
            el.hashError.classList.remove('hidden');
        }
        return ok;
    }

    // ── Parse sheet URL → update state.sheetId + toggle warning/error ───────
    function parseSheetUrl() {
        const raw = el.sheetUrl.value.trim();
        if (!raw) {
            state.sheetId = null;
            el.sheetUrlError.classList.add('hidden');
            el.sheetWarning.classList.remove('hidden');
            return;
        }
        const match = raw.match(SHEET_URL_REGEX);
        if (match) {
            state.sheetId = match[1];
            el.sheetUrlError.classList.add('hidden');
            el.sheetWarning.classList.add('hidden');
        } else {
            state.sheetId = null;
            const msg = chrome.i18n.getMessage('errInvalidSheetUrl') || 'Not a valid Google Sheets URL';
            el.sheetUrlError.textContent = msg;
            el.sheetUrlError.classList.remove('hidden');
            el.sheetWarning.classList.add('hidden');
        }
    }

    // ── Read currently-checked modifiers in DOM order ───────────────────────
    // Single source of truth so recipe-string and tag-message see the same set,
    // even if checkbox state changes between async ticks.
    function currentModifiers() {
        return Array.from(el.modifierRow.querySelectorAll('input[type="checkbox"]:checked'))
            .map(cb => cb.dataset.val);
    }

    // ── Build base recipe string (no tag) from current form state ───────────
    function buildBaseRecipe() {
        const hash = el.hash.value.trim();
        if (!hash || !state.position || !state.secret) return null;
        if (!HASH_VALID_CHAR.test(hash)) return null;

        const mods = currentModifiers().join('');
        return `${hash}${state.position}${state.secret}${mods}`;
    }

    // ── Ask SW to compute tag; returns null on failure ──────────────────────
    async function fetchTag(base) {
        const profileName = el.profile.value;
        const hash = el.hash.value.trim();
        const modifiers = currentModifiers();
        try {
            const r = await chrome.runtime.sendMessage({
                action: 'COMPUTE_RECIPE_TAG',
                recipeFields: { hash, position: state.position, secretIndex: state.secret, modifiers },
                sheetId: state.sheetId,
                profileName,
            });
            return r?.success ? r.tag : null;
        } catch {
            return null;
        }
    }

    // ── Form change handler: rebuild string, schedule preview ───────────────
    function onFormChange() {
        const base = buildBaseRecipe();
        // Block render if sheet URL was typed but invalid (forces user to fix)
        const urlTypedInvalid = el.sheetUrl.value.trim() !== '' && state.sheetId === null;
        if (!base || urlTypedInvalid) {
            el.recipeOut.textContent = '—';
            el.recipeOut.classList.add('muted');
            el.passwordOut.textContent = '—';
            el.passwordOut.classList.add('muted');
            el.passwordOut.classList.remove('error');
            return;
        }

        // Optimistic render: show base recipe now, fill in tag async
        el.recipeOut.textContent = base;
        el.recipeOut.classList.remove('muted');

        clearTimeout(debounceTimer);
        el.passwordOut.textContent = '…';
        el.passwordOut.classList.add('muted');
        el.passwordOut.classList.remove('error');
        debounceTimer = setTimeout(() => requestPreview(base), PREVIEW_DEBOUNCE_MS);
    }

    // ── Build final recipe (with tag if applicable) + fetch preview password
    async function requestPreview(base) {
        const myToken = ++previewToken;
        const profileName = el.profile.value;
        if (!profileName) {
            el.passwordOut.textContent = '(no profile)';
            el.passwordOut.classList.add('error');
            return;
        }

        // 1. Resolve tag (if sheetId present)
        let finalRecipe = base;
        if (state.sheetId) {
            const tag = await fetchTag(base);
            if (myToken !== previewToken) return;
            if (tag) finalRecipe = `${base}.${tag}`;
        }
        if (myToken !== previewToken) return;
        el.recipeOut.textContent = finalRecipe;

        // 2. Fetch preview password
        try {
            const response = await chrome.runtime.sendMessage({
                action: 'GENERATE_PASSWORD',
                text: finalRecipe,
                sheetId: state.sheetId,
                profileName,
            });
            if (myToken !== previewToken) return;
            if (response?.success) {
                el.passwordOut.textContent = response.password;
                el.passwordOut.classList.remove('muted', 'error');
            } else {
                el.passwordOut.textContent = response?.error || 'Error';
                el.passwordOut.classList.add('error');
                el.passwordOut.classList.remove('muted');
            }
        } catch (e) {
            if (myToken !== previewToken) return;
            el.passwordOut.textContent = e.message || 'Error';
            el.passwordOut.classList.add('error');
            el.passwordOut.classList.remove('muted');
        }
    }

    // ── Copy recipe string ──────────────────────────────────────────────────
    el.copyBtn.addEventListener('click', () => {
        const recipe = el.recipeOut.textContent;
        if (!recipe || recipe === '—') return;
        navigator.clipboard.writeText(recipe);
        const original = el.copyBtn.textContent;
        el.copyBtn.textContent = 'Copied!';
        setTimeout(() => { el.copyBtn.textContent = original; }, 1500);
    });

    // ── Back ────────────────────────────────────────────────────────────────
    el.backBtn.addEventListener('click', () => {
        reset();
        onBack();
    });

    // ── Public: load own profiles + auto-fill sheet URL from active tab ─────
    async function loadProfiles() {
        try {
            const { defaultProfile, ...all } = await chrome.storage.sync.get(null);
            const ownNames = Object.keys(all)
                .filter(k => k.startsWith('profile:'))
                .map(k => k.slice('profile:'.length))
                .sort();
            el.profile.innerHTML = '';
            ownNames.forEach(name => {
                const opt = document.createElement('option');
                opt.value = name;
                opt.textContent = name;
                if (name === defaultProfile) opt.selected = true;
                el.profile.appendChild(opt);
            });
        } catch (e) {
            console.error('loadProfiles failed', e);
        }

        // Auto-fill sheet URL if currently active tab is a Google Sheet
        try {
            const r = await chrome.runtime.sendMessage({ action: 'GET_SHEET_ID_FROM_ACTIVE_TAB' });
            if (r?.tabUrl && SHEET_URL_REGEX.test(r.tabUrl)) {
                el.sheetUrl.value = r.tabUrl;
                parseSheetUrl();
            } else {
                parseSheetUrl(); // applies initial empty-state warning
            }
        } catch {
            parseSheetUrl();
        }
    }

    // ── Public: reset form to empty ─────────────────────────────────────────
    function reset() {
        el.sheetUrl.value = '';
        el.sheetUrlError.classList.add('hidden');
        el.sheetWarning.classList.add('hidden');
        el.hash.value = '';
        el.hash.classList.remove('invalid');
        el.hashError.classList.add('hidden');
        el.hashError.textContent = '';
        state.position = null;
        state.secret = null;
        state.sheetId = null;
        el.positionGroup.querySelectorAll('button').forEach(b => b.classList.remove('active'));
        el.secretGroup.querySelectorAll('button').forEach(b => b.classList.remove('active'));
        el.modifierRow.querySelectorAll('input[type="checkbox"]').forEach(cb => { cb.checked = false; });
        el.recipeOut.textContent = '—';
        el.recipeOut.classList.add('muted');
        el.passwordOut.textContent = '—';
        el.passwordOut.classList.add('muted');
        el.passwordOut.classList.remove('error');
        clearTimeout(debounceTimer);
        previewToken++;
    }

    return { loadProfiles, reset };
}
