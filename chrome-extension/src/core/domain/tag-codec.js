/**
 * Recipe verification tag codec.
 *
 * Tag = first 4 chars of base32(HMAC-SHA256(secret, canonicalRecipe + "|" + sheetId)).
 *
 * Purpose: detect profile/sheet mismatch at decode time, fail explicitly instead of
 * silently producing a wrong password. Tag is verification-only metadata — it does
 * NOT affect the generated password value (preserves README "Cookbook" insight).
 *
 * Security:
 * - HMAC over secret bytes — preimage-resistant. 4 base32 chars = 20 bits truncation;
 *   not feasible to brute-force the secret without an oracle (vault stays locked).
 * - Custom alphabet excludes look-alikes o/l/0/1 to avoid copy-paste confusion.
 */

// 32 chars, no o/l/0/1
export const TAG_ALPHABET = "abcdefghijkmnpqrstuvwxyz23456789";
export const TAG_LENGTH = 4;
export const TAG_REGEX = /^[abcdefghijkmnpqrstuvwxyz23456789]{4}$/;

/**
 * Canonical recipe string used as HMAC message prefix.
 * Modifiers sorted lexicographically so `_!` and `!_` produce the same tag.
 * Tag itself is NOT included in canonical form (tag verifies itself).
 *
 * @param {{hash:string, position:string, secretIndex:number|string, modifiers:string[]}} r
 * @returns {string}
 */
export function canonicalize(r) {
    const sortedMods = [...(r.modifiers || [])].sort().join("");
    return `${r.hash}${r.position}${r.secretIndex}${sortedMods}`;
}

/**
 * Bit-pack a byte array into 5-bit groups, map via TAG_ALPHABET.
 * Returns full base32 string (no padding). Caller truncates to TAG_LENGTH.
 *
 * @param {Uint8Array} bytes
 * @returns {string}
 */
function base32Encode(bytes) {
    let bits = 0;
    let value = 0;
    let out = "";
    for (let i = 0; i < bytes.length; i++) {
        value = (value << 8) | bytes[i];
        bits += 8;
        while (bits >= 5) {
            out += TAG_ALPHABET[(value >>> (bits - 5)) & 31];
            bits -= 5;
        }
    }
    if (bits > 0) {
        out += TAG_ALPHABET[(value << (5 - bits)) & 31];
    }
    return out;
}

/**
 * Compute a recipe verification tag.
 *
 * @param {object} args
 * @param {string} args.hash
 * @param {string} args.position
 * @param {number|string} args.secretIndex
 * @param {string[]} args.modifiers
 * @param {string} args.secret    - raw secret bytes (UTF-8 encoded for HMAC key)
 * @param {string} args.sheetId   - Google Sheet ID (mixed into HMAC message)
 * @returns {Promise<string>}     - 4-char base32 tag
 */
export async function computeRecipeTag({ hash, position, secretIndex, modifiers, secret, sheetId }) {
    if (!secret) throw new Error("computeRecipeTag: secret is required");
    if (!sheetId) throw new Error("computeRecipeTag: sheetId is required");

    const message = canonicalize({ hash, position, secretIndex, modifiers }) + "|" + sheetId;
    const enc = new TextEncoder();

    const key = await crypto.subtle.importKey(
        "raw",
        enc.encode(secret),
        { name: "HMAC", hash: "SHA-256" },
        false,
        ["sign"]
    );
    const sig = await crypto.subtle.sign("HMAC", key, enc.encode(message));
    return base32Encode(new Uint8Array(sig)).slice(0, TAG_LENGTH);
}

/**
 * Verify a Recipe's embedded tag matches recomputed tag for given (secret, sheetId).
 *
 * **Precondition:** caller must check `recipe.tag` is non-null BEFORE calling.
 * This function returns `false` both for "tag present but wrong" AND "no tag at all";
 * callers must not conflate the two cases. The guarded callsite in
 * `generate_password.js` already handles legacy (untagged) recipes upstream.
 *
 * @param {{hash:string, position:string, secretIndex:number, modifiers:string[], tag:?string}} recipe
 * @param {string} secret
 * @param {string} sheetId
 * @returns {Promise<boolean>} true iff recipe.tag matches recomputed tag
 */
export async function verifyRecipeTag(recipe, secret, sheetId) {
    if (!recipe || !recipe.tag) return false;
    const expected = await computeRecipeTag({
        hash: recipe.hash,
        position: recipe.position,
        secretIndex: recipe.secretIndex,
        modifiers: recipe.modifiers,
        secret,
        sheetId,
    });
    return expected === recipe.tag;
}
