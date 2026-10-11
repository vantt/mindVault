import { test, expect } from './fixtures';
import type { Page } from '@playwright/test';

async function setup(page: Page, extensionId: string) {
  await page.goto(`chrome-extension://${extensionId}/options/options.html`);
  await page.locator('#setup-password').fill('SyntheticStrong123!');
  await page.locator('#setup-confirm').fill('SyntheticStrong123!');
  await page.locator('#setup-backup-confirm').check();
  await page.locator('#setup-form button[type="submit"]').click();
  await expect(page.locator('#dashboard-section')).toBeVisible({ timeout: 20000 });
}
const request = (page: Page, action: string, values = {}) => page.evaluate(
  ({ action, values }) => chrome.runtime.sendMessage({ action, ...values }), { action, values });

test('real Argon2 auth persists five failures across popup close and actual worker restart', async ({ page, context, extensionId }) => {
  test.setTimeout(60000);
  await setup(page, extensionId);
  const before = await page.evaluate(() => chrome.storage.sync.get(null));
  await request(page, 'AUTH_LOCK');
  const popup = await context.newPage();
  await popup.goto(`chrome-extension://${extensionId}/popup/popup.html`);
  for (let i = 0; i < 5; i++) {
    const client = i < 2 ? popup : page;
    await client.fill('#unlock-password', 'SyntheticWrong!');
    await client.click(i < 2 ? '#btn-unlock' : '#unlock-form button[type="submit"]');
    const error = client.locator(i < 2 ? '#unlock-error' : '#toast');
    await expect(error).toContainText(i === 4 ? 'Too many attempts' : `Invalid Password. ${4 - i} attempts remaining.`);
    expect((await page.evaluate(() => chrome.storage.local.get('authState'))).authState.failures).toBe(i + 1);
    if (i === 1) await popup.close();
  }
  await expect(page.locator('#unlock-form button[type="submit"]')).toBeDisabled();
  const cdp = await context.newCDPSession(page);
  const versions: any[] = [];
  cdp.on('ServiceWorker.workerVersionUpdated', event => versions.push(...event.versions));
  await cdp.send('ServiceWorker.enable');
  await expect.poll(() => versions.find(v => v.scriptURL === `chrome-extension://${extensionId}/service_worker.js`)?.versionId).toBeTruthy();
  const version = versions.filter(v => v.scriptURL === `chrome-extension://${extensionId}/service_worker.js`).at(-1);
  await cdp.send('ServiceWorker.stopWorker', { versionId: version.versionId });
  await expect.poll(() => versions.some(v => v.versionId === version.versionId && v.runningStatus === 'stopped')).toBe(true);
  const denied = await request(page, 'AUTH_UNLOCK', { password: 'SyntheticStrong123!' });
  expect(denied.code).toBe('LOCKED_OUT');
  expect(denied.retryAfterMs).toBeGreaterThan(280000);
  expect(await page.evaluate(() => chrome.storage.sync.get(null))).toEqual(before);
  expect((await page.evaluate(() => chrome.storage.session.get('sessionKey'))).sessionKey).toBeUndefined();
  await cdp.detach();
});

test('two real Options clients cannot overwrite the first encrypted Cookbook', async ({ page, context, extensionId }) => {
  test.setTimeout(30000);
  const other = await context.newPage();
  await Promise.all([page.goto(`chrome-extension://${extensionId}/options/options.html`), other.goto(`chrome-extension://${extensionId}/options/options.html`)]);
  const results = await Promise.all([request(page, 'AUTH_SETUP', { password: 'SyntheticFirst123!' }), request(other, 'AUTH_SETUP', { password: 'SyntheticSecond123!' })]);
  expect(results.filter(result => result.success)).toHaveLength(1);
  expect(['AUTH_BUSY', 'VAULT_EXISTS']).toContain(results.find(result => !result.success).code);
  const before = await page.evaluate(() => chrome.storage.sync.get(null));
  expect((await request(other, 'AUTH_SETUP', { password: 'SyntheticStale123!' })).code).toBe('VAULT_EXISTS');
  expect(await page.evaluate(() => chrome.storage.sync.get(null))).toEqual(before);
  await request(page, 'AUTH_LOCK');
  const winner = results[0].success ? 'SyntheticFirst123!' : 'SyntheticSecond123!';
  expect(await request(page, 'AUTH_UNLOCK', { password: winner })).toEqual({ success: true });
});

test('actual system clipboard compare-and-clear works after copying page closes', async ({ page, context, extensionId }) => {
  test.setTimeout(95000);
  await setup(page, extensionId);
  const reader = await context.newPage();
  await reader.goto(`chrome-extension://${extensionId}/options/options.html`);
  const read = async () => { await reader.bringToFront(); return reader.evaluate(async () => {
    const input = document.createElement('textarea'); document.body.append(input); input.focus(); input.select();
    if (!document.execCommand('paste')) throw new Error('Actual clipboard read unavailable');
    const value = input.value; input.remove(); return value;
  }); };
  expect(await request(page, 'COPY_DISH', { text: 'SyntheticDishClipboard123!' })).toEqual({ success: true });
  await page.close();
  expect(await read()).toBe('SyntheticDishClipboard123!');
  await expect.poll(read, { timeout: 35000, intervals: [1000] }).toBe('');
  expect(await request(reader, 'COPY_DISH', { text: 'SyntheticSecondDish!' })).toEqual({ success: true });
  await reader.bringToFront();
  await reader.evaluate(() => { const input = document.createElement('textarea'); document.body.append(input);
    input.value = 'SyntheticUserLaterText'; input.focus(); input.select();
    if (!document.execCommand('copy')) throw new Error('Actual clipboard write unavailable'); input.remove(); });
  await reader.waitForTimeout(31000);
  expect(await read()).toBe('SyntheticUserLaterText');
  await reader.evaluate(() => { const input = document.createElement('textarea'); document.body.append(input);
    input.focus(); input.select();
    const clear = event => { event.clipboardData.setData('text/plain', ''); event.preventDefault(); };
    document.addEventListener('copy', clear, { once: true }); document.execCommand('copy'); input.remove(); });
});
