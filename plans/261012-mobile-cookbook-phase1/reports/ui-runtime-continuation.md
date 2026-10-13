# Owner-ready Cookbook runtime continuation

Date2026-10-12 (Asia/Saigon). Main13cf9d1; mobile **Partial**. Production source,
assertions, biometric policy and waits unchanged. Root alone owns Windows ADB;
two agents independently interpreted sanitized logs, neither ran ADB/Gradle.

Owner confirmed ready for Run02. Native Windows ADB re-listed actual Xiaomi/API36
serialPN7PW8SGXKLVPRIV. Both reviewed APKs installed using data-preserving `install -r`;
actual installed hashes match the new local pair in [unattended preparation](unattended-regression.md):

- Debug:d4eb8401d0b2cc71f9ca800614dc7f7d8cf391d3e3432cd6a7537ce91382f9a4.
- Test:91da0f8cbfd83487d9e192aa24117decdd410a0be4372788a53b9d02363a3d95.

Owner encrypted Cookbook digest before/after install, Run02 and later checks:
894f73e51e316b6a369368ad4cebbc901609ccfe7eabe855aa73c2cbf4e7a4d6.
Only the digest was read; no uninstall/data clear or owner-record modification.

## Actual results

| Check | Result | Boundary |
|---|---|---|
| Run04 tested pair install/digest | **Passed** | Both installed hashes measured; original record preserved; newer comparison APK installation Pending |
| Locked diagnostic runtime | **Passed**1/1;2.314s | Real isolated Activity start/stop/resume; exact11 boolean states; no setup/authentication |
| UI Run02 | **Failed**1/1;59.481s | Owner authenticated; foreground interrupted before masked assertion |
| UI Run03 | **Failed**1/1;2.827s | Initial language query failed; no setup/authentication/reveal reached |
| UI Run04 | **Passed**1/1;62.575s | After owner unlock, immediate Awake/keyguard=false check; unchanged exact foreground flow |
| Comparison candidate install/digest | **Passed** | Reconnected USB; test086e7022.../debugd4eb8401... verified, original record unchanged |
| Locked diagnostic02 | **Passed**1/1;1.943s | Updated lifecycle/power/window snapshots on actual isolated Activity |
| UI Run05 | **Passed**1/1;53.663s | Normal proof-window screen policy, KEEP_SCREEN_ON=false; exact foreground flow |
| Google | **Pending** | Owner confirms configuration not complete; no consent/read |

## Run02 — evidenced foreground interruption

Before reveal delay: t89841009ms, lifecycleRESUMED, read-success=true; vault active/
unlocked/ready and retained Dish/panel present. At t89848885ms, Activity paused/stopped
(+7.876s from the pre-delay snapshot). After delay/read overhead: t89865657ms,
lifecycleCREATED, pause1/stop1/destroy0; vault active/unlocked=false and Dish invalidated.
Masked assertion failed with `No compose hierarchies found in the app`.

Owner explicitly confirmed switching/locking/pressing navigation after fingerprint
in **Run02**. Own-Activity event evidence records `userLeaving=true,pauseBackTasks`.
This matches `MainActivity.onStop` locking/clearing policy. `vaultClockExpired=true`
after that lock does not prove a5/10-minute deadline triggered. No timer or policy
repair is justified by this interrupted run. Both agents agree on this interpretation.
The owner admission applies only to Run02; **Run01 cause remains unknown**.

## Run03 and guarded next attempt

Run03 was launched before a fresh screen-readiness check. Initial EN-node inspection
failed without Compose hierarchy. Later read-only state was Dozing/keyguard showing;
own-Activity events show the newly launched proof Activity paused/stopped with `sleep`.
It reached none of the authenticated Cook/reveal steps. This is a separate startup
failure, not a passed retry or evidence of the original reveal defect.

Root corrected orchestration: immediately require read-only `Awake` plus keyguard
`showing=false` before invoking the single selected UI class. The next120s readiness
wait never satisfied both; **Run04 initially was not invoked**, no authentication deadline started.
No screen wake/keyguard dismissal/network ADB or production lock relaxation occurred.

Owner later explicitly reported unlocked. Root immediately verified Awake/keyguard=false
and unchanged Cookbook digest, then ran **Run04: OK (1 test),62.575s**. Before/after
reveal snapshots are10.653s apart; both RESUMED, no pause/stop/destroy, session ready.
Afterward reveal text is cleared while Dish/Copy nodes and retained Dish remain present.
All exact downstream assertions reached terminal pass; independent reviewer agrees.

Accepted scope: real setup/unlock with synthetic jars, exact Cook/reveal/remask,
edit/invalid/no-Menu tagged refusal, offline confirmed mapping/search/detail, tagged
masked-edit clearing, actual copy and username survival across old30s interval,
Back/background lock and unchanged encrypted proof jars. This does not prove untouched
Dish30s clearance, wrong different Menu, cache reopening/favorites, recreation/cold
start, actual5/10minute deadlines, broad EN/VI/capture/layout or Google.

