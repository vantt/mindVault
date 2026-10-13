# Unattended Cookbook continuation

Date2026-10-12 (Asia/Saigon). Baseline main/pushed13cf9d1. Mobile **Partial**.
No Bee. Two agents: debugger/test author and independent reviewer; root alone
ran Gradle/Windows ADB. No production main/shared/canonical source changed.
This is the earlier unattended snapshot. [Subsequent owner-ready runtime](ui-runtime-continuation.md)
records the newly installed pair, diagnostic pass and interrupted/failed UI attempts.

## Results

| Check | Result | Proof boundary |
|---|---|---|
| Shared JVM | **Passed**78;0 failures/errors/skips | Previous74 +4 new policy regressions |
| App JVM | **Passed**4;0 failures/errors/skips | Production source coordinator/range |
| Fresh packaging/lint | **Passed**114s;174 tasks executed | Shared packages, debug/test/release APKs; lint0 errors/11 warnings/1 hint |
| Final diagnostic compile/lint | **Passed**32s;10 executed/84 up-to-date | Includes new locked-only test; no new JVM/phone execution |
| Independent review | **Passed**; no P1/P2 found | Four shared tests and diagnostic/reflection/lifecycle source |
| Protected bytes | **Passed**208 tracked paths +23 unrelated tooling paths | Main/shared/canonical/extension + existing tests unchanged; scoped git status checked separately |
| Original encrypted Cookbook | **Passed**digest unchanged | Read only digest through authorized USB; no file contents |
| UI Run01 | **Failed**1/1;20.716s | Genuine owner fingerprint; no node after10.5s |
| UI Run02 | **Pending** | No biometric prompt/test started |
| New locked-only diagnostic test | **Pending runtime** | Compiled; phone Dozing/keyguard showing, no wake/unlock/launch |
| Android Google | **Pending** | No confirmed configuration, consent/token or real read |

Root fresh command:

```sh
./gradlew --no-daemon :shared:jvmTest :shared:assemble :app:testDebugUnitTest \
  :app:assembleDebug :app:assembleDebugAndroidTest :app:assembleRelease \
  :app:lintDebug --rerun-tasks
./gradlew --no-daemon :app:assembleDebugAndroidTest :app:lintDebug
```

JDK17/SDK36 paths remain those in [verification](verification.md). Bridge16 and
standalone29 results are retained historical evidence on unchanged sources, not
rerun counts tonight. The8 already-passed physical platform tests were not repeated.

## Source investigation and regression

`CookbookController.hide` only clears presentation (`revealed`, `dish`). It does
not invalidate `readyDish` or `DishLifetime`. The actual panel gate also requires
the current vault guard. Run01 has no pre-failure model/lifecycle evidence, so no
timer/IME/lifecycle cause or production fix is established.

Four new tests use actual kitchen, `DishLifetime` and `SessionClock`: literal Dish,
visibility just before/at10s and at10.5s, retained ready/copy after reveal expiry,
repeated reveal, idle5minute and total10minute refusal despite active reveal/touches.
They do not execute Android Handler/Compose/biometric or reproduce the phone failure.
Existing/Golden tests and exact physical assertions/waits stay intact.

Diagnostic refinements emit only booleans/lifecycle/monotonic timestamps: explicit
`diagnosticStateReadSucceeded`, active/clock-expired and presentation-presence bits.
Lifecycle is captured before reflective reads so failure does not erase that field.
Independent `javap` inspection confirms reflected field names/types in compiled
classes. New `CookbookUiDiagnosticsPhysicalTest` asserts locked-only start/stop/resume
flags using the real isolated Activity; it never calls setup/authentication/Google.

Interpret Run02 only when state-read succeeds:

| Observation | Next investigation; never automatic cause verdict |
|---|---|
| readyDish/lifetime absent | Trace edit/navigation/lock invalidation; same Recipe equality cannot exclude an edit callback |
| unlocked false; stopped/destroyed increased | Correlate actual lifecycle with locking; pause alone does not prove lock |
| unlocked true; ready false | Separate active flag from clock expiry |
| hasDish true; node absent | Trace page/composition/semantics |

Proof-only KEEP_SCREEN_ON remains the earlier reviewed WIP. A passing Run02 under
that environment would not by itself prove a production repair or Run01 cause.

## Local APKs versus installed phone

No ADB mutation/install/runtime job was performed in this continuation. Actual
installed package hashes were re-read; they still match the earlier diagnostic pair.
New local artifacts below **are not installed**; update both with data-preserving
`install -r` and verify original Cookbook digest when owner resumes acceptance.

| APK | Newly built local SHA256 | Still installed SHA256 |
|---|---|---|
| Debug dev.passchef.mobile.demo | d4eb8401d0b2cc71f9ca800614dc7f7d8cf391d3e3432cd6a7537ce91382f9a4 | 0bca9201612f18f03ceb5f08ae2f769c13d2ffdc618189d6f49cbc5cc6dbf4a0 |
| Instrumentation | 91da0f8cbfd83487d9e192aa24117decdd410a0be4372788a53b9d02363a3d95 | abb26371ed9904c20f80c055001bdd805cf52cf8e26de7c1fb5963688ca15702 |
| Unsigned release | 0e7eaf5b37cc65c45bc27cff459d2cdfeba253e6f435c62f8afb511f557d904e | Not installed |

Public signer SHA1 remains3C:FE:96:5E:66:AC:6F:89:4D:C1:A5:07:AA:C9:C3:8D:CF:F5:D2:C9.
Release DEX contains none of the proof Activities/helper/tag; package/permissions
remain correct. Owner Cookbook digest remains
894f73e51e316b6a369368ad4cebbc901609ccfe7eabe855aa73c2cbf4e7a4d6.

Evidence:`/tmp/passchef-cookbook-device-acceptance/unattended-regression-build.log`,
`unattended-regression-proof.json`, `unattended-diagnostic-compile.log`,
`unattended-diagnostic-proof.json`, `unattended-protected-before.sha256`,
`unattended-unrelated-before.sha256`. Reports record facts, not secrets/field contents.

Commit/push awaits evidenced repair and appropriate verification per owner instruction;
main remains13cf9d1. `.agents/`/skills-lock unchanged and unstaged. No archive/Done.

Unresolved: Run01 cause; owner-ready install/diagnostic runtime/Run02; actual Google
configuration/grant/read; remaining phone acceptance matrix.
