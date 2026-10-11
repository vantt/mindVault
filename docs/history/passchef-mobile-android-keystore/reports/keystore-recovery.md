# Android Keystore recovery — 2026-10-11

Scope: accepted original E2–E5 plus preserved E1; Core integrated at3532c160.
Status: candidate source implemented and independently reviewed/tested; actual
manual recovery now exposes an unresolved defect. Focused test diagnostics added.
Physical acceptance: **IN PROGRESS**; enrollment invalidation proven, complete
original-password recovery/new unlock still failing or pending.
Repair candidate `d2a76ca` over diagnostic `a9409ff`/baseline `e26bcd9` is committed,
pinned and installed, preserving the original Cookbook. Worktree remains open/
unmerged, not merge-ready; actual expiry/retry and complete enrollment recovery
await proof. No authentication/StrongBox policy changes. Inherited SDK-path
`local.properties` was untracked while its ignored local build file stayed intact.
Debug identity isolation `.demo` implemented after independent clean39-test pass;
release identity stays unchanged. Final impacted app/testAPK/lint builder check
passed in38s; APK metadata confirms `.demo` and `.demo.test` packages. Independent
post-isolation app/testAPK/lint pass completed in14s; no shared source change in this delta.
Bee state, original tooling WIP and golden tests remain unchanged.

## Implemented

- Real expect/actual boundary: Android Keystore AES256/GCM operation keys, JVM
  explicitly unsupported. Per-operation strong biometric CryptoObject for setup
  wrap and unlock unwrap; enrollment invalidation and guarded StrongBox fallback.
- Proven BC1.85 Argon2id moved from Core spike into this feature's production
  source set; same WASM vector and Web UTF-8 encoding verified in production test.
  This promotion belongs to accepted Keystore scope, not a change to Core history.
- Versioned bounded ciphertext-only record, <=5 seasoning jars, AES-GCM cache,
  random salt/IVs, app-private backup-excluded persistence and atomic replacement.
- Original-password cache authentication before alias mutation. New wrap commits
  before prior alias retirement; failures preserve old record, disposable alias
  is removed, bounded retirement metadata retries failed deletion after auth.
- Minimal FragmentActivity Compose demo with masked non-saveable inputs, FLAG_SECURE,
  explicit/background/idle5min/total10min locking. Single worker moves Argon/storage
  off UI; callback epochs prevent old requests reopening a session.
- Owned key/spice buffers are wiped on close/stale result; queued cleanup results
  are not discarded by lock. Cache encoding uses one owned plaintext buffer.
  Input strings/provider internals remain managed memory; no universal erasure claim.
- Real interactive physical instrumentation APK: actual Class3 prompts for wrap
  and unwrap, nonexportable/hardware inspection, unauthenticated refusal, cache/
  key equality after on-disk reopen, wrong-password preservation and cleanup.
  Actual process kill/relaunch and induced enrollment invalidation are manual cases.

## Review fixes before freeze

Early independent review found P2 input-key cleanup before corrupt/missing record
reads and a duplicate plaintext cache backing buffer. Both repaired with targeted
regression coverage. Root found handler-wide callback removal could strand owned
buffers; only named timer callback is removed now, stale results run cleanup, and
biometric success checks epoch before dispatch/worker work. Instrumentation proof
Activity is debug-only/nonexported in app process, not test APK's separate UID.

## Evidence and remaining proof

Builder final run: `./gradlew --no-daemon :shared:jvmTest :app:assembleDebug
:app:assembleDebugAndroidTest :app:lintDebug`, JDK17.0.20+8/SDK36: exit0,
BUILD SUCCESSFUL in53s;39 JVM tests,0 failures/errors/skips (26 inherited Core,
13 vault/security/transaction tests). Android app and test APKs built. Earlier
`:shared:assemble` passed alongside full build. App lint has0 errors,8 warnings
(existing target/version upgrade suggestions and missing icon); no shared lint
task exists. Source files remain under200 lines; whitespace check passes.

