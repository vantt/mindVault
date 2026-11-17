# Authenticated clipboard recreation review —4November2026

Status: **Source, selected emulator and physical recreation Passed; Mobile Partial**.
Independent source review only; no Gradle, device or implementation/test edits.
Scope: two additive Android test files and their existing production/helper paths.
Initial source hashes/unchanged170-file audit recorded in
`/home/vantt/.local/share/passchef-mobile-evidence/2026-11-04-plan-completion/reviewer-authenticated-source01.json`.

## Initial findings —resolved by source correction

- **P2 — preserve failure and complete exact-owned cleanup.**
  `authenticated-clipboard-recreation-test.kt:98–102`: cleanup runs record parsing,
  key deletion and directory removal sequentially in one finally block. A record/
  Keystore exception skips directory removal; any cleanup exception or final assert
  can replace the actual setup/recreation/clipboard failure. Close Activity, remove
  only owned aliases and remove the exact UUID directory independently; retain the
  original failure and add cleanup failures as suppressed, or fail cleanup itself
  when the test body otherwise succeeded. Record actual cleanup result separately.
- **Coverage limit — original authenticated session retirement.**
  `authenticated-clipboard-recreation-test.kt:62–69` checks new locked state and old
  Activity destruction, while `requireLocked()` reads only replacement-controller
  flags. That does not independently assert that the destroyed controller discarded
  its original authenticated session/Dish. Add read-only original-controller state/
  session-null assertions after destruction before claiming that retirement boundary.
  Replacement locking remains a valid, narrower assertion.

## Corrected source re-review

**Passed before runtime.** The test preserves/rethrows the primary body failure.
Clipboard cleanup, Activity closure, record read, each owned alias deletion and
UUID directory removal run independently; cleanup failures attach as suppressed
to the primary or fail an otherwise successful run. Directory removal is attempted
even when record/key/close cleanup fails. Marker/content ownership checks stay exact.

After actual old-Activity DESTROYED and replacement identity/PID/directory checks,
`requireCleared(original)` now observes the retired controller too. Both controllers
must have null session, expired clock, null entry selection/token and cleared
session/Dish flags. No cleanup/session field is written by the new assertions.

The one-row offline fixture now explicitly assigns Recipe2/label1/username3 and
first-data-row2. This honors the real minimum-three-Recipe proposal rule while
exercising normal UI confirmation, rather than changing the proposal algorithm or
fabricating a saved snapshot. UUID/PID, original deadline/generation and a bounded
biometric-wrap-present bit are emitted as safe metadata; no jar/clipboard text is
exported. A wrap-present bit alone still cannot certify an observed biometric.

Re-review hashes/unchanged old170 recorded in `reviewer-authenticated-source02.json`.
No remaining source-review blocker found. Root's compile/lint and frozen candidate,
host selection and actual native/cleanup result remain separate requirements.

## Source boundaries checked

- Setup/reopen use actual password forms, original `VaultController` and real kitchen;
  no injected session/jars, fake Google response or synthetic clock. A genuine
  strong-biometric prompt can occur through the ordinary product path. A later pass
  must identify password-only/emulator versus observed physical biometric scope;
  `vaultReady` alone does not certify a particular hardware authentication path.
- Actual offline preview/confirmed mapping creates a durable cache; source origin,
  literal cached Dish and favorite/recent identities are compared with known
  synthetic expectations. Full file inventory hashes precede Copy/recreation and
  are checked again before reopen. Post-password-recovery payload comparison permits
  legitimate envelope alias rotation while preserving encrypted Spice payload.
- One actual Copy is bracketed by monotonic time; generation increments once.
  Sensitive system label/text are checked before expiry; same process adapter,
  original deadline/generation and marker are checked across real recreation.
  Recreated foreground must be observed before that original deadline. After its
  real30.5s edge, clipboard absence plus cleared policy metadata is required;
  no policy write, manual reconcile, timer reset or test-clock advancement exists.
- Read-only reflection observes real session/clipboard state. The Looper-aware
  foreground helper avoids nesting `runOnMainSync` while already on main.
  Own foreground/keyguard gates protect clipboard observations; cleanup compares
  exact own marker and synthetic text before clearing. No unknown clipboard dump.
- UI interactions use Compose actions and normal scroll-to semantics, with actual
  foreground guards around named clicks. This does not prove physical pointer
  geometry or full IME/touch accessibility. No raw cross-app pointer injection or
  global keyboard change was introduced.
- UUID saved-state opt-in belongs to the existing debug proof Activity. Assertions
  require distinct destroyed/replacement Activities, same process and same owned
  storage directory. This is same-process Activity recreation, not process death,
  saved-task restoration, natural LMK or a background clipboard timing guarantee.
- Old170 source/test paths remain unchanged; two files are additive. Existing
  Golden/physical assertions and production lock/window/clipboard policies stay
  untouched. Compilation alone cannot prove any new recreation behavior.

## Selected native Run01 —Passed125.698s

Independent read-only audit:
`/home/vantt/.local/share/passchef-mobile-evidence/2026-11-04-authenticated-recreation/reviewer-authenticated-runtime01.json`.
Raw SHA256`675453f6bd7124bf23c7b7876f827e642ce730586afa0f0852ab4d640b5378c3`.
Native selected`[1,0]`, eight ordered diagnostic2 bundles, single terminal`[-1]`,
anchored`OK (1 test)`;125.698s native/137.635s host, native0, no timeout.

