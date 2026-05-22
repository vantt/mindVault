# PRD v2: Multi-Sheet Profiles & Shared Access

**Product:** mindVault Password Generator  
**Version:** 2.1 (Revised)  
**Date:** 2026-05-17  
**Status:** Draft — Design Finalized, Pending Implementation

### Changelog v2.2 (2026-05-22)
- **Verification Tag (additive grammar):** `<base>[.<tag>]` — 4-char base32 HMAC suffix detects profile/sheet mismatch at decode time. Mismatch → `RecipeProfileMismatchError`, no password generated. See §2.5.
- **Own-profile generation UNCHANGED** — tag is verification-only, does NOT affect password value. Manual rebuildability (README "Cookbook" insight) preserved. See [`recipe-tag-design-rationale.md`](../recipe-tag-design-rationale.md) for Option B vs Option A decision.
- **Shared-profile generation UNCHANGED** — §2.4 HKDF binding stays as-is.
- **Migration:** zero rotation — untagged (v2.1) recipes unaffected.

### Changelog v2.1
- Bỏ Tier 2 (bundle expiry), Tier 3 (_VAULT_TOKEN), Tier 4 (Gist URL) — bypassable, complexity không tương xứng lợi ích
- Thêm **Sheet-Bound Generation**: sheetId nhúng vào thuật toán generate → cryptographic binding, không cần allowlist trong storage
- Revocation model đơn giản hóa: Tier 1 + Tier 5
- Bundle format giản lược

---

## 1. Overview

### 1.1 Problem

v1.x dùng một bộ 5 secrets duy nhất cho tất cả Google Sheets. Không có:
- Phân biệt cấu hình bảo mật giữa các sheets khác nhau
- Cơ chế share quyền truy cập giữa người dùng
- Khả năng revoke access khi cần

### 1.2 Solution Summary

Thêm **Named Profiles** (mỗi profile là một bộ 5 secrets riêng) với:
- Per-sheet routing: mỗi sheet ID map tới 1 profile
- Export/Import profile cho shared access
- Derived secrets để cô lập secrets gốc khi sharing
- **Sheet-bound generation**: sheetId nhúng vào formula generate cho shared profiles
- Revocation đơn giản: Tier 1 (GSheets access) + Tier 5 (rotation)

### 1.3 User Roles mới

| Role | Hành động | Mô tả |
|------|-----------|-------|
| **Owner** | Tạo profile, manage sheets, export bundle | Người quản lý sheet và secrets |
| **Consumer** | Import bundle, generate password từ shared sheet | Người được share quyền |
| **Dual** | Cả hai cùng lúc | Có sheets riêng + dùng sheet của người khác |

---

## 2. Core Concepts

### 2.1 Named Profiles

Mỗi profile = 1 bộ 5 secrets riêng biệt, được mã hoá độc lập.

```
Profile "Personal"  → secrets 1-5 → dùng cho sheets cá nhân
Profile "Banking"   → secrets 1-5 → dùng cho sheets tài chính
Profile "TeamShare" → secrets 1-5 → dùng để share với người khác
```

### 2.2 Sheet Routing

```
sheetMapping: {
  "spreadsheetId_A" → "Personal",
  "spreadsheetId_B" → "Banking",
  (unknown sheet)   → defaultProfile ("Personal")
}
```

> Shared profiles không cần map thủ công — bundle đã chứa sheetId, extension tự route.

### 2.3 Derived Secrets (bắt buộc khi sharing)

**Nguyên tắc:** Secrets gốc của Owner **không bao giờ** được share trực tiếp.

```
DS_1 = HKDF(S1, salt = relationship-label, info = "mindvault-share-v1")
DS_2 = HKDF(S2, salt = relationship-label, info = "mindvault-share-v1")
...
```

Consumer nhận `DS_1..5`, không nhận `S_1..5`.

### 2.4 Sheet-Bound Generation (Mới — v2.1)

**Core insight:** Nhúng sheetId vào thuật toán tính password cho shared profiles.

```
// Personal profile (không thay đổi):
password = combine(S_i, recipe)

// Shared profile (mới):
password = combine(DS_i, recipe, sheetId)
```

`sheetId` đọc từ `window.location.href` tại thời điểm generate — **không lưu trong storage**.

**Tại sao đây là đúng về mặt cryptographic:**

