# A04 — Legal/data/permission local drafts

Date:2026-10-26, Asia/Saigon. Source:HEAD `039c76d17a2765bd753e09a7009e004dadfd6638`
+ preserved WIP; manifest2.3.0/package1.0.0, no release candidate.
Status:**Done2026-10-26 — local legal/data preparation**;
[independent review Passed](a04-independent-review.md). Legal/publisher approval C04,
hosting/publication C05/C11 remain Pending. No publication/account action.

## TODO

- [x] Read current data/storage/reset/sharing/permissions call sites and legal drafts.
- [x] Check official Chrome storage, privacy fields, user-data/limited-use guidance.
- [x] Correct Privacy/Terms locally; preserve layout and third-party notices.
- [x] Complete source-bound permission/data/deletion/hosting + Store draft matrix.
- [x] Verify HTML, links,8permissions+host coverage and unchanged production/tests.
- [x] Independent review Passed; task/catalog/master/index updated after acceptance.

## Exact edited scope

| File | Changes |
|---|---|
| [Privacy HTML](../../../landing-page/privacy.html) | Corrected stored/processed data,8permissions+host scope, clipboard/Tag/sharing limits, Reset versus uninstall and external website providers. Header/meta/body/footer explicitly local draft; no assumed publisher, unverified contact or .io public policy URL. Removed blanket GDPR/CCPA/children compliance exemptions and all-data-erasure promises. |
| [Terms HTML](../../../landing-page/terms.html) | Closed source and third-party rights separate; accurate cooking/Cookbook/Sync/Copy/export/recovery boundaries. Removes universal-free/MIT rights, zero-dollar liability cap and assumed legal identity. Final warranty/liability/jurisdiction/effective terms remain owner/legal decisions, not invented rules. Header/meta/summary/body/footer consistent. |
| [Permission/data record](../../../docs/commercialization/legal/permissions-review.md) |9-row call-site/purpose/justification matrix; data/retention/sharing/hosting matrix; draft Store purpose, categories, permissions, remote-code and Limited Use fields. Final Dashboard choices/certification remain Pending. |
| [Legal index](../../../docs/commercialization/legal/README.md) | Reflects corrected local draft state; pending legal/contact/URL/artifact gates retained. |
| [Publisher record](../../../docs/commercialization/operations/publisher-and-support.md) | One current-source bullet reconciled:legal HTML no longer advertises old unverified contact as approved. Existing owner decisions and old contact-decision row retained. |
| This report | Actual checks, limits and handoff. Registry/catalog status left to independent reviewer/root. |

No extension code/tests/manifest, website JS/CSS, third-party notices or historical
LICENSE changed. Existing surrounding WIP preserved. HTML uses existing layout/classes;
no new policy Markdown duplicate or actual external service setup.

## Important source-bound distinctions

- [Settings Reset](../../../chrome-extension/src/options/options-settings-tab.js):
  only Sync clear then session clear. Local authState/hasSeenQuickStart persist;
  there is no explicit immediate offscreen clipboard-owner clear. Normal owned-Dish
  cleanup remains conditional; reset is not remote revocation or OS-history erasure.
- [Background](../../../chrome-extension/src/security/background-security.js) and
  [session authority](../../../chrome-extension/src/security/session-authority.js):
  authentication/cooldown local records differ from encrypted Sync profiles and
  unlocked key/lifetime in session. Ordinary service-worker unload is not uninstall
  and does not itself delete local storage/reset the session deadline.
- [Popup](../../../chrome-extension/src/popup/popup.js)/[Options](../../../chrome-extension/src/options/options.js):
  pendingAction can hold a MenuID and is removed when consumed; normal key-only lock
  is not all-region deletion. Sync names/assignments/import metadata may be readable.
- [Offscreen owner](../../../chrome-extension/src/security/clipboard-owner.js):
  conditional30s current-match cleanup/read-denial handling; no OS-history access.
  [Offscreen client](../../../chrome-extension/src/security/offscreen-client.js)
  uses bundled worker/clipboard reasons. Recipe/bundle Copy are separate.
