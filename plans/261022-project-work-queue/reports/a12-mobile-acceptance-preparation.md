# A12 — remaining mobile acceptance case cards

Date:2026-10-26, Asia/Saigon. Status:**Done2026-10-26 — case preparation**;
[independent review Passed](a12-independent-review.md).
Mobile remains Partial. Root exclusively owns ADB/Gradle/device execution.

## TODO

- [x] Reuse accepted evidence and identify remaining cases without replaying Passed.
- [x] Map exact native fixtures/methods or MissingValidExercise.
- [x] Specify freeze/readiness/owner gates, bounded termination and preservation.
- [x] Verify paths/source methods/status consistency; no phone/build actions.
- [x] Independent review Passed; registries updated; C07/C08/C09 execution remains Pending.

## Existing evidence — retain scope, do not replay

| Evidence | Accepted boundary / remaining gap |
|---|---|
| [Cancellation history](../../261012-mobile-cookbook-phase1/reports/google-cancellation-preparation.md) |Host Android system-Back Passed17.949s; prior human attempt Failed197.803s and silent grant reuse NotExercised remain. Neither hostBack nor pending-consumed proves human Cancel/resultCode. |
| [Google cache](../../261012-mobile-cookbook-phase1/reports/google-cache-ui-runtime.md) |Provider cache/cooking and authenticated same-process recreation scoped acceptance; no blanket clipboard/process/task restoration credit. |
| [Clipboard return](../../261012-mobile-cookbook-phase1/reports/clipboard-return-handoff.md), [screen-off](../../261012-mobile-cookbook-phase1/reports/owner-screen-off-clipboard-runtime.md) |Strict real Chrome replacement return and selected same-Activity owner Copy/Power/return accepted. Exact background30s erasure/history/process death remain unaccepted. |
| [Recreation](../../261012-mobile-cookbook-phase1/reports/clipboard-recreation-runtime.md), [timers](../../261012-mobile-cookbook-phase1/reports/session-timer-runtime.md) |Locked same-process clipboard deadline and original foreground5/10minute deadlines accepted; not authenticated clipboard recreation or offscreen clipboard deadline proof. |
| [Provider cold](../../261012-mobile-cookbook-phase1/reports/google-cold-runtime.md), [offline cold](../../261012-mobile-cookbook-phase1/reports/cold-process-runtime.md) |Verified controlled SIGKILL plus fresh-process locked/reopen/cache/Dish acceptance. Not natural LMK, saved-task restoration or surviving clipboard ownership. |
| [Security/UI](../../261012-mobile-cookbook-phase1/reports/security-ui-runtime.md), [VI association](../../261012-mobile-cookbook-phase1/reports/vi-accessibility-runtime.md) |Selected EN/VI capture, same-Activity Recents/biometric reopening and actual OS password-field association accepted. Spoken TalkBack/font/orientation matrix remains Pending. |

Historical failed/unknown/skipped results and missing old `/tmp` raw evidence are
retained as recorded; no reconstructed evidence or widened verdict. A11/C06 actual401
is separate; accepted provider403 is not rerun to prepare cancellation.

## Candidate, readiness and completion contract — every card

This report changes no mobile source/helper/APK. Current tracked kebab-case source
files below contain the named classes/methods; their presence is not a fresh build
or proof of exact installed candidate. Read-only current APK hashes match the
[retained provider403 record](../../261012-mobile-cookbook-phase1/reports/google-provider403-preparation.md):
debug `37579b452315adb28067a29a1c46f43731082580d2b1d760ec05147b06691a6c`,
test `a5bdc551b621df13a24b3f377962efd99a52b89a54d45ba737f1fdf5ea8c8951`,
release `92fa8896fcba7224267d63cff0388fcdcb27bb775e4abbe05463b1450961369e`.
Retained frozen01 reference:
`35c8cb94f1b6d51249bf1eb9f6a103f699403ae9e28302574bf8c584155afedc`,
external evidence root `/home/vantt/.local/share/passchef-mobile-evidence/2026-10-21-google-provider403/`.
These read-only matches are not a new freeze/archive/install/device audit.

