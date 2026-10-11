import { bindUnlock, bindDishCopy, securityRequest, securityMessage } from '../security/page-security.js';

export function initPopupSecurity({ showSection, builder, passwordInput }) {
    const error = document.getElementById('unlock-error');
    bindUnlock({
        input: document.getElementById('unlock-password'), button: document.getElementById('btn-unlock'),
        showError: message => { error.textContent = message; error.style.display = 'block'; },
        clearError: () => { error.style.display = 'none'; }, onUnlocked: () => location.reload(),
    });
    for (const id of ['btn-lock', 'btn-lock-gen']) {
        document.getElementById(id)?.addEventListener('click', async () => {
            builder.close?.(); passwordInput.value = ''; showSection('status-locked');
            const result = await securityRequest('AUTH_LOCK');
            if (result.success) window.close();
            else { error.textContent = securityMessage({ code: 'LOCK_UNAVAILABLE' }); error.style.display = 'block'; }
        });
    }
    bindDishCopy({ button: document.getElementById('btn-copy'), readDish: () => passwordInput.value,
        showError: message => { const hint = document.getElementById('gen-hint'); hint.textContent = message; } });
    window.addEventListener("pagehide", () => builder.close?.(), { once: true });
    chrome.storage.onChanged.addListener((changes, area) => {
        if (area !== 'session' || !changes.sessionKey || changes.sessionKey.newValue) return;
        if (builder.close) builder.close();
        else builder.reset();
        passwordInput.value = '';
        showSection('status-locked');
        error.textContent = securityMessage({ code: 'VAULT_LOCKED' });
        error.style.display = 'block';
    });
}
