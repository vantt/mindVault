# PRD v2: Multi-Sheet Profiles & Shared Access

**Product:** mindVault Password Generator  
**Version:** 2.3 (Revised)  
**Date:** 2026-05-25  
**Status:** Draft — Design Finalized, Pending Implementation

### Changelog v2.3 (2026-05-25)
- **Full Access Sharing replaces HKDF Derived Secrets:** §2.3 rewritten — bundle now encrypts **raw secrets** (S_i), not HKDF-derived DS_i. Consumer gets identical secrets → generates identical passwords as Owner.
- **Why HKDF was wrong:** HKDF derives *different* secrets → *different* passwords → Consumer cannot log in to accounts Owner set up. mindVault has no server — the password IS the credential. True revocation always requires changing service passwords regardless of HKDF.
- **New bundle type:** `mindvault-fullaccess-share` v1.0 replaces `mindvault-profile-share` for new exports. Legacy bundles remain importable.
- **Export wizard:** 3 → 2 steps. Label step removed (no HKDF → no label). Steps: Sheet ID (optional) → Sharing password → Bundle.
- **Import behavior:** Full Access bundles create `profile:NAME` (own profile), not `shared:`. If bundle has sheetId → auto-added to `sheetMapping`. Legacy `mindvault-profile-share` imports unchanged → stored as `shared:NAME`.
- **§2.4 Generation unchanged for Full Access imports:** Full Access imports are `profile:` (isShared=false) → standard own-profile generation, no sheetId binding.
- **§6 Tier 5 revocation removed:** Label rotation meaningless without HKDF. Only Tier 1 (GSheets access) remains.
- **Backward compat:** Legacy `mindvault-profile-share` bundles remain importable → stored as `shared:NAME` → HKDF-bound generation as before.

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
- Export/Import profile cho shared access — **Full Access**: Consumer nhận secrets gốc → generate cùng passwords như Owner
- Revocation: Tier 1 (GSheets access revoke)

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

> Full Access imports tự thêm sheetId vào sheetMapping — Consumer không cần map thủ công.

### 2.3 Full Access Sharing (v2.3)

**Nguyên tắc:** Để Consumer generate cùng passwords như Owner, Consumer phải dùng **cùng secrets**.

```
Bundle = AES-GCM-Encrypt(
  plaintext  = { secrets: { "1": {base: S_1}, ..., "5": {base: S_5} } },
  key        = PBKDF2(sharingPassword, exportSalt),
  iv         = random 12 bytes
)
```

Consumer nhận `S_1..5` (raw) → generates **identical passwords** với Owner.

> **Tại sao không dùng HKDF:** HKDF derives *different* secrets → generates *different* passwords → Consumer không thể đăng nhập bằng account Owner đã set up. mindVault không có server nên không có "derived access" — password IS the credential. Revoke luôn đòi hỏi đổi service password bất kể HKDF hay không.

**Owner workflow (v2.3):**
```
1. Owner tạo "TeamShare" profile với secrets S_TS_1..5
2. Dùng S_TS_1..5 để set up account passwords trên các services (như bình thường)
3. Export Full Access bundle (protecting S_TS_1..5 bằng sharing password)
4. Share bundle + sharing password cho Consumer (qua kênh encrypted)
5. Consumer import → nhận S_TS_1..5 → generate cùng passwords ✅
```

### 2.4 Sheet Routing cho Full Access Profiles (v2.3)

Full Access bundles import thành `profile:NAME` (own profile, không phải `shared:`).

**Nếu bundle chứa sheetId → auto-add vào sheetMapping khi import:**
```
sheetMapping[bundle.sheetId] = profileName
```

Generation algorithm **không thay đổi** — giống hệt own profile:
```
password = combine(S_i, recipe)   // không có sheetId binding
```

**Revocation:** Dựa vào Tier 1 (GSheets access) — Owner revoke GSheets permission → Consumer không mở được sheet URL → extension không trigger. Đủ với threat model của mindVault (no server).

> **Legacy `shared:` profiles:** Vẫn dùng HKDF-bound generation (isShared=true, §2.4 cũ). Chỉ apply cho bundles cũ (`mindvault-profile-share`) đã import trước v2.3.

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
| Tagged | matches build-time | Generate with profile secrets |
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

### 3.1 chrome.storage.sync

