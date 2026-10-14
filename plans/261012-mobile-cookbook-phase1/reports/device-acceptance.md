# Android Cookbook physical acceptance

Updated2026-10-14 (Asia/Saigon). Status: **Partial — selected UI/clipboard/Google
cache UI Run02 Passed; broader phone/provider matrix Pending**.
Latest [cache UI evidence](google-cache-ui-runtime.md) covers app-disconnected Cook
and authenticated same-process recreation; earlier preflight/run snapshots retained.
Owner connected/unlocked Xiaomi14TPro/2407FPN8EG API36 and authenticated fingerprint.
Root exclusively runs native Windows adb over authorized USB; no network ADB.
Current [actual session timer continuation](session-timer-runtime.md): idle Run01/02
Failed before baseline pointer; Run03/04 setup Failed. Run05 actual5-minute idle
Passed1/1,341.722s; total Run01/02/03 setup Failed182.309s/182.279s/182.138s retained.
14October Total04 Passed1/1,609.969s, runner[1,0]/OK1,0 failures/errors/skips;
original opened/generation unchanged through minute1–9 real touches/recooking,
595106ms ready/masked/Copy then601725ms locked/session/Dish/Copy cleared, jars and
owner digest preserved. Same frozen03/debugfa4/test8f08, no source/build/install change.
Selected API36 foreground proof KEEP_SCREEN_ON5/10-minute accepted; earlier failure
causes and broader timer/device UX remain open, no production repair claimed.

## Preflight

Candidate main13cf9d1 debug and test APK hashes match [independent verification](verification.md).
Both `install -r` operations succeeded; no uninstall/data clear. Original encrypted
Cookbook hash before installation, after installation and after test remains:
`894f73e51e316b6a369368ad4cebbc901609ccfe7eabe855aa73c2cbf4e7a4d6`.
Only the record's digest was read. Test uses unique private storage and its own aliases.

## Run01 — failed, not accepted

Actual `CookbookUiPhysicalTest` ran after genuine owner fingerprint. JUnit1 test,
1 failure,20.716s. Native process exit0 does not override the JUnit failure. Initial
masked Dish and revealed literal synthetic Dish assertions passed; after a real
10.5s wait, the masked assertion at line51 found no Dish node. No full-flow pass.
The phone was Awake/no lockscreen afterward, which does not establish its lifecycle
at the failing instant. Cause requires pre-failure sanitized state/lifecycle evidence.
No deadline/IME/product cause is assumed; assertions/Golden are preserved.

Evidence:`/tmp/passchef-cookbook-device-acceptance/preflight.json`,
`ui-run-01.log`, `ui-run-01.json`. No owner jars/keys/token contents recorded.

## Diagnostic candidate — installed, runtime Pending

Independent review accepts read-only boolean/lifecycle/timestamp diagnostics in the
unique proof test; no assertion/authentication/production clock changes. Only the
proof Activity keeps the display awake, leaving production screen policy unchanged.
Builder compile/lint passed; independent impacted build15s passed (3 executed,108
UP-TO-DATE), lint0 errors/11 warnings/1 hint. This is reused exact-source compilation
evidence, not a fresh JVM/UI pass. Main/shared/canonical production stays unchanged.

Debug SHA256:`0bca9201612f18f03ceb5f08ae2f769c13d2ffdc618189d6f49cbc5cc6dbf4a0`.
Test SHA256:`abb26371ed9904c20f80c055001bdd805cf52cf8e26de7c1fb5963688ca15702`.
Both installed successfully with data-preserving updates. Run02 waits for explicit
owner readiness/unlock; no second biometric deadline started while owner configures Google.
Evidence:`diagnostic-builder-proof.json`, `diagnostic-independent-proof.json`,
`diagnostic-installed.json` under the same temporary evidence directory.

## Unattended platform checks — passed

Owner2026-10-12: phone connected, owner going to sleep; do only work not requiring
owner biometric/Google interaction. Native instrumentation selected only:
`AtomicRecordStorePhysicalTest`, `ExpiredWrapFailureTest`, `WrapFailureProbeTest`.
Actual Xiaomi/API36 result: **OK (8 tests)**,0 failures,0.049s. This proves real
Android atomic create/replace/reopen and local classifier/log-sanitization assertions;
it does not prove biometric, Cookbook UI, reveal/clipboard or Google acceptance.
Original owner Cookbook SHA256 remains unchanged after these tests.
Evidence:`unattended-platform-run-01.log` and `.json` in the temporary evidence folder.
No runner/Gradle job remains active at handoff.

## Handoff state — owner asleep (prior snapshot)

Main HEAD/pushed baseline:`13cf9d149df6b388f02b1a0db0da6f02a6de8798`.
Current uncommitted work is the3 reviewed diagnostic test/debug files and acceptance
documentation/report; no production main/shared/canonical changes. Preserve unrelated
`.agents/` and `skills-lock.json`; never stage/remove them. Diagnostics are installed
and compilation/review passed, but authenticated Run02 has not happened.

Use native Windows adb `/mnt/c/Users/van.tran_fgorg/AppData/Local/Android/Sdk/platform-tools/adb.exe`;
re-list devices/select actual physical serial (last `PN7PW8SGXKLVPRIV`). Linux adb
sees no device; do not change drivers/USB/settings or expose network adb. Run no
broad authentication test suite while owner sleeps and do not bypass real auth.
The8 selected platform assertions completed; avoid pointless repeats. Work on
non-interactive analysis/local regressions; wait for owner readiness before Run02.
Root-owned evidence directory:`/tmp/passchef-cookbook-device-acceptance/`.

