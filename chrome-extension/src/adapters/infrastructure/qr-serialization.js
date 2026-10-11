/** QR-only compact transport. Original bundle JSON and crypto stay unchanged. */
const byteFields = ['encryptedData', 'iv', 'exportSalt'];

function encodeBytes(bytes) {
    if (!Array.isArray(bytes) || Array.from(bytes).some(n => !Number.isInteger(n) || n < 0 || n > 255)) {
        throw new Error('Invalid encrypted bundle bytes');
    }
    let binary = '';
    for (const byte of bytes) binary += String.fromCharCode(byte);
    return btoa(binary);
}

export function serializeBundleForQr(bundle) {
    const compact = { ...bundle };
    for (const field of byteFields) compact[field] = encodeBytes(bundle[field]);
    return JSON.stringify(compact);
}

export function deserializeBundleFromQr(text) {
    const bundle = JSON.parse(text);
    for (const field of byteFields) {
        if (typeof bundle[field] !== 'string') throw new Error('Invalid QR bundle encoding');
        const binary = atob(bundle[field]);
        bundle[field] = Array.from(binary, char => char.charCodeAt(0));
    }
    return bundle;
}
