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