| Scenario | Result |
|---|---|
| Consumer generate trên sheet gốc (sheetId_A) | `f(DS_i, recipe, sheetId_A)` = đúng ✅ |
| Consumer copy sheet → sheetId_B | `f(DS_i, recipe, sheetId_B)` ≠ đúng ✅ |
| Consumer sửa storage (DevTools) | Không có gì trong storage để sửa ✅ |
| Tier 1: Owner revoke GSheets access | Consumer không mở được URL sheetId_A → extension không trigger ✅ |

**Owner workflow khi set up shared accounts:**
```
1. Owner tạo "TeamShare" profile với secrets S_TS_1..5
2. Export bundle → lấy DS_i = HKDF(S_TS_i, label)
3. Owner dùng DS_i + sheetId_A để set up account passwords trên các services
4. Share bundle + sharing password cho Consumer
5. Consumer import → generate cùng passwords ✅
```

> One-time setup cost: Owner phải config accounts dùng DS-generated passwords, không phải S-generated.

### 2.5 Verification Tag (v2.2 — Mismatch Detection)

> Design decision: see [`recipe-tag-design-rationale.md`](../recipe-tag-design-rationale.md) (Option B).

**Recipe grammar (additive, backward compatible):**

```
<hash><position><secret_num>[modifiers][.<tag>]
```

- `tag` = 4 chars from base32 alphabet `abcdefghijkmnpqrstuvwxyz23456789` (excludes lookalikes `o/l/0/1`).
- `tag = base32(HMAC_SHA256(rawSecret, canonical_recipe + "|" + sheetId)).slice(0,4)`
- `canonical_recipe = hash + position + secretIndex + sortedModifiers` (tag excluded).

**Decode behavior:**

| Recipe | Sheet matches | Action |
|--------|---------------|--------|
| Untagged (legacy) | any | Generate as v2.1 — no verification |
| Tagged | matches build-time | Generate (own: `S_i + hash`; shared: HKDF per §2.4) |
| Tagged | mismatch | **Throw `RecipeProfileMismatchError`** — no password generated |
| Tagged | sheetId missing | **Throw `RecipeProfileMismatchError`** |

**Crucial property:** Tag is verification-only. **Password value is identical for tagged and untagged recipes when both decode against the same sheet+profile.** This preserves the README "Cookbook" insight — user can still mentally compute `secret + hash` without the extension.

**Why this is correct cryptographically:**
- HMAC is preimage-resistant: 20-bit truncation (4 base32 chars) does NOT enable brute-force secret recovery (no offline oracle — vault stays AES-encrypted).
- 20 bits = ~1/1M random collision — adequate for verification, not exploitable.
- Different sheetId → different HMAC → different tag → mismatch detected.
- Different profile → different rawSecret → different HMAC → mismatch detected.

> Own-profile threat model differs from shared: user owns all sheets, mismatch is mistake not attack — explicit error is sufficient defense without HKDF crypto layer. See rationale doc §4.

---

## 3. Storage Schema

### 3.1 chrome.storage.sync (phía Owner)

```javascript
{
  salt: Array<number>,               // 16 bytes — salt cho master password

  "profile:Personal":  { encryptedData: Array<number>, iv: Array<number> },
  "profile:Banking":   { encryptedData: Array<number>, iv: Array<number> },

  "shared:TeamFromB":  {
    encryptedData: Array<number>,
    iv: Array<number>,
    readOnly: true,
    sheetId: "spreadsheetId_C",      // locked — đọc từ bundle
    meta: { importedFrom: "User B", importedAt: "2026-05-17", bundleId: "uuid-xxx" }
  },

  sheetMapping: {
    "sheetId_personal_1": "Personal",
    "sheetId_banking":    "Banking"
    // shared profiles tự route qua sheetId field
  },
  defaultProfile: "Personal"
}
```

> Không còn `expiresAt`, không còn `revocationConfig` trong storage.

### 3.2 Decrypted structure per profile (không đổi so với v1)

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

### 3.3 Storage constraints

| Limit | Value | Impact |
|-------|-------|--------|
| Total quota | 102,400 bytes | ~80 profiles max |
| Per-item quota | 8,192 bytes | Mỗi profile an toàn |

---

## 4. Export / Import Flow

### 4.1 Bundle JSON format (v2.1 — giản lược)

```json
{
  "type": "mindvault-profile-share",
  "version": "2.1",
  "bundleId": "uuid-generated-at-export",
  "profileName": "TeamShare",
  "exportedAt": "2026-05-17T10:00:00Z",
  "sheetId": "1BxCdefGhIjKlMnOpQrStUvWxYz",
  "encryptedData": [/* ciphertext của DS_1..5 */],
  "iv": [/* 12 bytes */],
  "exportSalt": [/* 16 bytes */]
}
```

