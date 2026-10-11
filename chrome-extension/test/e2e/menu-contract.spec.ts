/** Local DOM contract proof. Programmatic page-flow dispatch; no native-keyboard claim. */
import { test, expect } from './fixtures';
import { setupCurrentCookbook } from './current-cookbook-fixture';

const SHEET = 'menu-contract-fixture';
const URL = `https://docs.google.com/spreadsheets/d/${SHEET}/edit`;
const HTML = `<!doctype html><html><body>
<input id="ingredient"><textarea id="notes"></textarea>
<div contenteditable="true" id="cell"></div>
<div id="t-formula-bar-input"></div>
<div class="grid-container" tabindex="0">Synthetic Cookbook grid</div>
</body></html>`;

test('Menu extraction cooks exact dishes from input, textarea, editable cell and formula bar', async ({page, extensionId}) => {
  const extension = await setupCurrentCookbook(page, extensionId);
  await page.route(URL, route => route.fulfill({contentType:'text/html',body:HTML}));
  await page.goto(URL);
  const tabId = await extension.evaluate(async () => {
    const [tab] = await chrome.tabs.query({url:'https://docs.google.com/spreadsheets/d/menu-contract-fixture/*'});
    return tab.id;
  });
  // sendMessage to the automatically injected content script proves the contract only.
  const trigger = () => extension.evaluate(async id => {
    await chrome.tabs.sendMessage(id, {action:'TRIGGER_HOTKEY'});
  }, tabId);
  const dish = page.locator('.passchef-popup input');
  for (const [selector, recipe, expected] of [
    ['#ingredient','  input#1  ','MySecretPrefixinput'],
    ['#notes','notes$1','notesMySecretPrefix'],
    ['#cell','cell#1','MySecretPrefixcell'],
  ]) {
    await page.locator(selector).fill(recipe);
    await page.locator(selector).focus();
    await trigger();
    await expect(dish).toHaveValue(expected);
    await page.keyboard.press('Escape');
    await expect(dish).toHaveCount(0);
  }
  await page.locator('#t-formula-bar-input').evaluate(el => { el.textContent='bar#1'; });
  await page.locator('.grid-container').focus();
  await trigger();
  await expect(dish).toHaveValue('MySecretPrefixbar');
  await page.keyboard.press('Escape');
  // Google's focused rich-text editor can contain only newlines while the
  // selected cell's actual Recipe is visible in the formula bar.
  await page.locator('#cell').fill('\n\n\n');
  await page.locator('#cell').focus();
  await trigger();
  await expect(dish).toHaveValue('MySecretPrefixbar');
  await page.keyboard.press('Escape');
  await page.locator('#t-formula-bar-input').evaluate(el => { el.textContent=''; });
  await page.locator('#ingredient').fill('');
  await page.locator('#ingredient').focus();
  await trigger();
  await expect(dish).toHaveCount(0);
  await page.locator('#ingredient').fill('ordinary non-recipe text');
  await trigger();
  await expect(page.locator('.passchef-toast')).toBeVisible();
  await expect(dish).toHaveCount(0);
});

test('Menu tag mismatch refuses a dish in the real content UI; matched tag preserves dish', async ({page, extensionId}) => {
  const extension = await setupCurrentCookbook(page, extensionId);
  await page.route(URL, route => route.fulfill({contentType:'text/html',body:HTML}));
  await page.goto(URL);
  const tabId = await extension.evaluate(async () => {
    const [tab] = await chrome.tabs.query({url:'https://docs.google.com/spreadsheets/d/menu-contract-fixture/*'});
    return tab.id;
  });
  const tag = async (sheetId: string) => extension.evaluate(async sheetId => {
    const response = await chrome.runtime.sendMessage({action:'COMPUTE_RECIPE_TAG',
      recipeFields:{hash:'canary',position:'#',secretIndex:1,modifiers:[]},sheetId,profileName:'Default'});
    if (!response.success) throw new Error('Tag computation rejected');
    return response.tag;
  }, sheetId);
  const wrong = await tag('another-synthetic-cookbook');
  await page.locator('#ingredient').fill(`canary#1.${wrong}`);
  await page.locator('#ingredient').focus();
  await extension.evaluate(async id => chrome.tabs.sendMessage(id,{action:'TRIGGER_HOTKEY'}),tabId);
  const mismatch = await extension.evaluate(() => chrome.i18n.getMessage('errRecipeMismatch'));
  await expect(page.locator('.passchef-toast')).toContainText(mismatch);
  await expect(page.locator('.passchef-popup input')).toHaveCount(0);
  const matched = await tag(SHEET);
  await page.locator('#ingredient').fill(`canary#1.${matched}`);
  await page.locator('#ingredient').focus();
  await extension.evaluate(async id => chrome.tabs.sendMessage(id,{action:'TRIGGER_HOTKEY'}),tabId);
  await expect(page.locator('.passchef-popup input')).toHaveValue('MySecretPrefixcanary');
});
