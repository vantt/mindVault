# B05 — actual local QA slot01 and preserved setup failures

Date:2026-11-02, Asia/Saigon. **B05 Partial; website cases W01–W06 NotRun.**
Parent:[Track03 B05](../phase-03-website.md#task-b05),
[reviewed preparation](b05-local-qa-preparation-20261102.md).
Root operated runtime; author performed subsequent read-only source/evidence audit.
[Independent evidence/cleanup review Passed](b05-local-qa-runtime-20261102-independent-review.md); website QA remains NotRun.

## Authorization, candidate and actual return

Owner explicitly handed over desktop15minutes and requested notification when usable.
Actual approved slot06:01:27–06:16:27UTC (13:01:27–13:16:27Asia/Saigon);
planned cleanup checkpoint06:13:27UTC. No extension beyond that slot.
[Final root postflight](../../../chrome-extension/build/b05-local-qa-20261102-slot01/final-postflight.json)
records **desktop returned06:13:34.934UTC (13:13:34.934Asia/Saigon)**,before deadline.

Candidate HEAD`352ffa9300dbd3698f3d32e48b833cf41d841c83`; all7website file hashes
matched before runtime and remained unchanged at final postflight. Existing extension
runtime/Golden/artifact acceptance is not replaced by this website attempt.
[Actual preflight](../../../chrome-extension/build/b05-local-qa-20261102-slot01/preflight.json):
Node24.18.0,LinuxWSL2 5.15.133.1-microsoft-standard-WSL2,retained Playwright1.57.0.
Actual runner03 connected Chromium **143.0.7499.4**,existing executable
`/home/vantt/.cache/ms-playwright/chromium-1200/chrome-linux64/chrome`,SHA256
`2e61bc3fd990bd4d7b419ef6b6303c67aaed683e5b83b3b25e416f015f343209`.
Local server127.0.0.1:48761,CDP127.0.0.1:48762; no dependency/browser download.

## Actual attempts — no failed history overwritten

Raw root:`chrome-extension/build/b05-local-qa-20261102-slot01/` (ignored local only).

| Attempt / UUID | Actual host outcome / scope | Root recovery |
|---|---|---|
|01 `baebda54-e04a-4743-a5e8-09d3213909da`|Exit1;06:06:58.335–06:06:58.659UTC. `/proc` stat regex returned null; `TypeError: Cannot read properties of null (reading '1')` at runner.mjs:33:87. AllW00–W06NotRun.|Server spawned before `launch()` returned; local cleanup said spawned:false/parentsAbsent:true despite losing its handle. This was insufficient proof. Root identified exact uv/Python group1510126 with args/starttime and SIGTERM; remaining[]06:07:30.182UTC.|
|02 `ed141f60-68bf-40e6-b614-e5ec871252e8`|Exit1; root observed Node Undici `Parser.finish` assertion `!this.paused` during readiness. No result/preflight JSON; only retained server/chrome logs. Original assertion was tool output and was not persisted to a raw file; do not invent a missing log or exact internal cause.|Root identified exact owned groups1510817/1510818 and SIGTERM; remaining[]06:10:32.041UTC.|
|03 `4aadd757-7b04-49ef-a3d8-4688fad0c343`|Exit0;06:12:03.182–06:12:04.497UTC. Connected browser and installed configured guards; helper recorded W00launch-only Passed. W01/W02Skipped with `Cleanup reserve/fatal boundary`; W03–W06NotRun.|Helper closed browserPID1511911 exit0,serverPID1511910 exit143; parent absence verified. Root subsequently verified descendants/ports and removed profiles.|

[Run01 actual result](../../../chrome-extension/build/b05-local-qa-20261102-slot01/baebda54-e04a-4743-a5e8-09d3213909da/result.json),
[Run01 root recovery](../../../chrome-extension/build/b05-local-qa-20261102-slot01/failed-launch-postflight.json),
[Run02 root recovery](../../../chrome-extension/build/b05-local-qa-20261102-slot01/failed-02-postflight.json),
[Run03 actual result](../../../chrome-extension/build/b05-local-qa-20261102-slot01/4aadd757-7b04-49ef-a3d8-4688fad0c343/result.json),
[Run03 preflight](../../../chrome-extension/build/b05-local-qa-20261102-slot01/4aadd757-7b04-49ef-a3d8-4688fad0c343/preflight.json).
Run03 stdout/stderr retained separately; no website screenshots/case JSON produced.

## Frozen helper identity and scope

Source/syntax and independent prelaunch reviews preceded each revised launch;
they did not prove runtime acceptance. Original/revised helpers remain separate.

| Helper | SHA256 |
|---|---|
|Original runner.mjs|`bc4a1ffd38986d5fc9672ccfb900c209561461fd36898e205f1b8aa7bac914d3`|
|runner-02.mjs|`dac08a014e3413effccadfbd2c1464fce0a4c9001d0227f20d87ae5e6c126518`|
|runner-03.mjs|`3198e834b127583e64858160ac9bdc71d96826c198bbf0f56abe14d1af4490fe`|
|Unchanged cases.mjs|`b839ecdbc3eebace1aecc5384087253d0a0ed63a20f44578b70c220d465da6f4`|
|Run03 result.json|`a4f331e42438409e837358bb0981eba2e641c1a3e9233123bbba3b87cc750ef9`|
|Final-postflight.json|`a4d2095e58e5a7a62f8360a39095590053c8e7bf2426b71492211bece67a6d31`|

Runner02 repaired stat parsing/retained child handles and added bounded CDP readiness;
runner03 replaced Node fetch readiness with node:http after the host assertion.
[Last independent prelaunch review](../../../chrome-extension/build/b05-local-qa-20261102-slot01/prelaunch-review-03.md)
records selected-only source review; actual runtime review remains a separate step.

## Early skip diagnosis — Unknown

Run03 skipped cells by06:12:04UTC,about83seconds before cleanup checkpoint and
38seconds before its `cleanupAt-45000` budget threshold06:12:42UTC. Exhausted
wall-clock budget is therefore **not established** by the retained timestamps.
The predicate also checks `!fatal` and `!cleaning`; its branch inputs were not saved.
Only a thrown error copies fatal into result.fatal. A guard fatal could silently
skip cells without being serialized; this is a possible mechanism,not a proven cause.
Exact runtime reason remains **Unknown**. No helper change or rerun performed by author.

Run03 network[]/errors[] do not prove resource isolation acceptance: no Playwright
website navigation or guard canary occurred. Server log has only root's readiness
GET/200. W00means configured owned launch/connection only,not guard effectiveness,
responsive/keyboard/reduced-motion/links/DEMO01/OG/performance Passed. Browser DBus/GPU
diagnostics are retained; no evidence assigns them the early-skip cause.

Future offline repair should save every budget input/current clock/threshold/fatal/
cleaning state and guard event immediately,retain final fatal regardless of throw,
and classify silent aborted cells distinctly. Review/test that logic without GUI,
then require concrete guarded navigation/canary before claiming W00guard acceptance.
No new desktop run is authorized by the completed slot.

### Subsequent root diagnostic preparation — source only

After returning desktop, root created `runner-04-diagnostic.mjs`,SHA256
`5eb5a3555723d58fb91cf99e1e900928f5f2dd6baedc42fafa18c348895c63c4`.
It saves budget/fatal/cleaning state and guard events, explicitly surfaces fatal
boundaries and preserves final state. Node24 `--check` and independent source
review Passed; revision04 never executed. Historical run03 remains Unknown;
diagnostic logging does not establish a runtime fix. The old slot deadline remains
expired; no fresh browser action follows from this offline preparation.

## Cleanup and remaining acceptance

Final postflight proves remainingOwnedProcesses[],ports48761/48762free,all3UUID-owned
private-profile directories removed,website7hashes unchanged and runtimeStopped true.
Failed attempt receipts are not substituted for that final proof. Root returned
desktop before approved deadline; no browser/server work continued afterward.
No OS clipboard/Copy,real accounts,Menu write,publication/deployment or source repair.
Ignored raw evidence/helper links are local availability,not public uploads.

Unresolved:early-skip predicate cause; reviewed helper diagnosis/repair; actual
W01–W06 and full B05 matrix; OG/public URLs/deployment/publisher decisions.
Program/website/B05 remain Partial; no completed website QA task claimed.
