
let activePopup = null;

document.addEventListener('click', async (event) => {
    // [DISABLED] Feature disabled in favor of extension icon popup.
    // The in-page popup logic is preserved below for reference but will not execute.
    return;

    // 1. Cleanup existing popup if clicking outside
    if (activePopup && !activePopup.contains(event.target)) {
        activePopup.remove();
        activePopup = null;
    }

    const target = event.target;
    
    // 2. Detect Generic Recipe
    // Logic: If it looks like a recipe, try to generate.
    // Enhanced Regex from PRD: ^([a-zA-Z0-9]+)([#@$%^])(\d)([_!?~]*)(?:_(v[a-zA-Z0-9]+))?$
    // Simple verification check before sending to background
    const text = target.innerText || target.textContent;
    if (!text) return;
    
    const trimmed = text.trim();
    
    // Updated Regex to match the structure defined in RegexParserAdapter logic (hash, modifier, index, optional version)
    // RegexParserAdapter: /^([a-zA-Z0-9]+)([#@$%^])(\d)([_!?~]*)(?:_(v[a-zA-Z0-9]+))?$/
    // We use a looser find version here to extraction.
    const recipeMatch = trimmed.match(/([a-zA-Z0-9]+)([#@$%^])(\d)([_!?~]*)(?:_(v[a-zA-Z0-9]+))?/);
    
    // Check if directly clicked or HOTKEY triggered
    const isHotkey = event.detail && event.detail.isHotkey; 
    
    if (recipeMatch) {
       const exactRecipe = recipeMatch[0];
       
       // If clicked, we might want to be stricter (click ON the recipe), but cell click usually means the whole cell text.
       // Sending extracted recipe prevents "Recipe validation failed" in background.
       generateAndShow(target, exactRecipe);
    }
});

// Listen for Hotkey Message from Background
chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
    if (request.action === "TRIGGER_HOTKEY") {
        const target = document.activeElement;
        console.log("mindVault Hotkey Target:", target);
        
        if (target) {
            let text = "";
            if (target.tagName === 'INPUT' || target.tagName === 'TEXTAREA') {
                text = target.value;
            } else {
                text = target.innerText || target.textContent;
            }
            
            if (text) generateAndShow(target, text.trim());
        }
    } else if (request.action === "GET_CURRENT_CELL_PASSWORD") {
        (async () => {
            let target = document.activeElement;
            let text = "";

            // Google Sheets Strategy:
            // 1. Try generic active element (standard inputs, etc)
            if (target && (target.tagName === 'INPUT' || target.tagName === 'TEXTAREA')) {
                text = target.value;
            } else if (target && target.isContentEditable) {
                text = target.innerText || target.textContent;
            }

            // 2. Specialized Check for Google Sheets (Active Cell Fallback)
            // If we didn't get text, or text implies we are just at the body/grid level
            if (!text || (target && target.tagName === 'BODY') || (target && target.classList.contains('grid-container'))) {
                 if (window.location.hostname === 'docs.google.com') {
                     const formulaBar = document.getElementById('t-formula-bar-input');
                     if (formulaBar) {
                         const val = formulaBar.innerText || formulaBar.textContent; 
                         if (val) {
                             text = val;
                             console.log("mindVault: Extracted from Formula Bar:", text);
                         }
                     }
                 }
            }
            
            text = text ? text.replace(/[\r\n]+/g, '').trim() : "";
            console.log("mindVault: Final Extracted Text for Popup:", text);
            
            if (!text) {
                 sendResponse({ success: false, error: "Empty cell" });
                 return;
            }

            try {
                const response = await chrome.runtime.sendMessage({
                    action: "GENERATE_PASSWORD",
                    text: text
                });
                // Attach the text we tried to use for debugging
                response.extractedText = text;
                sendResponse(response);
            } catch (e) {
                sendResponse({ success: false, error: e.message });
            }
        })();
        return true; // Keep channel open for async response
    }
});

async function generateAndShow(target, text) {
     console.log("mindVault: Generating for", text);
     try {
        const response = await chrome.runtime.sendMessage({
            action: "GENERATE_PASSWORD",
            text: text
        });
        
        if (response && response.success) {
            showPopup(target, response.password, response.settings);
        } else if (response && response.error) {
            console.warn("mindVault:", response.error);
            // Show friendly error toast
            let friendlyMsg = "Generation failed";
            if (response.error.includes("Secret not found")) {
                friendlyMsg = "Secret not found (Check Options)";
            } else if (response.error.includes("Invalid recipe")) {
                friendlyMsg = "Invalid recipe format";
            } else {
                friendlyMsg = response.error;
            }
            showErrorToast(target, friendlyMsg);
        }
    } catch (e) {
        console.error("mindVault: Generation loop error", e);
        if (e.message.includes("Extension context invalidated")) {
             // If we can, show a toast telling user to reload?
             // But we might not be able to interact with DOM if invalid?
             // Actually content script might still be able to manipulate DOM if it's just the connection that died.
             try {
                 showErrorToast(target, "Extension reloaded. Please refresh this page.");
             } catch (domErr) {
                 console.error("Cannot show toast:", domErr);
             }
        }
    }
}


function showPopup(targetElement, password, settings = {}) {
    if (activePopup) {
        activePopup.remove();
        activePopup = null;
    }

    // 1. Create Host Element
    const host = document.createElement('div');
    host.style.position = 'absolute';
    host.style.zIndex = '2147483647'; // Max Z-Index
    host.style.fontFamily = 'sans-serif'; // Reset font
    
    // 2. Attach Shadow DOM
    const shadow = host.attachShadow({ mode: 'open' });

    // 3. Inject Styles
    // We use a link to the web_accessible_resource (need to check manifest if content.css is accessible, usually if in content_scripts it might not be accessible via URL unless in web_accessible_resources). 
    // Wait, manifest V3 content_scripts are not automatically web_accessible.
    // Safe bet: Inline the relevant styles or add content.css to web_accessible_resources.
    // I will inline core styles for reliability in MVP without changing manifest structure too much,
    // OR I can trust that chrome extension context implies access? No, web page can't access extension file unless declared.
    // BUT this script runs in the "Isolated World", so `chrome.runtime.getURL` works, but the `<link>` tag is processed by the browser in the context of the page?
    // Actually, `activePopup.shadowRoot` is still in the extension's isolated world context for JS, but DOM rendering is in page.
    // Let's use INLINE STYLE for simplicity and robustness against CSP.
    
    const style = document.createElement('style');
    style.textContent = `
        .mindvault-popup {
            background: #161b22;
            border: 1px solid #30363d;
            border-radius: 8px;
            padding: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.3);
            color: #f0f6fc;
            font-family: sans-serif;
            min-width: 200px;
            animation: fadeIn 0.15s ease-out;
            box-sizing: border-box;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 8px;
            font-size: 0.85rem;
            color: #8b949e;
        }
        .close-btn {
            cursor: pointer;
            font-size: 1.2em;
            line-height: 0.5;
        }
        .pwd-container {
            display: flex;
            gap: 8px;
        }
        input {
            background: #0d1117;
            border: 1px solid #30363d;
            color: white;
            padding: 6px;
            border-radius: 4px;
            font-family: monospace;
            width: 100%;
            box-sizing: border-box;
        }
        .copy-btn {
            background: #238636;
            color: white;
            border: none;
            border-radius: 4px;
            padding: 0 12px;
            cursor: pointer;
            white-space: nowrap;
        }
        .copy-btn:hover {
            background: #2ea043;
        }
        .copy-btn:focus {
            outline: 2px solid #58a6ff;
        }
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(5px); }
            to { opacity: 1; transform: translateY(0); }
        }
    `;
    shadow.appendChild(style);

    const hintHtml = settings.pepperingHint 
        ? `<div style="font-size:0.75rem; color:#8b949e; margin-top:6px; font-style:italic;">🔑 Don't forget your pepper!</div>` 
        : '';

    const popupContent = document.createElement('div');
    popupContent.className = 'mindvault-popup';
    popupContent.innerHTML = `
        <div class="header">
            <span>mindVault</span>
            <span class="close-btn" tabindex="0">&times;</span>
        </div>
        <div class="pwd-container">
            <input type="text" value="${password}" readonly>
            <button class="copy-btn">Copy</button>
        </div>
        ${hintHtml}
    `;
    shadow.appendChild(popupContent);

    // 4. Smart Positioning
    const rect = targetElement.getBoundingClientRect();
    const popupHeight = 100; // Estimated
    const spaceBelow = window.innerHeight - rect.bottom;
    
    let top, left;
    
    if (spaceBelow > popupHeight) {
        top = window.scrollY + rect.bottom + 5;
    } else {
        // Show above
        top = window.scrollY + rect.top - popupHeight - 5;
    }
    left = window.scrollX + rect.left;

    host.style.top = `${top}px`;
    host.style.left = `${left}px`;

    document.body.appendChild(host);
    activePopup = host;

    // 5. Events & Logic (Scoped to Shadow DOM)
    const closeBtn = shadow.querySelector('.close-btn');
    const copyBtn = shadow.querySelector('.copy-btn');
    const input = shadow.querySelector('input');

    const handleClose = () => {
        host.remove();
        activePopup = null;
    };

    closeBtn.onclick = handleClose;
    closeBtn.onkeydown = (e) => { if (e.key === 'Enter' || e.key === ' ') handleClose(); };

    copyBtn.onclick = () => {
        navigator.clipboard.writeText(password);
        const originalText = copyBtn.textContent;
        copyBtn.textContent = "✓";
        
        // Auto-clear logic
        setTimeout(() => {
            navigator.clipboard.readText().then(text => {
                if (text === password) {
                    navigator.clipboard.writeText("");
                    console.log("mindVault: Clipboard cleared");
                }
            }).catch(() => navigator.clipboard.writeText(""));
        }, 30000);

        setTimeout(() => {
            copyBtn.textContent = originalText;
            setTimeout(() => {
                if(activePopup === host) handleClose();
            }, 500);
        }, 1000);
    };
    
    // Auto-copy convenience (Only if document is focused to avoid errors)
    if (document.hasFocus()) {
        navigator.clipboard.writeText(password).catch(err => {
            // Ignore NotAllowedError (common when triggered from popup/hotkey without focus)
            if (err.name !== 'NotAllowedError') {
                 console.warn("mindVault: Auto-copy warning", err);
            }
        });
    }
    setTimeout(() => navigator.clipboard.writeText(""), 30000);

    // 6. Accessibility / Focus
    copyBtn.focus();
    
    // Global Key listener for this popup to close on ESC
    const escListener = (e) => {
        if (e.key === 'Escape') {
            handleClose();
            document.removeEventListener('keydown', escListener);
        }
    };
    document.addEventListener('keydown', escListener);
}

function showErrorToast(targetElement, message) {
    if (activePopup) {
         activePopup.remove();
         activePopup = null;
    }
    
    const host = document.createElement('div');
    host.style.position = 'absolute';
    host.style.zIndex = '2147483647';
    host.style.fontFamily = 'sans-serif';

    const shadow = host.attachShadow({ mode: 'open' });
    
    const style = document.createElement('style');
    style.textContent = `
        .mindvault-toast {
            background: #da3633;
            color: #f0f6fc;
            padding: 8px 12px;
            border-radius: 6px;
            font-size: 0.85rem;
            box-shadow: 0 4px 12px rgba(0,0,0,0.3);
            border: 1px solid #f85149;
            white-space: nowrap;
            animation: fadeIn 0.15s ease-out;
        }
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(5px); }
            to { opacity: 1; transform: translateY(0); }
        }
    `;
    shadow.appendChild(style);

    const toast = document.createElement('div');
    toast.className = 'mindvault-toast';
    toast.textContent = `⚠️ ${message}`;
    shadow.appendChild(toast);

    // Position
    const rect = targetElement.getBoundingClientRect();
    const spaceBelow = window.innerHeight - rect.bottom;
    let top, left;
    
    if (spaceBelow > 50) {
        top = window.scrollY + rect.bottom + 5;
    } else {
        top = window.scrollY + rect.top - 40;
    }
    left = window.scrollX + rect.left;

    host.style.top = `${top}px`;
    host.style.left = `${left}px`;

    document.body.appendChild(host);
    
    // Auto remove
    setTimeout(() => {
        host.style.transition = 'opacity 0.5s';
        host.style.opacity = '0';
        setTimeout(() => host.remove(), 500);
    }, 3000);
}
