# 05 — Recipe Builder (Popup)

Reverse-engineered từ [popup-recipe-builder.js](../../chrome-extension/src/popup/popup-recipe-builder.js) và [popup.html](../../chrome-extension/src/popup/popup.html) `#status-builder` section.

**Triggered bởi:** `[Build Recipe]` button trên Unlocked screen hoặc Generated screen.
**Scope:** Own profiles only. Shared profiles dùng sheetId binding qua `sheetMapping`, không qua builder.

---

## Layout

```
┌──────────────────────────────────────────┐
│ 🔐 mindVault                  🟢      ⚙️ │
│ ───────────────────────────────────────  │
│                                          │
│  Target Sheet ID                         │
│  ┌[Paste URL or Sheet ID_________] [↻]┐  │
│  └─ (input-with-refresh wrapper) ──────┘  │
│  [picker: tab1] [tab2] … (if 2+ tabs)    │
│  ⚠ Without target sheet, this recipe     │
│     won't verify on decode.              │
│                                          │
│  Hash                                    │
│  [____________________________]          │
│                                          │
│  Position                                │
│  [ # ][ $ ][ @ ][ % ][ ^ ]               │
│                                          │
│  Secret                                  │
│  [ 1 ][ 2 ][ 3 ][ 4 ][ 5 ]               │
│                                          │
│  Modifiers                               │
│  [☐ _] [☐ !] [☐ ?] [☐ ~]                 │
│                                          │
│  Profile  ⓘ Auto-selected from mapping   │
│  [ Banking (locked)                ▼]    │
│                                          │
│  ┌─ Recipe ────────────────────────────┐ │
│  │ fb#1_.a3f2                          │ │
│  └─────────────────────────────────────┘ │
│                                          │
│  ┌─ REAL PASSWORD ─────────────────────┐ │
│  │ XyZ4kP9mNqRsTu                      │ │
│  └─────────────────────────────────────┘ │
│                                          │
│  [Back]                  [Copy Recipe]   │
└──────────────────────────────────────────┘
```

Note: The `↻` refresh button and tabs picker are **dynamically injected** by `sheet-detector.js` — they are not in static HTML. `initSheetDetector` wraps `#bld-sheet-url` in a `.input-with-refresh` div and appends a `#bld-detect-refresh` button. A `#bld-tabs-picker` div is inserted after `#bld-sheet-url-error`. The `#bld-profile-hint` paragraph (after the profile dropdown) IS in static HTML — toggled by `alignProfileToSheet()`.

---

## Form Fields

### Target Sheet ID
- **Input:** `#bld-sheet-url` — accepts full Google Sheets URL hoặc bare Sheet ID
- **Auto-normalize:** Pasted URL → collapse to bare ID on the field (fits 300px popup width)
- **URL regex:** `/\/spreadsheets\/d\/([a-zA-Z0-9_-]+)/`
- **Sheet ID regex:** `^[a-zA-Z0-9_-]{20,}$` (min 20 chars heuristic)
- **States:**
  - Empty → `#bld-sheet-warning` shown (`warnNoSheetUrl`), error hidden
  - Invalid (non-empty, no match) → `#bld-sheet-url-error` shown (`errInvalidSheetUrl`), warning hidden
  - Valid → both warning + error hidden; `state.sheetId` set to extracted ID
- **Sheet detector** (`initSheetDetector`):
  - Refresh button `↻` (`#bld-detect-refresh`) — calls SW `GET_SHEET_ID_FROM_ACTIVE_TAB`
  - Result handling:
    - Active Sheets tab → auto-fills with full tab URL, calls `onDetected` → `parseSheetInput` normalizes
    - No active, 1 other tab → auto-fills silently (no picker shown)
    - No active, 2+ other tabs → shows `#bld-tabs-picker` with labeled buttons; user clicks one
    - Nothing found → leaves input empty, existing warning shown
  - `onDetected` callback receives **full tab URL**; `parseSheetInput()` then extracts/normalizes to bare ID
  - Button shows `…` + `disabled` while loading (`loading` CSS class)
  - `reset()` clears `#bld-tabs-picker` (hides + empties innerHTML)

