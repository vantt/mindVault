/**
 * E2E — Recipe Verification Tag Mismatch Flow
 *
 * Validates the v2.2 mismatch detection end-to-end via the service worker's
 * message contract. Three scenarios:
 *   1. Tagged recipe + correct sheetId → success
 *   2. Tagged recipe + wrong sheetId   → {success: false, code: 'RECIPE_MISMATCH'}
 *   3. Untagged recipe + sheetId       → success with warning: 'legacy_no_tag'
 *
 * Messages are sent from an extension page (options) because
 * `chrome.runtime.sendMessage` invoked inside the SW itself cannot reach the
 * SW's own listener (no other receiving end). Toast UI rendering is covered
 * by manual QA (Shadow DOM through Playwright is brittle).
 */

import { test, expect } from './fixtures';
import { setupExtension } from './test-utils';

const SHEET_A = 'test-sheet-A';
const SHEET_B = 'test-sheet-B';

test.describe('Recipe Verification Tag — Mismatch Flow', () => {
    test('tag verify catches sheet mismatch + legacy warning works', async ({ page, extensionId }) => {
        // 1. Setup extension with master password + Secret #1 in Default profile
        const optionsPage = await setupExtension(page, extensionId);
        // Reuse optionsPage as our "extension page context" — chrome.runtime.sendMessage
        // from here reaches the SW listener (different context from SW itself).
        const ext = optionsPage;

        // 2. Compute a valid tag for (recipe="r4nd0m#1", secret="MySecretPrefix", sheetId=SHEET_A)
        const tagResp: any = await ext.evaluate(async (sheetId) => {
            return await chrome.runtime.sendMessage({
                action: 'COMPUTE_RECIPE_TAG',
                recipeFields: { hash: 'r4nd0m', position: '#', secretIndex: 1, modifiers: [] },
                sheetId,
                profileName: 'Default',
            });
        }, SHEET_A);
        expect(tagResp.success).toBe(true);
        expect(tagResp.tag).toMatch(/^[abcdefghijkmnpqrstuvwxyz23456789]{4}$/);
        const taggedRecipe = `r4nd0m#1.${tagResp.tag}`;

        // 3. SCENARIO 1: tagged recipe + correct sheetId → success
        const okResp: any = await ext.evaluate(async ({ text, sheetId }) => {
            return await chrome.runtime.sendMessage({ action: 'GENERATE_PASSWORD', text, sheetId });
        }, { text: taggedRecipe, sheetId: SHEET_A });
        expect(okResp.success).toBe(true);
        expect(okResp.password).toBe('MySecretPrefixr4nd0m');
        expect(okResp.warning).toBeUndefined();

        // 4. SCENARIO 2: tagged recipe + WRONG sheetId → RECIPE_MISMATCH
        const mismatchResp: any = await ext.evaluate(async ({ text, sheetId }) => {
            return await chrome.runtime.sendMessage({ action: 'GENERATE_PASSWORD', text, sheetId });
        }, { text: taggedRecipe, sheetId: SHEET_B });
        expect(mismatchResp.success).toBe(false);
        expect(mismatchResp.code).toBe('RECIPE_MISMATCH');

        // 5. SCENARIO 3: untagged recipe + sheetId → success + legacy_no_tag warning
        const legacyResp: any = await ext.evaluate(async ({ text, sheetId }) => {
            return await chrome.runtime.sendMessage({ action: 'GENERATE_PASSWORD', text, sheetId });
        }, { text: 'r4nd0m#1', sheetId: SHEET_A });
        expect(legacyResp.success).toBe(true);
        expect(legacyResp.password).toBe('MySecretPrefixr4nd0m');
        expect(legacyResp.warning).toBe('legacy_no_tag');

        // 6. COOKBOOK CANARY: untagged and tagged produce IDENTICAL password
        expect(legacyResp.password).toBe(okResp.password);
    });
});
