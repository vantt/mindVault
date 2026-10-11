# Security Assessment: Password System + Chrome Extension

## Implemented controls — 2026-10-12

The [current browser contract](specs/extension-security.md) supersedes historical
implementation suggestions below: Argon2id-only fail-closed authentication, five
failed attempts/five-minute cooldown, authoritative session deadlines, guarded
encrypted writes and clipboard compare-and-clear after copying-page closure.
Independent147-unit/25-E2E acceptance and separate real Google/native proof are
recorded in the [verification report](../archive/plans/261012-extension-completion/reports/verification.md).
These controls do not establish universal safety: weak master passwords, offline
attacks, compromised devices and OS clipboard history remain outside their protection.
Cross-device Sync and broad platform/release verification remain pending.

## 1. Executive Summary

The proposed system (Google Sheet + Chrome Extension) implements a **Split Knowledge** architecture. This is inherently more secure than storing plain text passwords, but slightly less secure than a "purely mental" approach due to the introduction of a digital artifact (the extension database) that stores the secrets.

**Verdict:** The system is **SECURE** for most threat models, provided the Master Password is strong.
**User's Hypothesis:** _"Google Sheet and Extension are separate. Attacker won't know about the tool."_
**Assessment:** This is **"Security by Obscurity"**. While it adds a layer of confusion, it is **NOT** a valid primary security control. However, even if the attacker _knows_ about the tool, they still cannot access the accounts without cracking the Extension's Master Password.

---

## 2. Analysis of the User's "Obscurity" Argument

**User's Claim:** _"Kẻ tấn công lạ sẽ không hề biết sự tồn tại của một tool hỗ trợ."_ (An unknown attacker will now know the existence of a support tool.)

**Reality Check:**

1.  **The "Formula" Leak:** A leaked spreadsheet containing rows like `fb2024#1`, `gmail24@2` follows a recognizable **Structured Pattern**. An intelligent attacker will deduce that `#1`, `@2` are indicators for a generation algorithm, even if they've never seen "SecretHash" before.
2.  **Deduction vs. Tool:** The attacker doesn't need the _specific_ tool to understand the threat. They just need to realize "This is a formula." Once they realize that, they know they are missing a "Key" (the Secret Phrase).
3.  **The Tool is just a Calculator:** Whether the attacker uses your tool, their own script, or a pen and paper, the barrier is the same: **They don't have the Secret Phrase.**

**Conclusion on Obscurity:** It helps against "script kiddies" or automated scrapers, but does **nothing** against a targeted human attacker. Do not rely on it.

## 2.1 Deep Dive: Targeted Human Attack (Tấn Công Có Chủ Đích)

Hệ thống này dựa trên giả định _"Kẻ tấn công không biết tool là gì"_. Tuy nhiên, hãy xem xét kịch bản **Targeted Human Attack**:

**Kịch bản:**
Bạn là mục tiêu cụ thể (ví dụ: bạn làm việc trong dự án crypto, hoặc là nhân vật quan trọng). Kẻ tấn công không phải là bot quét ngẫu nhiên, mà là con người (hacker) đang theo dõi bạn.

1.  **Reconnaissance (Trinh sát):**

    - Họ đã hack được máy tính của bạn hoặc Google Sheets.
    - Họ thấy các dòng: `binance24%4`, `vcb$3`.
    - Họ thấy Chrome Extension "SecretHash" đã được cài đặt trên trình duyệt của bạn (có thể check qua Extension list nếu đã vào được máy).

2.  **Reverse Engineering (Dịch ngược):**

    - Kể cả khi không biết extension này hoạt động ra sao, họ có thể tải source code (vì extension lưu trên máy) hoặc đơn giản là đọc file `manifest.json`, `content.js`.
    - Họ sẽ thấy ngay logic: `Hash + Secret = Password`.

3.  **The Choke Point (Điểm nghẽn):**

    - Lúc này, _"sự tồn tại của tool"_ không còn là bí mật.
    - Bức tường bảo vệ **DUY NHẤT** còn lại là file mã hóa chứa Secret Phrases.
    - Nếu Master Password của bạn yếu (ví dụ: `123456` hoặc ngày sinh), họ sẽ crack nó trong tích tắc bằng cách brute-force file dữ liệu đã lấy trộm được.

