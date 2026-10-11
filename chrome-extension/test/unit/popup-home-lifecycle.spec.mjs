import { describe, it, expect, vi } from 'vitest';
const security = vi.hoisted(() => ({ request: vi.fn(async () => ({ unlocked: true })) }));
vi.mock('../../src/security/page-security.js', () => ({ securityRequest: security.request }));
vi.mock('../../src/i18n-loader.js', () => ({ t: key => key }));
import { createPopupHome } from '../../src/popup/popup-home.js';
const deferred = () => { let resolve; const promise = new Promise(done => { resolve = done; }); return { promise, resolve }; };
function fixture() {
    document.body.innerHTML = ['home-hint','home-notice','home-notice-text','home-sheet-context','btn-share-sheet',
        'dish','profile','hint'].map(id => `<div id="${id}"></div>`).join('');
    const dish = document.getElementById('dish'), shown = [], responses = [];
    globalThis.chrome = { tabs: { sendMessage: () => { const response = deferred(); responses.push(response); return response.promise; } } };
    let home = true;
    const controller = createPopupHome({ showSection: id => shown.push(id), genPasswordInput: dish,
        genProfileLabel: document.getElementById('profile'), genHint: document.getElementById('hint'), isHome: () => home });
    return { controller, dish, shown, responses, leave: () => { home = false; controller.cancelDetection(); } };
}
describe('Home asynchronous detection navigation', () => {
    it('a completed cell request cannot overwrite Builder after leaving Home', async () => {
        const f = fixture(); const pending = f.controller.tryAutoDetect({ id: 1, url: 'https://docs.google.com/spreadsheets/d/synthetic/edit' });
        f.leave(); f.responses[0].resolve({ success: true, password: 'synthetic-dish' }); await pending;
        expect(f.dish.value).toBeUndefined(); expect(f.shown).toEqual([]);
    });
    it('an older response cannot replace a newer detection', async () => {
        const f = fixture(), tab = { id: 1, url: 'https://docs.google.com/spreadsheets/d/synthetic/edit' };
        const first = f.controller.tryAutoDetect(tab), second = f.controller.tryAutoDetect(tab);
        f.responses[1].resolve({ success: true, password: 'newer-synthetic-dish' }); await second;
        f.responses[0].resolve({ success: true, password: 'stale-synthetic-dish' }); await first;
        expect(f.dish.value).toBe('newer-synthetic-dish'); expect(f.shown).toEqual(['status-generated']);
    });
});
