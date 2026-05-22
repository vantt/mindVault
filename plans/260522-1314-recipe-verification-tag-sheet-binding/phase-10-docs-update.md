# Phase 10 — Docs Update (PRD v2.2 + README + Rationale Link)

## Context Links
- `docs/recipe-tag-design-rationale.md` — **Option B decision record (canonical)**
- `docs/prd/v2-multi-sheet-profiles.md` — current PRD v2.1 (§2.4 sheet-bound generation, shared-only)
- `README.md` — recipe syntax section
- Parent plan: `plan.md`

## Overview
- **Priority:** P2
- **Status:** completed
- **Description:** Add v2.2 changelog to PRD reflecting **Option B** (verification tag only — no HKDF for own profiles). Update README Recipe Syntax section: tag is optional verification metadata; password value unchanged with/without tag. Link rationale doc throughout.

## Key Insights
- Do NOT rewrite v2.1 §2.4 in-place — add v2.2 section that references rationale doc for design history.
- README must emphasize: **tag does NOT change password value** — manual rebuildability ("Cookbook" insight) preserved.
- All design-debate content belongs in `docs/recipe-tag-design-rationale.md`. PRD records the spec; README addresses the user.

## Requirements
**Functional**
1. PRD update (`docs/prd/v2-multi-sheet-profiles.md`):
   - Add new section §2.5 "Verification Tag (v2.2)":
     - Grammar `<base>[.<tag>]`, alphabet, HMAC computation (`HMAC_SHA256(rawSecret, canonical_recipe + "|" + sheetId)`).
     - Mismatch behavior: `RecipeProfileMismatchError` thrown — no password generated.
     - **Explicit:** "Tag is verification-only. Password value identical for tagged vs untagged recipes when both decode against the same sheet+profile."
     - Link to `docs/recipe-tag-design-rationale.md`.
   - **Do NOT add §2.6 generation override** (previous Option-A plan called for it). §2.4 stands unchanged — Option B does not extend HKDF to own profiles.
   - Add §Changelog entry "v2.2 — 2026-05-22":
     - Verification tag suffix added (additive grammar).
     - Mismatch detection via tag verify (own + shared).
     - Own-profile generation unchanged (Option B — see rationale doc).
     - Shared-profile generation unchanged (PRD §2.4 preserved).
     - Migration: zero rotation — untagged recipes unaffected.
2. README update:
   - Add to Recipe Syntax section: `<hash><position><secret>[modifiers][.<tag>]`.
   - **Critical clarification:** "The optional `.tag` suffix is verification metadata only. It does NOT change the password value — you can still compute your password by hand using the same recipe minus the tag."
   - Add brief "Why verification tags?" subsection (2-3 lines): catches paste-to-wrong-sheet mistakes; doesn't replace your spice.
   - Cross-link to rationale doc for "Why no HKDF binding for own profiles?".

## Related Code Files
**Modify:**
- `docs/prd/v2-multi-sheet-profiles.md`
- `README.md`

**Read (for context):**
- `docs/recipe-tag-design-rationale.md`

## Implementation Steps
1. Open PRD, find §2.4 end, insert §2.5 (tag spec only). Do not override §2.4 generation matrix.
2. Find existing changelog at bottom (or create if absent); add v2.2 entry reflecting Option B.
3. Open README, locate Recipe Syntax section; append tag grammar + Cookbook-preserving clarification.
4. Cross-link: PRD §2.5 links rationale doc; README links rationale doc for design context.

## Todo List
- [x] PRD §2.5 tag grammar + mismatch behavior + rationale link
- [x] PRD changelog v2.2 entry (Option B, zero rotation, own generation unchanged)
- [x] README Recipe Syntax: tag grammar + "doesn't change password" clarification
- [x] README "Why verification tags?" subsection
- [x] Cross-links to `docs/recipe-tag-design-rationale.md` in PRD and README

## Success Criteria
- Reader of PRD can answer: "What happens if I paste a tagged recipe in the wrong sheet?" — explicit mismatch error, no password.
- Reader of README can answer: "Does the tag change my password?" — **no**, tag is verification only; manual rebuild still works.
- Reader of README can answer: "Do I need to rotate my recipes?" — no, legacy recipes still work.
- Rationale doc referenced from both PRD and README.

## Risk Assessment
- **R1:** Docs drift from code. Mitigation: gate this phase as final, post-implementation.
- **R2:** Future reader confused why own/shared have different paths. Mitigation: rationale doc §4 (threat model) explains.

## Security Considerations
- Do not leak HMAC algorithm specifics in user-facing README; reserve detail for PRD.

## Next Steps
- After merge, announce in changelog / release notes.

## Unresolved
- None.
