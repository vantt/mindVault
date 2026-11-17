# E03 extension role alignment verification — 2026-11-02

Current final revision02 source/census checks, direct validation, 151 unit tests, 67 packaging tests and independent new ZIP inspection Passed. Candidate01 results below remain historical. No product/test changes by tester. Browser/final release acceptance remains Pending.

Evidence: `chrome-extension/build/cross-platform-design-consistency-20261102/extension-role-alignment/`. Node24.18.0, pnpm10.24.0, PostCSS8.5.6; no downloads.

## Independent source results

Command from repository root:

```sh
/home/vantt/.nvm/versions/node/v24.18.0/bin/node chrome-extension/build/cross-platform-design-consistency-20261102/extension-role-alignment/verify-tester-source.mjs
```

Exit 0. Exactly popup/options/Demo CSS changed against 289 frozen paths; 286 protected paths unchanged. Those include 46 canonical tests, current 113-file Android source set, 19 website files including Privacy, package/provenance/dependency/crypto resources and both old/E02 development ZIP evidence. No HTML/JS/string/handler/state mutation.

- Three stylesheets parse/round-trip exactly:518rules, zero warnings. AST comparison independently identifies50existing declaration changes:32text colors,13field border colors,4named radii,1family. One new dynamic role rule and one focus selector extension account for author's52 edit operations. All remaining declarations,at-rule contexts,spacing/dimensions/font sizes/line heights/layout/overflow/transforms preserved. Eight font/provenance hashes and shared/Demo four-face declarations retained;Demo has no import/http.
- `options.css:746` directs dynamic `.profile-name` to Geist. `options.css:955` scopes Geist/normal to the two actual dynamic modal fragments. Actual untouched consumers remain `options-profiles-tab.js:69,112`, `profile-editor.js:24`, `options-export-wizard.js:14`. Static title inheritance remains Fraunces;no Unicode normalization introduced. This proves source roles,not actual NFD glyph shaping.
- Source opaque-pair arithmetic independently gives Ink600 text contrast5.2496 Canvas,4.9535 Surface,4.6155 Raised;all exceed4.5. Enabled field Ink500 boundaries yield3.3356 Canvas and3.1474 Surface. Ink500/Raised is2.9327 and is **not** accepted for a Raised boundary;the changed actual field backgrounds are Canvas/Surface. No universal rendered-contrast certification.
- Existing27focus/disabled/invalid/error/checked rule declaration sets remain exact. New `.secret-item .input-wrapper:focus-within` at `options.css:428` has specificity(0,3,0),above later default wrapper(0,2,0);it keeps the original Amber focus declarations effective. `.field-note` already used Ink600 and `.field-note.error` keeps Coral. Five Ink500 subjects remain deliberately:diagram plus,status separator,disabled primary button,quick-start close glyph and disclosure arrow.
- Named profile-card/modal surfaces become square;two profile badges use3px chip radius. All other radius declarations,identity colors,toggle/circle geometry,popup12px frame and compact26/30px controls retain their source values. No blanket enlargement or global replacement.

Raw `verification-tester-source.json` SHA256 `ef75e66f7df1fbd9f6fa157c03bed90fb794020634392f0f74d2bfab08f2af0a`; reusable checker and exact declaration deltas retained alongside it.

## Actual root commands

- `pnpm validate`,14:44:30.791–14:44:33.334UTC: direct exit0,83runtime files,zero external styles. `validate-run01-result.json` SHA256 `97e2349881fed818a266450e367f1d3a63fd6b499d15d9d8839bdd400c37c427`.
- `pnpm test:unit --maxWorkers=2`,14:46:14.160–14:46:22.432UTC: direct exit0,151/151Passed across21files,source stable289paths. `unit-run01-result.json` SHA256 `0c701611b997d0423a5712080f61f311f40090927fdbe4d529080d6e3912aa57`; log SHA256 `08b2b49f1bd91444ac29841fe70a9bd3b41a55ee710addb3372ce1635482c36f`.

Tester ran `pnpm test:package` once under root GO, using the direct Node 24 wrapper `run-package-tester.mjs`: 14:46:47.918–14:47:33.183 UTC, exit 0, **67/67 Passed**, no skips/cancellations, 44,498.611 ms test duration. All 289 identities in `tests-before.json` matched before/after. Unchanged packaging test SHA256 `1afffc51fce364969eeff022afc34cdff5a4b3024e2108c2fe920a47d3f1c01c`. Full TAP `package-run01.log` SHA256 `7c756b05b095875deb47bf0c38bd3aa7830656f28bb6a831a0b61c149e92ba28`; direct result SHA256 `4f39956cd23d2a93921c74e708b3122a547a8d9be6863caf3f5965996ece2868`. `git diff --check` exit 0. No failure or retry in this slice.

## New development artifact

Root created a unique `artifact-01/passchef-extension-2.3.1-development.zip`: 1,832,658 bytes, SHA256 `9519d2e05757ac826a5e8b6453a2befce10857a1d722dd526a7d2f3942140301`. Root's `artifact-run01-result.json` records reproducibility and extracted 83-file validation with zero external styles.

Tester independently ran:

```sh
/home/vantt/.local/bin/uv run --offline --no-project --python /usr/bin/python3 python chrome-extension/build/cross-platform-design-consistency-20261102/extension-role-alignment/verify-tester-artifact.py
```

