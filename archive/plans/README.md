# Completed PassChef plans

Canonical archive after the 2026-10-11 code/status audit. Done refers to the
accepted scope. Platform grouping added 2026-10-12: the QR sender is extension
work; its future phone receiver belongs to Mobile. Remaining product/device work
is listed in the
[active plan order](../../plans/README.md), not silently declared passed.

| Platform | Archived plan | Closure / retained boundary |
|---|---|---|
|Browser extension|[Five-slice completion](261012-extension-completion/plan.md)|Done implementation/independent acceptance and main integration/push at0cba85b; detailed proof retained.|
|Browser extension|[Recipe Builder](260522-0832-recipe-builder-popup/plan.md)|All6 current phases Done: race repair, literal modifier/profile QA and real Google parity.|
|Browser extension|[Verification Tag](260522-1314-recipe-verification-tag-sheet-binding/plan.md)|All10 phases Done; actual EN/VI refusal/warnings, compatibility preserved.|
|Browser extension|[Popup UX](260524-2127-popup-ux-redesign/plan.md)|All8 current phases Done: specs, bundled offline EN/VI Demo and popup/Builder/content acceptance.|
|Browser extension|[Core infrastructure](legacy-core-infrastructure/plan.md)|Done current architecture and actual Google/native8/8 acceptance; historical proposal retained.|
| Mobile (Android/JVM) | [Mobile Core](passchef-mobile/plan.md) | Done Android/JVM kitchen and bundle decrypt/parity; Native/iOS and full phone UI deferred. Context and independent reports retained. |
| Mobile (JVM client) | [OAuth/Menu client](passchef-mobile-oauth-sheets/plan.md) | Done client/runbook code; real account setup/live Google check tracked separately per original accepted D4. |
| Mobile (Android) | [Android Keystore](passchef-mobile-android-keystore/plan.md) | Done owner-accepted Xiaomi/API36 scope; [unexecuted matrix](261011-keystore-recovery/reports/current-device-acceptance.md) remains explicit. |
| Mobile (Android/JVM) | [Core recovery](261011-mobile-core-recovery/plan.md) | Verification, main integration and original checkout closure Done. |
| Mobile (JVM client) | [OAuth recovery](261011-oauth-sheets-worktree-recovery/plan.md) | Code verification, integration and checkout closure Done. |
| Mobile (Android) | [Keystore recovery](261011-keystore-recovery/plan.md) | Accepted device verification and integration Done; three focused diagnostic/repair plans in reports are also closed. |
| Browser extension | [QR handoff sender](qr-handoff/plan.md) | Done extension QR/chunking; phone receiver and Drive are separate outcomes. |
| Browser extension | [QR recovery](261011-qr-handoff-worktree-recovery/plan.md) | Sender verification, integration and checkout closure Done. |
| Repository tooling | [Tooling removal](261011-remove-bee-tooling/plan.md) | Done and pushed at `b5be3b0`; retained product decisions/backlog and unrelated skills. |
| Repository documentation | [Plan audit](261011-plan-status-audit/plan.md) | Phase/relevance statuses, archive migration and active ordering reconciled; verification/review report retained. |

The three extension plans returned to active work during the2026-10-11 audit;
their relevant gaps are now closed by the2026-10-12 completion. Full MVP release,
Sync/platform measurements and mobile/website follow-ups remain active. The historical report collection is now
in [reports](reports/); a review report is not an additional pending feature plan.
Original decisions/failures remain dated evidence. Obsolete workflow commands
describe previous tooling; they do not activate work.
