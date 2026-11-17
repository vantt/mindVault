# Saved-task continuation review —4November2026

Status: **Source and selected controlled emulator task restoration Passed; Mobile Partial**.
Independent source review of two debug proof paths and five additive Android test
paths. No Gradle, device or source edits by reviewer. Final compilation remains a
root requirement after its latest canonical-path/secure-window corrections.

## Initial findings —resolved by source correction

- **P2 — retain the requested UUID when canonicalizing cleanup input.**
  `saved-task-baseline.kt:25–30` resolves the requested UUID directory before
  comparison and checks only its parent plus the stored resolved fixture name.
  UUID-A symlink→sibling UUID-B beneath the same noBackup parent satisfies these
  predicates and redirects the cleanup selector toB. Retain the expected requested
  name and require canonical directory name equality; also bind baseline-file
  canonical path/name before reading it. Do not follow a different sibling UUID.
  Disposable source-predicate reproduction (not native acceptance):
  `reviewer-saved-task-locator-predicate01.json` in the authenticated evidence root.
- **Coverage correction — current restored grant absence.**
  `saved-task-cold-test.kt:78–84` describes no grant but checks session/Dish/selection
  only. The journal checks token absence at creation; add current token-null to the
  actual resumed-controller assertion before claiming the full stated invariant.

## Focused correction re-review

**Passed before runtime.** `SavedTaskBaseline.read` now retains the requested
canonical UUID name and requires both its canonical parent and exact name; baseline
file canonical identity must also equal its own path. Normal owned-directory
predicate accepts, sibling-UUID redirect and baseline-file symlink predicates refuse
in an independent disposable check. The resumed-controller locked assertion now
reads actual `CookbookSources.accessToken` and requires null alongside session/Dish/
selection state. All are read-only checks; no old assertion was weakened.

Current seven saved-task source hashes/line counts and predicate results are frozen
in `reviewer-saved-task-source02.json` in the authenticated evidence root. No remaining
source-review blocker found. Build08 after these edits, candidate/host review and
actual native restoration/cleanup remain required; earlier builds do not cover
the corrected source.

## Reviewed source design

- Ordinary debug fixtures retain null journal and existing behavior. Only explicit
  `passchefSavedTaskProof=true` enables the new passive journal. `super.onStop()`
  performs the real existing lock before stopped-state observation. Saved state
  contains only validated UUID locator; no session/key/jar/token/Dish is restored.
- Journal output is versioned, atomic and16KiB-bounded, with allowlisted lifecycle
  fields only: own UUID, PID/task IDs, counts, booleans and monotonic timestamps.
  Cleared-state reflection is read-only. Production `main`/shared/client code and
  release authentication policy remain outside this debug instrumentation scope.
- Warm stage genuinely authenticates, confirms the offline Menu and cooks through
  existing UI. It records encrypted-record/cache digests and then invokes actual
  Android Home; preparation requires real save/stop callbacks for the exact task,
  cleared original session and unchanged production files. Warm preparation grants
  no process-death/restoration acceptance. Failed warm work closes/removes only its
  owned fixture with independent cleanup and primary-failure suppression.
- Prepared warm intentionally leaves its real stopped Activity/task for the host
  and requires `waitForActivitiesToComplete=false`. Host must inspect actual task/
  PID after native terminal, kill only the background target, verify process death
  and retained task, then focus that existing task. Timeout alone proves no death.
- Cold has no ActivityScenario, Compose launch, `startActivity` or synthetic saved
  state. It requires host-recorded restored PID differing from warm, same task/UUID,
  nonnull saved state, matching locator, cleared creation and exactly one new
  creation. It registers actual Application callbacks before host Home/task-focus
  to capture the existing restored Activity. A new cold intent or CLEAR_TASK cannot
  substitute. Actual foreground/FLAG_SECURE and locked session/Dish are checked.
- Native accessibility actions are limited to the own package/window, bounded tree
  observation, enabled controls and unique known synthetic labels; ambiguity fails.
  SET_TEXT/CLICK and own-window IME hiding follow normal product UI. No fields,
  clipboard or arbitrary accessibility tree are dumped. Known-label matching is
  not spoken TalkBack or full physical pointer/IME-layout acceptance.
