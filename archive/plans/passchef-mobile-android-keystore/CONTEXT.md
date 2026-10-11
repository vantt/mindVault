# Android Keystore + Biometric Unlock — Context

**Feature slug:** passchef-mobile-android-keystore
**Date:** 2026-07-27
**Exploring session:** complete
**Scope:** Standard
**Domain types:** RUN, ORGANIZE, SEE (thin — biometric prompt + fallback UI only)

## Feature Boundary

Deliver a standalone Android Gradle module (`androidApp` + `shared` KMP module, package `dev.passchef.mobile`) that proves: a device-bound Android Keystore AES key wraps the Argon2id-derived master key, unlockable only via `androidx.biometric` BiometricPrompt (with master-password fallback), so the derived master key never exists on disk in plaintext and never leaves the Keystore-backed cipher boundary except as transient in-memory state during an unlocked session. That same master key also gates decryption of the on-device cache of the user's ≤5 recipe secrets ("spices") — losing the master key means losing access to those secrets too, by design. This is the foundation module PBI p-c46b217c (the full 6-screen app) will later depend on — it does not build the vault/cook/recipe screens, autofill, QR handoff, or Drive sync.

## Locked Decisions

| ID | Decision | Rationale (only if it changes implementation) |
|----|----------|-----------------------------------------------|
| D1 | Dev toolchain: install JDK + Android SDK cmdline-tools + platform-tools directly in this WSL2 worktree now, and use it for all real builds/tests for this feature — no mocked/faked verify. | WSL2 had no Java/adb/SDK at all; repo rule forbids mocking a build to pass; must have a real toolchain to honestly verify Keystore/Biometric code (AGENTS.md Test Case Protocol, development-rules.md). |
| D2 | This cell's deliverable is a minimal but runnable pair of Gradle modules — `shared` (KMP, holds the Keystore/crypto logic behind an expect/actual boundary) + `androidApp` (one Activity/Compose screen exercising it) — demonstrating store-in-Keystore + biometric-unlock + decrypt round-trip end-to-end. It is not the full 6-screen app skeleton, and `shared` is never delivered alone without a runnable `androidApp` to prove it against real hardware. | Repo has zero Android/KMP scaffolding today (only `chrome-extension/` + `landing-page/`). Full 6-screen app is a separate, larger PBI (p-c46b217c) that will reuse both modules produced here. |
| D3 | On Keystore key invalidation (OS auto-invalidates on biometric enrollment change) or repeated unlock failure, fall back to master-password re-entry: re-derive the Argon2id key from the password, generate a fresh Keystore key, re-wrap. No data is deleted and no forced full re-onboarding. | Matches locked product decision in `docs/mobile-app-brainstorm-qa.md` Q6 §1 "Unlock" (line 189: "Face ID/vân tay tự bật khi mở; fail → master password"); losing the vault on a routine biometric-enrollment change would be a severe, unacceptable UX regression. |
| D4 | `minSdkVersion = 26` (Android 8.0). StrongBox used when available, gated on `Build.VERSION.SDK_INT >= 28` (`setIsStrongBoxBacked(true)` with fallback catch on `StrongBoxUnavailableException`), never required below or above that check. | `setInvalidatedByBiometricEnrollment` needs API 24+; 26 gives headroom on other modern `KeyGenParameterSpec` behavior without dropping too much device coverage. StrongBox APIs are API 28+, hence the explicit version guard — a bare try/catch at minSdk 26 is not enough since the method doesn't exist below 28. StrongBox hardware is also inconsistent across OEMs, so it must degrade gracefully, never hard-block unlock. |
| D5 | Keystore-protected artifact is a Keystore-resident AES/GCM key that wraps (encrypts) the Argon2id-derived master key (t=3, mem=64MB, p=4, params fixed by prior product decision). `BiometricPrompt.CryptoObject` bound to that Keystore key gates every unwrap. The unwrapped derived master key lives only in process memory for the unlocked session (cleared on lock/background per the existing 5min-idle/10min-total auto-lock policy) — never written to disk in plaintext. The same master key also encrypts the local on-device cache of the user's ≤5 recipe secrets ("spices") at rest, so losing the master key means losing access to those secrets too (by design, not a bug). | Cites `docs/mobile-app-brainstorm-qa.md` Q1 (lines 41-60, master-key + biometric concept) and Q6 §5 "Onboarding 5 bước" (line 201: "Setup ≤5 secrets + master (cảnh báo mất master = mất secret)") — this line is the direct product evidence that recipe secrets, not only the master key, are within "secret không rời Keystore ở dạng rõ" (fresh-eyes review flagged the first draft as scoping only the master key; this revision closes that gap). |
| D6 | Verify uses a real physical Android device connected via USB/adb, bridged from the Windows host into this WSL2 environment (not a WSL2-hosted emulator/AVD). Every cell's recorded verify evidence for Keystore/BiometricPrompt behavior must be a real run against that device — no Robolectric/JVM-only substitute for the hardware-backed paths. | Robolectric has no hardware-backed `AndroidKeyStore`/`BiometricPrompt` provider, so it cannot honestly verify this feature's core claim; WSL2-hosted emulators hit known nested-virtualization problems (already flagged in `docs/mobile-app-brainstorm-qa.md` line 251). User confirmed a real device is available and will be bridged from the Windows host — this is now the one honest verify path per repo rule against mocked/faked verification. |

