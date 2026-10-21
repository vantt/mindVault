# Google external cancellation / stale reply preparation —15October2026

Status: **Actual Run01 NotExercised; cancellation/stale acceptance Pending; Mobile Partial**.
Baseline main `f0913729732517416e30dfa29745fee158dce101`.
No production repair, permission/account change, token/session injection or old
assertion change. All historical Failed verdicts and unknown causes remain.

## Selected executable scope

Two new androidTest files,107/104lines, reuse the existing unexported
`ColdStartProofActivity` and genuine UI/strong-biometric setup. The new method is:

`dev.passchef.mobile.proof.GoogleCancellationPhysicalTest#realExternalGoogleReplyAfterNaturalLockPreservesOfflineCache`

It confirms a UUID-owned synthetic **offline** Menu through the production UI,
favorites/Cooks it and freezes exact encrypted jars/cache plus expected RAM snapshot.
This baseline is not provider-loaded cache. Normal Connect uses AuthorizationClient
and the previously authorized Google test Menu; no SDK/account/grant changes force
a prompt. Source input/navigation already clears the prior Dish through normal UI.

Read-only pending-request identity and own natural-stop callbacks observe a real
external resolution. Google root package must actually be observed, with own stopped
Activity and pending request. Every observed stop must clear session/token/Dish/
lifetime/selected entry/metadata/draft/busy while preserving exact cache/jars.
Main-aware dispatch avoids nested instrumentation-main calls in lifecycle observers.

Only then emit `owner-cancel-google-now`: owner cancels the real Google window and
returns within180seconds. Require real pending consumption, own secure foreground
still locked, no old Dish/Copy, no grant/metadata/draft publication and exact cache/
jars in disk and RAM. This proves selected stale-result rejection if it actually
runs; consumed pending alone cannot establish `RESULT_CANCELED` or token contents.
Owner-observed cancellation is separate evidence, never inferred from JUnit.

Silent grant reuse or a completed no-resolution error is **NotExercised/Pending**
for cancellation. Skip requires no observed request/stop, no pending/busy work and
preserved cache/jars. Dirty stops, unresolved requests and cleanup errors cannot be
converted to a skip. Different-Menu callbacks, provider-loaded cancellation and
actual401/403 require separate acceptance.

## Local verification

| Check | Actual result / limit |
|---|---|
| Initial compile/lint | Passed72s;10executed/84UP-TO-DATE |
| Final compile/lint | Passed26s;10executed/84UP-TO-DATE |
| Lint XML | 0errors/11warnings/1hint |
| Independent source review | Passed after correcting new fixture's nested main dispatch, dirty-stop skip classification and guaranteed cleanup |
| Original app inventory | All68 old source/manifest files unchanged;70 frozen files at this checkpoint |
| Protected/unrelated | Original208/23 hashes match; `.agents/` and `skills-lock.json` preserved |
| Host evidence | All26 old Python helpers unchanged; new selected runner only |
| JVM/browser suites | Not rerun; unchanged production/old assertions retain prior scoped evidence |
| Android selected runtime | Run01 NotExercised15.608s;1skip,0failures/errors; no cancellation/stale/401/403 acceptance added |
| Install-r | Passed; installed debug/test identities match frozen01; owner digest unchanged before/after each package check |

Root command, JDK17/SDK36:

```sh
./gradlew :app:assembleDebugAndroidTest :app:lintDebug
```

Frozen01 artifacts:

| Artifact | SHA256 |
|---|---|
| Debug, unchanged | `1f42a2c4b369492b0ed804fab3ec282882e831336cf4ebfec48dbdba05bfd772` |
| New instrumentation | `669a8c9cdf566132bd2606a870d855ae80360c3b2a0c75f0abd03480a5c8e125` |
| Release, unchanged | `efd3b38644db2404e42faca714c963ef99467c644524833455dff32c11f6b653` |

Freeze binds all70 source/manifests, APKs, new runner and baseline; candidate archive
also retains all26 old helpers. Original frozen07 remains historical and untouched.
Host syntax/audit Passed; compile and static review do not certify phone execution.
The selected runner requires current connected serial, installed APK equality,
awake/keyguard-absent state before launch, own foreground before UI actions and owner
Cookbook digest before/after. It parses selected JUnit codes/terminal/OK separately
from diagnostic markers and fails closed on watchdog/audit/cleanup errors.

## Device checkpoint

