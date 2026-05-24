# Phase 06 — Demo Page (E)

**Status:** completed
**Effort:** ~2h
**Dependency:** Phase 01 complete (spec `16-demo-page.md` exists), Phase 07 in progress (i18n keys available)

---

## Context Links

- `docs/ui-specs/16-demo-page.md` — spec (created in Phase 01)
- `chrome-extension/src/manifest.json` — needs `web_accessible_resources` entry
- `chrome-extension/src/popup/popup.css` — CSS variables to reuse (do NOT import; copy vars into `demo.css`)

---

## Overview

A standalone full-page HTML file that explains the mindVault concept and daily workflow. Opened as a new browser tab from:
1. "Learn more →" on Quick Start panel
2. "? How it works" in Builder header

Not a popup — full browser tab, no 300px constraint.

---

## Requirements

### Functional
- 4 content sections (in order):
  1. **The Idea** — recipes vs passwords concept; visual example showing `r4nd0m#1` + secret → password
  2. **Anatomy of a Recipe** — hash/position/secret breakdown with before/after examples for each of the 5 position symbols
  3. **Modifiers** — the 4 optional modifiers (`_`, `!`, `?`, `~`) with examples
  4. **Daily Use** — 4-step workflow diagram; password rotation tip
- i18n: EN + VI via `chrome.i18n.getMessage()` (same mechanism as popup)
- Link at bottom: "← Back to extension" that calls `window.close()` (only works if the tab was opened programmatically — otherwise no-op; acceptable)
- Skip: verification tags, sheet binding, profile system — too advanced for an intro page

### Non-functional
- Dark theme using same CSS variable names as `popup.css` (`:root` redeclared in `demo.css`)
- Self-contained: no external fonts (use system fonts), no CDN dependencies
- Declared in `manifest.json` (MV3 requires `web_accessible_resources` for pages opened via `getURL`)
- Max width ~700px, centered, readable line length
- Responsive: works at 400px–1200px

---

## File Structure

```
chrome-extension/src/demo/
  demo.html   (NEW)
  demo.css    (NEW)
```

---

## Architecture

### `manifest.json` change

Add `web_accessible_resources`:
```json
"web_accessible_resources": [
  {
    "resources": ["demo/demo.html", "demo/demo.css"],
    "matches": ["<all_urls>"]
  }
]
```

