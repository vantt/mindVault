# Phase05 — Independent verification, honest acceptance and delivery

Status: **Partial — local verification/delivery Done; owner acceptance Pending**. Priority:5.
Owner/device acceptance below remains Pending.
Current [real session timer continuation](reports/session-timer-runtime.md): three
selective test-APK/lint builds Passed; idle Run01/02 pointer failures and Run03 setup
failure retained; Run04 setup Failed. Idle Run05 Passed1/1,341.722s; Total04 on14October
Passed1/1,609.969s, runner[1,0]/OK1,0 failures/errors/skips. Original ten-minute deadline
locked despite minute1–9 real touches/Cooks; Dish/session/Copy cleared and jars/digest
preserved. Same frozen03/APKs, no rebuild/install or fresh JVM. Selected5/10-minute
API36 foreground KEEP_SCREEN_ON accepted; all four idle/three total failures retained.
Selected [cache UI Run02](reports/google-cache-ui-runtime.md) Passed1/1,42.393s;
broader device/provider matrix remains Pending, whole phase Partial.
Latest [network-off Run04](reports/network-off-runtime.md) Passed1/1,31.477s:
actual offline UI/Cook/recreation and NETWORK_FAILED preservation;401/403/cancel,
process death/cold start/broader timer UX/UI and Run03 key-wrap cause remain Pending.
[Security/UI continuation](reports/security-ui-runtime.md): locked pixels01
Passed1/1,2.321s and authenticated02 Passed1/1,25.890s. Selected EN/VI setup/manual
Cook and actual secure shell display exclusion accepted on API36 foreground proof;
Locked01/Capture01/Auth01 retained Failed; Recents01 Failed206.052s without actual
post-checkpoint stop/resume, Recents02 Failed182.457s at setup before navigation.
Run03 Passed1/1,137.789s with same-Activity pause/stop/locked read and genuine reopening;
owner confirmed own card blank/covered. Same frozen04/APKs, no rebuild/install between
Recents attempts; earlier causes unconfirmed.
Six scoped test-APK/lint builds Passed; final06:42s,10 executed/84 UP-TO-DATE,
no production-source changes or fresh JVM/browser assertions. OS screenshot shortcut/
full UI/accessibility remain Pending. [Offline cold-process](reports/cold-process-runtime.md)
warm02 InterruptedByVerifiedSIGKILL and cold02 Passed1/1,5.993s,PID25682→25991:
initial locked/cleared state, genuine biometric reload/exact cache/Cook/jars preserved.
Build01/02 import/type failures retained; corrected build03 Passed54s13/146,
lint0/11/1 and release proof exclusion. Five new files and one intentional debug
manifest entry only; historical49 means48 unchanged+entry delta, protected208/23 intact.
Provider [cold02](reports/google-cold-runtime.md) Passed1/1,8.886s after verified
warm02 SIGKILL/PID13092→14408; real fingerprint/exact cache/Cook/jars/owned cleanup
preserved. Cold01 Failed181.588s reopening/cause unknown; diagnostic-only build03
Passed58s10/101, old54/17 assertions preserved, no production repair claim.
[VI OS-field01](reports/vi-accessibility-runtime.md) Passed1/1,1.5s, one own password
node with label descendants; prior same-node assertion remains Failed. Source59/
testb684/build55s9/102/lint0/11/1; no auth/capture/Google replay. TalkBack/LMK/task
restore/provider errors/full matrix Pending; whole mobile remains Partial.
[Language touch](reports/language-touch-runtime.md): center02 Passed1.852s, edge01/02
Failed1.408s/1.211s, no whole48dp target credit; cause unknown. Dwell04 built/reviewed
43s9/102/lint0/11/1/source60/testce61; USB initially empty14October. Reconnected15October:
exact candidate installed-r, dwell04 Failed1.709s/center04 Passed1.802s. Earlier
source/APK/failures retained; old59 unchanged. No production change/clock/input bypass.
Later geometry01 Passed1.505s, observed scroll coverage0. Paired pointer-kind finger01
Passed2.464s/source63/helper10/test9a64: UNKNOWN retained localized UI/FINGER switched
EN→VI→EN at equal guarded expanded midpoints/configured60ms, baseline constructor
Unknown/readback checked. New samples accepted; old failures/full-target limits retained.
[Manual shortcut](reports/manual-shortcut-runtime.md) Run03 Failed182.485s setup/
current wrap15; same-source/APK04 reached genuine secure masked Cook then Failed
236.235s owner-response completion wait. No observed reveal/stop/signal; hardware
shortcut Pending. Source64/helper12/test19b9/owner/exact UUID cleanup preserved.
Late04 missing-Reveal reply retained after terminal. Same-candidate05 genuine ready,
Failed188.013s completion wait/no sampled reveal/stop. Current screen requested before
further timed runtime; no production screenshot/owner-action cause inferred.

## Context and requirements

[Overview](plan.md), [Core](../../docs/specs/mobile-core.md),
[device runbook](../../docs/runbooks/android-keystore-device-proof.md).
Prior Core/Keystore acceptance proves the foundation, not new Cookbook UI/OAuth.
Preserve original Golden/Unicode/vector files and unrelated Cave/skills-lock WIP.
Build/test source must be frozen before independent review and final validation.

