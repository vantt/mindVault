
import { test, expect } from './fixtures';

test('Popup loads and shows correct title', async ({ page, extensionId }) => {
  await page.goto(`chrome-extension://${extensionId}/popup/popup.html`);
  await expect(page.locator('h1')).toHaveText('🔐 mindVault');
});

test('Should show Setup state initially', async ({ page, extensionId }) => {
    await page.goto(`chrome-extension://${extensionId}/popup/popup.html`);
    // Assuming fresh profile, it should show Setup Required
    await expect(page.locator('#status-setup')).toBeVisible();
});
