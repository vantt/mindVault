# E03 — extension readable roles and surface shapes

Date:2026-11-02. Current source:final02;[tester verification](extension-role-alignment-verification.md)/[independent review Passed](extension-role-alignment-independent-review.md).151unit/67package/new83file artifact verified locally. Initial01 retained;actual browser/visual acceptance Pending.

## Candidate01 — initial delivered scope

Only `popup/popup.css`,`options/options.css`,`demo/demo.css` changed.50existing declarations changed,one narrowly targeted dynamic-type rule andone focus-selector expansion added (52edit operations). No HTML/JS/labels/handlers/state/font-face/dependency/permission/package/disclosure changes. Existing compact desktop26px icons/30px toggles and400px/760px shell remain per reviewed platform exception.

Dynamic `.profile-name` now Geist;only `#edit-secrets-title .modal-title-em` and `#export-modal-title .modal-title-em` override the inherited display/italic role to Geist/normal. These are the stored names populated by `options-profiles-tab.js:69,112`,`profile-editor.js:24`,`options-export-wizard.js:14`. Static create/import titles retain Fraunces;user Unicode untouched.

Essential caption/setup/unlock/Builder hints and placeholders,Recipe/preview/Menu-picker subtitles,inactive Options tabs/wizard/quota labels,and Demo read-time/Recipe construction/source labels now Ink600. Named editable-field/Options-checkbox boundaries use Ink500 on actual canvas/surface. Error/selected/disabled rules remain unchanged. Profile-card/modal surfaces now square;profile title/default badges use3px chip radius. Existing toggle pills/circles,profile palette,full-tab12px frame,structural hairlines and illustration borders remain.

## Source role/state mapping

Raw full selector/declaration/actual HTML examples:`after.json` and `author-edit-log.json`;dynamic fields require the cited JS-generated nodes. Tables below describe source cascade,not computed browser values.

|Subject group|Assigned source surface / role|State preservation|
|---|---|---|
|Popup captions/setup note/unlock hint/password placeholder/Builder labels|Canvas050,Ink600;quick-start transparent surface overlays remain dark|`.caption.accent` remains Amber;disabled primary retains Ink500 on Ink200.|
|Popup preview labels/muted code/Menu-picker subtitle|Raised150,Ink600|Accent label remains Amber;preview error remains Coral;no output/opacity/geometry changes.|
|Options inactive tabs/eyebrows/footer/wizard/quota/placeholder labels|Canvas050 or surface100;segmented active label on raised150;Ink600|Inactive hover Ink800,active tab Ink900+Amber underline;active segmented backgrounds unchanged. Decorative footer-dot and bundle separator retain low Ink400/500.|
|Demo nav read-time/hero caption/Recipe-part labels/dim sources/sheet caption/construction labels and note|Canvas050/surface100,Ink600|Decorative plus remains Ink500;noninteractive Recipe illustration borders unchanged. Four local font faces unchanged;no stylesheet import/http.|
|Popup unlock/hash/MenuID/profile select fields|Transparent canvas050;Ink500 boundary|Existing Amber focus and Coral invalid overrides retained;no dimensions/spacing/native-type change.|
|Options inputs/wrappers/profile select/checkboxes|Surface100 or explicit canvas050;Ink500|Checked/selected/error/disabled conditions unchanged;existing focus Amber retained.|
|Options import bundle/form-group fields|Explicit canvas050;Ink500|Mono/input behavior and focus/error declarations unchanged.|
|Dynamic profile labels / edit-export title fragments|Geist400/body role|No stored-value,locale,escaping,trim/normalization or renderer changes.|

Options secret-wrapper focus specificity was a concrete source issue:later `.secret-item .input-wrapper` default border could override the earlier generic `:focus-within` rule of equal specificity. Added `.secret-item .input-wrapper:focus-within` to that existing Amber focus rule;its higher specificity now wins while all focus declarations remain exact. No new focus timing or handler.

