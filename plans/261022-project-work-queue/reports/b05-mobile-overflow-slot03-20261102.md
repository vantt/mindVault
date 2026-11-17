# B05 — slot03 residual mobile overflow

Date:2026-11-02, Asia/Saigon. **Diagnostic and first repaired-candidate W01 Failed.**
Final diagnostic W01 stillFailed462vs375;desktop returned16:39:57Asia/Saigon.
Parent:[B05](../phase-03-website.md#task-b05),
[reviewed diagnostic preparation](b05-overflow-diagnostic-preparation-20261102.md).
Prior slot02 failures/scoped passes and historical Run03 Unknown remain unchanged.

## Approved slot and diagnostic attempt

Owner replied “Bàn giao thêm 15 phút ngay”. Root bound start09:28:07UTC,
cleanup09:40:07UTC,deadline09:43:07UTC (16:28:07–16:43:07Asia/Saigon).
[Actual preflight](../../../chrome-extension/build/b05-offline-helper-20261102/slot03-preflight.json)
verified frozen runner10/cases/current nine-file candidate,HEAD352ffa9,
extension74/tests46/heldZIP unchanged and ports48761/48762free;Node24.18.0.
Root ran reviewed runner10 with `--slot-start 2026-11-02T09:28:07Z`.

Actual UUID`b30a76c5-1a19-46e4-b19a-63b052faf492`,host exit0;W00 launch-only,
W01 Failed at four390px scroll checkpoints;W02–W06NotRun. Exit0 is harness
completion,not website QA pass. Actual
[result](../../../chrome-extension/build/b05-offline-helper-20261102/b30a76c5-1a19-46e4-b19a-63b052faf492/result.json),
[geometry](../../../chrome-extension/build/b05-offline-helper-20261102/b30a76c5-1a19-46e4-b19a-63b052faf492/W01-overflow-diagnostics.json)
and stdout/stderr retain exact times/observations. No guards weakened.

All checkpoints:client375/root scrollWidth478. Content measurements:
`.step` width343 but grid track461.656;`.step-content` width461.656/right477.656,
with `.password-output` width411.656. Mobile bare1fr/auto minimum and unwrapped
Dish content are the concrete primary candidate. Separate `#demo-copy` right436.094
within a293px-wide `.result-content` identifies another content overflow. Fixed
background glows,unrevealed AOS transforms and clipped decoration also appear in
geometry;they are not substituted for the measured root-width failure.
Independent reviewer confirmed these measurements/source relationships before repair;
causal repair acceptance still requires an affected live retest.

Root [attempt01 postflight](../../../chrome-extension/build/b05-offline-helper-20261102/slot03-attempt01-postflight.json)
at09:29:34.126UTC proves owned processes[],portsfree,own profile removed.
Desktop remains reserved within original slot for reviewed repair/retest;
no final return claimed at this diagnostic checkpoint.

## Scoped next sequence

Preserve current CSS bytes/snapshot;fix only measured step/Dish/Copy intrinsic sizing,
with no new page overflow hiding. Compile/source checks → independent prelaunch
review/new frozen candidate → affected width/visibility checks → root native
postflight/desktop return → independent runtime review → plan update.
Reuse prior unrelated scoped passes;do not infer full responsive/accessibility/motion/
performance/provider/deployed acceptance or publication rights.

## Scoped CSS repair candidate — runtime verdict Pending

Builder preserved oldCSS`styles-runtime10.css`,SHA
`633a83bfab0990464d83e682b2c07bca0e1a48c51d5692445cbf7327c604c2e3`.
[Exact diff](../../../chrome-extension/build/b05-offline-helper-20261102/styles-runtime10-to-fix.diff)
changes step tracks to`minmax(0,1fr)`,sets content/text minimum widths0,allows long
Dish text to wrap and password-output flex wrapping. It adds no page-level overflow
hiding. AmendedCSS SHA`b56e69a8e2c931257fe3ef23f0c305c8a55c0d5a09df80b53f4aa4e13a6ec0ce`;
compatibilityJS remains`1974bb01dd921c9b323dd35e27ad159e6d340cbbc0139fad2c4c8d7aa431ed49`.
Builder reports PostCSS8.5.6 parse/roundtripPassed,516nodes,0warnings;source/parser
checks do not establish rendered width acceptance.

New immutable [candidate](../../../chrome-extension/build/b05-offline-helper-20261102/candidate-residual-fix.json)
binds9website files at09:33:17.275UTC. Reviewed runner11 uses focused W01 only:
390/768/1440viewports ×6scroll checkpoints and revealed Copy/Dish visibility.
Root actual retest result and final desktop return are Pending at this checkpoint;
author performed read/hash checks only,no GUI/browser/server/test replay.

## Actual repaired-candidate retest — attempt02

Root executed in the same approved slot:

```bash
/home/vantt/.nvm/versions/node/v24.18.0/bin/node chrome-extension/build/b05-offline-helper-20261102/runner-11-overflow-fix.mjs --slot-start '2026-11-02T09:28:07Z'
```

Actual UUID`394329c6-f575-450a-acbc-6f89fdb243be`,09:34:48.791–09:35:14.873UTC,
host exit0;**W01Failed**,W02–W06NotRun. Unchanged root-width assertion failed at
all6mobile checkpoints:root scrollWidth462/clientWidth375. At768/1440,all6each
document-width samplesfit753/1425. Affected result-value/Copy/step/Dish boxes fit
the viewport at all18checkpoints:the scoped fix helped measured content but did
not complete mobile overflow repair. `#recipe-syntax` itself still scrollWidth462.
Actual [result](../../../chrome-extension/build/b05-offline-helper-20261102/394329c6-f575-450a-acbc-6f89fdb243be/result.json)
and [18observations](../../../chrome-extension/build/b05-offline-helper-20261102/394329c6-f575-450a-acbc-6f89fdb243be/W01-affected-width-observations.json)
remain immutable;no failure removed or promoted toPassed.

The separate strict visibility assertion was not reached after root-width failure.
At fixed800ms,the targeted Copy/Dish boxes/text exist but several observed cumulative
opacities arebelow0.99,including desktopCopy0.283 andDish0.774. This is not visibility
acceptance. Future finite sampling should wait for actual reveal completion and keep
the visibility contract;do not lower its threshold to fit these transient samples.
No OS clipboard/Copy action exercised. Font routing remains blocked local fallback,
not real-font/public/wholeplatform acceptance. Helper parent receipts say absent;
root final descendants/ports/profile/desktop-return proof stillPending here.

| Frozen file | SHA256 |
|---|---|
|Runner11|`bb2457314a4cf411fb6df4ca291bca8f13d99d4505abc05626f8f511bc78356e`|
|Cases|`891d38e31c609541d038955938365ac45a50cb8df5927b0b0ae0fbe490fc4ebd`|
|Candidate9|`6c8c44a2797ec419fb6b4994c2f02b851294bac1285dc5953eb9651f3fa0d9c1`|
|Original diagnostic result|`05442d13f2d85bfeb779ef663c5b196dc3adb99d556dcb450fa6e53eb0d349a1`|
|Attempt02 result|`fd9c5d68a733df5b1b896e0be7ce157d6a163412d06e217935cf476f3ccf3bcb`|
|Attempt02 observations|`cc113a8491e0960920212865d3f232b43f0fff152227c56cba8e8e75d3de3ce2`|

Author read actual source/results and independently hashed evidence;no runtime replay.

## Follow-up diagnostic attempt03 and final desktop return

Root executed the same approved slot and same repaired candidate:

```bash
/home/vantt/.nvm/versions/node/v24.18.0/bin/node chrome-extension/build/b05-offline-helper-20261102/runner-12-diagnostic.mjs --slot-start '2026-11-02T09:28:07Z'
```

Actual UUID`e77b2171-5fed-49d5-a607-3d51f04e9aa0`,09:37:56.582–09:38:08.736UTC,
host exit0;W00configuredlaunchPassed,**W01Failed**,W02–W06NotRun.
[Result](../../../chrome-extension/build/b05-offline-helper-20261102/e77b2171-5fed-49d5-a607-3d51f04e9aa0/result.json)
and [four-checkpoint geometry](../../../chrome-extension/build/b05-offline-helper-20261102/e77b2171-5fed-49d5-a607-3d51f04e9aa0/W01-overflow-diagnostics.json)
retain root462/client375 at all4checkpoints. Measured outbound DOM boxes comprise
fixed glows,unrevealed AOS comparison/security cards and clipped CTAglow geometry;
no step/Dish/Copy outbound box remains. These decorative/unrevealed boxes do not
by themselves explain the exact residual root width. **Residual cause Unknown**;
unmeasured pseudo-elements are a possible investigation target,not proven cause.
The prior strict visibility assertion remains unreached;its transient samples above
are preserved. No width/visibility contract relaxed and no falsePass assigned.

Root [final postflight](../../../chrome-extension/build/b05-offline-helper-20261102/slot03-final-postflight.json)
at09:39:57.359UTC **returned desktop16:39:57.359Asia/Saigon**,before16:43:07deadline.
It records ownedprocesses[],ports48761/48762free,all3ownedprofilesremoved,
runtimeStopped:true,candidatewebsite9match,runtime74/tests46/heldZIPunchanged.
No more GUI authorized after return;author onlyread/hashed evidence.

| Additional frozen evidence | SHA256 |
|---|---|
|Runner12|`eed7babbbd704b924ecf227c94cbe17b229997dded1160927c2bb5711a84f8be`|
|Diagnostic cases|`532c71af8dabf48a9a6e11d8b81b67a7282d30b725e0f523561843e5435ba47d`|
|Attempt03 result|`3fbba685eee6b8908a016b67159ab6ba9536668caf74ea4eaf08435ab0db415c`|
|Attempt03 geometry|`360407c24ed1ae59fb2d355d4f9f7ecf962877b5192cc2491d83abb27dbcc693`|
|Final postflight|`cca741491f9f95e6b980fe0200e28e5f22609b163f2468ec4d1b44e3f8ff9ceb`|

Independent runtime/final bookkeeping review Passed. Unresolved: residual mobile
root overflow cause/repair;revealed visibility contract;remaining broad B05/publication gates.

Next [pseudo13 preparation](b05-pseudo-diagnostic-preparation-20261102.md) is independently source-reviewed/frozen and unexecuted; original-root acceptance remains Failed. No GUI after recorded desktop return.
