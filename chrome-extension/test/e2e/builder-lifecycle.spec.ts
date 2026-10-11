import { test, expect } from './fixtures';
import { setupCurrentCookbook } from './current-cookbook-fixture';
import { createOtherProfile } from './builder-test-utils';
import type { Page } from '@playwright/test';

async function modifier(page: Page, value: string, checked: boolean) {
    const input = page.locator(`#bld-modifier-row input[data-val="${value}"]`);
    // Checkbox is intentionally hidden behind its visible native label control.
    await expect(page.locator('#status-builder')).toBeVisible();
    if (await input.isChecked() !== checked) await input.locator('..').click({ timeout: 3000 });
    await expect(input).toBeChecked({ checked });
}

test('Builder cooks coherent previews via real worker and resets stale output', async ({ page, extensionId }) => {
    test.setTimeout(120000);
    const modifierToggle = (value: string, checked: boolean) => modifier(page, value, checked);
    const options = await setupCurrentCookbook(page, extensionId);
    await createOtherProfile(options, 'aB!*');
    await page.goto(`chrome-extension://${extensionId}/popup/popup.html`);
    await page.click('#btn-build-recipe');
    await expect(page.locator('#bld-more-options')).not.toHaveAttribute('open');
    await expect(page.locator('#bld-position-group .active')).toHaveCount(1);
    await expect(page.locator('#bld-secret-group .active')).toHaveCount(1);
    for (const [group, value] of [['position', '#'], ['secret', '1']]) {
        const button = page.locator(`#bld-${group}-group [data-val="${value}"]`);
        if (!(await button.evaluate(el => el.classList.contains('active')))) await button.click();
    }
    await page.fill('#bld-hash', 'Mix');
    await expect(page.locator('#bld-password-out')).toHaveText('MySecretPrefixMix');
    await page.click('#bld-more-options summary');
    for (const modifier of ['_', '!', '?', '~']) {
        await modifierToggle(modifier, true);
    }
    await expect(page.locator('#bld-password-out')).toHaveText('MixXIFERPTERCESYM');
    await page.selectOption('#bld-profile', 'Other');
    await expect(page.locator('#bld-password-out')).toHaveText('MixBA');
    // Literal independent dishes for every modifier subset; no production oracle.
    const combinations = [
        ['', 'aB!*Mix'], ['_', 'MixaB!*'], ['!', 'AB!*Mix'], ['?', '*!BaMix'], ['~', 'aBMix'],
        ['_!', 'MixAB!*'], ['_?', 'Mix*!Ba'], ['_~', 'MixaB'], ['!?', '*!BAMix'],
        ['!~', 'ABMix'], ['?~', 'BaMix'], ['_!?', 'Mix*!BA'], ['_!~', 'MixAB'],
        ['_?~', 'MixBa'], ['!?~', 'BAMix'], ['_!?~', 'MixBA'],
    ];
    for (const [modifiers, dish] of combinations) {
        for (const modifier of ['_', '!', '?', '~']) {
            await modifierToggle(modifier, modifiers.includes(modifier));
        }
        await expect(page.locator('#bld-password-out')).toHaveText(dish);
    }
    for (const value of ['_', '!', '?', '~']) await modifier(page, value, false);
    for (const [position, dish] of [['#', 'aB!*Mix'], ['$', 'MixaB!*'], ['@', 'MaB!*ix'], ['%', 'MaiBx!*'], ['^', 'MiaBx!*']]) {
        const button = page.locator(`#bld-position-group [data-val="${position}"]`);
        if (!(await button.evaluate(el => el.classList.contains('active')))) await button.click();
        await expect(page.locator('#bld-password-out')).toHaveText(dish);
    }
    await page.locator('#bld-position-group [data-val="#"]').click();
    // Rapid edits must use the newest snapshot, including the selected profile.
    await page.fill('#bld-hash', 'Obsolete'); await page.fill('#bld-hash', 'Latest');
    await expect(page.locator('#bld-password-out')).toHaveText('aB!*Latest');
    await page.fill('#bld-hash', 'Invalid♥');
    await expect(page.locator('#btn-copy-recipe')).toBeDisabled();
    await page.waitForTimeout(400);
    await expect(page.locator('#bld-recipe-out')).toHaveText('—');
    await expect(page.locator('#bld-password-out')).toHaveText('—');
    await page.click('#btn-builder-back'); await page.click('#btn-build-recipe');
    await expect(page.locator('#bld-hash')).toHaveValue('');
    await expect(page.locator('#bld-recipe-out')).toHaveText('—');
    await expect(page.locator('#btn-copy-recipe')).toBeDisabled();
    await expect(page.locator('#bld-more-options')).not.toHaveAttribute('open');
});
