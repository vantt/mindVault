# Authenticated clipboard recreation —4November2026

Status: **Selected emulator Run01 and physical Run04 Passed; broader Mobile Partial**.
Continue the existing Phase1 goal; physical Google/accessibility/lifecycle gates
remain required. Owner says they will connect a phone; ADB currently sees only the
separate task-owned `PassChefAuthenticated36` emulator on port5580.

## Actual gap and selected invariant

Existing clipboard recreation proof uses a locked Cookbook with no encrypted
jars. Existing authenticated cache recreation never copies a Dish. This separate
test combines genuine password setup, confirmed offline Menu/favorite/recent,
cached Cook and actual UI Copy, then actual same-process Activity recreation.
It does not replace natural LMK/saved-task restoration or process-death clipboard
guarantees; the latter remain explicitly outside the Cookbook contract.

`CookbookProofActivity.retainProofStorage=true` preserves only its canonical UUID.
The test requires a new Activity identity, destroyed old Activity, same PID and
same owned storage; both controllers must retire session/Dish/grant/selection.
Every encrypted jar/cache file remains byte-exact before reopening. The original
sensitive clipboard marker, actual deadline and generation must survive recreation,
then clear after that original30-second deadline without a timer reset.
Genuine password reopening and a new cached Cook must preserve the authenticated
encrypted Spice payload and cache identity. Legitimate biometric envelope rotation
is not misclassified as a payload change. A fresh Cook must never copy implicitly.

## Implementation and independent source review

Two additive instrumentation files only:

- `authenticated-cookbook-fixture.kt`:128lines; real Compose actions and private
  cache/encrypted-payload checks. One-row Menu columns are explicitly confirmed.
- `authenticated-clipboard-recreation-test.kt`:119lines; original adapter ownership
  observations, real recreation/monotonic wait, genuine reopen and exact cleanup.

No production source, existing test/assertion or accepted fixture changed.
[Source review](authenticated-recreation-review-20261104.md) initially identified
cleanup failure masking and incomplete original-controller retirement proof.
Both corrected before runtime; reviewer recheck Passed. Cleanup independently
attempts exact clipboard/Activity/record/key/UUID work, suppressing cleanup errors
onto an original failure rather than replacing it or silently passing.

## Build and frozen candidate

Root JDK17/SDK36 commands:

```sh
./gradlew --no-daemon :app:assembleDebugAndroidTest :app:lintDebug
```

Build03 Passed1m42s; corrected Build04 Passed2m33s; each94tasks,10executed and
84up-to-date. Retained lint XML0errors/7warnings/1hint; the report task was cached.
No fresh shared/bridge/app/standalone JVM rerun is claimed for these test additions.

Independent tester verified original170 source paths,54 protected baseline files
and all173 current frozen paths unchanged; the additional paths are these two
tests and the already-reviewed instrumentation build configuration. Both new
classes/selected method exist in the frozen APK DEX. Original Golden bytes intact.

| Frozen artifact | SHA256 |
|---|---|
| Debug, unchanged | `9fd023f4e98455a326faed00261bb503a280986c0307ecef9fff1e70b98631b8` |
| Test candidate04 | `210761dd0be3f313f0b552d3a20f4a893d11e90057df807e09ad2289d5f766c1` |
| Unsigned release, unchanged | `039e242045131067c9a401dc4342b69d48bafa0f309c00d2d6075a712f4c641d` |

Evidence root:
`/home/vantt/.local/share/passchef-mobile-evidence/2026-11-04-authenticated-recreation/`.
`source-freeze04.json`, `candidate04/`, build logs and
`tester-authenticated-candidate-audit04.json` bind exact source/artifacts.
Selected host runner differs only in evidence root; classifier unchanged and ten
new host-only adversarial probes Passed. Those probes provide no Android acceptance.

## Runtime

