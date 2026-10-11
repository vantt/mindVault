# Recipe Builder UI in Popup

**Status:** Done
**Created:** 2026-05-22 08:32; **original scope estimate:** Small (~200 LOC across 4 files)
## Goal
Add a form-based recipe builder inside the popup so users can compose a recipe string visually instead of typing it manually into a Google Sheet cell. Output is the recipe string to be copied and pasted into a Sheet.
## Design Decisions (from interview)
- **Preview mode:** debounced live (password rendered to verify recipe correctness)
- **Copy:** recipe string only (no copy-password — keeps Sheet as canonical source)
- **History:** one-shot, no persistence
- **Layout:** popup stays 300px wide, expands vertically as needed
## UX Flow
Unlock → Build Recipe → fill ingredient/position/spice/modifiers/own profile →300ms preview → display recipe and dish → Copy Recipe or Back. Recipe copy only; no history. Original optional masked toggle deferred.
## Recipe Format Reference
`<hash><position><secret_num>[modifiers]` — see [`recipe.js`](../../../chrome-extension/src/core/domain/recipe.js)
- position: `# $ @ % ^`
- secret_num: `1-5`
- modifiers: subset of `_ ! ? ~` (deduplicated, fixed apply order)
- _Note: version suffix `_vN` removed from grammar — rotate by changing hash text instead._
## Architecture
**No new use case.** Reuses `GeneratePassword` via service worker. Adds explicit-profile path because popup has no sheetId context.
`popup-builder → GENERATE_PASSWORD(profileName) → GeneratePassword.execute(recipe, null, profileName) → getSecretForGeneration(idx, null, profileName)`; explicit own profile loads directly, otherwise existing Menu routing.
**Why no shared profile support in builder:** Shared profiles are HKDF-bound to sheetId. Without sheet context, preview password would not match what the Sheet produces. → dropdown lists own profiles only.
## Files
Modify popup.html/css/js, service_worker.js, core/usecases/generate_password.js, adapters/infrastructure/chrome_storage_adapter.js and both locale JSON files. Create popup/popup-recipe-builder.js to isolate builder logic. All paths under `chrome-extension/src/`.
## Completion audit — 2026-10-12

Evidence: [completion verification](../261012-extension-completion/reports/verification.md): independent 147 units  + 22 full headed E2E, separate 8/8 real Google/native cases. Original cooking Golden tests unchanged.
Historical 2026-10-11 baseline `b5be3b0` found Phase3 Partial (invalid-form stale requests,455-line module) and Phase6 Pending (popup parity/modifier/profile/rapid-edit signoff). Those gaps are now closed by the current evidence; historical requirements remain below.

| Phase | Current status | Evidence / superseded mechanism |
|---|---|---|
| 1 Backend pipe | Done | Explicit own-profile path and real worker previews pass; DevTools-only smoke method Superseded, not claimed run. |
| 2 HTML/CSS | Done | Actual headed controls/rendering; current Precision styling supersedes original typography (644da28). |
| 3 Builder module | Done | Coherent snapshot/debounce, every-input invalidation, stale tag/Dish/detection/profile-load rejection and awaited Copy; 21 lifecycle + 4 locale units. Modules 73/81/69/37 lines, detector 42. |
| 4 Popup wiring | Done | Actual entry/Back/reset/lock/pagehide; popup.js 138 lines, under 200. |
| 5 i18n/polish | Done | Selected EN/VI warnings/errors visible; current locale loader supersedes direct Chrome locale. |
| 6 UI acceptance | Done | Actual headed 16 modifier combinations + 5 positions, own-profile switch/rapid edits/reset; real Google/native same Recipe/Dish parity and toolbar Copy. |

UI acceptance is headed automation through real extension controls, not an owner-manual signoff. Routed Menu fixtures are contract proof; 8/8 real anonymous Google cases separately establish provider/native acceptance. Optional masked preview remains a deferred out-of-scope display choice.
## Historical TODO with current implementation ticks
- [x] **Phase 1 — Backend pipe (profileName override)**
  - [x] Extend `getSecretForGeneration(idx, sheetId, profileName)` with profile-name path
  - [x] Extend `GeneratePassword.execute(recipeText, sheetId, profileName)` to forward param
  - [x] Extend `service_worker.js` `GENERATE_PASSWORD` handler to accept + forward `profileName`
  - [x] Backend smoke outcome verified; historical DevTools method Superseded (not claimed run): from devtools console send `{action:"GENERATE_PASSWORD", text:"abc$1", profileName:"Default"}` → verify response
- [x] **Phase 2 — Popup HTML/CSS**
  - [x] Add "Build Recipe" button to `status-unlocked` block
  - [x] Add new `status-builder` section (form: hash input, position toggle group, secret toggle group, modifier checkbox row, profile dropdown, recipe preview, password preview, Copy Recipe + Back buttons)
  - [x] CSS: `.toggle-group` (segmented buttons), `.modifier-row` (inline checkboxes), `.preview-block` (monospace box), reuse existing button/input styles
- [x] **Phase 3 — popup-recipe-builder.js**
  - [x] List own profiles into dropdown (filter `profile:*` keys from sync storage)
  - [x] Live recipe-string assembly on any form change (synchronous)
  - [x] Debounced (300ms) password preview via `chrome.runtime.sendMessage`
  - [x] Handle invalid recipe states (hash empty, secret not selected) → blank preview, no SW call
  - [x] Copy Recipe → `navigator.clipboard.writeText` + "Copied!" feedback
  - [x] Back → reset state, switch back to `status-unlocked`
- [x] **Phase 4 — popup.js wiring**
  - [x] Import + init builder module after auth check
  - [x] Wire "Build Recipe" button → show `status-builder`
- [x] **Phase 5 — i18n + polish**
  - [x] Add keys: `btnBuildRecipe`, `builderHash`, `builderPosition`, `builderSecret`, `builderModifiers`, `builderProfile`, `builderRecipeOutput`, `builderPasswordPreview`, `btnCopyRecipe`, `btnBuilderBack`
  - [x] EN + VI translations
- [x] **Phase 6 — Manual QA (outcomes verified by current headed UI + real Google acceptance)**
  - [x] Build a recipe matching an existing Sheet recipe → preview password must match
  - [x] Toggle every modifier combination → recipe string updates correctly
  - [x] Profile switching changes preview password
  - [x] Rapid toggling does not spam SW (debounce works)
## Risks
- **Argon2 cost** (~500ms-1s per derive) × debounce — if user toggles fast, queue may stall. Mitigation: cancel pending preview when new input arrives (latest-wins).
- **Profile dropdown stale** if user creates new profile in options while builder is open. Acceptable: dropdown loads at builder open, user can close+reopen.
- **popup.js growing** — already 152 lines. Builder logic externalized to keep under 200.
## Out of Scope
Shared profile support (requires sheetId binding); recipe history/favorites; copy generated password; inline-in-Sheet UI (separate future work); recipe parsing from clipboard (reverse direction).
## Success Criteria
- User can build a recipe in popup and produce a string identical to what they would type manually
- Preview password matches what the Sheet produces for the same recipe + profile
- No regression in existing decode flow (`GENERATE_PASSWORD` without `profileName` works as before)
- popup.js stays < 200 LOC after change
