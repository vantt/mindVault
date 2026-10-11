# Android Cookbook recovery — independent review

Date: 2026-10-11. Checkout: `passMan--wt--passchef-mobile-android-keystore`.
Comparison base: `3532c160`; latest reviewed repair commit: `0a99d63`.
Reviewer changed only this report; no source/state/staging/commit/device/merge actions.

**Lockout P2 fixed in source; no P1/P2 findings. Physical7/9 verification pending.**
Later owner-assisted Xiaomi API36 instrumentation and manual flows passed as
recorded below. A later actual-controller fault is now classified as public
code15; earlier failures without numeric evidence remain unclassified. Actual
enrollment recovery now passed; remaining lifecycle/timer/retry probes are pending.

## Scope

Read locked D1–D6/E1–E5, recovery plan, vault/backend tests, Keystore actuals,
controller/Compose/lifecycle/prompts, manifests/backups, debug harness and runbook.
Core recipe/crypto consumes its integrated prerequisite; golden tests unchanged.
Full screens, real spice authoring, OAuth, QR reader and autofill remain excluded.

## Repairs verified

- `VaultRepository.unwrap` owns record/cache cleanup; missing/corrupt/read-failed
  tests assert supplied key zeroing.
- `SeasoningCache.encode` uses one owned buffer; crypto and malformed decoding
  wipe owned plaintext/spice arrays.
- Lock removes only its timer; stale owned-result callbacks still execute cleanup.
- Password fallback checks epoch; shutdown rejection closes owned session.
  Real shutdown regression asserts key/spice zeroing.
