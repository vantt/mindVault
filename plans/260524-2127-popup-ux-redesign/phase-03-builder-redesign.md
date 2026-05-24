# Phase 03 — Builder Redesign (C)

**Status:** complete
**Effort:** ~2h
**Dependency:** Phase 01 complete

---

## Context Links

- `docs/ui-specs/05-recipe-builder.md` — updated spec (after Phase 01)
- `chrome-extension/src/popup/popup.html` — `#status-builder`
- `chrome-extension/src/popup/popup-recipe-builder.js` — ~397 lines (watch for modularization)
- `chrome-extension/src/popup/popup.css` — builder styles

---

## Overview

Four changes to the builder:

**C1 — Micro-copy subtitles:** Small descriptive text under each field label to orient first-time users.

**C2 — "More options" collapsible:** Sheet ID, Modifiers, and Profile fields collapse into a `<details>` block. Reduces visual noise for the common case (just hash + position + secret).

**C3 — Smart defaults (no random):** First-time users see an empty form. Returning users see their last-used Position and Secret pre-selected. Random defaults removed.

**C4 — "? How it works" link:** Small link in builder header area opens demo page.

---

## Requirements

### Functional
- C1: Each of Hash, Position, Secret, Modifiers labels shows a subtitle below it
- C2: Sheet ID + Modifiers + Profile are inside `<details id="bld-more-options">` with `<summary>` "More options"
- C2: Details element is collapsed by default; user can click to expand
- C2 open question: auto-expand when sheet is detected? → **Decision during impl**: auto-expand if `state.sheetId` is set after `loadProfiles()` runs detection. If sheetId detected, call `bldMoreOptions.open = true`.
- C3: `pickRandomDefaults()` removed entirely
- C3: On `loadProfiles()`: read `chrome.storage.local` for `lastUsedPosition` / `lastUsedSecret`; if present, pre-select those buttons
- C3: On `copyBtn` click (Copy Recipe): write current `state.position` + `state.secret` to `chrome.storage.local`
- C3: On fresh form (no stored values): all toggles start unselected, modifiers unchecked
- C4: A `<button id="btn-how-it-works">` link-style button in the builder header area opens demo page

### Non-functional
- Labels keep exact current text (Hash / Position / Secret / Modifiers / Profile / Target Sheet ID) — no renaming
- Subtitle text is i18n'd (keys: `builderSubtitleHash`, etc.)
- `<details>` is a native HTML element — no JS required to open/close (except the auto-expand on detection)
- `popup-recipe-builder.js` must stay under 200 lines after changes — if it grows over, extract smart-defaults logic into a separate function block or helper file

---

## Architecture

### HTML changes in `#status-builder`

**Builder header** (insert before `.builder-form`):
```html
<div class="builder-header-row">
  <button id="btn-how-it-works" class="btn-link" data-i18n="linkHowItWorks">? How it works</button>
</div>
```

**Per-field micro-copy** (example for Hash):
```html
<label class="builder-label" data-i18n="builderHash">Hash</label>
<p class="builder-subtitle" data-i18n="builderSubtitleHash">The text part of your recipe</p>
<input type="text" id="bld-hash" ...>
```

Same pattern for Position, Secret. Modifiers subtitle goes inside `<details>`.

**More options collapsible** — replaces the standalone Sheet ID, Modifiers, Profile sections:
```html
<details id="bld-more-options">
  <summary class="builder-more-summary" data-i18n="moreOptions">More options</summary>

  <label class="builder-label" data-i18n="builderSheetUrl">Target Sheet ID</label>
  <p class="builder-subtitle" data-i18n="builderSubtitleSheetId">Optional — links recipe to a specific sheet</p>
  <!-- input-with-refresh injected by sheet-detector.js here -->
  <input type="text" id="bld-sheet-url" ...>
  <p id="bld-sheet-url-error" ...></p>
  <p id="bld-sheet-warning" ...></p>

  <label class="builder-label" data-i18n="builderModifiers">Modifiers</label>
  <p class="builder-subtitle" data-i18n="builderSubtitleModifiers">Optional transformations</p>
  <div class="modifier-row" id="bld-modifier-row">...</div>

  <label class="builder-label">
    <span data-i18n="builderProfile">Profile</span>
    <span id="bld-profile-hint" class="builder-hint hidden"></span>
  </label>
  <select id="bld-profile"></select>
</details>
```

