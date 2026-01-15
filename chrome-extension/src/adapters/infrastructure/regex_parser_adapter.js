import { IParser } from "../../core/ports/interfaces.js";
import { Recipe } from "../../core/domain/recipe.js";

export class RegexParserAdapter extends IParser {
    constructor() {
        super();
        this.regex = /^([a-zA-Z0-9]+)([#@$%^])(\d)([_!?~]*)(?:_(v[a-zA-Z0-9]+))?$/;
    }

    /**
     * @param {string} text 
     * @returns {Recipe|null}
     */
    parse(text) {
        const match = text.match(this.regex);
        if (!match) return null;

        const [_, hash, position, secretIndex, modifiersStr, version] = match;
        
        const modifiers = modifiersStr ? modifiersStr.split('') : [];
        
        try {
            return new Recipe(hash, position, secretIndex, modifiers, version || null);
        } catch (e) {
            console.error("Recipe validation failed:", e);
            return null;
        }
    }
}
