# Owner TalkBack checkpoint review —4November2026

Status: **Selected physical owner/native checks Passed; Mobile Partial**.
Independent review of two additive Android test files and their reused proof paths.
No production/test implementation edits, Gradle or device calls by reviewer.

## Initial candidate12 findings —resolved by source correction

- **P2 — own cleanup must cover checkpoint construction and retain primary failure.**
  Both test methods instantiate `TalkBackOwnerCheckpoint` before their cleanup try;
  invalid/missing component or constructor guard failure can leave a new empty UUID
  directory. Locked and authenticated-preflight finally blocks can also replace the
  actual failure with close/delete errors. Put construction inside one owned cleanup
  scope; independently close and remove exact UUID, retaining/suppressing failures.
- **Authenticated cleanup must verify its own final result.** The reused timer
  fixture's legacy cleanup swallows alias errors and ignores directory-deletion
  return. Add outer checked cleanup for this new test, capturing only its owned
  aliases during authenticated work before legacy finally can remove the record.
  Re-delete those same aliases independently, then check exact UUID removal; retain
  original failure. Do not alter historical fixture/assertion behavior.

## Checked source boundaries

- The helper acquires UiAutomation with DONT_SUPPRESS_ACCESSIBILITY_SERVICES and
  verifies the exact supplied enabled spoken-service component, rather than calling
  service-toggle or authentication bypass APIs. The host must bind the supplied
  component to the genuine installed owner-enabled TalkBack service.
- Secure RESUMED own window, awake/unlocked/focused state and continuing locked or
  authenticated-masked eligibility are guarded throughout each real owner wait.
  Authenticated setup reuses real password/biometric cooking; jars remain exact,
  Dish stays masked, reveal/copy remain available but are not activated by the test.
  Production clocks/window/session policy and existing proof KEEP_SCREEN_ON remain.
- Each bounded120s owner checkpoint emits safe UUID/stage/service/layout metadata.
  Its fresh canonical private marker is bounded256bytes and accepts only exact
  Boolean completion, UUID and checkpoint fields. A stale/wrong-stage signal cannot
  complete another wait. No Dish/Spice/field text, audio or screenshot is captured.
- Completion means only that an owner observation occurred. Speech quality, actual
  focus order, usable labels and masked-Dish privacy require a separately preserved
  genuine owner verdict; native JUnit alone cannot certify them. Explicit decline
  becomes an assumption/skip after cleanup; unavailable service cannot earn credit.
- Touch exploration is reported, not required in candidate12. A claim about actual
  touch/gesture traversal needs observed exploration-enabled state plus the owner's
  account of that traversal. Source predicates/service identity alone prove none.
- Instrumentation-side Compose actions/label assertions do not establish physical
  TalkBack speech. The external runner must wait for an exact completed owner-ready
  bundle and current native liveness, poll immediately on owner replies and write
  only the current UUID/stage signal after the genuine response. Do not send owner
  guidance or marker writes after native terminal. Host review is still pending.

## Frozen candidate12 audit

`2026-11-04-talkback/reviewer-talkback-source12.json` independently confirms all182
archived source entries match the frozen manifest and all three candidate APK bytes
match. New checkpoint/test files are115/89 lines respectively. Debug retains
`707bb621…fc8b1`, release`039e2420…641d`; test is
`eb9a7c9bdc4fb171e422d14dc05fa1b7cb10418775aa96af6587e57e781e6c3f`.
Build12 records Passed2m1s,159 tasks11 executed/148 up-to-date. Candidate12 was not
installed or run; it cannot substitute for a corrected source/build/freeze.

## Corrected source/candidate13 re-review —Passed

Checkpoint construction and preflight now occur inside one exact-owned cleanup
scope in both methods. Primary failures are retained; scenario close, record read,
each owned alias deletion and checked directory removal/absence run independently.
Authenticated work captures its aliases before the reused legacy fixture can
remove the record. Existing fixture assertions/cleanup behavior remain unchanged.
Actual touch exploration is now required at preflight and throughout owner waits.
No marker/service/secure-window/masked-state assertion was weakened.

Independent `reviewer-talkback-source13.json` confirms current182 frozen paths,
all182 source ZIP entries and all candidate13 APK bytes exact. Build13 after these
edits records Passed32s,159 tasks11 executed/148 up-to-date. Debug/release remain
unchanged; test SHA256 is
`fcd175934b628e2a9cb84bab9a1406a740a27b1c32c04eee6591a3c806be0f86`.
No remaining source/candidate blocker found. External host and actual owner/native
evidence must still be reviewed before any TalkBack acceptance.

## External host review —Passed for root-controlled owner protocol

`reviewer-talkback-host01.json` verifies complete host-freeze01 and independent
16/16 pure protocol replay. The tester's separate32/32 probes remain local protocol
checks, not physical speech observations. Recorded installed app/test digests match
candidate13 and original owner inventory was preserved before native selection.

