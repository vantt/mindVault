import { t } from '../i18n-loader.js';

export function initSheetDetector({ inputEl, errorEl, onDetected, onStart = () => {}, onSettled = () => {}, isActive = () => true }) {
    let epoch = 0;
    const refresh = document.createElement('button');
    refresh.type = 'button'; refresh.id = 'bld-detect-refresh'; refresh.className = 'btn-detect-refresh';
    refresh.title = t('btnDetectRefreshTitle'); refresh.textContent = '↻';
    const wrapper = document.createElement('div'); wrapper.className = 'input-with-refresh';
    inputEl.before(wrapper); wrapper.append(inputEl, refresh);
    const picker = document.createElement('div'); picker.id = 'bld-tabs-picker';
    picker.className = 'tabs-picker hidden'; errorEl.after(picker);
    const current = token => token === epoch && isActive();
    function hide() { picker.classList.add('hidden'); picker.replaceChildren(); }
    function loading(value) {
        refresh.disabled = value; refresh.classList.toggle('loading', value); refresh.textContent = value ? '…' : '↻';
    }
    function invalidate() { epoch++; hide(); loading(false); }
    async function runDetection() {
        if (!isActive()) return;
        onStart(); const token = ++epoch; hide(); loading(true);
        try {
            const result = await chrome.runtime.sendMessage({ action: 'GET_SHEET_ID_FROM_ACTIVE_TAB' });
            if (!current(token)) return;
            const tabs = result?.active ? [result.active] : result?.otherTabs || [];
            if (tabs.length === 1) { onDetected(tabs[0].tabUrl); return; }
            if (!tabs.length) return;
            const label = document.createElement('span'); label.className = 'tabs-picker-label';
            label.textContent = t('detectedNTabs', [String(tabs.length)]); picker.append(label);
            for (const tab of tabs) {
                const button = document.createElement('button'); button.type = 'button';
                button.className = 'tabs-picker-item'; button.title = tab.tabUrl;
                button.textContent = (tab.title || tab.tabUrl).replace(/ [-–] Google Sheets$/i, '');
                button.addEventListener('click', () => { if (current(token)) { hide(); onDetected(tab.tabUrl); } });
                picker.append(button);
            }
            picker.classList.remove('hidden');
        } catch { /* Optional detection cannot replace the manually selected Menu. */ }
        finally { if (current(token)) { loading(false); onSettled(); } }
    }
    refresh.addEventListener('click', runDetection);
    return { runDetection, invalidate };
}
