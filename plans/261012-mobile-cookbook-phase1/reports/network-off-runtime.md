# Actual network-off Google provider cache

Date2026-10-13 (Asia/Saigon). **Network-off Run04 Passed1/1,31.477s**;
mobile remains **Partial**.
BaselineHEAD`d76e940`: prior [cache UI Run02](google-cache-ui-runtime.md) accepted
app-disconnected cached Cook and authenticated same-process recreation. This
continuation separately checks actual network absence and observed failure behavior.
Root alone runs Windows ADB/Gradle; original CookbookUIRun01 cause remains unknown.

## Prepared source and build

Build Passed55s,22 tasks executed/142 UP-TO-DATE; lint0 errors/11 warnings/1 hint.
App unit task UP-TO-DATE: retains earlier4, not fresh execution. Shared78/bridge16/
standalone29 likewise retained; no distinct aggregate case count or device credit.

| Artifact/source | SHA256 |
|---|---|
| Debug APK | `fa4c4ab40339230a416c629d437ecd0200da099032fabad4de3c27d4eab2f9c7` |
| Test APK | `310a310bee3998d0f2284fbde3c97f35332dd8bd3a24acd80e328e03a634f793` |
| Google fixture177lines | `d409a7b7f0f30c5dc0a79e48fbc7aa66dbed6236cd8ab694148000461d890926` |
| Network helper148lines | `4829b39886f24e016208a9bd73631bf0706bc916c68b7c6afa0d82d8766c55de` |
| Unchanged cache helper150lines | `6ca811464837fe61d61991ba4336c3cc1decadd53aef0942f537558205121ec5` |
| Debug manifest | `6d6d3e5264ef8534d7c8d78afa2e2451267519b35e195df63c2c7f8d0ab601c7` |

Changes limited to optional provider-fixture branch, new bounded helper and
debug-only ACCESS_NETWORK_STATE. Original17 provider assertions and entire150-line
cache helper byte-identical; production manifest/main/shared/canonical unchanged.
Network observation uses actual default/bound/internet-capable network absence,
plus default-network callback latch; reconnection is never reset to pass.

## Installation and preflight

Run01 exact APKs installed and actual Awake/keyguard=false preflight passed.
Root preserves owner
Cookbook digest:`894f73e51e316b6a369368ad4cebbc901609ccfe7eabe855aa73c2cbf4e7a4d6`.
No uninstall/data clear or owner-jar operation. Candidate requires authorized USB,
actual Awake/keyguard=false and real foreground on Xiaomi14TPro/API36.

## Network-off runs

| Run | Actual result | Transport failure accepted? |
|---|---|---|
| Network-off Run01 | Failed1/1,200.773s;0 errors/skips | Not reached; actual network-off gate unaccepted |
| Network-off Run02 | Failed1/1,54.677s;0 errors/skips | Actual NETWORK_FAILED observed; preservation/full offline matrix unaccepted |
| Network-off Run03 | Failed1/1,182.07s;0 errors/skips | Setup failed; network continuation not reached |
| Network-off Run04 | Passed1/1,31.477s;0 failures/errors/skips | Actual transport failure preservation and complete strict offline cache UI accepted |

Run01 runner[1,-2], terminalFAILURES despite native exit0. Failed at
`network-owner-disable-radios`; no `network-off-observed` phase. Actual provider
assertions/cached Cook had been reached, but no network-off/transport acceptance.
Initial safe marker showed default/internet network still present; Run01 did not
capture final gate network/foreground bits. Owner later reported both radios off
and returning to the app; actual switch time/final failed condition remain unknown.
Do not infer a late owner action or a production cause from reply/log timing.
Owner digest after actual run remains unchanged. Run01 helper148/test APK/frozen
manifest archived before a narrow helper-only failure diagnostic adjustment.

Retry preparation adds only safe failure-phase network/foreground bits around the
unchanged first gate; every previous assertion/provider17/cache150 retained. Root's
next authorized USB runner switches Wi-Fi/mobile data at the exact safe marker,
records command results and issues baseline online restoration commands in
finally. Actual restoration observations are distinguished below. Fixture ConnectivityManager gates remain the authority; settings integers/
successful commands alone are not offline proof.

## Diagnostic candidate and Run02

Incremental test-APK/lint build Passed81s,9 tasks executed/85 UP-TO-DATE. Source153
SHA256:`05b97019236e7bc1fc200e14b73935cae07d2130986f01b0ba76abad69f68da6`;
test APK:`db8d1007039e48e263224cc8cefdca85c7bb816b388be803f8eedd7cd2f9da01`.
Debug APK unchangedfa4c4ab4…; provider/cache source assertions unchanged. Exact test
APK installed, owner digest preserved before/after install and both actual runs.

