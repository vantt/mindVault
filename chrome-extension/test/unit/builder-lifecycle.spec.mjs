import { readFileSync } from 'node:fs';
import { beforeEach, afterEach, describe, it, expect, vi } from 'vitest';
vi.mock('../../src/i18n-loader.js', () => ({ t: key => `localized:${key}` }));
import { initRecipeBuilder } from '../../src/popup/popup-recipe-builder.js';

const html = readFileSync(`${process.cwd()}/src/popup/popup.html`, 'utf8');
const SHEET = 'syntheticMenu0000000001';
const otherSheet = 'syntheticMenu0000000002';
const byId = id => document.getElementById(id);
function deferred() { let resolve; const promise = new Promise(r => { resolve = r; }); return { promise, resolve }; }
let builder, send, get, clipboard, storageListener;
const input = (id, value) => { byId(id).value = value; byId(id).dispatchEvent(new Event('input')); };
const change = (id, value) => { byId(id).value = value; byId(id).dispatchEvent(new Event('change')); };
async function tick() { await vi.advanceTimersByTimeAsync(300); }
async function open() {
    builder.reset(); await builder.loadProfiles();
    for (const [id, value] of [['bld-position-group', '#'], ['bld-secret-group', '1']]) {
        const button = byId(id).querySelector(`[data-val="${value}"]`);
        if (!button.classList.contains('active')) button.click();
    }
}
beforeEach(() => {
    vi.useFakeTimers(); document.body.innerHTML = html;
    send = vi.fn(async request => request.action === 'GET_SHEET_ID_FROM_ACTIVE_TAB'
        ? { active: null, otherTabs: [] } : request.action === 'COMPUTE_RECIPE_TAG'
            ? { success: true, tag: 'abcd' } : { success: true, password: 'SyntheticDish' });
    get = vi.fn(async () => ({ 'profile:Default': {}, 'profile:Other': {}, defaultProfile: 'Default' }));
    clipboard = vi.fn(async () => {});
    vi.stubGlobal('chrome', { runtime: { sendMessage: send }, storage: { sync: { get },
        onChanged: { addListener: listener => { storageListener = listener; } } } });
    Object.defineProperty(navigator, 'clipboard', { configurable: true, value: { writeText: clipboard } });
    builder = initRecipeBuilder({ onBack: vi.fn() });
});
afterEach(() => { builder.close(); vi.useRealTimers(); vi.unstubAllGlobals(); document.body.replaceChildren(); });

