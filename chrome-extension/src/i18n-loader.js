/* i18n-loader.js — custom locale loader that honours the `language` preference
   stored in chrome.storage.sync instead of relying on browser UI language.

   Usage:
     import { loadLocale, t } from '../i18n-loader.js';
     await loadLocale();          // call once at page startup
     t('myKey')                   // returns translated string
     t('myKey', ['sub1', 'sub2']) // with substitutions
*/

/** @type {Record<string, {message: string, placeholders?: Record<string, {content: string}>}>|null} */
let _messages = null;

/** Fetches and caches the messages.json for the stored language preference. */
export async function loadLocale() {
    try {
        const { language = "en" } = await chrome.storage.sync.get("language");
        const url = chrome.runtime.getURL(`_locales/${language}/messages.json`);
        const res = await fetch(url);
        if (res.ok) {
            _messages = await res.json();
            return;
        }
    } catch { /* fall through */ }
    _messages = null; // use chrome.i18n fallback
}

/**
 * Look up a localized string. Falls back to chrome.i18n.getMessage if the
 * custom locale hasn't loaded or doesn't contain the key.
 * @param {string} key
 * @param {string|string[]} [substitutions]
 */
export function t(key, substitutions) {
    const entry = _messages && _messages[key];
    if (entry) {
        let msg = entry.message;
        if (substitutions !== undefined) {
            const subs = Array.isArray(substitutions) ? substitutions : [substitutions];
            const { placeholders } = entry;
            if (placeholders) {
                // Resolve named placeholders: $NAME$ → substitution value
                Object.entries(placeholders).forEach(([name, def]) => {
                    const m = def.content.match(/^\$(\d+)$/);
                    if (m) {
                        const val = subs[parseInt(m[1], 10) - 1];
                        if (val !== undefined) {
                            msg = msg.replace(new RegExp(`\\$${name}\\$`, 'gi'), val);
                        }
                    }
                });
            }
            // Handle positional $1, $2 … (with or without named placeholders)
            subs.forEach((s, i) => {
                msg = msg.replace(`$${i + 1}`, s);
            });
        }
        return msg;
    }
    // Fallback to browser locale via chrome.i18n
    return chrome.i18n.getMessage(key, substitutions) || "";
}
