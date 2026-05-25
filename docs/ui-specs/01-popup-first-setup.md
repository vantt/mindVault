# 01 — First Setup Screen (Popup)

Reverse-engineered từ [`popup.html`](../../chrome-extension/src/popup/popup.html) (`#status-setup`) và [`popup.js`](../../chrome-extension/src/popup/popup.js).

**Triggered bởi:** Không tìm thấy `salt` trong `chrome.storage.sync` → `showSection('status-setup')` (popup.js:95–98).
**Scope:** Màn hình đầu tiên người dùng thấy sau khi cài extension, trước khi tạo master password.

---

## Layout

```
┌──────────────────────────────────────────┐
│ 🔐 PassChef             🔴 Setup Required  ⚙️ │
│ ─────────────────────────────────────── │
│                                          │
│  Create a master password to start       │
│  using PassChef.                        │
│                                          │
│  [        Start Setup        ]           │
│                                          │
└──────────────────────────────────────────┘
```

**Header pill:** `#global-status` — dot class `red` + text từ i18n `statusSetup` ("Setup Required"). `active: false` → pill không có `.active` class (muted styling). Pill được set bởi `showSection()` thông qua `SECTION_STATUS` map (popup.js:66–72).

**Hint text:** hardcoded string, không có `data-i18n` — `"Create a master password to start using PassChef."` (popup.html:23).

**CTA button:** `#btn-start-setup` `.btn.primary` — label `"Start Setup"` hardcoded (không có `data-i18n`) (popup.html:24).

**Settings icon:** `#btn-settings` luôn visible, cũng gọi `openOptionsPage()` (popup.html:18).

---

## State Logic

```
DOMContentLoaded
    │
    ▼
chrome.storage.sync.get("salt")   (popup.js:95)
    │
    ├─ salt missing ──► showSection('status-setup')  ← THIS SCREEN
    │
    └─ salt present
           │
           ├─ sessionKey missing ──► status-locked (02)
           │
           └─ sessionKey present ──► status-unlocked / status-generated (03)
```

---

## CTA Behaviour

| Element | Event | Action |
|---------|-------|--------|
| `#btn-start-setup` | `click` | `chrome.runtime.openOptionsPage()` |
| `#btn-settings` | `click` | `chrome.runtime.openOptionsPage()` |

Cả hai button đều mở options page. Không có inline form trên popup — toàn bộ setup được thực hiện trên Options page.

---

## Master Password Creation (Options Page)

Khi options page mở mà chưa có `salt`, `checkStatus()` (options.js:58–77) gọi `showSection(setupSection)`, hiển thị `#setup-section` (options.html:17).

- **i18n'd elements trong form:**
  - `<h2 data-i18n="setupTitle">` — "Setup Master Password"
  - `<p data-i18n="setupDesc">` — "Create a strong master password…"
  - `<label data-i18n="lblMasterPassword">` — cho `#setup-password`
  - `<label data-i18n="lblConfirmPassword">` — cho `#setup-confirm`
  - `<label data-i18n="backupConfirm">` — cho `#setup-backup-confirm`
  - `<button data-i18n="btnStartSetup">` — submit button

- **Form fields:**
  - `#setup-password` — Master Password (`type="password"`, `required`, `minlength="8"`)
  - `#setup-confirm` — Confirm Password (`type="password"`, `required`, `minlength="8"`)
  - `#setup-backup-confirm` — Checkbox: "I have backed up my master password and secrets manually." (`required`)
  - Submit: `[Start Setup]` (i18n `btnStartSetup`)

- **On submit (options.js:113–138):**
  1. Validate passwords match → toast i18n `toastPwdMismatch` (error) nếu không khớp
  2. Validate min length ≥ 8 → toast i18n `toastPwdShort` (error) nếu quá ngắn
  3. Validate backup checkbox → toast hardcoded `"Please confirm backup"` (error) nếu chưa tick
  4. Toast hardcoded `"Setting up…"` (in-progress, không có i18n key)
  5. Generate `salt = crypto.getRandomValues(new Uint8Array(16))`
  6. Derive `sessionKey = await argon2.deriveKey(pwd, salt)` (Argon2Adapter)
  7. Encrypt initial secrets object via `encryptWithKey(initial, sessionKey)` → `encrypted`
  8. Persist to `chrome.storage.sync`: `{ salt: Array.from(salt), "profile:Default": encrypted, defaultProfile: "Default", sheetMapping: {} }`
  9. Persist to `chrome.storage.session`: `{ sessionKey }`
  10. Toast i18n `toastSetupComplete` ("Setup Complete!")
  11. `loadSecrets()` → `showSection(dashboardSection)` (mở dashboard, Secrets tab active mặc định)

  > **Lưu ý:** Không có flat `encryptedData`/`iv` ở top-level storage nữa — data nằm trong key `"profile:Default"` dạng object `{ encryptedData, iv }`.

- Spec đầy đủ cho Options page: xem [`06-options-tab-navigation.md`](./06-options-tab-navigation.md) và [`07-secrets-tab.md`](./07-secrets-tab.md).

---

## i18n Keys

### Popup (`#status-setup`)

| Key | English | Used in |
|-----|---------|---------|
| `statusSetup` | Setup Required | Header status pill text |

> Hint text và button label trong popup.html `#status-setup` là hardcoded — không có `data-i18n`.

### Options Page (`#setup-section`)

| Key | English | Used in |
|-----|---------|---------|
| `setupTitle` | Setup Master Password | Section heading `<h2>` |
| `setupDesc` | Create a strong master password to encrypt your secrets. This is the only way to access your data. | Description paragraph |
| `lblMasterPassword` | Master Password | Label cho `#setup-password` |
| `lblConfirmPassword` | Confirm Password | Label cho `#setup-confirm` |
| `backupConfirm` | I have backed up my master password and secrets manually. | Checkbox label |
| `btnStartSetup` | Start Setup | Submit button |
| `toastSetupComplete` | Setup Complete! | Success toast sau khi setup |
| `toastPwdMismatch` | Passwords do not match | Error toast — passwords không khớp |
| `toastPwdShort` | Password too short | Error toast — password < 8 chars |

**i18n gaps (hardcoded strings trong setup flow):**
- `"Setting up…"` — in-progress toast (options.js:121)
- `"Please confirm backup"` — validation error nếu checkbox chưa tick (options.js:119)
- `"Setup failed: " + err.message` — error toast khi setup throw (options.js:137)

Cả EN lẫn VI đều có đủ các key trên (kiểm tra `_locales/en/messages.json` và `_locales/vi/messages.json`).

---

## Related Screens

- **Next (sau setup):** [`03-popup-home.md`](./03-popup-home.md) — Home / Hub (popup landing khi có session)
- **Cùng flow, options page:** [`06-options-tab-navigation.md`](./06-options-tab-navigation.md)
- **Design tokens:** [`00-design-tokens.md`](./00-design-tokens.md)
