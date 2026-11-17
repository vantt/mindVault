# E02 extension font/package verification — 2026-11-02

Candidate03 source checks, direct validation,151 unit tests,67 packaging tests and independent new development ZIP inspection Passed. Tester changed no product code or canonical tests. Browser/final release acceptance remains Pending.

Evidence directory: `chrome-extension/build/cross-platform-design-consistency-20261102/extension-font-package/`. Node24.18.0, installed pnpm10.24.0 and PostCSS8.5.6; no downloads.

## Actual results and preserved failures

- Candidate02 actual `pnpm test:package`: exit0, **67/67 Passed**, no skipped/cancelled, duration41,239.126ms; 14:28:38.389–14:29:20.462UTC. All286 pre-test identities matched afterward. Raw `package-run01.log` SHA256 `29441002bc63c942fdd134be2982dadd976977bece17f5b71f6a77205a74067f`; result `package-run01-result.json` SHA256 `8e5adee3643c218e392277e44da75fb41bddcf8dcbd8ffb5f3a4c04d0b07f4b0`.
- Root's preserved candidate02 unit run: **150/151 Passed, one Failed** at `test/unit/demo-locale.spec.mjs:48`. Existing self-contained Demo contract forbids every `@import` and retains its three HTML assets. Implementation corrected: Demo embeds the same four local face declarations; no import or additional HTML link. Existing canonical test retained byte-for-byte. Candidate02 suite success does not erase this failure.
- Before execution, tester identified a new provenance-tamper fixture that appended invalid JSON, preventing the intended pinned-byte assertion. Author corrected only that newly added fixture to mutate a valid JSON commit. Original draft and receipts remain preserved; production validator unchanged.
- Root's initial validate wrapper anomaly is preserved: run01 printed validation but returned wrapper exit1/unknown; root's explicit run02 succeeded83/zero external styles. Final-candidate command results are recorded below when supplied.

## Candidate03 actual root commands

- Direct `pnpm validate`,14:35:01.733–14:35:04.358UTC: exit0,83files and `externalStyles:[]`. `validate-run04-result.json` SHA256 `80fbe9375a14fe503456493db09e797faecd26cb69d9f03af8ee885e23b56a09`; log SHA256 `6aa45816bd3e9c3a58741312566490e5af54eacbe8b0bc26e7ea4e64d6afc942`.
- Direct `pnpm test:unit --maxWorkers=2`,14:35:31.811–14:35:43.570UTC: exit0, actual **151/151 tests across21files Passed**, source stable. `unit-run03-result.json` SHA256 `789478341ed80874c374dc4196c26b6f400ba2cd8099a6c8e09da8a54a7e34bc`; log SHA256 `a18853621ade5b372020f4657f4c53c42ce7efd7b6cd4bb45c3678b887c85952`.
- Tester direct `pnpm test:package`,14:35:39.116–14:36:34.846UTC: exit0, **67/67 Passed**, no skips/cancellations;54,487.413ms test duration. All286 identities in `tests-before02.json` unchanged before/after; current packaging-test SHA256 `1afffc51fce364969eeff022afc34cdff5a4b3024e2108c2fe920a47d3f1c01c`. Reusable `run-package-tester02.mjs` captures child status/full TAP/source hashes without modifying previous run. `package-run02-result.json` SHA256 `e5b714119468253188f86e4bf73e3d37dc277948d6ba4b6d44035b1991767209`; log SHA256 `524a2611eb588f4a98014900db1485b892326806ce456a993ead65b6acc38c60`.
- Earlier unit02 printed151/151 and inner exit0 while execution wrapper returned1; its wrapper status remains Unknown rather than a product failure. Direct unit03 resolves final candidate execution status. Earlier validate03 wrapper discrepancy is likewise superseded by direct validate04; raw attempts retained.

## Independent source verification

Commands from repository root:

