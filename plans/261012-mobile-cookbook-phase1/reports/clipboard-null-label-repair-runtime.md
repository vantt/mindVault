# Null-label clipboard repair —20October2026

Status: **Build/JVM10, physical5 and selected strict Chrome return Passed; owner confirms normal app stable; Mobile Partial**.
Baseline main `fcee101dd1ff63c4fcb6f8bf4341dbbbb6dc11c3`.

## Evidenced defect and bounded correction

[Recovery01 and failure-tail01](clipboard-return-recovery-runtime.md) remain Failed.
The latter exhausted11 retries and retained expired ownership after1153ms; final
legal reads found the actual Chrome replacement intact. A separate read-only probe
matched that prior public UUID marker, then observed a null label and an NPE from
the original label expression. Actual old debugAPK classes8.dex contains the same
unguarded `getLabel` → `Object.toString` sequence. Probe01 Sampled1.198s; no Cook,
authentication, clipboard write or policy reconciliation. This establishes the
null-label failure path, not every historical retry exception or a crash cause.

`OwnedClipboard.kt` changes exactly one expression to
`clip.description.label?.toString() != label`. A null foreign label now enters the
existing non-owner branch, forgetting expired own metadata without clearing the
replacement. Original deadline/generation/digest/focus/retry rules are unchanged.
All existing assertions and84 other source/manifests remain byte-exact to the85-file
probe candidate; one new129-line same-UID null-label physical regression makes86.

The new regression performs a real adapter Copy, places its unique public null-label
replacement, observes a fixed30.5s with normal production Handler timing, then
asserts preserved marker, unchanged generation, absent ownership, zero deadline and
no queued retry. No manual reconciliation, fake elapsed time or session bypass.
It certifies a locked same-UID adapter boundary only; actual Chrome return is separate.

## Frozen build and install

Persistent evidence:
`/home/vantt/.local/share/passchef-mobile-evidence/2026-10-20-null-label-repair/`.
Reviewed `repair-support.py`/`repair-runner.py`, `repair-frozen-01.json` and
`repair-candidate-01` bind86 sources,327 unchanged canonical non-Markdown paths plus
the exact reviewed production delta, raw checkout bytes including clean CRLF, both
helpers, prior probe/raw/bytecode bindings and all APKs. Prior candidates remain intact.
Frozen SHA256 `1e916985a45c18dd8c0e508a6968f4dc2ba12c4071740d8d0d8a88274dfee38f`.

- Build Passed45s,31executed/133UP-TO-DATE; fresh app JVM10/0failures/0errors/0skips.
- Lint0errors/11warnings/1hint. Fresh JVM XML and lint report retained externally.
- Debug `37579b452315adb28067a29a1c46f43731082580d2b1d760ec05147b06691a6c`.
- Test `d05365630f0d2b2474e4f873c2fe19fac6237c676192192c69f550ed759ca6b6`.
- Unsigned release `92fa8896fcba7224267d63cff0388fcdcb27bb775e4abbe05463b1450961369e`.
- Debug/test install-r both native0/Success, actual installed identities matched;
  owner Cookbook digest unchanged before/after each installation. No uninstall/data clear.

Native profiles select exactly one method per invocation. Strict result requires
JUnit[1,0],terminal[-1],OK1,native0,no failed status/watchdog/orchestration error and
its final success phase. New regression also requires its final phase. Every run
audits owner/installed/local/source/archive/prior bindings, exact owned UUID absence
and preservation of the preexisting proof-directory set. Non-strict profiles can
Pass their own boundary while `acceptance` stays Pending for the strict repair.
Preexisting proof directories are preserved, never cleaned by unrelated runs;
an absence sample proves only its observation time, not continuous absence.

## Physical results

| Profile01 | Duration | Result and boundary |
| --- | --- | --- |
| null | 31.832s | Passed; new real-expiry null-label replacement preserved, ownership forgotten |
| untouched | 31.534s | Passed; unchanged actual30s owned Dish cleanup |
| external | 31.544s | Passed; unchanged labeled replacement survival |
| newer | 36.633s | Passed; unchanged newer Dish survives old deadline, clears at own deadline |
| recreation | 31.552s | Passed; unchanged locked same-process Activity recreation/original deadline |

Each selected method independently reports JUnit[1,0]/terminal[-1]/OK1/native0,
0failures/errors/skips, no watchdog/orchestration error. Each final owner/installed/
source86/checkout327+delta/helper/APK/archive/prior bindings/own-UUID-absence and
preexisting proof-directory-set audit Passed. New regression final phase
`null-label-regression-passed` matches its UUID. All original methods/assertions
remain unchanged; five method runs are distinct scoped executions, not a whole
instrumentation suite. Each regression's `acceptance` field stays Pending; strict
repair acceptance comes only from the separate unchanged case below.
## Actual strict Chrome return01

Unchanged `CrossAppReturnDiagnosticsPhysicalTest#actualChromeReplacementReturnDiagnostics`
**Passed99.870s**, JUnit[1,0]/terminal[-1]/OK1/native0,0failures/errors/skips; no
watchdog/orchestration error. UUID `54a18fa3-9cf7-44d5-8211-e098bb0bcae4`, PID23063,
actual Chrome UID10184. Genuine biometric setup/Cook and exactly one owner Copy.
Own Copy observed+23ms; actual Chrome Copy+3644ms; return requested+31036ms.

Legal focused same-Activity locked return at+56967ms retained exact cache/jars,
cleared session/Dish and original generation; expired ownership value/label were
absent and deadline zero at first RAM sample. Public Chrome marker remained exact
at+56973ms and again+57990ms after the unchanged1s stability interval. Single Copy,
no Dish-display/Copy controls and final+58015ms success assertions all Passed.
Final owner/installed/source86/checkout327+delta/helper/APK/archive/prior bindings/
exact owned-UUID absence/preexisting proof-directory-set audits Passed.

Owner's exact return reply was **“Không quay lại được hoặc app đóng”**. This is
retained in `strict-owner-observation-01.json`, not replaced with a positive visible
return claim. The selected native clipboard boundary Passed; owner-observed stable
return UX is separate. The original test closes its proof Activity in finally after
these assertions, which may explain a close but does not prove its exact timing or
the cause of every observed close. Selected PID crash-buffer scan found no fatal
metadata; this is not proof of crash absence.

## Normal launcher and owner observation

After instrumentation finished, normal `MainActivity` was explicitly opened without
authentication, clipboard access or a test Activity. Initial host observation01
could not establish its foreground: the activity selector missed Android16's resumed
record and the `window windows` subsection omitted current focus. Its false gates
remain saved, not reclassified as a production failure or used as acceptance.
Corrected read-only observation02 sampled the already-open app (no relaunch) at
0/5/10/20s: same PID24514, own MainActivity resumed/focused, awake/keyguard absent
all true. Owner digest, installed identities and frozen inputs remained exact.
These four samples do not establish continuous pixels or the prior close cause.

Owner then answered **“Đang thấy Cookbook, app không tự đóng”**. Exact question/reply
is saved in `normal-main-owner-observation-01.json`, bound to observation02 and the
earlier strict reply. This confirms the normal app stayed visible to the owner at
that checkpoint; the earlier close report and its unknown exact timing remain intact.
No native test/Gradle/owner wait is active. No rerun of unchanged accepted cases needed.

Unresolved: exact historical cause/timing of observed close/toast;
authenticated recreation/process death/history and broader Mobile provider/accessibility matrix.