### Hash
- **Input:** `#bld-hash` — Required, ASCII alphanumeric only (`/^[a-zA-Z0-9]*$/`)
- **Invalid → inline error** `#bld-hash-error`: i18n `errHashAscii`
  - Actual message: *"Only ASCII letters and digits (a-z, A-Z, 0-9). Unicode is unreliable across devices."*
- **Visual:** `.invalid` class adds red border on `#bld-hash`

### Position (single-select toggle group) — `#bld-position-group`
| Value | `data-i18n-title` |
|-------|------------------|
| `#` | `hintPositionHash` |
| `$` | `hintPositionDollar` |
| `@` | `hintPositionAt` |
| `%` | `hintPositionPercent` |
| `^` | `hintPositionCaret` |

Click active button again → deselects (`state.position = null`). Active button gets `.active` class; others lose it.

### Secret (single-select toggle group) — `#bld-secret-group`
Values: `1` · `2` · `3` · `4` · `5` — maps to secret index in profile.
Same click-twice-to-deselect behavior as Position. **No `data-i18n-title` tooltips on secret buttons.**

### Modifiers (multi-select checkboxes) — `#bld-modifier-row`
DOM order (determines recipe string order):
| Symbol | `data-i18n-title` |
|--------|------------------|
| `_` | `hintModifierFlip` |
| `!` | `hintModifierUpper` |
| `?` | `hintModifierReverse` |
| `~` | `hintModifierStrip` |

`currentModifiers()` reads checked checkboxes in DOM order via `querySelectorAll('input[type="checkbox"]:checked')`.

### Profile (dropdown) — `#bld-profile`
- **Auto-populated in `loadProfiles()`:** keys starting with `profile:` in `chrome.storage.sync`, sorted alphabetically
- **Default selected:** option whose value equals `defaultProfile` from storage
- **Auto-align with sheetMapping (`alignProfileToSheet()`):**
  - Called after every `parseSheetInput()` and at end of `loadProfiles()`
  - `sheetMapping` + `defaultProfileName` cached from storage on builder open
  - Behavior matrix:

| `state.sheetId` | `sheetMapping[sheetId]` | Dropdown | Hint (`#bld-profile-hint`) |
|---|---|---|---|
| set | exists & profile present | **Locked** to mapped profile (`disabled = true`) | `hintProfileFromMapping` (neutral italic) |
| set | not mapped | Selected `defaultProfile`, **enabled** (user can override) | `hintProfileUnmapped` (amber `.warning`) |
| null | — | **Enabled**, free pick | hidden |

- **Why:** decode-time routing uses `sheetMapping[sheetId]` → builder must match or computed tag will mismatch on decode. Locking when a mapping exists prevents silent footgun.
- Hint element: `#bld-profile-hint.builder-hint` — inline `<span>` AFTER the "Profile" text inside the `.builder-label` (sibling spans). Neutral grey italic; with `.warning` modifier → amber non-italic. CSS resets the label's `text-transform: uppercase` and `letter-spacing` so the hint reads normally.

---

## Preview Logic

**Recipe format:** `<hash><position><secret>[modifiers][.<tag>]`

Examples:
- No sheet: `fb#1_`
- With sheet (tag appended): `fb#1_.a3f2`

### Render rules

| Condition | Recipe output | REAL PASSWORD output |
|-----------|---------------|-----------------|
| Hash empty / position null / secret null | `—` (muted) | `—` (muted) |
| Sheet URL typed but invalid (non-empty + no match) | `—` (muted) | `—` (muted) |
| Valid base, no sheetId | base recipe immediate | `…` (muted) → password after 300ms |
| Valid base + sheetId | base recipe immediate → updated to `base.tag` after async | `…` (muted) → password after 300ms |
| Profile missing / empty | base shown | `(no profile)` (`.error`) |
| Generate error | base shown | error message (`.error`) |

### Verification tag (sheet-bound)
- Computed via SW action `COMPUTE_RECIPE_TAG`
- **Message shape sent from popup:**
  ```js
  {
    action: 'COMPUTE_RECIPE_TAG',
    recipeFields: { hash, position, secretIndex, modifiers },  // object
    sheetId,
    profileName,
  }
  ```
- SW resolves raw secret internally; returns `{ success: true, tag }` — 4-char HMAC string
- `finalRecipe = base + '.' + tag`
- Tag KHÔNG thay đổi password value — verification-only metadata
- See [docs/recipe-tag-design-rationale.md](../recipe-tag-design-rationale.md)

