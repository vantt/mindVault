# Phase01 — shared design contract

Context:[overview](plan.md),[actual source audit](reports/design-baseline-audit.md),
[design references](../../docs/designs/README.md),[reading map](../../docs/specs/reading-map.md).
Priority:1. Status:D01 Done/source review Passed;D02 authored/staged,independent review Pending.

## Requirements and insights

Runtime Precision is baseline;old UI-spec tokens are superseded. Website needs a
brand-family change;Android needs real hierarchy/components as well as palette.
Consistency adapts density/navigation to platform. No deployment/feature expansion.

## Architecture and related files

Contract:[canonical design mapping](../../docs/specs/cross-platform-design.md);
reading-map pointer added. [Actual D02 authoring/font checks](reports/design-contract-fonts.md).
Read current extension tokens/popup/options,landing styles/threeHTML,Android
MainCookbookScreen/VaultScreen/CookbookScreen/SourceScreen/MappingScreen.
No product source modifications or deletions in D01/D02.

## Tasks and implementation steps

D01 — source baseline:inventory actual tokens/screen structure/accepted behavioral
constraints;freeze file identities;separate source findings from visual hypotheses.
D02 — contract:freeze semantic colors/type/spacing/shapes/controls/states and
platform adaptations;stage official licensed Fraunces/Geist/Geist Mono assets for
web/native with source URL/SHA/OFL provenance and Vietnamese cmap/offline checks;
list exact W01/M01 edits and affected regression matrix. Independent review then
publish canonical local contract and update this plan before W01.

Font candidates:official Google Fonts Fraunces and Vercel/Google Fonts Geist
repositories;verify actual file/license/version before selecting binary assets.
Stage under ignored build evidence first,then reviewed web `assets/fonts/` and
Android `res/font/` delivery in their own slices. Never claim generic serif is
exact Fraunces. Extension self-hosting is optional separate E01 packaging scope:
retain held74-file source/ZIP until independently reviewed resource change.

- [x] D01 source inventory/34file snapshot prepared;no runtime acceptance.
- [x] D01 [independent source review Passed](reports/design-baseline-independent-review.md):34hashes/30links;plan updated.
- [x] D02 canonical contract/token mapping/font decision and unrendered representative board authored.
- [x] D02 four official unmodified TTFs/SHA/OFL/cmap/local checks;Fraunces NFD gaps explicitly retained with Geist fallback policy.
- [x] D02 [independent contract/staging review Passed](reports/design-contract-independent-review.md);source-bound checks retained,W01 Ready.

## Success criteria and verification

One unambiguous reference with colors/type roles/native sizes/shape/status semantics,
source links and explicit exceptions;each task has finite file/flow scope.
Static link/hash/read checks and independent review;synthetic boards are proposals,
not screenshots or new user/device acceptance. No decorative claims/testimonials.

## Risks and security

Font files unavailable or glyph mismatch:verify license/assets/fallback before
claiming exact matching;do not introduce network fonts in mobile or an external
fetch to unlock a screen. Preserve accepted extension UI01 deferral and all auth,
mask/lock/copy policies. Golden tests unchanged. Do not block the entire contract
on Cloudflare service choice;deployment is owner-deferred.

Next:W01 foundation only;M01 theme begins after the same reviewed mapping.
Unresolved:font provenance/fallback;contract review and board acceptance boundary.
