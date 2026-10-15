# Hardware screenshot shortcut —15October2026

Status: **Partial — Run03 Failed setup; Run04/05 Failed owner-response completion wait**.
Hardware shortcut remains Pending without a confirmed owner observation. Neither
JUnit failure proves a screenshot-security failure. Whole mobile remains Partial.

## Prepared isolated checkpoint

New149-line `manual-shortcut-physical-test.kt` reuses genuine `CookbookTimerFixture`
setup/biometric/Cook in UUID-owned storage. All63 prior app sources/manifests and ten
native helpers remain unchanged. Before the owner action require secure own awake/
unlocked/focused ready masked Dish, enabled Copy and visible Reveal. Owner controls
Reveal immediately before Power+VolumeDown; normal10s remask/session deadlines stay.
No screenshot bitmap/gallery access, Recents replay or owner Cookbook modification.

Passive200ms samples record only own state/lifecycle booleans and relative times.
Exact synthetic reveal is checked without logging content. No continuous visibility
or exact key-press timestamp inferred from sampling. OS overlays/Power-stop may lose
focus; actual own stops must clear session/Dish/lifetime, with irreversible any-stop
failure latch. UI Dish/Copy absence is asserted only if the own Activity is resumed/
focused. Encrypted fixture jars must remain equal; cleanup covers only its keys/dir.

Owner answer is separate evidence for method, OS result and Dish visibility. Root
publishes completion only after an actual answer, into the exact canonical UUID dir.
The private marker contains only completed/fixtureId, bounded256bytes, no verdict.
Host publisher requires actual owner-reply record, exact active selected runner start,
no failure/abort/crash/terminal, frozen source/APK/helper/installed identities, and
owner digest. Noclobber temporary inode→exclusive atomic hardlink avoids partial or
replaced completion. No mkdir or session/token/biometric injection. Maximum owner
wait180s is a harness bound, with normal production deadlines untouched.

JUnit Passed would certify the checkpoint and preservation only. It cannot certify
OS screenshot refusal without the separately recorded actual owner observation.

## Build and review history

Build01 Passed17s10 executed/101 UP-TO-DATE, debug/test/lint0 errors/11 warnings/1 hint.
Frozen01/candidate01/source64/helper11/test29f4 archived before review fixed a stop
history weakness: a later clean stop could hide an earlier failed read/clear. New
irreversible latch rejects any earlier stop failure. Original63/assertions unchanged.
Candidate01 never installed/run; no production repair or prior-failure resolution.

Corrected build02 Passed14s9 executed/102 UP-TO-DATE,lint0/11/1. Source64/helper12/
test19b9 frozen02 archived before publisher review added exact selected-packet and
abort/crash/failure rejection. Publisher-only correction needs no APK rebuild;
frozen03/candidate03 retain the exact same APKs and all64 app source identities.
Independent review found no remaining code/runner/publisher blockers before runtime.

| Artifact | SHA256 |
|---|---|
| Debug retained | `1f42a2c4b369492b0ed804fab3ec282882e831336cf4ebfec48dbdba05bfd772` |
| Early candidate01 test; never installed/run | `29f4fd281dfb586e6eebeab11718118266442900b9e8c37df8080567bee3b5b2` |
| Corrected installed test | `19b98e197b122c3ed9e05a707e9e4f050da0ee8cc00e19c5232344dbda15dde8` |

## Actual Run03 — setup Failed; shortcut NotExercised

Fresh serialPN7PW8SGXKLVPRIV/transport26, Awake/keyguardAbsent and owner894f73…a4d6
confirmed before install-r. Exact debug retained/test19b9 installed; digest unchanged.
Selected `ManualShortcutPhysicalTest#genuineOwnerHardwareShortcutCheckpoint` began
10:35:28.640Asia/Saigon/03:35:28.640UTC under frozen03. Real setup announced biometric,
but never reached owner-shortcut-ready. Actual terminal **Failed1/1,182.485s**,
JUnit[1,-2]/terminal[-1]/FAILURES, native0/no watchdog/orchestration error.

Before terminal, root verified own PID15980/cmdline and read only safe PassChefProof
diagnostics: at10:36:28.054, wrap-doFinal/level2,
IllegalBlockSizeException→KeyStoreException, code15/systemtrue/transienttrue/
authRequiredfalse/retry2. This is current setup error evidence; underlying cause and
exact owner authentication action are unconfirmed. No historic cause/owner-blame claim.
No Reveal/checkpoint/shortcut/owner-completion signal occurred. Actual hardware shortcut
is **NotExercised**, distinct from JUnit setup Failed.

