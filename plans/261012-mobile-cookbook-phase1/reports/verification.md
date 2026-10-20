# Independent Cookbook verification

Status: **Local code verification passed**; selected device acceptance Passed,
broader owner/device matrix Pending. Historical local snapshot below retained.
20October [Google persistent preparation](google-cancellation-preparation.md#persistent-continuation-20october2026):
unchanged source86/APKs, build Passed2s3/91,lint0/11/1; independent host review and11
parser checks Passed. Read-only installed/owner-digest checks match. New frozen03
prepared; no new Android/JVM/provider acceptance. External resolution remains Pending.
Latest [Google cancellation preparation](google-cancellation-preparation.md) adds a
reviewed selective external-resolution/natural-stop/stale-reply fixture. Final
compile/lint Passed26s,lint0/11/1; frozen70/test669a8c9c installed-r with exact owner
digest preserved. Old68 source/manifests, production/assertions and debug/release
unchanged. No fresh JVM/browser; initially asleep, then owner-ready selected Run01
NotExercised15.608s,[1,-4]/[-1]/OK1,1skip: SDK returned metadata without an observed
external resolution. Exact cache/jars/owner/UUID cleanup preserved; cancellation/
stale/401/403 acceptance remains Pending.
Selected [mapping/tag UI Run01](menu-mapping-runtime.md) subsequently Passed1/1,
36.892s,[1,0]/[-1]/OK1,0failures/errors/skips. Explicit duplicate/missing-role rejection,
irregular-row remapping and unchanged tagged Recipe under wrong saved Menu accepted;
exact cache/jars/owner/cleanup preserved. Source71/old70/helpers27/test3e5df260,
build02Passed12s/lint0/11/1; production/debug/release and previous assertions unchanged.
Later [screen-off/clipboard01/02](screen-off-clipboard-runtime.md) Failed37.492s/54.316s
at copy+25s stop/noninteractive precondition; no hold/return acceptance. Same source/
APKs across both; final build9s/lint0/11/1/source72/test181368be. Exact owner/old71/
helper28/protected208/23/UUID cleanup retained; no production fix or inferred key timing.
Later unchanged screen-off03 Failed105.248s at copy+25035ms; fresh transport31/
awake/unlocked checks and genuine setup/Cook/Copy, but no stop/noninteractive gate.
Late owner Power reply retained separately; source72/APKs/owner/UUID cleanup unchanged.
No build/install or new screen-off/clipboard acceptance. Separate owner-Copy variant
preparation addresses host-message timing while retaining all earlier failures.
New [owner-Copy screen-off01](owner-screen-off-clipboard-runtime.md) subsequently
Passed1/1,121.836s,[1,0]/[-1]/OK1,0failures/errors/skips. Real owner Copy bound to
production deadline/generation; stop/noninteractive at copy+2414ms, still off/locked
at off+35101ms, legal focused return at copy+64908ms with clipboard absent and exact
cache/jars/owner preserved. SameActivity/PID19125,source74/old72/helpers29/ownedcleanup
accepted. Final build10s10/84/lint0/11/1,test96c8ea99; old72/debug/release/assertions
unchanged. Sampled35s offscreen plus legal-return scope only; no precise background30s,
LMK/recreation/cross-app/history credit. Original Failed01–03 remain; Mobile Partial.
New [Chrome return diagnostics](cross-app-clipboard-runtime.md) observed an intact
different-UID Chrome marker but stale expired own-claim metadata on legal return.
Two reviewed production focus/retry changes retain every prior assertion and the
original30s deadline. Fresh app JVM10 Passed; build28s/lint0/11/1. Unchanged physical
adapter3/3 Passed99.636s and recreation1/1 Passed31.548s on the repair candidate.
Strict repair01 Failed182.282s at setup with two observed wrap code15 events, before
Cook/Copy; cause unknown. October16 run02 was launched, terminal unknown after loss
of old temporary evidence. October20 recovery01/failure-tail01 Failed with intact
actual Chrome marker and stale claim, including11 exhausted retries. Read-only
actual-marker probe01 Sampled1.198s confirmed a null label and original-expression
NPE. [Minimal null-safe repair](clipboard-null-label-repair-runtime.md) Passed45s
build/fresh appJVM10/lint0/11/1 and all five relevant physical profiles Passed.
Unchanged strict Chrome return01 subsequently Passed99.870s: marker preserved,
ownership forgotten, original1s stability/UI/session/cache/preservation checks all
Passed. Owner reported proof close/failed return, then confirmed normal app stable;
read-only normal02 sampled samePID/resumed/focused at0/5/10/20s with owner intact.
Both replies retained; exact prior close cause unknown. Mobile Partial.
[Resume checkpoint](clipboard-return-handoff.md).
Current [session timer preparation](session-timer-runtime.md): test-APK/lint builds
42s/13s/11s Passed, lint0/11/1; no fresh JVM execution. Idle Run01/02 Failed before
baseline pointer and Run03/04 Failed at setup; Run05 actual5-minute idle Passed1/1,
341.722s. Total Run01/02/03 setup Failed182.309s/182.279s/182.138s retained; Total04
on14October Passed1/1,609.969s,runner[1,0]/OK1,0 failures/errors/skips. Actual original
600000ms deadline locked despite real minute1–9 touches/recooking; jars/owner digest
unchanged, no source/build/install change. Selected5/10-minute API36 foreground proof
KEEP_SCREEN_ON accepted. Total02 code15 wrap/other earlier failure causes unknown;
broader screen-off/cold-start/process-death/timer UX Pending.
Date:2026-10-11 UTC. Reviewer agent A is independent of product author B.

## Final results

| Check | Result |
| --- | --- |
| Shared JVM | 74 tests: original56 + builder5 + independent13; zero failures/errors/skips |
| Canonical Sheets through Android bridge JVM | 16 tests: original9 + new7; zero failures/errors/skips |
| Production app coordinator/range JVM | 4 tests: deferred IO/cache/auth busy boundaries and quoted/grid-bounded ranges; zero failures/errors/skips |
| Standalone Sheets JVM21 | 29 tests: original22 + new7; zero failures/errors/skips |
| Root packaging/lint | Exit0;87s;179 tasks executed; shared/bridge AAR/JAR, debug/test/release APKs built |
| Lint | 0 errors,11 warnings,1 hint |
| Final source/build stability | 99 paths byte/mode unchanged before/after; all51 builder frozen source hashes match |
| Protected baseline | 75 original tests/fixtures/recipe/crypto/vault paths unchanged |
| Extension/unrelated preservation | 128 extension paths byte/mode unchanged;23 Cave/skills-lock hashes unchanged |
| New Kotlin size | 27 new files, all<200 lines; max193 independent regression file |

Shared74 was independently rerun before the final app/client delta and remains valid:
all shared sources/tests stayed unchanged. Bridge/app and standalone were rerun after
the actual streaming-client repair. These separate roots reuse common protocol tests;
the counts are not a claim of distinct test cases across all roots.

## Actual commands and environment

Root JDK17:`/home/vantt/dev-tools/jdk-17.0.20+8`; SDK36:
`/home/vantt/dev-tools/android-sdk`.

```sh
./gradlew --no-daemon :shared:jvmTest :sheets-android:jvmTest --rerun-tasks
```

Initial independent shared run exit0;38s;11 tasks executed. Its bridge15 result is
superseded by the final bridge16 rerun after adding non-EOF streaming regression.

```sh
./gradlew --no-daemon :shared:assemble :sheets-android:assemble :sheets-android:jvmTest :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:assembleRelease :app:lintDebug --rerun-tasks
```

Final root exit0;87s;179 tasks executed. XML counts and lint XML independently parsed.

Standalone JDK21.0.12:`/home/vantt/projects/passMan/.toolchain/jdk`;
working directory:`mobile/sheets-client`.

```sh
./gradlew --no-daemon build test --rerun-tasks
```

Final standalone exit0;21s;8 tasks executed,29 tests. Original loopback/configuration
and portable read/RAW append/update regressions pass. No real smoke tool invoked.

Evidence outside repository:
`/tmp/passchef-mobile-independent-jvm.json` and `.log`,
`/tmp/passchef-mobile-independent-final.json` and `.log`,
`/tmp/passchef-mobile-independent-standalone-final.json` and `.log`.
Earlier standalone28/bridge15 artifacts describe intermediate source only.

## Actual APKs and release isolation

| APK | Package | Bytes | SHA256 |
| --- | --- | ---: | --- |
| Debug | dev.passchef.mobile.demo | 18416447 | 32454b5fc7e3a72ab75a2663604f6d822bdbbccdb95ff8b20e1510f32eb0f739 |
| Instrumentation | dev.passchef.mobile.demo.test | 1146149 | d475460cd54a1a68823a1469a3819a1c5a948a8394993d5796c6db5c53d6927c |
| Unsigned release | dev.passchef.mobile | 14176301 | 84f788db2f0fbc56c39dfc1bf7622130134bb9a5388770f6730ec7e4dcc3b820 |

Actual manifest/DEX checks confirm release contains no BiometricProofActivity,
CookbookProofActivity, PassChefProof log tag or desktop loopback/smoke classes.
Release diagnostics helper remains a no-op that does not invoke the key inspector.
Debug proof activities remain unexported. Debug and release disable backups.
Release permissions:INTERNET,USE_BIOMETRIC,USE_FINGERPRINT and the app's generated
DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION; no camera/Autofill/Accessibility permission.
New production Cookbook/worker source has no print/log or token persistence path.

Lint warnings concern existing target/plugin/dependency versions and missing app icon;
the hint concerns boxed Compose state. They are recorded, not silently treated as zero.
Final independent debug/test APKs differ from prior builder packages; use the hashes
above for future device acceptance. Unsigned release matches the builder hash.

## Proof limits and verdict

HTTP mocks prove protocol behavior, ownership/close and non-EOF byte-budget refusal.
The injected timeout case proves exception propagation, not a real20second deadline.
JVM cache tests exercise actual private filesystem failure/corruption; app tests use
actual production coordinator with deferred IO. They are not Google consent proof.
Android UI test APK compiles a genuine owner-authenticated, storage-isolated harness;
**it was not executed**. No phone/emulator, OAuth grant, real provider read, clipboard,
biometric, reveal timer or lifecycle/UX physical job ran in this verification.
Owner configuration/physical acceptance remains Pending; mobile Phase1 is Partial.
Local checks do not imply Android CI coverage. No unresolved P1/P2 found in code review.

Extension [recheck](../../reports/261012-extension-recheck.md):147 units and25 E2E
passed on unchanged extension source. It grants no mobile live-provider proof.

## Continuation after main13cf9d1

[Unattended regression](unattended-regression.md) records fresh shared78/app4,
packaging/lint and additional diagnostic compilation. Those APKs were not installed
in that earlier snapshot; [later runtime evidence](ui-runtime-continuation.md) records
their data-preserving installation and locked diagnostic1/1 pass. [Device acceptance](device-acceptance.md) supersedes the historical
not-executed UI status above: actual Run01 failed after genuine fingerprint;
Run02/03 failed, Run04 passed selected foreground flow. Normal-screen-policy
comparison subsequently Passed Run05 1/1,53.663s after verified reconnect/unlock.
Remaining Google/phone proof and historical Run01 cause stay open.
No production fix claimed.

[Clipboard adapter runtime](clipboard-runtime.md),2026-10-13: exact prepared test APK
installed with data preservation; actualAPI36 Passed3/3,99.926s,0 skips/failures.
Real foreground untouched30s cleanup and replacement/newer ownership proven;
no authenticated Cook/Google/background/recreation/API26 fallback proof added.

Oct13 continuation: [clipboard Activity recreation](clipboard-recreation-runtime.md)
Passed1/1,31.721s on Xiaomi/API36. Actual old Activity destruction/new identity and
original29s retention/30.5s clearance asserted, with locked unique proof storage.
Final test-APK/lint Passed33s;0 errors/11 warnings/1 hint. This is same-process
adapter proof; authenticated recreation/background/process death remain Pending.

Oct13 owner reports Android OAuth client configured. New isolated real-Google
acceptance fixture reviewed/compiled: test APK/lint Passed59s,0 errors/11 warnings/
1 hint. [Preparation evidence](google-acceptance-preparation.md) separates new local
APK from the installed recreation candidate. Google device runtime0/Pending: dedicated
test Menu URL and ready owner awaited; no grant/read or new installation attempted.

Later [Google runtime](google-runtime.md): prepared5eb335 test APK installed with
owner digest preserved. Actual Google Run01 Failed1/1,44.766s at metadata; owner
confirmed genuine account/consent and absent expected Test tab, then renamed it.
Unchanged Run02 Failed1/1,19.259s at values with actual draft present. Usable
authorization/provider metadata/values and selected Test tab observed; fixed row/
origin/schema assertion not yet resolved, mapping/cache unaccepted. No production
repair inferred; forthcoming diagnostics retain every assertion.

Schema diagnostics subsequently reviewed/compiled Passed59s,0/11/1 lint. New11
boolean flags/fine phases retain all17 original assertions byte-identical; new
candidate cc4d5bbc is local only. Owner reports only A1/A2 populated; correct the
dedicated fixture and confirm readiness before another authentication/runtime.

After owner updated the fixture, exact refined candidatecc4d5bbc installed with data
preserved; **Google Run03 Passed1/1,23.309s**,0 errors/failures/skips. All11 match flags
true and original17 assertions pass actual metadata/tab/rows/explicit mapping/cache
file load plus unchanged fake jars. Owner digest unchanged after install/runtime.
Independent reviewer confirmed JUnit [1,0], diagnostic2 separate, exact source/APK
and scope. Google writes/errors/UI cache reopening remain unproven.

Latest [Google cache UI continuation](google-cache-ui-runtime.md): initial88s build
Passed fresh app4; shared78/bridge16/standalone29 retained, not freshly rerun. Corrected
helper sync build40s Passed, lint0/11/1; production unchanged. Default proof-storage
recreation Passed1/1,31.769s. Cache UI Run01 Failed1/1,294.651s; corrected Run02
Passed1/1,42.393s with exact provider cache Cook/UI/favorite/recent/search and genuine
biometric same-process authenticated recreation. All assertions retained; exact
source/APK/install/digest histories in linked report. No whole-mobile completion.

Latest [network-off acceptance](network-off-runtime.md): initial55s build and153-line
diagnostic helper81s build Passed, lint0/11/1; app4/shared78/bridge16/standalone29
retained (no fresh JVM execution). Exact diagnostic source/APKs unchanged across
Run02/03/04; no repeat build/install for Run03/04. Run01/02/03 Failed history retained;
Run04 Passed1/1,31.477s,0 failures/errors/skips with actual network absence, retained
provider NETWORK_FAILED preservation and all unchanged offline cache UI/recreation
assertions. Original17/cache150 untouched, owner digest unchanged; mobile Partial.

## Security/UI continuation,14October

[Exact evidence](security-ui-runtime.md): root-only test APK/lint builds01–06 Passed;
final06 42s,10 executed/84 UP-TO-DATE, lint0 errors/11 warnings/1 hint.
Shared78/bridge16/standalone29/app4 and extension147/25 retained, no fresh executions
or summed independent counts. Production/old tests unchanged; no passed cache/network/
timers replayed. Six new Android test/helper files remain<200lines.

Actual locked pixels01 Passed1/1,2.321s and authenticated02 Passed1/1,25.890s:
selected EN/VI setup/manual Cook and secure shell display exclusion. Each EN/VI
bitmap1220×2712 has sampled own1194×2395/all2,859,630 pixels opaque black; authenticated
proof verifies real visible/revealed Dish and unchanged jars. Original Locked01/
Capture01/Auth01 Failed retained; null UiAutomation is not capture acceptance.
Recents01 Failed206.052s post-checkpoint; Recents02 Failed182.457s at setup before any
navigation command. Recents03 Passed1/1,137.789s, JUnit[1,0]/terminal[-1]/OK1,
0 failures/errors/skips, watchdog=false. Actual pause1/stop1, stopped-state locked read,
return cleared/sessionAbsent and genuine biometric reopening; jars/digest unchanged.
Owner separately confirmed the own card blank/covered. OS shortcut visuals/full
accessibility/layout remain Pending; no rebuild/install for this third attempt.

At security/Recents runtime debugfa4c4ab4…/testea84ea13… match frozen04/all49 sources; accepted
pixels/auth runtime uses separately preserved teste3142923…/frozen03. Owner894f73…a4d6,
old43/protected208/unrelated23 preserved. No production repair or full-mobile completion.

## Offline cold-process continuation,14October

[Exact evidence](cold-process-runtime.md): warm02 InterruptedByVerifiedSIGKILL;
sole PID25682/UID10422/start identity and owned hashes rechecked before signal,
actual old-proc absence/pidofempty observed before fresh launch. Cold02 Passed1/1,
5.993s,JUnit[1,0]/terminal[-1]/OK1,0 failures/errors/skips,watchdog=false. NewPID25991
starts locked/cleared, then genuine biometric reload/exact offline cache/origin/Recipe/
username00123/favorites/recent/search/Cook and unchanged encrypted jars; owned cleanup
and owner digest intact. Provider-loaded cache/LMK/saved-task restoration Pending.

Compile01 Failed62s missing import,02 Failed39s rule type; import/type corrected only.
Build03 Passed54s,13 executed/146 UP-TO-DATE,debug+test+release/lint0/11/1; release
manifest/threeDEX omit proof. At offline runtime debug1f42a2c4…/test3004a0bb… equal
frozen03/all54. Warm01 Failed native-query orchestration before SIGKILL retained:
window-windows lacked focus property, displays returns exact own focus; strict
predicate unchanged, no rebuild/reinstall. Earlier candidate01/02 identities retained.
Historical49 now48 unchanged+one unexported debug manifest entry; old43 now42+delta.
Production/all prior assertions/protected208/unrelated23 unchanged; no fresh JVM/browser.

## Provider cold-process continuation — Run02 Passed

[Actual evidence](google-cold-runtime.md): four additional androidTest-only files,
all54 prior app sources/manifests unchanged, original17 provider predicates retained.
Review restored one omitted own-foreground conjunct in the new reconnect assertion
before runtime. Build01 Passed45s12/99 preserved; corrected02 Passed50s9/102,
lint0 errors/11 warnings/1 hint. Frozen02/source58/candidate02 archive binds exact debug
1f42a2c4…/test9b04c212… and six native helpers; target APK retained/test installed-r,
owner digest preserved; candidate01/test724d remains separately archived, never run.
Release APK remains exactlyefd3… with prior proof exclusion.
Warm01 InterruptedByVerifiedSIGKILL/PID5410, cold01 Failed1/1,181.588s at reopening;
initial locked state/exact disk persistence proven but no post-unlock cache/Cook.
Cause unknown, owner report ambiguous. Diagnostics whitelist/read-only active/click
marker added to two new files only; build03 Passed58s10/101/test3711/frozen03, still58
sources/all54 prior files unchanged. Warm02 verifiedSIGKILL/PID13092, separate cold02
Passed1/1,8.886s/PID14408,JUnit[1,0]/terminal[-1]/OK1/no watchdog/error: real fingerprint,
exact beforeinteraction provider cache/origin/Recipe/00123/search/favorites/recent/Cook
and jars/owned cleanup/digest preserved. Scope app-disconnected provider cache with
observed network available; no production repair/LMK/task-restore/401/403/cancel credit.

## Vietnamese OS field association — selected Run01 Passed

[Actual evidence](vi-accessibility-runtime.md): one new134-line test, all58 prior
sources unchanged; root build01 Passed55s9/102,lint0/11/1,source59/testb684 installed-r.
Run01 Passed1/1,1.500s,JUnit[1,0]/[-1]/OK1/no watchdog/error. Actual fieldID10 exactly1
visible/editable/password OS node; localized label in descendants, not the node itself.
First observer-cache-cleared attempt,38 nodes/3 descendants,32/32 own-tree extra requests.
No auth/Cook/Google/capture replay. Original same-node checker remains Failed; current
VI association does not prove previous optionalID0 cause or TalkBack spoken behavior.
Owner/protected208/unrelated23/old58/releaseefd3 preserved; whole mobile Partial.

## Native language touch — Partial

[Exact evidence](language-touch-runtime.md): edge01 Failed1/1,1.408s, one pair with
all point guards; center02 Passed1/1,1.852s [1,0]/[-1]/OK1/no watchdog/error, two
60ms center pairs with exact both-locale UI changes. Same frozen02/test84dd edge02
Failed1/1,1.211s [1,-2]/[-1]: English remains after one pair, foreground/readsuccess.
Raw58.153×40dp/public58.153×48dp, actualpoint outside raw/inside public bounds;
center differs in point+dwell, no edge/cause credit. Original all assertions remain.
Source60/old59/protected208/23/digest preserved; only the new language file evolves.
Build01/02/03/04 Passed51s/48s/52s/43s; latest9 executed/102 UP-TO-DATE,lint0/11/1.
Build03 old metadata-name candidate never run; corrected04/testce61 optionaledge60
wrapper compiled/reviewed. Initial install attempt14October aborted before package
checks, adbdevices empty/no runtime then. Reconnected15October transport26 with fresh
Awake/keyguardAbsent/digest; exactce61 installed-r and retaineddebug1f42. Dwell04
Failed1/1,1.709s [1,-2]/[-1], one configured60ms pair/all pointguards, stillEnglish.
Same-candidate center04 Passed1/1,1.802s [1,0]/[-1]/OK1, two60ms pairs/bothUI switches.
No watchdog/error; fresh owner894f73…a4d6/old59/protected208/23/source60/helpers8
preserved. Both actual raw verdicts independently reviewed; no cause/full-target claim.
Releaseefd3 unchanged/no fresh JVM/browser for these unchanged-source runtimes.
Later geometry01 Passed1.505s/source61/helper9/testc0fd after build55s10/101/lint0/11/1:
target layout excludes midpoint, both enclosing layouts contain it, observed scroll
coverage0. New paired explicit-tool finger01 Passed2.464s/source63/helper10/test9a64,
build29s12/99/lint0/11/1: UNKNOWN retained headline/Master/toggle; FINGER switched all
three both directions at equal guarded midpoint/configured60ms. Four pairs/eight
events with actual constructorUnknown/checked public non-tool equality/tool readback.
Strict raw verdicts and exact archived identities independently reviewed; owner digest/
old61/protected208/23 preserved. Accept new FINGER samples only; old failures, historical
event-readback and whole-target limits remain. See exact report for audit-stage identities.

## Manual hardware shortcut checkpoint — Partial

[Exact evidence](manual-shortcut-runtime.md): one new149-line isolated checkpoint,
all63 prior app sources/assertions and ten helpers retained. Build01 Passed17s10/101;
review fixed a stop-failure history latch, corrected02 Passed14s9/102/lint0/11/1.
Publisher-only selected-start/abort/failure gates fixed before runtime; frozen03/04
source64/helper12/test19b9 exact, debug1f42 retained/releaseefd3 unchanged. Early
candidate01/02 never installed/run; actual install03-r preserves owner digest.
Run03 Failed1/1,182.485s [1,-2]/[-1], setup never ready; safe ownPID15980 preterminal
wrap-doFinal/level2/code15/systemtrue/transienttrue/authRequiredfalse/retry2 observed,
underlying cause/owner action unknown. Actual shortcut NotExercised in03.
Same-source/APK04 reached genuine round-trip/Cook/secure masked-ready but Failed
1/1,236.235s [1,-2]/[-1], owner-response completion absent at elapsed180429ms.
First/lastReveal=-1/focusloss=-1/stops0; passive observation cannot prove no physical
attempt. No owner answer or completion signal fabricated; hardware shortcut Pending.
Both no watchdog/orchestration error, exact owned directories absent afterward,
owner894f73…a4d6/source64/helper12/protected208/23 preserved. No production fix inferred.
Late04 reply could not see Reveal, exact viewing phase unconfirmed. Same-candidate05
with clarified bilingual button/location question reached genuine secure masked-ready;
Failed1/1,188.013s [1,-2]/[-1]/FAILURES/no watchdog/error at completion wait180421ms.
No reveal/focusloss/stop sampled or confirmed screenshot; no fabricated signal.
Final exact UUID cleanup/digest/source64/helper12/installed19b9/releaseefd3 preserved.
Owner's current screen/labels requested before another timed run; no active runtime.

Unresolved: original Cookbook UI Run01 reveal failure, network Run03 key-wrap cause;
cancellation/stale callbacks/401/403/LMK/task restoration/TalkBack and remaining phone matrix.

### Run06 continuation — owner refusal; visual Dish still Pending

Same frozen06/source64/helper12/debug1f42/test19b9 reached genuine secure Cook. Owner
reports actual Power+VolumeDown refusal and visible dots unchanged after Reveal.
Model first/last reveal64979/86974ms is neither rendered text nor continuous22s proof.
Actual Failed1/1,193.721s [1,-2]/[-1], completion absent180483ms after publisher ADB
failure; runner native0/watchdogfalse/errornull do not erase that separate failure.
Final native audit06 preserves owner/exact UUID cleanup/source64/helper12/protected208/
23/releaseefd3. Isolated old-protocol probe01 observes hardlink Permission denied;
live06 stderr was discarded and is not recovered. Replacement cooperative lock/rename
protocol passed6isolated cases; only2host helper deltas, no APK rebuild for those.
A normal Compose synchronization gap is a supported harness hypothesis, exact06cause
unproven. [Scope and artifacts](manual-shortcut-runtime.md); mobile stays Partial.

### Diagnostic Run07 — Passed checkpoint and separate owner acceptance

New normally synchronized variant/source66, old64 unchanged. Build21s12/82/lint0/11/1;
debug1f42 retained/test0d4c installed-r with owner preserved. Actual **Passed1/1,
116.736s**, [1,0]/[-1]/OK1, zero failures/errors/skips/native0/no watchdog/error.
Owner confirms Dish text then blocked Power+VolumeDown. Actual tagged text matches
synthetic/controller reveal at88388ms, full own-content/window/IME/secure-foreground
flags true; masked98382ms. Samples do not establish exact key timing/continuous pixels.
Live completion publication native0/stdoutpublished/empty stderr at receipt age108.339s;
owner reply saved first. Exact UUID cleanup/owner/source66/old64/helper12/protected208/
unrelated23/releaseefd3 final audit passed. Selected owner hardware refusal accepted,
JUnit checkpoint-only. New synchronization is a material harness difference, consistent
with but not proof of06cause. Whole mobile Partial; all original failures retained.
[Full scope](manual-shortcut-runtime.md).

## Unattended source coverage —15October

[Exact scope and artifacts](unattended-source-coverage.md): six new app coordinator
cases +six canonical streamed-request cancellation cases, existing code/assertions
unchanged. Fresh app10/bridge22/independent standalone35, zero failures/errors/skips;
root9s10/57 and standalone20s4/1, lint0errors/11warnings/1hint. Bridge22 also runs
inside standalone35; do not sum repeated common cases. No shared/browser rerun.

Real temporary cache files prove commit readiness/ownership, schema reconfirmation,
invalid mapping/oversized input/IO failure preservation and recovery. Synthetic HTTP
streams prove public client cancellation/body release for metadata/values×2xx/401/403,
not live Google/SDK acceptance. No cancellation defect reproduced or production fix.
All old66 app sources/existing tracked code/Golden/protected208/unrelated23 and all
three APK hashes unchanged. Existing release manifest/3DEX/backup source audit Passed
at its bounded scope, not runtime log absence. No ADB/device/contact/install occurred.
Updated current plan/spec summaries; full mobile Partial and historical failures retained.
