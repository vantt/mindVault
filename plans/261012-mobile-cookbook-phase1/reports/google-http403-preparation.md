# Actual Google HTTP403 preparation —21October2026

Status: **Run01 Failed203.052s; actual provider HTTP403 Pending; Mobile Partial**.
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
