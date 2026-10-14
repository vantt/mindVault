# Independent mobile Cookbook review

Status: **No unresolved P1/P2 in the reviewed code**. Local verification and
selected Google/cache UI acceptance passed; broader phone/provider matrix Pending.
Historical local snapshot below retained; latest continuation review at the end.
Current [session timer harness](session-timer-runtime.md) separates original pointer/
setup failures from Idle05 Passed341.722s actual5-minute foreground proof. Total01/02
setup Failed; Total03 setup Failed182.138s, earlier causes unknown.14October Total04
Passed1/1,609.969s, runner[1,0]/OK1 after genuine setup/real pointer activity through
original ten-minute expiry. Same frozen03/APKs, no source/build/install changes;
selected API36 foreground proof5/10-minute scope, broader timer UX Pending.
Reviewer: agent A, independent of product author B. Date:2026-10-11 UTC.

## Scope and evidence

- Review actual Android Cookbook/session/authorization/clipboard adapters, private
  Menu cache/mapping and the canonical portable Sheets client reused by the Android bridge.
- Owned adversarial test: `shared/src/jvmTest/kotlin/dev/passchef/mobile/cookbook/CookbookAdversarialTest.kt`.
 13 tests independently executed and passed in the fresh 74-test shared JVM run.
- The 75 protected original extension/mobile tests, fixtures and recipe/crypto/vault
  sources match the 106-path baseline byte-for-byte at the latest incremental check.
- Extension recheck is separately recorded in [the report](../../reports/261012-extension-recheck.md).
  No extension production changes are part of this mobile slice.

## Findings and verified repairs

| Finding | Observed repair | Verification status |
| --- | --- | --- |
| Malformed UTF-8 cache bytes silently decoded with replacement characters | Strict decoding in cache load/inventory | Independent hostile-byte test passed |
| Refresh stage created a replacement write ticket, leaving originating request busy forever | Originating ticket passed through stage/save/finally | Source rechecked; production app deferred-IO tests passed |
| Concurrent favorite/recent saves could lose a metadata update | Busy ownership spans IO; actual list actions disabled while busy | Production app deferred-IO regression passed; actual conflicting controls disabled |
| Pending refresh could recreate a removed cache/source | Production `SourceOwnership` invalidation and atomic final commit; removal clears selected Dish | Independent deferred real-cache test passed |
| Stale authorization could be accepted after local lock/reopen | Production ownership guard; retired external request blocks overlap until consumed | Source rechecked; shared ownership/grant tests passed; actual Google callback runtime Pending |
| Partial Google permission grant could supply an unusable access token | Production grant policy requires actual Sheets scope and bounded nonblank token | Production grant-policy tests and canonical16 protocol tests passed |
| Clipboard cleanup disappeared with Activity destruction | Process-owned cleanup with separate foreground guard, digest/label comparison and resume reconciliation | Source rechecked; physical acceptance pending |
| Ordinary Ktor requests buffered the entire response before the decoder's cap | Streaming prepared requests bound before EOF | Non-EOF regression passed in bridge16 and standalone29 |
| Compose could keep masked tagged-Dish actions after an edit without observable state change | Observable readiness read by actual Dish panel, cleared by edit | Source reviewed; tagged masked-edit UI regression compiled, not executed |

Root also reviewed callback-exception safety and stale cooking work. Product author
retains ownership of fixes/tests. Inventory load, clear, offline refresh and waiting
Google-grant operations now own busy state; actual conflicting controls are disabled.
Production coordinator regressions pass; prior good caches survive failed reads/writes.
No production source was changed by this reviewer; only the owned hostile test and reports.

## Independent test intent

Tests reject tied/weak column guesses, mapping/header drift, hostile cache origins,
duplicate identities, malformed UTF-8, real atomic-write failure and source-limit
eviction. They verify literal usernames/physical row offsets, stale Dish publication,
clipboard denial/replacement, and production ownership cancellation/remove/switch.
No generated Dish oracle or synthetic authentication is credited as device proof.

## Acceptance boundary

Fresh independent results:shared74, bridge16, app4 and standalone29 tests; zero
failures/errors/skips. Shared/bridge packaging and debug/test/release APKs build.
Lint has0 errors,11 warnings,1 hint. All99 source/build byte/mode paths stayed stable;
51 author-frozen hashes match. 75 protected baseline and128 extension paths remain
unchanged;23 unrelated Cave/skills-lock hashes match. 27 new Kotlin files are<200lines.
[Verification](verification.md) records commands, final APK hashes and proof limits.
Android UI test APK compilation does not imply executed UI tests.
Real Google configuration/account consent/API read, phone biometrics, clipboard
and lifecycle/UX acceptance remain Pending. No emulator/device job was run.
QR receiver/camera, Autofill, Drive discovery and mobile authoring remain later scope.

## Continuation review

