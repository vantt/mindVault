import { test, expect } from './fixtures';
import { setupCurrentCookbook } from './current-cookbook-fixture';
import { createOtherProfile } from './builder-test-utils';

const A = 'syntheticMenu0000000001', B = 'syntheticMenu0000000002';

// Local Menu contract fixture; manifest injection and actual worker/content UI.
// Explicit tab-message dispatch tests UI wiring, not the native OS shortcut.
test('Builder tag reaches actual content mismatch and legacy UI in selected EN/VI', async ({ page, context, extensionId }) => {
    test.setTimeout(60000);
    const options = await setupCurrentCookbook(page, extensionId);
    await createOtherProfile(options);
    const menu = await context.newPage();
    await menu.route('https://docs.google.com/spreadsheets/**', route => route.fulfill({
        status: 200, contentType: 'text/html', body: '<html><body><input id="recipe"><div id="t-formula-bar-input" contenteditable="true"></div></body></html>',
    }));
    async function trigger(sheet: string, recipe: string) {
        const url = `https://docs.google.com/spreadsheets/d/${sheet}/edit`;
        await menu.goto(url); await menu.fill('#recipe', recipe); await menu.focus('#recipe');
        await expect.poll(() => options.evaluate(async url => {
            const [tab] = await chrome.tabs.query({ url });
            try { await chrome.tabs.sendMessage(tab.id, { action: 'TRIGGER_HOTKEY' }); return true; }
            catch { return false; }
        }, url)).toBe(true);
    }
    for (const language of ['en', 'vi']) {
        await options.evaluate(language => chrome.storage.sync.set({ language }), language);
        await page.goto(`chrome-extension://${extensionId}/popup/popup.html`);
        await page.click('#btn-build-recipe'); await page.click('#bld-more-options summary');
        await page.fill('#bld-sheet-url', A);
        for (const [group, value] of [['position', '#'], ['secret', '1']]) {
            const button = page.locator(`#bld-${group}-group [data-val="${value}"]`);
            if (!(await button.evaluate(el => el.classList.contains('active')))) await button.click();
        }
        await page.fill('#bld-hash', 'Dish');
        await expect(page.locator('#btn-copy-recipe')).toBeEnabled();
        const recipe = await page.locator('#bld-recipe-out').innerText();
        expect(recipe).toMatch(/^Dish#1\.[abcdefghijkmnpqrstuvwxyz23456789]{4}$/);
        await trigger(A, recipe);
        await expect(menu.locator('#mv-pwd-input')).toHaveValue('MySecretPrefixDish');
        // Same Menu with another active profile must refuse the original tag.
        await options.selectOption('#select-default-profile', 'Other');
        await expect.poll(() => options.evaluate(async () => (await chrome.storage.sync.get('defaultProfile')).defaultProfile)).toBe('Other');
        await trigger(A, recipe);
        await expect(menu.locator('.passchef-toast')).toContainText(language === 'vi' ? 'Recipe không khớp' : 'Recipe mismatch');
        await expect(menu.locator('#mv-pwd-input')).toHaveCount(0);
        await options.selectOption('#select-default-profile', 'Default');
        await expect.poll(() => options.evaluate(async () => (await chrome.storage.sync.get('defaultProfile')).defaultProfile)).toBe('Default');
        await trigger(B, recipe);
        const mismatch = language === 'vi' ? 'Recipe không khớp' : 'Recipe mismatch';
        await expect(menu.locator('.passchef-toast')).toContainText(mismatch);
        await expect(menu.locator('#mv-pwd-input')).toHaveCount(0);
        await trigger(B, 'Dish#1');
        await expect(menu.locator('#mv-pwd-input')).toHaveValue('MySecretPrefixDish');
        await expect(menu.locator('.passchef-popup')).toContainText(language === 'vi' ? 'Recipe cũ' : 'Legacy recipe');
        await page.fill('#bld-sheet-url', '');
        await expect(page.locator('#bld-legacy-warning')).toContainText(language === 'vi' ? 'Recipe cũ' : 'Legacy recipe');
    }
});