An existing unused `.input-slab input[type="text"]::placeholder` role receives the same readable Ink600;no current static subject is claimed for it. Dynamic wizard/Menu/profile nodes are identified by source JS,not invented live states. Compact labeled toggle/modifier/button hairlines remain accepted layout/shape context;this source check does not certify every non-text control boundary or hit area.

## Static verification

Ignored raw:`chrome-extension/build/cross-platform-design-consistency-20261102/extension-role-alignment/`.

- Root immutable `before.json`/289paths retained;`verify-role-alignment.mjs`→`after.json`:exactly3changed/286protected. Android113/web19/canonical46tests/allother80extension runtime files,packaging scripts/assets,workflow/dependencies/disclosures unchanged. Both old74ZIP and reviewed E02 ZIP+sidecars remain byte-identical.
- AST comparison preserves all layout dimensions,spacing,line-height/font-size/grid/overflow/transforms and non-role declarations. Border widths/styles unchanged;only named role colors/radii/family change. All font-face blocks byte-identical. Runtime inventory83/externalStyles0/font/OFL/provenance pins unchanged.
- PostCSS8.5.6 three CSS sheets parse/serialize exactly,warnings0. Actual rule occurrences518 (popup167/Options226/Demo125),recorded in `css-parse-counts.json`. Initial `after.json` per-file `css.rules` field counts root AST nodes;the supplemental receipt supplies the actual rule counts without rewriting earlier raw.
- `verify-states.mjs`→`state-preservation.json`:64state rule declaration sets unchanged (popup26/Options34/Demo4);only the deliberate secret-wrapper focus selector expansion changes applicability. No opacity,visibility or disabled/enabled condition changes.
- `contrast-receipt.json`:exact opaque Ink600 text ratios5.2496/4.9535/4.6155 on canvas/surface/raised;all≥4.5. Ink500 field boundaries3.3356/3.1474 on canvas/surface;≥3 andnot assigned to raised. Existing Amber focus ratios8.9501/8.4452/7.8690. These source-pair checks do not certify vignettes/transparency/anti-aliasing/rendered focus or WCAG generally.
- `git diff --check`:exit0. Author ran no test/package/browser/server/Gradle/ADB command. Root/tester own regression/new unique artifact;old candidates immutable.

Text diff:`role-alignment.patch`,SHA256 `991efad4cc8b3552f6c08a72ac04496970777136b89cf147e686000d35bd00f2`.

|Candidate01 CSS|SHA256|
|---|---|
|popup/popup.css|`94ca5be29de6f3f17b05096a2f72483952896e3a56364f5011d42c84b6c37419`|
|options/options.css|`526cd3935768aafa8b94d874df706ec9e82ee3135a31cd104891e271707e7f28`|
|demo/demo.css|`b3d87b02b716486905bf922f73e6132855fc0b1c6c72851c01cf5a902828c8dc`|

## Remaining gates

New source fonts/colors invalidate affected screenshot/font/cascade evidence. E04 must prepare actual resource/offline/EN-VI-NFD/profile/Builder/Options/Demo cases,compact control focus/keyboard,400px wrapping/scroll and source-bound selected states before fresh desktop/phone slots. Existing E02 font resources/83file artifact checks remain dated to that candidate;E03 needs its own unique artifact and affected test/review evidence. No new feature,Store/deployment/legal approval.

Historical candidate01 handoff:root regressions/artifact/review were pending;final02 actual closure below supersedes this source boundary. Current unresolved:E04preparation and fresh integrated runtime/font/visual acceptance.

## Candidate02 — complete major-surface census and radius repair

Reviewer caught four real Demo major surfaces still6px:.idea-box/.modifier-table-box/.sheet-preview/.construction-box. Root authorized their square correction,then an actual three-CSS radius census before closure. Census additionally found Options `.card` (setup/unlock/Vault cards) and Demo `.anatomy-box`/`.daily-flow-box`/`.tip-box`/`.recipe-example` as real6px surfaces,plus `.demo-toc-chip`999px navigation chips. Root authorized these six corrections in the same bounded fix.

