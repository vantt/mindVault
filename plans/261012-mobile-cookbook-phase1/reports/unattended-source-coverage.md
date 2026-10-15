# Unattended coordinator and request cancellation —15October2026

Status: **Local coverage Passed; Mobile Partial**. Owner requested work that needs
no phone interaction first. Baseline main1985e3b. No ADB/device/account operation,
biometric prompt, Google grant, clipboard write, radio change or APK install occurred.
All existing production/test sources, Golden/assertions and earlier verdicts retained.

## Added meaningful coverage

Three new test files only, each below200lines:

| File/class | Cases | Exercised boundary |
|---|---:|---|
| cookbook-source-commit-boundary-test.kt / CookbookSourceCommitBoundaryTest | 3 | Already-dispatched confirm/remove must recheck readiness before real-file commit; invalidated refresh cannot overwrite newer accepted cache |
| cookbook-source-mapping-recovery-test.kt / CookbookSourceMappingRecoveryTest | 3 | Header drift requests reconfirmation; invalid duplicate-role mapping preserves prior cache until explicit remap; oversized replacement and real pending-file obstruction preserve disk/RAM and permit recovery |
| stream-cancellation-test.kt / StreamCancellationTest | 6 | Both metadata and values ×2xx/401/403: cancel an incomplete response-body read, propagate cancellation and release body |

App cases use actual CookbookSources/MenuSnapshotStore and real temporary files.
Main/IO scheduling controls queued local work, not Android session deadlines. Pending
IO is reached before readiness/ownership changes. Assertions check exact prior file
bytes and RAM snapshots, busy/status/draft behavior, successful recovery and literal
username00123. Existing shared ownership/mapping/tag/clipboard tests were not copied.

The canonical client cases explicitly use synthetic MockEngine HTTP streams, as
supported by the existing client test boundary. One byte is seeded without EOF;
the test waits for handler entry and consumption, then requires request active/outcome
incomplete before cancel. The exception is captured inside the child into an independent
result; the parent inspects that after join. Awaiting a cancelled job alone would not
prove the observed API exception. Body release and all waits/cleanup are bounded.
These cases test the public client at an incomplete-body boundary, not exact internal
catch-branch execution, Google's consent UI or live authorization errors.

Review considered possible cancellation reclassification in the error-body fallback.
The initial four selected cases Passed7s, and all six final cases Passed in both
build roots. The suspected failure was not reproduced; no production fix justified.
Initial selected source/XML/log are archived separately rather than overwritten.

## Fresh verification

| Root/check | Result | Scope |
|---|---|---|
| App JVM | Passed10,0 failures/errors/skips | Original4 +6 new real-coordinator cases |
| Root Sheets bridge JVM | Passed22,0 failures/errors/skips | Original16 +6 cancellation cases |
| Independent Sheets JVM21 | Passed35,0 failures/errors/skips | Original29 +same6 cancellation cases |
| Root command | Passed9s;10executed/57UP-TO-DATE | App/bridge tests and lint |
| Standalone command | Passed20s;4executed/1UP-TO-DATE | Separate Kotlin1.9.24/JDK21 toolchain |
| Lint | XML0errors/11warnings; HTML1hint retained | No new production or instrumentation build |
| Source/artifact preservation | Passed | All old66 app source/manifest files, existing tracked code, protected208/unrelated23, all three APKs unchanged |

Bridge22 common tests also run inside standalone35; do not sum them as distinct
cases. Standalone adds13 JVM-specific cases. Shared78 and extension147unit/25E2E
remain historical unchanged-source evidence, not fresh execution in this continuation.

```sh
# Repo root, JDK17/SDK36
./gradlew :app:testDebugUnitTest :sheets-android:jvmTest :app:lintDebug
# mobile/sheets-client, independent JDK21
./gradlew test
```

No Android instrumentation ran. No rebuild/install was needed for unchanged debug,
instrumentation or release APKs. Local hashes remain debug1f42a2…d772,
test0d4c2b…993d9, releaseefd3b3…f6b653, matching the prior accepted Run07 artifacts.
Two added app-unit files bring the app source/manifest inventory to68; all prior66
retain their hashes. Frozen07 stays historical. A later physical candidate must
freeze the current inventory rather than overwrite or reuse07 as an exact source set.
The owner Cookbook digest is the last native audit07 value894f73…a4d6; this
local-only continuation did not reread or modify any device file.

## Local release/source inspection

Read the existing release APK manifest and all3DEX files without rebuilding.
Manifest keeps release package/dev.passchef.mobile, backup disabled and no proof
Activities/debug network-state/camera/Autofill permissions. DEX contains neither own
proof-package descriptors nor PassChefProof diagnostic tag. Source backup rules
exclude root/file/database/sharedpref/external from cloud and device transfer.
Release WrapFailureProbe source remains no-op. Inspected token paths use RAM and
request authorization headers; no token logging/persistence sink was observed in
those paths. This bounded source/artifact inspection is not runtime log-absence,
platform clipboard/history or all-device security acceptance.

## Current checklist and remaining work

Updated current plan/spec/product-status summaries that still called actual Google
read, controlled cold or screenshot shortcut wholly Pending. Selected provider read,
network-off, controlled offline/provider cold and owner shortcut07 retain their
recorded Passed scope. Historical reports/failures remain historical, not rewritten
as newly executed or repaired cases.

Remaining owner/device acceptance: actual Google cancellation/stale SDK callback/
401/403; wrong-Menu tagged UI/broader mapping UI; screen-off/long-background/natural
LMK/saved-task restoration; authenticated/background/cross-app clipboard observations;
TalkBack/large-font/orientation/full touch-target coverage. Current-Xiaomi Keystore
acceptance stays closed; other API/hardware conditions remain a separate coverage list.
QR receiving, Autofill, Drive/multiple profiles and authoring remain later work.

Evidence outside repo:
`/tmp/passchef-cookbook-device-acceptance/unattended-source-20261015-01/` contains
baseline/static audit, release manifest, initial cancellation candidate/log/XML,
root/standalone logs, three source snapshots, XML per root and final-local-audit.json.
Previous frozen candidates/logs were not overwritten. Independent checks are recorded
in [review](independent-review.md); overall evidence in [verification](verification.md).

Unresolved: original UI/setup/wrap/cold/Run06 causes and remaining live/device matrix.
