# UI Specs — PassChef Chrome Extension

**Version:** 2.1 (Revised)
**Date:** 2026-05-17
**Status:** Draft — Ready for Implementation
**PRD Reference:** [../prd/v2-multi-sheet-profiles.md](../prd/v2-multi-sheet-profiles.md)

UI specs được tách theo từng màn hình, đánh số theo **thứ tự user journey**: popup states (01–05) → options page (06–08) → modals (09–12) → cross-cutting (13–15).

---

## Changelog v2.1

- Export Wizard: 6 bước → 3 bước (bỏ Tier 2/3/4)
- Thêm Sheet ID field vào Export Wizard (sheet-bound generation)
- Bỏ expiry countdown khỏi Shared Profile card
- Bỏ error state "expired bundle"
- Consumer không cần manual assign sheet (sheetId locked trong bundle)

---

## Changelog v2.4 (2026-05-22)

- Reverse-engineered tất cả 15 specs từ code thực tế (2 wave Sonnet agents)
- Đánh số lại theo user journey: popup states (01-05) → options (06-08) → modals (09-12) → cross-cutting (13-15)
- Thêm 4 spec mới: first-setup, unlock, home, generated screen
- i18n cleanup: +7 keys mới, -2 orphan, wire 12 keys đã có sẵn (xem 15-i18n-keys.md)
- Total: 99 EN keys · 99 VI keys (perfect sync) · ~94 used · 5 truly unused (reserved future)

---

## Screen Index

### Popup States (first user contact)

| # | File | Khi nào hiện |
|---|------|--------------|
| 01 | [popup-first-setup.md](./01-popup-first-setup.md) | Lần đầu cài extension (chưa có `salt`) |
| 02 | [popup-unlock.md](./02-popup-unlock.md) | Đã setup, chưa có session (frequent) |
| 03 | [popup-home.md](./03-popup-home.md) | Sau unlock, không phải tab Sheets — main hub |
| 04 | [popup-generated.md](./04-popup-generated.md) | Auto-route khi mở trên Sheets tab có recipe cell |
| 05 | [recipe-builder.md](./05-recipe-builder.md) | `[Build Recipe]` từ Home hoặc Generated |

### Options Page

| # | File | Mô tả |
|---|------|-------|
| 06 | [options-tab-navigation.md](./06-options-tab-navigation.md) | Tab bar (Secrets / Profiles / Settings) |
| 07 | [secrets-tab.md](./07-secrets-tab.md) | Secrets tab (v1 — unchanged) |
| 08 | [profiles-tab.md](./08-profiles-tab.md) | Profiles tab — My / Shared / Assignments |

### Modals (triggered từ Profiles tab)

| # | File | Trigger |
|---|------|---------|
| 09 | [export-wizard.md](./09-export-wizard.md) | `[Export]` trên own profile (3 bước) |
| 10 | [import-wizard.md](./10-import-wizard.md) | `[+ Import Profile]` (3 bước) |
| 11 | [edit-secrets-modal.md](./11-edit-secrets-modal.md) | `[Edit Secrets]` trên profile card |
| 12 | [new-profile-modal.md](./12-new-profile-modal.md) | `[+ New Profile]` |

### Cross-cutting & Reference

| # | File | Mô tả |
|---|------|-------|
| 00 | [design-tokens.md](./00-design-tokens.md) | Colors, fonts, spacing — shared across all screens |
| 13 | [error-states.md](./13-error-states.md) | Sheet mismatch, fallback, edge cases |
| 14 | [migration-notice.md](./14-migration-notice.md) | v1 → v2 migration toast |
| 15 | [i18n-keys.md](./15-i18n-keys.md) | i18n key catalogue (v2 mới) |

---

## File Changes Summary (Implementation)

| File | Change type | Description |
|------|-------------|-------------|
| `options.html` | Modify | Tab bar, Profiles tab HTML, modal shells |
| `options.css` | Modify | Tab styles, profile card styles, modal overlay |
| `options.js` | Modify | Tab switching, profile CRUD, 3-step export/import wizard |
| `popup.html` | Modify | Profile name line in generated state |
| `popup.js` | Modify | Show profile name from service worker response |
| `_locales/vi/messages.json` | Modify | New i18n keys |
| `_locales/en/messages.json` | Modify | New i18n keys |

---

## Unresolved Questions

1. **Owner setup UX**: Wizard phải nhắc Owner rõ ràng rằng họ cần set up account passwords dùng DS-generated (không phải S-generated). Step 3 đã có warning — đủ chưa?
2. **"Use current sheet" button**: Yêu cầu `activeTab` permission + content_script messaging. Nếu không có GSheets tab active → disable với tooltip. Có worth it không hay bỏ?
3. **Import channel**: Paste + upload file (cả hai) hay chỉ paste?
4. **Multiple sheets per bundle**: Enforce 1 bundle per sheet hay chỉ document?
5. **Hardcoded English strings**: Popup error hints, button labels, master password placeholder — nhiều chỗ chưa có i18n key. Cần bổ sung trong `_locales/*/messages.json` cho VN localisation.
