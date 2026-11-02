# A10 — two-device Sync and upgrade protocol

Date:2026-10-26, Asia/Saigon. Source:HEAD039c76d + preserved WIP/A06–A09/A23-FA01.
Status:**Done2026-10-26 — protocol preparation**;
[independent review Passed](a10-independent-review.md).
All real Sync/upgrade cases NotRun. No browser/account/network publication action.

## TODO

- [x] Bind protocol to actual storage/rotation/import/migration behavior.
- [x] Check official Chrome storage/identity/update guidance and prerequisites.
- [x] Define safe synthetic fixture, per-case evidence, timeout and preservation.
- [x] Separate two-device Sync, unpacked source replacement and installed upgrade.
- [x] Verify local fixture JSON/Recipe, links and source/artifact identities.
- [x] Independent review Passed; registries updated; C02/C03/A13/B08 execution remains Pending.

## Readiness and identity — fill before execution

Use two **physical Chrome devices**, A and B, with a dedicated owner-controlled
test Google account signed into both and the relevant Chrome Sync settings enabled.
Two local profiles, JSDOM, mocked storage or copied JSON do not prove Sync. Owner
handles account creation if needed, login/2FA, device/network changes and grants;
this protocol authorizes none of those actions. Confirm both test namespaces are
empty of real Cookbook data before setup; a fresh local profile alone is insufficient.

Record device alias/OS, exact Chrome version/channel, Sync/account alias and enabled
data settings, installed extension ID/version/source and package hash on each device.
No emails/tokens/passwords/session keys in reports. Matching same-account and
same-ID are prerequisites; don't initialize independent Cookbooks on both devices.

