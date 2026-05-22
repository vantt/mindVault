import { describe, it, expect } from 'vitest';
import { Recipe } from '../../src/core/domain/recipe.js';

describe('Recipe entity', () => {
    it('accepts tag = null (legacy recipe)', () => {
        const r = new Recipe('fb', '#', 1, [], null);
        expect(r.tag).toBeNull();
    });

    it('accepts valid 4-char tag from alphabet', () => {
        const r = new Recipe('fb', '#', 1, [], 'abcd');
        expect(r.tag).toBe('abcd');
    });

    it('rejects tag length != 4', () => {
        expect(() => new Recipe('fb', '#', 1, [], 'abc')).toThrow(/tag/i);
        expect(() => new Recipe('fb', '#', 1, [], 'abcde')).toThrow(/tag/i);
    });

    it('rejects tag with uppercase', () => {
        expect(() => new Recipe('fb', '#', 1, [], 'ABCD')).toThrow(/tag/i);
    });

    it('rejects tag containing excluded chars (o/l/0/1)', () => {
        for (const bad of ['oabc', 'labc', '0abc', '1abc', 'aboo', 'aaal']) {
            expect(() => new Recipe('fb', '#', 1, [], bad)).toThrow(/tag/i);
        }
    });

    it('preserves existing modifier/position/index validation', () => {
        expect(() => new Recipe('', '#', 1)).toThrow(/hash/i);
        expect(() => new Recipe('fb', 'x', 1)).toThrow(/position/i);
        expect(() => new Recipe('fb', '#', 6)).toThrow(/secret index/i);
        expect(() => new Recipe('fb', '#', 1, ['z'])).toThrow(/modifier/i);
    });
});
