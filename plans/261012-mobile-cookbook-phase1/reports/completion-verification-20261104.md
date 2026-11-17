# Phase1 completion continuation —4November2026

Status: **Local verification Passed; Mobile Partial**. Current working tree verified with its
preexisting cross-platform Cookbook styling. Production source/test assertions
unchanged; the evidenced instrumentation dependency correction is tracked below.
Dates use Asia/Saigon. Historical runtime outcomes retain their original scope.

## Root kitchen and Android build — Passed

JDK17 `/home/vantt/dev-tools/jdk-17.0.20+8`, SDK36
`/home/vantt/dev-tools/android-sdk`; root command:

```sh
./gradlew --no-daemon :shared:jvmTest :shared:assemble :sheets-android:jvmTest :sheets-android:assemble :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:assembleRelease :app:lintDebug --rerun-tasks
```

Terminal exit0,335.520s;182 tasks executed. Fresh XML proves shared78,bridge22,
app10, zero failures/errors/skips. Lint0errors/10warnings/1hint. Android test APK
compilation alone provides no executed Compose/physical acceptance.

Independent tester confirmed170 frozen mobile paths and54 protected baseline
tests/fixtures unchanged, including both explicit Recipe-cooking Golden tests.
No encrypted owner Cookbook or phone contents were accessed.

| Candidate | SHA256 |
|---|---|
| Debug (`dev.passchef.mobile.demo`) | `9fd023f4e98455a326faed00261bb503a280986c0307ecef9fff1e70b98631b8` |
| Instrumentation | `7ee1e7c10cc2e110ca50e1c3b73d0aa02dc0065713a6991e75d5dcf8538088ed` |
| Unsigned release (`dev.passchef.mobile`) | `039e242045131067c9a401dc4342b69d48bafa0f309c00d2d6075a712f4c641d` |

Persistent evidence:
`/home/vantt/.local/share/passchef-mobile-evidence/2026-11-04-plan-completion/`.
Root log/terminal JSON, `tester-root-result01.json`, `root-xml/` and
`tester-root-lint01.xml` record exact source/test/APK identities and counts.

## Standalone Menu client — Passed

Root ran JDK21 `./gradlew --no-daemon build test --rerun-tasks` from
`mobile/sheets-client`: exit0,100.399s; fresh35 tests, zero failures/errors/skips.
No Google smoke tool or credentials involved. Common cases overlap with bridge
tests; counts are not a total of distinct assertions across these two roots.
`tester-standalone-build01.json/.log`, `tester-verification01.json` and
`final-xml/` retain exact terminal, fresh XML and unchanged-source evidence.

## Android runtime — selected locked diagnostics and full UI Passed

Physical ADB inventory was empty. A separate Windows `PassChefCookbook36` API36.1
AVD was created under task-owned temporary storage with fresh userdata/cache,
no SD card/camera/snapshot and headless port5580. Console verifies its exact name
and running state. ADB then reached device/boot-completed1,API36,qemu1,Awake and
keyguard showing=false. The original AVD was not selected for execution. Frozen
candidate APKs are retained under `emulator-candidate01/`; exact debug/test copies
installed successfully, with no PassChef package present before installation.
`emulator-install01.json` binds these identities and fresh instance preflight.

Selected unchanged `CookbookUiDiagnosticsPhysicalTest#realLockedActivityDiagnosticsSurviveStopAndResumeWithoutAuthentication`
completed native JUnit1/1,29.416s, exact selected start/end `[1,0]`, terminal`[-1]`,
anchored `OK (1 test)`. Actual locked Activity stop/resume asserted cleared state
and no encrypted Cookbook without setup. This is isolated-emulator evidence only.

The initial host classifier incorrectly counted diagnostic `sendStatus(0)` bundles
as extra JUnit ends and reported Failed despite the successful native result.
Original log/JSON remain retained. The corrected parser binds class/test/runner/
current/count bundles, then reclassifies the **same raw run**, without a native
retry, in `emulator-locked01-classification02.json`. Fifteen local adversarial
parser probes pass; those probes are host checks, not Android acceptance.

## Instrumentation compatibility repair — build and unchanged native retest Passed

Unchanged full UI Run01 failed natively before its first input: selected JUnit
`[1,-2]`, terminal`[-1]`, `FAILURES`, host21.267s. The initial candidate resolves
Espresso3.5.0 through Compose testing; its event-injection strategy calls removed
`android.hardware.input.InputManager.getInstance`. Native exit0 did not pass.
`emulator-cookbook-ui01.log/.json` and `espresso-dependency-before01.log` retain
the real failure and dependency provenance. No Recipe/cooking assertion was reached.

