/** Real Chrome action popover UI; routed Menu DOM is explicitly a contract fixture. */
import fs from 'node:fs';
import { test, expect } from './fixtures';
import { setupCurrentCookbook } from './current-cookbook-fixture';
import { openActualToolbar } from './actual-toolbar-popover';

const labels = (locale: string) => JSON.parse(fs.readFileSync(`src/_locales/${locale}/messages.json`, 'utf8'));
const visible = (id: string) => `(() => {
  const element = document.getElementById(${JSON.stringify(id)});
  const rect = element?.getBoundingClientRect();
  return Boolean(element?.checkVisibility({checkVisibilityCSS:true, checkOpacity:true}) && rect.width > 0 && rect.height > 0);
})()`;
const text = (id: string) => `document.getElementById(${JSON.stringify(id)}).textContent.trim()`;
const fixtureHtml = '<!doctype html><title>Popup Cookbook fixture - Google Sheets</title><input id="cell">';

test('actual toolbar Quick Start dismisses once; Home hints and controls localize in EN/VI', async ({page, context, extensionId}) => {
  test.setTimeout(60000);
  const options = await setupCurrentCookbook(page, extensionId);
  for (const locale of ['en', 'vi']) {
    const messages = labels(locale);
    await options.evaluate(async locale => {
      await chrome.storage.sync.set({language: locale});
      await chrome.storage.local.remove('hasSeenQuickStart');
    }, locale);
    await page.goto('about:blank');
    await page.bringToFront();
    let popup = await openActualToolbar(context, extensionId);
    await expect.poll(() => popup.evaluate(visible('status-unlocked'))).toBe(true);
    await expect.poll(() => popup.evaluate(visible('quick-start-panel'))).toBe(true);
    expect(await popup.evaluate(visible('home-notice'))).toBe(false);
    expect(await popup.evaluate(visible('home-sheet-context'))).toBe(false);
    expect(await popup.evaluate(text('home-hint'))).toBe(messages.hintClickCell.message);
    expect(await popup.evaluate(text('btn-build-recipe'))).toBe(messages.btnCreateFirstRecipe.message);
    await popup.evaluate('document.getElementById("btn-qs-got-it").click()');
    await expect.poll(() => popup.evaluate(visible('quick-start-panel'))).toBe(false);
    expect(await options.evaluate(async () => (await chrome.storage.local.get('hasSeenQuickStart')).hasSeenQuickStart)).toBe(true);
    await popup.close();
    popup = await openActualToolbar(context, extensionId);
    await expect.poll(() => popup.evaluate(visible('status-unlocked'))).toBe(true);
    expect(await popup.evaluate(visible('quick-start-panel'))).toBe(false);
    expect(await popup.evaluate(text('btn-build-recipe'))).toBe(messages.btnBuildRecipe.message);
    await popup.close();
  }
});

test('actual toolbar empty cell stays Home; Generated Back re-reads the focused Recipe', async ({page, context, extensionId}) => {
  test.setTimeout(60000);
  await setupCurrentCookbook(page, extensionId);
  const url = 'https://docs.google.com/spreadsheets/d/popup-acceptance-fixture/edit';
  await page.route(url, route => route.fulfill({contentType:'text/html', body:fixtureHtml}));
  await page.goto(url);
  await page.locator('#cell').focus();
  await page.bringToFront();
  let popup = await openActualToolbar(context, extensionId);
  await expect.poll(() => popup.evaluate(visible('home-notice'))).toBe(true);
  expect(await popup.evaluate(visible('status-unlocked'))).toBe(true);
  expect(await popup.evaluate(text('home-notice-text'))).toBe(labels('en').hintEmptyCell.message);
  expect(await popup.evaluate(text('home-sheet-context'))).toContain('Popup Cookbook fixture');
  expect(await popup.evaluate(text('home-hint'))).toBe('');
  await popup.close();
  await page.locator('#cell').fill('first#1');
  await page.locator('#cell').focus();
  popup = await openActualToolbar(context, extensionId);
  await expect.poll(() => popup.evaluate(visible('status-generated'))).toBe(true);
  expect(await popup.evaluate('document.getElementById("gen-password").value')).toBe('MySecretPrefixfirst');
  expect(await popup.evaluate(text('global-status-text'))).toBe(labels('en').statusReady.message);
  // Mutate only the synthetic provider DOM, without focusing another browser tab.
  await page.evaluate(() => { (document.querySelector('#cell') as HTMLInputElement).value = 'second$1'; });
  await popup.evaluate('document.getElementById("btn-back").click()');
  await expect.poll(() => popup.evaluate('document.getElementById("gen-password").value')).toBe('secondMySecretPrefix');
  expect(await popup.evaluate(visible('status-generated'))).toBe(true);
  await popup.close();
});

test('actual missing content receiver shows the localized reload notice without a dish', async ({page, context, extensionId}) => {
  test.setTimeout(60000);
  const options = await setupCurrentCookbook(page, extensionId);
  // Explicitly outside /spreadsheets/*; Chrome also injects on its bare directory URL.
  const url = 'https://docs.google.com/spreadsheets-missing-receiver';
  await page.route(url, route => route.fulfill({contentType:'text/html', body:fixtureHtml}));
  for (const locale of ['en', 'vi']) {
    await options.evaluate(async locale => chrome.storage.sync.set({language:locale}), locale);
    await page.goto(url);
    await page.bringToFront();
    const worker = context.serviceWorkers().find(worker => worker.url().includes(extensionId));
    if (!worker) throw new Error('Extension service worker is unavailable');
    const receiverMissing = await worker.evaluate(async url => {
      const [tab] = await chrome.tabs.query({active:true, currentWindow:true});
      if (tab?.url !== url) throw new Error('Missing-receiver fixture is not the active tab');
      try {
        await chrome.tabs.sendMessage(tab.id!, {action:'GET_CURRENT_CELL_PASSWORD'});
        return false;
      } catch (error) {
        return /Could not establish connection|Receiving end does not exist/.test(String(error));
      }
    }, url);
    expect(receiverMissing).toBe(true);
    const popup = await openActualToolbar(context, extensionId);
    await expect.poll(() => popup.evaluate(visible('home-notice'))).toBe(true);
    expect(await popup.evaluate(visible('status-unlocked'))).toBe(true);
    expect(await popup.evaluate(visible('status-generated'))).toBe(false);
    expect(await popup.evaluate(text('home-notice-text'))).toBe(labels(locale).hintNeedsReload.message);
    expect(await popup.evaluate(text('home-hint'))).toBe('');
    await popup.close();
  }
});
