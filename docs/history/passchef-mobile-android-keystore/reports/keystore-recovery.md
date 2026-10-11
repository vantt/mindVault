# Android Keystore — current-device checks accepted; coverage pending

Scope: accepted original E2–E5 plus preserved E1; Core integrated at3532c160.
Status: source independently reviewed/tested; actual expired-operation repair and
post-enrollment original-password recovery/new-key unlock now passed on `d2a76ca`.
Current-device feasible checks **ACCEPTED**; original-matrix coverage decision pending.
Installed diagnostic `0d38e62`: independent debug/test hashes verified, whole Cookbook
preserved, cold start locked. Prior `0a99d636` real biometric1jar/idle5 and `d2a76ca`
E4 proofs stand; worktree stays open/unmerged, remaining matrix pending.
Authentication/StrongBox unchanged; SDK `local.properties` untracked, ignored build file retained.
Debug `.demo` isolation followed independent clean39; release unchanged. Builder38s/
independent14s app/test/lint confirmed `.demo`/`.demo.test`, shared unchanged.
Bee state, original tooling WIP and golden tests remain unchanged.

## Implemented

- Real expect/actual boundary: Android Keystore AES256/GCM operation keys, JVM
  explicitly unsupported. Per-operation strong biometric CryptoObject for setup
  wrap and unlock unwrap; enrollment invalidation and guarded StrongBox fallback.
- Proven BC1.85 Argon2id moved from Core spike into this feature's production
  source set; same WASM vector and Web UTF-8 encoding verified in production test.
  This promotion belongs to accepted Keystore scope, not a change to Core history.
- Bounded ciphertext-only record, <=5 jars/AES-GCM cache, random salt/IVs, backup-excluded private atomic persistence.
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
  Process kill/relaunch and enrollment invalidation are manual.

## Review fixes before freeze

Early independent review found P2 input-key cleanup before corrupt/missing record
reads and a duplicate plaintext cache backing buffer. Both repaired with targeted
regression coverage. Root found handler-wide callback removal could strand owned
buffers; only named timer callback is removed now, stale results run cleanup, and
biometric success checks epoch before dispatch/worker work. Instrumentation proof
Activity is debug-only/nonexported in app process, not test APK's separate UID.

## Evidence and remaining proof

Baseline builder53s/independent73s:39 JVM(26 Core+13 vault)0fail/errors/skips, shared/app/test/lint0errors8warnings;
no shared lint task. Source under200 lines; whitespace check passes.

Executor-shutdown rejection wipes prepared buffers. Physical assertions use sanitized
constant-time equality and finally-wipe expected key copy; test-only compile passed25s.
Post-isolation independent app/testAPK/lint14s: demo package/test target, backup
exclusions and nonexported debug-only proof Activity confirmed; baseline review noP1/P2.

Diagnostic builder49s/independent16s,lint0/8; API31 corrected NewApi, release excludes proof tag/Activity.
[a9409ff metadata](/home/vantt/projects/passMan-recovery/20261011-keystore/actual-controller-probe-build.json) matched install; probe2PASS0.026s excludes messages/unknown phases.

## Observed physical proof — baseline and diagnostic follow-up

Windows adb36.0.2 connects owner-authorized Xiaomi14TPro/2407FPN8EG API36 over USB;
release/demo absent at preflight, isolated APK install passed. Earlier WPD-only/
missingADB/installrestriction resolved; wireless pairing not adopted.

- Actual `KeystorePhysicalTest`: **OK(1 test)** in12.431s, host exit0; real strong
  biometric CryptoObject wrap/unwrap, absent-auth refusal, owned lock wiping,
  master-key/cache equality after disk reopen and wrong-password no-write.
- HardwareProtected/nonexportable/cache=true; boolean alone does not classify TEE/StrongBox.
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
  behavior was still unchecked at this earlier stage; final owner reports below.
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

Baseline SHA256 `a2daf4a96dd4372f2c2ecb5203dd11da3c32f97a6067ae9c8dd14e87bb3144fc`; only ciphertext/digests/sanitized outcomes, no input values.

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