> Không có: `expiresAt`, `revocationTier`, `allowedSheetIds`.

### 4.2 Owner Export (3 bước)

```
Step 1: Relationship label  → "tenant-a-2026"
Step 2: Sheet ID            → auto-fill từ active tab (hoặc nhập URL)
Step 3: Sharing password    → Owner nhập
→ Extension derive DS_1..5 = HKDF(S_i, label)
→ Encrypt DS_1..5 với sharing password
→ Output bundle JSON
```

### 4.3 Consumer Import

```
Consumer: Options → Import Profile
  1. Paste bundle JSON hoặc upload file
  2. Verify bundle format & version
  3. Nhập sharing password
  4. Derive import key → decrypt bundle → lấy DS_1..5
  5. Re-encrypt DS_1..5 bằng Consumer's sessionKey
  6. Lưu vào "shared:..." với sheetId từ bundle
  7. Done — extension tự route sheetId → profile này
```

> Consumer không cần manual assign sheet — sheetId đã locked trong bundle.

### 4.4 Session handling

Consumer unlock bằng master password → sessionKey trong RAM → dùng cho cả own + shared profiles.

---

## 5. Content Script Changes

### 5.1 Sheet ID extraction

```javascript
function getCurrentSheetId() {
  const match = window.location.pathname.match(/\/spreadsheets\/d\/([a-zA-Z0-9_-]+)/);
  return match ? match[1] : null;
}

chrome.runtime.sendMessage({
  action: "GENERATE_PASSWORD",
  text: recipeText,
  sheetId: getCurrentSheetId()
});
```

### 5.2 Service worker routing

```javascript
async function handleGeneratePassword(text, sheetId, sendResponse) {
  // Check own profiles first
  let profileName = await storageAdapter.getProfileForSheet(sheetId);

  // Check shared profiles by sheetId
  if (!profileName) {
    const shared = await storageAdapter.getSharedProfileBySheetId(sheetId);
    if (shared) profileName = shared.key;
  }

  const password = await generatePasswordUseCase.execute(text, profileName, sheetId);
  sendResponse({ success: true, password, profileName });
}
```

### 5.3 Generation algorithm (updated)

```javascript
// generate_password.js
async function execute(recipe, profileName, sheetId) {
  const profile = await storage.getProfile(profileName);
  const { hash, modifier, secretIndex } = parseRecipe(recipe);
  const secret = profile.secrets[secretIndex].base;

  if (profile.isShared) {
    // Sheet-bound generation: sheetId là input
    return generateWithSheetBinding(hash, modifier, secret, sheetId);
  } else {
    // Personal: không dùng sheetId (backward compat)
    return generate(hash, modifier, secret);
  }
}
```

---

## 6. Revocation Model (v2.1 — Simplified)

Chỉ còn 2 tầng:

### Tier 1 — Google Sheets Access

Owner revoke Google Sheets sharing permission → Consumer không mở được sheet → extension không trigger → không generate được.

Với sheet-bound generation, đây là **true hard revoke**:
- Consumer không ở trên URL sheetId_A được
- Không có gì trong storage để bypass
- Tức thời, zero implementation cost

### Tier 5 — Derived Secret Rotation (nuclear)

Dành cho: nghi ngờ secrets bị lộ, insider threat từ technical user.

```
Owner:
  1. Export lại "TeamShare" với relationship label mới: "tenant-a-v2"
  2. KHÔNG gửi bundle mới cho Consumer bị revoke
  3. Dùng DS_v2_i để update account passwords trong shared sheet
  4. Consumer vẫn có DS_v1_i → generate passwords cũ → sai → locked out
```

**Tại sao Tier 5 vẫn cần:**
- Consumer có thể là technical user → bypass Tier 1 bằng cách mở URL của sheet khi vẫn có GSheets access tạm thời
- Nếu bundle bị leak (DS_i bị extract) → rotation là cách duy nhất

### Recommended workflow

```
Rời nhóm (thông thường):
  → Tier 1: Revoke Google Sheets access (done, 30 giây)

Nghi ngờ breach / technical insider:
  → Tier 1 + Tier 5: Rotate label + update account passwords
```

---

## 7. Migration từ v1

