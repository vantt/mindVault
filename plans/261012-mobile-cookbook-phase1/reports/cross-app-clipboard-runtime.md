# Chrome replacement / expired Dish ownership —15October2026

Status: **Null-safe repair build/JVM10/physical5 and strict Chrome return Passed; normal app owner-confirmed stable; Mobile Partial**.
Updated20October: recovered runner prepared in persistent storage; no active native
wait now. [Recovery01](clipboard-return-recovery-runtime.md) Failed at stale ownership
with Chrome marker intact; failure-tail01 also Failed with all11 retries exhausted
and ownership still present after1153ms. Actual read-only null-label probe established
the original expression's NPE; [minimal null-safe repair](clipboard-null-label-repair-runtime.md)
passed fresh build/JVM10/physical5 and unchanged strict Chrome return01 Passed99.870s.
Owner's proof close/failed-return reply is retained; subsequent normal app was
owner-confirmed stable. Exact prior close cause unknown. October16
run02 terminal is unknown and old `/tmp` raw evidence unavailable. Resume only from
the [current checkpoint](clipboard-return-handoff.md).
Baseline mainfc20fab. Initial preparation preserved production and all74 previous
app sources/manifests. The later evidenced repair changes only the two production
paths described below; every previous assertion and historical result is retained.

## Selected real boundary

`CrossAppClipboardPhysicalTest#actualChromeCopySurvivesExpiredDishCleanupOnReturn`
uses genuine UUID-owned setup/cache/Cook and unchanged `OwnerCopyObservation`.
Owner chose Chrome. The ready checkpoint is before actual UI Copy; owner then opens
a new Chrome tab and focuses its empty address bar. No message is required between
the owner's Copy and switching apps. No save/send/Enter/navigation action is issued.

New160-line test,86-line `ChromeClipboardAction` and28-line `PendingClipboardClaim`:

- Bind the real sensitive synthetic Dish Copy to its original production deadline
  and exactly one generation; prepare a masked ready Dish with visible enabled Copy.
- Read fixed, bounded installed Chrome UID metadata using UiAutomation's shell
  package query. Require Chrome UID differs from the process, target and test APK
  UIDs. This read adds no clipboard permission or helper APK.
- Observe actual PassChef stop, cleared session/Dish, same undestroyed Activity and
  no early return. Read only app-owned RAM/cache while offscreen. Require adapter
  foreground=false and original deadline/generation/label/value-presence still pending.
- Require a current Chrome root and focused, visible, enabled, editable, non-password
  EMPTY field in that window. Existing nonempty fields are left untouched. Perform
  actual remote SET_TEXT with a fresh public UUID marker, verify exact text, select
  all, verify selection and require ACTION_COPY success. Bounded refresh waits do
  not repeat mutations. Recheck current Chrome window/field immediately before and
  after Copy; post-Copy selection may legitimately collapse, exact text must remain.
- After the original deadline+1s, request owner return. Normal lifecycle/focus
  reconciliation must forget the old ownership while retaining its generation.
  Actual legal foreground clipboard read must retain the exact Chrome marker with
  a different label; observe it again after1s. Session/Dish stay locked and exact
  cache/jars/RAM remain. Cleanup never clears the Chrome replacement.

