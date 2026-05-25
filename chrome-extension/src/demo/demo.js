// demo.js — i18n localization + close button for the standalone explainer page

import { loadLocale, t } from '../i18n-loader.js';

/** Replace element text with localized message where data-i18n is set. */
function localizeHtml() {
  document.querySelectorAll('[data-i18n]').forEach(el => {
    const msg = t(el.dataset.i18n);
    if (msg) el.textContent = msg;
  });
}

document.addEventListener('DOMContentLoaded', async () => {
  await loadLocale();
  localizeHtml();

  // Close button: only works if tab was opened programmatically via chrome.tabs.create
  document.getElementById('btn-close-demo')?.addEventListener('click', () => {
    window.close();
  });
});
