# Android Cookbook — owner acceptance

Status2026-10-13: **Partial**. Verified candidate installed on Xiaomi/API36;
selected real UI Run04/05 Passed1/1 each, including normal screen policy in Run05.
Historical Run01 cause not reproduced; broader phone/Google matrix Pending. Prior Keystore demo
acceptance does not certify new screens or the Android connection.
Latest [Google cache UI Run02](../../plans/261012-mobile-cookbook-phase1/reports/google-cache-ui-runtime.md)
Passed1/1,42.393s: cached row Cook/lock/reopen/favorites/recent/search and authenticated
same-process recreation. Later [network-off Run04](../../plans/261012-mobile-cookbook-phase1/reports/network-off-runtime.md)
Passed1/1,31.477s: actual offline Cook/recreation and NETWORK_FAILED cache preservation.
Cancellation/stale callbacks/401/403/cold start/process death and broader matrix Pending;
original UI Run01 and network Run03 key-wrap causes unresolved.

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
and NETWORK_FAILED preservation;401/403/cancel/cold start remain **Pending**.
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

| Check | Required observation | Current status |
|---|---|---|
| Install/preflight | Debug package updated without clearing data; original encrypted Cookbook SHA256 unchanged before/after install and tests | Passed network-off Run04 exact candidate; original digest preserved after installs/all runs |
| Offline Cook | Unlock with real jars; paste known Recipe; exact expected Dish | Passed Run04/05 with isolated synthetic jars and real fingerprint |
| Tagged context | Correct Menu cooks; absent/wrong Menu refuses; previous Dish disappears | Partial: correct/absent Menu and edit clearing passed Run04; wrong different Menu Pending |
| Google consent | Registered package/signature; actual user grant; no token displayed/logged | Passed selected runtime scope: usable grant; owner confirmed account/consent in Google Run01 |
| Menu/tab read | Real metadata and bounded values; selected tab remains explicit | Passed Google Run03: selected Test tab and exact literal fake schema/row, including00123 |
| Mapping | Actual preview; confirm roles; duplicate/empty/ragged inputs handled | Partial: real Google preview/explicit role mapping Passed Run03; earlier offline flow Passed Run04; broader matrix Pending |
| Offline/cache | Reopen private cache; search label/username; favorites/recent retain origin | Passed network-off Run04 actual network absence/Cook/search/favorites/recent and authenticated same-process recreation; cold start/process death Pending |
| Errors/refresh | Cancel/401/403/network failure retain last good cache and clear stale results | Partial: actual NETWORK_FAILED exact cache/jars preservation and stale Dish/Copy absence Passed Run04;401/403/cancel/stale callback Pending |
| Reveal | Masked default; explicit reveal hides after10seconds | Passed Run04/05, including KEEP_SCREEN_ON=false in Run05; historical Run01 cause unknown |
| Clipboard | Dish copy; real30second compare-clear; replaced/username text survives old cleanup | Passed foreground API36 ownership + same-process locked adapter recreation; background/authenticated recreation/process death/history/API26 fallback Pending |
| Lifecycle | Navigation/edit/lock/background/recreation clear Dish and require unlock | Partial: edit/Back/background Run04; lock and authenticated same-process recreation Google cache UI Run02 Passed; cold start/process death Pending |
| Session | Real5minute idle and10minute total despite activity, no deadline reset | Pending |
| Security/UI | EN/VI, accessibility/layout, blocked capture/hidden Recents, sanitized own-app logs | Pending |

Record candidate SHA/APK hash, device/API, timestamps, observer and evidence for
each run. A compiled test APK is **not run** until a device/runtime executes it.
Account setup and consent require the owner; no phone action is requested now.

## Resume diagnostics when owner ready

[Google runtime](../../plans/261012-mobile-cookbook-phase1/reports/google-runtime.md)
records actual Google Run01/02 Failed, then Run03 Passed1/1,23.309s after owner updated
the fake fixture. Exact Google metadata/Test tab/rows, mapping and durable cache-file
load accepted; all17 assertions unchanged. Fresh consent was owner-observed in Run01;
Run03 may reuse the valid grant. Later cache UI Run02 accepted app-disconnected
reopening/Cook and authenticated same-process recreation. Later network-off Run04
accepted actual offline/error preservation;401/403/cancel/cold start and broader matrix Pending.

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
