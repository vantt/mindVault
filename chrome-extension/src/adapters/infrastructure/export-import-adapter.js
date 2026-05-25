/**
 * Profile export/import: HKDF-derived secrets + PBKDF2-encrypted bundles.
 * Owner exports derived secrets (DS_i) so originals never leave Owner's vault.
 * Consumer imports the bundle and generates sheet-bound passwords.
 */

const BUNDLE_TYPE = "passchef-profile-share";
const BUNDLE_VERSION = "2.1";
const HKDF_INFO = "passchef-share-v1";
const FULLACCESS_BUNDLE_TYPE = "passchef-fullaccess-share";
const FULLACCESS_BUNDLE_VERSION = "1.0";
const PBKDF2_ITERATIONS = 100000;

/** Derive a base64 string from a secret using HKDF-SHA256. */
async function hkdfDerive(secret, label) {
    const keyMaterial = await crypto.subtle.importKey(
        "raw", new TextEncoder().encode(secret), "HKDF", false, ["deriveBits"]
    );
    const bits = await crypto.subtle.deriveBits(
        {
            name: "HKDF",
            hash: "SHA-256",
            salt: new TextEncoder().encode(label),
            info: new TextEncoder().encode(HKDF_INFO)
        },
        keyMaterial,
        256
    );
    return btoa(String.fromCharCode(...new Uint8Array(bits)));
}

/** Derive an AES-GCM key from a sharing password using PBKDF2. */
async function deriveKeyFromPassword(password, salt) {
    const keyMaterial = await crypto.subtle.importKey(
        "raw", new TextEncoder().encode(password), "PBKDF2", false, ["deriveKey"]
    );
    return crypto.subtle.deriveKey(
        { name: "PBKDF2", salt: new Uint8Array(salt), iterations: PBKDF2_ITERATIONS, hash: "SHA-256" },
        keyMaterial,
        { name: "AES-GCM", length: 256 },
        false,
        ["encrypt", "decrypt"]
    );
}

export class ExportImportAdapter {
    /**
     * Create an encrypted share bundle.
     * @param {string} profileName
     * @param {{[idx]: {base: string}}} secrets  - own profile secrets (S_i)
     * @param {string} label                     - relationship label (e.g. "tenant-a-2026")
     * @param {string} sheetId
     * @param {string} sharingPassword
     * @returns {object} bundle JSON object
     */
    async createBundle(profileName, secrets, label, sheetId, sharingPassword) {
        // Derive DS_i from each secret
        const derivedSecrets = {};
        for (const [idx, secretObj] of Object.entries(secrets)) {
            derivedSecrets[idx] = { base: secretObj.base ? await hkdfDerive(secretObj.base, label) : "" };
        }

        // Encrypt derived secrets with sharing password
        const exportSalt = Array.from(crypto.getRandomValues(new Uint8Array(16)));
        const encKey = await deriveKeyFromPassword(sharingPassword, exportSalt);
        const iv = crypto.getRandomValues(new Uint8Array(12));
        const encrypted = await crypto.subtle.encrypt(
            { name: "AES-GCM", iv },
            encKey,
            new TextEncoder().encode(JSON.stringify(derivedSecrets))
        );

        return {
            type: BUNDLE_TYPE,
            version: BUNDLE_VERSION,
            bundleId: crypto.randomUUID(),
            profileName,
            exportedAt: new Date().toISOString(),
            sheetId,
            encryptedData: Array.from(new Uint8Array(encrypted)),
            iv: Array.from(iv),
            exportSalt
        };
    }

    /**
     * Create an encrypted Full Access bundle — raw secrets, no HKDF derivation.
     * Consumer gets identical secrets → generates identical passwords as Owner.
     * @param {string} profileName
     * @param {{[idx]: {base: string}}} secrets - own profile secrets (S_i)
     * @param {string|null} sheetId - optional; if provided, Consumer's import auto-sets sheetMapping
     * @param {string} sharingPassword
     * @returns {object} bundle JSON object
     */
    async createFullAccessBundle(profileName, secrets, sheetId, sharingPassword) {
        const exportSalt = Array.from(crypto.getRandomValues(new Uint8Array(16)));
        const encKey = await deriveKeyFromPassword(sharingPassword, exportSalt);
        const iv = crypto.getRandomValues(new Uint8Array(12));
        const encrypted = await crypto.subtle.encrypt(
            { name: "AES-GCM", iv },
            encKey,
            new TextEncoder().encode(JSON.stringify(secrets))
        );
        return {
            type: FULLACCESS_BUNDLE_TYPE,
            version: FULLACCESS_BUNDLE_VERSION,
            bundleId: crypto.randomUUID(),
            profileName,
            exportedAt: new Date().toISOString(),
            sheetId: sheetId || null,
            encryptedData: Array.from(new Uint8Array(encrypted)),
            iv: Array.from(iv),
            exportSalt
        };
    }

    /**
     * Validate bundle format without decrypting.
     * Accepts both legacy (passchef-profile-share) and Full Access (passchef-fullaccess-share) bundles.
     * @returns {{ valid: boolean, bundleType?: string, profileName?: string, sheetId?: string, exportedAt?: string, error?: string }}
     */
    validateBundle(bundle) {
        if (!bundle || typeof bundle !== "object") return { valid: false, error: "Not a valid JSON object" };

        const isLegacy = bundle.type === BUNDLE_TYPE;
        const isFullAccess = bundle.type === FULLACCESS_BUNDLE_TYPE;

        if (!isLegacy && !isFullAccess) return { valid: false, error: "Not a PassChef bundle" };

        if (isLegacy && bundle.version !== BUNDLE_VERSION)
            return { valid: false, error: `Unsupported bundle version: ${bundle.version}` };
        if (isFullAccess && bundle.version !== FULLACCESS_BUNDLE_VERSION)
            return { valid: false, error: `Unsupported bundle version: ${bundle.version}` };

        // sheetId required for legacy, optional for fullaccess
        if (isLegacy && (!bundle.sheetId || !bundle.encryptedData || !bundle.iv || !bundle.exportSalt))
            return { valid: false, error: "Incomplete bundle — missing required fields" };
        if (isFullAccess && (!bundle.encryptedData || !bundle.iv || !bundle.exportSalt))
            return { valid: false, error: "Incomplete bundle — missing required fields" };

        return {
            valid: true,
            bundleType: bundle.type,
            profileName: bundle.profileName,
            sheetId: bundle.sheetId || null,
            exportedAt: bundle.exportedAt
        };
    }

    /**
     * Decrypt bundle and return derived secrets.
     * @throws {Error} if sharing password is wrong
     * @returns {{[idx]: {base: string}}} derived secrets
     */
    async decryptBundle(bundle, sharingPassword) {
        const encKey = await deriveKeyFromPassword(sharingPassword, bundle.exportSalt);
        let decrypted;
        try {
            decrypted = await crypto.subtle.decrypt(
                { name: "AES-GCM", iv: new Uint8Array(bundle.iv) },
                encKey,
                new Uint8Array(bundle.encryptedData)
            );
        } catch {
            throw new Error("Incorrect sharing password");
        }
        return JSON.parse(new TextDecoder().decode(decrypted));
    }
}
