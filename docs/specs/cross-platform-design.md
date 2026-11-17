# PassChef cross-platform design contract

Date:2026-11-02; 2026-11-10 Android UX amendments (light theme, Picker-only Drive, dot-less wordmark, plain UI words) below supersede earlier rules where stated. **D02 source/staging and [independent review](../../plans/261102-cross-platform-design-consistency/reports/design-contract-independent-review.md) Passed;runtime delivery/visual acceptance Pending.**
Scope:existing website,Android Cookbook and extension UI family. No deployment or
new feature acceptance. [Execution plan](../../plans/261102-cross-platform-design-consistency/plan.md),
[source audit](../../plans/261102-cross-platform-design-consistency/reports/design-baseline-audit.md),
[font/contrast evidence](../../plans/261102-cross-platform-design-consistency/reports/design-contract-fonts.md).

## Baseline and precedence

Current `chrome-extension/src/styles/tokens.css` plus popup/options runtime CSS
is the Precision baseline:warm ink,amber actions,quiet hairlines,Fraunces/Geist roles.
`docs/designs/README.md`supersedes older blue/neon UI tokens;artboards are proposals,
not behavior/runtime proof. Preserve accepted extension journeys and owner-approved
UI01 Display-default deferral;no restored disconnected preferences/global reveal.
Platform-native reading/touch/navigation takes precedence over copying popup pixels.
No universal generated-token package or cross-project dependency import.

## Semantic colors

| Role | Value / usage |
|---|---|
| Canvas / surface / raised |`#0e0e10` / `#15151a` / `#1c1c22` opaque bases.|
| Decorative hairline / strong divider |`#232329` / `#2f2f36`;not sole field/focus/status affordance.|
| Primary / body / secondary / muted readable text |`#f0ece2` / `#d8d2c3` / `#b4ad9f` / `#8a857a`.|
| Disabled/decorative text only |`#6a655d`(ink500),`#44413c`(ink400);never essential small helpers/placeholders.|
| Primary action / hover / pressed |`#e8a341` / `#f5b35a` / `#b97d23`;button text`#0e0e10`.|
| Amber tint / ready tint / error tint |`#3a2a14` / `#1d2818` / `#2b1815`.|
| Ready/success / blocking error / warning |Moss`#84b577` / coral`#e0746c` / honey`#d4a548`;readable word/icon accompanies color.|
| Enabled field boundary / focus |Ink500`#6a655d` outline on ink100 field surface(3.15);on raised ink150 use ink600`#8a857a`(4.62). Amber focus,label remains visible.|

**Light theme (Android, Linen; D13, 2026-11-10).** Dark (Ink+Amber above) and light
are both first-class; app follows system, Settings › Theme = System/Dark/Light. Linen
tokens (`app/src/main/kotlin/dev/passchef/mobile/ui/passchef-palette.kt`): canvas `#f8f5ef`,
surface `#fffdf9`, raised `#ffffff`, hairline `#e3dcc9`, strong line `#d3cabe`, text `#1d1b17`,
body `#2e2b25`, secondary `#4c4840`, muted `#68645b`, outline `#8a8478`, amber `#e8a341`
(fill, on-amber `#1d1b17`), **accent-ink `#8a5610`** (text/icon accent on light; amber on dark),
amber tint `#fbedd2`, moss `#3c7535`/`#e6efe2`, coral `#b4372d`/`#f8e3e0`, honey `#8a6100`.
Amber fill = one main action per screen (plus logo). Chips/toggles/rows do not use amber.
Light-pair contrast: see [palette study](../../plans/261110-android-ux-flow-redesign/reports/06-palette-study.md); not re-verified here.

