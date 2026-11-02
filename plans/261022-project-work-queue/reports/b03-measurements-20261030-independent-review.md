# B03 — independent native measurements review

Date:2026-10-30, Asia/Saigon. Verdict:**Passed;selected Windows Stable/CfT116 B03 measurement task complete**,including P0/P1/P2 functional scope,both descriptive P3 methods,both P4 cells and final cleanup. Release completion not inferred.

## Scope and actual checks

Reviewed six ignored helpers,root wrapper,frozen inputs/native copies,actual JSON/completions and ten raw memory JSONLs under [measurement evidence](../../../chrome-extension/build/b03-measurements-20261030/). Reviewer performed read-only file/arithmetic/hash checks;no browser,Windows query,download,test replay or source/manager mutation.

- Genuine Options setup/editor Save→Settings visible Lock→authoritative locked/no session→await Unlock/dashboard readiness→real unlock→literal synthetic Dish. Prior Run01–03 driver mistakes avoided.
- Original startup finding resolved:DOM navigation and actual locked-ready endpoints recorded separately. P1 means process-cold;no OS-cache flushing/disk-cold claim.
- P0 frozen helper trio and two configs plus six v2 helpers/six configs:48 explicit archive/native-copy/hash checks Passed. Wrapper has exclusive evidence creation,selected mode/lane,600s measurement/360s fault budget plus30s host watchdog. Timeout alone is not child-cleanup proof.
- Both P0 records Passed/exit0/contextClosed/pageErrors empty/executable hashes unchanged. Actual CDP versions:explicitly branded **CfT116.0.5845.96** and installed **Stable156.0.8078.76** on Windows. Portable CfT is not branded Stable116.
- All four measurement blocks Passed/exit0/no host timeout/no page errors/binary unchanged. Each cold block has10samples,11distinct native root PIDs including setup,and11 native ESRCH exit proofs;one extension ID throughout. Each warm block has2retained warmups+20measured samples,one root PID and exit proof.
- Canonical74source hashes,46test hashes against Oct30 snapshot and held278b ZIP SHA verified unchanged. No weakening of Argon2 parameters/storage/permissions/Golden tests.

## Independently recomputed timings

All values milliseconds;one native monotonic interval per UI unlock. Setup/launch,IPC,WASM/AES and UI work are not pure Argon2 timing. Functional Pass is distinct from performance acceptance;no invented SLO/p95.

| Browser / phase | N | Min | Median | Mean | Max |
|---|---:|---:|---:|---:|---:|
| CfT116 process-cold unlock |10|887.724|892.479|895.489|915.240|
| CfT116 warm-browser unlock |20|865.882|881.118|880.302|890.852|
| Stable156 process-cold unlock |10|883.453|889.756|891.867|907.921|
| Stable156 warm-browser unlock |20|875.336|893.767|894.360|912.963|
| CfT116 launch→locked-ready |10|884.400|954.362|1032.402|1653.537|
| Stable156 launch→locked-ready |10|1015.872|1095.691|1108.188|1242.250|

CfT116 labels all22warm iterations **worker-unverified**;Stable labels all22 **fresh-worker-observed**,supported by captured target lifecycle. Neither is retained-worker/warm-WASM evidence. Warmups are preserved and excluded from20sample statistics. Host shell/native UTC clocks are separate;some shell completion timestamps precede native-ended timestamps slightly. No duration calculated across those clocks.

## P3 raw coverage and limits

Recomputed each JSONL's interval membership,root PID/creation identity,baseline,during maxima/deltas and actual gaps. Both cells have five intervals with a before row,**one complete during row**,and an after row;root association and summary arithmetic match. No unavailable-process row in CfT116 observations. Sampling uses actual owned root/descendants;group totals include browser/renderers/GPU and may double-count shared working pages.

| Browser | During group working-set range MiB | During group private-byte range MiB | Working-set delta range MiB | Private-byte delta range MiB | Per-interval maximum gap range ms |
|---|---:|---:|---:|---:|---:|
| CfT116 |607.105–662.703|447.344–515.777|65.121–78.527|67.145–74.352|715.816–817.085|
| Stable156 |796.711–887.742|502.734–598.098|−6.422–12.641|−7.492–−1.188|766.264–879.717|

