# Phase03 — Compose Cookbook, reveal and owned clipboard

Status: **Partial — code complete; physical acceptance Pending**. Priority:3.
Latest selected [Google cache UI](reports/google-cache-ui-runtime.md) Run02
Passed1/1,42.393s: exact cached Cook/favorite/recent/search and authenticated
same-process recreation with real biometric; broader physical matrix Pending.
Later [network-off Run04](reports/network-off-runtime.md) Passed1/1,31.477s:
same cached UI assertions under actual network absence plus real network-failure
cache preservation. Original UI Run01 and network Run03 wrap causes unresolved.
Selected [Security/UI](reports/security-ui-runtime.md) locked pixels01 Passed1/1,2.321s
and authenticated02 Passed1/1,25.890s: EN/VI setup/manual Cook and real secure shell
display exclusion. Earlier three security failures retained; Recents01/02 subsequently
Failed206.052s/182.457s. Later Recents03 Passed137.789s with genuine reopen and owner-observed hidden Card; broader accessibility Pending.

## Context and key insight

[Q6](../../docs/mobile-app-brainstorm-qa.md), [Keystore](../../docs/specs/mobile-keystore.md).
The existing Activity already uses FLAG_SECURE and cold/background locking.
Keep that boundary while replacing the setup/unlock-only screen with real flows.
No emulator has been established; test APK compilation is not Compose runtime proof.

## Requirements and architecture

Welcome → set up/recover local jars → unlocked Cookbook. Offer manual Cook without
Google; Connect/source/tab/column confirmation links to phase04 when available.
Keep existing1–5-jar password/biometric behavior, including password-only fallback.
Cookbook list searches label/username, shows source offline/sync/error state and
favorites/recent; tapping an entry opens a masked Dish detail with Menu context.
Reveal hides again after10 seconds. Dish Copy schedules compare-before-clear at30s;
username Copy is separate and must not be erased by an old Dish cleanup.

## Related files

- Modify `MainActivity`, `VaultScreen`, app resources and bounded screen/controller helpers.
- Add app `cookbook/` list/source/mapping/detail/paste/settings presentation files.
- Add app clipboard adapter and pure owned-reveal/clipboard policy in shared + JVM tests.
- Add Android instrumentation tests for real Compose wiring/clipboard/lifecycle;
  add required test dependencies only after compatibility is checked, no guessed versions.
- Keep release/debug isolation, backup exclusions and existing physical regressions.

## Implementation steps / TODO

- [x] Split navigation/setup/unlock/list/Cook/mapping/detail into focused components<200lines;
  provide EN/VI strings, accessibility labels and loading/empty/no-results states.
- [x] Use manual Cookbook source ID/URL + tab selection rather than add Drive picker scope;
  uncertain column roles show the confirmation preview from phase02.
- [x] Display verified-tag badge only as Recipe metadata, not prevalidated cooking proof.
  Show friendly mismatch/missing-context errors with no stale Dish or usable Copy.
- [x] Mask by default; explicit reveal/copy checks session ownership; timeout, navigation,
  edit, lock, background or new entry clears/hides the previous result.
- [x] Implement real Android ClipboardManager adapter with sensitive-content marker
  where available. Confirm successful write before toast; only clear matching owned
  text/generation, and never write after inaccessible read or replacement text.
- [x] Keep cleanup ownership outside the closing detail screen, schedule bounded30s
  work and reconcile when foreground access is legal. Android background clipboard
  restrictions mean no promise of exact background/history-wide removal.
- [x] Maintain5-minute idle/10-minute total monotonic clock; no navigation/reveal/copy
  grants a new absolute window. OnStop hides Dish and locks jars immediately.

## Success criteria and positive proof

- [x] JVM deferred-result/reveal/clipboard tests cover10s/30s edges, new copy, replacement
  text, denied read, screen close, lock/reopen and unchanged absolute session deadline.
- [x] Debug/release/test APKs compile and lint passes; no unmasked saved state, sensitive
  logging, remote favicon request or release proof harness is introduced.
- [x] Compiled UI tests assert actual setup→paste→Cook, mapping/list/search, exact Dish,
  mismatch refusal, reveal timeout, username/Dish copy and lock/background cleanup.
  Record them as **not run** until authorized device/runtime executes them.
- [ ] Physical phone observes layout/accessibility, biometric/password fallback,
  real clipboard/reveal, background/cold start,5/10-minute timers and Recents security.

## Risks and next steps