Read-only relist on15October found Xiaomi14TPro/API36 serialPN7PW8SGXKLVPRIV,
transport30. Actual screen was asleep/keyguard present; no instrumentation launched
and no wake/unlock command issued. Owner Cookbook SHA256 remains
`894f73e51e316b6a369368ad4cebbc901609ccfe7eabe855aa73c2cbf4e7a4d6`.
Owner readiness and fresh screen verification are required before genuine setup/
Google cancellation. Grant reuse may mean no cancellable window appears.
Reviewed candidate01 was subsequently installed-r: debug APK already matched;
only the test APK needed updating. Actual installed hashes equal frozen01 and all
owner digest observations remain exact. No instrumentation was launched by installation.

## Actual selected Run01 — NotExercised

Owner reported ready/unlocked. Fresh native preflight confirmed awake/keyguard
absent/API36; only the selected method ran with new UUID
`6987dded-9632-4b92-a957-8b18143f0f05`. Genuine biometric setup, offline UI mapping/
favorite/Cook, last-good baseline and normal Connect executed. The SDK returned
usable authorization and real metadata without an observed external window:
metadata/token present, pending false, stops0 and providerObserved=false. No account/
consent/grant changes were made to force a window; fresh consent is not claimed.

Raw verdict: **NotExercised15.608s**, selected JUnit[1,-4],terminal[-1],anchoredOK1,
native0,0failures/errors/1skip,no watchdog/orchestration error. The assumption skip
is intentional absent-resolution classification, never cancellation/stale Passed.
Exact cache/jars and RAM/inventory preserved; prior Dish cleared through normal
source navigation/input. Own directory absent after terminal; owner digest, old68/
protected208/unrelated23/old26helpers/source70/frozen APK identities remained exact.
No external cancellation checkpoint was reached; no owner cancellation answer needed.
401/403/different-Menu callbacks still Pending. Avoid repeating this unchanged flow
without evidence that an external resolution will actually be available.

Evidence outside repo, preserved without overwriting earlier runs:
`/tmp/passchef-cookbook-device-acceptance/google-cancellation-` prefix for
`baseline-01.json`, `build-01.log`, `build-02.log`, `build-audit-01.json`,
`frozen-01.json`, `candidate-01/`, `install-01.json`, `run-01.log`, `run-01.json`,
`run-01-preflight.json`, `run-01-immediate-prelaunch.json` and `runner.py`.

Unresolved: actual external-resolution availability/cancellation/stale runtime,
provider401/403/different-Menu callbacks, remaining lifecycle/clipboard/accessibility
matrix and historical UI/setup/wrap/cold/Run06 causes. Mobile stays Partial.

## Persistent continuation —20October2026

Continue main `e7032996325d967c807875949e3aa79ad715efbe`. Old `/tmp` raw Google
evidence is unavailable after restart; no historical raw result is reconstructed.
Run01 remains NotExercised. All existing source/assertions remain unchanged.

Persistent evidence root:
`/home/vantt/.local/share/passchef-mobile-evidence/2026-10-20-google-cancellation/`.
New `google-support.py`/`google-runner.py` (<200lines each) select only the existing
method above. Final `google-frozen-03.json`/`google-candidate-03/` bind current86
app source/manifests,360 protected checkout files including unrelated WIP, both
helpers, three existing APKs and prior repair freeze SHA2561e916985…4dfee38f.
Frozen03 SHA256 `f85077fd3acde9e514a776f21f1eeb25b16afd794ea0b2ca673908623f77589c`.
Candidate01/frozen01 remain untouched after a host-only classification correction:
nonzero native exit, incomplete selected JUnit codes or missing success summary
now yield Unknown; completed selected failure remains Failed.
Candidate02/frozen02 also remain intact. Final03 permits only documentation commits
descending from the frozen baseline while still checking exact protected/source/APK
bytes; authorized documentation delivery therefore cannot silently change the candidate.

- Build `:app:assembleDebugAndroidTest :app:lintDebug` Passed2s,3executed/91UP-TO-DATE;
  existing lint report0errors/11warnings/1hint. No fresh JVM/browser or Android test.
- Source86 and all APK hashes exactly match the accepted clipboard candidate:
  debug37579b45/testd0536563/release92fa8896. No reinstall or accepted-case replay.
- Read-only device-check01 at16:37:42Asia/Saigon: expected Xiaomi/API36 serial,
  awake/keyguard absent, installed debug/test exact, owner Cookbook SHA256 unchanged
  at894f73…a4d6. Six pre-existing proof directories remain untouched.
