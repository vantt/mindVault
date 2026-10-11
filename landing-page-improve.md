# Prompt: Tiếp tục cải tiến Landing Page cho PassChef

**Audit 2026-10-11 — Partial, còn phù hợp cho việc chuẩn bị phát hành.**
Các checkbox ban đầu bên dưới là danh sách ý tưởng; không tự động biến mọi ý
tưởng thành yêu cầu đã duyệt. Trạng thái hiện tại và việc còn cần làm ở bảng cuối.
Chưa có bằng chứng QA responsive, đo hiệu năng hay xác thực lời chứng thực.


## Project Context

**PassChef** là Chrome Extension biến Google Sheets thành Password Manager an toàn:

- **Split Knowledge Architecture**: Public formulas (vd: `gmail#1`) + Private secrets = Secure passwords
- **Zero-Knowledge**: Secrets không rời device, mã hóa với Argon2id + AES-256-GCM
- **Features**: Auto-generate passwords, clipboard auto-clear (30s), auto-lock (5 min idle/10 min total), cross-device sync via Chrome

## Original landing-page snapshot (current audit below)

- Đã tạo landing page với cyberpunk + glassmorphism style tại `landing-page/`.
- Code của extension tại `chrome-extension/` (tên `extension/` trong prompt cũ đã lỗi thời).

### Files

- `index.html` - 810 lines, 7 sections hoàn chỉnh
- `styles.css` - ~2000 lines CSS
- `script.js` - Animations, scroll effects, mobile menu
- `assets/favicon.svg` - SVG favicon

### Design Specs

- **Colors**: Primary #00FF41 (Matrix Green), Background #0A0E17, Accent #00FFFF, #7B61FF
- **Typography**: Space Grotesk (headings), DM Sans (body), Fira Code (code/mono)
- **Effects**: Glassmorphism cards, neon glow CTAs, grid patterns, smooth scroll animations

### Sections Implemented

1. **Hero** - Terminal demo "what hackers see vs your password"
2. **Problem/Solution** - "Recipe vs Cake" analogy
3. **Bento Grid Features** - 6 features (Zero-Knowledge, Instant Gen, Hotkey, Auto-Clear, Auto-Lock, Sync)
4. **Security** - Argon2id + AES-256-GCM với trust badges
5. **How It Works** - 3-step guide
6. **CTA** - Chrome Web Store install
7. **Footer** - Links, social

### Technical

- Pure HTML/CSS/JS (no frameworks)
- Mobile-first responsive (320px+)
- WCAG 2.1 AA accessible
- Reduced-motion support

## Task

Tiếp tục cải tiến landing page này theo phong cách hiện đại, high-tech, an toàn, đáng tin cậy và dễ dùng.

Những mục tiêu có thể cải tiến:

1. [ ] Cần thêm vài sections khác nhau (cùng mục đích, khác cách tiếp cận) để thử cách `giải thích giá trị cốt lõi` của phương pháp quản lý password này. Giá trị của nó chủ yếu xoay quay `sheet không lưu trọn vẹn password mà lưu recipe để tái tạo password`. vì vậy một người có thể tự tái tạo password dựa theo hướng dẫn tái tạo của recipe và các secrets của riêng mình. điều này phải thật dễ hiểu, sử dụng hình ảnh hoặc animation. phải làm nổi bật process này, extension chỉ là công cụ hỗ trợ.
2. [ ] Thêm animations/micro-interactions hấp dẫn hơn
3. [ ] Tạo real product screenshots/mockups
4. [ ] Thêm testimonials/social proof section
5. [ ] Tối ưu performance (lazy loading, etc.)
6. [ ] Thêm FAQ section
7. [ ] Cải thiện mobile experience
8. [ ] Thêm dark/light mode toggle
9. [ ] Tạo OG image cho social sharing
10. [ ] Khác (đề xuất)

## Instructions

