# Phase 04 — Connect-Menu wizard + mapping

## Context links
- Spec §3.3, §3.4, §3.8, CM1–CM5, D1/D9/D11 · audit #1, #4, #6, #7, #10, #21 · phase 01 results

## Overview
- Priority: P1 (owner's main complaint). Status: Done (2026-11-10); real Picker pick/refresh/re-pick + Share-in + M11 need owner on device — [report 08](reports/08-implementation-verification.md). Depends 02. Phase 01 = GO: `drive.file` only, `spreadsheets` scope dropped (D1).
- Replace `SourceScreen` + inline `MappingScreen` with 4-step wizard: Choose source → Choose tab → Map columns → Review & save.

## Key insights
- Pick > paste. Recent Menus = zero-scope repeat path. Share-in removes URL hunting.
- Mapping must speak Sheets language: letters + header names, role chips, suggestions proposed not confirmed.

## Requirements
- Functional: CM1 chooser (Drive★ primary, Recent, Share how-to, Paste w/ clipboard button, Offline); Paste + Share-in parse ID then open Picker filtered to that ID (`PICKER_FILE_IDS`), never read without a pick; refresh = stored ID + silent `authorize`; lost access (404/403) → filtered re-pick; Picker cancel (ApiException 16) → calm retry; scope migration: remove `spreadsheets` from request + `AuthorizationGrantPolicy`, existing Menus show "Choose this sheet again" once (filtered Picker), cache kept meanwhile; M11 multi-account check; share intent prefill (D11); CM2 tab list w/ row counts; CM3 table w/ letters, headers, role sheet, header toggle, advanced start row, swap duplicate roles; CM4 review; CM5 offline w/o Sheet ID (D9); drift → CM3 reconfirm banner; every error inline.
- Non-functional: limits 20/1000/32/4096; tokens RAM; wizard draft in route holder (phase 02); keep `source-id`, `source-table`, `column-<ROLE>`, `confirm-columns`.

## Architecture
`ConnectMenuWizard` (step state in holder) → `CookbookSources` (existing load/preview/confirm) → `MenuSnapshotStore`. Picker path: `AndroidSheetsAuthorization` → `pickMenu(fileIdFilter?)` (drive.file, opt-out granted, CONSENT, PICKER_OAUTH_TRIGGER/MIMETYPES/FILE_IDS; returns `picked_file_ids`) + `authorizeSilently()` (drive.file); `spreadsheets` scope removed. Reference: debug spike `proof/drive-picker-spike-activity.kt`. Offline: local origin id generator in shared models.

## Related code files
- Modify: `cookbook/SourceScreen.kt` (→ removed or KT2 detail), `cookbook/MappingScreen.kt` (→ CM3), `cookbook/CookbookSources.kt` (step API, no invalidate on keystroke), `cookbook/AndroidSheetsAuthorization.kt`, shared `AuthorizationGrantPolicy.kt` (scope → drive.file), `AndroidManifest.xml` (ACTION_SEND filter), shared `MenuModels.kt` (local origin id, D9)
- Create: `connect/connect-menu-chooser-step.kt`, `connect/choose-sheet-tab-step.kt`, `connect/column-mapping-table-step.kt`, `connect/column-role-picker-sheet.kt`, `connect/review-and-save-step.kt`, `connect/offline-table-paste-step.kt`, `connect/shared-sheet-link-intent-parser.kt`

## Implementation steps
1. Step state + navigation in holder. 2. CM1 (Drive card behind flag). 3. Paste w/ clipboard-on-tap + inline validation. 4. Share intent → unlock → CM1 prefilled. 5. CM2. 6. CM3 table + role sheet + auto-detect display. 7. CM4. 8. CM5 w/o ID. 9. Error map §3.8. 10. Recent Menus refresh path.

## Todo
- [x] state  - [x] CM1  - [x] paste  - [x] share-in  - [x] CM2  - [x] CM3  - [x] CM4  - [x] CM5  - [x] errors  - [x] recent refresh  - [x] Drive path  - [x] scope migration (drop spreadsheets)  - [x] docs: mobile-sheets-client, oauth runbook, permissions-review

## Success criteria
New Menu connected without typing a URL (Drive or share-in); repeat refresh = 1 tap; mapping completed without typing numbers; zero silent failures; consent round-trip keeps step.

## Risk assessment
`column-<ROLE>` proofs type digits → role field accepts digits/letters. Local origin id changes cache key semantics → migration test for existing caches. Share-in on cold start → locked first; keep prefill in holder.

## Security considerations
Clipboard read only on explicit tap. Share-in payload parsed for ID only, never logged. Only `drive.file` (non-sensitive); `spreadsheets` removed. Picker is Google UI outside FLAG_SECURE — warn in CM1 copy. Menu cache stays labeled "not encrypted".

## Next steps
Docs: mobile-sheets-client, oauth runbook, permissions review (if D1).
