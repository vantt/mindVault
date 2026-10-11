let presentationEpoch = 0;
function getCurrentSheetId() {
    const match = window.location.pathname.match(/\/spreadsheets\/d\/([a-zA-Z0-9_-]+)/);
    return match ? match[1] : null;
}

// Listen for messages from service worker / popup
chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
    if (request.action === "VAULT_LOCKED") {
        presentationEpoch++;
        PassChefContentPresentation.close();
        return;
    }

    if (request.action === "TRIGGER_HOTKEY") {
        const target = document.activeElement;
        const text = extractRecipeText(target);
        if (text) generateAndShow(target, text);
    }

    if (request.action === "GET_CURRENT_CELL_PASSWORD") {
        (async () => {
            const target = document.activeElement;
            const text = extractRecipeText(target);

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
                sendResponse(response);
            } catch (e) {
                sendResponse({ success: false, error: "Cookbook unavailable", code: "GENERIC" });
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

    text = (text || "").replace(/[\r\n]+/g, "").trim();
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
                        break;
                    }
                }
            }

            if (formulaBar) {
                const val = (formulaBar.innerText || formulaBar.textContent || "").replace(/[\r\n]+/g, "").trim();
                if (val) {
                    text = val;
                }
            }
        }
    }

    return text ? text.replace(/[\r\n]+/g, '').trim() : "";
}

async function generateAndShow(target, text) {
    const epoch = presentationEpoch;
    try {
        const response = await chrome.runtime.sendMessage({
            action: "GENERATE_PASSWORD",
            text,
            sheetId: getCurrentSheetId()
        });

        if (response?.success) {
            await PassChefContentUI.ready;
            if (epoch !== presentationEpoch) return;
            PassChefContentPresentation.showPopup(target, response.password, response.settings, response.warning);
        } else if (response?.error) {
            await PassChefContentUI.ready;
            if (epoch !== presentationEpoch) return;
            PassChefContentPresentation.showErrorToast(target, PassChefContentUI.error(response));
        }
    } catch (e) {
        if (e.message?.includes("Extension context invalidated")) {
            try { await PassChefContentUI.ready; PassChefContentPresentation.showErrorToast(target, PassChefContentUI.text("hintNeedsReload", "Reload the tab to activate PassChef.")); } catch {}
        }
    }
}
