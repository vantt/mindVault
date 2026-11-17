# 07 — Picker spike (phase 01): setup, run sheet, results

Status: **M1–M10 PASS on device (2026-11-10).** Go criteria met. Open: M11 (multi-account).

## What was built
- `app/src/debug/kotlin/dev/passchef/mobile/proof/drive-picker-spike-activity.kt` — debug-only Activity, registered in `app/src/debug/AndroidManifest.xml` (exported for `adb am start`; release unaffected).
- Flow: `AuthorizationRequest` scope `drive.file` only, `setOptOutIncludingGrantedScopes(true)`, `Prompt.CONSENT`, `ResourceParameter.PICKER_OAUTH_TRIGGER="true"`, `PICKER_MIMETYPES=application/vnd.google-apps.spreadsheet`, optional `PICKER_FILE_IDS`. Picked IDs read from `AuthorizationResult.tokenResponseParams["picked_file_ids"]`. Reads/appends through existing `SheetsClient` (`:sheets-android`).
- Confirmed offline: `play-services-auth:22.0.0` (current dependency) already ships all `PICKER_*` constants (`trigger_onepick`, `mimetypes`, `file_ids`, …) and `getTokenResponseParams`. No dependency bump.
- Safety: token RAM-only, never logged/shown; log shows HTTP status, counts, IDs only; append writes one marker row `passchef-picker-spike, <time>`.
- Verified: `:app:assembleDebug` + `:app:testDebugUnitTest` pass (0 failures); release APK unchanged (debug source set only).

## Owner setup (Google Cloud, same project as current Android client)
1. APIs & Services → Library → enable **Google Picker API** (and Google Drive API if the console asks).
2. Google Auth Platform → Data Access → add scope `https://www.googleapis.com/auth/drive.file` (non-sensitive). Keep existing `spreadsheets` scope.
3. Clients: Android OAuth client for package `dev.passchef.mobile.demo` + debug keystore SHA-1 must exist (same as current Cookbook connect). Test user = your account.
4. Make a **throwaway test spreadsheet** with a few fake rows (append writes into it). Note the ID of a second spreadsheet you will NOT pick.

## Run
```bash
cd /home/vantt/projects/passMan
adb.exe install -r app/build/outputs/apk/debug/app-debug.apk
adb.exe shell am start -n dev.passchef.mobile.demo/dev.passchef.mobile.proof.DrivePickerSpikeActivity
```
Buttons: 1 Pick (Picker) · 2 Silent re-authorize · 3 Read picked · 4 Append marker · 5 Read unpicked ID.

## Matrix (fill in)
| # | Case | Steps | Expect | Result | Notes |
|---|---|---|---|---|---|
| M1 | Picker opens | 1 | consent → Picker (browser/custom tab?) lists only spreadsheets | PASS | Opens INSIDE Play services full-screen (`com.google.android.gms/…FullScreenConsentActivity`, same task), not a browser. Steps: "Google hasn't verified this app" (Testing) → drive.file consent → Picker "Select an item": Recent, search, File type; MIME filter works (only Sheets). Thumbnails preview sheet content. 28.5s incl. user taps |
| M2 | Picked IDs returned | 1 → pick test sheet | `picked_file_ids` = 1 ID, token present, scopes = drive.file only | PASS | only key in tokenResponseParams = `picked_file_ids`; grantedScopes = [drive.file] |
| M3 | Read picked | 3 | metadata 200 + values.get 200 | **PASS** | metadata 200 (3 tabs), values.get 200 (5 rows). "File not found" risk did not happen |
| M4 | Append picked | 4 | append 200, row visible in sheet | PASS | append 200, updatedRange present |
| M5 | Unpicked denied | 5 with other ID | 403 or 404 | PASS | 404 with bare ID (Drive hides unpicked files; first try used full URL, retested with ID) |
| M6 | Silent re-auth | 2 (same session) | token without UI | PASS | 113ms, no UI, no picked_file_ids (expected) |
| M7 | Survives restart | force-stop, relaunch, 2 → 3 | token without Picker, read 200 | PASS | after force-stop: silent auth 81ms, read picked ID 200 (via field 5). App must persist picked IDs itself |
| M8 | Survives reinstall | uninstall/install, 2 → 3 | ? | PASS | After uninstall + fresh install: silent authorize needed a resolution but completed by itself in 3.4s (no taps, no Picker); previously picked ID read 200. Grant lives server-side per package + signing key |
| M9 | File-ID filter | paste test sheet ID in top field → 1 | Picker shows only that file | PASS (owner to confirm list showed 1 file) | `PICKER_FILE_IDS` accepted; returned `picked_file_ids` = the filtered ID. Re-pick of an already-granted file still goes through interstitial + consent + Picker (86s incl. human taps) |
| M10 | Cancel | 1 → back out of Picker | clean failure, no crash | PASS | X in Picker → `ApiException 16: Cancelled by user`, resultCode 0, app stable |
| M11 | Wrong browser account | browser signed in to other account | which account Picker uses | | |

## Findings
- `drive.file` token reads and appends a Picker-picked native Google Sheet through Sheets API v4. Phase-01 key unknown resolved.
- `PICKER_OAUTH_TRIGGER="true"` works on play-services-auth 22.0.0. No API key / app ID was needed on Android.
- Grant is per file and persists across process death; silent `authorize` returns a fresh token with no UI. Picked IDs are not returned again, so the app stores them (already does: snapshot sheet ID).
- Unpicked file = 404 (not 403). Error copy for "lost access" must treat 404 + 403 alike.
- Picker is a Play services screen, not ours: our FLAG_SECURE does not cover it, and its grid shows sheet thumbnails. Acceptable (Google UI), but the connect screen should say "Google will show your Drive".
- Each Picker run with `Prompt.CONSENT` repeats the unverified-app interstitial + consent (Testing). Production verification of a non-sensitive scope should remove the interstitial; consent still shows per pick.
- Reinstall keeps the grant, but app data (saved sheet ID) is gone; restoring sheet IDs after reinstall needs Recent/backup or one re-pick.
- Cancel = ApiException 16 → map to "Not connected. Try again" (no error tone).

## Go / no-go
Go = M3 + M4 pass, M5 denied, M7 passes. Then phase 04 builds "Choose from Google Drive" and D1 decides dropping `spreadsheets` scope.
No-go (M3 fails "File not found"/403) = hide Drive card; keep Share-in + Paste button + Recent on `spreadsheets` scope.

## Unresolved
- M11 multi-account: not run (owner, or defer to phase 04).
- Picked spreadsheet had 3 tabs / 1002 rows: confirm it was the throwaway sheet; delete marker row `passchef-picker-spike` if not.
