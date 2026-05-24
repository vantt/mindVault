# 15 — i18n Keys Catalogue (Audit v2.5)

## Methodology

Audit thực hiện bằng cách:
1. Đọc toàn bộ `_locales/en/messages.json` và `_locales/vi/messages.json`
2. Grep `data-i18n=` / `data-i18n-title=` / `data-i18n-placeholder=` trong mọi HTML
3. Grep `chrome.i18n.getMessage(` trong mọi JS/TS
4. So sánh 3 tập hợp: EN keys, VI keys, used-in-code keys

**Counts (after v2.5):** 120 EN keys · 120 VI keys · **0 unused** · 0 EN/VI gaps

### Changelog v2.5

- ➕ Added 26 new keys for popup UX redesign (Quick Start, Home states, Builder micro-copy, More options, Demo page)
- VI translations for all 26 new keys added in Phase 7
- No keys deleted in this version

---

## (A) Active Keys — defined EN+VI, used in code

Sorted alphabetically. EN value shown; VI exists for all (v2.4 keys) or TBD (v2.5 additions).

| Key | EN Value |
|---|---|
| `appDescription` | Generate passwords from recipes stored in Google Sheets |
| `appName` | mindVault Password Generator |
| `backupConfirm` | I have backed up my master password and secrets manually. |
| `backupConfirmNew` | I have backed up my NEW master password. |
| `btnBuildRecipe` | Build Recipe |
| `btnBuilderBack` | Back |
| `btnCancel` | Cancel |
| `btnChangePassword` | Change Password |
| `btnCopyRecipe` | Copy Recipe |
| `btnDetectRefreshTitle` | Re-detect Sheets tabs |
| `btnImportProfile` | Import Shared |
| `btnLock` | Lock |
| `btnNewProfile` | New Profile |
| `btnSafeChanges` | Save Changes |
| `btnStartSetup` | Start Setup |
| `btnUnlock` | Unlock |
| `btnUpdatePassword` | Update Password |
| `builderHash` | Hash |
| `builderModifiers` | Modifiers |
| `builderPasswordPreview` | REAL PASSWORD |
| `builderPosition` | Position |
| `builderProfile` | Profile |
| `builderRecipeOutput` | Recipe |
| `builderSecret` | Secret |
| `builderSheetUrl` | Target Sheet ID |
| `changePwdDesc` | Re-encrypt your secrets with a new master password. |
| `changePwdTitle` | Change Master Password |
| `detectedNTabs` | Detected $1 Sheets tabs — pick one: |
| `errHashAscii` | Only ASCII letters and digits (a-z, A-Z, 0-9). Unicode is unreliable across devices. |
| `errInvalidSheetUrl` | Not a valid Sheet ID or URL |
| `errRecipeMismatch` | Recipe mismatch — was this recipe built for a different sheet or profile? |
| `hintClickCell` | Click any cell in Google Sheets containing a recipe. |
| `hintModifierFlip` | `_` Flip position #↔$$ (no effect on @ % ^). One hash+secret pair → two passwords. |
| `hintModifierReverse` | `?` Reverse the secret string before combining |
| `hintModifierStrip` | `~` Strip non-alphanumeric chars from secret |
| `hintModifierUpper` | `!` Uppercase the secret before combining |
| `hintPositionAt` | `@` Middle — insert secret into the middle of hash |
| `hintPositionCaret` | `^` Interleave 2-by-2 — alternate two chars from each |
| `hintPositionDollar` | `$$` Suffix — hash + secret |
| `hintPositionHash` | `#` Prefix — secret + hash |
| `hintPositionPercent` | `%` Interleave 1-by-1 — alternate one char from each |
| `hintProfileFromMapping` | Auto-selected from sheet mapping |
| `hintProfileUnmapped` | Sheet not mapped — using default. Map in Options for stable behavior. |
| `importWizardTitle` | Import Shared Profile |
| `lblConfirmNewPassword` | Confirm New Password |
| `lblConfirmPassword` | Confirm Password |
| `lblCopied` | Copied! |
| `lblCurrentPassword` | Current Password |
| `lblMasterPassword` | Master Password |
| `lblNewPassword` | New Password |
| `lblPepperingHint` | Show "Don't forget pepper" hint |
| `lblSettings` | Settings |
| `manageSecretsDesc` | Configure your 5 secret phrases. These are combined with your recipes. |
| `manageSecretsTitle` | Manage Secrets |
| `profilesAssignTitle` | Sheet Assignments |
| `profilesOwnTitle` | My Profiles |
| `profilesSharedTitle` | Shared Profiles |
| `profilesSummary` | Profiles: $1 own · $2 shared |
| `setupDesc` | Create a strong master password to encrypt your secrets. This is the only way to access your data. |
| `setupTitle` | Setup Master Password |
| `statusBuilder` | Recipe Builder |
| `statusLocked` | Locked |
| `statusReady` | Ready |
| `statusSetup` | Setup Required |
| `statusUnlocked` | Unlocked |
| `tabProfiles` | Profiles |
| `tabSecrets` | Secrets |
| `tabSettings` | Settings |
| `toastPwdMismatch` | Passwords do not match |
| `toastPwdShort` | Password too short |
| `toastPwdUpdated` | Password Updated Successfully! |
| `toastSaveSuccess` | Secrets Saved! |
| `toastSetupComplete` | Setup Complete! |
| `toastUnlockSuccess` | Unlocked! |
| `unlockDesc` | Enter your master password to manage secrets. |
| `unlockTitle` | Unlock Extension |
| `warnNoSheetUrl` | Without target sheet, this recipe won't verify on decode. |

