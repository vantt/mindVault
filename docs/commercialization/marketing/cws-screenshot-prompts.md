# Chrome Web Store capture brief — PassChef

> **Status:** Corrected A08 local brief2026-10-26; no final captures/artwork accepted.
> Replaces historical AI product mockup prompts. Five actual-product shots are planned;
> A13 stable candidate/C01–C02 gates and B06 desktop capture are still required.
> Current source manifest2.3.0 is context, not an approved release version.

## Capture gates and truthful presentation

Use actual bundled UI from the reviewed candidate in an isolated synthetic Cookbook.
Record candidate source/ZIP/version/SHA256, browser/OS, locale, viewport/scale,
selected Menu fixture and exact capture/edit inventory. A06/A07 development ZIPs
alone do not unlock B06. Owner must have supplied the desktop slot or reviewed
isolation; any provider/account/write action needs its own authorization/readiness.
Do not open or write a real/private Menu merely to prepare these shots.

Source truth:[Popup](../../../chrome-extension/src/popup/popup.html),
[Builder](../../../chrome-extension/src/popup/popup-recipe-builder.js),
[Options](../../../chrome-extension/src/options/options.html),
[export](../../../chrome-extension/src/options/options-export-wizard.js) and
[Generated contract](../../ui-specs/04-popup-generated.md).
Current Precision styling comes from the candidate; don't redraw it as the obsolete
neon/glass/sidebar concept. The Popup Generated field is readonly `type=text`:
its synthetic Dish is visibly shown. There is no eye toggle or countdown strip
on that view. Builder lives inside the popup, not a Google Menu sidebar.

Capture native UI first; keep raw pixels and any source/editable composition separately.
Do not invent controls, badges, countdowns, masked states, provider content or success
messages. Crop/resize proportionally without changing UI; annotate sparingly and log
all redaction/edits. Show only deliberately synthetic Dishes. Remove account avatars,
email/bookmarks/private URLs and identifiers before publication. A routed Menu
fixture can prove extension UI, but must not be described as a fresh Google/native
acceptance or photographed real provider session.

## Synthetic fixture — preparation contract

Disposable own profiles `Demo Personal` and `Demo Work`; labels are examples, not
real people/testimonials. Select a dedicated authorized synthetic Menu. In the
fixture only, Spice1=`Basic*`, Recipe=`r4nd0m#1`, expected Dish=`Basic*r4nd0m`.
These strings are deliberately public and must never protect a real account.
Other rows may use valid `mx3il@2`/`str34m$3` with separately prepared synthetic jars.
No private master/sharing password or real QR/bundle is captured.

Generate any Tag through the actual candidate Builder for the selected fixture
Menu/profile. Never paste a decorative `.tag` as if verified. For mismatch shot,
use a separate synthetic context and confirm the presented Tag fails verification;
leave a clean fixture for later runs. This brief authorizes no live Menu write.
Do not replay unrelated accepted cases to fill a screenshot session.

## Five actual-product shots — all Pending B06

Proposed basenames only; these files do not yet exist. Use EN first as a draft;
C04 chooses final listing locales. Any VI set must show actual localized UI and
matching approved captions; untranslated wizard text must not be silently repainted.

| Shot / proposed basename | Actual flow, source selectors and expected frame | Draft caption |
|---|---|---|
| 01 — `cws-01-cook-en.png` | Select the fixture Recipe cell, then open actual Chrome action popup. Show `#status-generated`, `#global-status`, `#gen-profile-label`, visible synthetic `#gen-password`, `#btn-copy`, `#btn-build-recipe-gen`, Back/Lock as present. Use only synthetic provider background; do not claim a shortcut was pressed unless actually observed. | “Select a Recipe. Cook a Dish.” |
| 02 — `cws-02-builder-en.png` | Enter through `#btn-build-recipe` or `#btn-build-recipe-gen`. In `#status-builder`, set `#bld-hash`, `#bld-position-group`, `#bld-secret-group`; explicitly expand `#bld-more-options` to show `#bld-sheet-url`/`#bld-profile` if needed. Capture actual `#bld-recipe-out`, `#bld-password-out`, `#btn-copy-recipe`. Initial random style/jar and collapsed options are real defaults; no fabricated remembered defaults or forced expansion. | “Build a Recipe in the popup.” |
| 03 — `cws-03-profiles-en.png` | Actual Options `#dashboard-section`, horizontal `#tab-bar`/`.tab[data-tab="profiles"]`, `#tab-profiles`, `#own-profiles-list`, `#sheet-assignments-list`, `#select-default-profile`. Show synthetic profiles/assignments only; no imaginary sidebar/security/about tabs or unlimited-profile claim. | “Choose Spices for each Menu.” |
| 04 — `cws-04-sharing-en.png` | Actual `#modal-export` from a synthetic own-profile export action. Prefer sharing-password step:actual `#exp-sharing-pwd`, `#exp-sharing-pwd-confirm`, `#exp-generate`, keeping fields empty/masked. If showing result instead, `#exp-bundle-output`, `#btn-show-qr`/`#exp-qr-panel` must be genuine synthetic output; do not add a phone-success state. Avoid publishing scannable private QR or actual bundle/password. | “Full Access delegates your Spices.” |
| 05 — `cws-05-tag-mismatch-en.png` | With a known failing synthetic Tag/context, open actual popup Home:visible `#status-unlocked`, `#home-notice`/`#home-notice-text`, no visible `#status-generated` Dish. Source Home translates the mismatch; capture the real localized guidance. Frame the warning as a present-Tag check, not universal tamper protection. | “A mismatched Tag stops cooking.” |

