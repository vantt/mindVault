# A04 — Independent legal/data draft review

Date:2026-10-26, Asia/Saigon. Reviewer:independent agent, not draft author.
Source:HEAD `039c76d17a2765bd753e09a7009e004dadfd6638` + preserved WIP;
manifest2.3.0/package1.0.0. Exact scope:[A04 delivery](a04-legal-data-review.md).
Verdict:**Passed for local legal/data preparation**; no unresolved review finding.
This verdict is not legal advice, compliance certification or publication approval.

## Source and draft review

Reviewed Privacy/Terms, permission/data record, legal index and the single current-
contact source bullet in the publisher record. Existing publisher/domain/Cloudflare
decisions and older unconfirmed contact-decision rows remain intact.

Checked implementation for encrypted Sync profiles versus readable metadata;
local authentication/onboarding records; session key/nonce/deadline/pending action;
Reset, lock and profile deletion; Full Access raw-Spice transfer versus legacy
derived sharing; conditional offscreen clipboard ownership; installed host access;
and website fonts/hosting request metadata. Matrix covers all declared permissions,
data movement, deletion boundaries and draft Store fields without granting new scope.

Preliminary review found Terms conflated local deletion on uninstall with session
clearing on disable/reload/update/browser restart. Author corrected both pages to
separate these triggers and ordinary service-worker unloading. No actual uninstall,
Sync propagation, secure erasure or clipboard deadline acceptance is claimed.

Pages visibly say local draft, with identity/contact/effective terms/hosting/legal
approval Pending. Unsupported free/MIT grants, zero-dollar liability assumption,
blanket privacy-law exemptions and old unverified public contact were removed.
Third-party notices and historical LICENSE files remain untouched. Official Chrome
guidance is dated/linked in the matrix; final Dashboard categories, Limited Use
certification, privacy URLs and legal approval remain separate owner/artifact gates.

## Independent verification

Linux Node24.18.0 with existing jsdom; parsed HTML without executing page scripts.

| Check | Result |
|---|---|
| Parse current Privacy/Terms |11/12 legal sections;3 unique IDs each, identical to HEAD. |
| Compare all style/script/link elements against HEAD |Byte-identical resources and executable content. |
| Privacy permission cards vs parsed manifest |Exactly8permissions and1host;matrix includes all9. |
| HTML relative file/fragment resolution |40/40 links resolve. |
| Matrix/index/publisher/delivery Markdown file resolution |54/54 links resolve. |
| Reset handler source assertions |Sync clear +session clear;no local clear or explicit owner/clipboard clear. |
| Draft/contact/trigger wording scans |Draft labels present;no old .io/mailto/MIT footer or uninstall/unload conflation. |
| `sha256sum --quiet -c /tmp/passchef-a-ready-20261026/initial-tests.sha256` |All42 existing extension test files unchanged. |
| `git diff --exit-code -- chrome-extension .github/workflows/build_and_deploy.yml` |exit0;product/config/test bytes unchanged. |
| `git diff --check` |exit0. |

No unchanged unit-suite replay, browser/phone/Google run, storage/account mutation,
live legal URL check, website deployment, Store declaration or legal approval.

## Font-disclosure amendment review —2026-10-26

A06 source inventory exposed an omission in the original A04 review:extension
popup/Options also load bundled `styles/tokens.css`, whose line6 imports Google
Fonts CSS. Root corrected Privacy section7 and the permission/data matrix to cover
possible external stylesheet/font request metadata from extension pages. The
original website-only font disclosure was incomplete.

Independent amendment checks Passed:confirmed CSS import and both HTML references;
parsed amended Privacy with unchanged executable/style/link elements versus HEAD;
all46 local links in matrix/delivery resolve; `git diff --exit-code --
chrome-extension/src chrome-extension/test` and `git diff --check` exit0.
The new wording correctly labels a source finding and actual network verification
Pending, separate from remote executable code or developer telemetry. No runtime
request, provider behavior or final artifact acceptance inferred. A04 preparation
verdict remains Passed with this correction; original checks retain their scope.

## Handoff and unresolved

A04 can be Done as local preparation; next A05 permission audit. C04 publisher/
legal/contact approval, C05 hosting/log retention/HTTPS URLs, A05/B02 necessity,
A06/A13/B07 artifact resources and C03/B08 Sync/deletion acceptance remain open.
Product/website/release remain Partial.
