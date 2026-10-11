# Product Backlog

Maintained product records, updated2026-10-11. All15 original IDs and initial
acceptance criteria are preserved below; current state follows verified product
reports, rather than stale workflow statuses. Historical criteria/wording do not
override later accepted scope or prove an unexecuted device/API/UI path.

The five historically done rows included prototype work; a prototype is not a
production phone screen. Keystore completion is limited to owner-accepted Xiaomi
coverage. Its spice cache is encrypted at rest; owned plaintext exists briefly
in memory during an unlocked session, as the [storage spec](specs/mobile-keystore.md) explains.

| ID | Original story | Original acceptance criteria | Current state / evidence |
|---|---|---|---|
| p-de34a1ac | Nhóm 0: Chốt Q10-13 (write scope sheet, transport QR/Drive, chính sách rotate, nút 'Kết nối app điện thoại' trên extension) | User trả lời cả 4 câu hỏi mở; chặn Nhóm B và phần write-scope của Nhóm A/D | Decisions complete; [preserved choices](decisions/mobile-product-decisions.md). |
| p-2acbcf2e | Nhóm A: Prototype UI - màn onboarding chọn nhánh (đã dùng trên browser -> nhập QR \| chưa dùng -> setup mới) | Thêm màn vào docs/designs/passchef-vault-prototype.html, rẽ 2 nhánh rõ ràng, demo được trên browser | Prototype complete; production onboarding remains pending. |
| p-cc6e12ff | Nhóm A: Prototype UI - màn kết nối Google Sheet + confirm mapping cột | Thêm màn vào docs/designs/passchef-vault-prototype.html, mô phỏng chọn sheet + xác nhận mapping | Prototype complete; production Menu-connection screen remains pending. |
| p-c1480680 | Nhóm A: Prototype UI - màn recipe builder 'Thêm tài khoản' + nudge rotate | Thêm màn vào docs/designs/passchef-vault-prototype.html: form recipe, nấu thử, nudge đổi mật khẩu trang | Prototype complete; production add-account screen remains pending. |
| p-23394604 | Nhóm B: Extension - thêm thư viện QR + màn 'Kết nối app điện thoại' (tái dùng createFullAccessBundle) | Options page có nút tạo QR từ bundle export-import-adapter.js đã có; chặn bởi Nhóm 0 (Q11/Q13) | Extension sender complete; [QR spec](specs/qr-handoff.md). Phone reader excluded. |
| p-048c26bc | Nhóm B: Extension - xử lý bundle vượt dung lượng 1 QR (QR động/nhiều mảnh) | Bundle lớn vẫn truyền được qua QR an toàn, không mất dữ liệu; chặn bởi Nhóm 0 (Q11) | Extension chunking complete; [QR verification](../archive/plans/261011-qr-handoff-worktree-recovery/reports/verification.md). |
| p-7df5bb05 | Nhóm C: Google Cloud project + OAuth consent + xin scope Sheets cho mobile | Có OAuth client hoạt động; lead time dài (có thể cần Google verification review) nên khởi động sớm nhất | Client/runbook code complete; real Google Console setup/live read pending. [Connection spec](specs/mobile-sheets-client.md). |
| p-38074811 | Nhóm C: Client Sheets API đọc cả sheet trên mobile (KMP) | Đọc toàn bộ sheet 1 lần qua Sheets API, thay cho scrape DOM của extension | Read/append/update client complete; real Google read pending. [Connection spec](specs/mobile-sheets-client.md). |
| p-c2ad072e | Nhóm C (spike): Verify Argon2id trên KMP (Kotlin/Native + JVM) khớp output với argon2-browser WASM | Cùng params (t=3, mem=64MB, p=4) cho cùng key output với extension trên cùng input test vector | Android/JVM parity complete; Kotlin/Native/iOS deferred. [Core spec](specs/mobile-core.md). |
| p-6dee7ae8 | Nhóm C: Port RegexParserAdapter + tag-codec + generate_password sang KMP shared core | Password sinh ra từ KMP core khớp 100% với extension trên cùng recipe/secret (đối chiếu golden tests @approved) | Accepted Android/JVM Core complete; [Core spec](specs/mobile-core.md). |
| p-542801dc | Nhóm C: Giải mã full-access bundle (PBKDF2 100k + AES-GCM) trên mobile | Mobile nhập bundle JSON từ extension, giải mã đúng bằng sharing password, khớp createFullAccessBundle | Bundle decrypt complete; phone QR reader/import UI pending. [Core spec](specs/mobile-core.md). |
| p-8b12c072 | Nhóm C: Lưu secret an toàn trên Android (Keystore) + unlock biometric | Secret không rời Keystore ở dạng rõ; unlock bằng biometric mở được app | Complete within owner-accepted Xiaomi scope; [unexecuted matrix](../archive/plans/261011-keystore-recovery/reports/current-device-acceptance.md). |
| p-c46b217c | Nhóm D: Hiện thực 6 màn mobile thật (KMP/Compose, Android-first) theo prototype đã chốt | 6 màn (unlock, vault, cook, onboarding-branch, connect-sheet, add-account) chạy được trên Android thật, dựa 1:1 UX đã duyệt ở prototype | Pending production six-screen mobile app; Core/Keystore foundations complete. |
| p-fa39ea00 | Nhóm A/D: Màn sửa tài khoản đã tồn tại (edit-existing), không chỉ thêm mới | Mobile mở được 1 recipe đã có trong sheet, sửa các trường, ghi update (không append) vào đúng dòng cũ. Extension hiện chưa có luồng này (mới, không phải port). | Prototype/design marked done historically; production edit-existing workflow remains pending. |
| p-e117d9aa | Nhóm C: Drive appData transport cho onboarding handoff (bổ sung song song QR) | Extension nâng cấp Drive-sync (Phase 3) rồi mobile nhập bundle qua Drive appData, không cần QR | Pending; depends on extension Drive sync. QR sender is already complete. |

