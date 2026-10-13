# Unattended clipboard preparation

Date2026-10-12 (Asia/Saigon). Owner going to sleep; explicit unattended scope,
no biometric/Google prompt. Main13cf9d1; mobile **Partial**. Root alone owns
Windows ADB/Gradle. Two agents authored/reviewed component tests; no Bee.
This is the Oct12 preparation snapshot. [Oct13 actual runtime](clipboard-runtime.md)
subsequently installed this exact test APK and Passed3/3,99.926s; limits remain explicit.

## Results

| Check | Result | Scope |
|---|---|---|
| New component tests | **Prepared**3 | Actual Android clipboard adapter, not Cook/unlock proof |
| Independent review | **Passed**; no P1/P2 | Final181-line/API28+ timing/foreground/cleanup reviewed |
| Final test-APK/lint | **Passed**34s;9 executed/85 up-to-date | Compilation only; lint0 errors/11 warnings/1 hint |
| Test APK installation | **Pending — not attempted** | USB disappeared before first digest read, no copy/install mutation |
| Actual clipboard runtime | **Pending —0 tests executed** | Initially Dozing/keyguard showing; later Windows ADB devices list empty |
| Prior Run04/05 | **Passed**1/1 each | Retained exact earlier APK/runtime evidence; not credited to new APK |
| Existing/Golden/tooling | **Preserved** | Production source and existing tests unchanged; unrelated208/23 byte snapshots rechecked |

Source:`app/src/androidTest/kotlin/dev/passchef/mobile/proof/owned-clipboard-physical-test.kt`.
Class:`dev.passchef.mobile.proof.OwnedClipboardPhysicalTest`. Kotlin filename follows
kebab-case rule; class stays stable. API28+ fixture excludes the older empty-clip
fallback; it does not certify API26/27.

Three prepared assertions:

1. Own synthetic Dish remains readable at real29s, then absent after30.5s. Time
   before/after copy brackets the actual deadline; no virtual clock/forced reconcile.
2. A uniquely marked synthetic replacement survives the old Dish timer.
3. A newer Dish survives the older deadline and clears after its own30.5s interval.

Actual `ClipboardManager` and production `OwnedClipboard` run under the real proof
Activity's foreground management. No manually set foreground, session injection,
jar setup/derivation or authentication. The Cookbook must stay locked and never
create an encrypted proof record. No generated Dish/real jars acceptance is implied.

Every observation requires real RESUMED, interactive/unlocked screen and focused/
attached/shown window; denied reads cannot masquerade as successful clearance.
Proof-only KEEP_SCREEN_ON is bounded by each isolated Activity's lifetime. No device
setting/power/keyguard/biometric policy change. First fixture copy would replace the
global clipboard with synthetic text; the Cookbook's unique directory does not
isolate Android clipboard. Tests never inspect prior text; final cleanup compares
only tracked exact synthetic marker/text and preserves any later unmatched entry.

## Artifact and preservation

New local test APK SHA256:
0c1c35af8fbf230f6c83babd7bc7ebff150ff5b6a51b3f0d253809ee9239c694.
Source SHA256:
7851ab1df75320c022559eef705cf295b5c2a25ee142b4bedfbe7e005fc49269.
Debug APK remainsd4eb8401d0b2cc71f9ca800614dc7f7d8cf391d3e3432cd6a7537ce91382f9a4.

This candidate is **not installed**. The last measured installed test APK was
086e7022... for [Run05](ui-runtime-continuation.md). Last owner Cookbook digest was
verified unchanged after Run05; no nightly phone mutation was performed. `.agents/`
and skills-lock remain untouched/unstaged; original assertions/Golden remain intact.

Root final command (JDK17/SDK36 unchanged):

```sh
./gradlew --no-daemon :app:assembleDebugAndroidTest :app:lintDebug
```

Initial34s build passed; a final34s incremental build after filename/timing refinement
is the definitive source/artifact evidence. No JVM or prior8 platform tests repeated:
production/shared/canonical sources and their earlier test results stayed unchanged.

Evidence:`/tmp/passchef-cookbook-device-acceptance/unattended-clipboard-build.log`,
`unattended-clipboard-final-build.log`, `unattended-clipboard-proof.json`.
No `clipboard-adapter-run-01.log` exists because instrumentation was not invoked.

Resume after real USB re-list/install/digest checks and Awake/keyguard=false. Select
only `OwnedClipboardPhysicalTest`; it requires no biometric/Google. If unavailable,
keep runtime Pending; never wake/unlock an asleep owner's phone or override clipboard
permissions. No whole instrumentation suite, fake grant or repeated8 platform jobs.

All work WIP; no new commit/push/archive. Unresolved: actual clipboard runtime;
historical Run01 cause not reproduced; remaining phone matrix; Android Google setup/
grant/read. Overall mobile remains Partial.
