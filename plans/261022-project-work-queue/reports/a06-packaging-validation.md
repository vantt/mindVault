# A06 — Repeatable development packaging and validation

Date:2026-10-26, Asia/Saigon. Source:HEAD039c76d + preserved WIP;
manifest2.3.0/package1.0.0 unchanged. Development artifact only; no release acceptance.
Status:**Done2026-10-26 — local development packaging**;
[independent review Passed](a06-independent-review.md).

## TODO

- [x] Inspect accepted loader/WASM, dependency/version/lock provenance and QR notices.
- [x] Harden mandatory-asset copy preflight; preserve accepted runtime bytes.
- [x] Add focused validate/package commands and shipped notice/inventory checks.
- [x] Exercise filesystem negative fixtures and source syntax/unit/package checks.
- [x] Record artifact SHA/content inventory/limits; update tooling docs.
- [x] Independent review Passed; task/catalog/master/index updated after acceptance.

## Implementation and exact scope

| File(s) | Change |
|---|---|
| [package.json](../../../chrome-extension/package.json), [pnpm lock](../../../chrome-extension/pnpm-lock.yaml) | Add validate/package/validate:zip/test:package commands; pinned development-only fflate0.8.2 and es-module-lexer1.7.0. Runtime dependency and manifest/product versions unchanged. |
| [copy_wasm.js](../../../chrome-extension/scripts/copy_wasm.js), [provenance](../../../chrome-extension/scripts/packaging-provenance.mjs), [path preflight](../../../chrome-extension/scripts/package-paths.mjs) | Preflight both mandatory assets/version/lock/hash/license and all existing destination paths before writes. Missing asset fails even with stale destination. Destination symlinks rejected; legitimate pnpm dependency input symlinks remain usable. Accepted loader/WASM bytes retained. |
| [reviewed contract](../../../chrome-extension/scripts/package-contract.json) |73shipped paths,4explicit exclusions, runtime/library/notice hashes, lock provenance and current external Google Fonts CSS URL allowlist. Source inventory changes require editing this reviewable contract. |
| [source validator](../../../chrome-extension/scripts/validate-package.mjs), [resource checks](../../../chrome-extension/scripts/package-resources.mjs) | Reject missing/extra/private/development source payloads, child symlinks, malformed JSON/JS, missing/escaping/local import/resources, nonliteral executable imports or unreviewed dynamic paths. Explicit source-directory override is intentional. Verify manifest/icon/locales/content scripts, offscreen/worker, wrapper JS/WASM, HTML/CSS/Demo and bundled notices. |
| [ZIP command](../../../chrome-extension/scripts/package-extension.mjs) | Deterministic development ZIP; validate exact root entries/bytes, duplicates, missing manifest, extra payloads and sizes. Record SHA256/inventory; preflight output tree, ZIP/temp/inventory/hash paths against symlinks before writes. No general concurrent-filesystem race guarantee. |
| [filesystem fixtures](../../../chrome-extension/scripts/packaging-fixtures.mjs), [packaging tests](../../../chrome-extension/scripts/packaging-test.mjs) | New standalone49-case Node test suite; actual temporary source/dependency/archive mutations and outside-sentinel/stale-destination assertions. No existing unit/E2E/Golden tests edited. |
| [Argon2 MIT notice](../../../chrome-extension/src/vendor/argon2-browser-LICENSE.txt) | Exact1058-byte dependency license copied and shipped. Existing Nayuki embedded MIT and provenance README remain byte-identical. This is the only source-tree addition; all existing runtime source bytes unchanged. |
| [README](../../../README.md), [changelog](../../../docs/project-changelog.md), [roadmap](../../../docs/development-roadmap.md) | Tool commands, development-only artifact meaning, current limitations and A07/final acceptance handoff; prior WIP retained. |
| This report | Evidence/commands/artifact record. No registry Done before independent review. |

