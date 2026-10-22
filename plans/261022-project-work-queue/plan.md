# PassChef — master execution plan

Ngày:2026-10-22, Asia/Saigon. **Program Partial; planning system Ready**.
Baseline:`7dea4e5` + WIP được giữ nguyên. Entry:[plans index](../README.md).

## Mục tiêu và source of truth

Điều phối toàn project qua46 task nhỏ, dependencies và tài nguyên A/B/C.
[Backlog](../../docs/backlog.md) sở hữu15 requirement IDs/acceptance gốc;
PRD/decisions giữ scope sản phẩm. Master sở hữu rollup/ưu tiên/cross-track gates;
track plan sở hữu task status/checklist/deliverables; report sở hữu kết quả/evidence.
[Task catalog](task-catalog.md) là dispatch view dẫn tới task trong từng track.
Không thay report/code acceptance bằng trạng thái planning Done.

## Cây plan → detail → evidence

| Track / priority | Detailed plan | Rollup / dependencies | Existing evidence |
|---|---|---|---|
|01 Extension validation/tooling|[Track01](phase-01-extension.md)|Partial; blocker inventory→fix→candidate; desktop/Sync cells cần lịch|[Historical completion](../../archive/plans/261012-extension-completion/reports/verification.md)|
|02 Mobile|[Track02](phase-02-mobile.md)→[Cookbook5phases](../261012-mobile-cookbook-phase1/plan.md)|Partial; preparation trước owner runtime; QR/Autofill/write/Drive Later|[Verification](../261012-mobile-cookbook-phase1/reports/verification.md), [review](../261012-mobile-cookbook-phase1/reports/independent-review.md)|
|03 Website|[Track03](phase-03-website.md)→[landing scope](../../landing-page-improve.md)|Partial; claims/assets từ04, deployment choice trước push, listing sau publish|[Operations record](../../docs/commercialization/operations/publisher-and-support.md); QA Pending|
|04 Release|[Track04](phase-04-release.md)→[commercialization](../../docs/commercialization/plan.md)|Partial/Pending;01candidate+03public URLs→packet→owner publication→CTA/smoke|[Historical extension recheck](../reports/261012-extension-recheck.md); artifact acceptance Pending|

[Current system audit/report](reports/program-audit.md) phân biệt planning checks
với product verification. Existing reports giữ nguyên ngày/status/historical limits.
Future report chưa có ghi path dự kiến trong track; chỉ link khi file đã tồn tại.

## Dispatch A/B/C và dependency order

| Mode | Task count | Điều kiện |
|---|---:|---|
|A — agent tự làm|23|Local code/unit/build/lint/draft/review; anh dùng desktop được, build có thể nặng CPU/RAM|
|B — desktop riêng|10|Browser GUI/native shortcut/clipboard/ảnh/QA; bàn giao input/focus/clipboard|
|C — owner action/decision|13|Tin nhắn quyết định hoặc login/consent/biometric/observation/publish; chuẩn bị xong mới gọi|

Queue đầu:A01→A02; A03/A04/A05; A06/A07; A09/A10/A12/A21 độc lập khi phù hợp.
Release chain:A23→A13→B06/B07/B08→A14→C11→A22→B09.
B06 còn cần A08; A14 còn cần public URLs/account decisions; xem deps từng row.
C06 chọn transition trước A11, actual phone runtime sau A11 review/freeze.
C10 readiness/quyền trước B10 khi ca cần; C10 final acceptance sau runtime evidence.
Later:A15/A16 QR→A17 spike/Autofill; A18 write; A19 Drive→A20 profiles/service.
Không coi mũi tên rút gọn là thay toàn bộ prerequisites trong detailed plan.

## Coordination task

<a id="task-a01"></a>**A01 — Ready:** reconcile dated backlog/roadmap links với evidence
hiện tại, giữ15ID và WIP. Android Google read Passed chọn lọc không đồng nghĩa desktop
live/broader provider/401 Passed. Deliverable:index/backlog delta có link đúng scope;
report `reports/backlog-reconciliation.md` khi thực hiện. Planning-system audit hiện
có không tự đóng A01; task này còn cập nhật snapshot nội dung cũ.

## Execution/update protocol

1. Agent chọn A Ready khi không có owner/desktop slot; blocked row chỉ chặn dependents.
2. Trước B ghi candidate/tài nguyên/checkpoint/end condition; E2E hiện headed,
   `pnpm test` có E2E. A21 phải chứng minh isolation trước đổi phân nhóm.
3. Root độc quyền ADB/Gradle, tối đa2subagents; pnpm JS, uv Python; Golden bất biến.
4. C-phone:prepare→build/tests→review→freeze→fresh readiness/native audit→selected run.
   Yield ở biometric/Google; poll native ngay mỗi reply. Watchdog không chứng minh native dừng.
5. Account/grant/ACL/enrollment/write/publish cần quyền đúng hành động; USB không thay
   readiness. Website main push có thể deploy GitHub Pages; C05 giải quyết route/quyền.
6. Kết thúc task:report với source/artifact/command/result/limits→track task row→catalog
   dispatch row→master rollup/index nếu thay đổi. Failed/Unknown/NotExercised giữ nguyên.
7. Chỉ Done khi đạt exit criteria và evidence; quyết định deferred ghi owner/scope,
   không đổi thành Passed. Không replay accepted cases khi không có thay đổi liên quan.
8. Khi scope mới lớn:create child plan dưới plans/date-feature với reports riêng,
   nối từ track/master. Không tự triển khai mọi Later, tạo lịch định kỳ hay gửi quảng bá.

## Unresolved

Bug/UI specifics, release/Sync boundary, publisher/support/deployment,401 transition,
owner/desktop slots và extra device matrix. Không chặn các A Ready độc lập.
