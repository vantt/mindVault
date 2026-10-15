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

## Provider cold-process independent review — selected Run02 Passed

Reviewer checked four new bounded files/three native helpers and reused identities.
All17 original Google logical predicates preserved after restoring the new reconnect
foreground conjunct; original test and54 prior app sources/manifests unchanged.
Genuine Connect/provider metadata/tab/schema/mapping/cache, canonical safe provider
baseline, exact native PID/UID/start/hash signal guards, warm interrupted vs separate
strict cold JUnit, locked state/genuine reopen/cache/Cook and owned cleanup reviewed.
No remaining blockers. Reviewer audited actual warm02 PID13092/UID10422/SIGKILL18:24:28.873
and old-proc/pidofabsence18:24:29.308Asia/Saigon before actual coldPID14408. Linked cold02
Passed1/1,8.886s [1,0]/[-1]/OK1/no watchdog/error: initial locked/cleared state, real
fingerprint exact cache reload/search/favorites/recent/origin/Recipe/literal00123/Cook
and unchanged jars/owned cleanup/digest. Source58/six helpers/frozen03 exact; original54
and208/23 unchanged. Cold01 Failed181.588s remains unknown cause; two-file diagnostic
delta preserves all waits/assertions. SDK grant may be reused, new consent/revocation
never inferred. No mock/offline paste/token/session/deadline injection. Observed network
available; LMK/task restore/provider errors/full mobile Pending. [Evidence](google-cold-runtime.md).

## Vietnamese OS field independent review — Run01 Passed

Reviewer verified source134/runner105/frozen59/seven helpers/APKs current+archived exact,
original58/protected208/unrelated23/owner digest and release exclusion retained.
Actual Run01 Passed1/1,1.5s [1,0]/[-1]/anchoredOK1/completed/no watchdog/error:
one own visible/editable/password fieldID10, labelDescendants=true/labelSelf=false,
first fresh-observer-cache attempt,38 nodes/3 descendants and32/32 owned-tree requests.
Clone/recycle/literal500/100 bounds/own-window/no-input/no-auth/policy rules reviewed.
No blockers. Original failed direct same-node checker untouched; targeted-node refresh,
TalkBack spoken output and previousVI ID0 cause not claimed. [Evidence](vi-accessibility-runtime.md).

## Native language input independent review — Partial

Reviewer audited coordinate transform/own-window/finite/IME guards/native DOWN/UP,
original59 immutability, selectors and strict independent verdicts. Edge01 Failed
1.408s, edge02 Failed1.211s with actual post-input English=true/VI=false/foreguardtrue;
center02 Passed1.852s [1,0]/[-1]/OK1, two60ms center pairs and exact UI switches.
Source60/eight-helper/APK identities bind accepted/failed candidates separately.
Center point+dwell differ; no cause, physical-edge or whole-target acceptance inferred.
Original edge geometry/assertions unchanged except read-only diagnostics. Separate
edge60 wrapper leaves default edge0 behavior; exact wrapper selector/strictcompletion
reviewed. Metadata renamed explicitlyOptionalEdgeDwellMillis to avoid confusing
center60 with defaultedge0. No code/runner blockers; corrected build04/frozen04
testce61 exact. USB empty before initial install14October; record remains historical.
Reconnected15October/transport26, exactce61 installed-r/debug1f42 retained. Independent
raw audit: dwell04 Failed1/1,1.709s [1,-2]/[-1], one60ms pair/allpointguards/stillEnglish;
center04 Passed1/1,1.802s [1,0]/[-1]/OK1, two60ms pairs/exactEN→VI→EN switches.
No watchdog/error; owner894f73…a4d6 before/after and final audit preserved, current/
archived60/eighthelpers/protected208/23 exact. Same-candidate comparison supplies no
production-cause or whole-target acceptance at that checkpoint.
Later independent raw/archive audits: geometry01 Passed1.505s/source61/helper9/testc0fd,
same bottom midpoint outside40dp layout/inside48dp declared touch, both enclosing
layouts contain it and no observed scroll bounds cover it. Finger01 Passed2.464s/
source63/helper10/test9a64, four native pairs at equal geometry/configured60ms:
UNKNOWN retained all three localized controls; FINGER switched all three both ways.
Eight events checked actual baseline constructorUnknown/public non-tool readback/tool
readback/sourceTouchscreen; strict[1,0]/[-1]/OK1, no watchdog/error. Prior61/nine helpers/
protected208/23/ownerbaseline exact. Accept selected new FINGER samples only, no
retrospective old-event measurement/whole-target/production repair inference.
[Evidence](language-touch-runtime.md). Production/old assertions untouched; Mobile Partial.

