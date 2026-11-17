# B05 — offline helper repair preparation

Date:2026-11-02, Asia/Saigon. **Latest revision06 offline verification:36/36 Passed;
[independent source/evidence review Passed](b05-helper-repair-independent-review-20261102.md); final bookkeeping review Passed. B05 remains Partial/W01–W06 NotRun.**
Earlier revision05 implementation/syntax snapshot and failed verification stay below.
Parent:[offline continuation plan](b05-offline-continuation-plan-20261102.md).
Historical [slot01](b05-local-qa-runtime-20261102.md) early-skip remains Unknown.

## Demonstrated issue and scoped change

Tester retained [revision04 evidence](../../../chrome-extension/build/b05-offline-helper-20261102/verification-04.json):
15Passed/1Failed; F03 shows rejected owned cleanup suppressed final result persistence.
This is a demonstrated finalization defect, not the historical early-skip cause.

New ignored [runner-05.mjs](../../../chrome-extension/build/b05-offline-helper-20261102/runner-05.mjs)
is167lines. Original runners/cases stay unchanged. Changes:

- Stop Chrome/server independently; retain partial receipts/errors when either fails.
  Failed ownership/absence checks remain fail-closed; no unknown process termination.
- Assign cleanup receipt before its file save, retain save errors and continue final
  result persistence. Unknown parent absence remains false; root descendant/port/
  profile postflight is still mandatory.
- Idempotent finalization retains fatal/budget/cleanup state and clears both timers
  even if cleanup rejects. Cleanup failures produce nonzero exit. Timer rejection
  handlers prevent unhandled cleanup promises; hard reserve also finalizes before exit2.
  A final-result disk write failure emits explicit full fallback evidence on stderr
  and exits nonzero; this cannot guarantee a successful disk write.
- Require exactly `--slot-start <UTC ISO timestamp>` before output/profile directories,
  timers or child launch. Reject missing/malformed/calendar-invalid/future/expired
  start and exhausted budget. Derive15minute deadline/12minute cleanup checkpoint;
  record chosen slot start. Passing an argument does not confer desktop authorization.
- Import unchanged slot01 cases by their correct relative path. No website, product,
  canonical test/Golden, network policy, cooking behavior or runtime case changes.

## Identity and syntax verification

| Item | SHA256 |
|---|---|
| New runner05 |`12f29fb867282a1844db34677033c549f302b0e9f5c6f9b13235c550b4b72d48`|
| Immutable runner04 |`5eb5a3555723d58fb91cf99e1e900928f5f2dd6baedc42fafa18c348895c63c4`|
| Immutable cases |`b839ecdbc3eebace1aecc5384087253d0a0ed63a20f44578b70c220d465da6f4`|
| [Exact107line diff](../../../chrome-extension/build/b05-offline-helper-20261102/runner-04-to-05.diff) |`0c0ed6350fcdd85b4291401c2db23058ec67087f59160061545351bccb720e3e`|

`/home/vantt/.nvm/versions/node/v24.18.0/bin/node --check
chrome-extension/build/b05-offline-helper-20261102/runner-05.mjs` exit0.
Builder re-read source/diff. No full runner/body, browser/server/child process,
network, clipboard, account or desktop action executed. Behavior still requires tester.

Current proposed helper is revision06 below. After independent review and fresh
exclusive15minute handoff, root may bind actual approved UTC start to this command;
placeholder is deliberately invalid:

```bash
/home/vantt/.nvm/versions/node/v24.18.0/bin/node \
  chrome-extension/build/b05-offline-helper-20261102/runner-06.mjs \
  --slot-start '<APPROVED_UTC_START>'
```

Fresh guarded-navigation/canary proof precedes website acceptance. Source review,
syntax or offline contracts never establish live isolation or W01–W06 Passed.

## Unresolved

Final bookkeeping review Passed; fresh runtime slot/canaries/cells/postflight. Root retained
[revision06 freeze receipt](../../../chrome-extension/build/b05-offline-helper-20261102/freeze-06.json), binding runner/cases/checker/result hashes; no runtime executed. Revision06
[behavioral verification](b05-helper-repair-verification-20261102.md) Passed36/36. Old skip cause Unknown;
full B05, OG/public URLs and owner/deployment/release gates unchanged.

## Revision06 implementation checkpoint —2026-11-02

Tester [revision05 evidence](../../../chrome-extension/build/b05-offline-helper-20261102/verification-05.json)
retains35Passed/1Failed: original16 checks passed, supplemental R11 demonstrated
bounded `stop()` returning `parentAbsent:false` left host exitCode0. This additional
helper defect is retained separately from the historical early-skip Unknown.

New ignored [runner06](../../../chrome-extension/build/b05-offline-helper-20261102/runner-06.mjs),
169lines, changes only successful `stop()` receipt handling: any existing child
without explicit `parentAbsent:true` records `stop:<label>` failure and nonzero
exit, while continuing the second branch. An unspawned/undefined child creates no
false cleanup failure. Ownership checks/signals unchanged; no retry or unknown
process termination. Revision05 and its evidence remain immutable.

Runner06 SHA256`793c7085aa2e6f529093b4920eb6a3eb5bd8e6b4978d1d34a59c669c544b40d0`;
[exact05→06 diff](../../../chrome-extension/build/b05-offline-helper-20261102/runner-05-to-06.diff)
SHA256`4bf184310b6da4c486018fa2267a8d4e093e41622b684d6328cd591fa78dd85e`.
Node24 absolute `--check` exit0; builder re-read minimal diff. No runner launch,
GUI/server/network/child process or canary work. Behavioral rerun of unchanged
revision05 checker against06 and independent review remain Pending.

Unresolved at implementation checkpoint: tester/reviewer verdict, fresh-slot guard/canary preparation,
actual runtime and cleanup; B05Partial/W01–W06NotRun and original skipUnknown unchanged.
