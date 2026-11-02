# Permission/data and Store declaration working record

> **Status:** A04 corrected local draft2026-10-26; [independent review Passed](../../../plans/261022-project-work-queue/reports/a04-independent-review.md), final approval Pending.
> Source:HEAD039c76d + preserved WIP, manifest2.3.0. No approved Store declarations.
> A05/B02 own necessity/runtime decisions; C04 legal/publisher approval; C05 live URLs.

## Current permission matrix — draft justification text

Dated runtime update2026-11-02:[B02 comparison](../../../plans/261022-project-work-queue/reports/b02-permission-runtime-20261102.md)
and [independent review](../../../plans/261022-project-work-queue/reports/b02-runtime-20261102-independent-review.md)
record24/24selected synthetic cases Passed across8/7/7/6permission variants of the
held2.3.1 beta on WindowsStable156.0.8078.76. P2/P3 omit nonhost fixture URL/title
while scoped Menu metadata remains available. This does not certify general tab
privacy,physical/native/clipboard/provider/edge/fullsupport behavior or necessity.
Current production manifest still declares8permissions;no reduction or Store
declaration adopted. The header's2.3.0 source is the dated A04 snapshot,not the
current held artifact. Final B02 permission choice/C04/A13/A14 gates remain open.

[Manifest](../../../chrome-extension/src/manifest.json) declares8permissions and1host.
Minimum116 is a declared API boundary, not a tested browser support matrix.

| Declaration | Source / current purpose | Draft justification and remaining check |
|---|---|---|
| `storage` | [Background](../../../chrome-extension/src/security/background-security.js), [session](../../../chrome-extension/src/security/session-authority.js), [Options](../../../chrome-extension/src/options/options-settings-tab.js), [onboarding](../../../chrome-extension/src/popup/popup-home.js) | Persist encrypted Cookbook/metadata/preferences in Sync, unlocked keys/lifetime/action state in session, nonsecret attempt/onboarding state in local. A13/B08 bind final artifact/upgrade/Sync behavior. |
| `clipboardWrite` | [Clipboard owner](../../../chrome-extension/src/security/clipboard-owner.js), [preview](../../../chrome-extension/src/popup/builder-preview.js), [export](../../../chrome-extension/src/options/options-export-wizard.js) | Write an explicitly copied Dish, Recipe or encrypted bundle. Cooking/shortcut alone does not copy. Dish-only ownership cleanup does not cover Recipe/bundle Copy. |
| `clipboardRead` | [Clipboard DOM](../../../chrome-extension/src/offscreen/clipboard-dom.js), [owner](../../../chrome-extension/src/security/clipboard-owner.js) | Read current text at owned Dish cleanup deadline to avoid clearing a later different clipboard value. Denied reading means no clear; no history access. B04/B07 current artifact conditions remain Pending. |
| `offscreen` | [Client](../../../chrome-extension/src/security/offscreen-client.js), [document](../../../chrome-extension/src/offscreen/offscreen.js), [worker](../../../chrome-extension/src/offscreen/crypto-worker.js) | Bundled Argon2 worker plus clipboard owner after copying-page closure. Reasons are WORKERS/CLIPBOARD; A06/B07 verify resource/provenance/lifecycle in packaged artifact. |
| `idle` | [Background](../../../chrome-extension/src/security/background-security.js) | Lock on browser idle/device lock; configured idle threshold300seconds. No idle telemetry flow. Broader platform timing remains B03/B04. |
| `alarms` | [Session authority](../../../chrome-extension/src/security/session-authority.js), [background](../../../chrome-extension/src/security/background-security.js) | Enforce/reconstruct original ten-minute absolute session deadline; clipboard uses offscreen timer. No rolling deadline extension on worker wake. |
| `tabs` | [Worker detection/shortcut](../../../chrome-extension/src/service_worker.js), [popup](../../../chrome-extension/src/popup/popup.js), [lock broadcast](../../../chrome-extension/src/security/background-security.js) | Current active/open Menu picker URL/title detection and Recipe/lock messaging; opens bundled guidance/Options. Necessity versus available host grants unresolved A05/B02; no minimum-permission conclusion. |
| `activeTab` | [Manifest](../../../chrome-extension/src/manifest.json), [content reader](../../../chrome-extension/src/content/content.js) | Declared; no separate explicit API call proves necessity. Fixed host permission and installed content script already exist. Draft reason cannot claim access only at shortcut time; A05/B02 must decide retain/remove before final certification. |
| `https://docs.google.com/spreadsheets/*` | [Content reader](../../../chrome-extension/src/content/content.js), [popup Home](../../../chrome-extension/src/popup/popup-home.js) | Installed content script reads selected Recipe from editor/formula-bar DOM on matching pages when requested; opening unlocked popup can trigger a read. Persistent host scope is broader in time than a temporary click grant. Browser flow does not use Sheets API or write cells. |

