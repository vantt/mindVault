
// Minimal Content Script for verification
document.addEventListener('click', (event) => {
    const target = event.target;
    // Check if it might be a cell (Google Sheets structure is complex, but let's try generic)
    // For MVP Manual Verification: "Trigger Hotkey -> Adapter reads DOM".
    // I'll assume we can just click for now, or add a hotkey listener.
    
    // For now, let's just log and try to send any text that looks like a formula
    // Parsing is done in background, but we can do a quick check to avoid spamming
    // Regex: starts with alphanumeric, contains #@$%^
    
    const text = target.innerText || target.textContent;
    if (text && /[a-zA-Z0-9]+[#@$%^]\d/.test(text)) {
        console.log("mindVault: Detected potential formula:", text);
        
        chrome.runtime.sendMessage({
            action: "GENERATE_PASSWORD",
            text: text.trim()
        }, (response) => {
            if (chrome.runtime.lastError) {
                console.error("mindVault Error:", chrome.runtime.lastError);
                return;
            }
            
            if (response && response.success) {
                console.log("mindVault: Password Generated!", response.password);
                // In a real UI, show popup. Here just log or alert.
                // alert(`Password: ${response.password}`);
                
                // Copy to clipboard
                navigator.clipboard.writeText(response.password).then(() => {
                    console.log("mindVault: Copied to clipboard");
                }).catch(err => {
                    console.error("mindVault: Failed to copy", err);
                });
            } else {
                console.error("mindVault: Generation failed", response?.error);
            }
        });
    }
});
