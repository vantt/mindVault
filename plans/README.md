# PassChef — active plans in execution order

Reconciled against code and independent acceptance2026-10-12. The five prioritized
browser slices are complete, integrated and pushed to main. Evidence is retained in
the [completion dossier](../archive/plans/261012-extension-completion/plan.md). The full MVP remains
Partial because its release and broad verification scope extends beyond those slices.

| Order | Platform | Plan | Status | Next relevant work / dependency |
|---|---|---|---|---|
|1|Browser extension|[Extension MVP](../docs/prd/v1-mvp.md)|Partial; phases1–4 Done|Phase5: cross-device Chrome Sync/conflicts, broad platform/security/performance measurements. Phase6: release readiness/Chrome Web Store. Argon2-only fail-closed settled; vault PBKDF2 fallback Superseded.|
|2|Mobile (Android)|[Mobile roadmap](../docs/mobile-app-brainstorm-qa.md); [Phase1 execution plan](261012-mobile-cookbook-phase1/plan.md)|Partial; execution plan prepared|Offline Cook/cache/mapping/Compose code and JVM/APK/lint can proceed without phone; physical/Google acceptance Pending. Real OAuth connection → production onboarding/Cookbook screens and receiver/import → cache/mapping/copy integration. Autofill follows working Cookbook; Drive/advanced authoring later. Browser Google acceptance does not prove the mobile account connection.|
|3|Website|[Landing improvements](../landing-page-improve.md)|Partial|OG asset, actual product captures, responsive/performance verification, truthful social proof. Light mode optional. Extension UI now stable enough for captures.|
|4|Browser extension + Website / Release|[Commercialization](../docs/commercialization-plan.md)|Partial / Pending|Account/domain/support/screenshots, release and outreach. Depends on product verification and launch assets; growth follows release evidence.|

## Closed browser plans

| Former priority | Archived plan | Closure |
|---|---|---|
|2|[Recipe Builder](../archive/plans/260522-0832-recipe-builder-popup/plan.md)|All6 phases Done: async ownership repaired, literal modifier/position/profile QA and real Google parity passed.|
|3|[Verification Tag](../archive/plans/260522-1314-recipe-verification-tag-sheet-binding/plan.md)|All10 phases Done: matched/mismatched tag, warnings, EN/VI actual UI and compatibility evidence.|
|4|[Core infrastructure](../archive/plans/legacy-core-infrastructure/plan.md)|Done: current ports/adapters and genuine Google/native shortcut acceptance.|
|5|[Popup UX](../archive/plans/260524-2127-popup-ux-redesign/plan.md)|All8 phases Done: current specs, offline EN/VI Demo, real action-popover/Builder/content acceptance.|

[Combined verification](../archive/plans/261012-extension-completion/reports/verification.md):
147 unit,25 distinct headed E2E,8 actual Google/native cases. Golden tests unchanged.
Random initial picks/collapsed options supersede old stored-pick/auto-expand steps;
Precision styling and runtime locale selection supersede older design mechanisms.

## Preserved boundaries

Native/iOS, mobile Google account connection, full phone screens, QR receiver,
Drive and residual Keystore physical matrix remain follow-ups. Store publication
and a broad Sync/platform matrix were not certified by browser acceptance.
No unfinished outcome is discarded merely because its plan is old.

Completed recovery/foundation/tooling plans live under
[archive/plans](../archive/plans/README.md), with scope and proof retained.
Templates/design visualizations/review reports are not additional feature plans.