## Autonomous local commands

From repo root, current installed JDK17 and SDK36:

```sh
export JAVA_HOME=/home/vantt/dev-tools/jdk-17.0.20+8
export ANDROID_HOME=/home/vantt/dev-tools/android-sdk
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew --no-daemon :shared:jvmTest :shared:assemble \
  :sheets-android:jvmTest :sheets-android:assemble :app:testDebugUnitTest \
  :app:assembleDebug :app:assembleDebugAndroidTest :app:assembleRelease :app:lintDebug
```

Use `--rerun-tasks` for the final independent run when fresh assertions are needed;
count XML tests/failures/errors/skips rather than treating UP-TO-DATE as new proof.
The root bridge has Android/JVM targets and reuses canonical portable client tests.
App unit tests exercise the actual source coordinator with deferred IO and real files.

Preserve standalone Menu client, using its existing local JDK21:

```sh
export JAVA_HOME=/home/vantt/projects/passMan/.toolchain/jdk
export PATH="$JAVA_HOME/bin:$PATH"
cd mobile/sheets-client
./gradlew --no-daemon build test
```

No live smoke command belongs to unattended tests. No emulator executable/runtime
is established. `assembleDebugAndroidTest` compiles tests; it does not run them.

## Implementation / verification TODO

- [x] Capture baseline source/Golden hashes in [baseline](reports/baseline-hashes.json);
  protect existing phone data, `.agents/` and `skills-lock.json` from scoped staging.
- [x] Author meaningful pure cache/mapping/search/session/reveal/clipboard policies
  and HTTP/auth callback regressions as described in phases01–04; actual engine/UI
  wiring must exist rather than shipping fixture-only replacements.
- [x] Independently review key ownership, source/Menu binding, atomic cache update,
  token lifecycle, stale results, permissions, masking/copy and app foreground rules.
- [x] Freeze source, run relevant local commands once final, report actual XML counts,
  lint result/warnings, debug/test/release IDs, APK hashes and unchanged Golden bytes.
- [x] Inspect release manifest/no backup/FLAG_SECURE and absence of debug proof/logging;
  no camera/Autofill/Drive permissions introduced for this bounded read/cook slice.
- [x] Record compiled Android test matrix separately from tests actually executed;
  keep no-device status visible in `reports/verification.md`.
- [x] Update product specs/roadmap and independent report with passed/failed/Pending
  columns. Overall mobile roadmap Phase1 remains Partial while the broader live/UI matrix is Pending.
- [x] Root handles authorized scoped commit/integration/push after independent review;
  archive only a scope with no relevant remaining deliverable. Owner authorized commit/push.

## Owner/device acceptance — Partial

[Candidate and Android Google checklist](../../docs/runbooks/android-cookbook-acceptance.md).
Installation/digest checks and8 selected platform tests passed. Actual owner biometric
UI Run01 failed after reveal; Run02 failed after owner-confirmed interruption,
Run03 failed before setup; Run04 passed selected unchanged foreground flow. Run01
cause remains not reproduced; normal-policy Run05 passed. GoogleRun03 and cached
UI/Cook/authenticated same-process recreation Run02 Passed; wider acceptance Pending. The
[unattended continuation](reports/unattended-regression.md) adds shared78/app4 and
compiled diagnostics; it does not resolve the physical failure.

1. Owner configures Android OAuth package/signing and grants actual Sheets access;
   select dedicated test Menu/tab, confirm mapping and observe real API/cached results.
2. Install isolated debug candidate with data-preservation preflight; run actual
   Compose list/search/paste/tag/reveal/copy/ENVI/layout tests and record outcomes.
3. Broader biometric/password recovery/background/recreation/cold-start matrix;
   selected API36 foreground5/10-minute timers and clipboard ownership accepted.
4. Verify replaced clipboard text, inaccessible background read and OS restrictions
   honestly; selected secure shell capture/Recents accepted, OS screenshot shortcut
   and broader sanitized own-app/accessibility observations remain Pending.
5. Existing unexecuted Keystore device matrix remains separately Pending; a passing
   new Xiaomi screen flow does not certify API26/no hardware/no enrollment cases.

Historical cache continuation build88s Passed fresh app4; shared78/bridge16/standalone29 retained.
Helper-only sync build40s Passed, lint0/11/1. Default storage recreation Passed1/1,
31.769s; cache UI Run01 Failed1/1,294.651s remains retained, corrected Run02 Passed1/1,
42.393s. Owner digest preserved. No production fix, actual network-off/process-death/
cold-start/cancellation/401/403/timer/UI matrix claim; no new commit claimed here.
Network continuation later build55s and diagnostic81s Passed; app4/shared78/bridge16/
standalone29 retained (no fresh JVM reruns). Network Run01/02/03 Failed history
retained; identical Run02/03 source/APKs Run04 Passed1/1,31.477s with strict actual
network absence and preserved cache/jars. No build/install repeated for Run03/04.

## Success and remaining dependencies

Code-complete means real offline/connection code + independent JVM/build/lint proof,
with owner-dependent acceptance explicitly Pending. Product acceptance additionally
requires the relevant real phone/OAuth evidence. QR camera/receiver, Autofill,
Drive/multi-profile and account authoring remain later plans, not overnight promises.
