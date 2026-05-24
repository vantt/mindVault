# 04 — Generated Screen (Popup)

Reverse-engineered từ [`popup.html`](../../chrome-extension/src/popup/popup.html) (`#status-generated`) và [`popup.js`](../../chrome-extension/src/popup/popup.js) lines 121–161, 224–238.

**Triggered bởi:** Auto-route từ [03-popup-home.md](./03-popup-home.md) **SUCCESS ONLY** — `response.success === true`. This screen is reached only when a valid recipe cell is detected and a password is generated successfully.

**NOT triggered by:** Error responses. All error states (empty cell, connection error, parse error) remain on the Home screen with an amber inline notice. See [03-popup-home.md](./03-popup-home.md) for error handling.

**Precondition:** Setup done + unlocked + active tab matches `docs.google.com/spreadsheets` + `response.success === true`.

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
│  🔑 Don't forget your pepper!           │  ← optional hint (success only)
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
| `#btn-copy` | `<button class="btn primary">` | `data-i18n="btnCopy"` → "Copy" |
| `#gen-hint` | `<p class="hint">` | Status hint text (pepper reminder only); empty by default |
| `#btn-build-recipe-gen` | `<button class="btn primary">` | `data-i18n="btnBuildRecipe"` |
| `.actions-row` (inside `#status-generated`) | `<div>` | Flex row wrapping Back + Lock |
| `#btn-back` | `<button class="btn secondary">` | `data-i18n="btnBuilderBack"` → "Back" |
| `#btn-lock-gen` | `<button class="btn secondary">` | `data-i18n="btnLock"` → "Lock" |

---

## Profile Indicator

`#gen-profile-label` content format (set entirely in JS):

```js
const prefix = response.isShared ? '📥 ' : '';
genProfileLabel.textContent = `Profile: ${prefix}${response.profileName}`;
```

- Own profile: `Profile: Default`
- Shared profile: `Profile: 📥 TeamFromB`
- Hidden (`class="hidden"`) when `response.profileName` is absent

Styling: class `profile-label`, font-size 0.85rem.

---

## Password Display & Copy

- `#gen-password` is `readonly` — user cannot edit
- Value is always non-empty on this screen (error paths no longer route here)
- Click `[Copy]`:
  1. `navigator.clipboard.writeText(password)`
  2. Button label → `data-i18n="lblCopied"` ("Copied!"), after 1500ms → restored to `data-i18n="btnCopy"` ("Copy")

---

## Hint Text States

On this screen `#gen-hint` is **success-only** — it shows the pepper reminder or nothing:

| Condition | Hint text | `style.color` |
|-----------|-----------|---------------|
| `response.settings?.pepperingHint` truthy | `"🔑 Don't forget your pepper!"` | `""` (cleared to default) |
| No pepper hint (normal success) | `""` (empty) | — |

**All error states have been removed from this screen.** The following error hint rows are no longer rendered here:

- ~~Empty cell error~~ → moved to Home `#home-notice`
- ~~Recipe parse error~~ → moved to Home `#home-notice`
- ~~Connection error~~ → moved to Home `#home-notice`
- ~~Other JS exception~~ → moved to Home `#home-notice`

---

## Actions

| Button | Handler | Behavior |
|--------|---------|----------|
| `[Copy]` | Inline click | Clipboard write → flash "Copied!" 1500ms |
| `[Build Recipe]` | `openBuilder` (shared with `#btn-build-recipe` on Home) | `builder.reset()` → `builder.loadProfiles()` → `showSection('status-builder')` |
| `[Back]` | Inline click | `tryAutoDetect()` re-runs → if success still detected, stays on Generated; otherwise `showSection('status-unlocked')` |
| `[Lock]` | Inline click | `chrome.storage.session.remove("sessionKey")` → `window.close()` |

**Back button behavior change (Phase 4):** `[Back]` now calls `tryAutoDetect()` rather than directly calling `showSection('status-unlocked')`. This re-runs the auto-routing logic so the user ends up in the correct state (Generated if cell is still valid, Home otherwise).

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
| `btnCopy` | "Copy" | "Sao chép" | `#btn-copy` label |
| `lblCopied` | "Copied!" | "Đã sao chép!" | `#btn-copy` flash state |
| `btnBuilderBack` | "Back" | "Quay lại" | `#btn-back` label |
| `btnLock` | "Lock" | "Khóa" | `#btn-lock-gen` label |

**Remaining hardcoded strings (acceptable, future i18n):**

| String | Location |
|--------|----------|
| `"Profile: "` prefix | `genProfileLabel.textContent` template literal |
| `"📥 "` shared prefix | `genProfileLabel.textContent` template literal |
| `"🔑 Don't forget your pepper!"` | `genHint.textContent` |

`profileFallback` key exists (`"Using default profile"`) but is **not used** anywhere in this screen.

---

## Related Screens

- [03-popup-home.md](./03-popup-home.md) — Parent; auto-routes here on SUCCESS ONLY from Sheets tab
- [05-recipe-builder.md](./05-recipe-builder.md) — Forward via `[Build Recipe]`
- [13-error-states.md](./13-error-states.md) — Cross-cutting error rules (sheet mismatch, fallback)
- [02-popup-unlock.md](./02-popup-unlock.md) — Precondition (must be unlocked)
