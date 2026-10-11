# Android Cookbook storage and unlock

Implementation status: source independently reviewed/tested; physical acceptance pending.
Accepted scope: original Android Keystore E2–E5, minimum Android8/API26.
The debug demo uses `dev.passchef.mobile.demo`, with a separate UID/private data
from the release application `dev.passchef.mobile`. Its proof file is distinct from
the demo Cookbook, and existing demo data must be preserved during device preflight.
This foundation has one demo screen; broader mobile screens, QR, Menu connection
and autofill remain their separate plans.

The Chef sets a master password and1–5 spice jars. The device derives a32-byte
master key using Argon2id v19,time3,64MiB,parallelism4 and a random16-byte salt.
That key encrypts the spice cache with AES256/GCM. A Class3 biometric-gated Android
Keystore key wraps the master key; every setup wrap and unlock unwrap requires
its own real CryptoObject authorization. No PIN/screen-lock credential silently
substitutes for strong biometric authorization. Devices without strong enrollment
retain password-only setup/unlock. StrongBox is preferred when available, while
actual hardware protection is inspected and reported rather than assumed.

Only a versioned bounded encrypted record persists in app-private, backup-excluded
storage. It contains derivation salt, cipher IVs/ciphertexts, current device alias
and bounded pending-retirement aliases. Atomic replacement preserves a complete
old/new record if interrupted. Up to5 jars each contain1–4096 UTF-8 bytes; master
password bytes are nonempty and at most4096. Unsupported/corrupt records are
rejected; they never trigger replacement setup.

On device-key invalidation, missing key, cancellation, lockout or unavailable
biometrics, the Chef can enter the original master password. Cache authentication
must succeed before any device alias changes; wrong passwords preserve both data
and existing alias. During actual Android biometric lockout (typed prompt7/9), a correct password
opens the authenticated cache session. An existing encrypted record and all its
alias/retirement metadata stay byte-for-byte unchanged; only the disposable new
wrap alias is removed. Fresh wrapping is deferred until biometrics become usable.
First setup may persist the encrypted password-only cache. Generic cancellation
and a code15 retry never take this lockout fallback. Correct biometric recovery
authorizes wrapping with a fresh alias, then
atomically commits the new complete record before retiring prior aliases. Failed
commit removes only the new disposable alias; failed retirement stays explicitly
retryable after a later successful cache authentication.

The unlocked session owns key/spice byte buffers in memory, closes on explicit
lock or background, and expires after5min inactivity or10min total. Cold starts
and Activity recreation begin locked. Old worker/prompt callbacks cannot reopen
sessions and wipe their owned stale results; rejected worker submissions also
close owned buffers. Input fields are masked, non-saveable and cleared on submit/
background. FLAG_SECURE protects the window; UI/logs never show key/spice values.
Managed immutable strings/provider internals do not provide guaranteed erasure.

The KMP boundary preserves genuine Android actual key management; JVM actual
throws unsupported rather than impersonating hardware. JVM tests prove real
cache crypto, parsing, transaction failure/cleanup and session policies. Compiled
Android APKs do not prove actual Keystore or biometric behavior.

The [physical runbook](../runbooks/android-keystore-device-proof.md) requires
owner-authorized hardware wrap/unwrap, actual enrollment invalidation/recovery,
lockout/no enrollment, process restart, lifecycle/timers and logcat checks. The
[recovery report](../history/passchef-mobile-android-keystore/reports/keystore-recovery.md)
records observed outcomes. USB/install and actual hardware wrap/unwrap passed on Xiaomi14TPro/API36.
StrongBox level2 was inspected. Real enrollment invalidated the prior key;
original-password recovery on `d2a76ca` then preserved the cache while committing
a fresh alias/IV, followed by genuine new-key biometric unlock. A held prompt
produced typed wrapping code15 before recovery succeeded. The owner saw one
visible prompt; the fresh retry CryptoObject callback is inferred from reviewed
control flow and outcome, rather than two separately observed dialogs.

The bounded repair permits one cancellable five-second API33+ wrap-only retry
before commit, with a fresh Cipher and genuine strong-biometric authorization.
Cancellation wipes retained owned data and never deletes a committed alias.
Biometric/password-wrap cancellation, wrong-password preservation, pending-wrap
background locking and actual new-process restart also passed. A later review
found the lockout password-loop defect; its typed fallback repair is implemented
and source-reviewed. Builder and fresh independent56 JVM tests, debug/test/release/lint passed.
Candidate `0a99d63` is installed with exact Cookbook bytes preserved; genuine
biometric opening authenticated the same1 jar. A later held unlock failed with
"Device key unavailable"; its numeric cause was not recorded. No physical5/10min
pass is recorded. Actual lockout fallback, timers and native screen protections
remain pending; worktree stays open/unmerged.
