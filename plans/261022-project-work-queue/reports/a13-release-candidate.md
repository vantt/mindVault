# A13 — held local artifact provenance

Date:2026-10-27, Asia/Saigon. Status:**Held provenance verified — independent review Passed**;
[review](a13-held-independent-review.md);
**A13 NOT Done — stable candidate gate held**. No additional artifact or runtime action in this A13 step.

## TODO

- [x] Verify existing2.3.1 ZIP/root manifest/source inventory/provenance/notices.
- [x] Bind held record/source/test hashes and distinguish prior verification from new runs.
- [x] Record blocker/evidence reuse limits and concrete B06/B07/B08 prerequisites.
- [x] Independent held-provenance review Passed;manager/owner handoff retains A13 stable gate.

## Exact held identity

Source base:HEAD `039c76d17a2765bd753e09a7009e004dadfd6638` plus dirty preserved
WIP/FA01/UI01/approved metadata. **Not a clean release commit**;74per-file hashes
in inventory/held record bind actual bytes. This report reuses the C02 held capture;
no additional source freeze/repackage is performed by this A13 step.

| Item | Existing identity / location |
|---|---|
| [Development ZIP](../../../chrome-extension/build/passchef-extension-2.3.1-development.zip) |74files,1,102,457bytes;SHA256 `278b59df8ddef0085c9c064da11485695074419894c3f43b5077a10fc44f1e9e`. |
| [Inventory](../../../chrome-extension/build/passchef-extension-2.3.1-development-inventory.json), [SHA sidecar](../../../chrome-extension/build/passchef-extension-2.3.1-development.zip.sha256) |All runtime names/sizes/SHA,excluded inputs,external font-data declaration and dependency provenance. Inventory SHA `d3758ab96085138f0b6d334369221b55831063784cbbf6bd0758b45a8b39b071`. |
| [Held record](../../../chrome-extension/build/c02-version-231/held-record.json) |SHA `0d7e97303bdd67040710a37bb514ca893e40fee30797364a9264556b9f0cd336`;74source/46test hashes,artifact copies,metadata/lock/config/logs/open gates. |
| [Manifest](../../../chrome-extension/src/manifest.json) at ZIP root |Numeric2.3.1,display`2.3.1 beta`,minimum116;no identity key. Root files:manifest.json,service_worker.js,i18n-loader.js;no outer src folder. |
| [Package](../../../chrome-extension/package.json) / [lock](../../../chrome-extension/pnpm-lock.yaml) |Package aligned2.3.1;dependency lock unchanged SHA `f1fb64ebc7088d3cf327f9b2e1c42cf1b22dc19e4b920ad1735bbe340980712b`. |

No CRX/signing key/Store identity or publication. Current loader path-derived ID
must not be assumed stable across machines. This held ZIP is an input for later
candidate disposition,not an accepted stable candidate or installed artifact.

## Dependency and notices retained

Argon2-browser1.18.0/lock integrity and JS/WASM hashes are recorded in inventory;
no dependency upgrade/KDF fallback/format change. WASM SHA
`0c2149886c13e4eae4a6ca25ee71d47423c5c8740a874cf04ff816d1b2c901d7`.
Archive contains `vendor/argon2-browser-LICENSE.txt` (MIT notice SHA
`524a1d77701975a063a590424637abbd7fa519042a113f2bbe2eaf4cde76296d`)
and `vendor/README.md`;Nayuki QR embedded MIT header remains in `vendor/qrcodegen.js`
SHA`79f419f267ce5a80d97f8099e0a789a4ecc4697b5348d86371a1f3b2a75d6e03`,
with pinned upstream commit in [vendor provenance](../../../chrome-extension/src/vendor/README.md).
No top-level product LICENSE is shipped;historical root LICENSE was not rewritten.
Closed-source product direction/third-party notices remain distinct;C04legal approval is separate.

## Verification and blocker ledger

[2.3.1 delivery](c02-version-231-local-verification.md) and
[independent review](c02-version-231-independent-review.md) retain final exact
default151/151unit/21files,49/49package checks,frozen offline install and syntax/
resource validation. Initial frozen-copy discovery failure and bounded Vitest
artifact exclusion remain recorded. These tests were **not rerun** for this report.

| Gate / issue | Current disposition |
|---|---|
| FA01 Full Access |Own-envelope repair plus selected2.3.0 B01 mapped/no-Menu actual import→Builder verified;old malformed records not migrated. |
| UI01 Display defaults |Owner-approved temporary deferral,retained data and selected2.3.0 EN→VI→EN Settings verified;global defaults behavior remains deferred. |
| [B01 Run01](b01-run-01.md) |3/3Passed33.046s on Linux/WSLg ChromeTesting143 using working2.3.0 source. Not fresh2.3.1,Windows/116,toolbar/native/provider/ZIP/upgrade/Sync or final full suite. |
| [OWN01/C01](c01-owner-blocker-decisions.md) |MissingRepro/remaining owner bug question open;four C02 approvals did not answer it. |
| [A23 rollup](a23-blocker-repairs.md) |Partial;scoped verified fixes do not close remaining blockers or stable A13 gate. |
| [C02](c02-release-boundary.md) |Decisions Done:controlled2.3.1 beta,Win11x64 actual Stable+116 while floor retained,real two-device Sync mandatory before publication. Required evidence Pending. |