- Real password recovery must reload original mapping/favorites/recent, cook the
  known literal Dish and preserve encrypted Spice payload. Cold never injects a
  session or writes a fake provider/cache outcome. No clipboard Copy is requested.
- Cold always unregisters its callback. Host owns post-terminal task closure and
  separately selected cleanup; cleanup requires original warm PID/UUID and no active
  selected task, then independently removes only record-owned aliases/directory.
  Host failure/watchdog paths must still explicitly close/clean that known fixture.

## Evidence limits and next review

The earlier no-restart locked diagnostic/recognized task-focus command establish
only their observed capability; no saved task was restored by those checks.
Compile/freeze exact corrected debug/test identities, review the concrete host
sequence and execute warm→actual background death→retained
task restoration→cold→owned cleanup before granting bounded emulator acceptance.
Natural LMK, physical biometric/device matrix and Google401/cancel remain separate.

## Candidate08 and host sequence review

**Source/candidate review Passed for the selected isolated emulator scope.**
Independent recalculation confirms179 frozen paths match both current source and
all179 archived source entries; candidate08 APK bytes match the manifest. Relative
to old170, only opt-in `CookbookProofActivity` changes; previous test assertions stay
exact. Debug`707bb621…fc8b1`,test`95586c2d…bceb1eb`,unsigned release`039e2420…641d`.
Build08 after the reviewed corrections is recorded Passed1m33s. Install08 records
empty preexisting noBackup and successful candidate app/test installation; this
alone does not replace subsequent actual installed-byte identity evidence.

Latest host-source-freeze02 was reviewed, including corrected AVD CR/blank-line
normalization; classifier remains byte-identical to the previously reviewed one.
Independent protocol replay15/15 Passed; `reviewer-host-source01.json` freezes
reviewed runner/host/protocol/classifier hashes. No device calls were made by reviewer.

- Fixed emulator5580/AVD/qemu/boot checks forbid physical substitution. Warm requires
  exact selected JUnit/ordered completed phase bundles, original PID/task/UUID and
  bounded original save/stop journal before any kill. Host confirms native terminal
  and inactivity, actual background and retained ActivityRecord/task, then `am kill`.
- Original PID absence, retained same ActivityRecord/task and unchanged encrypted
  files are checked before focusing that existing task. Restoration requires a new
  PID and actual nonnull saved-state/locator/cleared journal with exactly one new
  creation. Neither task ID alone nor new process alone earns credit.
- Cold attaches without restart. Exactly its first completed callback-registration
  checkpoint, while the child remains live, permits one Home/focus sequence; later
  exact selected native terminal/phases/PID are required. Timeout/host error retains
  possible native liveness and Unknown; killing the ADB observer is never counted
  as native termination, and no destructive cleanup runs while liveness is unknown.
- Only after terminal/inactive does host force-stop the selected debug package and
  run the separate exact-UUID/warm-PID cleanup selector. Primary failure and cleanup
  failures remain separate; an absent cold pass cannot produce overall Passed.
- Host private inventory compares top-level names and default Cookbook digest,
  rather than every file in preexisting proof directories. For complete original
  file-preservation credit, require the selected initial inventory empty as observed
  in install08, or extend inventory hashing. Do not generalize names-only equality
  to all historical private-file contents. Native restoration/cleanup still Pending.

## Separate physical authenticated wrapper note

The read-only review found that `run-physical01.py` records native classification
but may still exit0 when JUnit Failed and preservation audits passed. Wrapper exit0
must never determine acceptance; root needs an independent post-terminal overall
native+preservation gate. An already-live readiness wait must not be duplicated or
restarted just to alter wrapper source. The wrapper also lacks its own inactive-
instrumentation check; root must independently verify absence of prior physical
instrumentation before install/runtime. Its fixed serial, actual Awake/keyguard
gates, original owner/all-file digests, public APK backups, verified installed
identities and timeout-without-cleanup boundaries were otherwise inspected.
No physical native acceptance follows from this source review.

## Attempt01 retained failure and native warm harness correction

