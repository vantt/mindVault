# B03 — native Windows Chrome Stable P0 execution

Date:2026-10-27, Asia/Saigon. **Run04 P0 Passed; independent review Passed**.
[Independent raw review](b03-native-stable-independent-review.md).
B03 remains Partial:Windows116, full platform matrix and P1–P4 measurements remain NotRun.

## Completed scope

- [x] Bind exact held2.3.1 beta ZIP/source74 and pinned native Playwright inputs.
- [x] Run genuine native Stable loader/setup/Spice Save/Settings Lock/unlock/Cook.
- [x] Preserve all three earlier driver failures and revised helpers separately.
- [x] Verify actual native process termination, source/test preservation and owned temp cleanup.
- [x] Independent raw review Passed; current plan updated.

Root operated the browser under owner's “làm luôn đi” continuation. No owner
profile/account/Google/Sync/clipboard/registry-write/policy/publication action.
Private native profile and modern CDP Extensions.loadUnpacked loaded the exact
ZIP extracted bytes. Computer Use UI automation was not used.

## Identity and actual result

Windows11 Pro N build26200/x64; installed Google Chrome156.0.8078.48.
Valid Google signer and updater ap=x64-stable-statsdef_1 bind Stable channel;
actual CDP Browser.getVersion reports Chrome/156.0.8078.48.
Native Windows Node22.14.0 and local copies of installed Playwright/core1.57.0;
no dependency resolution/download/install. Initial UNC module lookup failed through
Linux pnpm symlinks; copied pure-JS packages resolved successfully on Windows.

Held ZIP SHA256:278b59df8ddef0085c9c064da11485695074419894c3f43b5077a10fc44f1e9e.
Chrome executable SHA256 before/after:
480f275b63d1ea39113cbc8344ac395578acc25b432bd39af5fbc77005179aca.
Run04 helper SHA256:95c07b840cbeff7f2714378182b4118ea5ad0750b3b212df78e66149bedfbbd6.

[Run04 raw](../../../chrome-extension/build/b03-native-01/run-04.json) and
[completion](../../../chrome-extension/build/b03-native-01/run-04-completion.json):
**Passed,exit0,5.315s**, five stages:loader; genuine Options setup/backup/editor Save;
actual Settings/visible Lock; Lock→authoritative locked/no-session→reopened Options
UI unlock with encrypted Cookbook unchanged; literal Builder Dish Basic*r4nd0m.
Post-lock:one reload event,unlock visible,dashboard hidden,no lock/crypto error,
pageErrors empty. Popup HTML in a tab is not actual toolbar/native shortcut proof.
903.7816ms is one descriptive end-to-end P0 unlock observation,not a KDF benchmark,
latency distribution,SLO,memory result or P1/P2 completion.

## Preserved failed driver exercises

| Run | Actual result and supported cause |
|---|---|
| [01](../../../chrome-extension/build/b03-native-01/run-01.json) |Failed:parallel load wait timed out while Lock was hidden in Profiles; no product lock failure established. |
| [02](../../../chrome-extension/build/b03-native-01/run-02.json) |Failed:explicit click log says Lock not visible; missing Settings navigation. |
| [03](../../../chrome-extension/build/b03-native-01/run-03.json) |Failed:actual lock/session absence/reload occurred; driver sampled both sections hidden before async UI readiness. |

Run04 opens real Settings,waits Lock visible and locked UI ready before the original
security/no-error/retained-state/unlock/literal-Dish assertions. No application,
Golden,fixture,session or encrypted-vault injection/change. These were separate
numbered exercises after reviewed driver changes,not hidden automatic retries.

## Cleanup and limits

[Native process postflight](../../../chrome-extension/build/b03-native-01/run-04-process-postflight.json)
records no owned Chrome or remaining sampled native PIDs.
[Identity postflight](../../../chrome-extension/build/b03-native-01/identity-postflight.json)
verifies source74/harness50 including46canonical tests and copied dependencies177+335files unchanged.
[Cleanup](../../../chrome-extension/build/b03-native-01/cleanup.json):only recorded WinTemp
root and Linux extracted-ZIP workspace removed;both absent,all four raw runs retained;
desktop returned. Reviewer checked copies before deletion and absence afterwards.

[Current protocol/history](b03-browser-measurements.md),
[Linux full-suite/fault evidence](b07-zip-suite.md) and
[owner Sync scheduling](c03-sync-owner-readiness.md) retain their separate scopes.
Official protocol references:[Extensions domain](https://raw.githubusercontent.com/ChromeDevTools/devtools-protocol/master/pdl/domains/Extensions.pdl),
[Chrome extension testing guidance](https://developer.chrome.com/docs/devtools/agents/extensions).

Unresolved:Windows116/P1–P4/full native suite,upgrade/real Sync and final candidate/
publication acceptance. Owner has a second computer but defers Sync to a later slot;
mandatory real two-device acceptance before publication remains unchanged.
