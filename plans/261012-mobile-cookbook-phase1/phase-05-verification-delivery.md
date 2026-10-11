# Phase05 — Independent verification, honest acceptance and delivery

Status: **Partial — local verification/delivery Done; owner acceptance Pending**. Priority:5.
Owner/device acceptance below remains Pending.

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
  columns. Overall mobile roadmap Phase1 remains Partial while live/UI proof is absent.
- [x] Root handles authorized scoped commit/integration/push after independent review;
  archive only a scope with no relevant remaining deliverable. Owner authorized commit/push.

## Owner/device acceptance — all Pending

[Candidate and Android Google checklist](../../docs/runbooks/android-cookbook-acceptance.md).

1. Owner configures Android OAuth package/signing and grants actual Sheets access;
   select dedicated test Menu/tab, confirm mapping and observe real API/cached results.
2. Install isolated debug candidate with data-preservation preflight; run actual
   Compose list/search/paste/tag/reveal/copy/ENVI/layout tests and record outcomes.
3. Real biometric/password recovery, background/recreation/cold-start locking,
   genuine5-minute idle/10-minute total and actual30-second clipboard behavior.
4. Verify replaced clipboard text, inaccessible background read and OS restrictions
   honestly; observe screenshot/Recents/capture and sanitized own-app logs.
5. Existing unexecuted Keystore device matrix remains separately Pending; a passing
   new Xiaomi screen flow does not certify API26/no hardware/no enrollment cases.

## Success and remaining dependencies

Code-complete means real offline/connection code + independent JVM/build/lint proof,
with owner-dependent acceptance explicitly Pending. Product acceptance additionally
requires the relevant real phone/OAuth evidence. QR camera/receiver, Autofill,
Drive/multi-profile and account authoring remain later plans, not overnight promises.
