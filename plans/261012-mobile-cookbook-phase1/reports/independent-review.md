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

## Original screen-off Run03 and owner-controlled preparation

Independent raw/JSON audit confirms original-method Run03 **Failed1/1,105.248s**,
codes[1,-2]/terminal[-1]/FAILURES,native0,no watchdog/orchestration error. The failure
sample at copy+25035ms remained awake/resumed/focused; no hold/wake-return stage
was reached. Exact owner observation03 says “Đã tắt màn hình” arrived after native
terminal, with physical key time unknown. Post-audit records owner894f73…a4d6,
prior71/helper28/protected208/unrelated23 preservation and owned directory absent.
This adds no screen-off/clipboard acceptance and establishes no production cause.

Reviewed separate owner-controlled fixture137/helper70 and169-line runner; all72
old app files remain exact. Normal Back→row Cook produces actual masked Dish with
visible Copy/hidden IME before a bounded180s owner wait. Foreground-only normal
Compose idle permits Copied presentation updates; observation errors fail promptly.
Owner Copy→Power needs no host round trip. Main clipboard listener binds exact own
sensitive synthetic content/adapter label/COPIED to generation baseline+1 and the
unchanged production deadline. copiedAt=deadline−30000 must fall after preparation
and before observation; generation changes fail instead of renewing any clock.
The listener reads no clipboard after lost foreground or first capture. Volatile copiedAt is
published after its accompanying fields; required main-thread reads avoid nesting.
Original25s stop, sampled>=35s/final-off, locked return/cache/jar gates remain.
Listener removal errors propagate while nested finally still guarantees other owned
cleanup. None of these observations changes production, old assertions or deadlines.

Build03 raw log confirms Passed10s,10executed/84UP-TO-DATE; current lint XML0errors/
11warnings/1hint. Independently recomputed frozen01/current/archive74 source files,
old72/29 historical helpers, runner/baseline/frozen JSON and debug/test/release match.
Install01 record binds exact debug1f42/test96c8ea99 to the frozen candidate and exact
owner digest before/after; releaseefd3 unchanged. No fresh JVM/browser run inferred.
At this preparation checkpoint root found phone asleep/locked; no owner-controlled
instrumentation outcome exists. Fresh readiness/preflight is required before runtime.
Evidence: external `owner-screen-off-clipboard-frozen-01.json`, `candidate-01/`,
`install-01.json`, `owner-screen-off-build-03.log` and original `screen-off-clipboard-`
run/owner-observation03 files. Original Failed01/02/03 remain retained; selected
screen-off/clipboard acceptance and broader Mobile status remain Pending/Partial.

## Owner-controlled screen-off Run01 — independent actual acceptance

Independent raw/JSON audit confirms **Passed1/1,121.836s**,selected[1,0],terminal[-1],
anchoredOK1,native0,0failures/errors/skips,no watchdog/orchestration error. Actual
policy-bound Copy was observed atcopy+96ms; natural stop/noninteractive hold marker
atcopy+2414ms, with cleared session and not-resumed Activity. After sampled35s, the
final-off wake marker atoff+35101ms/copy+37513ms still reports noninteractive/locked.
Actual legal focused return atcopy+64908ms is still locked with no old Dish/Copy,
no original primary clipboard and exact cache/jars. All phases retain PID19125.
ElapsedOff62496ms at completion is time since the off observation including return;
it does not establish a continuous62.496s screen-off interval or an exact Power time.

Read exact owner observation01: “Đã Copy và tắt màn hình” before wake instruction,
then “Đã bật, mở khóa và trở lại app” after the native wake checkpoint. These agree
with executed phases without substituting owner receipt time for physical-key time.
Recomputed current/frozen/archive74, prior72 and29 helper identities, both APKs,
frozen/install/run bindings match. Post-audit records exact owner894f73…a4d6,
protected208/unrelated23/release preservation and UUID directory absent. Candidate
test96c8ea99/debug1f42 retains the reviewed source; build03 raw10s10/84 and lint0/11/1
are compilation evidence, not fresh JVM/browser tests. No production repair occurred.

Accept selected same-Activity/process authenticated Copy→sampled>=35s screen-off→
legal locked return and clipboard absence on Xiaomi/API36. Original automatic-Copy
Failed01/02/03 remain Failed. Natural LMK/task restoration, authenticated recreation/
process death, cross-app replacement/history, exact background30s and long-background
UX remain Pending; Google cancellation01 stays NotExercised and Mobile stays Partial.
Evidence: external `owner-screen-off-clipboard-` prefix for `run-01.{log,json}`,
`owner-observation-01.json`, `frozen-01.json`, `install-01.json` and `candidate-01/`.

## Real Chrome clipboard replacement — bounded preparation review

Reviewed separate CrossAppClipboardPhysicalTest160/PendingClipboardClaim28/
ChromeClipboardAction86 and169-line selected runner. Existing OwnerCopyObservation
is unchanged. Actual empty focused editable non-password Chrome field alone receives
the public UUID marker through remote SET_TEXT/selection/COPY actions; target code
never writes the replacement with ClipboardManager. Fixed read-only package metadata
requires one Chrome UID distinct from process/target/instrumentation UIDs. Fresh
Chrome root/window and refreshed exact-field guards apply immediately before/after
Copy; bounded5s refresh waits do not repeat mutations. No window dump, account text
or clipboard payload is logged. These are observed action/source-app checks, not
authenticated per-clip writer-UID metadata.