Debug emits constant phase/classes, API33 numeric/system/transient/auth/retry and API31 key level; release no-op.
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
was installed and hash-verified; actual OS7/9 fallback remains pending.
`d2a76ca` manual cancel, wrong password, pending-wrap background(return locked after6s)
and force-stop/new-PID restart passed with whole-record preservation; see manual artifact.
Earlier231.14s lock lacked confirmed input/session start. [Prior idle attempt](/home/vantt/projects/passMan-recovery/20261011-keystore/timer-idle-20261011T062931212610Z.json)
was INTERRUPTED before `timer_started`; real owner fingerprint then produced UI
"Device key unavailable"; numeric cause unobserved, no code15 unwrap inference.
Post-reboot(uptime380s) `0a99d636` real-owner idle5 **PASS**: open292.74s/locked305.95s from biometric status, no later touches
([proof](/home/vantt/projects/passMan-recovery/20261011-keystore/timer-idle-20261011T070708433357Z.json)).
Sampled foreground/awake/FLAG_SECURE; six known literals had no bounded own-UID hits, not continuous/comprehensive absence.
[Earlier total](/home/vantt/projects/passMan-recovery/20261011-keystore/timer-total-20261011T071305213998Z.json) FAILED before timer start: ~194s held real-owner prompt/device-key
unavailable; numeric cause unobserved, whole Cookbook unchanged; that observer stopped/keep-awake0.
Diagnostic `0d38e62` builder46s/independent140tasks69s,lint0/8; Android5formatter-only PASS; shared/release-noop unchanged, no retry.
[New pinned metadata](/home/vantt/projects/passMan-recovery/20261011-keystore/unwrap-diagnostic-candidate-build.json):
installed independent outputs supersede incremental hashes; older held-unwrap numeric cause unclassified.
[Prior automated total](/home/vantt/projects/passMan-recovery/20261011-keystore/timer-total-20261011T072607082007Z.json) FAIL: real fresh unwrap/open, lastopen296.45s/firstlocked309.84s.
ADB requests67.52/122.08/189.21/242.98s lack proven delivery; no10pass/productbug classification.
[Manual total](/home/vantt/projects/passMan-recovery/20261011-keystore/timer-total-20261011T073313499790Z.json) FAIL: owner unlock unobserved600s, last `prompt_pending`; timer NEVER started.
Root force-stop/new-process COLD locked, whole Cookbook preserved(SHA894f73…); keep-awake0 verified, observers ended.
[Actual total10](/home/vantt/projects/passMan-recovery/20261011-keystore/timer-total-20261011T100657992502Z.json) **PASS** on `0d38e62`: bioanchor11850.32,open597.21s/locked610.30s; pins unchanged/secure samples/bounded logs clean.
Owner confirmed touches through lock ([exact trusted reply](/home/vantt/projects/passMan-recovery/20261011-keystore/manual-total-20261011T100657992502Z-owner.json)); individual touch uptimes unmeasured.
Current wrong-password visible `password_open_failed`, whole ciphertext Cookbook unchanged.
[Physical OS-lockout availability fallback](/home/vantt/projects/passMan-recovery/20261011-keystore/actual-lockout-seed.json) **PASS**: owner5failed/"too many attempts"; password submitted10.10s after probe, then Main opened "strong biometric unavailable".
Intentional runner refusal NOTtestPASS; unique file/alias cleaned. Whole record+alias/retirement/salt/cache IV/cache unchanged; no same-jar UIcount claimed.
Main3+2 failed fingers logged only5,5; initial7/9 not delivered,56JVM/source route proof only. Current `0d38e62` frozen; idle5 prior `0a99d636`.
Owner exact reports "screenshot không cho, quay bị đen", then "riêng paschef đen": screenshot blocked/only PassChef black, no root visual or definitive Recents category claim.
Root ownforeground exit→ordinary return(no force-stop) locked+FLAG_SECURE13433.53–13436.77; fullcipherrecord894f73…/APKpins unchanged.
Final bounded ownUID10422 snapshot7lines/sixknownliterals0hits, not comprehensive absence; keep-awake original0 RESTORED verified, ADB workers ended/phone free.
No-enrollment/noHW/API26/noStrongBox/typed7&9/exactkill/retryexhaustion matrix coverage decision pending; older unwrap numeric remains unknown, not a new defect.
[Scope decision](../../../../plans/261011-keystore-recovery/reports/current-device-acceptance.md); original matrix not complete, source frozen/worktree unmerged.