Actual opaque-pair luminance checks:34assigned normal-text pairs≥4.5,all also≥3.
Ink600 canvas/surface/raised ratios5.25/4.95/4.62;ink5003.34/3.15/2.93 is restricted.
Button text canvas/amber8.95,hover10.54,pressed5.53. Tint text:amber6.41,moss6.47,
coral5.53,honey6.10. Thresholds use unrounded values;large text minimum3,normal4.5.
Transparency,actual fonts,focus,states and rendered content require later checks;
this is not blanket WCAG/platform acceptance.
Ink200/300 outlineVariant/hairlines are structural,not a passed control boundary.
Text-pair checks do not certify non-text controls or focus indicators;actual
component identification/focus/targets/scaling still require runtime verification.
[W3C contrast guidance](https://www.w3.org/WAI/WCAG22/Understanding/contrast-minimum.html).

## Typography and concrete staged assets

| Role | Family / platform use |
|---|---|
| Static ASCII/NFC title/wordmark |Fraunces upright400;italic400 only for short editorial emphasis. Not dynamic user labels.|
| Body/forms/navigation/dynamic Menu/profile/user labels |Geist400,labels500,emphasis600. Dynamic content retains original Unicode.|
| Recipe/Dish/masked Dish/terminal |Geist Mono400–500;terminal FULL BLOCK`█`belongs here. No proportional reformatting of Dish content.|
| Explicit fallback |For decomposed/NFD display text use Geist at component/role level;arbitrary unsupported scripts use native/system fallback with actual rendering verification. Never claim a generic serif is exact Fraunces.|

Four unmodified variable TTFs staged from Google Fonts commit
`6c1dbe27dcf7d817d4b62cec033ba77fa8e2a5ec`,with copyright/OFL1.1 and SHA provenance.
Fraunces upright/italic1.000 axes:wght100–900(default900),opsz9–144,SOFT0–100,WONK0–1.
Geist1.800 and Geist Mono1.701:wght100–900(default400). No conversion/subsetting.
Web:@font-face weight100900,explicit display weight400,normal/italic faces separately,
local URLs,font-display:swap and explicit fallback;no network font dependency needed.
Native:bundle reviewed copies under res/font and specify wght400 for Fraunces;
FontVariation.Settings can make the intended axis explicit. Do not assume raw VF
900 default is Regular. FontWeight can map to variation settings;it is not inherently
ignored. [Android font guidance](https://developer.android.com/develop/ui/compose/text/fonts).

Actual cmap checks:ASCII95,NFC Vietnamese146 and bullet/ellipsis pass all4fonts.
Full NFD Vietnamese22codepoints pass Geist/Geist Mono. Fraunces upright lacks
U+0309/U+0323,italic lacks U+0309;do not call every family NFD-complete. Freeze
static Fraunces strings as verified NFC;use Geist for decomposed/freeform display.
Geist lacks U+2588 from the broad page-text corpus;terminal uses verified Geist Mono.
Never normalize/filter/replace user Recipes,Spices or cooked Dishes to fit a font.
Cmap/name/axis checks are not shaping,mark placement,offline loading or visual proof.

| Scale role | Website | Android | Extension constraint |
|---|---|---|---|
| Display |32–64px responsive,weight400 |28–32sp static title,400 |Existing22–44px roles retained.|
| Body |16px,line1.5–1.65 |14–16sp,20–24sp lineheight |Existing13–14px context retained.|
| Helper/caption |12–14px readable muted |12–14sp;no10sp phone labels |Small mono captions are an existing exception,not phone sizing.|
| Dish |18–24px mono,wrap safely |20–24sp mono,mask remains |Existing22–32px slab rules retained.|

Font scaling/zoom must preserve content/actions. Use static NFC brand headers only;
long dynamic labels use Geist,Recipe/Dish use Mono without truncating the value.

## Spacing, shape and components

4unit rhythm:4/8/12/16/20/24/32/40/56. Website gutters16/24/32px,phone20/24dp,
extension popup20px/options20–32px. Native touch targets≥48dp,website standalone
controls≥44px;inline text links retain readable text flow. Desktop extension keeps
its accepted compact26px icon/30px toggle controls,with actual keyboard/target QA.
Controls4px/dp radius,chips3px/dp;surfaces square/hairline with restrained shadow.
Do not shrink hit areas to match icon size. Respect Android IME/system insets/back.

One component family:Pass/Chef wordmark (Android in-app: no amber dot, "Chef" in accent-ink; website/extension dot removal is the separate D16 plan),header/status,labeled input,
primary/secondary/ghost actions,Menu row,masked Dish slab,helper/warning/error block,
source/cache panels. Busy/disabled/error/empty/ready states have explicit wording;
no silent enabled-looking controls or green primary action. Focus remains visible.
Primary hierarchy differs by task:marketing CTA vs mobile Cook vs extension action.

Navigation is platform-specific:website sections;Android bottom nav Passwords/Enter code/
Settings with header status dot and Lock now (D4);accepted popup states/options tabs.
Do not invent routes or promote QR/scanner/Autofill/new profile/account authoring.
Google Drive access is limited to Picker-selected files (`drive.file`, D1). UI copy uses
plain words; the cooking metaphor is tagline/brand only (D14).
Native biometric/Google surfaces stay native;never imitate account login UI.

Motion:quiet vertical/opacity feedback,honor reduced motion. Preserve B05 mobile
tooltip shared anchoring/wrapping,AOS vertical override,ARIA and MQL legacy fallback.
Avoid glows/neon/glass as primary identity;no overflow hiding to force width Pass.

## Existing exceptions and security invariants

Extension profile identity palette may keep its four existing colors;it is identity,
not primary-action color. Full-tab popup12px outer frame remains a runtime exception.
Compact desktop targets follow the existing [Precision icon-button specification](../designs/README.md#51--cross-screen)
and accepted400px popup;they are not phone touch controls. E01 review clarifies
platform scope,not an owner waiver or measured accessibility certification.
Functional desktop toggle enclosures/circular markers retain their intended shape;
the existing6px language-toggle enclosure is separate from square reading surfaces.
The existing extension launcher PNG is a blue lock/hand mark;it is a separate
legacy brand-asset exception,not an extra action color. Reuse its unmodified
artwork for web/native icon coherence rather than inventing another logo;wordmark
and application controls follow Precision. Android launcher now uses brand kit v2 chef-keyhole adaptive + monochrome icon (D18, 2026-11-10); the legacy PNG is no longer the Android launcher.
The external font import and intentional Demo system fallback are historical.
Reviewed E02 delivers the83file local-font/resource/package migration,with Demo's
matching local faces embedded in its single stylesheet;E03 owns
specific text/field/name/shape roles. Preserve the held74file ZIP as historical;
all46canonical test files and security/behavior code remain unchanged.

Android controllers/session/cache/provider ownership remain intact. Preserve
remember(notSaveable)sensitive inputs,background clearing/lock,5minute idle/10minute
total deadline,10second reveal,30second owned clipboard cleanup and FLAG_SECURE. Own Picker/consent window: unlocked ≤120s only while that intent is pending (D2).
No raw Dish/Spice in previews/logs;synthetic design boards are proposals,not device
screenshots. Keep EN/VI labels/testTags and busy/invalidation paths. UI does not
infer first setup from status strings or read repository state in composition.

## Delivery and verification boundaries

D02 stages/contract only. W01 copies reviewed fonts/licenses into website assets,
changes shared CSS foundation and3HTML font heads;no DOM/handler/legal/CTA rewrite.
M01 bundles native font resources/theme/shell with explicit axes;Android resource
filenames use required lowercase underscores,other new source files kebab-case.
Each task:compile/static/affected existing checks→independent review→update plan→next.
Golden tests immutable;do not weaken visibility/width assertions to pass styling.
Fresh source-bound browser/phone QA establishes actual family/fallback/flow behavior;
build success and synthetic board do not establish accessibility/device acceptance.
Deployment remains owner-deferred until alignment;C05 service choice can wait.

Unresolved:actual shaping/font-loading/axis rendering,
font-size/IME/large-text/visual acceptance after web/native delivery;fresh resources.
