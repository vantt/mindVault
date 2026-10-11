const transfer = document.getElementById('clipboard');
function select(text) {
    transfer.value = text;
    transfer.focus();
    transfer.select();
}
export async function writeClipboard(text) {
    try {
        select(text);
        // Explicit empty text is necessary: copying an empty selection keeps the old clipboard.
        const transferText = event => { event.clipboardData.setData('text/plain', text); event.preventDefault(); };
        document.addEventListener('copy', transferText, { once: true });
        try { if (!document.execCommand('copy')) throw new Error('Clipboard unavailable'); }
        finally { document.removeEventListener('copy', transferText); }
    } finally { transfer.value = ''; }
}
export async function readClipboard() {
    try {
        select('');
        if (!document.execCommand('paste')) throw new Error('Clipboard read denied');
        return transfer.value;
    } finally { transfer.value = ''; }
}
