# Phase 01 — Verification Tag Domain Logic

## Context Links
- `chrome-extension/src/core/domain/recipe.js` — Recipe entity, currently no tag field
- `docs/prd/v2-multi-sheet-profiles.md` §2.4 — sheet-bound generation (current spec for shared profiles)
- Parent plan: `plan.md`

## Overview
- **Priority:** P1 (foundation for all other phases)
- **Status:** completed
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
- [x] Sanity: run `node -e "import('./tag-codec.js').then(...)"` smoke check

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

## Unresolved
- Q1: Sort modifiers in canonical? Recommendation: **yes**, lexicographic. Confirm with reviewer.
- Q4: Opaque mismatch (no field disclosure) — adopted by default per design constraint #4.
