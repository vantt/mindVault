# Phase 01 — Verification Tag Domain Logic

## Completion audit — 2026-10-12

- Current status: **Done**; evidence: [completion verification](../261012-extension-completion/reports/verification.md) (independent 147 units  + 22 full headed E2E; real Google/native 8/8 separately).
- Recipe/tag/error domain retained; tag-codec units verify deterministic HMAC/base32, alphabet and sorted modifiers. Current147-unit suite passes; domain files were not changed for UI completion.
- Original criteria and prior audit are retained below. Headed automation exercises the actual extension UI; neither routed fixtures nor SW-only checks are presented as live-provider or human-manual signoff.
- Original <100-line codec estimate is Superseded by the current <200-line source rule (codec 113 lines); no user outcome deferred.

## Historical relevance audit — 2026-10-11

- Audit status at baseline: **Done**; original recorded status: `completed`.
- Baseline: main `b5be3b0`; historical checklist/criteria below are retained. This was the baseline audit; the 2026-10-12 audit above governs current status. Old ticks alone did not certify acceptance.
- Evidence / remaining: Recipe/tag/error behavior implemented; src/core/domain/{recipe.js:16,tag-codec.js:71,recipe-errors.js} and eleven tag-codec unit cases verify domain outcomes. Original <100-line estimate is not a missing user outcome; codec 113 lines remains below current 200-line code rule.
- Evidence paths above are repository-root-relative; source paths beginning src/ are inside chrome-extension/. No source/test changes or new runtime verification in this audit.

## Context Links
- `chrome-extension/src/core/domain/recipe.js` — Recipe entity, currently no tag field
- `docs/prd/v2-multi-sheet-profiles.md` §2.4 — sheet-bound generation (current spec for shared profiles)
- Parent plan: `plan.md`

## Overview
- **Priority:** P1 (foundation for all other phases)
- **Status:** Done
- **Description:** Create pure-function tag codec (HMAC-SHA256 → base32 truncate to 4 chars), extend `Recipe` with optional `tag` field, add new error class `RecipeProfileMismatchError`.

## Key Insights
- HMAC over UTF-8 secret bytes — preimage-resistant, 20-bit truncation safe against guessing (no oracle to brute-force against without unlocking vault).
- Custom base32 alphabet `abcdefghijkmnpqrstuvwxyz23456789` (excludes `o/l/0/1`) → 20 bits = 4 chars exactly (2^20 = 1,048,576 < 32^4 = 1,048,576). Use first 4 chars after encoding all 32 bytes.
- Canonical recipe string for HMAC = `hash + position + secretIndex + sortedModifiers` (no tag, no `.`). Sort modifiers alphabetically to avoid order-sensitivity.
- Tag computation is **synchronous-async** — `crypto.subtle.sign` is Promise-based. Codec must return Promises.

## Requirements
**Functional**
- `computeRecipeTag({hash, position, secretIndex, modifiers, secret, sheetId})` returns `Promise<string>` (4 base32 chars).
- `verifyRecipeTag(recipe, secret, sheetId)` returns `Promise<boolean>`.
- `RecipeProfileMismatchError extends Error` with `code = "RECIPE_MISMATCH"`.
- Recipe entity accepts optional 5th constructor arg `tag` (null or 4-char string).

**Non-functional**
- No leaking which field (profile vs sheet) caused mismatch.
- File `tag-codec.js` <100 LOC.

## Architecture
```
core/domain/
  recipe.js           ← extended: optional tag field, validate format
  tag-codec.js (NEW)  ← computeRecipeTag, verifyRecipeTag, base32 alphabet
  recipe-errors.js (NEW) ← RecipeProfileMismatchError + future error classes
```

## Related Code Files
**Modify:**
- `chrome-extension/src/core/domain/recipe.js`

**Create:**
- `chrome-extension/src/core/domain/tag-codec.js`
- `chrome-extension/src/core/domain/recipe-errors.js`

## Implementation Steps
1. Create `recipe-errors.js`:
   ```js
   export class RecipeProfileMismatchError extends Error {
       constructor(msg = "Recipe verification tag mismatch") {
           super(msg);
           this.name = "RecipeProfileMismatchError";
           this.code = "RECIPE_MISMATCH";
       }
   }
   ```
2. Create `tag-codec.js`:
   - Constant `TAG_ALPHABET = "abcdefghijkmnpqrstuvwxyz23456789"` (32 chars).
   - `canonicalize({hash, position, secretIndex, modifiers})` → `hash + position + secretIndex + [...modifiers].sort().join('')`.
   - `_base32Encode(uint8Array)` — bit-pack into 5-bit groups, map via alphabet, return string.
   - `async computeRecipeTag({hash, position, secretIndex, modifiers, secret, sheetId})`:
     - If `!sheetId || !secret` → throw or return null? Decision: throw — caller must guard.
     - Build `message = canonicalize(...) + "|" + sheetId`.
     - `keyMaterial = importKey("raw", encode(secret), {name:"HMAC", hash:"SHA-256"}, false, ["sign"])`.
     - `mac = sign("HMAC", keyMaterial, encode(message))` → ArrayBuffer.
     - Return `_base32Encode(new Uint8Array(mac)).slice(0,4)`.
   - `async verifyRecipeTag(recipe, secret, sheetId)` → bool. Returns false if `recipe.tag` is null.
3. Extend `recipe.js`:
   - Constructor: `constructor(hash, position, secretIndex, modifiers = [], tag = null)`.
   - Store `this.tag = tag`.
   - In `validate()`: if `tag !== null`, must match `/^[abcdefghijkmnpqrstuvwxyz23456789]{4}$/`.

## Todo List
- [x] Create `recipe-errors.js`
- [x] Create `tag-codec.js` with alphabet + canonicalize + base32 + computeRecipeTag + verifyRecipeTag
- [x] Extend `Recipe` constructor with optional `tag`
- [x] Add tag validation in `Recipe.validate()`
- [x] Sanity outcome: actual module imports/domain units pass; original `node -e` smoke method Superseded (not claimed run).

## Success Criteria
- Importing all 3 modules in a vitest test passes.
- `computeRecipeTag` with fixed inputs produces deterministic, 4-char, alphabet-conformant output.
- `new Recipe("fb", "#", 1, [], "abcd")` succeeds; `new Recipe("fb", "#", 1, [], "abc")` throws.

## Risk Assessment
- **R1:** Base32 implementation bug → wrong tags everywhere. Mitigation: unit test with known HMAC vectors.
- **R2:** Modifier sort changes behavior — sort must use string compare not array order. Mitigation: explicit `[...modifiers].sort()`.

## Security Considerations
- HMAC key = secret bytes — preimage-resistant. 4-char truncation = 20 bits → 1-in-1M random hit, not exploitable without oracle.
- No timing-safe compare needed: tag comparison happens locally, no network attack surface.
- Do NOT log tag values or secret bytes.

## Next Steps
- Phase 02 wires this into the parser (regex update + tag extraction).
- Phase 03 wires verification into `GeneratePassword`.

## Historical unresolved questions
- Q1: Sort modifiers in canonical? Recommendation: **yes**, lexicographic. Confirm with reviewer.
- Q4: Opaque mismatch (no field disclosure) — adopted by default per design constraint #4.

## Current unresolved questions

None. Resolved: lexicographic modifier sorting is retained and tested; mismatch remains opaque.
