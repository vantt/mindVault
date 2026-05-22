# 03 — Popup: Home / Hub Screen

Reverse-engineered từ [`popup.html`](../../chrome-extension/src/popup/popup.html) (`#status-unlocked`) và [`popup.js`](../../chrome-extension/src/popup/popup.js) lines 104–184, 224–225.

**Triggered bởi:** Unlock thành công (`sessionKey` present in session storage) **và** active tab KHÔNG phải Google Sheets (hoặc Sheets tab nhưng content script không trả về password). Đây là fallback hub khi auto-route sang Generated screen không xảy ra.

**Precondition:** `salt` exists (setup done) + `sessionKey` in `chrome.storage.session` (unlocked).

---

## Layout

```
┌──────────────────────────────────────────┐
│ 🔐 mindVault          🟢 Unlocked    ⚙️ │
│ ──────────────────────────────────────── │
│                                          │
│  Click any cell in Google Sheets         │
│  containing a recipe.                    │
│                                          │
│  Profiles: 3 own · 1 shared          ←  click to open Options
│                                          │
│  [        Build Recipe        ]          │  ← primary CTA
│  [           Lock             ]          │  ← secondary CTA
│                                          │
└──────────────────────────────────────────┘
```

---

## Header

| Element | Detail |
|---------|--------|
| Title | `🔐 mindVault` (h1) |
| Status pill | Green dot · i18n `statusUnlocked` ("Unlocked") |
| Settings button | `⚙️` top-right (`#btn-settings`) → `chrome.runtime.openOptionsPage()` |

Status pill DOM: `#global-status` (wrapper), `#global-status-dot` (dot span), `#global-status-text` (text span). Classes applied dynamically by `showSection()`: dot gets `dot green`, wrapper gets `active`. Defined in `SECTION_STATUS['status-unlocked']`.

---

## Sections

### Hint text

- Element: `<p class="hint">` with `data-i18n="hintClickCell"`
- Text: "Click any cell in Google Sheets containing a recipe."
- Always visible when Home screen is shown.

### Profiles summary

- Element: `#profiles-summary` (`.hint.profiles-summary`)
- **Hidden by default** (class `hidden`); shown only when `ownCount > 0 || sharedCount > 0`.
- Format: `Profiles: N own · M shared`
- Counts from `chrome.storage.sync.get(null)`:
  - `ownCount` = keys matching `profile:*`
  - `sharedCount` = keys matching `shared:*`
- Style: `text-secondary`, `cursor: pointer`
- Click: `chrome.runtime.openOptionsPage()`
- **i18n gap:** string is hardcoded English — not yet localized.

### Actions

| Button | ID | Class | i18n key | Behavior |
|--------|----|-------|----------|----------|
| Build Recipe | `#btn-build-recipe` | `.btn.primary` | `btnBuildRecipe` | See [Build Recipe flow](#build-recipe-flow) |
| Lock | `#btn-lock` | `.btn.secondary` | `btnLock` | See [Lock flow](#lock-flow) |

---

## Auto-routing on Popup Open

Runs immediately after showing `#status-unlocked` — before user interaction.

```
popup open
  └─ sessionKey present?
       └─ YES → showSection('status-unlocked')
                 + load profiles summary
                 + query active tab
                      ├─ tab.url includes "docs.google.com/spreadsheets"?
                      │    YES → sendMessage(GET_CURRENT_CELL_PASSWORD)
                      │          ├─ response.success → showSection('status-generated')
                      │          │                     populate password + profile label
                      │          └─ response.error / catch → showSection('status-generated')
                      │                                       show error hint (red text)
                      └─ NOT Sheets tab → stay on Home screen
```

### Success path (Sheets tab, valid recipe cell)

- `showSection('status-generated')` — popup transitions to Generated screen.
- `genPasswordInput.value = response.password`
- If `response.profileName`: show `#gen-profile-label` with text `` `Profile: ${prefix}${response.profileName}` `` where `prefix = response.isShared ? '📥 ' : ''`. The `"Profile: "` label prefix is **hardcoded English** (i18n gap).
- If `response.settings?.pepperingHint`: `genHint.textContent = "🔑 Don't forget your pepper!"` — **hardcoded English** (i18n gap).
- Home screen is never seen; user lands directly on Generated.

**content.js response shape** (`GET_CURRENT_CELL_PASSWORD`):
- Success: `{ success: true, password, profileName?, isShared?, settings: { pepperingHint? } }`
- Empty cell: `{ success: false, error: "Empty cell" }`
- Other error: `{ success: false, error: string, extractedText: string }` — `extractedText` is always attached by content.js (`response.extractedText = text`) before forwarding SW response.

### Error paths (remain on Generated screen, not Home)

All error paths switch to `status-generated` with empty password and red hint text:

| Condition | Hint text |
|-----------|-----------|
| `response.error === "Empty cell"` | "No recipe found — select a cell with a recipe, then re-open." |
| Other `response.error` | `` `Error: ${error}` `` (+ `` `("${extractedText}")` `` debug suffix if `response.extractedText` present) |
| `catch(e)` where `e.message` matches "Extension context invalidated", "Could not establish connection", or "Receiving end does not exist" | "⚠️ Reload the tab to activate the extension." |
| Other JS exception | `` `⚠️ ${e.message}` `` |

All error hint strings above are **hardcoded English** (i18n gaps).

Error hint color: hardcoded `"#da3633"` (= CSS `--danger`).

### Non-Sheets tab

Active tab URL does not include `docs.google.com/spreadsheets` → no message sent → user stays on Home screen.

---

## Build Recipe Flow

Handler `openBuilder` wired to `#btn-build-recipe` (same handler also on `#btn-build-recipe-gen` from Generated screen).

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
| `hintClickCell` | "Click any cell in Google Sheets containing a recipe." | "Nhấp vào ô chứa Recipe trong Google Sheets." | Hint paragraph |
| `btnBuildRecipe` | "Build Recipe" | "Tạo Recipe" | Primary CTA |
| `btnLock` | "Lock" | "Khóa" | Secondary CTA — **spec previously said "Lock Extension"; actual value is "Lock"** |

### i18n gaps (hardcoded English strings in popup.js)

| Location | Hardcoded string | Context |
|----------|-----------------|---------|
| `profilesSummary.textContent` | `` `Profiles: ${ownCount} own · ${sharedCount} shared` `` | Profiles summary element |
| `genProfileLabel.textContent` | `` `Profile: ${prefix}${name}` `` | Profile label in Generated screen, set during auto-route |
| `genHint.textContent` | `"🔑 Don't forget your pepper!"` | Pepper hint in Generated screen, set during auto-route |
| `genHint.textContent` | `"No recipe found — select a cell with a recipe, then re-open."` | Empty cell error |
| `genHint.textContent` | `` `Error: ${error}("${extractedText}")` `` | Recipe parse error |
| `genHint.textContent` | `"⚠️ Reload the tab to activate the extension."` | Connection error |
| `genHint.textContent` | `` `⚠️ ${e.message}` `` | Other JS exception |

---

## Related Screens

| Screen | File | Relationship |
|--------|------|--------------|
| Unlock | [02-popup-unlock.md](./02-popup-unlock.md) | Precondition — enters Home after successful unlock |
| Recipe Builder | [05-recipe-builder.md](./05-recipe-builder.md) | Forward: `[Build Recipe]` button |
| Generated | [04-popup-generated.md](./04-popup-generated.md) | Auto-route target on Sheets tab open |
| Options tab nav | [06-options-tab-navigation.md](./06-options-tab-navigation.md) | `⚙️` and profiles-summary click destination |
