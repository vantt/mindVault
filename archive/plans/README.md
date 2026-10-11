# Completed PassChef plans

Canonical archive after the 2026-10-11 code/status audit. Done refers to the
accepted scope. Platform grouping added 2026-10-12: the QR sender is extension
work; its future phone receiver belongs to Mobile. Remaining product/device work
is listed in the
[active plan order](../../plans/README.md), not silently declared passed.

| Platform | Archived plan | Closure / retained boundary |
|---|---|---|
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

The three extension plans once stored in `plans/archive/` retain relevant gaps
and have returned to active `plans/`. The historical report collection is now
in [reports](reports/); a review report is not an additional pending feature plan.
Original decisions/failures remain dated evidence. Obsolete workflow commands
describe previous tooling; they do not activate work.