- Independent review-audit-03 and tester-audit-03 Passed;11 HOST-only AST parser
  checks include success/skip/wrongUUID/failure/missing-terminal/watchdog/nonzero
  exit/incomplete selected codes/missing summary. These are not provider evidence.

Runner binds selected class/method/UUID, JUnit codes/terminal/OK summary, failure
diagnostics/final phase, source/archive/installed identities, owner digest, own
UUID absence and the pre-existing directory set. New runs use exclusive evidence
files and new numbering. Watchdog detaches only the host client; incomplete native
outcomes remain Unknown and require inspection before another launch. A Passed
stale-return assertion still leaves owner cancellation Pending until actual owner
observation is recorded; exact Android resultCode is unobserved by this fixture.

No new instrumentation or biometric wait launched. Screen readiness is only a
point-in-time observation. Require fresh owner readiness and new evidence of an
available external Google resolution before another attempt. Current production
request has no forced chooser path; unchanged silent grant reuse adds no acceptance.
No account/scope/grant changes, token/session/result injection or production repair.
Any test-account grant withdrawal needs specific owner authorization first; this
preparation authorizes none. Actual401/403 also needs a permitted real denial condition.

Unresolved: external-resolution availability and owner readiness; actual Google
cancellation/stale return, different-Menu callbacks, provider-loaded cancellation
and401/403. Mobile remains Partial.

## Actual persistent Google run01 —21October2026

Status: **Failed1/1,197.803s; cancellation/stale acceptance Pending**.
This new persistent run01 is separate from15October's NotExercised run01.
Owner reported manual grant removal, then immediate readiness; root rechecked
awake/keyguard absent, exact installed/frozen03 identities and owner digest before
launching only the unchanged selected method. No rebuild/install or grant injection.

UUID `cf0dc704-1b76-4c38-8c36-b4701cb1e750`. Genuine setup reached normal Connect,
an actual Google-services window, original pending request and natural Activity stop.
At `owner-cancel-google-now`, observed session/token/Dish/lifetime absent, locked,
metadata/draft absent and all observed stops clean. Owner confirmed seeing Google.
Root requested Cancel, otherwise Back, then return without unlocking Cookbook.

The180s return gate expired with pending=true/providerObserved=true/stopped=true,
own focused/resumed=false, session/token/Dish absent and allStopsClean=true. No
consumed reply, own secure return or final preservation-success phase was observed.
JUnit[1,-2],terminal[-1],native0,watchdogfalse,orchestrationError=null; this is Failed,
not cancellation Passed or NotExercised. No production cause established.

Owner later reported: “nhấn Quay lại thì về web của google, không về app. nhấn lại
vào app thì vẫn show màn hinh google.” Exact observation is saved separately;
physical Back timestamps and exact screen during those actions are unobserved.
Read-only screen01 subsequently found Chrome focused, neither Google services nor
PassChef; accessibility tree unavailable. This current sample cannot reconstruct the
earlier navigation or prove a provider defect. No fresh runtime is launched blindly.

Post-run audits Passed: source86/protected360/helpers/current/archive03/APKs exact,
installed debug/test and owner894f73…a4d6 unchanged, own UUID directory absent and
six pre-existing proof directories preserved. No owner Cookbook content read.
Raw/result/preflight `google-run-01.*`, owner grant-removal/cancellation observations
and `google-current-screen-observation-01.json` remain under the persistent root.

Unresolved: owner-visible Google page/navigation and genuine cancellation return;
stale callback/different-Menu/provider-loaded cancellation/401/403. Mobile Partial.

Later owner clarification: “chỉ là màn hình passchef bình thường, chưa qua google”.
Read-only sample at11:52:29Asia/Saigon confirms PassChef focused, awake/keyguard
absent, Chrome/Google-services not focused; no new instrumentation. Saved as
`google-owner-current-screen-clarification-01.json`. This current observation does
not reconstruct the earlier “Đã thấy Google” or Back action; the relation remains
unresolved. No retroactive cancellation acceptance or blind retry follows.

Unresolved: which screen the owner saw during the earlier Google checkpoint;
actual cancelled-result return and the remaining provider matrix. Mobile Partial.

## Persistent Google run02 — white spinner and post-terminal recovery

