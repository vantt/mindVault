# Phase 05 — Service Worker + Content Script (Mismatch Propagation)

## Context Links
- `chrome-extension/src/service_worker.js` — message router
- `chrome-extension/src/content/content.js` — receives SW response, shows toast
- Phase 03 (throws `RecipeProfileMismatchError`)
- Parent plan: `plan.md`

## Overview
- **Priority:** P1
- **Status:** completed
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
- [x] Manual smoke: call new endpoint from devtools

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

## Unresolved
- Q2: warning propagation — adopted (SW forwards, content ignores, popup may render).
