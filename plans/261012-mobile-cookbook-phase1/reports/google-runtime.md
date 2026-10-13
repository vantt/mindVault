# Android Google runtime continuation

Date2026-10-13 (Asia/Saigon). Latest **Google Run03 Passed1/1,23.309s;0 failures/errors/skips**.
Selected live authorization/read/mapping/cache flow accepted; mobile stays **Partial**.
Later [Google cache UI Run02](google-cache-ui-runtime.md) Passed1/1,42.393s covers
app-disconnected cached Cook/lock/reopen and authenticated same-process recreation.
The provider runs and exact historical artifact hashes below remain unchanged;
actual network-off/cancellation/401/403/cold-start/process-death remain Pending.
That cache UI scope preceded [network-off Run04](network-off-runtime.md), which later
Passed actual offline Cook/recreation and NETWORK_FAILED cache preservation in31.477s.
Historical provider/candidate hashes below stay intact;401/403/cancel and broader
phone matrix remain Pending.
Root alone used native Windows ADB over USB; Xiaomi14TPro/API36, serialPN7PW8SGXKLVPRIV.
Independent reviewer confirmed actual runner statuses, all11 match flags and proof limits.

## Exact artifacts and preservation

| Scope | Fixture source SHA256 | Installed test APK SHA256 |
|---|---|---|
| Google Run01/02 | `8b19ef0eca500afd935fd8cf1ae668341863e774f6b6d9e71150b16be033be13` | `5eb335d423d6712809ddbe513b7171958dda517317e361d6f7d4f1fac33908d1` |
| Google Run03 | `0de333ad699af1ca3d18a20d1a2e0bf110b52a0ce7835ac8a5b0a1bbebaced8a` | `cc4d5bbc34e310df36f51e4b3dbebabcd89d3089f903f4989c6f23eabfb5801c` |

Debug APK unchanged:`d4eb8401d0b2cc71f9ca800614dc7f7d8cf391d3e3432cd6a7537ce91382f9a4`.
Data-preserving test-APK updates installed/verified both candidates. Original encrypted
Cookbook digest before/after installations and after each run remains
`894f73e51e316b6a369368ad4cebbc901609ccfe7eabe855aa73c2cbf4e7a4d6`.
Only digest read; no owner-jars operation/data clearing. Cleanup targets only unique
synthetic proof keys/directories. [Preparation](google-acceptance-preparation.md) is a prior snapshot.

## Actual numbered results

| Run | JUnit result | Reached observation | Limit at that run |
|---|---|---|---|
| Google Run01 | **Failed1/1**,44.766s;0 errors/skips | Usable Android authorization/requested Menu metadata | Coarse metadata phase failed; expected tab/schema/cache unaccepted |
| Google Run02 | **Failed1/1**,19.259s;0 errors/skips | Matching metadata, unique expected tab selected, provider rows staged | Coarse origin/schema check failed; mapping/cache unaccepted |
| Google Run03 | **Passed1/1**,23.309s;0 failures/errors/skips | Real metadata/tab/rows, exact known schema, explicit mapping and stored cache | Selected foreground flow only |

Only `dev.passchef.mobile.proof.GoogleCookbookPhysicalTest` ran after actual Awake/
keyguard=false, exact artifact and digest checks. Run03 has one start1/pass0 pair and
terminal `OK (1 test)`; diagnostic status2 is separate. Native exit0 alone is not pass
proof. Existing8 platform tests and earlier accepted flows were not rerun.

## Run03 accepted observations

- All11 known identity/header/row/width flags true; phase `cache`, metadata/cache
  present, draft absent, actual own foreground/session ready, no busy/error flags.
- Actual requested Menu metadata and unique selected tab bind the provider-row origin.
  Independent known header/row assertions passed, including literal synthetic username00123.
- Explicit Label/Recipe/Username columns1/2/3 and first data row2 saved the mapping.
  Real `MenuSnapshotStore.load` equals committed snapshot; encrypted synthetic jars
  remain byte-identical. This is durable file-load proof, not UI reopening/offline proof.

No injected grant/token/metadata/offline table or mocked transport. Real Android
AuthorizationClient and Sheets engine serve the provider path; natural background
lock/normal biometric reconnect policy is retained. No production repair made.
All17 original assertions remain independently verified byte-identical and ordered;
refinement adds only boolean diagnostics/finer phases. Compile Passed59s,9 executed/
85 UP-TO-DATE; lint0 errors/11 warnings/1 hint.

## Retained earlier failures and owner observations

Run01 phase `metadata` covered ID, unique-tab and click checks; exact failing assertion
was not separately recorded. Owner confirmed its tab was not named as expected and
renamed it before Run02. Owner explicitly confirmed account choice and actual Sheets
permission grant in Run01. No account/URL/token/provider cell contents logged here.

Run02 used the same old source/APK/assertions. Phase `values` covered origin and exact
header/row checks; actual draft was ready but no per-cell flags existed. Precise prior
field mismatch remains unrecorded. Owner subsequently completed the known synthetic
fixture; Run03 accepted it without relaxing assertions. Do not guess missing zeros or
claim a production defect. Run03 may reuse the earlier grant; no fresh consent claim.

## Evidence and remaining scope

`/tmp/passchef-cookbook-device-acceptance/`: `google-acceptance-install.json`,
`google-acceptance-preflight.json`, `google-acceptance-run-01.log`/`.json`,
`google-acceptance-run-02-preflight.json`, `google-acceptance-run-02.log`/`.json`,
`google-schema-diagnostic-build.log`/`.json`, and the separate Run03 install/preflight/
run log/JSON. Previous source/APK identities remain retained outside the repository.

Pending: UI cache reopening/offline/disconnect, refresh/cancellation/401/403/network
failure preservation, broader Google/account/platform matrix and remaining phone
lifecycle/session/security acceptance. Write UI remains deferred to later work.
Original **Cookbook UI Run01** remains
a separate unknown reveal failure. Whole mobile remains Partial; no Done/archive or
commit claimed by this report.
