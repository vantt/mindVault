import { IParser } from "../../core/ports/interfaces.js";
import { Recipe } from "../../core/domain/recipe.js";

export class RegexParserAdapter extends IParser {
    constructor() {
        super();
        // Grammar: <hash><position><secretIndex>[modifiers][.<tag>]
        //
        // ASCII-only hash by design: Unicode chars risk NFC/NFD normalization mismatch across
        // OS/IME → same-looking recipe could produce different passwords on different devices.
        // Recipes are identifiers (e.g. "gmail"), not display strings — ASCII is sufficient.
        //
        // Tag (optional, 4 chars): base32 verification tag from `tag-codec.js`. Char-class
        // here `[a-z2-9]` is a permissive superset; strict alphabet validation (no `o/l`)
        // happens in `Recipe.validate()` and parse() catches the throw.
        this.regex = /^([a-zA-Z0-9]+)([#@$%^])(\d)([_!?~]*)(?:\.([a-z2-9]{4}))?$/;
    }

    /**
     * @param {string} text
     * @returns {Recipe|null}
     */
    parse(text) {
        const match = text.match(this.regex);
        if (!match) return null;

        const [_, hash, position, secretIndex, modifiersStr, tag] = match;
        const modifiers = modifiersStr ? modifiersStr.split('') : [];

        try {
            return new Recipe(hash, position, secretIndex, modifiers, tag || null);
        } catch (e) {
            // Downgraded to warn — common case is permissive regex catching e.g. `l`/`o`
            // in tag (excluded from alphabet but regex permits a-z2-9). Not an error.
            console.warn("Recipe validation failed:", e.message);
            return null;
        }
    }
}
