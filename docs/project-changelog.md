# Project Changelog

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
