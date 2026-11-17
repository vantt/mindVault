# Phase04 — extension coherence without journey regression

Context:[contract](phase-01-contract.md),[audit](reports/design-baseline-audit.md),
[UI01 owner boundary](../261022-project-work-queue/reports/c01-owner-blocker-decisions.md),
[extension verification](../261022-project-work-queue/reports/b07-zip-suite.md).
Priority:4. Status:E01–E04 source/package/QA preparation Done and independently reviewed. Runtime family comparison Pending.

## Requirements and architecture

Current Precision extension is baseline. E01 is a specific coherence delta audit,
not a mandatory redesign. Existing behavior,400px popup/760px options,locale/session,
profile/Builder/verification/export/import interactions remain accepted constraints.
Exact CSS exceptions(profile identity colors/full-tab frame/compact desktop targets)
documented. Desktop26px icons/30px toggles follow the accepted Precision baseline;
website standalone44px/native48dp targets do not impose phone sizing on the popup.

## Related files and tasks

E01 read:`chrome-extension/src/styles/tokens.css`,popup/options/demo CSS/HTML,
manifest/package contract and current artifact/source provenance. Compare changed
web/native brand roles/fonts/icon treatments. Modify only demonstrated coherence
gaps after source review;otherwise record NoChange with evidence. No deletions.
Font packaging changes,if actually needed,must update required-asset inventory and
licensing/resource verification as a separate scoped change,not an unnoticed import.

- [x] E01 actual source/role/resource audit:[findings](reports/extension-coherence-audit.md),[verification](reports/extension-coherence-verification.md),[independent review](reports/extension-coherence-independent-review.md). F1–F5 require bounded work;Demo's intentional system fallback needs deliberate alignment.
- [x] E02 local fonts/package/disclosures:[delivery](reports/extension-font-package.md)/[verification](reports/extension-font-package-verification.md)/[independent review Passed](reports/extension-font-package-independent-review.md).151unit/67package/83file new ZIP+extract/reproducibility checked;heldZIP unchanged. Browser/release acceptance Pending.
- [x] E03 specific roles/shapes:[delivery](reports/extension-role-alignment.md)/[verification](reports/extension-role-alignment-verification.md)/[independent final02 review Passed](reports/extension-role-alignment-independent-review.md).151unit/67package/new83ZIP c11e…ae9 locally verified;74radius-declaration census/known gaps resolved,actual browser acceptance Pending.
- [x] E04 source-bound QA preparation:[packet](reports/extension-ui-qa-preparation.md),[verification32/32](reports/ui-qa-preparation-verification.md),[independent review Passed](reports/extension-ui-qa-preparation-independent-review.md). Revision04 binds397inputs/17helpers;runtime UNEXECUTED. Phase05 next.

## Reviewed bounded sequence

E02 copies four reviewed TTFs,threeOFLs,filtered provenance under `src/assets/fonts/`
and adds `src/styles/fonts.css`;tokens imports that font-only sheet. Demo mirrors
the four local face declarations inside its existing CSS to preserve the unchanged
self-contained Demo test(no style imports/no extra HTML assets). Both use the same
four pinned binaries;change Demo's three family aliases,not its palette/reset. Inventory
**83=74+9**,external stylesheet records**0**. No manifest permissions/WAR additions
for extension-owned pages;prove local resource graph. Retain four-face axes/ranges,
swap behavior,original font/license bytes and actual NFD limits.

Update `scripts/package-contract.json` sorted inventory,pinned asset hashes and
`packaging-provenance.mjs` font/license/provenance enforcement. Migrate unmarked
`packaging-test.mjs` positive74/1/ZIP74 assertions to reviewed83/0/83 and add meaningful
missing/tampered font/notice/provenance negatives. Preserve all existing remote,
symlink/path/private/crypto/WASM/ZIP negatives and all46canonical test files.
Update current-font Privacy/data-matrix/README/vendor notices in the same slice;
no blanket no-network/no-data claim,old74artifact evidence stays historical.

Root/tester run syntax/PostCSS/local graph/font-OFL-byte checks,`pnpm validate`,
`pnpm test:unit`,`pnpm test:package`. Root packages through `packageExtension(newRoot)`
in a new unique directory,extracts/validates83files and reproducibility. **Never run
default package output against the held2.3.1 ZIP/sidecars.** New candidate is not
browser/release accepted. Each slice receives independent review and plan update.

E03 changes only proven popup/options/Demo CSS roles:F3 dynamic names to Geist;
F4 essential helpers/placeholders to readable roles;F5 enabled outlines by actual
surface. Keep Ink500 disabled/decorative uses,identity palette and structural lines.
Correct named profile-card/modal surfaces to square and badge chips3px;keep intended
toggle pills/circles. No global color/radius replacement or JS/label/handler changes.

E04 prepares actual family/resource/font/offline/NFD,compact-control keyboard/target,
400px wrapping/scroll and affected journey cases for Phase05. No blanket44px popup
enlargement. Fresh desktop and phone readiness required;source checks are not visual
acceptance. No new profiles,QR,Autofill,Drive or Display-default restoration.

## Success criteria and verification

No contradictory brand/action/status mapping across final actual implementations.
No resurrected hidden Display defaults or new global inheritance/reveal promises.
No regression in accepted popup/options journeys. Source-only NoChange requires
hash/source comparison,not test replay. Changed extension code uses pnpm validate,
affected unit/package checks and relevant syntax;browser/resource checks only if
affected and with fresh reviewed candidate/slot. Full pnpm test includes headed
E2E and cannot be used as an autonomous desktop-free command.

## Risks and security

Changing extension runtime bytes breaks held-artifact identity:keep oldZIP/report
immutable and explicitly mark new candidate unaccepted until relevant checks pass.
No permission/crypto/storage/secret-handling changes for visual uniformity.
C01/UI01 deferral,C03 real Sync later/mandatory before publication and release gates
remain;design consistency does not grant Store/publication approval.

Next:Phase05 cross-platform comparison and bounded acceptance.
Unresolved:actual source-bound browser/native/font/target/owner acceptance and fresh
scoped resources. E02/E03 candidate and E04 preparation are complete;deployment held.
