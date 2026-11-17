# 00 — Insight → Product value (debate draft)

Nguồn: brain dump của owner, 2026-11-10. Bản này diễn giải lại + phản biện, chưa phải chốt.

## 1. Insight (diễn giải lại)
- Người dùng **không tin giao trọn mật khẩu cho một app**: app chết, công ty đóng, quên master password → mất hết.
- Họ **tin giấy và Google Sheet** vì tự nhìn thấy, tự cất được, người nhà đọc được.
- Nhu cầu ít ai nói ra: **kế thừa**. Mật khẩu quan trọng của gia đình phải đọc được khi mình có chuyện.
- Mâu thuẫn: ghi trọn mật khẩu thì người lạ nhặt được tờ giấy (hoặc lộ sheet) là vào được ngay.
- Lời giải: **chia đôi**. Nửa ngẫu nhiên ghi công khai được; nửa bí mật ngắn (1–3 cụm), giữ riêng. Dòng ghi chỉ nói "ghép thêm phần bí mật nào, ghép vào đâu".

## 2. Product value (đề xuất)
Câu chính (VI): **"Ghi mật khẩu ra giấy mà người lạ đọc cũng vô dụng."**
EN: *"Write your passwords down. A stranger who reads them still can't log in."*

Ba giá trị:
1. **Không phụ thuộc app.** Bảng tính + trí nhớ là đủ để tự ghép lại bằng tay. App chỉ để tiện hơn, không phải két sắt duy nhất. *(Đây là điểm khác biệt mạnh nhất, nên nói to.)*
2. **An toàn khi bị nhìn thấy.** Lộ sheet hoặc mất tờ giấy chưa đủ để đăng nhập.
3. **Truyền lại được cho người nhà.** Người nhà chỉ cần tờ giấy + biết vài cụm bí mật.

Ẩn dụ gợi ý thay cho nấu ăn ở tầng giải thích: **"mật khẩu hai nửa"** (giống két ngân hàng hai chìa: một chìa ghi trên giấy, một chìa trong đầu bạn). Nấu ăn giữ làm giọng thương hiệu nhẹ.

## 3. Phản biện (chỗ insight còn hở)
1. **Kế thừa chưa khép kín.** Người nhà cần biết phần bí mật. Nếu phần bí mật chỉ nằm trong đầu chủ hoặc mã hóa trong điện thoại thì người nhà vẫn mất. → Cần câu chuyện rõ: phần bí mật ghi **ở nơi khác** (phong bì niêm phong, két, người tin cậy). Hai nơi cất = hai chìa. App nên có màn "Chuẩn bị cho người nhà" (in hướng dẫn ghép).
2. **Dùng lại 1–3 cụm bí mật cho mọi tài khoản.** Một website lộ mật khẩu dạng chữ thường (plaintext) → kẻ gian biết phần bí mật. Muốn vào tài khoản khác vẫn cần phần ngẫu nhiên (nằm trong sheet). Rủi ro thật = **lộ 1 website + lộ sheet cùng lúc**. Phải nói thật, nhẹ nhàng; khuyên giữ sheet riêng tư và đổi phần ngẫu nhiên khi một site bị lộ.
3. **Sheet vẫn lộ danh sách tài khoản** (tên ngân hàng, email). Không phải mật khẩu nhưng là thông tin.
4. **Quên master password của app** không mất gì nếu phần bí mật nhớ được/ghi ở nơi khác. Đây là điểm cộng, nên đưa vào onboarding ("quên mật khẩu app cũng không mất tài khoản").
5. **Đối thủ đã có kế thừa** (Bitwarden Emergency Access, Apple Legacy Contact, Google Inactive Account Manager). Nhưng đều buộc vào nhà cung cấp. PassChef khác: **giấy + trí nhớ, không cần ai cấp quyền**.
6. **Phân khúc lệch.** Insight hướng gia đình/người lớn tuổi, nhưng extension hiện tạo công thức kiểu kỹ thuật (`#`, `$`, `@`, số 1–5). Android nên ẩn cú pháp, cho chọn "đặt phần bí mật ở đầu/cuối" bằng lời.
7. **Tên gọi.** "Thực đơn/Gia vị" đẹp cho người đã hiểu, gây khó cho người mới. Trong UI dùng từ thường; ẩn dụ chỉ ở slogan, minh họa.

