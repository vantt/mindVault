# D02 — design contract and official font staging

Date:2026-11-02, Asia/Saigon. **Authoring/local metadata checks complete;
independent source/staging review Passed. D02 not Done;no product UI/font delivery yet.**
[Canonical contract](../../../docs/specs/cross-platform-design.md),
[phase](../phase-01-contract.md),[source baseline](design-baseline-audit.md).
Default Precision direction is root-adopted,not owner approval of exact font assets.
Deployment remains owner-deferred;no GUI/browser/phone/Gradle/ADB/deploy action.

## Actual provenance and license evidence

Google Fonts API commit/three directory listings discovered actual filenames at
commit`6c1dbe27dcf7d817d4b62cec033ba77fa8e2a5ec`(committer2026-10-30T14:03:29Z),
observed2026-11-02T11:12:56UTC. Raw API metadata under ignored
`chrome-extension/build/cross-platform-design-consistency-20261102/font-stage/`.
[Provenance](../../../chrome-extension/build/cross-platform-design-consistency-20261102/font-stage/provenance.json)
retains exact pinned raw URLs/repository paths/Git blob SHA/SHA256/bytes.
All10selected font/license/METADATA bytes match the API Git blob identity;four
TTFs remain unmodified—no conversion,subsetting,instance export or font renaming.
Local filenames are aliases only;internal names/axes remain upstream bytes.

| Staged TTF / actual repository filename | Bytes / version | SHA256 |
|---|---|---|
|`fraunces-regular.ttf` ← `ofl/fraunces/Fraunces[SOFT,WONK,opsz,wght].ttf`|360440 /1.000|`177ff6c0f14e5550a3c624247cd1189611d4eb65d000b14944c63d967958abbb`|
|`fraunces-italic.ttf` ← `ofl/fraunces/Fraunces-Italic[SOFT,WONK,opsz,wght].ttf`|414904 /1.000|`b24448c43702fac4ee856781d461a0dfba8d8e594b6e8e190234b75fed2c0e01`|
|`geist.ttf` ← `ofl/geist/Geist[wght].ttf`|169056 /1.800|`73894e0448cae90a92b6c2f8732b7bb9acb7b94c418bff559dad4a18e1de9659`|
|`geist-mono.ttf` ← `ofl/geistmono/GeistMono[wght].ttf`|171948 /1.701|`d00e590b8eb3a59acc329b2d044fd143ae935090b7da33199ebee27cc7de8196`|

