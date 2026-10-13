# Tài liệu thương mại hóa PassChef

> **Status:** Draft — mục lục đã tổ chức; nội dung phát hành còn cần rà soát.
> **Last updated:** 2026-10-13

Điểm vào chung cho tài liệu listing, pháp lý và vận hành. Bắt đầu từ
[plan](plan.md): ưu tiên phần chuẩn bị 0.1–0.5; screenshot thật, ZIP cuối và phát hành
chờ sửa bug chặn release và hoàn tất UI. Bằng chứng test cũ giữ nguyên phạm vi.

## Mục lục và trạng thái

| Tài liệu | Trạng thái | Việc còn thiếu |
|---|---|---|
| [Plan thương mại hóa](plan.md) | Partial | Thực hiện checklist; vượt điều kiện sẵn sàng sản phẩm trước đóng gói cuối/phát hành. |
| [Listing Chrome Web Store](marketing/cws-listing-copy.md) | Needs review | Sửa Recipe mẫu, cách nấu Dish, Chrome Sync, Tag, sharing và cam kết bảo mật. |
| [Giá trị sản phẩm](marketing/product-value-statements.md) | Needs review | Rà các lời hứa tuyệt đối; chốt nội dung dùng công khai. |
| [Quyết định tên](marketing/naming-decision.md) | Approved — quyết định tên | PassChef đã chốt; phần phân tích/copy lịch sử vẫn cần rà trước sử dụng. |
| [Brief screenshot/promo](marketing/cws-screenshot-prompts.md) | Draft | Đồng bộ thiết kế hiện tại; chụp UI thật sau khi ổn định. |
| [Hồ sơ pháp lý](legal/README.md) | Needs review | Dẫn tới Privacy/Terms nguồn; rà licensing, dữ liệu, quyền và retention. |
| [Rà quyền và khai báo Store](legal/permissions-review.md) | Needs review | Xác nhận quyền tối thiểu, hoàn thiện justification và đối chiếu manifest cuối. |
| [Publisher, hosting và support](operations/publisher-and-support.md) | Draft | Chốt thông tin owner và ghi bằng chứng thực hiện. |

## Nguồn chỉnh sửa

- `marketing/`: tài liệu nội dung/brief; di chuyển từ thư mục marketing cũ, không lưu bản sao song song.
- `legal/`: hồ sơ rà soát. [Privacy HTML](../../landing-page/privacy.html) và [Terms HTML](../../landing-page/terms.html) vẫn là nguồn chỉnh sửa trong website; không nhân bản nội dung policy sang Markdown.
- `operations/`: lựa chọn publisher, hosting/domain, URL công khai, support và trạng thái thực hiện.
- `plan.md`: checklist thực hiện; các hồ sơ chi tiết dẫn về plan thay vì tạo checklist cạnh tranh.

## Quy ước bảo trì

- Mỗi tài liệu ghi trạng thái, ngày cập nhật và phần còn thiếu. `Draft`: đang soạn; `Needs review`: đã có nội dung nhưng còn điểm cần xác minh/sửa; `Approved`: đã chốt trong phạm vi ghi rõ.
- Mục lục phản ánh trạng thái thực tế. Di chuyển/viết tài liệu không đồng nghĩa nội dung đã duyệt hoặc sản phẩm đã phát hành.
- Dùng tên file kebab-case và link tương đối; cập nhật link khi chuyển file.
- Sửa nguồn hiện có; Git giữ lịch sử. Không tách thư mục `draft`/`approved` hoặc tạo nhiều bản nội dung giống nhau.
- Khi bắt đầu chốt bản phát hành, thêm `release/` cho release notes và hồ sơ artifact/kiểm chứng; liên kết bằng chứng kỹ thuật hiện có thay vì sao chép.

## Tài liệu liên quan

- [Security contract của extension](../specs/extension-security.md)
- [Bằng chứng kiểm chứng extension](../../plans/reports/261012-extension-recheck.md)
- [Landing improvements](../../landing-page-improve.md)
- [Roadmap](../development-roadmap.md)

Điểm còn mở: bug/UI chặn release; nội dung listing/legal chưa duyệt; publisher,
domain/hosting và support chưa có xác nhận vận hành trong hồ sơ này.
