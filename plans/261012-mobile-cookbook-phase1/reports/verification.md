# Independent Cookbook verification

Status: **Local code verification passed**; selected device acceptance Passed,
broader owner/device matrix Pending. Historical local snapshot below retained.
Current [session timer preparation](session-timer-runtime.md): test-APK/lint builds
42s/13s/11s Passed, lint0/11/1; no fresh JVM execution. Idle Run01/02 Failed before
baseline pointer and Run03/04 Failed at setup; Run05 actual5-minute idle Passed1/1,
341.722s. Total Run01/02/03 setup Failed182.309s/182.279s/182.138s,10-minute Pending;
foreground proof KEEP_SCREEN_ON. Total02 correlated code15 wrap cause unknown;
Total03 no opened session, no timer measurement or cause inferred.
Date:2026-10-11 UTC. Reviewer agent A is independent of product author B.

## Final results

| Check | Result |
| --- | --- |
| Shared JVM | 74 tests: original56 + builder5 + independent13; zero failures/errors/skips |
| Canonical Sheets through Android bridge JVM | 16 tests: original9 + new7; zero failures/errors/skips |
| Production app coordinator/range JVM | 4 tests: deferred IO/cache/auth busy boundaries and quoted/grid-bounded ranges; zero failures/errors/skips |
| Standalone Sheets JVM21 | 29 tests: original22 + new7; zero failures/errors/skips |
| Root packaging/lint | Exit0;87s;179 tasks executed; shared/bridge AAR/JAR, debug/test/release APKs built |
| Lint | 0 errors,11 warnings,1 hint |
| Final source/build stability | 99 paths byte/mode unchanged before/after; all51 builder frozen source hashes match |
| Protected baseline | 75 original tests/fixtures/recipe/crypto/vault paths unchanged |
| Extension/unrelated preservation | 128 extension paths byte/mode unchanged;23 Cave/skills-lock hashes unchanged |
| New Kotlin size | 27 new files, all<200 lines; max193 independent regression file |

Shared74 was independently rerun before the final app/client delta and remains valid:
all shared sources/tests stayed unchanged. Bridge/app and standalone were rerun after
the actual streaming-client repair. These separate roots reuse common protocol tests;
the counts are not a claim of distinct test cases across all roots.

## Actual commands and environment

Root JDK17:`/home/vantt/dev-tools/jdk-17.0.20+8`; SDK36:
`/home/vantt/dev-tools/android-sdk`.

```sh
./gradlew --no-daemon :shared:jvmTest :sheets-android:jvmTest --rerun-tasks
```

Initial independent shared run exit0;38s;11 tasks executed. Its bridge15 result is
superseded by the final bridge16 rerun after adding non-EOF streaming regression.

```sh
./gradlew --no-daemon :shared:assemble :sheets-android:assemble :sheets-android:jvmTest :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:assembleRelease :app:lintDebug --rerun-tasks
```

Final root exit0;87s;179 tasks executed. XML counts and lint XML independently parsed.

Standalone JDK21.0.12:`/home/vantt/projects/passMan/.toolchain/jdk`;
working directory:`mobile/sheets-client`.

```sh
./gradlew --no-daemon build test --rerun-tasks
```

Final standalone exit0;21s;8 tasks executed,29 tests. Original loopback/configuration
and portable read/RAW append/update regressions pass. No real smoke tool invoked.

Evidence outside repository:
`/tmp/passchef-mobile-independent-jvm.json` and `.log`,
`/tmp/passchef-mobile-independent-final.json` and `.log`,
`/tmp/passchef-mobile-independent-standalone-final.json` and `.log`.
Earlier standalone28/bridge15 artifacts describe intermediate source only.

## Actual APKs and release isolation

| APK | Package | Bytes | SHA256 |
| --- | --- | ---: | --- |
| Debug | dev.passchef.mobile.demo | 18416447 | 32454b5fc7e3a72ab75a2663604f6d822bdbbccdb95ff8b20e1510f32eb0f739 |
| Instrumentation | dev.passchef.mobile.demo.test | 1146149 | d475460cd54a1a68823a1469a3819a1c5a948a8394993d5796c6db5c53d6927c |
| Unsigned release | dev.passchef.mobile | 14176301 | 84f788db2f0fbc56c39dfc1bf7622130134bb9a5388770f6730ec7e4dcc3b820 |

Actual manifest/DEX checks confirm release contains no BiometricProofActivity,
CookbookProofActivity, PassChefProof log tag or desktop loopback/smoke classes.
Release diagnostics helper remains a no-op that does not invoke the key inspector.
Debug proof activities remain unexported. Debug and release disable backups.
Release permissions:INTERNET,USE_BIOMETRIC,USE_FINGERPRINT and the app's generated
DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION; no camera/Autofill/Accessibility permission.
New production Cookbook/worker source has no print/log or token persistence path.

Lint warnings concern existing target/plugin/dependency versions and missing app icon;
the hint concerns boxed Compose state. They are recorded, not silently treated as zero.
Final independent debug/test APKs differ from prior builder packages; use the hashes
above for future device acceptance. Unsigned release matches the builder hash.

## Proof limits and verdict

