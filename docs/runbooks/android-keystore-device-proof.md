# Android Cookbook — agreed Xiaomi scope integrated and closed

Date: 2026-10-11. Debug demo package `dev.passchef.mobile.demo`; test package `dev.passchef.mobile.demo.test`.
Release identity stays `dev.passchef.mobile`; debug has a separate UID/private data.
Build/JVM/emulator/repository reopen do not establish physical acceptance/process restart.
Owner2026-10-11: "Merge/push hôm nay, ghi rõ bài kiểm tra còn thiếu". Agreed Xiaomi scope **COMPLETE**; main56-JVM/build/lint passed; merged/pushed `b959aca`, archived/closed. Installed `0d38e62`:
independent debug/test APK hashes verified, entire Cookbook preserved
([metadata](/home/vantt/projects/passMan-recovery/20261011-keystore/unwrap-diagnostic-candidate-build.json)); incremental builder hashes superseded.
Prior `0a99d636` idle5 and `d2a76ca` E4 proofs remain below.
Windows adb sees owner-authorized Xiaomi14TPro/2407FPN8EG API36 over USB; release/demo
absent at preflight, pinned installs passed. Earlier missingADB/installrestriction
resolved; wireless pairing not adopted.

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
After the repair, **E4 enrollment recovery passed on `d2a76ca`**: a prompt held
at least327s produced current-process code15 at05:44:42Z, then recovery committed
a fresh alias/wrap IV while keeping cache/salt/cache IV unchanged and retirement
metadata empty. Owner locked the Cookbook, then genuine new-key biometric unlock
authenticated the same1 jar with the full record unchanged. Owner reported **one
visible recovery prompt**, so two distinct prompts are not directly confirmed.
[Reviewed control flow](../history/passchef-mobile-android-keystore/reports/recovery-independent-review.md)
requires a fresh CryptoObject and identity-checked real
callback after code15 before success; that is a source-based inference supported
by the recorded error→commit/new-key-unlock outcome. Source/APK pin unchanged.
Agreed current-Xiaomi scope merged/pushed `b959aca` and archived/closed; full original physical matrix remains incomplete.

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
build/review passed with no P1/P2. Actual-controller delayed-expiry recovery and
subsequent real new-key unlock now passed as described above; prior failures remain
recorded. [Error timing](/home/vantt/projects/passMan-recovery/20261011-keystore/retry-timestamp-diagnosis.json)
places the current-process code15 after the owner's held-prompt confirmation.

## Platform and security boundary

- Android8/API26 minimum; compile36, target34 retained from scaffold.
- Class3/strong enrollment needed for CryptoObject proof; unavailable retains password-only path.
- Keystore AES256/GCM, per-wrap/unwrap auth: API30+ `(0, AUTH_BIOMETRIC_STRONG)`, API26–29 `-1`.
- Enrollment invalidation enabled; API28+ StrongBox feature/Unavailable fallback, hardware level inspected,
  not assumed. JVM actual explicitly refuses Android hardware operations.
- Master key: Argon2id v19,t3,65536KiB,p4,32 bytes; real BouncyCastle1.85, matching
  proven extension WASM bytes. UTF-8 follows Web TextEncoder surrogate handling.
- Atomic bounded version1 ciphertext in `noBackupFilesDir`, backup/cloud/transfer disabled; <=5 jars,1–4096 UTF-8 bytes each.
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

Lockout builder71s/independent78s150tasks:56 JVM0fail/errors/skips, debug/test/release/lint0errors8warnings;
61 frozen hashes unchanged; release excludes proof Activity/log tag.
Real Argon/cache/ownership tests; control tests do not simulate biometric hardware. Earlier physical2probe(0.026s)/4control(0.032s)
results remain in [full report](../history/passchef-mobile-android-keystore/reports/keystore-recovery.md).
Only actual initial BiometricPrompt error7/9 may transfer a correct-password,
cache-authenticated session. Existing record and retirement queue remain byte-for-byte
unchanged: zero writes/retirement; only the disposable fresh alias is deleted.
New setup uses actual record absence and may atomically save encrypted password-only
cache. Cancellation/other errors and code15 retry→7/9 remain closed; no remembered
lockout bypass or authentication-policy downgrade. Debug logs actual callback
numeric code only; release no-op. Typed7/9 callback proof uncovered; OS availability fallback observed below.

## Safe native Windows adb route