Attempt01 is **Failed**, selected warm`[1,-2]/[-1]/FAILURES`, native0.105s,
no task-phase/UUID/prepared cache, no process death, no restored/cold proof.
The raw exception originates in ComposeRule coroutine `TestScope.enter` with a
missing ServiceLoader exception handler under the app-first no-restart classloader.
Its retained raw SHA256 is
`83ef483de988cdb178ea257256cb7f9cf6c50f2d016255e743862b8a5bbd78a8`.
Host correctly records Failed and `nativeMayStillLive=false`; none of that failed
pre-method run is converted into restoration acceptance.

**Corrected three-file harness source review Passed before runtime.** Warm now
uses an actual native UI fixture without ComposeRule/TestScope. It retains genuine
password setup, explicit Menu column confirmation, durable exact cache/favorite/
recent identities, masked/literal revealed Dish, real Home save/stop, original
locked session/token state, byte baselines and independent failed-stage cleanup.
The new fixture adds no service-loader workaround, dependency or product policy.
Existing authenticated-recreation tests are untouched. The password constant is
read-only synthetic test input, not an injected session.

Native UI matching now requires exact label equality, avoiding `Recipe` substring
matching inside the tab-separated rows textarea. Own-package/root, bounded500-node,
unique enabled/visible controls, normal SET_TEXT/CLICK and read-only observation
guards remain. Own-window IME hiding requests no global setting or pointer bypass;
this still grants no full touch-target or spoken-accessibility acceptance.

Host-freeze03 adds an exact empty private-namespace/default-null precondition,
closing the earlier names-only inventory scope gap for this selected fresh emulator.
The other reviewed host/protocol/classifier bytes remain bound. Independent source/
host hashes and original failure audit: `reviewer-native-warm-source01.json`.
Final Build09/new candidate identities and a new exclusive evidence prefix/root
are required before execution; candidate08 and attempt01 remain immutable history.

## Attempt02 and native scrolling correction

Attempt02 remains **Failed24.442s**, exact warm`[1,-2]/[-1]/FAILURES` after genuine
password setup. Native failed to make the Confirm mapping button visible; no warm
task preparation/death/cold acceptance occurred. Original raw SHA256
`16ec96bfc66255e192eb6119f4ad6f2d4571ab5f04b2a62a88618c62323b69ce` is retained.

Focused `SavedTaskUiActions` real-scroll source review **Passed before runtime**.
It adds ordinary accessibility SCROLL_FORWARD/BACKWARD on the first outer visible,
enabled, noneditable own-package scroller. It refreshes/checks foreground/package/
state immediately before movement; successful movement counters and boundary
direction reversal do not substitute for finding the requested actual control.
Known control actions still require fresh exact label, unique match, enabled/type,
visible state and own foreground before SET_TEXT/CLICK. No input coordinates,
session/key/lifecycle manipulation, test-clock movement or controller write exists.

Search has a64-scroll-attempt cap and real15s monotonic deadline; observations
remain500-node bounded, node copies are recycled and only numeric counters are
emitted. The direction reverses when the outer real scroller reports a boundary;
editable Recipe text areas are excluded. Failure still throws if the real target
never becomes visible; no assertion was relaxed into a diagnostic-only pass.

Independent audit `reviewer-native-scroll-source01.json` confirms the sole change
relative to the previous180-path source freeze is this142-line helper. Root Build10
after correction records Passed1m20s,94 tasks9 executed/85 up-to-date. New frozen
candidate/install identities and exclusive Attempt03 are still required; no source
review or successful compilation turns Attempt02 into native acceptance.

## Attempt03 retained failure and exact-PID SIGKILL host review

Attempt03 overall remains **Failed**. Its unchanged warm native stage Passed14.571s
with actual cache/Cook/save/stop, task16/PID9548 and exact selected phases. Host
`am kill` returned0 but the original process did not become absent within20s;
no death/restoration/cold credit follows from command success. The separate selected
cleanup Passed and final private inventory is empty. No native stage remains live.

