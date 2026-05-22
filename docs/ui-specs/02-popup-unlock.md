# 02 — Unlock Screen (Popup)

Reverse-engineered từ [`popup.html`](../../chrome-extension/src/popup/popup.html) `#status-locked` và [`popup.js`](../../chrome-extension/src/popup/popup.js) lines 95–222.

**Triggered khi:** `salt` tồn tại trong `chrome.storage.sync` (setup đã hoàn tất) nhưng `sessionKey` **không có** trong `chrome.storage.session` (session hết hạn hoặc browser vừa mở lại).

**Precondition:** [01-popup-first-setup.md](./01-popup-first-setup.md) đã hoàn tất.  
**Next screen:** [03-popup-home.md](./03-popup-home.md) (sau khi unlock thành công).

---

## Layout

```
┌──────────────────────────────────────────┐
│ 🔐 mindVault            🔴 Locked    ⚙️  │
│ ──────────────────────────────────────── │
│                                          │
│  [Master Password____________________]   │
│                                          │
│  [           Unlock                  ]   │
│                                          │
│  ⚠ Invalid Password                      │  ← hidden unless error
│                                          │
└──────────────────────────────────────────┘
```

**Header pill:** red dot (`🔴`) + text "Locked" (`statusLocked` i18n). `active: false` → pill không highlight.

---

## DOM Elements

| ID | Type | Notes |
|----|------|-------|
| `#status-locked` | `<div class="hidden">` | Section container; `hidden` class removed khi active |
| `#unlock-password` | `<input type="password">` | Placeholder: `"Master Password"` (hardcoded, không có `data-i18n`) |
| `#btn-unlock` | `<button class="btn primary">` | Label mặc định: `"Unlock"` (hardcoded, không có `data-i18n`) |
| `#unlock-error` | `<p class="hint">` | `style="display:none"` khi không có lỗi; text điền động |

**Wrapper:** `#status-locked` chứa một `<div class="unlock-container">` bao quanh cả 3 elements trên.

---

## Form Behavior

### Input

- Type: `password` — ký tự bị mask mặc định
- Placeholder: `"Master Password"` (hardcoded, không có `data-i18n`)
- **Enter key submits:** `keydown` listener → `if (e.key === 'Enter') handleUnlock()`

### Button States

| State | Label | Trigger |
|-------|-------|---------|
| Default | `"Unlock"` | Lúc load |
| Processing | `"Unlocking..."` | Ngay khi `handleUnlock` bắt đầu (trước storage call) |
| Reset (error) | `"Unlock"` | Khi bất kỳ error path nào trả về |

### Empty password

`if (!password) return;` — no-op, không hiện lỗi, không disable button.

---

## Unlock Flow

```
User submits password
        │
        ▼
btnUnlock.textContent = "Unlocking..."
unlockError.style.display = 'none'
        │
        ▼
chrome.storage.sync.get(["salt", "defaultProfile"])
        │
        ├─ salt missing ──► "Missing data. Reset in Options."  [STOP]
        │
        ▼
argon2.deriveKey(password, new Uint8Array(salt))
  • salt lấy ra là plain Array → convert bằng new Uint8Array(salt)
  • Argon2id, hashLen=32, time=3, mem=65536 (64 MB), parallelism=4
  • Returns JWK (JSON Web Key)
        │
        ▼
profileKey = `profile:${defaultProfile || "Default"}`
chrome.storage.sync.get([profileKey, "encryptedData", "iv"])
  verifyData = profileData  ──(fallback)──►  { encryptedData, iv }
        │
        ├─ verifyData.encryptedData missing ──► "No data found. Reset in Options."  [STOP]
        │
        ▼
crypto.subtle.importKey("jwk", derivedKey, AES-GCM, non-extractable, ["decrypt"])
crypto.subtle.decrypt({ name: "AES-GCM", iv: new Uint8Array(verifyData.iv) }, key,
                       new Uint8Array(verifyData.encryptedData))
  ← verification only; result discarded
        │
        ├─ e.name === "OperationError"          ┐
        ├─ e.message?.includes("OperationError")┘─► "Invalid Password"  [STOP]
        ├─ other exception ──► e.message || e.name || "An error occurred"  [STOP]
        │
        ▼
chrome.storage.session.set({ sessionKey: derivedKey })
        │
        ▼
window.location.reload()  →  popup reinit  →  sessionKey found  →  Home screen
```

