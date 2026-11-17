# PassChef — master execution plan

Ngày:2026-10-22, Asia/Saigon. **Program Partial; planning system Ready**.
Baseline:`7dea4e5` historical;[checkpoint2Nov](reports/repository-checkpoint-20261102.md) records committed source/push. Entry:[plans index](../README.md).

## Mục tiêu và source of truth

Điều phối46task gốc/A-B-C; ưu tiên mới2026-11-02:[đồng bộ UI web/Android/extension](../261102-cross-platform-design-consistency/plan.md) trước deployment.
[Backlog](../../docs/backlog.md) sở hữu15 requirement IDs/acceptance gốc;
PRD/decisions giữ scope sản phẩm. Master sở hữu rollup/ưu tiên/cross-track gates;
track plan sở hữu task status/checklist/deliverables; report sở hữu kết quả/evidence.
[Task catalog](task-catalog.md) là dispatch view dẫn tới task trong từng track.
Không thay report/code acceptance bằng trạng thái planning Done.

## Cây plan → detail → evidence

| Track / priority | Detailed plan | Rollup / dependencies | Existing evidence |
|---|---|---|---|
|01 Extension validation/tooling|[Track01](phase-01-extension.md)|Partial; blocker inventory→fix→candidate; desktop/Sync cells cần lịch|[Historical completion](../../archive/plans/261012-extension-completion/reports/verification.md)|
|02 Mobile|[Track02](phase-02-mobile.md)→[Cookbook5phases](../261012-mobile-cookbook-phase1/plan.md)|Partial; Phase03/C09 Done finite acceptance, named G1/G2/L3/D1/D2 remain; QR/Autofill/write/Drive Later|[Independent finite reconciliation](../261012-mobile-cookbook-phase1/reports/finite-acceptance-reconciliation-20261109.md), [local151JVM](../261012-mobile-cookbook-phase1/reports/current-verification-20261109.md), [Google36 restoration](../261012-mobile-cookbook-phase1/reports/google-prepositioned-cancellation-20261109.md)|
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

