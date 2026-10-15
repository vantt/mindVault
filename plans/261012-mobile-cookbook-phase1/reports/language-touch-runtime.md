# Language control native touch —14October2026

Status15October: **Partial — selected finger01/center02/04 Passed; legacy edge01/02/dwell04 Failed**.
Whole mobile Partial. Original raw48dp layout checker remains Failed/unchanged;
neither public expanded bounds nor a center pass certifies the whole touch rectangle.

One new bounded locked-UI fixture uses its own CookbookProofActivity/UUID directory,
no authentication/secret input/Google/cache/Cook/capture replay. Actual secure locked,
awake/unlocked/focused own window required. Fresh placed semantics/main-thread decor
offsets map root→window→screen; no coordinates or raw trees/text are logged.
Only booleans, DP×1000 dimensions and native event counts recorded. Actual
Instrumentation.sendPointerSync DOWN/UP required; no performClick fallback.

## Actual evidence

Edge01 **Failed1/1,1.408s**, raw JUnit[1,-2]/terminal[-1]/FAILURES, no watchdog/error.
One DOWN/UP returned; ten guards all true: finite/placed, declared touch≥48dp,
point strictly inside expanded bounds/outside raw layout, clear vertical-edge midpoint,
own window/visible frame/insets/IME-hidden. Measured raw58.153×40dp, public touch
58.153×48dp. No locale-switch marker after the first pair; generic catch cannot
identify the later failed assertion. Does not establish undersized-target/root cause.
Frozen01/source60/testa084 preserved exactly.

Center02 **Passed1/1,1.852s**, raw JUnit[1,0]/terminal[-1]/anchoredOK1,0 failures/
errors/skips, no watchdog/error. Two guarded native pairs at fresh centers with
ordinary60ms configured physical dwell; EN→VI→EN headline/Master/opposite-toggle
assertions and post-input foreground/read booleans passed. Source60/frozen02/test84dd.
This changes both point and dwell relative to edge01; no edge/cause credit.

Edge02 **Failed1/1,1.211s** on that same frozen02/test84dd, raw[1,-2]/[-1]/FAILURES,
no watchdog/error. One native pair, same raw/public touch sizes/all point guards true.
Added read-only post-input diagnostics: actualEnglish=true/Vietnamese=false,
headlineReadSucceeded/ownForeground=true. Original geometry/input/assertions unchanged;
only observer reads added. Failure persists; physical edge acceptance remains Pending.
Original source59/production/protected208/unrelated23 and owner894f73…a4d6 unchanged
before/after both runtimes. No encrypted Cookbook created; finally only owned-dir cleanup.

## Matched dwell candidate — preparation and initial USB interruption

New separate edge-dwell wrapper configures ordinary60ms hold, using the same edge
geometry/assertions. Original edge method defaults0/no added dwell; all assertions
retained. Existing center method still uses its own60ms. Numeric
`OptionalEdgeDwellMillis` records only the optional edge setting; never center duration.
Native selector/completion/log-stem gates distinguish center/edge/edge-dwell explicitly.

Build01 Passed51s10/101; diagnostic/center build02 Passed48s9/102. Build03 Passed52s
9/102 and exact frozen03/testa9da archived before an edge-only metadata-name repair;
no runtime under03. Corrected build04 Passed43s9/102,lint0 errors/11 warnings/1 hint,
frozen04/source60/testce61. Class stays<200lines; only this new language file differs
from earlier60 snapshots, all prior59 remain byte-identical. No new manifest entry/
production change. Releaseefd3 unchanged with prior proof exclusion; JVM/browser
counts retained, not fresh executions. All builds select debug/test/lint only.

| Identity | SHA256 |
|---|---|
| Debug retained | `1f42a2c4b369492b0ed804fab3ec282882e831336cf4ebfec48dbdba05bfd772` |
| Edge01 test | `a084e591d7d5bd2dd6e6924d4ac6d9249acd4d57164a163b3dee8076aae0fc9f` |
| Center02/edge02 test | `84dd72281a1eb92f213145c360d82c77e35521a5547328ecd31807aa8f87a5e6` |
| Candidate03 test; not run | `a9dafa341d015032e1a39fc0b74035128d862050255f3ca2a51c7d79cf862021` |
| Candidate04 test; installed15October | `ce61b888629b3bb1a6343ae2b05de6dcfa642002d9b9ca4590c2a90247a69cf2` |

