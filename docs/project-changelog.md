# Project Changelog

## Android Keystore accepted current-device scope (2026-10-11)

**Changes:** Persistent encrypted Cookbook, per-operation strong biometric
device-key wrapping/opening, original-password recovery, bounded code15 wrapping
repair and password fallback during biometric lockout. The minimal debug demo
has its own package/UID; release excludes the proof Activity and diagnostics.

**Verification:** Independent56 JVM tests and debug/test/release builds passed;
lint0 errors/8 known warnings. Xiaomi/API36 physical proof covered hardware
wrap/unwrap, enrollment invalidation/recovery, wrong-password preservation,
cancellation/background/cold locking, real idle5/total10 and OS-lockout
availability fallback. Owner reported blocked screenshot/only PassChef black;
the precise Recents/video category was not independently established.

**Scope:** The owner explicitly accepted this current-device scope and retained
the unexecuted physical matrix as follow-up; full original matrix is not claimed.
See [scope and remaining checks](../plans/261011-keystore-recovery/reports/current-device-acceptance.md)
and [device evidence](runbooks/android-keystore-device-proof.md).
Fresh main verification passed:56 JVM tests rerun,150/150 tasks executed,
all APK builds/lint0errors8warnings. Merged/pushed at `b959aca`, remote SHA
verified;149 main WIP paths restored exactly. Complete checkout archived and
closed; branch/recovery refs retained. All original feature checkouts are closed.

## Phone QR cookbook handoff (2026-10-11)

**Scope:** Complete the accepted extension sender plan. Own profiles open the
existing export wizard through **Connect phone app**; the cooked bundle displays
one QR or a fitted, animated sequence. Copy/Download and encryption stay intact.
Closing, going Back during cooking, or reopening clears the previous session.

**Status:** Implementation and independent review complete in the original QR
worktree; commit `4a4395c` merged into main at `3d3239d`. Fresh main checks
passed and the checkout was archived and closed. Fifteen QR unit regressions plus 22
unchanged recipe golden tests and two real headed extension E2E scenarios passed.
Independent review found no P1/P2. A separate test-only decoder
recovered the actual canvas payloads and original byte fields exactly. The phone
reader and Drive transport remain outside this plan.

**Contract:** [Phone handoff](specs/qr-handoff.md).
**Plan and proof:** [Recovery plan](../plans/261011-qr-handoff-worktree-recovery/plan.md)
and [verification report](../plans/261011-qr-handoff-worktree-recovery/reports/verification.md).
**Review:** [Independent product review](history/qr-handoff/reports/recovery-independent-review.md).

## Mobile Core recovery integrated (2026-10-11)

**Changes:** Android/JVM shared recipe cooking, tag verification, original
full-access bundle decrypt, and a test-only Argon2id parity proof. A single
root `:shared` module is used by the integrated Keystore storage.

**Verification:** 26 JVM tests pass; Android AAR/JVM JAR compile; 22 original
extension recipe tests pass. The actual Chromium extension supplies 56 vectors
and a complete scalar case-mapping oracle. Independent review closed the Unicode
parity defect; compatibility is pinned to Chromium 143.0.7499.4. BouncyCastle
was test-only in the Core slice; Keystore subsequently promotes production
Argon2id for encrypted Cookbook storage. Golden assertions are unchanged. Local main integrated `7d8808c`; a fresh main
build and browser parity check passed. The checkout was archived and closed.

**Evidence:** [Core recovery report](history/passchef-mobile/reports/mobile-core-recovery.md).


## OAuth/Menu client code recovery (2026-10-11)

**Scope:** Recover the standalone Kotlin recipe-reading/writing client and JVM
OAuth connection-check tool. Repairs cover callback state/PKCE, UTF-8 ranges
and properties, RAW writes, HTTP cleanup, and sanitized nonzero failures.

**Status:** Independent clean build and main integration passed with 22 tests
and no failures; product review found no blocking issue. Original track commits
and recovery commit `0f93c7d` are in local main. Wrapper executable mode is
explicitly tracked for fresh checkouts. The selected worktree is archived and
closed; existing main changes were preserved and remaining plans consolidated.
Real Google OAuth client creation and a live connection check remain the
account owner's pending follow-up. OAuth, Core and QR were subsequently pushed
to origin on 2026-10-11; their original closure reports retain the historical result.

**Plan:** [Recovery plan](../plans/261011-oauth-sheets-worktree-recovery/plan.md).
**Tracking:** [Worktree recovery](worktree-recovery.md).

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
