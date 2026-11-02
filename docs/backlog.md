# Product Backlog

Execution system: [Master plan](../plans/261022-project-work-queue/plan.md) → [detailed tracks/task dispatch](../plans/261022-project-work-queue/task-catalog.md).
This backlog retains requirement IDs; live task status and evidence belong to linked plans/reports.

Priorities and plan-level progress: [Active Plans](../plans/README.md).
This file owns requirement IDs and acceptance criteria; execution plans contain
checklists and evidence. [Plan-to-ID map](../plans/README.md#backlog-coverage-of-execution-plans).
Navigation/traceability updated 2026-10-16; current-state reconciliation 2026-10-26 (Asia/Saigon), A01.
Links to archived plans show historical scope, not newly completed acceptance.

Maintained product records, reconciled2026-10-26 against dated reports through2026-10-21. All15 original IDs and initial
acceptance criteria are preserved below; current state follows verified product
reports, rather than stale workflow statuses. Historical criteria/wording do not
override later accepted scope or prove an unexecuted device/API/UI path.

The five historically done rows included prototype work; a prototype is not a
production phone screen. Keystore completion is limited to owner-accepted Xiaomi
coverage. Its spice cache is encrypted at rest; owned plaintext exists briefly
in memory during an unlocked session, as the [storage spec](specs/mobile-keystore.md) explains.

| ID | Original story | Original acceptance criteria | Current state / evidence | Execution plan / scope |
|---|---|---|---|---|
| <a id="p-de34a1ac"></a>p-de34a1ac | Nhóm 0: Chốt Q10-13 (write scope sheet, transport QR/Drive, chính sách rotate, nút 'Kết nối app điện thoại' trên extension) | User trả lời cả 4 câu hỏi mở; chặn Nhóm B và phần write-scope của Nhóm A/D | Decisions complete; [preserved choices](decisions/mobile-product-decisions.md). | No execution plan assigned; [settled decisions](decisions/mobile-product-decisions.md). |
| <a id="p-2acbcf2e"></a>p-2acbcf2e | Nhóm A: Prototype UI - màn onboarding chọn nhánh (đã dùng trên browser -> nhập QR \| chưa dùng -> setup mới) | Thêm màn vào docs/designs/passchef-vault-prototype.html, rẽ 2 nhánh rõ ràng, demo được trên browser | Prototype complete; production onboarding remains pending. | Prototype/design record only; no dedicated production implementation plan assigned. |
| <a id="p-cc6e12ff"></a>p-cc6e12ff | Nhóm A: Prototype UI - màn kết nối Google Sheet + confirm mapping cột | Thêm màn vào docs/designs/passchef-vault-prototype.html, mô phỏng chọn sheet + xác nhận mapping | Prototype complete; Android Menu connection/confirmed mapping implemented, selected Google read/cache accepted2026-10-13; broader provider/phone acceptance Pending. [Google Run03](../plans/261012-mobile-cookbook-phase1/reports/google-runtime.md). | [Android Cookbook Phase1](../plans/261012-mobile-cookbook-phase1/plan.md), phases2/4; selected connection scope, not full mobile acceptance. |
| <a id="p-c1480680"></a>p-c1480680 | Nhóm A: Prototype UI - màn recipe builder 'Thêm tài khoản' + nudge rotate | Thêm màn vào docs/designs/passchef-vault-prototype.html: form recipe, nấu thử, nudge đổi mật khẩu trang | Prototype complete; production add-account screen remains pending. | Prototype/design record only; no account-authoring implementation plan assigned. |
| <a id="p-23394604"></a>p-23394604 | Nhóm B: Extension - thêm thư viện QR + màn 'Kết nối app điện thoại' (tái dùng createFullAccessBundle) | Options page có nút tạo QR từ bundle export-import-adapter.js đã có; chặn bởi Nhóm 0 (Q11/Q13) | Extension sender complete; [QR spec](specs/qr-handoff.md). Phone reader excluded. | [QR sender recovery — archived](../archive/plans/261011-qr-handoff-worktree-recovery/plan.md); sender only. |
| <a id="p-048c26bc"></a>p-048c26bc | Nhóm B: Extension - xử lý bundle vượt dung lượng 1 QR (QR động/nhiều mảnh) | Bundle lớn vẫn truyền được qua QR an toàn, không mất dữ liệu; chặn bởi Nhóm 0 (Q11) | Extension chunking complete; [QR verification](../archive/plans/261011-qr-handoff-worktree-recovery/reports/verification.md). | [QR sender recovery — archived](../archive/plans/261011-qr-handoff-worktree-recovery/plan.md); chunking only. |
| <a id="p-7df5bb05"></a>p-7df5bb05 | Nhóm C: Google Cloud project + OAuth consent + xin scope Sheets cho mobile | Có OAuth client hoạt động; lead time dài (có thể cần Google verification review) nên khởi động sớm nhất | Client/runbook code complete; owner confirmed Android client setup/consent. Selected Android live authorization/read/mapping/cache Passed2026-10-13 ([Google Run03](../plans/261012-mobile-cookbook-phase1/reports/google-runtime.md)); Run03 may reuse Run01 grant. Desktop live connection, broader provider/401/human cancellation and any required Google verification remain unaccepted. | [OAuth/Menu client recovery — archived](../archive/plans/261011-oauth-sheets-worktree-recovery/plan.md); code/runbook scope; [Android continuation](../plans/261012-mobile-cookbook-phase1/plan.md) owns selected live evidence. |
| <a id="p-38074811"></a>p-38074811 | Nhóm C: Client Sheets API đọc cả sheet trên mobile (KMP) | Đọc toàn bộ sheet 1 lần qua Sheets API, thay cho scrape DOM của extension | Read/append/update client complete; selected Android real read/mapping/cache Passed2026-10-13 ([Google Run03](../plans/261012-mobile-cookbook-phase1/reports/google-runtime.md)). Desktop live read/broader provider coverage Pending; append/update phone UI and live write acceptance deferred. | [OAuth/Menu client recovery — archived](../archive/plans/261011-oauth-sheets-worktree-recovery/plan.md); client scope plus [Android continuation](../plans/261012-mobile-cookbook-phase1/plan.md), not desktop live proof. |
| <a id="p-c2ad072e"></a>p-c2ad072e | Nhóm C (spike): Verify Argon2id trên KMP (Kotlin/Native + JVM) khớp output với argon2-browser WASM | Cùng params (t=3, mem=64MB, p=4) cho cùng key output với extension trên cùng input test vector | Android/JVM parity complete; Kotlin/Native/iOS deferred. [Core spec](specs/mobile-core.md). | [Mobile Core — archived](../archive/plans/passchef-mobile/plan.md); Android/JVM parity scope. |
| <a id="p-6dee7ae8"></a>p-6dee7ae8 | Nhóm C: Port RegexParserAdapter + tag-codec + generate_password sang KMP shared core | Password sinh ra từ KMP core khớp 100% với extension trên cùng recipe/secret (đối chiếu golden tests @approved) | Accepted Android/JVM Core complete; [Core spec](specs/mobile-core.md). | [Mobile Core — archived](../archive/plans/passchef-mobile/plan.md); cooking scope. |
| <a id="p-542801dc"></a>p-542801dc | Nhóm C: Giải mã full-access bundle (PBKDF2 100k + AES-GCM) trên mobile | Mobile nhập bundle JSON từ extension, giải mã đúng bằng sharing password, khớp createFullAccessBundle | Bundle decrypt complete; phone QR reader/import UI pending. [Core spec](specs/mobile-core.md). | [Mobile Core — archived](../archive/plans/passchef-mobile/plan.md); decrypt only, not phone QR reader/import UI. |
| <a id="p-8b12c072"></a>p-8b12c072 | Nhóm C: Lưu secret an toàn trên Android (Keystore) + unlock biometric | Secret không rời Keystore ở dạng rõ; unlock bằng biometric mở được app | Complete within owner-accepted Xiaomi scope; [unexecuted matrix](../archive/plans/261011-keystore-recovery/reports/current-device-acceptance.md). | [Keystore recovery — archived](../archive/plans/261011-keystore-recovery/plan.md); accepted Xiaomi scope. |
| <a id="p-c46b217c"></a>p-c46b217c | Nhóm D: Hiện thực 6 màn mobile thật (KMP/Compose, Android-first) theo prototype đã chốt | 6 màn (unlock, vault, cook, onboarding-branch, connect-sheet, add-account) chạy được trên Android thật, dựa 1:1 UX đã duyệt ở prototype | Partial production Android Cookbook: manual Cook, cache/mapping/lookup/reveal/copy and Google connection implemented; selected phone/Google acceptance Passed. [Phase1 evidence](../plans/261012-mobile-cookbook-phase1/reports/verification.md). Full six-screen scope, onboarding branch, authoring and broader acceptance remain Pending. | [Android Cookbook Phase1 — active](../plans/261012-mobile-cookbook-phase1/plan.md); partial production slice, not all six screens. |
| <a id="p-fa39ea00"></a>p-fa39ea00 | Nhóm A/D: Màn sửa tài khoản đã tồn tại (edit-existing), không chỉ thêm mới | Mobile mở được 1 recipe đã có trong sheet, sửa các trường, ghi update (không append) vào đúng dòng cũ. Extension hiện chưa có luồng này (mới, không phải port). | Prototype/design marked done historically; production edit-existing workflow remains pending. | No dedicated production edit-existing implementation plan assigned. |
| <a id="p-e117d9aa"></a>p-e117d9aa | Nhóm C: Drive appData transport cho onboarding handoff (bổ sung song song QR) | Extension nâng cấp Drive-sync (Phase 3) rồi mobile nhập bundle qua Drive appData, không cần QR | Pending; depends on extension Drive sync. QR sender is already complete. | No dedicated Drive transport implementation plan assigned. |

A01 reconciliation and scope matrix: [report](../plans/261022-project-work-queue/reports/backlog-reconciliation.md).
Selected cache UI/offline/cold/403 continuations remain scoped there; Mobile stays Partial.

## Historical acceptance amendments

Retained verbatim from their original dated records; delivery counts/statuses
inside these amendments are historical, not the current verification result.

- p-23394604, 2026-07-26T16:42:48.528Z: Options page co nut tao QR tu bundle export-import-adapter.js da co
- p-048c26bc, 2026-07-26T16:42:48.603Z: Bundle lon van truyen duoc qua QR an toan, khong mat du lieu
- p-7df5bb05, 2026-07-28T11:27:05.203Z: Có OAuth client hoạt động; lead time dài (có thể cần Google verification review) nên khởi động sớm nhất — Delivered: runbook đầy đủ (docs/runbooks/google-oauth-sheets-setup.md) + code đọc Client ID/Secret từ config/env sẵn sàng (mobile/sheets-client), xem docs/history/passchef-mobile-oauth-sheets/. Remaining: user tự tạo OAuth client thật trên Console theo runbook + chạy smoke-test 1 lần thành công với credentials thật (việc ngoài code, theo dõi riêng).
- p-38074811, 2026-07-28T11:27:05.287Z: Đọc toàn bộ sheet 1 lần qua Sheets API, thay cho scrape DOM của extension — Delivered: Sheets API v4 client (đọc+ghi append/update) implement + 6 unit test xanh với response giả lập, xem docs/history/passchef-mobile-oauth-sheets/. Remaining: chạy thật 1 lần với sheet + credentials thật (phần user, theo dõi riêng per D4).

## Remaining work

- Desktop live Google connection/read and broader Android provider/401/human-cancellation acceptance. Selected Android grant/read/mapping/cache already Passed; no cross-platform inference.
- Remaining production onboarding/authoring/edit-existing, QR reader/reassembly/import and Drive transport; broader lifecycle/TalkBack/layout acceptance.
- [Accepted residual Keystore physical coverage](../archive/plans/261011-keystore-recovery/reports/current-device-acceptance.md).

See [reading map](specs/reading-map.md) for current specifications and [history](history/README.md) for retrospective plans/reports.
