# Mobile foundation worktree recovery

Updated: 2026-10-11. The initial snapshot compared four feature branches with
local `main` at `ef720b1`. OAuth/Menu recovery commit `0f93c7d` is now in main;
its worktree is closed after verification and a complete recovery archive.
Mobile Core `7d8808c` and QR sender `4a4395c` (merge `3d3239d`) are also
in main and origin, independently verified there, archived and closed. Keystore
current-Xiaomi scope was explicitly accepted on 2026-10-11; fresh main verification
passed. Only that checkout remains open until verified push/closure.

Recovery policy: complete each of the three remaining features in its existing
worktree against its own plan, verify it, then merge and close that worktree.
Rank work by product impact and dependencies: Mobile Core first, dependent
Keystore/unlock second, independent QR transport third. A blocked physical-device
check allowed QR work to proceed. The owner subsequently accepted the verified
current-Xiaomi Keystore scope with the unexecuted physical matrix recorded. After each verified worktree merge, commit the integration and
push main before proceeding to the next tree, as requested by the owner. Start
new work directly from main only after all three are done.

## Worktrees and their plans

| Worktree branch | Latest relevant plan | Recorded progress | Recovery status |
|---|---|---|---|
| `wt/passchef-mobile-oauth-sheets` | [Track D: OAuth/Menu client](history/passchef-mobile-oauth-sheets/plan.md), approved 2026-07-28 | Original five commits and recovery commit `0f93c7d` integrated into main | Code scope complete; worktree archived and closed; live Google check pending |
| `wt/passchef-mobile` | [Track B: Mobile Core](history/passchef-mobile/implement-plan.md), updated 2026-07-28 | Full accepted Core slice implemented; 26 JVM tests and Android packaging pass | Complete; `7d8808c` in main, fresh integration passed, archived and closed |
| `wt/passchef-mobile-android-keystore` | [Android Keystore + biometric unlock](history/passchef-mobile-android-keystore/plan.md), approved 2026-07-30 | Core reconciled; genuine storage/unlock and repairs frozen at source `0d38e62`, acceptance documented at `fc38197` | 56 JVM tests/builds and agreed Xiaomi physical scope passed; fresh main56-JVM/build/lint passed; commit/push/closure pending |
| `wt/qr-handoff` | [Extension QR handoff](history/qr-handoff/plan.md), approved 2026-07-27; [recovery execution](../plans/261011-qr-handoff-worktree-recovery/plan.md) | Sender implemented in original checkout; 15 QR + 22 golden unit tests and two headed canvas-decoding E2E passed | Complete; `4a4395c` merged at `3d3239d`, fresh integration passed, archived and closed; phone reader excluded |

The linked Markdown planning dossiers are preserved unchanged here from their
respective worktrees so remaining work can be continued from main after merge.
They are archived planning evidence from **2026-07-27 through 2026-07-30**;
historical validation reports do not prove current environment/device
availability or completed code. QR sender now has separate current verification
and independent review recorded below. Historical dossiers remain planning
evidence; current recovery reports record actual implementation/acceptance.
At initial dossier consolidation, only planning files moved to main. Each feature
was subsequently completed in its original checkout before integration.

## Why OAuth/Menu client goes first

This branch already contains the accepted Track D code deliverable and can
build independently of an Android UI or physical device. Recovering it first
establishes a tested recipe-reading/writing foundation without expanding scope.
See the [recovery plan](../plans/261011-oauth-sheets-worktree-recovery/plan.md).

The accepted boundary remains an OAuth Console runbook, a standalone Kotlin
client for Google Menu reads/append/update, and a compiled JVM connection check.
Creating a real OAuth client and running that check with a real Google Menu
remain the account owner's follow-up; both are pending.

Recovery addresses these product defects before integration:

- Bind OAuth callbacks to their authorization attempt with state and PKCE S256.
- Preserve UTF-8 ranges and supplementary characters in Menu tab names.
- Write values as RAW by default so usernames and recipes remain literal text.
- Load configuration properties as UTF-8.
- Close HTTP resources explicitly and return sanitized, nonzero connection-check
  failures, including token-exchange failures.
