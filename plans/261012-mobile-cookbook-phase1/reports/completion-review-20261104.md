# Current Cookbook completion review —4November2026

Status: **Source/local-artifact review Passed; product acceptance Partial**.
Independent reviewer; no implementation/test edits, Gradle or device operations.

Reviewed current Android production session/kitchen/cache/Google ownership paths,
preexisting themed UI changes, new UI components/fonts and current continuation
against the [Cookbook contract](../../../docs/specs/mobile-cookbook.md).
No demonstrated P1/P2 source regression found within this scope.

## Source checks

- `MainActivity.kt:25,65` retains secure window and stop-time input/session cleanup.
  `VaultController` gates cooking and session publication by foreground, epoch and
  monotonic expiry; `VaultCooking` rejects older completions and clears jar copies.
- `CookbookController` clears prior Dish/reveal/Copy on edits/navigation/lock.
  Theme extraction preserves action callbacks and existing test tags; inputs remain
  unsaved Compose state. Reveal and clipboard ownership policies remain unchanged.
- `AndroidSheetsAuthorization.kt:16–20,25–39` consumes actual pending callbacks but
  publishes only current owned grants. `CookbookSources` clears RAM token on401,
  preserves last-good cache on error and serializes final cache commits with
  `SourceOwnership`. These source checks do not establish genuine provider outcomes.
- List/manual/source/mapping views retain parent scrolling, wrapping action rows,
  full-width fields, EN/VI labels and48dp minimum action heights. Busy source writes
  retain disabled source/tab/confirmation and favorite/open guards. Actual clipping,
  font shaping, spoken TalkBack and orientation remain runtime requirements.
- Cookbook/Spice entry fields retain password transformations; no new saved Dish,
  token persistence or production logging found. Theming adds no new permissions.

## Independently inspected fresh local evidence

Persistent audit:
`/home/vantt/.local/share/passchef-mobile-evidence/2026-11-04-plan-completion/reviewer-source-artifact-audit01.json`.

| Evidence | Observed result |
|---|---|
| Root terminal JSON/log | Exit0;335.520s;182 tasks executed |
| Actual shared XML |78 tests;0 failures/errors/skips |
| Actual Android bridge XML |22 tests;0 failures/errors/skips |
| Actual app JVM XML |10 tests;0 failures/errors/skips |
| Lint XML |0 Error/Fatal;10 Warning;1 Hint |
| Frozen mobile files |170 paths;0 changed/missing |
| Baseline protected tests/fixtures |54 paths;0 changed/missing, including Golden kitchen tests |

Current SHA256 identities:

- Debug `9fd023f4e98455a326faed00261bb503a280986c0307ecef9fff1e70b98631b8`.
- Test `7ee1e7c10cc2e110ca50e1c3b73d0aa02dc0065713a6991e75d5dcf8538088ed`.
- Unsigned release `039e242045131067c9a401dc4342b69d48bafa0f309c00d2d6075a712f4c641d`.

Read-only compiled-release manifest inspection confirms production package
`dev.passchef.mobile`, backup/full-backup disabled and existing extraction rules;
no proof Activity, camera, Autofill or Drive permission introduced by current UI work.
Unsigned release assembly is local verification, not release acceptance.

## Subsequent independent native/host review

Standalone JVM21 now completed exit0,100.399s; fresh XML confirms35 tests with
0 failures/errors/skips. Client common cases overlap bridge coverage.

Actual locked diagnostic raw log independently confirms selected native start/end
`[1,0]`, single terminal`[-1]`, anchored`OK (1 test)` and Time29.416s. Five diagnostic
bundles also legitimately use status0 and carry no JUnit class/test identity;
the initial host classifier incorrectly counted those as additional test endings.
Original raw log and initial Failed host JSON are retained; no Android assertion
was changed or rerun to obtain this conclusion. Independent raw-bound audit:
`reviewer-locked-runtime01.json`, SHA256
`0b34e9e4a859da43f6a36550f65d280fcd4fa3a72b6a0a13e649b2355045fde7`.

The corrected host classifier binds class, method, runner ID, current and test count,
requires exactly one selected start/end and terminal/summary, rejects unbound JUnit,
negative diagnostic/failure/crash outcomes, nonzero native exit and timeout.
Independent adversarial replay18/18 Passed in `reviewer-host-classifier01.json`.
The native selected run passes bounded **locked isolated-emulator diagnostics**:
initial/resumed active, stopped inactive and no ready session/Dish in all three.
It does not exercise password setup, Google, biometric or physical acceptance.

Root's password-only UI continuation remains separate work at this snapshot.
Historical physical verdicts retain their original candidate/scope; changed UI
requires its affected runtime observations.

## Demonstrated Android16 instrumentation dependency repair

Initial source-review snapshot: **Passed; replacement runtime accepted below**.
The unchanged full UI case initially Failed21.267s before its first UI input:
Espresso initialization called removed `InputManager.getInstance`, producing
`NoSuchMethodException`. Selected native outcome was`[1,-2]/[-1]/FAILURES`; retain
`emulator-cookbook-ui01.log` and its original Failed result. The separate locked
diagnostic pass does not cover Espresso-driven Compose interactions.

