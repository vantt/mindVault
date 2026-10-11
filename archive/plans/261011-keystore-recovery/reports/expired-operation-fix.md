# E4 expired wrapping operation — focused execution plan

**Status audit — 2026-10-11: Done/closed within final owner-accepted Xiaomi scope.**
Wrap-only source repair `d2a76ca` implemented/reviewed; independent48 JVM tests
and150 build tasks passed before subsequent lockout changes. Actual controller
recovered after captured code15, committed fresh wrapping and retired the old
alias; subsequent genuine biometric unlock passed. Owner saw ONE visible dialog:
fresh CryptoObject/callback after expiry is source/control-flow inference, not
independent visual proof of two dialogs. Exact hardware retry-exhaustion/cancel
windows remain tracked unexecuted coverage, not fulfilled physical assertions.
Final [acceptance](current-device-acceptance.md) includes source `0d38e62`,
56 JVM/150 main tasks and explicit owner acceptance; main `b959aca` pushed/closed.
Earlier failures without numeric evidence remain unclassified. The observations,
prospective verification and open questions below describe the initial plan.

Date: 2026-10-11. Planner/reviewer only; root owns implementation routing.
Scope: original password recovery wrapping failure protocol, no new mobile feature.
Read current controller, prompt host, Keystore actual, repository and owned-work
boundary; original E4/E5 and recovery plan remain the acceptance contract.

## Observed fault and platform evidence

`/home/vantt/projects/passMan-recovery/20261011-keystore/actual-controller-numeric.log`
records real owner-authorized `wrap-doFinal`, StrongBox level2, public code15,
system=true, transient=true, authRequired=false, retry policy2. This identifies
the captured attempt, not every earlier failure lacking numeric diagnostics.
Android defines code15 as an evacuated KeyMint operation and recommends a new
crypto object. Policy2 recommends an initial delay between five and thirty
seconds. Neither fact permits reuse of an authorized dead operation.
[Official API33 definitions](https://developer.android.com/reference/android/security/KeyStoreException#ERROR_KEY_OPERATION_EXPIRED).
[Official backoff policy](https://developer.android.com/reference/android/security/KeyStoreException#RETRY_WITH_EXPONENTIAL_BACKOFF).

## Smallest safe change

1. Extract the wrapping transaction into a focused helper; keep controller and
   helper under200 lines. Preserve the existing per-use strong-biometric policy,
   StrongBox selection, cache authentication, record format and retirement order.
2. Classify only a failure from `Cipher.doFinal` before record commit. On API33+
   inspect a bounded cause chain for public `ERROR_KEY_OPERATION_EXPIRED` (15).
   Never match text, generic IllegalBlockSizeException, or transient flags alone.
3. Permit one automatic retry: maximum two real owner prompts for this wrap.
   Retain the already password/cache-authenticated PreparedVault and the same
   disposable new alias. Do not create another key or rederive the password.
4. Schedule a cancellable five-second delay before creating the new Cipher.
   Then call `wrapCipher(alias)` again and display a new genuine
   BIOMETRIC_STRONG CryptoObject prompt. Require the returned cipher identity.
   Never call doFinal twice on the old cipher or treat old authorization as new.
5. Use the fresh cipher's IV and ciphertext for the atomic complete-record
   replacement; retire the old alias only after successful commit. The old
   Cookbook/cache/salt and alias remain unchanged while waiting or on failure.
6. Cancel, exhausted retry, other errors, unavailable biometric, API<33 or stale
   generation keep sanitized master-password guidance. An expired-operation
   retry failure must not silently write a password-only downgrade.
7. Explain the second prompt with a short Cookbook status; keep busy true while
   the operation owns its prepared data. Never display credentials or numeric
   provider details in release UI. Existing debug diagnostics may remain.

## Ownership and lifecycle edges

- One operation owns PreparedVault/new alias across worker, scheduled delay,
  main-thread prompt and callback. A transferred retry must not execute the
  prior attempt's finally cleanup. Final success transfers only session ownership.
- Register pending ownership before the delay. Background, explicit lock,
  destruction, cancelled prompt and stale delivery wipe buffers and delete only
  the disposable alias. Do not delete a committed alias or the original alias.
- Check epoch/active before scheduling, creating cipher, showing prompt,
  consuming callback and committing; stale callbacks cannot reopen the Cookbook.
- Cover rejected executor submission and failed Handler posting. No retained
  PreparedVault may become unreachable without cleanup; no UI-thread key deletion.
- Remove only this operation's delayed callback. Never discard unrelated owned
  cleanup callbacks. Serial worker and duplicate-callback guards prevent two
  doFinal/commit paths. Final success keeps existing retirement retry metadata.

## Required verification

- Meaningful bounded-policy regression: code15 first attempt retries once;
  second code15 stops; other codes, API<33 and post-doFinal commit failures do not
  retry. Verify wrappers/cause chains without relying on exception messages.
- Ownership regressions: cancel/background/destroy during delay, stale success,
  repeated callback and executor rejection wipe retained buffers, preserve old
  record/alias, remove disposable alias and never commit twice. These are control
  and ownership tests, not claims of simulated hardware authentication.
- Fresh-cipher/IV and prompt-count coverage must reject old cipher reuse.
  Preserve all existing golden tests and the39-test baseline; run impacted JVM,
  debug/instrumentation/release builds and lint before independent review.
- Parent-owned physical proof on the isolated synthetic demo: reproduce delayed
  owner prompt/code15, observe the new genuine prompt, authenticate promptly and
  verify same cache/salt with fresh wrapping and retired old alias. Cancel the
  retry and repeat delayed failure to prove bounded cleanup/no-mutation.
- Finish actual enrollment-invalidation/password-recovery acceptance separately.
  A formatter test, backend-only pass or code15 policy test cannot substitute for
  successful actual-controller recovery on the physical device.

## Original limits/questions — historical; current limits above

Wrap-only: no automatic unwrap retry in this repair. Root decides implementation
route after reviewing this plan; builder has not been authorized by this document.
Does a fresh operation with a second genuine prompt resolve the captured device
fault, and do real enrollment recovery plus bounded failure/cancel preserve data?
Earlier unclassified failures remain recorded; no repair success is claimed yet.
