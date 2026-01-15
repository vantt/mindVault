# PRD: Password Generator Chrome Extension

## MVP Version 1.0

---

## 📋 Document Info

| Field               | Value                                 |
| ------------------- | ------------------------------------- |
| **Product Name**    | SecretHash Password Generator         |
| **Version**         | MVP 1.1                               |
| **Created Date**    | January 2025                          |
| **Last Updated**    | January 2025                          |
| **Status**          | Draft                                 |
| **Target Platform** | Google Chrome Extension (Manifest V3) |

### Changelog v1.1

- 🔒 **Security Enhancement**: Argon2id thay thế PBKDF2 làm key derivation chính
- 🔒 **Security Enhancement**: PBKDF2 600k iterations làm fallback
- ⏱️ **Session**: Giảm timeout từ 30 phút xuống 10 phút
- ⏱️ **Session**: Thêm auto-lock khi idle 5 phút
- 📝 **Documentation**: Thêm Security Considerations section

### Changelog v1.2

- 🛠️ **Technical Fix**: Chuyển Session Storage từ `local` (disk) sang `session` (RAM secured)
- 🛡️ **Reliability**: Thêm bắt buộc "Backup Checkbox" trong Setup Flow
- 🌶️ **Security Feature**: Thêm tùy chọn "Manual Peppering Hint" cho UI

---

## 1. Executive Summary

### 1.1 Problem Statement (The "Chef" Insight)

Người dùng hiện tại giống như **IT Admin** quản lý password theo cách thủ công:

- **Recipe (Công thức)**: Lưu "r4nd0m#1" trên Spreadsheet.
- **Secret (Gia vị bí mật)**: Nhớ trong đầu ("Basic\*").
- **Process (Quy trình)**: Mỗi lần đăng nhập phải copy, paste, và gõ thêm bí mật thủ công.

→ **Rủi ro**: Tốn thời gian, dễ "nêm" sai gia vị (gõ nhầm), và mệt mỏi.

### 1.2 Solution (The "Sous-Chef")

Chrome Extension đóng vai trò là **"Phụ bếp ảo" (Sous-Chef)**:

1. **Detect (Nhận diện)**: Tự động thấy khi bạn chọn "Món ăn" (Recipe) trên Menu (Google Sheet).
2. **Cook (Chế biến)**: Lấy "Gia vị bí mật" (Secret Encypted) và "Nguyên liệu" (Hash) để chế biến ra password.
3. **Serve (Phục vụ)**: Đưa món ăn hoàn chỉnh (Password) cho bạn copy chỉ với 1 click.

### 1.3 Key Benefits

| Benefit                      | Description                          |
| ---------------------------- | ------------------------------------ |
| ⚡ **Nấu ăn cực nhanh**      | Từ 30s → 2s để có password           |
| ✅ **Không bao giờ nêm sai** | Máy tính làm, không bao giờ gõ nhầm  |
| 🔒 **Bảo mật tuyệt đối**     | Gia vị (Secrets) được khóa trong két |
| 🔄 **Bếp nào cũng dùng**     | Sync mọi nơi trên Chrome             |

---

## 2. Goals & Objectives

### 2.1 MVP Goals

| #   | Goal                                         | Success Metric                         |
| --- | -------------------------------------------- | -------------------------------------- |
| G1  | Auto-detect recipe khi click cell            | Detection rate > 95%                   |
| G2  | Tính đúng password cho tất cả position types | Accuracy = 100%                        |
| G3  | Bảo mật secrets với encryption               | Argon2id + AES-256-GCM                 |
| G4  | Sync across devices                          | Works on 100% Chrome-synced devices    |
| G5  | UX đơn giản                                  | < 3 clicks để copy password            |
| G6  | Session security                             | Auto-lock sau 10 phút hoặc 5 phút idle |

### 2.2 Non-Goals (Out of MVP Scope)

- ❌ Support các spreadsheet khác (Excel Online, Notion)
- ❌ Auto-fill password vào login forms
- ❌ Password strength analysis
- ❌ Backup/export secrets
- ❌ Multiple secret profiles
- ❌ Mobile support (Chrome mobile không support extensions đầy đủ)

---

## 3. User Stories

### 3.1 Core User Stories

```
US-01: Setup Secrets (Stock the Kitchen)
AS A user
I WANT TO securely store my 5 secret spices in the extension
SO THAT I don't have to search for them every time

Acceptance Criteria:
- [ ] User can set master password (min 8 chars)
- [ ] User can input 5 secret phrases
- [ ] Secrets are encrypted before storage
- [ ] Setup flow is intuitive (< 2 minutes)
```

```
US-02: Auto-detect Recipe
AS A user
I WANT THE extension to automatically detect when I click a cell containing a password recipe
SO THAT I don't have to manually trigger it

Acceptance Criteria:
- [ ] Extension detects click on any cell in Google Sheets
- [ ] Extension validates if cell content matches recipe pattern
- [ ] Works on docs.google.com/spreadsheets/*
- [ ] No false positives on regular text
```

```
US-03: View Cooked Dish (Password)
AS A user
I WANT TO see the calculated password in a popup
SO THAT I can verify it before using

Acceptance Criteria:
- [ ] Popup appears near the clicked cell
- [ ] Password is hidden by default (dots)
- [ ] Click to reveal/hide password
- [ ] Shows recipe being processed
```

```
US-04: Serve (Copy Password)
AS A user
I WANT TO copy the password to clipboard with one click
SO THAT I can quickly paste it into login forms

Acceptance Criteria:
- [ ] Copy button in popup
- [ ] Visual feedback on copy success
- [ ] Auto-clear clipboard after 30 seconds (configurable)
- [ ] Keyboard shortcut: Ctrl+C when popup is open
```

```
US-05: Unlock Kitchen (Master Password)
AS A user
I WANT TO unlock the extension with my master password
SO THAT my secrets remain protected

Acceptance Criteria:
- [ ] Prompt for master password on first use per session
- [ ] Session timeout configurable (default: 10 minutes)
- [ ] Auto-lock after 5 minutes of inactivity (idle)
- [ ] Lock button to manually lock
- [ ] Invalid password shows error (max 5 attempts)
- [ ] Lockout 5 minutes after max failed attempts
```

```
US-06: Cross-Kitchen Sync
AS A user
I WANT MY encrypted secrets to sync across my devices
SO THAT I can use the extension on any computer

Acceptance Criteria:
- [ ] Uses chrome.storage.sync
- [ ] Encrypted data syncs (not plaintext)
- [ ] Works when signed into same Chrome account
- [ ] Graceful handling of sync conflicts
```