Before a runnable card, root binds its exact class/method, source/manifest/helper
hashes and APKs to a reviewed immutable per-case freeze and persistent unique evidence
directory. Reuse unchanged reviewed bytes when available; any additive fixture needs
root compile/relevant verification/independent review/freeze before runtime. Golden/
old assertions stay immutable. No case is Ready merely because this report is Done.

Each run gets a fresh canonical UUID and only its own synthetic proof directory/
Keystore aliases. Preserve owner Cookbook digest (historically
`894f73e51e316b6a369368ad4cebbc901609ccfe7eabe855aa73c2cbf4e7a4d6`;
root must recheck actual current value), pre-existing proof-directory set, cache/
encrypted jars and source/APK/helper/history inventory. Freeze after all baseline
IO and favorite/recent updates complete. Exact preinteraction bytes and RAM baseline
must match; later legitimate recent updates use explicit semantic comparison.

Root obtains **fresh** serial/API/awake/keyguard/foreground/installed identities and
owner readiness immediately before each launch. USB or earlier ready reply is not
current readiness. Use genuine password/strong biometric and legitimate provider;
never inject token/session/result, change production clocks or force hidden overlap.
At owner markers yield immediately. Poll native result before acting on any reply;
late reply never restarts an ended run. Stop/lock invalidates pending ownership:
no old Dish/Reveal/Copy, app token/session/lifetime, selected entry, metadata/draft or
busy publication may survive; exact committed jars/cache remain. Old Dish absence
and durable recovery are separate assertions, never mere success UI.

For existing cancellation fixture, native resolution window30s and return180s are
actual source bounds; root uses its reviewed runner's bounded host watchdog (record
actual configured value) and owner/setup gates. Other cards below give proposed
budgets only:do not launch until a reviewed native fixture/host runner encodes them.
Normal completion requires selected raw JUnit start/end, terminal/anchored summary,
native exit, unique UUID/final phase, no watchdog/orchestration error and cleanup/
preservation audits. Host timeout/detach alone never proves native termination.
Missing/incomplete native evidence is Unknown; inspect/resolve active work before
another launch. Manual owner UX cards have owner observations and native Activity/
cleanup evidence, not invented JUnit Passed.

## C07 cancellation cards — all NotRun in A12

**G1 human cancellation, offline baseline.** Existing
[GoogleCancellationPhysicalTest](../../../app/src/androidTest/kotlin/dev/passchef/mobile/proof/google-cancellation-physical-test.kt)
`#realExternalGoogleReplyAfterNaturalLockPreservesOfflineCache`, with
[GoogleCancellationObservation](../../../app/src/androidTest/kotlin/dev/passchef/mobile/proof/google-cancellation-observation.kt).
Requires a genuine available external Google resolution, UUID offline cache baseline
and owner cancel opportunity; unchanged silent grant reuse must not be replayed blindly.
Source stages:real Cook→normal Connect→actual Google package/pending request/natural
stop→`owner-cancel-google-now`→owner taps an actual provider Cancel if offered, then
returns without unlocking Cookbook. Owner records control/action and observed return;
host Back cannot stand in for it. At natural stop and actual stale callback require
cleared sensitive state, consumed original pending request, secure locked foreground,
old Dish/Copy absent and exact disk/RAM/cache inventory. No resolution with clean
completed state is NotExercised; dirty/unresolved stop is Failed/Unknown. Actual
return must occur within existing180s; late owner reply does not change failure.
Cleanup:existing finally+root verify exact own UUID absent, owner/history unchanged.
**Readiness:Blocked C07 external-resolution/owner slot; not native-code absence.**

**G2 exact provider resultCode.** [AndroidSheetsAuthorization](../../../app/src/main/kotlin/dev/passchef/mobile/cookbook/AndroidSheetsAuthorization.kt)
callback consumes pending and checks ownership, but neither current observer nor
production diagnostics records `response.resultCode`. G1 can establish stale refusal,
not `RESULT_CANCELED`. **MissingValidExercise:**reviewed nonintrusive actual-result
observer tied to original request/UUID, plus legitimate external Cancel opportunity.
No injected Intent/fake result or inference from null token/consumed pending. Future
card inherits G1 natural-lock/preservation/cleanup and30s/180s bounds; capture only
numeric code/request identity, never response credentials. No helper authored here.

