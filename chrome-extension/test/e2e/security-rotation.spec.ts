import { test, expect } from './fixtures';
import { setupCurrentCookbook } from './current-cookbook-fixture';

test('real password rotation preserves Spices, retires old key and refuses wrong password mutation', async ({ page, extensionId }) => {
    test.setTimeout(45000);
    const options = await setupCurrentCookbook(page, extensionId);
    const before = await options.evaluate(() => chrome.storage.sync.get(null));
    await options.locator('.tab[data-tab="settings"]').click();
    await options.locator('#btn-show-change-pwd').click();
    async function fill(oldPassword: string) {
        await options.fill('#old-password', oldPassword);
        await options.fill('#new-password', 'SyntheticRotated123!');
        await options.fill('#confirm-new-password', 'SyntheticRotated123!');
        await options.check('#change-backup-confirm');
        await options.locator('#change-password-form button[type="submit"]').click();
    }
    await fill('SyntheticWrong!');
    await expect(options.locator('#toast')).toContainText('Invalid Password');
    expect(await options.evaluate(() => chrome.storage.sync.get(null))).toEqual(before);
    await fill('StrongPass123!');
    await expect(options.locator('#toast')).toContainText('Password Updated Successfully!', { timeout: 20000 });
    await expect(options.locator('#dashboard-section')).toBeVisible();
    const cooked = await options.evaluate(() => chrome.runtime.sendMessage({ action: 'GENERATE_PASSWORD', text: 'Dish#1' }));
    expect(cooked).toMatchObject({ success: true, password: 'MySecretPrefixDish' });
    const after = await options.evaluate(() => chrome.storage.sync.get(null));
    expect(after.salt).not.toEqual(before.salt);
    await options.evaluate(() => chrome.runtime.sendMessage({ action: 'AUTH_LOCK' }));
    expect((await options.evaluate(() => chrome.runtime.sendMessage({ action: 'AUTH_UNLOCK', password: 'StrongPass123!' }))).code).toBe('WRONG_PASSWORD');
    expect(await options.evaluate(() => chrome.storage.sync.get(null))).toEqual(after);
    expect(await options.evaluate(() => chrome.runtime.sendMessage({ action: 'AUTH_UNLOCK', password: 'SyntheticRotated123!' }))).toEqual({ success: true });
});