**Total v2.4 active keys: 94** (including all keys through `profilesSummary`)

---

## (A2) New Keys — v2.5 Popup UX Redesign

All 26 new keys added and wired. EN + VI translations complete.

### Home Screen — Contextual States (Phase 02)

| Key | EN Value | VI Value |
|---|---|---|
| `hintEmptyCell` | Selected cell is empty or has no recipe. | Ô được chọn trống hoặc không có Công thức. |
| `hintNeedsReload` | Extension needs tab reload. | Cần tải lại tab để kích hoạt tiện ích. |
| `hintSheetContext` | 📄 $1 (placeholder) | 📄 $1 (placeholder) |
| `hintCellError` | Could not read cell — check sheet permissions. | Không thể đọc ô — kiểm tra quyền truy cập trang tính. |
| `hintPepperReminder` | 🔑 Don't forget your pepper! | 🔑 Đừng quên pepper của bạn! |
| `lblProfile` | Profile | Hồ sơ |

### Builder Screen — Micro-copy (Phase 03)

| Key | EN Value | VI Value |
|---|---|---|
| `builderSubtitleHash` | The text part of your recipe | Phần văn bản của Công thức |
| `builderSubtitlePosition` | Where your secret goes | Vị trí đặt secret |
| `builderSubtitleSecret` | Which secret phrase to use | Secret nào sẽ được dùng |
| `builderSubtitleModifiers` | Optional transformations | Biến đổi tùy chọn |
| `builderSubtitleSheetId` | Optional — links recipe to a specific sheet | Tùy chọn — liên kết Công thức với một trang tính cụ thể |
| `moreOptions` | More options | Thêm tùy chọn |
| `linkHowItWorks` | ? How it works | ? Cách hoạt động |

### Home Screen — Quick Start Panel (Phase 05)

| Key | EN Value | VI Value |
|---|---|---|
| `quickStartTitle` | How it works | Cách hoạt động |
| `quickStartStep1` | Build a recipe | Tạo một Công thức |
| `quickStartStep2` | Paste it into a Google Sheets cell | Dán vào ô trong Google Sheets |
| `quickStartStep3` | Click the cell → password appears | Nhấp vào ô → mật khẩu xuất hiện |
| `btnGotIt` | Got it ✓ | Đã hiểu ✓ |
| `linkLearnMore` | Learn more → | Tìm hiểu thêm → |
| `btnCreateFirstRecipe` | Create First Recipe | Tạo Công thức đầu tiên |

### Demo Page (Phase 06)

