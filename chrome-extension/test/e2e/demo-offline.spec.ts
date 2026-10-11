import { test, expect } from './fixtures';
import { readFileSync } from 'fs';
import type { Page } from '@playwright/test';

const locale = (language: string) => JSON.parse(readFileSync(`src/_locales/${language}/messages.json`, 'utf8'));

for (const language of ['en', 'vi']) {
  for (const width of [400, 1200]) {
    test(`static demo ${language} at ${width}px loads offline and closes its own tab`, async ({ page, context, extensionId }) => {
      const errors: string[] = [];
      const external: string[] = [];
      await context.route(/^https?:\/\//, route => route.abort());
      await page.goto(`chrome-extension://${extensionId}/popup/popup.html`);
      await page.evaluate(language => chrome.storage.sync.set({ language }), language);
      const opened = context.waitForEvent('page');
      await page.evaluate(() => chrome.tabs.create({ url: chrome.runtime.getURL('demo/demo.html') }));
      const demo = await opened;
      demo.on('pageerror', error => errors.push(error.message));
      demo.on('request', request => { if (/^https?:/.test(request.url())) external.push(request.url()); });
      await demo.setViewportSize({ width, height: 900 });
      await expect(demo.locator('html')).toHaveAttribute('lang', language);
      const messages = locale(language);
      await expect(demo.locator('h1')).toHaveText(messages.demoHeroTitle.message);
      await expect(demo.locator('#section-daily-use')).toContainText(messages.demoStepCopyBody.message);
      // All translated DOM text and the image alt must match the stored preference.
      const rendered = await demo.locator('[data-i18n], [data-i18n-alt]').evaluateAll(elements => elements.map(element => ({
        key: (element as HTMLElement).dataset.i18n || (element as HTMLElement).dataset.i18nAlt,
        text: element.hasAttribute('data-i18n-alt') ? element.getAttribute('alt') : element.textContent,
      })));
      for (const item of rendered) expect(item.text).toBe(messages[item.key!].message);
      await expect(demo.locator('img')).toHaveJSProperty('complete', true);
      expect(await demo.locator('img').evaluate((image: HTMLImageElement) => image.naturalWidth)).toBeGreaterThan(0);
      expect(await demo.evaluate(() => document.documentElement.scrollWidth)).toBeLessThanOrEqual(width);
      await demo.locator('.demo-toc-chip').last().click();
      await expect(demo.locator('#section-daily-use')).toBeInViewport();
      await expect(demo.locator('#btn-close-demo')).toBeVisible();
      expect(external).toEqual([]);
      expect(errors).toEqual([]);
      const closed = demo.waitForEvent('close');
      await demo.locator('#btn-close-demo').click();
      await closed;
    });
  }
}

async function realSetup(page: Page, extensionId: string) {
  await page.goto(`chrome-extension://${extensionId}/popup/popup.html`);
  const opened = page.context().waitForEvent('page');
  await page.locator('#btn-start-setup').click();
  const options = await opened;
  await options.waitForLoadState();
  await options.fill('#setup-password', 'SyntheticDemoCooking123!');
  await options.fill('#setup-confirm', 'SyntheticDemoCooking123!');
  await options.check('#setup-backup-confirm');
  await options.click('#setup-form button[type="submit"]');
  await expect(options.locator('#toast')).toHaveText(locale('en').toastSetupComplete.message, { timeout: 15000 });
  await expect(options.locator('#dashboard-section')).toBeVisible();
  await options.close();
  await page.reload();
  await expect(page.locator('#status-unlocked')).toBeVisible();
}

test('both genuine popup entry controls open the bundled guide and both close controls work', async ({ page, context, extensionId }) => {
  test.setTimeout(60000);
  await context.route(/^https?:\/\//, route => route.abort());
  await realSetup(page, extensionId);
  await expect(page.locator('#quick-start-panel')).toBeVisible();
  let opened = context.waitForEvent('page');
  await page.locator('#btn-qs-learn-more').click();
  let demo = await opened;
  await expect(demo).toHaveURL(`chrome-extension://${extensionId}/demo/demo.html`);
  await expect(demo.locator('h1')).toHaveText(locale('en').demoHeroTitle.message);
  let closed = demo.waitForEvent('close');
  await demo.locator('#btn-close-top').click();
  await closed;
  await expect(page.locator('#quick-start-panel')).toBeHidden();
  await page.locator('#btn-build-recipe').click();
  await expect(page.locator('#status-builder')).toBeVisible();
  await expect(page.locator('#bld-more-options')).not.toHaveAttribute('open', '');
  opened = context.waitForEvent('page');
  await page.locator('#btn-how-it-works').click();
  demo = await opened;
  await expect(demo).toHaveURL(`chrome-extension://${extensionId}/demo/demo.html`);
  await expect(demo.locator('h1')).toHaveText(locale('en').demoHeroTitle.message);
  closed = demo.waitForEvent('close');
  await demo.locator('#btn-close-demo').click();
  await closed;
});
