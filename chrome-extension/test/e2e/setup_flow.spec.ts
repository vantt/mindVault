import { test, expect } from './fixtures';
import { setupExtension } from './test-utils';

test('Setup Flow: Configure Master Password and Secrets', async ({ page, extensionId }) => {
  // Genuine popup Start Setup, password/backup form, modal Save and persisted
  // Spice #1 are asserted by the shared current-UI helper without session injection.
  const options = await setupExtension(page, extensionId);
  await expect(options.locator('#dashboard-section')).toBeVisible();
  const cooked = await options.evaluate(() => chrome.runtime.sendMessage({
    action: 'GENERATE_PASSWORD', text: 'r4nd0m#1', profileName: 'Default',
  }));
  expect(cooked.success).toBe(true);
  expect(cooked.password).toBe('MySecretPrefixr4nd0m');
});