The before dependency report resolves Espresso3.5.0. Current
`app/build.gradle.kts:47` explicitly pins Espresso3.7.0 and aligns AndroidX
runner/rules1.7.0 plus ext.junit1.3.0, all under `androidTestImplementation`.
This matches the demonstrated failure and the documented API replacement in
[official Espresso3.7 release notes](https://developer.android.com/jetpack/androidx/releases/test#espresso_3_7_0).
The independently downloaded
[official POM](https://dl.google.com/dl/android/maven2/androidx/test/espresso/espresso-core/3.7.0/espresso-core-3.7.0.pom)
confirms core/runner1.7.0 and monitor1.8.0. No production dependency, Android test
assertion or session/kitchen policy changed. No source-review blocker found.

The original170-file freeze omits `app/build.gradle.kts`; those170 files remain
exact. Explicit additional build-file delta SHA256:
`a46d08686759c6ea277e268567a2cc4871c68578557dedb92d405304c4768c4a`
→`c0673df5c9953be6ada42c2f4696dc61d073cc11cd071f5c97090c68f99852a5`.
Independent evidence: `reviewer-espresso-dependency-review01.json` and retained
POM SHA256`b6e7f43cb396b89a4ce329a7e5acef79d375203a87a11a7639d18c0e6de50b56`.
Candidate01 identities/old source audit stay historical. Candidate02 requires its
new resolved-dependency/build/lint/APK freeze and unchanged selected native test
before any repaired UI-runtime acceptance. Local source review grants none.

## Candidate02 and unchanged offline UI —Passed111.247s

Independent read-only audit of raw native result, selected test source, frozen APK
bytes, install identities and fresh lint: **Passed for selected emulator scope**.
Artifact: `reviewer-offline-ui-runtime02.json`; raw SHA256
`ecd99889a69e6d6de5c9526e177f72a11626a4e42095bbe1ef6058f05766c263`.
Native selected JUnit`[1,0]`, four separate diagnostic0 bundles, terminal`[-1]`,
anchored`OK (1 test)`, Time111.247s; host116.292s, native0, no timeout.
The exact original test/assertions remain among unchanged61 Android test files
and unchanged170-file source freeze. Initial UI01 remains Failed.

Build02 exit0,194.447s,164 tasks:24 executed/140 up-to-date; lint0 Error/Fatal,
7 Warning/1 Hint. App JVM task is up-to-date: retain prior actual10-test pass,
not a second fresh execution. Shared78/bridge22/standalone35 passes also retained.
Resolved dependency report explicitly selects Espresso3.7.0 over transitive3.5.0.
The changed test APK SHA256 is
`91b81eea47fd3c59c6798c3088a02847a86a0bdb1c2632c363edc1125952ba50`;
debug and unsigned release remain byte-identical to candidate01. Candidate02
archive hashes and recorded installed `base.apk` identities match both app/test.
No APK/install claim is based only on source pins.

Actual unchanged UI assertions now exercise password setup on the clean emulator,
manual Cook with literal synthetic Dish, default mask and real10.5s reveal wait,
Dish Copy, edit/invalid-Recipe/missing-Menu refusal, offline Menu preview/confirmation,
correct-context tagged Cook, search/open by username, literal`00123` username Copy
surviving an old Dish timer for30.5s, detail Back cleanup and encrypted jars unchanged.
Controlled Activity CREATED→RESUMED returns locked with no Dish/list. Raw reveal
checkpoints agree: ready/revealed/nonempty before929091ms; still ready but hidden/
empty after939921ms. Actual stop checkpoint records inactive/unready with no Dish.

This accepts the demonstrated Espresso compatibility repair and selected offline
UI on isolated API36.1; it does not certify physical biometrics, real Google,
TalkBack, favorites/recent, natural OS background/LMK, exact30s Dish removal,
clipboard history or the full layout/font/orientation matrix. The separate VI
association/large-font observations are reviewed below.

## Selected VI association and large-font secure pixels —Passed

Independent raw/classifier/transport/observer review:
`reviewer-emulator-ui03-04.json`; unchanged candidate02/test assertions.

- VI association03 Passed20.781s native/28.586s host; exact selected`[1,0]/[-1]/OK1`,
  native0/no timeout. Attempts1/2 found no matching OS field; attempt3 identifies
  exactly one visible/editable/password node by Compose field ID, with the VI label
  on its descendants. Same-node label remains false; spoken TalkBack unobserved.
  Raw SHA256`0f1cd6aaa9c4f162b50d5cb944f0ec2e3b44f30fba5c95442876080ac2de046e`.
  Large-font/rotation settings were requested, but the persisted late snapshot03
  shows launcher portrait; it does not establish that case's own-Activity orientation.
- Secure pixels04 Passed12.496s native/17.036s host; selected`[1,0]/[-1]/OK1`,
  native0/no timeout. The separate concurrent observer04 binds actual own proof
  Activity to font scale2.0,landscape914×411dp,rotation90 and2400×1080 display.
  Both EN/VI captures are actual nonnull2400×1080 bitmaps; each excludes all
  2,222,920 sampled own-content pixels as opaque black. Language-control diagnostics
  report48dp touch bounds and full visible/touch geometry; displayed/enabled password
  field semantics are asserted. This selected case does not assert every field or
  whole-screen target/layout/font quality. Raw SHA256
  `bbc1d072bba648eff249b2822ed4bf38d5f029ba20d1e684e26ae2bf34092a02`.

The secure host runner differs from the reviewed selected runner only by fixed
`-e passchefSecureCaptureTransport shell`. Existing capture helper uses fixed
`screencap -p`, decodes in RAM and recycles the bitmap; no screenshot file, secure
layer enablement, permission grant, production/window-policy or assertion change.
Source review does not turn field diagnostics into TalkBack acceptance. These
passes remain isolated-emulator observations; required physical matrix stays open.

Unresolved: real phone/owner
availability; TalkBack/larger-font/orientation/language-target coverage, natural
LMK/saved-task and broader authenticated clipboard lifecycle; genuine Google human
cancellation/resultCode, different-Menu stale callback and401 preservation.
