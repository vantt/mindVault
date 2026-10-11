# Validation Report: passchef-mobile-android-keystore — slice E1+E2

## Reality Gate Report

```
REALITY GATE REPORT
Mode: high-risk
Current work: scaffold :app+:shared Gradle/KMP project (E1) and bridge the Windows-host real
  Android device's adb into WSL2 (E2)
MODE FIT: PASS       — 4 risk flags (auth, audit/security, external systems, cross-platform);
  no smaller lane honestly protects this work
REPO FIT: PASS       — zero prior Android/Kotlin code confirmed by exploring's scout; no
  false "existing pattern" assumed anywhere in plan.md/approach.md
ASSUMPTIONS: PASS    — all blocking assumptions listed in the feasibility matrix below
SMALLER PATH: PASS   — E1+E2 are the two no-dependency epics; nothing smaller unblocks E3
PROOF SURFACE: PASS  — both cells' verify commands were run literally by the orchestrator
  and produce the exit code their must_haves require (see matrix)
Decision: proceed
Evidence: see Feasibility Matrix
```

## Feasibility Matrix

| Assumption | Risk | Proof Required | Evidence | Result |
|---|---|---|---|---|
| A real JDK+Android SDK can be installed in this WSL2 worktree without root | Medium (no sudo access confirmed: `sudo -n true` → password required) | Real install + `java -version`/`adb version` | Installed Temurin JDK 17.0.20, Android cmdline-tools, platform-tools, platforms 34/36, build-tools, all user-space under `~/dev-tools`; `java -version` and `adb version` both ran clean | READY |
| `com.android.kotlin.multiplatform.library` (AGP 9) syncs and builds cleanly with Gradle 9.1.0 | High (brand-new plugin shape, zero local precedent) | Real `./gradlew :app:assembleDebug` on a scaffolded two-module project | `.bee/spikes/passchef-mobile-android-keystore/`: first attempt with AGP 9.3.1 failed (`NoClassDefFoundError: org/gradle/features/binding/ProjectTypeBinding`); pinned to AGP 9.0.0 → `BUILD SUCCESSFUL`, real APK at `app/build/outputs/apk/debug/app-debug.apk` | READY WITH CONSTRAINTS (AGP pinned to exactly 9.0.0) |
| `:app` can apply the legacy `org.jetbrains.kotlin.android` plugin alongside AGP 9 | — | Same spike build | Build failed: "the 'org.jetbrains.kotlin.android' plugin is no longer required for Kotlin support since AGP 9.0" — must NOT be applied | READY WITH CONSTRAINTS (constraint encoded in cell) |
| Compose UI (per D2/approach.md) builds on this exact stack | Medium (not covered by the first green build) | Add Compose deps to the spike, rebuild | First Compose attempt failed: androidx Compose BOM 2026.06.01 requires `compileSdk >= 36`; bumped both modules to `compileSdk 36`, installed `platforms;android-36` → `BUILD SUCCESSFUL` with a real Compose `ComponentActivity` | READY WITH CONSTRAINTS (compileSdk 36, not 34) |
| `adb.exe` is available on the Windows host for the `ADB_SERVER_SOCKET` bridge (D6) | Low | Locate the binary via WSL2 `/mnt/c` interop, invoke it | Found at `/mnt/c/Users/van.tran_fgorg/AppData/Local/Android/Sdk/platform-tools/adb.exe`; `adb.exe version` ran successfully from WSL2 (interop confirmed) | READY |
| A real device is currently attached for E2's device-state proof | — | `adb devices` | None attached right now (`List of devices attached` empty) — this is expected; E2 cannot cap until the user connects the device. Not a plan defect. | OUTSTANDING (user action required before E2 caps) |

## Spike

One spike, `.bee/spikes/passchef-mobile-android-keystore/`, answered: "does the AGP-9-era KMP module split + Compose actually build in this environment?" — **YES**, with three real constraints discovered and folded back into cell `android-keystore-e1`: pin AGP to exactly 9.0.0 (not 9.3.1), do not apply `org.jetbrains.kotlin.android` on `:app`, and use `compileSdk 36` (not 34) once Compose is added. Disposable spike code stays under `.bee/spikes/`; the real cell will scaffold the actual project files at the repo root.

## Plan-Checker Persona Panel (high-risk)

Ran coherence, feasibility, security, and a cold-pickup cell review in parallel (iteration 1), all against the cells as first drafted. Combined findings, deduped:

