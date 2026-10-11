# Android Keystore recovery — 2026-10-11

Scope: accepted original E2–E5 plus preserved E1; Core integrated at3532c160.
Status: genuine source implemented, frozen and independently reviewed/tested.
Physical device and owner biometric/enrollment acceptance remain BLOCKED.
Debug identity isolation `.demo` implemented after independent clean39-test pass;
release identity stays unchanged. Final impacted app/testAPK/lint builder check
passed in38s; APK metadata confirms `.demo` and `.demo.test` packages. Independent
post-isolation app/testAPK/lint pass completed in14s; no shared source change in this delta.
No Bee state, original tooling WIP, golden tests, commit or merge changed by builder.

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

Physical instrumentation has not run. Freeze includes a real executor-shutdown
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
Native Windows adb36.0.2 `devices -l` on2026-10-11 returned an empty list. Historical
WSL TCP bridge attempts are obsolete for this recovery; direct Windows adb from
WSL plus `wslpath -w` APK paths is the chosen safe route. Root's latest Windows
PnP observation sees Xiaomi14TPro only as WPD transfer-files device, USB
VID2717/PIDFF40, with no ADB interface after two owner USB-debug toggle/replug
attempts. A wireless-pairing request is pending; that transport has not been
adopted or verified yet.

[Device runbook](../../../runbooks/android-keystore-device-proof.md) records exact
build/install/instrumentation commands and owner manual acceptance. Build/JVM/lint
success does not close E2–E5 physical acceptance. Next: owner-assisted hardware proof;
source may be committed as a reviewable candidate, while this worktree stays open
and unmerged until accepted physical proof. No merge-ready claim yet.

Outstanding: real authorized physical serial; two biometric prompts; induced
invalidation/recovery, lockout/no enrollment, lifecycle/process/timers and logcat.
