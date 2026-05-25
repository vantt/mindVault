# 03 — Popup: Home / Hub Screen

Reverse-engineered từ [`popup.html`](../../chrome-extension/src/popup/popup.html) (`#status-unlocked`) và [`popup.js`](../../chrome-extension/src/popup/popup.js) lines 104–184, 224–225.

**Triggered bởi:** Unlock thành công (`sessionKey` present in session storage). Home is the primary hub; it shows contextual state depending on whether the user is on a Sheets tab and whether the current cell has a valid recipe.

**Precondition:** `salt` exists (setup done) + `sessionKey` in `chrome.storage.session` (unlocked).

---

## Contextual States

### A. First-use state

Displayed when `chrome.storage.local` key `hasSeenQuickStart` is absent or `false`. Shown INSTEAD of the normal hint text.

```
┌──────────────────────────────────────────┐
│ 🔐 PassChef          🟢 Unlocked    ⚙️ │
│ ──────────────────────────────────────── │
│ ┌── How it works ───────────────────── ×┐│
│ │ 1. Build a recipe                     ││
│ │ 2. Paste it into a Google Sheets cell ││
│ │ 3. Click the cell → password appears  ││
│ │ [Got it ✓]          [Learn more →]    ││
│ └──────────────────────────────────────┘│
│                                          │
│  [     Create First Recipe     ]         │
│  [           Lock              ]         │
└──────────────────────────────────────────┘
```

**DOM elements (Quick Start panel):**

| ID / Selector | Type | Notes |
|---|---|---|
| `#home-quick-start` | `<div>` | Entire panel; `hidden` when `hasSeenQuickStart` is truthy |
| `.quick-start-title` | `<h3>` | `data-i18n="quickStartTitle"` → "How it works" |
| `#btn-quick-start-dismiss` | `<button>` | `×` close icon; sets `hasSeenQuickStart = true` in local storage, hides panel |
| `.quick-start-steps` | `<ol>` | Three `<li>` steps with `data-i18n` keys (see below) |
| `#btn-got-it` | `<button class="btn secondary">` | `data-i18n="btnGotIt"` → "Got it ✓"; same effect as dismiss |
| `#btn-learn-more` | `<button class="btn link">` | `data-i18n="linkLearnMore"` → "Learn more →"; opens demo page |

**CTA button change:** Primary button shows `data-i18n="btnCreateFirstRecipe"` ("Create First Recipe") instead of `btnBuildRecipe` ("Build Recipe") when Quick Start panel is visible.

**Storage key:** `chrome.storage.local` → `hasSeenQuickStart` (boolean). Set to `true` on "Got it ✓" or `×` dismiss.

---

### B. Standard state

Displayed when `hasSeenQuickStart` is `true` AND the active tab is NOT a Google Sheets tab.

```
┌──────────────────────────────────────────┐
│ 🔐 PassChef          🟢 Unlocked    ⚙️ │
│ ──────────────────────────────────────── │
│                                          │
│  Click any cell in Google Sheets         │
│  containing a recipe.                    │
│                                          │
│  Profiles: 3 own · 1 shared              │
│                                          │
│  [        Build Recipe        ]          │
│  [           Lock             ]          │
└──────────────────────────────────────────┘
```

**DOM elements:**

| ID / Selector | Type | Notes |
|---|---|---|
| `#home-hint` | `<p class="hint">` | `data-i18n="hintClickCell"` — always visible in standard state |
| `#profiles-summary` | `<p class="hint profiles-summary">` | Hidden by default; shown when profiles exist |
| `#home-sheet-context` | `<div>` | Sheet name badge area; hidden in standard state |
| `#home-notice` | `<p class="hint notice">` | Amber inline notice/warning; hidden in standard state |

---

### C. On Sheets tab — empty or invalid cell

Displayed when the active tab IS a Sheets tab AND `GET_CURRENT_CELL_PASSWORD` returns `error === "Empty cell"` or any non-success error.

```
┌──────────────────────────────────────────┐
│ 🔐 PassChef          🟢 Unlocked    ⚙️ │
│ ──────────────────────────────────────── │
│                                          │
│ 📄 Budget 2026                           │
│ ⚠ Selected cell is empty or has no      │
│   recipe.                                │
│                                          │
│  [        Build Recipe        ]          │
│  [           Lock             ]          │
└──────────────────────────────────────────┘
```