### 3.2 Secondary User Stories

```
US-07: Edit Spices
AS A user
I WANT TO edit my secret phrases
SO THAT I can update them when I rotate

Acceptance Criteria:
- [ ] Access via extension options page
- [ ] Requires master password to view/edit
- [ ] Can edit individual secrets (1-5)
- [ ] Can add version-specific secrets
```

```
US-08: Change Master Key
AS A user
I WANT TO change my master password
SO THAT I can maintain security

Acceptance Criteria:
- [ ] Requires current master password
- [ ] Re-encrypts all secrets with new password
- [ ] Confirmation step
```

---

## 4. Functional Requirements

### 4.1 Recipe Parsing (Phân tích công thức)

Extension MUST parse các định dạng công thức sau:

```
Pattern: <base_ingredient><cooking_style><spice_index>[toppings][_version]

Examples:
├── r4nd0m#1           → Món cơ bản (Basic recipe)
├── r4nd0m_v2#1        → Đổi vị (With version)
├── r4nd0m_vU1#1       → Món cấp cứu (Urgent version)
├── r4nd0m_vB1#1       → Món dự phòng (Backup version)
├── r4nd0m#1_          → Lật mặt (Reverse position)
├── r4nd0m#1!          → Lửa lớn (Uppercase)
├── r4nd0m#1?          → Đảo gia vị (Reverse secret)
└── r4nd0m#1~          → Giảm vị (Remove special chars)
```

**Regex Pattern:**

```regex
^([a-zA-Z0-9]+)([#@$%^])(\d)([_!?~]*)(?:_(v[a-zA-Z0-9]+))?$
```

### 4.2 Cooking Styles (Position Types)

| Symbol | Style Name  | Action (Algorithm)                           |
| ------ | ----------- | -------------------------------------------- |
| `#`    | Top Garnish | Prefix (`secret + hash`)                     |
| `$`    | Base Sauce  | Suffix (`hash + secret`)                     |
| `@`    | Filling     | Middle (`hash[0:mid] + secret + hash[mid:]`) |
| `%`    | Mixed Salad | Interleave Char (Xen kẽ từng ký tự)          |
| `^`    | Layer Cake  | Interleave Pair (Xen kẽ từng cặp)            |

**Cooking Demonstration:**

```javascript
// Style #: Top Garnish
"r4nd0m" + "#1" + secret("Basic*") → "Basic*r4nd0m"

// Style $: Base Sauce
"r4nd0m" + "$3" + secret("Ultra$") → "r4nd0mUltra$"

// Style @: Filling
"r4nd0m" + "@2" + secret("Secure#") → "r4n" + "Secure#" + "d0m" = "r4nSecure#d0m"

// Style %: Mixed Salad
"r4nd0m" + "%4" + secret("Trade&")
→ r+T, 4+r, n+a, d+d, 0+e, m+&
→ "rT4rnadd0em&"
```

### 4.3 Toppings (Modifiers)

| Topping | Culinary Effect    | Example                                 |
| ------- | ------------------ | --------------------------------------- |
| `_`     | **Flip (Lật)**     | Secret ở cuối thay vì đầu               |
| `!`     | **Sear (Lửa lớn)** | Viết HOA secret (`Basic*` → `BASIC*`)   |
| `?`     | **Stir (Đảo)**     | Đảo ngược secret (`Basic*` → `*cisaB`)  |
| `~`     | **Mild (Giảm vị)** | Xóa ký tự đặc biệt (`Basic*` → `Basic`) |

**Multiple Toppings:** Chế biến theo thứ tự:

```
r4nd0m#1_! → Lật vị trí trước, sau đó bật Lửa lớn → "r4nd0mBASIC*"
```

### 4.4 Version Handling (Seasonal Menu)

| Version Format | Meaning             | Secret Key                       |
| -------------- | ------------------- | -------------------------------- |
| (none)         | Version 1 (default) | `secret_1`                       |
| `_v2`          | Version 2           | `secret_1_v2` hoặc apply pattern |
| `_v3`          | Version 3           | `secret_1_v3` hoặc apply pattern |
| `_vU1`         | Urgent version 1    | `secret_1_vU1`                   |
| `_vB1`         | Backup version 1    | `secret_1_backup`                |

**Version Pattern (User configurable):**

```
Base secret: "Basic*"
v2 pattern: "{base}Q224"  → "Basic*Q224"
v3 pattern: "{base}Q324"  → "Basic*Q324"
vU1 pattern: "{base}!0624" → "Basic*!0624"
```

### 4.5 Secret Storage Structure (The Pantry)

```javascript
// Stored in chrome.storage.sync (encrypted)
{
  "encryptedData": "AES-256-GCM encrypted blob",
  "salt": "random salt for key derivation",
  "iv": "initialization vector",
  "version": "1.0"
}

// Decrypted structure
{
  "secrets": {
    "1": {
      "base": "Basic*",
      "versions": {
        "v2": "Basic*Q224",
        "v3": "Basic*Q324",
        "vU1": "Basic*!0624"
      },
      "backup": "BasicBackup*"
    },
    // ... secrets 2-5
  },
  "settings": {
    "versionPattern": "{base}{quarter}",
    "autoLockMinutes": 30,
    "clipboardClearSeconds": 30
  }
}
```

---

## 5. Technical Requirements

### 5.1 Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                     CHROME EXTENSION                             │
│                     (Manifest V3)                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐ │
│  │  Content Script │  │   Background    │  │  Options Page   │ │
│  │                 │  │   Service       │  │                 │ │
│  │  - Detect cell  │  │   Worker        │  │  - Setup        │ │
│  │  - Show popup   │  │                 │  │  - Edit secrets │ │
│  │  - Copy to      │  │  - Encryption   │  │  - Settings     │ │
│  │    clipboard    │  │  - Storage      │  │                 │ │
│  │                 │  │  - Session mgmt │  │                 │ │
│  └────────┬────────┘  └────────┬────────┘  └────────┬────────┘ │
│           │                    │                     │          │
│           │    Messages        │                     │          │
│           ◄────────────────────►                     │          │
│                                │                     │          │
│                                ▼                     │          │
│                    ┌─────────────────────┐          │          │
│                    │  chrome.storage     │◄─────────┘          │
│                    │  .sync              │                      │
│                    │  (encrypted data)   │                      │
│                    └─────────────────────┘                      │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 5.2 File Structure