200ms is requested sleep after CIM acquisition,not achieved sample frequency. Sparse observations cannot establish true peak,pure extension/WASM memory or universal64MiB process usage. Negative deltas are preserved:background process reclamation/group changes can outweigh transient allocation. No cross-browser benchmark superiority claim;first five measured intervals include observer overhead,raw samples retained.

## Evidence identity

| Raw record | SHA256 |
|---|---|
| p0-cft116-run01.json |`c665ae6459f76fb9bbf39a77d072895c727c207d775fb178700bf4b16839e006`|
| p0-stable-run01.json |`a2bcb5fe66362ceb188b564eb03535ae67df3f62c5da3808fab18a9650e9b37a`|
| cold-cft116-run01.json |`59dd673a2441d5abb85d863030f086f366ffee48ed58b80af33973e5e96af8f7`|
| warm-cft116-run01.json |`d161ef22c51477c95a1c4767c1653efdbe09cdec4b6fba46bd32594f2817f928`|
| cold-stable-run01.json |`1d2279afbea92103b3db002b4a9435998f051814984371e5159b133bfa1ef211`|
| warm-stable-run01.json |`b40a475ac852780b4359078fc1ca475432cd0147afab035f51393beb3776e570`|
| measurement-freeze.json |`e6db80ecd9a17e3894c96190da8a553003db8bfc5076c5e7ba6bd8dbf76ab299`|

## CfT116 P4 actual followup

Read actual fault result/completion against frozen reviewed helper/input. **Passed**,exit0/no host timeout/four native ESRCH exits/no page errors/executable hash unchanged. All baseline/missing/corrupt/restored stages share installed path/profile/ID`fbaiicajjgnfkadncnlmmghbkhhgenab`;real synthetic nonempty Menu mapping was added through UI before baseline.

Missing/corrupt attempts take120140.910/120189.432ms,then explicit UI failure,authoritative locked/no session,hidden empty Dish and exact encrypted Sync equality. This follows the existing120s fail-closed timeout;no fast-error claim. Original WASM restored SHA`0c2149886c13e4eae4a6ca25ee71d47423c5c8740a874cf04ff816d1b2c901d7`;positive control unlock886.853ms and literal Cook pass. Raw SHA`9356c7b315d27fcaf66acf145f6260beec199074839b711e0f06d744a7112a7a`.

Stable156 P4 also independently **Passed**:four matching stages/same path/profile/ID,four native ESRCH exits,exit0/no host timeout/no page errors/binary unchanged. Missing/corrupt wait120229.153/120180.186ms,then explicit refusal/locked/no session/no Dish/exact Sync preservation. Restored control unlock904.561ms/literal Cook/original WASM hash Passed. Raw SHA`e3bb7b6193f5f010aa3d4ea146b09a938bc32cc27c22bb052ea64722ffc396f0`.

## Separate fast P3 preparation

Root selected a bounded observer improvement rather than relabel sparse measurements. Reviewed three new ignored files,not modifying earlier helpers/raw:

- `sample-memory-fast.ps1`:SHA`33c3601d7267d0d7e82f8327d0e66b8adcee86265d0d03e58ec698a08dbc4c74`.
- `memory-observer-fast.cjs`:SHA`9bcc8c31563bf59f47f118273b0eabb7c3048d96ce00c09ce93d9ad172de6258`.
- `measure-memory.cjs`:SHA`3b2c4a8168b977da36327d605d412a0f22a4454854125a69db38b6f7c798bd27`.

Prelaunch source review **Passed**:initial owned-profile/root-start validation,per-row root creation check,Toolhelp dynamic parent snapshot,actual per-PID working/private bytes,scheduled200ms-minus-work,raw gaps/degraded-quality flag. Actual genuine UI2warmups+5memory intervals,unchanged Sync,300s block/native PID exit.250ms flag is observer quality only. Root AST/compilation/canary Passed/native snapshot30.542ms/own-parent found/RemoteSigned unchanged. Twelve explicit helper/native-copy/config hash checks against fast freeze Passed;wrapper keeps exclusive evidence and300+30s budget. Runtime evidence review remains pending;no new Windows action by reviewer.

## Fast P3 actual followup

