# Track03 — Website và deployment

Ngày:2026-10-22. Status: **Partial website / Pending remaining tasks**.
Parent:[master plan](plan.md). Dispatch:[catalog](task-catalog.md).

## Context và inputs

[Landing plan/audit](../../landing-page-improve.md) giữ scope: OG asset, truthful
content, responsive/keyboard/reduced-motion/performance QA. Demo/FAQ/animation code
đã có; không thêm section/light mode chỉ vì checkbox lịch sử chưa tick.
[Publisher/hosting record](../../docs/commercialization/operations/publisher-and-support.md)
ghi owner chọn passchef.com + Cloudflare, chưa chứng minh ownership/deployment.
[Workflow hiện tại](../../.github/workflows/build_and_deploy.yml) deploy GitHub Pages
khi push landing-page paths lên main; phải giải quyết route trước publication.

Draft public claims/legal/assets thuộc
[release track A03/A04/A08](phase-04-release.md); track này nhận chúng vào website,
QA và nối URLs thực. Local edits không đồng nghĩa được quyền publish.

## Registry và deliverables

| Task | Status / depends | Các bước và tiêu chí hoàn tất |
|---|---|---|
| <a id="task-c05"></a>C05 — deployment readiness | Pending owner details | Xác nhận ownership domain, Cloudflare service và route mong muốn so với GitHub Pages; lập patch/deploy proposal cụ thể, URL/rollback và quyền deploy trước hành động. Xác minh HTTPS/landing/legal/support reachability sau deploy; không hỏi lại domain/nền tảng đã chọn. |
| <a id="task-c12"></a>C12 — social-proof provenance | Pending; A03/A08 draft vẫn làm được | Đối chiếu quote/adoption số liệu với nguồn/quyền dùng owner cung cấp. Có evidence thì retain; thiếu thì bỏ unsupported claim khỏi draft, không bịa testimonial hoặc giữ website đứng chờ. |
| <a id="task-b05"></a>B05 — website QA | Desktop slot; local after A03/A04/A08, final after C05/URLs | Serve local candidate, kiểm tra viewport/keyboard/reduced-motion/links/assets và đo performance có runtime/device/sample. Findings có repro và tiêu chí fix; retest affected cells. Public-site QA tách local QA, claim accessibility phải gắn evidence. |
| <a id="task-a22"></a>A22 — published CTA | Blocked C05 + C11 published listing | Nhận URL listing đã publish từ release track; thay generic CTA và canonical links, validate local→deploy theo quyền C05/C11→bàn giao B09 kiểm tra public artifact. Không fabricated listing ID hoặc link tới generic Store như acceptance. |

## Ordered steps và file boundaries

1. A03/A04/A08 tạo draft/asset theo contract; C12 không chặn bỏ unsupported claims.
2. C05 xác định deployment route và owner actions; mọi `landing-page/**` push cần
   cân nhắc workflow auto-deploy, không lẫn với commit docs-only.
3. Áp dụng nội dung/asset local, B05 QA; sửa theo findings, final QA sau stable URLs.
4. Release A14 gom public URLs đã verified; C11 submit/publish. A22 nối listing thật;
   [B09 release-installed/deployed smoke](phase-04-release.md#task-b09) ghi kết quả cuối.

Product files: `landing-page/index.html`, `privacy.html`, `terms.html`,
`styles.css`, `script.js`, `assets/`. Deployment config thay riêng, review trước apply.
Không dùng extension dependencies trong landing-page; giữ vanilla stack hiện có.

## Reports và exit criteria

Hiện có [landing audit](../../landing-page-improve.md) và
[operations decisions](../../docs/commercialization/operations/publisher-and-support.md);
chưa có responsive/performance/deployed acceptance mới trong lần planning này.
Khi thực hiện, tạo `reports/website-qa.md` cho viewport/keyboard/motion/performance
và `reports/deployment.md` cho candidate/route/URLs/HTTPS/rollback/owner approval.
Paths tương đối với track folder; chưa có report thì không gắn link như đã Passed.

Done khi content/asset/link có evidence, required local/deployed QA Passed và CTA
đến listing thật; các mục optional không giữ track mở. Plan tổng vẫn Partial theo
release và các track khác. Unresolved: deployment route/quyền, public URLs,
support config, testimonial provenance nếu muốn giữ, desktop QA slot.
