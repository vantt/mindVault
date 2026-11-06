# Valid mapping at full cache —4November2026

Status: **Coordinator repaired/JVM11 Passed; all four selected emulator UI cases independently Passed**.

The UI advertises20 local sources. `MenuSnapshotStore.save` rejects a new21st
source before committing it. `CookbookSources.confirm` currently catches mapping
validation and durable-save failures together as `INVALID_MAPPING`; therefore a
valid mapping can display “Choose distinct valid columns and a data row” when the
cache is full. Existing favorite/remove operations use `CACHE_FAILED` for storage
failure, with “Could not save local cache; prior data preserved”.

- [x] Locate the actual coordinator/store path and existing immutable unit contracts.
- [x] Add/run an isolated regression with20 real private cache files and a valid
 21st confirmation. Require retained draft, exact previous RAM/disk and cache-error
 status; preserve the pre-repair result.
- [x] Separate mapping/project validation failures from durable-save failures in
 `confirm`, keeping stale-ownership checks and invalid-mapping behavior intact.
- [x] Run relevant app JVM regressions and independent source review, then actual
 isolated UI capacity/proposal cases. Preserve Golden and existing test bytes.
- [x] Record exact candidates/results and synchronize scoped acceptance docs.

Pre-repair targeted Build17 Failed10s:1test/1failure/0errors, expected CACHE_FAILED
but actual INVALID_MAPPING. All draft/RAM/disk-preservation assertions precede that
status assertion. Original source/log/XML retained under
`/home/vantt/.local/share/passchef-mobile-evidence/2026-11-04-cache-capacity/`.
Build18 Passed32s,164tasks/29executed; fresh app JVM11/0failures/0errors/0skips.
Existing tests/Golden bytes unchanged. Independent source review Passed;189 paths
and source ZIP/APKs frozen as candidate18. Actual proposal/limits/capacity UI follows.

UI01 case01 Passed26.155s (tied/sparse proposals and missing/duplicate roles);
case02 Passed23.224s (explicit unique confirmation/literal cells). Case03 Failed
27.455s after three real INVALID_TABLE/preservation checkpoints: accepted field
text remained exact, but the new unmarked test additionally required an unconfirmed
draft to survive an oversized edit. Actual source conservatively invalidates prior
work on editing; this extra assertion exceeds the field/saved-data contract.
Independent diagnosis recorded in the review; no production change for this result.
The corrected new case verifies field/RAM/files immediately, presses actual Preview
again on the same unchanged inputs and then checks the fresh draft. Original failed
artifacts retained; case04 had not started. Native inactive/private namespace empty
and source189 exact were checked after failure.

Build20 Passed11s,159tasks/10executed/149up-to-date; only the two new UI test files
changed. Source189/ZIP and candidate20 archived. App debug and unsigned release are
byte-identical to18, so previous cases01/02 retain the same production artifact.
Retake host20 runs only corrected case03 and previously unstarted capacity case04;
all source/archive/APK guards remain, with actual installed test20 identity verified.

UI02 Passed: table/input limits29.497s native/30.730s host; actual20-source capacity
97.776s/99.013s. Each selected JUnit has exact ordered UUID/PID/status/source-count
phases, normal terminal and `OK (1 test)`; before/after source189/archive/APKs match,
native becomes inactive and its exact private UUID is removed. Initial empty private
namespace is restored after each method. Capacity uses19 real UI confirmations on
top of the real baseline, then a valid21st mapping: all20 existing snapshots/jars
remain exact and the visible storage-failure message is correct. No backend seeding,
fake clock/session or real Google/physical authentication acceptance is inferred.

Independent runtime review Passed for the four scoped acceptances across18/20;
old failed03 remains Failed. Scoped specs/roadmap/changelog/runbook now reflect
actual results. The shared owned API26 VM was then closed with empty private
namespace, actual processes/serial absent and exact temporary data removed.

No Google account, Menu contents or device setting changes are involved. Only
synthetic local cache directories are used. Full Mobile acceptance remains Partial.