Note: extension pages opened via `chrome.runtime.getURL()` and `chrome.tabs.create()` are actually extension pages (chrome-extension:// scheme), not web pages — they don't strictly require `web_accessible_resources`. However, declaring them is best practice and harmless.

Alternative: omit `web_accessible_resources` entirely — MV3 extension pages are accessible by default from within the extension. Verify during implementation; only add if needed.

### `demo.html` structure

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>mindVault — How it works</title>
  <link rel="stylesheet" href="demo.css">
</head>
<body>
  <div class="demo-container">
    <header class="demo-header">
      <h1>🔐 mindVault</h1>
      <p class="demo-tagline" data-i18n="demoTagline">...</p>
    </header>

    <section id="section-idea" class="demo-section">
      <h2 data-i18n="demoIdeaTitle">The Idea</h2>
      <!-- concept explanation + visual example box -->
    </section>

    <section id="section-anatomy" class="demo-section">
      <h2 data-i18n="demoAnatomyTitle">Anatomy of a Recipe</h2>
      <!-- hash, position (table with all 5 symbols), secret -->
    </section>

    <section id="section-modifiers" class="demo-section">
      <h2 data-i18n="demoModifiersTitle">Modifiers</h2>
      <!-- modifier table: symbol, name, example -->
    </section>

    <section id="section-daily-use" class="demo-section">
      <h2 data-i18n="demoDailyUseTitle">Daily Use</h2>
      <!-- 4-step flow; rotation tip -->
    </section>

    <footer class="demo-footer">
      <button id="btn-close-demo" class="btn-close-demo" data-i18n="demoBackLink">← Back to extension</button>
    </footer>
  </div>

  <script src="demo.js" type="module"></script>
</body>
</html>
```

### `demo.js` (NEW, ~30 lines)

Handles i18n localization using same `localizeHtml()` pattern as `popup.js`:

```js
// chrome-extension/src/demo/demo.js
function localizeHtml() {
  document.querySelectorAll('[data-i18n]').forEach(el => {
    const msg = chrome.i18n.getMessage(el.dataset.i18n);
    if (msg) el.textContent = msg;
  });
  document.querySelectorAll('[data-i18n-html]').forEach(el => {
    const msg = chrome.i18n.getMessage(el.dataset.i18nHtml);
    if (msg) el.innerHTML = msg;
  });
}

document.addEventListener('DOMContentLoaded', () => {
  localizeHtml();
  document.getElementById('btn-close-demo')?.addEventListener('click', () => window.close());
});
```

Note: use `data-i18n-html` for content that needs HTML formatting (e.g. recipe example with `<code>` tags). Use `data-i18n` for plain text.

### `demo.css` key styles

```css
:root {
  /* Redeclare same variables as popup.css */
  --bg: #0d1117;
  --surface: #161b22;
  --surface2: #1c2128;
  --border: #30363d;
  --text: #e6edf3;
  --text-muted: #7d8590;
  --accent: #58a6ff;
  --green: #3fb950;
  --amber: #d29922;
}

body { background: var(--bg); color: var(--text); font-family: -apple-system, sans-serif;
       font-size: 15px; line-height: 1.7; margin: 0; }

.demo-container { max-width: 700px; margin: 0 auto; padding: 32px 20px 60px; }

.demo-header { text-align: center; padding: 24px 0 32px; border-bottom: 1px solid var(--border); }
.demo-header h1 { font-size: 1.6rem; margin-bottom: 6px; }

.demo-section { padding: 28px 0; border-bottom: 1px solid var(--border); }
.demo-section h2 { font-size: 1.1rem; color: var(--accent); margin-bottom: 14px; }

/* Recipe example box */
.recipe-example {
  background: var(--surface2);
  border: 1px solid var(--border);
  border-radius: 8px;
  padding: 14px 16px;
  font-family: 'Fira Code', monospace;
  font-size: 0.9rem;
  margin: 12px 0;
}

/* Position / modifier table */
.demo-table { width: 100%; border-collapse: collapse; font-size: 0.85rem; margin: 10px 0; }
.demo-table th { text-align: left; color: var(--text-muted); font-weight: 500;
                 padding: 6px 10px; border-bottom: 1px solid var(--border); }
.demo-table td { padding: 6px 10px; border-bottom: 1px solid rgba(48,54,61,.5); }
.demo-table code { color: var(--accent); font-family: 'Fira Code', monospace; }

.demo-footer { text-align: center; padding-top: 32px; }
.btn-close-demo { background: none; border: 1px solid var(--border); color: var(--text-muted);
                  padding: 8px 16px; border-radius: 8px; cursor: pointer; font-size: 0.85rem; }
.btn-close-demo:hover { color: var(--text); border-color: var(--border-hover); }
```

---

## Section Content Outline

### 1. The Idea
- Problem: storing passwords in Sheets = leaving keys under mat
- Solution: store recipes, not passwords
- Visual box:
  ```
  Recipe (public):  r4nd0m#1
  Your secret (#1): Basic*
  ─────────────────────────
  Password:         Basic*r4nd0m
  ```
- Key insight: sheet can be public; password never stored

### 2. Anatomy of a Recipe
- Three parts: **Hash** (the text you choose) + **Position symbol** (where secret goes) + **Secret number** (which secret to use)
- Table of all 5 positions with example:

| Symbol | Meaning | Example (hash=`abc`, secret=`XYZ`) |
|--------|---------|-------------------------------------|
| `#` | Prefix | `XYZabc` |
| `$` | Suffix | `abcXYZ` |
| `@` | Middle | `aXYZbc` |
| `%` | Interleave 1-by-1 | `aXbYcZ` |
| `^` | Interleave 2-by-2 | `abXYcZ` |

### 3. Modifiers
Table: Symbol / What it does / Example

| Symbol | Effect | Example |
|--------|--------|---------|
| `_` | Flip `#`↔`$` | `#` → acts like `$` and vice versa |
| `!` | Uppercase secret | `XYZ` → `XYZ` (if already uppercase) |
| `?` | Reverse secret | `XYZ` → `ZYX` |
| `~` | Strip non-alphanumeric from secret | `X!Y@Z` → `XYZ` |

### 4. Daily Use
4 steps:
1. Open your Google Sheet
2. Click a cell containing a recipe
3. Open mindVault (or press Ctrl+Shift+L)
4. Click Copy → paste into the login field

Rotation tip: "Changed your password? Just update the recipe hash (e.g. `fb2`) — same secrets, new password."

---

## i18n Keys Needed

All keys prefixed `demo*`. Minimum set:

| Key | EN value |
|-----|---------|
| `demoTagline` | "Turn recipes into passwords" |
| `demoIdeaTitle` | "The Idea" |
| `demoAnatomyTitle` | "Anatomy of a Recipe" |
| `demoModifiersTitle` | "Modifiers" |
| `demoDailyUseTitle` | "Daily Use" |
| `demoBackLink` | "← Back to extension" |

Remaining section content: use static HTML strings for EN baseline. Add `data-i18n` attributes for Phase 07 to wire Vietnamese. Heavy content (table cells, multi-line paragraphs) may use `data-i18n-html` for HTML-formatted translations.

---

## Related Code Files

### Create
- `chrome-extension/src/demo/demo.html`
- `chrome-extension/src/demo/demo.css`
- `chrome-extension/src/demo/demo.js`

### Modify
- `chrome-extension/src/manifest.json` — add `web_accessible_resources` (verify if needed)

---

## Implementation Steps

1. Create `chrome-extension/src/demo/` directory
2. Write `demo.html` with all 4 sections; use `data-i18n` on headings and key strings; use inline HTML for example content (fallback English)
3. Write `demo.css` (redeclare CSS vars, layout, table styles)
4. Write `demo.js` (~30 lines: `localizeHtml()` + close button)
5. Test: open demo from popup "Learn more →" link (Phase 05 wired) — page renders in new tab
6. Update `manifest.json` only if `chrome.tabs.create` with `getURL` fails without declaration

---

## Todo List

- [x] Create `chrome-extension/src/demo/` directory
- [x] Write `demo.html` — 4 sections, i18n attributes, example content
- [x] Write `demo.css` — dark theme, max-width layout, table styles
- [x] Write `demo.js` — localizeHtml + close button
- [x] Update `manifest.json` if needed — not needed (MV3 extension pages accessible by default via chrome-extension:// scheme)
- [ ] Verify demo page opens from both entry points (Quick Start + Builder)
- [ ] Verify EN content renders correctly
- [ ] Verify "← Back to extension" button closes the tab

---

## Success Criteria

- Demo page opens in new tab from both popup entry points
- All 4 sections render with correct content and styling
- Dark theme matches extension aesthetic
- "← Back to extension" closes the tab
- No external dependencies (no CDN, no external fonts)
- Page readable at 400px–1200px viewport width

---

## Unresolved Questions

- Does MV3 require `web_accessible_resources` for extension pages opened via `chrome.tabs.create({ url: getURL(...) })`? → Verify during implementation. If not required, omit the manifest change (YAGNI).
