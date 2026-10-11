import { t } from '../i18n-loader.js';

const ASCII = /^[a-zA-Z0-9]*$/;
export function createBuilderForm(change) {
    const ids = {
        sheetUrl: 'bld-sheet-url', sheetUrlError: 'bld-sheet-url-error',
        sheetWarning: 'bld-sheet-warning', sheetAuto: 'bld-sheet-auto-hint',
        legacyWarning: 'bld-legacy-warning', previewError: 'bld-preview-error',
        hash: 'bld-hash', hashError: 'bld-hash-error', profile: 'bld-profile',
        profileHint: 'bld-profile-hint', positionGroup: 'bld-position-group',
        secretGroup: 'bld-secret-group', modifierRow: 'bld-modifier-row',
        recipeOut: 'bld-recipe-out', passwordOut: 'bld-password-out',
        copyBtn: 'btn-copy-recipe', backBtn: 'btn-builder-back',
    };
    const el = Object.fromEntries(Object.entries(ids).map(([key, id]) => [key, document.getElementById(id)]));
    const state = { position: null, secret: null };
    const touched = new Set();
    const charset = ['upper', 'lower', 'digits'].map(key => document.getElementById(`btn-hash-${key}`));
    for (const [key, group] of [['position', el.positionGroup], ['secret', el.secretGroup]]) {
        group.querySelectorAll('button').forEach(button => button.addEventListener('click', () => {
            touched.add(key);
            state[key] = state[key] === button.dataset.val ? null : button.dataset.val;
            group.querySelectorAll('button').forEach(b => b.classList.toggle('active', b.dataset.val === state[key]));
            change();
        }));
    }
    charset.forEach(button => button.addEventListener('click', () => {
        if (!button.classList.contains('active') || charset.filter(b => b.classList.contains('active')).length > 1) {
            button.classList.toggle('active'); change();
        }
    }));
    el.hash.addEventListener('input', () => { change(); validate(); });
    el.profile.addEventListener('change', () => change());
    el.sheetUrl.addEventListener('input', () => change(true));
    el.modifierRow.querySelectorAll('input').forEach(cb => cb.addEventListener('change', () => change()));
    document.getElementById('btn-hash-generate').addEventListener('click', () => {
        const sets = ['ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz', '0123456789'];
        const chars = sets.filter((_, i) => charset[i].classList.contains('active')).join('');
        el.hash.value = Array.from(crypto.getRandomValues(new Uint8Array(24)), b => chars[b % chars.length]).join('');
        change(); validate();
    });
    function validate() {
        const valid = ASCII.test(el.hash.value);
        el.hash.classList.toggle('invalid', !valid); el.hashError.classList.toggle('hidden', valid);
        el.hashError.textContent = valid ? '' : t('errHashAscii');
        const indicator = document.getElementById('bld-hash-len-indicator');
        if (indicator) indicator.textContent = el.hash.value.length ? `Len ${el.hash.value.length}` : '';
        return valid;
    }
    function snapshot(sheetId) {
        const hash = el.hash.value.trim();
        if (!validate() || !hash || !state.position || !state.secret) return null;
        if (el.sheetUrl.value.trim() && !sheetId) return null;
        const modifiers = Array.from(el.modifierRow.querySelectorAll('input:checked')).map(cb => cb.dataset.val);
        return Object.freeze({
            base: `${hash}${state.position}${state.secret}${modifiers.join('')}`,
            sheetId, profileName: el.profile.value,
            recipeFields: Object.freeze({ hash, position: state.position, secretIndex: state.secret, modifiers: Object.freeze(modifiers) }),
        });
    }
    function randomize() {
        for (const [key, group] of [['position', el.positionGroup], ['secret', el.secretGroup]]) {
            if (touched.has(key)) continue;
            const buttons = [...group.querySelectorAll('button[data-val]')];
            const button = buttons[Math.floor(Math.random() * buttons.length)];
            state[key] = button.dataset.val;
            buttons.forEach(b => b.classList.toggle('active', b === button));
        }
    }
    function reset() {
        el.hash.value = ''; el.sheetUrl.value = ''; el.profile.replaceChildren(); el.profile.disabled = false;
        state.position = null; state.secret = null;
        touched.clear();
        [el.positionGroup, el.secretGroup].forEach(group => group.querySelectorAll('button').forEach(b => b.classList.remove('active')));
        el.modifierRow.querySelectorAll('input').forEach(cb => { cb.checked = false; });
        charset.forEach(button => button.classList.add('active'));
        const more = document.getElementById('bld-more-options'); if (more) more.open = false;
        validate();
    }
    return { el, snapshot, reset, randomize };
}
