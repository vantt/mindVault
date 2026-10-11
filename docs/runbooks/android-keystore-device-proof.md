# Android Cookbook — physical Keystore proof

Date: 2026-10-11. Debug demo package `dev.passchef.mobile.demo`; test package `dev.passchef.mobile.demo.test`.
Release identity stays `dev.passchef.mobile`; debug has a separate UID/private data.
Build/compile evidence is separate from physical acceptance. Do not count a JVM,
emulator, compiled APK, or merely reopened repository as physical process-restart proof.
Physical acceptance is **IN PROGRESS**. Installed repair candidate is `d2a76ca`,
over diagnostic `a9409ff` and baseline `e26bcd9`; authentication/StrongBox policy
unchanged. Pinned APK installation preserved the original Cookbook and matches
[candidate metadata](/home/vantt/projects/passMan-recovery/20261011-keystore/expired-wrap-candidate-build.json).
Native Windows adb now sees the owner-authorized Xiaomi14TPro/2407FPN8EG, API36,
over USB. Neither release nor demo package existed at preflight. Both pinned APKs
installed successfully; the earlier install restriction and missing-ADB state are
resolved. Wireless pairing was not needed or adopted.

The physical runner passed **1 test in12.431s**, host exit0: real CryptoObject
wrap/unwrap, `hardwareProtected=true`, nonexportable key and authenticated cache.
This boolean does not distinguish TEE from StrongBox. Manual synthetic setup,
biometric unlock, cancellation, background-return locking, actual force-stop/
new-process restart locking and wrong-password no-mutation passed. Correct-password
re-wrap **failed twice after actual owner authorization**: the demo reported
"Could not save biometric wrap; prior data preserved" and the entire old record
and alias stayed unchanged. Genuine Android existing-record atomic replacement
passed its separate regression (1 test,0.013s); this does not establish successful
recovery. Focused real recovery instrumentation also failed at its first fresh
setup wrap after owner authorization: `IllegalBlockSizeException` caused by
`android.security.KeyStoreException`, before record commit. The evidence therefore
does not isolate the failure to recovery. The next diagnostic run then **passed
all four owner fingerprint prompts in17.193s**, including fresh wrap, password
recovery, replacement commit/retirement and final same-cache unlock. Actual setup
and recovery key security levels were2 (StrongBox). No product source changed;
the earlier failure's numeric cause was not captured or reproduced in this pass.
The demo subsequently completed correct-password fresh wrap with unchanged
encrypted cache and empty retirement metadata. Owner then added a fingerprint:
the old key was refused and the original-password path offered, with record
preserved. Wrong password after enrollment also preserved the entire record/alias.
Correct-password recovery after enrollment reached real owner-authorized wrap but
failed again with the same sanitized UI outcome; prior record stayed unchanged.
Complete enrollment recovery and new biometric unlock remain mandatory and open.
Feature/worktree is not closed or merge-ready; the recovery defect is unresolved.

Current local evidence:
[progress](/home/vantt/projects/passMan-recovery/20261011-keystore/physical-progress.json),
[manual cases](/home/vantt/projects/passMan-recovery/20261011-keystore/manual-evidence.json),
[physical runner](/home/vantt/projects/passMan-recovery/20261011-keystore/physical-instrumentation.log),
[atomic replacement regression](/home/vantt/projects/passMan-recovery/20261011-keystore/atomic-regression-physical.log),
[focused wrap failure](/home/vantt/projects/passMan-recovery/20261011-keystore/recovery-regression-physical.log),
[four-prompt diagnostic pass](/home/vantt/projects/passMan-recovery/20261011-keystore/recovery-numeric-physical.log),
[probe sanitization tests](/home/vantt/projects/passMan-recovery/20261011-keystore/actual-controller-probe-test.log).
Root owns device commands and updates these artifacts as further cases complete.
Actual-controller failure after the held prompt now has a numeric diagnosis:
`wrap-doFinal`, StrongBox level2, public code15 (`ERROR_KEY_OPERATION_EXPIRED`),
system/transient=true, authRequired=false, retry2. The provider operation expired;
owner authentication is not the reported problem. The accepted wrap-only repair
is now implemented over `a9409ff`: one cancellable5s delay, then a fresh Cipher
and fresh real strong-biometric prompt using the same authenticated prepared
Cookbook/disposable alias. Only API33+ typed public code15 at doFinal retries;
other errors/second expiry preserve prior data and guide manual password recovery.
Per-attempt identity and ownership guards cover cancellation/stale callbacks.
Builder48 JVM tests and debug/test/release/lint passed; independent fresh48-test
build/review passed with no P1/P2. Actual-controller delayed-prompt/new-prompt proof
remains open; current owner prompt is being held. Implementation
and local control tests do not establish successful physical repair.