Run02 runner[1,-2], terminalFAILURES despite native0. Initial `network-off-observed`
had default/bound/internet networks absent. At retained-provider-read phase default/
bound remained absent, but internetNetworksAbsent=false; default callback had not
observed reconnection. Actual retained-grant tab UI read returned NETWORK_FAILED,
then strict offline guard failed before cache/jars/stale-Dish preservation assertions
and complete cache helper could run. Record observed transport separately; do not
credit error-preservation/offline matrix. No network identity/SIM/cause inferred.

Run03/04 retain byte-identical153-line source/debug/test APKs; no rebuild/reinstall.
Only root's external authorized USB runner adds airplane mode at the exact disable
marker alongside Wi-Fi/mobile-data off; initial online setup and final baseline
restore commands recorded separately from observed state. All guards/assertions unchanged.

Run03 failed at setup, runner[1,-2], before the network marker. Sanitized own-process
PID17693 includes two epoch-stamped correlated authorized key-wrap events recording code15/securityLevel2 with
system/transient bits true and authenticationRequired=false inside this run. Cause
unresolved; do not infer owner biometric error or a production repair. Actual log:
`google-network-off-run-03-wrap-diagnostics.log`. Both failed identities remain intact.

## Accepted Run04 and restoration

Actual Run04 runner[1,0], terminalOK1,0 failures/errors/skips; final
`network-off-complete` reports true. Independent reviewer confirmed source39 frozen
paths and protected208/unrelated23 unchanged. Safe status2 packets26 separate from
two JUnit statuses; no native-exit/diagnostic false pass.

All14 post-off network checkpoints observed default/bound/all INTERNET-capable
networks absent and reconnection=false. Retained real provider tab read returned
NETWORK_FAILED with transportNetworkFailureObserved=true and authorizationUnavailable=
false. Exact last-good snapshot/disk/encrypted jars and stale Dish/Copy UI absence
passed, followed by every unchanged150-line helper assertion: provider origin/Recipe/
literal00123/exact masked/revealed Dish/favorites/recent/search, two genuine biometric
reopens and authenticated same-process recreation while all network guards remained
strict. No cached-grant injection, mock/paste, clock bypass or production change.

Root's authorized USB controls enabled airplane mode and disabled Wi-Fi/data at
the safe marker, then accepted restore commands in finally. Separate restoration
observation confirms Wi-Fi enabled, airplane mode off and system default network
available; mobile-data enable command accepted only, with no independent data-state
measurement. Owner Cookbook digest unchanged after all installs/runs. Historical
Run01 gate cause, Run03 key-wrap cause and original CookbookUIRun01 cause stay open.

First repeat genuine AuthorizationClient/provider metadata/tab/known row/mapping/
cache assertions and actual cached-row Cook. Root notifies owner at safe
`network-owner-disable-radios` phase. Run01 used owner manual changes; Run02–04 used
authorized root USB controls. No process-network binding or fixture-side radio toggles.

If real provider metadata/grant survives the owner's settings interaction, actual
Sources/tab UI reads through Sheets and must report NETWORK_FAILED. If settings
background-locks the Cookbook, genuine owner biometric reloads then ordinary Connect
attempts offline; CONNECT_REQUIRED or discarded background authorization is separately
recorded and does not receive Sheets transport-failure credit. Both paths require
exact last-good cache/disk/encrypted jars preserved and stale Dish/Copy UI absent.

The intact cache helper then exercises favorite/recent/search/exact origin/Recipe/
literal username00123/masked and revealed synthetic Dish, lock/biometric reopening
and authenticated same-process recreation while network guards/monitor remain armed.
Safe phases/booleans only; report observed transport separately from overall cached
Cook acceptance. Final expected phase `network-off-complete`; compilation/native
exit0/status2 diagnostics alone cannot establish a JUnit pass.

## Evidence and remaining scope

Evidence outside repo:`/tmp/passchef-cookbook-device-acceptance/`:
`network-off-baseline-source.json`, `google-network-off-build.log`/`.json`,
`google-network-off-frozen-01.json`, versioned install/preflight/run logs/JSON.
Diagnostic build log and frozen02/03/04/source/test identities preserved; Run04
radio-control and restoration-observation JSON records actual external controls.
All preceding provider/cache/clipboard runtime artifacts remain separate and intact.

Accepted selected actual network-off/cache Cook and NETWORK_FAILED cache preservation.
Pending 401/403/cancellation/stale
callback acceptance, process death/cold start, real5/10-minute timers, capture/Recents/
ENVI/accessibility and broader phone/platform matrix. No injected token/session,
HTTP mock, offline paste, virtual clock or whole-mobile completion claimed.
