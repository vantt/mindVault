import { copyArgon2Assets } from './packaging-provenance.mjs';

try {
    copyArgon2Assets();
    console.log('Verified and copied mandatory Argon2 JS/WASM and MIT notice.');
} catch (error) {
    console.error(`Argon2 assembly failed: ${error.message}`);
    process.exitCode = 1;
}
