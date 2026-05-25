# Project Changelog

## v2.3 — Full Access Sharing (2026-05-25)

**Feature:** Replace HKDF-derived secret sharing with Full Access (raw secret) sharing.

**Design decision:**
- HKDF derived secrets (`DS_i = HKDF(S_i, label)`) generate *different* passwords than Owner → Consumer cannot log in to accounts Owner set up → fundamentally broken for the actual use case.
- PassChef has no server. The password IS the credential. HKDF adds zero practical revocation benefit — true revocation always requires changing service passwords regardless.
- **New model:** Bundle encrypts raw secrets (`S_i`) with `PBKDF2(sharingPassword)`. Consumer gets identical secrets → generates identical passwords.

**Changes:**
- New bundle type `passchef-fullaccess-share` v1.0 (exported by default for all new exports).
- Export wizard: 3 → 2 steps. Relationship label step removed (no HKDF → no label needed).
- Import: `passchef-fullaccess-share` bundles create `profile:NAME` (own profile, not `shared:`). If bundle has `sheetId` → auto-added to `sheetMapping`.
- Legacy `passchef-profile-share` bundles remain importable → stored as `shared:NAME` → HKDF-bound generation unchanged.
- Tier 5 revocation (label rotation) removed from revocation model — meaningless without HKDF.
- PRD updated: `docs/prd/v2-multi-sheet-profiles.md` §2.3, §2.4, §4, §6, §10.2.

**Files to change:** `export-import-adapter.js`, `options-export-wizard.js`, `options-import-wizard.js`, i18n files.

---

## v2.2 — Recipe Verification Tag (2026-05-22)

**Feature:** Verification tag system for recipe integrity validation.

**Details:** 
- Adds optional 4-char verification tag (`.xxxx` suffix) to recipes using HMAC-SHA256 over canonical recipe + sheetId.
- Tag mismatch throws `RecipeProfileMismatchError` when recipe is pasted into wrong sheet/profile context.
- **Key design (Option B):** Tag is verification-only — password value unchanged for own profiles. Manual rebuildability ("Cookbook" insight) preserved.
- Shared profiles unaffected — PRD §2.4 HKDF binding unchanged.
- Zero forced rotation — legacy untagged recipes continue working.
- Popup builder includes "Target Sheet URL" input for tag computation.
- i18n: mismatch error + builder warnings in en/vi.

**Plan:** `plans/260522-1314-recipe-verification-tag-sheet-binding/plan.md`

**Rationale:** `docs/recipe-tag-design-rationale.md`

**Test Coverage:** 55 unit + 6 E2E passing.