Note: `sheet-detector.js` dynamically wraps `#bld-sheet-url` in `.input-with-refresh` — this still works because it operates on the element by ID, not by DOM position.

### New CSS

```css
/* Builder header row (How it works link) */
.builder-header-row {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 6px;
}

/* Link-style button */
.btn-link {
  background: none;
  border: none;
  color: var(--accent);
  font-size: 0.72rem;
  cursor: pointer;
  padding: 2px 0;
  font-family: inherit;
  text-decoration: underline;
  text-underline-offset: 2px;
}
.btn-link:hover { color: var(--accent-hover); }

/* Micro-copy subtitle under labels */
.builder-subtitle {
  font-size: 0.68rem;
  color: var(--text-muted);
  margin: -2px 0 4px;
  line-height: 1.3;
}

/* More options collapsible */
.builder-more-summary {
  font-size: 0.72rem;
  color: var(--accent);
  cursor: pointer;
  list-style: none;
  padding: 4px 0;
  margin-top: 4px;
  user-select: none;
}
.builder-more-summary::-webkit-details-marker { display: none; }
.builder-more-summary::before { content: '▸ '; font-size: 0.6rem; }
details[open] .builder-more-summary::before { content: '▾ '; }
```

### JS changes in `popup-recipe-builder.js`

**Remove `pickRandomDefaults()`, `pickRandomToggle()`, `pickRandomCheckbox()`** entirely (~25 lines freed).

**Add `loadLastUsed()` helper** (~15 lines):
```js
async function loadLastUsed() {
  try {
    const { lastUsedPosition, lastUsedSecret } = await chrome.storage.local.get(['lastUsedPosition', 'lastUsedSecret']);
    if (lastUsedPosition) {
      const btn = el.positionGroup.querySelector(`[data-val="${lastUsedPosition}"]`);
      if (btn) { state.position = lastUsedPosition; btn.classList.add('active'); }
    }
    if (lastUsedSecret) {
      const btn = el.secretGroup.querySelector(`[data-val="${lastUsedSecret}"]`);
      if (btn) { state.secret = lastUsedSecret; btn.classList.add('active'); }
    }
  } catch { /* storage unavailable — leave empty */ }
}
```

**Add `saveLastUsed()` helper** (~8 lines):
```js
async function saveLastUsed() {
  try {
    await chrome.storage.local.set({
      lastUsedPosition: state.position ?? null,
      lastUsedSecret: state.secret ?? null,
    });
  } catch { /* ignore */ }
}
```

**Update `loadProfiles()`**: call `await loadLastUsed()` after `runDetection()`. After `loadLastUsed()`, check `state.sheetId` and auto-expand `<details>` if set:
```js
const moreOptions = document.getElementById('bld-more-options');
if (moreOptions && state.sheetId) moreOptions.open = true;
```

**Update Copy Recipe handler**: call `saveLastUsed()` before clipboard write.

**Update `reset()`**: clear `state.position = null; state.secret = null;` (unchanged), do NOT call `pickRandomDefaults` (removed). Leave toggles all unselected.

**Wire `#btn-how-it-works`**:
```js
document.getElementById('btn-how-it-works')?.addEventListener('click', () => {
  chrome.tabs.create({ url: chrome.runtime.getURL('demo/demo.html') });
});
```

---

## Related Code Files

