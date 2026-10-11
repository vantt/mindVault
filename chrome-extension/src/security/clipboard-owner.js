import { createSerialQueue } from './serial-queue.js';

export function createClipboardOwner({ read, write, now = Date.now, schedule = setTimeout,
    cancel = clearTimeout, onIdle = () => {} }) {
    const serial = createSerialQueue();
    let owned;
    let timer;
    let version = 0;
    function release() {
        if (owned) owned.text = '';
        owned = undefined;
        cancel(timer);
        timer = undefined;
        onIdle();
    }
    function arm(id, delay) {
        timer = schedule(() => expire(id).catch(() => {}), delay);
    }
    async function expire(id) {
        return serial(async () => {
            if (!owned || owned.id !== id) return;
            const remaining = owned.deadline - now();
            if (remaining > 0) { arm(id, remaining); return; }
            try {
                const current = await read();
                if (owned?.id === id && current === owned.text) await write('');
            } catch {
                // Read denial must never erase a user's later clipboard contents.
            } finally { if (owned?.id === id) release(); }
        });
    }
    async function copy(text) {
        return serial(async () => {
            await write(text);
            cancel(timer);
            if (owned) owned.text = '';
            const id = ++version;
            owned = { text, id, deadline: now() + 30000 };
            arm(id, 30000);
            return { success: true };
        });
    }
    return { copy, expire, pending: () => !!owned, dispose: release };
}
