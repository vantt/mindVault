import { test, expect } from './fixtures';
import { setupExtension } from './test-utils';

test('Unlock Flow: Re-open Popup and Unlock', async ({ page, extensionId }) => {
  const options = await setupExtension(page, extensionId);
  await options.close();
  await page.goto(`chrome-extension://${extensionId}/popup/popup.html`);
  await expect(page.locator('#status-unlocked')).toBeVisible({ timeout: 10000 });

  const context = page.context();
  const closed = page.waitForEvent('close');
  await page.locator('#btn-lock').click();
  await closed;
  const reopened = await context.newPage();
  await reopened.goto(`chrome-extension://${extensionId}/popup/popup.html`);
  await expect(reopened.locator('#status-locked')).toBeVisible();
  await expect(reopened.locator('#status-unlocked')).toBeHidden();
  await reopened.locator('#unlock-password').fill('StrongPass123!');
  await reopened.locator('#btn-unlock').click();
  await expect(reopened.locator('#status-unlocked')).toBeVisible({ timeout: 10000 });
  expect(await reopened.evaluate(() => chrome.runtime.sendMessage({
    action: 'GENERATE_PASSWORD', text: 'r4nd0m#1', profileName: 'Default',
  }))).toMatchObject({ success: true, password: 'MySecretPrefixr4nd0m' });
});
