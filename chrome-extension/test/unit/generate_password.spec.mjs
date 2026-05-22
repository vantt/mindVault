
import { describe, it, expect } from 'vitest';
import { GeneratePassword } from '../../src/core/usecases/generate_password.js';
import { RegexParserAdapter } from '../../src/adapters/infrastructure/regex_parser_adapter.js';
import { computeRecipeTag } from '../../src/core/domain/tag-codec.js';

// MockStorage mirrors ChromeStorageAdapter.getSecretForGeneration shape after Phase 04.
// For own profiles: rawSecret === effectiveSecret (Option B — no HKDF binding).
// For shared profiles: effectiveSecret = HKDF(rawSecret, sheetId); we don't simulate
// the actual HKDF here, just return a marker so tests can assert "bound vs unbound".
class MockStorage {
    constructor(secrets = {}, { isShared = false } = {}) {
        this.secrets = secrets;
        this.isShared = isShared;
    }
    async getSecretForGeneration(index, sheetId = null, _profileNameOverride = null) {
        const rawSecret = this.secrets[index] || null;
        if (!rawSecret) return null;
        const shouldBind = this.isShared && !!sheetId;
        const effectiveSecret = shouldBind ? `BOUND(${rawSecret},${sheetId})` : rawSecret;
        return {
            rawSecret,
            effectiveSecret,
            profileName: this.isShared ? "TeamShare" : "Default",
            isShared: this.isShared,
            settings: {},
            sheetId: sheetId || null,
        };
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
        expect(result.password).toBe("Basic*r4nd0m");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Suffix ($) correctly', async () => {
        // "r4nd0m$1" -> "r4nd0mBasic*"
        const result = await useCase.execute("r4nd0m$1");
        expect(result.password).toBe("r4nd0mBasic*");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Middle (@) correctly', async () => {
        // "r4nd0m@2" + "Secure#" (2=Secure#) -> "r4nSecure#d0m"
        const result = await useCase.execute("r4nd0m@2");
        expect(result.password).toBe("r4nSecure#d0m");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Interleave (%) correctly', async () => {
        // "r4nd0m%3" + "Trade&" -> r+T, 4+r, n+a, d+d, 0+e, m+& -> "rT4rnadd0em&"
        const result = await useCase.execute("r4nd0m%3");
        expect(result.password).toBe("rT4rnadd0em&");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Interleave Pairs (^) correctly', async () => {
        // "r4nd0m^3" + "Trade&" -> r4+Tr, nd+ad, 0m+e& -> "r4Trndad0me&"
        const result = await useCase.execute("r4nd0m^3");
        expect(result.password).toBe("r4Trndad0me&");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Modifier Reverse Position (_) correctly', async () => {
        // "r4nd0m#1_" -> Suffix -> "r4nd0mBasic*"
        const result = await useCase.execute("r4nd0m#1_");
        expect(result.password).toBe("r4nd0mBasic*");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Modifier Uppercase (!) correctly', async () => {
        // "r4nd0m#1!" -> Secret "BASIC*" -> "BASIC*r4nd0m"
        const result = await useCase.execute("r4nd0m#1!");
        expect(result.password).toBe("BASIC*r4nd0m");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Modifier Remove Special (~) correctly', async () => {
        // "r4nd0m#3~" -> Secret "Trade&" -> "Trade" -> "Trader4nd0m"
        const result = await useCase.execute("r4nd0m#3~");
        expect(result.password).toBe("Trader4nd0m");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Modifier Reverse Secret (?) correctly', async () => {
        // "r4nd0m#1?" -> Secret "Basic*" -> "*cisaB" -> "*cisaBr4nd0m"
        const result = await useCase.execute("r4nd0m#1?");
        expect(result.password).toBe("*cisaBr4nd0m");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should handle Multiple Modifiers (_!) correctly', async () => {
        // "r4nd0m#1_!" -> Reverse Position (Suffix) + Uppercase -> "r4nd0mBASIC*"
        const result = await useCase.execute("r4nd0m#1_!");
        expect(result.password).toBe("r4nd0mBASIC*");
    });

    // Version suffix (`_vN`) was removed from the recipe grammar — rotating a password
    // is now done by changing the hash. Parser must reject any legacy `_vN` recipe.
    it('should reject legacy version suffix (_v2) as invalid', async () => {
        await expect(useCase.execute("r4nd0m#1_v2")).rejects.toThrow("Invalid recipe format");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should throw error for invalid recipe', async () => {
        await expect(useCase.execute("invalid")).rejects.toThrow("Invalid recipe format");
    });

    // @approved - Golden Test: Source of Truth. Do not modify without explicit approval.
    it('should throw error if secret not found', async () => {
        await expect(useCase.execute("r4nd0m#5")).rejects.toThrow("Secret #5 not found");
    });

    // ASCII-only by design — Unicode hashes risk NFC/NFD normalization mismatch across devices.
    it('should reject Vietnamese (Unicode) characters in hash', async () => {
        await expect(useCase.execute("gmáil#1")).rejects.toThrow("Invalid recipe format");
    });

    it('should reject Cyrillic lookalike characters in hash', async () => {
        // Cyrillic 'а' (U+0430) instead of Latin 'a' — looks identical but bytes differ
        await expect(useCase.execute("gmа il#1")).rejects.toThrow("Invalid recipe format");
    });

    // ─── Verification Tag (v2.2 — Option B) ─────────────────────────────────
    // See docs/recipe-tag-design-rationale.md

    describe('Verification tag (Option B)', () => {
        const SHEET_A = "sheetA-12345";
        const SHEET_B = "sheetB-67890";

        it('untagged recipe + sheetId returns warning legacy_no_tag', async () => {
            const result = await useCase.execute("r4nd0m#1", SHEET_A);
            expect(result.password).toBe("Basic*r4nd0m");
            expect(result.warning).toBe("legacy_no_tag");
        });

        it('untagged recipe + no sheetId has no warning', async () => {
            const result = await useCase.execute("r4nd0m#1");
            expect(result.warning).toBeUndefined();
        });

        // COOKBOOK CANARY — Option B's value-proposition guard.
        // Own-profile tagged recipe at correct sheetId MUST produce identical password
        // to the untagged equivalent. Tag is verification-only metadata.
        it('COOKBOOK: own-profile tagged-vs-untagged produce identical password', async () => {
            const tag = await computeRecipeTag({
                hash: "r4nd0m", position: "#", secretIndex: 1, modifiers: [],
                secret: "Basic*", sheetId: SHEET_A,
            });
            const untagged = await useCase.execute("r4nd0m#1", SHEET_A);
            const tagged = await useCase.execute(`r4nd0m#1.${tag}`, SHEET_A);
            expect(tagged.password).toBe(untagged.password);
            expect(tagged.password).toBe("Basic*r4nd0m");
        });

        it('tagged recipe with WRONG tag throws RECIPE_MISMATCH', async () => {
            await expect(useCase.execute("r4nd0m#1.zzzz", SHEET_A))
                .rejects.toMatchObject({ code: "RECIPE_MISMATCH" });
        });

        it('tagged recipe with correct tag but DIFFERENT sheetId throws RECIPE_MISMATCH', async () => {
            const tagForA = await computeRecipeTag({
                hash: "r4nd0m", position: "#", secretIndex: 1, modifiers: [],
                secret: "Basic*", sheetId: SHEET_A,
            });
            await expect(useCase.execute(`r4nd0m#1.${tagForA}`, SHEET_B))
                .rejects.toMatchObject({ code: "RECIPE_MISMATCH" });
        });

        it('tagged recipe with no sheetId throws RECIPE_MISMATCH', async () => {
            const tag = await computeRecipeTag({
                hash: "r4nd0m", position: "#", secretIndex: 1, modifiers: [],
                secret: "Basic*", sheetId: SHEET_A,
            });
            await expect(useCase.execute(`r4nd0m#1.${tag}`))
                .rejects.toMatchObject({ code: "RECIPE_MISMATCH" });
        });

        it('shared profile with tag uses bound effectiveSecret (PRD §2.4 path)', async () => {
            const sharedStorage = new MockStorage({ 1: "DS_TS_1" }, { isShared: true });
            const sharedUseCase = new GeneratePassword(parser, sharedStorage);
            const tag = await computeRecipeTag({
                hash: "r4nd0m", position: "#", secretIndex: 1, modifiers: [],
                secret: "DS_TS_1", sheetId: SHEET_A,
            });
            const result = await sharedUseCase.execute(`r4nd0m#1.${tag}`, SHEET_A);
            // Bound marker proves HKDF path was taken for shared profile
            expect(result.password).toBe(`BOUND(DS_TS_1,${SHEET_A})r4nd0m`);
            expect(result.isShared).toBe(true);
        });
    });
});
