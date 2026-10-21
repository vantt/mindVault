# Actual Google HTTP403 preparation —21October2026

Status: **Actual HTTP403 Run04 Passed27.530s; Run01/02 Failed and Run03 NotExercised retained; Mobile Partial**.
Continue checkpoint `a5a655640da15fdb9ea790d00ac8609c96a80a6e`; no restart.

## Candidate and acceptance boundary

Selected method:
`dev.passchef.mobile.proof.GoogleAccessDeniedPhysicalTest#actualGoogle403PreservesSyntheticOfflineCache`.
New androidTest fixture/observer reuse unchanged ColdStartProofActivity,
ColdCacheUiFixture and ColdCacheBaseline. Canonical UUID-owned storage; genuine
strong-biometric setup; UI-confirmed synthetic offline Menu, favorite/recent and
actual cached Cook before production Google Connect. Baseline is **synthetic offline**,
not provider-loaded. No token/session/result/HTTP injection or ACL/grant changes.

403 provenance: canonical `SheetsClient.decodeOrThrow` creates
SheetsForbiddenException only from actual response.status.value403;
CookbookSources.failure alone maps that exception to ACCESS_DENIED. SDK/no-grant
and401 do not map there. Strict pass requires current secure foreground, ready
session/RAM grant, no natural stop, no pending request/busy work, no metadata/draft,
ACCESS_DENIED UI and exact synthetic cache/jars bytes plus RAM/inventory equality.
Dish/reveal/lifetime/Copy UI must be absent. This proves preservation; fresh cached
Cook after403 is not included in this method.

Natural stops must clear session/token/Dish/selection/metadata/draft and preserve
fixture bytes. Returned locked/consumed callback is not403 acceptance. Other completed
production outcomes are NotExercised for403: SDK/authorization/401 are combined where
exact cause is unobservable; NETWORK_FAILED combines network/404/other HTTP. A usable
metadata response is readable, not refusal. Exact cancellation/resultCode and exact
non403 HTTP codes remain unobserved. Unresolved return/dirty stop/assertion failure
cannot be skipped. No automatic reopen/retry or timed runtime without prerequisites.

## Local verification

- Build01 Passed54s; final Build02 Passed16s, each104tasks/10executed/94up-to-date.
- `:app:assembleDebugAndroidTest :app:testDebugUnitTest :sheets-android:jvmTest :app:lintDebug`.
- App JVM10 and canonical bridge22:0failures/errors/skips; existing passing XML reused (test tasks UP-TO-DATE in both builds).
- Lint0errors/11warnings; existing Compose rule deprecation retained.
- New observer main-thread lifecycle read repaired before final build; original
  helpers/assertions/production unchanged. Independent read-only source/runner review
  found no actionable issue; separate tester inspected actual logs/XML.
- 43 isolated host classifier cases Passed. Host synthetic input tests certify no
  Google/device outcome. Current runner canonicalizes Menu URL before Android shell.
- All86 checkpoint app source/manifests and360 prior protected checkout hashes
  preserved, including unrelated work. New app inventory88; APK test alone changed.
- No new phone connection/readiness/install/runtime/owner-digest observation claimed.

APKs:

| Artifact | SHA256 |
|---|---|
| debug | `37579b452315adb28067a29a1c46f43731082580d2b1d760ec05147b06691a6c` |
| test | `26bdf5166e9b300cf64426c5897a1f5d3f27f02fb53d40bf8a8fd86a9763f229` |
| release | `92fa8896fcba7224267d63cff0388fcdcb27bb775e4abbe05463b1450961369e` |

Persistent evidence:
`/home/vantt/.local/share/passchef-mobile-evidence/2026-10-21-google-http403/`.
New access-denied support/runner/verdict/host-checks (<200lines each), build01/02,
checkpoint/verification/artifact/host-check records. Final frozen01/candidate01 binds
source, APKs, protected work, helper identities and complete prior persistent Google/
null-label evidence trees; created at candidate commit `0670fde`.
Frozen01 SHA256: `fb958da0c536a770c73bc457bc6748f99b87756a2b2abdbfb6f8d198db9cb63d`. Local archive audit Passed. No old raw evidence
is overwritten or reconstructed. Root alone owns ADB/Gradle.

## Owner checkpoint

Owner has not provided a denied test Menu and asked for clarification. Explained:
use a separate dummy-data Menu owned by another Google account, not shared with the
account in the app; owner must permit attempted access. Current readable Menu is
unsuitable and remains unchanged. A private Menu can return404 or another result;
record observed classification and leave403 Pending, never weaken assertions.

Before runtime: obtain permitted URL, fresh explicit owner readiness, verify actual
serial/awake/keyguard and owner digest by digest only; install-r test APK only if
needed, then verify installed candidate identities. Runner requires explicit owner
readiness/URL authorization, exclusive evidence names/new UUID, selected JUnit codes,
terminal/OK1, bound safe403 predicates, final owner/APK/source/archive/history audits
and exact UUID cleanup/pre-existing directory preservation. No uninstall/data clear.
At biometric/Google owner checkpoints, announce and yield promptly.

