# B03 — independent measurement-preparation review

Date:2026-10-27, Asia/Saigon. Reviewer:independent agent,not protocol author.
Scope:[protocol](b03-browser-measurements.md) and ignored
[resource proof](../../../chrome-extension/build/b03-preparation/resource-proof.json).
Verdict:**Passed for preparation only**. All actual browser/support/performance
cases remain NotRun;runner/observers/fault driver remain MissingValidExercise.
No helper,source/config/test/registry change or runtime approval by this review.

## Browser route and resource boundaries

Required C02 cells remain actual Windows11 x64 Chrome Stable plus116-floor
coverage. Cached Linux ChromeTesting143 and the old B01 source passes do not
replace those cells. Root-attributed Windows CIM/version metadata and a present
chrome.exe are correctly distinguished from live brand/channel/signature/support.

Reviewer independently read the installed file's size4,522,648bytes and SHA256
`480f275b63d1ea39113cbc8344ac395578acc25b432bd39af5fbc77005179aca`.
No executable/version query or GUI launch. Resource JSON SHA256 matches
`0b5ed3e20f0d8fe36d1ca44f64b5b1845634c9dfdee74c7422def4b141a78f19`;
all6runtime categories are NotRun,signature NotChecked,116 not downloaded/launched.

Independent primary-source checks2026-10-27:

- [Chrome team announcement](https://groups.google.com/a/chromium.org/g/chromium-extensions/c/1-g8EFx2BBY) confirms the branded137+ flag removal and distinguishes Chromium/CfT. Existing B01 flags cannot be treated as proven native Stable loading.
- [Load unpacked](https://developer.chrome.com/docs/extensions/get-started/tutorial/hello-world#load-unpacked) documents the proposed dedicated-profile manual route;[debugging guidance](https://developer.chrome.com/blog/remote-debugging-port) requires a nondefault profile for136+ debugging switches.
- [CDP schema](https://raw.githubusercontent.com/ChromeDevTools/devtools-protocol/master/pdl/domains/Extensions.pdl) and current [Puppeteer extension guide](https://pptr.dev/guides/chrome-extensions) provide possible automation APIs. Their existence does not establish this native Windows156 loader/control/measurement exercise. No new dependency or speculative helper is required by this preparation verdict.
- [Official CfT repository](https://github.com/GoogleChromeLabs/chrome-for-testing#json-api-endpoints) documents archived metadata. Web reader could not fetch the two small116 endpoints;independent Node fetch read text/JSON only:116.0.5845.96,revision1160321 and exact official win64 Chrome/Driver URLs match. Zero binary/archive requests. This remains CfT116 metadata,not acquired branded116 or Windows acceptance;required-cell equivalence must be agreed explicitly.

## Measurement and fail-closed protocol review

Actual source confirms Argon2id,t3,m65536KiB,p4,hash32bytes and120s production
offscreen timer. No KDF reduction,PBKDF2 vault fallback or format change. One
worker belongs to each offscreen document;idle closure can end its lifetime.
Protocol correctly requires observed lifetime when grouping warm samples.

P0–P4 cover loader/support,process-cold startup/unlock,warm-browser unlock,sampled
memory and genuine fail-closed controls. Planned10cold/20warm+2recorded warmups
retain failures/timeouts/raw N. Monotonic clock boundaries separate launch/unlock/
KDF measurements;production unlock includes IPC/worker/WASM/AES. No pure-Argon
label is applied to the end-to-end timer.

Windows memory needs owned PID/target association and a reviewed native sampler.
200ms sampled working-set/private-byte maxima are not true peak or complete WASM
usage;64MiB is a parameter,not observed total browser RAM. No invented p95,SLO or
latency/memory acceptance threshold. Block/derive caps are operational budgets;
incomplete samples remain incomplete. One bounded approved block per desktop slot.

Resolved P4 finding rechecked:without a manifest key,a differently pathed fault
copy may get a different extension ID/storage namespace. Protocol now requires
the **same owned installed path/ID/profile** across genuine control→missing/corrupt
WASM→restored control,with retained salt/profile/map checks. Only the disposable
copy changes after owned-browser close;canonical/held artifacts remain immutable.
Identity-preserving fault driver is explicitly missing;P4 is not executable Ready.

Fresh desktop handoff,live browser/extension identity,reviewed observers and
helpers precede any run. Separate browser profiles prevent newer-profile reuse
in116. Cleanup checks exact owned children/profile and preserves raw failures;
no blanket Chrome kill,policy changes,owner-profile/clipboard/account operations.
There is no invented measurement command or completed performance assertion.

## Local verification and unresolved

10local targets resolve;P0–P4/source constants/resource states checked. Current
held record/hash,all74source and46test hashes match. Final`git diff --check`
Passed. No unit/package/browser suite replay or extra Windows OS query.

Native Stable/116 loading/control canaries,accepted116 provenance,reviewed timing/
memory/fault observers and fresh desktop handoff remain open. Actual B03 metrics,
support and fail-closed acceptance are NotRun;broader OWN01/C01/A23,stable A13,
B07/B08/real Sync/release gates remain unchanged. No unresolved protocol finding.

## Manager reconciliation —2026-10-27

Passed:9manager/related documents,298local targets,46unique task IDs/46matching
owner anchors;master78lines. B03 remains PreparedOnly/all6runtime categories
NotRun. B01/A23/C01 stay Partial,C02 Done refers only to its approved decision;
native helper/observer/Stable116/desktop/real Sync and candidate gates remain open.
Final`git diff --check` Passed;no source/registry/runtime action by reviewer.
