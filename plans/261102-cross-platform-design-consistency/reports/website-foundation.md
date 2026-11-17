# W01 — website Precision foundation

Date:2026-11-02, Asia/Saigon. **Implementation/source verification complete;
independent bounded source/evidence review Passed. No browser/rendering acceptance.**
Parent:[website phase](../phase-02-website.md),
[reviewed contract](../../../docs/specs/cross-platform-design.md),
[reviewed font staging](design-contract-fonts.md).
W01 only;W02 section composition/W03 artwork remain Pending. Deployment owner-held.

## Exact source scope and retained behavior

Before modification,froze styles/threeHTML/README/script bytes and hashes under
ignored `chrome-extension/build/cross-platform-design-consistency-20261102/web-foundation/before/`.
[Before inventory](../../../chrome-extension/build/cross-platform-design-consistency-20261102/web-foundation/before.json),
[W01-only source patch](../../../chrome-extension/build/cross-platform-design-consistency-20261102/web-foundation/foundation-source.patch),
[candidate identities](../../../chrome-extension/build/cross-platform-design-consistency-20261102/web-foundation/candidate.json).
Source patchSHA256`9097669e0d1d09cc80e07dcbb9f977876613844d22eafc8317fe2ffa08c31185`.

| Changed files | Actual delivery |
|---|---|
|`landing-page/styles.css`|4local variable font faces with swap/range100–900;warm ink/amber actions,neutral legacy secondary/accent aliases,moss success/coral error/honey warning;readable text aliases;4px controls/3px chips/square major surfaces/restrained shadows.|
|`index.html`,`privacy.html`,`terms.html`|Only fonts comment and3old Google Fonts/preconnect links removed per head. Bodies and inline legal styles unchanged byte-for-byte.|
|`README.md`|Actual foundation/font provenance/fallback/development/verification limits;uv local server documentation,no unsupported broad browser/performance acceptance.|
|`assets/fonts/` fourTTFs +threeOFL +filteredprovenanceJSON|Exact unmodified reviewed binaries/notices;provenance contains pinned public source paths/URLs/SHA/bytes,not local-machine paths.|

Static display/wordmark headings now explicit Fraunces400,not inherited old700.
Shared buttons/mobile nav and stats use body Geist;existing Recipe/Dish/terminal
Mono roles stay. Legal inline heading weights are overridden by specific shared
role selectors without editing legal markup/styles. Body text uses ink800,muted
aliases ink600;no unreadable ink500 helper assignment. Input fields use ink100+
ink500 boundary;raised preview fields use ink600. Global focus is visible amber,
including existing builder input focus. Buttons retain neutral ghost surfaces,
amber hover/pressed and≥44px shared target;mobile nav toggle becomes44px with
padding preserving its old24px bars. No moss primary buttons added.

Pure decorative `.bg-grid`/`.bg-glow`layers are disabled as a design choice. No
content layer is hidden to force widths;no new overflow-clipping rule. Existing
mobile hero/comparison/Step/Dish/Copy grid/min-width/wrapping,tooltip shared anchor,
vertical mobile AOS,ARIA/MQL handlers all retained. The old anatomy warning-tint
`rgba(var(--color-warning),...)`hex misuse now uses the numeric RGB alias.

All three exact HTML body hashes remain unchanged:DOM IDs,sections,text,CTA targets,
Recipe example attributes and event hooks. `script.js` byte-identical. No app,
extension,manifest,workflow,behavioral test or controller edits. No new framework,
package dependency,install,canonical color-assignment tests,GUI/server/native/
clipboard/account/push/deployment action in W01.

## Assets and source checks actually run

[Actual source verification](../../../chrome-extension/build/cross-platform-design-consistency-20261102/web-foundation/verification.json):
PostCSS8.5.6 parses/processes526root nodes,roundtrip identical,0warnings;4relative
font URLs resolve.63`rgba(var(...))`references resolve numeric RGB triples.
FourTTF/threeOFL byte sizes/SHA match reviewed provenance;all8new font-directory
files are local. Font provenance SHA256
`c1e8245148251556672770c520349d59e7d0a7021abdf828ab9f0d6207c2ef54`.