The two-agent [unattended continuation](unattended-regression.md) independently
reviewed four shared regressions, refined read-only diagnostics and a compiled
locked-only lifecycle test; no P1/P2 found in those changes. Reviewer parsed fresh
shared78/app4 XML and checked reflected compiled field names/types. Production
source is unchanged. Actual [UI Run01 failure](device-acceptance.md) remains
undetermined; local policy checks do not prove a physical repair. [Later runtime](ui-runtime-continuation.md):
locked diagnostic1/1 passed; Run02 failed with owner-confirmed foreground interruption;
Run03 failed before setup. Run04 passed selected foreground flow; reviewer confirmed
terminal1/1,62.575s and actual10.653s remask
with retained Dish/session. Further test-only pause/stop diagnostics and explicit
normal-screen-policy argument were reviewed with no P1/P2; compile passed, runtime
initially Pending after USB loss; after reconnect, locked diagnostic02 Passed1/1,
1.943s and Run05 Passed1/1,53.663s. Reviewer independently confirms actual
KEEP_SCREEN_ON=false,10.617s remask with retained Dish/session, and pause/stop only
after the deliberate final background transition. No production repair inferred.

Oct13 independent clipboard review confirmed exact three runner start1/pass0 pairs,
terminal3/3,99.926s, no skips/failures. Real foreground ClipboardManager/OwnedClipboard
fixture observes29s retention/30.5s clear, replacement and newer deadlines, sensitivity
metadata, locked vault/no record and exact owned cleanup. Root separately measured
unchanged owner digest after runtime. API36/API28+ foreground adapter scope only;
no Cook/auth/Google/background/recreation/API26 fallback accepted by those tests.

Independent Oct13 [clipboard Activity recreation](clipboard-recreation-runtime.md)
review found no P1/P2. Final133-line source and build/install/runtime hashes match;
runner has one start1/pass0 pair, terminal1/1,31.721s with no skips/failure codes.
Actual original destruction/new identity/two proof directories, original29s retention
and30.5s clear are asserted behind real foreground/locked-vault guards. Owner digest
unchanged after runtime. API36 same-process adapter acceptance only; authenticated
Cookbook recreation/process death/background/Google/Run01 cause remain unproven.

Oct13 real-Google acceptance fixture source reviewed: no P1/P2. One real setup/
owner-biometric flow, actual production Connect/provider metadata/tab/rows, explicit
mapping and durable cache readback; one UI reconnect only after actual background
lock. Empty owned storage, immutable synthetic row expectations and sanitized
status2 diagnostics prevent injected/offline success or private failure dumps.
[Prepared candidate](google-acceptance-preparation.md) still has0 Google tests run;
usable permission may reuse a grant and does not alone prove fresh consent UI.

Independent [Google runtime](google-runtime.md) parsing confirms Run01 Failed1/1,
44.766s and Run02 Failed1/1,19.259s: each start1/failure-2, diagnostic2 separate,
no skips/errors and native0 not credited as pass. Source paths support real usable
grant/requested metadata and actual selected-tab values in Run02. Owner confirmed
fresh account/Sheets consent in Run01 and renamed the previously mismatched tab.
Run02 literal/origin assertion remains coarse; no schema/mapping/cache acceptance
or production repair credited. Owner digest unchanged after both runs.

Subsequent173-line diagnostic refinement independently verified: all17 original
assertTrue calls byte-identical/in order;11 read-only expected identity/header/row
match booleans and finer phases only. No private values/URL/token or original cause
dumped, final cache/status2 unchanged. No P1/P2; source0de333ad frozen, compilation
Passed59s, runtime/new install Pending. Owner reports only A1/A2 populated.

Independent Google Run03 parsing: Passed1/1,23.309s; exact JUnit[1,0], diagnostic2
separate, terminalOK1,0 failures/errors/skips. Source0de333ad/testcc4d5bbc/debugd4/
installed hashes agree; owner894 digest unchanged after install/runtime. All11
expected flags true; original17 assertions verify genuine authorization/provider
metadata/Test tab/literal rows/explicit mapping/actual cache-file load and unchanged
fake jars. Fresh consent owner-observed Run01; Run03 may reuse that grant. Accepted
selected scope only, not UI cache reopening/disconnect/errors/writes/full-mobile.

Independent latest [Google cache UI review](google-cache-ui-runtime.md): no P1/P2
in opt-in owned-storage retention or150-line continuation helper. Original17 Google
assertions byte-identical/in order; corrective four sync/phase lines retain all helper
assertions. Default two-directory physical regression Passed1/1,31.769s. Cache UI
Run01 Failed1/1,294.651s retained; corrected Run02 Passed1/1,42.393s, runner[1,0],
0 failures/errors/skips. Actual recreated biometric ready=true/cache=false checkpoint
then normal Compose sync/cache=true and unchanged exact cache/UI/Cook assertions
passed. Source/APK/owner-digest evidence checked; production unchanged. Scope only
app-disconnected cache and same-process authenticated recreation; no original UI fix.