**Note:** Toàn bộ flow nằm trong một `try/catch` duy nhất; chỉ hai error paths trước `deriveKey` là early-return explicit, các lỗi còn lại đều rơi vào `catch`.

---

## Error States

| Condition | Message displayed |
|-----------|------------------|
| Password field empty | (no-op — silent) |
| `salt` không có trong storage | `"Missing data. Reset in Options."` |
| `verifyData.encryptedData` không có | `"No data found. Reset in Options."` |
| Sai mật khẩu (`OperationError`) | `"Invalid Password"` |
| Lỗi khác | `e.message \|\| e.name \|\| "An error occurred"` |

Error element: `#unlock-error` (`.hint` class). Hiện bằng `style.display = 'block'`; ẩn khi submit mới bằng `style.display = 'none'`.

**OperationError detection** (code thực tế, không chỉ `e.name`):
```js
const isWrongPassword = e?.name === "OperationError" || e?.message?.includes("OperationError");
```
Chrome đôi khi set `e.message = ""` (empty) cho DOMException từ Web Crypto — check kép đảm bảo catch đủ cả hai case.

---

## Argon2 Parameters

| Param | Value | Notes |
|-------|-------|-------|
| `type` | `Argon2id` | |
| `hashLen` | `32` | 256-bit AES-GCM key |
| `time` | `3` | iterations |
| `mem` | `65536` | KiB = 64 MB |
| `parallelism` | `4` | |

Nguồn: [`argon2_adapter.js`](../../chrome-extension/src/adapters/infrastructure/argon2_adapter.js).

---

## Security Notes

- **Master password không được lưu** — chỉ dùng để derive key trong RAM, bị GC sau function kết thúc.
- **`sessionKey` (JWK)** lưu trong `chrome.storage.session` — tự xóa khi browser đóng, không persist.
- **Decrypt là verification-only** — `crypto.subtle.decrypt` chỉ dùng để xác nhận key đúng; kết quả bị discard ngay.
- Argon2id params (time=3, mem=64MB, parallelism=4) khiến brute-force trong popup không khả thi.
- **Lock action:** `chrome.storage.session.remove("sessionKey")` + `window.close()` — wired vào cả `#btn-lock` (Home screen) và `#btn-lock-gen` (Generated screen); quay về màn hình này lần sau.
- **`salt` serialisation:** `salt` được lưu dưới dạng plain JSON Array trong `chrome.storage.sync`; phải wrap lại bằng `new Uint8Array(salt)` trước khi truyền vào Argon2.

---

## i18n Keys

| Key | EN value | VI value | Usage |
|-----|----------|----------|-------|
| `statusLocked` | `"Locked"` | _(not checked — key exists)_ | Header status pill text |
| `unlockTitle` | `"Unlock Extension"` | `"Mở khóa Tiện ích"` | Defined but NOT wired to DOM |
| `unlockDesc` | `"Enter your master password to manage secrets."` | `"Nhập mật khẩu chủ để quản lý bí mật."` | Defined but NOT wired to DOM |
| `btnUnlock` | `"Unlock"` | `"Mở khóa"` | Defined but NOT wired to DOM (`#btn-unlock` dùng hardcoded text) |
| `hintUnlock` | `"Unlock to generate passwords."` | _(key exists)_ | Defined but NOT wired to DOM |
| `toastUnlockSuccess` | `"Unlocked!"` | _(key exists)_ | Defined, usage unclear (not in popup.js unlock flow) |

**⚠ DOM/i18n gap:** Tất cả error strings trong `handleUnlock` là hardcoded English — không có i18n binding. Nếu cần localisation, các chuỗi sau phải được đưa vào `messages.json` VÀ wired vào DOM:
- `"Missing data. Reset in Options."`
- `"No data found. Reset in Options."`
- `"Invalid Password"`
- `"An error occurred"`
- Placeholder `"Master Password"` trên input (key có thể dùng: key hiện tại `"Master Password"` ở dòng 17 en/messages.json nhưng chưa có `name`)
- Button labels `"Unlock"` / `"Unlocking..."` (`btnUnlock` key tồn tại nhưng chưa wired; `"Unlocking..."` hoàn toàn thiếu)

---

## Related Screens

- [01-popup-first-setup.md](./01-popup-first-setup.md) — setup screen (precondition; shown khi chưa có `salt`)
- [03-popup-home.md](./03-popup-home.md) — home/unlocked screen (destination sau unlock thành công)
- [00-design-tokens.md](./00-design-tokens.md) — shared colors, typography, border-radius
