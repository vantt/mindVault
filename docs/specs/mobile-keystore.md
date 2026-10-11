# Android Cookbook storage and unlock

Implementation status: source independently reviewed/tested; current-Xiaomi physical scope accepted on 2026-10-11.
Android8/API26 remains the supported minimum; unexecuted physical matrix is explicitly tracked.
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
Candidate `0a99d63` preserved exact Cookbook bytes and genuinely authenticated
the same1 jar; debug-only unwrap diagnostic `0d38e62` is now installed and pinned.
Its independent app builds/lint and five actual Android formatter tests passed;
release remains silent, with authentication/storage/session behavior unchanged.
Before diagnostic installation, a held unlock on `0a99d63` failed with
"Device key unavailable"; its numeric cause was not recorded. After Xiaomi reboot,
a fresh real biometric session on `0a99d63` passed idle5min: last observed open292.74s, first
observed locked305.95s, with no post-setup touch. Bounded own-app log checks found
none of six known synthetic literals; foreground/awake/FLAG_SECURE were sampled.
A fresh total10min probe then locked309.84s after four requested ADB header taps;
their delivery was unverified. The next owner-touch probe timed out before unlock.
Demo was safely restarted cold/locked; encrypted record stayed exact and temporary
keep-awake was restored. A later current-candidate total10min probe passed:
last observed open597.21s/first locked610.30s, with owner-confirmed manual title
touches through lock. Real OS lockout followed five rejected scans; original
password opened the authenticated cache through the biometric-unavailable path,
preserving the complete encrypted record. Typed callback7/9 was not delivered.
Owner reported blocked screenshot/only PassChef black; the precise Recents/video
category was not independently established. Ordinary app return was locked;
final bounded own-app log check found none of six known synthetic literals.
The owner accepted this current-device scope for merge/push and retained
[unexecuted physical checks](../../plans/261011-keystore-recovery/reports/current-device-acceptance.md):
API26/no hardware/no enrollment/StrongBox absent, typed7/9, exact kill/retry
windows and precise Recents observation. Those limits do not claim matrix passes.