### Generate password SW message shape
```js
{
  action: 'GENERATE_PASSWORD',
  text: finalRecipe,   // full recipe string including tag if present
  sheetId,             // null if no sheet
  profileName,
}
```
Response: `{ success: true, password }` or `{ success: false, error, code }`.

### Debounce & cancellation
- `PREVIEW_DEBOUNCE_MS = 300`
- `previewToken` counter — incremented on each `requestPreview` call; stale async responses check `myToken !== previewToken` and discard
- `previewToken` also incremented in `reset()` to cancel any in-flight preview

---

## Actions

### Copy Recipe
- Reads `#bld-recipe-out` textContent → `navigator.clipboard.writeText()`
- No-op if value is `—`
- Button label set to hardcoded `"Copied!"` for 1500ms then restored (not i18n'd)

### Back
- `reset()` called first → then `onBack()` callback (returns to Unlocked screen)

---

## State Diagram

```
loadProfiles() → cache sheetMapping + defaultProfile + populate dropdown
       │
       ▼
   runDetection() → auto-detect active Sheets tab (or show picker)
       │
       ▼
   if no sheet input → parseSheetInput() sets initial warning
       │
       ▼
   alignProfileToSheet() → lock dropdown if sheet mapped, else default
       │
       ▼
   [user fills form] ──onFormChange()──┐
       │                                │
       ▼                                │
   buildBaseRecipe()                   │
       │                                │
       ├─ invalid/incomplete → "—"     │
       │                                │
       └─ valid ──optimistic render──► debounce 300ms
                                            │
                                            ▼
                                  requestPreview(base)
                                            │
                          ┌─ sheetId? yes ──┴── fetchTag() ──► finalRecipe = base.tag
                          │                                    (cancellation check)
                          ▼
                 GENERATE_PASSWORD action
                          │
                          ▼
                   preview password rendered
                   (cancellation check at each await)
```

---

## Related Screens

- [03-popup-home.md](./03-popup-home.md) — Popup Home; `[Build Recipe]` button entry
- [04-popup-generated.md](./04-popup-generated.md) — Generated screen; also has `[Build Recipe]` button
- [13-error-states.md](./13-error-states.md) — Sheet mismatch errors

---

## i18n Keys

| Key | English |
|---|---|
| `btnBuildRecipe` | Build Recipe |
| `builderSheetUrl` | Target Sheet ID |
| `builderHash` | Hash |
| `builderPosition` | Position |
| `builderSecret` | Secret |
| `builderModifiers` | Modifiers |
| `builderProfile` | Profile |
| `builderRecipeOutput` | Recipe |
| `builderPasswordPreview` | REAL PASSWORD |
| `btnBuilderBack` | Back |
| `btnCopyRecipe` | Copy Recipe |
| `warnNoSheetUrl` | Without target sheet, this recipe won't verify on decode. |
| `errInvalidSheetUrl` | Not a valid Sheet ID or URL |
| `errHashAscii` | Only ASCII letters and digits (a-z, A-Z, 0-9). Unicode is unreliable across devices. |
| `hintPositionHash` · `hintPositionDollar` · `hintPositionAt` · `hintPositionPercent` · `hintPositionCaret` | (HTML tooltip content, position semantics) |
| `hintModifierFlip` · `hintModifierUpper` · `hintModifierReverse` · `hintModifierStrip` | (HTML tooltip content, modifier semantics) |
| `btnDetectRefreshTitle` | Re-detect Sheets tabs |
| `detectedNTabs` | Detected $1 Sheets tabs — pick one: |
| `noSheetTabsOpen` | No Google Sheets tabs open *(referenced in messages.json but not currently used in sheet-detector.js code)* |
| `hintSheetUrlAuto` | Auto-filled from current tab. Edit if targeting a different sheet. *(defined in messages.json, not currently used in code)* |
| `hintProfileFromMapping` | Auto-selected from sheet mapping |
| `hintProfileUnmapped` | Sheet not mapped — using default. Map in Options for stable behavior. |

**Hardcoded strings (not i18n'd):**
- `"Copied!"` flash on Copy Recipe button
- `"(no profile)"` error in password preview when profile dropdown is empty
- `"Error"` fallback when SW returns no error message
- Refresh button character `↻` / loading indicator `…`
