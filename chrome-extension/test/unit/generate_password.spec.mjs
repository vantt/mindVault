
import { describe, it, expect } from 'vitest';
import { GeneratePassword } from '../../src/core/usecases/generate_password.js';
import { RegexParserAdapter } from '../../src/adapters/infrastructure/regex_parser_adapter.js';

class MockStorage {
    constructor(secrets = {}) {
        this.secrets = secrets;
    }
    async getSecret(index) {
        return this.secrets[index] || null;
    }
    async isAuthenticated() { return true; }
}

describe('GeneratePassword Use Case', () => {
    const parser = new RegexParserAdapter();
    const secrets = {
        1: "Basic*",
        2: "Secure#",
        3: "Trade&",
        4: "Ultra$"
    };
    const storage = new MockStorage(secrets);
    const useCase = new GeneratePassword(parser, storage);

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Prefix (#) correctly', async () => {
        // "r4nd0m" + "#1" + "Basic*" -> "Basic*r4nd0m"
        const result = await useCase.execute("r4nd0m#1");
        expect(result).toBe("Basic*r4nd0m");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Suffix ($) correctly', async () => {
        // "r4nd0m$1" -> "r4nd0mBasic*"
        const result = await useCase.execute("r4nd0m$1");
        expect(result).toBe("r4nd0mBasic*");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Middle (@) correctly', async () => {
        // "r4nd0m@2" + "Secure#" (2=Secure#) -> "r4nSecure#d0m"
        const result = await useCase.execute("r4nd0m@2");
        expect(result).toBe("r4nSecure#d0m");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Interleave (%) correctly', async () => {
        // "r4nd0m%3" + "Trade&" -> r+T, 4+r, n+a, d+d, 0+e, m+& -> "rT4rnadd0em&"
        const result = await useCase.execute("r4nd0m%3");
        expect(result).toBe("rT4rnadd0em&");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Interleave Pairs (^) correctly', async () => {
        // "r4nd0m^3" + "Trade&" -> r4+Tr, nd+ad, 0m+e& -> "r4Trndad0me&"
        const result = await useCase.execute("r4nd0m^3");
        expect(result).toBe("r4Trndad0me&");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Modifier Reverse Position (_) correctly', async () => {
        // "r4nd0m#1_" -> Suffix -> "r4nd0mBasic*"
        const result = await useCase.execute("r4nd0m#1_");
        expect(result).toBe("r4nd0mBasic*");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Modifier Uppercase (!) correctly', async () => {
        // "r4nd0m#1!" -> Secret "BASIC*" -> "BASIC*r4nd0m"
        const result = await useCase.execute("r4nd0m#1!");
        expect(result).toBe("BASIC*r4nd0m");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Modifier Remove Special (~) correctly', async () => {
        // "r4nd0m#3~" -> Secret "Trade&" -> "Trade" -> "Trader4nd0m"
        const result = await useCase.execute("r4nd0m#3~");
        expect(result).toBe("Trader4nd0m");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Modifier Reverse Secret (?) correctly', async () => {
        // "r4nd0m#1?" -> Secret "Basic*" -> "*cisaB" -> "*cisaBr4nd0m"
        const result = await useCase.execute("r4nd0m#1?");
        expect(result).toBe("*cisaBr4nd0m");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Multiple Modifiers (_!) correctly', async () => {
        // "r4nd0m#1_!" -> Reverse Position (Suffix) + Uppercase -> "r4nd0mBASIC*"
        const result = await useCase.execute("r4nd0m#1_!");
        expect(result).toBe("r4nd0mBASIC*");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Version Handling (_v2) correctly', async () => {
        // "r4nd0m#1_v2" -> Secret "Basic*" -> "Basic*_v2" -> "Basic*_v2r4nd0m"
        const result = await useCase.execute("r4nd0m#1_v2");
        expect(result).toBe("Basic*_v2r4nd0m");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should throw error for invalid recipe', async () => {
        await expect(useCase.execute("invalid")).rejects.toThrow("Invalid recipe format");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should throw error if secret not found', async () => {
        await expect(useCase.execute("r4nd0m#5")).rejects.toThrow("Secret #5 not found");
    });
});
