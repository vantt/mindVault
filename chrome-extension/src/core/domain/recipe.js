/**
 * Domain Entity representing a parsed password recipe.
 * Encapsulates the structure: <hash><position><secret_num>[modifiers][_version]
 */
export class Recipe {
    constructor(hash, position, secretIndex, modifiers = [], version = null) {
        this.hash = hash;
        this.position = position;
        this.secretIndex = parseInt(secretIndex, 10);
        this.modifiers = modifiers; // Array of chars: ['_', '!', '?', '~']
        this.version = version;

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
    }
}