Separate read-only ZIP comparison confirms same74member set as old2.3.0
`8e921455…d92f7b7`;only manifest metadata differs,other73runtime files byte-identical.
Old B01 proof remains historical. Reuse assessment may trace unchanged logic,
but no blanket transfer to new version/identity/delivery/platform or performance.

## Concrete downstream handoff

- **A13 stable disposition:**resolve OWN01/C01/A23 required blocker list,identify
  exact reviewed source/dirty state or release commit and freeze final manifest/
  identity/provenance. Current gate stays held;don't pre-mark B07/B08 accepted.
- **B06:**stable UI/candidate disposition,A08five-shot brief and approved desktop
  slot;actual synthetic captures/captions/dimensions/rights,not draft artwork.
- **B07/B01 full suite:**exact final source/ZIP hashes,approved desktop or proven
  isolation,clean extracted-ZIP resource/WASM/offscreen smoke and actual full-suite
  results/cleanup. Working-source selected B01 is not extracted-ZIP acceptance.
- **B03/B04/B08:**[B03 protocol](b03-browser-measurements.md) load/control/measurement
  runner first;required Windows11 x64 actual Chrome Stable and116,appropriate native/
  clipboard/security/upgrade cells with frozen tools and resource handoff. No support
  inferred from a binary's metadata or old WSLg143 test duration.
- **C03/Sync M:**[A10](a10-sync-upgrade-protocol.md),two physical Chrome devices,
  same account/ID/candidate,owner login/2FA and explicit data-Sync/identity route
  readiness;actual preservation/conflict/rotation/recovery evidence before publication.
  No key/account/upload action inferred. Backend remains chrome.storage.sync.

Any source/version/key/permission/dependency/artifact mutation changes proof identity:
preserve old hashes,refreeze/review and assess affected reruns. Same-version reload
is not numeric/Store upgrade;support/Sync/hosting/publication retain separate gates.

## Original read-only checks and owner handoff — before proceed

Only this report added. Python stdlib ZIP CRC/root/74source equality,held record/
all held-file hashes,46test hashes,license/header/provenance/package metadata and
manifest-only diff checked;`git diff --check` exit0. Sixteen local targets/status checks
Passed. No packaging/tests/download/browser/GUI/input/clipboard/Windows/phone/
ADB/Gradle/account/Sync/signing/hosting/publish/deploy action.

**One remaining owner question for root to ask after review:**besides FA01/UI01,
are any other known bugs or unfinished UI still blocking release? If yes,record
version,expected/actual behavior and reproduction;otherwise record explicit scope.
This report does not send that question or certify the answer.

## Unresolved

Remaining OWN01/C01 symptoms/acceptance and overall A23;stable candidate freeze,
Windows Chrome Stable/116/B03,B01full suite/B06/B07/B08,real two-device C03/Sync,
publisher/legal/hosting/publication gates. Root owns the remaining owner question.

## Later owner proceed and actual provisional verification —2026-10-27

Owner “làm luôn đi” authorizes testing the known FA01/UI01 scope without repeating
the no-other-bugs question;absence of other bugs is unconfirmed,not a prerequisite
to provisional testing. [Exact heldZIP full151unit+28E2E and same-ID WASM fault](b07-zip-suite.md)
Passed/[review](b07-run-01-independent-review.md);[native Windows Stable156 P0](b03-native-stable-run-01.md)
Passed/[review](b03-native-stable-independent-review.md). No candidate byte changed.
Cleanup complete;desktop returned. A13 stable disposition is still held/NOT Done;
Windows116/metrics/full matrix,upgrade and [owner-scheduled later real Sync](c03-sync-owner-readiness.md)
remain separate requirements. No repeat C01 answer blocks provisional verification.

## 2026-10-30 selected measurement/upgrade followup

[B03selectedWindows measurements Done/reviewed](b03-measurements-20261030-independent-review.md)
and [localunpacked numeric upgrade Passed/reviewed](b08-upgrade-20261030-independent-review.md)
add scoped evidence to exact held278b artifact;source74/tests46 unchanged. Allowned
process/profile/workspace cleanup complete,desktop returned. A13 stable disposition
remains held/NOTDone;Store/shared-format/realSync/publisher/owner gates are separate.
No other-bugs answer is required to perform already-authorized provisional tests.