- Host fixes physical serial and genuine TalkBack component, checks current device/
  Awake/keyguard, inactive instrumentation/absent target PID, owner/all-file hashes,
  six historical directory names and actual installed APK bytes. No wake/unlock,
  service toggle or system-setting write occurs in this runner.
- Owner-ready requires exact completed status2 metadata, one UUID, allowed current
  checkpoint, expected service and exploration true plus a live ADB child. Host only
  exports a ready record/hash and marker template; it never writes completion.
- Root must inspect current raw terminal and native liveness immediately before
  guidance/marker, and use an actual reply to the current ready stage. A live child
  alone is not exact native-wait proof; initial service-availability confirmation
  is not an EN/VI or masked-Dish spoken/focus verdict.
- Verdicts bind UUID/checkpoint/ready-record SHA, bounded actual owner text,
  root-recorded flag, runtime timestamp and separate speech/focus outcomes. Native
  eligibility plus preservation plus every completed actual-owner Passed verdict
  is required for overall Passed. Missing/uncertain owner evidence stays Unknown;
  explicit failure fails; clean unavailable/declined stages stay NotExercised.
- Exact native selector/start/end/terminal/summary and ordered TalkBack phases are
  required. Timeout/host observation failure leaves possible native liveness and
  Unknown, with no automatic retry/cleanup. Post-terminal actual inactivity, exact
  UUID absence, complete original file/name equality and unchanged plugged-awake
  policy are independently audited before acceptance.

No source/host blocker remains for the selected manually coordinated run; only
actual current-stage owner/native/protection evidence can close its bounded scope.

## Physical locked Run01 —Passed136.423s

Independent `run01/reviewer-locked-runtime01.json` verifies strict selected
`[1,0]/[-1]/OK1`,136.423s native/139.038s host, no timeout and final native inactive.
Raw SHA256`d5c85f6e86d02cb3391ddd7c71a155cf3f2c2f5fe14cb5df1f11fc95034cd64b`.
One UUID`b51823b4-0cc0-45d3-8b83-eeeb5c543eff` binds all six ordered native phases.

Exact expected spoken service and touch exploration stay true, font1.1/portrait
throughout. Each separate owner verdict matches its ready-record SHA/UUID/stage,
is recorded after that stage's actual ready observation and within120s. Actual
owner replies report clear “Master password” and “Mật khẩu chính” speech plus next
focus movement in EN and VI respectively. Native semantics/password/secure-window
eligibility and those genuine owner reports jointly accept these selected locked
field-label/focus observations; JUnit alone did not certify speech.

Candidate13 installed identities,182-entry source archive and host freeze remain
exact. Original owner digest/all regular files/six old directory names and plugged-
awake policy are preserved; new UUID is absent after verified cleanup. No other
TalkBack field, full focus order, whole-screen layout or device matrix is inferred.

## Physical authenticated masked Run02 —Passed57.629s

Independent `run02/reviewer-masked-runtime02.json` verifies strict selected
`[1,0]/[-1]/OK1`,57.629s native/59.152s host, no timeout and final native inactive.
Raw SHA256`bf669390faf97bce83b972cab7eadb48a0581aafef1ac1a3636688f089c8a350`.
The intermediate native-result snapshot precedes host inactivity/overall aggregate;
the terminal `final.json` is the completed result.

Actual genuine biometric timer fixture reaches ready masked Dish, then UUID
`4259c27a-bd9a-4139-9953-c589b88db9de` stays in eligible secure foreground while
the owner observes it. Font1.1/portrait, expected spoken service and exploration
remain true. Native guards require unique actual mask, ready authenticated session,
no revealed/nonempty Dish and exact encrypted jars throughout the wait.

The current-stage owner reply is “Nhãn rõ, focus tiếp được, Dish thật không bị đọc”,
bound to the masked checkpoint's actual ready SHA/UUID and recorded within its120s
window. This accepts selected Reveal/Copy action-label clarity, next-focus movement
and owner-reported absence of real Dish speech for this masked state. Native code
does not independently hear audio or certify every focus target. New UUID is gone;
owner/all original files/names and policy remain exact. Existing proof KEEP_SCREEN_ON
remains part of both selected physical cases.

## Optional physical200%/landscape wrapper —corrected source review Passed

No execution/settings change occurred during review. The proposed wrapper records
original1.1/rotation0/accelerometer0, requires actual readiness, uses the same locked
candidate/host and requires two actual2000/landscape owner-ready configurations.
The corrected73-line source now marks launch before the child call; missing or
unreadable final metadata and inactivity-probe errors retain Unknown/recovery state.
Restoration requires explicit nativeMayStillLive=false plus actual inactivity;
each original setting is restored and read back. Restoration errors preserve the
primary runtime failure and leave recovery metadata; unresolved liveness requires
root inspection. Independent source/syntax result and exact wrapper SHA256 are
frozen in `reviewer-large-landscape-wrapper03.json`:
`d1401d557c2de5945598b987e8c32d34402dd7b7078fbfed5e94f097403a9920`.
No source-review blocker remains; actual optional owner authorization is pending.

## Separate candidate26 large-font packet — preparation Passed

