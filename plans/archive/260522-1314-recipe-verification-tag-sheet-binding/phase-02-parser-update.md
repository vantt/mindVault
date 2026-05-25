# Phase 02 — Parser Update (Regex + Tag Extraction)

## Context Links
- `chrome-extension/src/adapters/infrastructure/regex_parser_adapter.js` — current parser
- Phase 01 (`Recipe` now accepts tag)
- Parent plan: `plan.md`

## Overview
- **Priority:** P1
- **Status:** completed
- **Description:** Extend parser regex to accept optional `.<tag>` suffix; pass tag through to Recipe constructor.

## Key Insights
- Backward compatible — tag suffix is optional via `(?:\.([a-z2-9]{4}))?$`.
- Tag char-class `[a-z2-9]` is superset of actual alphabet (`o,l` are in `a-z`). Stricter validation happens in `Recipe.validate()` (Phase 01).
- Hash charset `[a-zA-Z0-9]` is broad enough that uppercase hash + lowercase tag can coexist unambiguously thanks to `.` separator.

## Requirements
**Functional**
- Parser still returns `null` for legacy untagged inputs only if they were valid before (no regression).
- Parser accepts `fb#1.abcd` → Recipe with `tag = "abcd"`.
- Parser rejects `fb#1.abc` (3-char), `fb#1.ABCD` (uppercase), `fb#1.abcd.efgh` (double tag).

**Non-functional**
- File stays <100 LOC.

## Architecture
No new files. Modify regex + match destructure + Recipe construction.

## Related Code Files
**Modify:**
- `chrome-extension/src/adapters/infrastructure/regex_parser_adapter.js`

## Implementation Steps
1. Update regex:
   ```js
   this.regex = /^([a-zA-Z0-9]+)([#@$%^])(\d)([_!?~]*)(?:\.([a-z2-9]{4}))?$/;
   ```
2. Destructure with 6th group:
   ```js
   const [_, hash, position, secretIndex, modifiersStr, tag] = match;
   ```
3. Pass tag to Recipe (null if undefined):
   ```js
   return new Recipe(hash, position, secretIndex, modifiers, tag || null);
   ```
4. Update top-of-file comment to mention tag grammar.

## Todo List
- [x] Update regex with optional tag group
- [x] Destructure 6th group
- [x] Forward `tag` to Recipe constructor (null fallback)
- [x] Add comment documenting new grammar

## Success Criteria
- Existing golden tests pass unchanged (no `tag` in input → `tag = null` on Recipe).
- New unit tests (Phase 08) for tag presence/absence/invalid-length all pass.

## Risk Assessment
- **R1:** Regex greedy match could swallow `.` into hash. Mitigation: hash charset `[a-zA-Z0-9]` excludes `.`, so safe.
- **R2:** Tag charset mismatch (regex `[a-z2-9]` vs alphabet excludes `o/l/0/1`). Letters `o` and `l` slip past regex but fail Recipe.validate → returns null via try/catch. Acceptable.

## Security Considerations
- No new attack surface; parser is pure-syntactic.

## Next Steps
- Phase 03 consumes parsed `recipe.tag` in `GeneratePassword.execute`.

## Unresolved
- None.