```
password-extension/
├── manifest.json           # Extension manifest (V3)
├── background/
│   └── service-worker.js   # Background service worker
├── content/
│   ├── content.js          # Content script for Google Sheets
│   └── content.css         # Popup styles
├── options/
│   ├── options.html        # Options page
│   ├── options.js          # Options logic
│   └── options.css         # Options styles
├── popup/
│   ├── popup.html          # Extension popup (toolbar icon)
│   ├── popup.js            # Popup logic
│   └── popup.css           # Popup styles
├── lib/
│   ├── crypto.js           # Encryption utilities (Argon2id + AES)
│   ├── argon2.js           # Argon2 wrapper
│   ├── parser.js           # Recipe parser
│   ├── generator.js        # Password generator
│   └── storage.js          # Storage wrapper
├── wasm/
│   ├── argon2.wasm         # Argon2 WebAssembly binary (~200KB)
│   └── argon2.js           # Argon2 WASM loader
├── assets/
│   ├── icon-16.png
│   ├── icon-32.png
│   ├── icon-48.png
│   └── icon-128.png
└── _locales/
    └── vi/
        └── messages.json   # Vietnamese localization
```

**Dependencies:**

```json
{
  "dependencies": {
    "argon2-browser": "^1.18.0"
  }
}
```

### 5.3 Manifest.json

```json
{
  "manifest_version": 3,
  "name": "SecretHash Password Generator",
  "version": "1.0.0",
  "description": "Generate passwords from recipes stored in Google Sheets",

  "permissions": ["storage", "clipboardWrite", "activeTab"],

  "host_permissions": ["https://docs.google.com/spreadsheets/*"],

  "background": {
    "service_worker": "background/service-worker.js",
    "type": "module"
  },

  "content_scripts": [
    {
      "matches": ["https://docs.google.com/spreadsheets/*"],
      "js": ["content/content.js"],
      "css": ["content/content.css"],
      "run_at": "document_idle"
    }
  ],

  "action": {
    "default_popup": "popup/popup.html",
    "default_icon": {
      "16": "assets/icon-16.png",
      "32": "assets/icon-32.png",
      "48": "assets/icon-48.png",
      "128": "assets/icon-128.png"
    }
  },

  "options_page": "options/options.html",

  "icons": {
    "16": "assets/icon-16.png",
    "32": "assets/icon-32.png",
    "48": "assets/icon-48.png",
    "128": "assets/icon-128.png"
  }
}
```

### 5.4 Security Requirements (Kitchen Safety Rules)

#### 5.4.1 Key Derivation (Primary: Argon2id)

| Requirement           | Specification          |
| --------------------- | ---------------------- |
| **Primary Algorithm** | Argon2id (memory-hard) |
| Memory                | 64 MB                  |
| Iterations (time)     | 3                      |
| Parallelism           | 4                      |
| Hash Length           | 32 bytes (256 bits)    |
| Salt                  | 16 bytes random        |

```javascript
// Primary: Argon2id (via WebAssembly)
import argon2 from "argon2-browser";

async function deriveKeyArgon2(masterPassword, salt) {
  const result = await argon2.hash({
    pass: masterPassword,
    salt: salt,
    type: argon2.ArgonType.Argon2id,
    hashLen: 32, // 256 bits for AES-256
    time: 3, // iterations
    mem: 65536, // 64 MB memory cost
    parallelism: 4, // threads
  });

  // Import as AES key
  return crypto.subtle.importKey(
    "raw",
    result.hash,
    { name: "AES-GCM" },
    false,
    ["encrypt", "decrypt"]
  );
}
```

**Tại sao Argon2id?**

- **Memory-hard**: Cần 64MB RAM per attempt → GPU/ASIC attack rất khó
- **Argon2id**: Kết hợp Argon2i (side-channel resistant) vàArgon2d (GPU resistant)
- **Winner of PHC**: Password Hashing Competition 2015
- **Modern standard**: Khuyến nghị bởi OWASP 2024

#### 5.4.2 Key Derivation (Fallback: PBKDF2)

Sử dụng khi Argon2 WASM không load được (hiếm):

| Requirement            | Specification                      |
| ---------------------- | ---------------------------------- |
| **Fallback Algorithm** | PBKDF2-SHA256                      |
| Iterations             | 600,000 (6x stronger than typical) |
| Salt                   | 16 bytes random                    |

```javascript
// Fallback: PBKDF2 with high iterations
async function deriveKeyPBKDF2(masterPassword, salt) {
  const encoder = new TextEncoder();
  const keyMaterial = await crypto.subtle.importKey(
    "raw",
    encoder.encode(masterPassword),
    "PBKDF2",
    false,
    ["deriveKey"]
  );

  return crypto.subtle.deriveKey(
    {
      name: "PBKDF2",
      salt: salt,
      iterations: 600000, // 600k iterations
      hash: "SHA-256",
    },
    keyMaterial,
    { name: "AES-GCM", length: 256 },
    false,
    ["encrypt", "decrypt"]
  );
}
```

#### 5.4.3 Encryption

| Requirement | Specification                  |
| ----------- | ------------------------------ |
| Algorithm   | AES-256-GCM                    |
| Key Size    | 256 bits                       |
| IV          | 12 bytes random per encryption |
| Auth Tag    | 128 bits (built into GCM)      |

#### 5.4.4 Session Management

| Setting             | Default    | Range          | Notes                    |
| ------------------- | ---------- | -------------- | ------------------------ |
| **Session timeout** | 10 minutes | 5-30 minutes   | Absolute timeout         |
| **Idle auto-lock**  | 5 minutes  | 2-15 minutes   | Lock khi không hoạt động |
| Max failed attempts | 5          | Fixed          | Per session              |
| Lockout duration    | 5 minutes  | Fixed          | After max attempts       |
| Clipboard clear     | 30 seconds | 10-120 seconds | After copy               |

**Session Behavior:**

