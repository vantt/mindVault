# A23-FA01 — Full Access own-profile envelope repair

Date:2026-10-26, Asia/Saigon. Source:HEAD039c76d + preserved WIP/A06–A09;
manifest2.3.0 unchanged. Selected FA01 **LocallyVerified**;
[independent review Passed](a23-full-access-import-independent-review.md).
A23 overall remains Partial;see the [current blocker rollup](a23-blocker-repairs.md)
for remaining C01 observations and affected browser acceptance. UI01's later deferral is separately reviewed.
No GUI/account/provider/clipboard/publish action authorized by this local repair.

## TODO

- [x] Add actual import-handler→real crypto→storage-reader→cooking regression.
- [x] Demonstrate old source fails the meaningful regression before the repair.
- [x] Wrap Full Access jar map in expected own-profile envelope; legacy unchanged.
- [x] Selected/full unit, packaging49, validation/package/ZIP/syntax checks.
- [x] Record source/artifact hashes and update guide/known-limit/tooling records.
- [x] Independent review Passed; actual B01/candidate/restore acceptance stays Pending; overall A23 Partial.

## Failure and narrow repair

The old actual wizard successfully decrypted a synthetic Full Access bundle, wrote
the raw jar map to `profile:NAME` and called completion. The real
[reader](../../../chrome-extension/src/adapters/infrastructure/chrome_storage_adapter.js)
L91–93 reads `profile.secrets[index]`; the successful import therefore left later
cooking failing with `Secret #1 not found`. This is implementation FA01, distinct
from documentary/tooling findings and UI01's unresolved intended behavior.

[confirmImport](../../../chrome-extension/src/options/options-import-wizard.js)L167–175
now calls the focused [writer](../../../chrome-extension/src/options/import-profile-writer.js)L4–34.
Only Full Access changes its encrypted payload to
`{secrets:state.derivedSecrets,settings:{}}`. It remains an own profile; original
Spices retain exact values and Menu assignment behavior. The legacy branch keeps
its `{secrets,isShared:true}` payload, shared routing and metadata. The extraction
retains existing async state-read boundaries, including the existing legacy initial
encryption; no import-lifetime/overwrite behavior is repaired here.

The importer was227lines. Extracting this existing storage block leaves196lines;
writer35, regression72 and fixture42lines. All touched/new code stays below200lines;
no unrelated UI/render refactor or production-only test hook.

## Actual regression and pre-fix evidence

[Regression](../../../chrome-extension/test/unit/full-access-import.spec.mjs) uses
[fixture](../../../chrome-extension/test/unit/import-wizard-fixture.mjs) to load
actual Options HTML, call `openImportWizard`, paste/decrypt/confirm handlers and
then production `ChromeStorageAdapter`/`GeneratePassword`. Genuine Node WebCrypto
performs production sharing PBKDF2/AES and profile AES; only Chrome storage is an
in-memory boundary. Synthetic public Spices/Menu/password only; no real account.

Before changing production source, ran from `chrome-extension`:

```sh
pnpm exec vitest run test/unit/full-access-import.spec.mjs -t 'Full Access with a Menu'
```

2026-10-26 14:28:27 Asia/Saigon (07:28:27 UTC):exit1,1failed/3notselected,151ms test/782ms total.
Failure occurred after the actual success callback at real cooking:
`GeneratePassword.execute`, `Secret #1 not found`. Old importer SHA256:
`cd0fbf3e06f6f352c17ac06c84c588f2ceef500f558c7dd9929de56b8d50829e`.
No existing/Golden test was edited to obtain this failure or the subsequent pass.

Four added cases cover Full Access with Menu, without Menu, wrong sharing password
and legacy import. Assertions include exact `Basic*r4nd0m` Dish and Tag invariance,
all five jar values including Unicode, encrypted envelope, unchanged unrelated
profile/default/assignments, refusal to write after wrong password and legacy
shared envelope/metadata/routing. No fake success-only restore assertion.

## Verification

Runtime:Node24.18.0, pnpm10.24.0 via
`export PATH="/home/vantt/.nvm/versions/node/v24.18.0/bin:$PATH"`.
Commands below run from `chrome-extension`, except repository diff checks.

