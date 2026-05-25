# Research: Multi-Sheet Security Config cho PassChef Extension

**Date:** 2026-05-17 | **Project:** passMan / chrome-extension

---

## Tóm tắt

Extension hiện tại (v1.3) dùng **một bộ 5 secrets duy nhất** cho tất cả Google Sheets. Không có cơ chế nhận dạng sheet hay routing config theo sheet. PRD đã liệt kê "Multiple profiles" là post-MVP (Low priority, Medium complexity).

Research này phân tích cách thêm per-sheet security config với **impact tối thiểu** lên kiến trúc hiện tại.

---

## 0. Use Case mở rộng (addendum)

### 3 roles thực tế

```
Owner (User B)                    Consumer (User A)
──────────────────                ─────────────────────────────────────
Tạo sheet, set secrets            Có sheets riêng của mình (vd: 2 sheets)
Export profile → sharing bundle   Nhận bundle từ B qua email/chat
                                  Import bundle → assign cho shared sheet
                                  Generate password từ sheet của B ✅
```

User A có thể là **cả hai cùng lúc**: vừa là Owner cho sheets riêng, vừa là Consumer cho sheet được share.

### Yêu cầu cốt lõi

| Yêu cầu | Ghi chú |
|---------|---------|
| User A không cần biết master password của B | Zero-knowledge giữa users |
| User B không lộ master password | Export dùng sharing password riêng |
| User A có thể có master password chung hoặc riêng cho mỗi profile của mình | Flexible |
| Shared profile: read-only với User A | Chỉ consume, không edit |
| Khi B update secrets → B export lại → A import lại | Versioned sharing |

---

## 1. Hiện trạng

### Storage schema hiện tại (chrome.storage.sync)

```
{
  salt:          Array<number>   // 16 bytes - Argon2 salt
  encryptedData: Array<number>   // AES-256-GCM ciphertext của JSON bên dưới
  iv:            Array<number>   // 12 bytes - IV cho AES-GCM
}

// Plaintext bên trong encryptedData:
{
  secrets: {
    "1": { base: "Basic*" },
    "2": { base: "Secure#" },
    "3": { base: "Ultra$" },
    "4": { base: "Trade&" },
    "5": { base: "Backup@" }
  },
  settings: { pepperingHint: true }
}
```

### Các điểm cần thay đổi

| Layer | File | Vấn đề |
|-------|------|--------|
| Content script | `content/content.js` | Không gửi sheet ID khi request password |
| Service worker | `service_worker.js` | `GENERATE_PASSWORD` handler không biết đang ở sheet nào |
| Storage adapter | `adapters/infrastructure/chrome_storage_adapter.js` | `getSecret(index)` đọc từ 1 blob duy nhất |
| Options page | `options/options.js` | UI chỉ có 1 bộ secrets, không có profile management |
| Manifest | `manifest.json` | OK, đã match toàn bộ `spreadsheets/*` |

---

## 2. Thiết kế đề xuất: Named Profiles

### Khái niệm

Thay vì map `sheetId → secrets` trực tiếp (quá cồng kềnh), dùng **Named Profiles** làm trung gian:

```
Sheet ID → Profile Name → Encrypted Secrets (5 phrases)
```

Ví dụ:
```
"ABC123" → "Banking"  → { 1: "Bank*", 2: "BankSecure#", ... }
"XYZ789" → "Work"     → { 1: "Work*", 2: "WorkSec#", ... }
"DEF456" → "Personal" → (default - dùng bộ secrets gốc)
unknown  → "Personal" → (fallback mặc định)
```

**Lợi điểm so với per-sheet encryption:**
- Nhiều sheets có thể share 1 profile (không phải setup lại từ đầu)
- User quản lý profiles, không phải quản lý từng sheet ID
- Migration path rõ ràng: existing secrets → profile "Default"

### Schema mới (chrome.storage.sync)

```javascript
// Global - dùng chung cho tất cả profiles
{
  salt: Array<number>,          // 16 bytes, dùng chung (same master password = same Argon2 key)

  // Profiles: mỗi profile có encrypted blob riêng
  "profile:Default":  { encryptedData: Array<number>, iv: Array<number> },
  "profile:Banking":  { encryptedData: Array<number>, iv: Array<number> },
  "profile:Work":     { encryptedData: Array<number>, iv: Array<number> },
  // ... (mỗi profile ~1-2KB, tổng 100KB quota → ~50-80 profiles tối đa)

  // Sheet mapping
  sheetMapping: {
    "spreadsheetId1": "Banking",
    "spreadsheetId2": "Work",
    // sheets không có entry → dùng "Default"
  },

  // Tên profile mặc định
  defaultProfile: "Default"
}
```

