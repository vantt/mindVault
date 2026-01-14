
import { ICrypto } from "../../core/ports/interfaces.js";
// import argon2 from 'argon2-browser'; // Expected to be available or bundled

export class Argon2Adapter extends ICrypto {
    constructor() {
        super();
    }

    /**
     * Derives an AES-GCM key from the master password using Argon2id.
     * @param {string} password 
     * @param {Uint8Array} salt 
     * @returns {Promise<JsonWebKey>} Exported JWK for storage
     */
    async deriveKey(password, salt) {
        // Dynamic import to handle browser/env differences if needed, 
        // or assuming bundler handles 'argon2-browser'
        // Import from local folder which we populated via copy_wasm script
        // Note: argon2-browser usually exports a global or default depending on build.
        // We will try to import the local file.
        // If this is running in Service Worker, we need the correct path relative to this file.
        // This file is in src/adapters/infrastructure/argon2_adapter.js
        // WASM is in src/wasm/argon2.js
        // Relative path: ../../wasm/argon2.js
        const argon2Module = await import('../../wasm/wrapper.js');
        const argon2 = argon2Module.default || argon2Module;

        const hashConfig = {
            pass: password,
            salt: salt,
            type: argon2.ArgonType.Argon2id,
            hashLen: 32, // 256 bits
            time: 3,
            mem: 65536, // 64 MB
            parallelism: 4
        };

        const result = await argon2.hash(hashConfig);
        
        // Result.hash is Uint8Array
        const keyMaterial = result.hash;

        // Import as CrytoKey
        const key = await crypto.subtle.importKey(
            "raw",
            keyMaterial,
            { name: "AES-GCM" },
            true,
            ["encrypt", "decrypt"]
        );

        // Export as JWK to store in session
        return await crypto.subtle.exportKey("jwk", key);
    }
}
