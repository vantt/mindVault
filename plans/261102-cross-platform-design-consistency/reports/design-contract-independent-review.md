# D02 — independent contract, font and contrast review

Date:2026-11-02, Asia/Saigon. **D02 contract/staging and final bookkeeping review Passed; D02 Done, W01 Ready.
Product delivery/runtime acceptance Pending.**
Parent:[phase](../phase-01-contract.md),[author evidence](design-contract-fonts.md),
[canonical contract](../../../docs/specs/cross-platform-design.md).
Owner deployment hold and later C05/C04/C11 gates remain unchanged.

## Provenance and actual font checks

Read contract/reading map,stage01 failure/stage02 source,API listings/commit,
provenance,all4TTFs/3OFL/3METADATA,metadata/role-policy/contrast scripts and receipts,
source-only board and source snapshots. Independently matched all10file lengths,
SHA256 and computed Git blobSHA against pinned API paths/commit
`6c1dbe27dcf7d817d4b62cec033ba77fa8e2a5ec`.
Aliases do not modify internal font bytes. Original copyright/OFL records retained,
including Fraunces2018license versus2020metadata discrepancy. Future delivered
copies still need original notices;this is provenance review,not legal certification.

Using existing FontTools environment,independently parsed actual4TTFs. All pass
ASCII95,Vietnamese NFC146 and bullet/ellipsis. Rebuilt NFC/NFD sets independently;
Geist/Mono cover all22NFD points,Fraunces upright lacks0309/0323 and italic lacks0309.
Fraunces wght default900 and other axes match;Geist/Mono default400. Mono has actual
terminalFULL BLOCK;Geist/Fraunces do not. No font binary alteration or instancing.

Declared role policy is coherent:Fraunces only verified static ASCII/NFC titles at
explicit400;dynamic/NFD labels use Geist;Recipe/Dish/terminal use Mono with platform
fallback for other scripts. No user Recipe/Spice/Dish normalization/filtering to
fit glyphs. Raw all-required coverage=false and documented gaps remain intact;
21role-policy checks are conditional coverage,not universal shaping/rendering proof.
Explicit bundled variation settings align with [Android guidance](https://developer.android.com/develop/ui/compose/text/fonts).
Native axis selection/loading/shaping must still be verified in M01/runtime;
raw VF alias `regular` is not proof of default400.

## Contrast and contract assessment

Independently recomputed37opaque sRGB pairs and every unrounded pass/fail;all34
assigned normal-text pairs meet4.5. Three Ink500 normal-text failures remain
restricted to disabled/decorative roles. All6nontext pair calculations match declared
3threshold;Ink500onRaised2.933 prohibited,Ink600onRaised4.616 allowed. Structural
hairlines are not sole control/focus affordances. Normal4.5/large3 and no rounding
of failures align with [W3C guidance](https://www.w3.org/WAI/WCAG22/Understanding/contrast-minimum.html).
This math does not certify rendered alpha states,actual controls/focus or WCAG.

No actionable contract blocker. Native reading/touch/scaling/IME/EN-VI constraints,
platform navigation and actual behavior/testTags remain;Golden tests/security/
clipboard/lock/FLAG_SECURE unchanged. Current Precision is root-adopted direction,
not owner approval of exact staged files or new visual acceptance. Profile colors,
full-tab frame and existing blue launcher mark are explicit exceptions. Reviewer
viewed actual extension128PNG;legacy blue lock/hand artwork matches the exception.
Reusing unchanged artwork in later slices does not add another invented logo.

Board has4actual local font references,no script/external URL and explicit proposal
labels;all referenced files exist. Not rendered;HTML sample panels are not native
screenshots or implemented behavior. New assets remain ignored staging only;
W01/M01 deliver separate reviewed copies. Source/metadata checks complete D02's
staging boundary;actual offline browser/native loading remains later evidence.

## Preservation and review limits

Independently rehashed34baseline sources,9website candidate files,extension74/
tests46/heldZIP unchanged. Initial stage01 count6failure and alias-overwrite history
remain;02explicit four-font selection/pinned blob verification does not erase it.
No GUI/browser/phone/Gradle/ADB/clipboard/product-source action or existing test replay.
Reviewer read actual public Android/W3C references;no font downloads repeated.
Independent raw receipts retained under font-stage:
`independent-font-review.json`, `independent-contract-contrast-review.json` and
`independent-d02-final-bookkeeping.json`.

Final bookkeeping:15documents/311local file links resolve;original46task IDs and
46owner anchors retained;master79lines. Phase01/child overview/canonical contract/
parent dispatch consistently record D02 source/staging Done and W01 Ready. All34
baseline sources,9website files,extension74/tests46/heldZIP and10staged files
rehash unchanged. Proposal board remains unrendered;deployment remains owner-held.

## Unresolved

Later delivered font loading/axis/shaping/mark placement,fallback metrics,rendered
contrast and source-bound web/native/owner visual acceptance. No source/evidence
blocker to W01;deployment stays owner-deferred.
