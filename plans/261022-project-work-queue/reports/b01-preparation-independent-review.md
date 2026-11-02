# B01 — independent selected-regression preparation review

Date:2026-10-26, Asia/Saigon. Reviewer:independent agent,not spec author.
Scope:[three-file delivery/protocol](b01-headed-regression.md):two new Playwright
specs plus report. Verdict:**Passed for preparation only**. All3browser cases
remain **NotRun**;B01 remains Pending until an authorized actual runtime.

## Source and assertion review

- FA01 mapped/unmapped Menu cases use genuine setup and actual Options editor,
  export JSON,sharing password,decrypt/import and reload. All5synthetic Spices,
  including Unicode,are checked through the imported profile editor. Original
  ciphertext/salt/default and mapping preservation are asserted. Imported own
  record exists without creating a legacy shared record.
- Builder uses actual popup HTML in a tab and the actual worker/Cook path. Mapped
  Menu enforces imported profile;unmapped case selects it explicitly. Literal
  independent `Basic*r4nd0m` expectation and Tagged/untagged Recipe assertions do
  not call a production oracle or seed the imported record.
- UI01 checks real Settings EN→VI→EN reloads,removed card/controls,localized
  description,active language,actual quota,password-panel focus/cancel/reset and
  dismissed Reset confirmation. Exact Sync snapshot changes only by language;
  opaque saved defaults and presence of session key remain asserted.
- Existing helper's genuine-AES readiness marker is bounded fixture setup;it
  does not replace export/import/Builder/Settings logic. No direct writer/core
  override,clipboard Copy,provider navigation or native command is introduced.
- New files90/56lines;34literal ID references resolve to actual HTML/templates.
  Dynamic profile/group/Spice selectors match current source. Static review does
  not establish browser timing,layout,focus or actionability.

## Independent checks executed

Node24.18.0/pnpm10.24.0/Playwright1.57.0. Exact final collection command from
`chrome-extension/`:

```sh
pnpm exec playwright test test/e2e/full-access-import-regression.spec.ts test/e2e/settings-defaults-deferral.spec.ts --list --reporter=line --workers=1 --retries=0 --global-timeout=420000
```

Exit0:3tests/2files collected and TypeScript-transformed;fixture setup/browser
launch/assertions were not executed. No standalone semantic `tsc` claim.

Independent Python stdlib ZIP reader and SHA checks:

| Check | Actual result |
|---|---|
| Freeze record |SHA256`b6e0669241095f2947360b8c98dc563ae74930f499ad9ad90d468c67cf865d94` exact. |
| Source snapshot |74frozen files match current source,record hashes/sizes and every ZIP member byte. |
| Harness snapshot |50files match current bytes/record hashes/sizes:44old tests,2new specs,4config/package/lock/contract. |
| Development ZIP |CRC74entries and exact SHA`8e921455f31f4ba6d749242c94d3b9f1b17f964656fdca2fc1d7dc449d92f7b7`;inventory/source agree. |
| Runtime case states |All3NotRun. |
| Immutable baseline |All44pre-B01 test SHA entries pass;fixture/config bytes unchanged. |
| Existing tracked WIP |Binary diff equals pre-B01 snapshot byte-for-byte. |
| Report links |9local targets resolve. |
| Whitespace |`git diff --check` exit0. |

No unrelated unit/package suite replay after spec-only changes. Prior151unit/
49package evidence is retained with its original source/scope.

## Runtime gate and handoff review

Future command explicitly selects3cases,one worker,no retries,120s/120s/90s case
budgets and420s global budget. Installed Playwright reporter source confirms the
JSON output-name option;report assigns unique JSON/log/test-output paths and
requires absence before a fresh run. Hash preflight covers working source74,
harness50,old44tests,ZIP/inventory/SHA and record identity. Any changed byte requires
stop/review/new numbered freeze;current fixture still loads working `src`.

Owner checkpoint occurs before headed launch:approved desktop slot or separately
demonstrated isolation,exact run/synthetic scope,budget and cleanup agreed. A21
isolation remains Unverified. Browser revision must be recorded after authorized
launch;cached browser presence is not a fresh runtime result.

Pass requires3expected results,no failures/skips,exit0 and retained raw evidence.
Timeout/failure cannot be replaced with a message/core oracle or weakened selectors.
Cleanup must check owned browser child exit and exact temporary-profile removal;
host timeout/fixture warning alone is insufficient. No broad Chrome kill,owner
data/clipboard access or unrelated native/provider actions are authorized.

## Unresolved

Actual3case execution,owner desktop handoff or proven isolation,and fresh runtime/
cleanup evidence. Preparation does not close FA01/UI01/A23/B01 or replace final
full-suite,B04native/clipboard,toolbar/Google,B07extracted-ZIP,B08upgrade/Sync,
broader OWN01/C01 or C02/A13 gates. No implementation/registry edits by reviewer.

## Manager reconciliation —2026-10-26

Passed:10associated plan/report/current-doc files,333local targets,46unique task
IDs/46matching owner anchors;master77lines. B01 is Prepared with3runtime cases
NotRun,not Done;A23/C01 remain Partial and C02 Pending. Required desktop/proven
isolation and actual runtime/cleanup gates remain explicit. Whole-WIP equality
is correctly dated before manager-only documentation edits. Frozen74source/
50harness bytes and record hash remain unchanged;44baseline test SHA and final
`git diff --check` Passed. No browser/source/broad-test execution or registry edits.