Immutable Compose strings cannot guarantee erasure. Physical clipboard/OS overlays
need honest platform limits ([Android clipboard](https://developer.android.com/develop/ui/views/touch-and-input/copy-paste)).
Recent/favorites contain entry IDs only; remote icons/swipe conveniences/settings
extras are later UI refinement, not a claim that every Q6 sketch shipped.
Continue to real connection code; offline screens remain usable while disconnected.

## Security/UI continuation — bounded physical acceptance

- [x] Add/review isolated locked UUID-storage instrumentation for actual EN/VI
  setup labels/buttons, password accessibility semantics and visible48dp control bounds.
  No setup/authentication, existing assertion change or production policy change.
- [x] Root-only minimal compile, artifact/protected hashes, owner Cookbook digest
  and actual Awake/keyguard=false/own foreground preflight; select only the new method.
- [x] Execute real display capture: nonnull valid bitmap, actual own-content region
  fully black, secure foreground and locked/no-record before/after; emit only bounded
  geometry/count/boolean metadata. Configuration flags alone do not accept capture.
  Locked pixels01 and authenticated02 Passed on API36 using opt-in shell capture;
  default UiAutomation-null failure remains Failed. [Exact evidence](reports/security-ui-runtime.md).
- [x] Actual same-Activity Recents stop/lock/genuine biometric reopen with hidden own
  Card observed by owner: Run03 Passed137.789s, pause/stop1, jars/digest preserved.
  Same frozen04/APKs, no rebuild/install; API36 proof KEEP_SCREEN_ON scope.
  Recents01 Failed206.052s: post-checkpoint pause/stop remained0; reopen not reached.
  Recents02 Failed182.457s before setup readiness/checkpoint; navigation events empty,
  no ADBKEY187 sent. Same frozen04/APKs, no rebuild/install between attempts; causes
  unconfirmed, cleanup lock is not acceptance. Owner reported "can't take screenshot";
  exact shortcut/reveal timing unconfirmed; own Card later observed in Run03.
- [x] Separate owner07 confirms actual Dish text then blocked Power+VolumeDown;
  new synchronized checkpoint Passed116.736s. No exact key timestamp/pixel claim.
- [x] Record selected EN/VI setup/manual Cook semantics and capture scope; raw-layout
  Locked01 and same-node-accessibility Capture01 remain Failed with original assertions.
  Later diagnostics distinguish40dp raw layout from48dp expanded touch bounds;
  later edge01/02/dwell04 executions Failed; separate explicit-finger01 samples Passed,
  whole-target acceptance remains unproved, no production cause inferred.
- [ ] Broader physical layout and Vietnamese OS accessibility; owner TalkBack traversal,
  larger-font/orientation and broader accessibility remain Pending without observation.

No provider/cache/timer replay belongs to this continuation unless related source changes.
Preserve historical failures and Mobile Partial; security test compilation is not runtime proof.
Selected [offline cold-process proof](reports/cold-process-runtime.md) Passed:
warm02 verified SIGKILL, cold02 Passed1/1,5.993s with genuine reopening/cache/Cook;
provider-loaded cold02 later Passed8.886s; wider physical accessibility/layout Pending;
[VI OS association01](reports/vi-accessibility-runtime.md) Passed1.5s with one password
node/descendant label; direct same-node checker stays Failed/TalkBack Pending.
[Native language touch](reports/language-touch-runtime.md) center02 Passed1.852s;
edge01/02 Failed; after USB reconnect15October, matched-dwell04 Failed1.709s and
same-candidate center04 Passed1.802s. No original assertion
or production change; later paired explicit-finger01 Passed2.464s at the sampled
midpoints. Effective whole-target/layout/font/rotation acceptance remains open.
no blind retry or historical-cause inference.

### Bounded geometry diagnostic plan —15October

- [x] Planner reviewed a new locked UUID geometry-only fixture; retain all60 prior
  sources/manifest/assertions and archived physical input candidates unchanged.
- [x] Compile/review `language-edge-geometry-physical-test.kt` and its isolated runner.
- [x] Observe the exact existing edge midpoint with the same ten point guards;
  bound layout ancestry/merged-unmerged ScrollBy observations to32 each, emit only
  booleans/dimensions/counts, and verify unchanged headline/foreground/record absence.
- [x] Freeze exact new source/APK/helper identities; fresh device/digest/install-r
  gates precede selected runtime. Geometry Passed means observations completed only.
  Scroll overlap/disjoint lineage cannot prove event interception or a production cause.
  Actual geometry01 Passed1.505s: target layout excludes sample, two enclosing layouts
  contain it; observed scroll coverage0. Source61/helper9/testc0fd/owner digest preserved.

### Paired pointer-kind plan —15October

- [x] Planner reviewed separate native UNKNOWN/FINGER comparisons; preserve old61/
  nine helpers. New `language-pointer-probe.kt` and `language-finger-physical-test.kt`
  reuse the exact sampled geometry, secure locked guards and configured60ms dwell.
- [x] Observe actual legacy constructor tool kind; reconstruct both variants with
  readback equality for public non-tool fields/axes. Fresh equal geometry is required
  before each variant; UNKNOWN retains headline/Master/toggle, FINGER switches all
  three. Repeat both languages, separate two-pair counters for each kind.
- [x] Review/compile/lint, freeze new exact source/APK/runner archive, then fresh
  device/digest/install-r and selected runtime. No retrospective old-event readback,
  production repair, whole touch rectangle or unrelated historical-cause inference.
  Actual finger01 Passed2.464s/source63/helper10/test9a64: UNKNOWN retained UI,
  FINGER switched both languages, four pairs/all field/point guards; owner preserved.

### Owner hardware shortcut plan —15October

- [x] Owner ready for genuine biometric and Power+VolumeDown; planner reviewed new
  `manual-shortcut-physical-test.kt`, separate from accepted Recents/shell captures.
- [x] Review/compile/freeze isolated checkpoint; retain old63 sources/ten helpers.
- [x] Genuine UUID setup/Cook, secure masked-ready checkpoint with Reveal visible:
  reached in Run04. Passive200ms observation preserves normal10s remask and jars.
- [ ] Confirm owner Reveal immediately before shortcut and actual OS result;
  no reveal/stop sampled or owner answer received within Run04's180s window.
- [ ] Root writes exact-UUID completion signal only after actual owner answer;
  JUnit certifies checkpoint/cleanup, owner reports OS message/method/Dish visibility
  separately. Power-stop must clear session/Dish; uncertain timing stays Pending.
  [Actual evidence](reports/manual-shortcut-runtime.md): Run03 Failed182.485s setup/
  current wrap15; Run04 Failed236.235s completion wait. No fake signal or screenshot
  acceptance. Owner/source64/helper12/exact UUID cleanup preserved; no production fix.
  Late04 missing-button reply retained; clearer Run05 question, genuine ready then
  Failed188.013s completion wait/no sampled reveal/stop. Current screen/labels requested
  before another timed run; no active runtime. Same exact source/APK, prior failures kept.

### Manual visual diagnostic variant — selected Run07 Passed

- [x] Add separate `ManualVisualPhysicalTest` and bounded helper; old64 sources and
  original assertions/deadlines/lifecycle stay unchanged. Normal instrumentation-thread
  Compose idle precedes each controller/actual tagged-text/layout snapshot; no clock manipulation.
- [x] Root review/compile/freeze and selective owner-ready runtime after the isolated
  publication repair. Run06 Failed193.721s remains; isolated hardlink permission failure is
  orchestration evidence, not a production repair or screenshot acceptance.
- [x] Compare actual masked/synthetic Text booleans and complete own-content/window
  containment with same-observation model/foreground bits. No text/pixel export;
  semantic/layout agreement alone never accepts owner-visible pixels.
- [x] Preserve actual owner07 report: Dish text appeared and Power+VolumeDown blocked.
  Actual07 Passed1/1,116.736s; native publication succeeded after actual reply.
  Owner/exact UUID cleanup/source66/old64/helper12/protected208/23 preserved.
  No exact key timestamp or retrospective06 cause/single22s reveal-deadline claim.

Implementation exists in the production app/shared/client paths above. Final local
results and unchanged Golden evidence are recorded in [verification](reports/verification.md);
Run01 executed with genuine owner biometric and failed after the reveal delay;
see [device acceptance](reports/device-acceptance.md). Run02/03 Failed; selected
Run04/05 flows Passed. Selected GoogleRun03/cache UI Run02 subsequently Passed;
broader phone/provider acceptance remains Pending.

## Continuing Run01 investigation

- [x] Trace production hide, Dish readiness, session guard and lifecycle paths;
  source alone does not establish the physical cause.
- [x] Execute independent shared regressions: exact10s/10.5s expiry retains ready/copy;
  repeated reveal respects idle5minute/total10minute session limits.
- [x] Compile/review sanitized diagnostic read-success, session and presentation bits.
- [x] Selectively execute diagnostic locked-start/stop/resume smoke in unique proof
  storage, with no setup/authentication; this is not authenticated UI acceptance.
- [x] Run02 with ready owner and real biometric; exact assertion retained. Failed after
  owner-confirmed foreground interruption; [subsequent runtime](reports/ui-runtime-continuation.md).
- [x] Uninterrupted foreground retry after fresh Awake/keyguard readiness check:
  Run04 Passed1/1 with exact masked-after10s assertion under proof KEEP_SCREEN_ON.
- [x] Compile/review optional proof-only production-screen-policy comparison and
  sanitized pause/stop interactive/keyguard/window bits; preserve all assertions.
- [x] Controlled owner-ready Run05 with normal screen policy Passed1/1,53.663s;
  actual KEEP_SCREEN_ON=false and10.617s remask. Run01's historical cause remains
  unknown/not reproduced; owner reports no navigation in Run01.
- [ ] Resolve historical Run01 cause before closing that investigation; if reproduced,
  fix the evidenced cause and recheck exact assertions. Current Run04/05 flow acceptance
  does not imply historical cause/fix proof.

## Unattended clipboard adapter acceptance

Owner going to sleep: no biometric/Google request. Existing8 platform tests stay
retained; do not rerun them. New isolated locked-Activity component tests may run
only while actual screen is awake/unlocked, with no setup or session injection.

- [x] Prepare/review actual ClipboardManager + production OwnedClipboard tests:
  untouched30s scheduled clear, replacement survival, newer-copy deadline ownership.
- [x] Assert real foreground/power/keyguard before observing empty clipboard;
  no denied-read false pass, virtual clock, forced reconcile or clipboard permission override.
- [x] Root-only compile/lint Passed; [preparation evidence](reports/unattended-clipboard-preparation.md).
- [x] Selective runtime Oct13 Passed3/3,99.926s after reconnect/manual OS unlock;
  [actual adapter evidence](reports/clipboard-runtime.md), no PassChef authentication.
- [x] Preserve owner Cookbook and earlier unrelated/Golden files; cleanup only own
  synthetic clipboard identity and captured unique proof storage; separate adapter
  evidence from Cook/authenticated UI and full-mobile acceptance.

## Clipboard Activity recreation continuation

- [x] Add one isolated API28+ adapter test: real Activity recreation at~5s;
  retain original clipboard identity at29s and clear after original30.5s deadline.
- [x] Capture both unique proof directories; assert locked/no-record and actual
  unlocked/focused foreground; clean only exact owned synthetic clipboard/storage.
- [x] Root compile/lint, independent review and selective phone runtime: Passed1/1,
  31.721s on API36; [actual recreation evidence](reports/clipboard-recreation-runtime.md).
  Earlier3 tests retained; authenticated recreation/process death/Google Pending.

## Authenticated provider-cache recreation continuation

- [x] Opt-in same proof storage preserves only owned UUID; default two-directory
  clipboard recreation regression Passed1/1,31.769s after harness change.
- [x] Actual cached Google row Cook, origin/Recipe/username00123, favorites/recent/
  search, explicit lock/token invalidation and genuine biometric reopen accepted.
- [x] Old authenticated Activity destroyed/new controller locked without Dish/session/
  token; normal biometric reloads exact durable cache and repeats Cook. Cache UI
  Run02 Passed1/1,42.393s after Failed Run01,294.651s and helper-only Compose sync.

Scope app-disconnected cache and same-process authenticated Activity recreation;
no actual network-off, process death/cold start, timer/capture/accessibility or
historical CookbookUIRun01 production-fix claim. [Evidence](reports/google-cache-ui-runtime.md).

## Real foreground session timers continuation

- [x] Add/review isolated genuine setup/biometric/Cook timer fixtures; original
  assertions/Golden and production sources unchanged, each new file<200lines.
- [x] Observe real5-minute idle from verified Activity ACTION_DOWN then no input;
  before original deadline ready/masked/Copy allowed, after expiry+1.5s locked/cleared.
- [x] Observe real10-minute total from original opened timestamp despite actual
  window pointer every60s and Cook/reveal/navigation; opened/generation never renewed.
- [x] Read-only clock/session/lifecycle/environment diagnostics and progress each
  minute; fail early lock/pause/stop/recreation/timestamp drift, no virtual clock.
- [x] Root-only build/hash/install/digest and separate owner-ready selective runs;
  proof-only KEEP_SCREEN_ON foreground scope, no full suite/clipboard writes.

Idle Run05 Passed1/1,341.722s;14October Total04 Passed1/1,609.969s, runner[1,0]/OK1.
Original opened/generation unchanged through minute1–9 real touches/recooking;
595106ms ready/masked/Copy,601725ms locked/session-null/lifetime/Dish/Copy cleared,
jars and owner digest unchanged. Same frozen03/APKs, no source/build/install changes.
Selected5/10-minute API36 foreground proof KEEP_SCREEN_ON accepted; four idle/three
total failures retained, original causes unknown. Broader timer UX remains Pending.
Original UI Run01/network Run03 wrap
causes and cancellation/401/403/cold-start/process-death/capture/ENVI/accessibility
remain open; whole mobile stays Partial.

## Selected wrong-Menu Recipe and mapping UI continuation

Google cancellation run01 could not exercise external resolution because the SDK
returned a usable grant without a prompt. That scope stays Pending. This independent
current-MVP UI boundary Passed selected Run01,36.892s on Xiaomi/API36.

- [x] Reuse UUID-owned proof storage and genuine biometric setup; seed a last-good
  synthetic Menu through production UI, freeze encrypted jars and exact cache/RAM.
- [x] Through actual mapping controls, submit duplicate role-column assignments
  and a blank required Recipe column. Require INVALID_MAPPING, retained draft and
  exact last-good cache/jars. Explicit valid remapping must recover without bypass.
- [x] Confirm a bounded synthetic table with ragged/empty/invalid-Recipe rows and
  literal Unicode/00123 values; assert exact projected entries, original row numbers
  and missing optional-cell behavior. Selected duplicate/blank header preview and
  explicit roles Passed; ambiguous automatic Recipe-column proposals remain Pending.
- [x] Save a second distinct Menu context via UI. Cook a synthetic tagged Recipe
  successfully under its matching Menu, then select the other Menu without editing
  the Recipe. Require RECIPE_MISMATCH with no previous Dish/Copy; preserve jars/cache.
- [x] Independent review then root-only compile/lint/freeze/install-r/preflight and
  owner-ready selected runtime. Keep every existing assertion/candidate unchanged,
  each new file below200lines and owner Cookbook digest exact before/after.

Actual Run01: selected[1,0]/terminal[-1]/OK1,native0,0failures/errors/skips; exact
rows2/3/7/8, preserved original cache/jars and both cache files after mismatch.
Frozen71/prior70/helper27/protected208/unrelated23/owner digest and owned UUID cleanup
preserved. Selected offline UI acceptance does not accept live provider errors,
broader mapping limits or whole-Mobile completion; Mobile remains Partial.

## Selected authenticated screen-off / clipboard return continuation

Selected gates below Passed through the owner-controlled variant recorded below;
the original automatic-Copy method's Failed01/02/03 verdicts remain unchanged.

- [x] Reuse genuine UUID-owned setup/cache/Cook; actual UI Copy must produce the
  unique owned sensitive clipboard marker and exact synthetic Dish while focused.
  Freeze encrypted jars/cache and original Activity/PID; retain production deadlines.
- [x] Announce bounded owner Power-off checkpoint. Require actual noninteractive
  state and natural stop before the original30s clipboard deadline; a late screen-off
  cannot earn background-cleanup credit. Assert locked/cleared session/token/Dish
  and exact cache/jars, with no clipboard read while offscreen.
- [x] Sample>=35s noninteractive state and require a final offscreen check with no observed early wake;
  root prompts owner wake/unlock only after native elapsed evidence reaches it.
  Return must be the same Activity/PID, focused/interactive/keyguard absent, still
  locked with no old Dish/Copy. Read clipboard only behind those foreground guards.
- [x] Observe original owned clipboard absence after normal production foreground
  reconciliation; never manually invoke reconcile. Preserve any unrelated clipboard
  content and perform failure cleanup only for the exact owned marker/text when legal.
- [x] Review/build/freeze/install-r/preflight before owner wait; root runs only this
  method and records source/owner digest/UUID cleanup plus actual outcome separately.

This selects35s screen-off/background and return. Long-background UX, natural LMK,
saved-task restoration, authenticated recreation/process death, cross-app replacement
and clipboard history remain separate; no exact background30s removal promise.

Run01 Failed1/1,37.492s,[1,-2]/[-1]/FAILURES,native0,no watchdog/error. Genuine
authenticated Cook/Copy completed; at25031ms awake/resumed/focused remained true and
the predeadline stop/noninteractive gate was unfulfilled. Hold/return stages were
not reached; original-method hold/return acceptance remains unearned. Exact owner/prior71/helper28/
protected208/unrelated23/UUID cleanup retained. Clarify owner action before retry;
preserve Failed evidence and existing assertions/deadlines, infer no production cause.
Same-source/APK Run02 Failed1/1,54.316s,[1,-2]/[-1]/FAILURES,native0,no watchdog/
error. At25025ms awake/resumed/focused remained true; hold/return stages were not
reached. Owner's later screen-off reply arrived after completion and cannot accept
the timed case. Preserve both failures; improve checkpoint coordination before
another attempt. No rebuild/install, assertion change or production repair occurred.
[Exact evidence](reports/screen-off-clipboard-runtime.md); exact key timestamp unknown.

## Owner-controlled Copy → Power diagnostic variant

Preserve original72 files, production, assertions/deadlines and Failed01/02/03
evidence. A separate selected fixture moves Copy to the owner so no host round trip
is needed between actual Copy and Power. Preparation alone earns no acceptance.

- [x] Prepare genuine setup/cache and a masked, ready synthetic Dish with the Copy
  button visible and IME hidden. Register the foreground clipboard listener on main
  and record baseline policy generation before emitting a bounded180s ready marker.
- [x] Owner taps Copy Dish, observes Copied, then immediately presses Power. Listener
  checks actual resumed/focused/awake/keyguard-absent state directly on main;
  no nested instrumentation dispatch or clipboard read after foreground is lost.
- [x] Bind observed marker to the actual adapter label, exact synthetic text,
  sensitive flag and COPIED status. Read-only policy deadline yields production
  copiedAt=deadline−30000; require preparedAt<=copiedAt<=observedAt<deadline and
  generation exactly baseline+1. Publish one immutable observation; reject later
  production copies without moving the accepted timestamp or deadline.
- [x] Retain actual stop/noninteractive before copiedAt+25s, sampled>=35s plus final
  offscreen/locked/cache guard, same Activity/PID, legal focused return and exact
  cache/jars assertions. The listener timestamp never substitutes for production
  copy time. An offscreen/unobserved Copy cannot earn credit from a later owner reply.
- [x] Never read clipboard offscreen or invoke manual reconciliation. Unregister
  listener/lifecycle observers on every path; cleanup only exact owned marker/text
  under legal foreground and only UUID-owned keys/files. Record cleanup limits.
- [x] Independently review/build/freeze new inventory/runner before owner-ready
  selective runtime; retain all earlier candidates and failures. Keep observed
  clipboard return separate from long-background/LMK/recreation/cross-app/history.

Owner-controlled Run01 Passed1/1,121.836s,[1,0]/[-1]/OK1,native0,0failures/errors/
skips,no watchdog/error. Actual Copy observed at96ms; stop/noninteractive hold marker
atcopy+2414ms; final-off wake marker atoff+35101ms/copy+37513ms. SamePID19125 returns
focused and locked atcopy+64908ms with no original clipboard/Dish/Copy and exact
cache/jars. The later elapsedOff62496ms counter includes wake/return time; it does
not prove62.496s continuously off. Owner/source74/old72/helper29/protected208/23/
release/UUID cleanup preserved. This selected35s sampled-off/return boundary Passed;
broader background/LMK/recreation/cross-app/history/exact background30s remain Pending.

## Selected real external-app clipboard replacement continuation

Preserve all74 existing source/manifests, assertions and prior runtime evidence.
Use the existing genuine setup/cache/Cook and unchanged `OwnerCopyObservation`.
Public clipboard metadata does not authenticate writer UID; the selected proof binds
an actual action in an observed different-UID app to the later exact replacement.
See [ClipboardManager](https://developer.android.com/reference/android/content/ClipboardManager)
and [ACTION_COPY](https://developer.android.com/reference/android/view/accessibility/AccessibilityNodeInfo.AccessibilityAction#ACTION_COPY).

- [ ] Select an installed allowlisted Notes/Chrome app and confirm actual package/
  UID differs from PassChef and instrumentation. At the checkpoint owner opens a NEW
  empty editor or blank omnibox; the fixture performs no save/send/navigation action and no
  helper APK is needed. Unavailable/ambiguous app, field or UID cannot earn credit.
- [ ] Prepare a masked ready synthetic Dish/visible Copy and register the existing
  policy-bound observer before owner readiness. Owner performs genuine UI Copy then
  switches to the prepared external editor; no host round trip follows Copy.
- [ ] Observe actual own stop and exact external root/focused-node package with
  enabled/visible/editable/non-password EMPTY field and required actions. Require
  source UID equality to the selected package and difference from both own UIDs;
  never dump the window tree, unknown text, clipboard contents or account details.
- [ ] Before remote Copy, require own adapter foreground=false and the original
  clipboard claim still pending: same generation/deadline/label and value presence.
  Through that external node perform SET_TEXT with a bounded public UUID marker,
  verify exact text, select all, verify actual selection, and require ACTION_COPY
  success. Recheck source package/window around actions; target test never writes
  the replacement with ClipboardManager or adopts clipboard permissions.
- [ ] Keep PassChef offscreen until the ORIGINAL deadline plus a margin, without
  reading clipboard there. Remote Copy may occur after expiry while ownership is
  still pending; do not claim replacement preceded30s or that the old Dish remained
  system-primary until remote Copy. Do not reset generation/deadline or reconcile.
- [ ] Owner returns to actual focused/keyguard-absent same Activity/PID. Require
  locked/cleared session/Dish, exact cache/jars and exact external marker/new label
  preserved after normal lifecycle reconciliation; old policy label/value cleared,
  original generation unchanged. Preserve the external replacement in cleanup;
  only the earlier exact PassChef-owned marker/text may be cleared when legal.
- [ ] Independently review/build/freeze the new selected fixture/runner, then root
  install-r/preflight/digest and owner-ready bounded runtime. Record external UID/
  successful remote action/matching return as observed action provenance, not an
  authenticated per-clip writer-UID claim. Keep LMK/recreation/history/API26 separate.

Actual cross-app Run01 Failed1/1,354.918s,[1,-2]/[-1]/FAILURES,native0,no watchdog/
error. Genuine Copy was observed, but no completed Chrome Copy action appeared by
copy+180073ms; final state locked/unresumed. Exact owner/prior74/helper30/protected208/
unrelated23/UUID cleanup preserved. The replacement/return boundary remains Pending;
owner's visible empty-omnibox confirmation does not identify the rejected predicate.

### Read-only focused Chrome editor probe

- [x] Add/review a separate selected probe without Activity launch, remote mutation
  or clipboard access. Preserve frozen77, Run01 logs/candidate and every assertion.
- [x] Capture one immediate sample with a60s host bound: root-is-Chrome, focus found, same window,
  node-is-Chrome/editable/enabled/visible/focused/non-password, text null/empty,
  hint-shown boolean, SET_TEXT/SET_SELECTION/COPY action presence.
  Emit no node text/hint/package strings, tree, account data or clipboard content.
- [x] Compile/freeze/install-r/recheck owner digest before selected owner-ready
  execution. Record capability observations separately from replacement acceptance;
  no blind retry or assertion relaxation follows a diagnostic-only Passed result.
- [x] After owner explicitly clears the Chrome address bar without Copy, take a
  new-number read-only sample. Do not repeat the full replacement case until the
  required safe-empty predicate is actually observed; preserve existing code/verdicts.

Source inspection supports a harness hypothesis: empty TextView accessibility may
expose hint text and omit SET_SELECTION until actual text exists. This does not
establish which frozen precondition rejected the observed device. Actual probe02
Sampled0.048s: Chrome field/window/focus/SET_TEXT/SET_SELECTION true; reported text
not empty, hint not shown, COPY absent. That sample rejects the frozen empty-text
gate; it does not show a hint/selection-action problem or identify all of Run01.
Cross-app replacement acceptance stays Pending; no unknown text was exported.

Probe03 after owner select-all/delete Sampled0.056s: current Chrome field/window/
focus/SET_TEXT true, textEmpty/textNull false, showingHint true, SET_SELECTION/COPY
absent. This is a different sampled editor state; it does not reconstruct Run01.

### Separate hint-aware real Copy variant

- [x] Add only a new selected test/helper; keep all78 old files, original Run01 and
  both read-only probe candidates/assertions exact. Use the existing real pending-
  claim, owner Copy observer, original deadlines and return preservation assertions.
- [x] Implement initial empty-editor recognition only when reported text is null/empty OR it is
  showing a nonempty hint and reported text equals that exact hint. Retain Chrome
  UID/root/window/editable/focused/visible/enabled/non-password guards; never export
  hint/text or replace a nonempty non-hint field.
- [x] Implement initial SET_TEXT requirement; after populating the public UUID marker, require
  exact marker/non-hint state and actual SET_SELECTION support before selecting all.
  Keep exact full selection/COPY success and fresh source window guards unchanged.
  No target-process clipboard replacement, permissions or production fix.
- [x] Independently review/static-compare originals, compile/lint/freeze new inventory
  and install-r/digest: Passed10s10/84,lint0/11/1; source80/prior78/helper32 exact,
  installed test2e7c5edf/debug1f42 and owner digest match the frozen candidate.
- [x] Execute the new selected owner-ready runtime after fresh native preflight.
  Diagnostic samples and source compatibility explain the new harness branch,
  not a retrospective production cause or actual replacement acceptance; Mobile
  remains Partial and the new variant's runtime acceptance remains Pending.

Hint-aware Run01 Failed1/1,124.584s,[1,-2]/[-1]/FAILURES,native0,no watchdog/error.
Actual Chrome Copy was observed atcopy+2808ms, return requested at31069ms and
focused locked return failed at66090ms, samePID7249. Exact owner/prior78/helper32/
protected208/unrelated23/UUID cleanup preserved. Generic return phase cannot
identify cache/RAM, replacement clipboard, policy-forget, stability or UI assertion;
no production cause or replacement preservation is accepted.

### Additive post-return diagnostic variant

- [x] Add only a separate diagnostic test and bounded ownership-observation helper;
  preserve all80 prior files/assertions/candidates, including both Failed methods.
- [x] Retain every original assertion; add phase checkpoints before/after return
  synchronization, exact cache/RAM, deadline, guarded replacement comparison,
  stale-owner forgetting, generation, stability and UI absence checks.
- [x] Implement own-RAM boolean diagnostics only for adapter foreground, value/label absence,
  original generation/deadline retention, zero deadline and original-label equality.
  Clipboard diagnostics occur only inside the original legal-foreground guard and
  export presence/single-item/new-label/exact-public-marker booleans, never content.
- [x] Review/static-compare, compile/lint/freeze/install-r/owner digest: source82/
  prior80/helper33 and candidate/local/installed testec93b9c0/debug1f42 match;
  build8s10/84,lint0/11/1; original9 assertTrue calls byte-identical and ordered.
- [x] Complete the selected owner-ready diagnostic runtime and record its actual
  outcome separately. No manual reconcile, clipboard rewrite, permissions or clock/
  reset changes; preserve external replacement and prior Failed evidence. Preparation
  and active setup alone earn no replacement acceptance.

Diagnostic Run01 Failed1/1,48.998s,[1,-2]/[-1]/FAILURES,native0,no watchdog/error.
Failure atcopy+2980ms preceded remote Chrome Copy/return; own focused session/Dish
were still ready. Owner explicitly confirmed Copy Dish more than once, which does
not meet this selected single-copy protocol. Generic failure does not identify the
exact assertion independently. Preserve Run01/frozen01/owner observation; a fresh
same-code one-Copy attempt requires new evidence numbers/preflight, no relaxed guard.
Owner/prior80/helper33/protected208/unrelated23/UUID cleanup remain exact; actual
replacement preservation and post-return diagnosis stay Pending.

Diagnostic Run02 Failed1/1,105.461s,[1,-2]/[-1],native0,no watchdog/error.
Chrome Copy+3769ms; focused return passed exact cache/session and deadline checks.
Atcopy+51677ms legal clipboard read showed one item, different label and exact
public marker; at51679ms `return-claim-forgotten-check-failed` localized stale
own-RAM ownership. Foreground/generation/original deadline remained true, old
value/label remained present. External replacement was preserved at that sample;
stability/full acceptance remain Pending. Owner/prior80/helper33/protected208/
unrelated23/UUID cleanup exact. Three sanitized own-package not-in-focus denials
support a missed eligible read; they do not prove the exact callback ordering.

### Repair evidenced foreground ownership reconciliation

- [x] Plan/review only two production paths: `MainActivity.kt` and
  `cookbook/OwnedClipboard.kt`. Keep all existing tests/assertions and shared
  ownership policy unchanged; preserve all Failed candidates and original manifests.
- [x] Follow actual window focus in onStart and both focus-change directions;
  retain onStop false. Add a separate retry callback for expired unavailable reads:
  first immediate post, at most ten further100ms retries per foreground/ownership
  generation. Guard each callback by foreground, original generation and expiry;
  cancel on focus loss, successful new Dish/username, successful clear or forget.
  Do not restart the budget on failed reads or queue duplicates. Retain claim on
  null/exception; never renew deadline/token or restart original30s cleanup.
- [x] Compile/lint and freeze exact before/after SHA deltas for those two paths;
  new runner/support must preserve original208 manifest and old34 helpers, verify
  other80 app sources/206 protected/23 unrelated files, and bind changed debug/test/
  release APKs. Archive original release; do not claim production/APKs unchanged.
  Passed28s28/136,JVM10 passed,lint0/11/1; frozen01 source82/80 unchanged+2 deltas,
  debugd90cf0e3/testec93b9c0/release153b0224 and archive/current inputs exact.
- [x] Install/recheck owner digest: install01 binds frozenf377224c, installed debug/
  test exact and owner894f73…a4d6 unchanged before/after each package check.
- [x] Run the unchanged strict diagnostic with
  fresh owner readiness. No added test wait/manual reconcile/clock/permission or
  assertion change; record Failed if natural main-queue retry remains insufficient.
  Accept only actual full selected success including exact replacement, forgotten
  ownership, original generation, stability, locked UI/cache and owned cleanup.
- [ ] Obtain actual repaired return-boundary evidence; Run01 failed during genuine
  setup before Cook/Copy and cannot verify or reject the clipboard repair.
- [x] After strict result, run unchanged relevant adapter3: Passed3/3,99.636s,
  per-method[1,0]/[-1]/OK3,native0,zero failures/errors/skips or watchdog.
- [x] Run unchanged recreation1 separately: Passed1/1,31.548s,[1,0]/[-1]/OK1,
  native0,zero failures/errors/skips or watchdog,
  through the frozen regression runner, with installed/owner/source and original
  proof-directory-set checks; record each actual outcome independently.

Repair runtime is Pending; no new cross-app acceptance or broader Mobile completion.

Repair Run01 Failed1/1,182.282s,[1,-2]/[-1],native0,no watchdog/error,
`owner-replacement-setup-biometric-failed`,copy elapsed=-1,PID17606. No Cook/Copy,
remote Chrome action or return boundary occurred. Final focused/busyfalse/readyfalse
does not establish a Keystore cause or failure of the clipboard repair. Frozen01/
APKs/owner/80 unchanged+2 deltas/34 helpers/206 protected+2 deltas/23 unrelated/
UUID cleanup preserved. Prior accepted Keystore evidence and prior failures remain.

Repair adapter regression01 confirms only actual foreground locked proof Activity
synthetic Dish cleanup after30s, synthetic replacement preservation and newer Dish
generation/deadline protection. Owner/source/APKs/helpers and original proof-directory
set exact. It does not cover genuine authenticated Cook/Chrome UID replacement;
strict repair01 remains setup Failed and cross-app repair acceptance Pending.

Repair recreation regression01 covers actual locked Activity recreation retaining
the synthetic owned Dish and original30s cleanup deadline, not authenticated session
restoration, process death or natural LMK. Frozen01/APKs/installed/owner/80+2delta/
34helpers/206+2delta/23 unrelated and UUID proof-directory set exact. All four
selected relevant regressions Passed; genuine cross-app repair remains Pending.
