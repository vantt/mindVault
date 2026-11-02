import { beforeEach, afterEach, it, expect, vi } from 'vitest';
import { importWizardFixture } from './import-wizard-fixture.mjs';
import { ExportImportAdapter } from '../../src/adapters/infrastructure/export-import-adapter.js';
import { ChromeStorageAdapter } from '../../src/adapters/infrastructure/chrome_storage_adapter.js';
import { GeneratePassword } from '../../src/core/usecases/generate_password.js';
import { RegexParserAdapter } from '../../src/adapters/infrastructure/regex_parser_adapter.js';
import { decryptWithKey } from '../../src/adapters/infrastructure/aes-storage-crypto-helper.js';
import { computeRecipeTag } from '../../src/core/domain/tag-codec.js';

const spices = { '1': { base: 'Basic*' }, '2': { base: 'Practice🙂漢字' }, '3': { base: 'Public$3' },
    '4': { base: 'Fixture@4' }, '5': { base: 'Demo^5' } };
const sharingPassword = 'PublicSyntheticSharingOnly!';
const menuId = 'synthetic-full-access-import-menu';
let fixture;
beforeEach(async () => { vi.resetModules(); fixture = await importWizardFixture(); });
afterEach(() => { vi.unstubAllGlobals(); document.body.replaceChildren(); });
const cooking = () => new GeneratePassword(new RegexParserAdapter(), new ChromeStorageAdapter());

it('Full Access with a Menu imports through actual handlers and cooks the exact own-profile Dish', async () => {
    const beforeDefault = JSON.stringify(fixture.store['profile:Default']);
    const bundle = await new ExportImportAdapter().createFullAccessBundle('SenderPractice', spices, menuId, sharingPassword);
    await fixture.decrypt(bundle, sharingPassword);
    await fixture.confirm('ImportedPractice');
    expect(fixture.completed).toHaveBeenCalledOnce();
    expect(fixture.element('modal-import').classList.contains('hidden')).toBe(true);
    // The regression is observable through real reader/cooking, not a success toast.
    const dish = await cooking().execute('r4nd0m#1', menuId);
    expect(dish.password).toBe('Basic*r4nd0m');
    expect(dish.profileName).toBe('ImportedPractice');
    expect(dish.isShared).toBe(false);
    const stored = fixture.store['profile:ImportedPractice'];
    expect(await decryptWithKey(stored.encryptedData, stored.iv, fixture.key)).toEqual({ secrets: spices, settings: {} });
    expect(stored.secrets).toBeUndefined();
    expect(fixture.store.sheetMapping).toEqual({ unrelatedMenu: 'Default', [menuId]: 'ImportedPractice' });
    expect(fixture.store.defaultProfile).toBe('Default');
    expect(JSON.stringify(fixture.store['profile:Default'])).toBe(beforeDefault);
    const tag = await computeRecipeTag({ hash: 'r4nd0m', position: '#', secretIndex: 1,
        modifiers: [], secret: spices['1'].base, sheetId: menuId });
    expect((await cooking().execute(`r4nd0m#1.${tag}`, menuId)).password).toBe('Basic*r4nd0m');
});
it('Full Access without Menu preserves existing assignments/default and supports explicit own-profile cooking', async () => {
    const bundle = await new ExportImportAdapter().createFullAccessBundle('SenderPractice', spices, null, sharingPassword);
    await fixture.decrypt(bundle, sharingPassword);
    await fixture.confirm('UnassignedPractice');
    expect(fixture.store.sheetMapping).toEqual({ unrelatedMenu: 'Default' });
    expect(fixture.store.defaultProfile).toBe('Default');
    expect((await cooking().execute('r4nd0m#1', null, 'UnassignedPractice')).password).toBe('Basic*r4nd0m');
    expect((await new ChromeStorageAdapter().getDecryptedProfile('profile:UnassignedPractice')).secrets).toEqual(spices);
});
it('wrong sharing password cannot advance or write an imported profile', async () => {
    const bundle = await new ExportImportAdapter().createFullAccessBundle('SenderPractice', spices, menuId, sharingPassword);
    await fixture.decrypt(bundle, 'WrongPublicPracticePassword');
    expect(fixture.element('imp-decrypt-error').textContent).toContain('Incorrect sharing password');
    expect(fixture.element('imp-confirm')).toBeNull();
    expect(fixture.writes).toHaveLength(0);
    expect(fixture.completed).not.toHaveBeenCalled();
});
it('legacy import retains derived jar envelope and shared-profile metadata through actual handlers', async () => {
    const adapter = new ExportImportAdapter();
    const bundle = await adapter.createBundle('LegacySender', spices, 'public-practice-relationship', menuId, sharingPassword);
    const derived = await adapter.decryptBundle(bundle, sharingPassword);
    await fixture.decrypt(bundle, sharingPassword);
    await fixture.confirm('LegacyPractice');
    const record = fixture.store['shared:LegacyPractice'];
    expect(await decryptWithKey(record.encryptedData, record.iv, fixture.key)).toEqual({ secrets: derived, isShared: true });
    expect(record.sheetId).toBe(menuId);
    expect(record.readOnly).toBe(true);
    expect(record.meta.importedFrom).toBe('LegacySender');
    expect(fixture.store['profile:LegacyPractice']).toBeUndefined();
    expect(fixture.store.sheetMapping).toEqual({ unrelatedMenu: 'Default' });
    expect((await new ChromeStorageAdapter().getSecretForGeneration(1, menuId)).isShared).toBe(true);
});