Exit 0: 83 unique contract entries, valid CRCs, all 83 entry contents equal current source and root-extracted files byte-for-byte, both sidecars and root source receipt exact. All eight pinned font/license/provenance entries match. All 289 frozen paths unchanged; historical ZIP `278b59df…f1e9e` and E02 ZIP `3ed8c41d…c1d1e` plus sidecars remain unchanged. Tester did not package or extract again. Raw `verification-tester-artifact.json` SHA256 `217f83834885f0650a22ebec5fb0c7fd70562d16c63de67cde85f5820b54feba`.

## Revision02 — complete surface/chip census

Independent review/root census found additional existing rounded major panels beyond candidate01's named profile/modal fixes. Root authorized exactly ten further radius edits: Options `.card` and eight Demo major containers become square; Demo TOC chips become 3px. Four initial Demo changes and the later six census changes have separate preserved snapshots. Candidate01 receipts, actual test logs, ZIP `9519d2e0…40301` and extraction remain immutable; its source-only pass did not establish complete surface alignment.

Fresh commands:

```sh
/home/vantt/.nvm/versions/node/v24.18.0/bin/node chrome-extension/build/cross-platform-design-consistency-20261102/extension-role-alignment/verify-tester-source02.mjs
/home/vantt/.nvm/versions/node/v24.18.0/bin/node chrome-extension/build/cross-platform-design-consistency-20261102/extension-role-alignment/verify-tester-radius02.mjs
```

Both exit 0. Candidate01→final02 differs in **exactly ten radius declarations**; normalized source text otherwise exact. Full E03-baseline comparison now identifies 60 existing declaration changes, one dynamic rule and one focus selector extension; all other geometry/state/font/color declarations retained. Three sheets still contain 518 rules with zero warnings. All 286 protected pre-edit identities remain exact.

Radius census reads every declaration across all three current sheets: **74 occurrences** (popup 16, Options 36, Demo 22), including at-rule context; 53 match static HTML subjects. Dynamic generated nodes and pseudo-elements are explicitly limited to source inventory, not rendered proof. All 11 named major surfaces are square; scoped profile badges and Demo TOC chips use 3px. Remaining fixed values above 4px are only popup body frame 12px and Options language-toggle enclosure 6px; intended 50% circles and input/action/note 4px roles remain. Compact desktop 26/30px dimensions unchanged.

Fresh source receipt SHA256 `2df0428c15037f5530cf85b5c23d30d53ac482bede348f914bb9431af420b145`; census receipt SHA256 `54ea68cdb7e97e6128c0c8fbdd746873b7d226d55b1247e50318399b1bc921e7`.

Root's direct `pnpm validate` run02, 14:55:01.824–14:55:04.257 UTC: exit 0, 83 files/zero external styles, source stable. Direct `pnpm test:unit --maxWorkers=2` run02, 14:55:04.258–14:55:13.003 UTC: exit 0, **151/151 Passed across 21 files**, source stable. Unit log SHA256 `286dddad40a9767059d7d338b127c0faecc04ed54d8385d75048e318210ba27d`.

Tester direct `pnpm test:package` run02, 14:55:32.838–14:56:16.629 UTC: exit 0, **67/67 Passed**, no skips/cancellations, 43,027.695 ms test duration. `run-package-tester02.mjs` binds all 292 paths in fresh `tests-before02.json` (prior 289 plus candidate01 ZIP/sidecars); every identity unchanged before/after. Packaging test code remains unchanged. Log SHA256 `1577a93e47ae36020c8c0f3071ae2cd5d444bebf6fa229e4ae1fd9c46f339e38`; direct result SHA256 `4d91b9059c8c4d800c02ec96bf3f63f2fddb5eed7d0585a863d43028262a0e90`.

Final revision02 artifact: unique `artifact-02/passchef-extension-2.3.1-development.zip`, 1,832,653 bytes, SHA256 `c11e3a6a1812800f45b4770af7e82b648709a28989ec937d45c1e90c27691ae9`. Root's `artifact-run02-result.json` records reproducibility and extracted 83-file validation/byte equality. Tester ran the new reusable read-only checker:

```sh
/home/vantt/.local/bin/uv run --offline --no-project --python /usr/bin/python3 python chrome-extension/build/cross-platform-design-consistency-20261102/extension-role-alignment/verify-tester-artifact02.py
```

Exit 0: **83 unique contract entries**, valid CRCs, all entry contents equal current source/extracted bytes, exact inventory/hash sidecars and eight pinned font/license/provenance resources. All **292 frozen paths unchanged**, including all three historical development ZIPs (`278b…`, `3ed8…`, `9519…`) and their recorded sidecars. Raw `verification-tester-artifact02.json` SHA256 `4115864f095e2ce03a6c70f02e402135ade5ed7b09b694eef7c12801d5535ae9`. No repackage, repeated extraction or historical raw rewrite by tester.

## Limits / unresolved

Source/cascade reasoning, opaque contrast arithmetic and package inspection only. No browser/GUI/server/Gradle/headed E2E; no computed layout, font loading, NFD shaping, actual focus visibility or journey acceptance. E04 prepares those checks. No unresolved source/package finding;independent final02review/plan bookkeeping Passed,E04Ready. All evidence binds final02,not future mutable sources.
