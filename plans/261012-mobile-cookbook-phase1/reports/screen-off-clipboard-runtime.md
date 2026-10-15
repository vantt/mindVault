# Authenticated Copy / screen-off checkpoint —15October2026

Status: **Run01/02 Failed; screen-off/clipboard acceptance Pending; Mobile Partial**.
New132-line selected test uses the existing UUID-owned `ColdStartProofActivity` and
genuine owner biometric setup. Production, previous assertions and deadlines unchanged.

## Prepared boundary

`ScreenOffClipboardPhysicalTest#authenticatedCopyScreenOffAndLegalForegroundCleanupPreserveCache`
sets up synthetic jars, confirms offline cache/favorite/recent and Cooks through UI.
Actual UI Copy must confirm COPIED, exact synthetic Dish and unique sensitive marker
while own foreground is focused/awake/unlocked. No prior clipboard content is read.

Owner Power-off must produce a real stop and noninteractive state before copy+25s,
conservatively preceding the original30s cleanup eligibility. Otherwise clipboard may
already clear in foreground and cannot establish background acceptance. Production
deadline is unchanged;25s is a proof precondition, not a shortened cleanup timer.

After observed stop, sample noninteractive/locked/cleared state and exact cache/jars
for>=35s, then recheck the final offscreen state before announcing wake. Read no
clipboard while offscreen. After owner wake/unlock, require same undestroyed Activity/
PID, own focused foreground, cleared session/Dish and preserved cache/jars. Observe
clipboard absence after normal lifecycle/focus reconciliation without invoking it
manually. This remains unaccepted until those stages actually execute.

No exact background30s/history-wide deletion, long-background UX, LMK/task restoration,
authenticated recreation/process death or cross-app replacement claim. Failure cleanup
only reads/clears the exact owned marker/text if focused foreground is legal; if still
offscreen, clipboard cleanup cannot be promised. UUID key/file cleanup stays separate.

## Review, build and artifacts

Initial build01 Passed12s,10executed/84UP-TO-DATE. Review added final>=35s offscreen
guard to close the last sampling boundary; initial source snapshot retained.
Final build02 Passed9s,9executed/85UP-TO-DATE; lint0errors/11warnings/1hint.
No fresh JVM/browser execution and no production correction.

Frozen01/02 both bind exactly72 app source/manifests and the same reviewed runner.
All71 previous app files and28 previous host helpers unchanged; protected208/unrelated23
match. No build/install between attempts. Debug remains
`1f42a2c4b369492b0ed804fab3ec282882e831336cf4ebfec48dbdba05bfd772`;
new test `181368becd049fc0bfa1f70ae3c3b0cee4aecc00a9ba265171b9f53f1155b412`;
release remains `efd3b38644db2404e42faca714c963ef99467c644524833455dff32c11f6b653`.
Install-r updated only test APK; installed identities equal both frozen candidates.

## Actual outcomes, Xiaomi/API36

Both attempts freshly verify serialPN7PW8SGXKLVPRIV/transport30,awake/keyguard absent,
installed identities and owner digest before selected instrumentation. Own secure
foreground guards actual UI. Native0 alone is never interpreted as JUnit Passed.

| Run | Actual result | Reached boundary |
|---|---|---|
| 01 | Failed1/1,37.492s;[1,-2]/[-1]/FAILURES | Genuine setup/cache/Cook/Copy; at25031ms still awake/resumed/focused; required stop/noninteractive gate unmet |
| 02 | Failed1/1,54.316s;[1,-2]/[-1]/FAILURES | Same source/APKs; genuine setup/cache/Cook/Copy; at25025ms still awake/resumed/focused; same gate unmet |

Neither reached `screen-off-observed-hold` or `owner-wake-unlock-return-now`.
The diagnostic `ElapsedOff`0/1ms comes from assigning a candidate observation start
before the failed precondition. It is **not** a measured screen-off interval.
Both have native0,no watchdog/orchestration error and terminal Failed evidence.

After Run01 terminal, owner replied exactly “Chưa bấm; có thể thử lại ngay”; root
retried the unchanged candidate with fresh prerequisites. In Run02 the async Power
question was posted late; owner replied “Đã tắt màn hình” after the native terminal.
The reply arrival does not establish the physical key timestamp. Do not infer owner
inaction from missing samples or reinterpret this late answer as accepted screen-off.
Root told owner the run had ended and the phone could be woken/unlocked.

Both UUID directories are absent after terminal:
01 `c0cf0725-bbd8-44f4-826a-ef1192410b3b`;
02 `8b7416f6-391b-449c-b813-f4ff3f6621a2`.
Source/APK/helper/protected/unrelated audits match; original Cookbook digest stays
`894f73e51e316b6a369368ad4cebbc901609ccfe7eabe855aa73c2cbf4e7a4d6`
before/after each installation/runtime. Directory cleanup is not clipboard-history proof.
Neither failure establishes a production bug or justifies changing deadlines/assertions.

## Evidence and continuation

`/tmp/passchef-cookbook-device-acceptance/screen-off-clipboard-` prefix retains
baseline01,build01/02 logs,build01-source snapshot,runner,frozen01/02,candidate01/02,
install01,run01/02 raw logs/JSON/preflights/immediate-prelaunch and exact owner
observations01/02. Frozen HEAD stays historical after a later commit.

Before another attempt, arrange the owner action checkpoint so the Power instruction
is visible promptly, then yield in time; no blind retries or rebuilt production.
Retain Failed01/02 and the predeadline proof gate. The selected mapping/tag Run01
is separately Passed; Google cancellation Run01 is separately NotExercised.

Unresolved: actual screen-off/clipboard return; long background/LMK/task restoration;
cross-app/authenticated recreation/process-death clipboard; actual Google cancellation/
stale/401/403; broader accessibility and historical UI/setup/wrap/cold/Run06 causes.
