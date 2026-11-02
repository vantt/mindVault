# A01 — Reconciliation backlog và mobile evidence

Ngày:2026-10-26, Asia/Saigon. Status: **Done — reconciliation tài liệu**.
Parent:[master A01](../plan.md#task-a01); [dispatch](../task-catalog.md).
Source:`039c76d17a2765bd753e09a7009e004dadfd6638` + WIP đầu phiên.
Không phải acceptance mới; Mobile/MVP/website/release vẫn Partial.

## Audit trước hành động

Đã đọc README/AGENTS, workflow, index/master/catalog, Track02, Cookbook Phase1,
roadmap/backlog và reports dưới đây. Planner độc lập xác nhận A01 đủ đầu vào.
WIP có sẵn: `docs/backlog.md`, `plans/README.md`, ba file commercialization
(README/plan/operations publisher-and-support), `.agents/`, `skills-lock.json`.
Giữ WIP; chỉ bổ sung snapshot/links trong backlog/index. Ba file commercialization
và các file untracked có sẵn không thuộc task, giữ byte-identical.
Snapshot đầu phiên dùng kiểm chứng delta: `/tmp/passchef-a01-before.pkl` (tạm thời,
không phải evidence runtime hoặc artifact bàn giao). Không stage/commit/push.
`docs/code-standards.md` được workflow nhắc tới nhưng không tồn tại; không cần tạo
cho thay đổi tài liệu này. Không đổi code/test, Golden, account, Menu hay Spices.

## Delta

- Backlog giữ15ID, original stories/acceptance và historical amendments; sửa current
  state của p-cc6e12ff/p-7df5bb05/p-38074811/p-c46b217c và remaining work.
- Menu connection đã có production slice và selected acceptance; full sáu màn
  vẫn Partial. Nối active Cookbook plan bên cạnh archived client recovery.
- Index phân biệt Android đã có evidence với desktop live chưa accepted; cập nhật
  coverage map. Roadmap thêm snapshot hiện tại, giữ nguyên continuation lịch sử.
- A01 thuộc master coordination; Track02 chỉ nối reconciliation, không đóng task
  mobile khác. Catalog/master/index ghi Done tài liệu, task tiếp A02.

## Dated evidence và giới hạn

| Report nguồn | Kết quả được kế thừa | Giới hạn giữ nguyên |
|---|---|---|
| [Google runtime](../../261012-mobile-cookbook-phase1/reports/google-runtime.md),2026-10-13 | Run03 Passed1/1,23.309s: provider metadata/rows, mapping, durable cache | Owner xác nhận consent ở Run01; Run03 có thể reuse grant. Run01/02 Failed; không chứng minh desktop live hoặc broader Console verification |
| [Cache UI](../../261012-mobile-cookbook-phase1/reports/google-cache-ui-runtime.md),2026-10-13 | Run02 Passed1/1,42.393s: cached Cook, lock/reopen, biometric same-process recreation | App-disconnected cache trong ca đó; Run01 Failed giữ nguyên |
| [Network-off](../../261012-mobile-cookbook-phase1/reports/network-off-runtime.md),2026-10-13 | Run04 Passed1/1,31.477s: actual offline Cook và NETWORK_FAILED preservation | Ba Failed trước vẫn giữ; không thay401/cancel |
| [Provider cold](../../261012-mobile-cookbook-phase1/reports/google-cold-runtime.md) | Cold02 Passed1/1,8.886s sau verified SIGKILL và biometric | Warm chỉ InterruptedByVerifiedSIGKILL; Cold01 Failed/cause unknown; không thay natural LMK/task restoration |
| [Provider-loaded403](../../261012-mobile-cookbook-phase1/reports/google-provider403-preparation.md#actual-provider-loaded-http403-run01--passed21902s),2026-10-21 | Run01 Passed1/1,21.902s: provider cache/jars preserved trước fresh cached Cook; stale Dish cleared | Không credit401/cancel; synthetic403 Run04 riêng, Failed01/02 và NotExercised03 giữ nguyên; enabled Copy không chứng minh clipboard write |
| [Cookbook Phase1](../../261012-mobile-cookbook-phase1/plan.md),updated2026-10-21 | Partial production slice; selected Google/phone scope | Onboarding branch/authoring/QR receiver/Drive và broader phone acceptance chưa Done |

Chỉ đối chiếu reports trong repo; không mở lại raw runtime/APK, không khôi phục
raw `/tmp` đã mất. Source reports và toàn bộ lịch sử verdict giữ byte-identical.
Không có fresh pnpm/Gradle/ADB/browser, biometric, Google consent hay deployment.
Không replay acceptance Passed; không cần compile cho delta chỉ Markdown.

## Verification và independent review

Kiểm chứng tĩnh:15ID/story/acceptance và historical amendments bất biến so với
snapshot đầu phiên; links/anchors trong tài liệu thay đổi; WIP ngoài scope nguyên
vẹn; whitespace delta. Kết quả chi tiết và reviewer độc lập:
[verification/review](backlog-reconciliation-review.md).

## Unresolved

Không blocker A01. Desktop live/broader provider,401 transition, human cancellation,
LMK/task restore/TalkBack/layout, onboarding/authoring/QR/Drive tiếp tục theo các
task riêng. Owner/desktop chưa cần cho A01; không launch runtime để chờ owner.
Task tiếp: A02 inventory bug/UI release-blocker, không suy ra release gate đã mở.
