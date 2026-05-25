---
title: "Recipe Verification Tag (v2.2)"
description: "Append 4-char verification tag to recipes so decode fails fast on profile/sheet mismatch. Tag is verification-only — password value unchanged for own profiles (preserves manual rebuildability)."
status: completed
priority: P1
effort: ~11h
branch: main
tags: [security, recipe, crypto, popup, i18n, prd-v2.2]
created: 2026-05-22
---

# Recipe Verification Tag (v2.2)

## Goal
Fix silent-wrong-password bug when a recipe built for Profile A is pasted into a sheet bound to Profile B. Add 4-char verification tag suffix `.xxxx` (base32 HMAC) to recipes. **Tag is pure verification metadata — does NOT change password value for own profiles** (preserves README "Cookbook" insight: user can still cook password by hand).

## Core Design (Option B — see `docs/recipe-tag-design-rationale.md`)
- **Recipe grammar (additive):** `<hash><position><secretIndex>[modifiers][.<tag>]` — tag 4 chars from `abcdefghijkmnpqrstuvwxyz23456789` (20 bits).
- **Tag = `base32(HMAC_SHA256(secret_bytes, canonical_recipe + "|" + sheetId)).slice(0,4)`** computed at build time.
- **At decode:** tag present → recompute → mismatch throws `RecipeProfileMismatchError` (code `RECIPE_MISMATCH`). No tag → legacy path.
- **Own profile generation:** `S_i + hash` (unchanged from v2.1, regardless of tag). Manual rebuildable.
- **Shared profile generation:** `HKDF(DS_i, sheetId) + hash` (PRD §2.4, unchanged).
- **Migration:** Untagged recipes keep v2.1 behavior unchanged. Zero forced rotation.

## Phases

| # | Phase | File | Est | Status |
|---|---|---|---|---|
| 01 | Domain: tag codec + Recipe extension + new error | `phase-01-verification-tag-domain.md` | 1h | completed |
| 02 | Parser: extend regex + tag extraction | `phase-02-parser-update.md` | 0.5h | completed |
| 03 | Generation use case: verify path + mismatch throw (no HKDF for own) | `phase-03-generation-usecase.md` | 1h | completed |
| 04 | Storage adapter: expose raw + effective secret + sheetId | `phase-04-storage-adapter.md` | 1h | completed |
| 05 | Service worker + content script: propagate mismatch | `phase-05-sw-content-propagation.md` | 1h | completed |
| 06 | Popup builder: sheet URL input + tag in output | `phase-06-popup-builder-sheet-input.md` | 2h | completed |
| 07 | i18n: en + vi messages | `phase-07-i18n.md` | 0.5h | completed |
| 08 | Unit tests (Vitest) | `phase-08-unit-tests.md` | 2h | completed |
| 09 | E2E (Playwright): mismatch flow | `phase-09-e2e-mismatch-flow.md` | 1.5h | completed |
| 10 | Docs: PRD v2.2 changelog + README | `phase-10-docs-update.md` | 0.5h | completed |

## Key Dependencies
- Phase 02 → 01 (Recipe needs tag field before parser sets it)
- Phase 03 → 01, 04 (use case calls storage helper exposing secret + sheetId for HMAC verify)
- Phase 05 → 03 (error code must exist)
- Phase 06 → 01, 05 (uses tag codec + SW endpoint)
- Phase 08 → 01–06
- Phase 09 → 05–07
- Phase 10 → 03, 04 (final-spec doc)

## Files Modified (high-level)
- `core/domain/recipe.js`, new `core/domain/tag-codec.js`, new `core/domain/recipe-errors.js`
- `adapters/infrastructure/regex_parser_adapter.js`
- `core/usecases/generate_password.js`
- `adapters/infrastructure/chrome_storage_adapter.js`
- `service_worker.js`, `content/content.js`
- `popup/popup-recipe-builder.js`, `popup/popup.html`, `popup/popup.css`, `popup/popup.js`
- `_locales/en/messages.json`, `_locales/vi/messages.json`
- `docs/prd/v2-multi-sheet-profiles.md`, `README.md`
- Tests: `test/unit/*.spec.mjs` (new files for tag, parser, generate-mismatch), `test/e2e/*.spec.mjs`

## Key Design Choices Baked In
- **Option B (no HKDF for own profiles)** — see `docs/recipe-tag-design-rationale.md`. Tag is verification-only, password value unchanged. Cookbook insight preserved.
- **Shared profiles untouched** — PRD §2.4 HKDF binding stays as-is.
- **Tag mismatch is the sole failure mode** for own-profile tagged recipes (vs. Option A's defense-in-depth via dual crypto layers).

## Unresolved Questions (rollup, see phases for context)
- Q1: Should `_` (position flip) be included in the canonical recipe string fed into HMAC? Current call: **yes**, include all modifiers sorted. Confirm in Phase 01.
- Q2: Should we warn-but-still-generate for legacy untagged recipes, or stay silent? Plan: emit `warning:"legacy_no_tag"` in SW response, content script ignores, popup builder optionally surfaces. Confirm in Phase 05.
- Q3: Auto-fill target-sheet URL from active tab — opt-in toggle or default-on? Plan: default-on, no toggle. Confirm in Phase 06.
- Q4: Should `RecipeProfileMismatchError` expose which fields differ (profile vs sheet)? Plan: no — opaque mismatch, less leakage. Confirm in Phase 01.
