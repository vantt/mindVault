import { t } from '../i18n-loader.js';

export function createBuilderSheetContext(el) {
    let mapping = {}, defaultProfile = 'Default', id = null;
    function show(node, key, visible) {
        if (!node) return;
        node.textContent = visible ? t(key) : ''; node.classList.toggle('hidden', !visible);
    }
    function parse(automatic, preserveProfile = false) {
        const raw = el.sheetUrl.value.trim();
        id = raw.match(/\/spreadsheets\/d\/([a-zA-Z0-9_-]+)/)?.[1]
            || (/^[a-zA-Z0-9_-]{20,}$/.test(raw) ? raw : null);
        if (id) el.sheetUrl.value = id;
        show(el.sheetUrlError, 'errInvalidSheetUrl', !!raw && !id);
        show(el.sheetWarning, 'warnNoSheetUrl', !raw);
        show(el.legacyWarning, 'warnLegacyNoTag', !raw);
        show(el.sheetAuto, 'hintSheetUrlAuto', !!id && automatic);
        align(preserveProfile);
    }
    function align(preserveProfile) {
        const names = [...el.profile.options].map(o => o.value);
        const mapped = id && mapping[id];
        el.profile.disabled = !!mapped && names.includes(mapped);
        if (el.profile.disabled) {
            el.profile.value = mapped; show(el.profileHint, 'hintProfileFromMapping', true);
            el.profileHint.classList.remove('warning');
        } else {
            if (!preserveProfile || !names.includes(el.profile.value)) {
                el.profile.value = names.includes(defaultProfile) ? defaultProfile : names[0] || '';
            }
            show(el.profileHint, 'hintProfileUnmapped', !!id);
            el.profileHint?.classList.toggle('warning', !!id);
        }
    }
    function configure(nextMapping, nextDefault) { mapping = nextMapping; defaultProfile = nextDefault; }
    return { parse, configure, sheetId: () => id };
}
