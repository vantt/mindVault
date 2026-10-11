import { createClipboardOwner } from '../security/clipboard-owner.js';
import { readClipboard, writeClipboard } from './clipboard-dom.js';

const worker = new Worker(new URL('./crypto-worker.js', import.meta.url), { type: 'module' });
const pending = new Map();
function notifyIdle() {
    if (!pending.size && !clipboard.pending()) {
        chrome.runtime.sendMessage({ action: 'OFFSCREEN_IDLE' }).catch(() => {});
    }
}
const clipboard = createClipboardOwner({ read: readClipboard, write: writeClipboard, onIdle: notifyIdle });
worker.onmessage = ({ data }) => {
    const entry = pending.get(data.id);
    if (!entry) { if (data.key) data.key.k = ''; return; }
    pending.delete(data.id);
    clearTimeout(entry.timer);
    entry.resolve(data);
};
worker.onerror = event => {
    event.preventDefault();
    for (const entry of pending.values()) {
        clearTimeout(entry.timer);
        entry.resolve({ success: false, code: 'CRYPTO_UNAVAILABLE' });
    }
    pending.clear();
    notifyIdle();
};
function derive(request) {
    return new Promise(resolve => {
        const id = crypto.randomUUID();
        const timer = setTimeout(() => {
            pending.delete(id);
            resolve({ success: false, code: 'CRYPTO_UNAVAILABLE' });
            notifyIdle();
        }, 120000);
        pending.set(id, { resolve, timer });
        try { worker.postMessage({ id, password: request.password, salt: request.salt, record: request.record, create: request.create }); }
        catch { clearTimeout(timer); pending.delete(id); resolve({ success: false, code: 'CRYPTO_UNAVAILABLE' }); notifyIdle(); }
    });
}
chrome.runtime.onMessage.addListener((request, sender, reply) => {
    if (request.target !== 'offscreen' || sender.id !== chrome.runtime.id || sender.tab
        || sender.url !== chrome.runtime.getURL('service_worker.js')) return;
    if (request.action === 'DERIVE_KEY') {
        derive(request).then(answer => {
            reply(answer);
            if (answer.key) answer.key.k = '';
            notifyIdle();
        }, () => reply({ success: false, code: 'CRYPTO_UNAVAILABLE' }));
        return true;
    }
    if (request.action === 'COPY_DISH') {
        clipboard.copy(request.text).then(reply, () => reply({ success: false, code: 'CLIPBOARD_UNAVAILABLE' }));
        return true;
    }
    if (request.action === 'OFFSCREEN_STATUS') reply({ busy: pending.size > 0 || clipboard.pending() });
});
