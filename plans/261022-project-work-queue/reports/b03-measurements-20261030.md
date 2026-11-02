# B03 — native Windows measurements2026-10-30

Date:2026-10-30, Asia/Saigon. **Scoped Done:P0–P4 selected WindowsStable156.0.8078.76/CfT116.0.5845.96
functional observations/descriptive measurements Passed and independently reviewed;cleanup complete.** Root operated all
browsers/Windows commands;author read evidence only. [Protocol](b03-browser-measurements.md),
[earlier Stable P0 history](b03-native-stable-run-01.md),[B08 selected upgrade](b08-artifact-acceptance.md).

## TODO / current runtime boundary

- [x] Bind exact source/browser/helper/native host identities and actual P0/P1/P2 outcomes.
- [x] Recalculate descriptive timing/RAM/cadence from raw samples;preserve uncertainty.
- [x] CfT116 P4 baseline/missing/corrupt/restored Passed;exit0/no host timeout.
- [x] Stable156 P4 baseline/missing/corrupt/restored Passed;exit0/no host timeout.
- [x] Independent raw review Passed P0/P1/P2/both P3 methods/P4.
- [x] Root final native/workspace cleanup/immutable identity verified;desktop released.

## Actual identity and execution

Same held2.3.1 beta ZIP74files SHA256
`278b59df8ddef0085c9c064da11485695074419894c3f43b5077a10fc44f1e9e`.
Windows11 Pro N10.0.26200/build26200/x64;AMD Ryzen7 PRO6850HS,8cores/16logical,
42,212,544,512bytes physical RAM. Power/thermal/background-load controls were not
recorded;these measurements describe this session rather than a benchmark guarantee.
Native Node22.14.0/Playwright1.57.0;reviewed P0/v2 helpers frozen separately.

| Browser cell | Native identity / provenance | Loader |
|---|---|---|
| Actual installed Google Chrome Stable |156.0.8078.76,valid Google LLC signer,exeSHA`66885d764fa17c7a7c53e1f94148e46959c5ec21fa5bc3ac22143eb2a1f7731a`;live CDP Chrome/156.0.8078.76 |Fresh owned profile,reviewed modern flags/CDP Extensions.loadUnpacked |
| Explicit Google Chrome **for Testing**116 |116.0.5845.96/revision1160321,exeSHA`7460094ab51e1cb2fa6abeef0683275bb99684ad00334556547d7f4a6dd84c4e`;host signature NotSigned. Official archive135,737,736bytes/SHA`58ce894d230738e5b05ec0e20f8283c77070a97afb21bae3175e13f2b0af08b7`,root checked Google MD5;live CDP116.0.5845.96 |Separate extracted browser/private profile,legacy unpacked flags |

CfT116 is accurately branded,not an installed branded Stable116 result. Its acquisition
provenance does not change the NotSigned executable observation. Actual binary hashes
remained identical before/after each completed cell;no updater/policy alteration.
Separate profiles per browser prevent newer-profile reuse in116;both configs use the
same reviewed owned source path,unchanged outside explicit closed-browser fault stages.
Production Argon2id t3/m65536KiB/p4/32byte key and AES protection remain unchanged;
no direct adapter loop,vault/session injection,KDF fallback or network account used.

P0 both Passed/exit0:genuine setup→enabled Spice Save→Settings Lock→authoritative
locked/no session and actual visible Unlock→same-master UI unlock→literal Builder
Dish `Basic*r4nd0m`. Initial P0 helper records context closure;the CfT P0 external
process postflight also records absence. Strengthened v2 used below additionally
requires native browser root PID ESRCH after context.close before reuse/mutation.

P1 each Passed10process-cold samples plus its genuine setup/control launch;11native
root exits each proved,all10measured browser PIDs fresh in these runs. Reopen retained
synthetic encrypted profile;launch→locked-ready waits AUTH_STATUS/no key/visible
Unlock/hidden Dashboard,not navigation-only load. No disk-cache flush or disk-cold claim.
P2 each Passed2retained warmups+20measured unlocks in one browser process;normal Lock
between attempts,exact encrypted Sync snapshot unchanged after each unlock. Both v2
warm root exits proved. All completed blocks exit0/no host timeout/page errors.

## Descriptive timing — milliseconds

