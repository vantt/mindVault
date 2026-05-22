# Phase 04 — Storage Adapter (Expose Raw + Effective Secret + sheetId)

## Context Links
- `chrome-extension/src/adapters/infrastructure/chrome_storage_adapter.js` — current `_bindSecretToSheet` + `getSecretForGeneration`
- `docs/prd/v2-multi-sheet-profiles.md` §2.4 (sheet-bind shared profiles only — unchanged)
- `docs/recipe-tag-design-rationale.md` — **Option B (no HKDF for own profiles)**
- Phase 03 (use case consumes new shape)
- Parent plan: `plan.md`

## Overview
- **Priority:** P1
- **Status:** completed
- **Description:** Extend `getSecretForGeneration` to (a) return both `rawSecret` and `effectiveSecret`, (b) expose resolved `sheetId` in return value. **Own profiles never bind** (Option B — preserves manual rebuildability). Shared profiles still bind to sheetId when present (PRD §2.4, unchanged).

## Key Insights
- **Option B (no HKDF for own profiles):** `effectiveSecret === rawSecret` for all own profiles regardless of tag. Tag is verification-only — does not alter secret derivation. This preserves the README "Cookbook" insight: user can manually compute `S_i + hash`.
- Shared profiles: HKDF binding when sheetId present (existing PRD §2.4 logic, **not modified**).
- `rawSecret` always = `secretObj.base` (decrypted). Exposed in return for:
  1. HMAC tag verify (Phase 03 use case).
  2. SW `COMPUTE_RECIPE_TAG` endpoint (Phase 05) — popup builder requests tag without ever seeing the raw secret directly.
- No `tagAware` flag needed in storage signature — own/shared decides everything.
- **Design choice — keep `effectiveSecret` field:** Even though `effectiveSecret === rawSecret` for own profiles, keep both fields in the return shape. Reasons: (1) callers stay unaware of own-vs-shared branching, (2) symmetric naming with shared path, (3) future-proofs if more derivation paths added. Test impact minimized — mocks set both equal.

## Requirements
**Functional**
- Signature: `getSecretForGeneration(index, sheetId, profileNameOverride = null)` (no `opts` param — Option B removed `tagAware`).
- Return: `{ rawSecret, effectiveSecret, profileName, isShared, settings, sheetId }`
  - `rawSecret` = `secretObj.base` always.
  - `effectiveSecret`:
    - `isShared && sheetId` → `_bindSecretToSheet(rawSecret, sheetId)` (PRD §2.4 unchanged)
    - else (own profile, OR shared without sheetId) → `rawSecret`
  - `sheetId` = the sheetId actually used (may be null if no context).

**Backward compat**
- Old callers using `result.secret` will break — update `getSecret(index)` shim to return `result.effectiveSecret`.
- `GeneratePassword` (Phase 03) updated to new shape.

## Architecture
```
getSecretForGeneration(idx, sheetId, override)
  ↓ resolve profileKey/isShared (existing logic)
  ↓ decrypt profile → secretObj
  ↓ rawSecret = secretObj.base
  ↓ shouldBind = isShared && !!sheetId          // OWN PROFILES NEVER BIND (Option B)
  ↓ effectiveSecret = shouldBind ? _bindSecretToSheet(rawSecret, sheetId) : rawSecret
  ↓ return {rawSecret, effectiveSecret, profileName, isShared, settings, sheetId}
```

## Related Code Files
**Modify:**
- `chrome-extension/src/adapters/infrastructure/chrome_storage_adapter.js`

## Implementation Steps
1. Keep signature `getSecretForGeneration(index, sheetId, profileNameOverride = null)` — no new opts param.
2. Compute `rawSecret = secretObj.base`.
3. Compute `shouldBind`:
   ```js
   const shouldBind = isShared && !!sheetId;   // own profiles always unbind
   ```
4. Compute `effectiveSecret`:
   ```js
   const effectiveSecret = shouldBind ? await this._bindSecretToSheet(rawSecret, sheetId) : rawSecret;
   ```
5. Return new shape — drop old `secret` field (callers must migrate).
6. Update `getSecret(index)` legacy shim:
   ```js
   async getSecret(index) {
       const r = await this.getSecretForGeneration(index, null);
       return r ? r.effectiveSecret : null;
   }
   ```

## Todo List
- [x] Compute `rawSecret` separately from `effectiveSecret`
- [x] Bind only when `isShared && sheetId` (own profiles never bind)
- [x] Include `sheetId` in return value
- [x] Update `getSecret` shim to use `effectiveSecret`
- [x] Update JSDoc to reflect Option B (link rationale doc)

## Success Criteria
- Unit tests in Phase 08 cover matrix:
  - own + sheetId → unbound (`effectiveSecret === rawSecret`)
  - own + no sheetId → unbound
  - shared + sheetId → bound (`effectiveSecret !== rawSecret`)
  - shared + no sheetId → unbound
- `getSecret(idx)` still returns string (legacy shim works).
- No regression in existing E2E shared-profile flow.

## Risk Assessment
- **R1:** Tagged own-profile recipe built with sheetId_A but consumed at sheetId_B → HMAC verify in Phase 03 catches mismatch and throws. Password never generated. **This is the ONLY safety net for own profiles.**
- **R2:** Removing `tagAware` flag simplifies API — no risk of caller passing wrong value.

## Security Considerations
- HKDF salt = sheetId for shared profiles only (existing PRD §2.4). No new crypto.
- `rawSecret` returned to use case (for HMAC) is in-memory only; never logged.
- Own-profile threat model: user owns all sheets, mismatch is mistake not attack → explicit error is sufficient defense (see rationale doc §4).

## Next Steps
- Phase 03 use case consumes new return shape.
- Phase 05 SW `COMPUTE_RECIPE_TAG` endpoint reads `rawSecret` from this helper.

## Unresolved
- None — Q5 from prior revision resolved: SW endpoint owns raw-secret exposure (Phase 05).