```javascript
{
  salt: Array<number>,               // 16 bytes — salt cho master password

  // Own profiles (created locally or imported via Full Access bundle)
  "profile:Personal":  { encryptedData: Array<number>, iv: Array<number> },
  "profile:Banking":   { encryptedData: Array<number>, iv: Array<number> },
  "profile:TeamFromAlice": { encryptedData: Array<number>, iv: Array<number> },
  // ↑ Full Access import: tạo profile:NAME như own profile

  // Legacy HKDF-imported profiles (mindvault-profile-share bundles, pre-v2.3)
  "shared:TeamFromB":  {
    encryptedData: Array<number>,
    iv: Array<number>,
    readOnly: true,
    sheetId: "spreadsheetId_C",      // locked — đọc từ bundle
    meta: { importedFrom: "User B", importedAt: "2026-05-17", bundleId: "uuid-xxx" }
  },

  sheetMapping: {
    "sheetId_personal_1": "Personal",
    "sheetId_banking":    "Banking",
    "sheetId_from_alice": "TeamFromAlice"  // auto-added khi import Full Access bundle
    // shared: profiles tự route qua sheetId field (legacy)
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

### 4.1 Bundle JSON format

**v2.3 — Full Access (mới, ưu tiên):**

```json
{
  "type": "mindvault-fullaccess-share",
  "version": "1.0",
  "bundleId": "uuid-generated-at-export",
  "profileName": "TeamShare",
  "exportedAt": "2026-05-25T10:00:00Z",
  "sheetId": "1BxCdefGhIjKlMnOpQrStUvWxYz",
  "encryptedData": [/* ciphertext của S_1..5 raw */],
  "iv": [/* 12 bytes */],
  "exportSalt": [/* 16 bytes */]
}
```

> `sheetId` là optional — nếu có, Consumer's import tự map vào sheetMapping. Nếu không có, Consumer tự assign.

**v2.1 — HKDF Profile Share (legacy, vẫn importable):**

```json
{
  "type": "mindvault-profile-share",
  "version": "2.1",
  "bundleId": "uuid-generated-at-export",
  "profileName": "TeamShare",
  "exportedAt": "2026-05-17T10:00:00Z",
  "sheetId": "1BxCdefGhIjKlMnOpQrStUvWxYz",
  "encryptedData": [/* ciphertext của DS_1..5 = HKDF(S_i, label) */],
  "iv": [/* 12 bytes */],
  "exportSalt": [/* 16 bytes */]
}
```

> Legacy bundles import thành `shared:NAME` như trước — backward compat.

### 4.2 Owner Export (2 bước — v2.3)

```
Step 1: Sheet ID (optional)   → [auto-fill từ active tab] hoặc nhập URL/ID
Step 2: Sharing password      → [input + confirm] → [Generate Bundle]
→ Encrypt S_1..5 (raw) với PBKDF2(sharingPassword, exportSalt)
→ Output bundle JSON  (type: "mindvault-fullaccess-share")
```

> Không còn bước Relationship Label — không có HKDF → không cần label.

### 4.3 Consumer Import

```
Consumer: Options → Import Profile
  1. Paste bundle JSON hoặc upload file
  2. Verify bundle format & version
  3. Nhập sharing password
  4. Derive import key → decrypt bundle → lấy secrets

  If type === "mindvault-fullaccess-share":
    5a. Re-encrypt secrets bằng Consumer's sessionKey
    6a. Lưu vào "profile:NAME" (own profile, không phải shared:)
    7a. Nếu bundle.sheetId → tự thêm sheetMapping[sheetId] = NAME

  If type === "mindvault-profile-share" (legacy):
    5b. Re-encrypt DS_1..5 bằng Consumer's sessionKey
    6b. Lưu vào "shared:NAME" với sheetId từ bundle (behavior cũ)

  8. Done — extension tự route sheetId → profile này
```

### 4.4 Session handling

Consumer unlock bằng master password → sessionKey trong RAM → dùng cho cả own + legacy shared profiles.

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
  // Check own profiles first (includes Full Access imports)
  let profileName = await storageAdapter.getProfileForSheet(sheetId);

  // Check legacy shared profiles by sheetId (pre-v2.3 HKDF imports)
  if (!profileName) {
    const shared = await storageAdapter.getSharedProfileBySheetId(sheetId);
    if (shared) profileName = shared.key;
  }

  const password = await generatePasswordUseCase.execute(text, profileName, sheetId);
  sendResponse({ success: true, password, profileName });
}
```

### 5.3 Generation algorithm

```javascript
// generate_password.js
async function execute(recipe, profileName, sheetId) {
  const profile = await storage.getProfile(profileName);
  const { hash, modifier, secretIndex } = parseRecipe(recipe);
  const secret = profile.secrets[secretIndex].base;

  if (profile.isShared) {
    // Legacy shared: HKDF-bound generation (pre-v2.3 imports only)
    return generateWithSheetBinding(hash, modifier, secret, sheetId);
  } else {
    // Own profile OR Full Access import: standard generation
    return generate(hash, modifier, secret);
  }
}
```

---

## 6. Revocation Model (v2.3 — Single Tier)

### Tier 1 — Google Sheets Access

Owner revoke Google Sheets sharing permission → Consumer không mở được sheet → extension không trigger → không generate được.

Đây là **sufficient revocation** với threat model của mindVault:
- Consumer không ở trên URL sheetId_A được
- Không có gì trong storage để bypass
- Tức thời, zero implementation cost