Own stopped adapter must retain original pending label/value/generation/deadline
before remote Copy and until allowed return after original deadline+1s. No offscreen
clipboard read, clock reset or manual reconcile. Remote Copy may occur after expiry;
the proof does not establish old Dish system-primary status while offscreen. Early
return/recreation fails. Legal locked return must preserve the exact new label/text,
forget stale ownership without another generation and retain replacement after1s.
Cleanup cannot erase Chrome's replacement; listener errors still propagate while
other owned cleanup is guaranteed. No production or old assertion change required.

Build02 raw Passed9s,10executed/84UP-TO-DATE; current lint XML0errors/11warnings/1hint.
Independently recomputed current/frozen01/archive77, prior74 and30 old helpers,
runner/baseline/frozen JSON and APKs match. Candidate testb86829cd/debug1f42 and
releaseefd3 bind to reviewed sources. Runner AST Passed and requires exact selected
[1,0]/terminal[-1]/anchoredOK1/final success phase/native0/no failure diagnostics,
900s watchdog plus owner/frozen/UUID cleanup audit gates. This checkpoint is compile/
review/freeze preparation only; no install/runtime or fresh JVM/browser result is
inferred. Actual Chrome replacement remains Pending and Mobile remains Partial.
Evidence: external `cross-app-clipboard-` prefix for `frozen-01.json`, `candidate-01/`
and `build-02.log`; earlier Passed/Failed/NotExercised evidence stays.

## Actual Chrome replacement Run01 — independent Failed evidence audit

Independent raw/JSON read confirms **Failed1/1,354.918s**,selected[1,-2],terminal[-1],
FAILURES,native0,no watchdog/orchestration error. Genuine owner Copy was observed
atcopy+20ms; Chrome UID10184 differs from the own UIDs. Atcopy+180073ms the external
action wait failed without a completed Chrome Copy or return marker. SamePID29864
was unresumed/locked with session absent at failure. Recorded post-audit preserves
owner894f73…a4d6, prior74/helper30/protected208/unrelated23 and UUID directory absent.
No replacement preservation or production defect is established by this timeout.

