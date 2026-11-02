# Publisher, hosting và support

> **Status:** Draft
> **Last updated:** 2026-10-15

Hồ sơ quyết định và bằng chứng cho [Phase 0.3](../plan.md#03-publisher-public-urls-and-support--prepare-now). Chuẩn bị được khi bug/UI còn mở. Owner xác nhận 2026-10-15: chưa có tài khoản Chrome Web Store developer; chọn domain `passchef.com` và nền tảng Cloudflare. Chưa kiểm chứng đăng ký domain, DNS, dịch vụ hosting cụ thể, HTTPS hay deployment trong lần cập nhật này.

## Nguồn hiện có

- [README sản phẩm](../../../README.md) và [plan thương mại hóa](../plan.md).
- [Privacy](../../../landing-page/privacy.html), [Terms](../../../landing-page/terms.html): A04 sửa thành draft contact Pending2026-10-26; contact cũ `tetnu26@gmail.com` chưa xác nhận và không còn trình bày như kênh được duyệt. Bảng quyết định dưới vẫn giữ nguyên.
- [Khảo sát domain 2026-06-13](../../../plans/reports/researcher-260613-1853-passchef-domain-availability.md): tư liệu lịch sử; không xác nhận khả dụng/giá hiện tại.

## Quyết định đã có

Theo [quyết định trong plan](../plan.md#11-open-decisions): tên PassChef, Closed source, Free/Pro Option A2 và lifetime theo các giai đoạn. Đây là quyết định thương mại; không chứng minh licensing/payment/gating đã triển khai. Soft launch miễn phí theo plan; không mở lại các quyết định này trong hồ sơ vận hành.

## Sổ quyết định và bằng chứng

| Hạng mục | Trạng thái | Owner cần xác nhận / bằng chứng cần lưu |
|---|---|---|
| Tài khoản Chrome Web Store developer | Chưa có — owner xác nhận 2026-10-15 | Chọn tài khoản Google sở hữu lâu dài trước khi đăng ký; ghi xác nhận đăng ký sau khi thực hiện. |
| Publisher name / danh tính bên duy trì | Pending | Tên hiển thị và thông tin phù hợp với tài khoản, landing và legal. |
| Email tài khoản Google sở hữu publisher | Pending | Chưa chọn. Phân biệt với email contact/support; xem quy định thay đổi bên dưới trước khi đăng ký. |
| Email contact đã verify | Pending | Xác nhận tài khoản đã verify email liên hệ. |
| Domain / nền tảng | Đã chọn — owner xác nhận 2026-10-15 | `passchef.com`, Cloudflare. Chưa kiểm chứng quyền sở hữu, DNS, hosting/deployment hoặc HTTPS; chưa tự chọn Cloudflare Pages/Workers thay owner. |
| Landing URL | Dự kiến; chưa kiểm chứng | `https://passchef.com`; xác nhận URL chính thức và kết quả mở trang khi kiểm tra triển khai. |
| Privacy URL | Pending | URL HTTPS đến bản HTML đã rà; thay tham chiếu chưa xác nhận `passchef.io/privacy`. |
| Terms URL | Pending | URL chính thức đến bản HTML đã rà. |
| Support channel / URL | Pending | Owner xác nhận email/Issues/Discord hoặc kênh chọn; bằng chứng nhận được yêu cầu thử. |
| Security disclosure riêng tư | Pending | Kênh tiếp nhận, người xử lý và hướng dẫn gửi báo lỗi. |
| Contact `tetnu26@gmail.com` | Pending | Owner xác nhận tiếp tục dùng, quyền quản lý và khả năng nhận support/báo lỗi. |
| Quốc gia, ngôn ngữ, visibility Store | Pending | Quyết định phạm vi soft launch; khớp nội dung listing. |
| Listing URL / CTA cài đặt | Pending | Có URL thật sau bước publication Phase 1; cập nhật CTA khi đó. |

Khi xác nhận, ghi ngày, người phụ trách và kết quả/URL thích hợp trong bảng. Không lưu mật khẩu, tokens hoặc account keys vào tài liệu repo.

## Vai trò email và việc đổi địa chỉ

Đối chiếu tài liệu chính thức 2026-10-15; đây là hướng dẫn chuẩn bị, chưa phải cấu hình được thực hiện.

| Vai trò | Ảnh hưởng / thay đổi |
|---|---|
| Tài khoản Google dùng đăng ký CWS developer | Sở hữu publisher và quyền quản lý/phát hành. Theo [hướng dẫn đăng ký](https://developer.chrome.com/docs/webstore/register), email gắn tài khoản developer không đổi trực tiếp; cần tài khoản mới và chuyển các extension nếu đổi tài khoản sở hữu. Chọn tài khoản dùng lâu dài. |
| Email contact/support công khai | Dùng nhận liên hệ và hỗ trợ; khác với việc chuyển quyền sở hữu. [Google cho phép cập nhật contact email trong dev console](https://developer.chrome.com/docs/webstore/program-policies/trader-verification-faq#how-can-i-update-the-information-on-my-listing); [email contact cần verify](https://developer.chrome.com/docs/webstore/set-up-account#verify-your-email-address). Đồng bộ địa chỉ trong listing, website và legal khi đổi. |

Đề xuất chưa chốt: dùng tài khoản Google riêng cho publisher, địa chỉ công khai
`support@passchef.com` cho hỗ trợ và `security@passchef.com` cho báo lỗi riêng tư.
Các địa chỉ này chưa được tạo hoặc xác nhận hoạt động. Owner chưa quyết định thay
contact hiện có `tetnu26@gmail.com`.

Nếu dùng [Cloudflare Email Routing](https://developers.cloudflare.com/email-service/configuration/email-routing-addresses/), địa chỉ theo domain có thể chuyển thư tới inbox được xác minh. Inbox nhận có thể đổi qua cấu hình routing mà giữ địa chỉ công khai. Khả năng gửi/trả lời dưới địa chỉ domain cần cấu hình và kiểm tra riêng; không coi forwarding là hộp thư gửi/nhận hoàn chỉnh.

## Chuẩn bị tiếp theo

1. Owner chốt tài khoản Google sở hữu publisher và contact/support; domain/nền tảng đã chọn, còn xác nhận tình trạng triển khai Cloudflare và quyền quản lý domain.
2. Lập bộ URL dùng chung cho listing, landing và legal; ghi URL dự kiến là Pending cho đến khi kiểm tra hoạt động.
3. Chuẩn bị nội dung support: cách gửi lỗi, thông tin phiên bản/bước tái hiện, hướng dẫn sao lưu Cookbook và tuyến báo lỗi riêng tư.
4. Đối chiếu các contact/link công khai với quyết định; kiểm tra provenance/consent testimonial và các tuyên bố số người dùng theo checklist trong plan.
5. Chuẩn bị lựa chọn distribution và hướng dẫn reviewer bằng Menu/Recipe minh họa; hoàn thiện hồ sơ nộp sau kiểm chứng bản phát hành.

## Còn cần chốt

Tài khoản Google/email sở hữu publisher, danh tính publisher, contact/support chính thức, cấu hình/triển khai Cloudflare và domain, bộ URL và phạm vi phân phối. Tài khoản developer xác nhận chưa có; lựa chọn domain/nền tảng đã chốt.
