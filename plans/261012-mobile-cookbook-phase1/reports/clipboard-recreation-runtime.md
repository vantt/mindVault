# Android clipboard Activity recreation runtime

Date2026-10-13 (Asia/Saigon). **Passed1/1,31.721s;0 failures/errors/skips**.
Actual Xiaomi14TPro/API36; fixture requires API28+. Mobile remains **Partial**.
Root alone used native Windows ADB over authorized USB, serialPN7PW8SGXKLVPRIV.
Independent source review found no P1/P2; independent runtime parser confirmed the
single method's start1/pass0 statuses and terminal `OK (1 test)` without skips.

## Exact candidate and preservation

Final incremental build Passed33s:9 tasks executed,85 UP-TO-DATE; lint0 errors,
11 warnings,1 hint. These are compilation/lint results, not another JVM rerun.

| Artifact | SHA256 |
|---|---|
| Frozen recreation fixture | `e1798e3503a21feac1ac672f8917df7e0952875778c6fb204e4ec9f67881153c` |
| Installed test APK | `a2b1622ed2f857bb2c70a5a1698206ab134599c0816cb7e829f4a65792907680` |
| Unchanged debug APK | `d4eb8401d0b2cc71f9ca800614dc7f7d8cf391d3e3432cd6a7537ce91382f9a4` |

Only test APK updated with `install -r`; installed hash matches the candidate.
Original owner Cookbook digest before/after install and after runtime remains
`894f73e51e316b6a369368ad4cebbc901609ccfe7eabe855aa73c2cbf4e7a4d6`.
Only digest read; no owner-jars operation or data clearing. Earlier3 adapter tests
and8 platform tests retained without rerun; existing assertions/production unchanged.

## Actual selected test

After actual Awake/keyguard=false preflight, root selected only
`dev.passchef.mobile.proof.ClipboardRecreationPhysicalTest`.
Run-start metadata:2026-10-13T02:40:43.930358+00:00; this is not completion time.

The locked isolated proof Activity copied one synthetic Dish through production
OwnedClipboard/real ClipboardManager. Before/after-copy monotonic timestamps bound
the original deadline. At~5s, genuine ActivityScenario recreation destroyed the
original Activity and created a different instance with a second unique directory.
Same marker/text survived recreation and the actual29s observation before the
earliest30s deadline; clipboard was empty after30.5s from completed original copy.

Every observation requires real RESUMED/interactive/unlocked/focused/attached/shown
foreground and a locked Cookbook with no retained Dish/encrypted record. Natural
MainActivity callbacks own foreground/reconciliation; test never resets the timer,
forces foreground/reconcile, injects a session, bypasses authentication or uses a
virtual clock. Cleanup matches exact own synthetic marker/text and deletes only
the two captured proof directories; no prior clipboard content logged.

Accepted: **same-process locked Activity recreation preserves adapter ownership and
the original clipboard deadline on API36**. Fixture writes replace global clipboard;
that platform resource is shared, unlike isolated proof storage.

## Evidence and remaining scope

Evidence directory:`/tmp/passchef-cookbook-device-acceptance/`:
`clipboard-recreation-build.json`, `clipboard-recreation-final-build.log`,
`clipboard-recreation-install.json`, `clipboard-recreation-preflight.json`,
`clipboard-recreation-run-01.log` and `.json`. The native exit0 alone is not pass proof.
[Earlier3-test runtime](clipboard-runtime.md) retains its own artifact/scope.

Pending: authenticated Cookbook recreation/background/cold start, clipboard access
while backgrounded, process death, cross-app UID/history, API26/27 fallback and the
broader phone matrix. Google remains unconfigured with no real grant/read. Run01
historical cause remains unknown/not reproduced; no production repair or whole-mobile
Done/archive claimed.
