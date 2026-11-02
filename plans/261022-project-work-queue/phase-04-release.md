# Phase04 — Release preparation, packet và publication gates

Ngày:2026-10-22, Asia/Saigon. Status: **Partial preparation; A03/A04/A08/A09 Done2026-10-26**.
Nối [master plan](plan.md), [46 task IDs](task-catalog.md) và
[extension candidate/acceptance](phase-01-extension.md).

## Phạm vi và giới hạn

Sở hữu9 ID: A03,A04,A08,A09,A14,B06,B09,C04,C11.
Chuẩn bị claims/legal/assets/guidance/publisher trước; final submission packet sau
stable candidate và artifact acceptance. Không tự publish/deploy/mua domain/trả fee,
gửi quảng bá hay dùng account cá nhân. Product patch thuộc A23; website implementation
A22 và C05 deployment route do phase website quản lý. Không mở lại accepted browser slices.

## Đầu vào có thật

- [Commercialization plan](../../docs/commercialization/plan.md), đặc biệt Phase0.1–0.7,
  product readiness gate và Phase1 publication; kế hoạch là authorization prep, không là release proof.
- [Listing draft](../../docs/commercialization/marketing/cws-listing-copy.md),
  [value statements](../../docs/commercialization/marketing/product-value-statements.md),
  [screenshot brief cũ](../../docs/commercialization/marketing/cws-screenshot-prompts.md).
- [Privacy](../../landing-page/privacy.html), [Terms](../../landing-page/terms.html),
  [permission/data working matrix](../../docs/commercialization/legal/permissions-review.md),
  [security contract](../../docs/specs/extension-security.md),
  [Tag rationale](../../docs/recipe-tag-design-rationale.md).
- [Publisher/support record](../../docs/commercialization/operations/publisher-and-support.md):
  passchef.com/Cloudflare đã chọn; domain ownership/DNS/HTTPS/service và publisher/contact
  chưa được chứng minh. Không hỏi lại lựa chọn đã chốt; xác minh phần còn thiếu.