[Official Espresso3.7.0 release notes](https://developer.android.com/jetpack/androidx/releases/test#espresso_3_7_0)
document replacing that reflection with `getSystemService`. Only
`app/build.gradle.kts` now pins instrumentation Espresso3.7.0, runner/rules1.7.0
and JUnit extension1.3.0. Production dependencies, source, test assertions and
Golden bytes remain unchanged. Build02 Passed194.447s,24 executed/140 up-to-date
tasks, debug/test/release and lint successful (0errors/7warnings/1hint; earlier
0/10/1 remains historical). Its app unit task reuses the earlier
fresh10 result; no new JVM execution is implied by that cached task.

Candidate02 testAPK SHA256:
`91b81eea47fd3c59c6798c3088a02847a86a0bdb1c2632c363edc1125952ba50`.
Debug/release SHA256 remain exactly candidate01. `emulator-candidate02/` and its
JSON retain all three artifacts. The additional build-file delta is explicitly
bound by `app-build-before01.gradle.kts`/`app-build-dependency-delta02.json`;
the original170-source freeze excludes `app/build.gradle.kts` and stays unchanged.
Independent tester artifact verification Passed:170 frozen files including61
Android test sources and54 protected baseline files unchanged; the sole extra
build-file delta matches the reviewed dependency pins/comment. Post-repair
`dependencyInsight` resolves Espresso3.7.0. Exact testAPK update succeeded;
installed debug/test base.apk SHA256 also matches candidate02 exactly.

Unchanged full UI Run02 completed selected JUnit1/1,111.247s native/116.292s host,
exact `[1,0]`,terminal`[-1]`,anchored `OK (1 test)`, no failure/skip/watchdog.
Original setup/password-only fallback, manual Cook/literal Dish,10.5s remask,
invalid/tagged-context refusal, actual offline mapping/cache/search, valid tagged
Cook, username/Dish copy and actual30.5s username preservation, unchanged encrypted
jars and Activity stop/resume lock assertions all executed. Run01 stays Failed;
only the instrumentation dependency correction is accepted by this comparison.
No physical authentication/Google acceptance or production behavior fix implied.
`emulator-cookbook-ui02.log/.json` retain the exact native/host result.

## Selected large-font and secure-pixels observations — Passed

| Unchanged selected case | Native / host | Accepted scope |
|---|---|---|
| VI OS-field association03 |20.781s /28.586s | Exact owned visible/editable/password field and localized descendant-label association;3 attempts, not spoken TalkBack/same-node text |
| EN/VI locked secure pixels04 |12.496s /17.036s | Actual own Activity font2.0,914×411dp landscape/rotation90; both real2400×1080 captures excluded all2,222,920 own content pixels |

Both selected1/1,JUnit`[1,0]`,terminal`[-1]`,anchored `OK (1 test)`, no failure,
skip or timeout. All Android assertions are unchanged. The VI03 requested rotation
and live tool observation must not be confused with its later persisted launcher
portrait snapshot; only observer04 permanently binds actual own landscape state.
Language48dp/visibility metrics in04 are diagnostics, not whole-form acceptance.

Secure04 uses the existing opt-in fixed shell `screencap -p` transport through
`securityDisplayCapture`; bitmaps stay in RAM and are recycled. No image saved,
secure-layer permission granted or window policy changed. Its host runner differs
from the reviewed selected runner only by the fixed capture-transport argument.
Raw/result03/04, `emulator-large-landscape-secure-observation04.json` and independent
`reviewer-emulator-ui03-04.json` retain bounded positive evidence.

[Independent completion review](completion-review-20261104.md) Passed source,
dependency correction, current artifact/install identities and all four selected
emulator outcomes. Original erroneous host classification and actual UI01 failure
stay retained with their respective explanations; no historical outcome overwritten.

## Requirement audit

| Existing phase / deliverable | Current proof / remaining gate |
|---|---|
|01 session-owned manual Cook | Current shared78 + unchanged full UI02; Done locally |
|02 cache/mapping/search | Current shared/app assertions + UI02 real private storage/mapping/search/literal Cook; Done locally |
|03 screens/clipboard/security | Implementation/build and selected emulator observations Passed; physical TalkBack/full layout/natural lifecycle/authenticated clipboard matrix Pending |
|04 real Google connection | Current bridge22/standalone35 and historical scoped provider/network/403 acceptance retained; genuine human cancellation/resultCode/different-Menu/401 and broader physical restoration Pending |
|05 verification/delivery | Fresh full builds, frozen Golden/source/APKs, independent source/artifact/runtime review and documentation updated; overall acceptance cannot close until required03/04 gates are evidenced |

## Remaining delivery

Owned-emulator cleanup completed: selected native processes terminal, exact named
instance stopped, launcher exit0, original QEMU PID absent and ADB serial absent.
Synthetic `no_backup` directory was empty before shutdown. Only the task-created
Windows temporary AVD/artifact copies were removed; retained definitions, candidate
APKs, logs and reviews remain in the persistent evidence root. Original Windows AVD
still exists; final source170/protected54 bytes remain exact. `emulator-cleanup01.json`
and `emulator-stop-request01.json` record these bounded checks.

Docs-manager synchronized spec, roadmap, changelog and acceptance runbook; root
updated this plan and phases01/02/03/05 as each selected step completed. Independent
reviews and completed local steps are linked; no staging/commit/push of unrelated WIP.
Required physical accessibility/layout/lifecycle/clipboard and genuine Google
human-cancel/different-Menu/401 evidence remains Pending. No overall Done/archive claim.

Unresolved: authorized physical phone/owner availability; genuine Google401
transition and cancellation/stale-callback conditions; remaining physical matrix
and retained historical UI/setup/wrap failure causes. Earlier failures were not
erased or explained by the separate Espresso correction.
