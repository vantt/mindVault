# W03 — independent brand asset/source verification

Date:2026-11-02, Asia/Saigon. **Asset/source/compiler verification Passed;
[independent review Passed](website-brand-assets-independent-review.md). Website/browser/viewport acceptance Pending.**
Parent:[website phase](../phase-02-website.md),[author delivery](website-brand-assets.md).
Tester performed read-only source,exported-asset and recorded shaping analysis.

## Actual exported assets and font evidence

Original extension128PNG is byte-identical to newpasschef-mark.png,SHA256
`b0d51f0ee397ed78c817c707089ea146abce4f0e046418fb234410d6d4eb5710`.
BothfaviconSVG andOGSVG embed those exact original bytes once. Six HTML logo slots
now reference that original mark;old competing inline crosshair shapes are removed.
The blue legacy lock/hand remains the documented mark exception;new surrounding
wordmark/card use Precision. OtherSVGpaths are glyph outlines and decorative rules.

OGPNG is actual1200×630,8bitRGBA;all18PNGchunk CRCs pass. A read-only FFmpeg decode
shows alpha255everywhere and canvas cornerRGBA14/14/16/255. Original mark remains
128×128RGBA with its encoded bytes unchanged. OGPNG SHA256
`fc0e27978b1517b08d7c8dbd75aec6050cf2e92aab0de067ddc34ab940e8d7b9`.
SVG contains43unique IDs and49resolved glyph uses;hrefs/url references are embedded
PNGorinternalIDs. No live text,script,foreignObject,external resource or DTD/entity.
BothSVGs parse;viewBoxes are128×128favicon and1200×630OG.

Read4actual Pango serialized layouts and command records:6shaped runs select exact
staged Frauncesupright/italic orGeistchecksums with explicitwght400;0unknown glyphs,
no wrapping/ellipsis. Fontconfig contains only the reviewed font directory and no
system includes. This is actual exported-card shaping evidence,not just family-name
labels;it does not establish browser fonts or arbitrary-script fallback. Four line
bounds fit the card frame. Public provenance matches asset/font hashes and has no
machine paths. All4TTFs/3OFLs remain unchanged/read-only inputs. Root separately
inspected the actual PNG for readability/framing;that is an asset-only preview.

## HTML/CSS scope and preserved behavior

Exactly7existing website files changed and2assets added(markPNG/provenance);10other
files match the17-file pre-edit snapshot,including JS,README and8font/license/
provenance files. All6nav/footer instances contain original32pxmark+Pass/Chef/dot;
dot is aria-hidden. All3heads declare warmcanvas theme-color and local128PNGfavicon.
Brand parents retain Fraunces400 and44px source minimums.

Canonical comparison removes only2brand-anchor contents perHTML plus the allowed
theme/favicon metadata. Everything else is byte-identical:marketing/legal copy,
IDs,CTA/hrefs,non-brandARIA,demos/order and all inlinelegalCSS. New brand decoration
is confined to its allowed slots. Currentlocal href/src74+OGmetadata1=**75HTMLlocal
references**,all resolve;4CSSfont refs remain separate. Fiveexternal/7placeholder
refs remainNotExercised,noHTTP/provider/Store/publication claim.

Fresh PostCSS8.5.6 checks529shared+34Privacy+34Terms=597rules,exactroundtrip,0warnings.
Outside the10allowed brand/standalone-anchor selectors,the entire semanticCSS tree
is identical. All30critical B05rule nodes/31selector memberships,20selectors and
10geometry-property lists retain exact values/importance/at-rule contexts. Global
button/social source floors remain;actual target geometry needs browser checks.
No new content hiding,JS behavior,font source or inlinelegal spacing change.

CurrentAndroid113/M04source bindings and8preserved build artifacts,liveAPKs,
extension74/tests46/heldZIP and deploymentworkflow all retain hashes. No Gradle/
ADB/browser/server/GUI/network action by tester;no application or physical-case replay.

## Actual fresh commands and receipts

```bash
/home/vantt/.nvm/versions/node/v24.18.0/bin/node --check chrome-extension/build/cross-platform-design-consistency-20261102/website-brand-assets/verify-tester-css.mjs
/home/vantt/.nvm/versions/node/v24.18.0/bin/node chrome-extension/build/cross-platform-design-consistency-20261102/website-brand-assets/verify-tester-css.mjs
/home/vantt/.local/bin/uv run --offline --no-project --python /usr/bin/python3 python chrome-extension/build/cross-platform-design-consistency-20261102/website-brand-assets/verify-tester-assets.py
/home/vantt/.local/bin/uv run --offline --no-project --python /usr/bin/python3 python chrome-extension/build/cross-platform-design-consistency-20261102/website-brand-assets/verify-tester-source.py
git diff --check
```

All exit0. Fresh immutable receipts:
[CSS/geometry](../../../chrome-extension/build/cross-platform-design-consistency-20261102/website-brand-assets/verification-tester-css.json),
[assets/shaping](../../../chrome-extension/build/cross-platform-design-consistency-20261102/website-brand-assets/verification-tester-assets.json),
[HTML/protection](../../../chrome-extension/build/cross-platform-design-consistency-20261102/website-brand-assets/verification-tester-source.json).
Scripts remain<200lines;asset verifier decodesPNGto memory only,without regenerating
or editing it. Old W01/W02/B05 receipts remain immutable and source-bound.

## Unresolved

Fresh website fonts/viewport/zoom/keyboard/ARIA/motion/
tooltip/settledDish/Copy and actual target acceptance;browser/favicon/social-provider
rendering. Deployment remains owner-held;asset review is not website visual approval.
