# Phase01 — Extension: blocker, verification và candidate

Ngày:2026-10-22, Asia/Saigon. Status: **Pending execution**.
Điều phối qua [master plan](plan.md) và [46 task IDs](task-catalog.md).
Ready trong catalog chỉ có nghĩa đủ điều kiện bắt đầu; chưa có patch hay acceptance mới.

## Phạm vi và giới hạn

Sở hữu17 ID: A02,A05,A06,A07,A10,A13,A21,A23,B01,B02,B03,B04,B07,B08,C01,C02,C03.
Mục tiêu: làm rõ blocker còn thật, chuẩn bị tooling/protocol độc lập, rồi xác minh
đúng candidate. Không mở lại5 browser slices đã Done, thay Golden, hạ Argon2 hoặc
đổi encrypted Cookbook format. Release copy/account/publication thuộc [Phase04](phase-04-release.md).
Mobile, landing implementation và Drive-sync tương lai ngoài phạm vi file này.

## Đầu vào có thật

- [MVP current status](../../docs/prd/v1-mvp.md#current-implementation-status--relevance--2026-10-12): phases1–4 Done trong phạm vi; Sync/platform/performance/release Partial.
- [Completion plan](../../archive/plans/261012-extension-completion/plan.md),
  [verification](../../archive/plans/261012-extension-completion/reports/verification.md),
  [delivery](../../archive/plans/261012-extension-completion/reports/delivery.md):147 unit,
  25 distinct headed E2E và8 actual Google/native là historical evidence, không phải run mới.
- [Security contract](../../docs/specs/extension-security.md),
  [permissions working record](../../docs/commercialization/legal/permissions-review.md),
  [manifest](../../chrome-extension/src/manifest.json),
  [storage adapter](../../chrome-extension/src/adapters/infrastructure/chrome_storage_adapter.js).
- [Unit/E2E commands](../../chrome-extension/package.json),
  [headed fixture](../../chrome-extension/test/e2e/fixtures.ts),
  [actual toolbar helper](../../chrome-extension/test/e2e/actual-toolbar-popover.ts).
- [Copy-WASM script](../../chrome-extension/scripts/copy_wasm.js),
  [CI workflow](../../.github/workflows/build_and_deploy.yml),
  [packaging/release gates](../../docs/commercialization/plan.md).
- [Owner-reported product gate](../../docs/commercialization/plan.md): bugs/unfinished UI reported; not a diagnosed issue list.

## Thứ tự thực hiện

1. A02 gom evidence/repro đã có; C01 chỉ bổ sung điều chưa biết. A05/A06/A10/A21
   có thể tiến hành độc lập; không chờ owner cho phần audit/local preparation.
2. C02 chốt release/support/Sync boundary. A23 chỉ sửa issue đã định danh; A07 theo A06.
   B01/B02/B04 lấy đúng patch/candidate khi cần, không chạy lại suite để lấp thời gian.
3. A10 chuẩn bị cả Sync và upgrade; C03 chuẩn bị hai Chrome environments nếu real
   Sync nằm trong boundary. B03 cần support matrix C02 và measurement protocol trước.
4. A13 tạo stable local candidate sau A06/A07/A23 và C01/C02. **Không phụ thuộc
   B07/B08 trước khi candidate tồn tại**. Nếu A23 không có blocker, ghi N/A có căn cứ.
5. B07/B08 xác minh đúng candidate/ZIP; chuyển evidence sang A14 ở Phase04. B01
   full-suite evidence có thể dùng lại cho B07 nếu cùng bytes/runtime/scope; ZIP smoke vẫn riêng.

## Task registry

Mọi report dưới đây là **planned, chưa tồn tại**; đường dẫn code chỉ là destination,
không phải evidence hoặc hyperlink tới file chưa có. Mỗi report ghi task ID,
source/candidate/commit, command/runtime khi có, verdict, giới hạn và next task.

| ID / nhóm | Status / dependencies hiện tại | Artifacts cụ thể và tiêu chí Done | Report destination (planned) |
|---|---|---|---|
| <a id="task-a02"></a>A02 / A | Pending; Ready; C01 bổ sung observations | Blocker ledger từ repo: repro hoặc MissingRepro, expected/actual, version, affected flows, acceptance, source. Không coi owner gate đã mở hoặc bịa bug; chuyển issue xác định sang A23. | `reports/a02-extension-blockers.md` |
| <a id="task-a05"></a>A05 / A | Pending; Ready; kết luận browser qua B02 | Cập nhật permission call-site matrix cho tabs/activeTab; phương án giữ/bỏ, affected picker/Menu/shortcut/lock/clipboard, manifest candidate và test protocol. Audit không tự xóa quyền. | `reports/a05-permission-audit.md` |
| <a id="task-a06"></a>A06 / A | Pending; Ready | Repeatable local package/validate commands; missing mandatory JS/WASM fail; imports/resources/ZIP root inventory/notices kiểm tra được. Local fixtures kiểm chứng failure modes; interim ZIP chỉ development artifact. | `reports/a06-packaging-validation.md` |
| <a id="task-a07"></a>A07 / A | Pending; A06 | Reviewed CI patch: PassChef artifact naming, verification trước ZIP, frozen-lockfile/runtime/retention/trigger rõ. Workflow validation/local checks ghi thật; không gọi GitHub run Passed nếu chưa chạy. Website deploy route giữ riêng C05. | `reports/a07-ci-packaging.md` |
| <a id="task-a10"></a>A10 / A | Pending; Ready; execution C03/B08 | Protocol real2-device Sync và ZIP upgrade: ciphertext/salt/profiles/default/Menu mappings, offline/concurrent edits/conflict/rotation/recovery, expected preservation, safe fixture/evidence/cleanup. Phân biệt protocol/unit với real Sync. | `reports/a10-sync-upgrade-protocol.md` |
| <a id="task-a13"></a>A13 / A | Pending; Blocked C01/C02/A06/A07/A23 | Stable candidate record: version/commit, manifest tại ZIP root, SHA256/content inventory/dependency provenance, blocker ledger đóng hoặc N/A. Chuẩn bị input B06/B07/B08; chưa gọi release accepted. Không cần B07/B08 để bắt đầu lắp candidate. | `reports/a13-release-candidate.md` |
| <a id="task-a21"></a>A21 / A | Pending; Ready; audit/config local only | Isolation feasibility record cho display/VM/browser: focus/input/clipboard/shortcut/network boundary, ca nào an toàn tách desktop, prerequisites và fallback. Không launch GUI trong A; chỉ chuyển B task sau review evidence isolation tương ứng. | `reports/a21-browser-isolation.md` |
| <a id="task-a23"></a>A23 / A | Pending; Blocked issue qua A02/C01 | Mỗi issue một patch/acceptance: production change có evidence, meaningful unit/static checks, independent review, impacted B01/B04 khi cần. Ledger đóng bằng evidence; no blocker thì N/A có source, không patch tùy tiện. | `reports/a23-blocker-repairs.md` + issue-specific records |
| <a id="task-b01"></a>B01 / B | Pending; desktop slot + patch hoặc A13 | Selected headed regression theo patch; full suite cho final candidate. Report đúng source/runtime/test identities/results/cleanup; Golden unchanged; không gộp historical147/25 thành run mới. | `reports/b01-headed-regression.md` |
| <a id="task-b02"></a>B02 / B | Pending; Blocked A05 + manifest candidate | Actual picker/Menu/native shortcut/lock/clipboard trên đúng permission candidate. Kết luận giữ/bỏ được trace tới manifest và assertions; failure giữ nguyên, không suy minimum permission từ call-site audit. | `reports/b02-permission-runtime.md` |
| <a id="task-b03"></a>B03 / B | Pending; Blocked C02 + local protocol | Chrome/OS/hardware/Argon2 params/sample count/cold-warm timing/memory/startup measurements và fail-closed recorded. Support từng matrix cell; không hạ KDF, suy Chrome116 từ143 hoặc suy broad metrics từ một sample. | `reports/b03-browser-measurements.md` |
| <a id="task-b04"></a>B04 / B | Pending; relevant candidate + desktop slot | Selected real native shortcut/OS clipboard/session timer trên patch liên quan; synthetic data, focus/exclusive clipboard gates, actual deadline/cleanup/limits. Không message injection thay native command; không replay Passed ngoài affected scope. | `reports/b04-native-regression.md` |
| <a id="task-b07"></a>B07 / B | Pending; Blocked A13 + desktop slot | Full pnpm test đúng candidate; clean extracted ZIP load + required resource/WASM/offscreen/fail-closed smoke. Bind source/ZIP/hash/results/Golden preservation; reuse matching B01 evidence khi hợp lệ, không double-count. | `reports/b07-zip-suite.md` |
| <a id="task-b08"></a>B08 / B | Pending; Blocked A10/A13/C02; C03 nếu real Sync | ZIP upgrade giữ encrypted Cookbook/Spices/profiles/Menu mapping; selected security/Menu/native Cook/Tag/Reveal/Copy/session/sharing/QR/ENVI và chosen Sync cases. Per-case pass/fail/deferred; required fail giữ release gate đóng. | `reports/b08-artifact-acceptance.md` |
| <a id="task-c01"></a>C01 / C | Pending; A02 giảm phần hỏi | Owner xác nhận blocker/UI ledger đã gom; mỗi mục repro/acceptance hoặc MissingRepro rõ. Owner confirmation danh sách không thay regression hoặc tự đóng A23. | `reports/c01-owner-blocker-decisions.md` |
| <a id="task-c02"></a>C02 / C | Pending | Decision record version/channel/Chrome-OS/release boundary và real Sync phải pass hay disclosed deferral. Giữ PassChef/Closed source/A2/lifetime đã chốt; chuyển choice sang A13/B03/B08. | `reports/c02-release-boundary.md` |
| <a id="task-c03"></a>C03 / C | Pending; A10 + lịch owner/device | Owner chuẩn bị2 Chrome devices/accounts và sign-in/2FA/authorization cụ thể khi cần; synthetic Cookbook, preservation/cleanup agreed. Agent thực hiện protocol trong quyền được duyệt; login readiness không chứng minh Sync Passed. | `reports/c03-sync-owner-readiness.md` |

## Bàn giao, kiểm chứng và cập nhật

- A dùng pnpm; meaningful unit/static/package checks không chiếm desktop. Root giữ
  ADB/Gradle; file này không cấp runtime permission. New code kebab-case,<200lines.
- Hiện E2E `headless:false`; full `pnpm test` gồm GUI. B phải có slot riêng hoặc
  isolation đã được chứng minh, đặc biệt native focus/clipboard/timer.
- C có thể chỉ cần trả lời message; account credentials không đưa vào report/chat.
  Hai Chrome profiles local/mock storage không thay actual cross-device Sync.
- Sửa website/legal HTML liên quan phải theo C05: push landing-page có thể auto-deploy.
- Evidence sau task ghi report thật rồi cập nhật row/catalog/master với commit/link/verdict;
  giữ historical Failed/Unknown/Pending và archived Done. Candidate đổi bytes thì
  đánh giá lại affected evidence; không blanket rerun.
- A13/B07/B08 bàn giao A14 [Phase04](phase-04-release.md#task-a14); publication xong
  mới A22 website rồi B09. Không đảo dependencies để yêu cầu listing URL trước packet.

Unresolved: C01 blocker details; C02 boundary/support/Sync decision; C03 permitted
devices/accounts; desktop slot và candidate/runtime cho từng B case. Chưa task nào Done.
