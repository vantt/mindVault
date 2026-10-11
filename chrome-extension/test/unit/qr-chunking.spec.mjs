import { describe, it, expect } from 'vitest';
import { chunkForCapacity, chunkBundleForQr } from '../../src/adapters/infrastructure/qr-chunking.js';
const text = JSON.stringify({ bundleId: 'synthetic-id', profileName: 'Bếp 🍳', encryptedData: 'A'.repeat(700) });
const size = value => new TextEncoder().encode(value).length;

describe('QR frames', () => {
    it('uses a single compact bundle at exact capacity', () => {
        expect(chunkForCapacity(text, candidate => size(candidate) <= size(text))).toEqual([text]);
    });
    it('chunks one byte over capacity with complete fitted headers', () => {
        const max = size(text) - 1;
        const frames = chunkForCapacity(text, candidate => size(candidate) <= max);
        expect(frames.length).toBeGreaterThan(1);
        expect(frames.every(frame => size(frame) <= max)).toBe(true);
        const parsed = frames.map(JSON.parse);
        expect(parsed.map(frame => frame.payload).join('')).toBe(text);
        expect(parsed.map(frame => frame.idx)).toEqual(parsed.map((_, idx) => idx));
        expect(parsed.every(frame => frame.bundleId === 'synthetic-id' && frame.total === frames.length)).toBe(true);
    });
    it('preserves Unicode boundaries and covers total/index digit growth', () => {
        const unicode = JSON.stringify({ bundleId: 'synthetic-id', payload: '🍳"\\'.repeat(300) });
        const frames = chunkForCapacity(unicode, candidate => size(candidate) <= 110);
        expect(frames.length).toBeGreaterThan(10);
        const parsed = frames.map(JSON.parse);
        expect(parsed.map(frame => frame.payload).join('')).toBe(unicode);
        expect(parsed.every(frame => !/[\uD800-\uDBFF]$/.test(frame.payload) && !/^[\uDC00-\uDFFF]/.test(frame.payload))).toBe(true);
        expect(frames.every(frame => size(frame) <= 110)).toBe(true);
    });
    it('documents the frame contract as plain substring payload', () => {
        const frame = { bundleId: 'synthetic-id', idx: 0, total: 2, payload: '{"bundleId":' };
        expect(JSON.parse(JSON.stringify(frame))).toEqual(frame);
    });
    it('fails impossible headers and propagates encoder faults', () => {
        expect(() => chunkForCapacity(text, () => false)).toThrow('header');
        expect(() => chunkForCapacity(text, () => { throw new Error('synthetic fault'); })).toThrow('synthetic fault');
    });
    it('reads the encoder only at use time and distinguishes overflow from faults', () => {
        const encodeBinary = (bytes) => {
            if (bytes.length > 150) throw new RangeError('Data too long');
            return {};
        };
        globalThis.qrcodegen = { QrCode: { Ecc: { LOW: 1 }, encodeBinary } };
        try {
            expect(chunkBundleForQr(text).length).toBeGreaterThan(1);
            globalThis.qrcodegen.QrCode.encodeBinary = () => { throw new Error('encoder failed'); };
            expect(() => chunkBundleForQr(text)).toThrow('encoder failed');
        } finally { delete globalThis.qrcodegen; }
    });
});
