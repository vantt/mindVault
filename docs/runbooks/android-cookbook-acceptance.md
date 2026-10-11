# Android Cookbook — owner acceptance

Status2026-10-12: **Pending**. New Cookbook code is implemented; this
checklist records no device or live Google pass. Prior Keystore demo acceptance
does not certify new screens or the Android connection.

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

The app uses Google's supported
[AuthorizationClient flow](https://developer.android.com/identity/authorization).
No desktop client secret is bundled. Grant consent through the app's explicit
Connect action; a public Menu URL alone is not an Android permission grant.
Use a dedicated synthetic Menu accessible to the selected Google account.

## Record each actual result

| Check | Required observation | Current status |
|---|---|---|
| Install/preflight | Isolated debug package; preserve existing encrypted Cookbook and aliases | Pending |
| Offline Cook | Unlock with real jars; paste known Recipe; exact expected Dish | Pending |
| Tagged context | Correct Menu cooks; absent/wrong Menu refuses; previous Dish disappears | Pending |
| Google consent | Registered package/signature; actual user grant; no token displayed/logged | Pending |
| Menu/tab read | Real metadata and bounded values; selected tab remains explicit | Pending |
| Mapping | Actual preview; confirm roles; duplicate/empty/ragged inputs handled | Pending |
| Offline/cache | Reopen private cache; search label/username; favorites/recent retain origin | Pending |
| Errors/refresh | Cancel/401/403/network failure retain last good cache and clear stale results | Pending |
| Reveal | Masked default; explicit reveal hides after10seconds | Pending |
| Clipboard | Dish copy; real30second compare-clear; replaced/username text survives old cleanup | Pending |
| Lifecycle | Navigation/edit/lock/background/recreation clear Dish and require unlock | Pending |
| Session | Real5minute idle and10minute total despite activity, no deadline reset | Pending |
| Security/UI | EN/VI, accessibility/layout, blocked capture/hidden Recents, sanitized own-app logs | Pending |

Record candidate SHA/APK hash, device/API, timestamps, observer and evidence for
each run. A compiled test APK is **not run** until a device/runtime executes it.
Account setup and consent require the owner; no phone action is requested now.

Android restricts background clipboard access. Observe the actual foreground
cleanup/reconciliation path; do not claim history-wide removal or exact background
timing. See [platform guidance](https://developer.android.com/develop/ui/views/touch-and-input/copy-paste).

Existing [unexecuted Keystore matrix](../../archive/plans/261011-keystore-recovery/reports/current-device-acceptance.md)
remains separately Pending. New Xiaomi acceptance cannot establish API26,
absent hardware/enrollment or every rare failure branch.