21October, same frozen03/source86/APKs/unchanged selected method; no rebuild/install.
UUID `7cf9b172-ed9c-4e8a-a893-74ebbcdce27e`. Fresh owner readiness/native checks
preceded launch. **Failed1/1,197.504s**,selected[1,-2]/terminal[-1],native0,
watchdogfalse,orchestrationError=null. Actual external Google/pending request/natural
stop observed; at the failure checkpoint pending=true, own foreground absent and
session/token/Dish/lifetime/metadata/draft absent, allStopsClean=true. No consumed
callback or successful return gate within the bounded180s wait. Generic failure
wrapper does not preserve an exact underlying assertion/exception; no provider or
production cause is established from the duration/phase alone.

Owner subsequently reported a white screen/spinner. Read-only sample at12:04:13
Asia/Saigon found Google services focused, neither PassChef nor Chrome. Run02 had
already terminated and its UUID cleanup completed. Root issued one real Android
Back key4 at12:04:37 to exit that window, then opened ordinary MainActivity through
the launcher. Read-only recovery at12:05:01 found PassChef focused/Google absent;
owner digest/installed/source/APKs and pre-existing directory set remained exact.
No authentication/Connect or new instrumentation occurred during recovery. App focus
does not prove rendered UI or owner authentication. These post-terminal actions
cannot retroactively accept cancellation or the failed stale-return assertion.

Independent reviewer confirms raw result, cleared-state/cleanup scope and separate
recovery boundary. Current provider/network metadata was sampled only after recovery;
it cannot establish network conditions or Google rendering during Run02. Keep Failed
and the spinner observation without inferring a production repair or account cause.

Evidence in the same persistent root: `google-run-02.log/.json`, its preflight,
`google-run-02-owner-white-screen-01.json`, `google-run-02-system-back-recovery-01.json`,
`google-run-02-normal-app-recovery-01.json` and current-provider-network metadata01.
All final owner/source/APK/UUID and pre-existing directory preservation gates Passed.

Unresolved: white-spinner cause, genuine in-run Android cancellation/consumed stale
reply, exact Android resultCode, different-Menu/provider-loaded cancellation/401/403.
Mobile remains Partial. No unchanged manual retry is launched automatically.

## In-run Android system Back — prepared, not executed

21October persistent root:
`/home/vantt/.local/share/passchef-mobile-evidence/2026-10-21-google-system-back/`.
Two new107/137-line host helpers preserve the unchanged existing selected fixture,
all86 sources/assertions and all APKs. No production repair, rebuild or reinstall.
New baseline main30b1b81; `system-back-frozen-01.json`/candidate01 archive binds
helpers/source86/protected360/APKs and hashes of historical Google frozen03,
failed run02 raw/result plus both post-terminal recovery records. Older roots stay intact.
Independent archive-audit01 Passed; frozen SHA256
`a002d38c57775871867a29613905f3a6773adfc4cc878bf4e8b5467734d4ee1c`.

The runner consumes only the live selected invocation. At the exact matching UUID
`owner-cancel-google-now` marker, require selected-start[1], no observed failure or
terminal, original pending/provider/natural-stop flags and every locked/cleared-state
predicate. Before one actual OS `input keyevent4`, verify Google-services focus twice,
same component, awake/keyguard absent, <=5s freshness and live host client. Persist
exclusive action intent/result with UTC/focus/state/native outcome; never repeat or
fall back to page-link clicks, injected Activity results, grants or authentication.
Actual buffered terminal before the host reads it cannot be categorically excluded;
fresh native gates and final unchanged fixture verdict remain mandatory.

Independent code review found no blocking issue;18 HOST-only gate/classifier/mocked
action checks Passed, including wrong UUID/unclean stop/prior failure/terminal/
duplicate/nonGoogle/locked/dead-process/nonzero-command/incomplete-result cases.
Syntax/size checks Passed. These synthetic host checks provide no provider acceptance.
No new Android build/JVM/lint/runtime claim; prior unchanged candidate build/lint retained.

Any actual success must still satisfy unchanged JUnit[1,0]/terminal[-1]/OK1/final
stale-preservation phase plus exact owner/installed/source/archive/own-UUID and
pre-existing directory audits, and a successfully issued host OS Back command.
Owner cancellation remains Pending and exact Android resultCode Unobserved even
then: host-operated actual OS Back is named separately from a human UI action.
Fresh explicit owner readiness and native screen checks are required before launch.

Unresolved: actual execution of this bounded OS-Back candidate, spinner cause,
owner-cancellation/resultCode, different-Menu/provider-loaded cancellation/401/403.
Mobile Partial; no native or biometric wait is active at preparation.
