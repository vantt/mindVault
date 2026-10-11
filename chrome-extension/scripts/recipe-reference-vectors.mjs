import { readFile, writeFile, mkdir } from 'node:fs/promises';
import { withChromiumOracle, ORACLE_BROWSER_VERSION } from './chromium-oracle.mjs';

const mode = process.argv[2] || '--write';
if (!['--write', '--check'].includes(mode) || process.argv.length > 3) {
  throw new Error('Usage: node recipe-reference-vectors.mjs --write|--check');
}
const rows = await withChromiumOracle(page => page.evaluate(async () => {
  const { GeneratePassword } = await import(chrome.runtime.getURL('core/usecases/generate_password.js'));
  const { RegexParserAdapter } = await import(chrome.runtime.getURL('adapters/infrastructure/regex_parser_adapter.js'));
  const { computeRecipeTag } = await import(chrome.runtime.getURL('core/domain/tag-codec.js'));
  const rows = [];
  for (const spice of [
    'SYNTHETIC_Món🍳ß', 'SYNTHETIC_\ud800_tail',
    'SYNTHETIC_Vithkuqi_\u{10597}', 'SYNTHETIC_Garay_\u{10d70}',
    'SYNTHETIC_SPECIAL_ßﬃΐŉǰςıi', 'SYNTHETIC_BROWSER_IDENTITY_\ua7cf\u{16ec0}',
    'SYNTHETIC_COMBINING_α\u0345\u0301_α\u0301\u0345_\u0345\u0301_ᾀᾷ',
    'SYNTHETIC_UTF16_a\ud800ß\udc00z_🍳\ud800',
  ]) {
    for (const recipeText of ['mix#1', 'mix@1', 'mix%1', 'mix^1', 'mix#1?', 'mix#1!', 'mix#1~?!']) {
      const parser = new RegexParserAdapter();
      const recipe = parser.parse(recipeText);
      const menuId = 'synthetic-menu-id';
      const storage = { getSecretForGeneration: async () => ({ rawSecret: spice, effectiveSecret: spice,
        profileName: 'Synthetic', isShared: false, settings: {}, sheetId: menuId }) };
      const result = await new GeneratePassword(parser, storage).execute(recipeText, menuId);
      const tag = await computeRecipeTag({ ...recipe, secret: spice, sheetId: menuId });
      rows.push({ recipeText, spice, menuId, tag, password: result.password });
    }
  }
  return rows;
}), { extension: true });
const path = new URL('../../shared/src/jvmTest/resources/recipe-reference-vectors.json', import.meta.url);
const fixture = JSON.stringify({ _warning: 'SYNTHETIC TEST DATA ONLY', browserVersion: ORACLE_BROWSER_VERSION, rows }, null, 2) + '\n';
if (mode === '--write') {
  await mkdir(new URL('.', path), { recursive: true });
  await writeFile(path, fixture);
} else if (await readFile(path, 'utf8') !== fixture) {
  throw new Error('Recipe vectors differ from the actual extension browser; investigate before regenerating');
}
console.log(`${mode === '--check' ? 'Verified' : 'Recorded'} ${rows.length} synthetic recipe vectors from the loaded extension in Chromium ${ORACLE_BROWSER_VERSION}.`);
