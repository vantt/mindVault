# Actual Google HTTP401 preparation —21October2026

Status: **Host checkpoint audit Passed; actual401 Pending; Mobile Partial**.
Continue accepted main `43501ec5770067c43853bf3136af59a99415df9c`.
No runtime, biometric wait, install, grant/account/ACL change or production repair.

## Checkpoint and evidence

Persistent preparation root:
`/home/vantt/.local/share/passchef-mobile-evidence/2026-10-21-google-http401/`.
Host audit re-executes the existing provider403 read-only local verifier: source,
protected WIP, helpers, historical evidence, candidate archive and three APKs match.
Frozen01 SHA256 remains
`35c8cb94f1b6d51249bf1eb9f6a103f699403ae9e28302574bf8c584155afedc`.
The four raw/result/preflight hashes bound by runtime-review01 also match.
This verifies retained evidence integrity, not fresh device state or rerun acceptance.
Existing Failed/Unknown/NotExercised records and missing old `/tmp` evidence remain.

## Contract and provenance

| Boundary | Evidence required / meaning |
|---|---|
| `SheetsClient.decodeOrThrow` | Real Android engine response401 creates `SheetsUnauthorizedException`;403 creates `SheetsForbiddenException` |
| `SheetsMenus.rows` | Production read with retained RAM grant; no SDK authorization call on this path |
| `CookbookSources.choose` / `failure` | Current request401 sets `CONNECT_REQUIRED` and clears RAM `accessToken`;403 sets `ACCESS_DENIED` and retains token |
| `AndroidSheetsAuthorization` / `disconnected` | SDK failure/no usable grant can also set `CONNECT_REQUIRED`; status alone cannot prove401 |
| Other outcomes | `NETWORK_FAILED` groups404/network/other HTTP; exact cause unobserved without additional provenance |
| Natural lock / stale reply | Ownership rejection and cleared session/token are separate from401; no provider acceptance credit |

Future candidate can establish401 by an exclusive retained-grant tab-read interval:
token present and actual metadata before dispatch; ordinary Sources tab UI invokes
`choose`; no Connect, field edit, invalidation or natural stop; same ready session;
then `CONNECT_REQUIRED`, token absent and completed current request. In the frozen
production path only typed HTTP401 can produce that conjunction. Re-review this
uniqueness against the actual candidate before using it; no generic status-only pass.
No token value/body/account identity logging or injected HTTP/exception is needed.

Dish ownership is separate: opening Sources calls `model.back()` and clears the old
Dish before the read. Assert previous Dish/reveal/lifetime/Copy unusable before and
after the error; do not claim401 itself first cleared a still-live Dish. Existing
clipboard contents/history are outside this old-Copy-action proof.

## Trigger feasibility and proposed approval scope

