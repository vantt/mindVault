# A05 — tabs/activeTab permission audit and B02 protocol

Date:2026-10-26, Asia/Saigon. Source:HEAD `039c76d17a2765bd753e09a7009e004dadfd6638`
+ preserved WIP; manifest2.3.0/package1.0.0. No changed manifest/candidate artifact.
Status:**Done2026-10-26 — audit/protocol preparation**;
[independent review Passed](a05-independent-review.md). Minimum permission claim Pending B02.

Current continuation2026-11-02:[B02 actual comparison](b02-permission-runtime-20261102.md)
and [independent runtime review](b02-runtime-20261102-independent-review.md):P0–P3
selected24/24synthetic cases Passed on nativeStable156.0.8078.76,held2.3.1 beta.
Observed grants8/7/7/6;without tabs,P2/P3 omit nonhost fixture URL/title while
scoped Menu metadata/picker still work. No physical/native/clipboard/provider/edge/
fullsupport acceptance or permission adoption. The dated static protocol below
retains its original NotExercised snapshot;full B02/minimum decision remain open.

## TODO

- [x] Read manifest, every tabs call-site and affected Menu/picker/lock/Copy paths.
- [x] Recheck official permission semantics; distinguish API use from permission need.
- [x] Record retain/remove manifest proposals and browser pass/fail/restore protocol.
- [x] Verify static inventory, proposal JSON, file links and unchanged production/tests.
- [x] Independent review Passed; task/catalog/master/index updated as preparation only.

## Current boundary

[A04 matrix](../../../docs/commercialization/legal/permissions-review.md) owns the
full data/9-declaration working record. This audit narrows tabs/activeTab questions;
it does not grant a desktop slot, alter permissions or prove browser behavior.

## Call-site matrix —11 calls across5 modules

| Source / line | Operation and consumed data | Permission question / affected flow |
|---|---|---|
| [Worker](../../../chrome-extension/src/service_worker.js):130 | `tabs.query(active,currentWindow)`, reads url/title | Builder active Menu detection; metadata access, not mere namespace access. |
| Worker:137 | `tabs.query(url:'*://docs.google.com/spreadsheets/*')`, then url/title | Other-Menu picker across windows. HTTPS host grant differs from http+https query; absent URL can reach `t.url.match` and catch returns empty detection. Must measure, not infer. |
| Worker:152 | `tabs.query(active,currentWindow)`, consumes id | Native command routing uses tab ID, not sensitive URL/title here. |
| Worker:154 | `tabs.sendMessage(id,TRIGGER_HOTKEY)` | Requires matching installed receiver; use of tabs API alone does not establish need for tabs permission. |
| [Popup](../../../chrome-extension/src/popup/popup.js):94 | `tabs.query(active,currentWindow)`, passes tab to Home | Home reads URL/title; supported Menu→Dish, non-Menu→generic Home. |
| Popup:135 | Same query before Back→Home | Must reread changed selection/context; popup lifetime remains Chrome behavior. |
| [Popup Home](../../../chrome-extension/src/popup/popup-home.js):81 | `tabs.sendMessage(id,GET_CURRENT_CELL_PASSWORD)` | Reads selected Recipe through installed Menu receiver; bad/missing receiver stays guidance/no Dish. |
| Popup Home:40 | `tabs.create(runtime.getURL(demo))` | Opens bundled guide; does not itself justify sensitive-tab metadata permission. |
| [Builder](../../../chrome-extension/src/popup/popup-recipe-builder.js):69 | Same bundled Demo create | Same distinction; no remote navigation/data upload feature. |
| [Security background](../../../chrome-extension/src/security/background-security.js):39 | `tabs.query(url:'https://docs.google.com/spreadsheets/*')`, consumes ids | Background lock broadcasts to open supported Menu receivers, without a fresh action gesture. |
| Security background:40 | `tabs.sendMessage(id,VAULT_LOCKED)` | Content closes visible Dish; query scope/receiver coverage must survive any reduction. |

[Detector UI](../../../chrome-extension/src/popup/sheet-detector.js):22–36 consumes
worker results through runtime messaging; picker expects URL/title labels. It is
not an additional tabs call. `runtime.openOptionsPage` is also a different API.
No `chrome.scripting` call or declaration found in current source.

## Official semantics and source-specific risks

Checked2026-10-26 after docs-seeker Context7 returned no matching documentation;
official Chrome pages used as fallback. Source-derived consequences are inferences,
not measured results.