[HTML reference/NFC audit](../../../chrome-extension/build/cross-platform-design-consistency-20261102/web-foundation/reference-verification-02.json):
69HTMLlocal file/fragment references Passed,including OG;5external+7placeholders
NotExercised. Four CSS font references checked separately(73local occurrences).
82static heading data chunks remain NFC;no user data normalization. First narrower
68reference receipt/checker remains retained;02adds metadata-asset references.
This is source graph/Unicode validation,not HTTP/dynamic behavior/font rendering.

[Verification](../../../chrome-extension/build/cross-platform-design-consistency-20261102/web-foundation/verification.json)
also proves three bodies/JS unchanged,20selected B05 intrinsic-geometry rule property
sets unchanged,and extension74/tests46/heldZIP/21Android baseline files/workflow
unchanged. New CSS SHA256
`9da9efe1c9de5161632b8412f4ee110d49fd09e64cab47387bc03174c36251fc`;
JS SHA256`1974bb01dd921c9b323dd35e27ad159e6d340cbbc0139fad2c4c8d7aa431ed49`.
Existing B05 visual evidence remains historical:new font metrics/colors invalidate
its candidate acceptance and require affected retakes,even with geometry retained.

Actual commands from repo root(existing Node24/PostCSS,uv/systemPython,no install):

```bash
/home/vantt/.nvm/versions/node/v24.18.0/bin/node chrome-extension/build/cross-platform-design-consistency-20261102/web-foundation/apply-foundation.mjs
/home/vantt/.nvm/versions/node/v24.18.0/bin/node chrome-extension/build/cross-platform-design-consistency-20261102/web-foundation/verify-foundation.mjs
/home/vantt/.local/bin/uv run --offline --no-project --python /usr/bin/python3 python chrome-extension/build/cross-platform-design-consistency-20261102/web-foundation/audit-references-02.py
git diff --check
```

These completed exit0;verification outputs are exclusive-create receipts.
Initial ignored codemod import failed with ERR_MODULE_NOT_FOUND because a relative
PostCSS tooling path resolved outside the checkout;import failed before source
mutation. Preserved attempt01script/receipt;corrected tooling path rerun succeeded.
This is a tooling-path failure,not a product CSS/build failure. No source assertions
were weakened;no failed browser run substituted with source checks.

## Remaining visible alignment scope

W02 still owns section composition and bespoke examples/cards/hover/animation
semantics and inline legal styling. W03 owns existing-extension-artwork favicon/OG
alignment;current green artwork and three green theme-color metadata values were
left deliberately outside font-head edits. Legacy gradients/tinted section details
and status elements coupled to old primary aliases need their own section review.
Privacy's existing website Google Fonts disclosure needs immediate W02 correction
before website acceptance/publication;legal body wording was explicitly frozen for
this slice,so no silent legal copy edit. Other legal/claims/CTA/source gates remain.

Fraunces remains staticASCII/NFC400 only with documented NFD gaps;Geist/Mono are
not a promise to render arbitrary scripts without fallback. Exact font loading,
shaping/axis appearance,320/390/768/1440 layout,zoom/keyboard/focus,ARIA/reduced motion,
all tooltip hovers and settled visibility need a reviewed fresh browser slot.
No full website/accessibility/public-domain acceptance. Android M01 proceeds only
after independent W01 review/plan update;no phone theme delivered by this task.

Unresolved:final W01 bookkeeping;affected rendering/interaction retakes,W02/W03
alignment/legal disclosure followup;Android/native delivery and owner visual verdict.
Deployment remains deferred;service/project/publication approval not implied.

Open finding: [independent review](website-foundation-independent-review.md) P2—Privacy still describes external website Google Fonts after local delivery. Immediate W02 correction is required now,before website acceptance/publication;extension external-font disclosure remains accurate and unchanged.

Dated followup2026-11-02:P2 resolved by [reviewed W02 correction](website-privacy-alignment-independent-review.md);original W01 finding/receipts retained. Source completion is separate from pending runtime/visual acceptance.
