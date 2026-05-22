# 11 — Edit Secrets Modal (Profile-scoped)

Triggered bởi `[✏️ Secrets]` trên own profile card (`openEditSecretsModal` trong `options-profiles-tab.js`).
Cũng tự mở sau khi tạo profile mới — xem [12-new-profile-modal.md](./12-new-profile-modal.md).

## DOM IDs

| Element | ID / selector |
|---|---|
| Modal overlay | `#modal-edit-secrets` |
| Title heading | `#edit-secrets-title` |
| Inputs container | `#edit-secrets-inputs` (innerHTML injected at open) |
| Save button | `#btn-save-modal-secrets` |
| Cancel / × | `.modal-close[data-modal="modal-edit-secrets"]` |

## Layout

```
┌─── Edit Secrets: Banking ──────────────── × ┐
│                                              │
│  Secret #1  [_______________] 👁             │
│  Secret #2  [_______________] 👁             │
│  Secret #3  [_______________] 👁             │
│  Secret #4  [_______________] 👁             │
│  Secret #5  [_______________] 👁             │
│                                              │
│  [Cancel]                    [Save Changes] │
└──────────────────────────────────────────────┘
```

## Rules

### Title
- Set bằng JS: `edit-secrets-title.textContent = "Edit Secrets: " + profileName`
- Không dùng `data-i18n` — hardcoded EN string (i18n gap)

### Fields
- **Cố định 5 trường** (`Array.from({length: 5}, …)` trong `openEditSecretsModal`)
- Mỗi field: `<input type="password" class="modal-secret-input" data-index="{i}" placeholder="Enter secret phrase">`
- Label: `Secret #{i}` (hardcoded EN — i18n gap)
- Placeholder: `"Enter secret phrase"` (hardcoded EN — i18n gap)

### Reveal / Mask Toggle
- Button: `<button class="btn-toggle-visibility" data-for="{i}">👁</button>`
- Click: toggle `inp.type` password ↔ text; icon đổi `👁` ↔ `🙈`
- **Fully wired** — handler gắn trong `openEditSecretsModal` mỗi lần mở modal

### Load
- Decrypt `profile:{name}` từ `chrome.storage.sync` dùng `deps.sessionKey`
- Điền `secrets[index].base` vào từng input; nếu decrypt thất bại → log error, fields trống

### Save (`[Save Changes]`)
- Thu thập `{index: {base: value}}` từ tất cả `.modal-secret-input`
- Đọc lại bản mã hiện tại để **preserve** `settings` object (pepperingHint, v.v.)
- `encryptWithKey(secrets, deps.sessionKey)` → `chrome.storage.sync.set({[profileKey]: encrypted})`
- Toast: `"Secrets saved"` (hardcoded EN — i18n gap)
- Close modal sau khi lưu

### Cancel / ×
- `closeModal('modal-edit-secrets')` — không lưu gì

### Shared Profile Enforcement
- Shared profiles (`shared:` prefix) render **không có** nút `✏️ Secrets` (chỉ có "Remove")
- Modal hoàn toàn không mở cho shared profiles — enforcement ở render layer, không phải ở modal

### Storage
- Write target: `chrome.storage.sync` key = `profile:{profileName}`
- Format: `{ encryptedData: …, iv: … }` (AES-GCM qua `aes-storage-crypto-helper.js`)
- Settings object được preserve khi save — không bị overwrite

## i18n Keys

| Key | EN | VI | Dùng ở |
|---|---|---|---|
| `btnEditSecrets` | "Edit Secrets" | "Sửa Bí mật" | Profile card button (không phải trong modal) |

### Hardcoded strings (i18n gaps)
- Modal title prefix: `"Edit Secrets: "` (JS string)
- Button label: `"Save Changes"` (HTML)
- Button label: `"Cancel"` (HTML)
- Field labels: `"Secret #1"` … `"Secret #5"` (JS template)
- Placeholder: `"Enter secret phrase"` (JS template)
- Toast: `"Secrets saved"` (JS string)
