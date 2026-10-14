# Security/UI physical continuation

Date2026-10-14 (Asia/Saigon); baseline main`173854d`. Mobile remains **Partial**.
Root exclusively coordinates Windows ADB/Gradle on Xiaomi14TPro/API36,
serialPN7PW8SGXKLVPRIV. Fresh device/screen gates precede each selective run.
Independent source/evidence reviewer ran no device/build commands.

## Actual selected results

| Scope | Verdict | Reached observation |
|---|---|---|
| Locked Security/UI Run01 | **Failed1/1**,1.732s | Language toggle raw layout rectangle failed48dp; fields/capture not reached |
| Capture Run01 | **Failed1/1**,1.584s | Same-node Android accessibility label predicate failed; screenshot not attempted |
| Authenticated Run01 | **Failed1/1**,12.104s | Genuine English Cook/reveal/visible Dish reached; UiAutomation bitmap null |
| Independent locked pixels Run01 | **Passed1/1**,2.321s | EN/VI selected setup labels/Password semantics and actual secure shell capture |
| Authenticated Run02 | **Passed1/1**,25.890s | Genuine EN/VI Cook/reveal and secure shell capture with visible Dish |
| Recents lifecycle Run01 | **Failed1/1**,206.052s | Actual post-checkpoint stop/resume not observed within180s; reopen not reached |
| Recents lifecycle Run02 | **Failed1/1**,182.457s | Genuine setup timed out before checkpoint; no ADB Recents navigation executed |
| Recents/OS shortcut visuals | **Pending** | Owner reported “can't take screenshot”; thumbnail and reveal timing not yet confirmed |

Failed runs have JUnit[1,-2]; native0/terminal[-1] does not override failure.
Passed runs have JUnit[1,0], anchoredOK1, terminal[-1], completed phase=true,
watchdog=false and0 failures/errors/skips. Status2 metadata is separate from JUnit.
Every failure and its exact candidate remain retained; no production repair claimed.

Recents01 reached genuine ready/revealed foreground, but actual pause/stop counters
and post-checkpoint deltas remained zero at timeout. Clear/session-absent bits emitted
after fixture cleanup do not certify a real Recents transition. Cause unconfirmed;
do not infer owner action/inaction. Screenshot availability and card contents require
separate actual owner observation. The original ten-second remask remains.

Recents02 retained the same49 frozen sources/APKs without rebuild/install. Reviewed
root orchestration would submit KEYCODE_APP_SWITCH187 at the owner checkpoint, hold
the Card for20s then return with serialized cancellation/join. Actual setup failed
before that checkpoint: navigation_events=[], no187 command sent. Safe pre-cleanup
flags show phone awake/unlocked, proof attached/shown but unfocused, vault busy and
not ready. Cause remains unknown; no owner blame, navigation or reopen acceptance.
No further runtime is active. Next attempt needs fresh genuine setup/owner readiness
and an actual own-Card observation; do not blind-retry or reset production deadlines.

## Accepted pixels and selected EN/VI scope

Locked pixels Run01 and Authenticated Run02 use fixed Android `screencap -p`
through the instrumentation's shell-command stream. No secure-layer option or
policy change. PNG/bitmap stays in RAM and is recycled; no pixels/tree are logged
or saved. Default UiAutomation capture remains available as its original route.
Nonnull bitmap, valid dimensions/own rectangle and every opaque-black pixel remain
required. Null is never accepted as capture-protection proof.

Both EN/VI captures in both accepted tests returned1220×2712 bitmaps. The inner
own-content region1194×2395 contains2,859,630 pixels; every pixel is opaque black.
Actual own resumed/awake/unlocked/focused secure foreground is checked before/after.
Locked fixture creates no encrypted record. Authenticated fixture requires genuine
password/strong-biometric setup, ready/exact synthetic Dish, explicit reveal and
actual visible Dish bounds inside the sampled region before capture, with
ready/revealed/displayed exact Dish checked after capture.

Language switching invalidated the old Dish/Copy; normal Vietnamese Cook and reveal
then passed. Encrypted UUID-owned jars remained byte-identical. No clipboard write
belongs to this continuation. This accepts selected EN/VI setup/manual
Cook screens and shell display exclusion on API36 foreground proof KEEP_SCREEN_ON.
OS screenshot shortcut, Recents, TalkBack, larger fonts/orientation, other screens
and ordinary screen-off UX need their own evidence.

## Retained layout/accessibility failures and diagnostics