- **BLOCKER** — `android-keystore-e1` verify command was not runnable literally (missing `JAVA_HOME`/`ANDROID_HOME`/`PATH`). **Fixed**: verify now embeds the exact env prefix; re-ran it literally, `BUILD SUCCESSFUL`.
- **BLOCKER** — `android-keystore-e1` `files` omitted `local.properties`, `gradle.properties`, and the `shared/src/androidMain/.../KeystoreCrypto.android.kt` `actual` required to match the `expect` (unmatched expect is a Kotlin compile error). **Fixed**: all three added.
- **BLOCKER** — `android-keystore-e1`'s must-have truth ("installs on a connected device/emulator") was circularly dependent on `android-keystore-e2` (which depends on e1) and used forbidden "emulator" wording (D6 explicitly bans a WSL2 emulator as a hardware-path substitute). **Fixed**: e1's truths are now build-only (exit 0 + APK exists); the install/launch proof moved entirely to e2, which has device access.
- **BLOCKER** — `android-keystore-e2` verify (`adb devices`) exits 0 even with zero devices attached — vacuously satisfiable. **Fixed**: verify is now `adb devices | grep -qw device`, confirmed to exit 1 right now (no device attached) and would exit 0 only with a real device in `device` state.
- **BLOCKER** — `android-keystore-e1` demanded a "real Compose Activity" from a spike that, at review time, had no Compose dependency at all. **Fixed**: extended the spike to add Compose for real (see Feasibility Matrix), re-proved `BUILD SUCCESSFUL`, and updated the cell to cite the compileSdk-36 constraint this uncovered.
- **WARNING** (2nd pass) — cell action referenced a `gradle/libs.versions.toml` version catalog the spike never actually used (pins are inline). **Fixed**: action corrected to state inline pinning, matching what was actually proven.
- **WARNING** (2nd pass) — `gradlew`/`gradlew.bat` not explicitly in e1's `files` (only the wrapper jar glob). **Fixed**: both added explicitly.

Iteration 2 (single reviewer, targeted at confirming every fix against the live cells + a fresh literal re-run of both verify commands) reports all five original BLOCKERs **CONFIRMED FIXED**, both new WARNINGs also fixed, and no new BLOCKER introduced. Two minor items left as accepted, non-blocking: e2's `verify` command only automates the device-state truth; the install+launch truth is evidenced by the worker's recorded `adb install`/`adb shell am start`/`logcat` output and the `adb-bridge-setup.md` artifact at cap time, not a second automated command — acceptable per cell quality rules (a `verify` command gates capping; a must-have truth may also be closed by recorded evidence).

## Cell Review

```
CELL REVIEW REPORT
Work: E1 + E2 (scaffold + adb bridge)
Cells reviewed: 2
CRITICAL FLAGS: none open (5 found in iteration 1, all fixed and reconfirmed in iteration 2)
MINOR FLAGS: none open (2 found in iteration 2, both fixed)
CLEAN CELLS: android-keystore-e1, android-keystore-e2
SUMMARY: Both cells now carry literally-runnable verify commands (re-run by the orchestrator,
  real exit codes recorded above), a complete file list matching the proven spike, and
  must-have truths that are each provable at the cell's own point in the dependency chain.
```

## Schedule

`node .bee/bin/bee.mjs cells schedule --feature passchef-mobile-android-keystore --json` → 2 waves, `[android-keystore-e1]` then `[android-keystore-e2]`, zero cycles, zero unsatisfiable deps.

## Decision

**READY WITH CONSTRAINTS.** The constraints (AGP pinned to 9.0.0, no `kotlin-android` plugin on `:app`, `compileSdk 36`) are encoded directly in `android-keystore-e1`'s action/must_haves — not left as prose the swarming worker has to rediscover. The one outstanding item — no Android device currently attached — blocks only `android-keystore-e2`'s cap, not e1's, and is a real-world precondition (plug in the device), not a plan or cell defect.

```
VALIDATION COMPLETE - APPROVAL REQUIRED BEFORE EXECUTION
Mode: high-risk
Work: current slice (android-keystore-e1, android-keystore-e2)
Reality gate: PASS
Feasibility: READY WITH CONSTRAINTS
Structure: PASS after 2 iterations
Spikes: passed (AGP 9.0.0/Gradle 9.1.0/Kotlin 2.4.10/Compose-36 combo proven; constraints folded into e1)
Cell review: PASS (2 cells, 0 CRITICAL open)
Unresolved concerns: no Android device currently attached to the Windows host (blocks e2's cap only; user action, not a plan defect)
```
