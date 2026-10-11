---
title: "Popup UX Redesign — Post-Setup Flow"
description: "Redesign unlock→home→builder flow: contextual home states, builder smart defaults, demo page, i18n"
status: partial
priority: P1
effort: 10h
branch: main
tags: [popup, ux, i18n, chrome-extension]
created: 2026-05-24
beads_id: ""
---

# Popup UX Redesign — Post-Setup Flow

## Current relevance audit — 2026-10-11

Status **Partial** on main `b5be3b0`; original completed labels are reconciled below. Historical requirements/checklists remain evidence of intended scope, not proof of acceptance.

Relevant remaining phases: **01,06,07,08**. Stored-pick restoration/auto-expand were superseded by 18b13eb; Precision styling by 644da28; runtime locale loading by 25dd3ed. Full demo localization, self-contained operation, current spec accuracy and current UI acceptance remain relevant. Oversized source is maintenance debt, not a new UX feature.

Current unit evidence: [77 passing existing tests](../../archive/plans/261011-remove-bee-tooling/reports/verification.md). Historical E2E reports do not establish live Google or complete manual UI signoff. This plan remains open; no archive move authorized by this audit.

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
| 1 | Update UI specs | Partial | 1h | [phase-01](phase-01-update-ui-specs.md) |
| 2 | Home contextual states + error routing (B1) | Done | 1.5h | [phase-02](phase-02-home-contextual-states.md) |
| 3 | Builder redesign (C) | Done | 2h | [phase-03-builder-redesign.md](phase-03-builder-redesign.md) |
| 4 | Navigation fix (D) | Done | 0.5h | [phase-04-navigation-fix.md](phase-04-navigation-fix.md) |
| 5 | Quick Start panel (A) | Done | 1h | [phase-05-quick-start-panel.md](phase-05-quick-start-panel.md) |
| 6 | Demo page (E) | Partial | 2h | [phase-06-demo-page.md](phase-06-demo-page.md) |
| 7 | i18n — all new strings EN + VI | Partial | 1h | [phase-07-i18n.md](phase-07-i18n.md) |
| 8 | Testing + code review | Partial | 1h | [phase-08-testing.md](phase-08-testing.md) |

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