- Document configuration, the actual connection-check command, and failures.

Synthetic regression tests exercise these paths without real account credentials.
No new extension or landing-page dependencies are required.

## Dependency order and remaining product gaps

Recovery completed **Mobile Core** before dependent Keystore master-key/fallback
work: recipe parity, real synthetic bundle decrypt and Argon2id parity were
verified. QR sender proceeded while physical Keystore acceptance was blocked.

**Extension QR handoff is independent** of OAuth and the core implementation.
Its internal order is QR button, then oversized-bundle chunking. A complete
phone import still needs QR reading/reassembly, compact-payload decoding,
bundle decrypt, storage, and device verification. The QR plan labels
`p-542801dc` as reader/reassembly work, while the newer Mobile Core plan scopes
that item to decrypt only. Reader ownership and acceptance need reconciliation;
neither current slice supplies a complete phone import flow.

**Gradle structure is reconciled.** The Menu client remains standalone in
`mobile/sheets-client/`. One repository-root build combines verified `:shared`
and Keystore `:app`, preserving Core code. Fresh main verification passed.

The Keystore plan requires a physical Android device; a JVM test or scaffold
build alone does not prove biometric or hardware-backed storage behavior.

## QR sender recovery record

The accepted extension sender scope is complete in its original worktree:
own-profile **Connect phone app**, unchanged encrypted JSON Copy/Download,
reversible compact byte fields, real encoder capacity fitting, and animated
frames. Close, Done, Back during cooking, and reopen reject obsolete results
and clear the canvas/timer. No phone reader or Drive transport was added.
See the [sender contract](specs/qr-handoff.md) and
[verification report](../plans/261011-qr-handoff-worktree-recovery/reports/verification.md).

Independent review found no P1/P2: **37/37 scoped unit tests** (15 QR plus 22
unchanged recipe golden tests) and **2/2 real headed extension E2E** passed.
The E2E decoder independently reconstructs actual canvas payloads, including
every animated frame and original byte arrays. Actual vendor boundary and
Unicode roundtrip checks also passed. See the
[independent review](history/qr-handoff/reports/recovery-independent-review.md).
Product commit `4a4395c` merged into main at `3d3239d`. Fresh main checks passed:
37 scoped unit tests and two headed canvas-decoding E2E scenarios. All 149 prior
main WIP paths were restored with exact hashes. The complete QR checkout archive
contains 3,237 verified files and 212 symlinks (65,099,951 bytes), at
`/home/vantt/projects/passMan-recovery/20261011-qr-handoff/`. The checkout is
closed; its branch and recovery refs remain. Keystore physical acceptance remains
recorded within its separately accepted current-device scope.

## Preserve existing work and integrate

Main has unrelated tracked/untracked edits. The selected checkout also has
deferred tooling edits; preserve these separately from product commits.
OAuth recovery originally reconciled `.gitignore` and `.bee/decisions.jsonl`.
The final Keystore integration has no incoming collision with existing main WIP.

Record recovery refs and save main's tracked/untracked changes before merging.
Retain the recovery refs while restoring main's work. Before closing the
selected checkout, archive its complete contents, including ignored toolchains
and runtime files, outside the repository and verify the archive inventory.
Private configuration must never be displayed in the recovery report.

## Validation and merge record

- Current product regression tests: **22 passed; zero failures/errors/skips**.
- Current module build and test run: independent `./gradlew --no-daemon clean
  build test` passed on 2026-10-11 with the portable JDK 21; all 10 tasks ran.
- Independent product review: no blocking findings after repairs. The original
  seven tests are unchanged; added tests use mocked Google HTTP and local
  loopback callbacks without contacting Google.
- Local merge: fast-forward to `0f93c7d`; all original OAuth commits are ancestors
  of main. Main integration build/test passed with 22 tests and no failures.
- Existing main work: all 149 dirty paths restored; 147 exact content hashes
  verified, with original ignore rules and decision events preserved in the two
  merged files. A retained Git recovery ref points to stash `c92a3379`:
  `refs/recovery/passchef-oauth-main-wip-20261011`.
