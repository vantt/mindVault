
import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const srcDir = path.resolve(__dirname, '../node_modules/argon2-browser/dist');
const destDir = path.resolve(__dirname, '../src/wasm');

if (!fs.existsSync(destDir)) {
    fs.mkdirSync(destDir, { recursive: true });
}

// Copy both wasm and js loader
const files = ['argon2.wasm', 'argon2.js'];

files.forEach(file => {
    const srcPath = path.join(srcDir, file);
    const destPath = path.join(destDir, file);

    if (fs.existsSync(srcPath)) {
        fs.copyFileSync(srcPath, destPath);
        console.log(`Copied ${file} to ${destDir}`);
    } else {
        console.warn(`Warning: ${file} not found in ${srcDir}. Run npm install first.`);
    }
});
