# A03 — Product claims and listing drafts

Date:2026-10-26, Asia/Saigon. Source:HEAD `039c76d17a2765bd753e09a7009e004dadfd6638`
+ preserved WIP; extension manifest2.3.0/package1.0.0, no release candidate.
Status:**Done2026-10-26 — local wording preparation**;
[independent review Passed](a03-independent-review.md).
Product/release gates unchanged. No publish, deploy, account action or GUI run.

## TODO

- [x] Read Track04, security/Tag/sharing contracts and implementation.
- [x] Recheck current field limits using official Chrome documentation.
- [x] Correct listing/value/README and public landing security copy locally.
- [x] Verify examples, field lengths, links, HTML structure and patch scope.
- [x] Independent review Passed; task/catalog/master/index updated after acceptance.

## Changed files and claim decisions

| File | Correction / basis |
|---|---|
| [Listing draft](../../../docs/commercialization/marketing/cws-listing-copy.md) | Replaced invalid Recipe and site/length/rules instructions with `r4nd0m#1`; separates string cooking from Argon2id/AES Cookbook protection. Matches current manifest name/summary; removes competitor/adoption/zero-storage absolutes, fabricated unlimited capacity and stale test counts. Sharing, Tag/legacy, session, data and clipboard boundaries explicit; draft final support/privacy links still gated. |
| [Value statements](../../../docs/commercialization/marketing/product-value-statements.md) | Reusable copy now grounded in current contract; simulated historical council opinions are not real endorsements/testimonials. Removed named quotes from reuse source. Original naming-decision history unchanged. |
| [README](../../../README.md) | Replaces universal public-Menu safety and Hacker-Proof/Unbreakable claims; explains encrypted Sync metadata/session/local records, Full Access recipient power, Tag limits and clipboard conditions. Corrects selected-cell/open-extension/manual Copy and Mac Control shortcut wording. Synthetic example is not a real credential recommendation. |
| [Landing index](../../../landing-page/index.html) | Readable metadata/weak Spices, Cookbook encryption vs cooking, Clipboard/Sync limitations, closed source; removes unsupported adoption/free/timing claims. Removed unproven testimonial section/footer anchor, invalid `_v2` anatomy/directions/buttons; no replacement invented quotes. Initial ingredient now valid ASCII; demo explicitly a simplified illustration with actual syntax/Tag/modifiers directed to extension Builder. CSS and CTA target route unchanged; route remains A22/C05. |
| [Landing script](../../../landing-page/script.js) | One literal changed:initial ingredient `g!th^b`→`r4nd0m`. This is the only executable edit, required to stop initialization overwriting the corrected HTML example with an invalid Recipe. Existing permissive demo parser is recorded as DEMO01 below, not silently rewritten. |
| This report | Verification and handoff; task/catalog statuses left to independent reviewer/root. |

No legal HTML, historical naming record, extension code/test/config or third-party
notices changed. A04 owns Privacy/Terms; A08 owns full asset brief; A22/C05 own
published website/CTA readiness. The literal change and HTML checks are not a website browser QA pass.

## DEMO01 — known standalone illustration limit

Existing `landing-page/script.js:417` uses a standalone permissive Recipe matcher,
accepts obsolete version suffixes, and concatenates fallback text for invalid input.
It does not establish production parser/Tag/modifier parity. A03 removes all version
controls/directions and shows valid default/example inputs, with a visible simplified
illustration caveat. This is not a claim arbitrary input is validated. Route website
audit/B05 and future scoped repair; no mobile A12 dependency and no browser runtime
acceptance. Full parser replacement is outside this wording task.

## Code-to-claim evidence

- [Cooking](../../../chrome-extension/src/core/usecases/generate_password.js),
  [parser](../../../chrome-extension/src/adapters/infrastructure/regex_parser_adapter.js):
  valid ASCII Recipe, modifiers, ordinary own-profile string combination; Tag check
  precedes cooking, legacy warning and unchanged Dish.
- [Storage routing](../../../chrome-extension/src/adapters/infrastructure/chrome_storage_adapter.js),
  [AES helpers](../../../chrome-extension/src/adapters/infrastructure/aes-storage-crypto-helper.js),
  [browser contract](../../../docs/specs/extension-security.md): encrypted profiles
  versus metadata, session keys, local nonsecret state, unchanged Argon2/AES parameters.
- [Full Access](../../../chrome-extension/src/adapters/infrastructure/export-import-adapter.js),
  `createFullAccessBundle`: original Spices encrypted with separately derived
  PBKDF2-SHA256100k/AES-GCM bundle key. Bundle/password recipient can cook same Dishes;
  legacy derived sharing is separate. QR sender does not establish phone receiver.