Freeze includes a real executor-shutdown
rejection regression: rejected fallback closes prepared master-key/spice buffers.
No local app build is presented as physical acceptance. Final physical-test equality
assertions use sanitized constant-time comparisons; their expected key copy is
wiped in finally. This test-only delta recompiled successfully in25s.
Independent tester: clean run exit0 in1m13s,107 tasks/105 executed,39 fresh JVM
tests0 failures/errors/skips plus app/testAPK/lint. Post-isolation impacted app/
testAPK/lint run exit0 in14s; actual APK badging/binary manifest confirms demo
package, test instrumentation target, disabled backup/extraction exclusions and
nonexported debug-only proof Activity. Reviewer has no remaining source P1/P2;
its separate report records the independent verdict and physical limitations.

Diagnostic delta builder: debug/test/release APKs and lint passed49s after an
explicit API31 caller guard corrected initial NewApi lint failure. Independent
same build passed16s,0 errors/8 known warnings; release dex excludes the debug
proof Activity/log tag. Installed hashes match
[a9409ff build metadata](/home/vantt/projects/passMan-recovery/20261011-keystore/actual-controller-probe-build.json).
Actual `WrapFailureProbeTest`: **OK(2 tests)** in0.026s, asserting exception
messages/unknown phases never enter summaries. No diagnosed cause or fix claim.

## Observed physical proof — baseline and diagnostic follow-up

Native Windows adb36.0.2 now connects to the owner-authorized physical Xiaomi14TPro,
model2407FPN8EG, API36 over USB. Both release and demo packages were absent at
preflight; both isolated app/test APK installations succeeded. Earlier WPD-only/
missing-ADB and `INSTALL_FAILED_USER_RESTRICTED` observations are historical and
resolved. No wireless-pairing transport was needed/adopted.

- Actual `KeystorePhysicalTest`: **OK(1 test)** in12.431s, host exit0; real strong
  biometric CryptoObject wrap/unwrap, absent-auth refusal, owned lock wiping,
  master-key/cache equality after disk reopen and wrong-password no-write.
- Key inspection: `hardwareProtected=true`, `nonexportable=true`, `cache=true`.
  No exact TEE/StrongBox classification is claimed by this boolean evidence.
- Manual synthetic setup/real wrap and demo biometric unlock passed; one spice jar
  authenticated. Background→return and actual force-stop→new-process launch were
  locked; encrypted data remained unchanged.
- Owner-observed biometric cancel passed with password guidance and unchanged
  record. Wrong-password attempt refused; full record digest and alias unchanged.
- Correct original-password recovery **failed twice after actual owner biometric
  authorization**. UI: "Could not save biometric wrap; prior data preserved".
  Entire old record and alias stayed unchanged. Those earlier attempts had no
  numeric diagnosis; a later actual-controller attempt established code15 below.
- Actual `AtomicRecordStorePhysicalTest`: **OK(1 test)** in0.013s; first create,
  atomic existing-record replacement and exact new-instance reopen passed. This
  focused storage pass does not prove the failing demo recovery path.
- MainActivity's owned window has FLAG_SECURE; physical screenshot/recents
  behavior remains to be checked.
- Focused `KeystoreRecoveryPhysicalTest` failed at its first fresh setup wrap
  after owner authorization, before record commit: `IllegalBlockSizeException`
  caused by `android.security.KeyStoreException`. One test/one failure in23.151s.
  This is not evidence of a recovery-only fault.
- Next diagnostic run **OK(1 test)** in17.193s: owner confirmed fingerprint and
  all four actual prompts passed. Password recovery authenticated old cache,
  fresh wrap/atomic replacement committed, old alias retired, and final unwrap
  restored the same key/spices. Setup/recovery actual `KeyInfo.securityLevel=2`
  (StrongBox). No product source change; prior numeric failure cause was not
  reproduced/captured.
- Actual demo later completed original-password fresh wrap: fresh alias, same
  encrypted cache/IV/salt and empty retirement metadata. Then owner added a
  fingerprint; old key correctly refused with original-password guidance and
  unchanged record. Post-enrollment wrong password also preserved record/alias.
- Post-enrollment correct password authenticated the cache and reached real
  owner-authorized new wrap, then failed with the same preservation message.
  Entire prior record remained unchanged. Complete enrollment recovery is unproven.

