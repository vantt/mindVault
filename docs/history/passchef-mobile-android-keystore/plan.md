---
artifact_contract: bee-plan/v1
mode: high-risk
approved_gate2: 2026-07-30
---

# Plan: Android Keystore + Biometric Unlock

Mode: `high-risk` — 4 risk flags: auth (biometric gate), audit/security (Keystore-wrapped secret storage), external systems (Android Keystore, BiometricPrompt, real-device adb bridge), cross-platform (first Android/KMP code in a repo that today is browser-extension + landing-page only).
Why this is the least workflow that protects the work: a wrong call here (e.g. a key shape that silently downgrades biometric-only to any-screen-lock, or a "verify" that only proves JVM logic and never touches real Keystore/BiometricPrompt hardware) would ship a false security guarantee — exactly the class of mistake `standard` ceremony exists to catch before code lands.

## Requirements (from CONTEXT.md)

- D1: Install a real JDK + Android SDK cmdline-tools + platform-tools in this WSL2 worktree; no mocked/faked verify anywhere in this feature.
- D2: Deliverable is two Gradle modules — `:app` (Android entry, one Compose screen) + `:shared` (KMP library, expect/actual crypto boundary) — a runnable demo, never `:shared` alone.
- D3: Keystore-key invalidation or repeated unlock failure falls back to master-password re-entry (re-derive Argon2id, re-wrap); no data loss, no forced re-onboarding.
- D4: `minSdkVersion = 26`; StrongBox opportunistic, gated `Build.VERSION.SDK_INT >= 28`, never required.
- D5: A Keystore AES/GCM key wraps the Argon2id-derived master key; that same master key also gates the on-device cache of the user's ≤5 recipe secrets. Unwrapped master key lives only in process memory for the unlock session.
- D6: Verify runs on a real physical Android device bridged via `adb` from the Windows host into this WSL2 worktree — never a WSL2 emulator, never a Robolectric/JVM-only substitute for the hardware-backed paths.

## Discovery

Ran at L2/L3 — see `docs/history/passchef-mobile-android-keystore/discovery.md` for the full evidence-labeled findings (BiometricPrompt+CryptoObject key-shape constraint, StrongBox API-28 gate, AGP-9/KMP plugin split, ADB_SERVER_SOCKET vs usbipd-win).

## Approach

High-risk lane — see `docs/history/passchef-mobile-android-keystore/approach.md` for the full recommended path, rejected alternatives, risk map, file order, and open questions for validating.

## Shape — Epic Map

**Feature outcome:** a runnable `:app` + `:shared` Android module proving Keystore-wrapped, biometric-gated master-key storage with a real-device-verified round trip, ready for `p-c46b217c` (the full 6-screen app) to build on.

**Repo-reality basis:** zero Android/KMP code exists today (`chrome-extension/` + `landing-page/` only); no local precedent to reuse — every epic below is genuinely new ground, per `bee-xia`'s discovery.

| Epic | Capability/Risk Area | Why It Exists | Slices | Proof Needed |
|---|---|---|---|---|
| E1 | Project scaffold (AGP 9 / Gradle 9.1 / KMP module split) | Nothing builds without it; MEDIUM risk per approach.md (brand-new plugin shape, no local precedent) | 1 slice: `:app` + `:shared` modules sync cleanly, empty Activity boots | `./gradlew :app:assembleDebug` succeeds; app installs and opens on the real device |
| E2 | Dev-loop device bridge | Nothing on-device can be verified without it (D6); LOW risk, well-documented mechanism | 1 slice: WSL2 SDK/adb install + `ADB_SERVER_SOCKET` bridge to Windows-host `adb.exe` | `adb devices` from WSL2 lists the real device |
| E3 | Keystore-wrapped master key + biometric gate | The feature's actual security guarantee (D5); HIGH risk — must be proven on hardware, not assumed from docs | 1 slice: `shared` expect/actual crypto boundary + Android `actual` (KeyGenParameterSpec, BiometricPrompt.CryptoObject, StrongBox opportunistic) | Instrumented round trip on the real device: wrap a test master key, lock, biometric-unlock, unwrap, byte-compare plaintext |
| E4 | Invalidation / fallback flow | Losing biometric access must never lose the vault (D3); MEDIUM risk — awkward to script, easy to skip | 1 slice: catch `KeyPermanentlyInvalidatedException` + repeated-failure path, route to master-password re-derivation + re-wrap | Manual induced-invalidation test on the real device: re-enroll a fingerprint, confirm fallback fires and re-wraps cleanly |
| E5 | Minimal demo UI wiring | Makes E3/E4 observable and demoable, not just unit-proven | 1 slice: one Compose screen — unlock button → biometric prompt → shows decrypted round-trip result | Manual demo run on the real device matches the epic outcome above |