```
┌─────────────────────────────────────────────────────────────────┐
│                    SESSION LIFECYCLE                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  [Unlock] ──► Session Active ──┬──► [10 min timeout] ──► Lock   │
│                                │                                 │
│                                ├──► [5 min idle] ──► Lock       │
│                                │                                 │
│                                ├──► [Manual lock] ──► Lock      │
│                                │                                 │
│                                └──► [Browser close] ──► Lock    │
│                                                                  │
│  Input Safety:                                                   │
│  - No "Auto-detect" (prevents accidental detection/popups)      │
│  - Hotkey/Context Menu required to activate                      │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

#### 5.4.5 Data Storage

| Storage Type             | Data                           | Encryption                          |
| ------------------------ | ------------------------------ | ----------------------------------- |
| `chrome.storage.sync`    | Encrypted secrets blob         | AES-256-GCM                         |
| `chrome.storage.sync`    | Salt, IV, KDF params           | Plaintext (required for decryption) |
| `chrome.storage.session` | Session state & Keys           | Memory only (Privileged, RAM)       |
| `chrome.storage.local`   | Cache (Non-sensitive)          | Disk (Not for secrets!)             |
| Memory                   | Derived key, decrypted secrets | Cleared on lock                     |

#### 5.4.6 Data in Transit

- Extension chỉ giao tiếp internal (content script ↔ service worker)
- Không có external API calls trong MVP
- `chrome.storage.sync` được Google encrypt trong transit

#### 5.4.7 Security Limitations (Transparency)

```
┌─────────────────────────────────────────────────────────────────┐
│              ⚠️ KNOWN SECURITY LIMITATIONS                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1. JAVASCRIPT MEMORY                                            │
│     - Không thể secure clear memory như native code              │
│     - Decrypted secrets có thể ở trong memory sau khi dùng       │
│     - Memory dump attack vẫn possible khi session active         │
│                                                                  │
│  2. BROWSER DEVTOOLS                                             │
│     - Khi unlocked, DevTools có thể inspect decrypted data       │
│     - Mitigation: Lock thường xuyên, short session timeout       │
│                                                                  │
│  3. MALICIOUS EXTENSIONS                                         │
│     - Extensions khác có thể đọc DOM, clipboard                  │
│     - Mitigation: Chỉ install trusted extensions                 │
│                                                                  │
│  4. CHROME ACCOUNT COMPROMISE                                    │
│     - Attacker có thể sync encrypted blob                        │
│     - Mitigation: Strong master password + Chrome 2FA            │
│     - Protection: Argon2id makes offline brute-force very hard   │
│                                                                  │
│  5. PHYSICAL ACCESS                                              │
│     - Ai có access máy khi unlocked = có access passwords        │
│     - Mitigation: Short idle timeout (5 min)                     │
│                                                                  │
│  ─────────────────────────────────────────────────────────────  │
│                                                                  │
│  THIS EXTENSION IS:                                              │
│  ✅ Much safer than plaintext passwords in Google Sheets        │
│  ✅ Good for everyday password convenience                       │
│  ✅ Protected against casual snooping                            │
│                                                                  │
│  THIS EXTENSION IS NOT:                                          │
│  ❌ A replacement for dedicated password managers                │
│  ❌ Suitable for high-security environments                      │
│  ❌ Protected against sophisticated targeted attacks             │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 5.5 Performance Requirements

| Metric                       | Target            | Notes                            |
| ---------------------------- | ----------------- | -------------------------------- |
| Cell click → Popup display   | < 300ms           | Increased due to security checks |
| **Unlock (Argon2id)**        | 2-3 seconds       | Memory-hard KDF takes time       |
| **Unlock (PBKDF2 fallback)** | 1-2 seconds       | 600k iterations                  |
| Password calculation         | < 50ms            | After unlock                     |
| Extension load time          | < 500ms           | Includes WASM load               |
| Storage sync latency         | Depends on Chrome | Typically < 1s                   |

**Performance vs Security Trade-off:**

```
┌─────────────────────────────────────────────────────────────────┐
│                                                                  │
│   Unlock Time          Security Level                            │
│   ──────────────────────────────────────────────────────────    │
│   ~0.3s (PBKDF2 100k)  ████░░░░░░  Weak - Easy to brute-force   │
│   ~1.5s (PBKDF2 600k)  ██████░░░░  Good - Harder to crack       │
│   ~2.5s (Argon2id)     ██████████  Best - Memory-hard           │
│                                                                  │
│   Chúng ta chọn: Argon2id (2-3s unlock) cho maximum security    │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 6. UI/UX Design

### 6.1 Password Popup (Content Script)

```
┌──────────────────────────────────────────┐
│ 🔐 SecretHash                        [×] │
├──────────────────────────────────────────┤
│                                          │
│  Recipe: r4nd0m_v2#1                    │
│  ─────────────────────────────────────   │
│                                          │
│  Dish (Password):                        │
│  ┌────────────────────────────────────┐  │
│  │ ••••••••••••••           👁 [Serve] │  │
│  └────────────────────────────────────┘  │
│                                          │
│  ✓ Served! (auto-clear in 30s)          │
│                                          │
└──────────────────────────────────────────┘

Dimensions: 320px × auto
Position: Below and right of clicked cell
```

**States:**

```
STATE: Locked
┌──────────────────────────────────────────┐
│ 🔐 SecretHash                        [×] │
├──────────────────────────────────────────┤
│                                          │
│  🔒 Kitchen is locked                    │
│                                          │
│  Master Password:                        │
│  ┌────────────────────────────────────┐  │
│  │ ••••••••                           │  │
│  └────────────────────────────────────┘  │
│                                          │
│           [Unlock]                       │
│                                          │
└──────────────────────────────────────────┘

STATE: Invalid Recipe
┌──────────────────────────────────────────┐
│ 🔐 SecretHash                        [×] │
├──────────────────────────────────────────┤
│                                          │
│  ⚠️ Invalid recipe format              │
│                                          │
│  Cell value: "Hello World"               │
│                                          │
│  Expected: <hash><#@$%^><1-5>           │
│                                          │
└──────────────────────────────────────────┘

