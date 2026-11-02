# B07 — extracted2.3.1 ZIP full-suite evidence

Date:2026-10-27, Asia/Saigon. Status:**full extracted-ZIP suite and separate Linux WASM fault smoke Passed**;
B07 remains Partial for final-candidate/required-platform requirements. Root operated the authorized
desktop;report author ran no GUI/tests. [Independent review](b07-run-01-independent-review.md)
contains independent actual runtime Passed for the full suite and separate fault smoke.

## TODO

- [x] Parse full-run unit/browser/raw exit and immutable ZIP/source/harness identity.
- [x] Record owned browser/profile/clipboard cleanup and evidence limits.
- [x] Link independent review;keep fault smoke/Windows/116/candidate gates separate.
- [x] Dated fault Run01 parsed separately:baseline/missing/corrupt/restored Passed;no addition to original28case count.

## Authorized scope and exact bytes

Owner said “làm luôn đi” after the candidate/full-verification proposal. Root proceeded
on the scoped FA01/UI01 provisional artifact;an answer declaring no other bugs is not
a prerequisite to testing. OWN01/C01 remains unconfirmed for broader release conclusions.
This run used the shared WSLg desktop with synthetic profiles and one real clipboard
case;filesystem separation is not A21 isolation or Windows support evidence.

[Preparation](../../../chrome-extension/build/b07-run-01/preparation.json) SHA256
`c26584305db3e1c752846e3a9fbda0ade0dfe40c83f3504f6611c258eb5ab266` binds
`/tmp/passchef-b07-dQiFTQ/workspace`:exact74runtime files extracted from held2.3.1
ZIP SHA`278b59df8ddef0085c9c064da11485695074419894c3f43b5077a10fc44f1e9e`;
46canonical tests/fixtures plus4original package/lock/Vitest/Playwright config files
are copied byte-exact. Installed node_modules is the explicit dependency symlink.
No source/config/fixture/assertion override or new package resolution.

Unchanged fixture uses `process.cwd()/src`,therefore browser and unit imports used
the **extracted ZIP bytes**,not canonical working source. Root's pre/postflight
confirms74source/50harness identities retained. Source base is HEAD039c76d plus
documented dirty WIP;this is held/provisional2.3.1 beta,not clean/stable release proof.

Node24.18.0/pnpm10.24.0/Playwright1.57.0;Linux Chrome for Testing143.0.7499.4,
executable SHA`2e61bc3fd990bd4d7b419ef6b6303c67aaed683e5b83b3b25e416f015f343209`.
Root preflight binary metadata and later matching owned-process samples support
that lane. No native Windows Stable156/116 or broad OS/hardware/performance verdict.

From the frozen workspace,root ran the original `vitest run && playwright test` script:

```sh
PLAYWRIGHT_JSON_OUTPUT_NAME=/home/vantt/projects/passMan/chrome-extension/build/b07-run-01/report.json pnpm test --workers=1 --retries=0 --reporter=line,json --global-timeout=1200000 --output=/home/vantt/projects/passMan/chrome-extension/build/b07-run-01/results
```

Flags append only to final Playwright command,verified by the separate harmless
forwarding probe. No selected-test filter/retry/assertion weakening;1200s browser cap
does not include prior Vitest time. Root retained combined output and native exit.

## Actual full-run results

| Phase | Result | Reported elapsed |
|---|---|---:|
| Canonical unit suite |151/151assertions,21/21files Passed |5.98s |
| Full headed E2E from extracted ZIP |28/28cases in16spec files Passed,one worker |320.237s |
| Combined script |exit0;browser0skips/unexpected/flaky/errors,every case exactly one passed result/retry0 |No invented overall wall-time |

Playwright raw start06:39:36.086UTC/13:39:36.086Asia/Saigon;completion record
06:44:47.462UTC. Timestamps and reported phase durations are retained independently;
no timing-derived Argon2/performance claim. Unit expected invalid-Tag diagnostics/
terminal environment warnings remain in raw log;no test failure hidden.

This includes genuine Argon2/worker/setup/unlock/rotation/profile lifetime,Builder/
Tag/FA01 import/UI01 Settings,QR/Demo cases,three actual Chrome action-popover cases
and one actual system clipboard compare/clear case. Routed synthetic Google DOM/
programmatic messages are not live Google or native OS shortcut acceptance.

## Cleanup and process observations

[Suite postflight](../../../chrome-extension/build/b07-run-01/suite-postflight.json)
identifies28unique owned `passchef-e2e-` profiles;all absent,no remaining process
references and no fixture cleanup warnings. It records74workspace source/50harness
hashes unchanged. Three retained process samples show0/0/9matching entries:initial
NUL-argv matcher was corrected for WSL's space-joined cmdline;the later sample
matches exact owned profile and expected executable. This is sampled ownership,
not continuous process history or a blanket browser-kill claim.

Separate [clipboard postflight](../../../chrome-extension/build/b07-run-01/clipboard-cleanup.json)
used one extra owned profile for1.444s:classification`Empty`,wrote=false,context
closed and profile absent. No owner clipboard text was logged or blindly overwritten.
The suite's own replacement/clear assertions passed;this does not promise removal
of OS/keyboard history or external copies. Desktop/resource release is root-owned;
this report requests no further GUI work.

