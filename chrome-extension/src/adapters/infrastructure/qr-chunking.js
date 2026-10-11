/**
 * Single QR: compact bundle JSON with base64 encryptedData/iv/exportSalt fields.
 * Multi QR: JSON {bundleId, idx, total, payload}; idx is zero-based, bundleId is
 * the original bundle's ID, payload is a plain substring of the compact JSON.
 * Reassemble payloads by idx, require one consistent ID/total and every index,
 * then restore the byte arrays. The encrypted bundle authenticates its contents.
 * This extension sends frames only; the phone reader is a separate feature.
 */
export function chunkForCapacity(compactStr, fitsFn) {
    if (fitsFn(compactStr)) return [compactStr];
    const { bundleId } = JSON.parse(compactStr);
    if (typeof bundleId !== 'string' || !bundleId) throw new Error('QR bundle ID is missing');
    const chars = Array.from(compactStr); // Do not split supplementary Unicode characters.
    const frames = [];
    let start = 0;
    // At most one frame per character; its digit count safely budgets final total.
    const conservativeTotal = chars.length;
    while (start < chars.length) {
        const idx = frames.length;
        let low = 1;
        let high = chars.length - start;
        let best = 0;
        while (low <= high) {
            const length = Math.floor((low + high) / 2);
            const candidate = JSON.stringify({ bundleId, idx, total: conservativeTotal,
                payload: chars.slice(start, start + length).join('') });
            if (fitsFn(candidate)) { best = length; low = length + 1; }
            else high = length - 1;
        }
        if (!best) throw new Error('QR frame header cannot fit. Download the cookbook bundle instead.');
        frames.push({ bundleId, idx, payload: chars.slice(start, start + best).join('') });
        start += best;
    }
    return frames.map(frame => {
        const text = JSON.stringify({ bundleId: frame.bundleId, idx: frame.idx,
            total: frames.length, payload: frame.payload });
        if (!fitsFn(text)) throw new Error('QR frame exceeds encoder capacity');
        return text;
    });
}

export function encodeQrText(text) {
    const encoder = globalThis.qrcodegen;
    if (!encoder) throw new Error('QR encoder unavailable. Reopen Options and try again.');
    return encoder.QrCode.encodeBinary(Array.from(new TextEncoder().encode(text)), encoder.QrCode.Ecc.LOW);
}

export function chunkBundleForQr(compactStr) {
    return chunkForCapacity(compactStr, text => {
        try { encodeQrText(text); return true; }
        catch (error) {
            if (error instanceof RangeError) return false;
            throw error;
        }
    });
}
