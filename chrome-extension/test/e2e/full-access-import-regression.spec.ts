import { test, expect } from './fixtures';
import { setupCurrentCookbook } from './current-cookbook-fixture';

const sharingPassword = 'PublicSyntheticSharing123!';
const menuId = 'b01_synthetic_full_access_menu_2026';
const spices = ['Basic*', 'Practice🙂漢字', 'Public$3', 'Fixture@4', 'Demo^5'];

for (const withMenu of [true, false]) {
  test(`FA01 actual Full Access export/import cooks exact own Dish ${withMenu ? 'with mapped Menu' : 'without Menu'}`, async ({ page, extensionId }) => {
    test.setTimeout(120000);
    // Existing genuine setup/modal helper uses AES only for its readiness marker.
    // The bundle, import record and Cook below all use production UI/crypto paths.
    const options = await setupCurrentCookbook(page, extensionId, spices[0]);
    const errors: string[] = [];
    options.on('pageerror', error => errors.push(error.message));
    page.on('pageerror', error => errors.push(error.message));
    const original = options.locator('.profile-card').filter({ has: options.locator('.profile-name', { hasText: /^Default$/ }) });
    await original.locator('.btn-edit-secrets').click();
    await expect(options.locator('.modal-secret-input[data-index="1"]')).toHaveValue(spices[0]);
    for (let i = 0; i < spices.length; i++) {
      await options.fill(`.modal-secret-input[data-index="${i + 1}"]`, spices[i]);
    }
    await options.click('#btn-save-modal-secrets');
    await expect(options.locator('#modal-edit-secrets')).toBeHidden();
    const before = await options.evaluate(() => chrome.storage.sync.get(null));

    await original.locator('.btn-export').click();
    await expect(options.locator('#exp-sheet')).toBeVisible();
    await options.fill('#exp-sheet', withMenu ? menuId : '');
    await options.click('#exp-next-1');
    await options.fill('#exp-sharing-pwd', sharingPassword);
    await options.fill('#exp-sharing-pwd-confirm', sharingPassword);
    await options.click('#exp-generate');
    await expect(options.locator('#exp-bundle-output')).toBeVisible({ timeout: 20000 });
    const json = await options.locator('#exp-bundle-output').inputValue();
    const bundle = JSON.parse(json);
    expect(bundle.type).toBe('passchef-fullaccess-share');
    expect(bundle.sheetId).toBe(withMenu ? menuId : null);
    await options.click('#exp-done');

    await options.click('#btn-import-profile');
    await options.fill('#imp-bundle-input', json);
    await options.click('#imp-next-1');
    await options.fill('#imp-sharing-pwd', sharingPassword);
    await options.click('#imp-decrypt');
    await expect(options.locator('#imp-profile-name')).toBeVisible();
    await options.fill('#imp-profile-name', 'ImportedPractice');
    await options.click('#imp-confirm');
    await expect(options.locator('#modal-import')).toBeHidden();
    await options.reload();
    await expect(options.locator('#dashboard-section')).toBeVisible();
    const imported = options.locator('.profile-card').filter({ has: options.locator('.profile-name', { hasText: /^ImportedPractice$/ }) });
    await expect(imported).toHaveCount(1);
    const after = await options.evaluate(() => chrome.storage.sync.get(null));
    expect(after['profile:Default']).toEqual(before['profile:Default']);
    expect(after.salt).toEqual(before.salt);
    expect(after.defaultProfile).toBe(before.defaultProfile);
    expect(after.sheetMapping).toEqual(withMenu ? { ...before.sheetMapping, [menuId]: 'ImportedPractice' } : before.sheetMapping);
    expect(after['shared:ImportedPractice']).toBeUndefined();
    expect(after['profile:ImportedPractice'].encryptedData).toBeTruthy();
    await imported.locator('.btn-edit-secrets').click();
    for (let i = 0; i < spices.length; i++) {
      await expect(options.locator(`.modal-secret-input[data-index="${i + 1}"]`)).toHaveValue(spices[i]);
    }
    await options.locator('#modal-edit-secrets .modal-close').click();

    await page.goto(`chrome-extension://${extensionId}/popup/popup.html`);
    await page.click('#btn-build-recipe');
    await page.click('#bld-more-options summary');
    await expect(page.locator('#bld-profile option[value="ImportedPractice"]')).toHaveCount(1);
    if (withMenu) {
      await page.fill('#bld-sheet-url', menuId);
      await expect(page.locator('#bld-profile')).toBeDisabled();
      await expect(page.locator('#bld-profile')).toHaveValue('ImportedPractice');
    } else {
      await expect(page.locator('#bld-sheet-url')).toHaveValue('');
      await expect(page.locator('#bld-profile')).toBeEnabled();
      await page.selectOption('#bld-profile', 'ImportedPractice');
    }
    for (const [group, value] of [['position', '#'], ['secret', '1']]) {
      const button = page.locator(`#bld-${group}-group [data-val="${value}"]`);
      if (!(await button.evaluate(el => el.classList.contains('active')))) await button.click();
    }
    await page.fill('#bld-hash', 'r4nd0m');
    // Literal independent Dish oracle. No direct core call or imported-record seeding.
    await expect(page.locator('#bld-password-out')).toHaveText('Basic*r4nd0m');
    await expect(page.locator('#bld-recipe-out')).toHaveText(withMenu ? /^r4nd0m#1\.[abcdefghijkmnpqrstuvwxyz23456789]{4}$/ : 'r4nd0m#1');
    expect(errors).toEqual([]);
  });
}