- Fresh checkout exposed an untracked executable-mode assumption. The wrapper
  is now explicitly recorded as mode `100755`; direct `./gradlew` execution
  succeeds. A check in a guaranteed empty temporary configuration directory
  correctly exits 1 with setup instructions, without starting Google sign-in.
- Recovery archive: `/home/vantt/projects/passMan-recovery/20261011-oauth-sheets/`.
  The complete checkout archive was checked byte-for-byte and by file mode:
  1,581 files, 526,740,907 bytes. Deferred tooling changes and ignored runtime
  files remain recoverable; the JDK was also relocated to main's `.toolchain/`.
- Worktree closure: selected OAuth checkout removed from Git's worktree list;
  its branch is retained. At that closure, three unfinished feature worktrees remained.
- Real Google OAuth client creation and live connection check: **pending**.

Local verification uses mocked Google responses and local loopback HTTP only.
It does not certify a real Google token exchange or Cookbook read. At OAuth
closure, no remote push had yet been performed. On 2026-10-11, main through
`ad1aec6`, including the completed OAuth, Mobile Core and QR integrations, was
pushed to origin and its remote commit verified. Before final Keystore integration,
main/origin both point to `3f4cda3`. New main-based plans wait only for this
accepted Keystore scope to pass fresh integration, push and checkout closure.

## Mobile Core current recovery

The accepted Track B slice is implemented in its original worktree and verified
locally: 26 JVM tests, Android packaging, and 22 unchanged extension recipe tests
pass. Independent review verified 56 real extension vectors and the full Unicode
scalar mapping against Chromium 143.0.7499.4; no blocking findings remain.
Product commit `7d8808c` is in main. Fresh main build/test passed with all
26 JVM tests and actual-browser scalar/vector checks. Its complete checkout
archive contains 3,700 verified files and 211 symlinks (69,441,512 bytes), at
`/home/vantt/projects/passMan-recovery/20261011-mobile-core/`. The checkout is
closed; its branch and recovery refs remain. Core was merged into the original
Keystore checkout at `3532c16`, preserving that checkout’s deferred WIP.
See [the Core report](history/passchef-mobile/reports/mobile-core-recovery.md).

## Final Keystore integration

Owner acceptance: **“Merge/push hôm nay, ghi rõ bài kiểm tra còn thiếu”**, 2026-10-11.
The validated Xiaomi14TPro/API36 scope is complete; full original matrix is not.
Source `0d38e62` and accepted scope docs `fc38197` are preserved in the branch.
Independent checks passed56 JVM tests, app/test/release APKs and lint0errors8warnings;
no P1/P2 remained. Real device evidence covers hardware/nonexportability,
enrollment invalidation/original-password recovery, wrong-password preservation,
cancellation/background/cold locking, idle5/total10 and OS-lockout availability fallback.

[Scope and remaining checks](../plans/261011-keystore-recovery/reports/current-device-acceptance.md)
retain API26/no-biometric hardware/no enrollment/StrongBox absent, typed7/9,
exact kill/retry-exhaustion windows and precise Recents observation as unexecuted.
Owner screen reports and all earlier failed probes remain recorded; no simulation
or missing numeric error was relabeled as a physical pass.

The complete final checkout archive at
`/home/vantt/projects/passMan-recovery/20261011-keystore/final-checkout/`
contains3,464 verified files (234,260,698 bytes), including deferred tooling WIP
and ignored build/runtime files. Branch and recovery refs will remain after closure.
Main's149 existing WIP paths are retained through recovery stash
`refs/recovery/passchef-keystore-final-main-wip-20261011` (`d2ea315`).
Fresh main verification passed:56 JVM0fail/errors/skips,150/150 tasks executed,
debug/test/release builds and lint0errors8warnings. Demo/test APKs match the pinned
physical candidates byte-for-byte. Integration commit/push, exact WIP restoration
and closure are pending. No new feature work has started.
