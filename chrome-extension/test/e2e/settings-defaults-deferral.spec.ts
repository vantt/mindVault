import { test, expect } from './fixtures';
import { setupCurrentCookbook } from './current-cookbook-fixture';

test('UI01 Settings EN/VI reloads retain legacy defaults and working language/quota/master panels', async ({ page, extensionId }) => {
  test.setTimeout(90000);
  const options = await setupCurrentCookbook(page, extensionId, 'Basic*');
  const errors: string[] = [];
  options.on('pageerror', error => errors.push(error.message));
  const legacy = { showPepperHint: false, revealPassword: true, autoDetectSheet: false, retainedUnknown: 'syntheticLegacy' };
  // Metadata fixture only: never replace the real Settings initializer or storage API.
  await options.evaluate(async displaySettings => {
    await chrome.storage.sync.set({ displaySettings, language: 'en' });
  }, legacy);
  const before = await options.evaluate(() => chrome.storage.sync.get(null));
  await options.reload();

  const languages = ['en', 'vi', 'en'];
  for (const [index, language] of languages.entries()) {
    await expect(options.locator('#dashboard-section')).toBeVisible();
    await options.click('[data-tab="settings"]');
    await expect(options.locator('#tab-settings')).toBeVisible();
    for (const id of ['disp-pepper-hint', 'disp-reveal-password', 'disp-auto-detect-sheet']) {
      await expect(options.locator(`#${id}`)).toHaveCount(0);
    }
    await expect(options.locator('[data-i18n="settingsDisplayTitle"]')).toHaveCount(0);
    await expect(options.locator('[data-i18n="settingsDesc"]')).toHaveText(language === 'en'
      ? 'Manage your Cookbook master password, language, and storage.'
      : 'Quản lý mật khẩu chủ, ngôn ngữ và dung lượng Sách nấu ăn.');
    await expect(options.locator(`#lang-radio [data-lang="${language}"]`)).toHaveAttribute('aria-checked', 'true');
    await expect(options.locator('#quota-pct-label')).toHaveText(/^\d+% used$/);
    const bytes = await options.evaluate(() => chrome.storage.sync.getBytesInUse(null));
    expect(bytes).toBeGreaterThan(0);
    await expect(options.locator('#quota-abs-label')).toHaveText(`${(bytes / 1024).toFixed(1)} / 100 KB`);
    await options.click('#btn-show-change-pwd');
    await expect(options.locator('#change-password-form')).toBeVisible();
    await expect(options.locator('#old-password')).toBeFocused();
    await options.fill('#old-password', 'SyntheticCancelledOnly!');
    await options.click('#btn-cancel-change-pwd');
    await expect(options.locator('#change-password-form')).toBeHidden();
    await expect(options.locator('#old-password')).toHaveValue('');
    await Promise.all([
      options.waitForEvent('dialog').then(async dialog => {
        expect(dialog.type()).toBe('confirm');
        await dialog.dismiss();
      }),
      options.click('#btn-reset-everything'),
    ]);
    expect(await options.evaluate(() => chrome.storage.sync.get(null))).toEqual({ ...before, language });
    expect(await options.evaluate(() => chrome.storage.session.get('sessionKey'))).toHaveProperty('sessionKey');
    if (index < languages.length - 1) {
      await Promise.all([options.waitForEvent('load'), options.click(`#lang-radio [data-lang="${languages[index + 1]}"]`)]);
    }
  }
  expect(await options.evaluate(() => chrome.storage.sync.get('displaySettings'))).toEqual({ displaySettings: legacy });
  expect(errors).toEqual([]);
});
