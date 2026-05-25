# 05 — Recipe Builder (Popup)

Reverse-engineered từ [popup-recipe-builder.js](../../chrome-extension/src/popup/popup-recipe-builder.js) và [popup.html](../../chrome-extension/src/popup/popup.html) `#status-builder` section.

**Triggered bởi:** `[Build Recipe]` button trên Unlocked screen hoặc Generated screen.
**Scope:** Own profiles only. Shared profiles dùng sheetId binding qua `sheetMapping`, không qua builder.

---

## Layout

```
┌──────────────────────────────────────────┐
│ 🔐 PassChef                  🟢      ⚙️ │
│ ───────────────────────────────────────  │
│  [Back]           ? How it works         │
│                                          │
│  Hash                                    │
│  The text part of your recipe            │  ← .builder-subtitle
│  [____________________________]          │
│                                          │
│  Position                                │
│  Where your secret goes                  │  ← .builder-subtitle
│  [ # ][ $ ][ @ ][ % ][ ^ ]               │
│                                          │
│  Secret                                  │
│  Which secret phrase to use              │  ← .builder-subtitle
│  [ 1 ][ 2 ][ 3 ][ 4 ][ 5 ]               │
│                                          │
│  ▶ More options                          │  ← <details> collapsed by default
│  ┌─────────────────────────────────────┐ │  (auto-expanded if sheet detected)
│  │ Target Sheet ID                     │ │
│  │ [Paste URL or Sheet ID] [↻]         │ │
│  │ [picker: tab1] [tab2] …             │ │
│  │ ⚠ Without target sheet…            │ │
│  │                                     │ │
│  │ Modifiers                           │ │
│  │ Optional transformations            │ │  ← .builder-subtitle
│  │ [☐ _] [☐ !] [☐ ?] [☐ ~]            │ │
│  │                                     │ │
│  │ Profile  ⓘ Auto-selected from…     │ │
│  │ [ Banking (locked)             ▼]   │ │
│  └─────────────────────────────────────┘ │
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

## "? How it works" Link

A small link/button in the builder header area, right of the back button row.

| Element | Detail |
|---|---|
| Selector | `#btn-how-it-works` or `.btn.link` in header row |
| Label | `data-i18n="linkHowItWorks"` → "? How it works" |
| Behavior | `chrome.tabs.create({ url: chrome.runtime.getURL('demo/demo.html') })` |
| Position | Top of builder section, beside or after the section title / back navigation |

See [16-demo-page.md](./16-demo-page.md) for the demo page spec.

---

## Micro-copy Subtitles

Each primary field label has a subtitle paragraph with class `builder-subtitle`. The subtitle is a short, plain-language description rendered directly below the field label and above the input/control.

| Field | `data-i18n` key | EN subtitle |
|-------|----------------|-------------|
| Hash | `builderSubtitleHash` | "The text part of your recipe" |
| Position | `builderSubtitlePosition` | "Where your secret goes" |
| Secret | `builderSubtitleSecret` | "Which secret phrase to use" |
| Modifiers | `builderSubtitleModifiers` | "Optional transformations" |

**DOM:** `<p class="builder-subtitle" data-i18n="builderSubtitleHash">` inserted after each `<label>` element and before the corresponding input/toggle-group.

**CSS:** `.builder-subtitle` — font-size: 0.75rem; color: `var(--text-secondary)`; margin-bottom: 0.25rem; font-weight: normal; text-transform: none; letter-spacing: normal.

---

## "More Options" Collapsible Section

The Sheet ID field, Modifiers row, and Profile dropdown are wrapped in a `<details>`/`<summary>` collapsible element to reduce visual complexity for new users.

**Structure:**
```html
<details id="bld-more-options">
  <summary data-i18n="moreOptions">More options</summary>

  <!-- Target Sheet ID field (existing) -->
  <!-- Modifiers row (existing) -->
  <!-- Profile dropdown (existing) -->
</details>
```