> **Storage size estimate:**  
> 5 secrets × ~30 chars = 150 bytes plaintext → ~250 bytes ciphertext → ~1KB per profile as Array<number>  
> 80 profiles = ~80KB (well within 100KB quota)

### Decrypted structure per profile (không đổi)

```javascript
{
  secrets: {
    "1": { base: "..." },
    "2": { base: "..." },
    "3": { base: "..." },
    "4": { base: "..." },
    "5": { base: "..." }
  },
  settings: { pepperingHint: true }
}
```

---

## 3. Thay đổi implementation

### 3.1 Sheet ID extraction (content.js)

```javascript
function getCurrentSheetId() {
  const match = window.location.pathname.match(/\/spreadsheets\/d\/([a-zA-Z0-9_-]+)/);
  return match ? match[1] : null;
}
```

Khi send message, đính kèm `sheetId`:
```javascript
// Thay vì:
chrome.runtime.sendMessage({ action: "GENERATE_PASSWORD", text })
// Thành:
chrome.runtime.sendMessage({ action: "GENERATE_PASSWORD", text, sheetId: getCurrentSheetId() })
```

### 3.2 Service worker (service_worker.js)

```javascript
// Handler nhận thêm sheetId
async function handleGeneratePassword(text, sheetId, sendResponse) {
  const password = await generatePasswordUseCase.execute(text, sheetId);
  // ...
}
```

### 3.3 Storage adapter (chrome_storage_adapter.js)

Thêm phương thức `getProfileForSheet(sheetId)` và `getSecret(index, profileName)`:

```javascript
async getProfileForSheet(sheetId) {
  const { sheetMapping, defaultProfile } = await this.sync.get(["sheetMapping", "defaultProfile"]);
  if (!sheetId || !sheetMapping?.[sheetId]) return defaultProfile || "Default";
  return sheetMapping[sheetId];
}

async getSecret(index, profileName = "Default") {
  const { sessionKey } = await this.session.get("sessionKey");
  if (!sessionKey) throw new Error("Vault is locked");

  const key = `profile:${profileName}`;
  const data = await this.sync.get([key]);
  const profileData = data[key];
  if (!profileData) {
    // Fallback to Default
    return this._decryptAndGetSecret(index, "Default");
  }
  return this._decryptAndGetSecret(index, profileName);
}
```

### 3.4 GeneratePassword use case

Interface `IStorageRepository.getSecret(index)` cần update signature:

```javascript
// Truyền sheetId vào execute()
async execute(text, sheetId = null) {
  const recipe = this.parser.parse(text);
  if (!recipe) throw new Error("Invalid recipe");

  const profileName = await this.storage.getProfileForSheet(sheetId);
  const secret = await this.storage.getSecret(recipe.secretIndex, profileName);
  // ... rest of logic unchanged
}
```

### 3.5 Options page (options.js + options.html)

Thêm tab/section mới: **"Profiles"**

```
[Secrets] [Profiles] [Settings]
           ↕
┌─ Profiles ──────────────────────────────┐
│ + New Profile                           │
│                                         │
│ 🔵 Default        [Edit] [Delete]       │
│ 🔴 Banking        [Edit] [Delete]       │
│ 🟢 Work           [Edit] [Delete]       │
│                                         │
│ Sheet Assignments:                      │
│ ABC123 (Personal Sheet) → Default  [▼]  │
│ XYZ789 (Bank Accounts)  → Banking  [▼]  │
│ + Add sheet assignment                  │
└─────────────────────────────────────────┘
```

---

## 4. Migration strategy

```javascript
// Khi detect schema cũ (có encryptedData nhưng không có profile:Default):
async migrateToMultiProfile() {
  const { encryptedData, iv } = await this.sync.get(["encryptedData", "iv"]);
  if (!encryptedData) return; // Fresh install, nothing to migrate

  // Copy sang profile key mới
  await this.sync.set({
    "profile:Default": { encryptedData, iv },
    defaultProfile: "Default",
    sheetMapping: {}
  });

  // Xóa keys cũ (optional, để backward compat thì giữ lại)
  // await this.sync.remove(["encryptedData", "iv"]);
}
```

