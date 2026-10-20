# Clipboard return recovery —20October2026

Status: **Recovery01 and failure-tail01 Failed; subsequent null-safe repair strict case Passed; Mobile Partial**.
Main baseline `fcee101dd1ff63c4fcb6f8bf4341dbbbb6dc11c3`.

## Evidence availability and reconstruction

Old `/tmp/passchef-cookbook-device-acceptance/` logs/helpers/candidates are unavailable
after restart. Historical repository reports retain their recorded outcomes; old
raw evidence was not reconstructed or re-certified. October16 repair run02 was
launched (UUID5c68d856…), but terminal outcome is unknown. October15's “unlaunched”
checkpoint was historical, not the state after that launch.

New persistent root:
`/home/vantt/.local/share/passchef-mobile-evidence/2026-10-20-clipboard-return/`.
Reviewed recovery helpers freeze82 exact committed Mobile sources,328 canonical Git
identities and their exact checkout bytes,2 host helpers and3 existing APKs. A clean
CRLF checkout difference is preserved, not falsely classified as WIP. No rebuild or
installation was needed for recovery01. New audits do not claim the lost208/34-helper
baselines remain independently verifiable.

Recovery frozen01 SHA256:
`6019a8556682edb3d75916db99eb03f17907b8284fc9caa5048b3469db55a258`.
Actual device transport1, serialPN7PW8SGXKLVPRIV, Xiaomi2407FPN8EG/API36; owner
approved normal USB debugging. Fresh awake/keyguard checks, owner Cookbook SHA
894f73…a4d6 and actual installed debugd90cf0e3/testec93 matched before/after runtime.
No grant/session/clock injection, test edits, manual reconciliation or offscreen
clipboard read was introduced.

## Actual recovery01

Selected `CrossAppReturnDiagnosticsPhysicalTest#actualChromeReplacementReturnDiagnostics`.
Failed145.299s,JUnit[1,-2]/terminal[-1],native0,no watchdog/orchestration error.
UUID `8f7807ee-01a0-47f5-85db-f139c23449a8`, same PID27371, Chrome UID10184.
Genuine biometric setup/Cook completed; owner confirmed one Copy and locked return.

- Actual own Copy observed+19ms; actual Chrome Copy action+24439ms.
- Still stopped/locked at return request+31115ms; focused locked return checks begin
  at+103192ms. Exact cache/jars/RAM and original deadline assertions passed.
- Legal foreground clipboard sample+103201ms: present/oneItem/labelDiffers/markerExact
  all true. The actual Chrome replacement was intact.
- `return-claim-forgotten-check-failed` at+103203ms. Own RAM showed foreground=true,
  generationSame/deadlineOriginal/labelStillOriginal=true; value/label absence=false.
- Later1s stability/UI absence assertions were not reached. Owner digest, source82,
 328 checkout identities, helper/APK/archive and exact UUID absence audits Passed.

Safe separate ClipboardService metadata records two own-package denied/not-in-focus
events within the actual run interval. No raw log text or clipboard values saved.
Neither this nor the first return sample establishes indefinite metadata persistence
or exactly when a queued retry ran. The strict failure remains unchanged.

## Additive failure-tail preparation

Separate `CrossAppFailureTailPhysicalTest#actualChromeReplacementFailureTailDiagnostics`
preserves the complete original181-line test body after removing only new names and
four failure-observation lines. All assertions/order/sanitized failure stay intact.
Only after the existing claim-forgotten assertion fails does a bounded diagnostic
sample own RAM immediately, after150ms and after another1000ms. Sleeps occur on the
instrumentation thread; no wait-for-success, reconciliation or policy/clock mutation.
Final clipboard booleans from the adapter and Activity managers require separate
legal foreground guards. A captured failure stays Failed even if a later sample
finds natural cleanup; missing samples are incomplete evidence, not success.

New files185/87lines independently reviewed; original82 sources unchanged. Build
Passed76s,10executed/84UP-TO-DATE; lint0errors/11warnings/1hint. No new JVM suites.
Debugd90cf0e3/release153b unchanged; test APK
`de93be29ab36c8c2daf3c64aaa935602952b9897af89cdfb5157fa7fcba281df`.
Persistent next-candidate root:
`/home/vantt/.local/share/passchef-mobile-evidence/2026-10-20-failure-tail/`.
Host helper binding/review and freeze Passed:84 sources (82 unchanged+2 reviewed),
328 committed non-Markdown checkout identities, prior recovery evidence bindings,
helpers and archived APKs verified. Frozen SHA256
`4e13c725001e5fd98ab078d8c42f3ee8991eb5f4758d596ae708fbef0fd78779`.
Test-only install-r Passed (`failure-tail-install-01.json`); actual installed
debug/test match frozen identities, owner digest unchanged before/after, device
authorized/awake/keyguard absent at install audit.

