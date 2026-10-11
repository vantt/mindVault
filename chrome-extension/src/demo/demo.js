// Static explainer: localize text/alt attributes without interpreting HTML.
import { loadLocale, t } from '../i18n-loader.js';

export function localizeDemo(doc = document, translate = t) {
  doc.querySelectorAll('[data-i18n]').forEach(element => {
    const message = translate(element.dataset.i18n);
    if (message) element.textContent = message;
  });
  doc.querySelectorAll('[data-i18n-alt]').forEach(element => {
    const message = translate(element.dataset.i18nAlt);
    if (message) element.setAttribute('alt', message);
  });
}

// Register close controls even if locale loading fails.
document.addEventListener('DOMContentLoaded', async () => {
  for (const id of ['btn-close-top', 'btn-close-demo']) {
    document.getElementById(id)?.addEventListener('click', () => window.close());
  }
  await loadLocale();
  const { language = 'en' } = await chrome.storage.sync.get('language').catch(() => ({}));
  document.documentElement.lang = language === 'vi' ? 'vi' : 'en';
  localizeDemo();
});