Owner confirms Run01 had **no navigation/screen-lock action** and Google setup is
incomplete. Do not apply the Run02 admission backward. Run04 passed under proof-only
KEEP_SCREEN_ON; it establishes selected foreground behavior, not original cause/fix.
Next controlled experiment prepares an explicit proof-window normal-screen-policy
argument and sanitized pause/stop power/keyguard/window snapshots; runtime Pending.
No production screen/lock policy change. Still select one class; no repeat of the8
already-passed platform tests.

## Controlled comparison candidate — preparation snapshot

Only two androidTest files changed after Run04. Explicit instrumentation argument
`-e passchefProofKeepScreenOn false` clears the isolated proof window's awake flag
before existing setup; default behavior retains it. Every original assertion/wait
is unchanged. Pause/stop snapshots now record power-interactive, keyguard/device-lock,
window focus/attach/show and KEEP_SCREEN_ON booleans, with separate read-success bits.
No contents/exceptions, device-setting change, production code or authentication bypass.

Independent review found no P1/P2. Incremental test-APK/lint build Passed35s,
10 executed/84 up-to-date; lint0 errors/11 warnings/1 hint. New local test APK SHA256:
086e7022d34423e151cb1a09bd81b76e927cd1399f7f78e8aa066e3a80f35cf3.
USB became unavailable before the first install/preflight digest read; Windows ADB
re-list showed no device. **No new installation or Run05 occurred**. Prior Run04
used installed91da0f8c... test APK; never credit it to this new candidate.
Evidence:`screen-policy-diagnostic-build.log`, `screen-policy-diagnostic-proof.json`.
Run05 awaits reconnect, data-preserving test update and actual Awake/keyguard=false.

## Run05 — actual normal-screen-policy pass

Owner reconnected/powered the same Xiaomi/API36 (USB transport8). Test086e7022... was
installed with `install -r`; installed debug remainsd4eb8401..., and measured owner
Cookbook digest remains894f73e5... before/after installation. Initial state was
Dozing/keyguard showing, so no runtime started. Owner then explicitly reported entering
the unlocked home screen; root's immediate read-only gate verified Awake/keyguard=false.
Updated locked diagnostic **OK (1 test),1.943s** ran before the controlled UI job.

Root selected only the unchanged UI test with `-e passchefProofKeepScreenOn false`.
Actual **Run05: OK (1 test),53.663s**,0 failures. Before119699545ms/after119710162ms
snapshots are10.617s apart: both RESUMED, no pause/stop, successful state/environment/
lifecycle reads, interactive/unlocked screen and focused/attached/shown proof window.
Window KEEP_SCREEN_ON is **false in both snapshots**. Dish/Copy/retained readiness and
vault session stay true after delay; revealed/plaintext presentation become false.
All later original mapping/tag/edit/copy/username/Back/background assertions passed.

The recorded pause119745539ms and stop119746409ms occur after the intentional final
background transition,35.377s after the post-reveal snapshot; stop locks the session.
This establishes selected flow under normal screen policy and no premature reveal
invalidation in this run. Independent reviewer agrees. It does **not** identify the
historical Run01 cause, which remains not reproduced. No production repair claimed.
Broader phone matrix and Google setup/grant/read remain Pending.

Owner Cookbook digest was re-measured unchanged after both runtime jobs and recorded
in `ui-run-05.json`/`diagnostic-locked-run-02.json`. Protected208 and unrelated23 byte
checks still pass; assertions/waits and production main/shared/canonical remain intact.

## Evidence and handoff

Directory:`/tmp/passchef-cookbook-device-acceptance/`:
`run02-install.json`, `diagnostic-locked-run-01.log`/`.json`,
`ui-run-02.log`/`.json`, `ui-run-02-own-activity-events.log`,
`ui-run-03.log`/`.json`, `ui-run-03-own-activity-events.log`,
`ui-run-04-preflight.json` (earlier wait), `ui-run-04-ready-preflight.json`,
`ui-run-04.log`/`.json`, `runtime-final-preflight.json` (earlier snapshot).
Latest:`run05-install.json`, `ui-run-05-preflight.json`, `ui-run-05-ready-preflight.json`,
`diagnostic-locked-run-02.log`/`.json`, `ui-run-05.log`/`.json`.
Owner Cookbook digest remains unchanged after Run04. Independent reviewer confirmed
the terminal pass/snapshots and limits; no P1/P2 in the accepted diagnostic changes.
Native process exit0 never overrides either JUnit failure.

Fresh local shared78/app4 and lint0 errors/11 warnings/1 hint remain valid on unchanged
sources. Protected208 and unrelated23 bytes rechecked; exact assertions/waits preserved.
No Gradle/instrumentation job remains active. All work remains WIP/unstaged; no
commit/push/archive/whole-mobile Done. `.agents/` and skills-lock untouched.

Unresolved: historical Run01 cause not reproduced; remaining clipboard/
lifecycle/session/UI matrix; actual Android Google configuration/grant/read.
