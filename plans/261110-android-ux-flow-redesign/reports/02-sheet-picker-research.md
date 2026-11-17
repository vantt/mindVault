# Android: pick Google Sheet without URL/ID paste

Date: 2026-11-10. Scope: research only, no code changed.

## 1. Current state (repo)

- Auth: Play Services `Identity.getAuthorizationClient` (`app/.../cookbook/AndroidSheetsAuthorization.kt:36`), one scope `AuthorizationGrantPolicy.SHEETS_SCOPE` = `https://www.googleapis.com/auth/spreadsheets` (`mobile/sheets-client/.../LoopbackOAuth.kt:70` same value for desktop smoke tool).
- API: direct Sheets v4 (`sheets.googleapis.com/v4/spreadsheets`, `SheetsClient.kt:24`). Read + append/update only. No Drive API calls.
- Menu select today = paste URL/ID, then pick tab (`CookbookSources.kt` flow: granted -> metadata -> choose tab -> confirm mapping).
- Privacy gap: `docs/specs/mobile-sheets-client.md` says scope "is not limited to that selected Menu". `spreadsheets` = read/write all sheets of account.
- Runbook `docs/runbooks/google-oauth-sheets-setup.md`: External, test users. Spec edge case: grant dies ~7 days while app not verified/published.
- No CASA / restricted-scope plan in `docs/commercialization/legal/permissions-review.md` (only extension host perms and data notes).
- Extension: no Sheets/Drive API, no Picker. Reads Recipe via content script DOM on `docs.google.com/spreadsheets/*`; `popup/sheet-detector.js` lists open Sheets tabs. Not portable to Android (no DOM access).
- `docs/specs/qr-handoff.md`: extension QR is sender-only, encrypted cookbook bundle, Drive transport out of scope.

## 2. Scope facts (cited)

Drive API scope table (developers.google.com/drive/api/guides/api-specific-auth):
- `drive.file`: NON-SENSITIVE. Covers files user opens with app, files shared with app via Picker or app's own picker, files app creates.
- `drive.install`: non-sensitive, only for Drive "Open with"/"New" menu.
- `drive.readonly`, `drive.metadata`, `drive.metadata.readonly`, `drive`: RESTRICTED.
- Sheets scopes: NOT verified (Sheets scope page fetched lacked table). `spreadsheets` assumed SENSITIVE (inference; confirm in OAuth verification FAQ).

CASA / restricted verification (developers.google.com/identity/protocols/oauth2/production-readiness/restricted-scope-verification):
- Security assessment needed when restricted-scope data stored/transmitted on servers. Reverify at least every 12 months.
- Client-only exemption NOT stated. Forum thread (discuss.google.dev, client-only Android + drive.readonly) unresolved in search.
- Sensitive scope = standard verification (no CASA). Restricted = Trust & Safety review + CASA.

Embedded WebView: Google blocks OAuth in embedded WebView (`disallowed_useragent`, developers.googleblog.com "upcoming security changes ... embedded webviews"). Use Custom Tab / system browser only.

## 3. Options

### A. Drive files.list, mimeType=spreadsheet
- UX: silent list of all sheets, pick one. Best UX on paper.
- Scope: `drive.metadata.readonly` (or `drive.readonly`) = RESTRICTED. Lists ALL Drive file metadata.
- Verification: restricted review + CASA (if server touches restricted data); yearly reverify. Client-only exemption unclear.
- Privacy: worst. Full Drive inventory visible to app. Conflicts with privacy-first brand.
- Effort: low code, high process cost.
- Risk: review rejection/delay, scary consent screen, owner trust.
- VERDICT: REJECT.

### B. Google Picker via Play Services, scope drive.file (primary)
- UX: tap "Choose Menu" -> Play Services consent -> Picker opens in browser tab (Custom Tab/new tab) filtered to spreadsheets -> pick -> return to app with `picked_file_ids`.
- Android mechanics (developers.google.com/workspace/drive/picker/guides/desktop-mobile-picker): `AuthorizationRequest` with `PICKER_OAUTH_TRIGGER` resource param; scope `drive.file` only (cannot combine); `setOptOutIncludingGrantedScopes(true)`; IDs in `getTokenResponseParams` bundle key `picked_file_ids`. No native Android Picker SDK found.
- Picker can filter by MIME type, specific file IDs, folders.
- Scope: `drive.file` = non-sensitive. No CASA. Probably no sensitive review (verify).
- Privacy: best. App sees only picked Menu(s). Narrows current `spreadsheets` grant.
- Effort: medium. Replace scope, add picker launch + result parse, keep Sheets client unchanged, re-consent for owner.
- Risks:
  1. UNPROVEN: does drive.file token allow `spreadsheets.values.get/append` on picked file? Sheets add-on dev reported "File not found" on picked files (community thread groups.google.com/g/google-apps-script-community/c/t09YQZakCLs). MUST spike.
  2. Browser hop UX; wrong Google account in browser.
  3. Grant persistence across reinstall/sign-out/new device unknown.
  4. Picker API must be enabled in GCP; per-platform OAuth clients.
