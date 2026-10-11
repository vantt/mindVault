import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
const mocks = vi.hoisted(() => ({ create: vi.fn(), decrypt: vi.fn() }));
vi.mock('../../src/adapters/infrastructure/export-import-adapter.js', () => ({
    ExportImportAdapter: class { createFullAccessBundle(...args) { return mocks.create(...args); } }
}));
vi.mock('../../src/adapters/infrastructure/aes-storage-crypto-helper.js', () => ({ decryptWithKey: mocks.decrypt }));
vi.mock('../../src/i18n-loader.js', () => ({ t: () => '' }));
import { openExportWizard } from '../../src/options/options-export-wizard.js';

function deferred() {
    let resolve;
    const promise = new Promise(done => { resolve = done; });
    return { promise, resolve };
}

const bundle = { bundleId: 'synthetic-id', encryptedData: [1], iv: [2], exportSalt: [3] };

beforeEach(() => {
    vi.clearAllMocks();
    globalThis.chrome = { storage: { sync: { get: vi.fn(async key => ({ [key]: { encryptedData: [], iv: [] } })) } } };
    vi.stubGlobal('confirm', () => true);
    mocks.decrypt.mockResolvedValue({ secrets: { '1': { base: 'SyntheticSpice' } } });
    document.body.innerHTML = '<div id="modal-export" class="hidden"><h3 id="export-modal-title"><span class="modal-title-em"></span></h3><button id="btn-close-export"></button><div id="export-step-content"></div><div id="export-modal-footer"></div><span id="exp-step-label"></span></div>';
});
afterEach(() => { vi.unstubAllGlobals(); delete globalThis.chrome; document.body.replaceChildren(); });

function startCooking(name = 'SyntheticProfile') {
    openExportWizard(name, `profile:${name}`, {}, () => {});
    document.getElementById('exp-next-1').click();
    document.getElementById('exp-sharing-pwd').value = 'SyntheticSharing';
    document.getElementById('exp-sharing-pwd-confirm').value = 'SyntheticSharing';
    return document.getElementById('exp-generate').onclick();
}

describe('QR wizard async lifetime', () => {
    it('Back invalidates an in-flight bundle result instead of replacing the chosen step', async () => {
        const cooking = deferred(); mocks.create.mockReturnValue(cooking.promise);
        const pending = startCooking();
        await vi.waitFor(() => expect(mocks.create).toHaveBeenCalledTimes(1));
        document.getElementById('exp-back-2').click();
        cooking.resolve(bundle); await pending;
        expect(document.getElementById('exp-sheet')).not.toBeNull();
        expect(document.getElementById('exp-step-label').textContent).toBe('Step 1 of 2');
        expect(document.getElementById('exp-bundle-output')).toBeNull();
        expect(document.getElementById('exp-qr-panel')).toBeNull();
    });
    it('Back during profile decryption stops cooking for the stale step', async () => {
        const decrypting = deferred(); mocks.decrypt.mockReturnValue(decrypting.promise);
        const pending = startCooking();
        await vi.waitFor(() => expect(mocks.decrypt).toHaveBeenCalledTimes(1));
        document.getElementById('exp-back-2').click();
        decrypting.resolve({ secrets: {} }); await pending;
        expect(mocks.create).not.toHaveBeenCalled();
        expect(document.getElementById('exp-sheet')).not.toBeNull();
    });
    it('closing and opening another profile cannot receive the previous result', async () => {
        const cooking = deferred(); mocks.create.mockReturnValue(cooking.promise);
        const pending = startCooking('FirstSyntheticProfile');
        await vi.waitFor(() => expect(mocks.create).toHaveBeenCalledTimes(1));
        document.getElementById('btn-close-export').click();
        openExportWizard('SecondSyntheticProfile', 'profile:second', {}, () => {});
        cooking.resolve(bundle); await pending;
        expect(document.querySelector('#export-modal-title span').textContent).toBe('SecondSyntheticProfile');
        expect(document.getElementById('exp-sheet')).not.toBeNull();
        expect(document.getElementById('exp-bundle-output')).toBeNull();
    });
});