**Default state:** Collapsed (`open` attribute absent).

**Auto-expand rule:** If sheet auto-detection succeeds (a Sheets tab is active and sheet ID is populated), `#bld-more-options` is opened automatically (`details.open = true`) so the user can see the pre-filled sheet context. This happens at the end of `runDetection()` in `sheet-detector.js`.

**Summary element:** `<summary>` with `data-i18n="moreOptions"` → "More options". Uses default browser disclosure triangle or custom CSS triangle indicator.

**i18n key:** `moreOptions` → "More options" (EN), TBD (VI, Phase 7).

---

## Smart Defaults (Returning Users)

On builder open, the Position and Secret fields are pre-selected from the last-used values stored in `chrome.storage.local`. This replaces the previous behavior of no defaults (fields start empty).

### Storage keys

| Key | Storage area | Type | Purpose |
|-----|---|---|---|
| `lastUsedPosition` | `chrome.storage.local` | string | Last selected Position symbol (e.g. `"#"`) |
| `lastUsedSecret` | `chrome.storage.local` | string | Last selected Secret index (e.g. `"1"`) |

### Load on open

In `loadProfiles()` (or a new `loadLastUsed()` helper called after `reset()`):
1. Read `lastUsedPosition` and `lastUsedSecret` from `chrome.storage.local`.
2. If `lastUsedPosition` is present, activate the matching toggle button in `#bld-position-group` (set `.active` class, update `state.position`).
3. If `lastUsedSecret` is present, activate the matching toggle button in `#bld-secret-group` (set `.active` class, update `state.secret`).
4. First-time users (keys absent): fields start empty — no change from previous behavior.

### Save on Copy Recipe

When user clicks `[Copy Recipe]` (and recipe is not `—`):
1. Write `lastUsedPosition = state.position` to `chrome.storage.local` (skip if null).
2. Write `lastUsedSecret = state.secret` to `chrome.storage.local` (skip if null).

---

## Form Fields

### Hash
- **Input:** `#bld-hash` — Required, ASCII alphanumeric only (`/^[a-zA-Z0-9]*$/`)
- **Subtitle:** `.builder-subtitle` with `data-i18n="builderSubtitleHash"`
- **Invalid → inline error** `#bld-hash-error`: i18n `errHashAscii`
  - Actual message: *"Only ASCII letters and digits (a-z, A-Z, 0-9). Unicode is unreliable across devices."*
- **Visual:** `.invalid` class adds red border on `#bld-hash`

### Position (single-select toggle group) — `#bld-position-group`
- **Subtitle:** `.builder-subtitle` with `data-i18n="builderSubtitlePosition"`

| Value | `data-i18n-title` |
|-------|------------------|
| `#` | `hintPositionHash` |
| `$` | `hintPositionDollar` |
| `@` | `hintPositionAt` |
| `%` | `hintPositionPercent` |
| `^` | `hintPositionCaret` |

Click active button again → deselects (`state.position = null`). Active button gets `.active` class; others lose it.

### Secret (single-select toggle group) — `#bld-secret-group`
- **Subtitle:** `.builder-subtitle` with `data-i18n="builderSubtitleSecret"`

Values: `1` · `2` · `3` · `4` · `5` — maps to secret index in profile.
Same click-twice-to-deselect behavior as Position. **No `data-i18n-title` tooltips on secret buttons.**

### Target Sheet ID (inside More Options)
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
    - Active Sheets tab → auto-fills with full tab URL, calls `onDetected` → `parseSheetInput` normalizes; auto-expands `#bld-more-options`
    - No active, 1 other tab → auto-fills silently (no picker shown); auto-expands `#bld-more-options`
    - No active, 2+ other tabs → shows `#bld-tabs-picker` with labeled buttons; auto-expands `#bld-more-options`
    - Nothing found → leaves input empty, `#bld-more-options` stays collapsed
  - `onDetected` callback receives **full tab URL**; `parseSheetInput()` then extracts/normalizes to bare ID
  - Button shows `…` + `disabled` while loading (`loading` CSS class)
  - `reset()` clears `#bld-tabs-picker` (hides + empties innerHTML)

