# Android clipboard adapter runtime

Date2026-10-13 (Asia/Saigon). **Passed3/3,99.926s;0 failures/errors/skips**.
Mobile remains **Partial**. Actual Xiaomi14TPro/API36, authorized USB serial
PN7PW8SGXKLVPRIV (transport9); native Windows ADB via WSL only. Root alone ran ADB;
independent reviewer parsed all three start/pass statuses and checked fixture scope.

## Preflight and actual artifact

Re-listed device; verified frozen source/local test APK, then installed only test
APK with `install -r`. Actual installed test SHA256:
0c1c35af8fbf230f6c83babd7bc7ebff150ff5b6a51b3f0d253809ee9239c694.
Debug APK remainsd4eb8401d0b2cc71f9ca800614dc7f7d8cf391d3e3432cd6a7537ce91382f9a4.
Original owner Cookbook digest before/after install and after runtime remains:
894f73e51e316b6a369368ad4cebbc901609ccfe7eabe855aa73c2cbf4e7a4d6.
Only digest read; no uninstall/data clear or owner-jar operation.

Initial phone was Dozing/keyguard showing, so root waited without invoking tests.
Owner explicitly entered unlocked home screen; read-only gate observed Awake/
keyguard=false before launching only `OwnedClipboardPhysicalTest`.

```sh
"$PASSCHEF_ADB" -s "$PASSCHEF_SERIAL" shell am instrument -w -r \
  -e class dev.passchef.mobile.proof.OwnedClipboardPhysicalTest \
  dev.passchef.mobile.demo.test/androidx.test.runner.AndroidJUnitRunner
```

No PassChef biometric/Google request, session injection, manually forced foreground,
clipboard permission override, virtual clock or explicit `reconcile` call occurred.
Activity start naturally owns foreground; unique proof storage remains locked with
no retained Dish/ready session/encrypted record. The values copied are fixture-only
synthetic strings, not values derived from authenticated jars.

## Actual results and limits

| Test | Result | Real observation |
|---|---|---|
| Untouched clipboard | **Passed** | Unique marker/text present at29s, before earliest30s deadline; absent after30.5s beyond latest possible deadline |
| Replacement survival | **Passed** | Direct real ClipboardManager replacement survives old Dish30.5s deadline |
| Newer-copy ownership | **Passed** | Copy at+5s survives old30.5s deadline and clears after its own30.5s interval |

All observations assert real RESUMED, interactive/unlocked/focused/attached/shown
foreground. Write/read identity and sensitivity metadata are asserted. Finally
clears only exact tracked synthetic marker/text, preserving later unmatched entries,
then closes/deletes only the captured proof directory. No prior clipboard text logged.
Fixture writes replace global clipboard; proof storage does not isolate that platform
resource. Replacement was written outside the adapter through the same test UID;
this is not proof of cross-app UID behavior or OS clipboard-history deletion.

Three distinct runner start1/pass0 pairs, terminal `OK (3 tests)`,99.926s; no ignored/
assumption/failure codes. Native exit0 is not the sole pass criterion. Actual source
SHA256 matches [prepared fixture](unattended-clipboard-preparation.md):
7851ab1df75320c022559eef705cf295b5c2a25ee142b4bedfbe7e005fc49269.
API28+ annotation is deliberate; this API36 result does not certify API26/27 fallback.

Accepted: actual foreground adapter clearance/ownership for synthetic clipboard.
Not established: background/recreation/process death, different app UID/history,
Cook without authentication, Google grant/read or the historical Run01 cause.
Previous [Run04/05](ui-runtime-continuation.md) separately certify selected Cook/UI
flows with genuine fingerprint; do not merge their APK/runtime identities.

## Evidence and preservation

`/tmp/passchef-cookbook-device-acceptance/clipboard-adapter-install.json`,
`clipboard-adapter-preflight.json`, `clipboard-adapter-run-01.log`/`.json`.
Prepared-build/lint evidence remains in `unattended-clipboard-final-build.log` and
`unattended-clipboard-proof.json`; no build or earlier8 platform test repeated today.
Independent reviewer confirmed complete3/3 runner/source/foreground/timing scope.
Protected208 and unrelated23 byte snapshots still match; original assertions/Golden,
production main/shared/canonical/extension and `.agents/`/skills-lock preserved.

All work remains WIP/unstaged; no whole-mobile Done/archive. Unresolved: historical
Run01 cause not reproduced; Google configuration/grant/read; remaining background/
recreation/cold/session/UI and API26/27 physical matrix.