**`#home-sheet-context`** — shown with sheet name extracted from tab title (text before `" - Google Sheets"` suffix). Format: `data-i18n="hintSheetContext"` with `$1` placeholder replaced by the sheet name.

**`#home-notice`** — shown with class `notice warning` (amber). `data-i18n="hintEmptyCell"` → "Selected cell is empty or has no recipe."

---

### D. On Sheets tab — connection error

Displayed when the active tab IS a Sheets tab AND a JS exception (`catch`) occurs (e.g. content script not injected, tab reload needed).

```
┌──────────────────────────────────────────┐
│ 🔐 PassChef          🟢 Unlocked    ⚙️ │
│ ──────────────────────────────────────── │
│                                          │
│ 📄 Budget 2026                           │
│ ⚠ Extension needs tab reload.           │
│                                          │
│  [        Build Recipe        ]          │
│  [           Lock             ]          │
└──────────────────────────────────────────┘
```

**`#home-notice`** — shown with class `notice warning` (amber). `data-i18n="hintNeedsReload"` → "Extension needs tab reload."

---

## Header

| Element | Detail |
|---------|--------|
| Title | `🔐 PassChef` (h1) |
| Status pill | Green dot · i18n `statusUnlocked` ("Unlocked") |
| Settings button | `⚙️` top-right (`#btn-settings`) → `chrome.runtime.openOptionsPage()` |

Status pill DOM: `#global-status` (wrapper), `#global-status-dot` (dot span), `#global-status-text` (text span). Classes applied dynamically by `showSection()`: dot gets `dot green`, wrapper gets `active`. Defined in `SECTION_STATUS['status-unlocked']`.

---

## DOM Elements Summary

| ID / Selector | Type | Visibility |
|---|---|---|
| `#home-quick-start` | `<div>` | Shown only when `hasSeenQuickStart` is falsy |
| `#home-hint` | `<p class="hint">` | Shown in standard state + hidden in sheet states |
| `#home-sheet-context` | `<div>` | Shown only on Sheets tab (states C, D) |
| `#home-notice` | `<p class="hint notice">` | Shown only on Sheets tab errors (states C, D) |
| `#profiles-summary` | `<p class="hint profiles-summary">` | Shown when `ownCount > 0 || sharedCount > 0` |
| `#btn-build-recipe` | `<button class="btn primary">` | Always visible; label changes in first-use state |
| `#btn-lock` | `<button class="btn secondary">` | Always visible |

---

## Profiles Summary

- Element: `#profiles-summary` (`.hint.profiles-summary`)
- **Hidden by default** (class `hidden`); shown only when `ownCount > 0 || sharedCount > 0`.
- Format: `Profiles: N own · M shared` — i18n key `profilesSummary` with placeholders
- Counts from `chrome.storage.sync.get(null)`:
  - `ownCount` = keys matching `profile:*`
  - `sharedCount` = keys matching `shared:*`
- Style: `text-secondary`, `cursor: pointer`
- Click: `chrome.runtime.openOptionsPage()`

---

## Updated Auto-routing on Popup Open

Runs immediately after showing `#status-unlocked` — before user interaction.

```
popup open (unlocked)
  └─ Sheets tab?
       ├─ YES → sendMessage(GET_CURRENT_CELL_PASSWORD)
       │         ├─ success (response.success === true)
       │         │     → showSection('status-generated')  ← SUCCESS ONLY
       │         │       populate password + profile label
       │         ├─ empty cell (response.error === "Empty cell")
       │         │     → stay Home, show #home-sheet-context + #home-notice (hintEmptyCell)
       │         ├─ other error (response.error, any)
       │         │     → stay Home, show #home-sheet-context + #home-notice (hintNeedsReload or error text)
       │         └─ catch (JS exception)
       │               → stay Home, show #home-sheet-context + #home-notice (hintNeedsReload)
       └─ NO → stay Home, standard hint (#home-hint visible, #home-sheet-context hidden)
```

**Key change from previous design:** Error paths NO LONGER route to the Generated screen. All errors keep the user on Home with an amber inline notice. Only `response.success === true` routes to Generated.

### Success path (Sheets tab, valid recipe cell)

- `showSection('status-generated')` — popup transitions to Generated screen.
- `genPasswordInput.value = response.password`
- If `response.profileName`: show `#gen-profile-label` with text `` `Profile: ${prefix}${response.profileName}` ``
- If `response.settings?.pepperingHint`: show pepper reminder hint in Generated screen.

