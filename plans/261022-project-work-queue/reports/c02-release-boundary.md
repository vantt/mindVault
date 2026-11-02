# C02 — release boundary decision packet

Draft date:2026-10-26;owner decision recorded2026-10-27, Asia/Saigon.
Status:**Done as owner decision — verification/publication gates remain Pending**.
[Original independent draft review Passed](c02-independent-review.md).
The original preparation below granted no approval by itself. The owner's later
reply approves the four scoped proposals recorded next;runtime/support/Sync proof,
account actions and publication still require their own evidence/authorization.

## Owner-approved decision —2026-10-27

Owner replied “ok em làm đi em” to the immediately preceding four explicit proposals.
This authorizes the associated local version/package work and preparation:

| Field | Approved choice | Remaining evidence/boundary |
|---|---|---|
| Version |Manifest2.3.1,tooling package2.3.1;beta display label consistent with chosen channel |Fresh artifact/hash/provenance and affected-evidence assessment;2.3.0 B01 evidence remains historical. |
| Channel |Controlled beta pilot |No account creation,Store visibility/upload/publication,fees,legal approval or deployment authorized. |
| Required support matrix |Windows11 x64,actual Chrome Stable and Chrome116 while retaining minimum116 |Targets for testing,not verified support;pin actual versions/hardware before run. Linux/WSLg ChromeTesting143 does not replace these cells. |
| Sync |M:real two-physical-device/same-ID Chrome Sync acceptance required before publication |C03 owner/device/account readiness and A10/B08 evidence required;no Sync Passed or grant/account action inferred. |

OWN01/C01's separate remaining-bug question was not answered by this approval.
Keep its MissingRepro/owner gate open;do not call A23 or stable A13 complete.
The prior desktop slot ended and was returned;new GUI needs a fresh handoff or
proven isolation. KDF,storage format,permissions,identity key and min116 stay unchanged.

Local follow-through:[2.3.1 metadata/held artifact verification](c02-version-231-local-verification.md)
and [independent review Passed](c02-version-231-independent-review.md). Manifest/
package are now2.3.1;the new development ZIP is `278b59df8ddef0085c9c064da11485695074419894c3f43b5077a10fc44f1e9e`.
Only manifest metadata differs inside the74-file runtime payload versus2.3.0.
The bounded Vitest artifact-discovery repair preserves all canonical assertions.
This does not transfer old2.3.0 B01 browser results to new2.3.1 candidate bytes.
[B03 preparation reviewed](b03-preparation-independent-review.md),all runtime NotRun.
[A13 held provenance reviewed](a13-held-independent-review.md),stable gate C01/A23 retained.

## Preparation checklist

- [x] Bind proposal to current source/evidence and official version/storage guidance.
- [x] Present four bounded owner fields and consequences without choosing for owner.
- [x] Check local links/JSON/status consistency.
- [x] Independent draft review Passed;proposal/dispatch updated before owner call.
- [x] Later owner approval recorded above for all four explicit proposals;verification gates retained.

## Original draft baseline evidence —2026-10-26

[B01 Run01](b01-run-01.md) and [independent review](b01-run-01-independent-review.md):
3selected source-patch cases Passed33.046s (14.402/13.627/4.354s), on Linux WSL2/
WSLg using cached Chrome for Testing143.0.7499.4. Actual Options export/import and
popup-tab Builder/Settings passed; no Windows/macOS/Chrome Stable/116 support,
native/clipboard/provider/ZIP-upgrade/Sync/full-candidate conclusion.

Current [manifest](../../../chrome-extension/src/manifest.json):2.3.0,
`minimum_chrome_version:116`, no key; [package](../../../chrome-extension/package.json):1.0.0.
Corresponding statically validated development ZIP (not loaded as an extracted ZIP
in B01) SHA256 `8e921455f31f4ba6d749242c94d3b9f1b17f964656fdca2fc1d7dc449d92f7b7`.
The116 declaration is an installation/API floor, not tested compatibility evidence.
PassChef, Closed source, A2/lifetime model and passchef.com/Cloudflare remain
[settled](../../../docs/commercialization/operations/publisher-and-support.md);
no price/fee/domain decision is reopened. Final publisher/visibility/URLs remain C04/C05.

Root's read-only Windows OS query (`Get-CimInstance Win32_OperatingSystem`, selected
Caption/Version/BuildNumber/OSArchitecture only) reports Windows11 Pro N,
version10.0.26200/build26200,64-bit. This identifies the available host OS,not a
Windows Chrome launch or compatibility result. The B01 browser was still Linux/WSLg.

## Original four proposal fields — before the owner approval above

