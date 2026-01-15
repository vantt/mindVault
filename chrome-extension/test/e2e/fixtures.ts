
import { test as base, chromium, type BrowserContext } from '@playwright/test';
import path from 'path';
import fs from 'fs';
import os from 'os';

export const test = base.extend<{
  context: BrowserContext;
  extensionId: string;
}>({
  context: async ({ }, use) => {
    const pathToExtension = path.join(process.cwd(), 'src');
    const tempDir = await fs.promises.mkdtemp(path.join(os.tmpdir(), 'mindvault-e2e-'));
    
    console.log(`Loading extension from: ${pathToExtension}`);
    console.log(`User Data Dir: ${tempDir}`);

    const context = await chromium.launchPersistentContext(tempDir, {
      headless: false,
      args: [
        `--disable-extensions-except=${pathToExtension}`,
        `--load-extension=${pathToExtension}`,
      ],
    });
    
    await use(context);
    
    await context.close();
    // Cleanup
    try {
        await fs.promises.rm(tempDir, { recursive: true, force: true });
    } catch (e) {
        console.error(`Failed to cleanup temp dir: ${tempDir}`, e);
    }
  },
  extensionId: async ({ context }, use) => {
    // Wait for the service worker to be ready
    let [background] = context.serviceWorkers();
    if (!background)
      background = await context.waitForEvent('serviceworker');

    const extensionId = background.url().split('/')[2];
    await use(extensionId);
  },
});
export const expect = base.expect;
