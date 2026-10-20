# Android Cookbook — owner acceptance

Status2026-10-15: **Partial**. Verified candidates installed on Xiaomi/API36;
selected real UI Run04/05 Passed1/1 each, including normal screen policy in Run05.
Historical Run01 cause not reproduced; broader phone/Google matrix Pending. Prior Keystore demo
acceptance does not certify new screens or the Android connection.
Latest [Google cache UI Run02](../../plans/261012-mobile-cookbook-phase1/reports/google-cache-ui-runtime.md)
Passed1/1,42.393s: cached row Cook/lock/reopen/favorites/recent/search and authenticated
same-process recreation. Later [network-off Run04](../../plans/261012-mobile-cookbook-phase1/reports/network-off-runtime.md)
Passed1/1,31.477s: actual offline Cook/recreation and NETWORK_FAILED cache preservation.
Controlled [Google-loaded cache cold02](../../plans/261012-mobile-cookbook-phase1/reports/google-cold-runtime.md)
Passed8.886s after verified SIGKILL and genuine biometric reopening.
Cancellation/stale callbacks/401/403/LMK/task restoration and broader matrix Pending;
original UI Run01 and network Run03 key-wrap causes unresolved.
Selected [Google cancellation preparation](../../plans/261012-mobile-cookbook-phase1/reports/google-cancellation-preparation.md)
compiles/reviews with70-source frozen01/test669a8c9c. External-resolution acceptance remains Pending;
require an actual external Google resolution before requesting owner cancellation.
Silent grant reuse earns no cancellation/stale acceptance; no account/grant changes
force the prompt. Offline synthetic baseline is disclosed separately from provider cache.
Actual Run01 NotExercised15.608s,1skip: SDK returned metadata without an observed
external resolution; cache/jars/owner/owned cleanup preserved, no cancellation credit.
Selected [mapping/tag UI Run01](../../plans/261012-mobile-cookbook-phase1/reports/menu-mapping-runtime.md)
Passed1/1,36.892s: rejected duplicate-role/missing-Recipe mappings preserve exact
last-good cache; explicit remap retains irregular/literal rows; unchanged tagged
Recipe succeeds with its saved Menu and refuses another with no old Dish/Copy.
Broader mapping proposals and device/UI cases remain separate Pending scope.
[Screen-off/clipboard01/02](../../plans/261012-mobile-cookbook-phase1/reports/screen-off-clipboard-runtime.md)
Failed37.492s/54.316s before the required predeadline screen-off gate; hold/return
stages unexercised. Same source/APKs, owner digest/UUID cleanup preserved. Late02
owner Power reply does not establish exact key time; those attempts earned no background clipboard credit.
Separate [owner-Copy screen-off01](../../plans/261012-mobile-cookbook-phase1/reports/owner-screen-off-clipboard-runtime.md)
Passed1/1,121.836s: owner Copy→Power bound to the original production deadline;
sampled>=35s off/locked, then same-Activity legal return with owned clipboard absent,
no Dish/session and exact cache/jars. Original Failed01–03 retained; long-background/
LMK/recreation/broader cross-app/history and precise background30s cleanup remain unaccepted.
[Chrome return diagnostics/repair](../../plans/261012-mobile-cookbook-phase1/reports/cross-app-clipboard-runtime.md)
localized stale expired ownership with the actual Chrome marker intact. Focus/retry
repair passed JVM10 and unchanged adapter3/recreation1; strict repair01 failed during
setup before Copy. October16 run02 terminal is unknown after loss of old temporary
evidence. October20 recovery01/failure-tail01 Failed after actual Chrome return;
all11 retries ended with ownership retained. Read-only actual-marker probe confirmed
a null Chrome label and NPE from the original expression. The
[minimal null-safe repair](../../plans/261012-mobile-cookbook-phase1/reports/clipboard-null-label-repair-runtime.md)
passed fresh build/JVM10, five relevant physical regressions and unchanged strict
Chrome return01 Passed99.870s (native selected boundary). Exact Chrome marker was
preserved while expired ownership was forgotten. Owner reported close/failed return
for the proof, then confirmed normal launcher app stable. Both replies are retained;
exact prior close cause remains unknown. No extra acceptance run is needed for this fix.
Mobile Partial.
[Saved resume checkpoint](../../plans/261012-mobile-cookbook-phase1/reports/clipboard-return-handoff.md).
[Security/UI continuation](../../plans/261012-mobile-cookbook-phase1/reports/security-ui-runtime.md)
locked pixels01 Passed1/1,2.321s and authenticated02 Passed1/1,25.890s: selected EN/VI
setup/manual Cook and secure shell display exclusion. Selected owner hardware
shortcut07 Passed; full layout/accessibility remain Pending, three earlier security
failures retained.
Recents01/02 Failed206.052s/182.457s, respectively after checkpoint without actual
stop/resume and at genuine setup before checkpoint. Later Recents03 Passed137.789s:
actual stop/lock/genuine reopen and owner-observed blank own Card. Earlier causes
unconfirmed; wider acceptance Pending, no blind retry.

