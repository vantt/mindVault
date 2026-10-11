import { test, expect } from './fixtures';
import { setupCurrentCookbook } from './current-cookbook-fixture';
import { createOtherProfile } from './builder-test-utils';

test('actual profile modal clears on rapid close/reopen and authoritative lock without changing ciphertext', async ({ page, context, extensionId }) => {
    test.setTimeout(60000);
    const options = await setupCurrentCookbook(page, extensionId);
    await createOtherProfile(options);
    const card = (name: string) => options.locator('.profile-card').filter({ has: options.locator('.profile-name', { hasText: new RegExp(`^${name}$`) }) });
    await card('Default').locator('.btn-edit-secrets').click();
    await options.locator('#modal-edit-secrets .modal-close').click();
    await expect(options.locator('#modal-edit-secrets')).toBeHidden();
    await expect(options.locator('.modal-secret-input[data-index="1"]')).toHaveValue('');
    await card('Other').locator('.btn-edit-secrets').click();
    await expect(options.locator('.modal-secret-input[data-index="1"]')).toHaveValue('OtherSpice');
    await expect(options.locator('#edit-secrets-title .modal-title-em')).toHaveText('Other');
    const recordDigest = () => options.evaluate(async () => {
        const records = await chrome.storage.sync.get(['profile:Default', 'profile:Other']);
        const bytes = new TextEncoder().encode(JSON.stringify(records));
        return [...new Uint8Array(await crypto.subtle.digest('SHA-256', bytes))].map(byte => byte.toString(16).padStart(2, '0')).join('');
    });
    const before = await recordDigest();
    const popup = await context.newPage();
    await popup.goto(`chrome-extension://${extensionId}/popup/popup.html`);
    await expect(popup.locator('#status-unlocked')).toBeVisible();
    const closed = popup.waitForEvent('close'); await popup.locator('#btn-lock').click(); await closed;
    await expect(options.locator('#unlock-section')).toBeVisible();
    await expect(options.locator('#modal-edit-secrets')).toBeHidden();
    await expect(options.locator('.modal-secret-input[data-index="1"]')).toHaveValue('');
    await expect(options.locator('#btn-save-modal-secrets')).toBeDisabled();
    expect(await recordDigest()).toBe(before);
});
