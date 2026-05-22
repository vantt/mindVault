# HƯỚNG DẪN HỆ THỐNG QUẢN LÝ PASSWORD

## Phương pháp Special Character + Number (The Recipe Method)

---

## 📋 MỤC LỤC

1. [Tổng quan hệ thống](#tổng-quan-hệ-thống)
2. [Triết lý "Nấu ăn" (The Recipe Insight)](#triết-lý-nấu-ăn-the-recipe-insight)
3. [Cách thức hoạt động](#cách-thức-hoạt-động)
4. [Hướng dẫn từng bước](#hướng-dẫn-từng-bước)
5. [Quản lý và lưu trữ](#quản-lý-và-lưu-trữ)
6. [Biến thể và Indicators (Topping)](#biến-thể-và-indicators-topping)
7. [Rotation — Đổi mật khẩu](#rotation--đổi-mật-khẩu)
8. [Backup và Recovery](#backup-và-recovery)
9. [Best Practices](#best-practices)
10. [Ví dụ thực tế chi tiết](#ví-dụ-thực-tế-chi-tiết)
11. [Support & FAQ](#support--faq)

---

## 🎯 TỔNG QUAN HỆ THỐNG

### Bối cảnh và Động lực

Trong quy trình vận hành hiện tại, mật khẩu thường được tạo bởi các công cụ generator và lưu trữ trực tiếp dưới dạng văn bản thuần túy (plaintext) trên Google Sheets. Tuy nhiên, cách làm này tiềm ẩn rủi ro: **nếu lộ Sheet, lộ tất cả.**

Hệ thống này thay đổi hoàn toàn cách tiếp cận: **Biến Sheet thành "sách dạy nấu ăn" (Cookbook), chứ không phải "Kho chứa thức ăn sẵn".**

### Hệ thống này là gì?

Đây là phương pháp **quản lý password theo dạng Recipe (Công thức chế biến)**.
Bạn không lưu mật khẩu. Bạn lưu **cách chế biến** ra mật khẩu đó.

- ✅ **An toàn tuyệt đối:** Hacker lấy được "Recipe" nhưng không có "Gia vị bí mật" (Secret) thì không thể "nấu" ra password.
- ✅ **Độc lập:** Không phụ thuộc vào phần mềm thứ 3.
- ✅ **Linh hoạt:** Dễ dàng thay đổi khẩu vị (rotate pass) mà không cần nhớ nhiều.

---

## 🍳 TRIẾT LÝ "NẤU ĂN" (THE RECIPE INSIGHT)

Để hiểu bản chất hệ thống, hãy tưởng tượng bạn đang là một **Bếp trưởng (Chef)** chứ không phải một IT Admin.

### 1. Nguyên lý Bếp trưởng

Mật khẩu hoàn chỉnh giống như một **Món ăn (Dish)**. Để tạo ra nó, bạn cần các yếu tố:

1.  **Nguyên liệu nền (Base Ingredient):** Đây là chuỗi **Random Hash** được tạo ra bởi các phần mềm generator (vd: `r4nd0m`). Nó giống như Thịt, Cá, Rau - thứ ai cũng thấy, có thể để trên bàn (lưu trong Sheet).
2.  **Gia vị bí mật (Secret Spices):** Đây là **Secret Phrase** (vd: `Basic*`). Đây là công thức gia truyền chỉ có trong đầu bạn. Muối, Tiêu, Hạt nêm - thiếu nó, món ăn vô vị (sai password).
3.  **Công thức chế biến (Recipe):** Cách bạn phối hợp chúng (vd: **r4nd0m`#1`**). Nấu thịt trước hay rau trước? Nêm gia vị vào đâu?

### 2. Tại sao gọi là Recipe (thay vì Formula)?

- **Formula (Công thức tính toán):** Gợi cảm giác về Excel, Toán học (`=SUM`). Nó khô khan và máy móc.
- **Recipe (Công thức chế biến):** Gợi cảm giác về sự **Lắp ghép & Sáng tạo**.
  - Bạn "nêm" Secret vào Hash.
  - Bạn "trộn" (Mix) chúng với nhau.
  - Bạn thêm "Topping" (Modifier) để món ăn ngon hơn.

👉 **Tư duy cốt lõi:** Sheet của bạn là một **Thực đơn (Menu)** ghi danh sách các món ăn (`Facebook`, `Gmail`...) và **Recipe** của chúng. Khi cần đăng nhập, bạn nhìn Recipe và tự tay "chế biến" ra password ngay tức thì.

---

## 🔧 CÁCH THỨC HOẠT ĐỘNG

### Cấu trúc của một Recipe

Mỗi Recipe trong Sheet sẽ trông như thế này:

```
<nguyên_liệu><cách_nêm><loại_gia_vị>[_phiên_bản]
```

**Ví dụ:** `r4nd0m#1`

Phân tích Recipe này dưới góc độ nấu ăn:

- **`r4nd0m`** (Nguyên liệu): Hash nền.
- **`#`** (Cách nêm): Đặt gia vị ở **ĐẦU**.
- **`1`** (Loại gia vị): Dùng hũ gia vị số **1** (Secret 1).

### 1. Nguyên liệu nền (Hash)

- Là chuỗi ký tự ngẫu nhiên.
- Ví dụ: `r4nd0m`, `h4sh3s`, `c0d3s`.
- Nguồn: Tự gõ hoặc dùng tool gen ra.

### 2. Cách nêm nếm (Vị trí đặt Secret)

Các ký tự đặc biệt đóng vai trò như chỉ dẫn chế biến:

| Ký hiệu Recipe | Ý nghĩa chế biến  | Giải thích                         |
| :------------: | ----------------- | ---------------------------------- |
|      `#`       | Nêm vào **ĐẦU**   | Phủ gia vị lên trên cùng (Top)     |
|      `@`       | Nêm vào **GIỮA**  | Nhồi gia vị vào bên trong (Center) |
|      `$`       | Nêm vào **CUỐI**  | Để gia vị dưới đáy (Bottom/End)    |
|      `%`       | Trộn **XEN KẼ**   | Trộn đều từng chút một (Mix)       |
|      `^`       | Trộn **TỪNG CẶP** | Kẹp bánh mì (Sandwich layer)       |

### 3. Hũ gia vị (Secret Index)

Bạn có 5 hũ gia vị bí mật (Secret Phrases). Trong Recipe chỉ ghi số thứ tự hũ, không ghi thành phần bên trong.

| Số  | Tên hũ (Ví dụ)      | Thành phần (Secret - Chỉ bạn biết) | Dùng cho món gì?                   |
| :-: | ------------------- | ---------------------------------- | ---------------------------------- |
| `1` | **Gia vị Cơ bản**   | `Basic*`                           | Mạng xã hội, forum (ít quan trọng) |
| `2` | **Gia vị Đậm**      | `Secure#`                          | Email, Cloud                       |
| `3` | **Gia vị Cay**      | `Ultra$`                           | Bank, Ví điện tử (Quan trọng)      |
| `4` | **Gia vị Đặc biệt** | `Trade&`                           | Sàn Trading, Crypto                |
| `5` | **Gia vị Cứu hộ**   | `Backup@`                          | Dùng khi quên/mất các hũ kia       |

---

## 📖 HƯỚNG DẪN TỪNG BƯỚC

### BƯỚC 1: Chuẩn bị Gian Bếp (Setup Secrets)

Tự nghĩ ra 5 chuỗi Secret và ghi nhớ, viết ra giấy (hoặc lưu trong Két sắt Password Manager):

```
Secret 1: Basic*
Secret 2: Secure#
Secret 3: Ultra$
Secret 4: Trade&
Secret 5: Backup@
```

### BƯỚC 2: Viết Recipe cho món ăn (Tạo password)

**Ví dụ: Món "Facebook"**

1.  **Chọn nguyên liệu:** Lấy hash `r4nd0m` (6-12 ký tự).
2.  **Chọn gia vị:** Facebook không quan trọng lắm -> Dùng hũ số `1` (`Basic*`).
3.  **Chọn cách nêm:** Nêm ở đầu cho dễ làm -> Dùng `#`.
4.  **Viết Recipe vào Menu (Sheet):** `r4nd0m#1`

### BƯỚC 3: Chế biến (Đăng nhập)

Khi cần đăng nhập Facebook:

1.  Mở Sheet, nhìn Recipe: `r4nd0m#1`.
2.  Hiểu rằng: "À, lấy `r4nd0m` và nêm `Secret 1` vào đầu".
3.  Nhớ lại `Secret 1` là `Basic*`.
4.  Ghép lại: `Basic*` + `r4nd0m` = `Basic*r4nd0m`.
5.  Gõ kết quả vào ô Password.

---

## 🎨 VÍ DỤ CHI TIẾT CÁC KIỂU CHẾ BIẾN

### Kiểu 1: Nêm đầu (`#`) - The Topping

**Recipe:** `r4nd0m#1`

```
  [Gia vị 1]  +  [Nguyên liệu]
   Basic*          r4nd0m
      ↓              ↓
  Basic*r4nd0m   (Món hoàn chỉnh)
```

### Kiểu 2: Nhồi giữa (`@`) - The Filling

**Recipe:** `r4nd0m@2`

```
  [Nguyên liệu 1/2] + [Gia vị 2] + [Nguyên liệu 2/2]
        r4n             Secure#           d0m
         ↓                 ↓               ↓
      r4nSecure#d0m  (Món hoàn chỉnh)
```

**Cách làm:**

1. Lấy Secret 2: `Secure#`
2. Chia Hash làm 2 phần: `r4n` và `d0m`
3. Nhồi Secret vào chính giữa.

### Kiểu 3: Nêm cuối (`$`) - The Base

**Recipe:** `r4nd0m$3`

```
  [Nguyên liệu]  +  [Gia vị 3]
     r4nd0m           Ultra$
       ↓                ↓
   r4nd0mUltra$   (Món hoàn chỉnh)
```

### Kiểu 4: Trộn đều (`%`) - The Mix

**Recipe:** `r4nd0m%4` (Trộn xen kẽ từng ký tự)

```
Nguyên liệu: r    4    n    d    0    m
             ↓    ↓    ↓    ↓    ↓    ↓
Gia vị:      T    r    a    d    e    &
             ↓    ↓    ↓    ↓    ↓    ↓
Kết quả:     rT + 4r + na + dd + 0e + m&

Pass: rT4rnadd0em&
```

### Kiểu 5: Trộn từng cặp (`^`) - The Sandwich

**Recipe:** `r4nd0m^4` (Trộn xen kẽ từng cặp 2 ký tự)

```
Nguyên liệu: (r4)  (nd)  (0m)
              ↓     ↓     ↓
Gia vị:      (Tr)  (ad)  (e&)
              ↓     ↓     ↓
Kết quả:     r4Tr + ndad + 0me&

Pass: r4Trndad0me&
```

---

## 📂 QUẢN LÝ VÀ LƯU TRỮ

### Cấu trúc Sheet (Menu)

**Sheet của bạn giờ đây là một Menu:**

```
┌──────────────┬───────────────┬────────────┬─────────────┐
| Món (Account)| Recipe        | Ngày nấu   | Ghi chú     |
├──────────────┼───────────────┼────────────┼─────────────┤
| Facebook     | r4nd0m#1      | 2024-01-15 | -           |
| Gmail        | h4sh3s@2      | 2024-06-01 | -           |
| Gmail (mới)  | h4sh3sB@2     | 2024-06-15 | Nghi ngờ lộ |
| Bank         | p4ssw0$3      | 2024-09-15 | -           |
| Binance      | cr7pt0%4      | 2024-03-20 | -           |
└──────────────┴───────────────┴────────────┴─────────────┘
```

> 💡 **Lưu ý:** Recipe (`r4nd0m#1`) hoàn toàn vô hại nếu người xem không có "Hũ gia vị" (Secret) của bạn.

---

## 🎭 BIẾN THỂ VÀ INDICATORS (TOPPING)

Bạn có thể thêm các ký tự đặc biệt vào cuối Recipe để thay đổi hương vị nhanh chóng (Modifiers):

```
┌──────────┬─────────────────────────┬──────────────────────┐
│ Modifier │ Tác dụng (Topping)      │ Ví dụ                │
├──────────┼─────────────────────────┼──────────────────────┤
│ _        │ Đảo vị trí (Start/End)  │ r4nd0m#1_            │
│ !        │ Viết HOA secret         │ r4nd0m#1!            │
│ ?        │ Đảo ngược secret        │ r4nd0m#1?            │
│ ~        │ Xóa ký tự đặc biệt      │ r4nd0m#1~            │
└──────────┴─────────────────────────┴──────────────────────┘
```

### Ví dụ chi tiết Modifiers

**1. Modifier `_` (Đảo vị trí)**

- `r4nd0m#1` (Gia vị ở đầu) → `Basic*r4nd0m`
- `r4nd0m#1_` (Đảo xuống cuối) → `r4nd0mBasic*`

**2. Modifier `!` (Viết hoa - High Heat)**

- `r4nd0m#1` (Bình thường) → `Basic*r4nd0m`
- `r4nd0m#1!` (Viết hoa gia vị) → `BASIC*r4nd0m`

**3. Modifier `?` (Đảo ngược - Stir)**

- `r4nd0m#1?` → `*cisaBr4nd0m` (Chữ `Basic*` bị đảo ngược)

---

## ♻️ ROTATION — ĐỔI MẬT KHẨU

### Khi nào cần Rotate?

- Định kỳ theo quy định của ngân hàng/công ty.
- Nghi ngờ mật khẩu bị lộ.

### Nguyên tắc: Thay Hash, giữ nguyên Secret

Password được tạo ra từ `Hash + Secret`. Để có password mới hoàn toàn khác, chỉ cần **đổi phần Hash** trong Recipe. Secret không cần thay.

```
Recipe cũ: fb2024#1   →  Basic*fb2024      ← password cũ trên Facebook
Recipe mới: fb2024r#1  →  Basic*fb2024r    ← password mới, khác hoàn toàn
```

> **Tại sao không dùng cùng một hash?** Vì password mới phải khác password cũ. Thêm 1-2 ký tự bất kỳ vào hash là đủ.

### Quy trình Rotate

1. **Trên Sheet:** Tìm dòng cần đổi → sửa phần Hash trong Recipe (thêm ký tự, đổi ký tự cuối, v.v.)
2. **Trên Web:** Login bằng password cũ → đổi sang password mới (generate từ recipe mới)
3. **Xong.** Secret không thay đổi, chỉ hash thay đổi.

**Ví dụ — Đổi pass VCB định kỳ:**

| | Recipe | Password |
|---|---|---|
| Cũ | `vcb2024$3` | `vcb2024Ultra$` |
| Mới | `vcb2024b$3` | `vcb2024bUltra$` |

**Ví dụ — Khẩn cấp (nghi ngờ lộ Gmail):**

| | Recipe | Password |
|---|---|---|
| Cũ | `gmail24@2` | `gmaSecure#il24` |
| Mới | `gmailX@2` | `gmaSecure#ilX` |

> **Ghi chú trong Sheet:** Thêm cột "Ghi chú" để note lý do rotate và ngày đổi. Hash mới ghi nhớ trong chính Recipe — không cần nhớ thêm gì.

---

## 💾 BACKUP VÀ RECOVERY

### Backup Secrets (Gia vị dự trữ)

Tạo **bộ secret riêng** chỉ dùng cho backup (đề phòng mất hũ gia vị chính):

```
┌─────────────┬──────────────────┬────────────────────┐
│ Secret      │ Backup Version   │ Khi nào dùng       │
├─────────────┼──────────────────┼────────────────────┤
│ Secret1     │ BasicBackup*     │ Recovery mạng xã   │
│ Secret2     │ SecureBackup#    │ Recovery email     │
│ ...         │ ...              │ ...                │
└─────────────┴──────────────────┴────────────────────┘
```

### Master Recovery Recipe

Luôn giữ một "Công thức tổ truyền" (Master Key) cất trong két sắt an toàn nhất. Nếu một ngày bạn quên sạch các công thức kia, hoặc mất hết hũ gia vị, công thức tổ truyền này sẽ mở được mọi cánh cửa.

```
Master Recipe: MyMaster2024!Backup@Safe
```

---

## ✨ BEST PRACTICES

### 10 Nguyên tắc Bếp trưởng

1.  **KHÔNG** để lộ hũ gia vị (Secret) lung tung.
2.  **KHÔNG** dùng 1 hũ gia vị cho tất cả món ăn (Phân loại Level).
3.  **NÊN** rửa tay (Rotate pass) định kỳ với món ăn quan trọng.
4.  **PHẢI** backup Menu (Sheet) thường xuyên.
5.  **TUYỆT ĐỐI** không nấu ăn (Login) ở nơi mất vệ sinh (Máy lạ/Wifi công cộng).
6.  **KHÔNG** dùng hash quá đơn giản (như 123456).
7.  **KHÔNG** chia sẻ Recipe với người khác.
8.  **KHÔNG** quên 2FA cho tài khoản quan trọng.

---

## 🎓 VÍ DỤ THỰC TẾ CHI TIẾT

### Case Study 1: User mới setup

**Profile:** An, chưa từng dùng password manager.

**Bước setup:**

```
[Tuần 1: Chuẩn bị Gian Bếp]
✓ Tạo 5 secret phrases:
  Secret1: "An@2024*" (Gia vị cơ bản)
  Secret2: "AnSecure#24" (Gia vị đậm)
  Secret3: "AnBank$2024" (Gia vị cay)
  ...

[Tuần 2: Viết Menu]
✓ Google Sheet: "Password Menu"
✓ Migrating tài khoản Facebook:
  - Hash: fb2024
  - Recipe: fb2024#1
  - Password: An@2024*fb2024
```

### Case Study 2: Rotation định kỳ (VCB Bank)

**Tình huống:** Đến hạn 3 tháng phải đổi pass VCB.

**Quy trình:**

```
[Hiện tại]
Recipe: vcb2024$3
Password: vcb2024AnBank$2024

[Đổi hash để tạo password mới]
Recipe mới: vcb2024b$3
Password mới: vcb2024bAnBank$2024

[Thực hiện]
1. Login VCB bằng pass cũ (vcb2024AnBank$2024).
2. Đổi thành pass mới (vcb2024bAnBank$2024).
3. Cập nhật Recipe trong Sheet: vcb2024$3 → vcb2024b$3
```

### Case Study 3: Nghi ngờ bị lộ (Khẩn cấp)

**Tình huống:** Gmail báo có login lạ lúc 3AM.

**Xử lý:**

```
1. Tạo hash mới ngay lập tức.
2. Recipe cũ: gmail24@2
3. Recipe mới: gmailX@2 (thêm X vào hash)
4. Password mới: gmaSecure#ilX (khác hoàn toàn)
5. Đổi password trên Google ngay.
6. Cập nhật Sheet.
```

---

## 📞 SUPPORT & FAQ

**Q1: Recipe này có an toàn không?**
A: Có. Hacker lấy được Recipe (Sheet) giống như lấy được vỏ hộp thuốc, nhưng không có thuốc bên trong (Secret).

**Q2: Nếu quên Secret Phrase thì sao?**
A: Dùng hệ thống backup (Gia vị dự trữ) hoặc Master Recovery Key.

**Q3: Có cần nhớ hết Recipe không?**
A: Không. Chỉ cần nhớ 5 Secret Phrases thôi. Recipe đã ghi trong Sheet rồi.

---

**Chúc bạn trở thành một Bếp trưởng Mật khẩu tài ba! 👨‍🍳🔐**