## Manual shortcut checkpoint review — Partial

New149-line fixture and bounded runner/publisher reviewed; before runtime corrected
irreversible any-stop failure latch and exact active selected-start/no-failure gate.
Source64/helper12/frozen03/04/APK identities retained, old63/ten helpers unchanged.
Actual03 Failed182.485s setup/no ready, safe preterminal ownPID wrap15 evidence only;
exact-UUID cleanup/owner preserved. Actual04 reached genuine secure masked Cook;
actual terminal independently verified Failed1/1,236.235s [1,-2]/[-1], elapsed180429ms
without owner-response completion, reveal/focus-loss=-1/stops0/anyStopFailurefalse.
No watchdog/error; final exact-UUID cleanup/owner/source64/helper12/installed19b9/
releaseefd3 match. Hardware shortcut remains Pending without confirmed observation.
No owner observation/signal was fabricated; screenshot verdict remains separate.
Late04 missing-button reply/phase ambiguity retained. Same-candidate05 reached ready;
independent raw05 audit confirms Failed1/1,188.013s [1,-2]/[-1]/FAILURES/no watchdog/
error, completion absent180421ms/reveal and focusloss=-1/stops0/anyStopFailurefalse.
No owner05 record/signal; exact UUID cleanup/owner/source64/helper12/installed19b9/
releaseefd3 match. No remaining code/docs blockers at05; actual shortcut remained Pending there.
[Evidence](manual-shortcut-runtime.md). No whole-mobile/production-cause acceptance.

## Manual visual preparation and selected Run07

Independent raw06 audit confirms Failed1/1,193.721s [1,-2]/[-1]/FAILURES, no watchdog/
runner error; completion absent180483ms after a separate publisher transport failure.
Owner reported Power+VolumeDown refusal and later confirmed visible dots stayed masked
after Reveal. Sampled exact model reveal64979/86974ms, masked87179ms, is neither pixels
nor continuous22s evidence. Exact UUID cleanup/owner/source64/helper12/installed19b9/
releaseefd3 preserved before helper edits. Live publisher stderr was lost; isolated
old-protocol hardlink Permission denied does not recover that live failure stage.

Revised host runner/publisher reviewed: exclusive child lock, existing parent only,
stale pending/final preservation, complete-file mv-n publication and owned cleanup;
consumer deletion cannot require final existence afterward. Exact class/method/UUID/
frozen identity/actual owner reply and boot-bound host-monotonic age<150s checked;
bounded native diagnostics/unknown transport outcomes retained. Final publisher hash
eec2a1de… matches isolated probe03: six cases Passed, all probe dirs removed and owner
unchanged. Consumer-parent deletion was controlled in one shell, not an independently
scheduled race. Isolated protocol success grants no actual fixture/screenshot verdict.

New ManualVisualPhysicalTest152/helper71 reviewed; old64 source paths remain exact.
Copied original assertions/report/180s/marker/stop latch unchanged. Snapshot adds
normal instrumentation-thread Compose idle synchronization and safe semantics/layout
booleans; stop callback still reads state directly. This is a material harness change,
consistent with documented Compose frame behavior, not a proven retrospective cause.
Node acquisition can straddle controller transitions; bounds/semantics do not establish
pixels, clipping or owner-visible Dish. JUnit remains checkpoint-only.