**Kết luận:** Đối với Targeted Attack, yếu tố "Obscurity" (giấu tool) hoàn toàn vô dụng. An toàn của bạn phụ thuộc vào **độ mạnh của Master Password** và thuật toán mã hóa (Argon2id).

---

## 3. Security Model Breakdown

### A. The Three Components

1.  **Public/Shared Component (The Sheet):** Contains the "Salt" (`r4nd0m`) and "Algorithm ID" (`#1`).
2.  **Private Component (The Extension):** Contains the "Keys" (Secret Phrases), encrypted at rest.
3.  **Authentication Component (Master Password):** The key to unlock the Private Component.

### B. Threat Scenarios

| Scenario                 | Attacker's Access                                       | Outcome                                                            | Risk Level  |
| :----------------------- | :------------------------------------------------------ | :----------------------------------------------------------------- | :---------- |
| **1. Sheet Leak Only**   | Attacker sees `r4nd0m#1`. Does not have Secret Phrases. | **SAFE.** Cannot regenerate password.                              | 🟢 Low      |
| **2. Computer Theft**    | Attacker has the physical machine + Extension data.     | **SAFE** (Temporarily). Must crack Master Password to get Secrets. | 🟡 Medium   |
| **3. Sheet + Computer**  | Attacker has everything.                                | **DEPENDS** on Master Password strength. If cracked -> Game Over.  | 🔴 High     |
| **4. Keylogger/Malware** | Malware captures Keystrokes or Clipboard.               | **UNSAFE.** Can capture Master Password or generated passwords.    | ⚫ Critical |

### C. Comparison: Manual vs. Extension

| Feature                | Manual System (Mental)            | Extension System                                 |
| :--------------------- | :-------------------------------- | :----------------------------------------------- |
| **Storage of Secrets** | Brain Only (No physical evidence) | Encrypted File on Disk (Potential attack target) |
| **Convenience**        | Low (Calculate manually)          | High (1-click)                                   |
| **Human Error**        | High (Typo risk)                  | Low (Automated)                                  |
| **Clipboard Risk**     | Low (Typing directly)             | High (Copy-paste exposes to clipboard monitors)  |
| **Phishing Risk**      | Neutral                           | Neutral                                          |

---

## 4. Recommendations & Hardening

To validate the safety of this method, implement the following (some are already in your PRD):

1.  **Argon2id is Critical:** Since the Extension stores the secrets on disk, a thief can try to brute-force the file offline. Using `Argon2id` (as mentioned in PRD v1.1) is **essential** to prevent this.
2.  **Aggressive Clipboard Clearing:** The extension copies the password. If the user doesn't paste immediately, or if the clipboard history is saved (Windows often does this), the password leaks.
    - _Fix:_ Auto-clear clipboard after 30s is good, but consider creating a specialized "Paste" event if possible to avoid clipboard entirely (hard on Web, but `document.execCommand('insertText')` might work in some contexts).
3.  **Fake Data / PDF:** The "Obscurity" argument works best if the Sheet doesn't _look_ like a password sheet.
    - _Tip:_ Name the columns "Product Code", "Inventory ID", "SKU" instead of "Formula", "Account".
    - `Facebook` -> `Item: FB-ADS-Managers`
    - `r4nd0m#1` -> `SKU-R4ND0M-01`
    - This _actively_ misleads the attacker, which is better than just hoping they don't notice.

## 5. Final Verdict

The method is **Safe for Daily Use**.
The vulnerability introduced by the Extension (storing encrypted secrets on disk) is an acceptable trade-off for the massive gain in usability and reduction of human error. The "Obscurity" argument is technically weak but practically useful; however, the system's security stands on the strength of the **Cryptography (Argon2id + AES)**, not the unexpectedness of the tool.

## 6. Opportunities for Higher Reliability (Nâng cấp)

Để hệ thống trở nên "Reliable" (đáng tin cậy) hơn nữa trước các mối đe dọa cao cấp, bạn có thể áp dụng 3 chiến lược sau:

### 6.1 Chiến thuật "Honeytokens" (Bẫy Mật Ngọt) - _Low Tech, High Value_

