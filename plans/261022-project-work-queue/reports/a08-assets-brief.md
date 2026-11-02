# A08 — Actual-capture brief and asset inventory

Date:2026-10-26, Asia/Saigon. Source:HEAD039c76d + preserved WIP;
manifest2.3.0, A13 stable candidate Pending. Status:**Done2026-10-26 — brief/inventory preparation**;
[independent review Passed](a08-independent-review.md).
No GUI, capture, artwork generation, image edit, upload, push or publication.

## TODO

- [x] Read actual Popup/Builder/Options/export/error UI and current graphic guidance.
- [x] Inspect actual asset existence and PNG/SVG metadata.
- [x] Replace historical AI mockup prompts with5truthful candidate capture briefs.
- [x] Check selectors, synthetic Recipes, inventory and links against source/files.
- [x] Independent review Passed; registries updated; B06 captures/rights/visual approval remain Pending.

## Exact scope and delivery

Only2files:A08 [corrected capture/asset brief](../../../docs/commercialization/marketing/cws-screenshot-prompts.md)
and this report. Historical AI-generated product UI prompts replaced with5actual
candidate-capture specifications:Generated Dish, Popup Builder, Options routing,
Full Access wizard and actual Tag-mismatch Home. No product/asset/script/test files
changed, and no final screenshot/image was created or represented as existing.

Current readonly Generated Dish is visible `type=text`, not an invented masked/eye
field. The brief requires deliberately synthetic data and actual source UI, keeps
Builder in the popup, and rejects fictitious countdown/sidebar/phone-receiver
success. It records frame/selectors/expected state/captions, synthetic fixture,
candidate/desktop gates, raw/editable/edited image records, rights review and cleanup.

Actual fields/options can remain untranslated in some wizard paths; the brief
requires capturing real localized UI rather than repainting missing translations.
There is no broad localization/visual/platform acceptance from source inspection.

## Official graphic check —2026-10-26

Read current official [image requirements](https://developer.chrome.com/docs/webstore/images),
[listing graphics overview](https://developer.chrome.com/docs/webstore/cws-dashboard-listing)
and [actual-experience guidance](https://developer.chrome.com/docs/webstore/best-listing).
The brief distinguishes mandatory icon/screenshot, optional current small/marquee
tiles, project-selected PNG masters and still-unknown Dashboard byte caps. The old
large-tile dimensions are removed. No Dashboard login/upload/acceptance occurred.
OG target dimensions are labeled an internal website design choice, not Chrome rules.

## Current inventory findings

PNG metadata was read from actual signature/IHDR fields, not filename assumptions.

| File | Actual dimensions / finding | SHA256 |
|---|---|---|
| `icon-16.png` |16×16 RGBA8,868bytes |`7dd8445ee3066f574397e75e82286d739aae451bb505702e7e1f5d7cfa067c53` |
| `icon-32.png` |32×32 RGBA8,2530bytes |`9c3d6ca6cba3bf133a56a6c0effbd66cfa87ab865a183dfe026335299f676a7c` |
| `icon-48.png` |48×48 RGBA8,4689bytes |`668222a6b07e72e631a3bf03b791238a46bec7f8a187d34b00e481bf495113a2` |
| `icon-128.png` |128×128 RGBA8,18875bytes |`b0d51f0ee397ed78c817c707089ea146abce4f0e046418fb234410d6d4eb5710` |
| `icon-1024.png` |**680×680**,242743bytes; filename is not pixel size; A06 excludes it |`cc9bb1f3d308d108fcccdaaef53c7b895441b0dbc09199db91d76c63b93ec445` |

Files above are under `chrome-extension/src/assets/`; the first4are manifest icons.
RGBA availability is not proof of transparent-margin pixels, visual weight, light/
dark readability or permission to redistribute. No artwork source/creator/license
approval was established; rights and visual approval remain Pending. No rename,
resize, alpha/pixel edit or invented1024px master was created.

`landing-page/assets/favicon.svg` parses as SVG with viewBox0 0 32 32; it is not a
Store PNG. `landing-page/assets/og-image.png` is missing while index's og:image
still references it relatively. No dedicated final CWS captures, small tile or
marquee exists in the inspected asset/marketing paths. Existing Demo PNGs/security
diagram are not substituted as final release captures. Proposed output names in
the brief are plans, not links to nonexistent screenshots.

## Actual local verification

Node24.18.0; existing JSDOM, parser and cooking modules imported for bounded checks.
No full-page scripts, resource fetching, browser, image generation/editing or GUI.

| Check | Result / limit |
|---|---|
| Static source selectors |28/28brief Popup/Options selectors resolve in actual HTML. Generated field is readonly type=text; no password-type field in Generated. This is source verification, not rendered state acceptance. |
| Dynamic export selectors |6/6IDs exist in actual wizard-generated HTML templates. No wizard runtime was invoked. |
| Synthetic Recipe fixture |All3Recipes parse in current production parser; `r4nd0m#1` + synthetic `Basic*` cooks exact `Basic*r4nd0m`. No real credential or Menu write used. |
| Asset metadata |5PNG signatures/dimensions/color type/byte sizes/digests read; SVG parses; missing OG and exact relative index reference confirmed. No visual/padding/rights proof claimed. |
| Brief/report local file links |6valid before report expansion; final handoff rechecks added links. Planned image basenames remain plain text, not fake evidence links. |
| `git diff --exit-code -- chrome-extension/test chrome-extension/src/manifest.json chrome-extension/src/assets landing-page/assets` |exit0; original tests/manifest/assets unchanged. No unchanged unit-suite rerun needed for this brief. |
| `git diff --check` |exit0. No image/website runtime or Store acceptance claimed. |

The five actual shots remain Pending B06 from a stable A13 candidate. Optional
non-UI artwork descriptions are separate, minimal briefs without fake product
windows, unsupported security claims or generated output. A14/C11 still need
approved rights, final graphics and accepted artifact binding.

Closed as A08 preparation after independent review; next A09 Ready.

## Unresolved

C01/C02/A13 stable candidate; B06 desktop capture; owner artwork rights/provenance;
C04 listing locales; C05 OG absolute public URL and publication route.