**G3 provider-loaded cache/stale callback and different-Menu scope.** G1's cache
is offline-pasted; it cannot certify provider-origin preservation. Reusable genuine
[GoogleColdProviderUi](../../../app/src/androidTest/kotlin/dev/passchef/mobile/proof/google-cold-provider-ui.kt)
and [GoogleColdCacheUi](../../../app/src/androidTest/kotlin/dev/passchef/mobile/proof/google-cold-cache-ui.kt)
establish provider provenance/favorite/Cook; they are not a complete cancellation
fixture. **MissingValidExercise:**reviewed combination freezes genuine provider
baseline after IO, then legitimate external resolution and original-pending stale
return with same G1 bounds/preservation. Different-Menu overlap has no established
legitimate path while busy UI is disabled; keep Blocked rather than manufacture it.
Any grant/account transition needs separate owner decision/authorization. Cleanup
only new UUID; no remote Menu/grant writes. G2 resultCode remains separate.

## C08 lifecycle/clipboard cards — all NotRun in A12

**L1 authenticated Copy→Activity recreation.** Existing
[ClipboardRecreationPhysicalTest](../../../app/src/androidTest/kotlin/dev/passchef/mobile/proof/clipboard-recreation-physical-test.kt)
`#realActivityRecreationPreservesOriginalClipboardDeadline` is a locked synthetic
clipboard adapter test; accepted authenticated recreation in Google cache helper
does not combine Copy with recreation. **MissingValidExercise:**reviewed genuine
Cook/one Copy→recreation fixture retaining same process and original clipboard
generation/deadline, distinct old/new Activity, natural lock/no old Dish and exact
jars/cache. Proposed owner setup180s, native postcopy75s and host total480s;
bind actual reviewed values before run. Compare synthetic text only in legal
foreground:untouched own clip expires by original deadline on legal reconciliation,
different app clip survives. No manual reconcile/deadline injection. Cleanup ownUUID/
owned synthetic clipboard only; do not clear another app's clip.

**L2 process death clipboard, natural LMK and saved task — distinct subcases.**
Existing [GoogleColdProcessPhysicalTest](../../../app/src/androidTest/kotlin/dev/passchef/mobile/proof/google-cold-process-physical-test.kt)
`#prepareProviderCacheForAbruptProcessDeath`/`#reopenProviderCacheInFreshProcess`
and [ColdStartCachePhysicalTest](../../../app/src/androidTest/kotlin/dev/passchef/mobile/proof/cold-start-cache-physical-test.kt)
`#prepareOfflineCacheForAbruptProcessDeath`/`#reopenOfflineCacheInFreshProcess`
prove prior controlled SIGKILL/cold cache scopes; don't replay them as new coverage.
**MissingValidExercise:**Copy-before-kill/actual clipboard observation and reviewed
saved-task locator/entry checks; natural LMK additionally needs an observed genuine
OS kill with identity/reason provenance. A SIGKILL, force-stop, Activity recreate or
`CLEAR_TASK` fresh launch cannot be relabelled LMK/task restore.

Future controlled-kill card needs root same-shell PID/cmdline/UID/startticks/hash
identity, old process absence and distinct newPID; warm verdict is deliberate
InterruptedByVerifiedSIGKILL, not Passed. Natural LMK waiting is proposed15minutes
maximum, then NotExercised if no qualifying event; no artificial memory pressure
or owner-device stress authorized. New cold/restore stage proposed180s including
owner reopen, host separately bounded; interruption needs native-process evidence.
Before unlock:locked/no session/token/Dish, exact disk; after genuine unlock correct
cache/jars and fresh exact Dish. [OwnedClipboard](../../../app/src/main/kotlin/dev/passchef/mobile/cookbook/OwnedClipboard.kt)
ownership is process-memory only:do not promise cleanup survives process death.
Record surviving OS clip/history as limitation, never assume erasure. Each subcase
has own UUID/freeze/result/cleanup and remains independently Blocked.

