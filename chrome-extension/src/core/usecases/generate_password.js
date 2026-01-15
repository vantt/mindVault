
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
     * Executes the password generation use case.
     * @param {string} recipeText 
     * @returns {Promise<string>}
     */
    async execute(recipeText) {
        const recipe = this.parser.parse(recipeText);
        
        if (!recipe) {
            throw new Error("Invalid recipe format");
        }

        // 1. Get Secret
        let secret = await this.storage.getSecret(recipe.secretIndex);
        if (!secret) {
            throw new Error(`Secret #${recipe.secretIndex} not found`);
        }

        // 2. Apply Version (if applicable)
        if (recipe.version) {
            // MVP Strategy: Append version to secret. 
            // This ensures "Basic" vs "Basic_v2" generates different results.
            // Future: Load pattern from settings.
            secret = `${secret}_${recipe.version}`;
        }

        // Let's check modifiers first as they apply to the secret.
        
        // Modifier: ~ (Remove special chars)
        if (recipe.modifiers.includes('~')) {
            secret = secret.replace(/[^\w\s]|_/g, ""); // Keep alphanumeric. _ is usually considered 'word' but typically 'special' in this context?
            // PRD: "Basic*" -> "Basic". * is special.
            // regex `[^\w]` removes everything not a-z, A-Z, 0-9, _. 
            // If I want to remove `_` too, I need `[^\w]`. 
            // Wait, `\w` includes `_`. 
            // "Remove special chars" usually means keep Alphanumeric.
            secret = secret.replace(/[^a-zA-Z0-9]/g, "");
        }

        // Modifier: ? (Reverse secret)
        if (recipe.modifiers.includes('?')) {
            secret = secret.split('').reverse().join('');
        }

        // Modifier: ! (Uppercase secret)
        if (recipe.modifiers.includes('!')) {
            secret = secret.toUpperCase();
        }

        // Modifier: _ (Reverse position)
        // This affects the "Position" logic, not the secret itself directly (except for where it goes).
        let position = recipe.position;
        if (recipe.modifiers.includes('_')) {
            if (position === '#') position = '$';
            else if (position === '$') position = '#';
            // what about @, %, ^? Assuming no-op or undefined. 
            // PRD only gave example for #.
        }

        const hash = recipe.hash;

        switch (position) {
            case '#': // Prefix: Secret + Hash? PRD says: "secret + hash" -> "Basic*r4nd0m"
                // Wait. Example: "r4nd0m" + "#1" + secret("Basic*") -> "Basic*r4nd0m".
                // Yes, Secret First.
                return secret + hash;
            
            case '$': // Suffix: Hash + Secret
                return hash + secret;
            
            case '@': // Middle: hash[0:mid] + secret + hash[mid:]
                const mid = Math.floor(hash.length / 2);
                return hash.slice(0, mid) + secret + hash.slice(mid);
            
            case '%': // Interleave chars
                return this.interleave(hash, secret, 1);
            
            case '^': // Interleave pairs
                return this.interleave(hash, secret, 2);
            
            default:
                throw new Error(`Unknown position: ${position}`);
        }
    }

    interleave(s1, s2, chunkSize) {
        let result = "";
        let i = 0, j = 0;
        while (i < s1.length || j < s2.length) {
            if (i < s1.length) {
                result += s1.substr(i, chunkSize);
                i += chunkSize;
            }
            if (j < s2.length) {
                result += s2.substr(j, chunkSize);
                j += chunkSize;
            }
        }
        return result;
    }
}