Run01 measured raw `boundsInWindow`, without touch-expansion diagnostics. Later
Capture01/Pixels01 observed clickable language-toggle layout189×130px at density3.25
(40dp high), with public `touchBoundsInRoot`189×156px (48dp high), fully visible.
The raw rectangle assertion remains unchanged and Failed. Actual edge-pointer
reachability is unexecuted; no production undersized-touch-target defect is inferred.

Capture01's same-node text/hint/password/editable/visible predicate remained Failed.
Pixels01 requested the optional Compose semantics ID on real OS accessibility nodes:
English Master field matched exactly one visible/editable/password node; localized
label was found in its descendants, with direct text/hint/description matches false.
This narrows the earlier checker limitation. Vietnamese extra requests succeeded but
the field ID matched zero nodes; cause unconfirmed. Diagnostic bits do not constitute
Vietnamese OS accessibility/TalkBack acceptance. Both older methods/assertions remain.

Authenticated Run01 failed the original nonnull assertion while exact ready/revealed
Dish and sampled visibility were true. Why this device's UiAutomation returned null
is unconfirmed. Opt-in shell Run02 accepted actual pixels with that assertion intact;
the earlier null failure does not become Passed or establish a production defect.

## Frozen artifacts and preservation

Debug APK throughout:`fa4c4ab40339230a416c629d437ecd0200da099032fabad4de3c27d4eab2f9c7`.

| Runtime candidate | Test APK SHA256 | Frozen manifest |
|---|---|---|
| Locked Run01 | `31aa7248895dbe6be3cf67258755b87d7f1e3fd23d3de955cb2c737a11db3508` | security-ui-frozen-01.json;45 sources |
| Capture01/Auth01 | `7179a3810a66c4ef31838366bf7dec1232a56fcc6547502d68d251d344f5c3f4` | security-ui-frozen-02.json;46 sources |
| Pixels01/Auth02 | `e3142923964c83ce7d582ebb6f19ac99936a8030b986152e77f4304d461d1a77` | security-ui-frozen-03.json;48 sources |
| Recents01/02 | `ea84ea132b108ce02456e0085e2507468e3236a14524301ddb29f764cdca8494` | security-ui-frozen-04.json;49 sources |

Each test update used `install -r`; installed APK hashes matched. Exact prior runtime
sources/APKs/manifests/runner copies survive in candidate02/03/04/05 respectively.
Candidate01 preserves earlier compiled pre-runtime code; it has no device verdict.
Historical session-timer-frozen-03 and its binaries remain separate and unchanged.

Owner Cookbook digest before/after every install and run remains:
`894f73e51e316b6a369368ad4cebbc901609ccfe7eabe855aa73c2cbf4e7a4d6`.
Only digest read; no owner record operation. Cleanup only unique synthetic storage/keys.
Original frozen43/protected208/unrelated23 match. Production main/shared/canonical/
extension and existing tests unchanged; `.agents/` and `skills-lock.json` untouched.

## Local verification and evidence

Root-only incremental `:app:assembleDebugAndroidTest :app:lintDebug` builds01–05
Passed50s/44s/45s/48s/46s; source changes/review corrections justified each build.
Final05:11 executed/83 UP-TO-DATE; Recents-only build06 Passed42s,10 executed/84
UP-TO-DATE. Lint0 errors/11 warnings/1 hint. APK compilation
alone grants no device acceptance. Test-source deprecation compiler warnings are
separate from lint counts. Shared78/bridge16/standalone29/app4 and extension147/25
remain earlier retained evidence, not fresh executions or independent summed cases.
Previously accepted Google/cache/network/timers were not replayed.

Outside repo:`/tmp/passchef-cookbook-device-acceptance/`: `security-ui-runner.py`,
build01–06 logs, versioned frozen/install/preflight/candidate artifacts, locked-run01,
capture-run01, pixels-run01, authenticated-run01/02 and recents-run01 logs/JSON. Evidence names are
exclusive-create; old manifests are not rewritten when HEAD advances.
Recents02 log/JSON, `security-recents-02-orchestration.json` and copied runner/helper
in `security-recents-orchestration-02/` identify its actual external controls.
`security-ui-owner-screenshot-observation-01.json` separately preserves the direct
owner report; no exact toast/reveal timing or hidden-thumbnail conclusion inferred.

## Unresolved questions

Recents/OS shortcut owner observation and full physical UI/accessibility matrix;
Vietnamese OS field-ID mismatch; UiAutomation null cause. Original CookbookUIRun01,
network/timer setup/key-wrap causes stay open. Cold-start/process death and actual
Google cancellation/stale callback/401/403 remain Pending; no whole-mobile Done/archive.
