# E01 — independent extension coherence audit review

Date:2026-11-02, Asia/Saigon. **Read-only source/audit,tester feasibility and final bookkeeping review Passed;
E01 Done,E02 Ready. No new product/runtime/package acceptance.**
Parent:[extension phase](../phase-04-extension.md),[author audit](extension-coherence-audit.md).
No product/config/test/resource changed by this review.

## Findings supported by actual source

Read actual tokens/popup/Options/Demo CSS/HTML,dynamic profile/wizard subjects,
package contract/resources/provenance/validator/fixtures/tests/output API,source
snapshots/role records and opaque-pair calculations. F1–F5 are concrete source gaps;
NoChange is not supported for the whole extension family.

F1:tokens actually imports Google Fonts;comment does not self-host it. Current
package contract explicitly permits this styling reference. Do not infer malicious
code,MV3 rejection or measured network behavior. F2:Demo intentionally chooses
Georgia/system fonts;this is a deliberate existing fallback,not proven load failure.
A shared font-face-only sheet aligns delivery without importing shared resets into
Demo. Existing manifest/pages support extension-owned local relative resources;
no permission/web-accessible-resource change is justified by this proposal.

F3:stored profile names reach `.profile-name` and dynamic modal-title fragments via
escaped markup/textContent. Fraunces NFD gaps apply;Geist is correct dynamic role.
Use specific edit/export title name fragments,retain static Fraunces400 titles and
original Unicode. No normalization/storage/handler changes.

F4/F5 actual essential caption/hint/placeholder/inactive-tab and enabled-field
selectors use Ink500/Ink300. Independently recomputed27opaque pairs/threshold flags:
Ink500 text3.336/3.147/2.933 fails4.5 on050/100/150;Ink6005.250/4.954/4.616 passes.
Ink300 boundaries1.452/1.370/1.276 fail3;Ink500 boundaries only suit050/100,Ink600
needed on150. Preserve Amber focus/error overrides,disabled/decorative/structural
roles. Actual vignette/alpha/cascade/font/anti-aliasing is not opaque-math acceptance.

Additional proven Demo subjects warrant E03 scope:demo.css12/17/32/103/105/108/112/
115 assign Ink500 to nav/read-time/Recipe component/sheet-caption/construction labels
and notes;actual demo.html contains them. Promote required text by selector,not
all Ink500. Decorative plus/separators and noninteractive illustration borders stay
separate. This closes the audit's optional-Demo ambiguity;no broad redesign needed.

## F6 interpretation and recommended grouping

Agree root's platform-specific judgment:docs/designs/README §5.1 explicitly defines
MIconButton26×26;actual design primitives and popup preserve it,with compact30px
choices in400×600desktop layout. Shared consistency is family/roles,not identical
phone touch geometry. Amend canonical contract to scope44px to marketing web touch
actions,48dp to native,retain documented compact extension desktop controls. This
is reviewed implementation/design interpretation,not owner waiver or WCAG pass.
Do not classify every26/30control as defective or add blanket44minimums to popup.
Actual keyboard/focus/targets/layout remain QA;new genuine geometry issues get their
own bounded fix after evidence.

Recommended sequence:

1. **E02 fonts/package/disclosures.** Four exactTTFs+3OFL+provenance+onefont sheet:
   sorted inventory83=74+9,external styling records0. tokens and Demo import only
   local font sheet;Demo family aliases change,reset/palette stay. Enforce exact
   font/license/provenance identity/notice source metadata with meaningful missing/
   tampered negatives;retain crypto/QR/resource/path/symlink/private/ZIP guards.
   Unmarked packaging positives74→83/1→0 legitimately follow reviewed contract;
   existing46tests unchanged. No fake Google import to satisfy an obsolete positive.
2. E02 disclosure edits ship with changed source:README/Privacy/matrix/vendor notes
   distinguish new bundled fonts from old held74candidate. Preserve service/Sync/
   hosting limits;no blanket no-network claim. Validate/unit/package checks and
   fresh extraction/resource identity required before source completion. Package
   only through `packageExtension(outputRoot)` into exclusively created unused
   candidate directory;default CLI overwrites fixed heldZIP/sidecars. Current oldZIP
   validates against its frozen old source,not automatically against new83source.
3. **E03 roles + named shape cleanup.** Dynamic-name fonts,required text/field
   contrast and proven Demo roles. Specific profile-card6→square/modal-box8→square/
   badge pill→3px align shared roles;retain functional switch pills/circles,profile
   palette/full-tab frame and compact desktop controls. No JS/state/security change.
4. **E04 verification preparation/actual targeted QA**,rather than blanket target
   enlargement. Record desktop compact-control contract/source inventory;prepare
   changed-font/NFD/offline/layout/focus and affected journey cases before fresh slot.
   Any demonstrated remaining geometry repair gets separate source review.

Revise author F6/E04 blanket44language before implementation/dispatch. Other evidence
and proposed83resource arithmetic are sound;future slices require their own checks.

## Preservation and limits

Independently rehashed74runtime/46tests/19website/113Android entries,11additional
package/disclosure/contract sources plus heldZIP/workflow unchanged. No Golden
marker in packaging-test.mjs;all existing negative cases/source constraints remain
required. No test/package/build/browser/network/Gradle/ADB or account action here.
[Independent receipt](../../../chrome-extension/build/cross-platform-design-consistency-20261102/extension-coherence-audit/independent-audit-review.json).
Source audit and packaging feasibility do not establish actual font loading,
accessibility/security/journey/native/owner acceptance. Existing failed/unknown/
release/Sync/UI01/deployment gates remain.

## Tester and final bookkeeping

Read actual tester receipt/report:readonly validateSource74/1external and heldZIP
validateArchive74Passed,83/0 remains unimplemented proposal. Protected74/46/19/113,
11support sources and heldZIP/workflow matched at tester closure. Canonical spec
then legitimately changed for reviewed platform clarification;future E02 source
changes are excluded from these historical audit identity assertions. No test/package
writer or new candidate was run by E01.

Root resolved F6/E04 wording:contract expressly separates marketing standalone44/
native48dp/compact desktop26/30;links authoritative26×26spec,retains keyboard/target
QA and states no owner waiver. Phase04implements recommended E02→E03→E04 grouping,
including actual Demo roles and specific shapes without blanket enlargement. Original
audit-initial.md matches old report hash;old proposal/evidence preserved.

Child/extension phase,parent master/track,index and roadmap/changelog agree E01
read-only Done/E02 Ready;new83candidate/runtime/owner acceptance remains Pending.
Actual11documents/399local file links resolve;original46IDs/46owner anchors retained,
master79lines,heldZIPunchanged.
[Final receipt](../../../chrome-extension/build/cross-platform-design-consistency-20261102/extension-coherence-audit/independent-final-bookkeeping.json).

## Unresolved

E02/E03implementation and E04actual resource/layout/keyboard/target/browser cases;
integrated/native/owner acceptance and publication remain Pending,owner-held.
