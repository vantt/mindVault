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
