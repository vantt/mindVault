# Phase 07 — Verification

## Context links
- `AGENTS.md` golden protocol · `docs/runbooks/android-cookbook-acceptance.md` · spec §7, §9 · phases 01–06

## Overview
- Priority: P1. Status: Paused by owner (2026-11-12). Done on device: automated + matrix V1–V13; saved-task, null-label, cross-app 4/4, screen-off 2/2, real Google Picker pick→map→save + refresh + filtered re-pick (owner). Open, owner-only: legacy Google* proofs triaged 2026-11-12 (proposal): run 3 (GoogleCookbook, GoogleColdProcess warm+cold, GoogleHumanCancellation), skip 8 as obsolete/duplicate under Picker + `drive.file`; awaiting owner approval + test sheet URL. Owner UX notes ("Written code" label, Refresh hard to find): fix options proposed, awaiting choice. See [report 08](reports/08-implementation-verification.md). Depends 01–06.
- Prove redesign keeps security + passes existing tests; physical-device UX acceptance.

## Key insights
Golden tests immutable. Compiled instrumentation ≠ executed acceptance. Synthetic boards ≠ device proof.

## Requirements
- Functional: unit + JVM + androidTest pass; all kept testTags present; new flows covered. Redesign adds: Picker matrix on real connect flow (pick/read/refresh/lost-access 404+403/cancel/filtered re-pick/reinstall, M11 multi-account) under `drive.file` only; existing-user scope migration (one filtered re-pick, cache kept); vault v2 migration from real v1 fixture (D17); 1–5 parts (D15); E1 tear animation + reduced motion + TalkBack live region (D18); light/dark both (D13).
- Non-functional: physical phone EN/VI, 200% font, landscape, TalkBack sample, reduced motion.

## Architecture
Test layers: route-state unit tests → controller tests (vaultPhase, grace) → Compose UI tests (new tags) → physical runbook.

## Related code files
- Modify: non-golden androidTest proofs only if layout moved (report each); `docs/runbooks/android-cookbook-acceptance.md`
- Create: tests for `app-route-state-holder`, back rules, wizard steps, row copy
- Never modify: files marked `@golden` / `@approved`

## Implementation steps
1. `./gradlew` build + lint + unit + JVM. 2. androidTest proofs. 3. Physical runs: first-run, unlock (bio/password/lockout), connect via Drive/share/paste/offline, consent cancel + return, 401/403/offline, row copy + 30s clear, Back everywhere, lock-survival of route, 5/10 min timers, FLAG_SECURE Recents. 4. Code review agent. 5. Docs update list (plan.md).

## Todo
- [x] build/lint/unit  - [ ] androidTest  - [ ] physical matrix  - [x] review  - [x] docs

## Success criteria
All tests green, no golden edits, physical matrix pass, owner sign-off on wireframe→device parity.

## Risk assessment
Golden test conflicts with new layout → STOP, report to owner (do not edit).

## Security considerations
Verify no Dish/Spice/token in logs, screenshots, snackbars, saved state; grace window ≤120s only for own ticket.

## Next steps
Changelog + roadmap update; extension parity follow-ups.
