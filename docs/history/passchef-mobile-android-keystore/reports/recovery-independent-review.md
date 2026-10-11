# Android Cookbook recovery — independent review

Date: 2026-10-11. Checkout: `passMan--wt--passchef-mobile-android-keystore`.
Comparison base: `3532c160`; latest reviewed probe commit: `0d38e62`.
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
- Debug target-APK/targetContext owns UID; AndroidX startActivitySync forbids test-APK hosting.
  [Official source artifact](https://dl.google.com/dl/android/maven2/androidx/test/core/1.6.1/core-1.6.1-sources.jar).
- Physical comparisons sanitize failures and wipe copies; disk reopen is named accurately.
- Debug `.demo` isolates release UID/files/keys; runbook uses its full component/
  `.demo.test` runner and preflight preserving unknown demo records.

## Security and correctness checks

- Core Argon2id v19/t3/65536KiB/p4/32bytes/Web UTF-8 uses genuine BC1.85.
- Cache authenticates before new alias; wrong password/tampering cause no record/key mutation.
- Version/length/index/size bounds enforced; no key/password/plain spices persisted.
  Ciphertext uses noBackupFilesDir, explicit cloud/device-transfer exclusions.
- Atomic replacement precedes old retirement; failed write preserves old record,
  deletes only new alias. Bounded retirement queue persists/retries after authentication.
- Nonexportable AES256/GCM uses per-use strong biometrics/enrollment invalidation.
  API30 auth parameters,26–29 duration-1 and28 StrongBox are guarded; StrongBox
  failures retry normal Keystore. Actual hardware protection is inspected.
- Each authorized callback must supply the same cipher and current prompt/operation
  generation. Repeat taps cannot initiate parallel unlocks; stale callbacks cannot
  publish sessions. Session buffers zero on lock/background; idle5min/total10min
  limits use monotonic time and cold recreation never restores unlocked state.
- Argon/storage operations use one worker. Product messages sanitize failures;
  no product key/password/spice logging found. FLAG_SECURE is applied. UI inputs
  use remember rather than saveable state and clear on submission/background.
  Immutable managed strings/provider copies cannot be promised erased.
- JVM hardware is unsupported; fault-injection storage tests make no hardware claim.

## Independent build/test evidence

Initial clean JDK17/SDK36 run passed39 tests (Core26/vault13), zero failures/skips,
107 tasks/105 executed in73s; shared/app/test artifacts built, lint0errors/8warnings.
Isolated-package rerun passed14s/93tasks; APKs confirm `.demo`/`.demo.test`, runner,
backup exclusions and debug-only proof. Synthetic isolated tests exercise actual
wrap/unwrap, absent-auth, equality, hardware, persistence and cleanup.

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

Safe debug probe leaves policy unchanged; release is no-op/proof-tag excluded.
Build passed16s/140tasks,0errors/8warnings; formatter2/0.026s device pass proves
sanitization only (`actual-controller-probe-build.json`). Later real wrap log
captured code15, system/transient=true, authRequired=false, retry2/StrongBox2:
expired operation for that attempt, not a retrospective cause for earlier failures.
[Official code15 definition](https://developer.android.com/reference/android/security/KeyStoreException#ERROR_KEY_OPERATION_EXPIRED).

## Frozen expired-operation repair — source review

Reviewed wrapping/state/typed classifier/controller/test scope against focused plan.
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
`independent-expired-wrap-build.json` pins identities;53 files stable/release isolated.
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
Earlier231s lock lacked a session-start anchor; `timer-idle-20261011T062931212610Z.json`
was interrupted before timer start. Later Device key unavailable had no numeric
diagnosis. Failed probes remain; neither establishes a timer/code defect.

## Lockout fallback defect and candidate source review

CONTEXT D3/plan:61 require lockout password fallback. In d2a76ca MainActivity:35–36
loses error reason; Controller:71–74 re-prompts correct passwords; VaultWrapping:58–60
closes authenticated data on prompt error. AOSP permits canAuthenticate success
during both lockouts, so this supported state can trap password recovery.
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
Independent: **56 JVM tests, zero failures/errors/skips,150/150 tasks,78s**; debug/test/release/shared artifacts, lint0/8,61 files/hashes/modes stable.
`independent-lockout-build.json` and `lockout-candidate-build.json` pin APK identities;
Release excludes proof Activity/tag. Debug/test installed-r; hashes match pins/whole Cookbook unchanged.
Root observed one genuine new-candidate biometric unlock with one jar; actual7/9 remains unproven.
Installed0a idle5 **PASS**: `timer-idle-20261011T070708433357Z.json`; fresh owner biometric/no later taps, open292.74s/locked305.95s.
APKs equal; six synthetic literals had no bounded own-UID hits. Awake/foreground/FLAG_SECURE sampled, not continuously traced.
Total10 before-start FAIL (`timer-total-20261011T071305213998Z.json`): key unavailable, no numeric cause;7/9/screens pending.

## Installed unwrap diagnostic delta — partial physical acceptance

Three-file probe review: **no P1/P2**; repository ownership/failure wiping and fixed phases remain accurate.
No retry/auth/storage change; release never logs/invokes inspector. Four reviewed file hashes remained stable.
Independent app rerun passed69s/140tasks all executed, lint0/8;61 files stable/shared unchanged, prior56 JVM tests applicable.
`independent-unwrap-diagnostic-build.json` pins tester APKs, superseding different builder incremental identities.
Committed0d38e62 installed-r with exact tester hashes/unchanged Cookbook; Android formatter **5 passed/0.027s**, no hardware assertions.
Earlier total10 FAIL: `timer-total-20261011T072607082007Z.json`, last open296.45s/locked309.84s.
Four requested header taps67.52/122.08/189.21/242.98s lack delivery proof; no product-bug classification.
Owner-touch073313 timed out600s/prompt_pending before timer start; force-stop/cold restart locked, record unchanged/keepawake0 restored.
Fresh unwrap succeeded without numeric error; older held unwrap failures remain unclassified. Prior0a idle5 PASS retained above.
**Actual0d total10 PASS**: `timer-total-20261011T100657992502Z.json`, last open597.21s/first locked610.30s; APKs before/after identical.
`manual-total-20261011T100657992502Z-owner.json`: trusted direct owner confirmation of regular manual title touches until lock; individual touch times unmeasured.
Actual0d wrong-password terminal failure preserved whole encrypted record. Main3+2 rejected scans yielded callbacks[5,5], not7.
Existing proof harness first-wrap five unenrolled-finger scans seeded owner-confirmed “too many attempts”; intentional auth rejection/cleanup, not a runner pass.
Root submitted ordinary correct password10.10s after runner completion, PID21987; `actual-lockout-seed.json` records the result.
**Physical OS-lockout availability fallback PASS**: UI “Cookbook open by password; strong biometric unavailable”; no jar count displayed.
Whole record SHA894f73e51e316b6a369368ad4cebbc901609ccfe7eabe855aa73c2cbf4e7a4d6 unchanged; installed0d APK identities matched.
Callback codes empty/numeric cause unknown: availability/passwordOnly path passed; typed initial7/9 path remains source/JVM-reviewed only. Seed JSON status INCONCLUSIVE refers to typed-code proof.
Native screen evidence (`manual-evidence.json/native_screen_protection`): owner “screenshot không cho, quay bị đen”, then “riêng paschef đen”; screenshot blocked/only PassChef black reported, no root visual/video capture or exact Recents-category claim.
Root confirmed own app left foreground without reading foreign UI; ordinary same-Main launch/no force-stop returned locked/FLAG_SECURE at13433.53–13436.77.
Whole ciphertext record SHA894f… unchanged; installed0d APK pins matched. No app/data clear occurred.
Final UID10422 bounded snapshot7 lines/six synthetic literals/no hits; not whole-flow absence. Keepawake0 restored/all ADB workers ended.
API26–29/no hardware/no enrollment/StrongBox-absent and exact process-kill/retry exhaustion unexecuted; force-stop evidence above remains distinct. See [device runbook](../../../runbooks/android-keystore-device-proof.md).
**Validated Xiaomi checks only; owner scope acceptance pending, no original full-matrix-complete/merge-ready verdict.**

Unresolved: owner acceptance, actual typed7/9, remaining matrix/retry/lifecycle and exact Recents-category observation. Two distinct visible retry dialogs were not observed or claimed.
