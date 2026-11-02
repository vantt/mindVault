# A23-UI01 — temporarily defer disconnected Display defaults

Date:2026-10-26, Asia/Saigon. Status:**LocallyVerified — owner-approved temporary deferral**;
[independent review Passed](a23-display-defaults-independent-review.md). Overall A23 remains Partial.
Source:HEAD039c76d + preserved WIP/A23-FA01. Manifest2.3.0 unchanged.

## Owner decision and TODO

Owner explicitly approved temporarily hiding the three disconnected global Display
defaults. Scope:remove their rendered card and orphan settings initialization/write
code; retain stored `displaySettings` values. This is not completion of inheritance/
reveal/Menu-detection behavior, blanket C01 closure, owner bug-list approval or C02
release/version choice. Existing per-profile hint/Builder detection/visible Dish
behavior stays unchanged. No GUI/browser/clipboard/device/publish/push/deploy.

- [x] Preserve current FA01 ZIP/inventory/SHA before creating new artifact.
- [x] Remove disconnected card/orphan logic and truthful EN/VI Settings description.
- [x] Ephemeral actual-HTML/JSDOM real init verifies remaining panels and retained data.
- [x] Unit/package/source/ZIP/syntax/preservation checks and artifact SHA.
- [x] Amend current guides/changelog/roadmap with precise local deferral scope.
- [x] Independent review Passed; manager updates the decision/rollup/track/catalog/master/index.

## Implementation and exact scope —12files

| Files | Change |
|---|---|
| [Options HTML](../../../chrome-extension/src/options/options.html) |Remove only Display Defaults card/three controls; truthful Settings description. |
| [Settings logic](../../../chrome-extension/src/options/options-settings-tab.js) |Delete orphan defaults constant/init/change handlers and init call;150lines. Language/quota/password/danger-zone logic retained. |
| [English](../../../chrome-extension/src/_locales/en/messages.json), [Vietnamese](../../../chrome-extension/src/_locales/vi/messages.json) |Change only `settingsDesc`; unused deferred-card translation keys retained for temporary deferral. |
| [Chef guide](../../../docs/commercialization/operations/chef-guide.md), [reviewer guide](../../../docs/commercialization/operations/store-reviewer-guide.md), [release notes](../../../docs/commercialization/operations/release-notes-template.md) |Describe owner-approved hidden controls/retained data and deferred behavior rather than visible disconnected controls. |
| [Changelog](../../../docs/project-changelog.md), [roadmap](../../../docs/development-roadmap.md) |Actual scoped source change and pending runtime/owner/release boundaries. |
| [FA01 report](a23-full-access-import-repair.md), [A10 protocol](a10-sync-upgrade-protocol.md), this report |Dated artifact amendments preserve old FA01 evidence/hash and point its links to frozen artifact; new UI01 evidence here. |

Rendered IDs `disp-pepper-hint`, `disp-reveal-password`, `disp-auto-detect-sheet`
are absent. No handler now writes their global data. Existing `displaySettings`
records remain untouched, including unknown fields; quota may still read/count
them through the normal all-metadata storage query. No clear/migration/delete.
Description now says “Manage your Cookbook master password, language, and storage.”
with corresponding Vietnamese text; no new inheritance or all-profile promise.

No CSS/permission/manifest/profile/Builder/popup/crypto/runtime-policy change.
The owner's [C01 scope decision](c01-owner-blocker-decisions.md) is recorded by root;
this delivery does not mark C01/OWN01/C02 or A23 overall complete.

## Meaningful ephemeral verification

No permanent test added for this low-impact UI deletion; existing/Golden tests
unchanged. `/tmp/passchef-ui01-settings-smoke.mjs` loads actual Options HTML in
JSDOM and imports real `initSettingsTab`. In-memory Chrome storage is seeded with
existing defaults, opposite boolean values and an unknown field. For both EN/VI:

- Assert all three controls/card absent, real init completes without orphan access.
- Assert no initial write/delete or targeted defaults read; seeded object exact.
- Check active language,80% quota label/warning and actual password expand/focus/
  cancel/reset behavior. Click Reset only with confirmation returning false;
  no storage clear is called. Switch language and observe the sole expected
  language write/reload, still no defaults mutation/delete.
- Parse actual locale JSON; Settings description lacks defaults/inheritance claims.

This runs actual Settings code with an isolated storage/DOM boundary; it is not
visual/browser/Chrome Sync acceptance. Script and logs are temporary local evidence,
not shipping source or an added regression suite.

