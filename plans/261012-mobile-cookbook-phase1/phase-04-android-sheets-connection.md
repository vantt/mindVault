# Phase04 — Android platform bridge and real Google connection

Status: **Partial — selected live Google flow Passed; broader acceptance Pending**. Priority:4.
21October selected [real host Android Back/stale-return run01](reports/google-cancellation-preparation.md#actual-host-android-system-back-run01--passed)
Passed17.949s: actual external pending/lock/Back/consumed reply, preserved synthetic
offline cache/jars/owner. Human cancellation/resultCode/401/403/different-Menu remain Pending.
GoogleRun03 Passed1/1: usable authorization/provider rows, exact mapping and durable cache.
Cache UI Run02 Passed1/1,42.393s: cached Cook/lock/reopen/authenticated same-process
recreation; app-disconnected cache only. [Evidence](reports/google-cache-ui-runtime.md).
Network-off Run04 Passed1/1,31.477s: actual strict network absence, real
NETWORK_FAILED preservation and offline Cook/recreation; [evidence](reports/network-off-runtime.md).

## Context and key insight

[Sheets spec](../../docs/specs/mobile-sheets-client.md),
[settled D1–D4/Q9](../../docs/decisions/mobile-product-decisions.md),
[setup runbook](../../docs/runbooks/google-oauth-sheets-setup.md).
Standalone `mobile/sheets-client` has JVM21/Kotlin1.9.24/common Ktor code, an owned
engine/token constructor and desktop loopback smoke tool. Root is Kotlin2.4.10/
AGP9/JDK17. Do not import its JVM jar/loopback server into Android or assume drop-in
compatibility. Owner configured the debug client and confirmed consent in GoogleRun01.
Selected live read/mapping/cache passed GoogleRun03; broader configuration/matrix Pending.

## Requirements and architecture

User Connect → Android Google authorization adapter → in-memory access token →
platform-compatible Sheets engine/client → metadata/selected tab values → confirmed
phase02 mapping/cache. Existing offline cache remains useful on cancellation/401/403.
Use Sheets read/write scope already settled; no Drive discovery scope or delete API.
Phase1 only consumes read paths; retain RAW append/update client regression behavior.

## Related files and integration choice

- Modify root settings/app dependencies/manifest; add INTERNET for HTTPS.
- Add a small root `:sheets-android` KMP bridge under `mobile/sheets-android/`, using
  current root plugin/toolchain and original Sheets `commonMain/commonTest` source
  directories as its single source of implementation/tests; no copied client fork.
- Add Android engine/authorization/source-refresh adapters under app `cookbook/`.
- Extend canonical portable client/models only if needed for spreadsheet/tab metadata;
  preserve independent `mobile/sheets-client` JVM smoke build/runbook.
- Verify existing Ktor/serialization and new Android dependencies during integration;
  pin actual compatible versions with evidence, not invented plan version numbers.

## Implementation steps / TODO

- [x] First prove root Android bridge compile/portable protocol tests; document target,
  source-set ownership and standalone build preservation. If this layout fails,
  revise the bridge before app wiring; do not silently upgrade all roots.
- [x] Add bounded spreadsheet metadata/tab discovery and bounded value reads (explicit range chunking when needed),
  quoted A1 tab names, Unicode encoding, literal value preservation and client closing.
- [x] Define authorization/provider boundary and genuine Android callback code using
  the supported Google user-data authorization flow ([official guide](https://developer.android.com/identity/authorization)).
  Desktop client secret/loopback exchange is not bundled in the app.
- [x] Request the settled Sheets scope at explicit Connect; use Activity-result and
  lifetime/epoch guards. Cancelled/stale account/permission replies cannot publish a
  token or refresh another source after lock/background/source change.
- [x] Keep tokens in RAM, out of disk/logs/saved state; reacquire/reconnect when needed.
  Missing configuration/services/grant is explicit disconnected state, never fake success.
- [x] Parse user-selected Menu URL/ID, retrieve title/tabs, preview and confirm mapping;
  refresh atomically.401 asks reconnect,403 explains access, network failure retains cache.
- [x] Add Android-specific manual setup addendum: package `.demo`/release and signing
  SHA registration, consent/test-user setup, permitted scope and dedicated test Menu.
  Existing public browser URL is not proof of an API authorization grant.

## Success criteria and proof boundary

- [x] Portable HTTP protocol tests cover empty read, metadata/tabs, Unicode ranges,
 401/403/malformed/timeouts/engine close and original RAW append/update/no delete.
- [x] Deferred callback tests prove cancelled/stale auth/refresh cannot replace token,
  source, mapping or cache. APK/release/lint positive compile evidence recorded.
- [x] Independent standalone JVM21 build/test still passes original connection tests.
- [x] Selected Xiaomi/API36 authorization/metadata/tab/known rows/mapping/cache-file
  acceptance: GoogleRun03 Passed1/1,23.309s; owner-observed consent in GoogleRun01.
- [x] Selected cache UI/Cook after lock/reopen and authenticated same-process recreation;
  no in-memory token, exact provider cache/favorites/recent/search preserved in Run02.
- [x] Selected actual network-off/NETWORK_FAILED preservation and full cached UI
  helper Passed Run04; source/owner digest preserved, no production repair.
- [ ] **Owner Pending:** cancellation/stale callbacks/401/403 preservation,
  natural LMK/saved-task restoration and broader configuration/account/platform matrix.
  Controlled offline/provider SIGKILL/fresh-process cold acceptance already Passed; no mock credit.

## Risks and next steps

OAuth prompts may background the Activity: existing background lock still applies;
resume reconnect result safely and require local unlock before cooking. Token access
is unrelated to Spice authorization. No backend/refresh-secret/service-account shortcut.
After code freeze, phase05 independently checks code while retaining owner Pending.

Implementation exists in the production app/shared/client paths above. Final local
results and unchanged Golden evidence are recorded in [verification](reports/verification.md);
The original local verification used no phone/account grant and left Google physical
tests unexecuted. Later preparation and actual runtime are recorded separately below.

## Oct13 owner-ready Google continuation

Owner supplied a Menu URL and confirmed genuine account/Sheets consent. GoogleRun01/02
remain Failed history; owner subsequently completed the known synthetic fixture.
GoogleRun03 Passed1/1,23.309s with all11 match flags true and mapped durable cache.
Preserve original Cookbook; [actual runtime](reports/google-runtime.md).

- [x] Add/review isolated real Compose Google acceptance fixture; actual owner
  setup/biometric and AuthorizationClient, metadata/tab/rows/mapping/cache assertions.
- [x] Retain natural background lock and discard stale consent replies; reconnect
  only through normal UI after owner unlock. No injected grant/token/offline table.
- [x] Root compile/lint and compare exact local/installed artifacts with current
  owner digest preserved:59s Passed, lint0/11/1. [Preparation](reports/google-acceptance-preparation.md).
  Preparation snapshot predates the data-preserving install and actual runtime above.
- [x] Run only this fixture after valid test Menu URL and owner readiness; record
  Run01/02 Failed and Run03 Passed with selected grant/read/mapping/cache accepted;
  preserve wider Pending matrix and consent-reuse distinction.

## Live Google mismatch diagnostics

- [x] Preserve Failed GoogleRun01/02 separately from original UI Run01; record
  owner-confirmed consent/tab correction without claiming the exact coarse failure line.
- [x] Add safe read-only identity/header/row match booleans and precise phase names;
  preserve every existing assertion, log no actual cell/URL/account/token contents.
- [x] Independent review and root-only compile of frozen diagnostics Passed59s,
  lint0/11/1; all17 original assertions byte-identical. Exact candidate installed for Run03.
- [x] Retry only after owner confirms dedicated synthetic schema and readiness:
  Run03 Passed1/1,23.309s. No production repair or whole-phase completion inferred.

## Google UI cache continuation

Continue the existing real-provider fixture with opt-in
`passchefGoogleCacheContinuation=true`; retain all17 provider assertions and
historical artifact hashes. Compiling this extension does not accept its runtime.

- [x] Add/review bounded helper (<200lines) for actual Lock → owner biometric
  reopen → cached Menu list/search → row Cook with exact origin/Recipe/username;
  assert masked/revealed synthetic Dish and persisted favorites/recent IDs.
- [x] Add opt-in same isolated proof directory across Activity recreation;
  default remains per-instance UUID so prior two-directory clipboard proof is intact.
  Restore only the owned fixture location, never jars/session/grant/Dish state.
- [x] Recreate actual authenticated Activity; assert old instance destroyed,
  new instance locked/no Dish, same fixture/cache and no in-memory Google token.
  Genuine owner unlock then reloads the provider cache without another Connect.
- [x] Root-only compile/lint, exact source/APK hashes, data-preserving install,
  Awake/keyguard=false preflight and before/after owner Cookbook digest checks.
- [x] Run only the selected provider fixture with ready owner; record actual
  JUnit/phase outcomes separately from GoogleRun03 and historical CookbookUIRun01.

Scope: app-disconnected offline cache (no in-memory grant), same-process Activity
recreation and real biometric UI. No device network toggle, process-death/cold-start,
provider cancellation/401/403/network-error or whole-mobile acceptance inferred.
Original CookbookUIRun01 cause and broader physical matrix stay Pending.

Cache UI Run01 Failed1/1,294.651s; preserved failed candidate/runner evidence.
Helper-only normal Compose synchronization correction retained every assertion.
Cache UI Run02 Passed1/1,42.393s; default-storage regression Passed1/1,31.769s.
No production repair inferred; [exact history and scope](reports/google-cache-ui-runtime.md).

## Owner-operated network-off continuation

- [x] Add/review debug-only network-state observation and bounded selective helper;
  retain original17 provider assertions and150-line cache helper byte-identical.
- [x] After real provider cache/Cook, authorized root USB disables radios at marker;
  require actual no default/bound/internet-capable network and latch reconnection.
- [x] Preserve natural background lock; genuine biometric reloads when needed.
  Try real retained-grant tab read, otherwise normal Connect; distinguish transport
  NETWORK_FAILED from authorization CONNECT_REQUIRED without injected grants.
- [x] Assert exact last-good cache/disk/jars and cleared stale Dish; execute existing
  cached Cook/favorite/recent/recreation assertions while network remains absent.
- [x] Root-only build/hash/install/digest/preflight and selective real runtime;
  report offline acceptance separately from actual transport failure preservation.

Root's authorized USB runner controls airplane/Wi-Fi/data and restores enabled
baseline; actual fixture network gates remain strict, no command-only proof.
No HTTP mock/offline paste/session/clock bypass or production permission change.
Run01/02/03 Failed history retained; unchanged diagnostic candidate Run04 Passed.
Cancellation/stale callbacks/401/403/process death/cold start and broader phone matrix
remain Pending; original CookbookUIRun01 and network Run03 wrap causes unknown.

## Abrupt process-death / fresh-launch continuation

Five scoped debug/test files implemented and reviewed; actual runtime acceptance
tracked in [cold-process evidence](reports/cold-process-runtime.md). Synthetic offline
Menu confirmed through the production UI; this proves bounded cache/jars persistence,
never provider-backed cold acceptance. Actual Google-loaded cache remains a separate
Pending continuation unless a new genuine provider read is needed and executed.

- [x] Add a debug-only unexported `ColdStartProofActivity` and one debug manifest
  entry with dedicated proof task affinity. Before `MainActivity.onCreate`, validate
  canonical UUID Intent locator and canonical app-private `cookbook-cold-proof-UUID`
  direct-child path. Warm requires a new directory; cold requires that same owned
  fixture/baseline already exist. Locator restores no session/token/Spices/Dish.
- [x] Add new bounded warm/cold instrumentation plus helpers; preserve every prior
  assertion. Use an empty Compose rule and explicit `ActivityScenario.launch(Intent)`
  so each new process launches the reviewed Activity with the actual UUID locator.
  New-task clearing, if required, affects only the dedicated proof task.
- [x] Warm: genuine password/strong-biometric setup, UI-pasted offline synthetic
  Menu, explicit mapping, favorite/recent, exact origin/Recipe/literal00123 and Cook.
  Wait for actual cache commits. Record encrypted jars/cache digests, expected fixture
  metadata and actual PID; no content/token/Spices/session bytes in diagnostics.
- [x] Emit a safe kill-ready marker while the same process remains ready with exact
  revealed Dish at checkpoint, secure focused foreground and unchanged files.
  Normal reveal timeout remains intact; exact revealed-at-kill state is not claimed.
  Wait boundedly; no
  normal Activity close/background transition or fixture cleanup before root's kill.
  Root independently verifies target PID/cmdline/UID, then uses `run-as` to SIGKILL
  only that exact owned process. Denial is reported; no policy override/fallback.
- [x] Save the intentionally interrupted warm runner outcome and actual old-PID
  disappearance; never manufacture warm JUnit Passed. Freeze source/APKs/baseline
  before this stage. Root confirms owner digest/files preserved after process death.
- [x] Run a separate cold instrumentation method with the same validated locator.
  Verify new PID differs and root confirms its identity; fresh Activity is locked,
  session/token/selected entry/metadata/draft/Dish/lifetime/Copy absent before unlock,
  with exact durable jars/cache preserved. No `scenario.recreate()` credit.
- [x] Genuine biometric reopen, normal Compose synchronization/cache reload, exact
  saved cache and favorites/recent/search, origin/Recipe/username and masked/revealed
  cached Cook. Recheck encrypted jars/cache preservation; only the cold method may
  earn terminal JUnit Passed. Finally close/delete only this fixture's keys/files.
- [x] Root-only build/review/freeze/install/preflight. Archive the original debug
  manifest and disclose its intentional one-entry delta: old frozen43 becomes42
  unchanged plus that delta, not43 unchanged. Preserve protected208/unrelated23,
  production main/shared/canonical/extension, old assertions and owner Cookbook.

Scope: controlled SIGKILL plus fresh Intent launch, not LMK/natural/background death
or Android saved-task restoration. `force-stop`, if chosen later, is separately named
administrative-stop scope. Cold stage cleanup also needs a bounded recovery path if
the process dies again; authorize no broad `uninstall`, data clear or alias deletion.

Actual warm02 InterruptedByVerifiedSIGKILL/cold02 Passed1/1,5.993s,PID25682→25991;
files/genuine reopening/cache/Cook/owned cleanup and owner digest preserved. Warm01
remains Failed native-focus-query orchestration, before SIGKILL; query correction
changed no APK/assertion. Root build03 Passed54s after import/type-only build01/02
failures; intentional manifest delta and release exclusion verified. Unresolved:
provider-loaded cold scope/background/LMK/task restoration, historical causes.

## Provider-loaded cache process-death continuation — selected Run02 Passed

Four new bounded instrumentation files reuse the reviewed `ColdStartProofActivity`
and its canonical UUID/dedicated task; existing54 source/manifest/assertion identities
remain unchanged. Prepare/review/build precede runtime; no provider acceptance from compile.
Independent review and corrected build02 Passed50s9/102,lint0/11/1; exact frozen58/
debug1f42/test9b04 candidate02 installed-r with owner digest preserved. Original
candidate01 retained before reconnect foreground-conjunct restoration. Cold01
Failed181.588s reopening; owner prompt-or-error report ambiguous, cause unknown.
Diagnostic-only build03 Passed58s10/101, test3711/frozen03; no assertion/policy change.
Warm02 InterruptedByVerifiedSIGKILL/PID13092 then cold02 Passed1/1,8.886s/PID14408:
initial locked/cleared state, real fingerprint reload/exact cache/Cook/jars and owned
cleanup preserved. [Actual evidence](reports/google-cold-runtime.md); no repair claim.

- [x] New provider warm method: genuine setup, normal Connect/AuthorizationClient,
  actual metadata/unique Test tab/literal synthetic rows, confirmed columns/cache;
  retain all17 existing Google predicates. SDK grant may be reused, never claim fresh consent.
- [x] Observe actual default/bound network capabilities without changing radios;
  real provider responses establish read success. Cancel/denial remains a real failure.
- [x] Favorite/recent and exact cached Cook through UI; freeze precise disk digests
  only after commit/busy=false and actual reread. Provider-kind baseline stores hashes,
  hashed cache filename, numeric tab/refresh/PID and UUID; no content or credentials.
- [x] Root verifies exact own PID/UID/cmdline, performs SIGKILL at ready/revealed marker
  and observes old-PID disappearance. Warm verdict is intentionally interrupted,
  never JUnit Passed. Separate cold method starts a different actual PID/same locator.
- [x] Cold begins locked/no app-usable token/session/Dish; frozen provider cache/jars
  unchanged before unlock. Genuine biometric/normal reload preserves exact bytes
  before interactions; search/favorites/recent/origin/Recipe/literal00123/Cook accepted
  only with actual assertions. After Cook compare cache semantics and encrypted jar bytes.

No Connect in the cold method, no SDK-grant revocation claim, token/session injection,
offline paste, forced clock or new Android manifest entry. Controlled abrupt death/fresh
Intent scope only; natural LMK/background restoration,401/403/cancellation and broader
matrix remain Pending. Root owns build/device/signal/evidence and announces owner prompts.

## Unattended coordinator and streaming cancellation continuation

- [x] Add six app JVM cases in two new files: readiness checked at deferred real-file
  confirmation/removal commit, cancelled refresh/replacement ownership, drift and invalid
  remapping recovery, oversized replacement preservation, failed favorite IO/retry.
  Preserve all earlier assertions and production code; real temporary files, no provider mocks.
- [x] Root executes app10/bridge22 with lint Passed; fresh XML0failures/errors/skips.
  Independent review and standalone35 Passed; [Unattended source coverage](reports/unattended-source-coverage.md).
  Report fresh outcomes
  separately from device acceptance. No Google/biometric/device operation required.
- [x] Root adds six canonical common tests for explicit cancellation during blocked
  streaming response/error bodies; existing timeout/budget/401/403 cases remain unchanged.
  Initial4 and final6 cases Passed; suspected cancellation failure not reproduced.
  No production correction justified; original canonical sources/tests unchanged.

These cases add coordinator wiring and cancellation evidence, not live Google cancel,
401/403, SDK stale-callback or physical clipboard acceptance. Those remain Pending.

## Selected real Google cancellation / stopped-result continuation

Selected instrumentation is implemented; retain every existing source assertion
and all historical candidates. Reuse `ColdStartProofActivity`,
`ColdCacheUiFixture` and the real production Connect flow. An offline synthetic cache
is sufficient for this preservation boundary; do not call it provider-loaded cache.

- [x] Implement and independently review the selected fixture plus bounded host
  runner; compile/lint Passed26s,lint0/11/1. Freeze70 current app sources, preserve
  all68 old files and26 old helpers. Install-r exact test669a8c9c with owner digest
  unchanged; no runtime launched while phone asleep. [Preparation](reports/google-cancellation-preparation.md).

- [x] Set up the UUID-owned Cookbook with genuine biometric authorization, confirm
  the synthetic offline Menu through UI, favorite/Cook it and freeze exact encrypted
  jars/cache bytes plus RAM snapshot. Validate the allowed Google Menu argument.
- [ ] Before Connect, register a read-only own-Activity stop monitor. Observe the
  real `AndroidSheetsAuthorization.pending` identity on main; require an actual
  external-resolution window and natural stop. At every stop require cleared
  session/Dish/lifetime/token, no metadata/draft and unchanged cache/jars.
- [ ] Announce the bounded owner cancellation checkpoint only after real pending
  resolution is observed. Owner cancels the genuine Google UI and returns. Require
  the observed pending result to be consumed, own foreground restored and the app
  still locked with no old Dish/Copy or grant publication; compare exact preserved
  cache/jars. A consumed pending reply does not alone prove `RESULT_CANCELED`.
- [x] Record actual selected outcome separately from owner cancellation evidence:
  Run01 NotExercised15.608s,1skip; usable metadata/token returned with no observed
  resolution, pending request or stop. Cache/jars/owner digest and owned cleanup
  preserved. No cancellation checkpoint or owner cancellation answer occurred;
  external cancellation/stale acceptance remains Pending. Do not force grant/account
  changes or inject results/tokens to manufacture an available prompt.

This selects natural background/lock stale-result handling. Late callbacks after a
different Menu, actual provider401/403 and provider-loaded cache cancellation remain
Pending unless separately exercised. No permission/account change is implied.

20October [persistent preparation](reports/google-cancellation-preparation.md#persistent-continuation-20october2026):
current86 sources/APKs unchanged; build Passed2s,lint0/11/1, independently reviewed
host runner/frozen03 and11 host parser checks Passed. Read-only installed/owner
digest checks match; no runtime/reinstall. External resolution availability and
fresh owner readiness required before retry; grant withdrawal needs specific approval.
21October owner reported grant removal; selected persistent run01 Failed197.803s
after actual Google resolution/natural lock, pending reply not consumed within180s.
Owner reports Back reaches Google web; current Chrome focus observed separately.
Exact owner/source/APK/UUID preservation Passed; cancellation/stale remains Pending.
Persistent run02 Failed197.504s; external resolution/clean lock observed, reply
unconsumed. White-spinner owner report and later Google focus do not establish cause.
Post-terminal Android Back/normal app recovery preserved owner/source/APKs; no
retroactive acceptance. Prepare actual in-run system Back before another owner wait.
Reviewed host-only system-Back candidate now frozen separately:18 isolated host
guard/classifier cases Passed; source86/APKs and existing fixture/assertions unchanged.
Actual runtime remains Pending until fresh owner readiness; no injected callback.
That candidate subsequently Passed17.949s with unchanged assertions/source/APKs;
selected real host OS-Back callback after natural lock accepted. Manual Failed01/02
retained. Readable allowed Menu provides no deterministic401/403; separate authorized
denied test Menu needed for403, legitimate credential transition for401. Input is
disabled while busy; no genuine different-Menu/pending callback overlap established.

## Actual HTTP403 continuation —21October2026 preparation

Synthetic offline baseline explicitly reused via unchanged ColdCacheUiFixture and
ColdCacheBaseline. New GoogleAccessDeniedPhysicalTest uses a canonical UUID-owned
ColdStartProofActivity, genuine biometric setup and production Connect/provider path.
Only ACCESS_DENIED qualifies: canonical HTTP403→SheetsForbiddenException→ACCESS_DENIED.
SDK/authorization/401 remain combined where unobservable; NETWORK_FAILED does not
separate network from404/other HTTP. External natural lock safely ends without403
credit; no automatic biometric reopening or retry, token/result injection or ACL change.

- [x] Prepare separate selected fixture/observation; preserve historical assertions.
- [x] Root compile/JVM/lint and independent review; strict separate host runner prepared.
- [x] Freeze committed candidate0670fde; exact source/helper/APK/history archive audit Passed.
- [x] Owner supplied separate denied test Menu URL and confirmed unshared.
- [x] Fresh owner readiness/device preflight and selected Run01 attempted; Failed, no403 accepted.
- [ ] Preserve exact synthetic cache/RAM/mapping/jars, stale Dish/Copy and lifecycle proof;
  audit owner digest/UUID cleanup and record actual outcome before updating acceptance.

No new runtime acceptance claimed. HTTP403 and whole Mobile remain Pending/Partial.


## Actual HTTP403 Run01 —21October2026

[Selected runtime](reports/google-http403-preparation.md#actual-selected-run01--failed203052s) Failed203.052s,
selected[1,-2]/terminal[-1]/native0; pending external request and natural locked stop,
no actual403 or accepted return gate. Exact cause unpreserved by generic wrapper.
Owner/UUID cleanup/source/APK/history audits Passed. Owner later clicked webpage
Back to Safety and returned to Chrome/help; prior Google message Unknown.
No Android Back/Sheets403 credit; no active runtime or automatic unchanged retry.
Denied Menu supplied; authorization-page diagnosis and actual403 remain Pending.


## HTTP403 continuation Run02/03 —21October2026

[Runtime record](reports/google-http403-preparation.md): Run02 Failed234.38s, unresolved external return;
Run03 NotExercised17.223s, actual metadata returned, no403. Exact synthetic cache/
jars and stale Dish assertions retained; owner/cleanup/APK/source audits Passed.
Owner saw no account chooser; account identity Unknown. Owner close report is
separate from fixture terminal/normal Activity recovery. No active wait/retry;
identify actual Google account and inaccessible test Menu before next403 attempt.
