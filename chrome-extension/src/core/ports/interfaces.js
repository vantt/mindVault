/**
 * @interface IParser
 */
export class IParser {
    /**
     * Parses a string into a Recipe object.
     * @param {string} text 
     * @returns {import("../domain/recipe").Recipe | null}
     */
    parse(text) {
        throw new Error("Method not implemented.");
    }
}

/**
 * @interface IStorageRepository
 */
export class IStorageRepository {
    /**
     * Retrieves the decrypted secret for the given index.
     * @param {number} index 
     * @returns {Promise<string>}
     */
    getSecret(index) {
        throw new Error("Method not implemented.");
    }

    /**
     * Checks if the user is authenticated (session active).
     * @returns {Promise<boolean>}
     */
    isAuthenticated() {
        throw new Error("Method not implemented.");
    }
}

/**
 * @interface ICrypto
 */
export class ICrypto {
    /**
     * Hashes or transforms strings if needed by generic operations.
     * Note: Core password generation logic (interleaving etc) might be pure string manipulation,
     * but if we need crypto operations (like hashing the master password), it goes here.
     * For now, this is a placeholder primarily for the infrastructure layer.
     */
}
