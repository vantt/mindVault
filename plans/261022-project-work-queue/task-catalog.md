# Task catalog —22October2026

Trạng thái: **Ready** = có thể bắt đầu phần đã mô tả; **Blocked** = chưa đủ đầu vào;
**Later** = theo sau nền hiện tại. Không dòng nào dưới đây tự nhận sản phẩm đã Done.
Nhóm A/B/C theo [quy tắc bàn giao](plan.md). Dependencies dùng ID task trong bảng.

## Nguồn và giới hạn audit

- [Backlog15ID](../../docs/backlog.md), [active plans](../README.md),
  [roadmap](../../docs/development-roadmap.md), [MVP](../../docs/prd/v1-mvp.md).
- [Mobile plan](../261012-mobile-cookbook-phase1/plan.md),
  [remaining acceptance](../261012-mobile-cookbook-phase1/phase-05-verification-delivery.md#ownerdevice-acceptance--partial),
  [401 preparation](../261012-mobile-cookbook-phase1/reports/google-http401-preparation.md),
  [Keystore residual matrix](../../archive/plans/261011-keystore-recovery/reports/current-device-acceptance.md).
- [Extension completion](../../archive/plans/261012-extension-completion/plan.md),
  [security contract](../../docs/specs/extension-security.md),
  [E2E fixture](../../chrome-extension/test/e2e/fixtures.ts),
  [test commands](../../chrome-extension/package.json).
- [Landing audit](../../landing-page-improve.md),
  [commercialization Phase0](../../docs/commercialization/plan.md),
  [permissions](../../docs/commercialization/legal/permissions-review.md),
  [publisher/hosting](../../docs/commercialization/operations/publisher-and-support.md),
  [auto-deploy workflow](../../.github/workflows/build_and_deploy.yml).

Audit đọc cả WIP đang có. Không có file tên `program` trong inventory liên quan;
dùng active plans/PRD/roadmap làm chương trình tổng. Một số snapshot backlog cũ ghi
Google read Pending; actual Android grant/read/mapping đã Passed chọn lọc. A01 sửa
trạng thái dẫn nguồn, không xóa acceptance gốc hoặc suy rộng sang desktop/401.

## A — Agent tự làm, không cần chiếm desktop

| ID | Task nhỏ | Trạng thái / phụ thuộc | Done khi |
|---|---|---|---|
| A01 | Reconcile trạng thái backlog/mobile links theo evidence mới | Ready; giữ15ID và WIP | Phân biệt Android read Passed với desktop live/broader provider Pending; link chứng cứ và ngày rõ |
| A02 | Lập inventory bug/UI release-blocker từ repo | Ready; bổ sung owner observations qua C01 | Mỗi issue có repro hoặc MissingRepro, expected/actual, phạm vi; không bịa bug hoặc coi gate đã mở |
| A03 | Rà wording listing/value/security theo code | Ready | Recipe hợp lệ; nấu Dish khác mã hóa Cookbook; Sync/Tag/Full Access/clipboard đúng contract; bỏ claim tuyệt đối/thiếu evidence trong draft |
| A04 | Đối chiếu data/permissions và draft Privacy/Terms | Ready; contact cuối cần C04 | Ma trận Sync/local/session/reset/sharing/clipboard/hosting; Closed source nhất quán; giữ notices; HTML sửa local chưa publish |
| A05 | Audit `tabs`/`activeTab`, thiết kế kiểm chứng tối thiểu quyền | Ready; kết luận runtime qua B02 | Call sites, phương án giữ/bỏ, affected flows và test matrix; không tự bỏ quyền từ suy đoán |
| A06 | Harden đóng gói: mandatory WASM/resources/imports | Ready | Lệnh repeatable, thiếu JS/WASM bắt buộc thì fail; kiểm tra ZIP inventory và notices; local tests có ý nghĩa, chưa release |
| A07 | Chuẩn bị thay CI artifact name và checks trước ZIP | Ready; A06 | Patch workflow được review, test/validation trước ZIP; giữ deployment thay đổi riêng C05; không tự chỉnh phiên bản release |
| A08 | Chuẩn bị brief/caption5 screenshot và inventory icon/OG/promo | Ready | Nội dung khớp UI thật, file/spec rõ; artwork không giả product screenshot; ảnh UI cuối chờ B06 |
| A09 | Draft hướng dẫn Chef và reviewer | Ready | Setup, Recipe, manual Cook, backup/restore, Full Access và rotate; synthetic fixture; không account/Spices thật |
| A10 | Chuẩn bị protocol Chrome Sync/conflict và upgrade | Ready; real execution C03/B08 | Kịch bản2 thiết bị, đồng thời/offline/conflict/recovery; expected không mất Cookbook, evidence và cleanup rõ |
| A11 | Chuẩn bị mobile401 native candidate/runner | Blocked C06 | Reuse provider baseline; retained-token tab read; provenance401, reconnect/token cleared/cache/jars/old Dish; compile/review/freeze; chưa runtime |
| A12 | Tách mobile Pending thành fixtures có thể chạy riêng | Ready; không replay Passed | Checklist cancellation/resultCode, lifecycle/clipboard, TalkBack/layout; mỗi ca prerequisite, natural lock, owner checkpoint, verdict và cleanup; chỉ code helper khi ca khả thi |
| A13 | Lắp release candidate local và provenance | Blocked C01/C02; A06/A07/A23 | Version/commit/ZIP root manifest/SHA256/inventory, stable UI, blockers đóng; chuẩn bị cho B07/B08 |
| A14 | Hoàn thiện submission packet trước nộp | Blocked A03/A04/A08/A09/A13/B06/B07/B08/C04/C05 | ZIP/ảnh/reviewer guide/legal URLs/claims nhất quán để C11 duyệt; chưa cần listing URL đã publish |
| A15 | QR receiver: plan parser/reassembly và native camera/import | Later; p-542801dc,p-c46b217c; QR sender/decrypt đã có | Tách parser→scanner→preview→commit; compact/chunks/expiry/invalid dữ liệu; code/unit/build trước C10, không coi sender là receiver |
| A16 | Onboarding browser-first/mobile-first và import vào encrypted storage | Later; A15 + Core/Keystore hiện có | Branch rõ, không ghi đè Cookbook âm thầm; review/storage regression; physical acceptance C10 |
| A17 | Autofill feasibility spike local | Later; Phase1 usable; có plan riêng | Service/lock→biometric→Cook/domain-package boundary prototype; build/test; chưa nhận latency/RAM thực tế khi chưa B10/C10 |
| A18 | Authoring thêm mới rồi edit-existing, tách2 deliverables | Later; p-c1480680,p-fa39ea00; read/mapping UI | Hai plan riêng; preview/explicit append hoặc đúng-row update/rotate/error preservation; local checks trước quyền ghi Menu fixture |
| A19 | Extension Drive-sync rồi mobile Drive transport | Later; p-e117d9aa; nền mobile/extension | Tách extension sync, consent, mobile import và conflict; không thêm scope/đổi grant khi chưa C-approved |
| A20 | Multi-profile mobile và service Autofill production | Later; A16/A17; quyết định mobile hiện có | Plan/domain mapping/ownership riêng; đo spike rồi mới production; không mở iOS khi thiếu Mac |
| A21 | Rà khả năng chạy browser trong display/VM riêng | Ready; chỉ audit/config local trước | Xác định isolation focus/clipboard/input và loại ca chuyển được khỏi B; không gọi UI acceptance Passed từ mock hoặc chỉ đổi headless flag |
| A22 | Cập nhật CTA bằng listing thật và deploy theo quyền đã chốt | Blocked C05/C11; listing URL published | Website/CTA trỏ đúng listing; bản deployed sẵn sàng cho B09 kiểm chứng |
| A23 | Sửa từng blocker/UI đã định danh, một patch một mục | Blocked issue xác định qua A02/C01; regression B01/B04 khi cần | Code/unit/review và affected UI regression đạt acceptance của issue; đóng bug bằng evidence, không chỉ owner xác nhận danh sách; nếu không có blocker thì ghi rõ N/A |

Các task Ready là sẵn sàng bắt đầu làm, không phải đã có patch hoàn chỉnh.
Node/pnpm/JDK/SDK availability kiểm tra lúc nhận task; thiếu runtime xử lý local,
không biến thành yêu cầu anh ngồi chờ. Gradle/Argon2 build có thể nặng máy.

## B — Cần một phiên desktop riêng; thường anh không cần ngồi cạnh

| ID | Task nhỏ | Trạng thái / phụ thuộc | Done khi |
|---|---|---|---|
| B01 | Chạy selected headed E2E khi patch cần regression; full suite ở final candidate | Chờ phiên máy; patch hoặc A13 | JUnit/report thực, Golden giữ nguyên; tách unit với E2E, không coi old147/25 là kết quả mới |
| B02 | Kiểm chứng candidate giảm quyền | Blocked A05 | Picker/Menu/shortcut/lock/clipboard hoạt động với đúng manifest candidate; quyết định quyền có evidence |
| B03 | Đo Argon2 timing/memory và browser support | Blocked C02 + protocol local | Ghi Chrome/OS/hardware/params và số đo thật, fail-closed; không hạ KDF hoặc suy diễn Chrome116 từ Chromium143 |
| B04 | Native shortcut/clipboard/timer trên patch liên quan | Chờ candidate và phiên máy | Dùng clipboard synthetic, không input ngoài ca; selected flow/evidence/cleanup; tránh replay acceptance không liên quan |
| B05 | Landing responsive/keyboard/reduced-motion/performance | Chờ phiên máy; draft A03/A04; final sau assets/URLs | Viewport/keyboard/measurements/findings; sửa theo lỗi; local QA không chứng minh deployed site |
| B06 | Chụp5 ảnh sản phẩm từ stable release candidate | Blocked C01/C02/A08/A13 | Ảnh thật synthetic, không thông tin riêng; captions/kích thước/locale phù hợp yêu cầu Store được recheck lúc làm |
| B07 | Full `pnpm test` + load ZIP sạch và resource smoke | Blocked A13 + phiên máy | Kết quả thật cho đúng ZIP/source; resources/WASM/offscreen/fail-closed; no Golden changes |
| B08 | ZIP upgrade/security/profile/sharing/ENVI và Sync đã chọn | Blocked A10/A13/C02; C03 nếu real Sync | Cookbook upgrade giữ nguyên; Menu/native Cook/Tag/Reveal/Copy/session/QR các ca cần thiết pass hoặc release gate vẫn đóng |
| B09 | Store-installed smoke và deployed landing/legal URLs | Blocked C11/A22 + public URLs | Artifact Store thực cài/chạy, links/HTTPS/CTA đúng; phân biệt upload với published/accepted |
| B10 | Autofill/QR handoff liên nền desktop-phone | Later A15/A17; C10 | Synthetic sender/receiver thực hoặc autofill apps/browsers thật; owner authentication đúng; desktop riêng khi QR/clipboard cần |

B01/B07 không phải hai lần bắt buộc chạy full suite: tái dùng evidence nếu đúng cùng
candidate/artifact và không có thay đổi liên quan; final ZIP acceptance vẫn tách riêng.

Nếu sau này chứng minh được browser display riêng không ảnh hưởng desktop owner,
có thể giảm yêu cầu bàn giao cho ca tương ứng; hiện chưa nhận headless/isolation
chỉ bằng sửa một cờ. GUI automation vẫn có thể dùng focus/clipboard toàn hệ thống.

## C — Cần owner: quyết định trước, thao tác ngắn sau

| ID | Task nhỏ / phần anh cần | Trạng thái / phụ thuộc | Agent chuẩn bị / Done khi |
|---|---|---|---|
| C01 | Cung cấp/duyệt danh sách bug/UI đang chặn release | Pending; A02 giảm phần phải hỏi | Agent gom issue trước; anh xác nhận lỗi còn gặp; repro/acceptance rõ rồi agent tự sửa, không yêu cầu kể lại lịch sử đã có |
| C02 | Chốt release boundary/version/channel/Chrome-OS; Sync phải pass hay deferred có công bố | Pending | Agent đề xuất từ evidence; quyết định ghi rõ, không mở lại PassChef/Closed source/A2/lifetime |
| C03 | Chuẩn bị2 Chrome devices/accounts cho real Sync | Pending; A10 và lịch riêng | Anh sign-in/authorize khi cần; dùng synthetic Cookbook, không đụng profile cá nhân; agent chạy phần còn lại theo quyền cụ thể |
| C04 | Publisher identity/account, support/security contact, visibility/locales | Pending | Agent gom form và draft; anh chọn/đăng ký/verify account theo dashboard; không gửi credential qua chat |
| C05 | Xác nhận domain ownership/Cloudflare service, deploy route và quyền publish | Pending | Domain/nền tảng đã chọn, không hỏi lại; agent nêu GitHub Pages auto-deploy hiện hữu; URL/HTTPS/support route kiểm tra thực sau quyền cần thiết |
| C06 | Chọn/duyệt điều kiện401 thật và tác động project | Pending; chưa coi “cắm phone/làm đi” là chỉ định revoke cụ thể | Agent đã có phương án remote grant removal + retained tab read; xác định đúng connection/affected clients/restoration, A11 xong mới hẹn vân tay/remote action; propagation không bảo đảm |
| C07 | Genuine Google human cancellation/resultCode và provider-loaded stale reply | Blocked A12 + external resolution khả thi | Anh biometric/cancel ở checkpoint; agent theo native và provenance; host Back Passed không thay human action; không loop grant reuse |
| C08 | Mobile lifecycle/clipboard: recreation/background/process death/task restore | Blocked A12 + fixture riêng | Anh setup/reopen khi cần; agent chạy phần USB đã cho phép; không gọi SIGKILL là natural LMK; OS history không có guarantee xóa |
| C09 | TalkBack thật, font/orientation/layout và quan sát UX | Blocked A12 + lịch phone | Anh nghe/quan sát các ca cần cảm nhận; automated nodes/layout tách evidence; không lặp screenshot/Recents Passed |
| C10 | QR import/Autofill/write acceptance, từng feature một | Later; A15–A20/B10 | Anh cấp camera/biometric/OS service hay quyền ghi fixture cụ thể; agent làm code/build trước; không đổi nội dung Menu thật mặc định |
| C11 | Duyệt submission/publication/deploy cuối và thao tác account | Blocked A14 | Packet/ảnh/ZIP/reviewer material review xong mới nộp; account/fee/legal approvals do owner; không tự publish hay gửi quảng bá |
| C12 | Testimonial/claims có nguồn và quyền dùng | Pending; A03/A08 | Anh cung cấp provenance nếu có; agent loại claim không chứng minh khỏi draft thay vì chờ/bịa quote; không chặn các draft khác |
| C13 | Keystore residual hardware/API26/no enrollment/typed callbacks | Later; cần môi trường phù hợp | Agent giữ matrix; owner cung cấp thiết bị/quyền đổi enrollment nếu chọn ca; phone API36 không thay evidence môi trường khác |

## Cách chia phiên để anh không phải chờ

1. **Phiên tự làm:** A01–A10/A12 phù hợp; task độc lập chạy tuần tự hoặc review tối đa
   hai subagents; không mở browser/phone runtime. Câu hỏi gom theo C01–C06/C12.
2. **Phiên desktop:** chọn đúng B task/candidate khi anh rảnh máy; agent báo kết thúc
   sau native/browser cleanup, không cần anh ngồi xem nếu không có login checkpoint.
3. **Phiên phone:** một mục C06/C07/C08/C09 đã Ready; anh chỉ setup/vân tay/thao tác
   có marker. Sau đó agent phân tích, review và ghi kết quả khi anh quay lại việc khác.
4. **Phiên phát hành:** gom C04/C05/C11; không biến mua domain/registration/publish
   thành tác vụ tự động chỉ vì mọi thứ đã được liệt kê trong backlog.

Chưa ấn định thời lượng: lấy duration từ đúng fixture/candidate sau chuẩn bị;
không hứa một phiên vân tay dài vài giây nếu có consent/timeout chưa biết.

## Later/optional, không phải blocker hiện tại

Payment/provider/license enforcement và A2 gating; analytics website; campaign/posts/
video; light mode, thêm animation/section; audit bên thứ ba; iOS/Kotlin Native khi
có Mac. Các quyết định thương mại đã có giữ nguyên; implementation có plan riêng.
Không tự tạo automation định kỳ hoặc gửi thông điệp ra ngoài từ danh sách này.

Unresolved: C01–C06/C12 và lịch bàn giao; C03/C13 thêm thiết bị; candidate/evidence
cho từng ca Pending. Chưa đổi status Partial của Mobile/MVP/website/release.
