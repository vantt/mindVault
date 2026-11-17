# PassChef chef-keyhole brand assets

Date:2026-11-06. Status:Asset package complete;runtime migration separate. Scope:design asset package.

## Context

- Owner requests chef hat + keyhole identity, usable horizontally, square and as icons.
- [Precision reference](../../docs/designs/README.md):warm ink `#0e0e10`, amber `#e8a341`, Fraunces upright400.
- Owner's new design request supersedes legacy-icon reuse for these proposed assets.
- Deliverables:[brand package](../../docs/designs/passchef-brand-v2/).

## Implementation TODOs

- [x] Draw canonical chef-hat/keyhole vector with a clear silhouette and transparent keyhole.
- [x] Derive horizontal light/dark wordmarks with outlined Fraunces400 typography.
- [x] Derive square/stacked arrangements and monochrome marks from the same geometry.
- [x] Prepare app tile, Android adaptive foreground/background, and optical small icons.
- [x] Export transparent PNGs;include16/24/32px icon sizes and larger delivery sizes.
- [x] Create visual preview and concise usage guide:palette,clearspace,minimum sizes,variant selection.
- [x] Keep reproducible export source and required font attribution/provenance.
- [x] Parse all SVGs,verify PNG dimensions/alpha,and inspect actual small-size renders.
- [x] Independently review geometry,identity consistency,packaging and usage documentation.
- [x] Record actual verification and completed deliverables here.

## Boundaries

Existing extension,website and Android runtime assets/references remain untouched.
Existing in-flight changes and Golden tests remain untouched.
Design previews/export checks establish assets only;device/browser/platform mask acceptance remains later work.

## Success criteria

One recognizable chef/keyhole identity across rectangle,square and small icons.
Standalone SVGs require no installed font or network resource;PNGs match documented sizes.
Artwork and keyhole remain legible at16/24/32px;adaptive artwork fits documented safe zone.
Files,preview,usage guide and reproducible exports delivered together.

## Risks

Fine strokes/keyhole collapse at small sizes:use optical simplification and inspect native pixels.
Variable Fraunces defaults to900:instantiate explicit400 before outlining.
Light/dark backgrounds and platform masks:provide deliberate variants and avoid claiming unrun device checks.

Unresolved questions:None for asset-package scope.

## Verification — 2026-11-06

Build Passed. Independent visual/code review:no blocking findings;two minor board
label/comment findings corrected before final export. Final asset validator
Passed477checks:17SVG,54deliveryPNGs,5AndroidXML,7ICOframes,91manifest files.
Exact RGBA parity for small ICO frames/native XML foreground;all ZIP/local hashes
match. Adaptive visible radius30.967806dp<33dp. Native16/20/24/32/48keyholes open.

Precommit:bundled exact Fraunces/Geist sources,OFL notices and filtered provenance
to remove dependency on untracked website fonts. Independent review Passed.
Ruff E9/F lint Passed. Extracted ZIP rebuilt and revalidated independently of the
repository font directories;Passed. Artwork unchanged by source bundling.

[Verification receipt](../../docs/designs/passchef-brand-v2/verification.json).
[Download package](../../docs/designs/passchef-brand-v2/passchef-brand-v2.zip).
No app behavior/source migration;extension suites/device/store acceptance not run
for this asset-only delivery. No unresolved asset-package questions.

## Follow-up 2026-11-10 — dot-less wordmark (owner)
Owner: wordmark has **no dot** between Pass and Chef (D16 in [Android UX redesign](../261110-android-ux-flow-redesign/plan.md)). Current package wordmarks carry the amber signature dot → revise wordmark SVG/PNG variants, usage guide line, and re-run validator. Mark/icon geometry unchanged. Status: Pending; needed before Android redesign phase 06.
