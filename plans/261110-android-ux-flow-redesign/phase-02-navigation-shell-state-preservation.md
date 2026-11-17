# Phase 02 — Navigation shell + state preservation

## Context links
- Spec §2, §3.8, §8, D2/D4/D5 · audit #3, #5, #8, #19 · `docs/specs/cross-platform-design.md` (nav fence)

## Overview
- Priority: P1. Status: Done (2026-11-10) — see [report 08](reports/08-implementation-verification.md)
- Replace chip/`if` switching with bottom-nav shell (Menu / Cook / Kitchen), per-tab route stack, `BackHandler`, and a non-secret UI-state holder that survives lock.

## Key insights
- Root cause of "feels broken": lock wipes place, not just secrets; system Back exits.
- No new dep needed: small route stack + `androidx.activity.compose.BackHandler`.

## Requirements
- Functional: 3 tabs; header w/ status dot + Lock-now; Back rules spec §2; restore tab/route/wizard step/draft after unlock; consent grace (if D2) ≤120s bound to ownership ticket.
- Non-functional: sensitive state never in saved/restored holder; cold start clears holder; files <200 lines, kebab-case.

## Architecture
`MainActivity` → gate (`vaultPhase`) → `AppShell` (header + content + `PassChefBottomNav`). `AppRouteState` (process-scope, non-secret: tab, stack per tab, wizardDraft, listQuery) owned outside composition. `onStop`: if pending own auth intent && grace → wipe Dish/clipboard ownership only; else lock (existing) and keep `AppRouteState`.

## Related code files
- Modify: `MainActivity.kt`, `cookbook/MainCookbookScreen.kt`, `cookbook/CookbookScreen.kt`, `cookbook/CookbookController.kt` (route-agnostic invalidation), `cookbook/AndroidSheetsAuthorization.kt` (expose pending ticket)
- Create: `navigation/app-route-state-holder.kt`, `navigation/app-shell-scaffold.kt`, `navigation/passchef-bottom-nav-bar.kt`, `navigation/back-navigation-rules.kt`
- Delete: chip nav block in `CookbookScreen.kt`

## Implementation steps
1. Define routes + `AppRouteState` (no secrets; unit test it).
2. Build shell + bottom nav (testTags `nav-*`, `lock-now`).
3. Wire `BackHandler` per spec §2.
4. Keep route state across lock; clear on cold start/process death.
5. (D2) grace window in `onStop`, ticket-bound, timers unaffected.
6. Map every controller `code` to on-screen text in current route (audit #4).

## Todo
- [x] routes+state  - [x] shell/nav  - [x] back rules  - [x] lock-survival  - [x] D2 grace  - [x] code→message map  - [x] compile + existing tests

## Success criteria
Back never exits from pushed screen; lock→unlock returns to same tab/route; Google consent round-trip lands on next step (with D2) or one-tap "Continue" (without); existing proofs pass.

## Risk assessment
Proofs that rely on old chip layout → update non-golden proofs only, report golden conflicts. Grace misuse → strict ticket + timeout + unit tests.

## Security considerations
Holder whitelist (spec §8) enforced by type; Dish/Spice/token never stored. FLAG_SECURE unchanged. Timers unchanged.

## Next steps
Unblocks 03, 04, 05.