1. Đọc files trong `landing-page/` để hiểu current state
2. Đọc file `docs/password-system-design.md` để hiểu về core-value và thiết kế gốc của project. Cần hiểu rõ `cơ chế nhiều thành phần` của recipe giúp dấu password đồng thời hướng dẫn người dùng tự tái tạo password (với secrect của riêng họ).
3. Đọc file `docs/prd.md` để hiểu về product-requirement của project
4. Sử dụng `ui-ux-pro-max` skill để gather design intelligence
5. Sử dụng `ai-multimodal` skill nếu cần generate images/assets
6. Implement improvements step by step
7. Test responsive trên multiple breakpoints

## Rà soát từng mục — 2026-10-11

| Mục | Trạng thái / bằng chứng trong repo | Việc còn phù hợp |
|---|---|---|
| 1. Giải thích giá trị Recipe | Implemented: `landing-page/index.html`:169/292/529/709 có giải thích Cookbook/Recipe/Spice, so sánh và ẩn dụ Chef; builder tương tác tại423, `script.js`:473–552 | Kiểm tra người dùng có hiểu cách tự nấu Dish; chưa có bằng chứng usability. Không cần thêm section chỉ vì checkbox cũ chưa tick. |
| 2. Animation/micro-interaction | Implemented: `script.js`:113–134/196/243, animation CSS và reduced-motion | QA khi kiểm tra trang; không có tính năng animation mới cụ thể đã duyệt. |
| 3. Screenshot/mockup sản phẩm | Partial: demo minh họa bằng HTML đã có; `landing-page/assets/` chỉ có favicon, chưa có ảnh chụp sản phẩm | 5 screenshot/promo phục vụ CWS theo [launch plan](docs/commercialization-plan.md); prompt ảnh chưa phải asset đã tạo. |
| 4. Testimonials/social proof | Section implemented: `index.html`:1157–1239, `styles.css`:2390 | Xác minh nguồn/quyền sử dụng các lời trích dẫn trước công bố; mã nguồn không chứng minh đây là phản hồi người dùng thật. |
| 5. Hiệu năng/lazy loading | Partial: lazy-loader `script.js`:338–359 và rAF đã có; index không có ảnh lazy để chạy nhánh đó | Đo hiệu năng trang/ảnh thực tế trước tối ưu tiếp; chưa có benchmark hay QA. |
| 6. FAQ | Implemented: `index.html`:1244 và accordion `script.js`:557–581 | Rà soát nội dung khi phát hành. FAQ hiện nói MIT/open source, khác quyết định Closed source trong launch plan; cần thống nhất trước công bố. |
| 7. Mobile experience | Partial: mobile menu `script.js`:49–82; CSS có breakpoint1024/768/640 | QA responsive nhiều viewport; không ghi Done QA chỉ từ CSS. |
| 8. Dark/light toggle | Optional, unapproved: trang hiện chỉ có dark theme; không thấy quyết định yêu cầu light mode | Không chặn hoàn thành/phát hành; chỉ thực hiện nếu được chọn thành yêu cầu riêng. |
| 9. OG image | Pending release asset: `index.html`:15 đang tham chiếu `assets/og-image.png`, file không tồn tại | Tạo asset thực hoặc điều chỉnh metadata trước chia sẻ/phát hành; link hỏng hiện tại vẫn liên quan. |
| 10. Khác (đề xuất) | Unscoped, optional | Không phải deliverable đã duyệt; không giữ plan mở chỉ vì mục này. |

## Thứ tự việc còn cần làm

1. Chuẩn bị asset CWS + OG, trỏ CTA tới listing thực sau publish, thống nhất
   lời hứa giấy phép và chỉ dùng lời chứng thực có nguồn xác minh.
2. QA responsive/keyboard/reduced-motion và đo hiệu năng trên trang thật.
3. Kiểm tra mức dễ hiểu của Recipe/Spice/Dish; chỉ thêm nội dung khi có thiếu hụt.

Giữ kế hoạch này Partial đến khi các việc phát hành/QA liên quan có bằng chứng.
Light mode và ý tưởng khác không phải điều kiện hoàn thành. Không có product test
hay browser QA mới được chạy trong lần kiểm kê tài liệu này.