> **Lưu ý:** mindVault không có server. Nếu Consumer đã biết password thông qua việc generate trước đó, Tier 1 chỉ ngăn generate mới. Đổi service password là biện pháp duy nhất nếu cần true invalidation — điều này đúng với bất kỳ hệ thống nào, có hay không có HKDF.

### Recommended workflow

```
Rời nhóm (thông thường):
  → Tier 1: Revoke Google Sheets access (done, 30 giây)

Nghi ngờ secrets bị lộ (bundle bị leak):
  → Tier 1 + đổi profile secrets + export bundle mới cho Consumer còn lại
  → Đổi service passwords (required bất kể, không phải vì HKDF)
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

Xem chi tiết tại [ui-specs/README.md](../ui-specs/README.md).

### Profiles Tab overview

```
[Secrets] [Profiles] [Settings]
           ↓
┌─ My Profiles ───────────────────────────────┐
│  [+ New Profile]                            │
│  🔵 Default      [Edit Secrets] [Export]    │
│  🟠 Banking      [Edit Secrets] [Export]    │
│  🟢 TeamShare    [Edit Secrets] [Export]    │
│  📥 TeamFromAlice (Full Access import)       │
│                                             │
├─ Shared Profiles (Legacy Imports) ──────────┤
│  [+ Import Profile]                         │
│  📥 TeamFromB    sheetId: 1BxC...  [Remove] │
│  (pre-v2.3 HKDF imports only)               │
│                                             │
├─ Sheet Assignments ─────────────────────────┤
│  ABC123  → Default [▼]                      │
│  XYZ789  → Banking [▼]                      │
│  Default fallback → Personal [▼]            │
└─────────────────────────────────────────────┘
```

### Export Wizard (2 bước — v2.3)

```
Step 1: Sheet ID (optional)   → [auto-fill từ active tab hoặc nhập URL]
Step 2: Sharing password      → [input + confirm] → [Generate Bundle]
```

---

## 9. Implementation Scope

| File | Thay đổi | Effort |
|------|----------|--------|
| `adapters/infrastructure/export-import-adapter.js` | Add `createFullAccessBundle()`, keep `createBundle()` as deprecated | M |
| `options/options-export-wizard.js` | Remove label step, use `createFullAccessBundle`, 2-step flow | M |
| `options/options-import-wizard.js` | Detect bundle type; fullaccess → `profile:NAME` + sheetMapping; legacy → `shared:NAME` | M |
| `chrome_storage_adapter.js` | Minor — no change to profile routing needed | S |
| `core/usecases/generate_password.js` | No change needed (Full Access imports are `profile:`, isShared=false) | XS |
| `_locales/en/messages.json` | Update export wizard i18n (remove label step strings) | S |
| `_locales/vi/messages.json` | Same | S |

**Estimate:** ~0.5 ngày (giảm mạnh so với v2.0 vì chỉ thay export/import layer).

---

## 10. Security Considerations

### 10.1 Không thay đổi từ v1

- Argon2id + AES-256-GCM
- sessionKey trong RAM (chrome.storage.session)
- Auto-lock, idle timeout

### 10.2 Thêm mới trong v2.3

| Concern | Mitigation |
|---------|-----------|
| Bundle interception | Bundle encrypt bằng sharing password (separate từ master) + PBKDF2 key derivation |
| Consumer copy sheet | Tier 1 (GSheets access revoke) — cryptographic binding not needed without server |
| DevTools bypass | No allowlist in storage to tamper with |
| Bundle leak (secrets exposed) | Transmit via encrypted channel; Owner can rotate profile secrets + re-export |
| Consumer generates passwords locally | Inherent to offline model — same as any password manager without server |

### 10.3 Honest limitations

- **Trust-based sharing:** Consumer có raw secrets — họ có thể generate offline không cần sheet. Đây là trade-off của offline model; acceptable vì Owner chọn ai để share.
- **Bundle transmission:** Phụ thuộc kênh truyền của Owner. Nên dùng kênh encrypted (Signal, Element, etc.).
- **No forward secrecy:** Nếu Consumer đã generate password trước khi bị revoke, password đó vẫn valid trên service. Đây là giới hạn của mọi offline credential system.

---

## 11. Non-Goals (v2)

- Auto-sync bundle khi Owner update secrets
- Server-based real-time sync
- Mobile support
- Per-Consumer audit log
- Bundle expiry / time-based access control (dropped — bypassable client-side)
- HKDF derived secrets for sharing (dropped — generates wrong passwords for shared accounts)

---

## Unresolved Questions

1. **Import UX cho Full Access:** Nên hiển thị warning rõ ràng rằng import tạo `profile:` (không phải `shared:`), tức là Consumer có quyền edit secrets? Hay treat as read-only UI dù lưu là `profile:`?
2. **Multiple sheets per bundle:** Khuyến nghị 1 bundle per sheet (vì sheetId optional). Có cần enforce không hay chỉ là documentation?
3. **Import channel:** Bundle qua paste text hay upload file hay cả hai? (Hiện tại: cả hai — giữ nguyên)
