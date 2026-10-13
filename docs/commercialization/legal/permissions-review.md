# Rà quyền và khai báo Chrome Web Store

> **Status:** Needs review
> **Last updated:** 2026-10-13

Hồ sơ làm việc cho [Phase 0.2](../plan.md#02-permissions-privacy-and-terms--prepare-now). Ma trận dưới mô tả code hiện tại, chưa phải justification hoặc khai báo Store đã phê duyệt. Bug/UI còn mở; xác minh lại trên bản phát hành cuối cùng.

## Nguồn đối chiếu

- [Manifest](../../../chrome-extension/src/manifest.json): 8 quyền và 1 phạm vi host; Chrome tối thiểu 116.
- [Hợp đồng bảo mật](../../specs/extension-security.md): Cookbook Argon2id/AES-GCM, phiên và clipboard; kiểm chứng đã ghi nhận có phạm vi riêng.
- [Background authority](../../../chrome-extension/src/security/background-security.js), [service worker](../../../chrome-extension/src/service_worker.js): storage, phiên, tìm Menu và thông báo khóa.
- [Clipboard owner](../../../chrome-extension/src/security/clipboard-owner.js), [offscreen](../../../chrome-extension/src/offscreen/offscreen.js): Copy và dọn clipboard, Worker Argon2.
- [Privacy hiện có](../../../landing-page/privacy.html), [Terms hiện có](../../../landing-page/terms.html): nguồn công khai cần sửa.

## Ma trận quyền hiện tại

| Quyền / phạm vi | Mục đích hiện tại / khoảng trống | Xác minh cuối cùng |
|---|---|---|
| `storage` | Cookbook mã hóa trong Sync; khóa/nonce/hạn phiên trong session; trạng thái xác thực/onboarding trong local. Metadata như tên profile/Menu mapping có thể dạng rõ; sửa tuyên bố mọi dữ liệu chỉ ở Sync. | Pending: liệt kê trường dữ liệu, nơi lưu, xóa và Sync. |
| `clipboardWrite` | Copy Dish theo thao tác người dùng; Recipe-only Copy và Copy bundle là hành động riêng. Không mô tả shortcut nấu Dish như tự động Copy. | Pending: đối chiếu các luồng Copy trong artifact. |
| `clipboardRead` | Khi đến hạn dọn, đọc clipboard hiện tại để chỉ xóa nếu còn khớp Dish đã Copy; không thu thập lịch sử clipboard. Privacy thiếu quyền này. | Pending: khớp/không khớp, đọc bị từ chối và đóng popup. |
| `offscreen` | Tài liệu đi kèm chạy Argon2 trong Worker và giữ trách nhiệm dọn clipboard khi trang Copy đóng. Privacy thiếu quyền này. | Pending: đường dẫn Worker/WASM và vòng đời offscreen trong artifact. |
| `idle` | Khóa Cookbook khi trình duyệt idle hoặc thiết bị khóa; ngưỡng idle hiện tại 5 phút. | Pending: luồng khóa và phản hồi UI trên phạm vi hỗ trợ. |
| `alarms` | Hạn phiên tuyệt đối 10 phút và dựng lại alarm thiếu. Dọn clipboard dùng timer offscreen; mô tả cũ dùng alarms cần sửa. | Pending: hết hạn và worker khởi động lại. |
| `tabs` | Tìm/chọn Google Menu đang mở, lấy metadata tab và gửi thông báo khóa. Rà khả năng thu hẹp quyền. | Pending: tính cần thiết và luồng picker/shortcut/khóa khi giảm quyền. |
| `activeTab` | Quyền đang khai báo; chưa có kết luận cần giữ khi đã có content script và host access cố định. | Pending: quyết định giữ/bỏ có bằng chứng browser. |
| `https://docs.google.com/spreadsheets/*` | Content script trên Google Menu để lấy Recipe đang chọn. Quyền cài đặt tồn tại ngoài thời điểm bấm nấu; không tự suy ra extension dùng Sheets API. | Pending: phạm vi host cuối cùng và luồng chọn Recipe/shortcut. |

## Dữ liệu và vòng đời cần đối chiếu

| Phạm vi | Bằng chứng / câu hỏi cần xử lý |
|---|---|
| Sync/local/session | [Background authority](../../../chrome-extension/src/security/background-security.js) và [popup onboarding](../../../chrome-extension/src/popup/popup-home.js). Phân biệt Spices mã hóa có thể qua hạ tầng Google với backend/telemetry do developer vận hành; khóa phiên và metadata không phải cùng loại dữ liệu. |
| Reset/uninstall | [Reset hiện tại](../../../chrome-extension/src/options/options-settings-tab.js) xóa Sync và session; không xóa mọi record local. Hành vi uninstall/Sync còn cần xác minh; chưa khẳng định xóa hết hoặc luôn giữ dữ liệu. |
| Clipboard | Owner giữ Dish/deadline trong RAM; sau 30 giây đọc và chỉ xóa khi khớp. Read denial không ghi; compare/write không nguyên tử giữa ứng dụng. Chrome ngủ/đóng và OS history nằm ngoài cam kết dọn đúng hạn. |
| Full Access sharing | [Export/import adapter](../../../chrome-extension/src/adapters/infrastructure/export-import-adapter.js) và [QR display](../../../chrome-extension/src/options/options-qr-display.js). Mô tả quyền người nhận đối với Spices/Dish; không gọi đây là chỉ chia sẻ Recipe. |
| Licensing/contact | Closed source đã chốt trong [plan](../plan.md#11-open-decisions); Privacy/Terms vẫn cần đồng bộ. Contact/URL cần xác nhận trong [hồ sơ vận hành](../operations/publisher-and-support.md). |

## Hồ sơ khai báo Store cần chuẩn bị

| Thành phần | Trạng thái / điều kiện |
|---|---|
| Single purpose | Draft cần soạn: nấu Dish từ Recipe trong Google Menu và Spices được bảo vệ trong Cookbook. |
| Justification từng quyền/host | Pending; hoàn thiện từ ma trận sau khi quyết định phạm vi quyền. |
| Loại dữ liệu, sử dụng/chia sẻ và limited use | Pending; đối chiếu toàn bộ luồng dữ liệu. Không mặc định “không xử lý dữ liệu” chỉ vì không có telemetry. |
| Remote code | Pending; kiểm tra JS/WASM/Worker được đóng gói và đường dẫn tải trước khi khai báo. |
| Privacy URL và xác nhận nhất quán | Pending; phụ thuộc URL hoạt động, nội dung HTML được rà và manifest cuối cùng. |

Lưu bằng chứng và kết luận trong hồ sơ này khi thực hiện; checklist tổng vẫn ở plan. Không tự bỏ quyền chỉ từ rà tài liệu.

## Còn cần chốt

Giữ/bỏ `tabs`/`activeTab`, phân loại dữ liệu trong dashboard, phạm vi kiểm chứng và nội dung phê duyệt cuối.
