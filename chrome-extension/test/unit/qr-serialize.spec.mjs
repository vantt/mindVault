import { describe, it, expect } from 'vitest';
import { serializeBundleForQr, deserializeBundleFromQr } from '../../src/adapters/infrastructure/qr-serialization.js';

const bundle = {
    type: 'passchef-fullaccess-share', version: '1.0', bundleId: 'synthetic-qr-id',
    profileName: 'Bếp 🍳', exportedAt: '2026-10-11T00:00:00Z', sheetId: null,
    encryptedData: [0, 1, 127, 128, 254, 255], iv: Array(12).fill(0), exportSalt: Array(16).fill(255)
};

describe('encrypted cookbook QR serialization', () => {
    it('roundtrips the exact original bundle without mutation', () => {
        const original = JSON.stringify(bundle);
        const compact = serializeBundleForQr(bundle);
        expect(JSON.parse(compact).encryptedData).toBe('AAF/gP7/');
        expect(deserializeBundleFromQr(compact)).toEqual(bundle);
        expect(JSON.stringify(bundle)).toBe(original);
        expect(compact).not.toContain('\n');
    });
    it('preserves empty encrypted bytes and optional metadata', () => {
        const empty = { ...bundle, encryptedData: [], extra: 'synthetic metadata' };
        expect(deserializeBundleFromQr(serializeBundleForQr(empty))).toEqual(empty);
    });
    it('rejects malformed byte arrays and QR fields rather than dropping data', () => {
        expect(() => serializeBundleForQr({ ...bundle, iv: [256] })).toThrow();
        expect(() => serializeBundleForQr({ ...bundle, iv: [-1] })).toThrow();
        expect(() => serializeBundleForQr({ ...bundle, iv: Array(2) })).toThrow();
        expect(() => deserializeBundleFromQr(JSON.stringify(bundle))).toThrow();
        expect(() => deserializeBundleFromQr('{bad json')).toThrow();
    });
});
