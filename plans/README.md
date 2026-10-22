# PassChef — active plans in execution order

## Master execution system —2026-10-22

**Start here:** [Master plan](261022-project-work-queue/plan.md) →
[detailed tracks](261022-project-work-queue/plan.md#cây-plan--detail--evidence) →
[task dispatch A/B/C](261022-project-work-queue/task-catalog.md) → track/child reports.
46 stable tasks:23 autonomous,10 desktop-slot,13 owner-action/decision.
Program remains Partial; planning integration is Ready.

Master owns priorities/cross-track dependencies; detailed plans own task status and
criteria; reports own actual results/evidence; backlog owns requirement IDs.
Update report → detail → dispatch → master/index on each delivery.
[System audit](261022-project-work-queue/reports/program-audit.md) records coverage,
static verification and limits. Historical source acceptance below stays scoped.

## Product plans and retained evidence

Reconciled against code and independent acceptance2026-10-12. The five prioritized
browser slices are complete, integrated and pushed to main. Evidence is retained in
the [completion dossier](../archive/plans/261012-extension-completion/plan.md). The full MVP remains
Partial because its release and broad verification scope extends beyond those slices.

| Order | Platform | Plan | Status | Next relevant work / dependency |
|---|---|---|---|---|
|1|Browser extension|[Extension MVP](../docs/prd/v1-mvp.md)|Partial; phases1–4 Done|Phase5: cross-device Chrome Sync/conflicts, broad platform/security/performance measurements. Phase6: release readiness/Chrome Web Store. Argon2-only fail-closed settled; vault PBKDF2 fallback Superseded.|
|2|Mobile (Android)|[Mobile roadmap](../docs/mobile-app-brainstorm-qa.md); [Phase1 execution plan](261012-mobile-cookbook-phase1/plan.md)|Partial; code/local verification Done|Offline Cook/cache/mapping/Compose and Android connection code implemented, independently tested/reviewed; physical/Google acceptance Pending. Autofill follows working Cookbook; QR receiver/Drive/advanced authoring later. Browser Google acceptance does not prove the mobile account connection.|
|3|Website|[Landing improvements](../landing-page-improve.md)|Partial|OG asset, responsive/performance verification and truthful social proof. Light mode optional. Final product captures wait for owner-reported bugs to be fixed and UI to stabilize.|
|4|Browser extension + Website / Release|[Commercialization](../docs/commercialization/plan.md)|Partial / Pending|Listing/privacy drafts, publisher/support decisions, non-UI assets and packaging tooling can proceed now. Final screenshots/ZIP acceptance and publication wait for resolved release blockers and stable UI; growth follows release evidence.|

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

Native/iOS, mobile Google account acceptance, new phone UI/device verification, QR receiver,
Drive and residual Keystore physical matrix remain follow-ups. Store publication
and a broad Sync/platform matrix were not certified by browser acceptance.
No unfinished outcome is discarded merely because its plan is old.

Completed recovery/foundation/tooling plans live under
[archive/plans](../archive/plans/README.md), with scope and proof retained.
Templates/design visualizations/review reports are not additional feature plans.
