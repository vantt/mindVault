# Android Cookbook — physical Keystore proof

Date: 2026-10-11. Debug demo package `dev.passchef.mobile.demo`; test package `dev.passchef.mobile.demo.test`.
Release identity stays `dev.passchef.mobile`; debug has a separate UID/private data.
Build/compile evidence is separate from physical acceptance. Do not count a JVM,
emulator, compiled APK, or merely reopened repository as physical process-restart proof.
Current native Windows adb sees the authorized Xiaomi14TPro/API36 over USB.
APK installation is blocked by INSTALL_FAILED_USER_RESTRICTED; physical steps
below remain pending until the owner permits installation on the device.
Latest USB observation: Xiaomi14TPro, VID2717/PIDFF40, WPD transfer-files interface
only; no ADB interface after two USB-debug toggle/replug attempts. Wireless pairing
has been requested but is not yet adopted/verified; commands below remain the
verified native Windows adb route once an authorized physical serial is available.

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

Owner fingerprint/enrollment actions need explicit participation; agents must not
manufacture acceptance. Instrumented real hardware and manual enrollment/lockout/
process/timer cases remain pending until their evidence is recorded.