Build Passed21s,12 executed/82 UP-TO-DATE; lint XML0errors/11warnings, HTML11warnings/
1hint read directly. Frozen07/current/archive source66/helper12/APKs exact: debug1f42,
test0d4c2bd844d72376a15533cea7132fcae4a1f24a7625c16347c79cb6916993d9.
Old ten helper hashes unchanged; only host runner/publisher intentionally changed.
No code/runner preparation blockers; this preparation review preceded runtime07.

Independent raw07 audit now confirms Passed1/1,116.736s [1,0]/[-1]/anchoredOK1,
native0/no watchdog/error, checkpointObserved/revealObserved=true and0 failures/errors/
skips. At elapsed88388ms, actual Dish semantics matched the synthetic text while exact
controller reveal/secure own foreground/full own-content and window bounds were true.
Mask semantics returned at98382ms; last model reveal sampled98113ms. These remain
sampled semantics/model observations, not pixel capture or exact shortcut timing.

Owner directly replied “Dish hiện chữ, máy chặn chụp” to the explicit single-Reveal/
immediate Power+VolumeDown question. Exact UUID observation was saved before live
publication, whose receipt age108.339s passed the150s gate. Native publisher returned0/
stdout published/empty stderr; result/signal/observation/frozen hashes link exactly.
This is actual cooperative lock+rename publication, separately from isolated probes.
Final native audit confirms exact UUID directory absent, owner894f73…a4d6, installed
debug1f42/test0d4c, source66/old64/helper12/protected208/unrelated23/releaseefd3 preserved.
Current/archive identities independently match. Accept selected owner-observed visible
Dish and hardware-shortcut refusal plus the JUnit checkpoint. No screenshot pixels,
exact OS wording/key timestamp or retrospective Run06/historical cause is accepted.
Original03..06 failures stay Failed; synchronization explains a supported harness
mechanism but this comparison does not prove the old visual cause. Full mobile remains
Partial. [Evidence](manual-shortcut-runtime.md); no production change or fresh JVM/browser
execution claimed.

## Unattended source continuation — independent test evidence audit

Tester read the archived XML/raw logs and current three new test files, then recomputed
test identities, source/log/APK hashes and preservation checks independently. Root logs
show app/bridge/lint Passed 9s and standalone JDK21 test Passed 20s. Archived XML confirms
app 10, bridge 22 and standalone 35, each with 0 failures/errors/skips. The 22 portable
class/method identities occur in both bridge and standalone; standalone adds 13 JVM-only
cases. These are overlapping executions, never an aggregate count of unique tests.
Shared 78 remains prior evidence; no fresh shared run or device execution is claimed.

Six app cases exercise real coordinator/files at readiness/cancelled commit boundaries,
invalid/remapped/oversized Menu recovery and actual filesystem failure/retry. Six common
cases cancel incomplete response bodies for values/metadata under 200/401/403 and require
CancellationException plus released channels. MockEngine supplies synthetic HTTP streams;
this does not prove live Google cancellation, SDK callbacks or grant rejection. The saved
initial four-case stream candidate also Passed; no production bug or repair was reproduced.

All three archived/current sources match (97/120/63 lines); all 66 prior app sources,
protected 208/unrelated 23 and debug 1f42/test 0d4c/release efd3 hashes match independently.
Existing tracked code diff is empty; earlier tests/assertions and production remain unchanged.
Current lint XML is 0 errors/11 warnings; no new APK build/install/provider interaction.
Evidence archive: `/tmp/passchef-cookbook-device-acceptance/unattended-source-20261015-01`.
No test-evidence blocker found. Broader mobile remains Partial.

Unresolved: original Cookbook UI Run01 cause and network Run03 key-wrap cause;
cancellation/stale callbacks/401/403/LMK/task restoration/TalkBack and remaining phone matrix.

## Google cancellation / stopped-result preparation —15October2026

