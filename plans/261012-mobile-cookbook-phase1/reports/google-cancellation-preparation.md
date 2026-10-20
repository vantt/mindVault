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
