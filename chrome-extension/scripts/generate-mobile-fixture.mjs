import { readFile, writeFile, mkdir } from 'node:fs/promises';
import { ExportImportAdapter } from '../src/adapters/infrastructure/export-import-adapter.js';

const warning = 'SYNTHETIC TEST DATA ONLY';
const fixturePath = new URL('../test/fixtures/full-access-bundle.fixture.json', import.meta.url);
const mobilePath = new URL('../../shared/src/jvmTest/resources/full-access-bundle.fixture.json', import.meta.url);
const expectedSpices = { 1: { base: 'SYNTHETIC_COOKBOOK_TEST_🍳' }, 2: { base: 'SYNTHETIC_Món_2' } };
const sharingPassword = 'SYNTHETIC_SHARE_🍳';
const adapter = new ExportImportAdapter();
let fixture;
try { fixture = JSON.parse(await readFile(fixturePath, 'utf8')); }
catch (error) {
  if (error.code !== 'ENOENT') throw error;
  fixture = {
    _warning: warning,
    bundle: await adapter.createFullAccessBundle('SYNTHETIC_PROFILE', expectedSpices, 'synthetic-menu-id', sharingPassword),
    expectedSpices,
    sharingPassword,
  };
}
if (fixture._warning !== warning || fixture.sharingPassword !== sharingPassword ||
    JSON.stringify(fixture.expectedSpices) !== JSON.stringify(expectedSpices) ||
    fixture.bundle.profileName !== 'SYNTHETIC_PROFILE') {
  throw new Error('Fixture must contain the specified synthetic inputs only');
}
if (!adapter.validateBundle(fixture.bundle).valid ||
    JSON.stringify(await adapter.decryptBundle(fixture.bundle, sharingPassword)) !== JSON.stringify(expectedSpices)) {
  throw new Error('Fixture failed the real extension round trip');
}
const json = JSON.stringify(fixture, null, 2) + '\n';
await mkdir(new URL('.', fixturePath), { recursive: true });
await mkdir(new URL('.', mobilePath), { recursive: true });
await writeFile(fixturePath, json);
await writeFile(mobilePath, json);
console.log('Synthetic bundle fixture verified with the real extension adapter.');
