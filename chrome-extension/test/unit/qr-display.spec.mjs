import { describe, it, expect, vi, afterEach } from 'vitest';
import { createQrDisplay } from '../../src/options/options-qr-display.js';
const bundle = { bundleId: 'synthetic-id', encryptedData: Array(400).fill(1), iv: [], exportSalt: [] };

afterEach(() => { vi.useRealTimers(); vi.restoreAllMocks(); delete globalThis.qrcodegen; delete globalThis.chrome; document.body.replaceChildren(); });

function setup() {
    vi.useFakeTimers();
    globalThis.chrome = { i18n: { getMessage: () => '' } };
    globalThis.qrcodegen = { QrCode: { Ecc: { LOW: 1 }, encodeBinary(bytes) {
        if (bytes.length > 180) throw new RangeError('Data too long');
        return { size: 21, getModule: (x, y) => x === y };
    } } };
    const context = { fillStyle: '', fillRect: vi.fn() };
    vi.spyOn(HTMLCanvasElement.prototype, 'getContext').mockReturnValue(context);
    const container = document.createElement('div'); document.body.append(container);
    return { container, display: createQrDisplay(container, bundle), context };
}

describe('QR display lifetime', () => {
    it('animates then clears frames, canvas, and timer on close', async () => {
        const { container, display, context } = setup();
        const showing = display.show(); await vi.advanceTimersByTimeAsync(0); await showing;
        const counter = container.querySelector('#exp-qr-progress');
        expect(Number(counter.dataset.total)).toBeGreaterThan(1);
        expect(context.fillRect).toHaveBeenCalled();
        await vi.advanceTimersByTimeAsync(1500); expect(counter.dataset.index).toBe('1');
        display.close(); expect(container.childNodes.length).toBe(0); expect(vi.getTimerCount()).toBe(0);
        await vi.advanceTimersByTimeAsync(3000); expect(container.childNodes.length).toBe(0);
    });
    it('closing during preparation prevents a stale QR from appearing', async () => {
        const { container, display } = setup();
        const showing = display.show(); display.close();
        await vi.advanceTimersByTimeAsync(0); await showing;
        expect(container.childNodes.length).toBe(0); expect(vi.getTimerCount()).toBe(0);
    });
    it('shows a safe failure message without rendering a broken QR', async () => {
        const { container, display } = setup();
        globalThis.qrcodegen.QrCode.encodeBinary = () => { throw new Error('private-payload'); };
        const showing = display.show(); await vi.advanceTimersByTimeAsync(0); await showing;
        expect(container.textContent).toContain('Cannot show QR');
        expect(container.textContent).not.toContain('private-payload');
        expect(container.querySelector('canvas')).toBeNull(); expect(vi.getTimerCount()).toBe(0);
        display.close();
    });
});
