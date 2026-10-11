# 15 — i18n Keys Catalogue (Audit v2.5)

## Methodology

Audit thực hiện bằng cách:
1. Đọc toàn bộ `_locales/en/messages.json` và `_locales/vi/messages.json`
2. Grep `data-i18n=` / `data-i18n-title=` / `data-i18n-placeholder=` trong mọi HTML
3. Grep `chrome.i18n.getMessage(` trong mọi JS/TS
4. So sánh 3 tập hợp: EN keys, VI keys, used-in-code keys

**Historical v2.5 count:**120 EN/VI keys. Current rendered-demo audit below supersedes that snapshot; unused keys are not inferred solely from equal locale counts.

### Changelog v2.5

- ➕ Added 26 new keys for popup UX redesign (Quick Start, Home states, Builder micro-copy, More options, Demo page)
- VI translations for all 26 new keys added in Phase 7
- No keys deleted in this version

---

## (A) Historical v2.4 key snapshot

This retained snapshot explains older UI wording. Current strings and usage must be checked in the runtime locale files and source; the table does not certify current usage.

| Key | EN Value |
|---|---|
| `appDescription` | Generate passwords from recipes stored in Google Sheets |
| `appName` | PassChef Password Generator |
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

Historical v2.5 list. The current rendered demo uses the larger catalogue below; runtime values may supersede these examples.

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

## Current feedback and safe rendering

The authoritative strings are `chrome-extension/src/_locales/{en,vi}/messages.json`.
`loadLocale()` selects the stored preference; HTML markers and `t()` calls share it.
Normal popup feedback and demo localization assign text, titles and placeholders
without interpreting messages as markup. The popup bundle tooltip intentionally
renders curated locale HTML; that trusted rich-tooltip exception is separate from
provider/error feedback and user-supplied profile/Menu names.

| Surface | Current keys and behavior |
|---|---|
| Home | `hintEmptyCell`, `hintInvalidRecipe`, `hintSecretNotFound`, `hintNeedsReload`, `hintCellError`, `errRecipeMismatch` select a safe visible outcome. `lblProfile`, `hintPepperReminder` localize the generated view. |
| Builder | `errNoProfile`, `errBuilderPreview`, `errRecipeTag`, `errRecipeCopy`, `errRecipeMismatch`, `hintSecretNotFound`, `securityCancelled` cover cooking/tag/copy failures. Provider details are not displayed. |
| Menu routing | `hintProfileFromMapping`, `hintProfileUnmapped`, `hintSheetUrlAuto`, `warnNoSheetUrl`, `warnLegacyNoTag` explain mapping, detection and intentional untagged Recipes. |
| Unlock | `securityWrongPassword`, `securityLockedOut`, `securityBusy`, `securityCancelled`, `securityInvalidData`, `securityPasswordRequired`, `securityCryptoUnavailable`, `securityUnlocking` describe authority results and retry countdown. |
| Dish copy | `securityClipboardUnavailable`, `lblCopied` distinguish an awaited successful clipboard request from failure. Clipboard cleanup is an attempt, not an unconditional promise. |

The Builder error/warning DOM uses `textContent`; its `Copy Recipe` success label
appears only after the real clipboard promise resolves. The selected Menu cannot
silently produce an untagged Recipe after a tag failure.
Content presentation loads extension-local locale data and selects safe public
feedback; it does not display arbitrary worker/provider exception strings.

## Catalogue boundaries

Equal EN/VI key counts do not prove translation completeness or runtime usage.
Legacy keys may remain for compatibility; an unused-key inventory must inspect
both HTML markers and runtime calls before removal. `warnLegacyNoTag` and
`hintSheetUrlAuto` are currently wired Builder behavior, not reserved future work.
Symbols, deterministic Recipe examples, the wordmark and ingredient length numerals
are language-neutral; visible explanatory prose and image alt text are localized.

The guide opens from extension pages with `chrome.runtime.getURL()` and
`chrome.tabs.create()`. It requires no `web_accessible_resources` expansion.
Demo styling uses local CSS and system font fallbacks. Other screens may still use
shared typography; this bounded demo slice does not certify all extension screens offline.

## Verification boundary

`test/unit/demo-locale.spec.mjs` verifies all74 rendered guide keys in EN/VI,
text-safe substitution and absence of unmarked explanatory text or remote demo
resources. Builder locale/lifetime tests independently cover safe feedback;
actual popup integration and native clipboard behavior require their headed suites.
A completed locale catalogue is not evidence that every legacy UI flow passed.

## Current demo locale audit — 2026-10-12

The standalone demo renders74 distinct locale keys (including its image alt).
The exact EN/VI catalogue is in [16-demo-page.md](16-demo-page.md#rendered-keys).
Runtime `messages.json` files are the source of strings; `loadLocale()` honors the
stored language preference. Both locale sets must contain every rendered key.
`data-i18n` uses safe text substitution and `data-i18n-alt` uses attribute assignment;
no localized message is inserted with innerHTML. Recipe code/section numbers and
the PassChef wordmark remain language-neutral.

Historical demoTagline/demoBackLink and former key names may remain for compatibility;
the guide uses demoCloseTab for both wired close controls and demoDailyUseTitle
for its final section. Current tests cover actual rendered text and not merely
matching overall key counts. Popup/content feedback keys are reconciled separately
with their finished phase1–3 handlers; this catalogue does not certify live Google.
