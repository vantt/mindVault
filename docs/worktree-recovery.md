# Mobile foundation worktree recovery

Updated: 2026-10-11. The initial snapshot compared four feature branches with
local `main` at `ef720b1`. OAuth/Menu recovery commit `0f93c7d` is now in main;
its worktree is closed after verification and a complete recovery archive.

Recovery policy: complete each of the three remaining features in its existing
worktree against its own plan, verify it, then merge and close that worktree.
Rank work by product impact and dependencies: Mobile Core first, dependent
Keystore/unlock second, independent QR transport third. A blocked physical-device
check may allow QR work to proceed, but never licenses merging incomplete
Keystore work. Start new work directly from main only after all three are done.

## Worktrees and their plans

| Worktree branch | Latest relevant plan | Recorded progress | Recovery status |
|---|---|---|---|
| `wt/passchef-mobile-oauth-sheets` | [Track D: OAuth/Menu client](history/passchef-mobile-oauth-sheets/plan.md), approved 2026-07-28 | Original five commits and recovery commit `0f93c7d` integrated into main | Code scope complete; worktree archived and closed; live Google check pending |
| `wt/passchef-mobile` | [Track B: Mobile Core](history/passchef-mobile/implement-plan.md), updated 2026-07-28 | Full accepted Core slice implemented; 26 JVM tests and Android packaging pass | Independent review passed; commit and merge pending |
| `wt/passchef-mobile-android-keystore` | [Android Keystore + biometric unlock](history/passchef-mobile-android-keystore/plan.md), approved 2026-07-30 | Scaffold commit `a1c57c9`; physical-device bridge verification recorded as blocked | Real device verification and later storage/unlock implementation pending |
| `wt/qr-handoff` | [Extension QR handoff](history/qr-handoff/plan.md), approved 2026-07-27; [recovery execution](../plans/261011-qr-handoff-worktree-recovery/plan.md) | Sender implemented in original checkout; 15 QR + 22 golden unit tests and two headed canvas-decoding E2E passed | Independent review complete, no P1/P2; product commit and main merge pending; phone reader excluded |

The linked Markdown planning dossiers are preserved unchanged here from their
respective worktrees so remaining work can be continued from main after merge.
They are archived planning evidence from **2026-07-27 through 2026-07-30**;
historical validation reports do not prove current environment/device
availability or completed code. QR sender now has separate current verification
and independent review recorded below; the other remaining dossiers remain
planning evidence.
Only planning documents were consolidated; unfinished product code and runtime
state for the three unfinished tracks remain in their original checkouts.
These dossiers are now available from main for reference; implementation stays
in each original worktree until its plan is complete and verified.

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

## Next priority and integration gaps

After the selected client is verified and merged, prioritize **Mobile Core**:
the shared scaffold, recipe parity with the extension, a real synthetic bundle
fixture, bundle decrypt, and Argon2id parity proof. The Argon2 proof must precede
the dependent Keystore master-key/fallback work. The existing Android scaffold
and device bridge can be reconciled in parallel with that prerequisite.

**Extension QR handoff is independent** of OAuth and the core implementation.
Its internal order is QR button, then oversized-bundle chunking. A complete
phone import still needs QR reading/reassembly, compact-payload decoding,
bundle decrypt, storage, and device verification. The QR plan labels
`p-542801dc` as reader/reassembly work, while the newer Mobile Core plan scopes
that item to decrypt only. Reader ownership and acceptance need reconciliation;
neither current slice supplies a complete phone import flow.

**Gradle structure needs reconciliation before combining the mobile tracks.**
The Menu client remains standalone in `mobile/sheets-client/`. Core recovery
uses one repository-root Gradle build and `:shared` in `shared/`, matching the
existing Keystore layout under the historical naming/layout discretion. The
Keystore recovery must combine its Android storage boundary and `:app` with
this verified shared module; it must not replace Core code or create a duplicate
root. Core packaging is verified; the combined app/device behavior is pending.

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
Product commit and main merge remain pending; Core and Keystore priority and
their device acceptance are unaffected by this independent sender completion.

## Preserve existing work and integrate

Main has unrelated tracked/untracked edits. The selected checkout also has
deferred tooling edits; preserve these separately from product commits.
Current overlapping dirty paths are `.gitignore` and `.bee/decisions.jsonl`.
Preserve both ignore-rule groups and distinct existing decision events.

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
  its branch is retained. Three unfinished feature worktrees remain.
- Real Google OAuth client creation and live connection check: **pending**.

Local verification uses mocked Google responses and local loopback HTTP only.
It does not certify a real Google token exchange or Cookbook read. No remote
push was performed. Complete the remaining plans in their existing worktrees,
beginning with Mobile Core and reconciliation of the Keystore scaffold. New
main-based plans wait until all remaining worktrees have been completed.

## Mobile Core current recovery

The accepted Track B slice is implemented in its original worktree and verified
locally: 26 JVM tests, Android packaging, and 22 unchanged extension recipe tests
pass. Independent review verified 56 real extension vectors and the full Unicode
scalar mapping against Chromium 143.0.7499.4; no blocking findings remain.
Commit, main integration, archival and closure remain pending.
See [the Core report](history/passchef-mobile/reports/mobile-core-recovery.md).
