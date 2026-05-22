# 13 — Error States

Cross-cutting errors xuất hiện trên Popup, Options page, hoặc content script overlay.

---

## Overview: Error surfaces

| Surface | Component | Presentation |
|---|---|---|
| Popup inline hint | `#gen-hint` (red `#da3633`) | Inline text, không dismiss |
| Builder inline error | `#bld-password-out` / `#bld-hash-error` / `#bld-sheet-url-error` (.error class) | Inline text |
| Content script toast | `showErrorToast()` | Red overlay anchored near cell, auto-dismiss 3s |
| Options page toast | `showToast(msg, isError=true)` | Inline toast, auto-dismiss 3s |
| Unlock error | `#unlock-error` (popup) / options toast | Inline text |

---

## 9.1 Fallback to default profile (unassigned sheet)

**Trigger:** Sheet không có trong `sheetMapping` và không có shared profile khớp sheetId. `resolveProfileForSheet()` trả về default.

**Behavior:** Silent fallback. Không blocking. Password vẫn được sinh.

**Popup hiển thị:** Không có error message. Profile indicator hiển thị tên profile thực tế (default).

**i18n:** `profileFallback` — `"Using default profile"` (hiện chưa được dùng trong popup UI; label hiển thị tên profile trực tiếp từ `response.profileName`).

---

## 9.2 Recipe/profile mismatch (verification tag fail)

**Trigger:** Recipe có verification tag (`.xxxx`) và tag không khớp với (recipe, secret, sheetId) → `RecipeProfileMismatchError` (code: `RECIPE_MISMATCH`).

**Content script path:**
```
⚠️ Recipe mismatch — was this recipe built for a different sheet or profile?
```
- Red toast, auto-dismiss 3s, anchored near cell.
- i18n: `errRecipeMismatch` — `"Recipe mismatch — was this recipe built for a different sheet or profile?"`

**Popup builder path:** `#bld-password-out` shows `response.error` với `.error` class (màu đỏ).

---

## 9.3 Sheet mismatch on shared profile

**Trigger:** `errSheetMismatch` i18n key xuất hiện trong Options profiles tab (khi shared profile bị dùng sai sheet).

**Displayed string:**
```
This profile is locked to a different sheet.
```
- i18n: `errSheetMismatch` — `"This profile is locked to a different sheet."`
- Không phải GENERATE_PASSWORD error path — dùng trong profiles UI logic.

---

## 9.4 Profile assigned to sheets (deletion guard)

**Trigger:** User cố xoá profile đang được assign trong `sheetMapping`.

**Displayed string:**
```
Profile is assigned to sheets. Remove assignments first.
```
- i18n: `errProfileAssigned` — `"Profile is assigned to sheets. Remove assignments first."`
- Surface: Options page toast (isError=true).

---

## 9.5 Unlock: wrong password

**Trigger:** Web Crypto `OperationError` khi decrypt với sai master password.

**Popup:** `#unlock-error` — `"Invalid Password"` (hardcoded)
**Options:** `showToast("Invalid Password", true)` (hardcoded)

---

## 9.6 Connection errors (content script not loaded)

**Trigger:** `chrome.tabs.sendMessage` throws khi content script chưa load hoặc extension reloaded.

**Popup `#gen-hint` displays:**
- Extension context invalidated / Could not establish connection / Receiving end does not exist →
  `"⚠️ Reload the tab to activate the extension."` (hardcoded)
- Lỗi khác → `"⚠️ <e.message>"` (hardcoded)
- Color: `#da3633`

**Content script path (hotkey):**
- `"Extension reloaded. Please refresh this page."` (hardcoded) via `showErrorToast()`

---

## 9.7 Empty cell / no recipe found

**Trigger:** `extractRecipeText()` trả về empty string, content script gửi `{ success: false, error: "Empty cell" }`.

**Popup `#gen-hint`:**
```
No recipe found — select a cell with a recipe, then re-open.
```
(hardcoded)

---

## 9.8 Recipe parse errors

**Trigger:** `parser.parse()` trả về null hoặc `Recipe.validate()` throws.

**Content script toast:** `"⚠️ Invalid recipe format"` (hardcoded, sau khi `msg.includes("Invalid recipe")` check)
**Other errors from response.error** → `"⚠️ <msg>"` prefixed

**Popup `#gen-hint`:**
```
Error: <response.error> ("<extractedText>")
```
(hardcoded, màu `#da3633`)

---

## 9.9 Secret not found

**Trigger:** `getSecretForGeneration()` trả về null (index không tồn tại).

**Content script toast:** `"⚠️ Secret not found (Check Options)"` (hardcoded, sau `msg.includes("not found")` check)

---

## 9.10 Builder: hash validation

**Trigger:** Hash field chứa ký tự không phải ASCII alphanumeric.

**Display:** `#bld-hash-error` inline:
```
Only ASCII letters and digits (a-z, A-Z, 0-9). Unicode is unreliable across devices.
```
- i18n: `errHashAscii`
- `.invalid` class trên hash input

---

## 9.11 Builder: invalid sheet URL / ID

**Trigger:** Sheet URL field có nội dung nhưng không match URL regex hoặc bare ID regex.

**Display:** `#bld-sheet-url-error` inline:
```
Not a valid Sheet ID or URL
```
- i18n: `errInvalidSheetUrl`
- Recipe output và password output đều hiển thị `—` (muted) khi URL typed nhưng invalid.

---

## 9.12 Builder: no sheet URL (warning, not error)

**Trigger:** Sheet URL field trống.

**Display:** `#bld-sheet-warning` (warning style, không phải error):
```
Without target sheet, this recipe won't verify on decode.
```
- i18n: `warnNoSheetUrl`

---

## 9.13 Builder: minor errors

- **No profile** (dropdown rỗng): `#bld-password-out` → `"(no profile)"` (hardcoded, `.error` class)
- **Storage quota** (options, change-password): toast → `"Storage quota exceeded. Delete unused profiles first."` (hardcoded)

---

## i18n Reference

| Key | Actual message | Used |
|---|---|---|
| `errSheetMismatch` | `"This profile is locked to a different sheet."` | Options profiles tab |
| `errProfileAssigned` | `"Profile is assigned to sheets. Remove assignments first."` | Options profiles tab |
| `profileFallback` | `"Using default profile"` | Not rendered in current UI |
| `errRecipeMismatch` | `"Recipe mismatch — was this recipe built for a different sheet or profile?"` | Content script toast |
| `errHashAscii` | `"Only ASCII letters and digits (a-z, A-Z, 0-9)…"` | Builder hash field |
| `errInvalidSheetUrl` | `"Not a valid Sheet ID or URL"` | Builder sheet URL field |
| `warnNoSheetUrl` | `"Without target sheet, this recipe won't verify on decode."` | Builder sheet warning |
| `warnLegacyNoTag` | `"Legacy recipe (no verification tag)."` | Key exists; render path unconfirmed |

**Hardcoded (no i18n key):** `"Invalid Password"`, `"Reload the tab to activate the extension."`, `"Extension reloaded. Please refresh this page."`, `"No recipe found — select a cell with a recipe, then re-open."`, `"Secret not found (Check Options)"`, `"Invalid recipe format"`, `"(no profile)"`, `"Storage quota exceeded. Delete unused profiles first."`, `"Missing data. Reset in Options."`, `"No data found. Reset in Options."`
