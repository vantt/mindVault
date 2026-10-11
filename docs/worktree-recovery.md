# Mobile foundation worktree recovery

Updated: 2026-10-11. Snapshot compares the four feature branches with local
`main` at `ef720b1`; this report records recovery work in progress.

## Worktrees and their plans

| Worktree branch | Latest relevant plan | Recorded progress | Recovery status |
|---|---|---|---|
| `wt/passchef-mobile-oauth-sheets` | [Track D: OAuth/Menu client](history/passchef-mobile-oauth-sheets/plan.md), approved 2026-07-28 | Five commits beyond main; independent client, setup guide, JVM connection-check tool | Selected; product repairs, current validation, and merge pending |
| `wt/passchef-mobile` | [Track B: Mobile Core](history/passchef-mobile/implement-plan.md), updated 2026-07-28 | Five planned tasks; no feature commits beyond main | Next product priority; recipe parity, bundle decrypt, Argon2 proof pending |
| `wt/passchef-mobile-android-keystore` | [Android Keystore + biometric unlock](history/passchef-mobile-android-keystore/plan.md), approved 2026-07-30 | Scaffold commit `a1c57c9`; physical-device bridge verification recorded as blocked | Real device verification and later storage/unlock implementation pending |
| `wt/qr-handoff` | [Extension QR handoff](history/qr-handoff/plan.md), approved 2026-07-27 | QR button and animated chunking planned; no feature commits beyond main | Independent extension work; implementation pending |

The linked Markdown planning dossiers are preserved unchanged here from their
respective worktrees so remaining work can be continued from main after merge.
They are archived planning evidence from **2026-07-27 through 2026-07-30**;
current implementation readiness has not been certified. Historical validation
reports do not prove current environment/device availability or completed code.
Only planning documents were consolidated; unfinished product code and runtime
state remain in their original checkouts. This report will remain in main when
the selected branch is integrated.

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
The Menu client lives in standalone `mobile/sheets-client/` with its own wrapper
and settings. Mobile Core plans `mobile/shared/` under a new `mobile/` Gradle
root. Keystore currently scaffolds root-level `:app` and `:shared`. Do not assume
these three structures already form one Android application or share compatible
plugin/toolchain versions. Keep the Menu client standalone for this recovery.

The Keystore plan requires a physical Android device; a JVM test or scaffold
build alone does not prove biometric or hardware-backed storage behavior.

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
- Local merge into main and restored-work preservation check: **pending**.
- Recovery archive and selected worktree closure: **pending**.
- Real Google OAuth client creation and live connection check: **pending**.

Replace these placeholders only with observed results and recorded commit/ref
identifiers. Historical progress above does not substitute for current checks.
