# B05 — diagnostic helper offline verification

Date:2026-11-02, Asia/Saigon. **Revision04 offline verification Failed:15 Passed,1 Failed.**
Parent:[scoped TODO](b05-offline-continuation-plan-20261102.md),
[Track03 B05](../phase-03-website.md#task-b05).
B05 remains Partial; W01–W06 NotRun; historical Run03 early-skip cause Unknown.

## Executed checks and exact scope

Node24.18.0 extracted the actual revision04 function declarations into a fresh
`node:vm` context and separately executed its actual finalization body. The script
never imports/evaluates runner top-level creation, timers, snapshot reads or launch.
Controlled clock/timers and in-memory JSON saves exercise helper contracts only;
cleanup-error injection substitutes the `stop` dependency. Actual own-process
`/proc/self/stat` read verifies the unmodified parser against independent parsing.
No browser/server/child-process launch, network, display, clipboard, phone or build.

Actual commands from repository root:

```bash
/home/vantt/.nvm/versions/node/v24.18.0/bin/node --check chrome-extension/build/b05-offline-helper-20261102/verify-helper.mjs
/home/vantt/.nvm/versions/node/v24.18.0/bin/node chrome-extension/build/b05-offline-helper-20261102/verify-helper.mjs
/home/vantt/.nvm/versions/node/v24.18.0/bin/node --check chrome-extension/build/b05-local-qa-20261102-slot01/runner-04-diagnostic.mjs
```

Syntax commands exit0; behavioral command exit1. Reusable
[script](../../../chrome-extension/build/b05-offline-helper-20261102/verify-helper.mjs)
and immutable [actual results](../../../chrome-extension/build/b05-offline-helper-20261102/verification-04.json)
retain each assertion, source/script SHA256, runtime and outcome.
Revision04 SHA256 remains
`5eb5a3555723d58fb91cf99e1e900928f5f2dd6baedc42fafa18c348895c63c4`.

Passed15: actual process fields; budget before/at/after cutoff; fatal/cleaning
exclusions; boundary event retention; pre-cell fatal refusal; reserve NotRun without
callback; successful/throwing/timed-out cells and timer clearing; post-cell boundary
propagation; final fatal serialization without prior throw; idempotent cleanup
receipt. Post-cell boundary can retain an already completed Passed cell while
throwing overall; that narrow observation is retained in JSON, not browser acceptance.

## Demonstrated defect — final result lost on cleanup rejection

Revision04 L137 awaits `cleanup()` before L138–140 final-state/cell/result saving.
Actual cleanup L76 awaits `stop(chrome)` then `stop(server)` without catching failure.
An ownership/read/signal error can reject `stop`; the finalization then rejects before
saving `result`, and server cleanup may never be attempted. `F03` substitutes a
throwing `stop` dependency, runs the actual cleanup and finalization code, and fails:

```text
AssertionError [ERR_ASSERTION]: Final result missing when owned cleanup rejects:
Error: Injected ownership mismatch:chrome
```

This demonstrates a helper control-flow defect; it does not prove a real ownership
failure occurred in Run03 or assign that historical skip cause. No old helper changed.

Repair recommendation: separate revision preserving original bytes; attempt both
owned cleanup branches independently, retain partial receipts/errors, and guarantee
final-state/result persistence after cleanup failure. Mark failed/incomplete cleanup
explicitly and preserve nonzero failure; never infer native termination from a saved
result or terminate a child whose ownership cannot be proved. Re-run unchanged `F03`
and affected contracts; independent source/evidence review before any GUI launch.

## Preservation and limits

Fresh [preservation receipt](../../../chrome-extension/build/b05-offline-helper-20261102/preservation-04.json)
records website7/runtime74/tests46 matching the earlier frozen snapshot, unchanged
held ZIP SHA256`278b59df8ddef0085c9c064da11485695074419894c3f43b5077a10fc44f1e9e`,
and matching historical runners01–04/cases/Run03-result/final-postflight hashes.
No canonical product, Golden test or retained raw evidence edited.
Ignored script/raw links are local artifacts; not public uploads or runtime acceptance.
Full `pnpm test` not run: no product code changed and its headed E2E still needs a slot.

## Unresolved

Demonstrated cleanup/finalization defect needs repair, affected verification and
independent review. Fresh desktop handoff, guarded navigation/canaries and real
W01–W06 remain Pending; historical early-skip cause remains Unknown.
