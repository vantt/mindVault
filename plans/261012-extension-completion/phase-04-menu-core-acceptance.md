# Phase04 — Current Menu/Core integration acceptance

Current acceptance2026-10-12: Done in the scope below.
[Combined evidence](reports/verification.md) records147 unit/25 distinct headed
E2E and8 actual Google/native cases, proof boundaries and independent review.
Original pre-repair observations and conditional verification paths remain below
as history; a checked criterion means its current applicable outcome was resolved,
not that every historical method/conditional branch was separately executed.


Status: Done. Priority:4. Legacy Core is now
`archive/plans/legacy-core-infrastructure/plan.md`; current native/provider
evidence closes its former gap. Integrates1–3.

## Existing harness limitation

`chrome-extension/test/e2e/google_sheet_integration.spec.ts` routes Google URL to
local mock HTML, attempts scripting injection without that permission, and can
replace a failed keyboard shortcut with a SW message. That is neither live Google
nor native-keyboard proof. Preserve golden tests; add explicit acceptance scenarios
rather than weakening expectations or silently relabeling old coverage.

## Implementation/QA TODOs

- [x] Add `test/e2e/menu-contract.spec.ts` with existing minimal DOM fixture,
  automatic manifest injection and observable content/popup state. Cover input,
  textarea/contenteditable/formula-bar fallback, whitespace/nonrecipe text,
  missing/unbound sheet, matched/mismatched tag and exact synthetic dish.
- [x] Programmatic command/message tests are labeled command-handler contract
  tests. Native shortcut test has NO message fallback; if shortcut is intercepted
  by desktop/Chrome, retain Pending/error with evidence rather than passing it.
- [x] Owner URL supplied: use only a dedicated dummy Cookbook with synthetic
  recipes and owner-authorized access. No reads of production sheets/account
  configuration; no login/credential automation or unapproved writes.
- [x] Actual Google QA: select ordinary recipe cell and formula-bar/edit contexts,
  open toolbar popup and compare dish to unchanged kitchen; exercise real
  Ctrl+Shift+L dispatch, no selected cell, mapping and wrong-tag refusal.
  Record browser/locale/URL purpose and pass/fail without key/spice screenshots/logs.
- [x] Conditional no-access path: Not applicable (owner URL/read/write proved).
  Without URL/access, keep live Google QA Pending; complete only local
  contract/UI proof. Do not close original plans with relevant real-Menu acceptance.
- [x] Remove raw recipe/dish/credential exception logging in touched paths; sanitize
  intentional diagnostics. Verify bounded log checks honestly, not universal absence.
- [x] Measure real WASM unlock/cook behavior and actual observed latency/memory
  if tools support it; report instrument/browser/config limits. Do not manufacture
  historical percentage or performance acceptance from a few fixture cases.

## Verification and closure

- [x] Existing unit suites plus new browser scenarios pass; no approved test edit.
- [x] Independent reviewer examines test oracle, provider separation and shortcut
  evidence; tester repeats impacted scenarios with real temporary extension profile.
- [x] Update legacy plan/current docs: completed infrastructure versus verified
  provider/manual acceptance. Actual failures trigger focused repair/retest.

Owner supplied the public synthetic Cookbook on2026-10-12. A fresh anonymous
Chromium143.0.7499.4 profile read it and saved Recipe `passchefProof#1` to A1;
CSV re-read confirms persistence. Genuine X11 XTest Ctrl+Shift+L dispatch reached
`chrome.commands` on the baseline, with no message fallback. Actual Google exposed
a relevant bug: its focused editor holds only newlines, so the pre-trim truthiness
check skipped the populated formula bar. This feasibility run was followed by the repaired current-source8/8 actual
provider/native proof retained in reports/live-google-native-proof.json.

Current-source live Cookbook/native-keyboard acceptance: Done. Timing is one
observed setup928ms; broad measurements remain the active MVP follow-up.
Chrome Web Store, cross-device Chrome Sync certification and publication excluded.