Migration tự động chạy khi extension update (trong `onInstalled` handler).

---

## 5. Security analysis

### Shared master password + shared salt

Tất cả profiles dùng **cùng master password** → cùng Argon2 salt → **cùng AES key**.

Implications:
- ✅ UX đơn giản, 1 lần unlock cho tất cả
- ✅ Session key hiện tại (`sessionKey` in RAM) vẫn dùng được
- ⚠️ Nếu master password lộ → tất cả profiles đều bị lộ
- ✅ Đây là trade-off được chấp nhận (tương tự 1Password's master password)

### Nếu muốn per-profile master password (strong isolation)

Cần per-profile salt + separate session keys:
```javascript
// session storage thay vì 1 sessionKey:
{ 
  "sessionKey:Default": JWK,
  "sessionKey:Banking": JWK,
}
```

Phức tạp hơn nhiều. **Không khuyến nghị cho MVP multi-sheet.**

### Threat model unchanged

| Threat | Impact |
|--------|--------|
| Sheet bị lộ | ✅ Vô hại (chỉ có recipe/hash) |
| Extension bị compromise | Như cũ - toàn bộ profiles lộ khi unlocked |
| Brute-force master password | Như cũ - Argon2id bảo vệ |
| Chrome account hijack | Có encrypted blobs nhưng không crack được |

---

## 6. Chrome storage.sync constraints

| Limit | Value | Impact |
|-------|-------|--------|
| Total quota | 102,400 bytes | ~80 profiles max |
| Per-item quota | 8,192 bytes | Mỗi profile dưới ~8KB → OK |
| Max items | 512 | 512 profiles (không bao giờ đạt) |
| Write ops/hour | 1,800 | Đủ cho normal usage |

**Lưu ý:** `sheetMapping` object có thể lớn nếu user có nhiều sheets. Nhưng thực tế người dùng chỉ có 5-20 sheets → không vấn đề.

---

## 7. Phạm vi thay đổi

| File | Loại thay đổi | Ước tính |
|------|--------------|----------|
| `chrome_storage_adapter.js` | Major refactor | +80 lines |
| `service_worker.js` | Minor (pass sheetId) | +5 lines |
| `content/content.js` | Minor (extract + send sheetId) | +5 lines |
| `core/usecases/generate_password.js` | Minor (pass sheetId through) | +3 lines |
| `core/ports/interfaces.js` | Minor (update IStorageRepository) | +2 lines |
| `options/options.js` | Major (profile management UI) | +200 lines |
| `options/options.html` | Moderate (new section) | +100 lines |
| Migration code | New (in service_worker.js) | +30 lines |
| Tests | Major update | significant |

**Tổng:** ~1 tuần implementation cho 1 dev.

---

## 8. Approach thay thế (rejected)

### A. Per-sheet encryption (1 blob per sheet)
- Rejected vì: cồng kềnh (100 sheets = 100 blobs), khó manage
- Named profiles linh hoạt hơn

### B. Per-sheet master password
- Rejected vì: UX tệ, user phải nhớ nhiều passwords
- Complexity không xứng với benefit

### C. Global secrets + per-sheet "secret remapping"
- Rejected vì: logic phức tạp, dễ nhầm

---

---

## 9. Shared Profile — Export/Import (Owner → Consumer)

### 9.1 Flow

```
[Owner - User B]                        [Consumer - User A]
Options → Profiles
  → Export "TeamSheet" profile
  → Nhập sharing password (khác master)
  → Copy JSON bundle / tải file
                  ────────── gửi qua chat/email ──────────►
                                                Options → Import Profile
                                                  → Paste bundle
                                                  → Nhập sharing password
                                                  → Đặt tên local: "TeamSheet (from B)"
                                                  → Assign cho sheet ID của B
                                                  → ✅ Dùng được
```

### 9.2 Export bundle format

```json
{
  "type": "PassChef-profile-share",
  "version": "1.0",
  "profileName": "TeamSheet",
  "exportedAt": "2026-05-17T12:00:00Z",
  "encryptedData": [/* AES-GCM ciphertext của secrets */],
  "iv": [/* 12 bytes */],
  "salt": [/* 16 bytes — per-export salt, khác với B's master salt */]
}
```

**Encryption key của bundle** = Argon2id(sharingPassword, exportSalt)  
→ Không liên quan gì đến master password của B.

### 9.3 Storage schema cho shared profiles

```javascript
// chrome.storage.sync (phía User A)
{
  // Own profiles
  "profile:Default":  { encryptedData, iv },   // key = User A's sessionKey
  "profile:Banking":  { encryptedData, iv },

  // Shared/imported profiles — lưu nguyên bundle đã decrypt một lần
  // key = Argon2id(sharingPassword, exportSalt) → decrypt trong import wizard
  "shared:TeamSheet": {
    encryptedData,   // re-encrypted với User A's sessionKey để tiện dùng sau
    iv,
    readOnly: true,
    importedFrom: "User B",
    importedAt: "2026-05-17"
  },

  // Salt chỉ dùng cho own profiles
  salt: [...],

  // Sheet mapping
  sheetMapping: {
    "ABC123": "Default",          // own sheet
    "XYZ789": "shared:TeamSheet"  // B's sheet
  },
  defaultProfile: "Default"
}
```

> **Re-encryption on import**: Khi A import, decrypt bundle bằng sharingPassword → lấy secrets plaintext → re-encrypt bằng A's sessionKey → lưu vào `shared:*`. Từ đó về sau A chỉ cần 1 unlock (master password của A).

### 9.4 Session handling

Không cần thêm phức tạp. Sau khi import và re-encrypt bằng A's key:
- A unlock bằng master password của A → `sessionKey` trong RAM
- `getSecret()` dùng `sessionKey` cho cả own và shared profiles
- ✅ 1 lần unlock, access toàn bộ

### 9.5 Read-only enforcement

```javascript
async saveProfile(name, data) {
  const profileKey = `profile:${name}`;
  const sharedKey = `shared:${name}`;
  const existing = await this.sync.get([sharedKey]);
  if (existing[sharedKey]?.readOnly) throw new Error("Shared profile is read-only");
  // ...
}
```

### 9.6 Update flow khi B thay đổi secrets

```
B update secrets → B export lại (version mới)
→ Gửi bundle mới cho A
→ A vào Options → Shared Profiles → Re-import "TeamSheet"
→ Overwrite old shared profile
```

Không có auto-sync — phải manual. Đơn giản, không cần thêm infrastructure.

---

## 10. Revised storage schema (complete picture)

```javascript
// chrome.storage.sync — phía User A (Consumer + Owner)
{
  // === OWN PROFILES ===
  salt: Array<number>,             // Argon2 salt cho master password của A
  "profile:Default":  { encryptedData, iv },
  "profile:Banking":  { encryptedData, iv },

  // === SHARED PROFILES (imported) ===
  "shared:TeamSheet": {
    encryptedData,   // re-encrypted với A's key
    iv,
    readOnly: true,
    meta: { importedFrom: "User B", importedAt: "2026-05-17" }
  },

  // === ROUTING ===
  sheetMapping: {
    "A_sheet_id_1": "Default",
    "A_sheet_id_2": "Banking",
    "B_sheet_id":   "shared:TeamSheet"
  },
  defaultProfile: "Default"
}
```

---

## 11. Revised implementation scope

| File | Thay đổi |
|------|----------|
| `chrome_storage_adapter.js` | Major — profile routing, shared profile read-only guard |
| `options/options.js` | Major — profile CRUD, export wizard, import wizard |
| `options/options.html` | Major — new sections |
| `service_worker.js` | Nhỏ — pass sheetId |
| `content/content.js` | Nhỏ — extract + send sheetId |
| `generate_password.js` | Nhỏ — route by profile |
| `adapters/infrastructure/export-import-adapter.js` | **Mới** — bundle encrypt/decrypt logic |

---

## Unresolved questions

1. **Sheet assignment UX**: Sheet chưa assign → silent dùng Default hay popup hỏi?
2. **Profile deletion**: Sheets đang dùng profile bị xóa → fallback Default hay show error?
3. **Profile rename**: Dùng profile name làm storage key → rename cần migrate sheetMapping. OK không?
4. **Import channel**: Bundle share qua paste JSON hay upload file? Hay cả hai?
5. **Bundle expiry**: Export bundle có nên có expiry date không? (security hygiene)
6. **Revoke access**: Nếu B muốn revoke A's access → B đổi secrets + export bundle mới không share cho A. A vẫn giữ old bundle trong local → A vẫn generate được passwords cũ. Có cần cơ chế revoke không?
7. **Per-profile master password cho own profiles**: Shared salt (1 master) hay mỗi profile 1 master?
