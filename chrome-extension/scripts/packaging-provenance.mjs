import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { createHash } from 'node:crypto';
import { preflightDestinations } from './package-paths.mjs';

export const PROJECT_ROOT = fileURLToPath(new URL('../', import.meta.url));
export const contract = JSON.parse(fs.readFileSync(new URL('./package-contract.json', import.meta.url)));
export const sha256 = bytes => createHash('sha256').update(bytes).digest('hex');
export function requireHash(bytes, expected, label) {
    if (sha256(bytes) !== expected) throw new Error(`Unapproved dependency bytes: ${label}`);
}
export function dependencyAssets(projectRoot = PROJECT_ROOT) {
    const dependency = path.join(projectRoot, 'node_modules/argon2-browser');
    const metadata = JSON.parse(fs.readFileSync(path.join(dependency, 'package.json'), 'utf8'));
    if (metadata.version !== contract.argon2.version) throw new Error('Unapproved argon2-browser version');
    const lock = fs.readFileSync(path.join(projectRoot, 'pnpm-lock.yaml'), 'utf8');
    if (!lock.includes(`argon2-browser@${metadata.version}:\n    resolution: {integrity: ${contract.argon2.integrity}}`)) {
        throw new Error('Argon2 dependency lock provenance mismatch');
    }
    // Preflight every input before any destination write, including stale destinations.
    const assets = {};
    for (const [name, digest] of Object.entries(contract.argon2.hashes)) {
        assets[name] = fs.readFileSync(path.join(dependency, 'dist', name));
        requireHash(assets[name], digest, name);
    }
    assets.license = fs.readFileSync(path.join(dependency, 'LICENSE'));
    requireHash(assets.license, contract.argon2.licenseHash, 'Argon2 MIT license');
    return assets;
}
export function copyArgon2Assets(projectRoot = PROJECT_ROOT) {
    const assets = dependencyAssets(projectRoot);
    const directories = ['src', 'src/wasm', 'src/vendor'];
    const targets = ['src/wasm/argon2.js', 'src/wasm/argon2.wasm', 'src/vendor/argon2-browser-LICENSE.txt'];
    // Do not follow a destination link before the source validator can reject it.
    preflightDestinations(projectRoot, directories, targets, 'assembly');
    const root = path.join(projectRoot, 'src');
    fs.mkdirSync(path.join(root, 'wasm'), { recursive: true });
    fs.mkdirSync(path.join(root, 'vendor'), { recursive: true });
    for (const name of Object.keys(contract.argon2.hashes)) fs.writeFileSync(path.join(root, 'wasm', name), assets[name]);
    fs.writeFileSync(path.join(root, 'vendor/argon2-browser-LICENSE.txt'), assets.license);
    return assets;
}
function validateFonts(files) {
    for (const [name, digest] of Object.entries(contract.fonts.hashes)) {
        requireHash(files[name], digest, `Font resource ${name}`);
    }
    const metadata = JSON.parse(files['assets/fonts/provenance.json']);
    if (metadata.repository !== contract.fonts.repository || metadata.commit !== contract.fonts.commit ||
        metadata.unmodifiedBytes !== true || metadata.files.length !== 7) {
        throw new Error('Font source provenance mismatch');
    }
    for (const asset of metadata.files) {
        const name = `assets/fonts/${asset.file}`;
        if (contract.fonts.hashes[name] !== asset.sha256 || files[name].length !== asset.bytes) {
            throw new Error(`Font source metadata mismatch: ${name}`);
        }
    }
    const notice = files['vendor/README.md'].toString();
    if (!notice.includes(contract.fonts.commit) || !notice.includes('SIL Open Font') ||
        !['fraunces-ofl.txt', 'geist-ofl.txt', 'geistmono-ofl.txt'].every(name => notice.includes(name))) {
        throw new Error('Font provenance notice is missing');
    }
}
export function validateProvenance(files, projectRoot = PROJECT_ROOT) {
    const dependency = dependencyAssets(projectRoot);
    for (const [name, digest] of Object.entries(contract.argon2.hashes)) {
        requireHash(files[`wasm/${name}`], digest, name);
        if (!files[`wasm/${name}`].equals(dependency[name])) throw new Error(`Dependency mismatch: ${name}`);
    }
    requireHash(files['vendor/argon2-browser-LICENSE.txt'], contract.argon2.licenseHash, 'Argon2 notice');
    requireHash(files['vendor/qrcodegen.js'], contract.qrHash, 'Nayuki QR source and embedded MIT notice');
    const qrNotice = files['vendor/README.md'].toString();
    if (!qrNotice.includes(contract.qrHash) || !qrNotice.includes('3c6d0b3cefb4e049dc337e82237c9644399716a8')) {
        throw new Error('QR provenance notice is missing');
    }
    if (WebAssembly.validate(files['wasm/argon2.wasm']) !== true) throw new Error('Invalid Argon2 WASM');
    validateFonts(files);
}
