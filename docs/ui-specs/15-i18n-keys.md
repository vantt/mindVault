# 15 — i18n Keys Catalogue (Audit v2.4)

## Methodology

Audit thực hiện bằng cách:
1. Đọc toàn bộ `_locales/en/messages.json` và `_locales/vi/messages.json`
2. Grep `data-i18n=` / `data-i18n-title=` / `data-i18n-placeholder=` trong mọi HTML
3. Grep `chrome.i18n.getMessage(` trong mọi JS/TS
4. So sánh 3 tập hợp: EN keys, VI keys, used-in-code keys

**Counts (sau v2.4 cleanup):** 94 EN keys · 94 VI keys · 94 used in code · **0 unused** · 0 EN/VI gaps · 6 hardcoded còn lại (profile card emoji-prefix labels — đã wire i18n nhưng giữ emoji)

### Changelog v2.4

- ➕ Added 7 new keys (popup gaps): `hintSetupPrompt`, `placeholderSheetUrl`, `btnCopy`, `btnBack`, `lblCopied`, `errNoProfile`, `profilesSummary`
- ➖ Deleted 7 orphan/YAGNI keys: `btnUnlockSettings`, `hintUnlock`, `errSheetMismatch`, `hintSheetUrlAuto`, `noSheetTabsOpen`, `profileFallback`, `warnLegacyNoTag`
- 🔌 Wired 12 previously-unused keys: `btnEditSecrets`, `btnExportProfile`, `btnRemoveProfile`, `btnSetDefault`, `errProfileAssigned`, `exportNextStep`, `exportWizardTitle`, `lblImportedFrom`, `lblSheetLocked`, `lblUsedBySheets`, `migrationNotice`, `placeholderSecret`
- 🔌 Wired existing-but-unattached: `btnStartSetup`, `btnLock` (on generated screen), `lblMasterPassword` (unlock placeholder)
- 🛠️ Extended `localizeHtml()` in both `popup.js` and `options.js` to handle `data-i18n-placeholder` attribute

---

## (A) Active Keys — defined EN+VI, used in code

Sorted alphabetically. EN value shown; VI exists for all.

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
| `importWizardTitle` | Import Shared Profile |
| `lblConfirmNewPassword` | Confirm New Password |
| `lblConfirmPassword` | Confirm Password |
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

**Total: 73 active keys**

---

## (B) Unused Keys

**None.** All 94 defined keys are referenced in code (either via static `data-i18n*` / `getMessage('literal')` or via dynamic `getMessage(variable)` lookup such as `SECTION_STATUS[id].i18nKey`).

After v2.4 YAGNI cleanup, the 5 keys previously "reserved for future" were deleted. Re-add them in messages.json when surfacing these features:
- `errSheetMismatch` — for profile routing error UI
- `warnLegacyNoTag` — for pre-v2.2 recipe decode warning
- `profileFallback` — for fallback routing label
- `hintSheetUrlAuto` — for auto-fill confirmation hint
- `noSheetTabsOpen` — for sheet-detector empty-state

### Note on `status*` keys

`statusSetup`, `statusLocked`, `statusUnlocked`, `statusReady`, `statusBuilder` and `btnCancel` appear "unused" via static grep but ARE active through dynamic lookup (e.g. `SECTION_STATUS[id].i18nKey` → `getMessage(cfg.i18nKey)`).

---

## (C) Missing Translations — EN/VI gaps

**None.** Both locale files have identical key sets (93 keys each). No asymmetric keys detected.

---

## (D) Hardcoded Strings — i18n gaps in HTML/JS

**Status after v2.4:** All 12 prominent popup gaps from the previous audit have been wired. Remaining hardcoded items are tightly coupled to UI emoji prefixes or low-priority error fallback strings.

### Remaining hardcoded items (acceptable)

| Location | Hardcoded Text | Reason kept |
|---|---|---|
| `options-profiles-tab.js` button labels | `'✏️ '`, `'↑ '`, `'★ '` emoji prefixes | Combined with i18n value at render (e.g., `` `✏️ ${getMessage('btnEditSecrets')}` ``) |
| `popup.js` error hint paths | `"⚠️ Reload the tab..."`, `"No recipe found — ..."`, `` `Error: ${...}` ``, `` `⚠️ ${e.message}` `` | Error-only surface, future i18n |
| `popup.js` pepper hint | `"🔑 Don't forget your pepper!"` | Optional UI hint, future i18n |
| `content.js:104` fallback | `"Recipe verification failed — ..."` | Fallback when SW doesn't return localized message; `errRecipeMismatch` is the proper key (already exists & wired) |
| `popup.js` profile label prefix | `"Profile: "` and `"📥 "` literals in `genProfileLabel.textContent` | Composite string — future i18n with placeholder format |

**Total: ~6 remaining hardcoded items (down from 12). All are deferrable.**

---

## Files Modified in v2.4

- ✅ `chrome-extension/src/popup/popup.html` — added `data-i18n` / `data-i18n-placeholder` to 7 elements
- ✅ `chrome-extension/src/popup/popup.js` — extended `localizeHtml()` for placeholder support; i18n'd profiles summary + copy button
- ✅ `chrome-extension/src/popup/popup-recipe-builder.js` — i18n'd `(no profile)` and `Copied!`
- ✅ `chrome-extension/src/options/options.html` — added `data-i18n-placeholder="placeholderSecret"` to 5 secret inputs
- ✅ `chrome-extension/src/options/options.js` — extended `localizeHtml()` for placeholder support; wired migration notice
- ✅ `chrome-extension/src/options/options-profiles-tab.js` — wired 8 keys (button labels, card details, delete guard)
- ✅ `chrome-extension/src/options/options-export-wizard.js` — wired `exportWizardTitle`, `exportNextStep`
- ✅ `chrome-extension/src/_locales/en/messages.json` — added 7 keys, removed 2 orphans
- ✅ `chrome-extension/src/_locales/vi/messages.json` — same delta with VI translations

## Future Work

5 truly unused keys reserved for future UI features:
- Surface `errSheetMismatch` in shared profile routing error UI
- Surface `warnLegacyNoTag` toast in content script after decoding pre-v2.2 recipes
- Surface `profileFallback` in popup when fallback routing kicks in
- Wire `hintSheetUrlAuto` to display after auto-detect succeeds
- Wire `noSheetTabsOpen` in sheet-detector picker for empty-state
