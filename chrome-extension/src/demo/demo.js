// demo.js — i18n localization + close button for the standalone explainer page

/** Replace element text with chrome.i18n message where data-i18n is set. */
function localizeHtml() {
  document.querySelectorAll('[data-i18n]').forEach(el => {
    const msg = chrome.i18n.getMessage(el.dataset.i18n);
    if (msg) el.textContent = msg;
  });

}

document.addEventListener('DOMContentLoaded', () => {
  localizeHtml();

  // Close button: only works if tab was opened programmatically via chrome.tabs.create
  document.getElementById('btn-close-demo')?.addEventListener('click', () => {
    window.close();
  });
});