On14October install04 stopped before package checks because physical USB unavailable;
repeat read-only `adb devices -l` empty. No instrumentation invoked that day.
The device-unavailable record stays historical; later actual execution is separate.

## Reconnected physical continuation —15October2026

Owner reported accepting the USB-debugging prompt. Fresh serialPN7PW8SGXKLVPRIV/
transport26, Awake/keyguardAbsent and owner894f73…a4d6 confirmed before install-r.
Exact frozen04/source60/eight helpers/APKs rechecked; target debug1f42 retained and
testce61 installed with owner digest unchanged. No rebuild or driver/security/radio/
network-ADB change. Initial wrapper run began10:10:49.802Asia/Saigon/03:10:49.802UTC.

Edge-dwell04 **Failed1/1,1.709s**, actual selected wrapper JUnit[1,-2]/terminal[-1]/
FAILURES, native0, no watchdog/orchestration error. Optional edge dwell60 configured;
one DOWN/UP returned, all ten original point guards true, raw58.153×40dp/public
58.153×48dp. After input actualEnglish=true/VI=false, reads/foreground=true;
no switch marker. Two-switch acceptance failed; this is not an accepted edge result.

Center04 **Passed1/1,1.802s** on the same frozen04/testce61/transport26, selected
center method JUnit[1,0]/terminal[-1]/anchoredOK1,0 failures/errors/skips, no watchdog/
orchestration error. Two native pairs with center's own configured60ms dwell changed
EN→VI→EN; headline/Master/opposite-toggle and foreground guards passed. Its numeric
optional-edge setting0 is unrelated to the center's60ms. This supplies a same-candidate/
device/configured-dwell center positive against the sampled-edge negative, not a
production-cause explanation or whole-target acceptance.

Owner digest matches before/after both runs and final fresh native audit; actual
installed debug1f42/testce61, source60/eight helpers/old59/protected208/unrelated23
unchanged. No encrypted Cookbook created. Existing finally cleanup is fixture-owned;
no externally known UUID-directory absence is claimed. Root owns device/builds;
independent reviewer confirmed actual raw verdicts and artifact identities.

## Geometry-only observation — selected01 Passed, no input acceptance

New127-line `language-edge-geometry-physical-test.kt` preserves all60 prior app sources/
manifests/assertions. Planner and independent reviewer checked the same bottom-first
midpoint/ten point guards, main-thread attached/placed layout ancestry bounded32,
merged/unmerged ScrollBy bounds bounded32 and disjoint lineage classification. It
emits only booleans/dimensions/counts, checks unchanged localized headline and secure
locked own foreground before/after. No native pointer/authentication/Recipe/Cook input.
Geometry containment does not expose clipping/hit recipients or establish causality.

Build01 Passed55s,10 executed/101 UP-TO-DATE, debug/test/lint0 errors/11 warnings/1 hint.
Source61/helper9/frozen01/exact candidate01 archived; debug1f42 retained and test
`c0fdd15f9d076610b0b6b5d90f6e9a75a5fd5aa13629e0df0cff890145bce941` installed-r.
Actual run01 **Passed1/1,1.505s**, strict JUnit[1,0]/terminal[-1]/anchoredOK1/
observed=true,0 failures/errors/skips/no watchdog/error. Same bottom midpoint satisfies
all ten guards; raw and target-layout58.153×40dp/public touch height48dp. Target
layout does not contain the sample; both enclosing layouts do (layoutNodes3,
ancestorOutside0). Merged/unmerged scroll counts1 each; covering scroll/ancestor/
disjoint counts all0. Thus observed scroll bounds do not cover this point; no input
reachability, interception or root cause is established by the observation.

