# Phase 01 — Picker spike: drive.file + Sheets API on device

## Context links
- [02 picker research](reports/02-sheet-picker-research.md) · spec §3.3, D1 · `docs/runbooks/google-oauth-sheets-setup.md` · `docs/specs/mobile-sheets-client.md`

## Overview
- Priority: P1 (gates "Pick from Google Drive"). Status: **Done — GO (owner 2026-11-10)**, M1–M10 PASS; M11 multi-account deferred to phase 04. See reports/07-picker-spike-results.md.
- Throwaway debug-only proof on physical phone + owner test account. Answer: can PassChef pick a Menu without paste and still read it?

## Key insights
- `drive.file` = non-sensitive, sees only picked files. Restricted scopes (drive.metadata.readonly) rejected (CASA, privacy).
- Android Picker = Play Services `AuthorizationRequest` w/ Picker trigger; result `picked_file_ids`. No native SDK; browser/Custom Tab hop.
- Unproven: Sheets `values.get` with drive.file token on picked file (community "File not found" reports).

## Requirements
- Functional: pick 1 spreadsheet → fileId returned → `spreadsheets.get` + `values.get` 200 with drive.file-only token; append on picked file (future authoring); non-picked file → 403.
- Non-functional: debug source set only (`app/src/debug/...`), no release behavior change, token RAM-only, no logs of token/content.

## Architecture
Debug proof Activity → AuthorizationClient (drive.file, Picker param, opt-out granted scopes) → parse fileIds → existing `:sheets-android` client reads with returned token. Record results in report.

## Related code files
- Create: `app/src/debug/kotlin/dev/passchef/mobile/proof/drive-picker-spike-activity.kt` (debug only)
- Read-only reference: `cookbook/AndroidSheetsAuthorization.kt`, `shared/.../AuthorizationGrantPolicy.kt`, `SheetsClient.kt`
- Modify: none in main source set.

## Implementation steps
1. GCP: enable Picker API; confirm Android OAuth client; add `drive.file` to consent screen (Testing).
2. Build debug spike; launch Picker filtered to `application/vnd.google-apps.spreadsheet`.
3. Run matrix: pick→read, pick→append (test sheet), unpicked→403, app restart, reinstall, sign-out/in, second account in browser.
4. Test share-in shortcut: Picker pre-filtered to one fileId.
5. Write `reports/07-picker-spike-results.md`: pass/fail per row, timings, screenshots w/o secrets.

## Todo
- [x] GCP config  - [x] spike Activity  - [~] matrix run (M1–M7 pass)  - [x] fileId-filter test  - [x] results report  - [x] owner go/no-go (Go, drop `spreadsheets`)

## Success criteria
Go = read 200 + append 200 on picked file, 403 on unpicked, grant survives restart. No-go → spec fallback (Share-in + Paste + Recent on `spreadsheets` scope).

## Risk assessment
- Picker opens wrong browser account → document; offer account hint. - Testing-mode 7-day expiry confuses results → note dates. - Sensitive/verification process cost → legal review.

## Security considerations
Non-sensitive scope narrows access vs current `spreadsheets`. Never request restricted scopes. Spike token never persisted/logged. Test sheet contains synthetic Recipes only.

## Next steps
Go → phase 04 Drive path + docs scope update + D1 migration decision (drop `spreadsheets`?). No-go → hide Drive card.
