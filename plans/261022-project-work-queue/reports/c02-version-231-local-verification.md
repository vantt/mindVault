# C02 — approved2.3.1 beta metadata and held local artifact

Date:2026-10-27, Asia/Saigon. Status:**LocallyVerified — independent review Passed**;
[review](c02-version-231-independent-review.md). Owner approved version2.3.1/package alignment,
controlled beta, required Windows11 x64 Chrome Stable/116 floor coverage and
real two-device Sync acceptance before publication. Root owns the decision record.
Broader OWN01/C01 remains unanswered; no A13/release acceptance inferred.

## TODO

- [x] Snapshot existing46tests/source/lock and preserve old frozen proofs.
- [x] Change only manifest version/display label and tooling package version.
- [x] Run151unit/49package/source/package/ZIP/syntax and metadata consistency checks.
- [x] Bounded tooling followup:exclude frozen build artifacts from default Vitest discovery;retain failure evidence and canonical tests.
- [x] Verify old/new runtime payload differs only at manifest metadata; notices/dependencies intact.
- [x] Update current README command/changelog/roadmap; record held artifact identities/gates.
- [x] Independent review Passed;root updates decision/track/catalog/master/index. Runtime/publication gates retained.

## Exact scope —7files

| File | Change |
|---|---|
| [Manifest](../../../chrome-extension/src/manifest.json) |Numeric version2.3.1 and display `version_name:"2.3.1 beta"` only. |
| [Package](../../../chrome-extension/package.json) |Tooling version1.0.0→2.3.1; all dependencies/scripts/package-manager metadata unchanged. |
| [Vitest config](../../../chrome-extension/vitest.config.mts) |Separate bounded verification repair:add `**/build/**` exclusion for frozen artifact copies. No canonical test/assertion changes. |
| [README](../../../README.md) |Current packaging verification command points to2.3.1 filename; historical evidence text retained. |
| [Changelog](../../../docs/project-changelog.md), [roadmap](../../../docs/development-roadmap.md), this report |Approved metadata, tooling failure/fix, held artifact and remaining gates. |

Root's [C02 decision](c02-release-boundary.md) owns approval/choices; this task
does not edit it or track/catalog/master statuses. No new dependency, permission,
identity key, minimum116, KDF, Cookbook/storage format, UI behavior or native code.
`pnpm-lock.yaml` remained byte-identical; frozen install required no resolution.
No permanent test added for metadata/config filtering; all46existing files and
Golden assertions retained. Touched config is9lines, well below200.

## Verification and initial discovery failure

Runtime:Node24.18.0/pnpm10.24.0, using PATH prefix
`/home/vantt/.nvm/versions/node/v24.18.0/bin`. pnpm commands run from extension.

Initial `pnpm test:unit` exit1:Vitest's broad glob discovered immutable
`build/b01-selected/harness/test/unit/` evidence copies. Twenty copied suites
could not resolve relative production imports from their archive layout; one
copied detector suite ran7duplicate assertions. All21live canonical files/
151assertions passed in that same run. Raw summary:20failed/22passed files,
158passed assertions,9.43s. This is a discovery/tooling failure, not failed2.3.1
product assertions. Initial failure/import paths/raw log remain preserved.

CLI diagnostic `pnpm test:unit --exclude 'build/**'` Passed151/151 in4.87s.
Root then authorized the proper bounded default fix:exclude the ignored build
artifact tree in Vitest config. Frozen snapshots were not moved, deleted or rewritten;
no canonical tests were skipped/edited. **Exact default `pnpm test:unit`** after
that fix Passed151/151 across21files in4.56s. Parsed executed filenames equal all
21canonical `test/unit/*.spec.mjs` files; assertion counts sum151. Original46test
SHA checks pass. The CLI diagnostic is not used as a substitute for default success.

| Actual check | Result |
|---|---|
| `pnpm install --frozen-lockfile --offline` |exit0,1.7s;up-to-date/no resolution/lock change. Postinstall verified/copied same mandatory Argon2 JS/WASM/MIT. Existing ignored-esbuild-build-script warning retained; no approval-setting change. |
| `pnpm test:unit` initial/diagnostic/final |Initial failure and diagnostic above retained;final exact default151/151,21live files,4.56s Passed. |
| `pnpm test:package` |exit0,49/49 filesystem/archive fixtures,42.31s;0fail/skip. Version-derived2.3.1 names covered. |
| `pnpm validate` |exit0,74reviewed resources/imports/JSON/JS syntax/WASM/notices; unchanged explicit external font-data record. |
| `pnpm package` |exit0,74files/1,102,457bytes; new development ZIP/inventory/SHA. Old2.3.0 outputs untouched. |
| `pnpm validate:zip build/passchef-extension-2.3.1-development.zip` |exit0,74exact entries/source bytes/matching SHA. |
| Separate stdlib ZIP comparison |CRC/member sets pass;only `manifest.json` payload differs from old8e92ZIP. Other73runtime files byte-identical; manifest parsed delta is exactly approved version/display label. |
| Metadata consistency |Package parsed delta only `version`; lock hash unchanged; manifest min116/permissions/no-key and every other field retained. Vitest config parsed by final real default invocation. |
| Existing proofs/tests |Pre-task/root46SHA checks Passed; old frozen B01 record/source74/harness50 and old2.3.0/A06/FA01 artifacts intact. |
| Link/source/status checks; `git diff --check` |13local targets/source hashes/current README command/held gates checked;diff exit0. |

