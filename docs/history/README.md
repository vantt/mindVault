# Historical plans and reports

These documents preserve earlier product decisions and learnings. Completed
feature dossiers, including plans, contexts and reports, now live under
`archive/plans/` after the 2026-10-11 audit. Dated phase-status annotations describe
current acceptance; original decisions, requirements and observations remain
historical evidence rather than today's automation instructions.

Bee tooling was removed from this repository on 2026-10-11 at the owner's request.
Older files may mention former `.bee/` scripts/state, skill names, cell paths,
gate commands or machine-specific paths. Those references describe past work,
not available tooling or a current requirement. Do not resume their automation.

Current guidance:

- [Reading map](../specs/reading-map.md) and [system overview](../specs/system-overview.md).
- [Product backlog](../backlog.md) and [preserved mobile decisions](../decisions/mobile-product-decisions.md).
- [Worktree recovery](../worktree-recovery.md), current specifications and
  [accepted device coverage](../../archive/plans/261011-keystore-recovery/reports/current-device-acceptance.md).
- [Device proof runbook](../runbooks/android-keystore-device-proof.md) for real
  hardware evidence; historical build reports do not replace physical proof.

Original feature plans:

- [OAuth/Menu](../../archive/plans/passchef-mobile-oauth-sheets/plan.md).
- [Mobile Core](../../archive/plans/passchef-mobile/plan.md).
- [Android Keystore](../../archive/plans/passchef-mobile-android-keystore/plan.md).
- [QR sender](../../archive/plans/qr-handoff/plan.md).

The accepted Keystore scope is the tested Xiaomi/API36 scope; its residual
matrix is unexecuted, not a full physical pass. Live Google verification and
broader phone screens/reader remain tracked separately in current product docs.

Recorded historical `docs/history/<feature>/` paths refer to the relocated
same-name dossiers in `archive/plans/`. The [active plan order](../../plans/README.md)
tracks outcomes that still need work; archive placement never proves full mobile
UI, live Google or the residual hardware matrix passed.