Use native Windows binary from WSL, no `-a`/network exposure/USB changes/local fallback; historical TCP route retired.

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
2. Cancel/failed biometrics must stay closed with original-password guidance.
   Real initial error7/9 plus correct password opens the same cache without changing
   an existing record/retirement queue; wrong password preserves it. Retry15→7/9 stays closed.
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

Unexecuted typed7/9, retry exhaustion/exact-kill and environment matrix;
`d2a76ca` cancellation, pending-wrap background/restart
passes remain in [manual evidence](/home/vantt/projects/passMan-recovery/20261011-keystore/manual-evidence.json).
Owner reports exactly "screenshot không cho, quay bị đen", then "riêng paschef đen".
Screenshot blocked/only PassChef black are manual reports; no root visual/video capture or definitive Recents category claim.
Root verified own app left foreground, ordinary return(no force-stop) locked+FLAG_SECURE at13433.53–13436.77.
Earlier231.14s lock lacked confirmed input/session start. [Prior idle attempt](/home/vantt/projects/passMan-recovery/20261011-keystore/timer-idle-20261011T062931212610Z.json)
was INTERRUPTED before `timer_started`; real owner fingerprint then showed
"Device key unavailable"; numeric cause is unobserved, so expired unwrap is not inferred.
Post-reboot(uptime380s), old `0a99d636` real-owner idle5 **PASS**: open292.74s/locked305.95s from biometric status;
no post-setup touches ([proof](/home/vantt/projects/passMan-recovery/20261011-keystore/timer-idle-20261011T070708433357Z.json)).
Sampled foreground/awake/FLAG_SECURE; six known literals had no bounded own-UID hits, not continuous/comprehensive absence.
[Earlier total](/home/vantt/projects/passMan-recovery/20261011-keystore/timer-total-20261011T071305213998Z.json) FAILED before timer start after ~194s held real-owner prompt: device-key
unavailable, numeric cause unknown; whole Cookbook unchanged; that observer stopped/keep-awake0.
Diagnosis builder46s/independent140tasks69s,lint0/8; Android5formatter-only tests passed.
[Prior automated total](/home/vantt/projects/passMan-recovery/20261011-keystore/timer-total-20261011T072607082007Z.json) FAIL on `0d38e62`: real fresh unwrap/open, lastopen296.45s/firstlocked309.84s.
Four ADB tap requests67.52/122.08/189.21/242.98s lack proven delivery: no10min pass/bug classification.
[Manual total](/home/vantt/projects/passMan-recovery/20261011-keystore/timer-total-20261011T073313499790Z.json) FAIL: owner unlock unobserved600s, last `prompt_pending`; timer NEVER started.
Root force-stopped/restarted own demo COLD locked, whole Cookbook preserved; keep-awake0 verified, observers ended.
[Actual total10](/home/vantt/projects/passMan-recovery/20261011-keystore/timer-total-20261011T100657992502Z.json) **PASS** on `0d38e62`: bioanchor11850.32,open597.21s/locked610.30s; pins unchanged/secure samples/bounded logs clean.
Owner confirmed continuous title touches through lock ([exact reply](/home/vantt/projects/passMan-recovery/20261011-keystore/manual-total-20261011T100657992502Z-owner.json)); individual touch uptimes unmeasured.
Current wrong-password `password_open_failed` preserved entire ciphertext Cookbook.
[Actual OS-lockout availability fallback](/home/vantt/projects/passMan-recovery/20261011-keystore/actual-lockout-seed.json) **PASS**: owner5 failures/"too many attempts"; password submitted10.10s after probe, then Main opened "strong biometric unavailable".
Intentional runner-auth rejection was NOT a test pass; unique probe cleanup completed. Whole record/alias/retirement/salt/cache IV/cache unchanged; no same-jar UI-count claim.
Main3+2 failures emitted only5,5; initial7/9 not delivered, route remains56JVM/source coverage. Installed `0d38e62` frozen.
Final ownUID10422 snapshot7lines: six known literals0hits, no comprehensive log guarantee; wholecipherrecord894f73…/APKpins unchanged.
Keep-awake original0 RESTORED/verified; ADB workers ended/phone free. No-enrollment/noHW/API26/noStrongBox/typed7&9/exactkill/retryexhaustion unexecuted, accepted limits tracked—not passed.
[Accepted scope](../../plans/261011-keystore-recovery/reports/current-device-acceptance.md): source frozen/completion within agreed scope; main56-JVM/build/lint passed; merged/pushed `b959aca`, archived/closed.
