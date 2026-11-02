# C01 — Owner blocker/UI decisions

Date:2026-10-26, Asia/Saigon. Recorded16:32 (09:32UTC).
Source:owner conversation; HEAD039c76d + preserved WIP.
Status:**Partial — UI01 disposition approved; broader owner observations Pending.**

## UI01 — temporarily defer unavailable Display defaults

Owner replied “ok , đồng ý, làm đi” after the recommendation to temporarily hide
the three disconnected Display defaults and the explanation of their current behavior.
Approved scope:

- Remove the unavailable Display Defaults card from rendered Options Settings.
- Remove its orphan initialization/read/write handlers and misleading default/
  inheritance promises from the active Settings description (English/Vietnamese).
- Preserve any existing `displaySettings` records; no preference migration/deletion.
- Keep current per-profile Spice hint, popup Dish visibility and Builder Menu
  detection behavior. No new reveal/inheritance behavior is authorized or implied.
- Verify/review the local patch and update the plan. Actual browser/candidate
  verification remains a separate B01/resource gate.

This is an honest UI deferral, not implementation of the three preferences.
[Local deferral delivered](a23-display-defaults-deferral.md) and
[independent review Passed](a23-display-defaults-independent-review.md):actual EN/VI
Settings initialization preserves saved preferences;151unit/49package checks Passed.
UI01's scope choice is resolved. Later [owner-authorized B01 Run01](b01-run-01.md)
and [independent review](b01-run-01-independent-review.md) verify selected EN→VI→EN
Settings/data preservation;broader C01 and final-candidate acceptance remain Pending.

## Remaining boundary

This reply does not approve an unknown owner bug list or close OWN01 MissingRepro,
the overall A23/release gate, C02 version/channel/support/Sync choices, desktop or
phone runtime, account changes, push/deployment/publication. Those limits persist.

## Unresolved

Other owner symptoms/repro/acceptance and final-candidate browser/upgrade/Sync evidence.

## 2026-10-27 followup

The boundary paragraph above records the scope of the original26October reply.
[C02 choices](c02-release-boundary.md) are now Done under a separate27October
approval;required runtime evidence remains Pending. Selected historical B01
browser evidence is linked above. Neither approval nor that selected run answers
the remaining OWN01 owner bug question;C01 stays Partial.

## 2026-10-27 proceed instruction

After root described candidate/full-suite/Windows/Sync next steps,owner said
“làm luôn đi”. Proceed with a provisional test candidate scoped to the known
FA01/UI01 dispositions;do not require a repeated no-other-bugs answer before
local verification. This instruction does not assert absence of other bugs.
C01 remains Partial for observations/final acceptance;publication/account/real
Sync actions retain their specific prerequisites. Bind any desktop/browser run
to its concrete reviewed cases,budget,synthetic data and owned cleanup record.

## Actual provisional verification followup

[HeldZIP full suite/fault](b07-zip-suite.md) and [native Windows Stable156 P0](b03-native-stable-run-01.md)
Passed with independent review. Owner proceed is sufficient for this bounded
testing;do not repeat the no-other-bugs question as its prerequisite. Broader C01
observations remain unconfirmed;[actual Sync slot is deferred by owner](c03-sync-owner-readiness.md).
