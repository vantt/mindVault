# E4 typed biometric lockout — focused execution plan

Date: 2026-10-11. Current documentation HEAD: `e4692d0`; product: `d2a76ca`.
Root owns implementation routing. This plan changes only original D3/E4 fallback;
no source, device, build or workflow actions were performed by its planner.

## Accepted contract and defect

- CONTEXT.md D3: invalidation/repeated unlock failure → master-password session,
  eventual fresh wrapping, no data deletion or forced onboarding. D5 permits
  a password-derived in-memory session; hardware unwrap still requires biometrics.
- Original plan.md:55/60 permits password-only cache when biometrics are absent;
  :61 explicitly probes real ERROR_LOCKOUT/ERROR_LOCKOUT_PERMANENT password fallback.
  Recovery plan.md:44–50 requires original-salt/cache authentication before mutation.
- Current MainActivity:35–36 discards the prompt error reason; Controller:71–74
  sends correct passwords to wrapping whenever canAuthenticate succeeds;
  VaultWrapping:58–60 closes authenticated data on errors and repeats password advice.
- AOSP explicitly tests canAuthenticate(BIOMETRIC_STRONG)==SUCCESS during both
  timed/permanent lockout. Thus availability polling cannot identify this case.
  [Official platform regression](https://android.googlesource.com/platform/frameworks/base/+/9759067e38ee5aa7c8ba62201f619cc0bc97bc40/services/tests/servicestests/src/com/android/server/biometrics/BiometricServiceTest.java#1240).
- AndroidX ERROR_LOCKOUT=7 and ERROR_LOCKOUT_PERMANENT=9 describe prompt lockout.
  [Official definitions](https://developer.android.com/reference/androidx/biometric/BiometricPrompt#ERROR_LOCKOUT).

## Focused implementation TODOs

- [ ] Preserve a typed prompt failure reason from real onAuthenticationError.
  Classify only BiometricPrompt constants7/9 as lockout. Source namespace must
  distinguish biometric codes from Keystore codes. No matching errString/text,
  no simulated7/9 for app cancellation, no device-credential or weak authenticator.
- [ ] Keep genuine CryptoObject identity/nonce/epoch checks. Ordinary cancel,
  negative-button, mismatch, generic error and three-reject app cancellation
  remain closed; their user action never implies password authentication.
- [ ] On the INITIAL wrapping prompt's actual7/9, transfer already master/cache-
  authenticated PreparedVault ownership once to a password-lockout session path.
  Close wrapping ownership and remove only its disposable new alias. Do not
  treat this as successful biometric wrapping or reuse any authorized Cipher.
- [ ] For an EXISTING Cookbook, publish the authenticated session without any
  store write or retireKeys call: exact record bytes, original alias, salt,
  cache/cacheIV/wrapped key/IV and pending retirement metadata remain unchanged.
  Defer fresh wrapping until a later genuine biometric operation can succeed.
- [ ] For FIRST SETUP with no record, root explicitly accepts the original
  password-only encrypted-cache shape after typed7/9. Commit that cache atomically
  before session publication; no raw key/spice persistence and no wrapped alias.
  Original plan explicitly names absent/unavailable biometrics rather than this
  setup error combination; root's direction resolves that operational detail.
- [ ] Distinguish setup vs recovery from the actual initial record presence.
  An empty alias is insufficient: a previously password-only Cookbook also has
  an empty alias and must take the existing-record no-write path.
- [ ] Keep the existing no-hardware passwordOnly path unchanged. Its
  commitPasswordOnly writes prepared.record unchanged; nevertheless the lockout
  session path must skip even that write and retirement to preserve exact bytes.
- [ ] Prior biometric-only7/9 returns password guidance. Next explicit correct
  password may get immediate real7/9 on its INITIAL wrapping prompt and then open
  the password session. No persisted/remembered lockout bypass flag is necessary;
  restored hardware is always eligible for the next genuine biometric request.
- [ ] A code15 expired doFinal retry NEVER falls back: its retry prompt7/9,
  cancellation or other error stays closed, preserves prior record and cleans
  disposable alias. A later explicit password action is a new operation, whose
  initial typed7/9 can follow the accepted master-password fallback.
- [ ] Preserve busy/active lifecycle semantics and session timer start. Lock,
  background/destroy, stale callbacks and rejected worker/main delivery wipe
  owned data; no double transfer, no concurrent commit and no committed-key deletion.
  If first-setup commit fails, close session, clean alias and show sanitized guidance.
- [ ] Keep files under200 lines; add only focused helpers when extraction is needed.

## Verification TODOs

- [ ] Typed classifier tests cover real constant7/9, unrelated codes, generic
  cancellation and numeric namespace distinction; never assert simulated hardware.
- [ ] Orchestration/ownership tests prove existing lockout fallback performs ZERO
  writes/deletions of original alias/retired aliases, authenticates preserved cache,
  transfers exactly once and removes disposable alias. Wrong password fails before
  key creation/session delivery; reopened records match exact previous bytes.
- [ ] Setup regression proves typed7/9 commits encrypted-cache-only record; an
  existing empty-alias record gets no write. Failed setup commit does not open.
- [ ] Code15→retry prompt7/9 stays closed with no downgrade/write; initial7/9 opens
  only after password/cache authentication. Cancel/mismatch/generic errors stay closed.
- [ ] Cover prior bio-only7/9→explicit password→initial7/9; restored biometric can
  still prompt. Stale/background/destroy/rejection and duplicate callbacks close
  retained buffers and never publish a stale session or delete a committed alias.
- [ ] Preserve golden tests and existing48-test baseline. Builder runs impacted
  JVM/debug/test/release/lint; independent reviewer/tester verify frozen delta.
- [ ] Root-owned physical probe on isolated synthetic demo: capture ACTUAL typed
  onAuthenticationError source/code only in guarded debug diagnostics (no errString,
  Throwable/message, alias, key, password, spice or biometric material). App's
  three-failure cancel is not evidence of OS7/9; never spoof it in proof logs.
- [ ] Real7/9→correct password opens same cache, exact unchanged record; wrong
  password stays closed/unchanged; lock/restart relocks. After OS lockout clears,
  genuine biometric remains usable and later rewrap follows original ordering.
  Record actual device/API/error code and disclose unexecuted permanent-lockout
  or no-hardware branches; control tests do not certify those hardware cases.

## Unresolved questions

Will this Xiaomi emit actual7/9 before the app's three-failure cancellation, and
which physical lockout branches can the owner safely exercise? Root owns the
device test and acceptance decision. Source repair/build/physical pass pending.