## Platform and security boundary

- Android8/API26 minimum; compile36, target34 retained from scaffold.
- Enrolled Class3/strong biometric needed for CryptoObject proof. No enrollment or
  no hardware retains the original master-password-only path.
- Android Keystore AES256/GCM; authentication on every wrap and unwrap. API30+
  uses `(0, AUTH_BIOMETRIC_STRONG)`; API26–29 uses validity `-1`.
- Enrollment invalidation enabled. StrongBox opportunistic on API28+ with feature
  check and `StrongBoxUnavailableException` fallback; hardware level inspected,
  not assumed. JVM actual explicitly refuses Android hardware operations.
- Master key: Argon2id v19,t3,65536KiB,p4,32 bytes; real BouncyCastle1.85, matching
  proven extension WASM bytes. UTF-8 follows Web TextEncoder surrogate handling.
- Atomic, bounded version1 ciphertext record lives in `noBackupFilesDir`;
  backup/cloud/device-transfer disabled. Up to5 spice jars, each1–4096 UTF-8 bytes.
- Password-cache authentication precedes key rotation. Fresh alias wrap is committed
  before prior aliases retire; bounded retirement metadata retains retry work.
- App uses FLAG_SECURE, clears owned session bytes on explicit lock/background,
  locks after5min idle/10min total, never restores unlocked state after recreation.
  Managed immutable UI/provider strings cannot be guaranteed erased; masked inputs
  are cleared on submission/background and are never saveable state or logs.

## Build and independent checks

From the original Keystore worktree:

```sh
export JAVA_HOME=/home/vantt/dev-tools/jdk-17.0.20+8
export ANDROID_HOME=/home/vantt/dev-tools/android-sdk
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew --no-daemon :shared:jvmTest :shared:assemble \
  :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug
```

JVM tests exercise real Argon/AES-GCM, bounds, tampering, password no-write,
interrupted temp-file replacement, failed retirement retry, owned-buffer cleanup,
clock limits and late queued results. Fault injection tests model storage failures;
they do not emulate Keystore hardware or authenticate a biometric.
Earlier diagnostic debug/test/release build and lint independently passed in16s,
0 errors/8 known warnings. Release APK has no proof Activity/log tag. Actual
`WrapFailureProbeTest` passed2 sanitization tests in0.026s. Debug diagnostics whitelist
constant stages and emit only classes, guarded public numeric flags/retry and key
security level; no throwable messages/aliases/bytes. Release helper is a no-op.
Repair candidate independently reran150 tasks in1m18s:48 JVM tests with0 failures,
errors/skips; shared assembly, debug/test/release packaging and lint0errors8knownwarnings.
All53 source/build files retained their frozen hashes; release dex excludes proof
Activity/log tag. Actual2 negative-classifier +2 probe tests passed together on
the phone in0.032s; these four tests do not authorize a biometric or prove wrap recovery.
[Independent build](/home/vantt/projects/passMan-recovery/20261011-keystore/independent-expired-wrap-build.json),
[four physical control tests](/home/vantt/projects/passMan-recovery/20261011-keystore/expired-wrap-negative-classifier-physical.log).

## Safe native Windows adb route

Use the actual Windows binary directly from WSL. No `-a`, network-server exposure,
USB configuration changes, or silent local-daemon fallback is needed. Historical
TCP bridge attempts are not the current chosen route.

