# B01 run-01 — independent runtime evidence review

Date:2026-10-26, Asia/Saigon. Reviewer:independent agent,not runtime operator.
Verdict:**Passed for3selected FA01/UI01 source-patch browser regressions**.
Parent:[preparation and exact assertions](b01-headed-regression.md),
[preparation review](b01-preparation-independent-review.md).
This review launches no browser and changes no implementation/harness/registry.

## Authorized scope and raw evidence

Owner explicitly handed over desktop after the3case scope,private synthetic
profiles,420s test cap plus preflight/cleanup were explained. Root alone launched
the selected headed command. [Preflight](../../../chrome-extension/build/b01-selected/run-01-preflight.json),
[raw JSON](../../../chrome-extension/build/b01-selected/run-01-report.json),
[terminal log](../../../chrome-extension/build/b01-selected/run-01.log),
[completion](../../../chrome-extension/build/b01-selected/run-01-completion.json),
[cleanup](../../../chrome-extension/build/b01-selected/run-01-cleanup.json) and
[process sample](../../../chrome-extension/build/b01-selected/run-01-process-observations.jsonl)
were independently read and cross-checked.

JSON reports Playwright1.57.0,one actual worker,retries0,global420000ms,
exactly3expected cases,0skipped/unexpected/flaky,empty top-level errors.
Each case has exactly one result,`passed`,retry0 and empty errors:

| Actual selected case | Duration | Independent evidence verdict |
|---|---:|---|
| FA01 mapped Menu export/import→Builder Cook |14.402s |Passed. |
| FA01 no-Menu export/import→Builder Cook |13.627s |Passed. |
| UI01 EN→VI→EN Settings/preferences/panels |4.354s |Passed. |

Suite start10:15:44.562UTC/17:15:44.562Asia/Saigon,total33.046s;completion exit0
at10:16:17.655UTC. Raw report SHA256 matches cleanup record:
`8fc880577f194a7bb95d57f3fed56451212297f276f6168802effc2d0de9fb9e`.
The immutable reviewed specs bind these results to genuine UI export/import,
all5Spices,own-profile storage/mapping/ciphertext preservation and literal Builder
Dish,plus genuine Settings reload/control absence/quota/master/Reset cancellation.
No assertion weakening or production oracle was introduced.

## Source, artifact and harness identities

Independent post-run checks compare current bytes to the immutable record and
frozen copies:74runtime source files and50harness/input files match every recorded
size/SHA256. All44pre-B01 test hashes remain unchanged. Record SHA256 remains
`b6e0669241095f2947360b8c98dc563ae74930f499ad9ad90d468c67cf865d94`.

Separate stdlib ZIP reader confirms CRC,74members and every current source byte;
development artifact/inventory/record/preflight agree on SHA256
`8e921455f31f4ba6d749242c94d3b9f1b17f964656fdca2fc1d7dc449d92f7b7`.
The actual fixture loaded working`chrome-extension/src`,not an extracted ZIP.
Frozen preparation case states remain NotRun as historical evidence;this dated
run provides the actual results without rewriting that freeze.

Preflight identifies cached Chromium1200 executable version
`Google Chrome for Testing143.0.7499.4`,SHA256
`2e61bc3fd990bd4d7b419ef6b6303c67aaed683e5b83b3b25e416f015f343209`.
Reviewer independently rehashed those executable bytes. Unchanged fixture/default
launcher selects that cached browser;raw run does not retain a live PID/executable
sample. No continuous process-identity claim is made.

Root disclosed an initial checksum invocation from the wrong directory before
launch;the correct repo-root verification then passed. Reviewer independently
repeated the original44-entry checksum successfully. This is a corrected preflight
command error,not a failed browser case. Final`git diff --check` Passed.

## Cleanup and resource boundary

Log/JSON identify exactly3unique synthetic`/tmp/passchef-e2e-` profiles. Cleanup
record reports all3absent,no live process arguments referencing those exact
profiles and no fixture cleanup warning. Reviewer independently checked all3paths
absent and read current `/proc/*/cmdline`:no exact owned-profile argument remained.
Successful fixture teardown/context-close completion and these postconditions
support cleanup;root released desktop at17:17Asia/Saigon.

The single PID sample landed between fast cases and observed no browser PID.
This limitation is explicit:the evidence is teardown/postcondition verification,
not continuous native-process observation. Host termination was not used as proof.
No further GUI launch,clipboard/native shortcut,Google/provider/account,phone,
remote CI,push/deployment or publication action is part of this review.

## Unresolved

Only these3source-patch cases Passed. B01 final candidate/full suite,actual toolbar,
B04native/clipboard,B07extracted ZIP,B08upgrade/Sync and A13/release acceptance
remain separate. A21 isolation remains Unverified;future desktop use requires a
new applicable resource handoff. Broader OWN01/C01 observations and C02 remain open;
these results alone do not mark overall A23/C01/B01/release Done.

## Final runtime-document claims audit —2026-10-26

Passed:[runtime report](b01-run-01.md) and preparation report's dated execution
follow-up agree with raw3case results,durations,exit/config/source/artifact hashes
and cleanup limits. All6published raw-file SHA values match;30local targets across
the2delivery reports and this review resolve. Root's
[postflight](../../../chrome-extension/build/b01-selected/run-01-postflight.json)
retains matching74source/50harness/record and6raw identities.

Preparation retains its historical NotRun freeze;its current unresolved wording
now identifies future final-candidate/full-suite handoff/preflight rather than
asking to repeat the3Passed cases. Root corrected that stale fragment and reviewer
rechecked it. Final`git diff --check` Passed;no unresolved report finding. Root may
record B01 Partial(selected3Passed,final suite Pending),without opening remaining
OWN01/C01/C02/A13/B07/B08 or future desktop gates.

## Runtime manager reconciliation —2026-10-26

Passed:16current/manager documents,390local targets,46unique catalog IDs with
46matching owner anchors;master77lines. B01 is Partial(selected3Passed,final suite
Pending),A23/C01 remain Partial and C02 Pending. Root corrected stale A23/C01
registry and roadmap wording so scoped import→Cook/Settings passes are retained
while final-candidate/full-suite/upgrade and OWN01 gates remain open. Current
guides/release-note template and dated A23 follow-ups preserve those boundaries.
Source74/harness50/record and44baseline test SHA remain unchanged;final
`git diff --check` Passed. No new GUI/source execution or registry edit by reviewer.