Failure packet identifies UUID23a6c343-9ad2-4f20-9121-2c4a0f43561e. Root final native
audit confirms that exact owned directory absent, owner894f73…a4d6/source64/helper12/
protected208/unrelated23 preserved and installedtest19b9. The original failure is retained.

## Actual Run04 — ready reached; completion wait Failed

Fresh UUID19ac6e6b-662a-4915-85f7-2f7c10ee6fb0 uses identical sources/helpers/APKs in
separate frozen04/candidate04 after fresh device/readiness/digest gates. No rebuild/
reinstall/policy change/time shortening or claimed production fix. Genuine biometric
round trip/Cook and secure resumed/focused/awake/keyguardAbsent masked-ready checkpoint
were accepted by actual assertions. The owner was then asked to tap Reveal followed
immediately by Power+VolumeDown, reporting OS result and actual Dish visibility.

Actual terminal **Failed1/1,236.235s**, JUnit[1,-2]/terminal[-1]/FAILURES, native0/
no watchdog/orchestration error. No owner answer/completion marker arrived within
the checkpoint's180s window. Failure packet records elapsed180429ms since ready,
first/lastReveal=-1, firstFocusLoss=-1, stops0/anyStopFailure=false. Passive sampling
did not observe exact revealed Dish; this does not prove no physical attempt occurred.
No completion signal was fabricated. Hardware shortcut/reveal timing remain Pending
without confirmed owner observation, separate from the JUnit completion-wait failure.

Final native audit confirms exact UUID directory absent, owner894f73…a4d6 and installed
1f42/19b9/source64/helper12/protected208/unrelated23 preserved. Releaseefd3 unchanged;
no fresh JVM/browser tests or production changes. Prior wrap-error causes stay open;
this successful fresh setup does not resolve Run03's failure.

Outside repo `/tmp/passchef-cookbook-device-acceptance/`: manual-shortcut build01/02,
runner/signal helper/frozen01..05/candidate01..05/install03, run03 log/result/preflight/
immediate, preterminal safe probe and exact-UUID final-audit03. Run04 log/result/
preflight/immediate/checkpoint/root-question-procedure/final-audit04 remain separate;
there was no owner-observation or signal record during either run. No overwritten artifacts.
The completion publisher was reviewed but never invoked; native hardlink publication
has no runtime acceptance from these runs.

After Run04 terminal, owner replied “Đã xác thực xong” to the earlier authentication
question (exact phase unconfirmed), and “anh đâu thấy option này `Reveal for 10 seconds`?”
to the screenshot question. Saved as separate late observations, never a screenshot
verdict/completion signal. Whether the owner looked during the ready interval or
after cleanup is unconfirmed; no cause or owner-blame inferred. Source label is
confirmed as EN Reveal for10seconds / VI Hiện trong10giây beneath the masked Dish.
Root announced fresh Run05 using identical APK/source/helper identities and clarified
the actual button location/labels; no rebuild, policy change or historical repair.

## Actual Run05 — ready reached; completion wait Failed

Same source64/helper12/APKs/frozen05/candidate05, no rebuild/reinstall. Fresh serial/
Awake/keyguardAbsent/owner digest gates passed. Genuine biometric round trip/Cook and
secure focused masked-ready checkpoint reached in UUID28dcad29-2d2e-45c4-97e0-2532e92cbdaa.
Root's new question explicitly named Run05, the masked dot line, EN/VI Reveal labels
and immediate Power+VolumeDown; missing-button screen/label description was offered.

Actual **Failed1/1,188.013s**, selected JUnit[1,-2]/terminal[-1]/FAILURES, native0/
no watchdog/orchestration error. Owner-response completion absent at elapsed180421ms;
first/lastReveal=-1, focusloss=-1, stops0/anyStopFailurefalse. No signal fabricated or
confirmed owner screenshot observation. Final native audit confirms exact UUID-dir
absent, owner894f73…a4d6/source64/helper12/installed1f42+19b9/protected208/23/releaseefd3
preserved. Earlier failures remain; no production screenshot or owner-action cause inferred.
Root requested the owner's currently visible screen/labels before any further timed
run. No additional runtime remains active at this checkpoint.
Run05 log/result/preflight/immediate/checkpoint and exact-UUID final-audit05 are
separate artifacts; no owner05 observation or signal exists at this checkpoint.

Unresolved: actual owner hardware shortcut/result/reveal timing, current wrap error
cause, original UI/setup/wrap causes and broader mobile/Keystore matrix.
