import { TAG_REGEX } from "./tag-codec.js";

/**
 * Domain Entity representing a parsed password recipe.
 * Structure: <hash><position><secret_num>[modifiers][.<tag>]
 * Tag is optional — when present, used for explicit profile/sheet mismatch detection.
 */
export class Recipe {
    /**
     * @param {string} hash
     * @param {string} position
     * @param {number|string} secretIndex
     * @param {string[]} modifiers
     * @param {?string} tag - 4-char base32 verification tag, or null for legacy recipes
     */
    constructor(hash, position, secretIndex, modifiers = [], tag = null) {
        this.hash = hash;
        this.position = position;
        this.secretIndex = parseInt(secretIndex, 10);
        this.modifiers = [...new Set(modifiers)]; // deduplicate; fixed apply order: ~ then ? then !
        this.tag = tag;

        this.validate();
    }

    validate() {
        if (!this.hash || this.hash.length === 0) {
            throw new Error("Hash component is required");
        }

        const validPositions = ['#', '$', '@', '%', '^'];
        if (!validPositions.includes(this.position)) {
            throw new Error(`Invalid position character: ${this.position}`);
        }

        if (isNaN(this.secretIndex) || this.secretIndex < 1 || this.secretIndex > 5) {
            throw new Error(`Secret index must be between 1 and 5. Got: ${this.secretIndex}`);
        }

        const validModifiers = ['_', '!', '?', '~'];
        this.modifiers.forEach(mod => {
            if (!validModifiers.includes(mod)) {
                throw new Error(`Invalid modifier: ${mod}`);
            }
        });

        if (this.tag !== null && !TAG_REGEX.test(this.tag)) {
            throw new Error(`Invalid tag format (must be 4 chars from base32 alphabet): ${this.tag}`);
        }
    }
}
