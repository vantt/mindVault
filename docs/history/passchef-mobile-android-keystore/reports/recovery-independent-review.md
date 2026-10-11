# Android Cookbook recovery — independent review

Date: 2026-10-11. Original checkout:
`/home/vantt/projects/passMan--wt--passchef-mobile-android-keystore`.
Comparison base: `3532c160`. Source freeze confirmed by the builder after the
ownership-safe shutdown repair and sanitized physical-proof assertions.
This reviewer changed only this report; no product source, workflow state,
staging, commit, device configuration, or merge was changed.

**Code result: no remaining P1/P2 findings in the reviewed implementation.**
Physical E2–E5 acceptance remains blocked without an authorized real device and
owner participation. Code review and compiled APKs do not certify biometric
hardware, enrollment invalidation, or completion of the original feature.

## Scope

Read original locked D1–D6 and E1–E5 plan, recovery execution plan, all new vault
backend modules and tests, Android Keystore actual/JVM unsupported actual,
controller, Compose screen, Activity lifecycle/prompt callbacks, manifests,
backup exclusions, debug proof Activity, instrumentation test and device runbook.
Core recipe/crypto was consumed from its integrated prerequisite, not redesigned.
Full mobile screens, real spice authoring, OAuth, QR reader and autofill remain
outside this demo. Existing golden/parity tests were not changed for recovery.

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
- The physical proof Activity resides only in the debug target APK and launches
  through `targetContext`, sharing the instrumented app process/Keystore UID.
  AndroidX core1.6.1 source was checked: its ordinary ActivityScenario invoker
  uses Instrumentation.startActivitySync, so a test-APK Activity is unsuitable.
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

Independent tester owns the fresh clean run after the builder released Gradle:

```sh
export JAVA_HOME=/home/vantt/dev-tools/jdk-17.0.20+8
export ANDROID_HOME=/home/vantt/dev-tools/android-sdk
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew --no-daemon --console=plain clean :shared:jvmTest :shared:assemble \
  :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug
```

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

## Physical acceptance and limits

The test creates unique synthetic `physical-proof-UUID.json` and `passchef-UUID`
alias; it never opens production `cookbook-v1.json` or user spices. Two real
owner biometric prompts authorize wrap then unwrap. Assertions cover absent-auth
refusal, nonexportability, actual hardware flag, key/cache equality, disk reopen,
lock wiping and wrong-password record preservation; only proof assets are deleted.

Native Windows adb invoked from WSL is the selected route, without all-interface
TCP exposure. The last observed device list was empty. No device installation,
physical test, fingerprint/enrollment change, hardware claim or device pass was
performed by this reviewer.

Manual enrollment invalidation, password recovery of the same cache, cancellation/
lockout, actual process death/relaunch, background/timer behavior and device log
checks remain required. API26/StrongBox absence needs honest device coverage;
one modern phone cannot certify all older API/device branches. Manual demo must
use a fresh synthetic installation/record and must not replace or recover an
existing user Cookbook. See the [device runbook](../../../runbooks/android-keystore-device-proof.md).

**Completion remains blocked on physical evidence. No merge-ready verdict.**
