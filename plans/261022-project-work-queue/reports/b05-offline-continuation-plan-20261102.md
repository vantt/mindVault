# B05 — offline helper continuation plan

Date:2026-11-02, Asia/Saigon. **Scoped preparation; B05 remains Partial.**
Parent:[Track03 B05](../phase-03-website.md#task-b05).
Retained [slot01](b05-local-qa-runtime-20261102.md) and
[independent review](b05-local-qa-runtime-20261102-independent-review.md)
keep W01–W06 NotRun and the historical early-skip cause Unknown.

## Authorized next sequence

1. Test actual declarations extracted from immutable revision04 in an isolated
   Node VM; never evaluate its top-level timers, directory creation or launch body.
   Exercise budget cutoff before/at/after, fatal and cleaning exclusions, event
   persistence, pre-cell fatal, cleanup reserve, successful/failed/timed-out cells,
   post-cell fatal, and final-state serialization. Check actual `/proc/self/stat`
   parsing against current process identity. No browser/server/process spawning.
2. Record actual commands, assertions/results and helper identity. If a defect is
   demonstrated, repair only that diagnosed helper behavior in a separate revision;
   preserve old runner/cases/result bytes. Syntax-check and rerun affected checks.
   Test dependencies are injected only into the offline harness, not website code
   or canonical tests. Contract checks do not establish runtime guard effectiveness.
3. Independent reviewer reads source/diff and verification evidence. Resolve
   actionable findings; then record a frozen helper/cases identity and outstanding
   live canaries. Update report → Track03 B05 → catalog → master/index.
4. Continue to actual B05 runtime only after a fresh exclusive desktop handoff.
   Old slot expired. Bind new deadlines/candidate, prove guarded navigation/canaries,
   run selected cells, verify owned-process/port/profile cleanup, review independently,
   and update plan before any next eligible task.

## TODO and exit criteria

- [x] Tester executes offline behavioral contract checks; [revision04 evidence](b05-offline-helper-verification-20261102.md):15 Passed/1 Failed, cleanup rejection suppresses final result.
- [x] Demonstrated cleanup/result-loss and surviving-parent exit0 defects repaired in separate revision06; [unchanged36 assertions Passed](b05-helper-repair-verification-20261102.md), old failures preserved.
- [x] [Independent source/evidence review Passed](b05-helper-repair-independent-review-20261102.md); final bookkeeping review Passed270links/46IDs/46anchors/master79lines.
- [x] B05 report, Track03, catalog, master and index updated; fresh-slot/runtime gate retained.
- [x] Fresh slot02 runtime/cleanup/review completed with [actual failures and scoped passes](b05-local-qa-slot02-20261102.md); B05 remains Partial for residual overflow and broader acceptance.

At the original offline checkpoint, helper preparation was verified/reviewed and
website QA was NotRun. Subsequent slot02 actual outcomes remain scoped below.
Do not claim a parser defect from escaped tool display: root's actual isolated
execution found50 fields and valid start/group values. Historical Run03 predicate
inputs were not saved; neither successful offline checks nor a diagnosed new issue
can retroactively assign its cause.

## Remaining dispatch gates

No autonomous A task currently Ready. A11 requires C06 transition selection;
A13 remains held for C01/A23 stable-candidate disposition. A14 needs candidate,
captures/artifact acceptance and C04/C05 details; A22 needs published listing and
C05/C11. A15–A20 remain Later. B02 remaining native/edge cells and B04/B05 need
resource slots; B06 needs stable candidate. Replaying accepted B01/B03/B07/local
B08 cells adds no missing coverage. C03 real Sync stays owner-deferred and mandatory
before publication; mobile owner/hardware/account/publication gates unchanged.

## Unresolved

Historical early-skip cause; residual W01 mobile overflow and reviewed diagnostic10 fresh slot; broader B05 matrix and owner/candidate/deployment gates.