STATE: Missing Secret
┌──────────────────────────────────────────┐
│ 🔐 SecretHash                        [×] │
├──────────────────────────────────────────┤
│                                          │
│  ⚠️ Secret #4 not configured            │
│                                          │
│  [Open Settings]                         │
│                                          │
└──────────────────────────────────────────┘
```

### 6.2 Extension Popup (Toolbar Icon)

```
┌──────────────────────────────────────────┐
│ 🔐 SecretHash Password Generator         │
├──────────────────────────────────────────┤
│                                          │
│  Status: 🟢 Unlocked                     │
│  Session expires in: 25:30               │
│                                          │
│  ─────────────────────────────────────   │
│                                          │
│  Quick Actions:                          │
│  ┌────────────────────────────────────┐  │
│  │ 🔒 Lock Kitchen                    │  │
│  └────────────────────────────────────┘  │
│  ┌────────────────────────────────────┐  │
│  │ ⚙️ Settings                        │  │
│  └────────────────────────────────────┘  │
│                                          │
│  ─────────────────────────────────────   │
│                                          │
│  Usage: Click any cell in Google Sheets  │
│  containing a password recipe.          │
│                                          │
└──────────────────────────────────────────┘
```

### 6.3 Options Page (Settings)

```
┌─────────────────────────────────────────────────────────────────┐
│ ⚙️ SecretHash Settings                                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌─────────────────────────────────────────────────────────────┐│
│  │ 🔐 MASTER PASSWORD                                          ││
│  │ ─────────────────────────────────────────────────────────── ││
│  │                                                              ││
│  │ Current Status: ✅ Configured                                ││
│  │                                                              ││
│  │ [Change Master Password]                                     ││
│  │                                                              ││
│  └─────────────────────────────────────────────────────────────┘│
│                                                                  │
│  ┌─────────────────────────────────────────────────────────────┐│
│  │ 🗝️ SECRET PHRASES (The Pantry)                              ││
│  │ ─────────────────────────────────────────────────────────── ││
│  │                                                              ││
│  │ Secret #1:                    [••••••••] [👁] [Edit]        ││
│  │ Secret #2:                    [••••••••] [👁] [Edit]        ││
│  │ Secret #3:                    [••••••••] [👁] [Edit]        ││
│  │ Secret #4:                    [••••••••] [👁] [Edit]        ││
│  │ Secret #5:                    [••••••••] [👁] [Edit]        ││
│  │                                                              ││
│  │ ─────────────────────────────────────────────────────────── ││
│  │ Version Pattern:                                             ││
│  │ ┌─────────────────────────────────────────────────────────┐ ││
│  │ │ {base}{quarter}                                         │ ││
│  │ └─────────────────────────────────────────────────────────┘ ││
│  │ Preview: Basic* → Basic*Q125 (for v2 in Q1-2025)            ││
│  │                                                              ││
│  └─────────────────────────────────────────────────────────────┘│
│                                                                  │
│  ┌─────────────────────────────────────────────────────────────┐│
│  │ ⏱️ SECURITY SETTINGS                                        ││
│  │ ─────────────────────────────────────────────────────────── ││
│  │                                                              ││
│  │ Session timeout:    [10 ▼] minutes  (max 30)                ││
│  │ Idle auto-lock:     [5  ▼] minutes  (max 15)                ││
│  │ Clear clipboard:    [30 ▼] seconds                          ││
│  │ [x] Show Manual Peppering Hint ("Don't forget your pepper!") ││
│  │                                                              ││
│  └─────────────────────────────────────────────────────────────┘│
│                                                                  │
│  ┌─────────────────────────────────────────────────────────────┐│
│  │ 🗑️ DANGER ZONE                                              ││
│  │ ─────────────────────────────────────────────────────────── ││
│  │                                                              ││
│  │ [Reset All Data] - Delete all secrets and settings          ││
│  │                                                              ││
│  └─────────────────────────────────────────────────────────────┘│
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 6.4 First-time Setup Flow

```
STEP 1: Welcome
┌─────────────────────────────────────────────────────────────────┐
│                                                                  │
│                    🔐 Welcome to SecretHash                     │
│                                                                  │
│         Secure password generation for Google Sheets             │
│                                                                  │
│  ─────────────────────────────────────────────────────────────  │
│                                                                  │
│  This extension helps you:                                       │
│  ✓ Generate passwords from formulas in your spreadsheet         │
│  ✓ Keep your secret phrases encrypted and synced                │
│  ✓ Copy passwords with one click                                │
│                                                                  │
│                     [Get Started →]                              │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘

STEP 2: Master Password
┌─────────────────────────────────────────────────────────────────┐
│                                                                  │
│                  🔑 Create Master Password                       │
│                                                                  │
│  This password will encrypt all your secret phrases.             │
│  Choose something strong that you can remember.                  │
│                                                                  │
│  ─────────────────────────────────────────────────────────────  │
│                                                                  │
│  Master Password:                                                │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │                                                         │    │
│  └─────────────────────────────────────────────────────────┘    │
│  Strength: ░░░░░░░░░░ Too weak                                  │
│                                                                  │
│  Confirm Password:                                               │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │                                                         │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                  │
│  ⚠️ This password cannot be recovered if forgotten!             │
│                                                                  │
│              [← Back]              [Continue →]                  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘

STEP 3: Secret Phrases
┌─────────────────────────────────────────────────────────────────┐
│                                                                  │
│                  🗝️ Configure Secret Phrases                    │
│                                                                  │
│  Enter your secret phrases for each security level.              │
│  These will be combined with formulas to generate passwords.     │
│                                                                  │
│  ─────────────────────────────────────────────────────────────  │
│                                                                  │
│  #1 - Low Security (Social media):                              │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │ Basic*                                                  │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                  │
│  #2 - Medium Security (Email, Cloud):                           │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │ Secure#                                                 │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                  │
│  #3 - High Security (Banking):                                  │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │ Ultra$                                                  │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                  │
│  #4 - Special (Trading):                                        │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │ Trade&                                                  │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                  │
│  #5 - Emergency (Backup):                                       │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │ Backup@                                                 │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                  │
│  ⚠️ IMPORTANT: If you lose your computer, you lose these secrets!│
│  [x] I have securely backed up these phrases (paper/offline)     │
│                                                                  │
│              [← Back]              [Continue →]                  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘

STEP 4: Complete
┌─────────────────────────────────────────────────────────────────┐
│                                                                  │
│                      ✅ Setup Complete!                          │
│                                                                  │
│  Your secrets are now encrypted and synced across devices.       │
│                                                                  │
│  ─────────────────────────────────────────────────────────────  │
│                                                                  │
│  How to use:                                                     │
│                                                                  │
│  1. Open your Google Sheet with password formulas                │
│  2. Click on any cell containing a formula (e.g., r4nd0m#1)     │
│  3. The password will appear in a popup                          │
│  4. Click Copy to copy to clipboard                              │
│                                                                  │
│                                                                  │
│                     [Start Using →]                              │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 7. Google Sheets Integration

### 7.1 Activation Strategy: Hotkey & Search (More Secure)

Thay vì "Auto-detect" (gây tốn resource và rủi ro lộ), Extension sẽ hoạt động theo cơ chế **On-Demand**:

1.  **User Action:**

    - Select cell containing formula (e.g., `r4nd0m#1`).
    - Press Hotkey: `Ctrl+Shift+L` (Default) or `Alt+S`.
    - Or Right-click -> "Generate Password".

