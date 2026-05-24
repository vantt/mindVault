# 16 — Demo Page (Standalone)

New standalone HTML page explaining how mindVault works. Opened in a new browser tab — not inside the popup.

**File path:** `chrome-extension/src/demo/demo.html`

---

## Entry Points

| Source | Trigger | Code |
|--------|---------|------|
| Quick Start panel "Learn more →" | Click `#btn-learn-more` on Home screen | `chrome.tabs.create({ url: chrome.runtime.getURL('demo/demo.html') })` |
| Builder "? How it works" | Click `#btn-how-it-works` in Builder header | `chrome.tabs.create({ url: chrome.runtime.getURL('demo/demo.html') })` |

The demo page is opened via `chrome.runtime.getURL()` so it runs as an extension page with access to `chrome.i18n.getMessage()`.

---

## manifest.json Declaration

The demo page must be declared as a web-accessible resource so `chrome.runtime.getURL()` resolves correctly:

```json
"web_accessible_resources": [
  {
    "resources": ["demo/demo.html", "demo/demo.css"],
    "matches": ["<all_urls>"]
  }
]
```

Alternatively, if the page is only opened from within the extension (popup), `matches` can be restricted to the extension's own origin. Check existing `web_accessible_resources` entries and append — do not replace.

---

## Layout

```
┌──────────────────────────────────────────────┐  max-width: 700px
│                                              │  dark theme
│  🔐 mindVault                                │  ← h1
│  How mindVault Works                         │  ← subtitle / page description
│  ──────────────────────────────────────────  │
│                                              │
│  ## The Idea                                 │  ← Section 1
│                                              │
│  Your passwords live in your head — not      │
│  in a database. A recipe is a short code     │
│  you store in Google Sheets. mindVault       │
│  reads the cell and computes your password   │
│  on demand.                                  │
│                                              │
│  ──────────────────────────────────────────  │
│                                              │
│  ## Anatomy of a Recipe                      │  ← Section 2
│                                              │
│  A recipe looks like `fb#1` — a hash, a      │
│  position, and a secret index. The extension │
│  combines these with your secret phrase to   │
│  generate a unique password.                 │
│                                              │
│  ┌─ Example ───────────────────────────────┐ │
│  │  fb # 1 _                               │ │
│  │  ^  ^ ^ ^                               │ │
│  │  │  │ │ └─ modifier (optional)          │ │
│  │  │  │ └─── secret index                 │ │
│  │  │  └───── position symbol              │ │
│  │  └──────── hash (your word)             │ │
│  └─────────────────────────────────────────┘ │
│                                              │
│  ──────────────────────────────────────────  │
│                                              │
│  ## Modifiers                                │  ← Section 3
│                                              │
│  Add `_`, `!`, `?`, or `~` to transform how  │
│  secrets are combined. Each modifier changes  │
│  the output in a predictable, reversible way. │
│                                              │
│  ──────────────────────────────────────────  │
│                                              │
│  ## Daily Use                                │  ← Section 4
│                                              │
│  Open a spreadsheet, click a cell with your  │
│  recipe, then open mindVault. Your password  │
│  appears instantly. Nothing is stored —      │
│  close the popup and it's gone.              │
│                                              │
│  ──────────────────────────────────────────  │
│                                              │
│  [Close this tab]                            │  ← closes via window.close() or tab nav back
└──────────────────────────────────────────────┘
```

---

## Styling

| Property | Value |
|---|---|
| Theme | Dark — matches popup CSS vars (`--bg`, `--surface`, `--text`, `--text-secondary`, `--accent`) |
| Max-width | 700px, centered (`margin: 0 auto`) |
| Padding | 2rem side padding on mobile, 3rem on wider screens |
| Font | Same as popup (system font stack or explicitly declared) |
| Code blocks / recipe examples | Monospace, `--surface` background, `--accent` border-left or background highlight |
| Responsive | `max-width: 700px` + `padding: 1.5rem` on narrow viewports |
| Section headings | `<h2>` with bottom border in `--border` color |
| Body text | `--text` color, line-height 1.6 |
| Secondary text | `--text-secondary` color |

**CSS file:** `chrome-extension/src/demo/demo.css` — imports or reuses popup CSS variables. Do NOT copy entire popup.css — import vars only or inline `--var` declarations in `:root`.

---

## Content Sections

### Section 1 — The Idea

**i18n heading key:** `demoSection1Title` → "The Idea"
**i18n body key:** `demoSection1Body` → "Your passwords live in your head — not in a database. A recipe is a short code you store in Google Sheets. mindVault reads the cell and computes your password on demand."

### Section 2 — Anatomy of a Recipe

**i18n heading key:** `demoSection2Title` → "Anatomy of a Recipe"
**i18n body key:** `demoSection2Body` → "A recipe looks like `fb#1` — a hash, a position, and a secret index. The extension combines these with your secret phrase to generate a unique password."