HTTP mocks prove protocol behavior, ownership/close and non-EOF byte-budget refusal.
The injected timeout case proves exception propagation, not a real20second deadline.
JVM cache tests exercise actual private filesystem failure/corruption; app tests use
actual production coordinator with deferred IO. They are not Google consent proof.
Android UI test APK compiles a genuine owner-authenticated, storage-isolated harness;
**it was not executed**. No phone/emulator, OAuth grant, real provider read, clipboard,
biometric, reveal timer or lifecycle/UX physical job ran in this verification.
Owner configuration/physical acceptance remains Pending; mobile Phase1 is Partial.
Local checks do not imply Android CI coverage. No unresolved P1/P2 found in code review.

Extension [recheck](../../reports/261012-extension-recheck.md):147 units and25 E2E
passed on unchanged extension source. It grants no mobile live-provider proof.

## Continuation after main13cf9d1

[Unattended regression](unattended-regression.md) records fresh shared78/app4,
packaging/lint and additional diagnostic compilation. Those APKs were not installed
in that earlier snapshot; [later runtime evidence](ui-runtime-continuation.md) records
their data-preserving installation and locked diagnostic1/1 pass. [Device acceptance](device-acceptance.md) supersedes the historical
not-executed UI status above: actual Run01 failed after genuine fingerprint;
Run02/03 failed, Run04 passed selected foreground flow. Normal-screen-policy
comparison subsequently Passed Run05 1/1,53.663s after verified reconnect/unlock.
Remaining Google/phone proof and historical Run01 cause stay open.
No production fix claimed.

[Clipboard adapter runtime](clipboard-runtime.md),2026-10-13: exact prepared test APK
installed with data preservation; actualAPI36 Passed3/3,99.926s,0 skips/failures.
Real foreground untouched30s cleanup and replacement/newer ownership proven;
no authenticated Cook/Google/background/recreation/API26 fallback proof added.

Oct13 continuation: [clipboard Activity recreation](clipboard-recreation-runtime.md)
Passed1/1,31.721s on Xiaomi/API36. Actual old Activity destruction/new identity and
original29s retention/30.5s clearance asserted, with locked unique proof storage.
Final test-APK/lint Passed33s;0 errors/11 warnings/1 hint. This is same-process
adapter proof; authenticated recreation/background/process death remain Pending.

Oct13 owner reports Android OAuth client configured. New isolated real-Google
acceptance fixture reviewed/compiled: test APK/lint Passed59s,0 errors/11 warnings/
1 hint. [Preparation evidence](google-acceptance-preparation.md) separates new local
APK from the installed recreation candidate. Google device runtime0/Pending: dedicated
test Menu URL and ready owner awaited; no grant/read or new installation attempted.

Later [Google runtime](google-runtime.md): prepared5eb335 test APK installed with
owner digest preserved. Actual Google Run01 Failed1/1,44.766s at metadata; owner
confirmed genuine account/consent and absent expected Test tab, then renamed it.
Unchanged Run02 Failed1/1,19.259s at values with actual draft present. Usable
authorization/provider metadata/values and selected Test tab observed; fixed row/
origin/schema assertion not yet resolved, mapping/cache unaccepted. No production
repair inferred; forthcoming diagnostics retain every assertion.

Schema diagnostics subsequently reviewed/compiled Passed59s,0/11/1 lint. New11
boolean flags/fine phases retain all17 original assertions byte-identical; new
candidate cc4d5bbc is local only. Owner reports only A1/A2 populated; correct the
dedicated fixture and confirm readiness before another authentication/runtime.

After owner updated the fixture, exact refined candidatecc4d5bbc installed with data
preserved; **Google Run03 Passed1/1,23.309s**,0 errors/failures/skips. All11 match flags
true and original17 assertions pass actual metadata/tab/rows/explicit mapping/cache
file load plus unchanged fake jars. Owner digest unchanged after install/runtime.
Independent reviewer confirmed JUnit [1,0], diagnostic2 separate, exact source/APK
and scope. Google writes/errors/UI cache reopening remain unproven.

Latest [Google cache UI continuation](google-cache-ui-runtime.md): initial88s build
Passed fresh app4; shared78/bridge16/standalone29 retained, not freshly rerun. Corrected
helper sync build40s Passed, lint0/11/1; production unchanged. Default proof-storage
recreation Passed1/1,31.769s. Cache UI Run01 Failed1/1,294.651s; corrected Run02
Passed1/1,42.393s with exact provider cache Cook/UI/favorite/recent/search and genuine
biometric same-process authenticated recreation. All assertions retained; exact
source/APK/install/digest histories in linked report. No whole-mobile completion.

Latest [network-off acceptance](network-off-runtime.md): initial55s build and153-line
diagnostic helper81s build Passed, lint0/11/1; app4/shared78/bridge16/standalone29
retained (no fresh JVM execution). Exact diagnostic source/APKs unchanged across
Run02/03/04; no repeat build/install for Run03/04. Run01/02/03 Failed history retained;
Run04 Passed1/1,31.477s,0 failures/errors/skips with actual network absence, retained
provider NETWORK_FAILED preservation and all unchanged offline cache UI/recreation
assertions. Original17/cache150 untouched, owner digest unchanged; mobile Partial.

Unresolved: original Cookbook UI Run01 reveal failure, network Run03 key-wrap cause;
cancellation/stale callbacks/401/403/cold-start/process-death and remaining phone matrix.
