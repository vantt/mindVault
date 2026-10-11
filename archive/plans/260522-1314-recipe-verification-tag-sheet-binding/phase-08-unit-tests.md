# Phase 08 — Unit Tests (Vitest)

## Completion audit — 2026-10-12

- Current status: **Done**; evidence: [completion verification](../../../plans/261012-extension-completion/reports/verification.md) (independent 147 units  + 22 full headed E2E; real Google/native 8/8 separately).
- Independent147-unit suite passes, including immutable original cooking Golden cases and added lifecycle/locale/session ownership regressions. No new coverage-percentage claim; historical Golden edits remain history, while this completion leaves marked tests unchanged.
- Original criteria and prior audit are retained below. Headed automation exercises the actual extension UI; neither routed fixtures nor SW-only checks are presented as live-provider or human-manual signoff.

## Historical relevance audit — 2026-10-11

- Audit status at baseline: **Done**; original recorded status: `completed`.
- Baseline: main `b5be3b0`; historical checklist/criteria below are retained. This was the baseline audit; the 2026-10-12 audit above governs current status. Old ticks alone did not certify acceptance.
- Evidence / remaining: All five domain/storage/generation test files exist; recent independent 77-unit suite passed (archive/plans/261011-remove-bee-tooling/reports/verification.md:24). No new tests run during this audit; no 100%-coverage report claimed. Generation file unchanged since 774d9ce; earliest34e99da golden changes are historical, not evidence of literal all-history immutability.
- Evidence paths above are repository-root-relative; source paths beginning src/ are inside chrome-extension/. No source/test changes or new runtime verification in this audit.

## Context Links
- `chrome-extension/test/unit/generate_password.spec.mjs` — existing test pattern
- `docs/recipe-tag-design-rationale.md` — **Option B (no HKDF for own profiles)**
- Phases 01–04 (code under test)
- Parent plan: `plan.md`

## Overview
- **Priority:** P1
- **Status:** Done
- **Description:** Comprehensive unit tests for new tag codec, parser tag handling, generation use-case match/mismatch/legacy paths, and storage adapter binding matrix (Option B: own profiles never bind).

## Key Insights
- Existing golden tests in `generate_password.spec.mjs` MUST keep passing unchanged — they use untagged recipes, prove backward compat.
- **Cookbook insight test (NEW critical):** own-profile tagged recipe at correct sheetId MUST produce same password as untagged equivalent. This is the canary guarding the value prop.
- Mock storage in existing test returns flat `{secret,...}` — update to new `{rawSecret, effectiveSecret, ...}` shape. For own profiles `rawSecret === effectiveSecret` so legacy asserts hold trivially.
- Node WebCrypto available in Vitest via `globalThis.crypto.subtle`.

## Requirements
**Functional — Test Files**

1. **`test/unit/tag-codec.spec.mjs`** (NEW)
   - `computeRecipeTag` returns deterministic 4-char string for fixed input
   - Output chars all in alphabet (no `o/l/0/1`)
   - Different sheetId → different tag
   - Different modifier order (same set) → same tag (sort invariance)
   - Different secret → different tag
   - `verifyRecipeTag` true on match, false on mismatch
   - `verifyRecipeTag` returns false when `recipe.tag == null`

2. **`test/unit/recipe.spec.mjs`** (NEW or extend if exists)
   - Recipe with tag length 4 → valid
   - Recipe with tag length 3 → throws
   - Recipe with tag containing `o/l/0/1` → throws
   - Recipe with `tag = null` → valid (legacy)

3. **`test/unit/regex_parser_adapter.spec.mjs`** (NEW or extend)
   - `fb#1` → recipe with `tag = null`
   - `fb#1.abcd` → recipe with `tag = "abcd"`
   - `fb#1_!.xyz2` → recipe with modifiers `["_","!"]` and tag `"xyz2"`
   - `fb#1.abc` → null (3-char tag invalid)
   - `fb#1.ABCD` → null (uppercase)
   - `fb#1.abcd.efgh` → null (double tag)

