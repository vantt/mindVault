# B05 — revision05 offline repair verification

Date:2026-11-02, Asia/Saigon. **Revision05 verification Failed:35 Passed,1 Failed.**
**Subsequent revision06 offline verification Passed:36/36; independent review Pending.**
Parent:[repair](b05-helper-repair-20261102.md),
[offline sequence](b05-offline-continuation-plan-20261102.md).
B05 remains Partial; W01–W06 NotRun; historical Run03 early-skip cause Unknown.

## Actual checks

Read revision04→05 diff and tested actual extracted revision05 declarations,
slot validation, timer callbacks and finalization in isolated Node VM contexts.
Original16 contract assertion intents remain; all16 Passed, including F03 result
persistence after cleanup rejection. Extended20 checks cover both cleanup branches,
partial absence receipts, cleanup/result save failures, unexpected cleanup rejection,
timer clearing/nonzero exit/idempotence, synchronous browser-close failure,
cleanup/hard-timer error handling, strict slot validation and derived deadlines.
Controlled dependencies/clock/timers exercise helper logic only; no runner import,
top-level creation, GUI/browser/server/network/clipboard/child-process launch.
Actual own-process `/proc/self/stat` read remains the parser check.

Commands from repository root:

```bash
/home/vantt/.nvm/versions/node/v24.18.0/bin/node --check chrome-extension/build/b05-offline-helper-20261102/runner-05.mjs
/home/vantt/.nvm/versions/node/v24.18.0/bin/node --check chrome-extension/build/b05-offline-helper-20261102/verify-helper-05.mjs
/home/vantt/.nvm/versions/node/v24.18.0/bin/node chrome-extension/build/b05-offline-helper-20261102/verify-helper-05.mjs
```

Syntax exit0; behavioral command exit1 on Node24.18.0. Immutable
[36-case results](../../../chrome-extension/build/b05-offline-helper-20261102/verification-05.json)
and [checker](../../../chrome-extension/build/b05-offline-helper-20261102/verify-helper-05.mjs)
retain individual assertions and source/script hashes. Source05 SHA256 remains
`12f29fb867282a1844db34677033c549f302b0e9f5c6f9b13235c550b4b72d48`.

The first checker setup attempt expected multiline finalization and failed before
any behavioral check or runner evaluation. Checker extraction was adapted to the
actual compact `finally{await finalize();}`; runner unchanged. This was a checker
adaptation error, not a source05 or website failure.

## Demonstrated remaining defect

`R11-surviving-parent-implies-nonzero` supplies a spawned Chrome identity and a
bounded `stop` return with `parentAbsent:false`, without throwing. Actual cleanup
correctly records `parentsAbsent:false` but does not call `cleanupFailure` or set
nonzero exit; actual finalization saves the result and process exitCode stays0.

```text
AssertionError [ERR_ASSERTION]: Surviving owned parent must not exit successfully
```

This return is reachable when the real bounded stop escalation cannot establish
parent absence. It is an offline demonstrated classification defect, not evidence
that a real parent survived any historical run. Recommend separate revision06:
record failed/incomplete absence and nonzero when an existing child lacks proven
`parentAbsent:true`; retain receipt and root postflight requirement. Do not signal
again or terminate an unproven owner. Preserve05 and rerun unchanged36 assertions.
Independent review remains Pending until repair verification succeeds.

## Preservation

Fresh [preservation receipt](../../../chrome-extension/build/b05-offline-helper-20261102/preservation-05.json)
confirms website7/runtime74/tests46 match frozen snapshot, original checker and all
seven historical helper/result/postflight identities match, held ZIP unchanged at
`278b59df8ddef0085c9c064da11485695074419894c3f43b5077a10fc44f1e9e`.
No product/Golden/historical helper or acceptance change. Full headed suite not run.

## Revision05 unresolved at its checkpoint

R11 requires scoped repair and re-verification, then independent review. Actual
slot/canaries/cells/owned cleanup and public/owner gates remain Pending.

## Subsequent revision06 verification — same36 assertions

Tester syntax-checked new runner06 and reran the unchanged185line checker against
that source into a new result path. No assertion relaxed or added. Both commands
exit0 on Node24.18.0; **36 Passed,0 Failed**, including original F03 and R11.

```bash
/home/vantt/.nvm/versions/node/v24.18.0/bin/node --check chrome-extension/build/b05-offline-helper-20261102/runner-06.mjs
/home/vantt/.nvm/versions/node/v24.18.0/bin/node chrome-extension/build/b05-offline-helper-20261102/verify-helper-05.mjs /home/vantt/projects/passMan/chrome-extension/build/b05-offline-helper-20261102/runner-06.mjs /home/vantt/projects/passMan/chrome-extension/build/b05-offline-helper-20261102/verification-06.json
```

Immutable [revision06 results](../../../chrome-extension/build/b05-offline-helper-20261102/verification-06.json)
record source SHA256
`793c7085aa2e6f529093b4920eb6a3eb5bd8e6b4978d1d34a59c669c544b40d0`.
Fresh [preservation receipt](../../../chrome-extension/build/b05-offline-helper-20261102/preservation-06.json)
confirms both original/05 checkers unchanged, runner05 unchanged, all seven historical
identities unchanged, website7/runtime74/tests46/heldZIP still match frozen baseline.
Prior04/05 failure evidence remains intact. No GUI/server/process-spawn or full
runner evaluation; contract-only checks do not establish live native cleanup or QA.

## Current unresolved

[Independent review Passed](b05-helper-repair-independent-review-20261102.md), freeze and plan updates complete; fresh desktop handoff/canaries/W01–W06
and real owned-process/port/profile postflight. Historical Run03 skip cause Unknown;
full B05 and public/owner gates remain Pending.
