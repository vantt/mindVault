import { type Page } from '@playwright/test';
import { setupCurrentCookbook } from './current-cookbook-fixture';

// Preserve StrongPass123!/MySecretPrefix through the current UI: genuine popup
// Setup, profile-modal Save, then reopen and verify persisted encrypted Spices.
export async function setupExtension(page: Page, extensionId: string) {
  return setupCurrentCookbook(page, extensionId);
}