2.  **Extension Action:**
    - Get selected text (activeElement or selection).
    - Parse formula.
    - Show Popup _next to user cursor_.

#### Implementation Details

```javascript
// background.js (Keyboard Command)
chrome.commands.onCommand.addListener((command) => {
  if (command === "generate_password") {
    // Send message to content script to get selection
    chrome.tabs.query({ active: true, currentWindow: true }, (tabs) => {
      chrome.tabs.sendMessage(tabs[0].id, { action: "get_selection" });
    });
  }
});

// content.js
chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
  if (request.action === "get_selection") {
    const selection = window.getSelection().toString().trim();
    // Also try to get from Active Element (Input/Textarea)
    const activeVal = document.activeElement.value;

    // Choose best value
    const target = selection || activeVal;

    if (isValidFormula(target)) {
      showPopup(target);
    }
  }
});
```

### 7.2 Popup Positioning

```javascript
function calculatePopupPosition(clickX, clickY) {
  const POPUP_WIDTH = 320;
  const POPUP_HEIGHT = 200;
  const PADDING = 10;

  let x = clickX + PADDING;
  let y = clickY + PADDING;

  // Adjust if popup would go off-screen
  if (x + POPUP_WIDTH > window.innerWidth) {
    x = clickX - POPUP_WIDTH - PADDING;
  }
  if (y + POPUP_HEIGHT > window.innerHeight) {
    y = clickY - POPUP_HEIGHT - PADDING;
  }

  return { x, y };
}
```

### 7.3 Handling Dynamic DOM

Google Sheets sử dụng virtualized rendering. Cần handle:

```javascript
// Re-attach observers khi DOM thay đổi
const bodyObserver = new MutationObserver(() => {
  const formulaBar = document.querySelector('[aria-label="Formula Bar"]');
  if (formulaBar && !formulaBar.hasAttribute("data-secrethash-observed")) {
    attachFormulaBarObserver(formulaBar);
    formulaBar.setAttribute("data-secrethash-observed", "true");
  }
});

bodyObserver.observe(document.body, {
  childList: true,
  subtree: true,
});
```

---

## 8. Data Flow Diagrams

### 8.1 First-time Setup Flow

```
┌─────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│  User   │    │  Options    │    │  Service    │    │   Chrome    │
│         │    │   Page      │    │   Worker    │    │   Storage   │
└────┬────┘    └──────┬──────┘    └──────┬──────┘    └──────┬──────┘
     │                │                   │                  │
     │  Open Options  │                   │                  │
     │───────────────>│                   │                  │
     │                │                   │                  │
     │                │  Check if setup   │                  │
     │                │───────────────────>                  │
     │                │                   │   Get data       │
     │                │                   │─────────────────>│
     │                │                   │   (empty)        │
     │                │                   │<─────────────────│
     │                │   Show Setup UI   │                  │
     │                │<──────────────────│                  │
     │                │                   │                  │
     │  Enter master  │                   │                  │
     │  password +    │                   │                  │
     │  secrets       │                   │                  │
     │───────────────>│                   │                  │
     │                │                   │                  │
     │                │   Encrypt data    │                  │
     │                │───────────────────>                  │
     │                │                   │                  │
     │                │                   │   Derive key     │
     │                │                   │   (PBKDF2)       │
     │                │                   │                  │
     │                │                   │   Encrypt        │
     │                │                   │   (AES-256-GCM)  │
     │                │                   │                  │
     │                │                   │   Store          │
     │                │                   │─────────────────>│
     │                │                   │   Success        │
     │                │                   │<─────────────────│
     │                │   Setup Complete  │                  │
     │                │<──────────────────│                  │
     │  Success UI    │                   │                  │
     │<───────────────│                   │                  │
     │                │                   │                  │
```

### 8.2 Password Generation Flow

```
┌─────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│  User   │    │  Content    │    │  Service    │    │   Chrome    │
│         │    │  Script     │    │   Worker    │    │   Storage   │
└────┬────┘    └──────┬──────┘    └──────┬──────┘    └──────┬──────┘
     │                │                   │                  │
     │  Click cell    │                   │                  │
     │───────────────>│                   │                  │
     │                │                   │                  │
     │                │  Detect formula   │                  │
     │                │  "r4nd0m#1"       │                  │
     │                │                   │                  │
     │                │  Request password │                  │
     │                │───────────────────>                  │
     │                │                   │                  │
     │                │                   │  Check session   │
     │                │                   │  (is unlocked?)  │
     │                │                   │                  │
     │                │                   │  Get encrypted   │
     │                │                   │  data            │
     │                │                   │─────────────────>│
     │                │                   │  Encrypted blob  │
     │                │                   │<─────────────────│
     │                │                   │                  │
     │                │                   │  Decrypt         │
     │                │                   │  (session key)   │
     │                │                   │                  │
     │                │                   │  Parse formula   │
     │                │                   │  hash: r4nd0m    │
     │                │                   │  pos: #          │
     │                │                   │  secret: 1       │
     │                │                   │                  │
     │                │                   │  Get secret[1]   │
     │                │                   │  "Basic*"        │
     │                │                   │                  │
     │                │                   │  Calculate:      │
     │                │                   │  Basic* + r4nd0m │
     │                │                   │                  │
     │                │  Password result  │                  │
     │                │<──────────────────│                  │
     │                │  "Basic*r4nd0m"   │                  │
     │                │                   │                  │
     │  Show popup    │                   │                  │
     │<───────────────│                   │                  │
     │                │                   │                  │
```

### 8.3 Unlock Flow

```
┌─────────┐    ┌─────────────┐    ┌─────────────┐
│  User   │    │  Content    │    │  Service    │
│         │    │  Script     │    │   Worker    │
└────┬────┘    └──────┬──────┘    └──────┬──────┘
     │                │                   │
     │  Enter master  │                   │
     │  password      │                   │
     │───────────────>│                   │
     │                │                   │
     │                │  Unlock request   │
     │                │───────────────────>
     │                │                   │
     │                │                   │  Derive key
     │                │                   │  (PBKDF2)
     │                │                   │
     │                │                   │  Try decrypt
     │                │                   │
     │                │                   │  [If success]
     │                │                   │  Store key in
     │                │                   │  session memory
     │                │                   │
     │                │                   │  Set auto-lock
     │                │                   │  timer
     │                │                   │
     │                │  Unlock success   │
     │                │<──────────────────│
     │                │                   │
     │  Hide lock UI  │                   │
     │  Show password │                   │
     │<───────────────│                   │
     │                │                   │
```

