import { verifyRecipeTag } from "../domain/tag-codec.js";
import { RecipeProfileMismatchError } from "../domain/recipe-errors.js";

export class GeneratePassword {
    /**
     * @param {import("../ports/interfaces").IParser} parser
     * @param {import("../ports/interfaces").IStorageRepository} storage
     */
    constructor(parser, storage) {
        this.parser = parser;
        this.storage = storage;
    }

    /**
     * @param {string} recipeText
     * @param {string|null} sheetId  - current Google Sheet ID for profile routing
     * @param {string|null} profileNameOverride - explicit own-profile selection (popup recipe builder)
     * @returns {Promise<{ password: string, profileName: string, isShared: boolean, settings: object, warning?: string }>}
     */
    async execute(recipeText, sheetId = null, profileNameOverride = null) {
        const recipe = this.parser.parse(recipeText);
        if (!recipe) throw new Error("Invalid recipe format");

        const result = await this.storage.getSecretForGeneration(recipe.secretIndex, sheetId, profileNameOverride);
        if (!result) throw new Error(`Secret #${recipe.secretIndex} not found`);

        const { rawSecret, effectiveSecret, profileName, isShared, settings, sheetId: resolvedSheetId } = result;

        // Verification tag check (Option B — see docs/recipe-tag-design-rationale.md).
        // Tag verifies (recipe, secret, sheetId) tuple via HMAC. Mismatch → throw, do NOT
        // silently produce a wrong password. Tag does NOT alter the password value.
        if (recipe.tag) {
            if (!resolvedSheetId) {
                throw new RecipeProfileMismatchError("Recipe requires sheet context");
            }
            const ok = await verifyRecipeTag(recipe, rawSecret, resolvedSheetId);
            if (!ok) throw new RecipeProfileMismatchError();
        }

        let secret = effectiveSecret;

        // Apply secret-transforming modifiers
        if (recipe.modifiers.includes('~')) secret = secret.replace(/[^a-zA-Z0-9]/g, "");
        if (recipe.modifiers.includes('?')) secret = secret.split('').reverse().join('');
        if (recipe.modifiers.includes('!')) secret = secret.toUpperCase();

        // Position modifier: _ flips # ↔ $
        let position = recipe.position;
        if (recipe.modifiers.includes('_')) {
            if (position === '#') position = '$';
            else if (position === '$') position = '#';
        }

        const password = this._combine(recipe.hash, position, secret);
        const out = { password, profileName, isShared, settings };
        // Legacy recipes used in a sheet context get a soft warning so popup UI / future
        // tooling can nudge users to rebuild with verification tags. Content script ignores.
        if (!recipe.tag && resolvedSheetId) out.warning = "legacy_no_tag";
        return out;
    }

    _combine(hash, position, secret) {
        switch (position) {
            case '#': return secret + hash;
            case '$': return hash + secret;
            case '@': {
                const mid = Math.floor(hash.length / 2);
                return hash.slice(0, mid) + secret + hash.slice(mid);
            }
            case '%': return this._interleave(hash, secret, 1);
            case '^': return this._interleave(hash, secret, 2);
            default: throw new Error(`Unknown position: ${position}`);
        }
    }

    _interleave(s1, s2, chunkSize) {
        let result = "", i = 0, j = 0;
        while (i < s1.length || j < s2.length) {
            if (i < s1.length) { result += s1.substr(i, chunkSize); i += chunkSize; }
            if (j < s2.length) { result += s2.substr(j, chunkSize); j += chunkSize; }
        }
        return result;
    }
}
