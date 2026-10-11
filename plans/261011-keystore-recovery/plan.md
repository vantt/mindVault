# Complete Android Keystore + Biometric Unlock

Date: 2026-10-11. Worktree: `passMan--wt--passchef-mobile-android-keystore`.
Scope: finish original E1–E5; no full mobile screens, OAuth, QR transport, or autofill.
Truth: `docs/history/passchef-mobile-android-keystore/CONTEXT.md` and `plan.md`.
Progress: E3–E5 genuine source implemented; local build/test and physical results in
`docs/history/passchef-mobile-android-keystore/reports/keystore-recovery.md`.
Workflow tooling deferred by user; preserve existing Bee state unchanged.

## Starting evidence and prerequisite

- E1 committed scaffold builds; current Android actual only returns `pong`.
- E2 historical bridge report exists, but today's Windows/local adb lists are empty.
- Today's WSL→Windows TCP bridge timed out; physical acceptance currently blocked.
- Finish/review Mobile Core first, then root merges it into this original worktree.
- Reuse its repository-root `:shared` Android+JVM KMP module and Argon2 port.
- Reuse proven BouncyCastle 1.85, `jvmAndroidMain`/`javax.crypto`, and parity vectors.
- Retain Android `:app`, `dev.passchef.mobile`, minSdk26, compileSdk36 and real SDK.
- Reconcile Gradle/toolchain versions against integrated Core; never duplicate its core.

## E2 — real device bridge

- Confirm physical serial in `device` state through native Windows `adb.exe` from WSL.
- Use that explicit binary and `wslpath -w` APK paths; no TCP exposure or silent daemon fallback.
- Install/launch real APK and capture crash-free logcat once physical device is available.
- USB authorization and real biometric participation require the device owner.

## E3 — genuine persistent storage and biometric boundary

- Replace placeholder with real Android Keystore AES256/GCM key management.
- Require per-operation strong biometric and bind each wrap/unwrap to `CryptoObject`.
- API30+: auth parameters `(0, AUTH_BIOMETRIC_STRONG)`; API26–29: duration `-1`.
- Enrollment invalidation true; StrongBox only API28+ with feature check/fallback.
- Inspect actual key hardware protection; do not assume every Keystore key is hardware-backed.
- Derive 32-byte master key using proven Argon2id: t3, mem65536KiB, p4, UTF-8.
- Persist versioned, bounded record: random derivation salt, alias, IV/wrapped key,
  and AES-GCM-encrypted seasoning cache of at most five items; no plaintext key/spices.
- Use fresh cipher-generated IV for each Keystore wrap and fresh IV for cache encryption.
- Validate record/version/lengths before operations; corrupt records never trigger overwrite.
- Store record atomically in app-private, backup-excluded storage; recover interrupted writes.

## E4 — password recovery without losing stored seasoning

- Invalidated/missing alias, biometric lockout/failure, or unavailable biometric → password path.
- Derive using original salt; authenticate existing cache before any persistent mutation.
- Wrong password leaves record and existing alias unchanged; display sanitized recovery guidance.
- Correct password unlocks same cache; fresh biometric wrapping uses a new alias.
- Persist complete new record atomically before retiring old alias; failed write removes only
  new disposable alias and preserves prior recoverable record.
- No-biometric devices retain a password-only path; never simulate successful biometric proof.

## E5 — secure minimal demo

- One Compose screen: setup, biometric unlock, password fallback, explicit lock, proof status.
- Host real AndroidX BiometricPrompt with FragmentActivity and required permissions.
- Perform Argon2/storage work off main thread; guard repeated taps and concurrent prompts.
- Keep master key/spices only in session byte arrays; zero owned buffers on lock/background.
- Process death never persists or restores an unlocked session.
- Cold start locked; enforce idle5min/total10min lock, cancel stale callbacks, use FLAG_SECURE.
- Show round-trip success/status, never plaintext key/spices in UI, logs, or exceptions.

## Independent verification and completion

- JVM: Core golden/parity suite plus record validation, incorrect-password no-mutation,
  atomic-write recovery, cache integrity, lock timers and stale-callback/session cleanup tests.
- Android build: real integrated `:app:assembleDebug`, `:app:assembleDebugAndroidTest`, lint;
  independent tester reruns relevant JVM tests/build after builder reports ready.
- Physical instrumentation: real AndroidJUnitRunner wrap→lock→biometric→unwrap equality,
  key export/hardware checks, persistence/restart and absent/failed-auth rejection.
- Physical manual: real biometric enrollment invalidates prior key; original password recovers
  same cache and fresh wrapping; lockout/cancel, no enrollment, background/restart and demo.
- Record exact device/API, commands, results, logcat checks and residual limitations.
- JVM/emulator success does not replace physical Keystore/BiometricPrompt acceptance.
- If device unavailable: report code/build/tests separately; E2/E3/E4/E5 physical acceptance
  remains BLOCKED; never declare feature complete or merge-ready on simulated evidence.
- Complete original worktree only after independent verification, documentation and commit;
  root integrates completed trees into main while preserving unrelated work.