Both new root-operated blocks independently **Passed**,exit0/no host timeout/no page errors/binary unchanged/native root ESRCH exit. Each preserves two warmups plus five actual unlock observations and encrypted Sync equality;no repetition or rewrite of original20sample P2 results. Read all ten additional JSONLs,recomputed before/during/after coverage,root creation identity,during maxima/baseline deltas and actual cadence. Every interval complete;CfT116 has5/6/3/3/4during rows,Stable3each.

| Browser | Gap N | Gap min/median/mean/max ms | Group working-set MiB | Group private MiB | Working-set delta MiB | Private delta MiB | Intervals with gap>250ms |
|---|---:|---|---:|---:|---:|---:|---:|
| CfT116 |42|201.245/216.650/215.629/296.760|647.457–706.371|461.293–548.855|61.785–75.477|71.469–81.965|2/5|
| Stable156 |36|200.954/216.119/214.809/285.934|852.328–923.563|569.074–647.195|43.762–78.629|42.531–80.785|1/5|

Both honestly retain `DegradedIntervalsPresent`;requested200ms is a scheduling target,not guaranteed cadence. These improve temporal coverage but still establish sampled owned-browser-group observations,not true peak/pure extension/KDF allocation or performance acceptance. Additional observer warmup/sample timings are not substituted into original P2 latency statistics. Earlier sparse negative deltas remain preserved.

CfT116 fast raw SHA`2432e180be53698a9303b7490cf9b99cc55ff2e36dc3182c0747400caedf5e2a`;Stable fast raw SHA`8c9624ef2075d89221f4b048582a2d84235456428ec9c56426fb4e0f0ac18551`. Native inputs/helpers remain separately frozen in `memory-fast-freeze.json`. Reviewer launched no browser or sampler.

## Findings and unresolved

No open actionable finding in reviewed functional/timing/RAM/fault scope. Selected task exit met:recorded browser/OS/hardware/production parameters,sample counts,cold/warm/startup/RAM/fail-closed outcomes for both selected cells,independent review and cleanup. Descriptive quality limits do not establish performance SLO/pure KDF/true peak/broad support. Formal candidate/other B tasks and owner-deferred real two-device Sync remain separate release gates.

## Final runtime cleanup review

Read root's `final-native-processes.json`,`identity-postflight.json`,`cleanup.json` and B08 cleanup mirror. Actual retained native query records zero owned processes;reviewer did not repeat Windows query. Independently confirmed exact owned root `/mnt/c/Users/van.tran_fgorg/AppData/Local/Temp/passchef-b03-oct30-XpxOSA` absent,all11raw records retained,38launch records context-closed and20RAMJSONLs preserved.36launches additionally have explicit ESRCH root-exit proofs;initial two P0 launches have context closure plus final zero-process proof. No claim of38ESRCH checks.

Saved Oct30 snapshot46canonical tests,held74source hashes and ZIP278b independently rechecked unchanged after cleanup. Native helper/source-copy reads preceded removal. Root retained original archives,official portable browser acquisition ZIP,frozen inputs/helpers/raw;desktop released,no further GUI/account/Sync/clipboard/publication action. Final runtime/cleanup review **Passed**;manager status/link reconciliation remains root's documentation step.

## Final manager reconciliation —2026-10-30

Bounded read-only review of15current changed documents:master,Track01,catalog,plans index,roadmap,changelog,README,A10/A13/A23 reports,B03 protocol/actual/review and B08 actual/review.522local targets resolved;46unique catalog IDs each resolve to exact owning task anchor;global46anchors retained;master78lines. Current status:B03 selected measurement task Done;B08 selected local upgrade Passed/overallPartial;C02 decision Done;A13 held/NOTDone,A23/C01 Partial and owner-deferred real Sync mandatory beforepublication unchanged. Historical measurements/preparation/failures retain dated scope.

One stale current plans-index sentence still called all B03 runtime Pending;root fixed it to Done30Oct with current actual/review links and explicitly historical preparation. Re-read correction and master unresolved wording narrowed to remaining candidate/native/Sync acceptance;finding resolved. No manager/source modification by reviewer.

Rechecked46test hashes,74source hashes,held ZIP278b and absent exact owned root;unchanged/cleaned. `git diff --check` Passed. Final bounded bookkeeping verdict **Passed**,no unresolved actionable finding;no research/browser/Windows query/test replay. Remaining release/Sync gates are documented tasks,not reopened B03 work.