Observed baseline record SHA256:
`a2daf4a96dd4372f2c2ecb5203dd11da3c32f97a6067ae9c8dd14e87bb3144fc`.
Only ciphertext/digests and sanitized UI outcomes are recorded; no input values.

Evidence:
[physical progress](/home/vantt/projects/passMan-recovery/20261011-keystore/physical-progress.json),
[manual cases](/home/vantt/projects/passMan-recovery/20261011-keystore/manual-evidence.json),
[runner log](/home/vantt/projects/passMan-recovery/20261011-keystore/physical-instrumentation.log),
[atomic regression](/home/vantt/projects/passMan-recovery/20261011-keystore/atomic-regression-physical.log),
[focused wrap failure](/home/vantt/projects/passMan-recovery/20261011-keystore/recovery-regression-physical.log),
[diagnostic recovery pass](/home/vantt/projects/passMan-recovery/20261011-keystore/recovery-numeric-physical.log),
[probe sanitization tests](/home/vantt/projects/passMan-recovery/20261011-keystore/actual-controller-probe-test.log).
Runner timestamp:2026-10-11T04:13:05.418792Z. Root owns ADB and subsequent proof;
this documentation snapshot does not predict later results.

## Remaining acceptance

E4 remains open: repair actual demo's diagnosed post-enrollment wrap failure,
then prove original-password same-cache recovery and new biometric unlock.
Current debug controller emits only constant phase, class chain, public API33
numeric/system/transient/auth/retry fields and API31 key level; release is a no-op.
After the held prompt's owner fingerprint,
[numeric log](/home/vantt/projects/passMan-recovery/20261011-keystore/actual-controller-numeric.log)
records `phase=wrap-doFinal`, level2, code15, system/transient=true,
authRequired=false, retry2. Public code15 is `ERROR_KEY_OPERATION_EXPIRED`;
the operation expired, rather than an authentication-required failure.
Accepted wrap-only repair is implemented over `a9409ff`: exactly one cancellable
5s delay before a fresh Cipher and fresh genuine strong-biometric prompt, keeping
the same authenticated prepared Cookbook/disposable alias. Other errors/second
expiry retain preservation guidance; retry failure never writes a password-only
downgrade. Controller129/helper102 lines; shared state guards one-shot callback/
cipher identity, in-flight cleanup, stale/rejected delivery and committed alias.
Builder final59s exit0:48 fresh JVM tests (39 baseline+9 ownership/policy),
0 failures/errors/skips; debug/test/release APKs and lint0errors8knownwarnings.
Independent fresh run1m18s/150 tasks all executed:48 JVM0failures/errors/skips,
shared assembly, debug/test/release APKs and lint0errors8knownwarnings;53 frozen
source/build hashes unchanged. Independent reviewer found no P1/P2. Installed
APK hashes match
[d2a76ca metadata](/home/vantt/projects/passMan-recovery/20261011-keystore/expired-wrap-candidate-build.json);
[independent results](/home/vantt/projects/passMan-recovery/20261011-keystore/independent-expired-wrap-build.json).
Actual2 negative-classifier +2 probe sanitization tests passed together in0.032s on API36
([log](/home/vantt/projects/passMan-recovery/20261011-keystore/expired-wrap-negative-classifier-physical.log));
these are control tests, not biometric-retry proof. Current owner prompt is being
held; actual-controller delayed-expiry/fresh-prompt proof remains open.
No unauthenticated retry, StrongBox policy change or physical repair claim.
Remaining matrix cases:
lockout/failed biometrics, no enrollment, pending-operation lifecycle, actual
idle5min/total10min, screenshot/recents protection and sanitized app logcat.
This API36 device does not establish physical API26–27 behavior.

Temporary keep-awake is active during testing; recorded original value0 must be
restored before closing device work. [Runbook](../../../runbooks/android-keystore-device-proof.md)
contains exact commands/manual sequence. Source/build/runner and partial manual
passes do not close E2–E5 acceptance. Keep this original worktree open/unmerged
until the required remaining evidence is recorded; no completion claim yet.
