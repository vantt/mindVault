import fs from 'node:fs';
import path from 'node:path';
import { pathToFileURL } from 'node:url';
import { zipSync, unzipSync } from 'fflate';
import { contract, PROJECT_ROOT, sha256 } from './packaging-provenance.mjs';
import { validateSource } from './validate-package.mjs';
import { preflightDestinations } from './package-paths.mjs';

export function validateArchive(bytes, files) {
    const seen = new Set();
    const extracted = unzipSync(bytes, { filter(entry) {
        if (seen.has(entry.name) || !contract.files.includes(entry.name) || !files[entry.name]) {
            throw new Error(`Unexpected/duplicate ZIP root entry: ${entry.name}`);
        }
        seen.add(entry.name);
        if (entry.originalSize !== files[entry.name].length) throw new Error(`ZIP size mismatch: ${entry.name}`);
        return true;
    } });
    if (seen.size !== contract.files.length) throw new Error('Missing mandatory ZIP root entries');
    for (const name of contract.files) {
        if (!Buffer.from(extracted[name]).equals(files[name])) throw new Error(`ZIP content mismatch: ${name}`);
    }
    return seen.size;
}
export function archiveSource(result) {
    const entries = Object.fromEntries(contract.files.map(name => [name,
        [result.files[name], { level: 9, mtime: new Date(1980, 0, 1) }]]));
    const bytes = zipSync(entries);
    validateArchive(bytes, result.files);
    return bytes;
}
export function packageExtension(outputRoot = path.join(PROJECT_ROOT, 'build')) {
    const result = validateSource();
    const bytes = archiveSource(result);
    const stem = `passchef-extension-${result.manifest.version}-development`;
    const record = { kind: 'development artifact; browser/artifact acceptance Pending', version: result.manifest.version,
        archive: `${stem}.zip`, sha256: sha256(bytes), files: result.inventory,
        excluded: contract.excluded, externalStyles: result.externalStyles,
        dependency: { name: 'argon2-browser', ...contract.argon2 } };
    preflightDestinations(outputRoot, [], [`${stem}.zip`, `${stem}.zip.tmp`, `${stem}-inventory.json`, `${stem}.zip.sha256`], 'package');
    fs.mkdirSync(outputRoot, { recursive: true });
    const target = path.join(outputRoot, stem + '.zip');
    fs.writeFileSync(target + '.tmp', bytes);
    fs.renameSync(target + '.tmp', target);
    fs.writeFileSync(path.join(outputRoot, stem + '-inventory.json'), JSON.stringify(record, null, 2) + '\n');
    fs.writeFileSync(target + '.sha256', `${record.sha256}  ${stem}.zip\n`);
    return { target, sha256: record.sha256, files: result.inventory.length, bytes: bytes.length };
}
if (process.argv[1] && import.meta.url === pathToFileURL(path.resolve(process.argv[1])).href) {
    try {
        if (process.argv[2] === '--verify' && process.argv.length === 4) {
            const result = validateSource();
            const bytes = fs.readFileSync(process.argv[3]);
            console.log(JSON.stringify({ verifiedZip: process.argv[3], files: validateArchive(bytes, result.files), sha256: sha256(bytes) }));
        } else {
            if (process.argv.length !== 2) throw new Error('Usage: package-extension.mjs [--verify archive.zip]');
            console.log(JSON.stringify(packageExtension(), null, 2));
        }
    } catch (error) { console.error(`Packaging failed: ${error.message}`); process.exitCode = 1; }
}