- VERDICT: PRIMARY (after spike).

### C. drive.file + Picker
- Same as B. This IS the recommended combo, not a separate option.

### D. Android intents

D1. SAF `ACTION_OPEN_DOCUMENT` via Drive DocumentsProvider:
- Returns `content://` URI. No source confirms native Google Sheets yields readable bytes or Drive fileId. Likely export-only.
- SAF grant gives no Sheets API token.
- VERDICT: REJECT.

D2. Share target (Sheets app -> Share -> PassChef):
- Sheets sends `text/plain` link. PassChef `ACTION_SEND` filter parses ID. No scope needed to receive.
- BUT: drive.file does NOT cover a file merely linked/shared by URL (only opened via app, Picker, or app picker). Share alone -> 403 under drive.file.
- Works as SHORTCUT: extract ID -> open Picker filtered to that file ID -> user taps Select -> grant. One tap instead of hunt.
- VERDICT: FALLBACK/SHORTCUT. Cheap.

D3. Drive "Open with" (Drive UI integration):
- Requires `drive.install` + Drive UI integration config. Open URL receives `state` with `ids`/`exportIds` (developers.google.com/drive/integrate-open, search snippet only).
- No Android-specific evidence. Docs web-focused.
- VERDICT: SKIP v1; investigate later.

### E. QR from extension
- Desktop shows QR of active Menu ID/URL; phone scans (new Android camera reader).
- Gives NO access. Phone still needs grant (Picker/scope). Only removes typing.
- Privacy: ID alone low risk, but Menu is private.
- Effort: medium (camera permission, reader, QR decode).
- VERDICT: SKIP v1. Optional later if desktop-first users.

### F. Recent memory + clipboard
- Recent: app already stores `snapshots` with `recent`/`favorites` (`CookbookSources.kt`). Show previously connected Menus = one tap, zero scope.
- Clipboard: explicit "Paste link" button reading `ClipboardManager` on tap. Avoid background clipboard reads (Android 10+ restricts; no auto-detect).
- Effort: low. No scope or verification cost.
- VERDICT: FALLBACK + repeat-use path. Keep existing paste as escape hatch.

## 4. Recommendation

Primary: B (Picker + `drive.file`, opt-out of granted scopes) for first connect. Save picked ID into existing snapshot. Later syncs use Sheets API with same token; if grant lost, re-pick with ID filter (one tap).

Fallback combo:
1. Recent Menus list (F1) for repeat use.
2. Share target (D2) pre-fills Picker ID filter.
3. Paste URL/ID + paste button (F2) when Play Services or Picker unavailable.

Skip for v1: A (restricted/CASA), D1 (SAF), D3 (Open with, unverified), E (QR, no access value).

Gate before build (spike on real device, owner account):
- drive.file token -> `spreadsheets.values.get` on picked sheet = 200.
- append/update on picked sheet works.
- Non-picked sheet -> 403 maps to ACCESS_DENIED -> prompt re-pick.
- Grant survives app restart; test reinstall and sign-out.

Doc impact if adopted: update `mobile-sheets-client.md` (scope, R-rules, "not limited to selected Menu" edge case), `google-oauth-sheets-setup.md` (Picker API, drive.file, Android client), `permissions-review.md` (narrowed scope; no restricted scope = no CASA). Consider publishing to production only after non-sensitive scopes confirmed (also removes 7-day Testing expiry if it applies - verify).

Sources:
- https://developers.google.com/workspace/drive/picker/guides/desktop-mobile-picker?hl=en
- https://developers.google.com/drive/api/guides/api-specific-auth
- https://developers.google.com/identity/protocols/oauth2/production-readiness/restricted-scope-verification
- https://developers.google.com/workspace/sheets/api/guides/authorizing
- https://developers.googleblog.com/en/upcoming-security-changes-to-googles-oauth-20-authorization-endpoint-in-embedded-webviews/ (search snippet)
- https://discuss.google.dev/t/does-a-client-only-android-app-with-no-server-need-the-casa-security-assessment-for-drive-readonly/393638 (search snippet, unresolved)
- https://groups.google.com/g/google-apps-script-community/c/t09YQZakCLs (community, unofficial)
- https://developers.google.com/drive/integrate-open (search snippet)

## Unresolved questions

1. Does `drive.file` token authorize Sheets `values` read/append on picked file? Needs spike.
2. Exact Android Picker trigger param, Custom Tab vs browser behavior, developer key need.
3. Is `spreadsheets` SENSITIVE or RESTRICTED? Sheets scope page not fetched.
4. Does `drive.file` grant survive reinstall, sign-out, device change?
5. Can Android Picker flow pre-filter to one file ID from share intent? (docs say filter supported; Android path unverified)
6. Are native Google Sheets readable via SAF/Drive provider? (likely no)
7. Is Play Services a hard requirement? (app already uses Identity API, so likely yes)
8. Client-only CASA exemption for restricted scopes - moot if B adopted, unresolved otherwise.
9. Does 7-day Testing-status refresh expiry apply to `drive.file` tokens too?
