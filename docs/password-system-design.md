# HƯỚNG DẪN HỆ THỐNG QUẢN LÝ PASSWORD

## Phương pháp Special Character + Number

---

## 📋 MỤC LỤC

1. [Tổng quan hệ thống](#tổng-quan-hệ-thống)
2. [Cách thức hoạt động](#cách-thức-hoạt-động)
3. [Hướng dẫn từng bước](#hướng-dẫn-từng-bước)
4. [Quản lý vàlưu trữ](#quản-lý-và-lưu-trữ)
5. [Version Control](#version-control)
6. [Rotation System](#rotation-system)
7. [Backup vàRecovery](#backup-và-recovery)
8. [Best Practices](#best-practices)

---

## 🎯 TỔNG QUAN HỆ THỐNG

### Bối cảnh và Động lực

Trong quy trình vận hành hiện tại, mật khẩu thường được tạo bởi các công cụ generator và lưu trữ trực tiếp dưới dạng văn bản thuần túy (plaintext) trên Google Sheets để tối ưu sự tiện lợi và tiết kiệm nguồn lực. Tuy nhiên, cách làm này tiềm ẩn rủi ro bảo mật cực lớn: **chỉ cần quyền truy cập vào Sheet là có thể chiếm đoạt toàn bộ tài khoản.**

Hệ thống này thay đổi cách tiếp cận để bảo vệ thông tin:

- **Biến Sheet thành "bản đồ", không phải "kho chứa":** Thay vì lưu mật khẩu thật, chúng ta chỉ lưu một phần mật khẩu bao gồm các chuỗi Hash đã tạo và công thức ghép nối.
- **Mật khẩu là sự kết hợp:** Password thực tế là sự hòa trộn giữa chuỗi Hash (trên Sheet) và các Secret Phrase (chỉ nằm trong đầu bạn).
- **Quy tắc ẩn:** Vị trí, trình tự và loại Secret được sử dụng là những thông tin "không bao giờ được ghi lại".

Với phương pháp này, dù kẻ xấu có cầm trong tay toàn bộ nội dung Sheet, họ cũng không thể đăng nhập vì thiếu đi "phần khuyết" nằm trong trí nhớ của bạn. Công cụ này được thiết kế để giúp bạn tái tạo lại mật khẩu thật từ công thức một cách nhanh chóng và chính xác nhất.

### Hệ thống này là gì?

Đây là một phương pháp **quản lý password thông minh** cho phép bạn:

- ✅ **Ghi nhớ 1 lần**, sử dụng cho nhiều tài khoản
- ✅ **Không lưu password thật** ở bất kỳ đâu
- ✅ **Tự động tạo** password khác nhau cho mỗi tài khoản
- ✅ **Dễ dàng rotate** (thay đổi định kỳ) khi cần

### Ý tưởng cốt lõi

Thay vì lưu password thật, bạn chỉ lưu **"công thức"**, cách để tạo password. Khi cần, bạn kết hợp công thức này với **secret phrase** (chỉ bạn biết) để tạo ra password thật.

**Công thức lưu trữ:**

```
<random_hash><ký_tự_đặc_biệt><số>[_version]
```

**Ví dụ:**

```
r4nd0m#1  ← Đây là công thức (lưu trong sheet)
```

Khi kết hợp với Secret Phrase, tạo ra password thật:

```
r4nd0m#1 + Secret("Basic*") = Basic*r4nd0m
```

---

## 🔧 CÁCH THỨC HOẠT ĐỘNG

### Thành phần của công thức

Mỗi công thức gồm **4 phần chính**:

```
┌─────────────┬──────────┬──────┬──────────┐
│ Random Hash │ Ký tự ĐB │ Số   │ Version  │
│  (r4nd0m)   │   (#)    │ (1)  │ (_v2)    │
└─────────────┴──────────┴──────┴──────────┘
      ↓            ↓         ↓        ↓
   Cơ sở        Vị trí    Loại     Lần thay
                đặt       secret   đổi
```

### 1. Random Hash (Cơ sở)

- **Làgì:** Chuỗi ký tự ngẫu nhiên, duy nhất cho mỗi tài khoản (thường hay được generate bởi các app password generator)
- **Ví dụ:** `r4nd0m`, `h4sh3s`, `c0d3s`, `p4ssw0rd`
- **Mục đích:** Làm nền để ghép với secret phrase

### 2. Ký tự đặc biệt (Vị trí đặt Secret)

| Ký tự | Ý nghĩa                | Cách nhớ                                |
| ----- | ---------------------- | --------------------------------------- |
| `#`   | Đặt secret ở **đầu**   | Dấu thăng (#) giống mũi tên lên ↑ → đầu |
| `@`   | Đặt secret ở **giữa**  | Chữ @ tròn → ở giữa                     |
| `$`   | Đặt secret ở **cuối**  | $ giống chữ S → cuối (South)            |
| `%`   | **Đan xen từng ký tự** | % như bánh xe quay → xen kẽ             |
| `^`   | **Đan xen từng cặp**   | ^ như mũi tên 2 đầu ↔ → xen cặp         |

### 3. Số (Phân loại Secret)

Thực tế, các con số này không hẳn là phân loại cứng nhắc, mà đơn giản là số thứ tự của các Secret Phrase bạn sở hữu. Bạn có thể có nhiều hơn 5 Secret, tuy nhiên việc gán nhãn mức độ bảo mật giúp bạn dễ dàng quyết định nên dùng "chìa khóa" nào cho tài khoản cụ thể.

| Số  | Mức độ bảo mật | Loại tài khoản       | Secret ví dụ |
| --- | -------------- | -------------------- | ------------ |
| `1` | **Thấp**       | Mạng xã hội, forum   | `Basic*`     |
| `2` | **Trung bình** | Email, cloud storage | `Secure#`    |
| `3` | **Cao**        | Ngân hàng, tài chính | `Ultra$`     |
| `4` | **Đặc biệt**   | Trading, crypto      | `Trade&`     |
| `5` | **Khẩn cấp**   | Backup, recovery     | `Backup@`    |

### 4. Version (Tùy chọn)

- **Là gì:** Đánh dấu lần thay đổi password. Vd: các mật khẩu ngân hàng hay bắt đổi định kỳ.
- **Ví dụ:** `_v1`, `_v2`, `_v3`, `_vU1` (urgent), `_vB1` (backup)
- **Khi nào dùng:** Khi rotate password định kỳ

### 5. Tại sao dùng cấu trúc công thức này?

Sau khi cân nhắc kỹ lưỡng, chúng tôi quyết định chuẩn hóa công thức theo định dạng:

```
<hash><ký_tự_đặc_biệt><số>[_version]
```

**Ví dụ:** `r4nd0m#1_v2` thay vì `r4nd0m_v2#1`.

**Lý do ưu việt:**

1.  **An toàn khi nhận diện (Parsing Safety):**

    - Ký tự đặc biệt (`#`, `@`, `$`,...) đóng vai trò là **vách ngăn (delimiter)** cứng.
    - Bất cứ thứ gì nằm **trước** ký tự đặc biệt chắc chắn là **DATA** (Input Hash).
    - Bất cứ thứ gì nằm **sau** là **METADATA** (Cấu hình & Version).
    - Điều này giúp loại bỏ sự nhầm lẫn: Người dùng sẽ không bao giờ lưỡng lự "liệu mình có phải gõ chữ `_v2` vào mật khẩu không?".

2.  **Tư duy mạch lạc (Logical Flow):**

    - Cấu trúc tuân theo thứ tự tự nhiên: `Input` -> `Function` -> `Tag`.
    - **Input**: `r4nd0m` (Dữ liệu gốc).
    - **Function**: `#1` (Xử lý: ghép đầu với secret 1).
    - **Tag**: `_v2` (Gắn nhãn phiên bản).

3.  **Khả năng mở rộng (Scalability):**
    - Dễ dàng thêm các modifiers khác vào cuối mà không làm rối phần Input.
    - Ví dụ tương lai: `r4nd0m#1!_v2` (Apply Rule #1 -> Modifier ! -> Version 2).

---

## 📖 HƯỚNG DẪN TỪNG BƯỚC

### BƯỚC 1: Chuẩn bị Secret Phrases

**Tạo 5 secret phrases** (chỉ lưu trong đầu hoặc nơi an toàn):

```
Secret 1 (Bảo mật thấp):     Basic*
Secret 2 (Bảo mật trung):    Secure#
Secret 3 (Bảo mật cao):      Ultra$
Secret 4 (Giao dịch):        Trade&
Secret 5 (Backup):           Backup@
```

**Lưu ý quan trọng:**

- **KHÔNG BAO GIỜ** lưu secret phrases trong sheet
- **KHÔNG BAO GIỜ** chia sẻ secret với ai
- Chỉ lưu trong đầu hoặc password manager riêng

### BƯỚC 2: Tạo công thức cho tài khoản

**Ví dụ: Tạo password cho Facebook**

1. **Tạo random hash:** `r4nd0m` (6-12 ký tự)
2. **Chọn vị trí:** `#` (đặt ở đầu)
3. **Chọn loại secret:** `1` (mạng xã hội - bảo mật thấp)
4. **Công thức hoàn chỉnh:** `r4nd0m#1`

→ Lưu `r4nd0m#1` vào sheet ghi chú

### BƯỚC 3: Tạo password thật

**Khi cần đăng nhập Facebook:**

1. Xem công thức trong sheet: `r4nd0m#1`
2. Phân tích:
   - Hash: `r4nd0m`
   - Vị trí: `#` (đầu)
   - Secret: `1` → `Basic*`
3. Kết hợp: `Basic*` + `r4nd0m` = `Basic*r4nd0m`
4. Nhập password: **Basic\*r4nd0m**

---

## 🎨 VÍ DỤ CHI TIẾT CÁC KIỂU KẾT HỢP

### Kiểu 1: Vị trí # (Đặt ở đầu)

**Công thức:** `r4nd0m#1`

```
┌──────────┐     ┌─────────┐
│ Secret 1 │  +  │  Hash   │
│ Basic*   │     │ r4nd0m  │
└──────────┘     └─────────┘
      ↓                ↓
  ┌────────────────────────┐
  │   Basic*r4nd0m         │ ← Password thật
  └────────────────────────┘
```

**Cách làm:**

1. Lấy Secret 1: `Basic*`
2. Lấy hash: `r4nd0m`
3. Ghép: Secret + Hash = `Basic*r4nd0m`

---

### Kiểu 2: Vị trí @ (Đặt ở giữa)

**Công thức:** `r4nd0m@2`

```
┌────────┬──────────┬────────┐
│  r4n   │ Secure#  │  d0m   │
└────────┴──────────┴────────┘
    ↓         ↓          ↓
  ┌──────────────────────────┐
  │   r4nSecure#d0m          │ ← Password thật
  └──────────────────────────┘
```

**Cách làm:**

1. Lấy Secret 2: `Secure#`
2. Chia hash thành 2 phần bằng nhau:
   - Phần 1: `r4n` (3 ký tự)
   - Phần 2: `d0m` (3 ký tự)
3. Ghép: Phần 1 + Secret + Phần 2 = `r4nSecure#d0m`

---

### Kiểu 3: Vị trí $ (Đặt ở cuối)

**Công thức:** `r4nd0m$3`

```
┌─────────┐     ┌──────────┐
│  Hash   │  +  │ Secret 3 │
│ r4nd0m  │     │  Ultra$  │
└─────────┘     └──────────┘
      ↓                ↓
  ┌────────────────────────┐
  │   r4nd0mUltra$         │ ← Password thật
  └────────────────────────┘
```

**Cách làm:**

1. Lấy hash: `r4nd0m`
2. Lấy Secret 3: `Ultra$`
3. Ghép: Hash + Secret = `r4nd0mUltra$`

---

### Kiểu 4: Vị trí % (Đan xen từng ký tự)

**Công thức:** `r4nd0m%4`

```
Hash:   r    4    n    d    0    m
        ↓    ↓    ↓    ↓    ↓    ↓
Secret: T    r    a    d    e    &
        ↓    ↓    ↓    ↓    ↓    ↓
Result: rT + 4r + na + dd + 0e + m&

Final: rT4rnadd0em&
```

**Cách làm từng bước:**

1. **Chuẩn bị:**

   - Hash: `r 4 n d 0 m` (6 ký tự)
   - Secret: `T r a d e &` (6 ký tự)

2. **Đan xen:**

   - Vị trí 1: `r` + `T` = `rT`
   - Vị trí 2: `4` + `r` = `4r`
   - Vị trí 3: `n` + `a` = `na`
   - Vị trí 4: `d` + `d` = `dd`
   - Vị trí 5: `0` + `e` = `0e`
   - Vị trí 6: `m` + `&` = `m&`

3. **Kết quả:** `rT4rnadd0em&`

**Trường hợp đặc biệt:**

- Secret ngắn hơn hash → Lặp lại secret
- Secret dài hơn hash → Cắt bớt secret

**Ví dụ Secret ngắn hơn:**

```
Hash:   r 4 n d 0 m  (6 ký tự)
Secret: A B          (2 ký tự)
Lặp:    A B A B A B  (lặp lại)
Result: rA4BnAdB0AmB
```

---

### Kiểu 5: Vị trí ^ (Đan xen từng cặp)

**Công thức:** `r4nd0m^4`

```
Hash:   (r4)  (nd)  (0m)
         ↓     ↓     ↓
Secret: (Tr)  (ad)  (e&)
         ↓     ↓     ↓
Result: r4Tr + ndad + 0me&

Final: r4Trndad0me&
```

**Cách làm từng bước:**

1. **Chia thành cặp:**

   - Hash: `(r4) (nd) (0m)` - 3 cặp
   - Secret: `(Tr) (ad) (e&)` - 3 cặp

2. **Đan xen cặp:**

   - Cặp 1: `r4` + `Tr` = `r4Tr`
   - Cặp 2: `nd` + `ad` = `ndad`
   - Cặp 3: `0m` + `e&` = `0me&`

3. **Kết quả:** `r4Trndad0me&`

**Xử lý hash lẻ:**

```
Hash: r4nd0m5 (7 ký tự)
Chia: (r4) (nd) (0m) (5)
       ↓    ↓    ↓    ↓
      r4   nd   0m    5  ← ký tự lẻ giữ nguyên
```

---

## 📂 QUẢN LÝ VÀ LƯU TRỮ

### Cấu trúc lưu trữ

**Sheet ghi chú** (có thể lưu bất kỳ đâu: Google Sheet, Excel, Notion...)

```
┌──────────────┬───────────────┬─────────┬────────────┬─────────────┐
│ Tài khoản    │ Công thức     │ Version │ Ngày đổi   │ Ghi chú     │
├──────────────┼───────────────┼─────────┼────────────┼─────────────┤
│ Facebook     │ r4nd0m#1      │ v1      │ 2024-01-15 │ -           │
│ Gmail        │ h4sh3s@2_v2   │ v2      │ 2024-06-01 │ Nghi ngờ lộ │
│ Bank         │ p4ssw0$3_v3   │ v3      │ 2024-09-15 │ Rotate định │
│ Binance      │ cr7pt0%4      │ v1      │ 2024-03-20 │ -           │
│ PayPal       │ m0n3y$3       │ v1      │ 2024-02-10 │ -           │
└──────────────┴───────────────┴─────────┴────────────┴─────────────┘
```

### Secret Phrases (lưu riêng, TUYỆT ĐỐI BẢO MẬT)

```
┌────────┬─────────────────┬──────────────────────────┐
│ Số     │ Secret Phrase   │ Sử dụng cho              │
├────────┼─────────────────┼──────────────────────────┤
│ 1      │ Basic*          │ Mạng xã hội, forum       │
│ 2      │ Secure#         │ Email, cloud storage     │
│ 3      │ Ultra$          │ Ngân hàng, tài chính     │
│ 4      │ Trade&          │ Trading, crypto          │
│ 5      │ Backup@         │ Backup, recovery         │
└────────┴─────────────────┴──────────────────────────┘
```

### Workflow thực tế

**Khi tạo tài khoản mới:**

```
1. Tạo random hash → r4nd0m
2. Chọn vị trí + số → #1
3. Công thức: r4nd0m#1
4. Lưu vào sheet
5. Tạo password thật: Basic*r4nd0m
6. Dùng để đăng ký
```

**Khi cần đăng nhập:**

```
1. Mở sheet → tìm tài khoản
2. Xem công thức: r4nd0m#1
3. Nhớ lại secret 1: Basic*
4. Tạo password: Basic*r4nd0m
5. Đăng nhập
```

---

## 🔄 VERSION CONTROL

### Tại sao cần version?

- ✅ Rotate password định kỳ (đổi mật khẩu)
- ✅ Xử lý khi nghi ngờ bị lộ
- ✅ Theo dõi lịch sử thay đổi
- ✅ Dễ dàng rollback nếu cần

### Các loại version

```
┌──────────┬─────────────────────────────────────┐
│ Ký hiệu  │ Ý nghĩa                             │
├──────────┼─────────────────────────────────────┤
│ v1       │ Version gốc (ban đầu)               │
│ v2, v3   │ Version thứ 2, 3... (rotate thường) │
│ vU1      │ Update khẩn cấp (Urgent)            │
│ vB1      │ Version backup                      │
└──────────┴─────────────────────────────────────┘
```

### Pattern rotation theo thời gian

**Phương pháp 1: Theo quý**

```
Version gốc:     Basic*
Version Q2-2024: Basic*Q224
Version Q3-2024: Basic*Q324
Version Q4-2024: Basic*Q424
Version Q1-2025: Basic*Q125
```

**Công thức trong sheet:**

```
r4nd0m#1_v1  → Basic*r4nd0m
r4nd0m#1_v2  → Basic*Q224r4nd0m
r4nd0m#1_v3  → Basic*Q324r4nd0m
```

**Phương pháp 2: Theo tháng**

```
Version gốc:  Basic*
May 2024:     Basic*0524
August 2024:  Basic*0824
November 2024: Basic*1124
```

**Công thức trong sheet:**

```
r4nd0m#1_v1  → Basic*r4nd0m
r4nd0m#1_v2  → Basic*0524r4nd0m
r4nd0m#1_v3  → Basic*0824r4nd0m
```

### Ví dụ thực tế version control

**Case study: Tài khoản Bank**

```
Tháng 1/2024:  h4sh3s$3_v1  → Ultra$h4sh3s
Tháng 4/2024:  h4sh3s$3_v2  → Ultra$Q124h4sh3s  (rotate định kỳ)
Tháng 7/2024:  h4sh3s$3_v3  → Ultra$Q224h4sh3s  (rotate định kỳ)
Tháng 10/2024: h4sh3s$3_v4  → Ultra$Q324h4sh3s  (rotate định kỳ)
```

**Sheet theo dõi:**

```
┌──────┬────────────────┬─────────┬────────────┬────────────┐
│ Bank │ h4sh3s$3_v4    │ v4      │ 2024-10-01 │ Rotate Q3  │
└──────┴────────────────┴─────────┴────────────┴────────────┘
```

---

## ♻️ ROTATION SYSTEM

### Lịch rotate theo loại tài khoản

```
┌────────────────────┬──────────────┬─────────────────────────┐
│ Loại tài khoản     │ Tần suất     │ Lý do                   │
├────────────────────┼──────────────┼─────────────────────────┤
│ Ngân hàng (3)      │ 3 tháng      │ BẮT BUỘC (bảo mật cao)  │
│ Trading (4)        │ 6 tháng      │ BẮT BUỘC (tài sản)      │
│ Email chính (2)    │ 6 tháng      │ BẮT BUỘC (khóa phục hồi)│
│ Cloud storage (2)  │ 12 tháng     │ Nên rotate              │
│ Mạng xã hội (1)    │ Không cần    │ Có 2FA là đủ            │
└────────────────────┴──────────────┴─────────────────────────┘
```

### Khi nào cần rotate khẩn cấp?

**Dấu hiệu cảnh báo:**

- 🚨 Có email cảnh báo "đăng nhập từ thiết bị lạ"
- 🚨 Phát hiện hoạt động bất thường
- 🚨 Website bị hack, rò rỉ database
- 🚨 Chia sẻ password cho người khác (đã xóa)
- 🚨 Đăng nhập từ máy công cộng

**Hành động:**

1. Đổi ngay version sang vU1 (urgent)
2. Update secret phrase với suffix khẩn cấp
3. Kiểm tra tất cả tài khoản liên quan

**Ví dụ:**

```
Công thức cũ:  r4nd0m#1_v2  → Basic*Q224r4nd0m
Công thức mới: r4nd0m#1_vU1 → Basic*!0624r4nd0m
                                      ↑
                                   Dấu ! = urgent
```

### Quy trình rotate chuẩn

**Bước 1: Chuẩn bị**

- ✅ Kiểm tra version hiện tại trong sheet
- ✅ Chuẩn bị secret phrase version mới
- ✅ Đảm bảo có thể đăng nhập bằng password cũ

**Bước 2: Update công thức**

- ✅ Tăng version number: v1 → v2
- ✅ Update ngày đổi
- ✅ Ghi chú lý do

**Bước 3: Đổi password trên website**

- ✅ Đăng nhập bằng password cũ
- ✅ Vào phần "Đổi mật khẩu"
- ✅ Tạo password mới từ công thức mới
- ✅ Xác nhận đổi thành công

**Bước 4: Verify**

- ✅ Đăng xuất
- ✅ Đăng nhập lại bằng password mới
- ✅ Kiểm tra các tính năng hoạt động bình thường

**Ví dụ cụ thể:**

```
═══════════════════════════════════════════════════
         ROTATE PASSWORD CHO BINANCE
═══════════════════════════════════════════════════

[Bước 1: Kiểm tra hiện tại]
Sheet: cr7pt0%4_v1
Password hiện tại: cTrade&r7Trade&p...

[Bước 2: Tạo version mới]
New formula: cr7pt0%4_v2
New secret: Trade&0624 (tháng 6/2024)
New password: cTrade&0624r7Trade&0624p...

[Bước 3: Update sheet]
Before: cr7pt0%4_v1 | v1 | 2024-03-20 | -
After:  cr7pt0%4_v2 | v2 | 2024-06-15 | Rotate định kỳ

[Bước 4: Đổi trên Binance]
1. Login với password cũ
2. Vào Security > Change Password
3. Nhập password cũ: cTrade&r7Trade&p...
4. Nhập password mới: cTrade&0624r7Trade&0624p...
5. Confirm bằng 2FA

[Bước 5: Verify]
✓ Logout
✓ Login với password mới
✓ Kiểm tra giao dịch
✓ Kiểm tra API keys
```

### Tips rotate hàng loạt

**Khi cần rotate nhiều tài khoản:**

```
1. Nhóm theo loại secret (1,2,3,4,5)
2. Update tất cả secret phrases cùng lúc
3. Rotate từng nhóm một
4. Ưu tiên: 3 (bank) → 4 (trading) → 2 (email)
```

**Template checklist:**

```
☐ Nhóm 3 (Ngân hàng):
  ☐ VCB Bank
  ☐ Techcombank
  ☐ PayPal

☐ Nhóm 4 (Trading):
  ☐ Binance
  ☐ Bybit

☐ Nhóm 2 (Email):
  ☐ Gmail chính
  ☐ Outlook work
```

---

## 🎭 BIẾN THỂ VÀ INDICATORS

### Indicators phụ (Modifier)

Thêm ký tự đặc biệt ở cuối công thức để **thay đổi cách xử lý**:

```
┌──────────┬─────────────────────────┬──────────────────────┐
│ Modifier │ Tác dụng                │ Ví dụ                │
├──────────┼─────────────────────────┼──────────────────────┤
│ _        │ Đảo ngược vị trí        │ r4nd0m#1_            │
│ !        │ Viết HOA toàn bộ secret │ r4nd0m#1!            │
│ ?        │ Đảo ngược secret        │ r4nd0m#1?            │
│ ~        │ Xóa ký tự đặc biệt      │ r4nd0m#1~            │
└──────────┴─────────────────────────┴──────────────────────┘
```

### Ví dụ chi tiết Modifiers

**1. Modifier `_` (Đảo vị trí)**

```
Normal:   r4nd0m#1   → Basic*r4nd0m    (secret ở đầu)
With _:   r4nd0m#1_  → r4nd0mBasic*    (secret ở cuối)

Normal:   r4nd0m@2   → r4nSecure#d0m   (secret ở giữa)
With _:   r4nd0m@2_  → Secure#r4nd0m   (secret ở đầu)

Normal:   r4nd0m$3   → r4nd0mUltra$    (secret ở cuối)
With _:   r4nd0m$3_  → Ultra$r4nd0m    (secret ở đầu)
```

**2. Modifier `!` (Viết hoa)**

```
Normal:   r4nd0m#1   → Basic*r4nd0m
With !:   r4nd0m#1!  → BASIC*r4nd0m

Normal:   r4nd0m@2   → r4nSecure#d0m
With !:   r4nd0m@2!  → r4nSECURE#d0m
```

**3. Modifier `?` (Đảo ngược secret)**

```
Normal:   r4nd0m#1   → Basic*r4nd0m
With ?:   r4nd0m#1?  → *cisaBr4nd0m
                        ↑
                    "Basic*" viết ngược
```

**4. Modifier `~` (Xóa ký tự đặc biệt trong secret)**

```
Normal:   r4nd0m#1   → Basic*r4nd0m
With ~:   r4nd0m#1~  → Basicr4nd0m
                        ↑
                    Xóa dấu *
```

### Kết hợp nhiều modifiers

```
r4nd0m#1_!   → r4nd0mBASIC*   (đảo vị trí + viết hoa)
r4nd0m#1?!   → *CISABr4nd0m   (đảo ngược + viết hoa)
r4nd0m#1_~   → r4nd0mBasic    (đảo vị trí + xóa ký tự đặc biệt)
```

### Quy tắc theo độ dài hash

Tự động áp dụng transform dựa vào độ dài:

```
┌──────────────┬────────────────────────────────┐
│ Độ dài hash  │ Transform tự động              │
├──────────────┼────────────────────────────────┤
│ 6 ký tự      │ Giữ nguyên rule                │
│ 8 ký tự      │ Tự động đảo ngược secret       │
│ 10 ký tự     │ Tự động viết hoa secret        │
│ 12 ký tự     │ Thêm số thứ tự vào cuối        │
└──────────────┴────────────────────────────────┘
```

**Ví dụ:**

```
abc123#1       (6 ký tự)  → Basic*abc123
abcd1234#1     (8 ký tự)  → *cisaBabcd1234     (tự động đảo)
abcd123456#1   (10 ký tự) → BASIC*abcd123456   (tự động HOA)
abcd12345678#1 (12 ký tự) → Basic*1abcd12345678 (thêm số 1)
```

---

## 💾 BACKUP VÀ RECOVERY

### Hệ thống Backup

**Tại sao cần backup?**

- 🔐 Quên version hiện tại
- 🔐 Lỗi đồng bộ giữa các thiết bị
- 🔐 Cần rollback khẩn cấp
- 🔐 Mất access vào sheet chính

### Backup Secrets

Tạo **bộ secret riêng** chỉ dùng cho backup:

```
┌─────────────┬──────────────────┬────────────────────┐
│ Secret      │ Backup Version   │ Khi nào dùng       │
├─────────────┼──────────────────┼────────────────────┤
│ Secret1     │ BasicBackup*     │ Recovery mạng xã   │
│ Secret2     │ SecureBackup#    │ Recovery email     │
│ Secret3     │ UltraBackup$     │ Recovery bank      │
│ Secret4     │ TradeBackup&     │ Recovery trading   │
│ Secret5     │ EmergencyBack@   │ Recovery tất cả    │
└─────────────┴──────────────────┴────────────────────┘
```

### Format backup trong công thức

```
<hash><type><số>_vB1

Ví dụ:
r4nd0m#1_vB1  → Dùng backup secret1 ở vị trí đầu
h4sh3s$3_vB1  → Dùng backup secret3 ở vị trí cuối
```

### Quy trình Recovery

**Tình huống 1: Quên version hiện tại**

```
1. Mở sheet, thấy: r4nd0m#1_v5
2. Không nhớ Secret1 version 5 là gì
3. Đổi sang backup: r4nd0m#1_vB1
4. Dùng BasicBackup*r4nd0m để login
5. Sau khi login, rotate lại về version mới
```

**Tình huống 2: Sheet bị mất**

```
1. Có list tài khoản nhưng không có công thức
2. Thử từng backup secret cho từng tài khoản:
   - Facebook: BackupBasic*<hash>
   - Gmail: BackupSecure#<hash>
   - Bank: BackupUltra$<hash>
3. Sau khi vào được, lập sheet mới
4. Rotate tất cả sang version mới
```

### Master Recovery Key

Tạo **1 secret đặc biệt** để recovery toàn bộ:

```
Master Recovery: MyMaster2024!Backup@Safe

Cách dùng:
- Với mọi công thức, thử: MyMaster2024!Backup@Safe<hash>
- Nếu không được, thử: <hash>MyMaster2024!Backup@Safe
- Cuối cùng thử ở giữa
```

** Lưu ý:** Master key này cực kỳ quan trọng, lưu ở:

- Password manager riêng
- Giấy viết tay trong két sắt
- File mã hóa trong USB

### Export/Import Sheet

**Định kỳ backup sheet:**

```python
# Format CSV backup
date,account,formula,version,notes
2024-06-15,Facebook,r4nd0m#1,v1,-
2024-06-15,Gmail,h4sh3s_v2@2,v2,Nghi ngờ lộ
2024-06-15,Bank,p4ssw0_v3$3,v3,Rotate định kỳ
```

**Nên backup:**

- ✅ Hàng tuần: export file CSV
- ✅ Hàng tháng: in ra giấy (công thức thôi, không in secret)
- ✅ Hàng quý: sync vào USB mã hóa

---

## ✨ BEST PRACTICES

### 1. Nguyên tắc vàng

```
┌─────────────────────────────────────────────────┐
│     10 ĐIỀU TUYỆT ĐỐI KHÔNG ĐƯỢC LÀM            │
├─────────────────────────────────────────────────┤
│ 1. KHÔNG lưu secret phrases trong sheet        │
│ 2. KHÔNG dùng chung secret cho nhiều level     │
│ 3. KHÔNG skip rotate với tài khoản quan trọng  │
│ 4. KHÔNG chia sẻ công thức với người khác      │
│ 5. KHÔNG dùng hash quá đơn giản (123456)       │
│ 6. KHÔNG quên backup sheet định kỳ             │
│ 7. KHÔNG lưu password thật ở đâu cả            │
│ 8. KHÔNG dùng cùng công thức cho nhiều site    │
│ 9. KHÔNG bỏ qua 2FA cho tài khoản quan trọng   │
│10. KHÔNG test password trên site không tin cậy │
└─────────────────────────────────────────────────┘
```

### 2. Setup ban đầu

**Checklist setup:**

```
☐ Bước 1: Tạo 5 secret phrases
  ☐ Secret1: Bảo mật thấp
  ☐ Secret2: Bảo mật trung bình
  ☐ Secret3: Bảo mật cao
  ☐ Secret4: Đặc biệt
  ☐ Secret5: Khẩn cấp

☐ Bước 2: Tạo backup secrets
  ☐ Backup cho mỗi level

☐ Bước 3: Setup sheet
  ☐ Tạo Google Sheet hoặc Excel
  ☐ Tạo các cột: Account, Formula, Version, Date, Notes
  ☐ Bảo vệ sheet bằng password riêng

☐ Bước 4: Migration
  ☐ List tất cả tài khoản hiện có
  ☐ Phân loại theo mức độ quan trọng
  ☐ Tạo công thức cho từng tài khoản
  ☐ Đổi password từng tài khoản một

☐ Bước 5: Setup calendar
  ☐ Reminder rotate Bank (3 tháng)
  ☐ Reminder rotate Trading (6 tháng)
  ☐ Reminder rotate Email (6 tháng)
  ☐ Reminder backup sheet (1 tháng)
```

### 3. Quản lý rotation hiệu quả

**Template Calendar:**

```
═══════════════════════════════════════════
            ROTATION SCHEDULE 2024
═══════════════════════════════════════════

[Q1 - Jan/Feb/Mar]
✓ Jan 15: Bank (forced)
✓ Mar 20: Trading (forced)

[Q2 - Apr/May/Jun]
☐ Apr 15: Bank (forced)
☐ Jun 01: Gmail (forced)
☐ Jun 20: Trading (forced)

[Q3 - Jul/Aug/Sep]
☐ Jul 15: Bank (forced)
☐ Sep 20: Trading (forced)

[Q4 - Oct/Nov/Dec]
☐ Oct 15: Bank (forced)
☐ Dec 01: Gmail (forced)
☐ Dec 20: Trading (forced)
```

### 4. Xử lý trường hợp đặc biệt

**Tài khoản liên kết:**

```
Ví dụ: Google Account liên kết với:
- Gmail
- Drive
- Photos
- YouTube

→ Nên dùng CÙNG công thức:
  google@2_v2  (cho tất cả service)

→ Khi rotate, đổi 1 lần cho tất cả
```

**Tài khoản công ty:**

```
Nếu công ty có policy riêng:
- Tuân thủ policy công ty trước
- Dùng hệ thống này cho note cá nhân
- Không lưu công thức công ty vào sheet cá nhân
```

**Tài khoản chia sẻ:**

```
Netflix, Spotify family...
→ Nếu bạn là owner:
  - Dùng hệ thống bình thường
  - Thông báo trước khi đổi pass

→ Nếu bạn là member:
  - Không nên dùng hệ thống này
  - Lưu password do owner cung cấp
```

### 5. Tips ghi nhớ

**Mnemonic cho vị trí:**

```
#  = Hashtag      → Cao lên → ĐẦU
@  = At           → Tròn    → GIỮA
$  = Dollar/Snake → Đuôi    → CUỐI
%  = Percent      → Quay    → XEN KẼ
^  = Caret        → 2 đầu   → XEN CẶP
```

**Mnemonic cho secret level:**

```
1 = Basic    → Mọi người đều dùng được
2 = Secure   → Cần bảo vệ
3 = Ultra    → Siêu quan trọng
4 = Trade    → Tiền bạc
5 = Backup   → Phao cứu sinh
```

### 6. Troubleshooting

**Vấn đề: Password không đúng**

```
Checklist debug:
☐ Kiểm tra version trong sheet (v1, v2, v3?)
☐ Kiểm tra secret có đúng không?
☐ Kiểm tra vị trí (#@$%^) có đúng không?
☐ Kiểm tra có modifier không? (_!?~)
☐ Kiểm tra độ dài hash có trigger rule không?
☐ Test với backup secret
```

**Vấn đề: Quên secret phrase**

```
Giải pháp:
1. Thử các backup secret
2. Thử Master Recovery Key
3. Dùng "Forgot Password" của website
4. Sau khi reset, tạo công thức mới
5. Update sheet với version mới
```

**Vấn đề: Sheet bị mất**

```
Phục hồi:
1. Check Google Drive Trash
2. Check version history (Google Sheet)
3. Dùng file backup CSV
4. Recreate từ email xác nhận của websites
5. Worst case: Reset tất cả và tạo lại
```

---

## 📊 BẢNG TỔNG HỢP NHANH

### Cheat Sheet - Công thức nhanh

```
╔═══════════════════════════════════════════════════╗
║           QUICK REFERENCE GUIDE                   ║
� ═══════════════════════════════════════════════════╣
║                                                   ║
║  Công thức: <hash><vị_trí><số>[_version]         ║
║                                                   ║
║  VỊ TRÍ:                                         ║
║  # → Đầu     @ → Giữa    $ → Cuối               ║
║  % → Xen 1   ^ → Xen cặp                        ║
║                                                   ║
║  SECRET LEVEL:                                    ║
║  1 → Basic   2 → Secure  3 → Ultra              ║
║  4 → Trade   5 → Backup                         ║
║                                                   ║
║  MODIFIERS:                                       ║
║  _ → Đảo     ! → HOA     ? → Ngược             ║
║  ~ → Xóa ký tự đặc biệt                         ║
║                                                   ║
║  EXAMPLES:                                        ║
║  r4nd0m#1     → Basic*r4nd0m                     ║
║  h4sh3s@2     → h4sSecure#h3s                    ║
║  p4ssw0$3     → p4ssw0Ultra$                     ║
║  cr7pt0%4     → cTrade&r7...                     ║
║  m0n3y^4      → m0Trade&n3...                    ║
║  r4nd0m#1_    → r4nd0mBasic*                     ║
║                                                   ║
╚═══════════════════════════════════════════════════╝
```

### Decision Tree - Chọn công thức

```
                    T� O PASSWORD MỚI
                           |
                 ┌─────────┴─────────┐
                 │                   │
        Tài khoản gì?        Quan trọng cỡ nào?
                 |                   |
        ┌────────┴────────┐   ┌─────┴─────────┐
        │        │        │   │   │   │   │   │
     Social  Email  Bank   1   2   3   4   5
        │        │        │   │   │   │   │   │
        #        @        $   Chọn secret level
        │        │        │
    Đầu     Giữa      Cuối
        │        │        │
        └────────┴────────┴── Tạo công thức
                 │
          <hash><vị_trí><số>
```

### Rotation Priority

```
┌────────────────────────────────────────┐
│  PRIORITY 1 (PHẢI ROTATE)              │
│  ├─ Bank (3 tháng)                     │
│  ├─ Trading (6 tháng)                  │
│  └─ Email chính (6 tháng)              │
├────────────────────────────────────────┤
│  PRIORITY 2 (NÊN ROTATE)               │
│  ├─ Cloud storage (12 tháng)           │
│  ├─ PayPal (12 tháng)                  │
│  └─ Work email (12 tháng)              │
├────────────────────────────────────────┤
│  PRIORITY 3 (TÙY Ý)                    │
│  ├─ Social media                       │
│  ├─ Gaming accounts                    │
│  └─ Shopping sites                     │
└────────────────────────────────────────┘
```

---

## 🎓 VÍ DỤ THỰC TẾ CHI TIẾT

### Case Study 1: User mới setup hệ thống

**Profile:**

- Tên: An
- Có 15 tài khoản
- Chưa từng dùng password manager

**Bước setup:**

```
[Tuần 1: Chuẩn bị]
✓ Tạo 5 secret phrases:
  Secret1: "An@2024*"
  Secret2: "AnSecure#24"
  Secret3: "AnBank$2024"
  Secret4: "AnTrade&24"
  Secret5: "AnBackup@24"

✓ Tạo backup secrets:
  Backup1: "AnBK1*"
  Backup2: "AnBK2#"
  ...

[Tuần 2: Tạo sheet]
✓ Google Sheet: "Password System"
✓ Columns: Account | Formula | Ver | Date | Notes
✓ Bảo vệ bằng password riêng

[Tuần 3-4: Migration]
Day 1-3: Social media (priority 3)
  ✓ Facebook:  fb2024#1
  ✓ Twitter:   tw2024#1
  ✓ Instagram: ig2024#1

Day 4-7: Email & Cloud (priority 2)
  ✓ Gmail:     gmail24@2
  ✓ Outlook:   outlook24@2
  ✓ Drive:     drive24@2

Day 8-10: Banking (priority 1)
  ✓ VCB:       vcb2024$3
  ✓ TCB:       tcb2024$3
  ✓ PayPal:    pp2024$3

Day 11-14: Trading (priority 1)
  ✓ Binance:   bnb2024%4
  ✓ Bybit:     bb2024%4

[Tuần 5: Setup calendar]
✓ Quarterly reminder: Rotate bank
✓ Bi-annual reminder: Rotate trading & email
✓ Monthly reminder: Backup sheet
```

**Sheet sau khi setup:**

```
┌───────────┬──────────────┬─────┬────────────┬────────┐
│ Account   │ Formula      │ Ver │ Date       │ Notes  │
├───────────┼──────────────┼─────┼────────────┼────────┤
│ Facebook  │ fb2024#1     │ v1  │ 2024-06-01 │ New    │
│ Gmail     │ gmail24@2    │ v1  │ 2024-06-05 │ New    │
│ VCB Bank  │ vcb2024$3    │ v1  │ 2024-06-08 │ New    │
│ Binance   │ bnb2024%4    │ v1  │ 2024-06-12 │ New    │
└───────────┴──────────────┴─────┴────────────┴────────┘
```

### Case Study 2: Rotation định kỳ

**Tình huống:**

- Tháng 9/2024
- Cần rotate VCB Bank (3 tháng 1 lần)
- Lần rotate đầu tiên

**Quy trình:**

```
[Chuẩn bị]
Current formula: vcb2024$3
Current password: vcb2024AnBank$2024
Current version: v1
Last rotate: 2024-06-08
Time passed: 3 tháng

[Tạo version mới]
New secret: AnBank$Q324 (Q3-2024)
New formula: vcb2024$3_v2
New password: vcb2024AnBank$Q324

[Execute]
1. Login VCB với password cũ: vcb2024AnBank$2024
2. Vào Settings → Security → Change Password
3. Enter old password: vcb2024AnBank$2024
4. Enter new password: vcb2024AnBank$Q324
5. Confirm với OTP/2FA
6. Logout
7. Login lại với password mới để verify

[Update sheet]
Before: vcb2024$3     | v1 | 2024-06-08 | New
After:  vcb2024$3_v2  | v2 | 2024-09-08 | Q3 rotate

[Set reminder]
Next rotate: 2024-12-08 (3 tháng sau)
```

### Case Study 3: Khẩn cấp - Nghi ngờ bị lộ

**Tình huống:**

- Nhận email: "Login từ IP lạ ở Gmail"
- Thời gian: 3:00 AM
- Nghi ngờ bị hack

**Xử lý:**

```
[Immediate Action - 5 phút đầu]
1. Kiểm tra Gmail activity
   → Confirm có login lạ

2. Xem công thức hiện tại:
   gmail24@2_v2 (version 2)

3. Logout tất cả devices khác
4. Enable 2FA nếu chưa có

[Recovery - 10 phút tiếp theo]
5. Tạo version khẩn cấp:
   Old: gmail24@2_v2 → gm12AnSecure#24 (Q2-2024)
   New: gmail24@2_vU1 → gm12AnSecure#!0624
                                      ↑
                               Urgent marker

6. Đổi password ngay:
   - Vào Google Security
   - Change password
   - Nhập password mới
   - Verify bằng phone

[Follow-up - 1 giờ sau]
7. Check các tài khoản liên quan:
   ☐ Drive
   ☐ Photos
   ☐ YouTube
   ☐ Gmail recovery email

8. Check email forwarding rules
9. Check authorized apps
10. Review recent activities

[Prevention - 24 giờ sau]
11. Rotate tất cả tài khoản dùng Secret2
12. Update backup secrets
13. Strengthen secret phrases
14. Review security practices

[Update sheet]
Before: gmail24@2_v2  | v2  | 2024-06-01 | Q2
After:  gmail24@2_vU1 | vU1 | 2024-09-15 | Urgent-Hack
```

### Case Study 4: Quên version hiện tại

**Tình huống:**

- Cần login Binance
- Không nhớ đang dùng version mấy
- Sheet chỉ ghi: bnb2024%4

**Giải quyết:**

```
[Bước 1: Thử các version có thể]
Try v1: bnb2024 + Trade&
→ bTrade&n2Trade&b0Trade&2024
→ Sai ✗

Try v2: bnb2024 + Trade&0624 (Jun 2024)
→ bTrade&0624n2Trade&0624b0Trade&06242024
→ Sai ✗

Try v3: bnb2024 + Trade&0924 (Sep 2024)
→ Sai ✗

[Bước 2: Dùng backup]
Use backup: bnb2024 + TradeBackup&
→ bTradeBackup&n2TradeBackup&b...
→ ĐÚNG ✓

[Bước 3: Sau khi login]
1. Vào Security settings
2. Check last password change date
   → Phát hiện: Last change 2024-06-15

3. Suy luận:
   - Đổi vào Q2 → phải là v2
   - Secret v2: Trade&0624

4. Update sheet:
   Before: bnb2024%4
   After:  bnb2024%4_v2 | v2 | 2024-06-15 | Found

[Bước 4: Rotate ngay]
5. Đổi sang version mới:
   New: bnb2024%4_v3
   New secret: Trade&0924

6. Update sheet đầy đủ
7. Test login với password mới
```

---

## 📞 SUPPORT & FAQ

### Câu hỏi thường gặp

**Q1: Hệ thống này có an toàn không?**

A: Có, vì:

- Password thật không được lưu ở đâu cả
- Secret phrase chỉ bạn biết
- Kể cả sheet bị lộ, hacker vẫn không tạo được password
- Mỗi tài khoản có công thức khác nhau

**Q2: Nếu quên secret phrase thì sao?**

A: Dùng hệ thống backup:

1. Thử backup secret đã lưu riêng
2. Dùng Master Recovery Key
3. Worst case: Reset password qua email/SMS
4. Sau khi vào được, tạo công thức mới

**Q3: Có cần nhớ tất cả công thức không?**

A: Không, chỉ cần:

- Nhớ 5 secret phrases (hoặc ít hơn tùy số level bạn dùng)
- Có quyền access vào sheet
- Hiểu cách đọc công thức

**Q4: Tôi có 100+ tài khoản, có khả thi không?**

A: Có, nhưng:

- Ưu tiên migrate tài khoản quan trọng trước
- Tài khoản ít dùng có thể dùng password manager thông thường
- Chỉ dùng hệ thống này cho 20-30 tài khoản quan trọng nhất

**Q5: So với password manager như LastPass, 1Password?**

A:

- **Ưu điểm:** Không phụ thuộc vào service, free, offline được
- **Nhược điểm:** Phải nhớ secret, không tự động fill
- **Kết hợp:** Dùng cả 2 - password manager cho tài khoản ít quan trọng

**Q6: Công thức có dễ bị guess không?**

A: Rất khó vì:

- Hash ngẫu nhiên
- Vị trí không đoán được (#@$%^)
- Secret phrase không ai biết
- Có thể thêm modifiers (\_!?~)

**Q7: Nếu sheet bị hack thì sao?**

A: Hacker vẫn không tạo được password vì:

- Không biết secret phrases
- Không biết cách kết hợp
- Mỗi tài khoản khác công thức

→ Nhưng nên:

- Bảo vệ sheet bằng password riêng
- Backup định kỳ
- Không share công khai

**Q8: Rotation có mất thời gian không?**

A: Mỗi lần rotate:

- Tạo công thức mới: 1 phút
- Đổi password trên site: 2-3 phút
- Update sheet: 1 phút
- **Tổng: ~5 phút/tài khoản**

Với 10 tài khoản quan trọng rotate 6 tháng/lần:
→ Chỉ mất ~1 giờ/năm

---

## 🎯 KẾT LUẬN

### Tóm tắt hệ thống

```
╔══════════════════════════════════════════════════╗
║  HỆ THỐNG QUẢN LÝ PASSWORD                       ║
║  Special Character + Number Method               ║
� ══════════════════════════════════════════════════╣
║                                                  ║
║  ✓ 1 secret → nhiều password                    ║
║  ✓ Không lưu password thật                      ║
║  ✓ Dễ rotate và maintain                        ║
║  ✓ An toàn ngay cả khi sheet bị lộ              ║
║  ✓ Backup và recovery dễ dàng                   ║
║                                                  ║
╚══════════════════════════════════════════════════╝
```

### Next Steps

**Để bắt đầu ngay:**

1. ✅ Tạo 5 secret phrases (hoặc ít hơn nếu không cần đủ 5 level)
2. ✅ Setup Google Sheet với template
3. ✅ Migrate 3-5 tài khoản quan trọng nhất
4. ✅ Practice 1 tuần để quen với hệ thống
5. ✅ Dần dần migrate các tài khoản còn lại
6. ✅ Setup calendar reminder cho rotation
7. ✅ Tạo backup secrets vàMaster Recovery Key

**Thời gian cần thiết:**

- Setup: 30 phút
- Migration: 5 phút/tài khoản
- Maintenance: ~1 giờ/năm

**Lợi ích:**

- ✅ Không cần lo nhớ 50+ passwords khác nhau
- ✅ Dễ dàng rotate khi cần
- ✅ An toàn hơn password đơn giản
- ✅ Không phụ thuộc vào service bên thứ 3
- ✅ Hoạt động offline

---

## 📚 PHỤ LỤC

### Template Google Sheet

```
┌──────────────┬────────────────┬──────────┬─────────────┬────────────────────┐
│ Account      │ Formula        │ Version  │ Last Change │ Notes              │
├──────────────┼────────────────┼──────────┼─────────────┼────────────────────┤
│ Facebook     │ fb2024#1       │ v1       │ 2024-01-15  │ -                  │
│ Gmail        │ gmail24@2      │ v2       │ 2024-06-01  │ Rotated Q2         │
│ VCB Bank     │ vcb24$3_v3     │ v3       │ 2024-09-15  │ Q3 mandatory       │
│ Binance      │ bnb24%4        │ v1       │ 2024-03-20  │ -                  │
│ Bybit        │ bb24^4         │ v1       │ 2024-03-20  │ -                  │
│ PayPal       │ pp24$3         │ v1       │ 2024-02-10  │ -                  │
│ Dropbox      │ db24@2         │ v1       │ 2024-01-20  │ -                  │
└──────────────┴────────────────┴──────────┴─────────────┴────────────────────┘
```

### Rotation Calendar Template

```
════════════════════════════════════════════════════════
                  ROTATION CALENDAR 2024
════════════════════════════════════════════════════════

JANUARY
├─ 15: Bank accounts (Q4-2023 → Q1-2024)

APRIL
├─ 15: Bank accounts (Q1 → Q2)
└─ 20: Trading accounts (H1 rotation)

JUNE
└─ 01: Main email (H1 → H2)

JULY
├─ 15: Bank accounts (Q2 → Q3)

OCTOBER
├─ 15: Bank accounts (Q3 → Q4)
└─ 20: Trading accounts (H2 rotation)

DECEMBER
└─ 01: Main email (H2 → end year)

════════════════════════════════════════════════════════
```

### Master Checklist

```
☐ SETUP PHASE
  ☐ Create secret phrases (1,2,3,4,5)
  ☐ Create backup secrets
  ☐ Setup sheet
  ☐ Setup calendar reminders

☐ MIGRATION PHASE
  ☐ Priority 1 accounts (Bank, Trading)
  ☐ Priority 2 accounts (Email, Cloud)
  ☐ Priority 3 accounts (Social)

☐ MAINTENANCE PHASE
  ☐ Monthly: Backup sheet
  ☐ Quarterly: Rotate banks
  ☐ Bi-annual: Rotate email + trading
  ☐ Annually: Review all accounts

☐ SECURITY PHASE
  ☐ Enable 2FA on critical accounts
  ☐ Check for suspicious activities
  ☐ Update recovery options
  ☐ Test backup secrets
```

---

**Chúc bạn quản lý password hiệu quả vàan toàn! 🔐**

_Tài liệu được viết bởi: System Documentation Team_
_Phiên bản: 2.0 - Enhanced Version_
_Ngày cập nhật: 2024_