- [Tabs API](https://developer.chrome.com/docs/extensions/reference/api/tabs):the
  tabs permission concerns sensitive Tab metadata; matching host grants can also
  expose URL/title. Tab creation/messaging and metadata access are distinct.
  URL filters can be ignored without appropriate grants, so inspect actual results.
- [activeTab](https://developer.chrome.com/docs/extensions/develop/concepts/activeTab):
  a user action/command can grant temporary access to the active tab; cross-origin
  navigation revokes it. It is not a persistent all-open-Menu background grant.
- [Match patterns](https://developer.chrome.com/docs/extensions/develop/concepts/match-patterns):
  host-permission paths are ignored; scheme wildcard covers http+https. Thus the
  declared `https://docs.google.com/spreadsheets/*` host grant must not be marketed
  as API access restricted to that path. It covers the granted HTTPS host.
- [Static content scripts](https://developer.chrome.com/docs/extensions/develop/concepts/content-scripts):
  manifest matches govern the installed reader's injection. Its `/spreadsheets/*`
  match differs from origin-wide API host-grant semantics. Candidates below keep
  both fields exactly unchanged; no programmatic injection or new wider host.

The worker's active detector matches `/spreadsheets/d/ID` in any visible URL
without separately checking origin. With tabs, or a temporary activeTab grant,
a nonhost page containing that path can become a source-level false Menu candidate.
This is not a supported flow to preserve or evidence that tabs is necessary.
B02 must record actual HTTP/nonhost/restricted-page behavior and safe guidance;
any origin-check repair gets a separate diagnosed issue/patch, not an audit change.
Removing metadata visibility can alter this symptom without repairing the detector.

Other risks:active-page metadata may be absent before a user gesture; malformed,
closed, redirecting or loading tabs can lack URL/title. Single catch hides whether
detection found no Menu or a permission/query failure. Fixed host grants may make
tabs/activeTab redundant for supported HTTPS Menu paths, but that remains a B02
hypothesis. Neither removal means Google host access or all metadata exposure ends.

## Exact hypothetical manifest candidates

All proposals overlay **only** the `permissions` array on the current full manifest.
Host permissions, content-script matches, CSP, commands, minimum116 and version
stay byte-equivalent. No JSON below has been installed or written to production.
P0 is current8permissions. Start B02 with P1 to isolate one change; compare P2/P3
if useful. No candidate is selected as the minimum set by this report.

P1 — remove activeTab; keep tabs (7permissions):

```json
{"permissions":["storage","clipboardWrite","tabs","idle","alarms","offscreen","clipboardRead"]}
```

P2 — remove tabs; keep activeTab (7permissions):

```json
{"permissions":["storage","clipboardWrite","activeTab","idle","alarms","offscreen","clipboardRead"]}
```

P3 — remove both (6permissions):

```json
{"permissions":["storage","clipboardWrite","idle","alarms","offscreen","clipboardRead"]}
```

| Proposal | Benefit hypothesis | What might require retain/fix |
|---|---|---|
| P0 retain both | Known historical manifest baseline; no evidence reduction is safe yet | Broad tabs metadata and temporary nonhost activeTab grants remain; not automatically justified just because accepted slices exist. |
| P1 | Removes potentially redundant temporary grant while broad tabs metadata remains | Unexpected user-invocation path needs activeTab; verify actual native command and action/popover behavior. No claim lower install warning count. |
| P2 | Removes broad sensitive-tab metadata outside host/temporary active grants | Background picker/lock metadata filtering without gesture; http pattern/query and missing URLs; nonhost activation/navigation behavior. |
| P3 | Eliminates both declarations; supported Menu uses fixed host grant | All P2 risks plus action/command transient access differences; needs its own evidence, not inferred by combining P1/P2 passes. |

Other6permissions remain for storage, encryption worker, owned clipboard and session
controls; this audit does not consider their removal. Any proposal not exercised
stays NotExercised. A failed reduced proposal does not by itself prove both current
declarations necessary; diagnose the exact failing API/path before retaining one.

## B02 execution protocol — prepared, not run

Prerequisites:reviewed isolated copy of exact source+manifest proposal; record
source SHA/dirty inventory/manifest SHA256 and package hash when packaged, Chrome
version/OS/profile, host/site-access settings and granted permissions. Never change
owner's installed extension or normal profile as an experiment. Use a new temporary
profile per proposal to avoid leftover grants; no personal account, login or real
Spices. Candidate resources require A06 validation when package tooling is available.

Root must have a B desktop slot or reviewed isolation with explicit focus/input/
clipboard ownership. Native command and OS Copy require their corresponding
exclusive resources; headless/message injection is not native proof. Use synthetic
Menu fixtures for contracts and clearly distinguish them from actual Google/provider
evidence. Any provider/account/write action needs its own permission/readiness.

| Case | Expected assertion / record | Status |
|---|---|---|
| P0 control then isolated candidate | Manifest/grants inspected; test exact candidate bytes, no inherited grant | NotExercised |
| Active supported HTTPS Menu | Action popup reads correct Recipe/profile and exact synthetic Dish; Copy only on request | NotExercised |
| Non-Menu active,0/1/many Menus in2windows | Generic Home/no Dish on non-Menu; Builder detection/picker enumerates correct supported URLs/titles and preserves manual edits | NotExercised |
| Active Menu plus other Menu tabs | Correct active context, no duplicate; explicit picker choice/manual Menu mapping and refresh remain coherent | NotExercised |
| Navigation/new popup/Back | Re-read current context/Recipe; no stale Dish or renewed session deadline; transient-grant revocation measured | NotExercised |
| Missing/empty/invalid/Tag mismatch | Guidance/no Dish; valid Tag preserves literal Dish; legacy behavior stays scoped | NotExercised |
| Genuine native shortcut | Observe real command→correct supported receiver; no message fallback; clipboard unchanged until explicit Copy | NotExercised |
| Background lock without fresh action | Open Dish on multiple supported Menu tabs then lock; every receiver closes; locked Cook fails | NotExercised |
| Idle/session and worker wake | Selected affected lock checks preserve5min idle/10min absolute contract; API mocks are labeled deterministic only | NotExercised |
| Explicit owned Copy | System clipboard writes exact synthetic Dish, selected30s ownership/later-text checks; read denial does not clear unrelated text | NotExercised |
| HTTP docs URL/nonhost path-shaped URL | Do not grant new hosts or modify routes to make a test pass; record actual filtered/missing metadata and source false-candidate symptom; no unsupported host called accepted Menu | NotExercised |
| Restricted/loading/closed tabs | No crash/stale Dish; unavailable receiver or metadata gets safe guidance; temporary grant not assumed on restricted pages | NotExercised |
| Bundled Demo/Options | Both Demo entry paths and Options open without needing broader metadata permission | NotExercised |

Record per case:P0/candidate differences, actual API errors/missing metadata, user
gesture/grant state, assertions, timestamps when measured, cleanup and screenshot/
log references without real credentials. Existing E2E can establish fixture contracts;
`menu-contract.spec.ts` message triggers do not prove the native row, and mirrored
`sheet-tab-detector.spec.mjs` logic does not test browser permission enforcement.

Pass/retain gate:all required supported flows pass on the exact proposal and final
support boundary; publish a specific retain/remove decision with residual limits.
Fail/unknown:keep failure evidence, do not accept the reduction or rewrite Golden
tests; restore only the disposable candidate's P0 manifest/grants, close its browser,
remove synthetic clipboard content if still owned, delete the temporary profile,
release desktop/clipboard and report cleanup. If a source repair is needed, review/
verify separately and invalidate affected candidate evidence. Final adoption later
updates manifest, A04 legal declarations and A13 artifact record together.

## Fresh verification / exact delivery scope

Only this report and the A04 [permission working record](../../../docs/commercialization/legal/permissions-review.md)
changed:the latter adds an A05 link and host-grant path-semantics qualification.
No production manifest/source/test, browser profile or user grant changed.

| Actual check | Result / limit |
|---|---|
| Node24 static source scan |11tabs calls:6query,3sendMessage,2create across5direct-call modules. `sheet-detector.js` is a sixth involved consumer via runtime messaging, not a direct tabs caller. Initial expected6module assertion failed; source recount corrected inventory to5, rerun Passed. |
| Parse current manifest and3report JSON proposals |Current8permissions+1host; P1/P2/P3 have7/7/6 permissions; exact removals only, all other merged manifest fields unchanged. No installed browser candidate. |
| Local Markdown file links |36/36 valid across report+working record before final self-link insertion; final handoff check covers the new report reference. |
| Manifest SHA256 |`0a8d20053a13dfd6bda522caab2f4950f7026a2543a01eeb2b8bf4e1e3a78bf3` for current unchanged manifest. |
| `git diff --exit-code -- chrome-extension .github/workflows/build_and_deploy.yml` |exit0; production and tests/config untouched. Golden SHA256`5f9faa1e2bb5e3d38151250ffaa961a94efba459f41786f5ae90402ab07aae26` unchanged. |
| `git diff --check` |exit0. Audit checks are not unit/browser/permission-enforcement results; no unchanged unit-suite rerun or headed test. |

No actual keep/remove decision is accepted. A05 closed after independent review;
B02 and final minimum-permission justification remain Pending.

## Unresolved

B02 browser evidence on each exact proposal; C02 support matrix; A13/B07/B08 final
artifact acceptance. Current product/UI gate C01 remains closed.
