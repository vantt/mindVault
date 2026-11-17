# 04 — Extension design language & Android alignment (Explore agent report)

## Key facts
- "Precision" extension look = agreed cross-platform baseline; Android theme already ported (M01–M04, source reviewed, phone acceptance pending). "Plain" = missing signature details, not missing tokens.
- Extension sheet selection: NO picker / Drive. Auto-detect active browser tab sheet (`popup/sheet-detector.js`, `GET_SHEET_ID_FROM_ACTIVE_TAB`); multiple sheet tabs -> tab-title picker; fallback paste URL/ID. Profiles bound to sheets via Sheet Assignments.
- Extension flow: Welcome/setup (options page: master pw, confirm, backed-up checkbox) -> Unlock (Argon2id ~0.8s) -> Home (How it works quick-start, sheet badge, amber notice, Build Recipe) -> Generated (password slab + inline Copy) -> Recipe Builder (hash charset, position `# $ @ % ^`, secret 1-5, More options) -> Options (Profiles, Settings; Edit secrets dialog; export/import wizards with step dots + QR).

## Tokens (tokens.css, docs/designs/README.md, docs/specs/cross-platform-design.md)
- Dark only. Ink: #0a0a0c, #0e0e10 canvas, #15151a surface, #1c1c22 raised, #232329 hairline, #2f2f36 divider; text #8a857a / #b4ad9f / #d8d2c3 / #f0ece2.
- Amber #e8a341 only tappable color (hover #f5b35a, pressed #b97d23, tint #3a2a14). Moss #84b577 ready, coral #e0746c error, honey #d4a548 warning. Profile colors #58a6ff #e3b341 #3fb950 #a371f7.
- Type: Fraunces 400 static titles (+italic emphasis), Geist body/dynamic labels, Geist Mono recipes/passwords/captions (UPPERCASE tracked). Android: display 28-32sp, body 14-16, min 12, password 20-24 mono.
- Radius 4 controls, 3 chips, 0 cards/dialogs. Spacing 4..56; gutters 20-24dp. Hairlines not shadows. Corner vignette tinted by status. Custom 1.3px line icons. Chef-keyhole logo (assets ready in android/, not installed; app still uses old blue icon).

## Signature components
Header (wordmark + status dot + cog + hairline), password slab with amber copy sweep 1.5s, mono chips, toggle groups, recipe/real-password preview, profile cards w/ 3px color strip, section panel (caption/title/sub), two-tone dialog titles, amber full-width primary w/ arrow, step dots. Tone calm, cooking-flavored, honest about risk, EN/VI.

## Android has (app/.../ui/)
palette, typography (Fraunces 400 fix), shapes, header, section box, 4-tone status, buttons >=48dp. Nav = Menu/Cook/Sources choice buttons + Lock (`cookbook/CookbookScreen.kt:30`).

## Android gaps
1 vignette, 2 header status dot/cog, 3 mono caption style, 4 chips, 5 slab sweep, 6 italic emphasis, 7 icon set, 8 profile strips, 9 quick-start panel, 10 primary arrow/pressed, 11 tab bar w/ amber underline, 12 brand assets, 13 step dots.

## Prior constraints (cross-platform-design.md, plans/261102.../phase-03-android.md)
- Native readability/targets/back behavior beat pixel copy. No light mode, no glow/glass, amber only tappable.
- Nav stays Menu/Cook/Sources + Lock; earlier plan said NO new QR/scanner/Autofill/Drive/new-profile screens. **This redesign must explicitly revisit that constraint (owner request) — decision needed.**
- Native biometric + Google consent. Fraunces only static titles. Never truncate user content.
- Security: FLAG_SECURE, no rememberSaveable for sensitive inputs, lock 5 min idle / 10 min total, reveal hides 10s, clipboard clear 30s, no secrets in previews/logs.
- Keep EN/VI + testTags; golden tests immutable; no new deps (for theme phase); kebab-case files <200 lines.