Independent [network-off Run04](network-off-runtime.md) review confirms no P1/P2,
runner[1,0]/OK1,31.477s,0 failures/errors/skips, original11 provider flags true and
all14 post-off strict default/bound/internet-network-absence checkpoints true with
no reconnection. Actual retained-grant UI tab read NETWORK_FAILED, exact cache/disk/
jars/stale Dish/Copy preservation and unchanged150-line cached Cook/favorite/recent/
search/biometric/recreation assertions accepted. Sources39/protected208/unrelated23
unchanged; no rebuild/install between identical Run02/03/04 candidates.26 safe
status2 packets separate from2 JUnit statuses. Prior three failures retained; no
production or original UI Run01 repair. Restoration Wi-Fi/airplane/default observed;
mobile-data enable command accepted only. Scope Xiaomi/API36 physical continuation.

## Security/UI independent continuation review,14October

Six new bounded test/helper files reviewed; final root build/lint Passed42s,0/11/1.
No P1/P2 remains in fixture/runner changes. Review repairs: actual localized OS node
predicate kept strict; masked guard follows empty plaintext contract, revealed capture
requires actual visible Dish inside sampled region before and displayed exact result
after; optional Compose ID explicitly requested before bounded diagnostic matching;
Recents needs fresh post-checkpoint pause/stop and fresh successful stopped-state read.
Reviewed external navigation uses cancellation/event/lock/join to finalize commands
before evidence; real fixture lifecycle remains authoritative, no command-only pass.

Reviewer independently parsed actual pixels01 Passed2.321s/authenticated02 Passed25.890s:
JUnit[1,0]/terminal[-1]/anchoredOK1,0 failures/errors/skips, completed=true/watchdog=false.
Both EN/VI shell captures valid1220×2712, own1194×2395/all2,859,630 pixels opaque black.
Genuine authenticated Cook/reveal/source-owned jars equality and exact Dish guards pass.
No policy/auth/session/clock bypass, private text/tree/pixel logging or production change.

Original three security failures retained. Recents01 Failed206.052s with no post-checkpoint
pause/stop; cleanup-only lock is excluded. Recents02 Failed182.457s at setup, busy=true/
focus=false, phone awake/unlocked, navigation_events=[]; cause unknown, no187 sent.
Owner “can't take screenshot” report separate; reveal timing/Card contents unconfirmed.
Reviewer independently audited Recents03 Passed1/1,137.789s: JUnit[1,0]/terminal[-1]/
anchoredOK1, completed=true/watchdog=false, actual pause1/stop1/stoppedLocked/cleared
return and genuine reopening/jars equality. Native187 at10:10:56.037UTC, return at
10:11:16.298UTC; fixture lifecycle authoritative. Run03-specific owner observation
“Thẻ trống/che, không lộ nội dung” JSON binds to the exact log SHA. No blockers;
selected API36 Recents lifecycle/card accepted. OS shortcut/full accessibility/broader
UI Pending; Mobile remains Partial. Earlier screenshot report remains separate.

Independent audits Passed frozen49/current APKs, old43/protected208/unrelated23 and
archived failed/accepted candidates02–05 exact sources/manifests/APKs. Accepted capture
source/APK identity frozen03/e314 is separate from installed Recents frozen04/ea84.
[Authoritative evidence and limits](security-ui-runtime.md). No blind rerun endorsed.

## Offline cold-process independent review,14October

Reviewer independently audited five bounded new files/unexported UUID-owned Activity,
pre-super locator validation, dedicated task, empty Compose rule, genuine UI/auth,
strict disk/cache/Dish assertions and owned cleanup. No prior assertion/production
change. Reviewed external signal revalidates PID/cmdline/fourUID/startticks plus
owned baseline/jars/cache hashes immediately before SIGKILL; focus uses actual
display property. Timer expiry distinguished from native detach errors. No blockers.

Actual warm02 InterruptedByVerifiedSIGKILL verified from raw runner crash and native
death evidence; never JUnit Passed. Cold02 independently parsed Passed1/1,5.993s,
JUnit[1,0]/terminal[-1]/anchoredOK1/no watchdog/error: PID25682→25991 sameUID10422,
initial secure/locked/cleared state, exact preinteraction disk hashes, genuine biometric
reload, offline search/favorites/recent/origin/Recipe/literal00123/Cook and jars equality.
Source54/three runner hashes/APKs/protected208/23 match. Historical49 has exactly one
intentional debug manifest entry; release proof excluded. Warm01 Failed preSIGKILL
focus-query guard retained, diagnosed windows property absent/displays exact focus.
[Evidence](cold-process-runtime.md). Scope synthetic offline controlled SIGKILL+fresh
Intent; provider cold/LMK/saved-task/background death and whole mobile remain Pending.

Unresolved: original Cookbook UI Run01 cause and network Run03 key-wrap cause;
cancellation/stale callbacks/401/403/cold-start/process-death and remaining phone matrix.