## Historical acceptance amendments

Retained verbatim from their original dated records; delivery counts/statuses
inside these amendments are historical, not the current verification result.

- p-23394604, 2026-07-26T16:42:48.528Z: Options page co nut tao QR tu bundle export-import-adapter.js da co
- p-048c26bc, 2026-07-26T16:42:48.603Z: Bundle lon van truyen duoc qua QR an toan, khong mat du lieu
- p-7df5bb05, 2026-07-28T11:27:05.203Z: Có OAuth client hoạt động; lead time dài (có thể cần Google verification review) nên khởi động sớm nhất — Delivered: runbook đầy đủ (docs/runbooks/google-oauth-sheets-setup.md) + code đọc Client ID/Secret từ config/env sẵn sàng (mobile/sheets-client), xem docs/history/passchef-mobile-oauth-sheets/. Remaining: user tự tạo OAuth client thật trên Console theo runbook + chạy smoke-test 1 lần thành công với credentials thật (việc ngoài code, theo dõi riêng).
- p-38074811, 2026-07-28T11:27:05.287Z: Đọc toàn bộ sheet 1 lần qua Sheets API, thay cho scrape DOM của extension — Delivered: Sheets API v4 client (đọc+ghi append/update) implement + 6 unit test xanh với response giả lập, xem docs/history/passchef-mobile-oauth-sheets/. Remaining: chạy thật 1 lần với sheet + credentials thật (phần user, theo dõi riêng per D4).

## Remaining work

- Real Google account setup and live Cookbook read.
- Production phone screens/edit-existing, QR reader/reassembly/import and Drive transport.
- [Accepted residual Keystore physical coverage](../archive/plans/261011-keystore-recovery/reports/current-device-acceptance.md).

See [reading map](specs/reading-map.md) for current specifications and [history](history/README.md) for retrospective plans/reports.