Every checkpoint binds one UUID`fcfee749-e2e8-421d-b00b-31b850b99232` and PID5719.
Actual authenticated Copy set deadline495028/generation1; after replacement Activity
and old/new controller checks, the original deadline/generation remained exact.
At495746ms,718ms after the original deadline, actual clipboard/policy cleanup and
exact jar/cache preservation assertions passed. Password reopening then loaded the
same cache, favorites/recent/search and fresh literal cached Dish; encrypted Spice
payload comparison and absence of an unsolicited new clipboard Copy passed.

All diagnostic biometric-wrap-present bits are false: this is genuine password-only
emulator authentication, with no physical/biometric/Google credit. The test requires
actual Activity recreation within the same process; no process-death/LMK/saved-task
or exact background30s/history acceptance follows.

Independently recomputed frozen173 source hashes and candidate04 APK bytes match.
The two new test source hashes remain the corrected reviewed versions. Changed
test APK SHA256`210761dd0be3f313f0b552d3a20f4a893d11e90057df807e09ad2289d5f766c1`;
debug/release remain identical to prior source candidates. Recorded fresh-emulator
install inputs match frozen app/test hashes and both installs report Success.
The subsequent `installed-apk-identity04.json` binds actual installed app/test APK
digests to these same candidate04 identities; install input alone was not used as
that stronger proof.

Build04 log terminal success2m33s,94 tasks10 executed/84 up-to-date; lintDebug
succeeded with retained/up-to-date XML0 errors,7 warnings,1 hint. No new JVM rerun
is claimed for additive Android tests. JUnit end0 follows the independently reviewed
finally cleanup, so its checked cleanup did not throw. The subsequent root
`post-runtime-audit01.json` explicitly records the exact UUID absent, no remaining
noBackup fixture names and173 frozen paths unchanged at that audit instant. This
does not claim continuous absence or shutdown of resources later reused for a
separate saved-task investigation.

## Selected physical Run04 —Passed113.868s

Independent raw/source/artifact/install/protection review **Passed** for Xiaomi
serial`PN7PW8SGXKLVPRIV`, candidate04's exact authenticated-recreation selector.
Artifact: physical `run04/reviewer-physical-runtime04.json`; raw SHA256
`9e3cb9971ea76b3c4773462125cfce03cf2a1339c8edd2c9295020ae46df6b61`.
Actual selected JUnit`[1,0]`, eight diagnostic2 bundles, terminal`[-1]`, anchored
`OK (1 test)`,113.868s native/116.477s host, native0/no timeout. The wrapper now
explicitly refuses failed native classification; wrapper exit0 alone is not proof.

One actual fixture`2f671f07-e7ea-4fb0-9222-780c181858a2` and PID15251 bind all
checkpoints. Genuine unique setup progresses from no wrap to wrap-present at ready;
root records owner biometric actions on the ordinary SDK/cipher path. Reopening
again uses the actual password form. A wrap-present bit alone does not certify the
timing or modality of every biometric prompt; no synthetic callback was introduced.

Actual UI Copy sets original deadline1461316489/generation1; these remain exact
after different/destroyed/replacement Activity assertions, same PID/UUID and old/new
session retirement checks. Sensitive own clipboard/Dish remains present before
expiry. At1461317046ms,557ms after the original deadline, actual clipboard absence,
cleared owned metadata and exact jar/cache byte comparisons pass. Genuine reopening
then reloads the original Menu, favorites/recent/search and cooks the literal Dish;
encrypted Spice payload remains exact and no unsolicited Copy occurs.

The independently checked173-entry candidate04 source archive matches its original
freeze, including the original debug fixture and reviewed two new tests. Physical
on-device app/test digest records match debug`9fd023f4…31b8` and test`210761dd…66c1`;
the later saved-task debug changes were not substituted. Real readiness gates
evaluate Awake/keyguard=false before each install/native; absent target PID is
required before native launch. Current root final audit records target PID absent
after terminal, owner digest unchanged and plugged-awake policy0.

Original owner encrypted Cookbook digest`894f73…a4d6` remains exact. The pre-runtime
all-file inventory contains that one original file; its digest is preserved, all
six preexisting top-level proof-directory names remain, and only the new UUID is
absent after checked cleanup. This is the observed before/after scope, not a claim
about continuously absent historical directories. Original public APKs are archived.

Prep01–03 contain no install/native-start records and earn no runtime credit;
keyguard-dependent preparation ended without replacing those outcomes. Temporary
plugged-awake preparation policy is recorded restored to original0 at fixture
creation, while no Copy checkpoint had yet been observed. This is a bounded host
observation, not an exact Copy/key timestamp. Existing proof `KEEP_SCREEN_ON` remains
explicitly part of the selected runtime; no general production sleep-policy claim.

This closes the selected genuine physical authenticated same-process Activity
recreation/original clipboard-deadline gap. It grants no process-death/saved-task/
natural-LMK, history-wide clipboard removal, exact background30s, TalkBack, full
layout/device matrix or Google401/cancellation acceptance.

Unresolved: saved-task/LMK and remaining physical/accessibility/provider plan gates.
