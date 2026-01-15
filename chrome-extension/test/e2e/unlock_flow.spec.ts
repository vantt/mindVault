
import { test, expect } from './fixtures';
import { setupExtension } from './test-utils';

test('Unlock Flow: Re-open Popup and Unlock', async ({ page, extensionId }) => {
  // 1. Setup Extension first
  const optionsPage = await setupExtension(page, extensionId);
  await optionsPage.close(); // Close options page
  
  // 2. Re-open Popup
  // Wait a bit to ensure storage is synced (though usually immediate in local)
  await page.waitForTimeout(500); 
  await page.goto(`chrome-extension://${extensionId}/popup/popup.html`);
  
  // 3. Verify it is UNLOCKED initially (since we just setup)
  // Check if we hit the "Locked" state by mistake
  if (await page.locator('#status-locked').isVisible()) {
      console.log("Unexpected: Locked state found. Session key might be missing.");
  }

  await expect(page.locator('#status-unlocked')).toBeVisible({ timeout: 10000 });
  
  // 4. Lock it manually
  await page.click('#btn-lock');
  // Popup closes on lock usually (window.close())
  // So 'page' is now closed/invalid.
  
  // Re-open Popup in a NEW page
  const newPage = await page.context().newPage();
  await newPage.goto(`chrome-extension://${extensionId}/popup/popup.html`);
  
  // 5. Verify Locked State
  await expect(newPage.locator('#status-locked')).toBeVisible();
  
  // 6. Unlock
  await newPage.fill('#unlock-password', 'StrongPass123!');
  await newPage.click('#btn-unlock'); // Or Enter
  
  // 7. Verify Unlocked
  await expect(newPage.locator('#status-unlocked')).toBeVisible({ timeout: 10000 });
});
