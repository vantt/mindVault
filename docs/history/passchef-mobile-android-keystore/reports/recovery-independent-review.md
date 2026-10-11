# Android Cookbook recovery — independent review

Date: 2026-10-11. Checkout: `passMan--wt--passchef-mobile-android-keystore`.
Comparison base: `3532c160`; latest reviewed repair commit: `d2a76ca`.
This reviewer changed only this report; no product source, workflow state,
staging, commit, device configuration, or merge was changed.

**Code review: no identified P1/P2 source findings after the reviewed repairs.**
Later owner-assisted Xiaomi API36 instrumentation and manual flows passed as
recorded below. A later actual-controller fault is now classified as public
code15; earlier failures without numeric evidence remain unclassified. The
reviewed retry source still needs device acceptance and enrollment recovery.

## Scope

Read locked D1–D6/E1–E5, recovery plan, vault/backend tests, Keystore actuals,
controller/Compose/lifecycle/prompts, manifests/backups, debug harness and runbook.
Core recipe/crypto consumes its integrated prerequisite; golden tests unchanged.
Full screens, real spice authoring, OAuth, QR reader and autofill remain excluded.

## Repairs verified

- `VaultRepository.unwrap` now owns key cleanup around record reading/decoding
  as well as cache authentication. Missing, corrupt and read-failed record tests
  assert the supplied unwrapped key is zeroed.
- `SeasoningCache.encode` uses one exactly sized byte array, eliminating the
  leftover plaintext stream backing copy; encryption/decryption wipe owned
  plaintext and malformed decoding wipes allocated spice arrays.
- Lock removes only its timer callback. Queued owned results still execute their
  stale-generation cleanup, instead of being silently discarded by Handler.
- Password fallback checks generation and `OwnedWork.submit` closes its prepared
  key/cache session when executor shutdown rejects delivery. A real shutdown
  regression asserts key and spice arrays are zeroed.