describe('Recipe Builder async lifetime through real form events', () => {
    it('valid→invalid before debounce cancels cooking and Copy', async () => {
        await open(); input('bld-hash', 'Valid'); input('bld-hash', 'Invalid♥'); await tick();
        expect(send.mock.calls.filter(([r]) => r.action === 'GENERATE_PASSWORD')).toHaveLength(0);
        expect(byId('bld-recipe-out').textContent).toBe('—'); expect(byId('btn-copy-recipe').disabled).toBe(true);
    });
    it('late tag cannot publish or cook after an invalid edit', async () => {
        await open(); const tag = deferred();
        send.mockImplementation(r => r.action === 'COMPUTE_RECIPE_TAG' ? tag.promise : Promise.resolve({ success: true, password: 'OldDish' }));
        input('bld-sheet-url', SHEET); input('bld-hash', 'Valid'); await tick();
        input('bld-hash', ''); tag.resolve({ success: true, tag: 'abcd' }); await tick();
        expect(send.mock.calls.filter(([r]) => r.action === 'GENERATE_PASSWORD')).toHaveLength(0);
        expect(byId('bld-recipe-out').textContent).toBe('—');
    });
    it('a tag failure for a selected Menu never silently cooks an untagged recipe', async () => {
        await open(); send.mockResolvedValue({ success: false, error: 'sensitive provider detail' });
        input('bld-sheet-url', SHEET); input('bld-hash', 'Valid'); await tick();
        expect(byId('bld-preview-error').textContent).toBe('localized:errRecipeTag');
        expect(send.mock.calls.filter(([r]) => r.action === 'GENERATE_PASSWORD')).toHaveLength(0);
        expect(byId('btn-copy-recipe').disabled).toBe(true);
    });
    it('profile and Menu edits invalidate an old tag before the next debounce and use one coherent snapshot', async () => {
        await open(); const old = deferred();
        send.mockImplementation(r => r.action === 'COMPUTE_RECIPE_TAG' && r.sheetId === SHEET ? old.promise
            : Promise.resolve(r.action === 'COMPUTE_RECIPE_TAG' ? { success: true, tag: 'wxyz' } : { success: true, password: 'NewDish' }));
        input('bld-sheet-url', SHEET); input('bld-hash', 'First'); await tick();
        input('bld-sheet-url', otherSheet); change('bld-profile', 'Other'); input('bld-hash', 'Second');
        old.resolve({ success: true, tag: 'abcd' }); await tick();
        const dishes = send.mock.calls.filter(([r]) => r.action === 'GENERATE_PASSWORD').map(([r]) => r);
        expect(dishes).toEqual([{ action: 'GENERATE_PASSWORD', text: 'Second#1.wxyz', sheetId: otherSheet, profileName: 'Other' }]);
        expect(byId('bld-password-out').textContent).toBe('NewDish');
    });
    it('late dish cannot restore outputs after Back and reopen', async () => {
        await open(); const dish = deferred(); send.mockReturnValue(dish.promise);
        input('bld-hash', 'First'); await tick(); byId('btn-builder-back').click(); builder.reset();
        dish.resolve({ success: true, password: 'OldDish' }); await tick();
        expect(byId('bld-recipe-out').textContent).toBe('—'); expect(byId('bld-password-out').textContent).toBe('—');
    });
    it('initial metadata load preserves early ingredient edits while leaving the form usable', async () => {
        const storage = deferred(); get.mockReturnValue(storage.promise); builder.reset(); const loading = builder.loadProfiles();
        input('bld-hash', 'Current'); storage.resolve({ 'profile:Default': {} }); await loading; await tick();
        expect(byId('bld-profile').value).toBe('Default'); expect(byId('bld-hash').value).toBe('Current');
        expect(byId('bld-position-group').querySelectorAll('.active')).toHaveLength(1);
        expect(byId('bld-secret-group').querySelectorAll('.active')).toHaveLength(1);
        expect(byId('btn-copy-recipe').disabled).toBe(false);
    });
    it('old profile metadata cannot cross close/reopen into a new session', async () => {
        const storage = deferred(); get.mockReturnValueOnce(storage.promise); builder.reset(); const loading = builder.loadProfiles();
        builder.close(); builder.reset(); await builder.loadProfiles();
        storage.resolve({ 'profile:Stale': {} }); await loading;
        expect([...byId('bld-profile').options].map(o => o.value)).toEqual(['Default', 'Other']);
    });
    it('metadata arriving after early explicit position/spice edits preserves those choices', async () => {
        const storage = deferred(); get.mockReturnValue(storage.promise); builder.reset(); const loading = builder.loadProfiles();
        byId('bld-position-group').querySelector('[data-val="$"]').click();
        byId('bld-secret-group').querySelector('[data-val="1"]').click();
        input('bld-hash', 'Current'); storage.resolve({ 'profile:Default': {} }); await loading; await tick();
        expect(byId('bld-recipe-out').textContent).toBe('Current$1');
    });
    it('late detection cannot replace a manually selected Menu', async () => {
        const detecting = deferred(); send.mockReturnValue(detecting.promise); builder.reset();
        const loading = builder.loadProfiles(); await Promise.resolve(); await Promise.resolve();
        input('bld-sheet-url', SHEET);
        detecting.resolve({ active: { tabUrl: `https://docs.google.com/spreadsheets/d/${otherSheet}/edit` } }); await loading;
        expect(byId('bld-sheet-url').value).toBe(SHEET); expect(byId('bld-sheet-auto-hint').classList.contains('hidden')).toBe(true);
    });
    it('no own profile fails safely with a localized outcome', async () => {
        get.mockResolvedValue({}); await open(); input('bld-hash', 'Valid'); await tick();
        expect(byId('bld-preview-error').textContent).toBe('localized:errNoProfile'); expect(byId('btn-copy-recipe').disabled).toBe(true);
    });
    it('Copy stays disabled while dish is unresolved and only reports an awaited successful write', async () => {
        await open(); const dish = deferred(); send.mockReturnValue(dish.promise); input('bld-hash', 'Valid'); await tick();
        expect(byId('btn-copy-recipe').disabled).toBe(true);
        dish.resolve({ success: true, password: 'Dish' }); await tick();
        const copying = deferred(); clipboard.mockReturnValue(copying.promise); byId('btn-copy-recipe').click();
        expect(byId('btn-copy-recipe').disabled).toBe(true); expect(byId('btn-copy-recipe').textContent).not.toContain('Copied');
        copying.resolve(); await tick(); expect(clipboard).toHaveBeenCalledWith('Valid#1');
        expect(byId('btn-copy-recipe').textContent).toBe('localized:lblCopied');
    });
    it('clipboard rejection gives a localized failure without false success', async () => {
        await open(); input('bld-hash', 'Valid'); await tick(); clipboard.mockRejectedValue(new Error('private detail'));
        byId('btn-copy-recipe').click(); await tick();
        expect(byId('bld-preview-error').textContent).toBe('localized:errRecipeCopy'); expect(byId('btn-copy-recipe').textContent).not.toContain('lblCopied');
    });
    it('legacy and automatic Menu warnings are visible and mapped profiles remain locked', async () => {
        get.mockResolvedValue({ 'profile:Default': {}, 'profile:Other': {}, sheetMapping: { [SHEET]: 'Other' } });
        send.mockResolvedValue({ active: { tabUrl: `https://docs.google.com/spreadsheets/d/${SHEET}/edit` } });
        await open(); expect(byId('bld-sheet-auto-hint').textContent).toBe('localized:hintSheetUrlAuto');
        expect(byId('bld-profile').value).toBe('Other'); expect(byId('bld-profile').disabled).toBe(true);
        input('bld-sheet-url', ''); expect(byId('bld-legacy-warning').textContent).toBe('localized:warnLegacyNoTag');
        expect(byId('bld-more-options').open).toBe(false);
    });
    it('recipe mismatch is localized rather than exposing the runtime error', async () => {
        await open(); send.mockResolvedValue({ success: false, code: 'RECIPE_MISMATCH', error: 'raw detail' });
        input('bld-hash', 'Valid'); await tick(); expect(byId('bld-preview-error').textContent).toBe('localized:errRecipeMismatch');
    });
    it('refresh detection returning no tabs recooks the existing Menu rather than leaving Copy pending', async () => {
        await open(); input('bld-sheet-url', SHEET); input('bld-hash', 'Valid'); await tick();
        expect(byId('btn-copy-recipe').disabled).toBe(false);
        byId('bld-detect-refresh').click(); expect(byId('btn-copy-recipe').disabled).toBe(true);
        await tick(); expect(byId('btn-copy-recipe').disabled).toBe(false);
        expect(byId('bld-recipe-out').textContent).toBe('Valid#1.abcd');
    });
    it('a stale clipboard completion cannot restore Copied feedback or enable an invalid form', async () => {
        await open(); input('bld-hash', 'Valid'); await tick();
        const copying = deferred(); clipboard.mockReturnValue(copying.promise); byId('btn-copy-recipe').click();
        input('bld-hash', ''); copying.resolve(); await tick();
        expect(byId('btn-copy-recipe').disabled).toBe(true); expect(byId('btn-copy-recipe').textContent).not.toContain('lblCopied');
    });
    it('closing during detection prevents both auto-fill and stale picker publication', async () => {
        const detecting = deferred(); send.mockReturnValue(detecting.promise); builder.reset();
        const loading = builder.loadProfiles(); await Promise.resolve(); await Promise.resolve(); builder.close();
        detecting.resolve({ otherTabs: [{ title: 'Stale', tabUrl: `https://docs.google.com/spreadsheets/d/${SHEET}/edit` },
            { title: 'Stale two', tabUrl: `https://docs.google.com/spreadsheets/d/${otherSheet}/edit` }] }); await loading;
        expect(byId('bld-sheet-url').value).toBe(''); expect(byId('bld-tabs-picker').children).toHaveLength(0);
        expect(byId('bld-profile').options).toHaveLength(0);
    });
    for (const stage of ['tag', 'dish']) for (const exit of ['lock', 'pagehide']) {
        it(`${exit} listener cancels a pending ${stage} without revealing stale outputs`, async () => {
            await open(); const pending = deferred();
            send.mockImplementation(r => r.action === (stage === 'tag' ? 'COMPUTE_RECIPE_TAG' : 'GENERATE_PASSWORD')
                ? pending.promise : Promise.resolve({ success: true, tag: 'abcd' }));
            input('bld-sheet-url', SHEET); input('bld-hash', 'Valid'); await tick();
            if (exit === 'lock') storageListener({ sessionKey: { oldValue: {}, newValue: undefined } }, 'session');
            else window.dispatchEvent(new Event('pagehide'));
            pending.resolve(stage === 'tag' ? { success: true, tag: 'abcd' } : { success: true, password: 'StaleDish' });
            await tick(); expect(byId('bld-password-out').textContent).toBe('—');
            expect(byId('bld-recipe-out').textContent).toBe('—'); expect(byId('btn-copy-recipe').disabled).toBe(true);
        });
    }
});
