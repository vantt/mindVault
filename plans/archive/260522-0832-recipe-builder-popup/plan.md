# Recipe Builder UI in Popup

**Status:** Planned
**Created:** 2026-05-22 08:32
**Scope:** Small (~200 LOC across 4 files)

## Goal
Add a form-based recipe builder inside the popup so users can compose a recipe string visually instead of typing it manually into a Google Sheet cell. Output is the recipe string to be copied and pasted into a Sheet.

## Design Decisions (from interview)
- **Preview mode:** debounced live (password rendered to verify recipe correctness)
- **Copy:** recipe string only (no copy-password — keeps Sheet as canonical source)
- **History:** one-shot, no persistence
- **Layout:** popup stays 300px wide, expands vertically as needed

## UX Flow
1. User unlocks → `status-unlocked` shows.
2. New button "Build Recipe" → switches to `status-builder`.
3. User fills form (hash text, position toggle, secret toggle, modifier checkboxes, profile dropdown).
4. On any change → debounced 300ms → assemble recipe string + send to SW for password preview.
5. Display recipe string (read-only, monospace) + password preview (monospace, masked toggle optional later).
6. "Copy Recipe" copies recipe string. "Back" returns to `status-unlocked`.

## Recipe Format Reference
`<hash><position><secret_num>[modifiers]` — see [`recipe.js`](../../chrome-extension/src/core/domain/recipe.js)
- position: `# $ @ % ^`
- secret_num: `1-5`
- modifiers: subset of `_ ! ? ~` (deduplicated, fixed apply order)
- _Note: version suffix `_vN` removed from grammar — rotate by changing hash text instead._

## Architecture
**No new use case.** Reuses `GeneratePassword` via service worker. Adds explicit-profile path because popup has no sheetId context.

```
popup-builder UI (form)
   ↓ debounced 300ms
service_worker (GENERATE_PASSWORD with new optional `profileName`)
   ↓
GeneratePassword.execute(recipeText, sheetId=null, profileName)
   ↓
ChromeStorageAdapter.getSecretForGeneration(idx, sheetId, profileName)
   → if profileName: load profile:<name> directly (own profiles only)
   → else: existing sheet-routing logic (unchanged)
```

**Why no shared profile support in builder:** Shared profiles are HKDF-bound to sheetId. Without sheet context, preview password would not match what the Sheet produces. → dropdown lists own profiles only.

## Files
**Modify:**
- `chrome-extension/src/popup/popup.html` — add `status-builder` section + "Build Recipe" button in unlocked state
- `chrome-extension/src/popup/popup.css` — styles for form (toggle buttons, checkboxes, output area)
- `chrome-extension/src/popup/popup.js` — state wiring, form handlers, debounce, SW call
- `chrome-extension/src/service_worker.js` — accept optional `profileName` in GENERATE_PASSWORD
- `chrome-extension/src/core/usecases/generate_password.js` — pass `profileName` through
- `chrome-extension/src/adapters/infrastructure/chrome_storage_adapter.js` — `getSecretForGeneration(idx, sheetId, profileName)` honors `profileName` override
- `chrome-extension/src/_locales/en/messages.json` + `vi/messages.json` — new i18n keys

**Create:**
- `chrome-extension/src/popup/popup-recipe-builder.js` — extracted builder logic (keeps popup.js < 200 LOC). Imports from popup.js as a module; exports `initBuilder(elements, sendPreview)`.

## TODO
- [ ] **Phase 1 — Backend pipe (profileName override)**
  - [ ] Extend `getSecretForGeneration(idx, sheetId, profileName)` with profile-name path
  - [ ] Extend `GeneratePassword.execute(recipeText, sheetId, profileName)` to forward param
  - [ ] Extend `service_worker.js` `GENERATE_PASSWORD` handler to accept + forward `profileName`
  - [ ] Smoke test: from devtools console send `{action:"GENERATE_PASSWORD", text:"abc$1", profileName:"Default"}` → verify response
- [ ] **Phase 2 — Popup HTML/CSS**
  - [ ] Add "Build Recipe" button to `status-unlocked` block
  - [ ] Add new `status-builder` section (form: hash input, position toggle group, secret toggle group, modifier checkbox row, profile dropdown, recipe preview, password preview, Copy Recipe + Back buttons)
  - [ ] CSS: `.toggle-group` (segmented buttons), `.modifier-row` (inline checkboxes), `.preview-block` (monospace box), reuse existing button/input styles
- [ ] **Phase 3 — popup-recipe-builder.js**
  - [ ] List own profiles into dropdown (filter `profile:*` keys from sync storage)
  - [ ] Live recipe-string assembly on any form change (synchronous)
  - [ ] Debounced (300ms) password preview via `chrome.runtime.sendMessage`
  - [ ] Handle invalid recipe states (hash empty, secret not selected) → blank preview, no SW call
  - [ ] Copy Recipe → `navigator.clipboard.writeText` + "Copied!" feedback
  - [ ] Back → reset state, switch back to `status-unlocked`
- [ ] **Phase 4 — popup.js wiring**
  - [ ] Import + init builder module after auth check
  - [ ] Wire "Build Recipe" button → show `status-builder`
- [ ] **Phase 5 — i18n + polish**
  - [ ] Add keys: `btnBuildRecipe`, `builderHash`, `builderPosition`, `builderSecret`, `builderModifiers`, `builderProfile`, `builderRecipeOutput`, `builderPasswordPreview`, `btnCopyRecipe`, `btnBuilderBack`
  - [ ] EN + VI translations
- [ ] **Phase 6 — Manual QA**
  - [ ] Build a recipe matching an existing Sheet recipe → preview password must match
  - [ ] Toggle every modifier combination → recipe string updates correctly
  - [ ] Profile switching changes preview password
  - [ ] Rapid toggling does not spam SW (debounce works)

## Risks
- **Argon2 cost** (~500ms-1s per derive) × debounce — if user toggles fast, queue may stall. Mitigation: cancel pending preview when new input arrives (latest-wins).
- **Profile dropdown stale** if user creates new profile in options while builder is open. Acceptable: dropdown loads at builder open, user can close+reopen.
- **popup.js growing** — already 152 lines. Builder logic externalized to keep under 200.

## Out of Scope
- Shared profile support (requires sheetId binding)
- Recipe history / favorites
- Copy generated password
- Inline-in-Sheet UI (separate future work)
- Recipe parsing from clipboard (reverse direction)

## Success Criteria
- User can build a recipe in popup and produce a string identical to what they would type manually
- Preview password matches what the Sheet produces for the same recipe + profile
- No regression in existing decode flow (`GENERATE_PASSWORD` without `profileName` works as before)
- popup.js stays < 200 LOC after change
