# W02 — website section composition

Date:2026-11-02. Candidate03 source complete;[tester7/7](website-composition-verification.md)/[independent review Passed](website-composition-independent-review.md). Fresh browser acceptance Pending.

Scope:only `landing-page/styles.css`,Privacy/Terms inline CSS. Existing visitor progression,all marketing/legal copy,DOM/heads/IDs,Recipe demonstrations,CTA anchors and handlers unchanged. No website server/browser,extension/native changes or deployment.

## Delivered

Existing hero emphasis now opaque warm-white italic Fraunces400,quiet terminal with Mono400–500 and Moss success;section transitions use canvas plus hairlines rather than gradients. Existing cards retain square opaque Precision surfaces without blur,glow text/shadows or hover lift. FAQ controls use Geist500;section descriptions have1.7 line-height. Ready/error/warning semantics remain distinct. Amber primary actions/focus retained;Recipe-builder choices and Copy receive44px minimum targets.

Pure connector/ripple/CTA glow decoration disabled;infinite badge/cursor/sync animations stopped. Finite feedback/AOS and content visibility rules retained. Desktop anatomy-marker hover transform remains the accepted B05 geometry exception;mobile shared tooltip anchor/wrapping and vertical AOS overrides untouched. Existing illustration/decorative dot structures retained,terminal dots reduced to6px. No content hidden or new page overflow clipping.

Legal title/h2 use actual inline Fraunces400;small h3 use Geist600,permission tokens Mono500. Removed now-redundant shared override of legal heading weights. Summary surfaces opaque,inline blur removed. Same-host font/hosting disclosures and separate extension Google Fonts disclosure preserved exactly.

## Author checks

Raw directory:`chrome-extension/build/cross-platform-design-consistency-20261102/website-composition/`.

- Root pre-edit `before.json`/17 frozen files retained;`verify-composition.mjs` and immutable `after.json` bind current files. Exactly3 changed,14 other website files byte-identical.
- Node24.18.0/PostCSS8.5.6 parsed shared CSS and both inline styles:594 rule occurrences,total warnings0,exact parse/serialize roundtrip. `git diff --check`:exit0.
- Legal documents outside `<style>` blocks byte-identical;index and JS exact.68 local href/src references resolve,including fragments;4 CSS font URLs local and present.4TTFs/3OFLs match reviewed provenance;provenance itself unchanged.
- All20 B05 selector names matched by selector-list membership:30 actual rule occurrences,including the combined mobile AOS rule,all at-rule contexts and10 protected geometry properties identical. Earlier29 count was incomplete;no runtime inference from this count.
- Current Android113 source files match M04 run01;extension74 runtime files/tests46,held ZIP and workflow unchanged. ZIP SHA256 `278b59df8ddef0085c9c064da11485695074419894c3f43b5077a10fc44f1e9e`.

Command:`node chrome-extension/build/cross-platform-design-consistency-20261102/website-composition/verify-composition.mjs` (absolute local Node24.18.0 used). Fresh verifier;old W01 verifier not executed. Apply helper initially had incorrect relative PostCSS import;failed before source writes,corrected to installed extension dependency;successful source checks above.

Exact bounded patch:`composition.patch`,SHA256 `7b255a07884017f321f48f337ffda97ca2f5698198a0c72aeaf207df35e96b6d`.

|Historical candidate01 file|SHA256|
|---|---|
|styles.css|`7dc9505ee17d695b5e5e3d2c0ba162c9e36c6ce7c285b799db8aed2dca3bda45`|
|privacy.html|`425d87025d869749341ca9c4a3459adc9f0efd8d469f47bcdf7c5eb7dca16849`|
|terms.html|`54b6716930364611ad374e2c80fd08b9c8d8ebb811128ee0962d78de5a475ca6`|

## Limits and next gates

