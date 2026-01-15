
import { test, expect } from './fixtures';

test('Setup Flow: Configure Master Password and Secrets', async ({ page, extensionId }) => {
  // 1. Open Popup
  await page.goto(`chrome-extension://${extensionId}/popup/popup.html`);
  
  // 2. Click "Start Setup" (opens options page)
  const pagePromise = page.context().waitForEvent('page');
  await page.click('#btn-start-setup');
  const newPage = await pagePromise;
  await newPage.waitForLoadState();
  
  const optionsPage = newPage;
  
  // 3. Unlocks the settings (first time might not need unlock? Let's check logic)
  // Options: If no master password, it asks to "Set Master Password".
  // Let's assume the UI shows "Create New Master Password" section.
  
  // Fill Master Password
  await optionsPage.fill('#setup-password', 'StrongPass123!');
  await optionsPage.fill('#setup-confirm', 'StrongPass123!');
  await optionsPage.check('#setup-backup-confirm');
  await optionsPage.click('#setup-form button[type="submit"]');

  // Verify Success Message (Toast)
  // Toast uses inline styles, not classes for success state.
  await expect(optionsPage.locator('#toast')).toBeVisible({ timeout: 5000 });
  await expect(optionsPage.locator('#toast')).toContainText('Setup Complete');
  
  // 4. Configure Secret #1
  // Wait for Dashboard Section to be visible (implies Setup is done)
  await expect(optionsPage.locator('#dashboard-section')).toBeVisible();

  // Fill Secret #1
  await optionsPage.fill('input.secret-input[data-index="1"]', 'MySecretPrefix');
  
  // Save Secrets
  await optionsPage.click('#secrets-form button[type="submit"]');
  
  // Verify Success
  await expect(optionsPage.locator('#toast')).toContainText('Saved');
});
