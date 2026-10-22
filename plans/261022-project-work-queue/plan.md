# PassChef — hàng đợi công việc toàn project

Ngày:2026-10-22, Asia/Saigon. Trạng thái: **Ready — phân nhóm và chia task;
chưa thực thi các task sản phẩm**. Source baseline:`e3e9eab` + WIP hiện có.

Yêu cầu: để agent làm việc không cần owner chờ; gom việc cần phone/desktop thành
phiên riêng. Không thay acceptance criteria hay mở lại các slice đã Passed.

## Ba nhóm để xếp lịch

| Nhóm | Anh cần làm gì | Agent làm được gì |
|---|---|---|
| A — Tự làm | Không cần hiện diện; vẫn có thể dùng desktop | Đọc/sửa code local, unit/protocol tests, build/lint, draft và review. Build có thể tốn CPU/RAM; không chiếm chuột/bàn phím. |
| B — Dành riêng desktop | Bàn giao một phiên máy, không thao tác chuột/phím/clipboard trong phiên | Browser có giao diện, native shortcut, clipboard/timer, ảnh và đo hiệu năng. Mặc định chưa coi môi trường browser cách ly là đã có. |
| C — Cần anh | Quyết định, đăng nhập/consent, vân tay, quan sát thật, quyền thay đổi hoặc phát hành | Agent chuẩn bị trước, chỉ gọi anh ở checkpoint; tự thu evidence/review sau đó. Có task chỉ cần trả lời bằng tin nhắn. |

Task có nhiều giai đoạn được tách A/B/C; không kéo anh vào phần build/review.
Phone cắm USB không chứng minh đã sẵn sàng vân tay, cũng không cấp quyền revoke.
Một task chỉ chuyển Done khi có evidence đúng phạm vi; build không thay runtime.

## Bản đồ và hàng đợi

[Danh sách task, dependencies, tiêu chí Done](task-catalog.md).
ID ở đây là work-package;15 ID backlog gốc vẫn là ID yêu cầu, không đổi/xóa.

| Hàng | Việc ưu tiên | Điều kiện |
|---|---|---|
| Tự làm ngay | A01→A02; A03/A04/A05; A06/A07 | Không cần phone, không browser GUI, chưa publication |
| Đặt lịch desktop | B01; B02/B03 sau audit và candidate liên quan | Có phiên desktop riêng; tests chỉ chạy khi thay đổi/candidate cần evidence |
| Anh trả lời một lần | C01,C02,C03,C04,C05 | Gom thông tin; thiếu phần nào chỉ chặn task phụ thuộc phần đó |
| Hẹn phone ngắn | C06; C07/C08/C09 khi helper/build/review sẵn | Fresh readiness và native preflight; một ca mỗi lượt, không chờ anh suốt build |
| Phát hành | A23→A13→B06/B07/B08→A14→C11→A22→B09 | Bug/UI gate, tài khoản/URL, version và acceptance đã đủ |
| Sau nền Phase1 | A15–A20/C10/B10 | Theo phụ thuộc QR/Autofill/authoring/Drive; không cam kết cùng đợt acceptance |

Tại22October: host/device read-only preflight Passed; phone connected/API36,
đang ngủ/khóa lúc lấy mẫu, APK và digest Cookbook gốc khớp,6 proof directories.
Không native test mới được launch. Actual401 Pending; Mobile/MVP/release Partial.

## Quy tắc bàn giao tài nguyên

1. Mặc định chọn task A Ready tiếp theo; blocked task không chặn cả project.
2. B phải báo task/candidate, tài nguyên dùng và điều kiện kết thúc trước khi chạy.
   Browser E2E hiện `headless:false`; `pnpm test` cũng chứa E2E, không phải task A.
3. Root giữ độc quyền ADB/Gradle; tối đa hai subagents cho plan/code/review.
   Không đồng thời test desktop clipboard và dùng desktop cho Google account actions.
4. C-phone: helper→compile/tests→review→freeze xong mới gọi anh; poll native ngay
   mỗi owner reply. Đến biometric/Google checkpoint thì nhường lượt ngay.
5. Hết deadline: lưu verdict, kiểm tra native còn chạy không rồi mới bàn giao máy.
   Watchdog dừng host client không chứng minh instrumentation đã dừng.
6. Không đổi ACL/nội dung Menu, account/grant, biometric enrollment hoặc publish
   chỉ vì task đã được ghi vào bảng. Tách quyền cho hành động cụ thể khi tới bước đó.
7. Push `landing-page/**` lên main có thể tự deploy GitHub Pages. A sửa local/draft;
   C05 phải giải quyết deployment route/quyền publish trước push website.
8. Giữ nguyên WIP backlog/commercialization/index/skills. Lần audit này chỉ thêm
   hai tài liệu hàng đợi, không stage hoặc đưa WIP cũ vào commit.

Mỗi task hoàn tất: ghi commit/evidence, kết quả Passed/Failed/Pending và next task.
Không đặt lịch chạy nền định kỳ trong lần phân nhóm này; chưa có khung giờ bàn giao.

## Câu hỏi còn thiếu

Bug/UI extension cụ thể; phạm vi release/Sync; publisher/support và deployment;
phương án401 được phép; phiên desktop rảnh và thời điểm anh có thể dùng phone.
Không cần trả lời hết để bắt đầu các task A Ready.
