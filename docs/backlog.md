# Product Backlog

<!--
GENERATED FILE — do not hand-edit.
Rendered by `bee backlog render` from event-sourced PBI records in .bee/backlog.jsonl (backlog-unification D1/D3).
Regenerate: `bee backlog render --write`. Check freshness: `bee backlog render --check`.
Deterministic: byte-identical for the same backlog.jsonl contents — status-grouped, id-sorted entries, LF endings,
never a generation timestamp or any other wall-clock value.
-->

| ID | Story | CoS | Status | Feature |
|----|-------|-----|--------|---------|
| p-048c26bc | Nhóm B: Extension - xử lý bundle vượt dung lượng 1 QR (QR động/nhiều mảnh) | Bundle lon van truyen duoc qua QR an toan, khong mat du lieu | proposed | passchef-mobile |
| p-23394604 | Nhóm B: Extension - thêm thư viện QR + màn 'Kết nối app điện thoại' (tái dùng createFullAccessBundle) | Options page co nut tao QR tu bundle export-import-adapter.js da co | proposed | passchef-mobile |
| p-38074811 | Nhóm C: Client Sheets API đọc cả sheet trên mobile (KMP) | Đọc toàn bộ sheet 1 lần qua Sheets API, thay cho scrape DOM của extension | proposed | passchef-mobile |
| p-542801dc | Nhóm C: Giải mã full-access bundle (PBKDF2 100k + AES-GCM) trên mobile | Mobile nhập bundle JSON từ extension, giải mã đúng bằng sharing password, khớp createFullAccessBundle | proposed | passchef-mobile |
| p-6dee7ae8 | Nhóm C: Port RegexParserAdapter + tag-codec + generate_password sang KMP shared core | Password sinh ra từ KMP core khớp 100% với extension trên cùng recipe/secret (đối chiếu golden tests @approved) | proposed | passchef-mobile |
| p-7df5bb05 | Nhóm C: Google Cloud project + OAuth consent + xin scope Sheets cho mobile | Có OAuth client hoạt động; lead time dài (có thể cần Google verification review) nên khởi động sớm nhất | proposed | passchef-mobile |
| p-8b12c072 | Nhóm C: Lưu secret an toàn trên Android (Keystore) + unlock biometric | Secret không rời Keystore ở dạng rõ; unlock bằng biometric mở được app | proposed | passchef-mobile |
| p-c2ad072e | Nhóm C (spike): Verify Argon2id trên KMP (Kotlin/Native + JVM) khớp output với argon2-browser WASM | Cùng params (t=3, mem=64MB, p=4) cho cùng key output với extension trên cùng input test vector | proposed | passchef-mobile |
| p-c46b217c | Nhóm D: Hiện thực 6 màn mobile thật (KMP/Compose, Android-first) theo prototype đã chốt | 6 màn (unlock, vault, cook, onboarding-branch, connect-sheet, add-account) chạy được trên Android thật, dựa 1:1 UX đã duyệt ở prototype | proposed | passchef-mobile |
| p-e117d9aa | Nhóm C: Drive appData transport cho onboarding handoff (bổ sung song song QR) | Extension nâng cấp Drive-sync (Phase 3) rồi mobile nhập bundle qua Drive appData, không cần QR | proposed | passchef-mobile |

## Done / Declined

- [p-2acbcf2e] Nhóm A: Prototype UI - màn onboarding chọn nhánh (đã dùng trên browser -> nhập QR \| chưa dùng -> setup mới) — done
- [p-c1480680] Nhóm A: Prototype UI - màn recipe builder 'Thêm tài khoản' + nudge rotate — done
- [p-cc6e12ff] Nhóm A: Prototype UI - màn kết nối Google Sheet + confirm mapping cột — done
- [p-de34a1ac] Nhóm 0: Chốt Q10-13 (write scope sheet, transport QR/Drive, chính sách rotate, nút 'Kết nối app điện thoại' trên extension) — done
- [p-fa39ea00] Nhóm A/D: Màn sửa tài khoản đã tồn tại (edit-existing), không chỉ thêm mới — done
