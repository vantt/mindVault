import { chromium } from '@playwright/test';
import { mkdtemp, rm } from 'node:fs/promises';
import { tmpdir } from 'node:os';
import { join } from 'node:path';
import { fileURLToPath } from 'node:url';

export const ORACLE_BROWSER_VERSION = '143.0.7499.4';

/** Use the repo's E2E Chromium without upgrading it or touching a user's profile. */
export async function withChromiumOracle(action, { extension = false } = {}) {
  const profile = await mkdtemp(join(tmpdir(), 'passchef-browser-oracle-'));
  let context;
  try {
    const extensionPath = fileURLToPath(new URL('../src', import.meta.url));
    context = await chromium.launchPersistentContext(profile, {
      headless: false,
      env: { ...process.env, DISPLAY: process.env.DISPLAY || ':0' },
      args: extension ? [`--disable-extensions-except=${extensionPath}`, `--load-extension=${extensionPath}`] : [],
    });
    const browserVersion = context.browser().version();
    if (browserVersion !== ORACLE_BROWSER_VERSION) {
      throw new Error(`Use repo E2E Chromium ${ORACLE_BROWSER_VERSION}; observed ${browserVersion}. Review compatibility before refreshing.`);
    }
    const page = await context.newPage();
    if (extension) {
      const worker = context.serviceWorkers()[0] || await context.waitForEvent('serviceworker');
      const extensionId = new URL(worker.url()).hostname;
      await page.goto(`chrome-extension://${extensionId}/options/options.html`);
    }
    return await action(page, browserVersion);
  } finally {
    try { if (context) await context.close(); }
    finally { await rm(profile, { recursive: true, force: true }); }
  }
}
