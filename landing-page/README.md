# mindVault Landing Page

Modern, high-tech landing page for the **mindVault** Chrome Extension - a secure password manager that transforms Google Sheets into a password vault.

## Tech Stack

- **HTML5** - Semantic, accessible markup
- **CSS3** - Custom properties, glassmorphism, grid, flexbox
- **Vanilla JS** - No frameworks, lightweight interactions

## Design System

### Colors
- Primary: `#00FF41` (Matrix Green)
- Secondary: `#00FFFF` (Cyan)
- Background: `#0A0E17` (Deep Dark)
- Accent: `#7B61FF` (Purple)

### Typography
- Headings: Space Grotesk
- Body: DM Sans
- Code: Fira Code

### Visual Style
- Cyberpunk + Glassmorphism
- Dark mode (OLED-optimized)
- Neon glows and grid patterns
- Smooth scroll animations

## Structure

```
landing-page/
  index.html      # Main HTML
  styles.css      # All styles (~1000 lines)
  script.js       # Interactions (~300 lines)
  assets/
    favicon.svg   # SVG favicon
```

## Features

1. **Hero Section** - Headline, CTA, terminal demo
2. **Problem/Solution** - Traditional vs mindVault comparison
3. **Recipe vs Cake** - Visual analogy explaining the concept
4. **Features Grid** - Bento-style feature showcase
5. **Security Section** - Argon2id + AES-256-GCM details
6. **How It Works** - 3-step guide
7. **CTA Section** - Final call to action
8. **Footer** - Links and social

## Browser Support

- Chrome 80+
- Firefox 75+
- Safari 13+
- Edge 80+

## Development

Simply open `index.html` in a browser. No build step required.

For local development with hot reload:
```bash
npx serve landing-page
```

## Performance

- No external JS libraries
- Minimal CSS (single file)
- SVG icons (inline)
- Lazy-loaded images
- Respects `prefers-reduced-motion`