These16files are the A06 scope. Legal/font-disclosure amendments by root are
separate A04 follow-up WIP, not authored or claimed here. No ADB/Gradle/GUI,
account mutation, Git push, publish or release-candidate assembly occurred.

## Dependency provenance and notices

Before editing, current source, HEAD and installed dependency loader/WASM matched.
Read-only `pnpm view argon2-browser@1.18.0 dist --json`, then fetching its registry
tarball and hashing the archive confirmed the exact locked SHA512:

`sha512-ImVAGIItnFnvET1exhsQB7apRztcoC5TnlSqernMJDUjbc/DLq3UEYeXFrLPrlaIl8cVfwnXb6wX2KpFf2zxHw==`

Original tarball members were read in memory, never installed/executed. Their
SHA256 matched the accepted/installed files and newly shipped notice:

| Member | SHA256 |
|---|---|
| `dist/argon2.js` | `cdb6ef704dbc287ffa0056376d9af297c8691ddeb985f9137b96fc9b0ddea8b0` |
| `dist/argon2.wasm` | `0c2149886c13e4eae4a6ca25ee71d47423c5c8740a874cf04ff816d1b2c901d7` |
| `LICENSE` | `524a1d77701975a063a590424637abbd7fa519042a113f2bbe2eaf4cde76296d` |
| Nayuki compiled QR with embedded MIT | `79f419f267ce5a80d97f8099e0a789a4ecc4697b5348d86371a1f3b2a75d6e03` |

