/** Current profile-modal setup. Legacy helpers/tests retained without weakening assertions. */
import { expect, type Page } from '@playwright/test';

export async function setupCurrentCookbook(page: Page, extensionId: string, spice = 'MySecretPrefix') {
  await page.goto(`chrome-extension://${extensionId}/popup/popup.html`);
  const opened = page.context().waitForEvent('page');
  await page.locator('#btn-start-setup').click();
  const options = await opened;
  await options.waitForLoadState();
  await options.locator('#setup-password').fill('StrongPass123!');
  await options.locator('#setup-confirm').fill('StrongPass123!');
  await options.locator('#setup-backup-confirm').check();
  await options.locator('#setup-form button[type="submit"]').click();
  await expect(options.locator('#toast')).toContainText('Setup Complete',{timeout:20000});
  await expect(options.locator('#dashboard-section')).toBeVisible();
  // Use genuine AES with the actual unlocked key only in this disposable test
  // profile. The marker makes completion of the asynchronous edit load observable.
  await options.evaluate(async () => {
    const { encryptWithKey } = await import('../adapters/infrastructure/aes-storage-crypto-helper.js');
    const { sessionKey } = await chrome.storage.session.get('sessionKey');
    const fixture = {secrets:{'1':{base:'SyntheticLoadingMarker'}},settings:{}};
    await chrome.storage.sync.set({'profile:Default':await encryptWithKey(fixture,sessionKey)});
  });
  await options.locator('.btn-edit-secrets').first().click();
  await expect(options.locator('#edit-secrets-inputs input[data-index="1"]')).toHaveValue('SyntheticLoadingMarker');
  await options.locator('#edit-secrets-inputs input[data-index="1"]').fill(spice);
  await options.locator('#btn-save-modal-secrets').click();
  await expect(options.locator('#modal-edit-secrets')).toBeHidden();
  await expect(options.locator('#toast')).toContainText('Secrets saved');
  await options.locator('.btn-edit-secrets').first().click();
  await expect(options.locator('#edit-secrets-inputs input[data-index="1"]')).toHaveValue(spice);
  await options.locator('#modal-edit-secrets .modal-close').click();
  await expect(options.locator('#modal-edit-secrets')).toBeHidden();
  return options;
}
