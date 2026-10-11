# Phase03 — Tag error/warning presentation

Current acceptance2026-10-12: Done in the scope below.
[Combined evidence](reports/verification.md) records147 unit/25 distinct headed
E2E and8 actual Google/native cases, proof boundaries and independent review.
Original pre-repair observations and conditional verification paths remain below
as history; a checked criterion means its current applicable outcome was resolved,
not that every historical method/conditional branch was separately executed.


Status: Done. Priority:3. Baseline tag phases05/06/07/09 were Partial;
current acceptance closes them. Domain/parser/cooking/storage/units/docs were
already Done. Depends on Phase02; original plan is now under archive/plans/.

## Source and scope

- `chrome-extension/src/service_worker.js`: stable RECIPE_MISMATCH code and warning
  exist; opaque/safe error contract must survive extraction for security.
- `src/content/content.js`: mismatch uses chrome.i18n browser locale while popup
  uses user-selected runtime locale. Retained hotkey content UI is current product.
- `src/popup/popup-recipe-builder.js`: raw response.error display and tag failure
  fallback require coherent code-to-message routing after lifecycle repair.
- `src/core/usecases/generate_password.js` and `core/domain/tag-codec.js` remain
  unchanged: tag is verification metadata, own dish value unchanged; shared HKDF
  binding preserved. Historical unanswered prompts do not override current choices.

## Implementation TODOs

- [x] Reuse bounded localized message helper for stable mismatch/locked/missing-
  seasoning/runtime failures. Render text safely, never exception/provider details.
- [x] Content presentation honors selected EN/VI locale with explicit safe fallback;
  no broad exposed secret/profile diagnostic. Do not import ESM directly into
  current classic content-script entry without compatible wiring.
- [x] Complete existing missing-sheet/legacy-untagged and unmapped-profile warning
  outcomes in their intended UI. Preserve generation for explicitly allowed legacy
  recipes; tagged mismatch refuses and displays no dish/copyable stale output.
- [x] Keep mapped profile alignment and default-on sheet detection; sheet-input
  ambiguity/error and tag failure are visible. No new product choice for alphabet,
  tag length, canonical modifiers, own-profile derivation or forced rotation.
- [x] Complete required EN/VI messages and current UI specs/plan criteria.

## Verification

- [x] New unit UI-contract cases: selected-locale mismatch, warning mapping,
  denied/missing context, HTML-shaped errors rendered as text, no stale dish.
- [x] New `test/e2e/tag-ui.spec.ts`: builder creates real tag from synthetic profile,
  route fixture to different sheet/profile, drive actual content UI/toast and popup
  preview, assert localized refusal and absent dish. Observe matching tag success
  and legacy warning separately. Direct SW response alone is insufficient.
- [x] Existing tag/parser/approved cooking suite stays unchanged. New harness uses
  actual manifest content injection; no `chrome.scripting` permission/test shortcut.

Fixture-hosted browser tests prove extension UI contracts, not actual Google DOM
or native shortcut delivery. Final Phase04 records those provider-specific limits.