```sh
ADB_WIN=/mnt/c/Users/van.tran_fgorg/AppData/Local/Android/Sdk/platform-tools/adb.exe
"$ADB_WIN" devices -l
```

The owner connects/unlocks the device and accepts its USB authorization prompt.
Proceed only after a physical serial shows `device`; select that actual serial:

```sh
DEVICE_SERIAL='the-actual-listed-serial'
APP_APK=$(wslpath -w "$PWD/app/build/outputs/apk/debug/app-debug.apk")
TEST_APK=$(wslpath -w "$PWD/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk")
"$ADB_WIN" -s "$DEVICE_SERIAL" shell pm path dev.passchef.mobile.demo
# If an existing demo package is listed, preserve its data and review its origin
# before reinstall/manual setup; never clear unknown existing Cookbook data.
"$ADB_WIN" -s "$DEVICE_SERIAL" install -r "$APP_APK"
"$ADB_WIN" -s "$DEVICE_SERIAL" install -r "$TEST_APK"
"$ADB_WIN" -s "$DEVICE_SERIAL" shell am instrument -w -r \
  -e class dev.passchef.mobile.proof.KeystorePhysicalTest \
  dev.passchef.mobile.demo.test/androidx.test.runner.AndroidJUnitRunner
```

The test creates a distinct synthetic Cookbook file and alias, then requires two
owner-authorized prompts: **wrap**, then **unwrap** (120s deadline each).
It checks nonexportability, actual hardware-protection status, absent-auth refusal,
master-key equality, same spice cache after on-disk reopening, lock wiping and
wrong-password no-write; cleanup deletes only its own synthetic file/key.
Proof Activity is nonexported and debug-only in the app process/UID. No injected
biometric success, test-only unauthenticated key, or shared user Cookbook is used.
Record runner pass/fail, physical manufacturer/model/API, hardware status and time.
A test timeout/cancel/no enrollment is failure or blocked acceptance, never success.

## Owner-assisted manual acceptance

Use only synthetic master password/spices and record outcomes without their values:

1. Launch `dev.passchef.mobile.demo/dev.passchef.mobile.MainActivity`; setup1–5 spice jars + master password.
   Authorize setup wrap; lock; biometric unlock must show authenticated jar count.
2. Cancel prompt and try failed biometrics/lockout; original password path must stay
   available. Wrong password preserves record/aliases; correct password authenticates
   the same cache and authorizes a fresh wrap without reenrolling the Cookbook.
3. Induce real enrollment invalidation in Android settings (owner adds/removes a
   fingerprint). Biometric must route to password; correct original password must
   recover the same cache, then authorize a fresh device key. Old aliases retire.
4. Background the app and return: locked. While a prompt/Argon job is active,
   background/rotate/lock; late callbacks must never reopen a session.
5. Force-stop the app then relaunch; starts locked. Unlock the same persistent cache:
   this is the actual process-death/restart check absent from repository-reopen test.
6. Leave session idle5min; locks. Keep using it but reach10min total; locks.
7. Check a device with no enrollment/no strong hardware: setup/recovery works by
   password; UI states biometric unavailable. API26–27 must never call StrongBox.
8. Check screenshot/recents protections and review app-scoped logcat after flows:
   no plaintext seasoning, master password, derived key or raw exception content.

Owner fingerprint/enrollment actions need explicit participation. Remaining evidence:
repair the diagnosed expired-operation enrollment recovery wrap failure, then prove complete
original-password same-cache recovery and new biometric unlock;
failed-biometric/lockout and no-enrollment path; pending-operation lifecycle;
real idle5min/total10min and screenshot/recents/logcat checks. The owned MainActivity
window's FLAG_SECURE is confirmed; screenshot/recents behavior remains separate.
API26–27 behavior has guarded-source/build evidence, not physical coverage from
this API36 device.
The recorded temporary keep-awake setting is active for testing; restore its
original0 value before closing device work. Record new results before declaring
physical acceptance complete; prior runner/manual passes do not close these cases.