Đừng chỉ phòng thủ, hãy đặt bẫy.

- **Cách làm:** Tạo 1-2 dòng trong Sheet cho tài khoản "ngon ăn" nhưng giả mạo.
  - Ví dụ: `Coinbase_Admin | formula_gia`
  - `AWS_Root_Key | formula_gia`
- **Cơ chế:** Dùng một email hoặc username _chỉ tồn tại trong dòng này_. Nếu có bất kỳ nỗ lực đăng nhập nào vào tài khoản đó (hoặc email đó nhận được mail reset pass), bạn biết ngay lập tức là **Sheet đã bị lộ**.
- **Tác dụng:** Cảnh báo sớm (Early Warning System).

### 6.2 Chiến thuật "Manual Peppering" (Gia vị Thủ công) - _Split Execution_

Khôi phục lại lớp bảo mật "trong đầu" mà Extension đã vô tình làm yếu đi.

- **Cách làm:** Quy ước một chuỗi ngắn (Pepper) mà _chỉ bạn biết_, Extension _không biết_.
- **Quy trình:**
  1.  Extension tính toán & copy: `Basic*r4nd0m`
  2.  Bạn Paste vào ô password.
  3.  Bạn tự gõ thêm: `!99` (Pepper của bạn).
  4.  Password cuối cùng: `Basic*r4nd0m!99`.
- **Tác dụng:** Kể cả khi Hacker có trọn bộ: Máy tính + Master Password + Extension DB -> Họ vẫn **KHÔNG THỂ** đăng nhập được vì thiếu `!99`. Đây là lớp bảo vệ tuyệt đối chống lại kịch bản "Keylogger/Malware" ở mức độ nhất định (nếu keylogger không bắt được lúc bạn gõ pepper).

### 6.3 Chiến thuật "Hardware Binding" (WebAuthn) - _High Tech_

Thay vì dùng Master Password (thứ bạn có thể quên hoặc bị lộ), hãy dùng phần cứng.

- **Cách làm:** Nâng cấp Extension để support **WebAuthn** (Windows Hello, TouchID, YubiKey).
- **Cơ chế:** Khóa mã hóa (Encryption Key) sẽ được bọc bởi TPM (Chip bảo mật) trên máy tính.
- **Tác dụng:** Dữ liệu Extension chỉ có thể mở **trên chính máy tính này**. Nếu Hacker copy file dữ liệu sang máy khác, file đó là rác vô nghĩa. Chống hoàn toàn việc "copy trộm dữ liệu".

## 7. Discussion: Remote Server Architecture? (Thảo luận)

**Câu hỏi:** _Tại sao không để Server giữ Secret và tính toán? Extension chỉ gửi `Hash` lên và nhận về `Password`?_

**Phân tích:**

1.  **Vấn đề lớn nhất: Trust (Niềm tin)**

    - Để Server tính được `Hash + Secret`, Server buộc phải biết `Secret` (dạng plaintext lúc tính toán).
    - Điều này vi phạm nguyên tắc **Zero Knowledge**. Admin của Server có thể âm thầm ghi lại Password của bạn.
    - Hiện tại (Local Extension): Chỉ có BẠN giữ chìa khóa. Server Google/Sync chỉ giữ cục dữ liệu mã hóa mà họ không đọc được.

2.  **Single Point of Failure (Điểm chết duy nhất)**

    - Server bị hack -> Toàn bộ Secret của mọi user bị lộ.
    - Server "sập" (DDoS, lỗi, hết tiền thuê) -> Bạn mất khả năng đăng nhập vào TẤT CẢ tài khoản.

3.  **Network Risk (Rủi ro mạng)**
    - Mỗi lần login là một lần gửi request. Hacker có thể không sniff được nội dung (do HTTPS) nhưng biết bạn đang login vào đâu, tần suất ra sao (MetaData).

**Kết luận:** Đúng như bạn nhận định, phương án Remote **"Rườm rà mà không thêm bảo mật"**. Nó chuyển rủi ro từ "Máy của bạn" sang "Server của người khác" - và trong bảo mật cá nhân, "Server của người khác" thường kém tin cậy hơn chính mình.