| Cell | Real UI interval | N | Min | Median | Mean | Max |
|---|---|---:|---:|---:|---:|---:|
|CfT116|Process-cold startup→locked-ready|10|884.400|954.362|1032.402|1653.537|
|CfT116|Process-cold unlock|10|887.724|892.479|895.489|915.240|
|CfT116|Warm-browser unlock|20|865.882|881.118|880.302|890.852|
|Stable156|Process-cold startup→locked-ready|10|1015.872|1095.691|1108.188|1242.250|
|Stable156|Process-cold unlock|10|883.453|889.756|891.867|907.921|
|Stable156|Warm-browser unlock|20|875.336|893.767|894.360|912.963|

Unlock interval is native Node monotonic click-dispatch→visible Dashboard,including
UI/IPC/worker/WASM/AES;startup interval is launch-dispatch→authoritatively locked-ready.
Warmups remain in raw records but are excluded from N20 summary. CfT116 all22warm
labels are `worker-unverified`;Stable all22are `fresh-worker-observed` from lifecycle
events. Neither establishes retained/hot WASM. Observer/sampler overhead is included
in those samples;warm-browser versus cold are descriptive conditions,not an SLO.
No p95,approved latency threshold or pure Argon2 duration inferred. Native UTC fields
and Linux host completion timestamps are preserved separately;do not subtract their
clock offsets to manufacture runtime durations.

## P3 — sparse sampled owned-browser-group RAM

Five complete bracketed unlock intervals per cell;**one during-unlock row per interval**.
Requested200ms cadence was not achieved because native enumeration/measurement costs
add overhead. Actual observed start-to-start gaps across all10JSONL files:

- CfT116:N13 gap min/median/mean/max 641.441/728.679/721.257/817.085ms.
- Stable156:N10 gap min/median/mean/max 687.311/795.585/795.719/879.717ms.

Ranges below use5intervals per cell;1MiB=1,048,576bytes. During-only maximum excludes
baseline/post rows;delta subtracts each interval's own last complete pre-unlock row.

| Cell | Baseline working-set sum MiB | During sampled working-set sum MiB | Working-set delta MiB | Baseline private bytes MiB | During sampled private bytes MiB | Private delta MiB |
|---|---:|---:|---:|---:|---:|---:|
|CfT116|541.660 to 584.176|607.105 to 662.703|65.121 to 78.527|373.633 to 448.633|447.344 to 515.777|67.145 to 74.352|
|Stable156|798.230 to 875.102|796.711 to 887.742|-6.422 to 12.641|504.262 to 599.285|502.734 to 598.098|-7.492 to -1.188|

These sums include owned Chrome browser/renderers/GPU/utility descendants discovered
dynamically each iteration;shared working-set pages may be counted more than once.
They are not extension-only/WASM memory,instantaneous allocations or true peaks.
Negative Stable deltas remain visible and reflect whole-process lifecycle/noise;
they do not imply negative derivation cost. Raw per-PID timestamps/cadence and missing
process fields remain available;different baselines prevent clean browser-efficiency
comparison. P3 is observed but200ms/high-resolution peak characterization is Incomplete.

## P4 — actual missing/corrupt WASM and restored control

Both native browser cells Passed/exit0/no host timeout. Real setup/Spice Save and
nonempty synthetic Menu assignment establish a usable encrypted baseline;literal
Cook passes before faults. Same owned installed path/profile/ID
`fbaiicajjgnfkadncnlmmghbkhhgenab` retained across each browser's4stages. Owned
browser root ESRCH is proved before every missing/corrupt/restore mutation;canonical
and held files stay untouched. Faults show explicit crypto-unavailable UI,authoritative
locked/no session,hidden empty old Dish and exact complete Sync snapshot preservation
including salt/ciphertext/default/nonempty Menu mapping. Restored control genuinely
unlocks and cooks the original literal Dish;original WASM SHA restored:
`0c2149886c13e4eae4a6ca25ee71d47423c5c8740a874cf04ff816d1b2c901d7`.

| Cell | Missing refusal ms | Corrupt refusal ms | Restored unlock ms | Native root exits |
|---|---:|---:|---:|---|
|CfT116|120140.910|120189.432|886.853|4/4proved|
|Stable156|120229.153|120180.186|904.561|4/4proved|

Fault waits consume the existing production120s fail-closed timeout;these are failure
path observations,not ordinary unlock latency or a new timeout/SLO.4stages and4exits
per browser are separate from P1/P2sample counts. All raw outcomes remain retained;
final workspace cleanup and independent raw review are complete,linked below.

## Raw evidence and checks

All results/config/start/completion/logs plus10memory JSONL files are retained in
[owned evidence directory](../../../chrome-extension/build/b03-measurements-20261030/).
P0 completion files:`p0-cft116-completion.json`,`p0-stable-completion.json`;
P1/P2 completion files share their result stem with `-completion.json`. Each reports
exit0;v2 hostTimeout false. Helper freezes bind runtime config hashes/budgets;Windows
sampler AST errors empty,no policy change. Key raw SHA256:

