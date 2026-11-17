# 06 — Palette study (Android redesign)

Visual: `../visuals/palette-study.html` (same 3 screens × 4 options, contrast tables, D light/dark toggle). Design only, no Kotlin.

## Trigger
Owner: layout good. Unsure the dark ink + amber palette feels calm, pleasant and SAFE enough. Remove the dot from the wordmark everywhere: now "PassChef", where "Chef" uses the accent-ink colour. Audience (corrected by owner): primary = TECHNICAL users (owner + peers already using the sheet method because it's simple, instant and hand-recomputable). Secondary = family/older relatives who only read the paper/sheet in an emergency.

## Research (brief)
- Trust colours: blue/teal = trust/security convention in finance; green = OK, red = loss/danger. Users expect category conventions. Evidence mostly practitioner writing, not controlled studies. [ABP 2025](https://abp.io/community/articles/color-psychology-in-web-design-how-to-choose-the-perfect-palette-in-2025-z383jph8), [Progress fintech colours](https://www.progress.com/blogs/how-choose-right-colors-fintech).
- Bitwarden: blue #175DDC + deep blue #0C3276, palette revised 2025–26 (secondary source) [madegooddesigns](https://madegooddesigns.com/?p=109387), [Bitwarden brand](https://bitwarden.com/en-gb/brand.md).
- 1Password refresh: blues + warm beige "Biscuit" neutral [1Password blog](https://1password.com/blog/1password-brand-refresh). Warm neutral + trust accent = same idea as B/D.
- Proton Pass: Proton purple → yellow-orange gradient, dark-first app [Proton](https://proton.me/blog/pass-logo-story).
- Google Password Manager: Material You dynamic colour, follows system theme (observed, no citation).
- VCB Digibank: "An Vui" mode for older customers, Oct 2024, larger text and clearer numbers [Lao Động](https://news.laodong.vn/kinh-doanh/vcb-digibank-cap-nhat-phien-ban-chu-to-so-ro-cho-khach-hang-lon-tuoi-1402056.ldo). Dark mode offered as an option [App Store](https://apps.apple.com/us/app/vcb-digibank/id561433133). Pattern: user picks the mode, it is not forced. Older users are now the secondary audience, so this matters less.
- Dark vs light for older users: Piepenbrock et al. found light mode better for acuity and proofreading at ages 18–33 and 60–85. Smaller gain for older adults. Cataract/low-vision users may prefer dark [NN/g](https://www.nngroup.com/articles/dark-mode/). Study is 2013, NN/g summary 2020; no 2024–26 study found that covers only older adults.
- WCAG 2.2 AA: text 4.5:1, large 3:1, UI components/focus 3:1 (1.4.11).

## Tokens (all hex; contrast = WCAG relative luminance)

| token | A Ink+Amber (dark) | B Paper+Teal (light) | C Navy+Teal (dark) | D Linen+Amber (light; dark=A) |
|---|---|---|---|---|
| canvas | #0e0e10 | #f5f0e6 | #0c1824 | #f8f5ef |
| surface | #15151a | #fbf8f1 | #122131 | #fffdf9 |
| raised | #1c1c22 | #ffffff | #18293b | #ffffff |
| hairline | #2f2f36 | #ddd3c1 | #25394e | #e3dccf |
| control border | #6a655d | #857e70 | #5f7891 | #8a8478 |
| text primary | #f0ece2 | #1e2422 | #ebf1f6 | #1d1b17 |
| text secondary | #b4ad9f | #454c48 | #b2c2d0 | #4c4840 |
| text muted | #8a857a | #625f57 | #8a9db0 | #68645b |
| accent (fill, tappable) | #e8a341 | #1d5c55 | #6cc5b8 | #e8a341 |
| on-accent | #0e0e10 | #ffffff | #062420 | #1d1b17 |
| accent-ink (text/icons) | = accent | = accent | = accent | #8a5610 (also 1px button edge) |
| accent-tint | #3a2a14 | #dfece7 | #133a3a | #fbedd2 |
| success | #84b577 | #2f6b3c | #83cc9b | #3c7535 |
| warning | #d4a548 | #8a5a00 | #e3b562 | #8a6100 |
| error | #e0746c | #b03a2e | #f08d83 | #b4372d |
| snackbar bg/fg | #1c1c22/#f0ece2 | #1e2422/#f5f0e6 | #1e3247/#ebf1f6 | #2a2722/#f6f1e7 |

## Contrast (ratio : 1)

| check | A | B | C | D light |
|---|---|---|---|---|
| text / canvas | 16.3 | 13.9 | 15.7 | 15.8 |
| secondary / canvas | 8.6 | 7.8 | 9.8 | 8.4 |
| muted, worst surface | 4.6 | 5.6 | 5.3 | 5.4 |
| accent text/icon / canvas | 9.0 | 6.8 | 8.8 | 5.6 (accent-ink) |
| button label / accent | 9.0 | 7.7 | 8.1 | 8.0 |
| accent fill vs canvas (UI 3:1) | 9.0 | 6.8 | 8.8 | **2.0 ✗ → 5.6 via #8a5610 edge** |
| control border (UI 3:1) | 3.15 | 3.54 | 3.56 | 3.66 |
| error / surface | 6.0 | 5.7 | 6.9 | 5.9 |

All pass AA except the amber fill on linen. D fixes it with a 1px #8a5610 edge on the button, and the dark label keeps the text at 8.0. Hairlines (1.3–1.5) are decorative only. Never use them as the only sign of a control's edge.

## Options
- **A Ink+Amber (current)**: focused, technical, premium. Pro: already built, matches extension. Con: reads as "terminal"/dev tool to non-tech users; dark-only goes against reading research; amber on black can feel like a warning. Extension: no change.
- **B Paper+Teal**: calm, orderly, "sheet in the drawer". Pro: calmest, bank-like, paper metaphor. Con: drops amber (brand breaks), teal close to success green, needs a dark set too. Extension: big change (new light theme + teal accent). The spec rules "dark-only" and "amber only tappable colour" are replaced. Logo colour question.
- **C Navy+Teal**: secure, vault-like, professional. Pro: instant "security" read, best contrast. Con: still dark-only, generic (VPN/antivirus look), drops amber. Extension: medium change (re-tint ink ramp navy, teal accent). Stays dark-only.
- **D Linen+Amber (hybrid)**: warm, reassuring, homely, still PassChef. Pro: brand continuity (amber, logo, type); light default follows system; dark mode = A unchanged. Con: two token sets to QA; two amber roles (fill vs ink) need a strict rule. Extension: small change. Can stay dark-only; dark tokens identical. Spec edit below. Add `accent-ink`.

## Recommendation
**D, re-weighted for technical users.** Android supports both themes and follows the system. Dark = A is the primary design target: technical users expect dark (dark IDE/terminal habit), it is identical to the extension, and it needs zero extension work. Light "Linen" is the secondary theme: it adds calm in daylight and serves relatives reading in an emergency.
Calm in dark comes from restraint, not a new hue (judgement, not tested): amber on one main action per screen, faint vignette, no copy-sweep glow, secondary text/hairlines quiet. Amber is only a fill on primary buttons and the logo. Text, icons, links, active nav and chip outlines use accent-ink #8a5610 on light (amber #e8a341 on dark). Snackbars use an inverse dark surface on light.
Runner-up **C** (Navy + Soft Teal, dark) if the owner feels amber itself is the unsafe part. It stays dark, as technical users expect, and reads as security, but the extension's ink ramp and accent must change. **B** drops to third: its strength (paper, older readers) serves the secondary audience. If chosen, define a B-dark set (not drawn).

Spec edits needed if D is chosen (`docs/specs/cross-platform-design.md`):
- Replace "dark only / no light mode" with: "Android: dark + light, follow system (dark = primary design target). Extension: dark (light optional later, same tokens)."
- Amber rule becomes: "amber = the only *action* colour. Light theme uses accent-ink #8a5610 for text/icon actions and a #8a5610 1px edge on amber fills."
- Darker light-theme status colours: moss → #3c7535, honey → #8a6100, coral → #b4372d.
- Wordmark: "PassChef", no dot, "Chef" in accent-ink. Update line 90 ("+amber dot") and any brand-kit SVGs that draw the dot.
- Remove the corner vignette on light, or keep it at ≤12% alpha.

## Unresolved questions
1. Owner pick: D (rec) / C / B / A? Is a light theme worth the extra QA now, or ship dark-only (A with restraint tweaks) first and add Linen later?
2. Should Android offer a manual Light/Dark/System setting, or only follow the system? Technical users may want to force dark or light independently of the system; relatives may have a phone set to dark without knowing.
3. Does Geist on Google Fonts fully cover Vietnamese diacritics? Rows "Ngân hàng", "Email gia đình" rendered fine in Chromium, but the Android bundled font must be checked. Fallback: Be Vietnam Pro.
4. Should the extension get a light theme later (same D tokens), or stay dark-only for good?
5. Brand-kit logo SVGs (commit d058311): check that none bakes in the dot. Amber logo on linen is fine (2.0:1 is acceptable for a decorative logo, but if the logo is also a button, give it a dark tile).
6. Do we need a larger-text "easy" mode, like VCB's An Vui? That is a separate decision from colour.
