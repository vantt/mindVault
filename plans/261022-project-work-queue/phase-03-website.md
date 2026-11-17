# Track03 — Website và deployment

Ngày:2026-10-22; updated2026-11-02. Status: **Partial website; selected width/hover/ARIA/parallax/OG verified; broader/deployed QA Pending**.
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
| <a id="task-c05"></a>C05 — deployment readiness | Owner-held2026-11-02 until [cross-platform UI alignment](../261102-cross-platform-design-consistency/plan.md). Domain management owner-confirmed; [readiness packet](reports/c05-deployment-readiness-20261102.md)/[independent review](reports/c05-deployment-readiness-20261102-independent-review.md) preparationPassed. Service/project/account/HTTPS/permissions deferred; draft GitHub Pages gate unapplied; C05NOTDone | Xác nhận ownership domain, Cloudflare service và route mong muốn so với GitHub Pages; lập patch/deploy proposal cụ thể, URL/rollback và quyền deploy trước hành động. Xác minh HTTPS/landing/legal/support reachability sau deploy; không hỏi lại domain/nền tảng đã chọn. |
| <a id="task-c12"></a>C12 — social-proof provenance | Pending; A03/A08 draft vẫn làm được | Đối chiếu quote/adoption số liệu với nguồn/quyền dùng owner cung cấp. Có evidence thì retain; thiếu thì bỏ unsupported claim khỏi draft, không bịa testimonial hoặc giữ website đứng chờ. |
| <a id="task-b05"></a>B05 — website QA | Partial2Nov: [slot04 tooltip/AOS repair and original-page retest](reports/b05-pseudo-overflow-slot04-20261102.md)/[independent review](reports/b05-pseudo-overflow-slot04-20261102-independent-review.md). Scoped W01Passed21width samples390/768/1440(root/client375/753/1425),6settled Copy/Dish opacity1 and9real tooltip hovers. Prior diagnostic failures and slot02 scoped evidence retained. Desktop returned17:29:41Asia/Saigon;owned0/portsfree/2profilesremoved. Broader responsive/accessibility/motion/performance/link/font/deployed matrix and C05/URLs Pending [Local link/asset source audit](reports/b05-local-link-source-audit-20261102.md)69/69local occurrencesPassed; dynamic/HTTP/external/placeholders NotExercised. [New UI foundation](../261102-cross-platform-design-consistency/reports/website-foundation.md) changes fonts/styles;prior B05 runtime/source results retain old candidate scope and affected new-font retakes are Pending. | Serve local candidate, kiểm tra viewport/keyboard/reduced-motion/links/assets và đo performance có runtime/device/sample. Findings có repro và tiêu chí fix; retest affected cells. Public-site QA tách local QA, claim accessibility phải gắn evidence. |
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

[UI child W02 composition03 reviewed](../261102-cross-platform-design-consistency/reports/website-composition-independent-review.md):quiet Precision section/legal CSS,594rules/0warnings,source copy/DOM/JS and30geometry nodes preserved. Base buttons/social controls declare44px;actual layout/targets/font/ARIA/motion retakes Pending on changed candidate. W03 artwork next;B05/website Partial and deployment held.

[W03 brand assets reviewed](../261102-cross-platform-design-consistency/reports/website-brand-assets-independent-review.md):six original-mark/Precision wordmarks,local favicon/warm metadata and self-contained OG SVG/1200×630 PNG verified;actual OG artwork readable/unclipped. Web source complete;E01 comparison next. B05/website browser/layout/font/targets/ARIA/motion and deployed/owner acceptance remain Partial/Pending.
