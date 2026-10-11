import { createSerialQueue } from './serial-queue.js';

let creating;
const serial = createSerialQueue();
const PATH = 'offscreen/offscreen.html';
export async function ensureOffscreen() {
    const existing = await chrome.runtime.getContexts({ contextTypes: ['OFFSCREEN_DOCUMENT'],
        documentUrls: [chrome.runtime.getURL(PATH)] });
    if (existing.length) return;
    if (!creating) {
        creating = chrome.offscreen.createDocument({ url: PATH, reasons: ['WORKERS', 'CLIPBOARD'],
            justification: 'Authenticate the Cookbook in a bundled Argon2 worker and safely clear only its copied dish after the popup closes.' });
    }
    try { await creating; }
    finally { creating = null; }
}
export async function callOffscreen(action, values = {}) {
    return serial(async () => {
        await ensureOffscreen();
        return chrome.runtime.sendMessage({ target: 'offscreen', action, ...values });
    });
}
export function closeOffscreenIfIdle() {
    return serial(async () => {
        const contexts = await chrome.runtime.getContexts({ contextTypes: ['OFFSCREEN_DOCUMENT'],
            documentUrls: [chrome.runtime.getURL(PATH)] });
        if (!contexts.length) return;
        const state = await chrome.runtime.sendMessage({ target: 'offscreen', action: 'OFFSCREEN_STATUS' });
        if (!state?.busy) await chrome.offscreen.closeDocument();
    });
}