---

## 9. Error Handling

### 9.1 Error Types & Messages

| Error Code | Condition              | User Message                                            | Action           |
| ---------- | ---------------------- | ------------------------------------------------------- | ---------------- |
| `E001`     | Invalid formula format | "Invalid formula format. Expected: <hash><#@$%^><1-5>"  | Show in popup    |
| `E002`     | Secret not configured  | "Secret #X is not configured. Open settings to add it." | Link to settings |
| `E003`     | Extension locked       | "Extension is locked. Enter master password."           | Show unlock form |
| `E004`     | Wrong master password  | "Incorrect password. {N} attempts remaining."           | Retry/lockout    |
| `E005`     | Lockout active         | "Too many failed attempts. Try again in {N} minutes."   | Show countdown   |
| `E006`     | Storage sync failed    | "Failed to sync data. Check your internet connection."  | Retry option     |
| `E007`     | Encryption error       | "Encryption error. Please reset and reconfigure."       | Link to reset    |
| `E008`     | Session expired        | "Session expired. Please unlock again."                 | Show unlock form |

### 9.2 Recovery Procedures

```
SCENARIO: User forgets master password

┌─────────────────────────────────────────────────────────────────┐
│ ⚠️ Forgot Master Password?                                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│ Unfortunately, your master password cannot be recovered.         │
│ Your secrets are encrypted and only you have the key.            │
│                                                                  │
│ Options:                                                         │
│                                                                  │
│ 1. Try to remember your password                                 │
│    Hint: Check if you saved it somewhere safe                   │
│                                                                  │
│ 2. Reset extension                                               │
│    This will delete ALL stored secrets.                         │
│    You'll need to reconfigure everything.                       │
│                                                                  │
│ [Try Again]              [Reset Extension]                      │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 10. Testing Requirements

### 10.1 Unit Tests

| Component     | Test Cases                                      |
| ------------- | ----------------------------------------------- |
| **Parser**    | Valid formulas, invalid formulas, edge cases    |
| **Generator** | All position types, all modifiers, combinations |
| **Crypto**    | Encrypt/decrypt, key derivation, error handling |
| **Storage**   | Save/load, sync, conflict resolution            |

### 10.2 Integration Tests

| Flow         | Test Cases                                |
| ------------ | ----------------------------------------- |
| **Setup**    | First-time setup, re-setup after reset    |
| **Unlock**   | Correct password, wrong password, lockout |
| **Generate** | All formula types, missing secrets        |
| **Sync**     | Multi-device, conflict handling           |

### 10.3 E2E Tests

| Scenario         | Steps                                       |
| ---------------- | ------------------------------------------- |
| **Full flow**    | Setup → Click cell → Copy password → Verify |
| **Cross-device** | Setup on device A → Use on device B         |
| **Session**      | Unlock → Wait for timeout → Re-unlock       |

### 10.4 Test Formula Samples

```javascript
const testCases = [
  // Basic formulas
  { formula: "r4nd0m#1", secret: "Basic*", expected: "Basic*r4nd0m" },
  { formula: "h4sh3s$3", secret: "Ultra$", expected: "h4sh3sUltra$" },
  { formula: "c0d3s@2", secret: "Secure#", expected: "c0dSecure#3s" },

  // With version
  {
    formula: "r4nd0m_v2#1",
    secret: "Basic*Q224",
    expected: "Basic*Q224r4nd0m",
  },
  {
    formula: "h4sh3s_vU1$3",
    secret: "Ultra$!0624",
    expected: "h4sh3sUltra$!0624",
  },

  // With modifiers
  { formula: "r4nd0m#1_", secret: "Basic*", expected: "r4nd0mBasic*" },
  { formula: "r4nd0m#1!", secret: "Basic*", expected: "BASIC*r4nd0m" },
  { formula: "r4nd0m#1?", secret: "Basic*", expected: "*cisaBr4nd0m" },
  { formula: "r4nd0m#1~", secret: "Basic*", expected: "Basicr4nd0m" },

  // Interleave
  { formula: "r4nd0m%4", secret: "Trade&", expected: "rT4rnadd0em&" },
  { formula: "r4nd0m^4", secret: "Trade&", expected: "r4Trndad0me&" },

  // Multiple modifiers
  { formula: "r4nd0m#1_!", secret: "Basic*", expected: "r4nd0mBASIC*" },
];
```

---

## 11. Implementation Plan

### 11.1 Phases

```
┌─────────────────────────────────────────────────────────────────┐
│                    IMPLEMENTATION PHASES                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  PHASE 1: Core Infrastructure (Week 1-2)                        │
│  ├── Manifest V3 setup                                          │
│  ├── Crypto module:                                             │
│  │   ├── Argon2id via WebAssembly (primary KDF)                │
│  │   ├── PBKDF2 600k iterations (fallback KDF)                 │
│  │   └── AES-256-GCM encryption                                │
│  ├── Storage wrapper (chrome.storage.sync)                      │
│  ├── Formula parser                                             │
│  └── Password generator (all position types)                    │
│                                                                  │
│  PHASE 2: UI Components (Week 2-3)                              │
│  ├── Options page (setup flow)                                  │
│  ├── Extension popup                                            │
│  ├── Content script popup                                       │
│  └── Styling & animations                                       │
│                                                                  │
│  PHASE 3: Google Sheets Integration (Week 3-4)                  │
│  ├── Cell detection strategy                                    │
│  ├── Formula bar observer                                       │
│  ├── Popup positioning                                          │
│  └── Edge case handling                                         │
│                                                                  │
│  PHASE 4: Security & Session (Week 4)                           │
│  ├── Session management (10 min timeout)                        │
│  ├── Idle detection (5 min auto-lock)                          │
│  ├── Failed attempt handling                                    │
│  └── Clipboard auto-clear                                       │
│                                                                  │
│  PHASE 5: Testing & Polish (Week 5)                             │
│  ├── Unit tests                                                 │
│  ├── Integration tests                                          │
│  ├── E2E tests                                                  │
│  ├── Security tests (KDF timing, memory)                       │
│  ├── Bug fixes                                                  │
│  └── Performance optimization                                   │
│                                                                  │
│  PHASE 6: Release (Week 6)                                      │
│  ├── Chrome Web Store submission                                │
│  ├── Documentation                                              │
│  └── User guide                                                 │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 11.2 Milestones