The target test never calls setPrimaryClip to write the replacement. Chrome's own
editable node performs the actual [ACTION_COPY](https://developer.android.com/reference/android/view/accessibility/AccessibilityNodeInfo.AccessibilityAction#ACTION_COPY).
This binds an observed different-UID app/action to the matching result; public
clipboard metadata does not supply an authenticated per-clip writer UID.

Remote Copy may occur after30s if the original app ownership is still pending while
backgrounded. There is no extra25s gate for this boundary, no restarted deadline,
and no assertion that the old Dish remained system-primary until remote Copy.
No clipboard reads occur offscreen and no manual reconcile is invoked.

## Preparation evidence

| Check | Actual result / scope |
|---|---|
| Build01 | Passed12s;10executed/84UP-TO-DATE |
| Final build02 | Passed9s;10executed/84UP-TO-DATE |
| Lint XML | 0errors/11warnings/1hint |
| Independent review | Passed after both-own-UID and fresh Chrome window/field guards |
| Frozen/current/archive | 77 app sources/manifests, old74 exact;30 historical helpers unchanged |
| Original baselines | Protected208/unrelated23 intact; `.agents/` and `skills-lock.json` preserved |
| JVM/browser suites | Not rerun; production and earlier assertions unchanged |
| Runtime01 | Failed354.918s; waiting for eligible Chrome editor expired180s after Copy |

## Actual Run01 — preserved Failed

Selected JUnit[1,-2], terminal[-1], native0, no watchdog/orchestration error;
failure phase `own-copy-observed-await-chrome-failed` at Copy+180073ms. PID29864,
fixture `8ca82a00-19c4-463e-8272-889ce3480444`, Chrome installed UID10184.
Owner reported Copy Dish and an empty focused Chrome address bar with keyboard.
No successful remote SET_TEXT/selection/COPY or return checkpoint was observed.
The silent editor-precondition rejection is not yet localized; this does not
establish a production clipboard defect or a successful external replacement.

Final sample shows the same Activity stopped, unfocused, locked, no session/Dish;
exact old74 sources,30 helpers, protected208/unrelated23 and owner digest preserved.
Exact UUID directory is absent. Original log/result/frozen/candidate remain unchanged.
Owner replies are bound separately to the selected method/UUID/frozen hash; recorded
reply time does not establish a physical Copy/switch timestamp.

## Read-only Chrome editor probes

`ChromeEditorProbePhysicalTest#focusedChromeEditorCapabilities` launches no Activity,
mutates no field and reads no clipboard. One sample emits16 fixed booleans, no tree,
unknown text, clipboard content or hint wording. JUnit success certifies sampling only.

| Probe | Actual bounded observation |
|---|---|
| 02 | Sampled; selected[1,0]/terminal[-1]/OK1/native0; focused Chrome editor, textEmpty=false, showingHint=false, SET_TEXT/SET_SELECTION present |
| 03, after owner manually cleared address bar | Sampled; selected[1,0]/terminal[-1]/OK1/native0; focused Chrome editor, textEmpty=false, showingHint=true, SET_TEXT present, SET_SELECTION absent |

Both samples require current Chrome root/field/window and report editable/enabled/
visible/focused=true,password=false. Probe02 does not establish what was in the
field throughout Run01. Probe03 exposes two incompatible preconditions in the old
helper for this empty-editor representation. Android16's [TextView implementation](https://raw.githubusercontent.com/aosp-mirror/platform_frameworks_base/refs/heads/android16-release/core/java/android/widget/TextView.java)
returns the hint as accessibility text when its input is empty, reports showingHint,
and offers SET_SELECTION only once text exists. This explains the sampled flags;
it is not an authenticated reconstruction of earlier unsampled phone states.

Build03 Passed7s,9executed/85UP-TO-DATE, lint0/11/1; source78/old77 exact.
Test APK `5d976536e8e420a69d8fe92484d321c8efce3ffd704966796f8fb24a660ffb2c`;
debug/release unchanged. Probe02/03 final owner/source/runner/local+installed APK/
old30 helpers/protected208/unrelated23 audit Passed. Probe01 prepared-only freeze/
archive/install preserved; numbered02/03 use reviewed strict16-flag result gates
and60s host timeout with partial-output/unknown-native-outcome preservation.

## Additive editor-selection variant — Run01 Failed

`CrossAppEditorSelectionPhysicalTest#actualChromeHintEditorCopySurvivesExpiredDishCleanupOnReturn`
is a separate160-line test. Apart from class/method/helper names, its orchestration
and every assertion are byte-identical to the original test. The88-line helper
accepts actual null/empty text or a shown nonempty hint exactly matching accessibility
text. Only SET_TEXT must exist initially; once populated, exact non-hint public
marker plus SET_SELECTION support are required before selecting. Exact full selection,
real Copy success, same current Chrome window, distinct UID and post-Copy marker
guards remain. Existing nonempty input is never overwritten.

These additive copies preserve the original fixture/assertions and failed candidate;
they are a scoped harness correction, not a production change. Original78 sources,
including the47-line probe, remain exact. Final build Passed10s,10executed/84UP-TO-DATE,
lint0errors/11warnings/1hint; inventory80, old32 host helpers unchanged. New test APK
`2e7c5edf319ff2d74e0dcc2646f4eccac24d90cc214dfde22dbb24a2f1034639`;
debug/release unchanged. Independent source/runner review clear; frozen01/candidate01
bind80 sources,32 old helpers and APKs. Install-r updated only the test APK; actual
installed hashes match frozen01 and owner digest remains exact before/after both
package checks. Its actual selected Run01 Failed124.584s, JUnit[1,-2]/terminal[-1],
native0, no watchdog/orchestration error. UUID `c23ea927-0e48-4182-af93-4cbf97bc74ab`,
same PID7249; actual Chrome Copy action observed at Copy+2808ms, still stopped/locked
at return request+31069ms, actual focused locked return failed at Copy+66090ms.
The generic `owner-return-to-passchef-now-failed` phase does not identify which
post-return cache/marker/ownership/UI assertion failed. This is not yet evidence
of a localized production defect or accepted replacement preservation.

Owner reported actual Copy/switch and returning without Cookbook unlock. Final owner
digest, old78 sources,32 helpers,protected208/unrelated23 and exact UUID absence all
Passed. Preserve raw log/JSON/frozen/candidate and owner replies unchanged. A separate
additive diagnostic variant will label each post-return invariant and emit only
legal-foreground clipboard presence/equality booleans; all existing assertions stay.

## Additive return diagnostics — Run01/02 Failed; stale claim localized

`CrossAppReturnDiagnosticsPhysicalTest#actualChromeReplacementReturnDiagnostics`
adds distinct checkpoints before each unchanged return assertion. Its181-line test
uses the same reviewed Chrome helper and real original deadlines. Only after the
existing awake/unlocked/focused/resumed own-Activity guard may its actual clipboard
read emit present/oneItem/labelDiffers/markerExact booleans. The30-line
`ClipboardReturnObservation` reads only own RAM: adapter foreground, value/label
absence, generation equality, original/zero deadline and original-label equality.
Neither diagnostic writes clipboard/policy nor manually reconciles ownership.

Original80 sources/assertions and33 host helpers remain exact; build01 Passed8s,
10executed/84UP-TO-DATE, lint0errors/11warnings/1hint. Inventory82; test APK
`ec93b9c0bd7223438794b2461c5599b3f9b99fbaa82df59e89d3ba92789c3cf5`;
debug/release unchanged. Reviewed frozen01/install01 bind actual installed APKs;
owner digest unchanged. Actual Run01 Failed48.998s,selected[1,-2]/terminal[-1],
native0/no watchdog/orchestration error. At Copy+2980ms it failed in
`own-copy-observed-await-chrome-failed`, before any remote Copy/return diagnostics.
Owner explicitly reported pressing Copy Dish more than once, outside the required
single original generation/deadline. Generic diagnostics do not independently
identify the exact assertion. Preserve the failed run, UUID
`c6c93aa4-e5fd-4ed7-be26-047525f68e93`, PID12094 and bound owner observation.
Final owner/old80/helpers33/protected208/unrelated23/UUID absence audit Passed.
Frozen02/candidate02 retain identical82 sources/APKs/runner for a single-Copy retry;
no rebuild, installation or assertion change required.

Actual diagnostic02 Failed105.461s,selected[1,-2]/terminal[-1]/native0, no watchdog/
orchestration error; UUID `008e6df0-3a12-4814-b401-40b3bcbdd482`, PID13830. Owner
confirmed one Copy and returning without unlock. Chrome Copy observed at Copy+3769ms,
return requested+31022ms. Same locked Activity/cache/jars and original deadline
checks passed. The legal focused clipboard sample at+51677ms reports present,
oneItem,labelDiffers,markerExact all true: the Chrome replacement was intact.

The next unchanged assertion failed at+51679ms:
`return-claim-forgotten-check-failed`. Earlier own-RAM sample reports foreground=true,
generationSame=true,deadlineOriginal=true,labelStillOriginal=true, while valueAbsent/
labelAbsent/deadlineZero=false. The failure is expired ownership metadata remaining,
not loss of the Chrome marker. One-second stability/UI-absence checks were not reached.
Owner/old80/helpers33/protected208/unrelated23/UUID absence audit Passed.

Separate sanitized ClipboardService metadata within this actual run contains3 own-
package denial events with the fixed reason “application is not in focus”. No raw
log line, clipboard value or unknown text was saved. Together with source callbacks
that reconcile before platform clipboard focus is available and never retry a null
read, this supports a focused-return retry repair. Exact callback timing for earlier
unsampled failures is not established. Production repair is being planned; all old
assertions and failed candidates remain unchanged. Overall acceptance stays Pending.

## Focused-return repair — physical acceptance Pending

Evidence above justifies two production changes. `MainActivity` now passes actual
decor focus onStart and both focus true/false events to the process-owned adapter.
`OwnedClipboard` keeps its original30s cleanup Runnable and policy deadline/token.
A separate retry Runnable handles unavailable expired reads: one immediate dispatch,
then at most ten100ms retries, guarded by focus, captured ownership generation and
expiry. Repeated failed reconcile calls cannot replenish the budget or queue duplicate
work. Focus loss, successful Dish/username Copy, non-owner forgetting and owned clear
cancel retries. Null/exception never authorizes forgetting or overwriting clipboard.

Both changes independently reviewed. Build01 Passed28s,28executed/136UP-TO-DATE;
fresh app JVM10/0failures/errors/skips; lint0errors/11warnings/1hint. Every existing
test/assertion remains unchanged. Inventory82:80 exact previous sources plus these
two deliberate production deltas. Original protected208 manifest is preserved:
206 remain byte-exact and2 are explicitly bound before/after; unrelated23 and34
historical host helpers remain exact. Historical failed candidates/APKs remain.

New debug `d90cf0e393d181c9b71f3fc4561a715c5be59578c98be9f1c96fb6c709233dcc`;
test unchanged `ec93b9c0bd7223438794b2461c5599b3f9b99fbaa82df59e89d3ba92789c3cf5`;
new release `153b0224a6d3913a0060f08877406189b232b6a276e1629ad808f4a394a267e1`.
Release manifest/package/backup and all3DEX inspected: no proof Activities/descriptors,
PassChefProof tag, debug network-state/camera/Autofill permission. This is artifact
inspection, not broad runtime/device coverage. The old release remains archived.

New repair support/runner bind the exact two-path delta, source/APKs, helpers and
historical preservation without editing prior baselines/runners. Strict diagnostic
and existing foreground adapter/recreation tests will run selectively after install
and fresh native preflight; no altered assertions, test waits or manual reconcile.

Repair selected Run01 Failed182.282s during genuine setup, before Cook/Copy:
`owner-replacement-setup-biometric-failed`, copiedAt=-1, PID17606, UUID
`588b4cb1-2bcb-489d-816a-bf3090abf167`. Selected[1,-2]/terminal[-1]/native0,
no watchdog/orchestration error; owner/source/delta/helper/UUID cleanup audit Passed.
Owner reported fingerprint action and a remaining fingerprint dialog. A separate
own-PID/actual-run-window diagnostic records two `wrap-doFinal` numeric code15
events. No raw messages/key aliases were collected. This identifies observed setup
failures, not their cause or a clipboard-repair regression; accepted current-Xiaomi
Keystore scope is not reopened. This run earns no repaired clipboard acceptance.

Relevant unchanged adapter regression01 Passed3/3,99.636s on the repair candidate:
untouched Dish clears after its actual30s, same-UID synthetic replacement survives,
and a newer Dish survives the old deadline then clears at its own. Each exact method
has[1,0],terminal[-1]/anchoredOK3/native0, no failures/errors/skips/watchdog/orchestration
error. Final installed/source/2delta/34helper/206+2/23/owner audit and proof-directory
set preservation Passed. These locked proof Activities require no Cookbook session;
this regression does not substitute for authenticated Chrome return acceptance.

Unchanged recreation regression01 Passed1/1,31.548s: an actual replacement Activity
retains the original clipboard label/value before its original30s deadline and the
existing production cleanup clears afterward. Exact selected[1,0]/terminal[-1]/OK1/
native0, no failures/errors/skips/watchdog/orchestration error; owner/APKs/source/
delta/helpers/protected/unrelated/proof-directory set audit Passed. This is locked
adapter Activity recreation, not authenticated recreation or process death. The
repair candidate thus retains these4 relevant existing physical tests unchanged.

Frozen02/candidate02 preserve the identical repair source/APKs/inputs for another
strict diagnostic attempt after fresh owner readiness. No rebuild or install is
needed; setup Failed01/code15 and all earlier failures remain separately preserved.

Frozen01 debug remains `1f42a2c4b369492b0ed804fab3ec282882e831336cf4ebfec48dbdba05bfd772`;
new test APK `b86829cdd4019d03be5f735de13a42f2c8da67c6387eec8fd382f953e4f61b63`;
release remains `efd3b38644db2404e42faca714c963ef99467c644524833455dff32c11f6b653`.
Install-r updated only the test APK; installed debug/test hashes equal frozen01.
Owner Cookbook digest before/after each package check remains
`894f73e51e316b6a369368ad4cebbc901609ccfe7eabe855aa73c2cbf4e7a4d6`.

The169-line runner binds the exact selected method, source/APKs/helper/archive and
baseline. Strict selected[1,0]/terminal[-1]/anchoredOK1/native0/finalphase, no watchdog/
orchestration/failed diagnostics plus final owner/source/owned-directory cleanup
are required. The900s outer bound covers multiple native180s owner phases; it never
extends production session or clipboard deadlines. Ready owner/fresh native device
checks precede actual execution. Logs never serialize Chrome trees, unknown field
contents, clipboard values, account data or Dish/Spices.

Evidence outside repo: `/tmp/passchef-cookbook-device-acceptance/cross-app-clipboard-`
prefix for baseline01,runner,frozen01,candidate01,build01/02/03 logs,build-audit01,
build01-sources snapshot,install01,run01 and owner-observation01. Additional
`chrome-editor-probe-` prefix holds runner,frozen/candidate/install01/02/03,
actual run02/03 and owner-observation03; `chrome-editor-replacement-` holds
the additive runner,baseline01,build01 log and build-audit01.
Frozen HEAD remains historical after commits.

Unresolved: actual Chrome replacement runtime; authenticated recreation/process-death
clipboard; long-background/natural LMK/saved-task restoration; Google cancellation/
stale/401/403; broader accessibility and historical failure causes. Mobile Partial.
