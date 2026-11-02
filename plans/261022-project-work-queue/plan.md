# PassChef — master execution plan

Ngày:2026-10-22, Asia/Saigon. **Program Partial; planning system Ready**.
Baseline:`7dea4e5` historical;[checkpoint2Nov](reports/repository-checkpoint-20261102.md) records committed source/push. Entry:[plans index](../README.md).

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

Queue:A01–A10/A12/A21 Done; A23 FA01 LocallyVerified/UI01 owner-approved deferral LocallyVerified2026-10-26; overallPartial, no A Ready.
A03 [claims review](reports/a03-independent-review.md), A04 [legal/data review](reports/a04-independent-review.md):local prep only; DEMO01/approval/runtime gates Pending.
A02 [inventory](reports/a02-extension-blockers.md)/[independent review](reports/a02-independent-review.md):
UI01/FA01 [heldZIP full151unit+28E2E+WASM fault Passed/reviewed](reports/b07-run-01-independent-review.md);desktop/workspaces released;stable candidate/C01 observations/upgrade remain.
Release chain:A23→A13→B06/B07/B08→A14→C11→A22→B09;[B08local upgrade Passed/reviewed30Oct](reports/b08-upgrade-20261030-independent-review.md),overallPartial.
C02 [Done27Oct](reports/c02-release-boundary.md):2.3.1beta;[B03 Done30Oct selectedWinStable156/CfT116 measurements/review](reports/b03-measurements-20261030-independent-review.md);[C03 Sync later](reports/c03-sync-owner-readiness.md),mandatory beforepublication;A13 held.
B02 [Partial2Nov](reports/b02-permission-runtime-20261102.md)/[review](reports/b02-runtime-20261102-independent-review.md):P0–P3 selected24/24Passed,8/7/7/6grants;2harness failures retained;6profiles/2workspaces cleaned,desktop returned;native/grant/clipboard/edge coverage and permission decision Pending.
B06:A08 [brief/review](reports/a08-independent-review.md) Done,candidate/desktop Pending; A14 còn public URLs/account gates.
A21 [audit/review](reports/a21-independent-review.md) Done;isolation Unverified,B gates unchanged;[execution handoff](reports/a-ready-execution-summary.md).
C06 chọn transition trước A11, actual phone runtime sau A11 review/freeze.
C10 readiness/quyền trước B10 khi ca cần; C10 final acceptance sau runtime evidence.
Later:A15/A16 QR→A17 spike/Autofill; A18 write; A19 Drive→A20 profiles/service.
Không coi mũi tên rút gọn là thay toàn bộ prerequisites trong detailed plan.

## Coordination task

<a id="task-a01"></a>**A01 — Done2026-10-26:** reconcile backlog/mobile/index/roadmap
với dated evidence, giữ15ID/story/acceptance và WIP. Selected Android Google
read/cache/offline/cold/403 Passed không thay desktop live/broader provider/401.
[Delivery/evidence](reports/backlog-reconciliation.md),
[static verification và independent review](reports/backlog-reconciliation-review.md).
Mobile/MVP/website/release vẫn Partial; dispatch hiện tại xem queue ở trên.

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
   không đổi thành Passed. Không replay accepted cases khi không có thay đổi liên quan. Lỗi nêu rõ bước/lệnh,error/exit/log/ảnh hưởng;thiếu detail ghi Unknown.
8. Khi scope mới lớn:create child plan dưới plans/date-feature với reports riêng,
   nối từ track/master. Không tự triển khai mọi Later, tạo lịch định kỳ hay gửi quảng bá.

## Unresolved

Remaining OWN01/C01,candidate/native/Sync proof,publisher/support/deployment,401 transition,
owner/desktop slots và extra device matrix. Không chặn các A Ready độc lập.
