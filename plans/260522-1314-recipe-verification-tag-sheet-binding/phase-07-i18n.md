# Phase 07 — i18n (en + vi)

## Context Links
- `chrome-extension/src/_locales/en/messages.json`
- `chrome-extension/src/_locales/vi/messages.json`
- Parent plan: `plan.md`

## Overview
- **Priority:** P2
- **Status:** completed
- **Description:** Add i18n keys for new mismatch error, legacy warning, builder sheet URL label/error/warning.

## Key Insights
- Existing convention: camelCase keys, `message` field. Some have `description` for translators.
- Vietnamese translations must match tone of existing messages (informal, concise).

## Requirements
**Functional**
New keys (both `en` and `vi`):

| Key | en | vi |
|---|---|---|
| `errRecipeMismatch` | Recipe mismatch — was this recipe built for a different sheet or profile? | Recipe không khớp — recipe này được tạo cho sheet hoặc profile khác? |
| `errInvalidSheetUrl` | Invalid Google Sheets URL | URL Google Sheets không hợp lệ |
| `warnNoSheetUrl` | Without target sheet, this recipe won't verify on decode. | Không có sheet đích, recipe này sẽ không xác minh được khi giải mã. |
| `warnLegacyNoTag` | Legacy recipe (no verification tag). | Recipe cũ (không có verification tag). |
| `builderSheetUrl` | Target Sheet URL | URL Sheet đích |
| `hintSheetUrlAuto` | Auto-filled from current tab. Edit if targeting a different sheet. | Tự động điền từ tab hiện tại. Sửa nếu nhắm đến sheet khác. |

## Related Code Files
**Modify:**
- `chrome-extension/src/_locales/en/messages.json`
- `chrome-extension/src/_locales/vi/messages.json`

## Implementation Steps
1. Add 6 keys to `en/messages.json` with `message` + `description` fields.
2. Mirror keys in `vi/messages.json` with Vietnamese translations.
3. Verify JSON valid (no trailing commas).

## Todo List
- [x] Append 6 keys to en messages
- [x] Append 6 keys to vi messages
- [x] JSON-validate both files (`node -e "JSON.parse(require('fs').readFileSync(...))"`)
- [x] Verify `chrome.i18n.getMessage("errRecipeMismatch")` returns expected string in extension reload

## Success Criteria
- Both locale files valid JSON.
- Content script toast on mismatch shows localized message based on browser locale.
- Builder shows localized warning when sheetId blank.

## Risk Assessment
- **R1:** Translation drift between en/vi. Mitigation: keep messages short, technical terms in English where conventional (e.g., "Sheet", "profile").
- **R2:** Missing key fallback silently returns "" in `chrome.i18n.getMessage`. Mitigation: all callers use `|| 'English fallback'` pattern as already done in `popup-recipe-builder.js`.

## Security Considerations
- N/A.

## Next Steps
- Phase 08 unit tests can mock `chrome.i18n.getMessage`.

## Unresolved
- None.