Same-ID preparation can use an already approved item's/public signing identity,
or an owner-approved unpublished Dashboard draft upload to obtain its public key.
[Chrome's key guidance](https://developer.chrome.com/docs/extensions/reference/manifest/key)
explicitly supports obtaining that key without publishing. An approved development
manifest containing the common public `key` must then yield identical installed IDs
on A/B. Private signing material stays private. If a prepared manifest differs from
today's ZIP, create/record its own reviewed hash/inventory; don't reuse today's hash.
No key is present in current source. Path-derived unpacked IDs must not be assumed
equal across machines. This prepares a local cross-device experiment; only actual
convergence proves its installation mode supports the required Sync behavior.

Store publication is not a prerequisite to write this protocol or prepare identity.
Any Dashboard upload, signed package, managed installation or hosted update route
requires the separate owner authorization/channel decision. C02/C03 record the
chosen permitted route, two devices and test slot. On Windows/macOS, don't assume
an arbitrary local CRX is installable; [Chrome distribution guidance](https://developer.chrome.com/docs/extensions/how-to/distribute/install-extensions)
limits those routes. If prerequisites fail, mark Blocked and keep real Sync NotRun.

## Source contract and official facts

Checked2026-10-26; docs-seeker detection/fetch returned no Context7 result, then
official Chrome primary pages were used. No runtime behavior inferred as Passed.

- [Storage](https://developer.chrome.com/docs/extensions/reference/api/storage):
  enabled Sync propagates extension data between signed-in browsers; disabled
  Sync acts locally and offline writes resume syncing later. Writes are asynchronous;
  approximate Sync quotas are100KB total/8KB per item. Inspect actual API quota
  constants/errors on tested Chrome; no network convergence deadline is guaranteed.
- Session storage clears on extension disable/reload/update/browser restart;
  local storage clears on removal. Record those areas separately; do not expect
  session keys/auth authority to sync. Ordinary worker suspension is a distinct event.
- [Version semantics](https://developer.chrome.com/docs/extensions/reference/manifest/version)
  compare manifest numeric version components; an installed update needs a newer
  version. package.json's tooling version is not Chrome's installed version.

| Actual source | Bound expectation/risk |
|---|---|
| [Background setup](../../../chrome-extension/src/security/background-security.js)L12–26 |Sync `salt`, encrypted own `profile:NAME`, default and Menu map; setup writes one initial Cookbook. Never run setup on B while A's data is still arriving. |
| [Storage reader](../../../chrome-extension/src/adapters/infrastructure/chrome_storage_adapter.js)L26–108 |Own assignment→shared Menu match→default routing; AES record decrypts to `{secrets,settings}`. Same salt/ciphertext/context must yield same Dish after local unlock. |
| [Profiles](../../../chrome-extension/src/options/profile-management.js), [routing](../../../chrome-extension/src/options/profile-rendering.js) |Profile changes and shared `sheetMapping` object writes are separate operations; no application-level cross-device merge protocol. |
| [Password change](../../../chrome-extension/src/options/options-password-change.js)L19–50, [rotation writer](../../../chrome-extension/src/security/rotation-writer.js)L4–22 |Fresh salt/re-encrypted profiles, local expected-state check; not a distributed transaction/conflict resolver. Master rotation preserves Spices/Dishes; inspect remote mixed salt/records and stale sessions explicitly. |
| [Security listener](../../../chrome-extension/src/security/background-security.js)L104–108 |Listens for removed local session key, not Sync salt/profile changes. Do not promise remote rotation auto-locks an already-unlocked device. |
| [Full Access writer](../../../chrome-extension/src/options/import-profile-writer.js)L4–34 |New imports locally repaired; same-name/profile and Menu-map overwrites remain. Export is not a full Cookbook/settings/default/map backup or old-record repair. |
| [Migration method](../../../chrome-extension/src/adapters/infrastructure/chrome_storage_adapter.js)L131–147 |Contains flat-v1 envelope migration logic, but method existence is not proof the candidate invokes it. Legacy-format acceptance needs its actual historical producer and observed entry path. |

Use the [A04 data matrix](../../../docs/commercialization/legal/permissions-review.md)
for complete metadata/local/session declarations. UI01 Settings defaults remain
C01; this protocol checks stored values without claiming the disconnected controls work.

## Synthetic fixture and evidence

Use only disposable test Cookbooks/Menus. A alone creates the Cookbook using a
strong synthetic test master held privately by the owner. Profiles `A10Practice`
and `A10Other`; jar1=`Basic*`, jar2=`Practice🙂漢字`, jar3=`Public$3`,
jar4=`Fixture@4`, jar5=`Demo^5` are public fixtures, never real credentials.
Give Other a distinct public jar1=`Other*`. Map two owner-authorized synthetic
Menu IDs to their respective profiles; default=`A10Practice`. Recipe=`r4nd0m#1`:
expected own Dishes `Basic*r4nd0m`/`Other*r4nd0m`. Include a Tag produced by the
actual Builder for each Menu; preserve it and test wrong-context refusal.
`A10_SYNTHETIC_MENU_A/B` below are placeholders, not valid Google IDs or access grants.

```json
{"profileNames":["A10Practice","A10Other"],"defaultProfile":"A10Practice","sheetMapping":{"A10_SYNTHETIC_MENU_A":"A10Practice","A10_SYNTHETIC_MENU_B":"A10Other"},"recipe":"r4nd0m#1","expectedDish":"Basic*r4nd0m"}
```

Before each destructive/concurrent case, save a protected **test-only** full Sync
snapshot with salt, every own/shared encrypted record+IV+metadata, default and all
assignments/settings. This is an operator test safeguard, not a product backup UI.
Record canonical sorted-key JSON hashes plus per-key lengths/bytes and
`getBytesInUse(null)`; compare actual values on both devices in protected evidence.
Never include session JWKs or real secrets. Keep independent original fixture and
master recovery material; Full Access export alone cannot restore all these fields.

Each case record:ID/time in Asia/Saigon plus raw UTC, source/package/installed ID+version per device, prior
snapshot hash, steps/writes and timestamps, observations at both devices, errors,
post hashes, exact synthetic Dish/routing/Tag results, cleanup and verdict.
All cases start **NotRun**. Passed requires every stated preservation assertion;
Failed records mismatch/loss; Blocked records missing prerequisite; Deferred requires
explicit C02 decision and disclosure. Observation alone is not release acceptance.

Bound each online settle attempt to10minutes, read at30second intervals without
repeated writes. Require matching snapshots on3consecutive reads and successful
local unlock/cooking. This is a test budget, not a Chrome SLA. At timeout retain
both snapshots/events, mark Failed-to-converge within budget (cause unknown), stop
dependent cases; any approved later retry is a separate run, never silently Passed.

## Per-case execution — all NotRun

Start each destructive case from a verified baseline or a fresh isolated rerun;
do not chain onto a damaged Cookbook. Normal edits use actual UI; low-level quota/
storage instrumentation is separately approved, synthetic and clearly labelled.

| ID | Steps | Required observations / stop condition |
|---|---|---|
| S00 transport prerequisite |Only in C03-authorized fresh namespace, approved tooling writes one nonsecret disposable marker A→B then B→A, observing the bounded settle policy; remove it and confirm removal. |Actual transport/installed IDs/Sync grant work in selected installation mode before Cookbook setup. Failure blocks S01; a marker pass is not Spice/Cookbook acceptance. |
| S01 online baseline |A creates fixture; B remains locked until salt/profiles/default/map settle. Unlock B locally. Repeat a harmless A→B then B→A profile change. |Exact encrypted records/salt/metadata converge; Unicode jars retained; correct own Dishes/Tags and default/map on both. No second setup, no session-key transfer. |
| S02 offline |After S01, owner takes B offline. A changes Practice jar1 to `Online*`; B must retain offline baseline. Reconnect B, settle, lock/reunlock. Repeat offline edit from A with B online. |Queued changes eventually observed in both directions; expected Dish matches observed jar. No salt/profile/default/map loss. Don't call stale offline data a Sync failure before reconnect. |
| S03 disjoint edits |Both offline from same baseline; edit different named profiles, reconnect in recorded order; reverse order in fresh run. |Record resulting keys/ciphertexts and no silent unrelated profile/map loss. Distributed merge not promised. Any required lost edit blocks chosen Sync boundary. |
| S04 same-record conflict |Both offline edit different jars inside the same profile; reconnect A→B, then reverse in separate fresh run. Also concurrent distinct Menu assignments (same mapping object). |Retain both pre-conflict snapshots; record complete winner/loser, unexpected mixture and exact Dish. Never infer field-level merge/last-writer rule from one run. Loss is a recorded limitation requiring C02 disposition, not automatic Pass. |
| S05 rotation |S01 baseline; rotate master on A while B locked, settle; B old master must fail/new master succeed with unchanged Dishes. Fresh reruns: B already unlocked; then B offline with queued edit during rotation. |Salt/ciphertext change together in final usable state; all profiles/maps/default unchanged; record any transient decrypt error/stale session/Dish. Do not require unsupported auto-lock claim or hide it; required safety/convergence failure blocks. |
| S06 quota/error |Record actual quota constants/bytes. Approved test tooling attempts one oversized disposable prefixed item, or UI synthetic profile save reaching per-item quota; never flood writes. Preserve baseline first. |Actual API rejection/error and no false successful durable save or prior-data loss. UI-only and instrumented outcomes separately labelled. Total quota/write-rate stress is Deferred unless C02 explicitly selects a bounded disposable run. |
| S07 loss/recovery |After snapshots, owner closes/restarts B or uses another empty authorized test installation with same ID/account. Do not reset A. Observe hydration before unlock. |Salt/all records/maps/default arrive, correct master cooks exact Dishes; local/session areas assessed separately. Forgotten master/Spices has no developer recovery. Uninstall/Reset is not a promised remote recovery/delete mechanism. |
| S08 import recovery |With approved candidate, export synthetic Full Access and import under new unused name into an isolated test Cookbook. Compare exact jars/Dishes/Tag/context after reload. Also legacy derived import. |FA01 new own-envelope works in real browser; unrelated fields remain. No assertion all settings/default/maps restored, no repair of old malformed imports; legacy Dish compared to its actual original derived baseline. |

## Upgrade and preservation — three different claims

| Lane | Artifact/identity record | Meaning |
|---|---|---|
| Local source replacement |Frozen A06 ZIP2.3.0→A23 development ZIP2.3.0, same installed ID if deliberately maintained. |Only reload/source/format smoke rehearsal; neither a Store update nor numeric version upgrade. |
| Installed version upgrade |C02-approved historical old ZIP/CRX+format→A13-approved new ZIP/installed artifact, **same ID**, new manifest version greater than old; exact source+SHA for both and permitted delivery route. |Can assess real installed upgrade/preservation. If route is unpacked version replacement, label it so; it is not Store delivery. |
| Store delivery |Actual approved old/new Store packages/item ID and observed update event/version. |Store update acceptance needs that route's own evidence; local loading cannot substitute. No publication authorized here. |

Current reference artifacts (not approved candidates):

- [Frozen A06 ZIP](../../../chrome-extension/build/a06-frozen/passchef-extension-2.3.0-development.zip):
  73files/1,102,759bytes/SHA256
  `6c6fc80055c61d49cba404694fa8691a1c41316bccc2a14b491e930995fc0fe9`.
- [Preserved FA01 development ZIP](../../../chrome-extension/build/a23-fa01-frozen/passchef-extension-2.3.0-development.zip):
  74files/1,103,161bytes/SHA256
  `b4986a20b48da3b7a519c09b02ed19ebd409ace8aeb50781cf61b7fbd1fac4de`.
- Both manifest2.3.0; tooling package1.0.0. Neither has a manifest key. C02 chooses
  release version/channel; this report changes neither. A13 records installed IDs,
  final manifests/resources/notices/provenance and chosen formats before B08.

U01:populate old version with fixture; snapshot/cook/tag all selected own/shared
profiles; set nondefault routing/language. Record accepted legacy source format;
do not manually manufacture a historical storage format and call it migration.
U02:install/update by selected permitted lane without uninstall/Reset; record
observed old/new installed IDs+versions, update/reload event and artifacts.
Before any post-update write, compare Sync salt/records/default/maps byte-for-byte
unless a documented reviewed migration explicitly requires a transformation.
Session clearance/re-authentication is expected on extension update; encrypted
Cookbook and local records (including auth attempt/onboarding state) must be
assessed separately and preserved. Worker wake alone is not extension update and
must not restart the original session deadline. Unexpected setup prompt,
salt replacement, decrypt failure, missing jar/profile/map or changed Dish is Failed;
stop before setup can overwrite evidence. For a claimed migration, inspect every
documented transformed key plus exact Dishes, and prove the entry path ran.
U03:locally unlock with same master; verify all selected Spices/profile routes,
own/shared Dishes and Tagged wrong-context refusal. Run affected security/Menu/
native Cook/visible Dish/explicit Copy/session/sharing/QR/EN–VI cases through B08's actual
selected acceptance records. A checklist reference does not mark them Passed.
U04:repeat required Sync S01/S02/S05 on the new candidate, then independently
assess S08 recovery. A new installation, source reload and upgrade remain separate
verdicts. Any downgrade/rollback is a separate approved experiment; don't assume
older code can safely read newly written data or use uninstall as rollback.

## Cleanup and failure preservation

On failure stop writes, disconnect the affected disposable installation if owner
approved, retain both devices' snapshots/events and exact packages, then diagnose.
Do not auto-merge, rewrite salt, reinitialize or retry rotation over mixed records.
Use the saved full synthetic snapshot only with an approved restoration procedure;
otherwise rebuild the fixture in a fresh isolated test namespace and record a new
baseline (new salt/IV/hash). Never represent manual rebuilding as successful restore.

After evidence/owner signoff, remove only synthetic test data using the approved
method, restore prior test settings/network state and verify both devices. Reset
clears Sync/session locally in source and may propagate changes; uninstall/local
deletion does not prove server/other-device deletion. Record cleanup convergence or
remaining replicas explicitly. No production Cookbook or only recovery copy is touched.

## Local verification and delivery

Only this report edited. Actual source/JSON checks found manifest2.3.0, package1.0.0,
no `key`; read-only SHA/size checks matched both reference ZIPs above. A JSON
roundtrip of the synthetic fixture passed. Node24.18.0 inline production
`RegexParserAdapter`→`GeneratePassword` with an explicit synthetic storage boundary
cooked `r4nd0m#1`+`Basic*` to exactly `Basic*r4nd0m`; this is not Chrome/Sync proof.
Existing runtime/tests/Golden unchanged; no repeated unit suite on a document.

Commands:docs-seeker `detect-topic.js`/`fetch-docs.js` (no Context7 documentation),
official page reads above; `node --input-type=module` fixture roundtrip/parser/cook/
manifest/hash check; local Markdown target/line-anchor/protocol coverage check;
`git diff --check`. Local checks Passed:12Markdown targets, one JSON block,
13case IDs and every cited source line range; fixture/cooking/hash command exit0,
diff check exit0. Actual real-case verdicts remain S00–S08/U01–U04 **NotRun**.
No Chrome/account/login/2FA/provider/clipboard/Sync/update/GUI/ADB/Gradle/publish/push.

## Unresolved

Dated artifact amendment2026-10-26:the referenced FA01 package is preserved at
`build/a23-fa01-frozen/` before the subsequent owner-approved UI01 source change.
Its hash/results above are unchanged; [UI01 evidence](a23-display-defaults-deferral.md)
records the later development artifact. Neither package is an approved A13 candidate.

C02 version/channel/support/Sync boundary; C03 two physical devices and owner
account/login/2FA/authorization; A13 exact old/new candidate and identity;
B08 actual Sync/upgrade acceptance. A23 UI01/C01 and affected browser acceptance remain.

## Dated selected local execution followup —2026-10-30

[Selected B08 localunpacked numeric2.3.0→2.3.1](b08-artifact-acceptance.md)
Passed/[review](b08-upgrade-20261030-independent-review.md):samepath/ID/profile,
allSync/local snapshots unchanged beforeunlock andafter,10jars/hints/2Menus/Otherdefault/VI/
4mappedDishes+Tags preserved. Exact oldpatched8e92/new278b archives,2nativePIDexits;ownedcleanup complete.
This is a selected U01/U02/U03 local lane,not whole historical-format/Store delivery
or U04/realSync acceptance. Original preparation NotRun states above retain their
date;[owner Sync slot later](c03-sync-owner-readiness.md),mandatory beforepublication unchanged.