### Error paths (stay on Home screen)

| Condition | `#home-notice` text | DOM key |
|-----------|---------------------|---------|
| `response.error === "Empty cell"` | "Selected cell is empty or has no recipe." | `hintEmptyCell` |
| Other `response.error` (parse error, etc.) | "Selected cell is empty or has no recipe." | `hintEmptyCell` |
| `catch(e)` — connection error | "Extension needs tab reload." | `hintNeedsReload` |

Sheet name badge (`#home-sheet-context`) is populated from the active tab title for all error states on a Sheets tab.

### Non-Sheets tab

Active tab URL does not include `docs.google.com/spreadsheets` → no message sent → `#home-hint` shown, `#home-sheet-context` hidden.

---

## Build Recipe Flow

Handler `openBuilder` wired to `#btn-build-recipe`.

**Order is critical:**

```js
builder.reset();          // 1. Clear all form fields first
await builder.loadProfiles(); // 2. Populate profiles + run sheet auto-detection
showSection('status-builder'); // 3. Switch to Builder screen
```

Inverting 1 and 2 would wipe the sheet URL that `loadProfiles()` auto-detects from the active Sheets tab.

Sheet auto-detection is handled by [`sheet-detector.js`](../../chrome-extension/src/popup/sheet-detector.js) — see [05-recipe-builder.md](./05-recipe-builder.md).

---

## Lock Flow

```js
await chrome.storage.session.remove("sessionKey");
window.close();
```

- Removes session key → popup re-opens locked next time.
- `window.close()` closes popup immediately — no confirmation.
- Same handler logic also on `#btn-lock-gen` (Generated screen).

---

## i18n Keys

| Key | EN value | VI value | Notes |
|-----|----------|----------|-------|
| `statusUnlocked` | "Unlocked" | "Đã Mở khóa" | Header status pill |
| `hintClickCell` | "Click any cell in Google Sheets containing a recipe." | "Nhấp vào ô chứa Recipe trong Google Sheets." | `#home-hint` paragraph |
| `btnBuildRecipe` | "Build Recipe" | "Tạo Recipe" | Primary CTA (returning user) |
| `btnLock` | "Lock" | "Khóa" | Secondary CTA |
| `quickStartTitle` | "How it works" | TBD (Phase 7) | Quick Start panel title |
| `quickStartStep1` | "Build a recipe" | TBD | Step 1 in Quick Start |
| `quickStartStep2` | "Paste it into a Google Sheets cell" | TBD | Step 2 in Quick Start |
| `quickStartStep3` | "Click the cell → password appears" | TBD | Step 3 in Quick Start |
| `btnGotIt` | "Got it ✓" | TBD | Quick Start dismiss CTA |
| `linkLearnMore` | "Learn more →" | TBD | Quick Start demo link |
| `btnCreateFirstRecipe` | "Create First Recipe" | TBD | Primary CTA for first-use state |
| `hintSheetContext` | "📄 $1" | TBD | Sheet name badge (placeholder `$1` = sheet name) |
| `hintEmptyCell` | "Selected cell is empty or has no recipe." | TBD | Amber notice for empty/invalid cell |
| `hintNeedsReload` | "Extension needs tab reload." | TBD | Amber notice for connection error |
| `profilesSummary` | "Profiles: $1 own · $2 shared" | TBD | Profiles count summary |

### Storage keys

| Key | Storage area | Type | Purpose |
|-----|---|---|---|
| `hasSeenQuickStart` | `chrome.storage.local` | boolean | Controls Quick Start panel visibility |

---

## Related Screens

| Screen | File | Relationship |
|--------|------|--------------|
| Unlock | [02-popup-unlock.md](./02-popup-unlock.md) | Precondition — enters Home after successful unlock |
| Recipe Builder | [05-recipe-builder.md](./05-recipe-builder.md) | Forward: `[Build Recipe]` button |
| Generated | [04-popup-generated.md](./04-popup-generated.md) | Auto-route target on Sheets tab — SUCCESS ONLY |
| Demo page | [16-demo-page.md](./16-demo-page.md) | "Learn more →" from Quick Start panel |
| Options tab nav | [06-options-tab-navigation.md](./06-options-tab-navigation.md) | `⚙️` and profiles-summary click destination |
