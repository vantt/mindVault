# Phase 03 — Generation Use Case (Verify + Mismatch Throw)

## Context Links
- `chrome-extension/src/core/usecases/generate_password.js`
- `docs/recipe-tag-design-rationale.md` — **Option B decision (no HKDF for own profiles)**
- Phase 01 (`tag-codec.js`, `RecipeProfileMismatchError`)
- Phase 04 (storage helper exposes raw + effective secret + sheetId)
- Parent plan: `plan.md`

## Overview
- **Priority:** P1
- **Status:** completed
- **Description:** When `recipe.tag` present → verify via HMAC; mismatch → throw `RecipeProfileMismatchError`. **Tag NEVER changes the password value for own profiles** (Option B — preserves Cookbook insight). Generation secret path for own profiles is identical to v2.1. Shared profiles unchanged (PRD §2.4 HKDF still applies).

## Key Insights
- **Option B (no HKDF for own profiles):** Password value identical with/without tag for own profiles. Tag is pure verification metadata. User can still "cook" password manually: `S_i + hash`.
- Two distinct secret references in flight:
  - `rawSecret` — `secretObj.base` decrypted, used as HMAC key for tag verify AND as the generation secret for own profiles.
  - `effectiveSecret` — equals `rawSecret` for own profiles (always); equals `HKDF(rawSecret, sheetId)` for shared profiles when sheetId present (PRD §2.4, unchanged).
- Tag verify runs **before** modifier transforms. Verify uses `rawSecret`.
- Mismatch detection is the **only** failure mechanism for own-profile tagged recipes. No silent password divergence — tag catches everything.

## Requirements
**Functional**
- `execute(recipeText, sheetId, profileNameOverride)`:
  - Parse → if fail, throw `Error("Invalid recipe format")` (unchanged).
  - Load via `storage.getSecretForGeneration(index, sheetId, profileNameOverride)`.
  - If `recipe.tag`:
    - Require `sheetId` (from storage result or override); if missing, throw `RecipeProfileMismatchError("Tag requires sheet context")`.
    - `verifyRecipeTag(recipe, result.rawSecret, result.sheetId)` → if false, throw `RecipeProfileMismatchError`.
  - Apply modifiers + combine over `effectiveSecret` (= raw for own, bound for shared).
- Return shape unchanged + optional `warning: "legacy_no_tag"` when tag absent and sheetId present.

**Non-functional**
- Keep file <100 LOC (currently 66) — minimal additions.

## Architecture
```
execute(text, sheetId, profileOverride)
  ↓ parse
  ↓ storage.getSecretForGeneration(idx, sheetId, override)
     → returns {rawSecret, effectiveSecret, profileName, isShared, settings, sheetId}
       (own: effectiveSecret === rawSecret; shared+sheetId: effectiveSecret = HKDF(...))
  ↓ if recipe.tag → verifyRecipeTag(recipe, rawSecret, sheetId) → mismatch ⇒ throw
  ↓ apply modifiers on effectiveSecret
  ↓ combine
  ↓ return {password, profileName, isShared, settings, warning?}
```

## Related Code Files
**Modify:**
- `chrome-extension/src/core/usecases/generate_password.js`

**Read (for context):**
- `chrome-extension/src/core/domain/tag-codec.js` (Phase 01)
- `chrome-extension/src/adapters/infrastructure/chrome_storage_adapter.js` (Phase 04)

## Implementation Steps
1. Import `verifyRecipeTag` from `../domain/tag-codec.js`.
2. Import `RecipeProfileMismatchError` from `../domain/recipe-errors.js`.
3. Update `execute`:
   - Call `storage.getSecretForGeneration(index, sheetId, profileNameOverride)` (no `tagAware` flag — Option B removed it).
   - Destructure `rawSecret, effectiveSecret, profileName, isShared, settings, sheetId: resolvedSheetId` from result.
   - If `recipe.tag`:
     - If `!resolvedSheetId` → throw new `RecipeProfileMismatchError("Recipe requires sheet context")`.
     - `const ok = await verifyRecipeTag(recipe, rawSecret, resolvedSheetId);`
     - If `!ok` → throw new `RecipeProfileMismatchError()`.
   - Set `let secret = effectiveSecret;` then apply modifiers as today.
4. Build return obj; if `!recipe.tag && resolvedSheetId` → add `warning: "legacy_no_tag"`.

## Todo List
- [x] Import tag-codec + error class
- [x] Destructure new fields (rawSecret, effectiveSecret, sheetId)
- [x] Verify tag when present, throw on mismatch
- [x] Throw early if tag present but sheetId missing
- [x] Add `warning` field for legacy + sheet context
- [x] Confirm own-profile tagged generation produces same password as untagged

## Success Criteria
- Existing golden tests still pass (no tag → no behavior change).
- New tests: own-profile tagged recipe at correct sheetId → password equals untagged equivalent (Cookbook insight preserved).
- New tests: tag-wrong throws `RecipeProfileMismatchError`; no-tag-with-sheet returns warning.
- Shared-profile flow (PRD §2.4 HKDF) unchanged.

## Risk Assessment
- **R1:** Storage shape change breaks mock storage in existing tests. Mitigation: Phase 08 updates mock to new shape (`rawSecret`, `effectiveSecret`); for own profiles `rawSecret === effectiveSecret` so legacy asserts hold.
- **R2:** Caller forgets to pass sheetId for tagged recipe → explicit mismatch error (intentional; better than silent wrong password).

## Security Considerations
- Tag verify uses `rawSecret` not derived; HMAC key fixed regardless of binding path.
- Constant-time equality not needed for 20-bit local check.
- No new exfiltration path — all data already in-process.
- Tag preimage-resistant: 20-bit truncation prevents brute-force secret recovery.

## Next Steps
- Phase 04 implements storage helper to return raw + effective secrets and resolved sheetId.

## Unresolved
- Q2: Should `warning` propagate to content script or stay in SW logs only? Plan: include in SW response; content script ignores; popup builder can render. Confirm in Phase 05.