Final02 changes only ten approved radius declarations vs candidate01:nine surface classes6px→0,Demo TOC chips999px→3px. No other color/font/state/geometry/HTML/JS change. Combined E03 delta from original before snapshot:60existing declarations,one dynamic-role rule andone focus-selector expansion (62operations). This is the current source;initial delivery/hash table above is candidate01 history.

Actual remaining radius exceptions are fully classified in `radius-census02-final.json`:popup full-tab body12px;status/profile/wordmark/circular help dots50%;intended language segmented-toggle enclosure6px;attached input corners4/0 and editorial note corners0/4. Controls retain4px/chips3px;compact26/30px targets and intentional toggle/circle treatment unchanged. No unclassified remaining explicit radius outside shared roles;this is source classification,not a universal shape rewrite or runtime acceptance.

Root's candidate01 validate/unit151/package67 and development ZIP SHA256 `9519d2e05757ac826a5e8b6453a2befce10857a1d722dd526a7d2f3942140301` remain immutable historical evidence,not final02 acceptance. `verify-radius02.mjs` verifies that ZIP and all83 extracted original files still match its captured inventory. Candidate01 threeCSS copies/hashes retained at `before-radius-fix02/`;the initial four-fix StepA source retained at `step-a-radius-fix02/`. No earlier source/test/failure/review/raw artifact overwritten.

Final combined receipts:`after02-final.json`,`state-preservation02-final.json`,`contrast-state-preservation02.json`,`radius-census02-final.json`. Preliminary `after02.json`/`state-preservation02.json` describe StepA and remain unchanged. Contrast/cascade/dimensions/font faces identical to candidate01;only ten radius declarations differ,so the original eight opaque contrast pairs remain exact. Full before289paths:threeCSS changed/286protected;all46tests,both earlierZIPs/E02sidecars,83inventory/local-font pins remain intact. PostCSS518rules/zero warnings/exact roundtrip andgit diff --check exit0;no builder test/package/runtime operation.

|Final02 CSS|SHA256|
|---|---|
|popup/popup.css|`94ca5be29de6f3f17b05096a2f72483952896e3a56364f5011d42c84b6c37419`|
|options/options.css|`28bae81b462629172212d370db3f98c99da3e3cc82ea90fc8419147f3e19c122`|
|demo/demo.css|`5a170114ae6cf4ce00bcc1dc512fe6cbd2a0acff11f8232c804c8e4b150af509`|

Full bounded patch:`role-alignment02.patch`,SHA256 `8792d8213143769f26371ab3cef8599c3875e9a7e55d21a20b0b6d935f543da2`. Source frozen before root final unit/package/new artifact02. At that final02 source handoff independent review/plan update were pending;actual verified closure below supersedes it. E04preparation/runtime remain next.

## Final02 — actual verification and closure

Direct validate02exit0confirms83files/externalStyles[];unit02exit0Passed151/151;
package02exit0Passed67/67with292source identities stable.
[Tester actual evidence](extension-role-alignment-verification.md) and
[independent final02review](extension-role-alignment-independent-review.md) Passed.
Raw after02-final.changedDeclarations52 retains the original operation counter;
supplemental/tester/independent AST establish60changed existing declarations plus
one dynamic block/one focus-selector expansion=62operations. Raw not rewritten.

New artifact-02ZIP83files/1832653bytes SHA256
`c11e3a6a1812800f45b4770af7e82b648709a28989ec937d45c1e90c27691ae9`.
Root reproduced/extracted/validated it;tester/reviewer read CRCs/source/extracted
bytes/sidecars independently. Older278b/3ed8/9519ZIPs remain immutable. Final
source/test/artifact and plan bookkeeping Passed;E03Done,E04QA preparation Ready.
This source closure is not actual browser/font/owner/release acceptance.
Deployment remains held;future changed source needs separate candidate evidence.
