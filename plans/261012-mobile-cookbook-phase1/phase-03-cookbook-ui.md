# Phase03 — Compose Cookbook, reveal and owned clipboard

Status: **Partial — code complete; physical acceptance Pending**. Priority:3.
Latest selected [Google cache UI](reports/google-cache-ui-runtime.md) Run02
Passed1/1,42.393s: exact cached Cook/favorite/recent/search and authenticated
same-process recreation with real biometric; broader physical matrix Pending.
Later [network-off Run04](reports/network-off-runtime.md) Passed1/1,31.477s:
same cached UI assertions under actual network absence plus real network-failure
cache preservation. Original UI Run01 and network Run03 wrap causes unresolved.

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