```sh
/home/vantt/.nvm/versions/node/v24.18.0/bin/node chrome-extension/build/cross-platform-design-consistency-20261102/extension-font-package/verify-tester-static.mjs
/home/vantt/.nvm/versions/node/v24.18.0/bin/node chrome-extension/build/cross-platform-design-consistency-20261102/extension-font-package/verify-tester-protection.mjs
```

Fresh candidate03 checks **6/6 Passed**:

- Exact nine authorized old-file changes, nine new runtime resources,264 original protected paths unchanged against273-path pre-edit receipt. Contract74→83 unique sorted entries, original exclusions/Argon2/QR values intact; external stylesheet allowlist empty.
- Four TTFs and three OFL notices match reviewed D02 files byte-for-byte, including SHA256 and Git blob IDs. Filtered seven-entry public provenance matches original source metadata at Google Fonts commit `6c1dbe27dcf7d817d4b62cec033ba77fa8e2a5ec`; eight resources pinned by package contract.
- Three CSS files parse:130 rules, zero warnings. Shared and Demo declarations contain identical four local faces,100–900 weight ranges and swap behavior. Tokens imports only local `fonts.css`; Demo contains no import/http. All Demo non-font declarations and token body after `:root` retained.
- All original copy/source negatives and ZIP/destination negatives remain byte-identical; original cryptographic validation body retained. Added18 cases cover nine missing resources, eight altered assets/provenance and one missing notice. Unmarked package positives legitimately migrate to83/zero external styles; canonical46 files untouched.
- Privacy replaces exactly one extension-font paragraph; other HTML and website-hosting paragraph unchanged. Current-source bundled-font statement retains final artifact verification limits.

Supplemental identities independently match **113 M04 Android files**,18 other website files,46 canonical extension tests, package manifest/lock/workflow and original held ZIP. The110 app/mobile subtotal in static receipt excludes three root Android configuration files; supplemental receipt checks the full113-file M04 source set. Held ZIP remains SHA256 `278b59df8ddef0085c9c064da11485695074419894c3f43b5077a10fc44f1e9e`.

Raw static receipt SHA256 `9c362df2be678f999c43ef021498878e70f0e73d08310c20996c6838d6cd88bb`; preservation receipt SHA256 `4a6ef74a74df76b0df6bff19268ec6fcb4b24bb8d8ae300ef16fa8d1f2035ba0`. Scripts and receipts retained in ignored evidence directory. `git diff --check` exit0.

## New development artifact — independent read-only inspection

Root created a fresh `artifact-01/` output directory using the packaging API; no default package writer or held artifact overwrite. New `artifact-01/passchef-extension-2.3.1-development.zip`:1,832,602bytes, SHA256 `3ed8c41db4232f54f610150ff4be58ce1ef01460d20b1ad97a2f39af6d4c1d1e`. Root's `artifact-run01-result.json` records in-memory reproducibility and83-file extracted-source validation with zero external styles.

Tester ran independently:

```sh
/home/vantt/.local/bin/uv run --offline --no-project --python /usr/bin/python3 python chrome-extension/build/cross-platform-design-consistency-20261102/extension-font-package/verify-tester-artifact.py
```

Exit0:83 unique root entries exactly match contract; CRC checks pass; all83 ZIP entries equal current source and root-extracted bytes. Inventory/hash sidecars and root source receipt agree. All eight pinned font/license/provenance entries match contract; all286 frozen paths remain unchanged after artifact creation and original held ZIP retains its historical SHA256. Tester did not package or extract again. `verification-tester-artifact03.json` SHA256 `864e3d15d91e68be9ff4b3a61f114cc4d022797b1376148f54ce09b70793c12d`.

## Limits / unresolved

Source/packaging verification only. No browser, headed E2E, GUI, server, Gradle or runtime network exercise. Bundled bytes do not prove font loading, glyph shaping, dynamic/NFD role correctness, Store or final release acceptance. E03 consumer roles remain separate. No unresolved source/package finding;independent review/final plan bookkeeping Passed,E03 Ready. Evidence binds candidate03 before later role changes.
