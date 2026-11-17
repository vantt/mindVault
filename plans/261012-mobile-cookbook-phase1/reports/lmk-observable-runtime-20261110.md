# Observable LMKD runtime —10November2026 (Asia/Saigon)

Status: **Diagnostic prerequisite Passed; L3 recovery Pending**.

External evidence:
`/home/vantt/.local/share/passchef-mobile-evidence/2026-11-09-lmk-observable-baseline/`.
The packet was prepared9November; the actual operations below occurred10November.

## Actual empty guest

Independent preflight verified35 pure guards,15 host files,13 dependencies,
source205/archive/three APKs and118 protected SDK/image/tool/AVD files. The new
nonce was absent; no earlier VM/userdata was reused.

One actual launch **BootedOwnedUserdebugIdle**,201.818s. Nonce
`PassChefLmkObserve_20261109_3345129b`, emulator5580, installed API36.1 Google APIs
revision4; actual2GiB/two cores/512MiB heap/6GiB data and owned disk path verified.
Persisted actual launcher receipt and current Windows parent/child lineage match.

Collection **CollectedForReview**: one scoped guest-only adbd root transition,
UID0, SELinux Enforcing unchanged, actual same guest lineage before/after. Both
idle samples show running LMKD PID221 and readable PSI/vmstat/meminfo/zRAM.
Source205/APKs/all118 protected files and the physical owner Cookbook remained exact.
No Cookbook/pressure APK, Google operation or workload occurred in this collection.

## Preserved monitor failure and separate repair

The frozen monitor parser rejected its actual descriptor inventory at its64-FD
capacity guard. Both original snapshots retain `UnavailableOrChanged`; successful
FD listings and absent fdinfo reads exclude a permission-denial explanation here.
A separate bounded read observed95 descriptors and reproduced that guard failure.

New external `monitor-followup.py`, reviewed SHA256
`94eea5bc346af1a574cdd455a3758132e5254a7a15da7f9a257f376e4cc24c7a`,
retains original category parsing/identity/read limits and expands only the unique
descriptor bound to512. Actual95-FD/duplicate/capacity guards and compilation Passed;
independent source/correlation review Passed before execution. Frozen helpers and
original results remain unchanged.

One actual followup **CollectedForReview**: two samples15.755s apart, same PID221,
98 unique descriptors before/after each sample, stable monitor categories. EpollFD3
references PSI FDs6 and7; third PSI FD20 is opened but unreferenced. All33 commands
are read-only. Independent runtime review Passed. Sequential open-FD/epoll interest
does not prove trigger health, victim selection or any actual low-memory kill.

## Remaining work

Actual reason3 for the original target PID/window, retained task/nonnull saved
state, locked recovery, genuine password/cached Cook and cleanup remain required.
Any workload must use a separate reviewed current-source/owned-guest adapter and
bounded control-plane monitoring. This diagnostic alone closes no L3/product gate.
The [two subsequent controlled attempts](lmk-controlled-runtime-20261110.md) both
remained NotExercised; their original native fixture cleanups Passed.

## Owned VM disposal

Original cleanup dispatched one actual `emu kill` with exit0, then its5s host
control call timed out; original `cleanup-result.json` remains Unknown. No second
kill was sent. Separate recovered-absence checks observed the original processes,
listeners and emulator serial absent before bounded archive/removal reconciliation.

`disposal-recovery-result.json` now records **DisposedOwnedVmAfterRecoveredAbsence**:
exact owned temporary root removed, existing archive command exit0, source205/all118
protected SDK/image/tool/AVD files and physical owner Cookbook exact. Retained archive
SHA256:`5b594194d712b4e7fb0ff819a5d4d7de5c07644710a3cd4ee3a5cb34deb54305`.
Root's actual disposal result Passed; independent final disposal audit Passed.
This closes owned-resource cleanup only, without changing original Unknown or L3.
