import path from 'node:path';
import { init, parse } from 'es-module-lexer';
import { JSDOM } from 'jsdom';
import { contract } from './packaging-provenance.mjs';

await init;
export function checkResources(files, manifest) {
    const externalStyles = [];
    function reference(from, value, root = false, style = false) {
        if (value.startsWith('#')) return;
        if (/^[a-z][\w+.-]*:/i.test(value)) {
            if (style && contract.externalStyles[from]?.includes(value)) {
                externalStyles.push({ from, url: value, kind: 'external stylesheet/font data; not executable code' });
                return;
            }
            throw new Error(`Remote/unsupported resource: ${from} -> ${value}`);
        }
        const clean = value.split(/[?#]/)[0];
        if (!clean || clean.includes('\\') || clean.includes('%') || path.posix.isAbsolute(clean)) {
            throw new Error(`Unsafe resource path: ${from} -> ${value}`);
        }
        const target = path.posix.normalize(path.posix.join(root ? '' : path.posix.dirname(from), clean));
        if (target === '..' || target.startsWith('../') || !files[target]) throw new Error(`Missing/escaping resource: ${from} -> ${value}`);
    }
    for (const entry of [manifest.background?.service_worker, manifest.action?.default_popup, manifest.options_page,
        manifest.options_ui?.page, ...Object.values(manifest.icons || {}),
        ...(manifest.content_scripts || []).flatMap(s => [...s.js || [], ...s.css || []])].filter(Boolean)) {
        reference('manifest.json', entry, true);
    }
    reference('manifest.json', `_locales/${manifest.default_locale}/messages.json`, true);
    for (const [name, bytes] of Object.entries(files)) {
        const text = bytes.toString();
        if (name.endsWith('.html')) {
            const document = new JSDOM(text).window.document;
            for (const element of document.querySelectorAll('[src],link[href],a[href]')) {
                const value = element.getAttribute('src') ?? element.getAttribute('href');
                reference(name, value, false, element.matches('link[rel="stylesheet"]'));
            }
            if ([...document.scripts].some(s => !s.src && s.textContent.trim())) throw new Error(`Inline executable script: ${name}`);
        }
        if (name.endsWith('.css')) {
            for (const match of text.matchAll(/url\(\s*['"]?([^)'"\s]+)['"]?\s*\)/g)) reference(name, match[1], false, true);
            for (const match of text.matchAll(/@import\s+['"]([^'"]+)['"]/g)) reference(name, match[1], false, true);
        }
        if (!name.endsWith('.js') || ['wasm/argon2.js', 'vendor/qrcodegen.js'].includes(name)) continue;
        for (const imported of parse(text)[0]) {
            if (imported.d === -2) continue; // import.meta is metadata, not an import path.
            if (!imported.n) throw new Error(`Nonliteral executable import: ${name}`);
            if (!imported.n.startsWith('.')) throw new Error(`Nonlocal executable import: ${name} -> ${imported.n}`);
            reference(name, imported.n);
        }
        for (const match of text.matchAll(/new URL\(([^)\n]*)\)/g)) {
            const local = match[1].match(/^\s*['"]([^'"]+)['"]\s*,\s*import\.meta\.url\s*$/);
            if (!local) throw new Error(`Unreviewed module-relative URL: ${name}`);
            reference(name, local[1]);
        }
        for (const match of text.matchAll(/new Worker\(\s*([^\n]+)/g)) {
            if (!/^new URL\(['"][^'"]+['"], import\.meta\.url\)/.test(match[1])) throw new Error(`Unreviewed Worker resource: ${name}`);
        }
        for (const match of text.matchAll(/chrome\.runtime\.getURL\(([^)\n]*)\)/g)) {
            const expr = match[1].trim();
            if (/^['"][^'"]+['"]$/.test(expr)) reference(name, expr.slice(1, -1), true);
            else if (/^`_locales\/\$\{(?:language|locale)\}\/messages\.json`$/.test(expr)) {
                for (const locale of ['en', 'vi']) reference(name, `_locales/${locale}/messages.json`, true);
            } else if (name === 'security/offscreen-client.js' && expr === 'PATH' && text.includes("const PATH = 'offscreen/offscreen.html';")) {
                reference(name, 'offscreen/offscreen.html', true);
            } else if (name === 'security/background-security.js' && expr === 'path' &&
                text.includes("['popup/popup.html', 'options/options.html'].some(path => sender.url === chrome.runtime.getURL(path))")) {
                for (const page of ['popup/popup.html', 'options/options.html']) reference(name, page, true);
            } else throw new Error(`Unreviewed dynamic extension resource: ${name} -> ${expr}`);
        }
    }
    return externalStyles;
}