Queue: original A queue has no Ready;A01–A12/A21 Done,A23 FA01 verified/UI01 approved deferral(overallPartial). [UI child](../261102-cross-platform-design-consistency/plan.md):D01/D02/W01–W03/M01–M04/E01–E04 source/preparation and V01 local consolidation reviewed. New83file artifact locally verified;fresh resources/runtime acceptance Pending. Actual browser/native/owner acceptance Pending. [B05 history](phase-03-website.md#task-b05) retains scoped old-candidate passes/failures;new styles need retakes. Deployment owner-held.
A03 [claims review](reports/a03-independent-review.md), A04 [legal/data review](reports/a04-independent-review.md):local prep only; DEMO01/approval/runtime gates Pending.
A02 [inventory](reports/a02-extension-blockers.md)/[independent review](reports/a02-independent-review.md):
UI01/FA01 [heldZIP full151unit+28E2E+WASM fault Passed/reviewed](reports/b07-run-01-independent-review.md);desktop/workspaces released;stable candidate/C01 observations/upgrade remain.
Release chain:A23→A13→B06/B07/B08→A14→C11→A22→B09;[B08local upgrade Passed/reviewed30Oct](reports/b08-upgrade-20261030-independent-review.md),overallPartial.
C02 [Done27Oct](reports/c02-release-boundary.md):2.3.1beta;[B03 Done30Oct selectedWinStable156/CfT116 measurements/review](reports/b03-measurements-20261030-independent-review.md);[C03 Sync later](reports/c03-sync-owner-readiness.md),mandatory beforepublication;A13 held.
B02 [Partial2Nov](reports/b02-permission-runtime-20261102.md)/[review](reports/b02-runtime-20261102-independent-review.md):P0–P3 selected24/24Passed,8/7/7/6grants;2harness failures retained;6profiles/2workspaces cleaned,desktop returned;native/grant/clipboard/edge coverage and permission decision Pending.
B06:A08 [brief/review](reports/a08-independent-review.md) Done,candidate/desktop Pending; A14 còn public URLs/account gates.
A21 [audit/review](reports/a21-independent-review.md) Done;isolation Unverified,B gates unchanged;[execution handoff](reports/a-ready-execution-summary.md).
A11/C06 selected4November401 campaign và usable-grant restoration accepted; C07/C08 residual acceptance và C09 finite completion theo [Track02](phase-02-mobile.md). Latest9Nov: [bilingual secure Cook](../261012-mobile-cookbook-phase1/reports/bilingual-secure-landscape-20261109.md) Passed11.949s/reviewed at200%landscape on32; [fresh release package audit](../261012-mobile-cookbook-phase1/reports/release-package-audit-20261109.md) Passed. [Separate VI source/mapping/detail](../261012-mobile-cookbook-phase1/reports/vietnamese-offline-landscape-20261109.md) Passed61.969s after header-action test repair, independently reviewed; original Failed10.003s retained. Owner/display restored; source205/original204/production-release32 exact, only testAPKe59af…12f changed. Earlier151JVM/three-APK32 snapshot unchanged; C07/C08/Mobile Partial; C09 finite scope Done.
9Nov reviewed: SDK32 local clear succeeded/core NotExercised126.745s/zero Google results; reconnect Passed18.564s. EN full offline200%landscape Run02 Passed57.549s; Run01 Unknown/setup-code15 failure retained.
[Provider33](../261012-mobile-cookbook-phase1/reports/google-provider33-runtime-20261109.md) Failed106.553s after Allow; [provider34](../261012-mobile-cookbook-phase1/reports/google-provider34-runtime-20261109.md) Failed316.896s despite ready-bound hand-Cancel: pending/resultCode unavailable/same task. Normal restores Passed18.313s/41.291s; final obligation false/owner inventory/source204/APK32 exact/native inactive;34 preserved=false/new empty directory retained separately from safe post-inactive rmdir recovery. Both bounded audits Passed.
C07/C08 remain Partial for G1/G2/L3; Phase03/C09 finite scope Done, D1/D2 delivery remain. Earlier [approved35](../261012-mobile-cookbook-phase1/reports/google-topology-diagnostic-20261109.md) core Failed138.641s/142.174s at120s marker wait; owner later confirmed deletion, exact gesture time unknown/no stale marker/extra revoke/core. Restore01 Failed182.301s/186.36s; unchanged restore02 Passed43.846s/48.462s with actual metadata/Test rows/mapping/cache, obligation=false/owner/source205/APKs exact/native inactive. Final closure audit Passed; original core-null/failures retained, no further revocation planned or historical cause/fix claim. Current [Google36](../261012-mobile-cookbook-phase1/reports/google-prepositioned-cancellation-20261109.md): new approval/owner-reported prepared page, actual revoke marker before120s, retained401/cache preservation and same Google/app task6841; owner clarified Allow, core Failed254.970s/257.829s before Cancel route/resultCode. Normal restore Passed19.830s/22.212s; obligation=false, nine names/one owner file/source205/APKs exact/native inactive. Independent audits Passed; no extra36 revoke/core planned.
C10 readiness/quyền trước B10 khi ca cần; C10 final acceptance sau runtime evidence.
Later:A15/A16 QR→A17 spike/Autofill; A18 write; A19 Drive→A20 profiles/service.
Không coi mũi tên rút gọn là thay toàn bộ prerequisites trong detailed plan.

## Coordination task

<a id="task-a01"></a>**A01 — Done2026-10-26:** reconcile backlog/mobile/index/roadmap
với dated evidence, giữ15ID/story/acceptance và WIP. Selected Android Google read/cache/offline/cold/403 Passed không thay desktop live/broader provider/401.
[Delivery/evidence](reports/backlog-reconciliation.md), [static verification và independent review](reports/backlog-reconciliation-review.md).
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

Remaining OWN01/C01,candidate/native/Sync proof,publisher/support/deployment,human Google/different-Menu/natural LMK,owner/desktop slots và extra device matrix. Không chặn các A Ready độc lập.
