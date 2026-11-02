import fs from 'node:fs';
import path from 'node:path';
import assert from 'node:assert/strict';
import test from 'node:test';
import { spawnSync } from 'node:child_process';
import { zipSync } from 'fflate';
import { PROJECT_ROOT, copyArgon2Assets, dependencyAssets } from './packaging-provenance.mjs';
import { validateSource } from './validate-package.mjs';
import { archiveSource, validateArchive, packageExtension } from './package-extension.mjs';
import { sourceFixture, dependencyFixture, temporary, append } from './packaging-fixtures.mjs';

for (const missing of [['argon2.js'], ['argon2.wasm'], ['argon2.js', 'argon2.wasm']]) {
    test(`copy preflight rejects missing ${missing.join('+')} without writing stale destinations`, () => dependencyFixture(root => {
        for (const name of missing) fs.rmSync(path.join(root, 'node_modules/argon2-browser/dist', name));
        assert.throws(() => copyArgon2Assets(root), /ENOENT/);
        for (const name of ['argon2.js', 'argon2.wasm']) assert.equal(fs.readFileSync(path.join(root, 'src/wasm', name), 'utf8'), 'stale destination');
    }));
}
test('copy preflight rejects changed upstream bytes before writes', () => dependencyFixture(root => {
    append(root, 'node_modules/argon2-browser/dist/argon2.js', '\n// unexpected');
    assert.throws(() => copyArgon2Assets(root), /Unapproved dependency bytes/);
    assert.equal(fs.readFileSync(path.join(root, 'src/wasm/argon2.wasm'), 'utf8'), 'stale destination');
}));
test('copy preflight rejects wrong lock provenance', () => dependencyFixture(root => {
    fs.writeFileSync(path.join(root, 'pnpm-lock.yaml'), 'wrong lock');
    assert.throws(() => copyArgon2Assets(root), /lock provenance/);
}));
for (const link of ['src', 'src/wasm', 'src/vendor', 'src/wasm/argon2.js', 'src/wasm/argon2.wasm', 'src/vendor/argon2-browser-LICENSE.txt']) {
    test(`copy rejects destination symlink ${link} before all writes`, () => temporary(outside => dependencyFixture(root => {
        const destination = path.join(root, link), external = path.join(outside, 'target');
        fs.cpSync(destination, external, { recursive: true });
        fs.rmSync(destination, { recursive: true }); fs.symlinkSync(external, destination);
        const targets = ['src/wasm/argon2.js', 'src/wasm/argon2.wasm', 'src/vendor/argon2-browser-LICENSE.txt'];
        const before = targets.map(p => fs.readFileSync(path.join(root, p)));
        assert.throws(() => copyArgon2Assets(root), /Unsafe assembly destination/);
        targets.forEach((p, i) => assert.deepEqual(fs.readFileSync(path.join(root, p)), before[i]));
        const sentinel = fs.statSync(external).isDirectory() ? path.join(external, link === 'src' ? 'wasm/argon2.js' : link.endsWith('vendor') ? 'argon2-browser-LICENSE.txt' : 'argon2.js') : external;
        assert.equal(fs.readFileSync(sentinel, 'utf8'), 'stale destination');
    })));
}
test('copy CLI returns failure for missing asset, without stale destination bypass', () => dependencyFixture(root => {
    fs.mkdirSync(path.join(root, 'scripts'));
    for (const name of ['copy_wasm.js', 'packaging-provenance.mjs', 'package-paths.mjs', 'package-contract.json']) fs.copyFileSync(path.join(PROJECT_ROOT, 'scripts', name), path.join(root, 'scripts', name));
    fs.writeFileSync(path.join(root, 'package.json'), '{"type":"module"}');
    fs.rmSync(path.join(root, 'node_modules/argon2-browser/dist/argon2.wasm'));
    const run = spawnSync(process.execPath, [path.join(root, 'scripts/copy_wasm.js')], { encoding: 'utf8' });
    assert.equal(run.status, 1); assert.match(run.stderr, /Argon2 assembly failed/);
    assert.equal(fs.readFileSync(path.join(root, 'src/wasm/argon2.js'), 'utf8'), 'stale destination');
}));
test('copy preserves exact dependency JS/WASM/license', () => dependencyFixture(root => {
    const expected = dependencyAssets(root); copyArgon2Assets(root);
    for (const name of ['argon2.js', 'argon2.wasm']) assert.deepEqual(fs.readFileSync(path.join(root, 'src/wasm', name)), expected[name]);
    assert.deepEqual(fs.readFileSync(path.join(root, 'src/vendor/argon2-browser-LICENSE.txt')), expected.license);
}));
const mutations = [
    ['missing loader', root => fs.rmSync(path.join(root, 'wasm/argon2.js')), /Missing mandatory/],
    ['missing WASM', root => fs.rmSync(path.join(root, 'wasm/argon2.wasm')), /Missing mandatory/],
    ['missing static import', root => append(root, 'popup/popup.js', "\nimport './missing.js';"), /Missing\/escaping resource/],
    ['missing dynamic import', root => append(root, 'popup/popup.js', "\nvoid import('./missing.js');"), /Missing\/escaping resource/],
    ['nonliteral executable import', root => append(root, 'popup/popup.js', '\nvoid import(window.packagePath);'), /Nonliteral executable import/],
    ['nonliteral module URL', root => append(root, 'popup/popup.js', '\nnew URL(window.packagePath, import.meta.url);'), /Unreviewed module-relative URL/],
    ['remote executable import', root => append(root, 'popup/popup.js', "\nvoid import('https://example.invalid/code.js');"), /Nonlocal executable import/],
    ['missing Worker', root => fs.rmSync(path.join(root, 'offscreen/crypto-worker.js')), /Missing mandatory/],
    ['missing runtime.getURL resource', root => append(root, 'popup/popup.js', "\nchrome.runtime.getURL('missing.html');"), /Missing\/escaping resource/],
    ['unreviewed runtime.getURL expression', root => append(root, 'popup/popup.js', '\nchrome.runtime.getURL(window.packagePath);'), /Unreviewed dynamic/],
    ['missing CSS resource', root => append(root, 'popup/popup.css', '\n.test{background:url(missing.png)}'), /Missing\/escaping resource/],
    ['unapproved external CSS data', root => append(root, 'popup/popup.css', "\n@import url('https://example.invalid/remote.css');"), /Remote\/unsupported/],
    ['missing HTML resource', root => append(root, 'popup/popup.html', '\n<script src="missing.js"></script>'), /Missing\/escaping resource/],
    ['remote executable script', root => append(root, 'popup/popup.html', '\n<script src="https://example.invalid/code.js"></script>'), /Remote\/unsupported/],
    ['malformed manifest JSON', root => fs.writeFileSync(path.join(root, 'manifest.json'), '{broken'), /JSON/],
    ['malformed locale JSON', root => fs.writeFileSync(path.join(root, '_locales/vi/messages.json'), '{broken'), /JSON/],
    ['resource escapes root', root => {
        const file = path.join(root, 'manifest.json'), manifest = JSON.parse(fs.readFileSync(file));
        manifest.icons['16'] = '../../private.png'; fs.writeFileSync(file, JSON.stringify(manifest));
    }, /Missing\/escaping resource/],
    ['development payload', root => fs.writeFileSync(path.join(root, 'debug.js'), 'debug'), /Unreviewed\/private/],
    ['private payload', root => fs.writeFileSync(path.join(root, '.env'), 'TEST_SYNTHETIC=1'), /Unreviewed\/private/],
    ['symlink payload', root => { fs.rmSync(path.join(root, 'assets/icon-16.png')); fs.symlinkSync('/etc/hosts', path.join(root, 'assets/icon-16.png')); }, /Symlink/],
    ['missing Argon2 notice', root => fs.rmSync(path.join(root, 'vendor/argon2-browser-LICENSE.txt')), /Missing mandatory/],
    ['changed Argon2 notice', root => append(root, 'vendor/argon2-browser-LICENSE.txt', 'changed'), /Unapproved dependency bytes/],
    ['removed QR MIT notice', root => fs.writeFileSync(path.join(root, 'vendor/qrcodegen.js'), 'var qrcodegen = {};'), /Unapproved dependency bytes/],
];
for (const [name, mutate, expected] of mutations) test(`source validation rejects ${name}`, () => sourceFixture(root => {
    mutate(root); assert.throws(() => validateSource(root), expected);
}));
const source = validateSource(path.join(PROJECT_ROOT, 'src'));
test('source validates exact inventory and records Google Fonts data separately', () => {
    assert.equal(source.inventory.length, 74);
    assert.equal(source.externalStyles.length, 1);
    assert.equal(source.externalStyles[0].from, 'styles/tokens.css');
    for (const entry of source.inventory) assert.doesNotMatch(entry.name, /(^|\/)(?:test|debug|\.env)(\/|\.|$)/);
});
test('ZIP has manifest at root, exact file bytes, notices and reproducible bytes', () => {
    const first = archiveSource(source), second = archiveSource(source);
    assert.deepEqual(first, second); assert.equal(validateArchive(first, source.files), 74);
});
test('ZIP validator rejects wrapper directory', () => {
    const wrapped = Object.fromEntries(Object.entries(source.files).map(([name, bytes]) => ['src/' + name, bytes]));
    assert.throws(() => validateArchive(zipSync(wrapped), source.files), /ZIP root/);
});
test('ZIP validator rejects missing manifest', () => {
    const { 'manifest.json': omitted, ...other } = source.files;
    assert.throws(() => validateArchive(zipSync(other), source.files), /Missing mandatory ZIP/);
});
test('ZIP validator rejects private extra entries', () => {
    assert.throws(() => validateArchive(zipSync({ ...source.files, '.env': new Uint8Array([1]) }), source.files), /ZIP root/);
});
test('ZIP validator rejects altered packaged bytes', () => {
    const files = { ...source.files, 'manifest.json': Buffer.from(source.files['manifest.json']) };
    files['manifest.json'][0] = 32;
    assert.throws(() => validateArchive(zipSync(files), source.files), /ZIP content mismatch/);
});
test('ZIP validator rejects duplicate central-directory names', () => {
    const bytes = Buffer.from(archiveSource(source));
    const from = Buffer.from('assets/icon-32.png'), to = Buffer.from('assets/icon-16.png');
    for (let offset = bytes.indexOf(from); offset >= 0; offset = bytes.indexOf(from, offset + to.length)) to.copy(bytes, offset);
    assert.throws(() => validateArchive(bytes, source.files), /duplicate ZIP/);
});
test('ZIP validator rejects truncated archive', () => assert.throws(() => validateArchive(new Uint8Array([1, 2]), source.files)));
const stem = `passchef-extension-${source.manifest.version}-development`;
const outputs = [`${stem}.zip`, `${stem}.zip.tmp`, `${stem}-inventory.json`, `${stem}.zip.sha256`];
for (const link of ['', ...outputs]) test(`package rejects destination symlink ${link || 'output directory'} before all writes`, () => temporary(outside => temporary(root => {
    const output = path.join(root, 'build'); fs.mkdirSync(output);
    for (const name of outputs) fs.writeFileSync(path.join(output, name), 'stale artifact');
    const destination = path.join(output, link), external = path.join(outside, 'target');
    fs.cpSync(destination, external, { recursive: true }); fs.rmSync(destination, { recursive: true });
    fs.symlinkSync(external, destination);
    const before = outputs.map(name => fs.readFileSync(path.join(output, name)));
    assert.throws(() => packageExtension(output), /Unsafe package destination/);
    outputs.forEach((name, index) => assert.deepEqual(fs.readFileSync(path.join(output, name)), before[index]));
    assert.equal(fs.readFileSync(link ? external : path.join(external, outputs[0]), 'utf8'), 'stale artifact');
})));
