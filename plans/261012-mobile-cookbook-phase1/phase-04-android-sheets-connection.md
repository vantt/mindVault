# Phase04 — Android platform bridge and real Google connection

Status: **Partial — code/local verification Done; owner Google acceptance Pending**. Priority:4;
real authorization and successful owner Sheets read remain Pending.

## Context and key insight

[Sheets spec](../../docs/specs/mobile-sheets-client.md),
[settled D1–D4/Q9](../../docs/decisions/mobile-product-decisions.md),
[setup runbook](../../docs/runbooks/google-oauth-sheets-setup.md).
Standalone `mobile/sheets-client` has JVM21/Kotlin1.9.24/common Ktor code, an owned
engine/token constructor and desktop loopback smoke tool. Root is Kotlin2.4.10/
AGP9/JDK17. Do not import its JVM jar/loopback server into Android or assume drop-in
compatibility. Android package/signing registration and user consent are absent.

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
- [ ] **Owner Pending:** manual Android client configuration, real account consent,
  actual selected-Menu/tab API read and cache reload after disconnect; no mock credit.

## Risks and next steps

OAuth prompts may background the Activity: existing background lock still applies;
resume reconnect result safely and require local unlock before cooking. Token access
is unrelated to Spice authorization. No backend/refresh-secret/service-account shortcut.
After code freeze, phase05 independently checks code while retaining owner Pending.

Implementation exists in the production app/shared/client paths above. Final local
results and unchanged Golden evidence are recorded in [verification](reports/verification.md);
compiled physical tests remain unexecuted. No phone/account grant was used tonight.