## Google setup, once the candidate is ready

Use the existing [Cloud/consent setup](google-oauth-sheets-setup.md) steps1–4.
Enable Sheets API, add the chosen test account and the settled
`https://www.googleapis.com/auth/spreadsheets` scope. The current slice only reads;
future writes remain append/update, never deletion.

Create an **Android** OAuth client under Google Auth Platform → Clients, with
the installed candidate's package and signing-certificate SHA-1. Debug is
`dev.passchef.mobile.demo`; release is `dev.passchef.mobile`. Register each actual
package/signing pair used. Retrieve the candidate's public certificate fingerprint
from its APK or Gradle signing report; do not disclose signing keys or passwords.
The separate desktop client's loopback/secret configuration stays separate.

Verified debug candidate signing fingerprint (2026-10-12):
`3C:FE:96:5E:66:AC:6F:89:4D:C1:A5:07:AA:C9:C3:8D:CF:F5:D2:C9`.
Measured from the signed APK public certificate; it is not a private signing key.

The app uses Google's supported
[AuthorizationClient flow](https://developer.android.com/identity/authorization).
No desktop client secret is bundled. Grant consent through the app's explicit
Connect action; a public Menu URL alone is not an Android permission grant.
Use a dedicated synthetic Menu accessible to the selected Google account.

### Owner setup checklist for the currently installed phone build

1. Open [Google Cloud Console](https://console.cloud.google.com/) and select/create
   the PassChef development project; enable **Google Sheets API** in API Library.
2. In **Google Auth Platform → Branding**, configure PassChef's app/support details.
   Under **Audience**, choose **External**, retain **Testing**, and add the Google
   account used on the phone to **Test users**.
3. Under **Data Access**, add `https://www.googleapis.com/auth/spreadsheets`.
   This grants Sheets read/write permission; the current Android slice consumes reads.
4. Under **Clients → Create client**, choose **Android**; name it `PassChef Android debug`.
   Enter package `dev.passchef.mobile.demo` and SHA-1
   `3C:FE:96:5E:66:AC:6F:89:4D:C1:A5:07:AA:C9:C3:8D:CF:F5:D2:C9`, then create.
5. On the phone, open PassChef, unlock the Cookbook and tap **Connect**; choose the
   listed test account and grant permission. Use a synthetic Menu that account can read.
   Record actual grant and API read separately; Console configuration alone is not Passed.

### Live Google acceptance after client setup

Owner reported Android client configured on2026-10-13. Later selected runtime
Passed Google Run03 with actual grant/metadata/rows/mapping/cache-file readback.
Selected cache UI Run02 accepted app-disconnected reopening/Cook and authenticated
same-process recreation. Later network-off Run04 accepted strict actual network absence
and NETWORK_FAILED preservation. Later Google-loaded cache cold02 accepted controlled
SIGKILL/fresh-process reopening;401/403/cancel/LMK/task restoration remain **Pending**.
Confirm Sheets API enabled, the phone account in Test users and the Sheets scope saved.
Create a dedicated private Menu `PassChef OAuth Test`, rename its tab `Test`, and
enter this fixture only; format C2 as plain text before entering the literal username:

| Label | Recipe | Username |
|---|---|---|
| Mail-test | abcd#1 | 00123 |

The selected account must own or have access to this Menu; no public sharing required.
Provide its URL to the coordinating agent. For owner acceptance, use the unique proof
Cookbook with synthetic jars, never reset/recover/reconfigure the existing owner record.
The prepared selective test must exercise the real Sources screen's **Connect / load tabs**,
Google account/consent, actual tab selection, fetched preview and confirmed cache.
Owner performs genuine setup/unlock biometric and Google approval. If consent backgrounds
and locks the Cookbook, unlock and reconnect through the normal UI; stale grants stay rejected.
Successful configuration/account selection alone is not Passed: verify actual metadata,
the selected `Test` tab, literal fake rows and persisted mapping/cache, with before/after
owner Cookbook digest. Never use **Preview columns** plus a pasted offline table as
live Google proof. Record each actual result and preserve broader Google error/write
and phone acceptance as separate Pending checks.
The SDK may reuse a previous valid grant; successful API reads prove usable permission,
not that a fresh consent screen appeared. Record any actual consent screen separately
from the grant/read result using the owner's observation.

PassChef owns the Cloud project/client registration; each user grants access using
their own Google account. Users do not create their own OAuth clients. Android's
client registration identifies the package/signature; this app's on-device
AuthorizationClient flow has no desktop client secret or backend offline-code exchange.
Register the production package and actual release/Play signing certificate separately
when distributing release builds; public rollout also needs applicable OAuth verification.

### Usage cost and project quota — checked2026-10-13

Client registration itself has no separate rental fee. Google's [setup codelab](https://codelabs.developers.google.com/codelabs/sign-in-with-google-button)
describes the Cloud account/project as free; enabled billable services have their own costs.
[Sheets API pricing](https://developers.google.com/workspace/sheets/api/limits) currently
states standard usage has no additional cost. Published read/write limits are300 requests
per minute per project and60 per minute per user per project, separately for each category.
The project quota is shared by users of that project, not a fresh project quota per user.
Google also announces planned charges for usage above quota later in2026; consult its
[Workspace tiering policy](https://developers.google.com/workspace/tools-safety) and the
project's actual quotas/billing before scaling. Do not promise unlimited free API use.

## Selective network-off check — Run04 accepted

Use the prepared provider fixture with `passchefGoogleNetworkContinuation=true`;
root handles exact APK/digest/preflight and launches only that selected method.
Keep network available while the genuine provider fixture loads. At
`network-owner-disable-radios`, root's authorized USB runner switches Wi-Fi/mobile
data off and airplane mode on, records commands and restores the enabled baseline
with airplane mode off in finally. The fixture independently requires actual network
absence; command success/settings values alone do not count. Owner performs genuine
biometric prompts during cached Cook/reopen/recreation. SDK authorization failure is separate
from actual Sheets transport failure; record only the observed path.
[Actual runtime evidence](../../plans/261012-mobile-cookbook-phase1/reports/network-off-runtime.md)
records Run04 Passed and Run01/02/03 Failed history. Restoration separately observed
Wi-Fi enabled, airplane mode off and default network available; mobile-data enable
command accepted only, not independently measured. Exact cache/jars preserved.

## Record each actual result

[Current real timer continuation](../../plans/261012-mobile-cookbook-phase1/reports/session-timer-runtime.md)
selects idle and total methods separately with genuine setup/owner fingerprint.
After timing begins, leave the phone/test screen untouched for about5 or10minutes;
proof itself sends total-test activity and emits progress each minute. No new owner
unlock during timing; any pause/stop/focus loss fails. Idle Run01/02 pointer failures
and Run03/04 setup failures retained; Run05 actual5-minute idle Passed1/1,341.722s.
Total Run01/02/03 setup Failed182.309s/182.279s/182.138s retained;14October Total04
Passed1/1,609.969s,runner[1,0]/OK1. Minute1–9 real touches/Cooks preserved original
opened/generation;595106ms ready then601725ms locked/cleared with idle60945ms.
Jars/owner digest unchanged; same frozen03/APKs, no source/build/install changes.
Selected5/10-minute API36 foreground proof KEEP_SCREEN_ON accepted; broader timer UX
Pending. No source/time/auth bypass or hidden failure; earlier causes remain unknown.

| Check | Required observation | Current status |
|---|---|---|
| Install/preflight | Debug package updated without clearing data; original encrypted Cookbook SHA256 unchanged before/after install and tests | Passed network-off Run04 exact candidate; original digest preserved after installs/all runs |
| Offline Cook | Unlock with real jars; paste known Recipe; exact expected Dish | Passed Run04/05 with isolated synthetic jars and real fingerprint |
| Tagged context | Correct Menu cooks; absent/wrong Menu refuses; previous Dish disappears | Selected Passed: correct/absent/edit Run04; unchanged Recipe under different saved Menu refuses and clears old Dish/Copy in mapping/tag Run01 |
| Google consent | Registered package/signature; actual user grant; no token displayed/logged | Passed selected runtime scope: usable grant; owner confirmed account/consent in Google Run01 |
| Menu/tab read | Real metadata and bounded values; selected tab remains explicit | Passed Google Run03: selected Test tab and exact literal fake schema/row, including00123 |
| Mapping | Actual preview; confirm roles; duplicate/empty/ragged inputs handled | Partial: Google preview/mapping Run03; irregular-row/duplicate+blank-header explicit remap and invalid-role preservation mapping/tag Run01 Passed; ambiguous proposals/broader matrix Pending |
| Offline/cache | Reopen private cache; search label/username; favorites/recent retain origin | Passed network-off Run04/recreation, synthetic offline cold02 5.993s and Google-loaded cold02 8.886s after verified SIGKILL; LMK/task restore Pending |
| Errors/refresh | Cancel/401/403/network failure retain last good cache and clear stale results | Partial: actual NETWORK_FAILED exact cache/jars preservation and stale Dish/Copy absence Passed Run04;401/403/cancel/stale callback Pending |
| Reveal | Masked default; explicit reveal hides after10seconds | Passed Run04/05, including KEEP_SCREEN_ON=false in Run05; historical Run01 cause unknown |
| Clipboard | Dish copy; real30second compare-clear; replaced/username text survives old cleanup | Selected Passed foreground ownership/locked adapter recreation + authenticated owner-Copy35s screen-off/legal return; cross-app/authenticated recreation/process death/history/API26 fallback Pending |
| Lifecycle | Navigation/edit/lock/background/recreation clear Dish and require unlock | Partial: earlier edit/Back/background/recreation/Recents/cold Passed; owner-Copy35s actual screen-off/same-Activity locked return Passed; long-background/LMK/task matrix Pending |
| Session | Real5minute idle and10minute total despite activity, no deadline reset | Passed selected API36 foreground proof KEEP_SCREEN_ON: Idle05 341.722s and Total04 609.969s; earlier failures retained, broader timer UX Pending |
| Security/UI | EN/VI, accessibility/layout, blocked capture/hidden Recents, sanitized own-app logs | Partial: secure shell capture/Recents03/VI OS field01/center02/04/explicit-finger01 Passed; legacy edge01/02/dwell04 Failed; owner revealed-Dish shortcut07 Passed; TalkBack/full layout Pending |
| Recents lifecycle | Actual same-Activity stop/lock, preserved jars and genuine biometric reopen | Passed Run03 1/1,137.789s; Run01/02 failures retained; API36 proof KEEP_SCREEN_ON scope |

Record candidate SHA/APK hash, device/API, timestamps, observer and evidence for
each run. A compiled test APK is **not run** until a device/runtime executes it.
Account setup and consent require the owner; root announces each genuine runtime
checkpoint after candidate review and actual device readiness checks.

### Selected offline process-death / cold launch — accepted

[Exact cold evidence](../../plans/261012-mobile-cookbook-phase1/reports/cold-process-runtime.md)
uses an unexported dedicated-task debug Activity and validated canonical UUID locator,
five isolated bounded helpers/tests, genuine owner setup and actual UI-confirmed offline
synthetic Menu. Warm02 was InterruptedByVerifiedSIGKILL, never JUnit Passed. Root
revalidated solePID25682/cmdline/UID/start identity and owned hashes in the signal
shell, then confirmed `/proc/25682` absent and target pidof empty before fresh launch.
Separate cold02 Passed1/1,5.993s with actualPID25991 initially locked/no app session/
token/Dish, exact preunlock/cache-reload disk hashes and genuine biometric reopening.
Origin/Recipe/literal00123/search/favorites/recent/Cook and encrypted jars preserved;
final semantic cache equality permits normal recent updates. Owned directory cleanup
and owner digest preserved. Provider-loaded cold cache, LMK/background death and
Android saved-task restoration remain separate Pending scope. Warm01 failed before
SIGKILL due to missing focus property in windows dump; displays query correction
preserves strict focus guard/APKs/assertions and historical Failed verdict.

### Google-loaded cache process death — selected cold02 accepted

[Provider cold candidate](../../plans/261012-mobile-cookbook-phase1/reports/google-cold-runtime.md)
uses separately archived frozen58/debug1f42/test3711 for accepted Run02. It preserves
all54 prior app sources/manifests and original17 Google predicates. Warm selector is
`GoogleColdProcessPhysicalTest#prepareProviderCacheForAbruptProcessDeath`, followed
only after verified old-PID death by `#reopenProviderCacheInFreshProcess` with the
same canonical UUID and explicit warm/cold stage. Root announces genuine biometric/
Google prompts after fresh owner readiness. Warm02 InterruptedByVerifiedSIGKILL/
PID13092→coldPID14408, cold02 Passed1/1,8.886s with real fingerprint/exact cache reload/
search/favorites/recent/origin/Recipe/00123/Cook and preserved jars/owned cleanup/digest.
Cold01 Failed181.588s at reopening/cause unknown; diagnostic retry is no production
repair. SDK may reuse grant; no fresh consent/revocation or offline-paste credit.
Observed network available; network-off/LMK/task restoration are separate scopes.

### Selected secure capture — accepted; owner observations separate

[Exact Security/UI evidence](../../plans/261012-mobile-cookbook-phase1/reports/security-ui-runtime.md)
requires nonnull actual display capture and every pixel in the visible own-content
region opaque black, with secure/awake/unlocked/focused foreground before/after.
Accepted route is fixed shell `screencap -p`, without secure-layer/policy changes;
no images or accessibility trees are saved. Authenticated02 also verifies genuine
EN/VI Cook/reveal and visible exact synthetic Dish inside that region.

Locked01 raw-layout48dp, Capture01 same-node accessibility and Auth01 UiAutomation-null
remain Failed. Later diagnostics show40dp raw language layout with48dp expanded touch
bounds; no actual undersized-target defect inferred. English OS password-field identity
diagnosed. Later [VI OS field01](../../plans/261012-mobile-cookbook-phase1/reports/vi-accessibility-runtime.md)
Passed1/1,1.5s: one visible/editable/password ID10 with localized descendant label;
old direct same-node checker remains Failed, previous optionalVI ID0 cause unknown.
TalkBack spoken behavior/full layout remain Pending. [Native language input](../../plans/261012-mobile-cookbook-phase1/reports/language-touch-runtime.md)
center02 Passed1.852s with two60ms native taps; edge01/02 Failed after one pair.
Post-edge02 stillEnglish/foreguardtrue; no whole48dp target/undersized-defect claim.
Matched edge-dwell04 initially blocked by USB on14October, then installed/run15October:
Failed1.709s with configured60ms; same-candidate center04 Passed1.802s. Owner digest/
old59/protected208/unrelated23 unchanged. Later geometry01 Passed1.505s, observed scroll
coverage0. Separate paired explicit-tool finger01 Passed2.464s: at equal expanded
midpoints/configured60ms, UNKNOWN retained localized UI and FINGER switched both
directions. Actual baseline constructorUnknown and checked non-tool readback observed;
old61/owner preserved. Accept new FINGER samples only; historical event properties/
whole-target coverage remain open, no production repair claim.

[Manual shortcut continuation](../../plans/261012-mobile-cookbook-phase1/reports/manual-shortcut-runtime.md)
uses a separate genuine UUID setup/Cook and passive200ms reveal/lifecycle checkpoint;
owner controls Reveal then immediate Power+VolumeDown. JUnit checkpoint is separate
from actual owner OS-result/Dish-visibility evidence. Run03 Failed182.485s in setup
with current wrap15 diagnostic; Run04 reached secure masked-ready Cook but Failed
236.235s awaiting owner-response completion180s. No observed reveal/stop/answer or
fabricated signal. Actual hardware shortcut stays Pending; original failures retained.
Owner digest/source64/helper12/exact owned-directory cleanup verified after both runs.
Late04 missing-Reveal reply arrived after terminal, timing unconfirmed. Clarified
same-candidate05 reached ready then Failed188.013s completion wait, no sampled reveal/
stop or confirmed screenshot; cleanup/digest preserved. Ask current screen/labels
before a further timed run; no runtime remains active at this checkpoint.


Run06 owner explicitly used Power+VolumeDown and reported screenshot refusal; after
Reveal the visible dot line stayed masked. Controller reveal samples do not establish
rendered text. JUnit remains Failed193.721s after publication error/completion timeout.
Native isolated publication repair checks passed6cases, no phone acceptance inferred.
Separate normally synchronized Compose observer07 Passed1/1,116.736s; owner confirms
Dish text appeared then Power+VolumeDown was blocked. Selected hardware shortcut
accepted on Xiaomi/API36 proof foreground. Original64 app sources/assertions and
production unchanged; final owner/UUID cleanup/source66/helper12/protected208/23 intact.
No exact key timestamp/pixel proof or retrospective06 cause claimed.

Separate owner checkpoint `owner-screenshot-recents-check` asks for hardware screenshot,
inspection of only the own Recents card, then return to the app. Prepared fixture
asserts actual stop/lock and genuine biometric reopen; compilation alone accepts none
of these. Root records screenshot rejection/card appearance separately from lifecycle
JUnit. Original10second remask remains; note whether Dish was visible at the observation.

Recents01 Failed206.052s: post-checkpoint pause/stop remained0, so neither actual
transition nor genuine reopening was accepted. Cleanup-only locked state is excluded.
Recents02 Failed182.457s: setup remained busy/ready=false/focus=false while OS was
awake/unlocked; checkpoint never reached, navigation events empty and no ADBKEY187
sent. Same frozen04/testea84 source/APKs, no rebuild/install between attempts.
Causes unconfirmed; no owner-action/inaction inference. Owner reported "can't take
screenshot"; exact shortcut/reveal timing unconfirmed.
After fresh readiness, identical Run03 Passed1/1,137.789s: root opens real Recents20s,
actual pause/stop1 and cleared session/Dish/Copy observed, then owner biometric reopens
and Cook succeeds with unchanged jars/digest. Owner confirms own Card blank/covered
without Recipe/Dish. That visual observation is separate from JUnit lifecycle proof.
OS screenshot shortcut not retried; broader matrix and earlier causes remain Pending.

## Unattended work completed before further owner prompts

[Source coverage15October](../../plans/261012-mobile-cookbook-phase1/reports/unattended-source-coverage.md)
adds six actual-coordinator/temporary-cache cases and six canonical HTTP streaming
cancellation cases. Fresh app10/bridge22/standalone35 Passed; repeated client common
cases are not independent totals. Existing code/assertions/APKs unchanged. No phone
interaction occurred. Local cancellation/error-classification tests do not accept
actual Google cancel/stale SDK callback/401/403 or broader mapping UI. Owner-dependent
rows in the table above remain Pending at those scopes; prior physical passes retained.

## Resume diagnostics when owner ready

[Google runtime](../../plans/261012-mobile-cookbook-phase1/reports/google-runtime.md)
records actual Google Run01/02 Failed, then Run03 Passed1/1,23.309s after owner updated
the fake fixture. Exact Google metadata/Test tab/rows, mapping and durable cache-file
load accepted; all17 assertions unchanged. Fresh consent was owner-observed in Run01;
Run03 may reuse the valid grant. Later cache UI Run02 accepted app-disconnected
reopening/Cook and authenticated same-process recreation. Later network-off Run04
accepted actual offline/error preservation. Later Google-loaded cache cold02 accepted
controlled SIGKILL/fresh-process reopening;401/403/cancel/LMK/task restoration and broader matrix Pending.

[Current continuation](../../plans/261012-mobile-cookbook-phase1/reports/unattended-regression.md)
records new local APK hashes separately from the still-installed diagnostic pair.
That snapshot predates the [owner-ready runtime](../../plans/261012-mobile-cookbook-phase1/reports/ui-runtime-continuation.md):
Run04 pair installed with digest preserved, locked-only diagnostic Passed1/1,
Run02/03 Failed, then Run04 Passed1/1 after verified unlock; phone scope/limits are
recorded separately. Comparison test086e7022... was later installed after reconnect;
updated locked diagnostic1/1 and normal-policy Run05 1/1 passed, digest preserved.
Original Run01 cause remains not reproduced. Google setup was incomplete at Run05;
that Run05 snapshot left grant/read Pending. Later GoogleRun03 and cache UI Run02
accepted the selected provider/cache flows described above.

[Unattended clipboard preparation](../../plans/261012-mobile-cookbook-phase1/reports/unattended-clipboard-preparation.md)
adds3 compiled/reviewed API28+ adapter tests for untouched30s clear and replacement/
newer-copy survival. [Oct13 actual runtime](../../plans/261012-mobile-cookbook-phase1/reports/clipboard-runtime.md)
Passed3/3,99.926s after exact APK install and genuine OS unlock; Cookbook digest preserved.
This class uses a real locked proof Activity and no authentication, but requires an
awake/unlocked OS foreground. Its first synthetic copy replaces global clipboard;
it reads no prior content and cleans only exact own synthetic marker/text. It does
not certify cooking without authentication or the API26/27 clipboard fallback.
Replacement proof uses the same test UID's real ClipboardManager; cross-app UID,
background/authenticated recreation/process death and OS clipboard history remain
separate observations.
After readiness/digest/install checks, select only
`dev.passchef.mobile.proof.OwnedClipboardPhysicalTest`, not the full suite.

[Same-process clipboard recreation](../../plans/261012-mobile-cookbook-phase1/reports/clipboard-recreation-runtime.md)
Passed1/1,31.721s on API36 after exact test-APK update; debug APK and owner Cookbook
digest preserved. API28+ locked fixture recreates the actual Activity at~5s, retains
its original marker/text before30s and clears after the original30.5s boundary.
Only MainActivity callbacks manage foreground; no timer reset/authentication/session
injection. This accepts adapter recreation within a live process; authenticated
Cookbook recreation/background/process death/history and Google remain Pending.
Select only `dev.passchef.mobile.proof.ClipboardRecreationPhysicalTest` after actual
screen/digest/artifact readiness checks; no biometric request belongs to this class.

Root owns Windows ADB through WSL; re-list with
`/mnt/c/Users/van.tran_fgorg/AppData/Local/Android/Sdk/platform-tools/adb.exe devices -l`
and select the actual authorized physical serial (lastPN7PW8SGXKLVPRIV). Do not wake/
unlock the sleeping phone or invoke any owner biometric job until owner is ready.
Preserve original encrypted Cookbook; proof Activities use unique synthetic storage.
Immediately before each runtime attempt, verify actual `Awake` and keyguard
`showing=false`; owner readiness alone does not establish current screen state.
After fingerprint, owner leaves the test screen untouched for about1minute.

After installing/verifying the current candidate, select the locked-only diagnostic
test first; it makes no setup/authentication call. Separately select the authenticated
UI test for the next numbered foreground retry with the ready owner. Never launch
the entire instrumentation suite.

```sh
"$PASSCHEF_ADB" -s "$PASSCHEF_SERIAL" shell am instrument -w -r \
  -e class dev.passchef.mobile.proof.CookbookUiDiagnosticsPhysicalTest \
  dev.passchef.mobile.demo.test/androidx.test.runner.AndroidJUnitRunner
"$PASSCHEF_ADB" -s "$PASSCHEF_SERIAL" shell am instrument -w -r \
  -e class dev.passchef.mobile.proof.CookbookUiPhysicalTest \
  dev.passchef.mobile.demo.test/androidx.test.runner.AndroidJUnitRunner
```

Set these task variables to the verified Windows executable/physical serial above.
Keep separate sanitized evidence for each run and inspect JUnit failures, not just
process exit0. `diagnosticStateReadSucceeded=false` means state bits are unavailable.
Preserve every assertion/real10.5s wait. KEEP_SCREEN_ON is proof-only; passing under
it alone does not establish Run01's cause or a production fix. Do not repeat the8
already-passed platform tests without a relevant change.

Android restricts background clipboard access. Observe the actual foreground
cleanup/reconciliation path; do not claim history-wide removal or exact background
timing. See [platform guidance](https://developer.android.com/develop/ui/views/touch-and-input/copy-paste).

Existing [unexecuted Keystore matrix](../../archive/plans/261011-keystore-recovery/reports/current-device-acceptance.md)
remains separately Pending. New Xiaomi acceptance cannot establish API26,
absent hardware/enrollment or every rare failure branch.
