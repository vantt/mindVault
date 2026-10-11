import { expect, type Page } from '@playwright/test';

export async function createOtherProfile(options: Page, spice = 'OtherSpice') {
    await options.click('#btn-new-profile');
    await options.fill('#new-profile-name', 'Other');
    await options.click('#btn-create-profile');
    await expect(options.locator('#modal-edit-secrets')).toBeVisible();
    await options.locator('#modal-edit-secrets .modal-close').click();
    // Genuine AES in the disposable profile; marker guards the modal's async load.
    await options.evaluate(async () => {
        const { encryptWithKey } = await import('../adapters/infrastructure/aes-storage-crypto-helper.js');
        const { sessionKey } = await chrome.storage.session.get('sessionKey');
        await chrome.storage.sync.set({ 'profile:Other': await encryptWithKey({
            secrets: { '1': { base: 'OtherLoadingMarker' } }, settings: {},
        }, sessionKey) });
    });
    await options.locator('.profile-card').filter({ has: options.locator('.profile-name', { hasText: /^Other$/ }) }).locator('.btn-edit-secrets').click();
    await expect(options.locator('.modal-secret-input[data-index="1"]')).toHaveValue('OtherLoadingMarker');
    await options.fill('.modal-secret-input[data-index="1"]', spice);
    await options.click('#btn-save-modal-secrets');
    await expect(options.locator('#modal-edit-secrets')).toBeHidden();
}
