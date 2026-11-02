import fs from 'node:fs';
import path from 'node:path';
import { pathToFileURL } from 'node:url';
import { spawnSync } from 'node:child_process';
import { contract, PROJECT_ROOT, sha256, validateProvenance } from './packaging-provenance.mjs';
import { checkResources } from './package-resources.mjs';
import { preflightDestinations } from './package-paths.mjs';

export function readSource(root) {
    preflightDestinations(root, [], [], 'source');
    const files = {};
    function visit(dir, prefix = '') {
        for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
            const name = prefix + entry.name, absolute = path.join(dir, entry.name);
            if (entry.isSymbolicLink()) throw new Error(`Symlink not allowed: ${name}`);
            if (entry.isDirectory()) { visit(absolute, name + '/'); continue; }
            if (!entry.isFile()) throw new Error(`Unsupported source entry: ${name}`);
            if (contract.excluded.includes(name)) continue;
            if (!contract.files.includes(name)) throw new Error(`Unreviewed/private/development payload: ${name}`);
            files[name] = fs.readFileSync(absolute);
        }
    }
    visit(root);
    if (Object.keys(files).sort().join('\n') !== contract.files.join('\n')) throw new Error('Missing mandatory package inventory file');
    return files;
}
export function validateSource(root = path.join(PROJECT_ROOT, 'src'), projectRoot = PROJECT_ROOT) {
    const files = readSource(root);
    for (const [name, bytes] of Object.entries(files)) {
        if (name.endsWith('.json')) JSON.parse(bytes.toString());
        if (name.endsWith('.js')) {
            const checked = spawnSync(process.execPath, ['--check', path.join(root, name)], { encoding: 'utf8' });
            if (checked.status !== 0) throw new Error(`JavaScript syntax error: ${name}: ${checked.stderr}`);
        }
    }
    const manifest = JSON.parse(files['manifest.json']);
    if (manifest.manifest_version !== 3 || !/^\d+\.\d+\.\d+(?:\.\d+)?$/.test(manifest.version) ||
        !manifest.background?.service_worker || !manifest.action?.default_popup || !manifest.default_locale) {
        throw new Error('Malformed MV3 manifest contract');
    }
    for (const locale of ['en', 'vi']) {
        const bundle = JSON.parse(files[`_locales/${locale}/messages.json`]);
        if (!Object.values(bundle).every(value => typeof value.message === 'string')) throw new Error(`Malformed locale: ${locale}`);
    }
    validateProvenance(files, projectRoot);
    const externalStyles = checkResources(files, manifest);
    return { files, manifest, externalStyles, inventory: Object.entries(files).map(([name, bytes]) => ({ name, size: bytes.length, sha256: sha256(bytes) })) };
}
if (process.argv[1] && import.meta.url === pathToFileURL(path.resolve(process.argv[1])).href) {
    try {
        if (process.argv.length > 3) throw new Error('Usage: validate-package.mjs [source-directory]');
        const result = validateSource(process.argv[2]);
        console.log(JSON.stringify({ status: 'Validated development source', version: result.manifest.version,
            files: result.inventory.length, externalStyles: result.externalStyles }, null, 2));
    } catch (error) { console.error(`Validation failed: ${error.message}`); process.exitCode = 1; }
}