| Evidence | SHA256 |
|---|---|
|[fault-cft116-run01.json](../../../chrome-extension/build/b03-measurements-20261030/fault-cft116-run01.json)|`9356c7b315d27fcaf66acf145f6260beec199074839b711e0f06d744a7112a7a`|
|[fault-stable-run01.json](../../../chrome-extension/build/b03-measurements-20261030/fault-stable-run01.json)|`e3bb7b6193f5f010aa3d4ea146b09a938bc32cc27c22bb052ea64722ffc396f0`|
|[host-preflight.json](../../../chrome-extension/build/b03-measurements-20261030/host-preflight.json)|`ea9e86e56968fffe007a6ad8405da1259ae672c6593606956fbe157d17d3bf65`|
|[workspace.json](../../../chrome-extension/build/b03-measurements-20261030/workspace.json)|`f2bca5fd49b5e11c135754bd91c8c030519f071c250fb9826459db6b63343800`|
|[p0-freeze.json](../../../chrome-extension/build/b03-measurements-20261030/p0-freeze.json)|`19e70760af9b85f7b157f3fc0f000c51a4e179a156c8483cc8c17f5a975bcdfa`|
|[measurement-freeze.json](../../../chrome-extension/build/b03-measurements-20261030/measurement-freeze.json)|`e6db80ecd9a17e3894c96190da8a553003db8bfc5076c5e7ba6bd8dbf76ab299`|
|[p0-cft116-run01.json](../../../chrome-extension/build/b03-measurements-20261030/p0-cft116-run01.json)|`c665ae6459f76fb9bbf39a77d072895c727c207d775fb178700bf4b16839e006`|
|[p0-stable-run01.json](../../../chrome-extension/build/b03-measurements-20261030/p0-stable-run01.json)|`a2bcb5fe66362ceb188b564eb03535ae67df3f62c5da3808fab18a9650e9b37a`|
|[cold-cft116-run01.json](../../../chrome-extension/build/b03-measurements-20261030/cold-cft116-run01.json)|`59dd673a2441d5abb85d863030f086f366ffee48ed58b80af33973e5e96af8f7`|
|[cold-stable-run01.json](../../../chrome-extension/build/b03-measurements-20261030/cold-stable-run01.json)|`1d2279afbea92103b3db002b4a9435998f051814984371e5159b133bfa1ef211`|
|[warm-cft116-run01.json](../../../chrome-extension/build/b03-measurements-20261030/warm-cft116-run01.json)|`d161ef22c51477c95a1c4767c1653efdbe09cdec4b6fba46bd32594f2817f928`|
|[warm-stable-run01.json](../../../chrome-extension/build/b03-measurements-20261030/warm-stable-run01.json)|`b40a475ac852780b4359078fc1ca475432cd0147afab035f51393beb3776e570`|

Author checks recalculate N10/N20/min/median/mean/max and10memory intervals,raw exits,
bytes and reported identities. Current74source/46tests compare against held record
and fresh Oct30 snapshot;no unit/E2E rerun by author. [Independent raw review Passed](b03-measurements-20261030-independent-review.md);
final cleanup is verified separately below,alongside context closure/native root PID exits. No old report/failure/frozen input relabelled.

## Separate P3 observer repair preparation — historical prelaunch snapshot

Sparse raw observations above remain immutable. Root requested a focused observer
repair rather than replaying accepted P2N20. New ignored `sample-memory-fast.ps1`
(81lines) uses initial CIM ownership/root-creation check,then native Toolhelp32
PID/parent snapshots plus Get-Process memory for dynamically discovered descendants.
Tick scheduling subtracts work from requested200ms;actual gaps/work and a descriptive
observer-degraded flag(any gap>250ms) remain raw,not an app performance threshold.
`memory-observer-fast.cjs`55lines retains during-only maxima/baseline deltas;
`measure-memory.cjs`49lines performs genuine UI setup/control,2warmups+5memory unlock
intervals only,Sync preservation/native PID exit and300sblock cap. Both CJS syntax
checks Passed;Windows AST/native compile/runtime were root-owned pending at preparation.
[Separate preparation hashes](../../../chrome-extension/build/b03-measurements-20261030/fast-memory-preparation.json)
bind new3helpers;all prior6helper hashes still match reviewed v2freeze. Independent
source review/freeze precedes any new P3 run;do not overwrite sparse outcomes or
claim exact200ms/high-resolution peaks from scheduled cadence alone.

