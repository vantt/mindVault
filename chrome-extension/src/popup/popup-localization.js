import { t } from "../i18n-loader.js";

export function localizeHtml() {
    document.querySelectorAll('[data-i18n]').forEach(el => {
        const msg = t(el.dataset.i18n);
        if (msg) el.textContent = msg;
    });
    document.querySelectorAll('[data-i18n-placeholder]').forEach(el => {
        const msg = t(el.dataset.i18nPlaceholder);
        if (msg) el.placeholder = msg;
    });
    // data-i18n-title="<key>" → store i18n message in `data-tooltip-html` + wire up custom rich tooltip
    document.querySelectorAll('[data-i18n-title]').forEach(el => {
        const msg = t(el.dataset.i18nTitle);
        if (msg) {
            el.dataset.tooltipHtml = msg;
            attachTooltip(el);
        }
    });
}

// ── Custom HTML tooltip (supports color spans inside hint text) ─────────────
let _tooltipEl = null;
function attachTooltip(el) {
    el.addEventListener('mouseenter', _showTooltip);
    el.addEventListener('mouseleave', _hideTooltip);
}
function _showTooltip(e) {
    if (!_tooltipEl) {
        _tooltipEl = document.createElement('div');
        _tooltipEl.className = 'tooltip-popup';
        document.body.appendChild(_tooltipEl);
    }
    _tooltipEl.innerHTML = e.currentTarget.dataset.tooltipHtml;
    // Position below the target; clamp to viewport horizontally
    const rect = e.currentTarget.getBoundingClientRect();
    _tooltipEl.style.left = '0px';
    _tooltipEl.style.top = (rect.bottom + 6) + 'px';
    _tooltipEl.classList.add('visible');
    // Adjust horizontal: keep within popup width (300px)
    const tipRect = _tooltipEl.getBoundingClientRect();
    const popupWidth = document.body.clientWidth;
    let left = rect.left;
    if (left + tipRect.width > popupWidth - 4) left = popupWidth - tipRect.width - 4;
    if (left < 4) left = 4;
    _tooltipEl.style.left = left + 'px';
}
function _hideTooltip() {
    if (_tooltipEl) _tooltipEl.classList.remove('visible');
}
