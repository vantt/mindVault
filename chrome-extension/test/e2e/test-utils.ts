
import { Page, expect } from '@playwright/test';

export async function setupExtension(page: Page, extensionId: string) {
  // 1. Open Popup
  await page.goto(`chrome-extension://${extensionId}/popup/popup.html`);
  
  // 2. Click "Start Setup" (opens options page)
  const pagePromise = page.context().waitForEvent('page');
  const setupBtn = page.locator('#btn-start-setup');
  // Wait for button to be visible just in case
  await setupBtn.waitFor();
  await setupBtn.click();
  
  const optionsPage = await pagePromise;
  await optionsPage.waitForLoadState();

  // 3. Setup Master Password
  await optionsPage.fill('#setup-password', 'StrongPass123!');
  await optionsPage.fill('#setup-confirm', 'StrongPass123!');
  await optionsPage.check('#setup-backup-confirm');
  await optionsPage.click('#setup-form button[type="submit"]');

  // Verify Setup Complete
  await expect(optionsPage.locator('#toast')).toContainText('Setup Complete');
  
  // 4. Configure Secret #1 (Prefix)
  await expect(optionsPage.locator('#dashboard-section')).toBeVisible();
  await optionsPage.fill('input.secret-input[data-index="1"]', 'MySecretPrefix');
  
  // Save Secrets
  await optionsPage.click('#secrets-form button[type="submit"]');
  await expect(optionsPage.locator('#toast')).toContainText('Saved');
  
  // Close Options Page to return focus/context if needed, or return it?
  // Usually we want to verify Popup state after setup, so we might want to keep it or just return.
  // We'll leave it open but return the page handle if useful.
  return optionsPage;
}
