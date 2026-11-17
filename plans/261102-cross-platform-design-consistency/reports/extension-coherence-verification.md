# E01 — independent extension coherence/packaging feasibility verification

Date:2026-11-02, Asia/Saigon. **Read-only audit verification Passed.**
Parent:[author audit](extension-coherence-audit.md). No font migration implemented;
root review/plan update precedes E02. No browser/artifact-release acceptance.

## Actual protected source and current contract

Fresh readonly verifier matched extension74/canonicaltests46/website19/Android113,
11additional scripts/disclosure/spec files,workflow and heldZIP before/after audit.
Current validateSource returned74inventory/1externalCSS record;validateArchive read
heldZIP against those exact74source bytes and returned74. This called no package
writer or test suite. HeldZIP remains
`278b59df8ddef0085c9c064da11485695074419894c3f43b5077a10fc44f1e9e`.

Popup/Options HTML directly loadtokens.css. Its unconditional Google Fonts import
is actually allowed by the current package contract;it is not merely a runtime
“development fallback”. No actual network request was observed. Demo instead
explicitly declares offlineGeorgia/system-ui/ui-monospace and only linksdemo.css.
That is intentional current behavior,not failed named-font loading.

Reviewed local sources provide4TTFs+3OFLs with exact pinned bytes. Deliberate E02
alignment can add8font/license/filtered-provenance files plus font-face-only
styles/fonts.css:9unique safe additions,**83=74+9**,zero external styling records.
Sharedfont CSS can be locally imported from tokens andDemo without importing the
sharedglobal reset intoDemo;only3Demo family aliases then change. Resource checker
already resolves local CSSurl/import targets generically. Current mandatory allowlist
rejects these unapproved additions until reviewed contract implementation.

Current provenance validator pins Argon2/QR/WASM/notices only;it does not yet pin
font/licence/metadata bytes. Merely adding83paths is insufficient. E02 needs immutable
reviewed TTF/OFL/provenance identities and meaningful validation,not trust solely in
self-declared checksums. Filter provenance scope for extension delivery;the available
website provenance describes website resources. No new downloads/dependencies needed.

## Golden boundaries and output hazard

All46canonical tests remain protected unchanged. Explicit marker scan found12approved
markers in generate_password.spec.mjs. The whole scripts/packaging-test.mjs file has
**no Golden/approved/status marker**. Its positive assertions at88–90hardcode74files,
1external record/fromtokens;ZIPpositive95hardcodes74. After the83/0contract is
reviewed,those unmarked positives legitimately need migration to the new intended
contract. This does not authorize editing marked tests,weakening negative guards,
using tautological expectations or retaining a dummy Google import to satisfy1.

Default packageExtension() writes the existing versioned development stem under
chrome-extension/build and renames over regular outputs;CLI has no output-dir option.
E02 must exclusively create a fresh unique output directory and use
packageExtension(outputRoot),preserving heldZIP/inventory/SHA sidecars. Old74artifact
checks remain bound to old74source;failure against future83source would be a boundary
mismatch,not proof oldZIPcorruption. New83ZIP is a new unaccepted development candidate.

## Bounded E02 verification plan

1. Assert independently reviewed exact83unique sorted paths/0external records,all3font-consumer edges and4face family/style/variable-weight mappings. Validate actual local TTF/OFL/provenance bytes and filtered source metadata;no manifest/CSP/permission expansion by default.
2. Add missing/tampered font,OFL,malformed/inconsistent/changed provenance negatives. Updating a mutant's own metadata must not bypass pinned identities. Keep allcrypto/WASM/QR/source-private/path/symlink/ZIP guards.
3. Retain the existing unapproved-remote-CSS negative;also reject reintroduced oldGoogleURL after allowlist removal. Validate newfont-sheet/Demo local graph;no dummy unused remote import. Positive83/0 and ZIP83 assertions must reflect reviewed requirements,not derive their expectation from the same implementation value.
4. Test font-file/directory symlinks and copier preflight before any writes. If packaging-provenance gains a helper import,update only the unmarked CLI fixture's copied dependency list as required;do not let missing-helper errors substitute for its intended missing-WASM preflight case.
5. Verify exact root83ZIP bytes/notices,duplicate/private/wrapper/truncated/altered entries and deterministic archive bytes. Run affected pnpmvalidate,test:package,test:unit;all46canonical tests unchanged. No headed pnpmtest without an authorized slot.
6. Package only into the unique owned output path;freeze/extract/validate newcandidate and recheck held74bytes/sidecars. Update current-source font disclosures in README,Privacy,permission matrix/notices together,retaining service/Sync/network and historical acceptance boundaries.
7. Fresh owned browser acceptance later covers popup/Options/Demo fonts,400/500axes,offline loading,EN/VI/syntheticNFDdynamic labels,layout/focus and unchanged Recipe/Dish behavior. Source/resource checks cannot certify rendering or release.

## Independently corroborated role findings

Actual dynamic card names come from options-profiles-tab.js69/112;.modal-title-em
receives profileName in profile-editor.js24 and options-export-wizard.js14.
Those display selectors currently use/inherit Fraunces;D02's known NFD mark gaps
therefore apply to real freeform subjects. Profile selects already usebody/Geist.
Future E03 must preserve original Unicode and target actual dynamic selectors.

Independent opaque-pair calculations corroborate Ink500 normal-text ratios
3.3356/3.1474/2.9327 oncanvas/surface/raised and Ink300 boundaries
1.4516/1.3697/1.2763. Ink600 text gives5.2496/4.9535/4.6155. Actual source has enabled
helpers/placeholders/fields using the weaker roles;disabled/decorative exceptions
and already-Ink600.field-note must remain. These are cited opaque-pair constraints,
not rendered contrast/WCAG certification. Keep current compact desktop26/30geometry
in this font/role work;root defines subsequent specific shape/QA scope,no blanket44.

## Actual command and evidence

```bash
/home/vantt/.nvm/versions/node/v24.18.0/bin/node --check chrome-extension/build/cross-platform-design-consistency-20261102/extension-coherence-audit/verify-tester.mjs
/home/vantt/.nvm/versions/node/v24.18.0/bin/node chrome-extension/build/cross-platform-design-consistency-20261102/extension-coherence-audit/verify-tester.mjs
```

Both exit0. [Actual audit receipt](../../../chrome-extension/build/cross-platform-design-consistency-20261102/extension-coherence-audit/verification-tester.json)
retains allcurrent/proposed counts,marker boundaries,positive assertion lines,
licensed asset identities,contrast arithmetic and preservation. Source validator
performs JSsyntax-only checks;no build/test/package writer/GUI/server/network/Gradle
or candidate mutation. No product/test/contract edits by tester.

## Unresolved

E02implementation/affected tests/new unique83candidate,
source disclosure alignment and fresh browser/visual acceptance. Existing release/
Sync/provider/publication gates and UI01 deferral remain.

Dated closure2026-11-02:independent source review/plan bookkeeping Passed;E01 Done,
E02 Ready. All implementation/runtime limits above remain.