## Actual fast P3 followup — separate targeted observations

Both root-operated blocks Passed/exit0/no host timeout;each retains2warmups plus
5new memory unlock intervals only. Original P2N20 timings and sparse CIM outcomes
above remain unchanged. Root's Windows AST/native compile/Toolhelp canary Passed;
[fast freeze](../../../chrome-extension/build/b03-measurements-20261030/memory-fast-freeze.json)
binds5runtime helpers/2configs. Native root ESRCH exit,complete Sync preservation,
no page errors and unchanged browser hash proved for both blocks.

All5intervals per cell have baseline/during/post coverage. CfT116 during-row counts
5/6/3/3/4;Stable3each. Actual start-to-start gaps across10new raw JSONLs:

| Cell | Gap N | Min ms | Median ms | Mean ms | Max ms | Intervals with gap>250ms |
|---|---:|---:|---:|---:|---:|---:|
|CfT116|42|201.245|216.650|215.629|296.760|2/5|
|Stable156|36|200.954|216.119|214.809|285.934|1/5|

Both retain **DegradedIntervalsPresent**.200ms is requested scheduling,250ms a
transparent observer-quality flag;neither is an app performance threshold. Temporal
coverage improved,but exact200ms/no-gap/true-peak claims remain unsupported. New
sample intervals include observer overhead and are not substituted into P2 timings.

During-only sampled ranges and deltas,MiB using each interval's own baseline:

| Cell | Baseline working-set sum | During working-set sum | Working-set delta | Baseline private bytes | During private bytes | Private delta |
|---|---:|---:|---:|---:|---:|---:|
|CfT116|572.727 to 630.895|647.457 to 706.371|61.785 to 75.477|379.328 to 475.105|461.293 to 548.855|71.469 to 81.965|
|Stable156|786.570 to 844.934|852.328 to 923.562|43.762 to 78.629|495.191 to 566.859|569.074 to 647.195|42.531 to 80.785|

These remain dynamically discovered owned-browser-group sums;shared working-set
pages,other Chrome components and temporal variation prevent pure extension/WASM
allocation,efficiency ranking or true-peak claims. Earlier sparse negative deltas
remain retained. [Independent raw review Passed](b03-measurements-20261030-independent-review.md)
recomputed all10additional JSONLs/coverage/cadence/baseline deltas;key raw hashes:

| Evidence | SHA256 |
|---|---|
|[memory-fast-cft116-run01.json](../../../chrome-extension/build/b03-measurements-20261030/memory-fast-cft116-run01.json)|`2432e180be53698a9303b7490cf9b99cc55ff2e36dc3182c0747400caedf5e2a`|
|[memory-fast-stable-run01.json](../../../chrome-extension/build/b03-measurements-20261030/memory-fast-stable-run01.json)|`8c9624ef2075d89221f4b048582a2d84235456428ec9c56426fb4e0f0ac18551`|

**Scope assessment:**selected P0–P4 executions/observations are complete and reviewed
for actual Windows Stable156.0.8078.76 and explicitly branded CfT116.0.5845.96.
P3has explicit sampling-quality limits,not a performance SLO Pass;this does not
certify installed branded Stable116,other OS/browsers,Store delivery,Sync or final
candidate acceptance. Selected B03 protocol is scoped Done;broader release gates belong
to their own tasks and do not negate these completed measurements.

## Final cleanup and immutable identity

Root [identity postflight](../../../chrome-extension/build/b03-measurements-20261030/identity-postflight.json)
verifies canonical46tests,canonical/three owned74file source copies,held ZIP278b
and restored production WASM. [Final cleanup](../../../chrome-extension/build/b03-measurements-20261030/cleanup.json)
records all38browser launches closed,native CIM process count0,all11profiles and
exact owned WinTemp root absent. Desktop released;no further GUI.11raw records,
20RAM JSONL streams,frozen inputs/helpers and official portable acquisition ZIP
remain retained. Temporary Oct30 snapshot was copied to
[initial-snapshot.json](../../../chrome-extension/build/b03-measurements-20261030/initial-snapshot.json)
before its temp directory removal. Closure/PID proofs and later workspace absence
are separate verified postconditions. Author performed read-only checks only.

## Unresolved

High-resolution memory/worker identity limits;broader platform/formal candidate acceptance;
[B08 selected local upgrade Passed](b08-artifact-acceptance.md) remains separate from
Store delivery and real2physical-device Sync,which remain NotRun. Owner deferred Sync to a
later slot;mandatory before-publication gate retained,no repeated owner question here.