| Milestone | Deliverable                  | Target |
| --------- | ---------------------------- | ------ |
| M1        | Core crypto + parser working | Week 2 |
| M2        | UI components complete       | Week 3 |
| M3        | Google Sheets integration    | Week 4 |
| M4        | Security features complete   | Week 4 |
| M5        | All tests passing            | Week 5 |
| M6        | Chrome Web Store published   | Week 6 |

### 11.3 Dependencies

```
┌─────────────────┐     ┌─────────────────┐
│  argon2.wasm    │     │  Web Crypto API │
│  (WebAssembly)  │     │  (Native)       │
└────────┬────────┘     └────────┬────────┘
         │                       │
         └───────────┬───────────┘
                     │
                     ▼
             ┌─────────────────┐
             │   Crypto.js     │
             │  (KDF + AES)    │
             └────────┬────────┘
                      │
                      ▼
             ┌─────────────────┐     ┌─────────────────┐
             │   Storage.js    │────►│   Service       │
             └────────┬────────┘     │   Worker        │
                      │              └────────┬────────┘
                      │                       │
                      ▼                       ▼
             ┌─────────────────┐     ┌─────────────────┐
             │   Parser.js     │────►│   Content       │
             │   Generator.js  │     │   Script        │
             └─────────────────┘     └─────────────────┘
```

**External Dependencies:**

| Library        | Version | Purpose                 | Size          |
| -------------- | ------- | ----------------------- | ------------- |
| argon2-browser | ^1.18.0 | Argon2id key derivation | ~200KB (WASM) |

**Browser APIs Used:**

- `crypto.subtle` - AES-256-GCM encryption, PBKDF2 fallback
- `chrome.storage.sync` - Encrypted data sync
- `chrome.runtime` - Message passing
- `navigator.clipboard` - Copy to clipboard

---

## 12. Success Metrics

### 12.1 Functional Metrics

| Metric                        | Target | Measurement    |
| ----------------------------- | ------ | -------------- |
| Formula detection accuracy    | > 95%  | Test suite     |
| Password generation accuracy  | 100%   | Test suite     |
| Encryption/decryption success | 100%   | Test suite     |
| Cross-device sync success     | > 99%  | Manual testing |

### 12.2 Performance Metrics

| Metric                            | Target      | Notes                 |
| --------------------------------- | ----------- | --------------------- |
| Cell click → popup display        | < 300ms     | When already unlocked |
| **Unlock time (Argon2id)**        | 2-3 seconds | Memory-hard, secure   |
| **Unlock time (PBKDF2 fallback)** | 1-2 seconds | 600k iterations       |
| Password calculation              | < 50ms      | After unlock          |
| Extension load time               | < 500ms     | Includes WASM load    |

**Note:** Unlock time tăng so với typical extensions làtrade-off có chủ đích cho security. User chỉ cần unlock 1 lần per session (10 phút).

### 12.3 User Experience Metrics

| Metric                | Target | Measurement   |
| --------------------- | ------ | ------------- |
| Setup completion rate | > 90%  | Analytics     |
| Daily active users    | Track  | Analytics     |
| Error rate            | < 1%   | Error logging |

---

## 13. Risks & Mitigations

| Risk                         | Likelihood | Impact | Mitigation                                              |
| ---------------------------- | ---------- | ------ | ------------------------------------------------------- |
| Google Sheets DOM changes    | High       | High   | Multiple detection strategies, regular monitoring       |
| Chrome API changes           | Medium     | High   | Follow Manifest V3 best practices, monitor deprecations |
| User forgets master password | Medium     | High   | Clear warnings, no recovery by design                   |
| Storage sync issues          | Low        | Medium | Local fallback, conflict resolution                     |
| Performance on large sheets  | Medium     | Medium | Debounce, efficient selectors                           |
| Argon2 WASM fails to load    | Low        | Medium | PBKDF2 600k fallback                                    |
| Chrome account compromise    | Low        | High   | Argon2id makes offline brute-force very difficult       |
| Malicious extensions         | Medium     | High   | Document limitations, recommend trusted extensions only |

---

## 14. Future Enhancements (Post-MVP)

| Feature                            | Priority | Complexity |
| ---------------------------------- | -------- | ---------- |
| Auto-fill passwords in login forms | High     | High       |
| Support Excel Online               | Medium   | Medium     |
| Support Notion tables              | Medium   | Medium     |
| Password strength indicator        | Low      | Low        |
| Export/import secrets              | Medium   | Low        |
| Multiple profiles                  | Low      | Medium     |
| Biometric unlock (WebAuthn)        | Low      | High       |
| Dark mode                          | Low      | Low        |

---

## 15. Appendix

### A. Glossary

| Term                   | Definition                                                      |
| ---------------------- | --------------------------------------------------------------- |
| **Recipe** (Formula)   | Chuỗi định dạng lưu trong sheet: `<hash><position><secret_num>` |
| **Ingredient** (Hash)  | Phần ngẫu nhiên của công thức, ví dụ: `r4nd0m`                  |
| **Secret Spice**       | Chuỗi bí mật do user định nghĩa, ví dụ: `Basic*`                |
| **Cooking Style**      | Ký tự xác định vị trí ghép secret: `#@$%^`                      |
| **Topping** (Modifier) | Ký tự thay đổi cách xử lý: `_!?~`                               |
| **Master Key**         | Mật khẩu chính để encrypt/decrypt secrets                       |

### B. Related Documents

- SystemDesign.md - Tài liệu gốc về hệ thống password
- SystemDesign_Enhanced.md - Hướng dẫn chi tiết

### C. Revision History

| Version | Date     | Author | Changes                                                                                                                                                       |
| ------- | -------- | ------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1.0     | Jan 2025 | -      | Initial MVP PRD                                                                                                                                               |
| 1.1     | Jan 2025 | -      | Security enhancements: Argon2id KDF, PBKDF2 600k fallback, reduced session timeout (10 min), idle auto-lock (5 min), added security limitations documentation |

---

**End of Document**

## 16. Technical Documentation

For detailed engineering observations, debugging logs, and solutions to specific issues (Manifest V3 CSP, WASM, Shadow DOM, etc.), please refer to `docs/technical_insights.md`.