Audit does not itself remove permissions or certify the final manifest.

A05 [call-site audit and hypothetical candidates](../../../plans/261022-project-work-queue/reports/a05-permission-audit.md)
maps11tabs calls/5modules and prepares B02; no permission reduction is accepted.
Chrome host-permission paths are ignored:the declared HTTPS docs.google.com grant
is not API access limited to `/spreadsheets/*`. The installed content script's
match remains path-specific. See the report's official semantics and HTTP/nonhost
metadata cases before making a minimum-permission or narrow-host claim.

## Data, movement and retention matrix

| Data / fields | Storage/recipient and purpose | Clear/delete boundary |
|---|---|---|
| Cookbook Spices/profile settings | `profile:*`/`shared:*` encryptedData+IV in Sync, legacy encryptedData/iv migration records possible; Google may Sync per Chrome settings | Profile removal deletes its key; Reset calls Sync clear. Not secure physical erasure proof or remote replica timing. |
| Salt and metadata | Sync readable salt/defaultProfile/sheetMapping, key-encoded profile names; shared sheetId/readOnly/importedFrom/importedAt/bundleId, language/displaySettings/migrationNotified | Same Sync controls; no promise every preference is implemented (UI01). Salt is not an encrypted master-password token. |
| Unlocked key/lifetime | Session `sessionKey`, `sessionNonce`, `sessionStartedAt`, `sessionDeadline` | Normal lock removes these4fields; Chrome session clearing on disable/reload/update/restart is documented. Lock is not all-region deletion. |
| Pending sharing action | Session `pendingAction` including type/MenuID, written by popup and removed by [Options handler](../../../chrome-extension/src/options/options.js) | Consumed on Options handling or session clear; may outlive normal key-only lock. |
| Authentication/onboarding | Local `authState` attempts/cooldown/reservation identity; `hasSeenQuickStart` flag | Reset leaves local records. Chrome documents local removal on uninstall; no custom uninstall erasure handler. No password/key/Spice field written here. |
| Master password/decrypted Spices/Dish | Working popup/Options/worker/offscreen memory; Cookbook operations and Recipe cooking | No app persistence of master password or saved cooked-Dish list. Clearing selected buffers is not perfect memory erasure; OS/page capture/malware remain risks. |
| Recipe/Menu/tab context | Google Menu DOM; selected text and MenuID/URL/title in content/popup/worker messages; names/assignments may persist as above | Google Menu remains owner-controlled. No developer upload/telemetry endpoint implemented. A read can still handle sensitive website content/browsing metadata. |
| Owned Dish clipboard | OS clipboard + separate offscreen RAM text/deadline | Attempt30s exact-match clear; read denial/different text prevents write. Reset/lock has no explicit immediate owner-dispose/clipboard-clear command. Sleep/closed browser/history/sync/races outside guarantee. |
| Export/QR/recipient | Full Access original Spices encrypted by separate PBKDF2-SHA256100k/AES-GCM password; readable bundle headers; user-chosen file/clipboard/QR/recipient | Source Reset/delete/master-password rotation cannot revoke independent copies. Legacy HKDF-derived Menu-bound sharing is separate. |
| Extension font requests | [Bundled tokens.css](../../../chrome-extension/src/styles/tokens.css):6 imports Google Fonts CSS; popup/Options link this stylesheet. External stylesheet/font requests can expose ordinary network/header metadata to Google | A06 source inventory adds this disclosure; actual requests, provider behavior and final artifact network acceptance Pending. This is external font data, not a developer telemetry client or remote executable code. |
| Website requests/support | Legal pages also load external Google Fonts; deployed host can receive IP/request logs. Any future submitted support content depends on chosen channel | passchef.com+Cloudflare chosen, but ownership/service/log retention/cookies/deployment and contacts unverified C04/C05. Extension no-telemetry finding does not cover all website/provider processing. |