No full headed suite/browser/display/Windows Chrome/phone/ADB/Gradle/account/Sync/
push/deploy/Store/publication. Prior desktop slot was released26October. Package
and resource validation prove bytes/static assembly, not browser-loaded behavior.

## Held development artifact and provenance

New [2.3.1 ZIP](../../../chrome-extension/build/passchef-extension-2.3.1-development.zip),
[74-file inventory](../../../chrome-extension/build/passchef-extension-2.3.1-development-inventory.json)
and [SHA sidecar](../../../chrome-extension/build/passchef-extension-2.3.1-development.zip.sha256):
`278b59df8ddef0085c9c064da11485695074419894c3f43b5077a10fc44f1e9e`.

Ignored [`build/c02-version-231/`](../../../chrome-extension/build/c02-version-231/)
holds independent copies of those artifact files, metadata/config/lock/contract,
initial/diagnostic/final unit logs and49-case package log. No additional test-source
copies were created. [Held record](../../../chrome-extension/build/c02-version-231/held-record.json)
includes74per-file hashes/sizes,46canonical test hashes, baseline, dependency/license
provenance and explicit open gates. Record SHA256:
`0d7e97303bdd67040710a37bb514ca893e40fee30797364a9264556b9f0cd336`.
It is a **held local development artifact**, not a stable/release-accepted A13 candidate.

Dependency unchanged:argon2-browser1.18.0, pinned lock integrity and runtime hashes
recorded in inventory/held record. Copied Argon2 MIT notice hash:
`524a1d77701975a063a590424637abbd7fa519042a113f2bbe2eaf4cde76296d`.
Vendored Nayuki QR code/embedded MIT header remains byte-identical:
`79f419f267ce5a80d97f8099e0a789a4ecc4697b5348d86371a1f3b2a75d6e03`.
Existing [vendor provenance](../../../chrome-extension/src/vendor/README.md) and
other notices retained; no historical LICENSE rewrite or licensing-model change.

Source identities:

```text
23a91f7bc4c550dc1347857d2b2fe86879db05d98a8599f673d2af8f26f39ffe  src/manifest.json
ca4edfdc2dc1efec550bdd2f49883410c9c551f30c48411f4c51f4c18793cf20  package.json
f1fb64ebc7088d3cf327f9b2e1c42cf1b22dc19e4b920ad1735bbe340980712b  pnpm-lock.yaml (unchanged)
552ddc2127d094801469834f1ec339c0c9fd8768faf85210f72fd3f3fb4c261e  vitest.config.mts
```

Old2.3.0 ZIP remains`8e921455f31f4ba6d749242c94d3b9f1b17f964656fdca2fc1d7dc449d92f7b7`;
immutable B01 record stays`b6e0669241095f2947360b8c98dc563ae74930f499ad9ad90d468c67cf865d94`.
All old frozen74source/50harness copies retain recorded hashes. Current manifest
differs intentionally, so old B01 preflight does not silently qualify new2.3.1 bytes.
A06`6c6fc800…0fe9` and FA01`b4986a20…fac4de` frozen packages remain exact.

## Affected evidence and current gate

Old B01 selected3/3 proof is historical2.3.0 source evidence. Production logic and
73runtime files are unchanged, but there is no fresh2.3.1 browser/upgrade proof;
assess reuse explicitly against the eventual candidate, metadata/ID/delivery route.
Stable A13 remains held by broader OWN01/C01/A23. Required Windows11 x64 Chrome
Stable and116 support/performance, same-ID real two-device Sync, full candidate
B01/B07 and B08upgrade still need their own prepared/authorized runs. Version bump
does not establish an actual installed update or select an identity key/account.

## Unresolved

OWN01/C01/A23 and stable A13 gate; Windows Chrome Stable/116 B03 support/performance,
actual two-device C03/B08 Sync, candidate B01/full suite/B07/upgrade, publisher/legal/
hosting/publication gates. No GUI/device/account/Sync/push/deploy/publish authorized.
