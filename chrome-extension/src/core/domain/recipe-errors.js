/**
 * Domain errors related to recipe parsing, verification, and generation.
 *
 * Each error carries a stable `code` field for I18N + UI mapping (so error
 * messages can be translated without string matching on the `.message`).
 */

export class RecipeProfileMismatchError extends Error {
    constructor(msg = "Recipe verification tag mismatch") {
        super(msg);
        this.name = "RecipeProfileMismatchError";
        // Opaque on purpose — does NOT disclose whether mismatch was profile, sheet,
        // or modifier. Less information to a probing attacker; UI shows generic message.
        this.code = "RECIPE_MISMATCH";
    }
}