### Agent's Discretion

- Exact Kotlin package/module layout inside `androidApp`/`shared` (class names, file split) — engineering detail, not a product decision.
- Which specific androidx.biometric API version and Gradle/AGP/Kotlin versions to pin — planning/implementation picks current stable versions and records them in the plan.
- Which KMP Argon2id library/binding to use on JVM/Android — planning picks one and cites it; bit-for-bit parity with the extension's `argon2_adapter.js` is verified by the separate spike PBI `p-c2ad072e`, not re-derived inside this cell.

## Terms

| Term | Meaning in this feature |
|------|-------------------------|
| Master key | The Argon2id-derived key (from the user's master password) that ultimately unlocks the vault. Never stored raw; only its Keystore-wrapped ciphertext is persisted. |
| Keystore key | An Android Keystore-resident AES/GCM key, `setUserAuthenticationRequired(true)` + `setInvalidatedByBiometricEnrollment(true)`, used only to wrap/unwrap the master key. Never exportable from hardware. |
| Unlock session | The window between a successful biometric (or fallback password) unlock and the next auto-lock (5 min idle / 10 min total / background→lock), during which the unwrapped master key is held in memory. |

## Specific Ideas And References

- Product architecture for this exact mechanism was already worked out in brainstorming (not re-derived here): `docs/mobile-app-brainstorm-qa.md` lines ~41-60 (Q1, master-key + biometric wrap concept), ~173 (Q5, KMP module shape: `shared/` linked by `androidApp`/`iosApp`, Keystore/Keychain access group holding the cached derived key), ~185-206 (Q6, Phase 1 scope, unlock UX, auto-lock/FLAG_SECURE security UX), ~247-251 (Argon2id only on unlock path, not the cook hot path; WSL2/Android Studio dev-loop caveat).

## Existing Code Context

### Reusable Assets

- None. No Android/Kotlin/Gradle code exists anywhere in this repo yet.

### Established Patterns

- `chrome-extension/` has its own Argon2id adapter (`argon2_adapter.js`, per `docs/mobile-app-brainstorm-qa.md` line 247) with fixed params (t=3, mem=64MB, p=4) that this feature's Argon2id derivation must match bit-for-bit — cross-platform Argon2id parity is validated separately by PBI `p-c2ad072e` (KMP Argon2id spike), not re-verified inside this cell.

### Integration Points

- None yet in this repo. This module is the first Android integration point; PBI `p-c46b217c` (full app) and `p-6dee7ae8` (ported crypto core) will later depend on what this cell produces.

## Canonical References

- `docs/mobile-app-brainstorm-qa.md` — locked product decisions on mobile architecture, KMP/Compose Android-first stack choice (Q8), unlock/security UX (Q6, section "1. Unlock").
- `docs/backlog.md` / PBI `p-8b12c072` — Course-of-Success text for this feature.

## Outstanding Questions

### Resolve Before Planning

*(none — all material gray areas were resolved during exploring)*

### Deferred To Planning

- [ ] Exact Gradle/AGP/Kotlin/androidx.biometric version pins — planning verifies current stable versions and available cmdline-tools packages against what actually installs cleanly in this WSL2 environment.
- [ ] Concrete mechanism to bridge the Windows-host-attached Android device's `adb` into this WSL2 worktree (per D6) — e.g. `adb.exe` server on Windows + WSL2 client pointed at it via `ADB_SERVER_SOCKET`, or `usbipd-win` USB passthrough — planning verifies which one actually works in this environment before cells are cut.

## Deferred Ideas

- Full KMP `iosApp` target (Keychain-backed equivalent) — deferred; no Mac available (per already-locked Q8 decision), Android-first only for now.
- Autofill service integration (domain/package-mapping) — separate, larger PBI track per `docs/mobile-app-brainstorm-qa.md` roadmap Phase 2; not this cell.
- Full 6-screen app UI — tracked already as PBI `p-c46b217c`; this cell only produces the module it will consume.

## Handoff Note

CONTEXT.md is the source of truth. Decision IDs (D1-D6) are stable and cited, never reinterpreted. Planning reads the locked decisions, code context, canonical references, and the two deferred-to-planning technical questions above.