## Actual failure-tail01

Failed156.066s, JUnit[1,-2]/terminal[-1], native0, no watchdog/orchestration error;
UUID `47610d32-9610-4a6d-93e8-e0ded71b16c8`, same PID10684. Own Copy observed+126ms,
actual Chrome Copy+4141ms, return requested+31058ms. Focused locked cache/session,
original deadline and intact replacement checks passed at+58073…58080ms. Original
claim-forgotten assertion failed+58083ms; all later original assertions unreached.

| Tail sample | Since Copy | Retry attempts / queued | Own metadata | Legal foreground |
| --- | --- | --- | --- | --- |
| 0 | 58084ms | 3 / true | Original value/label/deadline/generation retained | true |
| 1 | 58235ms | 4 / true | Original value/label/deadline/generation retained | true |
| 2 | 59237ms | 11 / false | Original value/label/deadline/generation retained | true |

Only after final RAM snapshot, both adapter and Activity managers legally read the
public Chrome marker: present/oneItem/labelDiffers/markerExact/readSucceeded all true.
Thus the one-second retry budget ended with stale ownership; no delayed success was
observed. This does not by itself reveal null versus exception for each retry read.
Final owner/source84/checkout328/helper/APK/archive/owned-UUID-absence audits Passed.
Actual raw/result, exact owner replies and sanitized device metadata are retained
under the failure-tail evidence root. Two own-package ClipboardService denied-focus
events were found within the run; selected PID crash-buffer scan found no matching
fatal metadata. Absence of crash metadata is not proof no crash occurred.

Owner reported a white screen/close and approximate “Passchef copied from clipboard”
toast; exact toast/action timestamps unknown. Original test finally closes its proof
Activity and removes its fixture after failure. Do not classify owner-observed close
as a production crash or definitively attribute its cause from this alone.

## Null-label hypothesis and read-only probe preparation

Read-only actual debugAPK disassembly (classes8.dex) and compiled class both show
`getLabel` followed directly by `Object.toString`, without a null guard. Production
`clip.description.label.toString()` may therefore throw before the non-owner branch
forgets metadata. Existing tests deliberately compare `label?.toString()`, so their
`labelDiffers=true` does not establish that Chrome supplied a non-null label.
This is a concrete hypothesis; actual null/NPE classification is still required.

Separate89-line `ClipboardLabelNullProbePhysicalTest#actualPriorChromeMarkerLabelClassification`
launches a fresh locked UUID fixture without authentication/Cook/Copy. Legal secure
foreground and exact single-item prior public Chrome marker are required before
classifying labelNull/directExpressionSucceeded/exceptionIsNpe/nullsafeLabelNull.
No clipboard/policy writes or manual reconciliation. Success means **Sampled** only;
all84 previous sources/assertions and prior failures stay unchanged.
Build Passed18s,10executed/84UP-TO-DATE, lint0errors/11warnings/1hint; no JVM rerun.
Debug/release unchanged; testAPK
`14519a9896557ed9144b0dec2e01b066f8298a921a0de9ed16303b2b6b224cee`.
Persistent candidate root `2026-10-20-label-probe/` under the same evidence parent;
Host review/freeze/install Passed; frozen SHA256
`d63cc25c28b3dbfbf214d7db976472646bc42e3d7d5e2feaa8910d9ddbad2efd`.
Actual probe01 **Sampled1.198s**, JUnit[1,0]/terminal[-1]/OK1/native0, no watchdog/
orchestration error. New UUID `db9456e5-8bd0-45d9-8018-f0a903937010`, PID15609.
Legal locked/secure foreground and exact prior public marker gates passed. Actual
classification: labelNull=true, directExpressionSucceeded=false, exceptionIsNpe=true,
nullsafeLabelNull=true. Final source85/checkout328/input/archive/APKs/owner digest
and exact owned-UUID absence passed. No clipboard write or Cook/auth was performed.

Together with actual prior-marker provenance and compiled unguarded dereference,
this establishes a null-label failure path before production's non-owner forget.
It explains how a valid Chrome replacement can repeatedly throw through the retry
handler; it does not identify every earlier retry exception or establish the cause
of the owner's observed close. Minimal null-safe label comparison and an additive
actual30s null-label adapter regression are documented in the
[current repair candidate](clipboard-null-label-repair-runtime.md). Original failures
remain Failed. The unchanged strict Chrome-return case subsequently Passed99.870s
on the minimal null-safe repair; see that report for exact scope and both owner replies.

Unresolved: exact historical close timing/cause; authenticated recreation/process death/history and broader Mobile
provider/accessibility matrix. No previous failure or unknown result was relabeled.