Independent review read both new instrumentation files (104/107lines), the final
172-line external runner, raw build02 log and build-audit JSON. Main-aware reads
avoid nested main dispatch; natural-stop failures are latched and fail before any
NotExercised assumption. Skip requires no observed request/stop/pending and no busy
source. Real pending identity, Google active-window package, natural stop, consumed
reply and returned locked state gate stale-result acceptance. Cleanup uses nested
finally. No production callback/session/token injection or old assertion change.

The fixture creates its last-good offline synthetic cache through actual UI and
genuine biometric setup. This is not provider-loaded cache cancellation acceptance.
JUnit can certify the selected stale-reply preservation boundary, not the owner's
cancellation action or exact Android resultCode. Owner observation remains separate.
The runner requires selected class/method codes[1,0], terminal[-1], anchoredOK1,
native0, no watchdog/error/failure diagnostics and the final success phase. Codes
[1,-4] plus the explicit no-resolution phase mean NotExercised/Pending. Its quoted
UUID-only cleanup query, owner digest and final source/artifact audit gate the result;
final-audit errors are saved as Failed. No instrumentation outcome exists here yet.

Build02 raw log confirms Passed26s,10executed/84UP-TO-DATE. Current lint XML contains
0errors/11warnings/1hint. Independently recomputed frozen01/current/archive equality
for70 app sources, both APKs, runner, baseline and frozen JSON. All68 prior app
sources and26 historical helpers remain exact; releaseefd3 unchanged. Candidate01
debug1f42/test669a8c9c identities match the current APKs. The earlier tracked491
audit differed only in the permitted phase04 plan before documentation updates.

Prepared only: no fresh JVM/browser execution, installation, actual Google prompt
or runtime acceptance is claimed. Root's phone check found asleep/locked; runtime
still requires fresh awake/keyguard/foreground gates and timely owner biometric/
Google interaction. No remaining preparation blocker found. Mobile remains Partial;
401/403, different-Menu callbacks and historical failure causes remain unresolved.

Actual selected Google cancellation run01 is **NotExercised**,15.608s: independent
raw-log/JSON read confirms selected codes[1,-4], terminal[-1], anchoredOK1, native0,
0failures/errors and1skip; no watchdog/orchestration error. Genuine setup completed,
but Connect returned usable metadata/token without an observed pending resolution,
own stop or Google window. Exact baseline cache/jars assertions completed before the
explicit assumption. This is consistent with SDK grant reuse, not actual cancellation
or stale provider-callback acceptance; both remain Pending. Recorded post-audit has
owner894f73…a4d6, old68/source70/helper26/protected208/unrelated23 preservation and
exact UUID directory absent. Run01 and candidate01 remain retained; no permission/
account change or production repair is inferred. Runtime artifacts:
`/tmp/passchef-cookbook-device-acceptance/google-cancellation-run-01.{log,json}`.

## Selected Menu mapping / wrong-Menu tag — independent actual Run01 audit

Reviewed new139-line fixture and169-line host runner before runtime. Actual UI
rejects duplicate role-column assignments and a missing Recipe column while keeping
the last-good disk/RAM snapshot; explicit remapping then retains physical rows2/3/7/8,
literal00123/Unicode and missing optional cells while omitting empty/invalid Recipes.
Duplicate/blank headers remain exact in the preview and confirmed header. This
fixture does not prove ambiguous automatic-column proposals. A genuine tagged Cook
under the matching saved Menu provides the positive control; editable-text equality
then proves the Recipe remains unchanged when choosing a distinct saved Menu.
RECIPE_MISMATCH, absent Dish/Copy and preserved both caches/jars are actual assertions.