**L3 exact background30s clipboard deadline.** Existing
[OwnerScreenOffClipboardPhysicalTest](../../../app/src/androidTest/kotlin/dev/passchef/mobile/proof/owner-screen-off-clipboard-physical-test.kt)
`#ownerCopyThenPowerUsesOriginalClipboardDeadline` accepted stop before+25s,
sampled offscreen≥35s and legal-return absence, not a legal background read at30s.
**MissingValidExercise:**on the actual OS, establish a legitimate observation path
that does not focus the app/alter lifecycle or bypass clipboard restrictions. Without
one, exact-background erasure stays unobservable/Blocked; legal-return behavior
retains its accepted scope. No old fixture rerun just to restate that limit.
Any future reviewed card binds original monotonic deadline/generation, same PID/
Activity, actual stop/lock and return timing; proposed35s offscreen sample plus
180s owner-return budget. Preserve replacement clip, original jars/cache and absence
of old Dish; cleanup ownUUID. No promise OS/keyboard history or external copies erase.

## C09 accessibility/layout cards — all NotRun in A12

**X1 actual spoken TalkBack.** Existing
[VietnamesePasswordAccessibilityPhysicalTest](../../../app/src/androidTest/kotlin/dev/passchef/mobile/proof/vietnamese-password-accessibility-physical-test.kt)
`#realLockedVietnamesePasswordFieldHasOwnedOsLabelAssociation` proves selected OS
node association only; [AuthenticatedSecurityUiPhysicalTest](../../../app/src/androidTest/kotlin/dev/passchef/mobile/proof/authenticated-security-ui-physical-test.kt)
`#realAuthenticatedEnglishVietnameseCookingAndSecureDisplayCapture` proves selected
secure UI/capture, not speech/traversal. **MissingValidExercise:**reviewed sustained
UUID-owned manual fixture/hold checkpoint for owner listening (short finished
instrumentation is insufficient), owner-approved TalkBack setting and phone slot.
No concurrent UiAutomation service that disrupts the actual owner TalkBack session.

Proposed separate locked then genuine synthetic Cook stages, each owner window180s
and finite5minute observation session. Owner records actual spoken EN/VI field
labels/password role, focus order, error guidance and reachable Cook/Reveal/Copy/Lock;
masked Dish must not leak through an unintended accessible node. Record actual
speech/actions, not node-based inference. Natural background/recreation still locks
and removes old Dish; reauthenticate only at explicit checkpoint. Cleanup only
UUID and restore exact prior accessibility/language settings after owner agrees.

**X2 font/orientation layout.** **MissingValidExercise:**same sustained synthetic
manual fixture plus approved setting/rotation path; no existing reviewed full-layout
runner. Default and one owner-selected supported larger font, portrait/landscape,
EN/VI are separate cells, not an all-device claim. Record actual font/display scales,
orientation/window bounds/IME and each cell's screen/state; Main manifest declares
no fixed orientation, which is not layout acceptance. Proposed90s per locked cell,
180s per authenticated cell, bounded10minute total; stop on actual lock/recreation,
then verify no old Dish and genuine reopen before next authenticated cell.
Owner checks clipping/scroll reach, labels/errors and all action targets at locked,
Sources/Menu, manual Cook and masked/revealed synthetic Dish screens. Existing
language touch geometry is not full UX evidence. No private screenshots; secure
capture restrictions remain. Restore prior font/display/orientation/language and
TalkBack states; verify owner data and ownUUID cleanup. Missing cells remain Pending.

## Local checks and delivery

Only this report edited. Read source class/method content (kebab-case filenames),
existing scoped reports, MainActivity natural stop/focus and authorization ownership/
clipboard paths. `sha256sum` of three existing APK files matches retained hashes
above; no build/install/current-device audit. `git diff --exit-code -- app mobile`
Passed:mobile source/tests unchanged. Local checks Passed23Markdown targets,
9actual method declarations,8case cards and three explicit all-NotRun groups;
`git diff --check` exit0. No ADB/SDK/Gradle/phone/account/
provider action, no native/JVM test execution or fresh Android compile claimed.

## Unresolved

Legitimate external cancellation/provider-loaded callback exercise; resultCode
observer; per-case lifecycle/process/task fixtures and owner slot; spoken TalkBack/
font/layout assessment. A11/C06 actual401 is separate.
