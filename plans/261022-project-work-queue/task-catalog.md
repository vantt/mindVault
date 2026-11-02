# Task catalog —22October2026

Trạng thái: **Ready** = có thể bắt đầu phần đã mô tả; **Blocked** = chưa đủ đầu vào;
**Later** = theo sau nền hiện tại. Không dòng nào dưới đây tự nhận sản phẩm đã Done.
Nhóm A/B/C theo [master plan](plan.md). Mỗi ID dẫn tới detailed task; dependencies dùng ID ổn định.
Track plan sở hữu status/criteria; bảng này là dispatch view, cập nhật cùng report/track sau mỗi delivery.

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
dùng active plans/PRD/roadmap làm chương trình tổng. A01 đã reconcile2026-10-26 các snapshot backlog cũ ghi
Google read Pending; actual Android grant/read/mapping đã Passed chọn lọc.
[Report](reports/backlog-reconciliation.md) giữ acceptance gốc và giới hạn desktop/401.

## A — Agent tự làm, không cần chiếm desktop

| ID | Task nhỏ | Trạng thái / phụ thuộc | Done khi |
|---|---|---|---|
| [A01](plan.md#task-a01) | Reconcile trạng thái backlog/mobile links theo evidence mới | Done2026-10-26; [report/review](reports/backlog-reconciliation.md); giữ15ID và WIP | Phân biệt Android read Passed với desktop live/broader provider Pending; link chứng cứ và ngày rõ |
| [A02](phase-01-extension.md#task-a02) | Lập inventory bug/UI release-blocker từ repo | Done2026-10-26; [report](reports/a02-extension-blockers.md)/[review](reports/a02-independent-review.md); broader C01 Partial | Original ledger8ID/fresh147unit/55JS/4JSON retained;later UI01 deferral approved/locally reviewed, OWN01 MissingRepro/release gate remain |
| [A03](phase-04-release.md#task-a03) | Rà wording listing/value/security theo code | Done2026-10-26; [delivery](reports/a03-product-claims.md)/[review](reports/a03-independent-review.md); local draft only | Listing/value/README/landing corrected; Recipe/Tag/field/static checks Passed; DEMO01 retained; final artifact/deploy/Store gates unchanged |
| [A04](phase-04-release.md#task-a04) | Đối chiếu data/permissions và draft Privacy/Terms | Done2026-10-26; [delivery](reports/a04-legal-data-review.md)/[review](reports/a04-independent-review.md); C04/C05/A05/B02 final gates |9declarations/data/retention/sharing/hosting/Store drafts, Closed source/notices và local HTML verified; no publication/legal certification |
| [A05](phase-01-extension.md#task-a05) | Audit `tabs`/`activeTab`, thiết kế kiểm chứng tối thiểu quyền | Done2026-10-26; [audit](reports/a05-permission-audit.md)/[review](reports/a05-independent-review.md); runtime B02 Pending |11calls/5direct modules,3permission-only proposals và B02 protocol verified; manifest unchanged/no minimum-necessity conclusion |
| [A06](phase-01-extension.md#task-a06) | Harden đóng gói: mandatory WASM/resources/imports | Done2026-10-26; [delivery](reports/a06-packaging-validation.md)/[review](reports/a06-independent-review.md); development only |49package/147unit,73-file ZIP/resource/notices/provenance and independent artifact checks Passed; runtime/Golden unchanged; no release acceptance |
| [A07](phase-01-extension.md#task-a07) | Chuẩn bị thay CI artifact name và checks trước ZIP | Done2026-10-26; [delivery](reports/a07-ci-packaging.md)/[review](reports/a07-independent-review.md); remoteCI NotRun | Validated development ZIP/inventory/SHA,Node24/pnpm10.24,frozen install,14day retention;local checksPassed,landing deployment unchanged |
| [A08](phase-04-release.md#task-a08) | Chuẩn bị brief/caption5 screenshot và inventory icon/OG/promo | Done2026-10-26; [brief/inventory](reports/a08-assets-brief.md)/[review](reports/a08-independent-review.md); captures B06 Pending | Five actual-UI briefs/official specs+asset metadata verified; no captures/artwork/rights approval; OG missing and680px source retained |
| [A09](phase-04-release.md#task-a09) | Draft hướng dẫn Chef và reviewer | Done2026-10-26; [guides](reports/a09-chef-reviewer-guides.md)/[review](reports/a09-independent-review.md); docs only | Source/synthetic/API checks Passed; actual-handler FA01 confirmed,restore explicitly blocked and routed A23; no product/restore acceptance |
| [A10](phase-01-extension.md#task-a10) | Chuẩn bị protocol Chrome Sync/conflict và upgrade | Done2026-10-26; [protocol](reports/a10-sync-upgrade-protocol.md)/[review](reports/a10-independent-review.md); real cases NotRun |13Sync/upgrade cases,sameID2devices,preservation/conflict/recovery/timeout/cleanup;reload≠installedupgrade;C02/C03/A13/B08 gates retained |
| [A11](phase-02-mobile.md#task-a11) | Chuẩn bị mobile401 native candidate/runner | Blocked C06 | Reuse provider baseline; retained-token tab read; provenance401, reconnect/token cleared/cache/jars/old Dish; compile/review/freeze; chưa runtime |
| [A12](phase-02-mobile.md#task-a12) | Tách mobile Pending thành fixtures có thể chạy riêng | Done2026-10-26; [8cards](reports/a12-mobile-acceptance-preparation.md)/[review](reports/a12-independent-review.md); allNotRun | Existing G1fixture vsMissingValidExercise combinations explicit;9methods/3APK/23links checked;no build/phone/helper/new acceptance |
| [A13](phase-01-extension.md#task-a13) | Lắp release candidate local và provenance | A06/A07/C02 Done;[held provenance](reports/a13-release-candidate.md)/[review](reports/a13-held-independent-review.md) Passed;A13NOTDone C01/A23 | Manifest-root74files/SHA278b59…f1e9e/source+notices verified;not stable/final acceptance untilremaining blocker disposition+freeze;B07/B08/WinStable116/Sync stillrequired |
| [A14](phase-04-release.md#task-a14) | Hoàn thiện submission packet trước nộp | A03/A04/A08/A09 Done; Blocked A13/B06/B07/B08/C04/C05 | ZIP/ảnh/reviewer guide/legal URLs/claims nhất quán để C11 duyệt; chưa cần listing URL đã publish |
| [A15](phase-02-mobile.md#task-a15) | QR receiver: plan parser/reassembly và native camera/import | Later; p-542801dc,p-c46b217c; QR sender/decrypt đã có | Tách parser→scanner→preview→commit; compact/chunks/expiry/invalid dữ liệu; code/unit/build trước C10, không coi sender là receiver |
| [A16](phase-02-mobile.md#task-a16) | Onboarding browser-first/mobile-first và import vào encrypted storage | Later; A15 + Core/Keystore hiện có | Branch rõ, không ghi đè Cookbook âm thầm; review/storage regression; physical acceptance C10 |
| [A17](phase-02-mobile.md#task-a17) | Autofill feasibility spike local | Later; Phase1 usable; có plan riêng | Service/lock→biometric→Cook/domain-package boundary prototype; build/test; chưa nhận latency/RAM thực tế khi chưa B10/C10 |
| [A18](phase-02-mobile.md#task-a18) | Authoring thêm mới rồi edit-existing, tách2 deliverables | Later; p-c1480680,p-fa39ea00; read/mapping UI | Hai plan riêng; preview/explicit append hoặc đúng-row update/rotate/error preservation; local checks trước quyền ghi Menu fixture |
| [A19](phase-02-mobile.md#task-a19) | Extension Drive-sync rồi mobile Drive transport | Later; p-e117d9aa; nền mobile/extension | Tách extension sync, consent, mobile import và conflict; không thêm scope/đổi grant khi chưa C-approved |
| [A20](phase-02-mobile.md#task-a20) | Multi-profile mobile và service Autofill production | Later; A16/A17; quyết định mobile hiện có | Plan/domain mapping/ownership riêng; đo spike rồi mới production; không mở iOS khi thiếu Mac |
| [A21](phase-01-extension.md#task-a21) | Rà khả năng chạy browser trong display/VM riêng | Done2026-10-26; [audit](reports/a21-browser-isolation.md)/[review](reports/a21-independent-review.md); isolation Unverified | Cached Xvfb/source/resource feasibility checked;future canaries only,noGUI or B reclassification;desktop/native/clipboard gates retained |
| [A22](phase-03-website.md#task-a22) | Cập nhật CTA bằng listing thật và deploy theo quyền đã chốt | Blocked C05/C11; listing URL published | Website/CTA trỏ đúng listing; bản deployed sẵn sàng cho B09 kiểm chứng |
| [A23](phase-01-extension.md#task-a23) | Repair each identified blocker/UI | Partial;[heldZIP full suite/fault Passed/reviewed](reports/b07-run-01-independent-review.md);[nativeP0 review](reports/b03-native-stable-independent-review.md) | FA01/UI01 known scope verified;global defaults deferred/old malformed imports unmigrated. C01 other observations unconfirmed,not prerequisite to provisional testing;stable/116/upgrade/Sync acceptance separate |

Các task Ready là sẵn sàng bắt đầu làm, không phải đã có patch hoàn chỉnh.
Node/pnpm/JDK/SDK availability kiểm tra lúc nhận task; thiếu runtime xử lý local,
không biến thành yêu cầu anh ngồi chờ. Gradle/Argon2 build có thể nặng máy.

## B — Cần một phiên desktop riêng; thường anh không cần ngồi cạnh

| ID | Task nhỏ | Trạng thái / phụ thuộc | Done khi |
|---|---|---|---|
| [B01](phase-01-extension.md#task-b01) | Selected/full browser regression on exact artifact | Partial;[held2.3.1 full28Passed/reviewed](reports/b07-run-01-independent-review.md) | Exact extracted ZIP151unit+28E2E/320.237s browser/no retry/skip/error;cleanup verified. Historical3case source proof retained;stable/full-platform acceptance separate |
| [B02](phase-01-extension.md#task-b02) | Verify disposable permission variants | Partial2026-11-02;[P0–P3 24/24selected cases Passed](reports/b02-permission-runtime-20261102.md)/[review](reports/b02-runtime-20261102-independent-review.md) | Actual8/7/7/6grants;2P0harness failures retained;source74/tests46/heldZIP unchanged. Two separately approved15min slots,6profiles/2workspaces cleaned,desktop returned. Native/grant/clipboard/provider/edge/fullsupport acceptance and minimum-permission decision remain open;no adoption |
| [B03](phase-01-extension.md#task-b03) | Argon2 production unlock/startup/RAM and browser support observations | Done2026-10-30 scopedWindowsStable156.76/CfT116;[actual](reports/b03-measurements-20261030.md)/[review](reports/b03-measurements-20261030-independent-review.md) | P0–P4selectedcells complete:10cold+20warm each,5fastRAM intervals each,failclosed/restored;descriptive/noSLO/no truepeak/fullplatform claim. Source/tests/ZIP retained,ownedcleanup complete |
| [B04](phase-01-extension.md#task-b04) | Native shortcut/clipboard/timer trên patch liên quan | Chờ candidate và phiên máy | Dùng clipboard synthetic, không input ngoài ca; selected flow/evidence/cleanup; tránh replay acceptance không liên quan |
| [B05](phase-03-website.md#task-b05) | Landing responsive/keyboard/reduced-motion/performance | Chờ phiên máy; draft A03/A04; final sau assets/URLs | Viewport/keyboard/measurements/findings; sửa theo lỗi; local QA không chứng minh deployed site |
| [B06](phase-04-release.md#task-b06) | Chụp5 ảnh sản phẩm từ stable release candidate | A08/C02 Done;Blocked C01/A13 + desktop slot/isolation | Ảnh thật synthetic, không thông tin riêng; captions/kích thước/locale phù hợp yêu cầu Store được recheck lúc làm |
| [B07](phase-01-extension.md#task-b07) | Full pnpm test + clean ZIP/WASM smoke | Partial;[exact heldZIP full suite+fault Passed](reports/b07-zip-suite.md)/[review](reports/b07-run-01-independent-review.md) |151unit+28E2E and separate same-ID baseline/missing/corrupt/restored248.666s Passed;46tests unchanged,cleanup verified. Stable candidate/required platform/upgrade gates remain |
| [B08](phase-01-extension.md#task-b08) | ZIP upgrade/security/profile/sharing/ENVI and selected Sync | Partial;[local2.3.0→2.3.1 Passed](reports/b08-artifact-acceptance.md)/[independent review](reports/b08-upgrade-20261030-independent-review.md) | Exact8e92→278b/sameID/2nativeexits,allSync+local/10jars/hints/2Menus/VI/4DishTags preserved. Local unpacked only;Store/fullformat/real2deviceSync still required,owner Sync later |
| [B09](phase-04-release.md#task-b09) | Store-installed smoke và deployed landing/legal URLs | Blocked C11/A22 + public URLs | Artifact Store thực cài/chạy, links/HTTPS/CTA đúng; phân biệt upload với published/accepted |
| [B10](phase-02-mobile.md#task-b10) | Autofill/QR handoff liên nền desktop-phone | Later feature A15/A17 phù hợp; C10 readiness/quyền trước run, không cần C10 Done | Synthetic sender/receiver thực hoặc autofill apps/browsers thật; owner authentication đúng; desktop riêng khi QR/clipboard cần |

B01/B07 không phải hai lần bắt buộc chạy full suite: tái dùng evidence nếu đúng cùng
candidate/artifact và không có thay đổi liên quan; final ZIP acceptance vẫn tách riêng.

Nếu sau này chứng minh được browser display riêng không ảnh hưởng desktop owner,
có thể giảm yêu cầu bàn giao cho ca tương ứng; hiện chưa nhận headless/isolation
chỉ bằng sửa một cờ. GUI automation vẫn có thể dùng focus/clipboard toàn hệ thống.

## C — Cần owner: quyết định trước, thao tác ngắn sau

| ID | Task nhỏ / phần anh cần | Trạng thái / phụ thuộc | Agent chuẩn bị / Done khi |
|---|---|---|---|
| [C01](phase-01-extension.md#task-c01) | Owner blocker/UI disposition | Partial;[UI01 approved;provisional proceed and actual review recorded](reports/c01-owner-blocker-decisions.md) | Known fixes verified on heldZIP;other observations unconfirmed. Do not repeat no-other-bugs question before testing;no blanket final-release acceptance |
| [C02](phase-01-extension.md#task-c02) | Chốt release boundary/version/channel/Chrome-OS; Sync phải pass hay deferred có công bố | Done2026-10-27;[owner decision](reports/c02-release-boundary.md)/[local follow-through reviewed](reports/c02-version-231-independent-review.md) |2.3.1+packagealigned/controlledbeta/Win11x64Stable+116/Syncmandatory approved;verificationPending,no account/publication or blanketC01 approval |
| [C03](phase-01-extension.md#task-c03) | Two-device Chrome Sync readiness | Partial;[owner second computer available,slot later](reports/c03-sync-owner-readiness.md) | “Có, nhưng để lượt sau”;actual Sync NotRun,mandatory before-publication gate retained. Later device/account/same-ID route/login readiness required |
| [C04](phase-04-release.md#task-c04) | Publisher identity/account, support/security contact, visibility/locales | Pending | Agent gom form và draft; anh chọn/đăng ký/verify account theo dashboard; không gửi credential qua chat |
| [C05](phase-03-website.md#task-c05) | Xác nhận domain ownership/Cloudflare service, deploy route và quyền publish | Pending | Domain/nền tảng đã chọn, không hỏi lại; agent nêu GitHub Pages auto-deploy hiện hữu; URL/HTTPS/support route kiểm tra thực sau quyền cần thiết |
| [C06](phase-02-mobile.md#task-c06) | Chọn/duyệt điều kiện401 thật và tác động project | Pending; chưa coi “cắm phone/làm đi” là chỉ định revoke cụ thể | Agent đã có phương án remote grant removal + retained tab read; xác định đúng connection/affected clients/restoration, A11 xong mới hẹn vân tay/remote action; propagation không bảo đảm |
| [C07](phase-02-mobile.md#task-c07) | Genuine Google human cancellation/resultCode và provider-loaded stale reply | A12 Done; Blocked external resolution/owner/frozen candidate;G2/G3 exercise gaps | Anh biometric/cancel ở checkpoint; agent theo native và provenance; host Back Passed không thay human action; không loop grant reuse |
| [C08](phase-02-mobile.md#task-c08) | Mobile lifecycle/clipboard: recreation/background/process death/task restore | A12 Done; Blocked per-case valid exercise/reviewed fixture + owner slot | Anh setup/reopen khi cần; agent chạy phần USB đã cho phép; không gọi SIGKILL là natural LMK; OS history không có guarantee xóa |
| [C09](phase-02-mobile.md#task-c09) | TalkBack thật, font/orientation/layout và quan sát UX | A12 Done; Blocked sustained manual fixture/settings authorization + lịch phone | Anh nghe/quan sát các ca cần cảm nhận; automated nodes/layout tách evidence; không lặp screenshot/Recents Passed |
| [C10](phase-02-mobile.md#task-c10) | QR import/Autofill/write acceptance, từng feature một | Later; feature A15–A20 phù hợp | Readiness/quyền camera/biometric/OS service/fixture trước B10 nếu ca cần; final acceptance sau evidence B10 hoặc selected phone run; không yêu cầu B10 trước readiness, không đổi Menu thật mặc định |
| [C11](phase-04-release.md#task-c11) | Duyệt submission/publication/deploy cuối và thao tác account | Blocked A14 | Packet/ảnh/ZIP/reviewer material review xong mới nộp; account/fee/legal approvals do owner; không tự publish hay gửi quảng bá |
| [C12](phase-03-website.md#task-c12) | Testimonial/claims có nguồn và quyền dùng | Pending; A03/A08 | Anh cung cấp provenance nếu có; agent loại claim không chứng minh khỏi draft thay vì chờ/bịa quote; không chặn các draft khác |
| [C13](phase-02-mobile.md#task-c13) | Keystore residual hardware/API26/no enrollment/typed callbacks | Later; cần môi trường phù hợp | Agent giữ matrix; owner cung cấp thiết bị/quyền đổi enrollment nếu chọn ca; phone API36 không thay evidence môi trường khác |

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
