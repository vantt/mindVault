# Phase 05 — Service Worker + Content Script (Mismatch Propagation)

## Completion audit — 2026-10-12

- Current status: **Done**; evidence: [completion verification](../../../plans/261012-extension-completion/reports/verification.md) (independent 147 units  + 22 full headed E2E; real Google/native 8/8 separately).
- Stable mismatch codes now reach localized Builder/content UI; content also displays legacy warnings. EN/VI wrong-Menu/wrong-profile refusal and no-Dish assertions pass headed tests; real Google/native tagged match and profile refusal pass.
- Original criteria and prior audit are retained below. Headed automation exercises the actual extension UI; neither routed fixtures nor SW-only checks are presented as live-provider or human-manual signoff.
- Superseded mechanism: string-matched raw errors/ignored legacy warnings; current stable localized codes and visible legacy warning fulfill the outcome. DevTools-only smoke is replaced by actual endpoint/UI acceptance.

## Historical relevance audit — 2026-10-11

- Audit status at baseline: **Partial**; original recorded status: `completed`.
- Baseline: main `b5be3b0`; historical checklist/criteria below are retained. This was the baseline audit; the 2026-10-12 audit above governs current status. Old ticks alone did not certify acceptance.
- Evidence / remaining: SW contracts and localized content mismatch implemented (chrome-extension/src/service_worker.js:34–100; src/content/content.js:103). Actual wrong-Menu toast/manual flow remains relevant and unverified; SW-only E2E does not render the toast. Original DevTools smoke is a verification method, not another feature.
- Evidence paths above are repository-root-relative; source paths beginning src/ are inside chrome-extension/. No source/test changes or new runtime verification in this audit.

## Context Links
- `chrome-extension/src/service_worker.js` — message router
- `chrome-extension/src/content/content.js` — receives SW response, shows toast
- Phase 03 (throws `RecipeProfileMismatchError`)
- Parent plan: `plan.md`

## Overview
- **Priority:** P1
- **Status:** Done
- **Description:** Surface mismatch error with stable code `RECIPE_MISMATCH`; route to i18n message in content-script toast and popup-builder preview. Also add new `COMPUTE_RECIPE_TAG` SW endpoint for popup builder (Phase 06).

## Key Insights
- Existing handler returns `{success: false, error: e.message}` — opaque string. Need to add `code` field for clients to discriminate.
- Content script already shows toast; just needs to swap message based on error code.
- New `COMPUTE_RECIPE_TAG` endpoint encapsulates raw-secret access — popup never sees raw secret, just gets back the 4-char tag.

## Requirements
**Functional**
- SW `GENERATE_PASSWORD`:
  - On `RecipeProfileMismatchError` → respond `{success:false, error, code:"RECIPE_MISMATCH"}`.
  - On other errors → `{success:false, error, code:"GENERIC"}` (or absent).
  - Success path: forward optional `warning` from use case.
- New SW `COMPUTE_RECIPE_TAG` handler:
  - Input: `{action:"COMPUTE_RECIPE_TAG", recipeFields:{hash,position,secretIndex,modifiers}, sheetId, profileName}`.
  - Loads raw secret via `storage.getSecretForGeneration(index, null, profileName)` → use `result.rawSecret` (own profiles never bind per Option B; passing `sheetId=null` also unbinds shared).
  - Calls `computeRecipeTag({...recipeFields, secret:rawSecret, sheetId})`.
  - Responds `{success:true, tag}`.
- Content script: on `code === "RECIPE_MISMATCH"` → show i18n message (Phase 07 key `errRecipeMismatch`).

**Non-functional**
- SW file stays organized; extract handler if grows >200 LOC (currently 109).

## Architecture
```
content.js ──GENERATE_PASSWORD──▶ SW ──▶ usecase
                                  ◀── {success, password|error, code, warning?}

popup-builder ──COMPUTE_RECIPE_TAG──▶ SW ──▶ storage.getRawSecret + tag-codec
                                       ◀── {success, tag}
```

## Related Code Files
**Modify:**
- `chrome-extension/src/service_worker.js`
- `chrome-extension/src/content/content.js`

## Implementation Steps
1. In `service_worker.js`:
   - Import `RecipeProfileMismatchError` from `core/domain/recipe-errors.js`.
   - Import `computeRecipeTag` from `core/domain/tag-codec.js`.
   - `handleGeneratePassword`:
     ```js
     try {
       const result = await generatePasswordUseCase.execute(text, sheetId, profileName);
       sendResponse({ success: true, ...result }); // includes warning if set
     } catch (e) {
       const code = e instanceof RecipeProfileMismatchError ? "RECIPE_MISMATCH" : "GENERIC";
       sendResponse({ success: false, error: e.message, code });
     }
     ```
   - Add new handler:
     ```js
     if (request.action === "COMPUTE_RECIPE_TAG") {
       handleComputeRecipeTag(request, sendResponse);
       return true;
     }
     ```
   - `handleComputeRecipeTag({recipeFields, sheetId, profileName}, sendResponse)`:
     - Validate inputs (sheetId non-empty, profileName non-empty, fields valid).
     - `const r = await storageAdapter.getSecretForGeneration(recipeFields.secretIndex, null, profileName);`
     - `if (!r) → respond {success:false, error:"Secret not found"};`
     - `const tag = await computeRecipeTag({...recipeFields, secret: r.rawSecret, sheetId});`
     - Respond `{success:true, tag}`.
2. In `content.js`:
   - In `generateAndShow` error branch, check `response.code === "RECIPE_MISMATCH"` → use i18n `errRecipeMismatch`.
   - Other codes fall through to existing string-match heuristics.

## Todo List
- [x] SW: import error class + tag codec
- [x] SW: attach `code` to error responses
- [x] SW: add `COMPUTE_RECIPE_TAG` handler
- [x] Content: branch on `code === "RECIPE_MISMATCH"` → i18n message
- [x] Endpoint smoke outcome: headed Builder requests actual tag endpoint; DevTools-only method Superseded (not claimed run).

## Success Criteria
- Smoke test: paste tagged recipe in correct sheet → password generates; paste in wrong-bound sheet → toast shows mismatch i18n message.
- COMPUTE_RECIPE_TAG returns 4-char string for valid input.

## Risk Assessment
- **R1:** Locked vault during COMPUTE_RECIPE_TAG → `getDecryptedProfile` throws. Mitigation: surface `error:"Vault locked"` cleanly.
- **R2:** Sender forges `code` field — N/A, comes from SW, content script consumer is trusted.

## Security Considerations
- `COMPUTE_RECIPE_TAG` only reachable by extension scripts (popup) — `chrome.runtime.sendMessage` is intra-extension.
- Raw secret stays inside SW; only tag (20 bits) crosses to popup.

## Next Steps
- Phase 06 consumes `COMPUTE_RECIPE_TAG` from popup builder.
- Phase 07 adds `errRecipeMismatch` i18n key.

## Historical unresolved questions
- Q2: warning propagation — adopted (SW forwards, content ignores, popup may render).

## Current unresolved questions

None. Resolved: legacy warning propagates and is displayed by content/Builder; historical ignore-content suggestion Superseded.