Shot04 caption context must explain:Full Access bundle plus sharing password lets
the recipient recover the original Spices and cook the same Dishes. A sender/QR
image does not establish completed phone receiving/import. Shot05 does not imply
legacy untagged Recipes are refused; removing a Tag removes that check.
Clipboard text must retain the conditional ownership/30s limits from the
[A03 listing](cws-listing-copy.md), not an invented timer or unconditional erasure.

## Current graphic specification check —2026-10-26

| Asset | Official requirement / project choice | Acceptance still needed |
|---|---|---|
| Store icon |128×128 PNG; square artwork guideline96×96 plus transparent16px margins. | Actual padding, visual weight, light/dark legibility and rights; dimensions alone insufficient. |
| Screenshots |At least1, at most5;1280×800 or640×400, square corners/full bleed. Project chooses1280×800 PNG masters. | Real candidate pixels, legibility at640×400, honest edits/locale/source binding. |
| Small promotional tile |Optional440×280; PNG/JPEG. | Brand artwork/rights/review; not a product screenshot. |
| Marquee promotional tile |Optional1400×560; PNG/JPEG. | Brand artwork/rights/review; old920×680 brief is superseded. |

Sources:[Supplying Images](https://developer.chrome.com/docs/webstore/images),
[listing fields](https://developer.chrome.com/docs/webstore/cws-dashboard-listing),
[actual-experience guidance](https://developer.chrome.com/docs/webstore/best-listing).
The listing overview emphasizes1280×800; detailed image guidance also permits640×400.
No official byte-size cap was established from these opened pages; recheck actual
Dashboard constraints during A14/C11. Promo tiles are not locale-specific in the
current guidance. Final graphics must follow Google brand rules where applicable.

## Existing asset inventory — metadata, not visual/rights approval

| Existing path | Actual metadata / current use | Status / rights |
|---|---|---|
| `chrome-extension/src/assets/icon-16.png` |16×16 PNG RGBA8,868bytes; declared manifest icon. | Present; owner artwork provenance/rights and visual QA Pending. |
| `chrome-extension/src/assets/icon-32.png` |32×32 PNG RGBA8,2530bytes; declared manifest icon. | Present; same limits. |
| `chrome-extension/src/assets/icon-48.png` |48×48 PNG RGBA8,4689bytes; declared manifest icon. | Present; same limits. |
| `chrome-extension/src/assets/icon-128.png` |128×128 PNG RGBA8,18875bytes; declared/packaged icon and Store-icon candidate. | Size matches; transparent-margin pixels/visual suitability/rights not verified. |
| `chrome-extension/src/assets/icon-1024.png` |**680×680** PNG RGBA8,242743bytes, despite filename; not declared/shipped by A06. | Existing raster, not a1024px editable master or accepted Store asset. No resize/rename here; provenance/rights Pending. |
| `landing-page/assets/favicon.svg` |SVG viewBox0 0 32 32; existing website favicon. | Vector file present; not a128px PNG Store icon. Rights/brand consistency Pending. |
| `landing-page/assets/og-image.png` |Missing; index og:image still points at this relative path. | No OG image accepted. C05/A22 must bind future file to verified absolute HTTPS URL. |
| CWS captures/small promo/marquee |No dedicated final files in inspected assets/marketing paths. | Missing/Pending B06 or later authorized artwork. Existing Demo PNGs/security diagram are not final Store captures. |

A proposed OG master of1200×630 is an internal website design target, not a Chrome
requirement or a verified social-platform acceptance. No editable raster master,
creator/license record or artwork approval was found in the inspected asset paths.
Repository presence is not proof of ownership/redistribution rights; owner must
confirm before A14/C11. SVG can be edited as vector if later authorized; no bitmap
regeneration is requested by this brief.

## Optional non-UI artwork — brief only, no output created

Small tile:existing approved logo/wordmark with a simple Recipe-and-Spice cooking
motif; use actual candidate brand tokens as reference. Keep text minimal, omit
mock controls, review badges, user counts and false zero-storage/no-server claims.
Marquee:extend the same abstract composition to1400×560 without suggesting a phone
receiver or displaying a fake extension window. If a security diagram is later
wanted, label it “Illustration” and separate Recipe cooking from Cookbook protection;
Google-hosted Menu/Sync must not be drawn as nonexistent infrastructure.

## B06 evidence and completion checklist

- Freeze actual source/ZIP/version/SHA/locale before capture; prepare only synthetic data.
- Save raw capture, edited deliverable, editable annotation source if used, image dimensions/hash and exact steps/source selectors. Proposed filenames above remain Pending until files exist.
- Check UI state/Dish/profile/Tag against the fixture; real pixels, no control or typography fabrication.
- Review all5shots for source consistency,640×400 readability, sensitive content and rights. Preserve failed/missing captures as such.
- Remove disposable profile/fixtures and owned clipboard content under the approved resource slot; return focus/desktop and record cleanup. No actual account deletion/revocation presumed.
- A14 binds approved final images and rights to the accepted release artifact. A08 brief Done never means B06 capture/release acceptance Done.

Unresolved:C01/C02/A13 candidate; B06 slot/captures; C04 locale/rights decisions;
C05 OG/live URLs; actual Dashboard limits and final legal/asset approval.
