---
title: "Popup UX Redesign — Post-Setup Flow"
description: "Redesign unlock→home→builder flow: contextual home states, builder smart defaults, demo page, i18n"
status: done
priority: P1
effort: 10h
branch: main
tags: [popup, ux, i18n, chrome-extension]
created: 2026-05-24
beads_id: ""
---

# Popup UX Redesign — Post-Setup Flow

## Current acceptance — 2026-10-12

All8 phases **Done** in their current relevant scope.
Independent147 unit +25 distinct headed E2E passed; actual Google/native8/8 passed.
[Verification and current A–J matrix](../../../plans/261012-extension-completion/reports/verification.md)
separate routed contract tests, actual popovers and real Google/native delivery.

Specs now match current IDs, random picks/collapsed options and Precision styling.
Demo is fully EN/VI, bundled/offline and responsive. Historical stored-pick,
auto-expand and dark-layout mechanisms are Superseded; original phase history remains.


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
| 1 | Update UI specs | Done | 1h | [phase-01](phase-01-update-ui-specs.md) |
| 2 | Home contextual states + error routing (B1) | Done | 1.5h | [phase-02](phase-02-home-contextual-states.md) |
| 3 | Builder redesign (C) | Done | 2h | [phase-03-builder-redesign.md](phase-03-builder-redesign.md) |
| 4 | Navigation fix (D) | Done | 0.5h | [phase-04-navigation-fix.md](phase-04-navigation-fix.md) |
| 5 | Quick Start panel (A) | Done | 1h | [phase-05-quick-start-panel.md](phase-05-quick-start-panel.md) |
| 6 | Demo page (E) | Done | 2h | [phase-06-demo-page.md](phase-06-demo-page.md) |
| 7 | i18n — all new strings EN + VI | Done | 1h | [phase-07-i18n.md](phase-07-i18n.md) |
| 8 | Testing + code review | Done | 1h | [phase-08-testing.md](phase-08-testing.md) |

## Key Dependencies

- Phase 1 MUST complete before any code phase
- Phase 7 (i18n) runs in parallel with phases 2–6 but must consolidate before phase 8
- Phase 4 (navigation) is a prerequisite for phase 5 (Quick Start re-detection after dismiss)

## Implementation entry points

- `chrome-extension/src/popup/`: focused Home, security, form/preview helpers.
- `chrome-extension/src/demo/`: static bundled EN/VI explainer.
- `chrome-extension/src/content/`: selected-locale feedback and on-demand Menu read.
- `docs/ui-specs/{03,04,05,15,16}-*.md`: current reverse-engineered specifications.
- No new web-accessible resources or broad hosts; internal Demo uses `runtime.getURL`.