## 4. Hệ quả cho UX Android
- Onboarding mở bằng so sánh **"Cách thường vs Cách hai nửa"** (giấy bị người lạ đọc).
- Thêm 1 màn **"Không có app vẫn ghép được"** (ví dụ ghép tay) — đây là lý do để tin.
- Đưa **"Chuẩn bị cho người nhà"** vào lộ trình (phase sau): in/hướng dẫn ghép, nhắc cất phần bí mật nơi khác.
- Cảnh báo rủi ro viết bình tĩnh, ở chỗ người dùng tạo phần bí mật.

## Câu hỏi mở
- Người nhà được biết phần bí mật bằng cách nào là chuẩn: phong bì, người tin cậy, hay chủ tự quyết?
- Có muốn làm tính năng "Chuẩn bị cho người nhà" không, hay giữ ngoài phạm vi?
- Đối tượng ưu tiên: gia đình/người lớn tuổi hay người dùng kỹ thuật? (quyết định độ lộ cú pháp)
- Bỏ hẳn ẩn dụ nấu ăn khỏi UI, hay giữ ở mức slogan?

## 5. Cập nhật sau phản hồi owner (2026-11-10)
Owner: người dùng chính là **dân kỹ thuật** (owner + người kỹ thuật khác được giới thiệu). Họ chọn sheet vì đơn giản, dùng được ngay, và **không có app vẫn tự tính lại mật khẩu**.
Hệ quả:
- Phản biện #6 (lệch phân khúc) đóng lại: đối tượng chính = kỹ thuật. **Không giấu cú pháp** `#`, `$`, `@`, số; hiện công thức rõ, mono, có chú thích ngắn.
- Gia đình = người dùng phụ (chỉ đọc giấy khi cần). Câu chuyện kế thừa giữ nhẹ, không lái thiết kế.
- Giá trị số 1 được owner xác nhận bằng hành vi thật: **tự tính tay, không phụ thuộc app**. App phải tôn trọng điều đó: công thức luôn nhìn thấy, app chỉ tăng tốc (tìm, ghép, copy), không biến thành hộp đen.
- Từ ngữ: vẫn dùng từ thường ("Phần bí mật", "Mã đã ghi"), nhưng không cần đơn giản hóa kiểu cho người 50+. Màn giải thích rút ngắn (3 màn thay vì 5), có nút Bỏ qua rõ cho người đã biết cách.
- Đối thủ thật không phải Bitwarden mà là **chính tờ sheet + tính tay**. App phải nhanh hơn tính tay mà không mất sự minh bạch của nó.

## 6. Chốt của owner (2026-11-10, lần 2)
- **Bảng màu: chọn D** (Ink + Amber tối, Linen sáng, theo cài đặt điện thoại). Sáng và tối ngang hàng.
- **Đối tượng: mọi người, chủ yếu dân văn phòng.** Không riêng dân kỹ thuật, **không có người lớn tuổi**.
  - Thay mục 5: insight gốc từ dân kỹ thuật (owner + người được giới thiệu), nhưng sản phẩm nhắm dân văn phòng.
  - Từ ngữ thường trong UI (report 05) là đúng hướng.
  - Mã đã ghi (`r4nd0m#1`) vẫn hiện, luôn kèm chú thích bằng lời. Cho chọn vị trí bằng lời ("đặt ở đầu/cuối"), cú pháp là kết quả, không bắt gõ ký hiệu.
  - Bỏ mọi thứ hướng người 50+ (chữ lớn, "An Vui"...). Kế thừa cho gia đình: nhắc nhẹ.
- **Nhiều bộ phần bí mật (D17): có.** Mỗi bảng tính gắn một bộ, giống Profile bên extension. Chỉ có một bộ thì UI ẩn hẳn. Két cũ chuyển thành một bộ "Default".
