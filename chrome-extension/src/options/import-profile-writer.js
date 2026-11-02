import { encryptWithKey } from '../adapters/infrastructure/aes-storage-crypto-helper.js';

// Keep existing wizard-state reads at their async boundaries; lifetime guards are separate.
export async function writeImportedProfile(readState, localName) {
    const initial = readState();
    const isFullAccess = initial.bundle?.type === 'passchef-fullaccess-share';
    const payload = isFullAccess ? { secrets: initial.derivedSecrets, settings: {} } : initial.derivedSecrets;
    const encrypted = await encryptWithKey(payload, initial.sessionKey);

    if (isFullAccess) {
        const updates = { [`profile:${localName}`]: encrypted };
        if (readState().bundle.sheetId) {
            const existing = await chrome.storage.sync.get('sheetMapping');
            const sheetMapping = existing.sheetMapping || {};
            sheetMapping[readState().bundle.sheetId] = localName;
            updates.sheetMapping = sheetMapping;
        }
        await chrome.storage.sync.set(updates);
    } else {
        const sharedData = { secrets: readState().derivedSecrets, isShared: true };
        const encryptedShared = await encryptWithKey(sharedData, readState().sessionKey);
        await chrome.storage.sync.set({
            [`shared:${localName}`]: {
                ...encryptedShared,
                readOnly: true,
                sheetId: readState().bundle.sheetId,
                meta: {
                    importedFrom: readState().bundle.profileName,
                    importedAt: new Date().toISOString().slice(0, 10),
                    bundleId: readState().bundle.bundleId || '',
                },
            },
        });
    }
}