The QR [existing provenance](../../../chrome-extension/src/vendor/README.md) retains
upstream commit3c6d0b3cefb4e049dc337e82237c9644399716a8. No speculative loader patch,
compiler rebuild, crypto/KDF parameter change or notice removal. Dependency helper
docs consulted:[fflate](https://github.com/101arrowz/fflate),
[es-module-lexer](https://github.com/guybedford/es-module-lexer); neither is shipped.
This is byte/provenance verification, not a fresh security audit or legal clearance.

## Resource/exclusion boundaries

Static/dynamic ESM imports use es-module-lexer; import.meta records are not treated
as imports. Literal module URLs/Worker resources and root-relative runtime.getURL
references are checked. Current dynamic locale URLs expand to both EN/VI bundles;
the offscreen PATH constant and trusted-page list have explicit source-bound cases.
Other nonliteral executable imports/dynamic paths fail rather than assuming success.
Malformed JS receives Node syntax checks; the WASM binary validates structurally.

The4excluded files remain in source unchanged:legacy unreferenced `content_script.js`,
`assets/icon-1024.png`, and the2UUID-named legacy Demo PNGs. No shipped resource
reference resolves to them; source deletion was unnecessary. The manifest's actual
content reader is `content/content.js`; required icon16/32/48/128 and current
`demo/sheet-recipes.png` remain shipped. Unknown source payload paths fail; tests,
dev dependencies/config and private/debug extras are never copied wholesale.
An explicit path inventory does not detect arbitrary sensitive text inserted into
otherwise approved authored files; normal source review still applies.

Source-discovered external resource:styles/tokens.css declares Google Fonts CSS.
Its exact existing URL is recorded as stylesheet/font data separately from remote
executable resources. The validator neither fetches it nor proves availability,
provider retention, offline font rendering or privacy compliance. Root's A04
font-disclosure amendment owns that correction; no fonts were removed.

## Fresh commands/results — Node24.18.0, pnpm10.24.0

Working directory:chrome-extension. Prepend
`/home/vantt/.nvm/versions/node/v24.18.0/bin` to PATH. One early test invocation
without that PATH reached the Windows pnpm shim and exited127; corrected runtime
commands below are the actual verification. No dependency upgrade to suggested
pnpm/tool versions was performed.

| Command/check | Actual result / limit |
|---|---|
| `pnpm add -D fflate@0.8.2 es-module-lexer@1.7.0` |Pinned dev dependencies/lock updated; no product dependency change. |
| `pnpm install --frozen-lockfile --offline` |exit0, lock current; postinstall verified/copied JS/WASM/notice. Existing ignored-esbuild-build-script warning retained; no approval-setting change. |
| `pnpm test:unit` |exit0;147/147 tests,20files,3.07s, no skip/failure. Expected invalid-Tag diagnostics only. Existing tests unchanged. |
| `pnpm test:package` |Final exit0;49/49 tests,25.84s, no skip/failure. Earlier36/44-case runs Passed before additive independent-review repairs; final49-case result includes both fixes. |
| `pnpm validate` |exit0;73reviewed shipped files,54runtime JS syntax checks,3runtime JSON bundles parse, dependency/notices/WASM/resources valid;1declared external stylesheet record. |
| `pnpm package` |exit0; development ZIP1,102,759bytes,73root files; deterministic in repeated in-memory assembly fixture. |
| `pnpm validate:zip build/passchef-extension-2.3.0-development.zip` |exit0;73exact entries/current source bytes; ZIP hash below. No extracted-ZIP Chrome loading or runtime KDF test. |
| `node --check` over scripts |13/13scripts passed;8authored/changed code files each below200lines. |
| Source/test preservation |Existing runtime loader/WASM/QR/manifest and all existing tests have empty Git diff. Golden SHA256`5f9faa1e2bb5e3d38151250ffaa961a94efba459f41786f5ae90402ab07aae26` unchanged. |
| `git check-ignore` development ZIP; `git diff --check` |Both exit0; artifact remains ignored; scoped Markdown links checked at handoff. No nonexistent lint/build result claimed. |

Actual filesystem negative fixtures cover missing loader/WASM/both with stale
destinations, modified dependency/lock bytes, missing static/dynamic imports,
nonliteral executable/module/runtime URLs, missing Worker/getURL/HTML/CSS resources,
remote executable/unapproved CSS resources, malformed manifest/locale JSON,
traversal, symlink/debug/private payloads and missing/changed Argon2/QR notices.
ZIP mutations cover prefixed root, missing manifest, extra private entry, changed
bytes, duplicate central-directory names and truncated archives.

Independent review identified2P2 write-through destination-symlink repros:copy
assembly and package output/temp/sidecars. Fixed by complete destination preflight
before writes;6assembly and5output actual filesystem fixtures preserve outside
sentinels and every existing destination. Input pnpm links remain allowed. This
does not claim protection against a hostile process racing path replacement.

## Development artifact record

- ZIP:[passchef-extension-2.3.0-development.zip](../../../chrome-extension/build/a06-frozen/passchef-extension-2.3.0-development.zip)
- SHA256:`6c6fc80055c61d49cba404694fa8691a1c41316bccc2a14b491e930995fc0fe9`.
- [Per-file size/SHA inventory](../../../chrome-extension/build/a06-frozen/passchef-extension-2.3.0-development-inventory.json),
  [hash sidecar](../../../chrome-extension/build/a06-frozen/passchef-extension-2.3.0-development.zip.sha256).
  Generated artifacts are intentionally ignored and reproducible by the commands.
- Reviewed [contract inventory](../../../chrome-extension/scripts/package-contract.json)
  lists all73shipped paths and exclusions; generated record additionally binds exact
  content hashes, dependency provenance and external styling data.
- This artifact includes source/WIP described above; it is neither a frozen A13
  release candidate nor browser/upgrade/Sync/Store acceptance.

Dated path amendment2026-10-26:A23-FA01 changes runtime source and produces a new
development ZIP at the ordinary build-root path. The original A06/A07 ZIP/inventory/
SHA sidecars above were preserved under `build/a06-frozen/`; original counts, hashes,
results and limitations remain unchanged. Re-running packaging on changed source
is expected to yield A23's different bytes/hash, not reproduce the old artifact.

## Unresolved

A07 CI adoption; C01/C02 stable product boundary; A13 candidate; B07 extracted ZIP/
browser/WASM/fail-closed runtime and B08 upgrade/Sync acceptance remain Pending.