Source-only evidence;no rendered layout/font shaping/focus/touch/contrast runtime acceptance. New font metrics and section styling require fresh320/390/768/1440 widths,zoom/keyboard,initial/live reduced-motion,all anatomy tooltips and settled Dish/Copy visibility. Earlier B05 passes remain dated historical evidence,not acceptance of current source. Retain≥.99 opacity threshold with bounded finite-transition wait. W03 icon/wordmark/favicon/OG/theme-color metadata remain next;artwork assets and HTML untouched here. No new product capability,Later feature or legal/publication approval.

Unresolved:fresh desktop slot and browser QA;W03 brand work;broader website acceptance/deployment gates.

## Candidate02 — root minimum-target finding

Root review01 source checks passed,then found `.demo-btn` padding/text could yield≈38–40px without a shared button minimum. Added only `min-width:44px;min-height:44px` to existing base `button` rule;no font shrink,clipping,content/markup or legal-line-height changes. Existing nav-toggle explicit44px and Copy/choice minima remain. All narrower `min-width:0` rules belong to protected content selectors,not buttons;no button minimum override below44px exists in shared CSS. Actual rendered targets still need browser QA.

Reviewed candidate01 three-file bytes retained at `before-target-fix02/` with `before-target-fix02.json`. Initial `after.json`,`composition.patch`,verifier/review01 remain immutable. Candidate02 current CSS SHA256 `01ff76ff1c5750e1027c9071da190236c263546a9e5763e00009d7c4ab711157`;legal hashes above unchanged. Fresh `verify-composition02.mjs`→`after02.json` Passed:594rules/zero warnings/exact roundtrip,30geometry occurrences unchanged,68local references,4font URLs;14other webfiles/113Android/74extension/46tests/ZIP/workflow exact. `git diff --check`:exit0.

Full candidate02 patch:`composition02.patch`;two-declaration delta:`button-target02.patch`;exact identities/allowlist proof in `target-fix02.json`. Historical candidate02 verification/review subsequently Passed;superseded by03,no runtime acceptance.

## Candidate03 — standalone social icon targets

Independent tester found footer `.social-link` anchors still explicitly40×40px and outside the base button minimum. Changed only those two declarations to44px;candidate02 CSS/Privacy/Terms bytes preserved at `before-target-fix03/` plus JSON. Legal links unchanged. Old candidate01/02 raws/verifiers/patches not rewritten.

Fresh `verify-composition03.mjs`→`after03.json` Passed:594parsed rules/zero warnings/exact roundtrip,30protected geometry occurrences/68local refs/4font URLs;14other webfiles,Android113/extension74/tests46/held ZIP/workflow exact. `git diff --check`:exit0. Current CSS SHA256 `2ad0a1417b4573b8b384401717b89163b9dc2f01d156c2d7c119b9b09445f07e`;legal hashes unchanged. `composition03.patch` SHA256 `1a81ab2b529ce536b1b9c91bf51fcad41f9fa48e8fc3a901cad2e7673bffe37f`;isolated delta `social-target03.patch`,identity in `target-fix03.json`.

Targeted actual-HTML audit:46 rule occurrences contain fixed pixel dimensions below44;conservative terminal-selector matching finds no interactive subjects among them. Remaining matches are inner SVGs,decorative dots/dividers/labels,illustration components and screen-reader utility;their small artwork sizes do not set the enclosing control target. Raw `fixed-under44-source03.json`,`fixed-control-audit03.json`;command:`uv run --no-project python .../website-composition/audit-fixed-controls03.py`. This source audit excludes pseudo decoration and does not compute hit areas,ancestor/layout constraints or relative units. No universal44px/runtime claim;nav/footer wordmark treatment remains W03 and all targets need fresh browser QA. Candidate03 tester7/7 and independent source review Passed;actual target/runtime acceptance Pending.

Final bookkeeping2026-11-02:independent candidate03 source/dispatch review Passed;
W02 source Done,W03 Ready. Older01/02 evidence retained;browser/native/owner and
publication acceptance remain Pending,deployment owner-held.
