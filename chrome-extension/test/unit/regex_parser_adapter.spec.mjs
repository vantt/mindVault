import { describe, it, expect } from 'vitest';
import { RegexParserAdapter } from '../../src/adapters/infrastructure/regex_parser_adapter.js';

describe('RegexParserAdapter — tag suffix', () => {
    const parser = new RegexParserAdapter();

    it('parses untagged recipe with tag=null', () => {
        const r = parser.parse('fb#1');
        expect(r).not.toBeNull();
        expect(r.tag).toBeNull();
    });

    it('parses tagged recipe with 4-char tag', () => {
        const r = parser.parse('fb#1.abcd');
        expect(r).not.toBeNull();
        expect(r.tag).toBe('abcd');
    });

    it('parses tagged recipe with modifiers', () => {
        const r = parser.parse('fb#1_!.xyz2');
        expect(r).not.toBeNull();
        expect(r.modifiers.sort()).toEqual(['!', '_']);
        expect(r.tag).toBe('xyz2');
    });

    it('rejects 3-char tag', () => {
        expect(parser.parse('fb#1.abc')).toBeNull();
    });

    it('rejects 5-char tag', () => {
        expect(parser.parse('fb#1.abcde')).toBeNull();
    });

    it('rejects uppercase tag', () => {
        expect(parser.parse('fb#1.ABCD')).toBeNull();
    });

    it('rejects double tag', () => {
        expect(parser.parse('fb#1.abcd.efgh')).toBeNull();
    });

    it('rejects tag with excluded alphabet chars via Recipe.validate', () => {
        // regex allows [a-z2-9] so `l` slips past the regex but Recipe.validate rejects it
        expect(parser.parse('fb#1.labc')).toBeNull();
        expect(parser.parse('fb#1.oabc')).toBeNull();
    });

    it('preserves backward compat — all v2.1 forms still parse', () => {
        expect(parser.parse('gmail@2')).not.toBeNull();
        expect(parser.parse('r4nd0m#1_!?~')).not.toBeNull();
        expect(parser.parse('Site$3')).not.toBeNull();
    });
});