| Check | Actual result |
|---|---|
| `pnpm exec vitest run test/unit/full-access-import.spec.mjs` |exit0,4/4 Passed,397ms tests/1.10s total after repair. |
| `pnpm test:unit` |exit0,151/151 Passed across21files,3.52s; all147existing plus4added cases. Expected invalid-Tag diagnostics retained. |
| `pnpm test:package` |exit0,49/49 actual filesystem/ZIP fixtures Passed,27.26s; inventory counts updated74. |
| `pnpm validate` |exit0,74reviewed shipped files; import/resources/JSON/syntax/WASM/notices checks. Known external Google Fonts CSS recorded, no network execution. |
| `pnpm package` |exit0,74files,1,103,161bytes; development ZIP/inventory/SHA created in ignored build directory. |
| `pnpm validate:zip build/passchef-extension-2.3.0-development.zip` |exit0,74exact root entries/current source bytes and matching hash. |
| `node --check` importer/writer/new spec/new fixture/packaging-test |exit0 for all5; each file below200lines. |
| `git diff --check`; existing test/manifest/WASM/QR preservation checks |exit0; Golden and runtime assets unchanged. New tests are additions. |
| Local Markdown target/line-anchor checks |All links resolve and referenced source lines checked; no remote link/network or browser acceptance inferred. |

The package suite reran once to retain a fresh bounded result after an earlier
completed session's output became unavailable across context recovery. Unit and
source/package/ZIP checks were not repeated without a source change.

## Exact edited scope —15files

| Files | Change |
|---|---|
| `chrome-extension/src/options/options-import-wizard.js`; new `src/options/import-profile-writer.js` |Focused writer extraction and Full Access envelope repair. |
| New `chrome-extension/test/unit/full-access-import.spec.mjs`; new `test/unit/import-wizard-fixture.mjs` |Four actual-handler/real-crypto/reader regressions with synthetic data. |
| `chrome-extension/scripts/package-contract.json`; `scripts/packaging-test.mjs` |Register new runtime module; two count assertions73→74. No packaging rule weakened. |
| `docs/commercialization/operations/chef-guide.md`; `store-reviewer-guide.md`; `release-notes-template.md`; `docs/commercialization/README.md` |Dated local repair, existing malformed imports not migrated, browser/candidate/upgrade recovery still Pending. |
| `docs/project-changelog.md`; `docs/development-roadmap.md` |Actual selected repair and remaining acceptance. |
| `reports/a09-chef-reviewer-guides.md`; `reports/a06-packaging-validation.md`; this report |Preserve historical failure; A09 dated amendment; point A06 artifact links to frozen original before new source packaging. |

All other existing WIP preserved. No plan/catalog registry closure in this delivery.

## Source and development artifact

SHA256 of repaired importer:
`5c6838d43830b3cb03e77e467d09f805626dc456cfa53075ab6aa315e926145f`.
New writer:
`14dac104f68cd030f2c9bef11d7be9b81aa8c5b0eaf8a385effe2eab3c88c7d1`.
Package contract:
`8f8d30cd92c7b994f1742f72c611a7aad955ec064f58d9a75c51381d36cce9ce`.
Golden `generate_password.spec.mjs` unchanged:
`5f9faa1e2bb5e3d38151250ffaa961a94efba459f41786f5ae90402ab07aae26`.

- [Preserved FA01 development ZIP](../../../chrome-extension/build/a23-fa01-frozen/passchef-extension-2.3.0-development.zip):
  `b4986a20b48da3b7a519c09b02ed19ebd409ace8aeb50781cf61b7fbd1fac4de`.
- [74-file inventory](../../../chrome-extension/build/a23-fa01-frozen/passchef-extension-2.3.0-development-inventory.json)
  and [SHA sidecar](../../../chrome-extension/build/a23-fa01-frozen/passchef-extension-2.3.0-development.zip.sha256).
- [Frozen A06/A07 ZIP](../../../chrome-extension/build/a06-frozen/passchef-extension-2.3.0-development.zip)
  remains73files/1,102,759bytes/hash
  `6c6fc80055c61d49cba404694fa8691a1c41316bccc2a14b491e930995fc0fe9`.

This is changed development source/artifact, not an A13 release candidate. Genuine
sharing/profile crypto was exercised locally; browser-loaded WASM, actual Chrome
Sync, reload/upgrade/import recovery, Google Menu interaction and runtime clipboard
were not run. Full headed/E2E suite is gated; no GUI/account/publish/push/ADB/Gradle.

## Unresolved

Dated artifact amendment2026-10-26:before the later owner-approved UI01 deferral
rebuilt the ordinary development ZIP, this FA01 ZIP/inventory/SHA were preserved
byte-exact under `build/a23-fa01-frozen/`. Original hashes/results above remain
historical; [UI01 report](a23-display-defaults-deferral.md) owns subsequent bytes.

Later [B01 Run01](b01-run-01.md) and [independent review](b01-run-01-independent-review.md)
verify selected new-import→Cook and UI01 Settings assertions. Final-candidate/full-suite/
extracted-ZIP and B08 upgrade/restore acceptance;remaining OWN01/C01 observations;
C02/A13 stable release boundary/candidate. No existing malformed-profile migration,
overwrite UX or broader import-lifetime refactor is included.
