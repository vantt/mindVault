import { test, expect } from './fixtures';
import type { Page } from '@playwright/test';
import jsQR from 'jsqr';
import { serializeBundleForQr, deserializeBundleFromQr } from '../../src/adapters/infrastructure/qr-serialization.js';

// Existing setup_flow/test-utils pattern, adapted to the current profile modal.
// Golden legacy test helpers retain their original selectors and assertions.
async function setupCookbook(page: Page, extensionId: string, large: boolean) {
    await page.goto(`chrome-extension://${extensionId}/popup/popup.html`);
    const opened = page.context().waitForEvent('page');
    await page.locator('#btn-start-setup').click();
    const options = await opened;
    await options.waitForLoadState();
    await options.fill('#setup-password', 'SyntheticCooking123!');
    await options.fill('#setup-confirm', 'SyntheticCooking123!');
    await options.check('#setup-backup-confirm');
    await options.click('#setup-form button[type="submit"]');
    await expect(options.locator('#dashboard-section')).toBeVisible({ timeout: 15000 });
    // Synthetic readiness marker makes the asynchronous decrypt/load observable.
    await options.evaluate(async () => {
        const { encryptWithKey } = await import('../adapters/infrastructure/aes-storage-crypto-helper.js');
        const { sessionKey } = await chrome.storage.session.get('sessionKey');
        const fixture = { secrets: { '1': { base: 'SyntheticLoadingMarker' } }, settings: {} };
        await chrome.storage.sync.set({ 'profile:Default': await encryptWithKey(fixture, sessionKey) });
    });
    await options.locator('.btn-edit-secrets').first().click();
    await expect(options.locator('#modal-edit-secrets')).toBeVisible();
    // Wait for asynchronous existing profile load before writing synthetic spices.
    await expect(options.locator('#edit-secrets-inputs input[data-index="1"]')).toHaveValue('SyntheticLoadingMarker');
    for (let index = 1; index <= 5; index++) {
        await options.fill(`#edit-secrets-inputs input[data-index="${index}"]`, large ? 'S'.repeat(400) : (index === 1 ? 'SyntheticSpice' : ''));
    }
    await options.click('#btn-save-modal-secrets');
    await expect(options.locator('#modal-edit-secrets')).toBeHidden();
    return options;
}

async function showPhoneQr(options: Page) {
    await options.locator('.btn-connect-phone').first().click();
    await expect(options.locator('#modal-export')).toBeVisible();
    await options.click('#exp-next-1');
    await options.fill('#exp-sharing-pwd', 'SyntheticSharing123!');
    await options.fill('#exp-sharing-pwd-confirm', 'SyntheticSharing123!');
    await options.click('#exp-generate');
    await expect(options.locator('#exp-qr-canvas')).toBeVisible({ timeout: 20000 });
}

async function decodeVisibleQr(options: Page): Promise<string> {
    const image = await options.locator('#exp-qr-canvas').evaluate((canvas: HTMLCanvasElement) => {
        const rgba = canvas.getContext('2d')!.getImageData(0, 0, canvas.width, canvas.height).data;
        return { rgba: Array.from(rgba), width: canvas.width, height: canvas.height };
    });
    const result = jsQR(new Uint8ClampedArray(image.rgba), image.width, image.height);
    expect(result, 'independent decoder must read the actual displayed QR').not.toBeNull();
    return result!.data;
}

test('phone handoff cooks a real offline single QR and keeps JSON export intact', async ({ page, extensionId }) => {
    const options = await setupCookbook(page, extensionId, false);
    const errors: string[] = []; options.on('pageerror', error => errors.push(error.message));
    await showPhoneQr(options);
    await expect(options.locator('#exp-qr-progress')).toHaveText('QR 1/1');
    await options.click('#btn-show-qr');
    await expect(options.locator('#exp-qr-canvas')).toBeVisible();
    const original = JSON.parse(await options.locator('#exp-bundle-output').inputValue());
    expect(Array.isArray(original.encryptedData)).toBe(true);
    expect(original.type).toBe('passchef-fullaccess-share');
    const decoded = await decodeVisibleQr(options);
    expect(decoded).toBe(serializeBundleForQr(original));
    expect(deserializeBundleFromQr(decoded)).toEqual(original);
    await options.click('#exp-done');
    await expect(options.locator('#modal-export')).toBeHidden();
    await expect(options.locator('#exp-qr-canvas')).toHaveCount(0);
    await options.locator('.btn-connect-phone').first().click();
    await expect(options.locator('#exp-sheet')).toBeVisible();
    await expect(options.locator('#exp-qr-canvas')).toHaveCount(0);
    expect(errors).toEqual([]);
});

test('oversized cookbook animates fitted QR frames and clears them on close', async ({ page, extensionId }) => {
    test.setTimeout(60000);
    const options = await setupCookbook(page, extensionId, true);
    const errors: string[] = []; options.on('pageerror', error => errors.push(error.message));
    await showPhoneQr(options);
    const counter = options.locator('#exp-qr-progress');
    const total = Number(await counter.getAttribute('data-total'));
    expect(total).toBeGreaterThan(1);
    const captured = new Map<number, { bundleId: string, idx: number, total: number, payload: string }>();
    for (let attempt = 0; captured.size < total && attempt < total * 3; attempt++) {
        const frame = JSON.parse(await decodeVisibleQr(options));
        expect(frame.total).toBe(total); expect(frame.idx).toBeGreaterThanOrEqual(0); expect(frame.idx).toBeLessThan(total);
        captured.set(frame.idx, frame);
        if (captured.size < total) {
            const displayed = await counter.getAttribute('data-index');
            await expect(counter).not.toHaveAttribute('data-index', displayed!);
        }
    }
    expect(captured.size).toBe(total);
    const pretty = await options.locator('#exp-bundle-output').inputValue();
    const original = JSON.parse(pretty);
    expect(pretty).toContain('\n'); expect(Array.isArray(original.encryptedData)).toBe(true);
    const ordered = Array.from(captured.values()).sort((a, b) => a.idx - b.idx);
    expect(ordered.every(frame => frame.bundleId === original.bundleId)).toBe(true);
    const compact = ordered.map(frame => frame.payload).join('');
    expect(compact).toBe(serializeBundleForQr(original));
    expect(deserializeBundleFromQr(compact)).toEqual(original);
    await options.click('#btn-close-export');
    await expect(options.locator('#modal-export')).toBeHidden();
    await expect(options.locator('#exp-qr-canvas')).toHaveCount(0);
    await options.waitForTimeout(1700); await expect(options.locator('#exp-qr-progress')).toHaveCount(0);
    expect(errors).toEqual([]);
});