Historical cancellation Failed197.803s/197.504s and all Unknown/NotExercised remain.
Selected system-Back Passed17.949s remains scoped to that prior candidate.

Unresolved: authorized denied Menu URL, fresh owner readiness and actual403 runtime;
SDK/401 and network/404 cannot be individually diagnosed from current status alone;
all previous broader Mobile acceptance gaps remain Pending. Mobile stays Partial.


## Actual selected Run01 — Failed203.052s

21October2026, selected Xiaomi/API36, UUID
`a84e91f9-52d0-453e-9a4d-1ec874aa466f`, unchanged frozen01 candidate.
Owner supplied a separate test Menu URL, confirmed it was unshared, then explicitly
reported ready. No owner email or complete Menu URL committed. Fresh device/awake/
keyguard/digest checks Passed. Only instrumentation APK needed install-r; debug
remained unchanged and both installed hashes matched frozen01. Original Cookbook
was observed by digest only, exact before/after. No uninstall/data clear.

Genuine setup completed, synthetic offline baseline/Cook and production Connect
executed. Selected JUnit **Failed1/1,203.052s**,codes[1,-2],terminal[-1],native0,
watchdogfalse,no host orchestration error. Failure marker:connect-dispatch-failed.
At that sample, pending=true,stopped=true,allStopsClean=true,stoppedReturnLocked=true;
session/token/Dish/lifetime/metadata/draft absent, sourceOFFLINE, own foreground
absent, actual403=false. No return gate or provider403 accepted. The generic wrapper
omits the original exception/assertion, so duration/state alone do not establish
its exact cause or a Google configuration defect. Failed is retained, never a skip.

Final audits: exact owner digest, installed APKs/source/protected/helpers/history/
archive preserved; new UUID directory absent and all six prior proof directories
unchanged. These audits do not retroactively accept the failed fixture assertions.
Earlier diagnostic cache/jars digests identify this synthetic baseline only.

Owner reported Google screen after terminal. Root initially suggested account/grant
steps before polling the result, then promptly corrected that advice after observing
terminal failure; no post-terminal action is part of runtime acceptance. Owner later
reported clicking webpage **Back to Safety**, returning to Chrome/help page. This is
not an observed Android OS Back key. A separate native sample confirms Chrome focus
and preserved owner digest. Owner does not remember the prior Google message; exact
text/code and reason for that webpage remain **Unknown**. Do not label it Sheets403,
SDK failure, consent denial or fresh consent from the button label alone.

Persistent new records:install-preflight01,run01 preflight/raw/result,
run-01-owner-back-to-safety-01.json,run-01-owner-google-message-01.json and independent
runtime review. No native/biometric wait remains active. No unchanged retry is started.
Next: clarify the Google authorization page through a permitted deliberate diagnostic
before another timed attempt; current denied Menu prerequisite is supplied, Google
flow readiness remains unproven. Actual403 and Mobile remain Pending/Partial.


### Later owner clarification — unverified-app warning

