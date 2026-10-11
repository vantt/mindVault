# Android Keystore recovery — 2026-10-11

Scope: accepted original E2–E5 plus preserved E1; Core integrated at3532c160.
Status: source independently reviewed/tested; actual expired-operation repair and
post-enrollment original-password recovery/new-key unlock now passed on `d2a76ca`.
Physical acceptance: **IN PROGRESS**; remaining lifecycle/limits/fallback cases open.
Committed candidate `0a99d636` is installed; debug/test hashes match pinned metadata
and the entire Cookbook remained unchanged. One genuine biometric open authenticated
1 jar. Prior `d2a76ca` E4 proof stands; worktree stays open/unmerged, matrix pending.
No authentication/StrongBox policy changes. Inherited SDK-path
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

Baseline builder53s/independent clean73s:39 JVM tests(26 Core+13 vault),0 failures/
errors/skips; shared/app/test assembly and app lint0errors8knownwarnings. No shared
lint task exists. Source stays under200 lines; whitespace check passes.

Executor-shutdown rejection wipes prepared buffers. Physical assertions use sanitized
constant-time equality and finally-wipe expected key copy; test-only compile passed25s.
Post-isolation independent app/testAPK/lint run exit0 in14s; APK metadata confirms demo
package, test instrumentation target, disabled backup/extraction exclusions and
nonexported debug-only proof Activity. Reviewer has no remaining source P1/P2;
its separate report records the independent verdict and physical limitations.

Diagnostic debug/test/release/lint builder49s, independent16s:0errors8warnings;
API31 guard fixed initial NewApi lint; release excludes proof Activity/log tag.
[a9409ff metadata](/home/vantt/projects/passMan-recovery/20261011-keystore/actual-controller-probe-build.json)
matches installed hashes. Actual probe2tests passed0.026s: no messages/unknown phases.

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
  Entire prior record remained unchanged; enrollment recovery was unproven at that point.

Baseline record SHA256 `a2daf4a96dd4372f2c2ecb5203dd11da3c32f97a6067ae9c8dd14e87bb3144fc`.
Only ciphertext/digests and sanitized outcomes recorded; no input values.

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

## Expired wrap repair and remaining acceptance

E4 enrollment recovery now **PASS on pinned `d2a76ca`**. Following a genuine owner
prompt held at least327s, current-process12308 code15 was recorded at05:44:42Z;
[safe timing diagnosis](/home/vantt/projects/passMan-recovery/20261011-keystore/retry-timestamp-diagnosis.json)
places it after held-prompt owner confirmation. The app then committed fresh alias/
wrap IV with identical encrypted cache, salt and cache IV; retired list empty.
Owner Lock→real new-key biometric unlock authenticated the same1 jar and preserved
the entire new record. Evidence keys: `enrollment_recovery_fixed_candidate`,
`enrollment_recovered_key_biometric_unlock` in the linked manual artifact.

Owner reported **one visible recovery prompt** (`recovery_retry_owner_observation`);
separate visual confirmation of two prompts is absent. [Reviewed source](recovery-independent-review.md) requires
fresh Cipher/CryptoObject and identity-checked genuine callback after code15 before
success; this is control-flow inference, supported by actual error→commit and
subsequent new-key unlock, not a claim that two prompts were directly observed.
Pinned source/APK unchanged; previous failures remain historical evidence.

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
these are control tests, separate from the actual-controller E4 pass above.
No unauthenticated retry or StrongBox policy change.

## Typed lockout fallback candidate — physical proof pending

`0a99d636` accepts only actual initial BiometricPrompt error7/9 after correct-password
cache authentication. Existing-record session transfer performs zero writes/retirement,
preserving the whole record and retirement queue; only its disposable fresh alias is
deleted. New setup checks actual record absence, then may save encrypted password-only
cache atomically. Cancel/other errors and code15 retry→7/9 stay closed; no remembered
lockout bypass. Debug emits callback numeric code only; release helper remains a no-op.
Builder71s and independent fresh78s/150 executed tasks:56 JVM tests(48+8 new real
repository/cache/ownership cases),0failures/errors/skips; debug/test/release build,
lint0errors8knownwarnings;61 frozen files unchanged and release excludes proof code.
Independent review has no new P1/P2; prior lockout P2 is addressed in source.
[Pinned build](/home/vantt/projects/passMan-recovery/20261011-keystore/lockout-candidate-build.json)
is installed and hash-verified; actual OS7/9 fallback remains pending.
`d2a76ca` manual cancel, wrong password, pending-wrap background(return locked after6s)
and force-stop/new-PID restart passed with whole-record preservation; see manual artifact.
No timer pass: existing session locked231.14s after requested tap, but input delivery/
session start were unobserved. Latest [idle attempt](/home/vantt/projects/passMan-recovery/20261011-keystore/timer-idle-20261011T062931212610Z.json)
was INTERRUPTED before `timer_started`. Real owner fingerprint then produced UI
"Device key unavailable"; numeric cause unobserved, no code15 unwrap inference.
Root awaits owner reboot after Xiaomi USB debugging(Security settings) enablement;
temporary keep-awake restored0. No source fault follows from uncertain input delivery.
Remaining: actual7/9, retry cancel/exhaustion, remaining lifecycle/no-enrollment matrix,
real5min idle/10min total, native screenshot/Recents and sanitized-log coverage.
API36 does not establish physical API26–27 behavior. [Runbook](../../../runbooks/android-keystore-device-proof.md)
has commands/manual sequence; partial passes do not close E2–E5. Worktree open/unmerged.