[Android authorization](https://developer.android.com/identity/authorization)
documents one-hour access tokens and reacquisition through `authorize`.
Inference from current five-minute idle/ten-minute total lock: waiting a fresh token
out cannot preserve the required foreground session/RAM grant; reconnect may obtain
a new token. Near-expiry SDK reuse is unproven, so no timed attempt for chance expiry.
Restricted Menu/ACL denial proves403, not401. Offline/network/SDK failure/cancellation
also cannot substitute. No scope/client/account change, token cache eviction,
device clock change, token extraction, forged token or error injection is proposed.

**Conditional proposal only, not authorized or executable yet:** after provider
baseline/Cook and a retained-token Sources checkpoint, owner uses a separate device
or desktop browser to remove this app's Google connection for the intended account.
Phone stays in the ready foreground; then one ordinary existing-tab read uses the
already-held RAM token, without another Connect. This is a plausible401 condition,
not a guarantee of immediate propagation or a token-derived identity assertion.

[Google revocation guidance](https://developers.google.com/identity/protocols/oauth2/web-server#tokenrevoke)
states project-wide scope/token invalidation across its clients and warns of delayed
effect. Treat the account's other clients under the same project as potentially
affected; owner must identify the exact connection/project and acceptable impact
before approval. Google access may require fresh consent afterward. Cookbook cache
and encrypted jars are not intended targets, but their preservation still needs proof.
Do not change Menu contents/sharing, revoke unrelated connections or automate account UI.
Any restoration/reconsent must be separately agreed in that concrete approval scope.

## Ordered checklist — no runtime until prerequisites satisfied

- [x] Verify accepted checkpoint/frozen/archive/history integrity and unrelated WIP.
- [x] Review401/403/SDK/network/stale ownership paths and trigger feasibility.
- [x] Record conditional method, potential project-wide impact and approval boundary.
- [ ] Owner selects a permitted genuine401 transition; approve exact connection,
  affected clients and restoration scope if revocation is chosen. Earlier manual
  grant removal for cancellation does not authorize another removal for401.
- [ ] Prepare a separate selected UUID-owned native fixture and bounded host runner;
  reuse GoogleColdProviderUi, GoogleColdBaseline and cache UI helpers. Preserve all
  historical assertions; no production changes without defect evidence.
- [ ] Genuine biometric setup; actual provider metadata/Test tab/rows/schema/literal
  origin, explicit mapping and durable reload; favoriteCook once and real Cook.
  Finish all cache/favorite/recent writes, then freeze complete regular-file cache/
  jars bytes and full RAM/inventory/mapping/favorites/recent before error interaction.
- [ ] Navigate normally to Sources; require old Dish/reveal/lifetime/Copy gone,
  ready secure foreground, retained metadata/tab/token, no busy/pending or stops.
  No Menu input edits, Connect or token/session mutation after this checkpoint.
- [ ] Root compile/lint/relevant tests, independent candidate/runner review and
  immutable source/APK/helper/history/archive freeze before requesting phone actions.
- [ ] Fresh owner readiness and native serial/awake/keyguard/installed identities,
  owner digest only, six historical proof-directory manifests; install-r only if needed.
- [ ] Only then selected runtime and approved external transition; bound marker/UUID,
  native liveness and deadline. Poll native result immediately on every owner reply;
  yield at owner biometric/Google checkpoints. No post-terminal instructions.
- [ ] One ordinary tab read; prove exclusive production401 path above, RAM token
  absent, reconnect UI, same ready session, no stop/pending; exact cache/jars and
  complete RAM/inventory equality before any subsequent Cook or cache interaction.
- [ ] Evaluate fresh cached Cook separately after preservation; do not toggle favorite
  twice or claim byte preservation after legitimate recent writes.
- [ ] Runner requires selected JUnit start/end, terminal/anchored OK, exact UUID/phase,
  native result and no watchdog/orchestration failure, owner/source/APK/history/
  archive/cleanup audits. Host watchdog cannot prove native instrumentation stopped.
- [ ] Independent runtime review; retain every result, update scoped docs, commit/push
  main as already authorized. Whole Mobile stays Partial; no archive.

## Classification and present limits

Only the full401 provenance/preservation conjunction qualifies Passed. A successful
read means401 NotExercised and may legitimately refresh cache; never relabel success
as refusal or apply a failed-read byte-preservation claim across a successful write.
403, grouped network/other HTTP and SDK outcomes receive no401 credit. A dirty stop,
failed assertion or unconsumed callback remains Failed/Unknown as appropriate, never
silently skipped. Natural lock ends this retained-grant attempt; no bypass or auto-retry.
Delayed revocation is recorded; no repeated unbounded polling or grant reuse loops.

No new native candidate/build/test/runtime is claimed. Host audit and official docs
review do not replace actual provider acceptance. Code deferred until trigger choice
makes its acceptance path concrete; no speculative production instrumentation needed.

Unresolved: permitted actual401 transition, exact connection/project impact and
restoration approval if revocation chosen; then reviewed candidate and fresh owner
readiness. Historical causes and broader Mobile acceptance remain unresolved.