Independent raw/JSON audit confirms selected Run01 **Passed1/1,36.892s**,codes[1,0],
terminal[-1], anchoredOK1,native0,0failures/errors/skips,no watchdog/orchestration error.
Build02 raw log Passed12s,9executed/85UP-TO-DATE; current lint XML0errors/11warnings/
1hint. Recomputed frozen01/current/archive71 sources, prior70 and27 old helpers,
runner/baseline/frozen JSON and both APK identities match. Install record binds
debug1f42/test3e5df260 to the exact frozen candidate and unchanged owner digest.
Runtime audit records protected208/unrelated23/release preservation, owner894f73…a4d6
and exact UUID directory absent. No production or old assertion repair was required.

Evidence: `/tmp/passchef-cookbook-device-acceptance/menu-mapping-run-01.{log,json}`
and `menu-mapping-candidate-01/`. This accepts selected offline mapping/tag UI on
Xiaomi/API36; no fresh JVM/browser suite or broader Menu/provider/platform acceptance
is inferred. Google cancellation run01 remains NotExercised; cancellation/stale
provider callback/401/403, ambiguous proposals and historical causes stay Pending.

## Screen-off / clipboard selected Run01 — independent Failed evidence audit

Reviewed the new132-line fixture and169-line runner before runtime. Actual UI Copy
checks an exact owned sensitive marker while focused; natural stop/noninteractive
must occur before25s, followed by sampled35s and a final still-off/cleared/cache gate.
Clipboard is never inspected offscreen; return uses normal production reconciliation,
and failure cleanup requires legal foreground plus the exact owned marker/text.
These checks do not cover universal continuity, long background, LMK/recreation,
cross-app replacement, exact background30s cleanup or clipboard history.

Independent raw/JSON read confirms Run01 **Failed1/1,37.492s**,selected codes[1,-2],
terminal[-1],FAILURES,native0,no watchdog/orchestration error. Genuine setup/cache/
Cook/Copy completed and owner-power-off-now was published at elapsedCopy33ms.
Failure was reported at25031ms with awake/resumed/focused=true and session still
ready; the required predeadline stop/noninteractive condition was not fulfilled.
No screen-off-observed-hold or wake/return marker exists, so no screen-off/background
clipboard-cleanup acceptance is earned. The failed path's elapsedOff1ms is a timestamp
assignment before the failed assertion, not observed evidence of screen-off.

Recorded post-audit preserves owner894f73…a4d6, prior71 app sources/helper28,
protected208/unrelated23 and the exact UUID directory is absent. Failure/candidate/
logs remain retained; no production cause or repair is established. Owner action is
being clarified before any retry; no assertion/deadline was weakened. Evidence:
`/tmp/passchef-cookbook-device-acceptance/screen-off-clipboard-run-01.{log,json}`.
Selected screen-off/clipboard acceptance remains Pending; Google cancellation run01
still remains NotExercised and mapping Run01 retains its separate Passed scope.

Selected screen-off Run02 also remains **Failed1/1,54.316s**. Independent raw/JSON
read confirms[1,-2]/[-1]/FAILURES,native0,no watchdog/orchestration error. The Power
marker was published at elapsedCopy29ms; at25025ms the app was still awake/resumed/
focused and failed before any screen-off-observed-hold marker. Frozen02 retains the
same72 source identities, APKs and runner as01; no rebuild/install was needed. The
recorded audit again preserves owner894f73…a4d6, prior71/helper28/protected208/
unrelated23 and exact UUID cleanup. Both failures/candidates/logs remain retained.

Owner had clarified that Power was not pressed in01 and explicitly agreed to retry.
Root reports the specific Power follow-up in02 was posted too late for the native25s
window; the owner reply “Đã tắt màn hình” arrived after terminal completion. That
late reply has no exact physical-key timestamp and cannot retroactively establish
screen-off during the test. This is a coordination timing limit, not evidence of a
production screen-off/clipboard defect or successful cleanup. No assertions or
deadlines were weakened; selected acceptance remains Pending and no native wait is
currently active. Evidence: `screen-off-clipboard-run-02.{log,json}` in the same
external evidence directory. A later attempt needs a prepared owner checkpoint,
without discarding either Failed verdict or claiming universal sampling continuity.
