import fs from 'node:fs';
import path from 'node:path';
import os from 'node:os';
import { PROJECT_ROOT } from './packaging-provenance.mjs';

export function temporary(work) {
    const root = fs.mkdtempSync(path.join(os.tmpdir(), 'passchef-package-test-'));
    try { return work(root); }
    finally { fs.rmSync(root, { recursive: true, force: true }); }
}
export function sourceFixture(work) {
    return temporary(root => {
        const source = path.join(root, 'src');
        fs.cpSync(path.join(PROJECT_ROOT, 'src'), source, { recursive: true });
        return work(source);
    });
}
export function dependencyFixture(work) {
    return temporary(root => {
        fs.cpSync(path.join(PROJECT_ROOT, 'node_modules/argon2-browser'), path.join(root, 'node_modules/argon2-browser'), { recursive: true, dereference: true });
        fs.copyFileSync(path.join(PROJECT_ROOT, 'pnpm-lock.yaml'), path.join(root, 'pnpm-lock.yaml'));
        fs.mkdirSync(path.join(root, 'src/wasm'), { recursive: true });
        for (const name of ['argon2.js', 'argon2.wasm']) fs.writeFileSync(path.join(root, 'src/wasm', name), 'stale destination');
        fs.mkdirSync(path.join(root, 'src/vendor'), { recursive: true });
        fs.writeFileSync(path.join(root, 'src/vendor/argon2-browser-LICENSE.txt'), 'stale destination');
        return work(root);
    });
}
export function append(root, file, value) { fs.appendFileSync(path.join(root, file), value); }
