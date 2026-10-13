# Google cached Menu UI and authenticated recreation

Date2026-10-13 (Asia/Saigon). **Cache UI Run02 Passed1/1,42.393s; Run01 Failed**.
Mobile remains **Partial**.
Continue GoogleRun03 provider acceptance; this report numbers cache UI runs
separately from GoogleRun01–03 and historical CookbookUIRun01–05.
Root exclusively coordinates Windows ADB/Gradle; independent source review passed.

## Run01 candidate and local verification

Build Passed88s:23 tasks executed/146 UP-TO-DATE; fresh app4,0 failures/errors/skips;
shared78 remains earlier retained evidence, not a new run. Lint0 errors/11 warnings/
1 hint. Release assemble UP-TO-DATE; production main/shared/canonical sources unchanged.

| Artifact/source | SHA256 |
|---|---|
| Debug APK | `23eebfaa052420f583ed69aa3e06e304ed180cbaaaf2d210c2f29e7f0902bbe7` |
| Test APK | `be123680d73b358a0755747f50becbb63110032c41ce464f6077567c8880aead` |
| Google fixture | `6fa298f46375758624e7a37c86d67f02ac155a70df23f0f4caaff6176ca5019d` |
| Cache UI helper | `a64673fc2bc00a51e25de6d77fb060b9c2f0d782649d4fbdf8bcd7c009097a25` |
| Proof Activity | `67613edca37fadbec8874ab05164cdf853f5b6ddfbc23ffe34c549af1257a3c5` |

Google fixture175/helper146/proof33lines. All17 original Google assertions retained
byte-identical/in order; optional `passchefGoogleCacheContinuation=true` invokes
additional UI assertions before the existing terminal report. Original source/hash
and GoogleRun03 artifacts remain in [their report](google-runtime.md), not replaced.

## Installation and owner data

Both exact APKs installed with `install -r`; actual installed hashes match above.
Owner Cookbook digest before installation, after debug update and after test update:
`894f73e51e316b6a369368ad4cebbc901609ccfe7eabe855aa73c2cbf4e7a4d6`.
Only digest read; no uninstall/data clear or owner-jar operation. API36 Xiaomi14TPro,
authorized USB serialPN7PW8SGXKLVPRIV; native Windows ADB through WSL.

## Default-storage regression

Selected unchanged `ClipboardRecreationPhysicalTest` **Passed1/1,31.769s**;
independent reviewer confirmed actual JUnit verdict. Default two-directory assertion
still passes after adding opt-in same-storage recreation; original29s retention/
30.5s clipboard cleanup assertions unchanged. Exact frozen APKs and owner digest
remain preserved. Existing8 platform tests are retained without rerun.

## Google cache UI runs

| Run | Actual verdict | Accepted observation |
|---|---|---|
| Cache UI Run01 | Failed1/1,294.651s;0 errors/skips | Recreated owner unlock ready, expected cache absent; complete continuation not accepted |
| Cache UI Run02 | Passed1/1,42.393s;0 failures/errors/skips | Exact provider cache UI/Cook, lock/reopen and authenticated same-process recreation |

Cache UI Run01 runner statuses[1,-2], terminalFAILURES; native exit0 is not Passed. Safe final flags
show actual own foreground/sessionReady=true,cachePresent=false,busy=false, with all
11 provider-match flags true. Earlier cached-row Cook/favorite/recent, explicit lock/
owner biometric reopening and old-destroyed/new-controller/locked-state assertions
were reached; final recreated cache reload/Cook was not accepted. Owner digest after
runtime remains the original894f73e5… value above. No production cause inferred.

Independent review identified a test synchronization risk: polling main-thread state
after biometric can wait for cache before ordinary Compose synchronization runs the
unlock-triggered LaunchedEffect. Narrow helper-only correction adds real authenticated
checkpoint, `ui.waitForIdle()` and safe cache-reload phase before the unchanged exact
cache predicate/assertion. All existing assertions stay unchanged; no session/grant
injection, production-clock reset or production repair. Corrected helper150lines;
Run01 helper/test APK/frozen manifest archived separately before editing.

## Corrected Run02 candidate and observed result

Helper-only synchronization build Passed40s:10 executed/84 UP-TO-DATE; lint0/11/1.
Initial88s build supplied fresh app4; shared78/bridge16/standalone29 remain retained
earlier evidence, not new executions or independent aggregate cases. Exact current
test APK:`be9daca7423650d6a2dc90290ff2e34a35a24a7be3016c63672c6535a394c689`;
helper150 SHA256:`6ca811464837fe61d61991ba4336c3cc1decadd53aef0942f537558205121ec5`.
Google fixture/proof Activity/debug APK hashes stay as in the Run01 table. Only
test APK updated for Run02; installed hashes match, owner digest preserved before/
after install and runtime. Root rechecked actual Awake/keyguard=false preflight.

Actual runner[1,0], terminalOK1,0 failures/errors/skips; safe status2 diagnostics
separate from JUnit statuses. After genuine recreated biometric, authenticated
checkpoint observed ready=true/cache=false/tokenAbsent=true. Normal Compose sync
then observed cache=true and all unchanged exact reload/disk/favorites/recent/UI
origin/Recipe/username00123/masked and revealed synthetic Dish assertions passed.
Final phase `cache-ui-complete`, provider11 match flags true; independent review
confirmed exact candidate/source/runner and scope. This supports the harness
synchronization diagnosis for this new failed run; production source unchanged.
Historical CookbookUIRun01 remains a distinct unknown/not-reproduced failure.

The fixture first repeats the real AuthorizationClient/provider metadata/selected
tab/known row/explicit mapping/cache-file assertions. Continuation then uses actual
Menu UI favorite/recent/search and cached-row Cook with exact origin/Recipe/username,
masked/exact synthetic Dish, explicit lock and genuine owner biometric re-unlock.
Authenticated Activity recreation starts a new controller in the same owned fixture
directory: original destroyed, new locked, no session/Dish/provider token. Normal
owner biometric reloads the durable provider cache and repeats cached Cook.

Storage retention restores only a canonical internal UUID before Activity setup;
default remains per-instance. No jars/session/grant/Dish restoration, external path
input, injected authentication, offline paste, HTTP mock or device network toggle.
KEEP_SCREEN_ON remains proof-only. Safe status2 diagnostics emit phases/booleans;
they are not JUnit pass statuses. Real bounded elapsed waits observe actual reload.

## Evidence and remaining scope

Evidence:`/tmp/passchef-cookbook-device-acceptance/`:
`google-cache-ui-build.log`/`.json`, `google-cache-ui-frozen.json`,
`google-cache-ui-run-01-install.json`, default-storage regression and actual cache UI
preflight/run logs/JSON; `google-cache-ui-sync-build.log`/`.json`,
`google-cache-ui-frozen-01.json`/`-02.json`, `google-cache-ui-run01-helper.kt` and
`google-cache-ui-run01-test.apk` retain the failed candidate. Run02 install/preflight/
log/JSON records corrected candidate separately. Native exit0 alone is insufficient.

Accepted: app-disconnected cache (no in-memory grant), exact cached Cook, favorite/
recent/search and genuine unlock after same-process authenticated Activity recreation.
Pending: radio-off/network-unavailable
behavior, provider cancellation/stale callback/401/403 errors, process death/cold
start, real5/10-minute timers, capture/Recents/ENVI/accessibility and broader phone
matrix. Historical CookbookUIRun01 cause remains unknown/not reproduced; new proof
does not establish a production repair or whole-mobile completion.