- Debug target-APK proof Activity uses `targetContext` and the instrumented UID.
  AndroidX core1.6.1 startActivitySync makes test-APK hosting unsuitable.
  [Official source artifact](https://dl.google.com/dl/android/maven2/androidx/test/core/1.6.1/core-1.6.1-sources.jar).
- Physical comparisons sanitize failures and wipe copies; disk reopen is named accurately.
- Debug `.demo` isolates release UID/files/keys; runbook uses its full component/
  `.demo.test` runner and preflight preserving unknown demo records.

## Security and correctness checks

- Real Argon2id v19, t3, 65536KiB, p4, 32-byte derivation and Web UTF-8 surrogate
  handling reuse the proven Core boundary and actual BouncyCastle1.85.
- Cache AES-GCM authentication completes before caller creates a rotation alias;
  wrong password/tampering never writes the record or retires an existing alias.
- Version/byte lengths/jar indexes and sizes are bounded. No master key, password
  or plaintext seasoning is persisted. Ciphertext records use noBackupFilesDir;
  cloud backup and device-transfer exclusions are explicit.
- Atomic complete-record replacement precedes old-alias retirement. Failed write
  preserves old data and disposes only the new alias. Bounded retired-alias
  metadata survives interruptions and retries after successful authentication.
- Android generates nonexportable AES256/GCM keys requiring per-use strong
  biometric authentication and enrollment invalidation. API30 auth parameters,
  API26–29 duration -1, and API28 StrongBox calls have explicit guards. StrongBox
  failures retry normal Keystore; hardware protection is inspected, not assumed.
- Each authorized callback must supply the same cipher and current prompt/operation
  generation. Repeat taps cannot initiate parallel unlocks; stale callbacks cannot
  publish sessions. Session buffers zero on lock/background; idle5min/total10min
  limits use monotonic time and cold recreation never restores unlocked state.
- Argon/storage operations use one worker. Product messages sanitize failures;
  no product key/password/spice logging found. FLAG_SECURE is applied. UI inputs
  use remember rather than saveable state and clear on submission/background.
  Immutable managed strings/provider copies cannot be promised erased.
- JVM actual explicitly throws unsupported for Android hardware operations;
  fault-injection storage tests do not claim to simulate biometric hardware.

## Independent build/test evidence

Initial clean JDK17/SDK36 run passed39 tests (Core26/vault13), zero failures/skips,
107 tasks/105 executed in73s; shared/app/test artifacts built, lint0errors/8warnings.
Isolated-package rerun passed in14s,93tasks/two executed. Independent APK checks
confirm `.demo`/`.demo.test` and correct runner target, backup/transfer exclusions,
debug-only nonexported proof Activity. Final48-test rerun is recorded below.
Unique synthetic proof files/aliases isolate user Cookbooks. Genuine wrap/unwrap
tests cover absent-auth, key/cache equality, hardware, persistence and cleanup.

## Later physical evidence and focused regression review

On 2026-10-11 root established native Windows adb USB access and ran the actual
APK on Xiaomi14TPro/2407FPN8EG, API36, with owner biometric participation. This
reviewer ran no ADB/device actions, and independently read the saved logs under
`/home/vantt/projects/passMan-recovery/20261011-keystore/`.

- `physical-instrumentation.log`: **OK (1 test), 12.431s**, genuine wrap/unwrap,
  hardwareProtected=true, nonexportable=true, cache equality. This supersedes
  the earlier absent-device/install-blocker status, not those historical events.
- `atomic-regression-physical.log`: **OK (1 test), 0.013s**, actual Android
  first-create, existing-file replacement and exact reopened bytes. This proves
  successful replacement on this filesystem, not every crash timing.
- `recovery-regression-physical.log`: **FAIL (1 test), 23.151s**, first setup-wrap
  doFinal after owner authentication raised IllegalBlockSizeException caused by
  android.security.KeyStoreException. Numeric cause was not recorded; do not
  classify this as transient, authentication failure, or fixed source defect.
- `recovery-numeric-physical.log`: later **OK (1 test), 17.193s**, all four real
  prompts, old-cache authentication, fresh alias, committed replacement, prior
  retirement and final new-key/cache equality. Setup/recovery KeyInfo security
  level=2, which is StrongBox. [Platform constant](https://developer.android.com/reference/android/security/keystore/KeyProperties#SECURITY_LEVEL_STRONGBOX).
- `manual-evidence.json`: real demo setup/unlock, background locking, actual
  force-stop/relaunch locked, cancel and wrong-password no-mutation passed.
  Preserved two re-wrap failures keep old data; later success has new alias,
  unchanged salt/cache-IV/cache hashes and empty retirement queue, independently checked.

Reviewed the focused physical regressions: isolated files/aliases, real same-cipher
prompts, owned buffers closed/wiped in finally and safe constant/class diagnostics.
API31/33 calls are guarded; no original error messages/causes or key bytes logged.
That backend-only pass did not repair/prove the actual-controller failure.
Physical-diagnostic APK/lint rerun passed32s,93tasks/eight executed,0errors/8warnings;
identities are in `recovery-numeric-build.json`, with product then still `e26bcd9`.

## Actual-controller diagnostic probe review

Probe delta preserves authorization/storage policy; bounded class chain, whitelisted
phase and guarded numeric flags exclude messages/aliases/bytes. Release is no-op.
Independent debug/test/release/lint passed16s,140tasks/three executed,0errors/8warnings;
release lacks proof Activity/log tag. Formatter tests passed2/0.026s on device;
`actual-controller-probe-build.json` records identities. This proves sanitization only.
The subsequent `actual-controller-numeric.log` captured wrap-doFinal code15,
system/transient=true, authRequired=false, retry2 and StrongBox level2. This
identifies an expired KeyMint operation for that attempt, not earlier failures.
[Official code15 definition](https://developer.android.com/reference/android/security/KeyStoreException#ERROR_KEY_OPERATION_EXPIRED).

## Frozen expired-operation repair — source review

Reviewed VaultWrapping, WrapOperationState, typed bounded WrapFailureCauses/
ExpiredWrapFailure, controller extraction and new tests against the focused plan.
**No identified P1/P2 source findings.** API33+ public code15 only at doFinal
permits one retry after a cancellable5s delay. It recreates Cipher/IV on the same
disposable alias and requires a new genuine strong-biometric CryptoObject prompt.
Nonce/cipher identity prevent replay; deferred worker cleanup, cancelled/rejected
handoffs and committed-alias ownership remain correct. Atomic commit precedes old
retirement; exhausted/other/postcommit failures never downgrade. Initial hardware
fallback remains. Nine JVM ownership tests and two Android negative classifier
tests do not claim simulated biometric success.
Reviewed source committed `d2a76ca`. Independent tester's fresh `--rerun-tasks`
verification passed: **48 JVM tests, zero failures/errors/skips; exit0, 78s,
150/150 tasks executed**, debug/test/release/shared artifacts and lint0errors/8warnings.
APK SHA256 identities are in `independent-expired-wrap-build.json` beside its log;
53 frozen source/build files remained unchanged; release excludes proof/tag.
Parent installed pinned APKs preserving the existing invalidated synthetic demo.
`expired-wrap-negative-classifier-physical.log`: **OK (4 tests), 0.032s**.
These prove classifier/diagnostic controls, not actual-controller retry/E4 success.

## Actual enrollment recovery result and inference limits

`manual-evidence.json` records **actual E4 recovery PASS on installed d2a76ca**:
new alias/fresh wrapIV, unchanged salt/cacheIV/cache hashes, empty retirement queue.
Independent comparisons confirmed those hashes and the subsequent locked→genuine
biometric unwrap PASS with one authenticated jar and unchanged full record.
Current PID12308 logged code15 at05:44:42.074Z after the held prompt/owner confirmation;
`retry-timestamp-diagnosis.json` preserves timestamps. Prior failed attempts remain.

Owner reports **one visible dialog**, without independent two-dialog confirmation.
Given log/UI correlation and frozen source, code15→commit requires5s retry/fresh
Cipher/new CryptoObject and same-cipher callback. This is source inference supported
by recovery, not captured callback telemetry or two visually distinct dialogs.
Continuous sensor/OEM presentation remains an unverified explanation.

Latest d2a76ca manual `retry_candidate_*` keys record real bio/wrap cancel, wrong
password, pending-wrap Home→locked after6s and force-stop/newPID/restart locked;
all preserve exact record. Generic unavailable UI is not typed7/9 proof or retry-delay cancel.
No5/10-minute physical pass: root observed lock231s after requested tap without
verified session delivery/start. `timer-idle-20261011T062931212610Z.json` was
interrupted before timer start; later owner authentication showed Device key
unavailable, numeric cause unobserved. This does not classify a timer/code defect.

## Lockout fallback defect and candidate source review

CONTEXT D3 and original plan:61 require a working master-password fallback for
ERROR_LOCKOUT/ERROR_LOCKOUT_PERMANENT. In reviewed d2a76ca MainActivity:35–36 loses
the reason; Controller:71–74 sends correct passwords back to wrapping whenever
canAuthenticate succeeds; VaultWrapping:58–60 closes the authenticated session
and repeats password advice on error. AOSP explicitly permits canAuthenticate
success during both lockouts, so this supported state can trap password recovery.
[Official platform tests](https://android.googlesource.com/platform/frameworks/base/+/9759067e38ee5aa7c8ba62201f619cc0bc97bc40/services/tests/servicestests/src/com/android/server/biometrics/BiometricServiceTest.java#1240).
Root accepted the [focused plan](../../../../plans/261011-keystore-recovery/reports/lockout-fallback-fix.md).
Reviewed candidate delta over e4692d0: **prior P2 addressed; no new P1/P2 findings**.
Real typed initial7/9 transfers owned cache-authenticated session once; same-cipher/
nonce/epoch checks remain and retries!=0 forbids code15 downgrade. firstSetup uses
actual record presence; PasswordLockoutSession existing branch performs zero writes/
retirements, setup alone commits encrypted cache. No remembered lockout bypass.
Stale/rejected delivery wipes transferred data; disposable alias cleanup remains.
Eight regressions cover real cache/record bytes, setup/failures and control ownership,
not hardware proof. Debug logs only source/numeric real prompt code;
no errString/messages/bytes, release no-op. All45 Kotlin hashes matched before/after
review. Source committed0a99d63; builder and independent fresh rerun both passed.
Independent result: **56 JVM tests, zero failures/errors/skips,150/150 tasks,78s**;
debug/test/release/shared artifacts and lint0errors/8warnings;61 files/hashes/modes stable.
`independent-lockout-build.json` and `lockout-candidate-build.json` pin APK identities;
release excludes proof Activity/tag. Debug/test APKs installed-r; actual hashes
match pinned identities and complete Cookbook bytes remain unchanged. Root observed
one genuine new-candidate biometric unlock with one jar; actual7/9 remains unproven.
Owner reboot/security setup pending; keepawake restored0; screenshot/Recents/timers unproven.

Repeated-failure/lockout, retry cancel/exhaustion, late background
callbacks, timer UI and device log checks remain pending unless separately
recorded later. API26–29/no-biometric/StrongBox-absent are unverified device-matrix
branches; this phone certifies neither those nor every OEM. Manual demo must
remain synthetic and isolated. See the [device runbook](../../../runbooks/android-keystore-device-proof.md).

**Partial physical acceptance only; no completed-feature/merge-ready verdict.**

Unresolved: actual7/9 acceptance and remaining retry/lifecycle/timer physical probes.
Two distinct visible retry dialogs were not independently observed or claimed.