**Slice queue (deps, feasibility status):**

1. E1 (no deps) — feasibility: MEDIUM, unproven plugin combo; validating's persona panel should include a feasibility check before cells are cut.
2. E2 (no deps, parallel-safe with E1) — feasibility: LOW, mechanism is well-documented.
3. E3 (deps: E1, E2) — feasibility: HIGH risk, the core proof of this whole feature.
4. E4 (deps: E3) — feasibility: MEDIUM.
5. E5 (deps: E3) — feasibility: LOW, thin UI wrapper.

**Current slice to prepare:** E1 + E2 — the two no-dependency epics that unblock everything else and that validating's feasibility proof most needs to de-risk first (E1's plugin-shape risk, E2's device-bridge risk). E3/E4/E5 are future slices; their cells do not exist yet.

## Test matrix (high-risk depth — written probe per dimension)

1. **User types** — single local device owner only, no multi-tenant concept in this module. Probe: device with zero biometrics enrolled must fall through to master-password-only, never crash.
2. **Input extremes** — master password empty / very long / Unicode. Probe: empty password is rejected client-side before an Argon2id call is even attempted.
3. **Timing** — process killed between "biometric succeeded" and `cipher.doFinal()` completing. Probe: on relaunch, the on-disk wrapped key is either fully-old or fully-new, never partially written (atomic write, temp+rename). Double-tap on the unlock button while a BiometricPrompt is already showing must not spawn a second prompt or crash.
4. **Scale** — N/A at meaningful scale: exactly one master key and a fixed ≤5-secret cache; no loop/collection-scale concern in this module.
5. **State transitions** — locked → unlocking → unlocked → (idle 5min / total 10min / background) → locked, per already-locked auto-lock policy. Probe: calling unlock while already unlocked is a no-op (no redundant biometric prompt); cold app start after process death always begins locked, never resumes an in-memory unlocked state.
6. **Environment** — device with no biometric hardware at all, or hardware present but nothing enrolled (`BiometricManager.canAuthenticate() != BIOMETRIC_SUCCESS`): probe both report the master-password-only path cleanly, not a raw exception. Boundary device at exactly `minSdk 26` (no StrongBox APIs available at all): probe the StrongBox code path is skipped, not attempted-and-crashed.
7. **Error cascades** — `BiometricPrompt` `onAuthenticationError` (`ERROR_LOCKOUT`, `ERROR_LOCKOUT_PERMANENT`): probe the user sees a clear fallback-to-password message, not a raw error code. An unexpected `Cipher`/Keystore exception during `doFinal()` is caught and surfaced as "try again," never an app crash.
8. **Authorization** — N/A: single local device owner, no server-side authorization surface exists in this module.
9. **Data integrity** — the wrapped-key file write is atomic (temp+rename), so a crash mid-write can never corrupt it into an unreadable state. After a fallback re-wrap (D3), the old Keystore key material is not left reachable in a way that could decrypt anything.
10. **Integration** — N/A for this cell: no external API (Sheets/Drive/OAuth) is touched here; that integration is separate PBI territory.
11. **Compliance** — probe: grep the built APK/logcat output during a full unlock/wrap/unwrap cycle for the plaintext master password, derived key bytes, or any recipe secret — none may appear in logs, crash reports, or exception messages.
12. **Business logic** — N/A/deferred: the ≤5-recipe-secret UI/authoring flow belongs to `p-c46b217c`; this cell only proves the storage/unlock mechanism, not the secret-management UI around it.

## Out of scope

- Full 6-screen app UI (`p-c46b217c`).
- Autofill service / domain-package mapping (later roadmap phase per `docs/mobile-app-brainstorm-qa.md`).
- iOS/Keychain target (deferred — no Mac available, per already-locked Q8 decision).
- QR handoff, Drive sync, Sheets OAuth integration (separate PBI tracks).
- Argon2id KMP library selection/parity verification — owned by spike PBI `p-c2ad072e`; this feature only consumes whatever key material that spike's chosen implementation produces.
