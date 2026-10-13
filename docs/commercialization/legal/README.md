# Hồ sơ pháp lý và quyền

> **Status:** Needs review
> **Last updated:** 2026-10-13

Điểm vào cho nhóm 0.2 trong [plan thương mại hóa](../plan.md#02-permissions-privacy-and-terms--prepare-now). Bản phát hành còn chờ sửa bug và hoàn tất UI; hồ sơ này phục vụ chuẩn bị và rà soát.

## Nguồn chỉnh sửa

| Tài liệu | Vai trò / trạng thái |
|---|---|
| [Privacy Policy](../../../landing-page/privacy.html) | Nguồn HTML công khai; cần cập nhật nội dung. |
| [Terms of Service](../../../landing-page/terms.html) | Nguồn HTML công khai; còn mô tả MIT/open source. |
| [Rà quyền và khai báo Store](permissions-review.md) | Hồ sơ đối chiếu code và phần khai báo cần chuẩn bị. |
| [Hợp đồng bảo mật extension](../../specs/extension-security.md) | Cơ chế Cookbook, phiên và clipboard hiện tại; bằng chứng có phạm vi giới hạn. |
| [Publisher và support](../operations/publisher-and-support.md) | Danh tính, contact và URL còn cần xác nhận. |

Chỉnh nội dung Privacy/Terms trực tiếp trong HTML; không tạo bản Markdown chứa chính sách trùng lặp. URL HTTPS chính thức và phê duyệt nội dung còn Pending.

## Các khoảng trống đã biết

- **Licensing:** áp dụng quyết định Closed source trong plan; sửa lời hứa MIT/open source và giữ thông báo thư viện bên thứ ba.
- **Storage:** phân biệt Cookbook mã hóa trong Sync, khóa/nonce/hạn phiên trong session, trạng thái xác thực/onboarding trong local và metadata có thể lưu dạng rõ.
- **Quyền:** bổ sung `clipboardRead`/`offscreen`; giải thích lại `alarms` theo cơ chế phiên hiện tại.
- **Clipboard:** thử dọn sau 30 giây, chỉ khi còn khớp Dish; mô tả giới hạn khi Chrome ngủ/đóng, đọc bị từ chối và lịch sử clipboard hệ điều hành.
- **Retention/reset:** đối chiếu từng vùng dữ liệu và hành vi Sync sau reset/uninstall; tránh lời hứa xóa toàn bộ hoặc luôn giữ được dữ liệu.
- **Sharing:** giải thích Full Access/QR và khả năng người nhận dùng Spices để nấu Dish.
- **Khai báo Store:** chuẩn bị single purpose, quyền, dữ liệu và remote code; đối chiếu lại manifest/code khi chốt bản phát hành.

Checklist thực hiện nằm trong plan; hồ sơ quyền ghi bằng chứng và quyết định cụ thể. Thay đổi code/quyền phải được phản ánh lại trước phê duyệt.

## Còn cần chốt

Publisher/contact, URL pháp lý chính thức, phạm vi quyền cuối cùng và người phê duyệt nội dung.
