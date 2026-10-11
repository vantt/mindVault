import { t } from '../i18n-loader.js';

const messages = {
    VAULT_EXISTS: ['securitySetupExists', 'Cookbook already exists. Unlock it in Options.'],
    LOCK_UNAVAILABLE: ['securityLockUnavailable', 'Cookbook lock could not be confirmed. Close this window and try again.'],
    WRONG_PASSWORD: ['securityWrongPassword', 'Invalid Password. $1 attempts remaining.'],
    LOCKED_OUT: ['securityLockedOut', 'Too many attempts. Try again in $1 seconds.'],
    AUTH_BUSY: ['securityBusy', 'Another Cookbook unlock is in progress.'],
    AUTH_CANCELLED: ['securityCancelled', 'Cookbook locked. Unlock again.'],
    INVALID_DATA: ['securityInvalidData', 'Cookbook data is unavailable. Check Options.'],
    INVALID_PASSWORD_INPUT: ['securityPasswordRequired', 'Enter your master password.'],
    CRYPTO_UNAVAILABLE: ['securityCryptoUnavailable', 'Cookbook could not unlock. Please try again.'],
    SECURITY_UNAVAILABLE: ['securityCryptoUnavailable', 'Cookbook could not unlock. Please try again.'],
    CLIPBOARD_UNAVAILABLE: ['securityClipboardUnavailable', 'Copy failed. Your clipboard was kept unchanged.'],
    VAULT_LOCKED: ['securityCancelled', 'Cookbook locked. Unlock again.'],
};
export function securityMessage(result = {}) {
    const [key, fallback] = messages[result.code] || messages.SECURITY_UNAVAILABLE;
    const value = result.code === 'LOCKED_OUT' ? Math.ceil((result.retryAfterMs || 0) / 1000)
        : (result.attemptsRemaining ?? '');
    return t(key, [String(value)]) || fallback.replace('$1', String(value));
}
export async function securityRequest(action, values = {}) {
    try { return await chrome.runtime.sendMessage({ action, ...values }) || { success: false, code: 'SECURITY_UNAVAILABLE' }; }
    catch { return { success: false, code: 'SECURITY_UNAVAILABLE' }; }
}
export function bindUnlock({ input, button, form, showError, clearError, onUnlocked }) {
    let busy = false;
    let countdown;
    async function refreshLockout() {
        const status = await securityRequest('AUTH_STATUS');
        if (status.retryAfterMs > 0) {
            button.disabled = true;
            showError(securityMessage({ ...status, code: 'LOCKED_OUT' }));
            countdown = setTimeout(refreshLockout, 1000);
        } else if (!busy) {
            button.disabled = false;
        }
    }
    async function unlock(event) {
        event?.preventDefault();
        if (busy || !input.value) return;
        busy = true;
        clearTimeout(countdown);
        button.disabled = true;
        clearError();
        const original = button.textContent;
        button.textContent = t('securityUnlocking') || 'Unlocking…';
        const result = await securityRequest('AUTH_UNLOCK', { password: input.value });
        input.value = '';
        busy = false;
        button.textContent = original;
        if (result.success) {
            button.disabled = false;
            await onUnlocked();
        } else {
            showError(securityMessage(result));
            if (result.retryAfterMs > 0) await refreshLockout();
            else button.disabled = false;
        }
    }
    if (form) form.addEventListener('submit', unlock);
    else {
        button.addEventListener('click', unlock);
        input.addEventListener('keydown', event => { if (event.key === 'Enter') unlock(event); });
    }
    refreshLockout();
    window.addEventListener('pagehide', () => clearTimeout(countdown), { once: true });
}
export function bindDishCopy({ button, readDish, showError }) {
    let busy = false;
    button.addEventListener('click', async () => {
        const text = readDish();
        if (busy || !text) return;
        busy = true;
        button.disabled = true;
        const original = button.textContent;
        const result = await securityRequest('COPY_DISH', { text });
        busy = false;
        button.disabled = false;
        if (!result.success) { showError?.(securityMessage(result)); return; }
        button.textContent = t('lblCopied') || 'Copied!';
        setTimeout(() => { button.textContent = original; }, 1500);
    });
}