### Modifiers (multi-select checkboxes, inside More Options) — `#bld-modifier-row`
- **Subtitle:** `.builder-subtitle` with `data-i18n="builderSubtitleModifiers"`

DOM order (determines recipe string order):
| Symbol | `data-i18n-title` |
|--------|------------------|
| `_` | `hintModifierFlip` |
| `!` | `hintModifierUpper` |
| `?` | `hintModifierReverse` |
| `~` | `hintModifierStrip` |

`currentModifiers()` reads checked checkboxes in DOM order via `querySelectorAll('input[type="checkbox"]:checked')`.

### Profile (dropdown, inside More Options) — `#bld-profile`
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

- Hint element: `#bld-profile-hint.builder-hint` — inline `<span>` AFTER the "Profile" text inside the `.builder-label`

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
- Tag does not change password value — verification-only metadata
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
- Button label flashes `data-i18n="lblCopied"` ("Copied!") for 1500ms then restored to `data-i18n="btnCopyRecipe"`
- On copy: writes `lastUsedPosition` and `lastUsedSecret` to `chrome.storage.local` (smart defaults save)

### Back
- `reset()` called first → then `onBack()` callback (returns to Unlocked screen)

---

## State Diagram

```
loadProfiles() → cache sheetMapping + defaultProfile + populate dropdown
       │
       ├── loadLastUsed() → pre-select position + secret from chrome.storage.local
       │
       ▼
   runDetection() → auto-detect active Sheets tab (or show picker)
       │               └── if detected → auto-expand #bld-more-options
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
- [16-demo-page.md](./16-demo-page.md) — Demo page opened by "? How it works" link

---

## i18n Keys

| Key | English |
|---|---|
| `btnBuildRecipe` | Build Recipe |
| `builderSheetUrl` | Target Sheet ID |
| `builderHash` | Hash |
| `builderSubtitleHash` | The text part of your recipe |
| `builderPosition` | Position |
| `builderSubtitlePosition` | Where your secret goes |
| `builderSecret` | Secret |
| `builderSubtitleSecret` | Which secret phrase to use |
| `builderModifiers` | Modifiers |
| `builderSubtitleModifiers` | Optional transformations |
| `builderProfile` | Profile |
| `builderRecipeOutput` | Recipe |
| `builderPasswordPreview` | REAL PASSWORD |
| `btnBuilderBack` | Back |
| `btnCopyRecipe` | Copy Recipe |
| `lblCopied` | Copied! |
| `moreOptions` | More options |
| `linkHowItWorks` | ? How it works |
| `warnNoSheetUrl` | Without target sheet, this recipe won't verify on decode. |
| `errInvalidSheetUrl` | Not a valid Sheet ID or URL |
| `errHashAscii` | Only ASCII letters and digits (a-z, A-Z, 0-9). Unicode is unreliable across devices. |
| `hintPositionHash` · `hintPositionDollar` · `hintPositionAt` · `hintPositionPercent` · `hintPositionCaret` | (HTML tooltip content, position semantics) |
| `hintModifierFlip` · `hintModifierUpper` · `hintModifierReverse` · `hintModifierStrip` | (HTML tooltip content, modifier semantics) |
| `btnDetectRefreshTitle` | Re-detect Sheets tabs |
| `detectedNTabs` | Detected $1 Sheets tabs — pick one: |
| `hintProfileFromMapping` | Auto-selected from sheet mapping |
| `hintProfileUnmapped` | Sheet not mapped — using default. Map in Options for stable behavior. |

**Hardcoded strings (acceptable, future i18n):**
- `"(no profile)"` error in password preview when profile dropdown is empty
- `"Error"` fallback when SW returns no error message
- Refresh button character `↻` / loading indicator `…`