Owner reported a visually empty focused omnibox/visible keyboard. Frozen predicates
cannot distinguish rejected root/focus/field/empty-text/action bits, so that statement
does not identify the cause. Source research found a missed harness compatibility
hypothesis: [AOSP TextView](https://android.googlesource.com/platform/frameworks/base/+/e0938b8/core/java/android/widget/TextView.java)
exposes hint state through accessibility and adds SET_SELECTION for nonempty text.
Requiring empty reported text plus an initial selection action may reject an empty
editor. This is source-supported inference, not actual-device causation or permission
to modify the frozen fixture/assertions. Run01 remains Failed and evidence is retained.

Reviewed plan for a separate read-only focused-editor probe: no Activity launch,
remote mutation or clipboard read; one bounded5s sample of known predicate/hint/
action booleans only, no unknown strings/tree/payload output. Compile/review/freeze
precedes selected execution; probe Passed would mean observations completed only.
Actual cross-app replacement remains Pending, Mobile Partial. Evidence: external
`cross-app-clipboard-run-01.{log,json}` and unchanged frozen01/candidate01.

## Read-only Chrome editor probe02 — independent sampled evidence

Independent raw/JSON read confirms **Sampled0.048s**,selected[1,0],terminal[-1],
anchoredOK1,native0,no orchestration error; replacement acceptance remains Pending.
Exactly16 fixed booleans were emitted by the no-Activity/no-mutation/no-clipboard
probe. Chrome root/node, input focus, same window, editable/enabled/visible/focused,
SET_TEXT and SET_SELECTION were true. Password, textNull, textEmpty, showingHint
and COPY were false. Thus accessibility reported nonempty text at this one sample;
the text itself was neither exported nor retained in diagnostics. Owner's visually
empty description does not make that reported text empty.

This sample does not support missing SET_SELECTION or shown-hint as its rejection
reason. The earlier AOSP hypothesis remains source context, not device causation.
The observed nonempty predicate would reject the frozen helper at this sample, but
does not reconstruct every state during Run01's180s wait. No fixture/assertion was
changed and no completed remote Copy/return was accepted. Root requested an explicit
owner manual select-all/delete without Copy before any new-number read-only probe.

Final recorded audit Passed for owner894f73…a4d6, source78/prior77,30 old helpers,
protected208/unrelated23, frozen/local/installed test5d976536/debug1f42 and runner.
Frozen/candidate01 stays preserved; revised numbered runner/frozen02 checks exact
phase/16 boolean schema and post identities, with60s native bound and captured timeout
output/native-unknown status. There is no explicit internal5s execution guarantee.
Evidence: external `chrome-editor-probe-run-02.{log,json}`, `frozen-02.json` and
`candidate-02/` under the `chrome-editor-probe-` prefix. Mobile remains Partial;
original cross-app Run01 stays Failed and all earlier scoped verdicts remain intact.

Read-only probe03, after owner explicitly selected/deleted the omnibox without Copy,
independently confirms **Sampled0.056s**,[1,0]/[-1]/OK1,native0,no orchestration error,
final identity audit Passed. Current Chrome/window/focus/editable/enabled/visible/
SET_TEXT were true; reported text was nonnull/nonempty, showingHint=true and
SET_SELECTION/COPY=false. No actual text/hint/clipboard value was exported. Owner
digest/prior77/source78/30helpers/protected208/unrelated23 remain recorded exact.
The sampled state supports a hint-aware empty-editor compatibility branch; it does
not establish Run01's full history or accept a remote Copy. Probe02's earlier
non-hint/nonempty/selection-present state remains a distinct observation.

Planned separate variant preserves all78 old sources/assertions and accepts initial
null/empty text or a showing-hint field whose reported text equals its nonempty hint.
It must still require exact public-marker/non-hint state and selection support after
SET_TEXT, then the original full-selection/actual COPY/window/UID/pending ownership/
legal-return assertions. This follows the sampled capabilities and documented
TextView behavior; no old assertion or production deadline is relaxed. Review/build/
freeze/install precede runtime. Original Run01 stays Failed, actual replacement
Pending. Evidence: external `chrome-editor-probe-run-03.{log,json}` and frozen03.

## Hint-aware replacement variant — bounded preparation audit

Independent static comparison confirms the new160-line test is byte-identical to
the original after three class/method/helper substitutions; every original assertion
remains. New88-line helper accepts null/empty text or showing-hint plus exact text/
nonempty-hint equality. It requires SET_TEXT initially, then exact populated marker,
not-showing-hint and actual SET_SELECTION before selecting. Fresh Chrome root/window/
field/UID/full-selection/COPY guards remain, with no permission or clipboard injection.
This is a separate capability-aware harness, not a repair of the original frozen
Run01 or production. Both diagnostic samples remain observations only.

Reviewed169-line selected runner and independently recomputed current/frozen01/
archive80, original78 and32 historical helper identities. Runner/baseline/frozen
JSON and both APKs/archive match. Build-audit records Passed10s,10executed/84UP-TO-DATE,
lint0errors/11warnings/1hint; no fresh JVM/browser execution. Install01 record binds
debug1f42/test2e7c5edf to that frozen candidate and exact owner894f73…a4d6 before/
after each package check. Protected208/unrelated23 preservation remains recorded.
No actual variant runtime result exists at this preparation checkpoint; fresh
owner-ready preflight/execution is still required. Original Run01 remains Failed,
probe02/03 Sampled, cross-app replacement Pending and Mobile Partial. Evidence:
external `chrome-editor-replacement-` prefix for `baseline-01.json`, `frozen-01.json`,
`candidate-01/`, `build-audit-01.json` and `install-01.json`.

## Hint-aware actual Run01 — independent post-return Failed audit

Independent raw/JSON read confirms **Failed1/1,124.584s**,selected[1,-2],terminal[-1],
FAILURES,native0,no watchdog/orchestration error. Actual remote Chrome Copy action
was observed atcopy+2808ms, after UID10184/window/field/selection guards; owner return
was requested at31069ms after the original deadline margin. SamePID7249 returned
focused/resumed with session/Dish cleared, then failed at66090ms under the generic
return phase. No completed replacement/return-preservation acceptance is earned.
Recorded owner894f73…a4d6, prior78/helper32/protected208/unrelated23 and exact UUID
cleanup remain preserved. Original cross-app Run01/probe02/03 outcomes stay intact.

The generic phase covers synchronization, cache/RAM, deadline, clipboard exactness,
ownership forgetting, generation, stability and UI absence; final state flags do
not identify which assertion failed. Successful remote action alone is not proof
of exact returned clipboard bytes. No production defect or root cause is established.
Plan a separate two-file diagnostic variant retaining every old assertion and all80
frozen files, adding phase checkpoints and own-RAM ownership booleans. Any clipboard
comparison remains inside its original legal foreground guard, exporting only fixed
booleans with no raw labels/text or offscreen read. No reconciliation/reset/injection
is permitted. Review/build/freeze precede runtime; replacement acceptance stays
Pending and Mobile Partial. Evidence: external `chrome-editor-replacement-run-01`
raw log/JSON and original frozen01/candidate01, retained without overwriting.

## Post-return diagnostic variant — independent preparation audit

Reviewed separate181-line diagnostic test,30-line own-RAM observation helper and
169-line selected runner. Extracted all9 original assertTrue calls: byte-identical,
same order; ownership/generation and UI-absence checks remain. Added return phases
localize cache/deadline/clipboard/forget/generation/stability/UI failures. Ownership
reflection exports only7 booleans; clipboard exports only4 booleans inside the
original main-thread legal-foreground guard. No reconcile, clipboard replacement,
permission/deadline or production change. Other cleanup stays guarded and owned.

Independently recomputed frozen01/current/archive82, original80 and33 historical
helpers, runner/baseline/frozen JSON and both APKs match. Install01 binds exact
debug1f42/testec93b9c0 and unchanged owner894f73…a4d6 before/after package checks.
Build-audit records Passed8s,10executed/84UP-TO-DATE,lint0errors/11warnings/1hint;
no fresh JVM/browser result. Runtime acceptance remains Pending at this preparation
checkpoint; compile/install or active genuine setup cannot earn replacement credit.
Original methods' failures and diagnostic probes remain retained. Evidence: external
`cross-app-return-diagnostics-` prefix for `baseline-01.json`, `frozen-01.json`,
`candidate-01/`, `build-audit-01.json` and `install-01.json`. Mobile stays Partial.

## Post-return diagnostic Run01 — independent early Failed audit

Independent raw/JSON read confirms **Failed1/1,48.998s**,selected[1,-2],terminal[-1],
FAILURES,native0,no watchdog/orchestration error. Own Copy observed atcopy+26ms;
failure at2980ms remained focused/resumed with ready session/Dish. No remote Chrome
Copy, return checkpoint or new ownership/clipboard-return diagnostic was reached,
so this run cannot diagnose the preceding post-return failure or accept replacement.

Read exact UUID/frozen-bound owner observation01: “Có bấm Copy Dish hơn một lần”.
That confirmed input does not satisfy the intended single-copy generation protocol;
the generic failure still does not independently identify its exact assertion. No
production defect or deadline repair follows. Recorded owner894f73…a4d6, prior80/
helper33/protected208/unrelated23 and exact UUID cleanup remain preserved. Keep
Run01/candidate01 and owner observation intact; any same-code one-Copy retry uses
fresh numbering/preflight with original guards unchanged. Evidence: external
`cross-app-return-diagnostics-` prefix for `run-01.{log,json}` and
`owner-observation-01.json`. Actual replacement/return acceptance remains Pending,
earlier failures remain Failed and Mobile remains Partial.

## Post-return diagnostic Run02 — localized ownership failure

Independent raw/JSON review confirms **Failed1/1,105.461s**,selected[1,-2],terminal[-1],
native0,no watchdog/orchestration error. SamePID13830; actual Chrome Copy atcopy+3769ms,
return requested31022ms. Exact cache/session and deadline checks passed. Legal
focused clipboard sample51677ms reported clipPresent/oneItem/labelDiffers/markerExact
all true. At51679ms `return-claim-forgotten-check-failed` localized the failing
boundary: adapterForeground/generationSame/deadlineOriginal true, valueAbsent/
labelAbsent/deadlineZero false, labelStillOriginal true. External public replacement
survived that sample; stale own-RAM claim failed, later stability/UI checks not reached.
No replacement-loss claim or full acceptance follows. All prior failures stay Failed.

Recorded owner894f73…a4d6, old80/helper33/protected208/unrelated23 and exact UUID
cleanup passed. Sanitized access02 contains three own-package ClipboardService
not-in-focus denials within this run, no clip text/raw unknown log. This supports
unavailable reads around foreground handoff; exact callback ordering remains an
inference. Evidence: external `cross-app-return-diagnostics-run-02.{log,json}`,
`frozen-02.json` and `clipboard-access-02.json` under the same diagnostics prefix.

Reviewed proposed two-path production repair: actual window focus drives adapter
foreground; expired null/exception keeps claim and schedules a separate immediate
retry plus at most ten100ms retries. Callback checks foreground/original generation/
expiry; cancellation on focus loss, successful new copy or successful forget/clear.
Failed reads do not restart budget or duplicate queue; original deadline/token and
30s cleanup scheduling remain unchanged. Source diff71/82 lines has no review
blocker. No offscreen read, blind clear or test/manual reconcile is introduced.
Compile/freeze/install and unchanged strict diagnostic runtime still require new
evidence. Preserve original208 manifest and old34 helpers; new audits must explicitly
allow only exact two before/after production SHA deltas and bind changed APKs.
Mobile remains Partial and repair acceptance Pending.

Repair preparation source/host review: current82 sources differ from baseline only
the two allowed paths; all other80, old34 helpers and non-document tracked files
verify. Read-only support audit confirms206 original protected files unchanged plus
two exact intentional deltas, unrelated23 unchanged, original manifest/archive and
historical candidates retained. New runner170/support67 parse; delta JSON binds
before/current SHA and old/new release. Strict unchanged selected diagnostic,
native/class/result/final-phase and owned UUID cleanup gates remain. No blocker.

Read raw build01 and audit: Passed28s,28executed/136UP-TO-DATE; app JVM10 tests,
zero failures/errors/skips; lint0errors/11warnings/1hint. Debugd90cf0e3 and release
153b0224 changed as expected; testec93b9c0 unchanged. Existing releaseefd3 archive
still matches pre-repair baseline. This is source/compile evidence only; no fresh
physical repair acceptance is claimed before frozen/install/runtime evidence.

Independent repair frozen/install preparation audit: recomputed frozen01 SHA
f377224c…1398cb3; all82 current/archive sources, all APKs, baseline/frozen/runner,
new support/delta/regression inputs and old34 helpers match. Source identity is
80 unchanged plus the two exact authorized production deltas. Install01 records
debugd90cf0e3/testec93b9c0 exact and owner894f73…a4d6 unchanged before/after both
package checks. Release153b0224 is bound to the frozen archive; no release install
is claimed. Reviewed regression runner selects only unchanged adapter3/recreation1,
requires per-method[1,0]/terminal[-1]/exact OK count and owner/installed/source/
proof-directory preservation. It is frozen as a repair input. Compile/install and
active genuine setup earn no runtime credit: selected repair and relevant regression
outcomes remain Pending at this checkpoint. No ADB/Gradle used by this review.

## Clipboard repair Run01 — genuine setup Failed before clipboard exercise

Independent raw/JSON review confirms **Failed1/1,182.282s**,selected[1,-2],terminal[-1],
FAILURES,native0,no watchdog/orchestration error. UUID588b4cb1…f167,samePID17606;
only `owner-replacement-setup-biometric` then its Failed checkpoint appeared, copy
elapsed=-1. No Cook/Copy, remote Chrome action or repaired return assertion ran.
Final ready=false with busy=false/focus=true localizes only unmet genuine setup;
it does not establish a Keystore cause or a failure of the clipboard repair. Prior
accepted Keystore results stay accepted; all historical Failed runs stay Failed.

Recorded frozenf377224c/APKs unchanged, owner894f73…a4d6 exact, all80 unchanged
sources plus two intentional production deltas,34 old helpers,206 protected plus
two deltas,23 unrelated files and owned UUID cleanup passed. Relevant adapter/
recreation results must be recorded separately; they cannot replace the unexercised
real cross-app return boundary. Evidence: external `clipboard-return-repair-run-01`
raw log/JSON and frozen01/candidate01. Repair acceptance Pending; Mobile Partial.

## Repair adapter regression01 — independent bounded Passed audit

Independent raw/JSON review confirms **Passed3/3,99.636s**, each exact expected
OwnedClipboardPhysicalTest method[1,0],terminal[-1],anchored OK3,native0,zero failures/
errors/skips,no watchdog/orchestration error. On the repaired production candidate,
unchanged tests verify synthetic owned Dish cleanup after actual30s, synthetic
replacement survival and newer Dish surviving the old deadline then clearing at
its own deadline. Scope is actual foreground locked proof Activity and adapter;
replacement is test-written synthetic data, not a distinct Chrome UID or genuine
authenticated Cook/return flow. It cannot accept the unexercised real cross-app repair.

Before/after audits bind frozenf377224c, installed/local/APK identities,80 unchanged
sources plus exact two production deltas,34 old helpers,206 protected plus two
deltas,23 unrelated, owner894f73…a4d6 and original UUID proof-directory set. Evidence:
external `clipboard-return-repair-regression-adapter-01.{log,json}`. Recreation1
still needs its separate result; strict repair01 stays setup Failed, real return
acceptance Pending and Mobile Partial. No test assertions/source changed.

## Repair recreation regression01 — independent bounded Passed audit

Independent raw/JSON review confirms **Passed1/1,31.548s**, exact unchanged
`realActivityRecreationPreservesOriginalClipboardDeadline` method[1,0],terminal[-1],
anchored OK1,native0,zero failures/errors/skips,no watchdog/orchestration error.
Scope is actual locked Activity recreation, retained synthetic owned Dish and
cleanup at its original30s deadline. It does not prove authenticated session
restoration, process death/natural LMK, Chrome replacement or the repaired return.

Before/after audit again binds frozenf377224c/current/installed APKs, owner894f73…a4d6,
80 unchanged sources plus the two exact production deltas,34 old helpers,206
protected plus two deltas,23 unrelated and unchanged UUID proof-directory set.
Evidence: external `clipboard-return-repair-regression-recreation-01.{log,json}`.
Relevant adapter3/recreation1 now total4 Passed without assertion changes; repair
strict01 remains setup Failed and genuine cross-app repair acceptance Pending.

## 20October recovery — independent bounded preparation audit

The old `/tmp/passchef-cookbook-device-acceptance/` tree is unavailable after reboot.
Historical recorded results remain reports; their old raw logs/frozen candidates/
208-file manifests/34-helper identities cannot now be independently re-certified.
October16 repairrun02 was launched (session23025,UUID prefix5c68d856), terminal
unknown with raw evidence missing. The earlier October15 “not launched” checkpoint
is historical. No Passed/Failed result or reconstructed old evidence is assigned.

Reviewed new persistent `recovery-support.py`82lines and `recovery-runner.py`135lines:
strict same selected method/result/final-phase gates; bounded terminate/kill client
cleanup; missing terminal/final audit yields Unknown/Pending. No source/assertion,
clipboard permission/write, production deadline or manual reconcile change. Before
freeze, independently compared82 Mobile raw files against mainfcee101,328 canonical
Git blobs and all3 expected APK hashes. Raw favicon differs only by existing Git-
normalized CRLF checkout; it is clean and preserved without altering its bytes.

Fresh persistent root is
`/home/vantt/.local/share/passchef-mobile-evidence/2026-10-20-clipboard-return/`.
Recomputed frozen01 SHA6019a855…55a258; local/archive82,2 helpers,3 APKs and frozen
JSON match,328 raw checkout hashes retained alongside canonical blob identities.
Read device-check01: selected authorized transport1,API36,awake/keyguard absent,
owner894f73…a4d6 exact, installed debugd90cf0e3/testec93b9c0 exact. Release153b0224
bound locally/archived, no release installation claimed. No rebuild/install needed.
Fresh owner readiness and selected recovery-run01 terminal/post-audit evidence remain
required; preparation earns no runtime credit. Cross-app repair Pending, Mobile Partial.
No ADB/Gradle performed by this independent review; unrelated WIP preserved.

## 20October recovery-run01 — independent return failure audit

Read persistent raw log/JSON: **Failed1/1,145.299s**,selected[1,-2],terminal[-1],native0,
no watchdog/orchestration error. UUID8f7807ee…49a8,PID27371; genuine setup/Cook reached
own Copy+19ms, remote Chrome Copy+24439ms, return requested+31115ms. Focused cache/
session and original-deadline checks passed. Legal clipboard sample+103201ms reports
present/single-item/different-label/exact-marker all true; next claim assertion failed
at+103203ms. Own-RAM sample still had original generation/deadline/label/value with
foreground=true. This is a sampled stale claim with external replacement intact;
the later stability/UI checks were not reached. Repair acceptance remains Pending.

Final fresh audit passed82 current/archive sources,328 raw checkout identities,
helpers/APKs/installed identities/owner894f73…a4d6 and exact owned UUID absence.
Source permits a timing hypothesis: delayed100ms natural retries may run after strict
immediate assertions, while finally closes the Activity and cancels pending retries.
Neither retry state nor focus-to-sample timing was captured, so this does not establish
indefinite production failure or an incorrect original assertion. Preserve Failed01.
Plan a separate additive failure-tail diagnostic with original assertions intact:
immediate and>=1s own-state observations before cleanup, guarded public-marker reads,
then rethrow the captured failure even if reconciliation finishes naturally. No manual
reconcile, wait-for-success or changed deadlines; review/build/freeze precede runtime.
Earlier lost temporary evidence remains unavailable, not reconstructed. Mobile Partial.

## Failure-tail diagnostic — independent preparation audit

Reviewed additive185-line test/87-line observer. Normalizing only class/method names
and four added catch lines yields byte-exact original181-line test; original82 files
and all assertions/order/throw remain unchanged. Failure stage is captured/reported
before observation; tail errors are contained and unchanged failure throw remains.
No delayed sample can turn the captured failure into Passed. Helper samples own RAM
on main, sleeps150ms then1000ms on instrumentation thread, samples RAM before final
app/Activity-manager clipboard reads. Each read has a fresh legal foreground guard;
only booleans, bounded counters and monotonic times exported, no reconcile/mutation.
Missing sample indices mean incomplete diagnosis, not inferred policy state.

Reviewed host support103/runner135: old recovery frozen6019/source82/candidate/helpers/
APKs and raw/result/owner/denialmetadata hashes bound without modifying prior evidence.
New baseline requires precisely the two reviewed additions. Independent frozen audit
SHA4e13c725…fd78779 matches current/archive84,helpers,APKs and328 checkout identities;
old82 raw exact, plus2 additions, not84 committed files. Build-audit records Passed76s,
10executed/84UP-TO-DATE,lint0errors/11warnings/1hint; no fresh JVM result claimed.

Read install01: native0/Success,test-only update ec93→de93be29; installed debugd90cf0e3
unchanged, release153b0224 locally archived, owner894f73…a4d6 exact before/after.
Nativeafter transport1/API36/awake/keyguard clear, all final local/prior checks pass.
Evidence root `~/.local/share/passchef-mobile-evidence/2026-10-20-failure-tail/`
holds frozen/candidate/build/install01. Fresh owner readiness and actual selected
runtime still required; no failure-tail observation or repaired acceptance claimed
at this preparation checkpoint. Production unchanged, Mobile Partial. No ADB/Gradle
used by this independent review; unrelated WIP and missing historical limits retained.

## Failure-tail01 — independent sampled persistence audit

Persistent raw/JSON confirms **Failed1/1,156.066s**,selected[1,-2],terminal[-1],native0,
no watchdog/orchestration error. UUID47610d32…16c8,PID10684; Chrome Copy+4141ms,
return requested+31058ms. Exact public-marker clipboard check passed+58080ms;
original `return-claim-forgotten-check` failed+58083ms and remained Failed after tail.
All three diagnostic indices present: sample0+58084ms retryAttempts3/queuedtrue;
sample1+58235ms4/true; sample2+59237ms11/false. Each kept original RAM label/value/
deadline/generation, legalforeground=true. No natural ownership forget occurred
within that1153ms sampled interval; attempts exhausted. This does not prove indefinite
persistence or identify which read/exception caused retries.

After final RAM sampling, both stored adapter and Activity ClipboardManager reads
succeeded legally with single exact public marker/different label. External marker
survived; test failure remains ownership forgetting. Before/after84=prior82+2/328
checkout/frozen/APKs/installed/owner894f73…a4d6 and UUID cleanup passed. Read bound
owner observation: approximate white/close/toast report, wording/timestamps uncertain.
Bound device metadata has two own-package not-in-focus denials and no matched
own crash-buffer records; empty buffer does not prove no crash. Existing finally
closes proof Activity, so visible close alone cannot establish a production crash.

Concrete next hypothesis: original direct platform-label `.toString()` may fail for
a null external label; current samples do not include labelNull/exception identity.
Prefer one additive read-only locked public-marker probe plus existing bytecode
inspection before any repair. Preserve original84/assertions; no permission/write/
reconcile. Only observed null+NPE evidence justifies minimal null-safe non-owner
comparison, followed by unchanged strict return acceptance. No arbitrary retry
extension or delayed-success credit. Repaired return Pending, Mobile Partial.

## Actual null-label probe01 — independent Sampled cause gate

Persistent raw/JSON confirms **Sampled1.198s**,exact selected[1,0],terminal[-1],OK1,
native0,no watchdog/orchestration error. UUIDdb9456e5…7010,PID15609; all legal secure
locked foreground booleans true, exact public prior UUID47610d32…16c8 marker required
before classification. Actual labelNull=true,directExpressionSucceeded=false,
exceptionIsNpe=true,nullsafeLabelNull=true. No auth/Cook/Copy or clipboard write.
Final source85/prior84/328checkout/input/archive/APKs/native owner894f73…a4d6 and
owned UUID cleanup passed. Frozen SHA d63cc25c…bad2efd; prior bytecode audit binds
debugd90/source2ecfd184 and actual classes8 getLabel→Object.toString without null guard.

Observed actual null+NPE and matching production bytecode establish the concrete
null-label defect and justify only null-safe non-owner comparison. They do not
retroactively prove every historical failure's cause or grant repaired acceptance.
Plan: one production path `.label?.toString() != label`, leaving deadline/token/
retry/focus unchanged; one additive real30.5s same-UID null-label replacement
regression requiring intact marker plus forgotten own claim/same generation, no
manual reconcile/time injection. All old85 sources preserved except explicitly
bound production delta, all old assertions exact. Review/build/freeze/install and
new regression+unchanged4 adapter/recreation+strict actual Chrome return remain
required. Probe success stays Sampled; production repair acceptance Pending.

## Null-label repair — independent frozen/install preparation audit

Production diff is precisely `.label.toString()`→`.label?.toString()` in the non-owner
comparison; prior84 other sources exact. New129-line regression uses actual null-label
same-UID public replacement and fixed real30.5s expiry, requiring intact marker and
forgotten claim/deadline0/same generation/no retry, no manual reconcile. This is locked
adapter coverage; actual Chrome return still separately required. Existing assertions
unchanged. Reviewed support93/runner147 exact six selected profiles and final native/
JUnit/phase/source/archive/owner/directory gates; only strict can grant acceptance.

Recomputed frozen SHA1e916985…4dfee38f; local/archive86=prior84 exact plus one reviewed
production delta and one additive regression,327 canonical files unchanged plus that
delta, raw checkout bytes preserved. Prior source85/probe actual null/bytecode/raw/
owner evidence bindings match; old missing temporary history is not re-certified.
Build audit Passed45s,31executed/133UP-TO-DATE,JVM10/0failures/errors/skips,lint0/11/1.
Install01 records debug37579b45/testd0536563 native0/Success and owner894f73…a4d6 exact
before/after each; release92fa8896 locally archived, not installed. Nativeafter API36/
awake/keyguard clear; all local checks passed. No physical success claimed at prep.

Install snapshot includes six pre-existing proof directories, retained untouched.
Earlier recorded UUID absence is a point-in-time observation; later presence cannot
be explained by those checks and no continuous absence is claimed. New run gates
preserve baseline directory set plus their own UUID absence. Relevant5 regressions
and unchanged strict Chrome return outcomes remain Pending at this checkpoint.
No ADB/Gradle performed by this independent review; unrelated WIP preserved.

## Null-label repair — independent selected regression results

Read new persistent raw/JSON individually: null-label regression Passed31.832s,
untouched owned Dish31.534s, synthetic replacement31.544s, newer Dish36.633s.
Each exact selected method[1,0],terminal[-1],anchored OK1,native0,zero failures/errors/
skips,no watchdog/orchestration error; null regression reports its required final
success phase for matching UUID. Actual original30.5s Handler cleanup now forgets
own metadata/deadline while preserving same-UID null-label public replacement and
generation. Existing3 adapter cases preserve their earlier boundaries/assertions.
These are locked proof adapter results, not authenticated Cook/Chrome return.

Each final audit binds frozen1e916985/current/archive86,327 canonical unchanged plus
the exact null-safe delta,prior evidence/helpers/APKs/installed/owner894f73…a4d6,
unchanged pre-existing proof-directory set and that run's UUID absence at observation.
Independent recreation raw/JSON also Passed31.552s,exact selected method[1,0]/[-1]/
OK1,native0,zero failures/errors/skips,no watchdog/error; same final preservation
gates and baseline proof-directory set passed. Scope is locked same-process actual
Activity recreation/original deadline, not authenticated restoration/process death.
All5 relevant method runs now Passed; each non-strict acceptance field remains
Pending as intended. Strict actual Chrome return is still unlaunched/Pending at
this checkpoint and requires fresh immediate owner readiness.

## Null-label repair strict01 — independent narrow clipboard acceptance

Read actual raw/JSON: **Passed1/1,99.870s**,exact unchanged selected[1,0],terminal[-1],
anchored OK1,native0,zero failures/errors/skips,no watchdog/orchestration error and
`cross-app-replacement-passed`. UUID54a18fa3…cae4,samePID23063,ChromeUID10184. Genuine
one Copy+23ms,real Chrome Copy+3644ms,return requested+31036ms. Focused return cache/
session/deadline checks passed; own ownership sample value/label absent,deadline0,
same generation,adapterforeground=true. Exact public replacement legal reads+56973/
+57990ms both passed, then unchanged single-Copy/stability/UI-absence assertions and
success+58015ms. Selected expired-claim forgetting with preserved Chrome replacement
is accepted on this candidate; no authenticated recreation/process-death/history or
authenticated per-clip writer UID proof follows.

Final audit binds frozen1e916985/current/archive86,327 canonical unchanged plus
one exact production delta,helpers/APKs/installed/owner894f73…a4d6 and preserved
pre-existing directory set plus own UUID absence at observation. Prior raw/probe/
bytecode evidence remains bound; earlier Failed and October16 unknown/lost evidence
unchanged. The five relevant physical regressions and fresh appJVM10 also Passed.

Read exact bound owner reply: “Không quay lại được hoặc app đóng”. It does not confirm
stable visible return or locate close timing. Native return/marker checks passed
before existing finally closes the proof Activity; that cleanup is a possible
explanation, not established cause. Selected-PID crash-buffer fatal metadata empty,
which cannot prove no crash. Keep owner-visible close/return UX unresolved pending
separate normal MainActivity observation and owner clarification. Native selected
clipboard acceptance and unresolved UX are recorded separately; Mobile Partial.

## Normal MainActivity follow-up — independent current observation

Read normal01/02 and bound owner data. Normal01 launcher opened only ordinary
MainActivity,PID24514,no instrumentation/auth/clipboard read/write. Its four false
resumed/focus gates used selectors missing Android16 topResumedActivity/current
window focus; preserve those records as unavailable detector evidence, not a proved
app close. Corrected normal02 did not relaunch; four samples at0.314/5.338/10.346/
20.376s report samePID24514,resumed/focused/awake/keyguard-clear all true. Before/
after owner894f73…a4d6,installed/source/archive/prior bindings/proof-directory set
remain intact. Four samples do not prove continuous pixels or general crash absence.

Verified owner observation binds normal02 and strict prior reply hashes. Exact later
answer: “Đang thấy Cookbook, app không tự đóng”. This confirms current ordinary
Cookbook visibility; earlier “Không quay lại được hoặc app đóng” remains unchanged.
No retroactive owner confirmation of strict proof return or exact cause of the old
close follows. Narrow strict clipboard acceptance stands; current normal app stable
observation/owner confirmation is separate. Historical close cause and broader
Mobile acceptance remain open. Evidence: new repair root `normal-main-return-`
`observation-01/02.json` and `normal-main-owner-observation-01.json`.

## Google continuation —20October persistent preparation review

Independent code reviewer audited new host support/runner and frozen03/current/
archive86 sources,360 protected files, helper syntax, three APKs and prior repair
freeze binding. Candidate01 remains internally consistent and untouched. Corrected
incomplete selected JUnit/native-exit/summary classification to Unknown before final
freeze. Final review-audit-03 reports no unresolved preparation blocker.

Separate tester-audit-03 confirms11 HOST-only AST result-parser cases Passed and
the same frozen identities; root build Passed2s3executed/91UP-TO-DATE, existing lint
0errors/11warnings/1hint. Neither agent used ADB/Gradle or ran device acceptance.
Root read-only check confirms exact installed debug/test and owner digest; no install,
instrumentation or biometric wait. [Scope/evidence](google-cancellation-preparation.md#persistent-continuation-20october2026).

Unresolved: available external Google resolution/fresh owner readiness, actual
cancellation/stale reply/different-Menu/401/403. Mobile stays Partial; historical
NotExercised/Failed/Unknown results and old missing-raw-evidence limits retained.

## Persistent Google run01 —21October outcome review

Independent reviewer read raw/result/owner evidence and four scoped documentation
additions: selected Failed197.803s,[1,-2]/terminal[-1],native0,no watchdog/error.
Actual external Google window/pending request/natural stop were observed, sensitive
state cleared, but no consumed reply or returned-own-foreground gate before180s.
Post owner/source/APK/UUID audits match; no production defect or cancellation/stale
callback acceptance established. Current Chrome focus cannot reconstruct earlier
navigation. Clarify the owner-visible screen before another runtime.

Later root-only read confirms current PassChef foreground with owner clarification;
this is separate from the independently reviewed failed runtime. No new test launched.
Unresolved: earlier owner-visible screen/navigation, actual cancellation/returned
stale callback and broader provider matrix. Mobile remains Partial.

## Persistent Google run02 — independent failure/recovery review

Reviewer confirms Failed197.504s,[1,-2]/[-1],native0,no watchdog/error; actual external
Google/pending/natural stop, cleared sensitive state and no completed returned reply.
Owner/source/APK/UUID preservation Passed. Owner's white-spinner report coincides
with a later post-terminal Google-services focus sample; it does not identify an
internal Google/network/PassChef cause. Actual Android Back exits that window, then
ordinary MainActivity launch restores PassChef focus with owner data/audits preserved.
Recovery is separate from the failed runtime and adds no cancellation/stale-return
acceptance or rendered/authenticated UI proof. No production edit or new test run.

Unresolved: spinner/navigation cause and actual in-run cancellation/returned callback,
exact resultCode and remaining provider matrix. Mobile Partial.

## In-run host Android Back — preparation review

Reviewer audited two new host helpers,107/137lines; source86/APKs/unchanged selected
native fixture retained. Exact live UUID marker/start/failure/terminal/locked-state
gates precede two fresh Google focus checks, awake/keyguard and live-client guards,
then one real Android Back. Exclusive intent/result records, native command outcome
and original strict fixture/post-audit verdicts gate acceptance. No injected result,
grant/account change or authentication bypass. Owner cancellation/resultCode remain
unaccepted;18 HOST-only action-gate/classifier/mocked-command checks Passed. No
ADB/Gradle/provider runtime by reviewer, no blocking source finding. Fresh owner
readiness and actual device outcome remain necessary; buffered unseen terminal is
not absolutely excluded by stream observation. Earlier Failed results retained.

Unresolved: actual OS-Back candidate runtime, spinner cause and broader provider
matrix. Mobile Partial.

## Actual host Android Back run01 — independent selected acceptance

Reviewer independently read raw/result/back-intent/back-result/freeze/archive and
post-audits: Passed17.949s,selected[1,0]/terminal[-1]/anchoredOK1,native0,
no failures/errors/skips/watchdog/error. UUIDa11bfbb8…7b82 binds the actual live
checkpoint and two native `FullScreenConsentActivity` focus samples, awake/keyguard
clear, followed by one actual host Android Back exit0. Native unchanged assertions
observe pending consumption, secure locked own foreground, absent session/token/
Dish/lifetime/old Copy and exact synthetic offline cache/jars preservation. Source86/
protected360/helpers/history/APKs/installed/owner digest/own UUID absence/pre-existing
directory audits Passed. No production/test/assertion changes or fresh-consent claim.

Accept selected real host OS-Back/stale external callback after natural lock.
Human hand cancellation remains Pending; exact Android resultCode Unobserved.
No provider-loaded cache/different-Menu/401/403 or whole-Mobile acceptance inferred.
Prior manual Failed01/02 and historical results remain unchanged. Owner close report
is separate: fixture finally closes after assertions; precise visible close cause
unknown. Ordinary launcher follow-up samples samePID/PassChef focus, not rendered or
continuous UI. Evidence: new system-Back root `runtime-review-01.json` and linked
native/owner/normal-return records. Reviewer used no ADB/Gradle.
Later owner confirmed current “Cookbook hoặc màn hình mở khóa bình thường”; the
separate owner record binds normal-return01 and earlier close observation. This
does not establish the exact earlier close cause or continuous rendering.

Unresolved: earlier close timing, human cancellation/resultCode,
permitted real401/403 and broader callback/device matrix. Mobile Partial.


## Actual HTTP403 preparation —21October2026

[Selected candidate preparation](google-http403-preparation.md) adds a separate UUID-owned genuine
biometric/production-provider attempt with synthetic offline cache preservation.
Final compile/JVM app10+bridge22/lint0errors and independent source/host review
Passed;43 host classifier cases do not certify Google. No new runtime/install;
denied owner-authorized test Menu URL and fresh readiness remain Pending.
Prior failures and selected system-Back acceptance retained; Mobile Partial.
