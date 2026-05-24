---
title: "Popup UX Redesign — Post-Setup Flow"
description: "Redesign unlock→home→builder flow: contextual home states, builder smart defaults, demo page, i18n"
status: completed
priority: P1
effort: 10h
branch: main
tags: [popup, ux, i18n, chrome-extension]
created: 2026-05-24
beads_id: ""
---

# Popup UX Redesign — Post-Setup Flow

## Scope

Fix the post-unlock popup experience:
- **B1** Home screen contextual states + correct error routing
- **C** Builder redesign (micro-copy, collapsed advanced, smart defaults)
- **D** Back-from-Generated re-detection fix
- **A** Quick Start panel (first-use onboarding)
- **E** New demo page

## Phases

| # | Phase | Status | Effort | File |
|---|-------|--------|--------|------|
| 1 | Update UI specs | completed | 1h | [phase-01](./phase-01-update-ui-specs.md) |
| 2 | Home contextual states + error routing (B1) | completed | 1.5h | [phase-02](./phase-02-home-contextual-states.md) |
| 3 | Builder redesign (C) | completed | 2h | [phase-03-builder-redesign.md](./phase-03-builder-redesign.md) |
| 4 | Navigation fix (D) | completed | 0.5h | [phase-04-navigation-fix.md](./phase-04-navigation-fix.md) |
| 5 | Quick Start panel (A) | completed | 1h | [phase-05-quick-start-panel.md](./phase-05-quick-start-panel.md) |
| 6 | Demo page (E) | completed | 2h | [phase-06-demo-page.md](./phase-06-demo-page.md) |
| 7 | i18n — all new strings EN + VI | completed | 1h | [phase-07-i18n.md](./phase-07-i18n.md) |
| 8 | Testing + code review | completed | 1h | [phase-08-testing.md](./phase-08-testing.md) |

## Key Dependencies

- Phase 1 MUST complete before any code phase
- Phase 7 (i18n) runs in parallel with phases 2–6 but must consolidate before phase 8
- Phase 4 (navigation) is a prerequisite for phase 5 (Quick Start re-detection after dismiss)

## Key Files

```
chrome-extension/src/popup/
  popup.html
  popup.js          (~245 lines → will grow, watch size)
  popup-recipe-builder.js  (~397 lines → needs modularization at ~200 line sections)
  popup.css         (~485 lines)
  sheet-detector.js

chrome-extension/src/demo/
  demo.html         (NEW)
  demo.css          (NEW)

chrome-extension/src/manifest.json   (web_accessible_resources)
chrome-extension/src/_locales/en/messages.json
chrome-extension/src/_locales/vi/messages.json

docs/ui-specs/
  03-popup-home.md
  04-popup-generated.md
  05-recipe-builder.md
  15-i18n-keys.md
  16-demo-page.md   (NEW)
```