```javascript
async function migrateV1ToV2() {
  const { encryptedData, iv } = await chrome.storage.sync.get(["encryptedData", "iv"]);
  if (!encryptedData) return;

  await chrome.storage.sync.set({
    "profile:Default": { encryptedData, iv },
    defaultProfile: "Default",
    sheetMapping: {}
  });
  // Giữ encryptedData/iv cũ để backward compat
}
```

---

## 8. Options Page UI

Xem chi tiết tại [ui-spec-v2-multi-sheet-profiles.md](../ui-spec-v2-multi-sheet-profiles.md).

### Profiles Tab overview

```
[Secrets] [Profiles] [Settings]
           ↓
┌─ My Profiles ───────────────────────────────┐
│  [+ New Profile]                            │
│  🔵 Default      [Edit Secrets] [Export]    │
│  🟠 Banking      [Edit Secrets] [Export]    │
│  🟢 TeamShare    [Edit Secrets] [Export]    │
│                                             │
├─ Shared Profiles (Imported) ────────────────┤
│  [+ Import Profile]                         │
│  📥 TeamFromB    sheetId: 1BxC...  [Remove] │
│                                             │
├─ Sheet Assignments ─────────────────────────┤
│  ABC123  → Default [▼]                      │
│  XYZ789  → Banking [▼]                      │
│  Default fallback → Personal [▼]            │
└─────────────────────────────────────────────┘
```

### Export Wizard (3 bước)

```
Step 1: Relationship label    → "tenant-a-2026"
Step 2: Sheet ID              → [auto-fill từ active tab]
Step 3: Sharing password      → [input] → [Generate Bundle]
```

---

## 9. Implementation Scope

| File | Thay đổi | Effort |
|------|----------|--------|
| `chrome_storage_adapter.js` | Major — profile routing, shared profile by sheetId | L |
| `adapters/infrastructure/export-import-adapter.js` | Mới — bundle encrypt/decrypt, HKDF derive | L |
| `service_worker.js` | Minor — pass sheetId, route profile | S |
| `content/content.js` | Minor — extract + send sheetId | S |
| `core/usecases/generate_password.js` | Minor — sheet-bound generation cho shared profiles | S |
| `core/ports/interfaces.js` | Minor — update IStorageRepository | S |
| `options/options.js` | Major — profile CRUD, export/import wizard | XL |
| `options/options.html` | Major — new Profiles tab | L |
| Migration code (service_worker.js) | Mới — v1 → v2 schema | S |
| Tests | Major update | XL |

**Estimate:** ~1.5 tuần cho 1 dev (giảm so với v2.0 vì bỏ revocation tiers).

---

## 10. Security Considerations

### 10.1 Không thay đổi từ v1

- Argon2id + AES-256-GCM
- sessionKey trong RAM (chrome.storage.session)
- Auto-lock, idle timeout

### 10.2 Thêm mới trong v2.1

| Concern | Mitigation |
|---------|-----------|
| Shared secrets exposure | Derived secrets (HKDF) — secrets gốc không bao giờ rời Owner |
| Bundle interception | Bundle encrypt bằng sharing password (separate từ master) |
| Consumer copy sheet | sheetId trong generation formula — copy = different ID = wrong passwords |
| DevTools bypass | Không có allowlist trong storage để sửa |
| Privilege escalation | Shared profiles là readOnly trong storage |

### 10.3 Honest limitations

- **Trust-based sharing:** Consumer có DS_i — nếu extract khỏi extension và reverse engineer generation algorithm, có thể generate trên local. Mitigation: Tier 5 rotation.
- **Bundle transmission:** Phụ thuộc kênh truyền của Owner. Nên dùng kênh encrypted.
- **One-time setup cost:** Owner phải set up shared accounts dùng DS-generated passwords (không phải S-generated). Cần document rõ trong UX.

---

## 11. Non-Goals (v2)

- Auto-sync bundle khi Owner update secrets
- Server-based real-time sync
- Mobile support
- Per-Consumer audit log
- Bundle expiry / time-based access control (dropped — bypassable client-side)

---

## Unresolved Questions

1. **Owner setup workflow UX**: Cần hướng dẫn rõ ràng trong wizard rằng Owner phải dùng DS (export trước, set up accounts sau). Wizard có nên nhắc không?
2. **Multiple sheets per bundle**: Khuyến nghị 1 bundle per sheet. Có cần enforce không hay chỉ là documentation?
3. **Import channel**: Bundle qua paste text hay upload file hay cả hai?
4. **Bundle versioning**: Algorithm HKDF thay đổi trong tương lai — bundle version field đủ để handle không?