[Reset source](../../../chrome-extension/src/options/options-settings-tab.js):
confirmed handler calls `chrome.storage.sync.clear()` then `chrome.storage.session.clear()`;
no `local.clear`, no explicit clipboard clear. No `setUninstallURL` or uninstall erasure
handler found in current extension source. Uninstall is not evidence that Google/server/
other-device replicas, downloaded bundles, recipients or OS history were erased.
Sync delete propagation/upgrade/uninstall-reinstall still need selected B08/C03 evidence.
No promise that Store removal/discontinuation preserves recoverability.

## Draft Store privacy fields — not checkbox certification

| Field | Prepared text / classification / final gate |
|---|---|
| Single purpose | “Cook Dishes from Recipes in a Google Menu using private Spices in an encrypted Cookbook, with related profile/session/Copy/sharing controls.” |
| Permission justification | Text prepared in9rows above; tabs/activeTab necessity awaits A05/B02. Reconcile final manifest before upload. |
| Authentication information | Master password, Spices, unlocked key and cooked Dish are handled locally; encrypted records may Sync and authorized bundles delegate Spices. Do not select “no data handled” solely because no developer backend. |
| Website content / web activity | Selected Recipe/page content and MenuID/tab URL/title are processed for detection/cooking/picker. No general history database feature, but required current-page data still needs disclosure. |
| Identifiable/other sensitive data | Profile names, Menu context, user-entered Recipes/labels and future support reports can contain identifiers. Final Dashboard category choices must reflect actual accepted input scope and all applicable categories; don't categorically exclude content just because fixture data is synthetic. |
| Other Dashboard categories | No source flow for advertising, credit scoring or payment data collection in this extension. This is not certification of every user-entered value, deployed website or future billing service. Owner must inspect final Dashboard wording/categories. |
| Remote executable code | Current resource paths use bundled JS/Worker/WASM; locale fetch uses runtime.getURL. Provisional draft:“No remote executable code in the accepted extension artifact.” A06/A13/B07 must verify imports/resources/provenance before asserting final No. Popup/Options bundled CSS imports external Google Fonts stylesheet/font data; remote data requests differ from remote executable code and still require disclosure/network verification. |
| Limited Use | Draft commitment:user data only for disclosed Recipe/Cookbook/session/clipboard/sharing purpose; no unrelated advertising, sale, credit scoring or human access workflow implemented. Owner must approve/certify full restrictions and ensure a corresponding public policy statement. |
| Privacy URL / support / identity | Pending C04/C05; relative local HTML is not a verified public HTTPS URL. Existing unconfirmed .io/email contact is not promoted as approved identity. |
| Final consistency | A14 binds accepted ZIP/version/manifest, Store categories/certifications, policy URLs and listing. C11 approval/submission remains separate. |

## Official checks — 2026-10-26

- [Chrome storage](https://developer.chrome.com/docs/extensions/reference/api/storage):local removal on uninstall; session in-memory and cleared on disable/reload/update/restart; Sync depends on browser settings and resumes after offline use. These are platform docs, not newly executed deletion/Sync acceptance.
- [Privacy fields](https://developer.chrome.com/docs/webstore/cws-dashboard-privacy):purpose, per-permission justification, remote-code declaration, data categories/certification and policy URL must be consistent. Actual Dashboard not accessed.
- [User Data FAQ](https://developer.chrome.com/docs/webstore/program-policies/user-data-faq):local processing/storage still requires data-handling disclosure; scraped website content and browsing metadata are relevant.
- [Limited Use](https://developer.chrome.com/docs/webstore/program-policies/limited-use):purpose-bound use/transfer restrictions, constraints on human access and prohibited uses, plus an affirmative website disclosure. Draft text is not owner certification or proof of legal compliance.

## Current legal source state

[Privacy](../../../landing-page/privacy.html) and [Terms](../../../landing-page/terms.html)
are corrected local drafts. Closed source is the settled direction; third-party
notices and historical LICENSE files are unchanged. Unsupported GDPR/CCPA exemptions,
all-data-erasure/recovery promises and assumed publisher/jurisdiction/contact removed.
No duplicate Markdown policy is created; this record is the implementation/declaration matrix.

Unresolved:C04 publisher/legal/contact approval; C05 host/retention/URLs; A05/B02
necessity/runtime; A06/A13/B07 artifact resources; C03/B08 Sync/deletion acceptance.
