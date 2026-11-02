import fs from 'node:fs';
import path from 'node:path';

export function preflightDestinations(root, directories, files, kind) {
    const absolute = path.resolve(root), checks = [];
    for (let dir = absolute; ; dir = path.dirname(dir)) {
        checks.push({ name: dir, directory: true });
        if (dir === path.dirname(dir)) break;
    }
    for (const dir of directories) checks.push({ name: path.join(absolute, dir), directory: true });
    for (const file of files) checks.push({ name: path.join(absolute, file), directory: false });
    for (const { name, directory } of checks) {
        let entry;
        try { entry = fs.lstatSync(name); }
        catch (error) { if (error.code === 'ENOENT') continue; throw error; }
        if (entry.isSymbolicLink() || (directory ? !entry.isDirectory() : !entry.isFile())) {
            throw new Error(`Unsafe ${kind} destination: ${name}`);
        }
    }
}
