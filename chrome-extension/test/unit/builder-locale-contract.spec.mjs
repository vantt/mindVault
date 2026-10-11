import { readFileSync } from 'node:fs';
import { beforeEach, afterEach, it, expect, vi } from 'vitest';
import { loadLocale } from '../../src/i18n-loader.js';
import { initRecipeBuilder } from '../../src/popup/popup-recipe-builder.js';

const html = readFileSync(`${process.cwd()}/src/popup/popup.html`, 'utf8');
const locales = Object.fromEntries(['en', 'vi'].map(language => [language,
    JSON.parse(readFileSync(`${process.cwd()}/src/_locales/${language}/messages.json`, 'utf8'))]));
let builder, language, response;
const el = id => document.getElementById(id);
beforeEach(() => {
    vi.useFakeTimers(); document.body.innerHTML = html; language = 'en';
    response = { success: false, code: 'RECIPE_MISMATCH', error: '<img src=x onerror=alert(1)>' };
    vi.stubGlobal('chrome', {
        runtime: { getURL: path => path, sendMessage: vi.fn(async request =>
            request.action === 'GET_SHEET_ID_FROM_ACTIVE_TAB' ? { active: null, otherTabs: [] } : response) },
        i18n: { getMessage: () => '' },
        storage: { sync: { get: vi.fn(async key => key === 'language' ? { language } : { 'profile:Default': {} }) },
            onChanged: { addListener: vi.fn() } },
    });
    vi.stubGlobal('fetch', vi.fn(async url => ({ ok: true, json: async () => locales[url.includes('/vi/') ? 'vi' : 'en'] })));
});
afterEach(() => { builder?.close(); vi.useRealTimers(); vi.unstubAllGlobals(); document.body.replaceChildren(); });
async function cook() {
    builder = initRecipeBuilder({ onBack: () => {} }); builder.reset(); await builder.loadProfiles();
    for (const [group, value] of [['position', '#'], ['secret', '1']]) {
        const button = el(`bld-${group}-group`).querySelector(`[data-val="${value}"]`);
        if (!button.classList.contains('active')) button.click();
    }
    el('bld-hash').value = 'Synthetic'; el('bld-hash').dispatchEvent(new Event('input'));
    await vi.advanceTimersByTimeAsync(300);
}
for (const locale of ['en', 'vi']) {
    it(`selected ${locale} mismatch and intentional legacy warning use actual locale messages`, async () => {
        language = locale; await loadLocale(); await cook();
        expect(el('bld-preview-error').textContent).toBe(locales[locale].errRecipeMismatch.message);
        expect(el('bld-legacy-warning').textContent).toBe(locales[locale].warnLegacyNoTag.message);
        expect(el('bld-recipe-out').textContent).toBe('—'); expect(el('btn-copy-recipe').disabled).toBe(true);
        expect(document.querySelector('#bld-preview-error img')).toBeNull();
    });
    it(`selected ${locale} locked/missing-seasoning/runtime responses never display provider details`, async () => {
        language = locale; await loadLocale(); await cook();
        for (const [code, key] of [['VAULT_LOCKED', 'securityCancelled'], ['SECRET_NOT_FOUND', 'hintSecretNotFound'], ['GENERIC', 'errBuilderPreview']]) {
            response = { success: false, code, error: '<b>private provider detail</b>' };
            el('bld-hash').value += 'A'; el('bld-hash').dispatchEvent(new Event('input'));
            await vi.advanceTimersByTimeAsync(300);
            expect(el('bld-preview-error').textContent).toBe(locales[locale][key].message);
            expect(el('bld-preview-error').children).toHaveLength(0); expect(el('btn-copy-recipe').disabled).toBe(true);
        }
    });
}
