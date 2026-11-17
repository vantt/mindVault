# Phase 06 — Visual polish + brand assets

## Context links
- Spec §6 · [04 design language](reports/04-extension-design-language.md) · `docs/specs/cross-platform-design.md` · `docs/designs/passchef-brand-v2/`

## Overview
- Priority: P2. Status: Done (2026-11-10): light/dark, icons, launcher, dot-less in-app wordmark. Open: illustrations kept minimal, Fraunces weight, brand-kit SVG dot — [report 08](reports/08-implementation-verification.md). Depends 03–05 components.
- Make Android "less plain" with extension signature details; Palette D (D13): dark Ink+Amber + Linen light, follow system; amber = fill only (main action, logo), accent-ink #8a5610 for text/icons on light; calm via restraint (one amber action/screen, faint vignette, no copy-sweep glow). Wordmark "PassChef" without dot (D16) — needs revised brand-kit wordmark first.

## Key insights
Tokens already ported (dark only) → add Linen light set + System/Dark/Light setting. Missing = vignette, status dot, chips, slab sweep, icons, step dots, brand icon, illustrations.

## Requirements
- Functional: items 1–16 spec §6.
- Non-functional: reduced-motion respected; contrast pairs from contract; motion ≤300ms except 1.5s sweep; no new deps (custom ImageVectors).

## Architecture
`ui/` gains reusable pieces; screens consume. Launcher: copy brand kit drawables/mipmaps (lowercase_underscore names).

## Related code files
- Modify: `ui/passchef-theme.kt`, `ui/cookbook-components.kt`, `ui/passchef-palette.kt` (identity colors), `AndroidManifest.xml` (icon), `res/mipmap-*`, `res/drawable/`
- Create: `ui/status-corner-vignette.kt`, `ui/passchef-line-icons.kt`, `ui/mono-caption-text.kt`, `ui/filter-and-role-chips.kt`, `ui/dish-slab-copy-sweep.kt`, `ui/wizard-step-dots.kt`, `ui/onboarding-line-illustrations.kt`, `ui/clipboard-countdown-snackbar.kt`, `ui/skeleton-recipe-rows.kt`

## Implementation steps
1. Icons + captions + chips. 2. Header dot + vignette. 3. Slab sweep + countdown. 4. Step dots + transitions. 5. Illustrations. 6. Launcher icon swap (adaptive + monochrome). 7. Contrast + reduced-motion check.

## Todo
- [x] icons  - [x] captions/chips  - [x] header/vignette  - [x] slab  - [x] dots/motion  - [x] illustrations  - [x] launcher  - [x] contrast

## Success criteria
Side-by-side with extension reads as one family; owner judges "not plain"; contract contrast pairs hold.

## Risk assessment
Over-decoration on security screens → vignette static/low alpha; illustrations only in onboarding/empty states.

## Security considerations
FLAG_SECURE unchanged; animations never render Dish beyond reveal rules.

## Next steps
Phase 07 visual acceptance.