- [Clipboard owner](../../../chrome-extension/src/security/clipboard-owner.js) and
  [session authority](../../../chrome-extension/src/security/session-authority.js):
  owned-match conditional cleanup/read-denial handling and original absolute deadline.
- [Tag codec](../../../chrome-extension/src/core/domain/tag-codec.js): four-character
  truncated HMAC over canonical Recipe and Menu ID. A person with public context can
  compute candidate-Spice Tags locally; weak-Spice guessing is not prevented by a
  locked Cookbook. Prose qualification preserves current algorithm and format;
  historical rationale/source comments are not adopted as universal guarantees.
- [Commercialization decision](../../../docs/commercialization/plan.md): closed source
  remains settled; A03 makes no new pricing, version, country or publisher decision.

## Official field check — 2026-10-26

Repo `docs-seeker` skill used:detect-topic completed; fetch-docs returned
“Documentation not found on context7.com”. Fell back to official Chrome pages.

| Field / guidance | Verified source / result |
|---|---|
| Extension name | [Manifest name](https://developer.chrome.com/docs/extensions/reference/manifest/name):maximum75. Draft27 characters, exact manifest match. |
| Summary/manifest description | [Manifest description](https://developer.chrome.com/docs/extensions/reference/manifest/description):maximum132, plain text. Draft102 characters, exact manifest match. |
| Detailed description | [Listing information](https://developer.chrome.com/docs/webstore/cws-dashboard-listing) and [text guidance](https://developer.chrome.com/docs/webstore/best-listing) require accurate descriptive prose. Opened official pages give no numeric detailed-description maximum; actual Dashboard cap remains A14/C11 check. Body4245 Unicode code points. No third-party16,000-limit assertion. |

No developer dashboard login/submission performed. A08 must recheck graphic specs
for its own artifact brief; this task removes obsolete graphic claims rather than
declaring final images compliant.

## Verification — actual local checks

Linux Node24.18.0 from `/home/vantt/.nvm/versions/node/v24.18.0/bin`.
Two `node --input-type=module` verification invocations imported current production
parser, cooking use case and Tag codec, plus existing jsdom; no install/network/GUI.

| Check | Result / limit |
|---|---|
| Listing text fields + manifest JSON.parse |27/75 name,102/132 summary; both exact manifest match.4245-code-point body between explicit markers. Numeric body cap not independently established. |
| Synthetic `r4nd0m#1` + `Basic*` |Exact expected `Basic*r4nd0m` Dish. Real code's tagged version preserves exact Dish; mismatched Tag rejects. No real credential used or reported. |
| Synthetic Tag candidate comparison |Locally recomputing a known synthetic candidate matched public Recipe/Menu context, supporting the weak-Spice-guessing qualification. No exploit against an account or cryptographic change. |
| JSDOM parses landing HTML |20 unique IDs; only `testimonials` ID removed; zero new broken local anchors; script/link inventory count unchanged; no version controls. No full-page scripts executed and no visual/runtime QA claimed. |
| Displayed example/standalone helper check |Valid initial Recipe and all3 `data-formula` examples parse in actual extension. Extracted existing illustration helper matches extension cooking for all4 shown styles with synthetic default ingredient/Spice. This bounded comparison does not accept arbitrary-input/Tag/modifier parity. |
| `node --check landing-page/script.js` |exit0 after single ingredient-literal edit; no parser or extension behavior changed. |
| Markdown file links |35 local file links across listing/value/README valid before report; report links checked at handoff. |
| Unsupported-copy scan |No public named testimonial, adoption-count, open-source/MIT, never-leave, Hacker-Proof or Unbreakable claim remains in edited public copy. Historical expressions in reusable copy are explicitly prohibited, not endorsements. |
| `git diff --exit-code -- chrome-extension .github/workflows/build_and_deploy.yml` |exit0; production/tests/config untouched. Golden SHA256 remains`5f9faa1e2bb5e3d38151250ffaa961a94efba459f41786f5ae90402ab07aae26`. |
| `git diff --check` |exit0. No extension build/lint script or new product-suite pass claimed. A02's fresh147-unit baseline is separate evidence; no reason to replay unchanged suite. |

No website deployment, Store release, mobile acceptance or product gate closure.
A03 closed only as local wording preparation; next A04 Ready.

## Unresolved

C01/C02 product boundary; C04 publisher/contact; C05 deploy; final artifact/Store acceptance.
