# Prompt: Tiếp tục cải tiến Landing Page cho mindVault

## Project Context

**mindVault** là Chrome Extension biến Google Sheets thành Password Manager an toàn:

- **Split Knowledge Architecture**: Public formulas (vd: `gmail#1`) + Private secrets = Secure passwords
- **Zero-Knowledge**: Secrets không rời device, mã hóa với Argon2id + AES-256-GCM
- **Features**: Auto-generate passwords, clipboard auto-clear (30s), auto-lock (5 min idle/10 min total), cross-device sync via Chrome

## Current Landing Page Status

- Đã tạo landing page với cyberpunk + glassmorphism style tại `landing-page/`.
- Code của extension tại `extension/`.

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
