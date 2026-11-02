# PassChef — active plans in execution order

## Master execution system —2026-10-22

**Start here:** [Master plan](261022-project-work-queue/plan.md) →
[detailed tracks](261022-project-work-queue/plan.md#cây-plan--detail--evidence) →
[task dispatch A/B/C](261022-project-work-queue/task-catalog.md) → track/child reports.
46 stable tasks:23 autonomous,10 desktop-slot,13 owner-action/decision.
Program remains Partial; planning integration is Ready.
A01–A10 Done2026-10-26: [reconciliation](261022-project-work-queue/reports/backlog-reconciliation.md),
[blocker review](261022-project-work-queue/reports/a02-independent-review.md),
[claims review](261022-project-work-queue/reports/a03-independent-review.md),
[legal/data review](261022-project-work-queue/reports/a04-independent-review.md),
[permission review](261022-project-work-queue/reports/a05-independent-review.md),
[packaging review](261022-project-work-queue/reports/a06-independent-review.md),
[CI review](261022-project-work-queue/reports/a07-independent-review.md),
[capture brief review](261022-project-work-queue/reports/a08-independent-review.md),
[guides/FA01 diagnosis review](261022-project-work-queue/reports/a09-independent-review.md).
A23-FA01 [locally verified/reviewed](261022-project-work-queue/reports/a23-full-access-import-independent-review.md);
UI01 [owner-approved temporary deferral reviewed](261022-project-work-queue/reports/a23-display-defaults-independent-review.md);
overall A23 Partial for remaining OWN01/C01 and final-candidate/full-suite acceptance.
[B01 Run01 selected3cases Passed/reviewed](261022-project-work-queue/reports/b01-run-01-independent-review.md),33.046s;
source/ZIP/harness unchanged,owned profiles cleaned and desktop released. B01 final-candidate/full-suite Pending.
[C02 owner choices Done2026-10-27](261022-project-work-queue/reports/c02-release-boundary.md):2.3.1 controlledbeta,
Win11x64ChromeStable+116,real2deviceSyncbeforepublication. [Local metadata/held artifact reviewed](261022-project-work-queue/reports/c02-version-231-independent-review.md);
[A13 held provenance reviewed](261022-project-work-queue/reports/a13-held-independent-review.md),NOTDone with stable gate C01/A23;
[B03 selected Windows measurements Done2026-10-30](261022-project-work-queue/reports/b03-measurements-20261030.md)/[independent runtime review Passed](261022-project-work-queue/reports/b03-measurements-20261030-independent-review.md);prior preparation snapshots retain their historical NotRun scope.
A10 [Sync/upgrade protocol reviewed](261022-project-work-queue/reports/a10-independent-review.md).
A12 [mobile case cards reviewed](261022-project-work-queue/reports/a12-independent-review.md);all runtime NotRun.
A21 [isolation audit reviewed](261022-project-work-queue/reports/a21-independent-review.md);isolation Unverified.
No remaining A Ready;[execution handoff/owner gates](261022-project-work-queue/reports/a-ready-execution-summary.md).

Master owns priorities/cross-track dependencies; detailed plans own task status and
criteria; reports own actual results/evidence; backlog owns requirement IDs.
Update report → detail → dispatch → master/index on each delivery.
[System audit](261022-project-work-queue/reports/program-audit.md) records coverage,
static verification and limits. Historical source acceptance below stays scoped.

## Product plans and retained evidence

Execution overview: priorities, plan-level progress and next dependencies.
Requirements, stable IDs and acceptance criteria live in the [Product Backlog](../docs/backlog.md).
Navigation/traceability updated 2026-10-16; the acceptance dates below are unchanged.

Reconciled against code and independent acceptance2026-10-12. The five prioritized
browser slices are complete, integrated and pushed to main. Evidence is retained in
the [completion dossier](../archive/plans/261012-extension-completion/plan.md). The full MVP remains
Partial because its release and broad verification scope extends beyond those slices.

| Order | Platform | Plan | Status | Next relevant work / dependency |
|---|---|---|---|---|
|1|Browser extension|[Extension MVP](../docs/prd/v1-mvp.md)|Partial; phases1–4 Done|Phase5: cross-device Chrome Sync/conflicts, broad platform/security/performance measurements. Phase6: release readiness/Chrome Web Store. Argon2-only fail-closed settled; vault PBKDF2 fallback Superseded.|
|2|Mobile (Android)|[Mobile roadmap](../docs/mobile-app-brainstorm-qa.md); [Phase1 execution plan](261012-mobile-cookbook-phase1/plan.md)|Partial; code/local verification Done|Offline Cook/cache/mapping/Compose and Android connection code implemented, independently tested/reviewed; selected Android Google grant/read/mapping/cache and scoped UI/offline/cold/403 acceptance Passed; broader phone/401/human cancellation Pending. [Dated evidence and limits](261022-project-work-queue/reports/backlog-reconciliation.md). Autofill follows working Cookbook; QR receiver/Drive/advanced authoring later. Android acceptance does not prove desktop live OAuth/Menu-client connection.|
|3|Website|[Landing improvements](../landing-page-improve.md)|Partial|OG asset, responsive/performance verification and truthful social proof. Light mode optional. Final product captures wait for owner-reported bugs to be fixed and UI to stabilize.|
|4|Browser extension + Website / Release|[Commercialization](../docs/commercialization/plan.md)|Partial / Pending|Listing/privacy drafts, publisher/support decisions, non-UI assets and packaging tooling can proceed now. Final screenshots/ZIP acceptance and publication wait for resolved release blockers and stable UI; growth follows release evidence.|

## Backlog coverage of execution plans

Backlog currently retains 15 original items, mainly mobile/QR; it is not a complete
inventory of every extension, website or release task. A plan link identifies scope,
not completion; use its reports for current evidence. Archived plans retain their
original proof boundary; the active Android Cookbook plan covers a partial slice.

| Execution plan | Related backlog IDs / boundary |
|---|---|
| [QR sender — archived](../archive/plans/261011-qr-handoff-worktree-recovery/plan.md) | [p-23394604](../docs/backlog.md#p-23394604), [p-048c26bc](../docs/backlog.md#p-048c26bc); extension sender/chunking only, no phone reader |
| [OAuth/Menu client — archived](../archive/plans/261011-oauth-sheets-worktree-recovery/plan.md) | [p-7df5bb05](../docs/backlog.md#p-7df5bb05), [p-38074811](../docs/backlog.md#p-38074811); client/runbook recovery, not broad live account acceptance |
| [Mobile Core — archived](../archive/plans/passchef-mobile/plan.md) | [p-c2ad072e](../docs/backlog.md#p-c2ad072e), [p-6dee7ae8](../docs/backlog.md#p-6dee7ae8), [p-542801dc](../docs/backlog.md#p-542801dc); Android/JVM cooking/parity/decrypt, no phone QR import UI |
| [Keystore recovery — archived](../archive/plans/261011-keystore-recovery/plan.md) | [p-8b12c072](../docs/backlog.md#p-8b12c072); accepted Xiaomi scope, residual physical matrix retained |
| [Android Cookbook Phase1 — active](261012-mobile-cookbook-phase1/plan.md) | [p-cc6e12ff](../docs/backlog.md#p-cc6e12ff), [p-7df5bb05](../docs/backlog.md#p-7df5bb05), [p-38074811](../docs/backlog.md#p-38074811), [p-c46b217c](../docs/backlog.md#p-c46b217c); partial production slice, not all six screens |

For items without an assigned execution plan, the backlog says so explicitly;
a decision or prototype does not establish a production implementation plan.

## Keeping progress consistent

- Backlog owns requirement IDs and acceptance criteria; execution plans own detailed checklists and evidence.
- When assigning a plan, link it from the backlog and list its related IDs here or in the plan.
- Update the overview when a plan's priority/status changes. Preserve dated evidence and distinguish Done/Partial/Failed/Pending; do not copy full test logs into both indexes.
- A01 reconciles current backlog/mobile snapshots as of2026-10-26; dated source reports and historical amendments retain their original verdicts.

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

Native/iOS, desktop live OAuth/Menu-client acceptance, broader Android Google/phone verification, QR receiver,
Drive and residual Keystore physical matrix remain follow-ups. Store publication
and a broad Sync/platform matrix were not certified by browser acceptance.
No unfinished outcome is discarded merely because its plan is old.

Completed recovery/foundation/tooling plans live under
[archive/plans](../archive/plans/README.md), with scope and proof retained.
Templates/design visualizations/review reports are not additional feature plans.

## Extension verification continuation —2026-10-27

[Held2.3.1 ZIP](261022-project-work-queue/reports/b07-zip-suite.md):151unit+28E2E and
same-ID missing/corrupt/restored WASM Passed/[review](261022-project-work-queue/reports/b07-run-01-independent-review.md).
[Windows Stable156 P0](261022-project-work-queue/reports/b03-native-stable-run-01.md)
Passed/[review](261022-project-work-queue/reports/b03-native-stable-independent-review.md);earlier3driver failures retained.
Desktop/owned workspaces released. A13 stable/B01/B03/B07 overall remain Partial/held
for remaining scope;Windows116/metrics/upgrade unaccepted. [Owner Sync later](261022-project-work-queue/reports/c03-sync-owner-readiness.md),mandatory beforepublication unchanged.

## Extension measurements/upgrade continuation —2026-10-30

[B03 Done in selected Windows cells](261022-project-work-queue/reports/b03-measurements-20261030.md)/[review](261022-project-work-queue/reports/b03-measurements-20261030-independent-review.md):actualStable156.0.8078.76/CfT116.0.5845.96,P0–P4/cold10/warm20/5fastRAM intervals each.
Cadence degradation/sparse originals/worker-label limits retained;descriptive,noSLO/truepeak/fullplatform claim.
[B08 selected local numeric2.3.0→2.3.1 upgrade Passed](261022-project-work-queue/reports/b08-artifact-acceptance.md)/[review](261022-project-work-queue/reports/b08-upgrade-20261030-independent-review.md);overallPartial,notStore/realSync.
Source74/tests46 unchanged,11profiles/portableworkspace cleaned,desktop returned. RealSync remains owner-scheduled later and mandatory before publication;A13/program/release gates retained.

## Continuation recovery and permission packet —2026-10-30

[Four failed Codex turns](261022-project-work-queue/reports/continuation-builder-error-20261030.md)
returned exact builder error/null details before any commands;internal cause Unknown.
Fresh pnpmvalidate/source andheldZIP checks exit0;do not call this a verified repo build failure.
[B02 preparation/native freeze Passed](261022-project-work-queue/reports/b02-preparation-independent-review.md):
296disposable files,permissions-only overlays,4configs/5nativehelpers,private Windows workspace ready.
Runtime remains NotRun;fresh15min desktop handoff Pending. No permission reduction adopted;Sync later/publication gates retained.

## Permission comparison continuation —2026-11-02

[B02 actual P0–P3 comparison](261022-project-work-queue/reports/b02-permission-runtime-20261102.md)/[independent review](261022-project-work-queue/reports/b02-runtime-20261102-independent-review.md):24/24selected cases Passed,actual8/7/7/6grants on nativeStable156.76/Node26.13;two earlier P0harness failures preserved.
Two separately approved15minute slots completed;6profiles/2ownedworkspaces removed,desktop returned. Earlier preparation/slot01NotRun snapshots remain historical. FullB02 Partial:physical/native grant+shortcut,clipboard/provider/edge/fullsupport coverage and minimum-permission choice remain open. No canonical permission/Golden/artifact change;Sync stays later/mandatory before publication.

## Repository checkpoint —2026-11-02

[Fresh verification and commit scope](261022-project-work-queue/reports/repository-checkpoint-20261102.md)/[independent review](261022-project-work-queue/reports/repository-checkpoint-20261102-independent-review.md):code `2281761` committed/pushed on `codex/checkpoint-extension-validation-20261102`;151unit/49package/source/ZIP/static checks Passed. Documentation/landing/current plans accompany the checkpoint on the same branch. Main/Pages/Store gates retained;local skills and ignored build/raw artifacts excluded. Historical held-source capture metadata stays immutable;no stable release accepted.
