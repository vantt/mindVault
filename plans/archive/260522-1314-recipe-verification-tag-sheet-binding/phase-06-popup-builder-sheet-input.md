# Phase 06 — Popup Builder UI (Target Sheet URL + Tag Inclusion)

## Context Links
- `chrome-extension/src/popup/popup-recipe-builder.js` — current builder logic
- `chrome-extension/src/popup/popup.html` — `status-builder` section
- `chrome-extension/src/popup/popup.css`
- `chrome-extension/src/popup/popup.js`
- Phase 05 (`COMPUTE_RECIPE_TAG` SW endpoint)
- Phase 07 (i18n keys)
- Parent plan: `plan.md`

## Overview
- **Priority:** P1
- **Status:** completed
- **Description:** Add "Target Sheet URL" text input above existing form. Parse sheetId via regex `/\/spreadsheets\/d\/([a-zA-Z0-9_-]+)/`. Auto-fill from active tab if Google Sheets URL. When sheetId valid → compute tag via `COMPUTE_RECIPE_TAG` → append `.<tag>` to recipe output. When blank → output legacy recipe + show warning.

## Key Insights
- Tag must be computed AFTER all other form changes, sharing the same debounce cycle as password preview.
- Latest-wins token pattern (already used) extends naturally: token guard around tag computation too.
- Preview password for **own profiles is identical** with or without tag (Option B — no HKDF binding). Sending `sheetId` for tagged recipes is only needed so the use case can run tag-verify; password value does NOT change.
- For **shared profiles**, sending `sheetId` does trigger PRD §2.4 HKDF binding (unchanged behavior). The popup builder cannot tell own vs shared at the UI layer — it just sends `sheetId` whenever a tag is computed; the use case + storage adapter handle the branch.
- If sheetId blank → recipe = legacy (no tag), preview uses no sheetId — matches current behavior.

## Requirements
**Functional**
- New input field `bld-sheet-url` above hash field. Placeholder e.g. `https://docs.google.com/spreadsheets/d/...`.
- Auto-fill: on builder open, `chrome.runtime.sendMessage({action:"GET_SHEET_ID_FROM_ACTIVE_TAB"})` → if `tabUrl` matches Sheets URL, prefill.
- Live parse: extract sheetId from input on each `input` event.
- Recipe output:
  - sheetId valid + form complete → request tag from SW → append `.<tag>` → display.
  - sheetId blank + form complete → legacy recipe (no tag), show warning text.
  - sheetId provided but invalid (no regex match) → show inline error, no recipe rendered.
- Preview password:
  - When tag present → send `GENERATE_PASSWORD` with `text=<tagged-recipe>, sheetId, profileName`.
  - When no tag → send with `sheetId=null, profileName` (current behavior).

**Non-functional**
- Keep `popup-recipe-builder.js` <250 LOC (currently 210). Extract helpers if needed.

## Architecture
```
form change → debounce 300ms →
  parse sheetId →
    if valid + form complete:
      COMPUTE_RECIPE_TAG → append .tag → render recipe
      GENERATE_PASSWORD (with sheetId+profileName) → render password
    else if blank + form complete:
      render legacy recipe + warning
      GENERATE_PASSWORD (no sheetId) → render password
    else:
      render placeholder, no SW calls
```

## Related Code Files
**Modify:**
- `chrome-extension/src/popup/popup-recipe-builder.js`
- `chrome-extension/src/popup/popup.html` (add input, warning span)
- `chrome-extension/src/popup/popup.css` (input style + warning style)
- `chrome-extension/src/popup/popup.js` (no major change; verify init order)

## Implementation Steps
1. **HTML** — inside `#status-builder .builder-form`, before hash label:
   ```html
   <label class="builder-label" data-i18n="builderSheetUrl">Target Sheet URL</label>
   <input type="text" id="bld-sheet-url" autocomplete="off" spellcheck="false" />
   <p id="bld-sheet-url-error" class="builder-error hidden"></p>
   <p id="bld-sheet-warning" class="builder-warning hidden" data-i18n="warnNoSheetUrl">
     Without target sheet, this recipe won't verify on decode.
   </p>
   ```
2. **CSS** — add `.builder-warning` (subtle amber).
3. **Builder JS** — extend `state` with `sheetId: null`:
   - On builder show, prefill via `GET_SHEET_ID_FROM_ACTIVE_TAB`.
   - Wire `input` event on `#bld-sheet-url`:
     - Empty → `state.sheetId = null`, hide error, show warning.
     - Match → `state.sheetId = match[1]`, hide error+warning.
     - No match → `state.sheetId = null`, show error (i18n `errInvalidSheetUrl`).
     - Trigger `onFormChange()`.
   - `buildRecipeString()` returns base recipe (no tag).
   - New async `assembleTaggedRecipe(base, recipe)`:
     - If `!state.sheetId` → return base (no tag).
     - Send `COMPUTE_RECIPE_TAG` with `{recipeFields:{hash,position,secretIndex,modifiers}, sheetId:state.sheetId, profileName}`.
     - On success → return `${base}.${tag}`; on fail → return base + flag warning.
   - In `requestPreview`:
     - Build base; call `assembleTaggedRecipe`; update `el.recipeOut`.
     - Send `GENERATE_PASSWORD` with `text=taggedRecipe, sheetId:state.sheetId, profileName`.
     - On `code === "RECIPE_MISMATCH"` (shouldn't happen in builder; safety net) → render mismatch warning.
4. **Latest-wins:** all async branches guarded by `myToken !== previewToken`.

## Todo List
- [x] Add `bld-sheet-url` input + error/warning markup
- [x] Add `.builder-warning` CSS
- [x] Extend builder state with sheetId
- [x] Prefill from active tab on open
- [x] Live-parse on input event with inline error + warning toggle
- [x] `assembleTaggedRecipe` helper using `COMPUTE_RECIPE_TAG`
- [x] Wire tagged recipe + tagged-preview generation
- [x] Reset includes clearing sheetId + warning UI
- [x] Token-guard all SW round-trips

## Success Criteria
- Builder opens on a Sheets tab → sheet URL field auto-filled.
- Form complete + sheet URL valid → recipe output has `.xxxx` suffix.
- Sheet URL blank → recipe output without tag, warning visible.
- Invalid URL pasted → red error message, no recipe shown.
- Copy Recipe copies the full tagged string verbatim.

## Risk Assessment
- **R1:** Race: user changes profile while tag in-flight → stale tag rendered. Mitigation: token-guard.
- **R2:** Auto-fill annoyance if user wants different sheet. Mitigation: field editable, prefill is just default.
- **R3:** Popup width 300px — sheet URL input may overflow visually. Mitigation: existing `input { width:100% }` rule already covers; add `text-overflow:ellipsis` on input when not focused.

## Security Considerations
- Tag computation goes through SW endpoint → popup never sees raw secret.
- URL parsing uses anchored regex — no DOM injection risk (input is `type=text`, never injected as HTML).

## Next Steps
- Phase 07 adds i18n keys.
- Phase 08 covers unit-test of `assembleTaggedRecipe` logic (mock SW).

## Unresolved
- Q3: Auto-fill toggle? Plan: default-on, no toggle (simpler). Confirm.
- Q6: Show preview when sheet URL invalid but form otherwise complete? Plan: no preview (block on URL fix). Confirm.
