# Phase02 — Builder latest-input ownership

Current acceptance2026-10-12: Done in the scope below.
[Combined evidence](reports/verification.md) records147 unit/25 distinct headed
E2E and8 actual Google/native cases, proof boundaries and independent review.
Original pre-repair observations and conditional verification paths remain below
as history; a checked criterion means its current applicable outcome was resolved,
not that every historical method/conditional branch was separately executed.


Status: Done. Priority:2. Existing plan: `archive/plans/260522-0832-recipe-builder-popup/`.
Crypto policy settled; final browser QA integrates security changes.

## Source and accepted boundaries

`chrome-extension/src/popup/popup-recipe-builder.js` is455 lines. Invalid form
returns before clearing debounce; token increments only when a request starts.
Tag/preview can therefore publish stale data after input becomes invalid or before
the next debounce starts. Preview reads mutable sheet/profile/form state at different
times. reset guards running preview, but loadProfiles/detection can still complete late.
Preserve own profiles only, recipe-only Copy,300ms debounce, current random position/
spice defaults, collapsed options and current grammar/modifier order.

## Implementation TODOs

- [x] Extract focused form/preview helpers, each under200 lines; retain public
  loadProfiles/reset interface and existing popup controls/styles.
- [x] Invalidate generation and clear pending timer on EVERY form change before
  validation. Snapshot base recipe, modifiers, own profile and sheetId together.
- [x] Check generation after tag and dish responses, before publication or Copy.
  Profile/sheet changes, invalid form, Back/reset/close and async load/detection
  completion cannot restore a stale recipe/dish. Disable Copy while unresolved.
- [x] With target sheet selected, tag failure is explicit and blocks publication/
  copy; never silently fall back to an untagged recipe. Without sheet context,
  preserve intentional untagged path with the existing warning.
- [x] Do not restore superseded stored-pick behavior or introduce copy-password,
  history/shared-profile authoring. Remove obsolete last-pick writes only if unused.
- [x] Use existing localized error contracts; handle disconnected runtime safely
  without exposing exception details or falsely displaying Copied on failed write.

## Verification

- [x] New `test/unit/builder-lifecycle.spec.mjs`: valid→invalid before debounce,
  delayed tag/dish→invalid, old profile/sheet result during new debounce, Back→reopen,
  stale profile load/detection, missing profile, failed tag and rejected copy.
  Use controllable unresolved promises and real DOM form events; test behavior.
- [x] New `test/e2e/builder-lifecycle.spec.ts`: actual extension form to real SW
  kitchen, known synthetic dish, own-profile routing and modifier combinations.
  Rapid form changes publish final input only; invalid/reset screens stay blank.
- [x] Preserve all approved cooking/parser/tag tests. UI output must agree with
  unchanged kitchen; no changed expected dishes to accommodate implementation.

Reviewed repair, independent unit/headed UI proof and Phase04 real Google parity
are complete. Local form tests are still distinguished from provider proof.