## Actual commands and results

Runtime:Node24.18.0/pnpm10.24.0; PATH prefix
`/home/vantt/.nvm/versions/node/v24.18.0/bin`. pnpm commands from `chrome-extension`.

| Check | Result |
|---|---|
| `node /tmp/passchef-ui01-settings-smoke.mjs` |exit0;EN/VI real-init cases Passed as above. |
| `pnpm test:unit` |exit0;151/151,21files,3.89s; no changed assertions/new permanent tests. |
| `pnpm test:package` |exit0;49/49filesystem/ZIP cases,30.48s; no skips/failures. |
| `pnpm validate` |exit0;74reviewed resources/imports/JSON/JS/WASM/notices; external font-data record unchanged. |
| `pnpm package` |exit0;74files/1,102,447bytes; development ZIP/inventory/SHA written after freezing old artifact. |
| `pnpm validate:zip build/passchef-extension-2.3.0-development.zip` |exit0;74exact entries/source bytes,matching new SHA. |
| `node --check src/options/options-settings-tab.js` |exit0;150lines<200. HTML template remains existing large file. |
| Python zipfile CRC/source comparison |New ZIP74entries+CRC Passed;exact4expected runtime paths differ from frozen FA01,other70files byte-identical. |
| Existing test SHA snapshot |`sha256sum --quiet -c /tmp/passchef-ui01-20261026/initial-tests.sha256` exit0;44existing test files retained,including FA01 additions. |
| Preservation |Tracked test/manifest/popup/security/offscreen/profile-editor/Playwright diffs empty;FA01 writer/importer retained. Stored defaults preserved in actual-init fixture. |
| Local JSON/link/source checks; `git diff --check` |Locale JSON valid,deferred translations retained;50local targets resolve;three controls/orphan logic absent;diff exit0. |

Unit log:`/tmp/passchef-ui01-unit.log`;package log:`/tmp/passchef-ui01-package.log`.
No headed `pnpm test`, GUI/actual Settings layout/focus/clipboard/Google/Sync,
phone/ADB/Gradle/account/push/deploy/publication. Runtime B01/candidate remains Pending.

## Artifact identities and preservation

New [development ZIP](../../../chrome-extension/build/passchef-extension-2.3.0-development.zip),
[inventory](../../../chrome-extension/build/passchef-extension-2.3.0-development-inventory.json)
and [SHA sidecar](../../../chrome-extension/build/passchef-extension-2.3.0-development.zip.sha256):
`8e921455f31f4ba6d749242c94d3b9f1b17f964656fdca2fc1d7dc449d92f7b7`.
This is not a stable A13 candidate;manifest2.3.0/package1.0.0 unchanged.

Before overwrite, ZIP/inventory/SHA were copied byte-exact to ignored
[`build/a23-fa01-frozen/`](../../../chrome-extension/build/a23-fa01-frozen/)
after asserting old SHA `b4986a20b48da3b7a519c09b02ed19ebd409ace8aeb50781cf61b7fbd1fac4de`
(74files/1,103,161bytes). Earlier ignored
[`build/a06-frozen/`](../../../chrome-extension/build/a06-frozen/) remains
`6c6fc80055c61d49cba404694fa8691a1c41316bccc2a14b491e930995fc0fe9`
(73files/1,102,759bytes). Old evidence/results were not relabelled for new bytes.

New source SHA256:

```text
01148bfe0836bd7dcb8afced50ee88c5bd6c4e9e108efbf7419bc0c9e369ee8c  options/options-settings-tab.js
0afb0d937a65113ab965aef68b50bde06efa72699e48d9a9147f5473330a651e  options/options.html
ad140cd45f906dd54f71c02d2da69f6fd194c57b5d61cdfceebb2e78e4c9b67d  _locales/en/messages.json
4e5310c463e0304bc4762bd5df342549657af82bb8014d92378a00ae99dc4b4f  _locales/vi/messages.json
```

## Later selected B01 browser follow-up —2026-10-26

[Owner-authorized Run01](b01-run-01.md) and [independent review](b01-run-01-independent-review.md)
verify selected EN→VI→EN Settings,retained preferences and working panels on this
unchanged source. The local preparation checks above retain their original scope;
full-candidate/full-suite/upgrade acceptance remains separate and Pending.

## Unresolved

Final-candidate/full-suite acceptance, owner remaining bug symptoms/C01 scope,
C02 version/channel/support boundary and final release/runtime gates. No stored
defaults migration/deletion or security-sensitive semantics implemented.