Independent raw/identity audit and fresh native final audit retain actual installed
1f42/c0fd, owner894f73…a4d6/source61/helper9/old60/protected208/unrelated23. Releaseefd3
unchanged; no fresh JVM/browser or production changes. External artifacts add
language-geometry build01/runner/frozen01/candidate01/install01/run01/preflight/
immediate/final-audit01, without overwriting physical failures or prior APKs.

## Paired explicit pointer kind — selected finger01 Passed

Two new bounded files, `language-pointer-probe.kt`82 lines and
`language-finger-physical-test.kt`87 lines, preserve all61 existing sources and nine
helpers. Local cached Compose1.11.4 bytecode maps Android tool0 to Unknown and tool1
to Touch for this probe's touchscreen source; a separate mouse/trackpad branch exists.
Its minimum-touch hit branch tests PointerType.Touch. This motivated a
new controlled comparison; it is not retrospective instrumentation of old events.

For each language, fresh equal geometry/ten guards precede UNKNOWN then FINGER at
the exact same expanded midpoint with configured60ms dwell. A simple-obtain baseline
is observed, never injected; full-constructor variants retain pointer id/coordinates/
axes0..63/actions/time ordering/meta/buttons/precision/device/edge/source/flags/raw
coordinates/history and public classification readback, differing in requested tool
kind. Checked public fields are not a claim about every hidden event field; sequential
gestures have distinct actual times. No screenshot/session/production manipulation.

Build01 Passed29s,12 executed/99 UP-TO-DATE, debug/test/lint0 errors/11 warnings/1 hint.
Frozen01/source63/helper10/candidate01 exact; retained debug1f42, installed-r test
`9a649f601fec8cd4fc98af81b6f80ecd866bbbf34ed8802083ccdfb233345108`.
Actual finger01 **Passed1/1,2.464s**, JUnit[1,0]/terminal[-1]/anchoredOK1/finalpassed,
0 failures/errors/skips/no watchdog/error. Four native pairs: UNKNOWN retained
headline/Master/opposite toggle in both languages; FINGER switched all three
EN→VI→EN. Final counters UNKNOWN2DOWN/2UP and FINGER2DOWN/2UP. All four point records
retain ten guards/pairedGeometryUnchanged; all eight events report actual baseline
constructorUnknown/nonToolFieldsMatchLegacy/requestedToolReadback/sourceTouchscreen=true.

Accept only these new FINGER midpoint samples. Fixed UNKNOWN-first/FINGER-second
ordering and checked public fields establish this probe's tool-dependent behavior,
consistent with the cached minimum-touch branch. Old tests remain Failed; their
actual historical tool fields were not recorded. No whole rectangle, other controls,
human finger accuracy or production repair inferred. Earlier UI/setup/wrap causes
are unrelated and remain open. Observed scroll coverage0 does not support the earlier
overlap hypothesis or expose actual hit recipients.

Independent raw/archive/identity review and fresh native final audit confirm owner
894f73…a4d6 and installed1f42/9a64; all63 frozen source paths/ten helper hashes unchanged.
That final audit separately records the newly pending64th manual-shortcut source,
which was not yet compiled/installed then. Artifacts add language-finger build01/
runner/frozen01/candidate01/install01/run01/preflight/immediate/final-audit01.
Backing cached bytecode outputs and hashes are retained separately as
`language-pointer-adapter-bytecode-01.txt`, `language-pointer-coordinator-bytecode-01.txt`
and `language-pointer-bytecode-01.json`;
cached1.11.4 AAR classes.jar matches the inspected transformed jar byte-for-byte.

Outside repo `/tmp/passchef-cookbook-device-acceptance/`: language-edge-build01..04,
frozen01..04/candidate01..04 (source60/exact APKs/eight helper hashes), install01/02,
edge-run01/02 and center-run02 log/result/preflight/immediate, runner and
edge-dwell-04-device-unavailable.json. Later install04, edge-dwell-run04 and center-run04
log/result/preflight/immediate files, plus language-touch-04-final-audit01 retain the
reconnected execution. Failed original binaries/reports never overwritten.

Unresolved: historical event readback/effective whole-target coverage; old raw48dp checker,
spoken TalkBack, actual shortcut/larger-font/orientation/full target matrix. No
production repair or historical-cause resolution inferred from center acceptance.
