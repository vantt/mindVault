import { describe, it, expect } from 'vitest';
import {
    computeRecipeTag,
    verifyRecipeTag,
    canonicalize,
    TAG_ALPHABET,
    TAG_LENGTH,
} from '../../src/core/domain/tag-codec.js';

const FIXED = {
    hash: 'fb', position: '#', secretIndex: 1, modifiers: [],
    secret: 'Basic*', sheetId: 'sheet-A-12345',
};

describe('tag-codec', () => {
    it('canonicalize concatenates fields and sorts modifiers', () => {
        expect(canonicalize({ hash: 'fb', position: '#', secretIndex: 1, modifiers: [] }))
            .toBe('fb#1');
        // modifier sort invariance
        expect(canonicalize({ hash: 'fb', position: '#', secretIndex: 1, modifiers: ['_', '!'] }))
            .toBe('fb#1!_');
        expect(canonicalize({ hash: 'fb', position: '#', secretIndex: 1, modifiers: ['!', '_'] }))
            .toBe('fb#1!_');
    });

    it('computeRecipeTag returns deterministic 4-char string', async () => {
        const t1 = await computeRecipeTag(FIXED);
        const t2 = await computeRecipeTag(FIXED);
        expect(t1).toBe(t2);
        expect(t1).toHaveLength(TAG_LENGTH);
    });

    it('tag chars are all in TAG_ALPHABET (no o/l/0/1)', async () => {
        const tag = await computeRecipeTag(FIXED);
        for (const ch of tag) {
            expect(TAG_ALPHABET.includes(ch)).toBe(true);
        }
        // Sanity: alphabet excludes these chars
        for (const bad of ['o', 'l', '0', '1']) {
            expect(TAG_ALPHABET.includes(bad)).toBe(false);
        }
    });

    it('different sheetId produces different tag', async () => {
        const t1 = await computeRecipeTag(FIXED);
        const t2 = await computeRecipeTag({ ...FIXED, sheetId: 'different-sheet' });
        expect(t1).not.toBe(t2);
    });

    it('different secret produces different tag', async () => {
        const t1 = await computeRecipeTag(FIXED);
        const t2 = await computeRecipeTag({ ...FIXED, secret: 'OtherSecret' });
        expect(t1).not.toBe(t2);
    });

    it('modifier order invariance: ["_","!"] == ["!","_"]', async () => {
        const t1 = await computeRecipeTag({ ...FIXED, modifiers: ['_', '!'] });
        const t2 = await computeRecipeTag({ ...FIXED, modifiers: ['!', '_'] });
        expect(t1).toBe(t2);
    });

    it('throws when secret missing', async () => {
        await expect(computeRecipeTag({ ...FIXED, secret: '' })).rejects.toThrow(/secret/i);
    });

    it('throws when sheetId missing', async () => {
        await expect(computeRecipeTag({ ...FIXED, sheetId: '' })).rejects.toThrow(/sheetId/i);
    });

    describe('verifyRecipeTag', () => {
        it('returns true on match', async () => {
            const tag = await computeRecipeTag(FIXED);
            const recipe = {
                hash: FIXED.hash, position: FIXED.position,
                secretIndex: FIXED.secretIndex, modifiers: FIXED.modifiers, tag,
            };
            expect(await verifyRecipeTag(recipe, FIXED.secret, FIXED.sheetId)).toBe(true);
        });

        it('returns false on tag mismatch', async () => {
            const recipe = {
                hash: FIXED.hash, position: FIXED.position,
                secretIndex: FIXED.secretIndex, modifiers: FIXED.modifiers, tag: 'zzzz',
            };
            expect(await verifyRecipeTag(recipe, FIXED.secret, FIXED.sheetId)).toBe(false);
        });

        it('returns false when recipe.tag is null', async () => {
            const recipe = {
                hash: FIXED.hash, position: FIXED.position,
                secretIndex: FIXED.secretIndex, modifiers: FIXED.modifiers, tag: null,
            };
            expect(await verifyRecipeTag(recipe, FIXED.secret, FIXED.sheetId)).toBe(false);
        });
    });
});
