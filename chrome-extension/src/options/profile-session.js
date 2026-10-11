import { securityRequest } from '../security/page-security.js';

export async function readProfileSession() {
    const status = await securityRequest('AUTH_STATUS');
    const stored = await chrome.storage.session.get(['sessionKey', 'sessionNonce']);
    if (!status.unlocked || !status.nonce || stored.sessionNonce !== status.nonce || !stored.sessionKey) return null;
    return { nonce: status.nonce, key: stored.sessionKey };
}
export async function ownsProfileSession(owner) {
    const status = await securityRequest('AUTH_STATUS');
    return !!owner && status.unlocked && status.nonce === owner.nonce;
}
export function commitProfile(owner, profileKey, record, createOnly = false) {
    return securityRequest('AUTH_SAVE_PROFILE', { nonce: owner.nonce, profileKey, record, createOnly });
}
