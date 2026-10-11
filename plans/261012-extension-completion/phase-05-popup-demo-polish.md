# Phase05 — Demo, i18n and current popup acceptance

Current acceptance2026-10-12: Done in the scope below.
[Combined evidence](reports/verification.md) records147 unit/25 distinct headed
E2E and8 actual Google/native cases, proof boundaries and independent review.
Original pre-repair observations and conditional verification paths remain below
as history; a checked criterion means its current applicable outcome was resolved,
not that every historical method/conditional branch was separately executed.


Status: Done. Priority:5. Existing UX plan remaining phases01/06/07/08.
Locale/demo work may parallel2–3; combined acceptance depends on1–4.

## Current scope and source

- `chrome-extension/src/demo/demo.html` has four explainer sections but mostly
  English body/control text; demo.js localizes only marked elements and closes tab.
- `src/styles/tokens.css` imports remote Google Fonts; self-contained/offline
  outcome remains relevant despite later Precision styling superseding dark design.
- Current demo is a STATIC explainer, not a new interactive cooking feature.
  Keep current Precision style, responsive layout and accepted random Builder defaults.
- Specs: `docs/ui-specs/{03-popup-home,04-popup-generated,05-recipe-builder,
  15-i18n-keys,16-demo-page}.md`; update to current source and actual outcomes.

## Implementation TODOs

- [x] Localize all relevant demo headings/body/examples/control labels in EN/VI,
  preserving semantic markup and text-safe substitution. Keep recipes/examples
  deterministic and synthetic; no secret-bearing demo persistence.
- [x] Remove demo dependency on external fonts/CDNs via system fallback or already
  licensed local assets. Prefer bounded demo styling; do not redesign unrelated
  extension screens or download unverified font assets.
- [x] Verify Quick Start and Builder How-it-works entry paths open bundled demo,
  programmatically opened close works, narrow/wide layouts remain usable.
  No web_accessible_resources/all_urls expansion is needed for internal getURL tab.
- [x] Correct misleading clipboard/session copy, missing translated feedback and
  obsolete spec behaviors; all status texts must match implemented security.
- [x] Do not restore old stored picks/auto-expand or broad onboarding proposals
  merely to satisfy superseded checklist mechanisms.

## Verification

- [x] New locale completeness checks and `test/e2e/demo-offline.spec.ts`: actual
  EN/VI demo, entry/close paths,400/1200px, external network blocked with relevant
  content still rendered. Browser-native extension resources stay available.
- [x] Actual popup scenarios cover setup-required/locked/unlocked/no-sheet/builder/
  generated/mismatch states and navigation after form/copy/lock failures.
  Tests assert visible outcome and safety; avoid new visual golden baselines.
- [x] Independent final review + affected unit/headed suites; preserve original
  approved tests and QR regressions. Report live Google/native shortcut separately.
- [x] Update original phase statuses with observed evidence.
- [ ] Delivery tracked in overview: root commits/pushes
  scoped work only. Whole-plan closure requires no remaining relevant outcome;
  current UI tests do not establish Store publication or full legacy MVP release.

Phase04 provider/shortcut evidence and combined acceptance are complete.
No unresolved design question in this bounded slice.