- [MVP current status](../../docs/prd/v1-mvp.md#current-implementation-status--relevance--2026-10-12),
  [archived browser verification](../../archive/plans/261012-extension-completion/reports/verification.md),
  [landing audit](../../landing-page-improve.md),
  [auto-deploy workflow](../../.github/workflows/build_and_deploy.yml).

## Thứ tự và dependency gates

1. A03/A04/A08/A09 làm draft local độc lập; C04 gom publisher/support/account choices.
   Thiếu contact không chặn draft khác; placeholder ghi Pending, không invent address.
2. A05/B02 permission findings, C02 release boundary và C05 hosting/publication route
   cập nhật claims/legal. Final policy declarations chỉ freeze theo manifest cuối.
3. A13 tạo stable candidate sau blocker gate; B06 chụp thật từ candidate đó. B07/B08
   kiểm artifact cùng baseline. Không chụp UI giả để thay release screenshot.
4. A14 đối chiếu toàn packet; **A14 → C11**, không đòi published listing URL trước
   khi nộp. C11 cần approval cụ thể và account actions; upload khác published.
5. Sau publication có listing thật: **C11 → A22 website → B09**. A22 nằm phase
   website, không thuộc9 ID file này. B09 kiểm cả Store-installed artifact và deployed URLs.

## Task registry

Report có link là evidence đã tạo; các path còn để code là **planned**.
Report thực phải bind task/source/version/candidate/artifact,
decisions, checks/verdict/limitations và next task; không lưu account credentials.

| ID / nhóm | Status / dependencies hiện tại | Artifacts cụ thể và tiêu chí Done | Report / planned destination |
|---|---|---|---|
| <a id="task-a03"></a>A03 / A | Done2026-10-26; local wording only; final choices C02/C04 | Listing/value/README/landing claims corrected; Recipe/Tag/source checks, field27/102, HTML/script/static checks and independent review Passed. DEMO01 standalone illustration limit retained; no deployed/browser QA or submission approval. | [Delivery](reports/a03-product-claims.md), [review](reports/a03-independent-review.md) |
| <a id="task-a04"></a>A04 / A | Done2026-10-26; local draft only; C04/C05/A05/B02 final gates | Privacy/Terms và9-row permission/data/retention/sharing/hosting/Store draft matrix corrected; source/HTML/link preservation checks và independent review Passed. No legal certification/publication or runtime deletion proof. | [Delivery](reports/a04-legal-data-review.md), [review](reports/a04-independent-review.md) |
| <a id="task-a08"></a>A08 / A | Done2026-10-26; brief/inventory only; captures B06 Pending | Five actual-UI shot briefs/captions,official graphic specs,PNG/SVG/OG/promo metadata inventory verified and independently reviewed. No final captures/artwork/rights approval; icon-1024 actual680px and OG missing retained. | [Delivery](reports/a08-assets-brief.md), [review](reports/a08-independent-review.md) |
| <a id="task-a09"></a>A09 / A | Done2026-10-26; truthful docs; later FA01 repair verified separately | Original A09 reproduction found restore blocked;later [FA01 repair](reports/a23-full-access-import-repair.md) and [held-ZIP regressions](reports/b07-run-01-independent-review.md) Passed selected cases. Unique-name/mapping and one-profile backup limits retained;no broad restore/release acceptance. | [Delivery](reports/a09-chef-reviewer-guides.md), [review](reports/a09-independent-review.md) |
| <a id="task-a14"></a>A14 / A | Pending; A03/A04/A08/A09 Done; Blocked A13/B06/B07/B08/C04/C05 | Reviewable submission packet inventory: exact ZIP/version/commit/SHA, screenshots/captions, claims/manifest/permissions/legal HTTPS URLs/locale/reviewer guide/known limits nhất quán; required artifact acceptance satisfied hoặc explicit approved boundary. Packet ready cho C11; không cần listing URL đã published. | `reports/a14-submission-packet.md` |
| <a id="task-b06"></a>B06 / B | Pending;A08/C02 Done;Blocked C01/A13 + desktop slot/isolation | Five actual product captures từ stable candidate, synthetic Recipe/account fields, no private URL/identifier/Spices. Source/editable inventory, sizes/legibility/locale/captions/rights và candidate binding rõ; unsupported UI không xuất hiện. Browser cleanup và slot returned. | `reports/b06-release-captures.md` |
| <a id="task-b09"></a>B09 / B | Pending; Blocked C11/A22 + published listing/public URLs | Install đúng published Store artifact, smoke chosen flows/version; deployed landing/legal/support HTTPS/CTA trỏ listing thật; failures giữ mở và route về owning phase. Upload/submission/draft listing không thay published/install proof. | `reports/b09-published-smoke.md` |
| <a id="task-c04"></a>C04 / C | Pending; account/contact decisions có thể làm trước candidate | Publisher identity/owner account/contact verification/support/private security route/visibility/countries/locales decisions và account readiness record; owner tự sign-in/2FA/checkout. Domain/Cloudflare choices giữ nguyên, C05 verify deployment. Không credentials/chat hoặc tự trả fee. | `reports/c04-publisher-decisions.md` |
| <a id="task-c11"></a>C11 / C | Pending; Blocked A14 | Owner xem exact packet, duyệt submission/publication/deploy action cụ thể; account/fee/legal requirements hoàn tất bởi owner. Record submitted/review-needed/rejected/published riêng, listing URL chỉ khi thật. Sau publication bàn giao A22, không gửi campaign tự động. | `reports/c11-store-delivery.md` |

## Handoff artifacts và Done của phase

- A13/B07/B08 đến từ [Phase01 registry](phase-01-extension.md#task-a13): candidate
  và artifact acceptance phải đúng bytes. A08 draft không phụ thuộc candidate;
  B06 capture có phụ thuộc candidate và blocker gate.
- A03/A04/A09 có thể sửa wording local nhưng push landing-page có thể auto-deploy;
  C05 phải giải quyết route/quyền trước push. Không gộp draft approval với public deploy.
- C12 testimonial provenance do website/claims owner quản lý: không có chứng cứ thì
  A03 bỏ claim khỏi draft; không chặn toàn bộ preparation hoặc tự bịa quote.
- Final packet liên kết existing source records và reports thật đã tạo. Expected report
  missing giữ Pending; không thêm link giả hoặc gọi preparation là accepted artifact.
- Store sizes/policies/fees/support requirements là dữ liệu thay đổi; kiểm official
  sources lúc execute task tương ứng, ghi ngày/source. Số trong brief cũ chưa đủ.
- Code/UI/candidate đổi sau capture/acceptance: đánh giá affected images/tests/claims,
  cập nhật packet rồi mới xin approval C11; không dùng approval cũ cho artifact khác.
- Phase chỉ Done khi9 owned tasks có verdict/evidence đúng phạm vi hoặc N/A có căn cứ
  và owner boundary rõ; website A22 dependency hoàn tất để B09 có actual deployed proof.
  Broader MVP/Mobile Partial vẫn giữ nếu remaining outcomes chưa được nhận.

## Bước tiếp theo và unresolved

A03/A04/A08/A09 Done local preparation;known FA01/UI01 repairs have selected
verification at Track01. Continue remaining C01 observations/A13 candidate and
C04/C05/B06/B08/A14 prerequisites;do not replay completed repairs as next work.
Tiếp tục C04 bằng nhóm câu hỏi đã chuẩn bị, không gọi owner ngồi chờ draft/build.
Còn thiếu C01 blocker closure,C02-required support/Sync runtime evidence,C04 publisher/
contact, C05 deployment ownership/quyền, candidate B06/B07/B08 và approval C11.
DEMO01 standalone illustration cần website QA/repair; không có submission,
publication/deployment hoặc screenshot acceptance mới.