| Field | Concrete proposal / alternatives | Consequence of approval |
|---|---|---|
| 1.Version |Propose manifest `2.3.1`, optional display `version_name:"2.3.1 beta"`; align tooling package version to2.3.1 as an explicit companion proposal. Owner may choose another numeric target or keep development2.3.0 pending a release. |Only after approval:scoped version patch and fresh artifact. Existing2.3.0 B01 proof stays historical; no automatic file write here. |
| 2.Channel |Propose controlled **beta pilot**. “Beta” is product release labelling, not an assumed Store channel/visibility configuration. Alternative:stable only after required selected release gates pass. |C04/C11 still select/authorize actual distribution/visibility/submission; approving beta does not authorize account creation/upload/publication. |
| 3.Required Chrome/OS matrix |**Narrow proposal N:**Windows11 x64 + actual Google Chrome Stable, exact version pinned before freeze; retain Linux WSLg/ChromeTesting143 as existing source-regression reference. **Wider option W:**N plus macOS15 arm64 and Ubuntu24.04 x64 on actual Chrome Stable, exact versions recorded. These are targets, not verified support or available devices. |B03 performance/fail-closed/support and affected B01/B04/B07/B08 run on required cells. Stable-only proof does not certify116:include declared116 in lowest-version testing, or explicitly approve a narrower floor/support policy and candidate change. No floor change made here. |
| 4.Sync publication gate |Recommend **M:required real cross-device acceptance before publication**. Alternative **D:explicit owner-approved disclosed deferral** of broad cross-device acceptance for this release. |M requires A10-selected real2physical-device/same-ID/account C03+B08 evidence. D records exact deferred cases/known limits in packet/listing/reviewer guidance, with accepted remaining security/data-preservation cases; it does not mark Sync Passed or erase B08upgrade requirements. |

For N/W, owner can name another available concrete OS/version/hardware set; record
exact chosen cells before testing, not a universal platform promise. macOS/Ubuntu
targets may need additional resources; no VM/device/Chrome install authorized here.

## Sync and identity consequences

[Storage adapter](../../../chrome-extension/src/adapters/infrastructure/chrome_storage_adapter.js)
uses `chrome.storage.sync` directly. D does **not** disable Sync, make a local-only
backend, guarantee offline-only use, conflict merge or safe remote rotation.
Draft disclosure if D is chosen: “The encrypted Cookbook uses Chrome Sync.
Cross-device conflict, rotation and recovery acceptance is deferred for this beta.
Keep protected recovery material; profile export is not a whole-Cookbook backup.”
Finalize wording against the actual approved case boundary; no unsupported guarantee.

[A10 protocol](a10-sync-upgrade-protocol.md) has13real Sync/upgrade cases still NotRun.
Same-ID preparation can use an approved public identity without prior publication;
account/key/upload actions need their own authorization. Version/identity/floor/
manifest changes create new bytes/hash and may change namespace/upgrade behavior:
preserve8e92 proof, assemble A13 with chosen source/provenance/ID, run B07/B08 and
assess affected prior evidence. Do not silently transfer all2.3.0 results to2.3.1.
The original A10/A06/FA01 development artifacts referenced by the26October draft
were all2.3.0;they could support only same-version source replacement rehearsal,
which has not been executed. The later approved2.3.1 artifact has separate provenance;
they cannot prove a greater-version installed update or Store delivery.

## Original draft official guidance and local checks —2026-10-26

Checked2026-10-26; docs-seeker detect/fetch returned no Context7 result, then read
official Chrome [version](https://developer.chrome.com/docs/extensions/reference/manifest/version),
[key](https://developer.chrome.com/docs/extensions/reference/manifest/key) and
[storage](https://developer.chrome.com/docs/extensions/reference/api/storage) pages.
Version uses numeric components; `version_name` is descriptive, not update ordering.
Public key supports consistent development ID; Sync depends on enabled browser
syncing and is asynchronous. Proposed beta/floor/matrix policy is our draft, not
a Chrome policy assertion or observed support result.

Only this report edited. Local JSON checks confirm2.3.0/1.0.0/min116/no key;
proposal2.3.1 is numerically greater. Seven local targets, four pending fields,
settled-scope/status checks and `git diff --check` Passed. No source version/key/candidate change, browser/device/
account/Store action or tests replayed. Independent review precedes owner call.

## Original owner reply template — retained as preparation history

```text
1. Version: 2.3.1 + align package / other: ...
2. Channel: controlled beta pilot / stable / other: ...
3. Matrix: N / W / explicit OS+Chrome cells and minimum-version policy: ...
4. Sync: M mandatory before publication / D disclosed deferral (exact cases): ...
```

Separate C01 question remains:are other owner-reported blockers still present?
If yes, provide version, expected/actual behavior and reproduction. The four C02
answers do not certify OWN01 resolved or approve a complete blocker list.

## Unresolved

Remaining OWN01/C01 symptoms/acceptance,exact A13 candidate/identity and actual
WindowsStable/116,Sync/upgrade/full-suite/legal/hosting/publication evidence.
The four C02 choices are recorded;they do not mark these outcomes Passed.