New `2026-11-04-talkback-large26` sibling contains7 byte-identical earlier reviewed
helpers, including unchanged wrapper03 SHA above and historical README. Current
README explicitly identifies that historical13 reference and candidate26 entry.
Independent15-entry freeze check and pure source194/archive/3APK/candidate26 plus
recovery-host binding all pass. Preparation SHA256
`da541d5947571991e0429535aff4d72420ad54d27cf6e8f3766b121f784ead75`;
host freeze SHA256
`5bf66e8416b549871a4bb08f8bee64a4df6fac416dbc1627e8e608c2301c46bd`.

New entry only validates local provenance before/after and explicit owner font
approval, then invokes unchanged wrapper03. Google grant approval and default-null
template are ineligible. Separate bounded1h approval must specify actual physical
serial, font2.0/landscape, locked EN/VI TalkBack only, restore-original and exact
preparation SHA. Independent13 approval-gate plus32 unchanged protocol probes all
Passed. Pure protocol replay avoided the script's immutable output write.

Wrapper still refuses unexpected initial settings, requires actual Awake/unlocked/
native inactivity, requests temporary200%/landscape, and accepts configuration only
when both own Activity checkpoints show font2000/orientation2/width>height. Actual
owner speech/focus verdicts remain separately live-ready-SHA/UUID bound; native
JUnit cannot supply them. Settings are restored/read back only after known terminal
native and actual inactivity; Unknown retains pending recovery record. No new
settings/native implementation, auto marker or accessibility verdict is added.

Evidence: external `reviewer-talkback-large26-preparation.json`. No reviewer device,
settings, native or Gradle calls. No preparation-review blocker before optional
concrete owner approval; no200%/landscape runtime acceptance yet.

## 6November candidate27 large-font sibling — preparation Passed

Separate `2026-11-06-talkback-large27` preserves old26's15 frozen entries unchanged.
Seven runtime/helpers including wrapper03 remain byte-identical. New pinned195/
candidate27 verifier equals the independently reviewed physical L1 verifier: one
new grid test, old194 unchanged, production26 APKs unchanged, new test APK pinned.
Actual pure local195/archive/3APK checks pass; all17 freeze entries exact.
Independent32 protocol +13 font approval probes all45 Passed.

New preparation SHA256
`d04bada4b86ea4fe9b8ffe99de9a1ea07fc918f506eb11b0d2a9d85178216cfd`;
host freeze SHA256
`46f91e132534b57833c03cd7ce93ac527f2bd99f630491d4fc68a893753fcb42`.
The pre-freeze manifest-copy mismatch was caught locally; prior snapshot/probe
remains retained without device acceptance. Evidence: external
`reviewer-talkback-large27-preparation.json`. No reviewer device/Gradle/Git calls.

Owner's4November font deferral remains in force; resuming the overall goal or
Google approval does not authorize temporary physical display settings. Default
template is null/ineligible. Renewed explicit phone/font2.0/landscape/locked ENVI/
restore-original approval must bind new preparation SHA and bounded validity.
Runtime wrapper/owner speech/focus/configuration and terminal-only settings
restoration rules remain unchanged. No settings/native action accepted yet.

## Renewed approval and observed-original-settings wrapper04

Owner explicitly renewed scope: “Có, làm cả Google và 200%/xoay ngang”, then reported
TalkBack enabled/at Home. Actual font approval binds preparation27, phone, font2.0/
landscape, locked ENVI and restore-original with bounded validity. This replaces
the earlier deferral only for this scoped case; no broader accessibility claim.

Old wrapper03 refused actual accelerometer_rotation1 against its dated0 baseline,
before any settings write/native dispatch. Original outer Unknown remains retained.
Appended `old-preparation-resolution03.json` records source-based NotStarted and
actual inactive/no run03 directory/settings still1.1/rotation0/auto1. This is not a
native test failure or a fabricated liveness rewrite.

New standalone `run-observed-display27.py` captures actual original1.1/rotation0/
auto0-or1, reuses approved/current195 provenance and unchanged locked runner run04.
It marks settings change before writes and possible native liveness before child
dispatch, requires both own2000/landscape/wide configurations plus actual owner
aggregate. Restoration requires explicit final nativeMayStillLive=false and actual
inactivity, then exact readback of all observed original keys including auto1.
Owner/source/installed/policy/approval/self-source checks follow. Missing/unreadable
final/Unknown keeps restoration pending; restoration command error cannot pass and
retains original settings in final metadata for root recovery.

Independent source/approval/provenance review Passed; four isolated mock cases pass:
stable exact auto1 restoration, Unknown, missing final and restoration error. These
are simulations with zero device calls. Wrapper SHA256
`49839ceabe6994596d2435f020cca754bf5d17acb37506c7e944a51997bd7237`;
evidence external `reviewer-observed-display04.json`. No old frozen helper changed.
Actual native/settings/owner-spoken acceptance still requires separate audit.

Unresolved: actual scoped run04 result/configurations/owner speech-focus/precise
settings restoration and owner preservation evidence; broader plan gates separate.
