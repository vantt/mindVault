import { chunkBundleForQr, encodeQrText } from '../adapters/infrastructure/qr-chunking.js';
import { serializeBundleForQr } from '../adapters/infrastructure/qr-serialization.js';
import { t } from '../i18n-loader.js';

/** Owns the currently visible encrypted QR frames and its animation timer. */
export function createQrDisplay(container, bundle) {
    let frames = [];
    let timer = null;
    let generation = 0;
    let payload = serializeBundleForQr(bundle);
    const canvas = document.createElement('canvas');
    canvas.id = 'exp-qr-canvas';
    canvas.setAttribute('aria-label', t('qrCanvasLabel') || 'Encrypted cookbook QR');
    const counter = document.createElement('p');
    counter.id = 'exp-qr-progress';
    counter.setAttribute('aria-live', 'polite');
    const status = document.createElement('p');
    status.id = 'exp-qr-status';
    status.setAttribute('role', 'status');
    container.replaceChildren(status);

    function draw(index) {
        const qr = encodeQrText(frames[index]);
        const scale = 4;
        const border = 4;
        canvas.width = canvas.height = (qr.size + border * 2) * scale;
        const context = canvas.getContext('2d');
        if (!context) throw new Error('Canvas unavailable');
        context.fillStyle = '#fff';
        context.fillRect(0, 0, canvas.width, canvas.height);
        context.fillStyle = '#000';
        for (let y = 0; y < qr.size; y++) for (let x = 0; x < qr.size; x++) {
            if (qr.getModule(x, y)) context.fillRect((x + border) * scale, (y + border) * scale, scale, scale);
        }
        counter.textContent = `QR ${index + 1}/${frames.length}`;
        counter.dataset.index = String(index);
        counter.dataset.total = String(frames.length);
    }

    function fail() {
        if (timer !== null) clearInterval(timer);
        timer = null;
        frames = [];
        status.textContent = t('qrDisplayError') || 'Cannot show QR. Download the cookbook bundle and try again.';
        container.replaceChildren(status);
    }

    return {
        async show() {
            if (timer !== null) clearInterval(timer);
            timer = null;
            const attempt = ++generation;
            status.textContent = t('qrPreparing') || 'Preparing encrypted cookbook QR…';
            container.replaceChildren(status);
            await new Promise(resolve => setTimeout(resolve, 0));
            if (attempt !== generation || !payload) return;
            try {
                frames = chunkBundleForQr(payload);
                container.replaceChildren(canvas, counter);
                draw(0);
                if (frames.length > 1) {
                    let index = 0;
                    timer = setInterval(() => {
                        if (attempt !== generation || !container.isConnected) return this.close();
                        try { index = (index + 1) % frames.length; draw(index); }
                        catch { fail(); }
                    }, 1500);
                }
            } catch { fail(); }
        },
        close() {
            generation++;
            if (timer !== null) clearInterval(timer);
            timer = null;
            frames = [];
            payload = '';
            canvas.width = canvas.height = 0;
            container.replaceChildren();
        }
    };
}
