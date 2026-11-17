# API26 clipboard fallback —4November2026

Status: **All four selected API26 native cases and independent runtime review Passed**.
This verifies the minSdk26 adapter branch on a disposable emulator, separate from
physical-device/Keystore authentication, Google and background/history guarantees.

Evidence: `/home/vantt/.local/share/passchef-mobile-evidence/2026-11-04-api26-clipboard/`.
Fresh `PassChefLegacy26`, serial5582, actual API26/x86_64/boot1. Google APIs image
revision16 installed with existing SDK licenses; its required Linux emulator package
was also installed. Actual runtime uses Windows emulator36.3.10 and a unique
task-created AVD/image directory. Original user AVD untouched; host clipboard sharing
disabled. Windows emulator launcher detaches: live emulatorPID34260/headless QEMUPID
31220 were separately observed; launcher exit0 alone is not native termination.

## Candidate and preparation

Build15 Failed17s: new fixture passed ColdStartProofActivity to a diagnostic helper
typed for CookbookProofActivity. Corrected code uses typed ColdCacheObservation off
main plus direct secure/focused/window guards during real clipboard actions. Existing
tests remain unchanged; no assertion was relaxed to repair compilation.

Build16 Passed16s,159tasks/11executed/148up-to-date. Freeze186 source paths and ZIP;
debug`71ccff6ad9ac4c5f896e9541f1534ed6b12258720b6cb91a5ea7280bc4fd38c2`,
test`a716cd39438417a568c93f152a79b53a21fc2e563373df9b7859533a060f1221`,
unsigned release`039e242045131067c9a401dc4342b69d48bafa0f309c00d2d6075a712f4c641d`.
Actual guest installed debug/test bytes match. Ordinary locked MainActivity bootstrap
creates an empty private namespace; pre-test force-stop is preparation only.
[Source/candidate/host review](api26-clipboard-review-20261104.md) Passed.

## Selected native results

Each method runs alone with strict selected JUnit identity, one normal terminal and
anchored `OK (1 test)`. Completed diagnostic phases bind one UUID/PID/API26; native
inactivity and empty private namespace are required after every method.

| Case | Native | Meaning |
|---|---|---|
| Untouched Dish | Passed36.503s | Before original deadline the owned text/claim remain; after actual30s production Handler leaves one empty-label/empty-text clip and forgets ownership |
| Foreign and null-label replacements | Passed68.043s | Two real expiry intervals retain replacement text; null-label content is read only behind this UUID's metadata marker; original owned claim is forgotten |
| Newer Copy | Passed42.502s | Newer generation and later deadline survive original expiry, then clear at its own deadline |
| Username Copy | Passed37.054s | Username survives the retired Dish cleanup |

API26 fallback intentionally leaves a nonnull empty ClipData; `hasPrimaryClip=false`
is not the acceptance condition. Sensitive-extra retention is metadata evidence,
not an old-OS privacy-overlay guarantee. No authentication, jars, session or cooked
Dish is created; synthetic adapter strings are copied through the production adapter.
Only policy metadata is read; no clock/policy writes or manual reconcile occurs.

Independent review verifies all four selected results,186 archived source paths,
installed APKs,61 original Android tests and54 protected/Golden files. The owned
emulator was retained for the separate bounded UI batch, then closed after it ended.
`resource-cleanup20.json` verifies native inactive/private empty before shutdown,
all exact-AVD launcher/QEMU/headless processes and serial absent afterward, and
only its task-created Windows temporary AVD/image directory removed. Definitions,
APKs, raw results and original user AVD remain.

Separate [API27 runtime](api27-clipboard-runtime-20261104.md) is now also accepted.
Physical devices, authenticated recreation, natural LMK,
clipboard history and exact background timing remain outside these selected cases.