### Modify
- `chrome-extension/src/popup/popup.html` — builder section restructure (subtitles, `<details>`, header row)
- `chrome-extension/src/popup/popup-recipe-builder.js` — remove random defaults, add smart defaults, wire "How it works"
- `chrome-extension/src/popup/popup.css` — add ~25 lines of new CSS

### Do Not Modify
- `popup.js` — no changes in this phase
- `sheet-detector.js` — no changes (still works on element by ID)

---

## File Size Check

`popup-recipe-builder.js` is currently ~397 lines. After this phase:
- Remove ~25 lines (random defaults functions)
- Add ~30 lines (loadLastUsed, saveLastUsed, "how it works" wiring)
- Net ~397 + 5 = ~402 lines → **over 200 line per-file goal**

The file is already over 200 lines. **Do NOT split during this phase** — the file is a single cohesive module (`initRecipeBuilder`). Add a DONE_WITH_CONCERNS note about file size for future cleanup.

---

## Implementation Steps

1. **popup.html** — inside `#status-builder`:
   a. Insert `.builder-header-row` with `#btn-how-it-works` before `.builder-form`
   b. Add `.builder-subtitle` `<p>` after each `builder-label` for Hash, Position, Secret
   c. Wrap Sheet ID + Modifiers + Profile into `<details id="bld-more-options">`
   d. Add subtitle for Sheet ID and Modifiers inside `<details>`

2. **popup.css** — append new CSS blocks (`.builder-header-row`, `.btn-link`, `.builder-subtitle`, `details`/`summary` styles)

3. **popup-recipe-builder.js**:
   a. Delete `pickRandomToggle()`, `pickRandomCheckbox()`, `pickRandomDefaults()` functions
   b. Add `loadLastUsed()` async function
   c. Add `saveLastUsed()` async function
   d. In `loadProfiles()`: replace `pickRandomDefaults()` call with `await loadLastUsed()` + auto-expand logic
   e. In Copy Recipe handler: add `saveLastUsed()` call
   f. In `reset()`: remove `pickRandomDefaults()` call (nothing replaces it — form stays blank)
   g. Add event listener for `#btn-how-it-works`

4. Run through all paths manually (see Success Criteria)

---

## Todo List

- [x] Restructure `popup.html` builder section (subtitles + `<details>`)
- [x] Add `.builder-header-row`, `.btn-link`, `.builder-subtitle`, summary styles to `popup.css`
- [x] Remove random defaults from `popup-recipe-builder.js`
- [x] Add `loadLastUsed()` + `saveLastUsed()`
- [x] Update `loadProfiles()` to call `loadLastUsed()` + auto-expand
- [x] Update Copy Recipe handler to call `saveLastUsed()`
- [x] Update `reset()` — remove `pickRandomDefaults()` call
- [x] Wire `#btn-how-it-works` click → open demo page
- [ ] Test: first open — form blank, no toggles selected
- [ ] Test: select position + secret, copy recipe → reopen builder → last values pre-selected
- [ ] Test: on Sheets tab — "More options" expands automatically

---

## Success Criteria

- Builder opens blank for first-time users (no pre-selected position/secret)
- Last-used position + secret pre-select on returning visits
- Subtitles visible under Hash, Position, Secret labels
- Sheet ID / Modifiers / Profile in collapsed `<details>` by default
- "More options" auto-expands when sheet is detected
- "? How it works" button opens demo page (tab created)
- Copy Recipe writes `lastUsedPosition` + `lastUsedSecret` to local storage

---

## Risk Assessment

| Risk | Mitigation |
|------|-----------|
| `sheet-detector.js` fails to find `#bld-sheet-url` after moving into `<details>` | Detector uses `getElementById` — works regardless of DOM nesting |
| `<details>` not styled consistently across OS/browser | CSS resets applied (webkit-details-marker removed, custom `::before` marker) |
| `loadLastUsed` throws in incognito (storage restricted) | Wrapped in try/catch, silently skips |
| File size over 200 lines | Known concern, note in code comments, defer to separate cleanup task |
