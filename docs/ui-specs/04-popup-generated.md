# 04 — Generated Screen (Popup)

Reverse-engineered từ [`popup.html`](../../chrome-extension/src/popup/popup.html) (`#status-generated`) và [`popup.js`](../../chrome-extension/src/popup/popup.js) lines 121–161, 224–238.

**Triggered bởi:** Auto-route từ [03-popup-home.md](./03-popup-home.md) khi popup mở trên Google Sheets tab và content script trả về password cho cell đang chọn. Cũng là destination khi error xảy ra trong flow `GET_CURRENT_CELL_PASSWORD`.

**Precondition:** Setup done + unlocked + active tab matches `docs.google.com/spreadsheets`.

---

## Layout

```
┌──────────────────────────────────────────┐
│ 🔐 mindVault                🟢 Ready ⚙️ │
│ ─────────────────────────────────────── │
│                                          │
│  Profile: Default              ← hidden if no profileName
│                                          │
│  ┌───────────────────────┐  [Copy]      │
│  │ XyZ4kP9mNqRsTu        │              │
│  └───────────────────────┘              │
│  🔑 Don't forget your pepper!           │  ← optional hint
│                                          │
│  [     Build Recipe     ]                │
│                                          │
│  [Back]                       [Lock]    │
└──────────────────────────────────────────┘
```

---

## DOM Elements

| ID / Selector | Type | Notes |
|--------------|------|-------|
| `#status-generated` | `<div>` | Section container |
| `.recipe-meta` (inside `#status-generated`) | `<div>` | Wrapper for profile label |
| `#gen-profile-label` `.profile-label` | `<span>` | `hidden` class by default; shown when `response.profileName` present |
| `.pwd-container` | `<div>` | Flex row wrapping `#gen-password` + `#btn-copy` |
| `#gen-password` | `<input type="text" readonly>` | Generated password output |
| `#btn-copy` | `<button class="btn primary">` | No `data-i18n`; hardcoded "Copy" |
| `#gen-hint` | `<p class="hint">` | Status/error hint text; empty by default |
| `#btn-build-recipe-gen` | `<button class="btn primary">` | `data-i18n="btnBuildRecipe"` |
| `.actions-row` (inside `#status-generated`) | `<div>` | Flex row wrapping Back + Lock |
| `#btn-back` | `<button class="btn secondary">` | No `data-i18n`; hardcoded "Back" |
| `#btn-lock-gen` | `<button class="btn secondary">` | No `data-i18n`; hardcoded "Lock" |

---

## Profile Indicator

`#gen-profile-label` content format (set entirely in JS, no i18n):

```js
const prefix = response.isShared ? '📥 ' : '';
genProfileLabel.textContent = `Profile: ${prefix}${response.profileName}`;
```

- Own profile: `Profile: Default`
- Shared profile: `Profile: 📥 TeamFromB`
- Hidden (`class="hidden"`) when `response.profileName` is absent

**No "(fallback)" suffix exists in this screen.** The `profileFallback` i18n key (`"Using default profile"`) is defined but not used here. ~~Spec previously claimed `Profile: Default (fallback)` — this was wrong.~~

Styling: class `profile-label`, font-size 0.85rem.

---

## Password Display & Copy

- `#gen-password` is `readonly` — user không thể edit
- Empty string (`""`) set on all error paths
- Click `[Copy]`:
  1. Guard: `if (!password) return` (no-op khi rỗng)
  2. `navigator.clipboard.writeText(password)`
  3. Button label → `"Copied!"`, after 1500ms → `"Copy"` (setTimeout)
- Both "Copy" / "Copied!" labels are **hardcoded English** (no i18n)

---

## Hint Text States

`genHint.textContent` và `genHint.style.color` được set tùy theo response:

| Condition | Hint text | `style.color` |
|-----------|-----------|---------------|
| Success + `response.settings?.pepperingHint` truthy | `"🔑 Don't forget your pepper!"` | `""` (cleared to default) |
| Success + no pepper hint | `""` (empty, unchanged) | — |
| `response.error === "Empty cell"` | `"No recipe found — select a cell with a recipe, then re-open."` | `"#da3633"` |
| Other `response.error` | `` `Error: ${response.error}` `` + optional `` `("${response.extractedText}")` `` suffix if `extractedText` present | `"#da3633"` |
| `catch (e)` + message includes `"Extension context invalidated"` / `"Could not establish connection"` / `"Receiving end does not exist"` | `"⚠️ Reload the tab to activate the extension."` | `"#da3633"` |
| `catch (e)` other JS exception | `` `⚠️ ${e.message}` `` | `"#da3633"` |

Note: error color is literal `"#da3633"`, **not** CSS variable `--danger`.

**`response.warning` field** returned by service worker is present in the response object but **not displayed** in this screen — no handler in popup.js.

---

## Actions

| Button | Handler | Behavior |
|--------|---------|----------|
| `[Copy]` | Inline click | Guard empty → clipboard write → flash "Copied!" 1500ms |
| `[Build Recipe]` | `openBuilder` (shared with `#btn-build-recipe` on Home) | `builder.reset()` → `builder.loadProfiles()` → `showSection('status-builder')` |
| `[Back]` | Inline click | `showSection('status-unlocked')` — no state cleanup |
| `[Lock]` | Inline click | `chrome.storage.session.remove("sessionKey")` → `window.close()` |

`#btn-lock-gen` và `#btn-lock` (Home screen) share identical handler logic (both wired separately, not the same function reference).

---

## Status Pill

SECTION_STATUS config for `'status-generated'`:

```js
{ i18nKey: 'statusReady', dot: 'green', active: true }
```

- Green dot, label = i18n `statusReady` ("Ready" / "Sẵn sàng")
- `globalStatusEl.classList` gets `active` class (pill highlighted)

---

## i18n Keys Used

| Key | EN | VI | Usage |
|-----|----|----|-------|
| `statusReady` | "Ready" | "Sẵn sàng" | Header status pill |
| `btnBuildRecipe` | "Build Recipe" | "Tạo Recipe" | `#btn-build-recipe-gen` via `data-i18n` |

**Gaps — hardcoded English strings in this screen (no i18n):**

| String | Location |
|--------|----------|
| `"Copy"` / `"Copied!"` | `#btn-copy` label (JS) |
| `"Back"` | `#btn-back` HTML + no JS override |
| `"Lock"` | `#btn-lock-gen` HTML + no JS override |
| `"Profile: "` prefix | `genProfileLabel.textContent` template literal |
| `"📥 "` shared prefix | `genProfileLabel.textContent` template literal |
| `"🔑 Don't forget your pepper!"` | `genHint.textContent` |
| `"No recipe found — select a cell with a recipe, then re-open."` | `genHint.textContent` |
| `` `Error: ${...}` `` pattern | `genHint.textContent` |
| `"⚠️ Reload the tab to activate the extension."` | `genHint.textContent` |
| `` `⚠️ ${e.message}` `` pattern | `genHint.textContent` |

`profileFallback` key exists (`"Using default profile"`) but is **not used** anywhere in this screen.

---

## Related Screens

- [03-popup-home.md](./03-popup-home.md) — Parent; auto-routes here when on Sheets tab
- [05-recipe-builder.md](./05-recipe-builder.md) — Forward via `[Build Recipe]`
- [13-error-states.md](./13-error-states.md) — Cross-cutting error rules (sheet mismatch, fallback)
- [02-popup-unlock.md](./02-popup-unlock.md) — Precondition (must be unlocked)
