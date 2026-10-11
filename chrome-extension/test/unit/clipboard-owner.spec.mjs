import { describe, it, expect, vi } from 'vitest';
import { createClipboardOwner } from '../../src/security/clipboard-owner.js';

function fixture() {
    let clock = 0, text = '', timer;
    const write = vi.fn(async value => { text = value; });
    const read = vi.fn(async () => text);
    const owner = createClipboardOwner({ write, read, now: () => clock,
        schedule: (callback, delay) => { timer = { callback, delay }; return timer; }, cancel: () => {} });
    return { owner, write, read, timer: () => timer, text: () => text,
        external: value => { text = value; }, tick: ms => { clock += ms; } };
}
describe('owned clipboard deadline', () => {
    it('clears only its own dish at30s without depending on a popup', async () => {
        const f = fixture(); await f.owner.copy('synthetic-dish'); f.tick(29999);
        await f.owner.expire(1); expect(f.text()).toBe('synthetic-dish');
        f.tick(1); await f.owner.expire(1); expect(f.text()).toBe(''); expect(f.owner.pending()).toBe(false);
    });
    it('preserves later external clipboard text', async () => {
        const f = fixture(); await f.owner.copy('synthetic-dish'); f.external('user-later-text'); f.tick(30000);
        await f.owner.expire(1); expect(f.text()).toBe('user-later-text'); expect(f.write).toHaveBeenCalledTimes(1);
    });
    it('does not write when read permission is denied', async () => {
        const f = fixture(); await f.owner.copy('synthetic-dish'); f.read.mockRejectedValue(new Error('Denied'));
        f.tick(30000); await f.owner.expire(1); expect(f.write).toHaveBeenCalledTimes(1);
        expect(f.owner.pending()).toBe(false);
    });
    it('an old deadline cannot clear a newer copied dish', async () => {
        const f = fixture(); await f.owner.copy('first-dish'); f.tick(10000); await f.owner.copy('second-dish');
        f.tick(20000); await f.owner.expire(1); expect(f.text()).toBe('second-dish');
        await f.owner.expire(2); expect(f.text()).toBe('second-dish');
        f.tick(10000); await f.owner.expire(2); expect(f.text()).toBe('');
    });
    it('failed copy reports failure and preserves the previous ownership', async () => {
        const f = fixture(); await f.owner.copy('first-dish'); f.write.mockRejectedValueOnce(new Error('Denied'));
        await expect(f.owner.copy('second-dish')).rejects.toThrow();
        expect(f.text()).toBe('first-dish'); expect(f.owner.pending()).toBe(true);
    });
});
