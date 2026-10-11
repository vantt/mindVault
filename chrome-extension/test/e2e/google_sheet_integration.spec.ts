import { test, expect } from './fixtures';
import { readFileSync } from 'fs';
import { setupCurrentCookbook } from './current-cookbook-fixture';

const MENU = 'https://docs.google.com/spreadsheets/d/mock-id/edit';

test('Menu DOM/message contract: input and formula-bar extraction present exact Dishes', async ({ page, context, extensionId }) => {
  // Actual setup/modal Save/reopen persistence uses the original Basic* fixture.
  await setupCurrentCookbook(page, extensionId, 'Basic*');

  // Local provider DOM contract only. Native shortcut + live Google proof is
  // separate: never silently replace a failed keyboard shortcut with a message.
  const mock = readFileSync('test/fixtures/mock-sheet.html', 'utf8');
  await page.route(MENU, route => route.fulfill({ status: 200, contentType: 'text/html', body: mock }));
  await page.goto(MENU);
  const worker = context.serviceWorkers().find(w => w.url() === `chrome-extension://${extensionId}/service_worker.js`);
  expect(worker).toBeTruthy();
  const trigger = () => worker!.evaluate(async url => {
    const tabs = await chrome.tabs.query({ url });
    if (tabs.length !== 1) throw new Error('Expected exactly one synthetic Menu tab');
    // Manifest naturally injects every production content module; no scripting
    // permission, duplicate injection or test-only presentation implementation.
    await chrome.tabs.sendMessage(tabs[0].id, { action: 'TRIGGER_HOTKEY' });
  }, MENU);
  const dish = page.locator('div.passchef-popup input[type="text"]');

  await page.locator('#generic-input').fill('r4nd0m#1');
  await page.locator('#generic-input').focus();
  await trigger();
  await expect(page.locator('div.passchef-popup')).toBeVisible();
  await expect(dish).toHaveValue('Basic*r4nd0m');
  await page.keyboard.press('Escape');
  await expect(page.locator('div.passchef-popup')).toHaveCount(0);

  await page.locator('#generic-input').fill('');
  await page.locator('#t-formula-bar-input').evaluate(element => { element.textContent = 'formulaRecipe#1'; });
  await page.locator('.grid-container').click();
  await expect(page.locator('.grid-container')).toBeFocused();
  await trigger();
  await expect(page.locator('div.passchef-popup')).toBeVisible();
  await expect(dish).toHaveValue('Basic*formulaRecipe');
});
