# Phase04 — Android platform bridge and real Google connection

Status: **Partial — selected live Google flow Passed; broader acceptance Pending**. Priority:4.
GoogleRun03 Passed1/1: usable authorization/provider rows, exact mapping and durable cache.
Cache UI Run02 Passed1/1,42.393s: cached Cook/lock/reopen/authenticated same-process
recreation; app-disconnected cache only. [Evidence](reports/google-cache-ui-runtime.md).

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
- [ ] **Owner Pending:** actual network-off, cancellation/network/401/403 preservation,
  cold-start/process-death and broader configuration/account/platform matrix; no mock credit.

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