The next exclusive Attempt04 host-only delta is **source-reviewed Passed before
runtime**; `reviewer-sigkill-host-source01.json` binds its exact freeze11 hashes/diff.
No app/test source, candidate10 APK or protocol/classifier change accompanies it.
After genuine warm terminal/inactivity/background, same task/ActivityRecord and
byte guards, it reconfirms the exact package PID, reads own UID/start ticks and
uses one own-UID `run-as` shell to check package cmdline, UID and start ticks before
one `kill -9` against that exact positive PID. Package/whole guard use `shlex.quote`;
UID/start-tick strings are numeric-validated and no arbitrary target is accepted.

Existing isolated AVD/qemu, empty private namespace, no unknown instrumentation,
retained task identity, actual PID absence, new PID/non-null saved-state restoration,
live cold checkpoint and separate post-terminal cleanup/first-failure gates remain.
This is controlled SIGKILL with immediate shell checks, not kernel-atomic identity
binding, `force-stop` restoration, natural LMK or physical acceptance. Actual kill/
retained task/cold result must still be observed; source/quoting review proves none.

## Attempt04 actual retained-task restoration —Passed

Independent full raw/host/source/archive/install/provenance/cleanup review **Passed**.
Evidence root:`2026-11-04-saved-task/attempt04`; independent result:
`reviewer-saved-task-runtime04.json`. Each native selector has exact`[1,0]/[-1]/OK1`,
native0/no timeout: warm16.242s, cold7.850s, cleanup0.294s. Host overall Passed,
no primary/cleanup failure and `nativeMayStillLive=false`. Attempts01–03 retain
their original Failed results; the successful warm stages do not replace them.

Actual warm task17/UUID`6295c57f-eb7f-4f17-aa41-d3615402f1dc` starts with PID9840.
Real password setup, confirmed cached Menu/favorite/recent and literal Cook pass;
actual Home produces cleared stop and saved-locator callbacks. Recorded same-task
ActivityRecord identity`92892908` is preserved in all three independent recents
snapshots before death, after death and after restoration.

After warm terminal/inactivity/background and original byte checks, the recorded
guarded own-UID SIGKILL targets PID9840/UID10227/startTicks562464. Two subsequent
`pidof` observations exit1 before existing-task focus. The after-death recents
snapshot retains that exact ActivityRecord/task while its root process is absent;
the restored snapshot binds root PID9955. This is actual observed process death,
followed by focus of existing task17, with no command-success-only inference.

Restored journal independently validates createCount1→2, new PID9955, same task/UUID,
nonnull saved state, matching locator and cleared initial session. The cold native
attaches without restart; its completed live callback checkpoint gates the one
Home/focus bounce and captures the existing restored Activity. Actual controller/
window assertions require foreground, secure flag, locked null session/token and
absent Dish before real password entry. Recovery reloads the original exact cache/
favorite/recent mapping, cooks/reveals the literal Dish and preserves encrypted
Spice payload. Original production file digests are checked before recovery.

The separate post-proof cleanup selector uses PID10075 and the same UUID/warm PID/
task provenance. Its selected native pass and final inventory prove the new UUID
removed; initial/final private inventory are exactly empty with no default Cookbook.
Root's final controlled result SHA256 is
`5c4b232835be99beb55fed026a65a2f5ffcb621cc1deb0bab1202e833e10947b`.
Raw SHA256: warm`1704b298…138c1`,cold`893ab9a3…37668`,cleanup`691b31fc…48246`.

Independent recalculation confirms all180 frozen source paths still match current
files and all180 archived source entries; subsequently added files are outside
that frozen run. Candidate10 archived bytes and recorded actual installed app/test
digests match debug`707bb621…fc8b1`,test`c01026a2…c9e16`; unsigned release retains
`039e2420…641d`. Host-freeze11 also remains exact. No changed test/source or newer
candidate was substituted for this actual run.

This accepts controlled background SIGKILL, genuine saved-state restoration of
the retained system task and password/cache behavior on this isolated emulator.
It grants no natural LMK, physical biometric/task-restoration matrix, Google error/
cancellation, process-death clipboard deadline/history or full accessibility credit.

Unresolved: natural LMK and broader physical saved-task/biometric matrix;
physical overall-result/inactivity gates;
physical owner/device readiness.