Owner subsequently recalled **Google hasn't verified this app**. New exclusive
`run-01-owner-google-message-02.json` preserves the clarification; earlier Unknown
record is retained. This is owner-reported Google authorization UI, not actual
Sheets HTTP403 or instrumented confirmation of the complete page/error code.
Google documents this warning for apps requesting sensitive/restricted scopes that
have not completed verification ([official guidance](https://support.google.com/cloud/answer/7454865?hl=en)).
It identifies the observed consent-stage obstacle; it does not prove the exact cause
of the fixture failure, current Cloud configuration or why every earlier run failed.
Run01 remains Failed203.052s and actual403 Pending. Before another run, explain
expected development consent and verify intended app/account/test-user eligibility;
no automatic grant, scope, publishing-status or Menu ACL changes.


## Actual Run02 — Failed234.38s; Run03 — NotExercised17.223s

Both use unchanged frozen01 source/APKs/assertions and separate new canonical UUIDs.
Owner instructed continuation; fresh actual serial/awake/keyguard/owner digest/
installed identity audits preceded each invocation. No rebuild/reinstall between runs.

Run02 UUID `1b4ebecd-0af5-4ad6-b983-1f783554d897`:
**Failed234.38s**,selected[1,-2],terminal[-1],native0,no watchdog/host error.
Last observed pending=true,stopped=true,allStopsClean=true,stoppedReturnLocked=true;
no session/token/Dish/lifetime/metadata/draft, sourceOFFLINE, actual403=false.
Generic wrapper again prevents exact underlying assertion/cause attribution.
Owner described the unverified-app/Continue page, then PassChef account-access
Continue/Cancel, then closing after Continue. Root continued guidance before polling
terminal; exact owner-action times relative to terminal are unobserved. No completed
grant or return acceptance follows. Ordinary MainActivity launch initially sampled
launcher, later PassChef focus; owner ultimately confirmed normal UI with biometric
open button. Those post-terminal observations are separate from acceptance.

Run03 UUID `45a358ab-2a94-4a56-a526-39fe3f885ef0`:
**NotExercised17.223s**,selected[1,-4],terminal[-1],native0,OK1,no watchdog/host error.
Genuine biometric setup, UI-confirmed synthetic offline cache/Cook and production
Google Connect executed. Final metadataReturned=true,tokenAbsent=false,ready=true,
secure foreground,stopped=false,pending=false,sourceBusy=false,actual403=false.
Google returned metadata for the supplied test Menu; no values/tab selection claim.
Exact synthetic baseline bytes/jars/RAM/inventory retained; stale Dish/reveal/Copy
lifetime absent. This is a completed non403 outcome, never a failed403 assertion
converted to Pass. No fresh-consent or exact Google account identity is inferred.

Owner reported 'crash (app tự đóng)' after fingerprint. The selected native invocation
completed a clean assumption skip and fixture finally calls scenario.close; exact
human-visible closing time/cause remains unobserved. No crash stack/process-death
proof or categorical absence of later crash follows. Owner explicitly clarified no
account chooser appeared and no account was manually selected. Root's earlier account
selection question does not establish an action. Authorized Google account identity
remains Unknown; do not infer it is the Menu creator or that ACL was changed.

Both final audits Passed: owner digest unchanged, exact installed debug/test/source/
protected/helper/history/archive identities, own UUID directory absent and prior six
proof directories preserved. Independent `runtime-review-02-03.json` confirms raw
verdicts and limits. Additional owner-close/normal-return/no-account-chooser evidence
is preserved separately. No runtime/native/biometric wait active. No unchanged retry.

Next prerequisite: identify the Google account used by genuine authorization without
reading tokens/private Cookbook content, then select a separately authorized test
Menu inaccessible to that account. Current Menu returned metadata in Run03; use that
observation rather than owner-intended sharing as the refusal premise. Actual403
and whole Mobile remain Pending/Partial; all historical Failed verdicts retained.


## Actual Google HTTP403 Run04 — Passed27.530s

21October2026, Xiaomi14TPro/API36, UUID
`e47c8e4f-8c7a-4c8c-8922-13375186f58b`. Owner supplied a new separate-account Menu,
confirmed Restricted/unshared to the current PassChef account and explicitly ready.
Account association is owner-reported; no email/token/userinfo was read to identify
Google's account. Complete Menu URL/account emails omitted from committed evidence.
Fresh native serial/awake/keyguard/digest/installed APK and frozen audits Passed.
Same frozen01/APKs/source/assertions as Run01–03; no rebuild/reinstall/code change.

**Passed1/1,27.530s**,selected[1,0],terminal[-1],anchoredOK1,native0,
watchdogfalse,no orchestration error/failure/skip; final phase
`actual-http403-preservation-passed`. Genuine biometric setup, UI-confirmed synthetic
offline mapping/cache/favorite/recent/Cook and production AuthorizationClient→Android
Sheets metadata request executed. No token/session/result/HTTP injection, account
reset/grant revocation, ACL change or consent-forcing mechanism.

Actual provider403 provenance is uniquely bound to canonical
SheetsClient.decodeOrThrow response.status.value403→SheetsForbiddenException→
CookbookSources.failure ACCESS_DENIED; no other production path maps that status.
Final actual403=true, metadata/draft absent, not busy/pending, own secure/resumed/
focused/awake/unlocked foreground, ready session and RAM grant present, stopped=false.
Exact localized refusal UI asserted. No401 clearing/SDK failure/network/404/Google
page warning is credited as403. No fresh-consent or account-picker claim.

Exact last-good **synthetic offline** cache file bytes and encrypted fixture jars
unchanged, complete RAM/inventory/mapping/favorite/recent equality; no corrupt cache.
After Connect no Dish/reveal/ready/lifetime/cooking/Copy UI; selected entry absent.
No cached Cook after403 or provider-loaded baseline acceptance follows. Session/token
retained on this foreground403 path by contract; no lifecycle stop occurred, so
external-return/cancellation/lock acceptance stays scoped to earlier runs.

Final owner Cookbook digest894f73…a4d6, installed debug/test hashes, source/protected/
helpers/history/frozen/archive identities preserved. Own UUID directory absent,
all six prior proof directories unchanged. Independent runtime-review04 checks raw,
result and scope. Owner reported completion and app closing; fixture finally closes
Activity, exact human-visible closing time/cause unmeasured. No crash finding inferred.
No native/biometric wait remains active and no automatic retry needed.

Persistent records:run-04-preflight.json,run-04.log,run-04.json,
run-04-owner-close-observation-01.json, second-menu-received-precheck01 and
runtime-review-04.json. Earlier Failed203.052/234.38 and NotExercised17.223 remain;
new success does not reinterpret those or missing historical raw evidence.

Accepted: selected actual Google metadata HTTP403 refusal and exact synthetic cache/
jars preservation/stale Dish clearing on this device/candidate. Unresolved: actual401,
provider-loaded403/cache Cook after refusal, human cancellation/resultCode/different-
Menu callbacks, broader lifecycle/clipboard/accessibility matrix. Mobile stays Partial.