- [Export/import](../../../chrome-extension/src/adapters/infrastructure/export-import-adapter.js):
  Full Access includes original Spices encrypted by a separate sharing password;
  recipient can cook the same Dishes. Independent exports/recipient copies survive
  source Reset/delete/master-password rotation. Legacy derived sharing differs.
- Inspected extension has no custom uninstall erasure handler or developer telemetry
  upload client. This source observation does not assert that local handling is
  legally exempt or that Google/hosting/font providers process no request metadata.

## Official checks —2026-10-26

Repo docs-seeker detect/fetch workflow attempted; Context7 had no matching docs.
Fallback read official Chrome storage API, privacy-fields, User Data FAQ and
Limited Use pages. Exact sources and policy implications are cited in the
[working matrix](../../../docs/commercialization/legal/permissions-review.md#official-checks--2026-10-26).
An initial user-data URL404 and a transient Limited Use timeout were replaced by
the working official FAQ/page; no failed fetch is represented as verified guidance.

These sources support platform/declaration statements, not a compliance opinion,
owner certification, executed Sync/uninstall proof or an effective legal contract.

## Fresh local verification

Linux Node24.18.0, existing jsdom imported via extension dependency directory;
`node --input-type=module` checks run without full-page scripts/network/GUI.

| Check | Result / limit |
|---|---|
| Parse both HTML documents |Privacy11sections, Terms12sections; one legal-body each;3unique IDs each, exact same IDs as HEAD. Draft/contact-pending state visible. |
| Style/script/resource preservation |All inline style, script and link elements byte-identical to HEAD for both legal pages. This is structural evidence, not visual/browser QA. |
| Permissions coverage |Privacy card names exactly equal manifest8permissions+1host; matrix includes all9 declarations. No permission reduction/minimum-necessity result claimed. |
| HTML relative links/fragments |40/40 resolve to existing local files/IDs, including current index anchors. External citations read separately; no live project legal URL verified. |
| Markdown file links |40/40 across permission matrix/legal index/publisher record resolve; report links checked at handoff. |
| Reset source assertion |Contains sync.clear and session.clear; no local storage or explicit clipboard-clear/owner-dispose call in handler. No storage or account mutation executed. |
| Unsupported contact/claim scan |No old .io/approved personal mail link, general MIT license grant, universal-free wording or blanket legal exemption in corrected public drafts. Existing historical decisions remain labeled as such in internal records. |
| `git diff --exit-code -- chrome-extension .github/workflows/build_and_deploy.yml` |exit0; source/config/tests unchanged. Golden SHA256`5f9faa1e2bb5e3d38151250ffaa961a94efba459f41786f5ae90402ab07aae26` matches accepted record. |
| `git diff --check` |exit0. No nonexistent build/lint result, unchanged unit-suite rerun, browser/runtime or publication acceptance claimed. |

A04 closed as local preparation after independent review. Next:A05
permission audit; final legal contacts/host disclosures/artifact-linked certifications
still require their own evidence and owner approval.

## A06 source-inventory amendment —2026-10-26

A06 review found an omitted extension data path:popup/Options load
[`styles/tokens.css`](../../../chrome-extension/src/styles/tokens.css), whose line6
imports Google Fonts CSS. The original A04 draft disclosed website fonts only.
Root amended Privacy section7 and the data/remote-code matrix to cover possible
extension stylesheet/font request metadata. JS/WASM remain bundled; no font/code
change, network run, external-provider acceptance or new telemetry finding.
Independent amendment review [Passed](a04-independent-review.md#font-disclosure-amendment-review-2026-10-26);
original checks above retain their scope.

## Unresolved

C04 legal identity/contact/approval; C05 verified hosting/URLs; A05/B02 permission
necessity/runtime; A13/B07/B08 final artifact/Sync/deletion acceptance.