## Raw evidence identities

| File under ignored build/b07-run-01 | SHA256 |
|---|---|
| [Preflight](../../../chrome-extension/build/b07-run-01/preflight.json) |`bf2ed900d89362b7ca56412db45b1c98ae095056461b0cc64493121d1a2b4919` |
| [Playwright report](../../../chrome-extension/build/b07-run-01/report.json) |`27a7638e1b36ef028ae4ccdd445b2c604fbe3bab9e88998ef80ba5a882d6ee0b` |
| [Combined run log](../../../chrome-extension/build/b07-run-01/run.log) |`8f39fba6a821c1fa1c97b98f28d310bb9069a20700b67e29c5382a8b1b99406e` |
| [Completion](../../../chrome-extension/build/b07-run-01/completion.json) |`1f352b15fc123c0956aad5e7dd699917a7771516ebb9c4a948ddcaae45518341` |
| [Suite postflight](../../../chrome-extension/build/b07-run-01/suite-postflight.json) |`2574e55fa1c3b17f3dca58b538f5c93240602875ee558516bc175d31cf916d30` |
| [Process samples](../../../chrome-extension/build/b07-run-01/process-observations.jsonl) |`f8861b5dc3619440c1d6a20e5a8b2d422af9abcabf65135ca1973224f3b1f377` |
| [Clipboard verification](../../../chrome-extension/build/b07-run-01/clipboard-cleanup.json) |`d6c9980005bf73cd2a73da4e7341f6a35016ab2b92b992e3e2a925387591e058` |

Report author directly parsed28nested JSON results/empty errors,unit log summary,
native exit,postflight/hashes and raw process/clipboard metadata. Read-only local
11targets/46test hashes/source74/harness50/ZIP CRC checks and `git diff --check` Passed;
no runtime or helper rerun. Preparation/frozen records retain their original NotRun
states;this dated report owns actual execution results.

## Dated execution followup —Linux WASM fault Run01

After the full suite,root separately ran the reviewed disposable fault probe:
**1/1test Passed248.666s**,exit0,0skips/unexpected/flaky/top-level errors and retry0.
This is a separate case,not a relabelling of the28full-suite cases or frozen prep.
Raw start06:51:39.653UTC;completion06:55:41.829UTC;reported duration retained separately.

All four stages passed on Linux CfT143.0.7499.4:baseline→missing WASM→corrupt WASM→
restored control. Same installed path/profile and extension ID throughout:
`acbbpioacgekbichpmmbonihnellccpo`. Genuine UI setup/Spice Save and literal
`Basic*r4nd0m` baseline/restored Cook passed;negative stages retained exact encrypted
Sync state,remained locked with no session,showed crypto-unavailable UI and no old Dish.
No injected vault/key/token,core oracle,weaker fallback or canonical-source mutation.
Source faults occurred only after context close in owned `/tmp/passchef-wasm-IlHowJ/src`.

Original/restored WASM hashes match
`0c2149886c13e4eae4a6ca25ee71d47423c5c8740a874cf04ff816d1b2c901d7`.
Postflight records lab absent and no live owned PIDs. Case allows production120s
fault timeout;248.666s is whole fault exercise,not normal Argon2 latency/performance.

| Fault raw file | SHA256 |
|---|---|
| [Playwright report](../../../chrome-extension/build/wasm-failclosed-01/run-01-report.json) |`bbab6cedbf73d5fd9a9ab08c7581915e9c2a13f24a9a41c1a99b957b27c242f8` |
| [Four-stage smoke record](../../../chrome-extension/build/wasm-failclosed-01/run-01-smoke.json) |`f1d2c3f8cb7f6b58a1aa358901cf478982e4aadc71282a1dc4741d0d017f6668` |
| [Completion](../../../chrome-extension/build/wasm-failclosed-01/run-01-completion.json) |`aff2710376fdea8f1d7798a5c26267785e2c7d88c62e1db0022a294ec91c1b5a` |
| [Postflight](../../../chrome-extension/build/wasm-failclosed-01/run-01-postflight.json) |`cfc63592529ccbb7ce941490725221642b77b30868994bce493a40b80a1ab1ca` |
| [Log](../../../chrome-extension/build/wasm-failclosed-01/run-01.log) |`8b2f7372c578a47e3518ec2a1cbb6d5f89723c1592260760d27210fed2d93122` |

Report author parsed raw result/stages/hashes and cleanup metadata only;root was
operator. Original preparation records remain NotRun snapshots. Native Windows
P0/support/116,B03metrics,upgrade/real Sync and final release remain separate.
The Linux fault/suite passes do not certify broad A13/A23/C01/release completion.

## Unresolved

Independent runtime review and final candidate/
OWN01/C01 disposition,Windows Stable/116,upgrade/real Sync and release acceptance.

## Final cleanup/current cross-platform followup

[Workspace cleanup](../../../chrome-extension/build/b07-run-01/workspace-cleanup.json) confirms
owned Linux/native roots removed with raw evidence retained;desktop released.
[Native Windows Stable P0](b03-native-stable-run-01.md) also Passed/reviewed separately;
Windows116/metrics/full native suite remain unaccepted. [Owner defers Sync slot](c03-sync-owner-readiness.md);
mandatory pre-publication acceptance unchanged.
