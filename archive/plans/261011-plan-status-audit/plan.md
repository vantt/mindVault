# Audit plan phases and archive completed work

Date: 2026-10-11. Owner requests every plan/phase status updated and completed
work archived under repository-root `archive/plans/`. Documentation-only scope.
Status: Done — phase/relevance audit, archive migration and review complete.
Owner's 2026-10-11 instruction: archive a whole plan only when no relevant
unfinished outcome remains; otherwise retain it and rank the remaining work.

## Status rules

- Done: accepted outcome implemented and supported by actual verification.
- Partial: some named phase outcomes exist; list each remaining deliverable.
- Pending: accepted outcome has no implementation/verification evidence yet.
- Deferred: explicitly excluded future platform or follow-up, never a pass.
- Superseded: historical implementation mechanism replaced by a verified route.
- Preserve dates, locked decisions, failures and original acceptance criteria.
  A historical unchecked box is reconciled against current proof, not copied.

## Execution and TODOs

1. [x] Inventory every plan, phase file and embedded roadmap; include existing
   `plans/archive/`, mobile history and five 2026-10-11 recovery/removal plans.
2. [x] Map every named phase/epic/checklist group to actual source, reports and
   accepted scope. Produce an unfinished-work list before moving anything.
3. [x] Mark verified bounded scopes Done: Core Android/JVM; OAuth client/runbook
   code; QR extension sender; owner-accepted current-Xiaomi Keystore; tooling removal.
4. [x] Keep live Google connection, production phone screens/editing, QR reader,
   Drive, autofill/domain mapping and Native/iOS clearly pending or deferred.
   Keep unexecuted Keystore device/timing matrix separate from accepted completion.
5. [x] Archive wholly completed/superseded plans under `archive/plans/`.
   Return incorrectly archived mixed-status plans to `plans/`; obsolete mechanisms
   are Superseded within the active plan, without discarding relevant verification.
6. [x] Update status headers/checklists and a concise plan index; preserve original
   planning narrative and link current verification/accepted scope.
7. [x] Move selected completed artifacts with their needed reports; repair local
   links in current docs, indexes and moved artifacts. Preserve historical records.
8. [x] Independently review every status and archive mapping; fix evidence/link
   mistakes. Root owns scoped commit, push and final remote confirmation.

## Evidence and boundaries

- Core: `archive/plans/passchef-mobile/reports/mobile-core-recovery.md`;
  26 Core JVM tests, real extension fixtures/WASM and Chromium parity oracles.
- OAuth: `docs/worktree-recovery.md` and accepted original D4;22 client tests;
  real Google Console setup/live read remains the owner's separate follow-up.
- QR: `archive/plans/261011-qr-handoff-worktree-recovery/reports/verification.md`;
  37 scoped unit tests and2 headed canvas-decoding E2E; no phone reader claim.
- Keystore: `archive/plans/261011-keystore-recovery/reports/current-device-acceptance.md`;
  owner quote accepts Xiaomi/API36 limits;56 integrated JVM tests/150 build tasks.
- Removal: `archive/plans/261011-remove-bee-tooling/reports/verification.md`;
  77 extension unit tests, preserved product/history/assets and record comparison.
- Current integration: `docs/worktree-recovery.md`; all four original feature
  checkouts closed; tooling-removal commit `b5be3b0` follows merged mobile source.
- This audit does not run Bee, change product source/golden tests, access devices
  or credentials, or turn compile/unit proof into hardware/API acceptance.

## Verification

- Ensure every inventoried named phase appears once with status and evidence.
- Check all moved paths/local links and canonical archive destination; no orphan
  reports, dropped unresolved TODOs, duplicate active plans or false completion.
- Source/build/golden hashes preserved; fresh77-unit check and whitespace pass.
  [Verification/review evidence](reports/verification.md) records scope and limits.
- Review final archive inventory and unresolved-work index before commit/push.

## Decisions

Owner asks to recheck missing phases against code and later decisions before
archiving. Prioritize next actionable slices; a release phase still depends on
verification/landing work even when its parent roadmap has high-priority security.
