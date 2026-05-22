let activePopup = null;

function getCurrentSheetId() {
    const match = window.location.pathname.match(/\/spreadsheets\/d\/([a-zA-Z0-9_-]+)/);
    return match ? match[1] : null;
}

// Listen for messages from service worker / popup
chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
    if (request.action === "TRIGGER_HOTKEY") {
        const target = document.activeElement;
        const text = extractRecipeText(target);
        if (text) generateAndShow(target, text);
        else console.log("mindVault: No recipe found in active element or formula bar");
    }

    if (request.action === "GET_CURRENT_CELL_PASSWORD") {
        (async () => {
            const target = document.activeElement;
            const text = extractRecipeText(target);
            console.log("mindVault: Final Extracted Text for Popup:", text);

            if (!text) {
                sendResponse({ success: false, error: "Empty cell" });
                return;
            }
            try {
                const response = await chrome.runtime.sendMessage({
                    action: "GENERATE_PASSWORD",
                    text,
                    sheetId: getCurrentSheetId()
                });
                response.extractedText = text;
                sendResponse(response);
            } catch (e) {
                sendResponse({ success: false, error: e.message });
            }
        })();
        return true;
    }
});

function extractRecipeText(target) {
    let text = "";

    if (target && (target.tagName === 'INPUT' || target.tagName === 'TEXTAREA')) {
        text = target.value;
    } else if (target && target.isContentEditable) {
        text = target.innerText || target.textContent;
    }

    if (!text || (target && target.tagName === 'BODY') || (target && target.classList.contains('grid-container'))) {
        if (window.location.hostname === 'docs.google.com') {
            // Try primary selector first, then fallbacks (Google may change the ID)
            let formulaBar = document.getElementById('t-formula-bar-input');

            if (!formulaBar) {
                // Fallback 1: any element whose ID contains "formula-bar"
                formulaBar = document.querySelector('[id*="formula-bar"][contenteditable]');
            }
            if (!formulaBar) {
                // Fallback 2: heuristic — wide contenteditable near the top of page (formula bar area)
                for (const el of document.querySelectorAll('[contenteditable="true"]')) {
                    const rect = el.getBoundingClientRect();
                    if (rect.top > 0 && rect.top < 150 && rect.width > 300) {
                        formulaBar = el;
                        console.log("mindVault: Formula bar found via heuristic:", el.id || el.className);
                        break;
                    }
                }
            }

            if (formulaBar) {
                const val = formulaBar.innerText || formulaBar.textContent;
                if (val) {
                    text = val;
                    console.log("mindVault: Extracted from Formula Bar:", text);
                } else {
                    console.log("mindVault: Formula bar found but empty. id=", formulaBar.id);
                }
            } else {
                console.log("mindVault: Formula bar element not found. Try reloading the tab.");
            }
        }
    }

    return text ? text.replace(/[\r\n]+/g, '').trim() : "";
}

async function generateAndShow(target, text) {
    console.log("mindVault: Generating for", text);
    try {
        const response = await chrome.runtime.sendMessage({
            action: "GENERATE_PASSWORD",
            text,
            sheetId: getCurrentSheetId()
        });

        if (response?.success) {
            showPopup(target, response.password, response.settings);
        } else if (response?.error) {
            let msg = response.error;
            if (response.code === "RECIPE_MISMATCH") {
                msg = chrome.i18n.getMessage("errRecipeMismatch") || "Recipe verification failed — was this recipe built for a different sheet or profile?";
            } else if (msg.includes("not found")) {
                msg = "Secret not found (Check Options)";
            } else if (msg.includes("Invalid recipe")) {
                msg = "Invalid recipe format";
            }
            showErrorToast(target, msg);
        }
    } catch (e) {
        console.error("mindVault: Generation error", e);
        if (e.message?.includes("Extension context invalidated")) {
            try { showErrorToast(target, "Extension reloaded. Please refresh this page."); } catch {}
        }
    }
}

function showPopup(targetElement, password, settings = {}) {
    if (activePopup) { activePopup.remove(); activePopup = null; }

    const host = document.createElement('div');
    host.style.cssText = 'position:absolute;z-index:2147483647;font-family:sans-serif;';
    const shadow = host.attachShadow({ mode: 'open' });

    const style = document.createElement('style');
    style.textContent = `
        .mindvault-popup{background:#161b22;border:1px solid #30363d;border-radius:8px;padding:12px;box-shadow:0 4px 12px rgba(0,0,0,.3);color:#f0f6fc;font-family:sans-serif;min-width:200px;animation:fadeIn .15s ease-out;box-sizing:border-box}
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
    content.className = 'mindvault-popup';
    content.innerHTML = `
        <div class="header"><span>mindVault</span><span class="close-btn" tabindex="0">&times;</span></div>
        <div class="pwd-container">
            <input type="text" id="mv-pwd-input" readonly>
            <button class="copy-btn">Copy</button>
        </div>${hint}`;
    shadow.appendChild(content);
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
        host.remove();
        activePopup = null;
        document.removeEventListener('keydown', escListener);
    };
    closeBtn.onclick = handleClose;
    closeBtn.onkeydown = (e) => { if (e.key === 'Enter' || e.key === ' ') handleClose(); };

    copyBtn.onclick = () => {
        navigator.clipboard.writeText(password);
        copyBtn.textContent = "✓";
        setTimeout(() => { navigator.clipboard.readText().then(t => { if (t === password) navigator.clipboard.writeText(""); }).catch(() => navigator.clipboard.writeText("")); }, 30000);
        setTimeout(() => { copyBtn.textContent = "Copy"; setTimeout(() => { if (activePopup === host) handleClose(); }, 500); }, 1000);
    };

    copyBtn.focus();

    escListener = (e) => { if (e.key === 'Escape') handleClose(); };
    document.addEventListener('keydown', escListener);
}

function showErrorToast(targetElement, message) {
    if (activePopup) { activePopup.remove(); activePopup = null; }

    const host = document.createElement('div');
    host.style.cssText = 'position:absolute;z-index:2147483647;font-family:sans-serif;';
    const shadow = host.attachShadow({ mode: 'open' });

    const style = document.createElement('style');
    style.textContent = `.mindvault-toast{background:#da3633;color:#f0f6fc;padding:8px 12px;border-radius:6px;font-size:.85rem;box-shadow:0 4px 12px rgba(0,0,0,.3);border:1px solid #f85149;white-space:nowrap;animation:fadeIn .15s ease-out}@keyframes fadeIn{from{opacity:0;transform:translateY(5px)}to{opacity:1;transform:translateY(0)}}`;
    shadow.appendChild(style);
    const toast = document.createElement('div');
    toast.className = 'mindvault-toast';
    toast.textContent = `⚠️ ${message}`;
    shadow.appendChild(toast);

    const rect = targetElement.getBoundingClientRect();
    const top = (window.innerHeight - rect.bottom) > 50 ? window.scrollY + rect.bottom + 5 : window.scrollY + rect.top - 40;
    host.style.top = `${top}px`;
    host.style.left = `${window.scrollX + rect.left}px`;
    document.body.appendChild(host);

    setTimeout(() => { host.style.transition = 'opacity .5s'; host.style.opacity = '0'; setTimeout(() => host.remove(), 500); }, 3000);
}
