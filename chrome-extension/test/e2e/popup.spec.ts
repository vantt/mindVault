import { test, expect } from './fixtures';

test('Popup loads and shows correct title', async ({ page, extensionId }) => {
  await page.goto(`chrome-extension://${extensionId}/popup/popup.html`);
  await expect(page).toHaveTitle('PassChef');
  // Accepted Precision layout keeps the same identity in its split wordmark.
  await expect(page.locator('#header-wordmark')).toBeVisible();
  await expect(page.locator('#header-wordmark')).toHaveText('PassChef');
});

test('Should show Setup state initially', async ({ page, extensionId }) => {
  await page.goto(`chrome-extension://${extensionId}/popup/popup.html`);
  await expect(page.locator('#status-setup')).toBeVisible();
  await expect(page.locator('#btn-start-setup')).toBeVisible();
});
