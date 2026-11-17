# E02 — bundled extension fonts and package contract

Date:2026-11-02. Candidate03 source/test/package and [independent review Passed](extension-font-package-independent-review.md);[tester verification](extension-font-package-verification.md). No browser/release acceptance.

## Initial delivery — candidate01 history

Nine existing files changed:extension tokens/Demo CSS/vendor README;package contract/provenance validator/unmarked packaging tests;root README,Privacy extension-font paragraph and permissions matrix. Nine runtime files added:four unchanged TTFs,three original OFLs,filtered provenance,and `styles/fonts.css`. **Exact83runtime files;external stylesheet records0.** No manifest/permission/WAR/security/JS journey/dependency changes;46canonical test files byte-identical.

Shared font-only stylesheet declares Fraunces upright/italic,Geist and Geist Mono,variable weight100–900 and swap. Tokens and standalone Demo import it with local paths. Demo's existing intentional Georgia/system fallback changes only through its three font aliases;palette/reset/layout untouched. Consumer role corrections remain E03;this slice does not claim dynamic profile headings already use Geist. Original font axes/cmap limits retained;no downloads/conversion/subsetting or Unicode normalization.

`package-contract.json` pins exact hashes for all eight font/license/provenance assets plus upstream Google Fonts commit `6c1dbe27dcf7d817d4b62cec033ba77fa8e2a5ec`. Provenance enforcement checks hashes,source metadata/count/byte sizes and required source/OFL notice;existing Argon2 dependency/lock/license/WASM and QR source/notice checks retained. Empty external styling allowlist makes the existing resource walker reject remote font CSS;local CSS imports/font URLs are validated.

Unmarked packaging positives legitimately migrate74/1/ZIP74→83/0/83. Added18negative cases:nine missing font resources (including shared CSS),eight tampered font/license/provenance assets,and removed vendor font-source notice. Positive local import/four-face graph assertions replace old Google CSS acceptance. Entire previous `mutations` array remains byte-identical;all pre-existing path/symlink/private/remote-code/CSS/crypto/notice/ZIP negatives retained. Protected46tests untouched.

Disclosure updates distinguish current local typography from other Google services/Chrome Sync/network metadata. Privacy uses product-facing bundled-font wording with final artifact verification limit;matrix/README retain earlier held74artifact external-font history. Vendor README retains original QR section and appends source/OFL/font notices. Root README uses an exclusively new output-directory API recipe;default `pnpm package` would overwrite the original versioned artifact and is explicitly excluded for this migration. No universal no-network/no-data/legal certification.

## Initial author source evidence — candidate01

Ignored raw:`chrome-extension/build/cross-platform-design-consistency-20261102/extension-font-package/`.

- Root `before.json` plus273 immutable before paths retained. `verify-font-package.mjs`→`after.json`:264original paths unchanged,nine existing paths changed,nine new runtime paths;all original source hashes bind exact reviewed baseline.
- FourTTFs/threeOFLs equal website source bytes and reviewed provenance exactly. Font TTF signatures verified;commit/path/size/hash metadata preserved with extension-specific scope only. `asset-receipt.json` and `font-binary-identities.json` bind original binary identities. Provenance has no local machine path.
- CSS PostCSS8.5.6:three sheets,130rules,zero warnings,exact roundtrip;four local font references plus two local sheet imports resolve against83file contract. No remote URL in this font graph. Added font stylesheet29lines;modified provenance module79lines and packaging-test155lines.
- Node24.18.0 `--check` both modified mjs:Passed. `git diff --check`:exit0. Author ran no test/package command and created no ZIP.
- Android113,current website18of19 (Privacy paragraph intentionally changed),71of74 original extension files,all46tests,manifest/dependencies/workflow and heldZIP unchanged;author made no sidecar writes. Runtime changes are only tokens/Demo/vendor notice plus nine resources;popup/options CSS roles/HTML/JS remain unchanged.

Text diff:`font-package-text.patch`,SHA256 `b8efaf0ff38d1f828550a80fea262ec0bd3a2a0209fb55b819a6a6ec94301672`;binary identities separate;all282 current captured paths/hashes in `after.json`.

|Historical candidate01 runtime file|SHA256|
|---|---|
|styles/fonts.css|`ce6b33f669f02299fe76779010b39493ea658458706c4b8301cb42b8674172b1`|
|styles/tokens.css|`07867dda695d73f6eca96606cafa22370f2daa1aaefd45d58717251075449383`|
|demo/demo.css|`0b3b627148b18ff1eb1e704c6065fe740b4bbf76953621ca840ca333b9cf5e30`|
|assets/fonts/provenance.json|`6dd333444b15e3a15238da9bd8cddeec07d6d1886226df9a67719e66e730c99b`|

Held old ZIP remains `278b59df8ddef0085c9c064da11485695074419894c3f43b5077a10fc44f1e9e`;its74-file acceptance/provenance cannot be reused as current83candidate acceptance. Root alone owns validation/unit/package commands and new unique outputRoot packaging/extraction/reproducibility;no default output writes by author.

## Limits and next gates