Owner Google client configuration is **not completed/confirmed**. Android package
`dev.passchef.mobile.demo`, public signer SHA1
`3C:FE:96:5E:66:AC:6F:89:4D:C1:A5:07:AA:C9:C3:8D:CF:F5:D2:C9`.
Google setup guide was supplied; no real consent/token/read exists. Synthetic test
Menu URL: `https://docs.google.com/spreadsheets/d/129-4u0fRtayCVQWUYLEhhy24e4fb7I68wI1sNgV8DoY/edit`.
Public sharing is not an Android permission grant. Sheets read/write scope settled,
current slice reads; account-write UI, QR receiver, Autofill and Drive are later work.

## Remaining acceptance

Owner returned to sleep; [unattended clipboard preparation](unattended-clipboard-preparation.md)
adds3 reviewed API28+ real adapter tests with no authentication/session injection.
Oct12 final compile/lint Passed; runtime was0/Pending while locked/USB unavailable.
[Oct13 clipboard runtime](clipboard-runtime.md) subsequently installed0c1c35af...,
Passed3/3,99.926s on API36 and preserved original Cookbook digest after runtime.
Foreground untouched clearance/replacement/newer-copy ownership is now accepted;
background/recreation/cross-app/history/API26 fallback remain outside that proof.

Owner subsequently returned and confirmed readiness. [Runtime continuation](ui-runtime-continuation.md)
records latest APK install/digest Passed; locked diagnostic1/1 Passed; Run02 Failed
after owner-confirmed foreground interruption; Run03 Failed before setup with a
sleeping/locked phone; Run04 later Passed1/1 after verified unlock. Run04 tested pair
was installed; newer comparison test086e7022... subsequently installed after reconnect.
Locked diagnostic02 Passed1/1 and normal-screen-policy Run05 Passed1/1,53.663s.
Original Run01 cause remains unknown; owner confirms no navigation then.
Selected UI pass has explicit limits; no Google pass or production fix exists.

### Earlier unattended snapshot

[Regression and diagnostic preparation](unattended-regression.md).
Fresh shared78/app4 passed; packaging/lint passed. Additional read-success/session/
presentation diagnostics and a locked-only lifecycle test compile and pass independent
source review. New test runtime remains Pending: read-only preflight found Dozing and
keyguard showing; no wake/unlock/test/install was performed. Installed APKs remain the
earlier pair above; newer local hashes and required data-preserving update are explicit
in the continuation report. No production repair or Run02 result is claimed.

### Oct13 clipboard recreation acceptance

[Same-process locked adapter recreation](clipboard-recreation-runtime.md) Passed1/1,
31.721s after exact final test-APK update and actual Awake/keyguard=false preflight.
Original deadline retained through real Activity destruction/replacement;29s retention
and30.5s cleanup asserted. Original Cookbook digest remains unchanged after runtime.
No authentication, production change or historical Run01 repair inferred.

[Owner matrix](../../../docs/runbooks/android-cookbook-acceptance.md) remains open.
Wrong Menu/tag, cache reopening/favorites/recent, actual background/authenticated
recreation/cold start,5/10-minute session limits, EN/VI/capture and real Google Android
setup/grant/read need their own evidence. Foreground clipboard and same-process
locked adapter recreation now have separate Passed evidence above.

### Selected Google runtime, separate from Cookbook UI runs

[Google runtime](google-runtime.md) records real account/Sheets consent and usable
authorization/provider metadata and values. Google Run01 Failed1/1,44.766s at metadata;
owner confirmed missing expected Test tab and renamed it. Unchanged Google Run02
Failed1/1,19.259s at values; schema/origin checks not yet accepted, mapping/cache Pending.
Owner reports only A1/A2 populated, so six-cell fixture completion is awaited.
Owner digest unchanged after installation and both runs. Safe diagnostics reviewed/
compiled with all17 assertions intact; new local candidate not installed or executed.

Owner then updated the six-cell fixture. Refined testcc4 installed with digest
preserved; Google Run03 Passed1/1,23.309s with all11 schema/identity flags true.
Actual Google metadata/Test tab/rows/mapping/cache-file load and unchanged fake jars
accepted. Original17 assertions unchanged; reviewer confirms complete runner/hash/
preservation evidence. UI cache reopening/disconnect/errors/writes remain Pending.

Latest separate [Google cache UI continuation](google-cache-ui-runtime.md): default
proof-storage recreation Passed1/1,31.769s. Cache UI Run01 Failed1/1,294.651s at
recreated cache reload; helper-only normal Compose synchronization added without
altering any assertion. Cache UI Run02 Passed1/1,42.393s with real provider/cache UI
origin/Recipe/username00123/Cook/favorites/recent/search, lock/reopen and authenticated
same-process recreation. Exact source/APK histories retained; owner digest unchanged.

Latest separate [network-off runtime](network-off-runtime.md): Run04 Passed1/1,
31.477s,0 failures/errors/skips after preserved Failed Run01/02/03. Strict actual
network absence, real provider NETWORK_FAILED cache/jars preservation, stale Dish/
Copy absence and unchanged cache UI/Cook/favorites/recent/biometric same-process
recreation passed. Sources/APKs unchanged across Run02/03/04; owner digest preserved.
USB radio restore separately observed Wi-Fi enabled/airplane off/default available;
mobile-data enable command accepted only. No production change or broader completion.

Unresolved: original Cookbook UI Run01 masked-after-reveal cause not reproduced in
Run04/05, network Run03 key-wrap cause; cancellation/stale callbacks/401/403/cold-start/
process death and remaining phone matrix. Whole mobile Phase1 stays Partial.
