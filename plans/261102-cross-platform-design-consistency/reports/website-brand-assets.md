# W03 — website brand assets

Date:2026-11-02. Source/assets complete;[tester verification](website-brand-assets-verification.md)/[independent review Passed](website-brand-assets-independent-review.md). Website/browser acceptance remains Pending.

## Delivered scope

Nine files changed/new:shared CSS,threeHTML,`assets/favicon.svg`,`og-image.svg`,`og-image.png`,new `passchef-mark.png`,`brand-provenance.json`. Ten other website files remain byte-identical,including JS,README,fourTTFs/threeOFLs/font provenance. Root's original17-file snapshot retained under ignored `website-brand-assets/before/landing-page/` plus `before.json`.

All six nav/footer crosshair SVGs replaced with the original extension blue lock/hand PNG,unmodified bytes. Empty image alt avoids duplicating the adjacent wordmark. Static Fraunces400 wordmark uses muted Pass/primary Chef/amber dot;dot aria-hidden. Legacy blue mark is the existing contract exception. Three theme-color values now warm canvas;favicon references PNG with correct type and128×128 size. Retained SVG favicon is a wrapper embedding identical PNG bytes,no obsolete crosshair.

Nav/footer wordmarks,nav links,mobile-menu links and footer-list anchors receive44px minimum width/height;nav/footer text links center within their targets. Existing button/social44px floors unchanged. Inline legal paragraph links untouched. Original navigation hrefs,all DOM IDs,section order,labels,demos,ARIA/AOS/tooltip rules and JS preserved. No capability/claim/legal copy additions.

OG retains “PassChef / Your Recipes. Your Spices. / Cook on demand.” Original mark,opaque Precision canvas/surface,hairlines and outlined typography;no product screenshot or competing mark. Portable1200×630 SVG embeds original PNG plus glyph paths,with matching PNG export. Public provenance contains repo/asset paths and hashes,no local machine paths;original font/OFL provenance remains linked.

## Headless export and actual asset checks

Raw:`chrome-extension/build/cross-platform-design-consistency-20261102/website-brand-assets/`.

- `build-brand-assets.py`:isolated Fontconfig directory points only at reviewed web fonts;Pango1.52.1/Cairo `--no-display --pixels --background=transparent --margin=0` with explicit `@wght=400`. Wordmark72px and Recipe heading76px use Fraunces upright;Spices76px Fraunces italic;tagline30px Geist. Raw commands/fontconfig/four serialized Pango layouts and line SVGs under `export/`.
- Actual shaped six font runs match reviewed font SHA256 and explicit400 descriptions;unknown glyphs0,not wrapped/ellipsized. Cairo glyph IDs prefixed per line;43unique IDs and all references resolve. No live SVG text/font request or remote image dependency.
- Raster command:`/home/vantt/.local/bin/ffmpeg -hide_banner -loglevel error -i landing-page/assets/og-image.svg -frames:v 1 -y landing-page/assets/og-image.png` (FFmpeg n9.0.2/librsvg). PNG signature/dimensions1200×630 verified. Author opened actual PNG using `view_image`:mark/wordmark/threecopy lines visible,no clipping. Root independently opened the actual PNG and observed the original blue brain-lock mark,readable wordmark/headlines/subline and no clipping within frame;this is asset inspection only,not website/native acceptance.
- Exact original icon SHA256 `b0d51f0ee397ed78c817c707089ea146abce4f0e046418fb234410d6d4eb5710`;copied128×128 PNG and both embedded image payloads match. No source artwork redraw/resampling. Displayed sizes are CSS/SVG layout only.
- OG PNG SHA256 `fc0e27978b1517b08d7c8dbd75aec6050cf2e92aab0de067ddc34ab940e8d7b9`. All text layout boxes fit within card bounds. `verify-assets.py`→`asset-verification.json` records exact font runs/image references/bounds.

## Source and preservation checks

`verify-brand.mjs`→immutable `after.json`:three HTML files match exactly the permitted two brand substitutions/theme/favicon changes;all other body/head bytes exact. Sixbrand instances,74local href/src references resolve including fragments. CSS4font URLs local and assets/threeOFLs unchanged. Shared/2inline styles parse with PostCSS8.5.6:597rules,total warnings0,exact roundtrip. Both new ignored JS helpers `node --check` Passed;`git diff --check`exit0.

All20 B05 geometry selector names/all30 actual rule occurrences,at-rule contexts and10protected properties unchanged. Android113source files match M04run01;extension74runtime files/tests46,held ZIP and workflow match protected baselines. No Gradle/ADB/GUI/browser/server/network/publish action.

Text diff:`brand-text.patch` SHA256 `cf5f7aab0869c996e0eda86467d7e124a919504191e314522fc5111741f4e855`;binary old/new hashes in `binary-identities.json`;all19current file identities in `after.json`. Source helper commands and receipts retained,old W01/W02/runtime snapshots not rewritten.

## Remaining gates

Fresh website QA must cover320/390/768/1440,brand/nav/footer wrapping/target boxes,keyboard/focus/zoom,all affected tooltip and Dish/Copy geometry/settled opacity,initial/live reduced motion and font loading. Earlier B05 screenshots/pass cells are historical,not current redesign acceptance. Local PNG inspection does not certify browser/social-provider favicon/OG rendering. Existing metadata copy,URLs/provider/legal/release gates remain;no deployment.

Unresolved:fresh runtime slot;integrated cross-platform visual acceptance and deployment hold.

Final bookkeeping2026-11-02:source/assets,tester evidence and independent dispatch
review Passed;W03 source Done,E01 read-only audit Ready. Browser/native/owner and
public-domain/provider acceptance remain Pending,deployment held.