Fresh task-owned Windows AVD uses separate userdata/cache and no owner account.
Boot completed1,API36,qemu1,Awake/keyguard showing=false verified; no PassChef
package before exact debug/test install. Select only
`AuthenticatedClipboardRecreationTest#authenticatedCopyRetainsOriginalDeadlineAcrossRecreation`.
Run01 terminal selected1/1,125.698s native/137.635s host, exact JUnit`[1,0]`,
terminal`[-1]`,anchored `OK (1 test)`, no failure/skip/timeout. One UUID
`fcfee749-e2e8-421d-b00b-31b850b99232`,samePID5719; original clipboard deadline
495028/generation1 retained through actual replacement Activity. Both controllers
cleared, exact disk preserved; original cleanup then genuine password reopening
and new cached Cook assertions passed. Biometric-wrap-present remained false:
this is the real emulator password fallback, not genuine biometric acceptance.

Native finally removed the exact synthetic directory; post-runtime audit confirms
its absence, empty own noBackup inventory and all173 frozen source paths unchanged.
Native raw/result and `post-runtime-audit01.json` remain retained. No genuine
biometric/provider/phone or saved-task/natural-LMK pass inferred.

## Actual Xiaomi/API36 Run04 — Passed113.868s

Owner connected the phone, enabled/authorized USB and genuinely unlocked Android.
Preparations01–03 remained locked/Dozing before their bounded gates; **no APK
install or native test** launched in those preparations. Existing private files
were only digested and installed public APKs backed up; no owner Cookbook copied.

Actual Run04 checked Awake/keyguard=false before each exact candidate04 install
and native start. Installed debug9fd023…31b8/test210761…66c1 hashes matched the
frozen173-source archive. The saved-task debug observer changes are a separate
later candidate and were not installed for this selected phone run.

Physical native selected1/1,113.868s; host116.477s; exact JUnit`[1,0]`,terminal`[-1]`,
anchored `OK (1 test)`, no failures/skips/timeouts. UUID
`2f671f07-e7ea-4fb0-9222-780c181858a2`,PID15251. Genuine setup/reopen record wrapping
was present after authentication; same original clipboard deadline1461316489 and
generation1 survived actual replacement Activity. Both retired/new controllers
were locked, session/grant/Dish cleared, all owned jars/cache byte-exact before
recovery. Legal foreground cleanup observed at1461317046 (+557ms), followed by
genuine password recovery and fresh literal cached Cook. All original assertions.

Phone reports its wired charging as AC. Preparation temporarily kept the display
awake across installation, preserving original global plugged-in policy0. That
policy was restored to0 at proof-Activity creation **before the Copy checkpoint**;
the ordinary proof KEEP_SCREEN_ON flag then applied. No authentication, application
session/clipboard deadline or production window policy was bypassed. Failed
preparations and each restoration record remain separate.

Post-runtime audit: owner Cookbook SHA256 remains
`894f73e51e316b6a369368ad4cebbc901609ccfe7eabe855aa73c2cbf4e7a4d6`;
every preexisting regular-file digest and all six prior proof directories remain
exact/preserved. Only this UUID removed. Native terminal then normal proof close;
target PID absent, phone returned to Launcher, no live native wait. Private owner
Recipe/Spice contents were not read or exported. Only installed public APKs backed up.

Physical evidence:
`/home/vantt/.local/share/passchef-mobile-evidence/2026-11-04-authenticated-recreation-physical/run04/`.
Retain raw/result, per-install hashes, before/after manifests and temporary screen
policy restoration. Independent physical audit is being recorded in the
[review](authenticated-recreation-review-20261104.md).

Accepted only selected authenticated same-process recreation/original-deadline
clipboard cleanup and genuine reopening/cache Cook on Xiaomi/API36. No process-
death/history-wide/exact-background30s, TalkBack/full-layout, Google or natural LMK
claim. Earlier unknown/failure causes remain separate.

Unresolved: final saved-task runtime/observer delivery; broader physical matrix
and remaining Google401/human-cancellation/different-Menu conditions.
