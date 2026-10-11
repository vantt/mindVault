(() => {
let activePopup = null;
function showPopup(targetElement, password, settings = {}, warning) {
    if (activePopup) { activePopup.__close?.(); activePopup.remove(); activePopup = null; }

    const host = document.createElement('div');
    host.style.cssText = 'position:absolute;z-index:2147483647;font-family:sans-serif;';
    const shadow = host.attachShadow({ mode: 'open' });

    const style = document.createElement('style');
    style.textContent = `
        .passchef-popup{background:#161b22;border:1px solid #30363d;border-radius:8px;padding:12px;box-shadow:0 4px 12px rgba(0,0,0,.3);color:#f0f6fc;font-family:sans-serif;min-width:200px;animation:fadeIn .15s ease-out;box-sizing:border-box}
        .header{display:flex;justify-content:space-between;align-items:center;margin-bottom:8px;font-size:.85rem;color:#8b949e}
        .close-btn{cursor:pointer;font-size:1.2em;line-height:.5}
        .pwd-container{display:flex;gap:8px}
        input{background:#0d1117;border:1px solid #30363d;color:white;padding:6px;border-radius:4px;font-family:monospace;width:100%;box-sizing:border-box}
        .copy-btn{background:#238636;color:white;border:none;border-radius:4px;padding:0 12px;cursor:pointer;white-space:nowrap}
        .copy-btn:hover{background:#2ea043}
        @keyframes fadeIn{from{opacity:0;transform:translateY(5px)}to{opacity:1;transform:translateY(0)}}
    `;
    shadow.appendChild(style);

    const hint = settings?.pepperingHint
        ? `<div style="font-size:.75rem;color:#8b949e;margin-top:6px;font-style:italic;">🔑 Don't forget your pepper!</div>` : '';

    const content = document.createElement('div');
    content.className = 'passchef-popup';
    content.innerHTML = `
        <div class="header"><span>PassChef</span><span class="close-btn" tabindex="0">&times;</span></div>
        <div class="pwd-container">
            <input type="text" id="mv-pwd-input" readonly>
            <button class="copy-btn">${PassChefContentUI.text('btnCopy', 'Copy')}</button>
        </div>${hint}<div class="legacy-warning"></div>`;
    shadow.appendChild(content);
    if (warning === "legacy_no_tag") shadow.querySelector(".legacy-warning").textContent = PassChefContentUI.text("warnLegacyNoTag", "This recipe has no verification tag. Rebuild it to check the Menu and profile.");
    // Set value via property to avoid XSS from password content
    shadow.getElementById('mv-pwd-input').value = password;

    const rect = targetElement.getBoundingClientRect();
    const spaceBelow = window.innerHeight - rect.bottom;
    const top = spaceBelow > 100 ? window.scrollY + rect.bottom + 5 : window.scrollY + rect.top - 105;
    host.style.top = `${top}px`;
    host.style.left = `${window.scrollX + rect.left}px`;
    document.body.appendChild(host);
    activePopup = host;

    const closeBtn = shadow.querySelector('.close-btn');
    const copyBtn = shadow.querySelector('.copy-btn');
    let escListener;
    const handleClose = () => {
        password = "";
        shadow.getElementById("mv-pwd-input").value = "";
        host.remove();
        activePopup = null;
        document.removeEventListener('keydown', escListener);
    };
    host.__close = handleClose;
    closeBtn.onclick = handleClose;
    closeBtn.onkeydown = (e) => { if (e.key === 'Enter' || e.key === ' ') handleClose(); };

    copyBtn.onclick = async () => {
        if (!password || copyBtn.disabled) return;
        copyBtn.disabled = true;
        await PassChefContentUI.ready;
        try {
            const result = await chrome.runtime.sendMessage({ action: 'COPY_DISH', text: password });
            if (!result?.success) {
                copyBtn.textContent = PassChefContentUI.text('securityClipboardUnavailable', 'Copy failed.');
                return;
            }
            copyBtn.textContent = '✓';
            setTimeout(() => { if (activePopup === host) handleClose(); }, 1500);
        } catch {
            copyBtn.textContent = PassChefContentUI.text('securityClipboardUnavailable', 'Copy failed.');
        } finally { copyBtn.disabled = false; }
    };

    copyBtn.focus();

    escListener = (e) => { if (e.key === 'Escape') handleClose(); };
    document.addEventListener('keydown', escListener);
}

function showErrorToast(targetElement, message) {
    if (activePopup) { activePopup.__close?.(); activePopup.remove(); activePopup = null; }

    const host = document.createElement('div');
    host.style.cssText = 'position:absolute;z-index:2147483647;font-family:sans-serif;';
    const shadow = host.attachShadow({ mode: 'open' });

    const style = document.createElement('style');
    style.textContent = `.passchef-toast{background:#da3633;color:#f0f6fc;padding:8px 12px;border-radius:6px;font-size:.85rem;box-shadow:0 4px 12px rgba(0,0,0,.3);border:1px solid #f85149;white-space:nowrap;animation:fadeIn .15s ease-out}@keyframes fadeIn{from{opacity:0;transform:translateY(5px)}to{opacity:1;transform:translateY(0)}}`;
    shadow.appendChild(style);
    const toast = document.createElement('div');
    toast.className = 'passchef-toast';
    toast.textContent = `⚠️ ${message}`;
    shadow.appendChild(toast);

    const rect = targetElement.getBoundingClientRect();
    const top = (window.innerHeight - rect.bottom) > 50 ? window.scrollY + rect.bottom + 5 : window.scrollY + rect.top - 40;
    host.style.top = `${top}px`;
    host.style.left = `${window.scrollX + rect.left}px`;
    document.body.appendChild(host);

    setTimeout(() => { host.style.transition = 'opacity .5s'; host.style.opacity = '0'; setTimeout(() => host.remove(), 500); }, 3000);
}

globalThis.PassChefContentPresentation = { showPopup, showErrorToast, close: () => { activePopup?.__close?.(); } };
})();
