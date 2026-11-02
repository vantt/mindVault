import { readFileSync } from 'node:fs';
import { webcrypto } from 'node:crypto';
import { TextEncoder, TextDecoder } from 'node:util';
import { vi } from 'vitest';

const clone = value => JSON.parse(JSON.stringify(value));
export async function importWizardFixture() {
    vi.stubGlobal('crypto', webcrypto);
    vi.stubGlobal('TextEncoder', TextEncoder);
    vi.stubGlobal('TextDecoder', TextDecoder);
    document.body.innerHTML = readFileSync(`${process.cwd()}/src/options/options.html`, 'utf8');
    const key = await webcrypto.subtle.exportKey('jwk', await webcrypto.subtle.generateKey(
        { name: 'AES-GCM', length: 256 }, true, ['encrypt', 'decrypt']));
    const { encryptWithKey } = await import('../../src/adapters/infrastructure/aes-storage-crypto-helper.js');
    const store = { defaultProfile: 'Default', sheetMapping: { unrelatedMenu: 'Default' },
        'profile:Default': await encryptWithKey({ secrets: { '1': { base: 'OriginalPractice*' } }, settings: { pepperingHint: true } }, key) };
    const writes = [];
    vi.stubGlobal('chrome', { storage: {
        sync: {
            get: async keys => clone(keys === null ? store : Object.fromEntries(
                (Array.isArray(keys) ? keys : [keys]).filter(k => k in store).map(k => [k, store[k]]))),
            set: async updates => { writes.push(clone(updates)); Object.assign(store, clone(updates)); },
        },
        session: { get: async () => ({ sessionKey: clone(key) }) },
    } });
    const { openImportWizard } = await import('../../src/options/options-import-wizard.js');
    const completed = vi.fn();
    const element = id => document.getElementById(id);
    async function decrypt(bundle, password) {
        openImportWizard(key, completed);
        element('imp-bundle-input').value = JSON.stringify(bundle);
        element('imp-bundle-input').oninput();
        element('imp-next-1').onclick();
        element('imp-sharing-pwd').value = password;
        await element('imp-decrypt').onclick();
    }
    async function confirm(name) {
        element('imp-profile-name').value = name;
        await element('imp-confirm').onclick();
    }
    return { key, store, writes, completed, element, decrypt, confirm };
}