Includes a static annotated example block (not i18n'd — monospace diagram). The diagram labels may use `data-i18n` if multi-language support is needed, but they can be hardcoded for Phase 6 and i18n'd in Phase 7.

### Section 3 — Modifiers

**i18n heading key:** `demoSection3Title` → "Modifiers"
**i18n body key:** `demoSection3Body` → "Add `_`, `!`, `?`, or `~` to transform how secrets are combined. Each modifier changes the output in a predictable, reversible way."

Optional: a modifier reference table showing symbol + effect. Can reuse the `hintModifier*` i18n keys for consistency.

| Symbol | Effect (i18n key) |
|--------|------------------|
| `_` | `hintModifierFlip` |
| `!` | `hintModifierUpper` |
| `?` | `hintModifierReverse` |
| `~` | `hintModifierStrip` |

### Section 4 — Daily Use

**i18n heading key:** `demoSection4Title` → "Daily Use"
**i18n body key:** `demoSection4Body` → "Open a spreadsheet, click a cell with your recipe, then open mindVault. Your password appears instantly. Nothing is stored — close the popup and it's gone."

---

## Close / Navigation

- A `[Close this tab]` button or link at the bottom of the page.
- **i18n key:** `demoBtnClose` → "Close this tab"
- **Behavior:** `window.close()` — works because the tab was opened programmatically by the extension.
- No "back to extension" link is needed (popup is a separate window context).

---

## i18n Implementation

The demo page uses `chrome.i18n.getMessage()` to localize all text. On `DOMContentLoaded`, a `localizeHtml()` function (same pattern as `popup.js`) iterates `data-i18n` attributes and sets `textContent`.

```js
// demo.js
document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('[data-i18n]').forEach(el => {
    const msg = chrome.i18n.getMessage(el.dataset.i18n);
    if (msg) el.textContent = msg;
  });
});
```

**Vietnamese note:** Use "Công thức" for "recipe" — NOT "Công thức nấu ăn" (cooking recipe). The shortened form is correct in tech context.

---

## i18n Keys

| Key | EN Value |
|---|---|
| `demoTitle` | How mindVault Works |
| `demoSection1Title` | The Idea |
| `demoSection1Body` | Your passwords live in your head — not in a database. A recipe is a short code you store in Google Sheets. mindVault reads the cell and computes your password on demand. |
| `demoSection2Title` | Anatomy of a Recipe |
| `demoSection2Body` | A recipe looks like `fb#1` — a hash, a position, and a secret index. The extension combines these with your secret phrase to generate a unique password. |
| `demoSection3Title` | Modifiers |
| `demoSection3Body` | Add `_`, `!`, `?`, or `~` to transform how secrets are combined. Each modifier changes the output in a predictable, reversible way. |
| `demoSection4Title` | Daily Use |
| `demoSection4Body` | Open a spreadsheet, click a cell with your recipe, then open mindVault. Your password appears instantly. Nothing is stored — close the popup and it's gone. |
| `demoBtnClose` | Close this tab |

All keys above are also listed in [15-i18n-keys.md](./15-i18n-keys.md) section A2.

---

## Files to Create

| File | Notes |
|------|-------|
| `chrome-extension/src/demo/demo.html` | Main page HTML |
| `chrome-extension/src/demo/demo.css` | Demo-specific styles + popup CSS var imports |
| `chrome-extension/src/demo/demo.js` | i18n localizeHtml() + close button handler |

---

## Related Screens

- [03-popup-home.md](./03-popup-home.md) — "Learn more →" entry point (Quick Start panel)
- [05-recipe-builder.md](./05-recipe-builder.md) — "? How it works" entry point (Builder header)
- [15-i18n-keys.md](./15-i18n-keys.md) — All i18n keys including demo page keys
