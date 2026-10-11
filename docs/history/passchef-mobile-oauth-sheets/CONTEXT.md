# Track D — Bên ngoài: OAuth + Sheets API client — Context

**Feature slug:** passchef-mobile-oauth-sheets
**Date:** 2026-07-27
**Exploring session:** complete
**Scope:** Standard
**Domain types:** CALL, ORGANIZE

## Feature Boundary

Chuẩn bị OAuth client cho Google Cloud (p-7df5bb05) và scaffold một module Kotlin
Multiplatform (KMP) tối giản chứa Sheets API client đọc/ghi sheet (p-38074811),
thay cho scrape DOM của extension. Kết thúc ở: OAuth runbook + code đọc
credentials sẵn sàng, module KMP build/test được độc lập với Sheets API client —
chưa bao gồm UI app 6 màn thật (đó là p-c46b217c, PBI riêng).

## Locked Decisions

| ID | Decision | Rationale |
|----|----------|-----------|
| D1 | Phần Google Cloud project + OAuth consent screen làm bằng tay trên Console (agent viết runbook từng bước), consent screen ở **Testing mode** với user hiện tại thêm vào danh sách test users (không submit publish/verification ngay — verification là việc riêng, xem D4). Agent KHÔNG tự động hoá phần này bằng gcloud CLI — chỉ chuẩn bị sẵn code phía client đọc Client ID/Secret từ config/env (không hardcode), sẵn sàng cắm vào ngay khi user cung cấp. | Repo không có gcloud CLI cài/đăng nhập sẵn; tạo GCP project + OAuth consent là hành động của chủ tài khoản Google, không thể code thay. Testing mode cho phép smoke-test (D4 mục 3) chạy ngay mà không phải chờ Google duyệt app trước. |
| D2 | Sheets API client scaffold thành 1 module KMP/Gradle mới, tối giản, build + test độc lập được — chưa dựng UI/app đầy đủ. | Repo hiện chưa có project mobile/KMP nào (chỉ chrome-extension, landing-page); 6 màn thật (p-c46b217c) là PBI riêng, làm sau dựa trên module này. |
| D3 | Xin OAuth scope `https://www.googleapis.com/auth/spreadsheets` (đọc+ghi), không phải readonly. | Khớp quyết định đã chốt trước đó (Q10, docs/mobile-app-brainstorm-qa.md dòng 257-260): mobile được ghi sheet (append+update, không delete) là hành vi chuẩn đã duyệt; xin sẵn scope rộng tránh phải re-consent/re-verification với Google sau này. |
| D4 | Definition of Done của p-7df5bb05 tách 2 phần. **Phần code (cap được trong track này, không phụ thuộc người khác):** (1) runbook Console đầy đủ — mỗi bước đánh số, có tiêu chí xong rõ ràng (VD: "bước 3 xong khi OAuth consent screen hiển thị status Testing với ≥1 test user"), đã tự chạy thử lại (dry-run) 1 lần theo đúng thứ tự; (2) code đọc Client ID/Secret từ config/env đã sẵn (chưa commit secret thật) + unit test dùng fake/mock token chạy xanh. **Phần cần user (theo dõi riêng, KHÔNG chặn cap cell code ở trên):** (3) user tự tạo OAuth client thật trên Console theo runbook, cung cấp Client ID/Secret; (4) chạy script smoke-test 1 lần thành công (token exchange + 1 lời gọi Sheets API đọc) với credentials thật đó. Google app-verification review (nếu publish sau này) cũng là tiến trình ngoài riêng, không chặn (3)/(4). | Tách rõ để track không bị treo vô thời hạn chờ hành động của user (tạo OAuth client thời điểm không xác định) hay chờ Google review — cell code cap được ngay khi phần agent kiểm soát được đã xong. |

### Agent's Discretion

- Tên thư mục/module KMP cụ thể, cấu trúc Gradle, vị trí runbook trong `docs/` — quyết định ở planning.
- Cách smoke-test lấy được token (device flow / paste token thủ công) — quyết định ở planning, miễn thoả D4.

## Terms

| Term | Meaning in this feature |
|------|--------------------------|
| OAuth runbook | Tài liệu hướng dẫn từng bước thao tác tay trên Google Cloud Console (tạo project, cấu hình OAuth consent, tạo OAuth client, xin scope) mà agent viết cho user tự làm. |
| Sheets API client module | Module KMP mới, độc lập, chứa code gọi Google Sheets API (đọc + ghi trong giới hạn append/update) — chưa gắn UI app. |

## Specific Ideas And References

- p-38074811 phụ thuộc p-7df5bb05: code client có thể viết + unit test song song bằng fake/mock token, nhưng smoke-test thật (D4 mục 3) chỉ chạy được sau khi OAuth client hoạt động.

## Existing Code Context

### Reusable Assets

- `chrome-extension/test/e2e/google_sheet_integration.spec.ts` — test tích hợp Sheet hiện tại của extension (cách tiếp cận DOM-scraping đang được thay thế cho mobile).

### Established Patterns

- Không có pattern OAuth/Sheets API sẵn trong repo — đây là lần đầu tiên repo có code gọi API ngoài qua OAuth.

### Integration Points

- Không có module mobile nào tồn tại — module KMP mới sẽ là entry point đầu tiên, độc lập, chưa tích hợp vào đâu.

## Canonical References

- `docs/mobile-app-brainstorm-qa.md` (dòng 257-260, mục Q9) — quyết định write scope (append+update, không delete) và scope OAuth cần xin.
- `.bee/backlog.jsonl` — PBI p-7df5bb05, p-38074811 (feature gốc: passchef-mobile).

## Outstanding Questions

### Resolve Before Planning

(none — cả 4 gray area đã chốt ở trên)

### Deferred To Planning

- [ ] Tên/vị trí module KMP + toolchain Gradle/Kotlin version cụ thể — planning khảo sát.
- [ ] Cơ chế smoke-test lấy token thật (device flow OAuth vs paste token thủ công) — planning quyết định kỹ thuật.
- [ ] Nơi lưu Client ID/Secret lúc dev (file local gitignore vs biến môi trường) — planning chốt, đảm bảo không commit secret.

## Deferred Ideas

(none mới — scope giới hạn đúng 2 PBI của Track D)

## Handoff Note

CONTEXT.md là nguồn sự thật. D1-D4 đã khoá, planning trích dẫn nguyên văn, không diễn giải lại.