| Key | EN Value | VI Value |
|---|---|---|
| `demoTagline` | Turn recipes into passwords | Biến Công thức thành mật khẩu |
| `demoIdeaTitle` | The Idea | Ý tưởng |
| `demoAnatomyTitle` | Anatomy of a Recipe | Cấu trúc Công thức |
| `demoModifiersTitle` | Modifiers | Biến đổi |
| `demoDailyUseTitle` | Daily Use | Sử dụng hàng ngày |
| `demoBackLink` | ← Back to extension | ← Quay lại tiện ích |

**Total v2.5 new keys: 26**
**Total after v2.5: 120 active keys**

---

## (B) Unused Keys

**None.** All defined keys are referenced in code (either via static `data-i18n*` / `getMessage('literal')` or via dynamic `getMessage(variable)` lookup).

**Note on v2.5 new keys:** Keys in section A2 are defined in spec and will be wired in Phases 2–7. They should be added to `messages.json` in Phase 7 (i18n implementation phase).

Previously deleted keys (re-add when surfacing features):
- `errSheetMismatch` — for profile routing error UI
- `warnLegacyNoTag` — for pre-v2.2 recipe decode warning
- `profileFallback` — for fallback routing label
- `hintSheetUrlAuto` — for auto-fill confirmation hint
- `noSheetTabsOpen` — for sheet-detector empty-state

---

## (C) Missing Translations — EN/VI gaps

**v2.4 keys:** No gaps. Both locale files have identical key sets.

**v2.5 keys:** All 26 new keys have complete VI translations (added Phase 7).

**Vietnamese note for demo page:** Use "Công thức" (recipe/formula) — NOT "Công thức nấu ăn" (cooking recipe).

---

## (D) Hardcoded Strings — i18n gaps in HTML/JS

**Status after v2.5:** Same remaining items as v2.4 — no new hardcoded strings introduced.

### Remaining hardcoded items (acceptable)

| Location | Hardcoded Text | Reason kept |
|---|---|---|
| `options-profiles-tab.js` button labels | `'✏️ '`, `'↑ '`, `'★ '` emoji prefixes | Combined with i18n value at render |
| `popup.js` pepper hint | `"🔑 Don't forget your pepper!"` | Optional UI hint, future i18n |
| `content.js:104` fallback | `"Recipe verification failed — ..."` | Fallback when SW doesn't return localized message |
| `popup.js` profile label prefix | `"Profile: "` and `"📥 "` literals | Composite string — future i18n with placeholder format |

**Total: ~4 remaining hardcoded items. All are deferrable.**

---

## Files to Modify in v2.5

When implementing (Phases 2–7), update these files:

- `chrome-extension/src/popup/popup.html` — add new DOM elements (`#home-quick-start`, `#home-sheet-context`, `#home-notice`, `#btn-how-it-works`, `#bld-more-options` `<details>`, `.builder-subtitle` paragraphs)
- `chrome-extension/src/popup/popup.js` — Quick Start logic, state routing changes, sheet context display
- `chrome-extension/src/popup/popup-recipe-builder.js` — smart defaults load/save, More Options auto-expand
- `chrome-extension/src/popup/popup.css` — `.builder-subtitle`, `#home-notice`, `#home-sheet-context`, `.notice.warning` styles
- `chrome-extension/src/demo/demo.html` — new demo page (Phase 6)
- `chrome-extension/src/_locales/en/messages.json` — add 26 new keys
- `chrome-extension/src/_locales/vi/messages.json` — add 26 new keys with VI translations (Phase 7)
- `chrome-extension/manifest.json` — declare `demo/demo.html` as web-accessible resource

## Future Work

5 truly unused keys reserved for future UI features:
- Surface `errSheetMismatch` in shared profile routing error UI
- Surface `warnLegacyNoTag` toast in content script after decoding pre-v2.2 recipes
- Surface `profileFallback` in popup when fallback routing kicks in
- Wire `hintSheetUrlAuto` to display after auto-detect succeeds
- Wire `noSheetTabsOpen` in sheet-detector picker for empty-state