- Debug target-APK proof Activity uses `targetContext` and the instrumented UID.
  AndroidX core1.6.1 ActivityScenario uses startActivitySync, so test-APK hosting
  is unsuitable; official source was checked.
  [Official source artifact](https://dl.google.com/dl/android/maven2/androidx/test/core/1.6.1/core-1.6.1-sources.jar).
- Physical key/cache equality uses sanitized boolean assertions; the expected
  synthetic key copy is wiped in finally. The test accurately names disk reopen,
  without claiming it killed/restarted the process.
- Debug applicationIdSuffix `.demo` isolates its UID/private files/Keystore from
  release `dev.passchef.mobile`; targetContext still launches the debug proof
  Activity. Runbook uses the full Activity component and `.demo.test` runner,
  with preflight preserving any unknown existing demo installation/record.

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

Independent tester ran clean JVM tests, shared assembly, app/test APKs and lint
with JDK17.0.20+8/SDK36 after the builder released Gradle; exact commands are
recorded in the recovery report and device runbook.

Independent tester observed **BUILD SUCCESSFUL, exit0, 1m13s**, 107 tasks with
105 executed. Fresh six XML suites contain **39 tests, zero failures/errors/skips**
(26 inherited Core plus 13 vault). Android AAR/JVM JAR and app/instrumentation APKs
built. Lint: zero errors, eight warnings (old target, plugin/dependency updates,
and missing application icon).

After the final debug-package isolation delta, the independent tester reran
`./gradlew --no-daemon --console=plain :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug`:
**BUILD SUCCESSFUL, exit0, 14s**, 93 tasks (two executed), same zero lint errors/
eight warnings. Shared source was unchanged; the earlier clean 39-test result
remains applicable. Independent aapt2 APK/binary-manifest checks confirm app
`dev.passchef.mobile.demo`, test `dev.passchef.mobile.demo.test`, and instrumentation
target `dev.passchef.mobile.demo`. Compiled backup resource excludes all listed
data domains in cloud backup and device transfer. Proof Activity is compiled
debug-only and nonexported. Whitespace validation passes.

Unique synthetic proof files/aliases isolate user Cookbooks. Two real owner
prompts authorize wrap/unwrap; assertions cover absent-auth refusal, hardware/
exportability, key/cache equality, disk reopen, wiping and wrong-password no-write.
Only proof assets are deleted.

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
  It preserves **two correct-password re-wrap failures**, both retaining the
  entire old record. The later successful demo re-wrap has a different alias,
  unchanged salt/cache-IV/cache hashes, and an empty retirement queue. These
  comparisons were independently checked; no password/key/spice values needed.

Reviewed new AtomicRecordStorePhysicalTest/KeystoreRecoveryPhysicalTest plus
generic debug harness label and API-guarded test-only numeric diagnostics.
Unique synthetic filenames/aliases isolate user Cookbooks; four operations wait
for actual same-cipher BiometricPrompt success, never inject authorization.
Owned sessions, expected key, password and spices close/zero in finally. Failure
diagnostics contain constant phases, exception classes, method/line frames and
API33+ numeric/boolean flags; no original exception message/cause is chained.
API31 KeyInfo calls are guarded; retryPolicy is read only for transient errors.
At this four-prompt run the production vault/prompt source remained `e26bcd9`;
no production fix explains the later pass. Root-owned physical evidence includes manual UI re-wrap, so
backend-only regression success is not substituted for the demo path.

Independent tester rebuilt the final physical diagnostic delta: app/test APKs
and lint **passed, exit0, 32s, 93 tasks/eight executed**, zero lint errors and the
same eight warnings. APK hashes match `recovery-numeric-build.json`; no production
shared/app-main/Gradle changes occurred, so prior 39 JVM tests remain applicable.

## Actual-controller diagnostic probe review

Reviewed the subsequent VaultController phase/catch delta, debug WrapFailureProbe,
release no-op and two Android formatter tests: **no identified P1/P2 findings**.
The controller tracks constant phases and preserves existing authorization,
storage, retirement and cleanup behavior. Debug output contains whitelisted phase,
bounded exception class chain, guarded API31 security level and API33 numeric/
boolean flags only. It excludes exception messages, aliases, bytes and Throwable
logging; diagnostic failure is contained. Release never invokes key inspection.

Independent tester debug/test/release APK and lint verification passed: **exit0,
16s, 140 tasks/three executed**, zero lint errors/eight known warnings. Release
APK inspection found neither the diagnostic log tag nor debug proof Activity.
`actual-controller-probe-test.log` records **OK (2 tests), 0.026s** on the real
device; APK identities are recorded in `actual-controller-probe-build.json`.
These assertions prove formatter sanitization, not biometric recovery success.
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
Prompt nonce/cipher identity are one-shot; old/duplicate callbacks cannot authorize
the retry. PreparedVault ownership survives waiting; synchronized working state
defers cancellation wiping until its worker releases input. Rejected submission/
post/delay, stale delivery and cancel dispose ownership; committed alias survives
concurrent lock. Atomic replacement precedes old retirement; exhausted/other-code/
postcommit errors never rewrap or downgrade. Initial hardware-unavailable password
fallback remains. Nine JVM control regressions cover these policy/ownership edges;
two Android negative classifier tests do not claim simulated biometric success.
Reviewed source committed `d2a76ca`. Independent tester's fresh `--rerun-tasks`
verification passed: **48 JVM tests, zero failures/errors/skips; exit0, 78s,
150/150 tasks executed**, debug/test/release/shared artifacts and lint0errors/8warnings.
APK SHA256 identities are in `independent-expired-wrap-build.json` beside its log;
53 frozen source/build files remained unchanged; release excludes proof/tag.
Parent installed pinned APKs preserving the existing invalidated synthetic demo.
`expired-wrap-negative-classifier-physical.log`: **OK (4 tests), 0.032s**.
These prove classifier/diagnostic controls, not actual-controller retry/E4 success.

Real enrollment invalidation/recovery, repeated-failure/lockout, late background
callbacks, timer UI and device log checks remain pending unless separately
recorded later. API26–29/no-biometric/StrongBox-absent are unverified device-matrix
branches; this phone certifies neither those nor every OEM. Manual demo must
remain synthetic and isolated. See the [device runbook](../../../runbooks/android-keystore-device-proof.md).

**Partial physical acceptance only; no completed-feature/merge-ready verdict.**

Unresolved: does fresh-Cipher retry pass actual-controller delayed-fault/cancel/
exhaustion probes, and can real enrollment invalidation/password recovery pass?