Total TTF bytes1116348. Each family's actual OFL1.1 and METADATA copied alongside.
Fraunces OFL copyright2018 vs font name/METADATA copyright2020 both retained as
upstream records;not silently reconciled. Geist/Geist Mono copyright2024 retained.
License/name records identify Fraunces/Geist Project Authors;bundle original
copyright/OFL with future copies,do not sell font files alone or imply endorsement.
[Pinned Fraunces OFL](https://raw.githubusercontent.com/google/fonts/6c1dbe27dcf7d817d4b62cec033ba77fa8e2a5ec/ofl/fraunces/OFL.txt),
[pinned Geist OFL](https://raw.githubusercontent.com/google/fonts/6c1dbe27dcf7d817d4b62cec033ba77fa8e2a5ec/ofl/geist/OFL.txt),
[pinned Geist Mono OFL](https://raw.githubusercontent.com/google/fonts/6c1dbe27dcf7d817d4b62cec033ba77fa8e2a5ec/ofl/geistmono/OFL.txt).
Web-tool API/directory fetches were inaccessible;actual public urllib API/raw GETs
succeeded,and primary pinned OFL pages were read with web. No account/auth request.

## Metadata/cmap checks — real gaps preserved

Pinned uv-managed FontTools4.60.1 in ignored build/.toolchain/font-stage,CPython3.12.3;
no product dependency/lockfile changes. [Actual verification](../../../chrome-extension/build/cross-platform-design-consistency-20261102/font-stage/font-verification.json)
reads family/version/copyright/license,all fvar ranges/defaults,nonzero gvar glyphs
and non-.notdef cmap targets. Fraunces698glyphs and696/697nonzero variation glyphs;
Geist975/967;Geist Mono1159/1031. All variable axes have valid nonzero ranges.
Fraunces wght100–900 DEFAULT900,opsz9–144,SOFT0–100,WONK0–1;Geist/Mono wght100–900
DEFAULT400. `regular` is an upright VF alias,not proof that a raw instance is400.

| Coverage group | Actual result / assignment |
|---|---|
|ASCII95 + complete Vietnamese NFC146 + mask bullet/ellipsis2|All4TTFs have every required codepoint.|
|Complete Vietnamese NFD22 unique codepoints|Geist/Mono complete;Fraunces upright missingU+0309/U+0323,italic missingU+0309. No universal NFD pass.|
|Static display/headings corpus71|Both Fraunces faces cover actual sampled static source characters. Reserve them for static ASCII/NFC display;weight400 explicit.|
|Broad body source corpus150|Geist covers all exceptU+2588`█`,which belongs to the terminal's Mono role.|
|Recipe/Dish/terminal|Geist Mono covers tested ASCII/NFC/NFD,mask,ellipsis,arrows and actual FULL BLOCK glyph. Arbitrary other scripts still need platform fallback/render checks.|

Vietnamese set=12vowel bases×6tones×2cases+Đ/đ,then Unicode NFC/NFD expansion;
ASCII includes ordinary consonants/digits/punctuation. Role corpora read current
threeHTML visible text/headings and Android strings,plus explicit mono examples.
[21role-policy checks](../../../chrome-extension/build/cross-platform-design-consistency-20261102/font-stage/role-policy-verification.json)
Passed with declared restrictions;original all-required/all-font NFD result stays
false. For NFD/freeform display choose Geist;never normalize user Recipes/Spices/
Dishes or pretend generic serif is Fraunces. Actual shaping/mark placement/font
loading/variable-axis rendering/owner visual acceptance remain NotRun.

Native M01 must bind explicit wght400/settings for Fraunces;FontWeight can map to
variation settings,so do not claim it is always ignored. No downloadable provider
needed. [Android bundled-font guidance](https://developer.android.com/develop/ui/compose/text/fonts).
Web W01 uses local normal/italic @font-face100–900 plus explicit display400.

## Actual contrast and representative board

[Contrast receipt](../../../chrome-extension/build/cross-platform-design-consistency-20261102/font-stage/contrast-verification.json)
uses standard sRGB relative luminance/unrounded thresholds:37opaque pairs,34assigned
normal-text pairs all≥4.5(also≥3large). Ink500text fails normal at3.336/3.147/2.933
on canvas/surface/raised;disabled/decorative text only. Ink6005.25/4.95/4.62 is readable
muted. Primary button canvas text/amber8.95,hover10.54,pressed5.53;status tint pairs
amber6.41,moss6.47,coral5.53,honey6.10. No rounding a failure into Pass.
[Non-text pair receipt](../../../chrome-extension/build/cross-platform-design-consistency-20261102/font-stage/nontext-contrast-verification.json):
Ink500 outline on Ink1003.147 allowed;on Ink1502.933 prohibited(useInk6004.616).
Ink200/300 hairlines remain structural. Color-pair math is not rendered text,
control/focus/target/accessibility or blanket WCAG certification.
[W3C thresholds](https://www.w3.org/WAI/WCAG22/Understanding/contrast-minimum.html).

[Local representative board](../../../chrome-extension/build/cross-platform-design-consistency-20261102/font-stage/design-board.html)
uses all4actual relative TTF URLs and sample website/Android/extension roles with
explicit DESIGN PROPOSAL labels. No scripts/external URLs/account/Copy action.
HTML/relative files and PostCSS inline-style parse checked;not rendered or a native
screenshot. Android/website will deliver separate reviewed asset copies later.

## Commands, failure history and next slice

Actual commands from repository root:

```bash
/home/vantt/.local/bin/uv venv --python /usr/bin/python3 chrome-extension/build/.toolchain/font-stage
/home/vantt/.local/bin/uv pip install --python chrome-extension/build/.toolchain/font-stage/bin/python fonttools==4.60.1
/home/vantt/.local/bin/uv run --offline --no-project --python /usr/bin/python3 python chrome-extension/build/cross-platform-design-consistency-20261102/font-stage/stage-fonts-02.py
/home/vantt/.local/bin/uv run --offline --no-project --python chrome-extension/build/.toolchain/font-stage/bin/python python chrome-extension/build/cross-platform-design-consistency-20261102/font-stage/verify-fonts.py
/home/vantt/.local/bin/uv run --offline --no-project --python chrome-extension/build/.toolchain/font-stage/bin/python python chrome-extension/build/cross-platform-design-consistency-20261102/font-stage/verify-role-policy.py
/home/vantt/.local/bin/uv run --offline --no-project --python /usr/bin/python3 python chrome-extension/build/cross-platform-design-consistency-20261102/font-stage/verify-contrast.py
```

All listed commands exit0. Offline uv means no dependency resolution during checks;
the staging script itself intentionally performs public HTTPS GETs. Outputs are
exclusive-create receipts:use new output files for independent reruns.
Initial `stage-fonts.py` mistakenly selected optional Geist/Mono italics too:exit1
`ValueError: Expected exactly4TTFs,observed6`. Its alias overwrites and saved final
bytes/script/error are retained in attempt01-assets/attempt01-failure.json;not a
font/build failure. New02explicitly selects four,refetches pinned fonts,retains
matching licenses/METADATA and validates API blob SHA for every byte. No source UI
was touched by either attempt. Initial raw diagnostic cmap gaps are not rewritten.

[Authoring verification](../../../chrome-extension/build/cross-platform-design-consistency-20261102/font-stage/authoring-verification.json):
contract131lines at author receipt;root's documented launcher-asset exception brings
current contract to135lines(`wc` verified). Four relative board fonts,PostCSS parsed;34source baseline files,
website9/runtime74/tests46/heldZIP unchanged. Reading map gains canonical pointer.
D02 authoring/asset-check boxes can close;independent review/dispatch remain open.

Next W01 after review:copy these four TTFs+original notices into website assets;
map shared CSS semantic aliases/control treatments and threeHTML font heads only.
Do not change DOM/Recipe handlers/legal/CTA destinations or claim prior B05 geometry
still applies with new font metrics. M01 separately bundles font resources/theme
with native48dp/IME/EN-VI/security invariants. Extension self-hosting remains optional
E01 provenance/packaging scope;no extension/manifest/resource inventory change here.

Unresolved:actual font shaping/loading/fallback/axis rendering,
UI implementation and fresh source-bound browser/phone/owner visual acceptance.
C05 deployment hold and later feature/release gates unchanged.