4. **`test/unit/generate_password.spec.mjs`** (MODIFY)
   - Update `MockStorage.getSecretForGeneration` to return `{rawSecret, effectiveSecret, profileName, isShared:false, settings:{}, sheetId}` where for own profiles `effectiveSecret === rawSecret`.
   - All existing golden tests pass unchanged.
   - **NEW (Cookbook canary):** own-profile tagged recipe with correct tag+sheetId produces SAME password as untagged-equivalent recipe (proves Option B preserves manual rebuildability).
   - NEW: own-profile tagged recipe with correct tag+sheetId → password = `rawSecret + hash` (manual-rebuildable).
   - NEW: tagged recipe with WRONG tag → throws `RecipeProfileMismatchError` with `code === "RECIPE_MISMATCH"` (sole failure mode for own profiles).
   - NEW: tagged recipe but `sheetId` null → throws mismatch error.
   - NEW: untagged recipe + sheetId → result includes `warning: "legacy_no_tag"`.
   - NEW: shared-profile tagged recipe with correct tag+sheetId → password uses HKDF-bound secret (PRD §2.4 path, unchanged).
   - **REMOVE (Option A artifact):** any test asserting own-profile tagged recipe produces different password than untagged — Option B says they MUST be equal.

5. **`test/unit/chrome_storage_adapter.spec.mjs`** (NEW — minimal, mocks `chrome.storage`)
   - Matrix: (isShared T/F) × (sheetId present/absent)
     - own + sheetId → `effectiveSecret === rawSecret` (**Option B: own never bind**)
     - own + no sheetId → `effectiveSecret === rawSecret`
     - shared + sheetId → `effectiveSecret !== rawSecret` (HKDF bound, PRD §2.4)
     - shared + no sheetId → `effectiveSecret === rawSecret`
   - Note: no `tagAware` axis — Option B removed that flag.

**Non-functional**
- All new test files <200 LOC each; split if needed.
- Use `describe.concurrent` where independent.

## Related Code Files
**Create:**
- `chrome-extension/test/unit/tag-codec.spec.mjs`
- `chrome-extension/test/unit/recipe.spec.mjs`
- `chrome-extension/test/unit/regex_parser_adapter.spec.mjs`
- `chrome-extension/test/unit/chrome_storage_adapter.spec.mjs`

**Modify:**
- `chrome-extension/test/unit/generate_password.spec.mjs`

## Implementation Steps
1. Write tag-codec tests first (no Chrome dependency).
2. Write recipe + parser tests.
3. Update generate_password mock + add new test cases including Cookbook canary.
4. Write storage adapter test with `chrome.storage.{sync,session}` mocks (use `vi.fn()` for `get`/`set`); reuse aes-helper mock pattern if present.
5. Run `pnpm test:unit` (or project equivalent) — all green.

## Todo List
- [x] tag-codec.spec.mjs
- [x] recipe.spec.mjs
- [x] regex_parser_adapter.spec.mjs
- [x] generate_password.spec.mjs updated + new cases (incl. Cookbook canary)
- [x] chrome_storage_adapter.spec.mjs (own-never-bind matrix)
- [x] Full unit suite green

## Success Criteria
- All existing tests still pass.
- Cookbook canary test passes: own-profile tagged-vs-untagged produce identical password.
- New tests cover every branch in Phases 01–04.
- Coverage report (if enabled) shows tag-codec at 100% line coverage.

## Risk Assessment
- **R1:** `crypto.subtle` in Node test env may differ in alphabet edge cases. Mitigation: pin to Node 20+ (already required by Vitest).
- **R2:** Mock storage drift — keep mock shape locked to real adapter return signature; add type comment.
- **R3:** Future regression to Option A could silently break Cookbook insight — canary test guards against this.

## Security Considerations
- Tests must not log secrets even on failure (use fixed dummy strings).

## Next Steps
- Phase 09 covers E2E.

## Unresolved
- None.
