# Phase 07 — i18n (EN + VI)

**Status:** complete
**Effort:** ~1h
**Dependency:** Phases 02–06 complete (all new keys finalized)

---

## Context Links

- `docs/ui-specs/15-i18n-keys.md` — updated catalogue (v2.5 from Phase 01)
- `chrome-extension/src/_locales/en/messages.json` — 94 keys currently
- `chrome-extension/src/_locales/vi/messages.json` — 94 keys currently

---

## Overview

Add all new i18n keys introduced by this redesign to both locale files. Keys must be added in both files simultaneously — no EN/VI gaps permitted (catalogue rule: 0 asymmetric keys).

Vietnamese note: use "Công thức" (neutral) for recipe/formula references on the demo page. Do NOT use "Công thức nấu ăn" (cooking recipe).

---

## New Keys by Phase

### Phase 02 — Home contextual states

| Key | EN value | VI value |
|-----|---------|---------|
| `hintEmptyCell` | "Selected cell is empty or has no recipe." | "Ô được chọn trống hoặc không có Công thức." |
| `hintNeedsReload` | "Extension needs tab reload." | "Cần tải lại tab để kích hoạt tiện ích." |
| `hintSheetContext` | "📄 $1" | "📄 $1" |
| `hintCellError` | "Could not read cell — check sheet permissions." | "Không thể đọc ô — kiểm tra quyền truy cập trang tính." |
| `hintPepperReminder` | "🔑 Don't forget your pepper!" | "🔑 Đừng quên pepper của bạn!" |
| `lblProfile` | "Profile" | "Hồ sơ" |

Note: `hintSheetContext` uses a `$1` placeholder for the sheet name (same pattern as `detectedNTabs`). Add `placeholders` object in messages.json.

### Phase 03 — Builder redesign

| Key | EN value | VI value |
|-----|---------|---------|
| `builderSubtitleHash` | "The text part of your recipe" | "Phần văn bản của Công thức" |
| `builderSubtitlePosition` | "Where your secret goes" | "Vị trí đặt secret" |
| `builderSubtitleSecret` | "Which secret phrase to use" | "Secret nào sẽ được dùng" |
| `builderSubtitleModifiers` | "Optional transformations" | "Biến đổi tùy chọn" |
| `builderSubtitleSheetId` | "Optional — links recipe to a specific sheet" | "Tùy chọn — liên kết Công thức với một trang tính cụ thể" |
| `moreOptions` | "More options" | "Thêm tùy chọn" |
| `linkHowItWorks` | "? How it works" | "? Cách hoạt động" |

### Phase 05 — Quick Start panel

| Key | EN value | VI value |
|-----|---------|---------|
| `quickStartTitle` | "How it works" | "Cách hoạt động" |
| `quickStartStep1` | "Build a recipe" | "Tạo một Công thức" |
| `quickStartStep2` | "Paste it into a Google Sheets cell" | "Dán vào ô trong Google Sheets" |
| `quickStartStep3` | "Click the cell → password appears" | "Nhấp vào ô → mật khẩu xuất hiện" |
| `btnGotIt` | "Got it ✓" | "Đã hiểu ✓" |
| `linkLearnMore` | "Learn more →" | "Tìm hiểu thêm →" |
| `btnCreateFirstRecipe` | "Create First Recipe" | "Tạo Công thức đầu tiên" |

### Phase 06 — Demo page

| Key | EN value | VI value |
|-----|---------|---------|
| `demoTagline` | "Turn recipes into passwords" | "Biến Công thức thành mật khẩu" |
| `demoIdeaTitle` | "The Idea" | "Ý tưởng" |
| `demoAnatomyTitle` | "Anatomy of a Recipe" | "Cấu trúc Công thức" |
| `demoModifiersTitle` | "Modifiers" | "Biến đổi" |
| `demoDailyUseTitle` | "Daily Use" | "Sử dụng hàng ngày" |
| `demoBackLink` | "← Back to extension" | "← Quay lại tiện ích" |

---

## messages.json Format

Standard key format (no placeholder):
```json
"hintEmptyCell": {
  "message": "Selected cell is empty or has no recipe."
}
```

Key with placeholder (`$1`):
```json
"hintSheetContext": {
  "message": "📄 $1",
  "placeholders": {
    "1": { "content": "$1", "example": "Budget 2026" }
  }
}
```

---

## Implementation Steps

1. Open `chrome-extension/src/_locales/en/messages.json`
2. Append all new EN keys in logical groups (Phase 02 together, Phase 03 together, etc.)
3. Open `chrome-extension/src/_locales/vi/messages.json`
4. Append all new VI keys in the same groups
5. Verify key counts match (EN count == VI count)
6. Update `docs/ui-specs/15-i18n-keys.md` section (A) active keys table and bump count

---

## Existing Keys to Reuse (No New Key Needed)

| Usage | Existing key |
|-------|-------------|
| Builder "Back" button | `btnBuilderBack` |
| Copy Recipe flash | `lblCopied` |
| "Build Recipe" CTA | `btnBuildRecipe` |
| Builder status pill | `statusBuilder` |
| "Lock" buttons | `btnLock` |

---

## i18n Audit Update

After adding new keys, update `docs/ui-specs/15-i18n-keys.md`:
- Bump version label to **v2.5**
- Add new keys to section (A) Active Keys table
- Update total count (94 + 26 new = **120 active keys**)
- Note any keys added to messages.json but not yet wired to HTML (future-reserved) in section (B)

---

## Todo List

- [x] Add Phase 02 keys to `en/messages.json` (6 keys)
- [x] Add Phase 03 keys to `en/messages.json` (7 keys)
- [x] Add Phase 05 keys to `en/messages.json` (7 keys)
- [x] Add Phase 06 keys to `en/messages.json` (6 keys)
- [x] Mirror all 26 keys in `vi/messages.json` with VI translations
- [x] Verify EN key count == VI key count (no asymmetry) — 120 == 120
- [x] Update `docs/ui-specs/15-i18n-keys.md` — v2.5, new count

---

## Success Criteria

- Both locale files have identical key sets after update
- All keys used by HTML `data-i18n` attributes or JS `getMessage()` calls exist in messages.json
- No hardcoded English strings remain in popup.js, popup.html, popup-recipe-builder.js, demo.js for features introduced in this redesign
- `chrome.i18n.getMessage()` returns non-empty string for every new key in both EN and VI contexts
- `15-i18n-keys.md` accurately reflects the full key set

---

## Unresolved Questions

None — all key names and values agreed above. VI translations can be adjusted by native speaker if phrasing is off; functional correctness takes priority.
