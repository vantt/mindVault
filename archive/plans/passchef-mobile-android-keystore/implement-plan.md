---
artifact_contract: bee-implement-plan/v1
feature: passchef-mobile-android-keystore
lane: high-risk
status: Done
accepted_scope: owner-accepted Xiaomi/API36
historical_status: Ready for Review
updated: 2026-07-27
sources: [archive/plans/passchef-mobile-android-keystore/CONTEXT.md, archive/plans/passchef-mobile-android-keystore/discovery.md, archive/plans/passchef-mobile-android-keystore/approach.md, plan.md]
decisions: [D1, D2, D3, D4, D5, D6]
---

# Implementation Plan: Android Keystore + Biometric Unlock

**Status audit — 2026-10-11: Done within accepted Xiaomi/API36 scope.**
E1–E5 completed; [audited plan](plan.md) and
[device acceptance](../261011-keystore-recovery/reports/current-device-acceptance.md)
record actual proof and unexecuted coverage. Scaffold/build/device availability
questions below are historical and resolved. The projected TCP bridge was
Superseded by native Windows `adb.exe` from WSL; no TCP success is claimed.
The duplicated future-slice lists are preserved planning history, now completed
within the accepted scope. Full matrix/phone UI remain outside that completion.

> Human-layer projection of the truth artifacts. Truth lives in CONTEXT.md
> (decisions), plan.md + cells (work), and the validating report (evidence).
> Feedback on this document flows back to those artifacts, then this re-renders.

## 1. Goal

Prove that a device's secret (the Argon2id-derived master key, and through it the user's ≤5 recipe secrets) can be stored on Android without ever touching disk in plaintext, unlockable only by the device owner's own biometric — the foundation the full 6-screen mobile app (`p-c46b217c`) will later build on.

**Success looks like**
- A Keystore AES/GCM key wraps the master key; unwrapping requires a real biometric prompt (D5).
- Losing biometric access (re-enrollment) or repeated failure never loses the vault — master password re-entry recovers it (D3).
- Every claim above is proven on a real Android device, not simulated (D6).

## 2. Current State

No Android or Kotlin Multiplatform code exists anywhere in this repo today — only `chrome-extension/` (the browser extension, with its own `argon2_adapter.js`) and `landing-page/`. This WSL2 development environment has no JDK, no Android SDK, and no `adb` installed (confirmed by direct check). Product architecture for this exact mechanism (master-key + biometric wrap, KMP module shape, Phase-1 unlock UX) was already settled in `docs/mobile-app-brainstorm-qa.md` before this feature's exploring session — restated, not re-derived, in CONTEXT.md D1-D6.

## 3. Scope

**In scope**
- Installing a real JDK + Android SDK + platform-tools in this WSL2 worktree (D1).
- A two-module Gradle project — `:app` (Android entry, one Compose screen) + `:shared` (KMP library, expect/actual crypto boundary) (D2).
- Keystore AES/GCM key wrapping the Argon2id-derived master key, gated by `androidx.biometric` BiometricPrompt + CryptoObject, `minSdk 26`, StrongBox opportunistic behind an `SDK_INT >= 28` guard (D4, D5).
- Master-password fallback on key invalidation or repeated failure (D3).
- Bridging the Windows-host-attached real Android device's `adb` into WSL2, and running every verify against it (D6).

**Out of scope**
- The full 6-screen app UI (`p-c46b217c`).
- Autofill / domain-package mapping (later roadmap phase).
- iOS/Keychain target (no Mac available, per already-locked Q8).
- QR handoff, Drive sync, Sheets OAuth (separate PBI tracks).
- Argon2id KMP library selection and its cross-platform parity check — owned by spike PBI `p-c2ad072e`.

## 4. Proposed Approach

