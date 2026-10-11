# PassChef — active plans in execution order

Rechecked against code and later decisions on 2026-10-11. All eight previously
flagged plans retain relevant unfinished outcomes. None is wholly obsolete.
Order below ranks the **next remaining slice**, not every phase of a roadmap:
the MVP release phase still waits for product verification and launch preparation.

| Order | Plan | Status | Next relevant work / dependency |
|---|---|---|---|
| 1 | [Extension MVP](../docs/prd/v1-mvp.md) | Partial | Phase 4: failed-unlock limit/lockout and toolbar clipboard clearing. Phase 1: decide the unimplemented fallback KDF against current Argon2 behavior before any format/security change. Complete security verification before release. |
| 2 | [Recipe Builder Popup](260522-0832-recipe-builder-popup/plan.md) | Partial | Phase 3: invalidate queued/in-flight preview immediately on every change, including invalid form states; then phase 6 real Cookbook, profile and rapid-input verification. |
| 3 | [Verification Tag](260522-1314-recipe-verification-tag-sheet-binding/plan.md) | Partial | Phases 5–7/9: localized mismatch/warnings and real builder/content-flow tests, including two profiles. Depends on stable preview in order 2. |
| 4 | [Legacy Core integration](../docs/implementation_plan.md) | Partial verification | Reuse one real Google Sheets hotkey → Recipe → dish check for the Core and extension plans; do not rebuild the existing kitchen. Run after orders 1–3 fixes. |
| 5 | [Popup UX](260524-2127-popup-ux-redesign/plan.md) | Partial | Reconcile specs with current UI, finish demo Vietnamese/offline asset requirements and remaining UI/manual matrix. Reuse prior Recipe/mismatch checks instead of rerunning duplicate work. |
| 6 | [Mobile roadmap](../docs/mobile-app-brainstorm-qa.md) | Partial | Live Google connection → production onboarding/Cookbook screens and receiver/import → cache/mapping/copy integration. Autofill follows a working Cookbook; Drive and advanced authoring follow later. |
| 7 | [Landing improvements](../landing-page-improve.md) | Partial | Missing OG asset, actual product captures, responsive/performance verification and truthful social proof. Light mode remains an optional low-priority suggestion. Product screenshots follow stable extension UI. |
| 8 | [Commercialization](../docs/commercialization-plan.md) | Partial / Pending | Phase 0 account/domain/support/screenshots, then release and outreach. Depends on security/UI checks and launch assets; monetization/growth follow release evidence. |

## Superseded mechanisms; preserved outcomes

- Popup UX: restoring last-used picks and automatically expanding advanced
  options were replaced by random initial picks and collapsed options in
  `18b13eb`. Do not implement the older mechanism again.
- Legacy Core/MVP: former file locations/rotation suffix and the continuous
  DOM-observer approach are historical; current on-demand Cookbook detection
  still needs actual Sheets/hotkey verification.
- Native/iOS, Drive, full mobile screens and the residual Keystore device/error
  matrix remain follow-ups. Accepted foundation scope does not complete them.
- No unimplemented requirement is discarded merely because it is old. Missing
  verification is relevant unless a later accepted scope explicitly excludes it.

## Closed plans

Completed recovery/removal plans and original accepted mobile foundation plans
are under [archive/plans](../archive/plans/README.md), with their scope and proof.
Three prematurely archived extension plans above have returned to active `plans/`.
Per-phase audits in the linked documents supersede their historical progress boxes;
original requirements/observations remain available.

Templates and design visualizations are not additional unfinished feature plans.
This audit changes documentation and archive paths only; it starts no new feature.