Exact copies inherit D02 cmap evidence and limitations:Geist/Mono Vietnamese NFC/NFD;Fraunces upright lacks U+0309/U+0323,italic lacks U+0309;Geist lacks U+2588 used by Mono terminal. Bundling is not actual loading/shaping/fallback/visual proof. New metrics invalidate affected previous screenshots/font evidence. Fresh popup/Options/Demo resource/offline/EN-VI-NFD/wrapping/keyboard/focus checks must bind the new extracted83candidate in reviewed desktop slots. Dynamic-font/helper/outline/surface corrections remain E03;E04 prepares source-bound QA. Deployment/release/C03/owner gates remain held.

Unresolved:E03 roles/E04runtime preparation and actual platform font/visual acceptance. Current actual validation/unit/package/artifact review is recorded below.

## Candidate02 — valid provenance tamper setup

Tester caught a new negative-test setup error before package-test execution:appending raw text to provenance.json made it invalid JSON,so source parsing would fail before the intended pinned-hash check. Fixed only that new test branch to parse metadata,change commit to `unapproved`,and serialize valid JSON. TTF/OFL append mutations and the expected hash-guard regex remain unchanged;production validation/old negative cases/46tests untouched.

Initial draft test source/hash retained in `packaging-test-before-json-fix02.mjs`/`before-json-fix02.json`;initial after/patch/asset receipts immutable. Final `verify-font-package02.mjs`→`after02.json`/`asset-receipt02.json` repeats all preservation/resource/font checks Passed;modified test module159lines,`node --check` and `git diff --check`exit0. Current full text patch:`font-package-text02.patch`,SHA256 `436a30fc088d520e5ec8b07dfc11208cd3f92218b7f9ba3d31720d4fbb2e5a6c`;original binary identities remain unchanged. At that historical handoff candidate02 superseded the preserved first draft;candidate03 below is current.

Root validation run02 actual83files/externalStyles[] exit0. Earlier run01 printed the same validated result but wrapper exit1 causeUnknown;both logs retained,no product failure inferred. Those candidate02 pending runs are historical;final candidate03 direct command/artifact evidence below resolves current verification,without erasing prior outcomes.

## Candidate03 — canonical standalone Demo constraint

Root unit run01:150Passed/1Failed (151total) at existing `test/unit/demo-locale.spec.mjs:48`:Demo CSS must contain no stylesheet imports/http,and HTML resources remain exactly demo.css/sheet-recipes.png/demo.js. The test expresses the existing self-contained teaching-page boundary;46canonical test files remain unchanged. Initial package run01 independently Passed67/67 with stable286-path source;its pass does not override this unit failure. All raw logs/failure JSON/candidate02 source/evidence retained.

Corrected actual Demo CSS to embed the same four local face declarations directly,with the same pinned binaries/relative URLs;no CSS import,extra HTML resource,JS loader or external URL. Popup/Options continue using shared font-only CSS. This deliberate four-declaration duplication honors the canonical standalone constraint;there is still one copy of each font binary and exact83inventory/zero external styling records. Changed only the new unmarked packaging positive expectation to require the token import and both four-face local graphs/no Demo imports. Existing negative cases,new18tamper/missing guards and production validation remain unchanged.

Vendor README/data matrix now distinguish shared Popup/Options declarations from standalone Demo;original commit/OFL notice guards retained. Root README/Privacy were reviewed and already accurately say bundled fonts without a contradictory shared-Demo assertion;no additional edits needed. Candidate02 CSS/test copies preserved under `before-standalone-fix03/`,with separate original and supplementary disclosure inventories;no old after/patch/raw rewritten.

Final `verify-font-package03.mjs`→`after03.json`/`asset-receipt03.json`:273before paths/264unchanged/9existing changes/9new;all46tests/Android/web-protected paths/held ZIP exact. Nine local CSS resource edges resolve (four faces per sheet plus token import),130rules/zero PostCSSwarnings/exact roundtrip;node syntax andgit diff --check exit0. Current full source text patch `font-package-text03.patch`,SHA256 `0d0ad1d00a444177b9af6a378ef487bb2c3025caca753c1debe120883699095d`;font binaries and original OFLs unchanged. Root owns unit/packageRun02/new unique ZIP;source frozen before those runs. Current implementation is candidate03;the shared-Demo proposal above remains dated history.

## Current candidate03 — actual closure evidence

Direct validate04exit0 confirms83inventory/zero external records;unit03exit0Passed
151/151;package02exit0Passed67/67with286source identities stable. See
[tester actual evidence](extension-font-package-verification.md) and
[independent source/package/final bookkeeping review](extension-font-package-independent-review.md).
Initial Demo150/151failure,invalid-JSON mutant draft and wrapper anomalies remain
retained/dated;no canonical test altered. Current03Demo has4embedded local faces,
no import;Popup/Options use shared sheet. Original fonts/OFL bytes remain exact.

New unique artifact-01ZIP has83root files/1832602bytes,SHA256
`3ed8c41db4232f54f610150ff4be58ce1ef01460d20b1ad97a2f39af6d4c1d1e`.
Root reproduced/extracted/validated it;tester/reviewer independently read bytes,
CRCs/sidecars/source bindings. Held74ZIP remains278b59…f1e9e,unchanged.
Final bookkeeping Passed;E02source/package Done,E03Ready. This closure is historical
to exact candidate03;later E03CSS needs a separate artifact. Browser/font/runtime/
owner/Store/release acceptance remains Pending,deployment held.