Two Gradle modules from the start: `:app` and `:shared` (via the new `com.android.kotlin.multiplatform.library` plugin, AGP 9.0+/Gradle 9.1+/JDK 17). `:shared` exposes an expect/actual crypto boundary; the Android `actual` uses `KeyGenParameterSpec` with `setUserAuthenticationParameters(0, AUTH_BIOMETRIC_STRONG)` (never `AUTH_DEVICE_CREDENTIAL` in the same key — that would silently break `CryptoObject` binding and downgrade the guarantee). `BiometricPrompt.CryptoObject` gates every wrap/unwrap of the master key. StrongBox is attempted opportunistically, never required. `KeyPermanentlyInvalidatedException` and repeated failure route to master-password re-derivation. Verify runs on a real device reached from WSL2 via `ADB_SERVER_SOCKET` pointed at a Windows-host `adb.exe` server.

**Why this approach** — every alternative considered either reinvents a platform capability that already exists cleanly (custom TEE access) or silently weakens the stated security guarantee (a device-credential-inclusive key, mandatory StrongBox, a WSL2 emulator standing in for real hardware).
**Alternatives considered** — single-module legacy KMP setup (inherits AGP-9 migration debt with no benefit); `AUTH_DEVICE_CREDENTIAL`-inclusive Keystore key (breaks CryptoObject binding); mandatory StrongBox (excludes real devices per Google's own guidance); `usbipd-win` as the primary device bridge (more moving parts than `ADB_SERVER_SOCKET` for this single-device dev loop); custom NDK/Keymaster access (reinvents AndroidKeystore).

## 5. Technical Design

```text
User taps Unlock -> BiometricPrompt shown -> biometric succeeds
  -> CryptoObject-bound Cipher authorized -> Cipher.doFinal() unwraps
  master key from Keystore-wrapped ciphertext -> master key held in memory
  for the unlock session -> gates decryption of the ≤5 recipe-secrets cache
```

On invalidation (biometric re-enrollment) or repeated failure:

```text
Keystore throws KeyPermanentlyInvalidatedException (or N failed attempts)
  -> UI falls back to master-password entry -> Argon2id re-derives the
  master key -> a fresh Keystore key is generated -> master key re-wrapped
  -> vault access restored, no data lost
```

**Data model** — one Keystore-resident AES/GCM key (alias TBD at implementation) per device install; one on-disk ciphertext blob holding the Keystore-wrapped master key; the existing ≤5-recipe-secrets cache (already planned elsewhere) becomes encryptable once the master key is available in memory. No migration concern — this is new, greenfield storage.

**UI / UX** — one Compose screen (`:app`) for this cell: an Unlock action triggering BiometricPrompt, a visible round-trip result (proves unwrap worked), and a master-password fallback input surfaced on invalidation/failure per the flow above. Full vault/cook screens are out of scope (`p-c46b217c`).

**Security / Permissions** *(mandatory, high-risk)* — the Keystore key never leaves hardware (TEE, or StrongBox when available); it is usable only via a biometric-authorized `CryptoObject`, never exportable. The unwrapped master key exists only in process memory for the unlock session (auto-lock: 5 min idle / 10 min total / background→lock, already-locked policy) and is never logged, printed, or written to disk in plaintext (test matrix probe #11 in plan.md). `minSdk 26` + `SDK_INT >= 28` StrongBox guard (D4) bound the API surface used.

## 6. Affected Files

<Projected from `approach.md` — no cells exist yet; this list becomes authoritative once E1/E2 cells are created at Prep.>

| Action | File / Component | Purpose |
|--------|------------------|---------|
| Create | `settings.gradle.kts`, root `build.gradle.kts`, `gradle/libs.versions.toml` | AGP 9 / Gradle 9.1 / Kotlin version catalog |
| Create | `shared/build.gradle.kts`, `shared/src/commonMain/.../KeystoreCrypto.kt` | expect crypto/keystore contract |
| Create | `shared/src/androidMain/.../KeystoreCrypto.android.kt` | actual Keystore/BiometricPrompt implementation |
| Create | `app/build.gradle.kts`, `app/src/main/.../MainActivity.kt` | Compose demo screen |

## 7. Implementation Steps

<Projected from the current-slice cells after Prep (Gate 2 approved).>

**Current slice:**
- [x] Scaffold `:app` + `:shared` Gradle/KMP project, AGP9/Gradle9.1, empty Activity boots (`android-keystore-e1`)
- [x] Real Windows-host device accessible from WSL2 via native `adb.exe` (planned `ADB_SERVER_SOCKET` route Superseded) (`android-keystore-e2`, deps: `android-keystore-e1`)

**Future slices (not yet cells):**
- E3 — Done within accepted Xiaomi scope: Keystore-wrapped master key + biometric gate (the core security proof).
- E4 — Done within accepted Xiaomi scope: Invalidation / fallback flow.
- E5 — Done within accepted Xiaomi scope: Minimal demo UI wiring.

**Future slices (not yet cells):**
- E3 — Done within accepted Xiaomi scope: Keystore-wrapped master key + biometric gate (the core security proof).
- E4 — Done within accepted Xiaomi scope: Invalidation / fallback flow.
- E5 — Done within accepted Xiaomi scope: Minimal demo UI wiring.

## 8. Validation Plan

**Automated** — `android-keystore-e1` verify: `JAVA_HOME=... ./gradlew :app:assembleDebug --no-daemon` → clean build + APK produced. `android-keystore-e2` verify: `adb devices | grep -qw device` → exits 0 only with a real device in `device` state.
**Manual** — [x] APK installs and launches on the real device with no crash in logcat (E2, per its second must-have truth). [x] Working device route documented in `docs/runbooks/android-keystore-device-proof.md`; historical TCP setup superseded.
**Evidence** — `archive/plans/passchef-mobile-android-keystore/reports/validation-e1-e2.md`: both verify commands re-run literally by validating (E1: `BUILD SUCCESSFUL`, real APK produced; E2: confirmed to correctly exit 1 with zero devices attached — no false-pass). A real spike (`.bee/spikes/passchef-mobile-android-keystore/`) proved the AGP 9.0.0/Gradle 9.1.0/Kotlin 2.4.10/Compose-`compileSdk 36` combination builds cleanly, including two build failures along the way (AGP 9.3.1 incompatibility, legacy kotlin-android plugin conflict) whose fixes are now encoded in e1's cell. One outstanding precondition: no Android device is currently attached — blocks e2's cap only.

## 9. Risks & Mitigation

| Risk | Impact | Mitigation |
|------|--------|------------|
| AGP 9 / new KMP plugin split has no local precedent | Medium | Feasibility spike in validating before cells are cut for E1 |
| BiometricPrompt+CryptoObject+Keystore round trip (E3, future slice) | High | Must be proven on the real device; no JVM/emulator substitute accepted (D6) |
| StrongBox opportunistic path | Low | Explicitly best-effort (D4); try/catch fallback either way is a pass |
| `KeyPermanentlyInvalidatedException` fallback (E4, future slice) | Medium | Requires a manual induced-invalidation test (re-enroll a fingerprint) — awkward to script, called out explicitly so it isn't skipped |
| `ADB_SERVER_SOCKET` bridge new to this project's dev-loop | Low | One-line `adb devices` proof required before any cell claims to need the device |

## 10. Rollback Plan

This slice only adds new, greenfield files (a new Gradle project + WSL2 dev-tooling); nothing existing is modified. Rollback is `git revert` of the E1/E2 cells' commits, plus removing the installed JDK/Android SDK from this WSL2 worktree if desired (no other part of the repo depends on them). No data migration, no flag, and no production surface exists yet to roll back from.

## 11. Open Questions

- Does `com.android.kotlin.multiplatform.library` sync cleanly in this exact environment on the first try, or does it need `enableLegacyVariantApi` here too? (approach.md risk map)
- Is `adb.exe` already installed on the Windows host (e.g. via Android Studio/platform-tools), or does that also need installing before `ADB_SERVER_SOCKET` can be proven? (approach.md)
- Does the real device support `FEATURE_STRONGBOX_KEYSTORE`? (affects which StrongBox branch E3 actually exercises later — informational for now, not a blocker for E1/E2)
