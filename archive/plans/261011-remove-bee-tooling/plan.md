# Remove retired Bee tooling from PassChef

**Status audit — 2026-10-11: Done.** Execution1–7 completed, independently
reviewed and committed/pushed as `b5be3b0`. Product ledgers migrated, tracked and
ignored runtime/hooks/mirrors removed, unrelated assets/history preserved.
[Verification](reports/verification.md):77 extension unit tests, record comparisons,
306 product/history files and23 unrelated assets checked. No remaining removal
outcome; eligible for whole-plan archive. Current product follow-ups stay in
[backlog](../../../docs/backlog.md), not in the retired tooling plan.

Date: 2026-10-11. User explicitly requested complete Bee removal.
Scope: current `/home/vantt/projects/passMan` checkout only; no Bee execution.
Outside `../passMan-recovery` archives and Git recovery refs remain preserved.

## Execution

1. [x] Preserve all10 product decision records and15 product backlog IDs/criteria
   in maintained Markdown before deleting runtime ledgers. Reconcile stale
   statuses with completed OAuth code/Core/QR/accepted Xiaomi Keystore evidence;
   live Google verification, phone reader/full UI/Drive remain pending.
2. [x] Remove `.bee/`, `.agents/skills/bee-*`, ignored `.claude/skills/bee-*`
   mirrors and `.claude/agents/bee-{extract,gather,review}.md`.
   Remove `.agents/skills/.bee-render.json` and `.bee-skills-version.json`.
3. [x] Remove Bee-only `.codex/hooks.json` and `.codex/hooks.json.bak`.
   Filter only11 Bee commands from ignored `.claude/settings.json`; preserve
   its5 unrelated hooks/statusLine/includeCoAuthoredBy and non-Bee backups.
   Preserve `.claude/settings.local.json` and unrelated Claude tooling.
4. [x] Remove the managed Bee block from AGENTS and Bee introduction in CLAUDE;
   preserve golden-test/project rules and a generic AGENTS import if retained.
   Remove Bee-managed ignore entries and leftover active installer metadata.
   Preserve unrelated `skills-lock.json` and JuliusBrussee caveman/cavecrew WIP.
5. [x] Convert backlog to ordinary maintained product records; update active
   reading-map/system overview and remove Bee-only guard instructions from
   critical patterns while retaining Kotlin/Gradle lessons.
   Keep original product plans/reports unchanged. Add a historical directory
   note: former runtime links/commands are retrospective, not runnable guidance.
6. [x] Verify no active Bee hook, skill mirror, instruction or command dependency
   survives, including ignored Claude files. Historical text references may
   remain only as labeled evidence. Do not delete product code or golden tests.
7. [x] Independently review the scoped diff; commit/push only this removal and
   migrated docs. Preserve all unrelated current work exactly.

## Verification

- Inventory tracked and ignored paths; validate retained configuration JSON.
  Compare non-Bee Claude hook/config fields before/after; no blanket config wipe.
- Verify10 decisions retain ID/date/scope/choice/rationale/alternatives;
  all15 backlog IDs retain original criteria and honest current status.
- Check local links in active docs and historical retirement note; old runtime
  references are intentional evidence, not active requirements.
- Hash product source/golden tests and unrelated caveman/Claude assets before/
  after; inspect Git status/diff/check for unintended changes.
- Run existing extension unit tests and scoped lint/check commands. Android
  source/build files unchanged: reuse fresh56-JVM/150-task proof unless a
  dependency/build delta appears. Do not invoke removed Bee commands as checks.
- Confirm remote pushed commit after review; report remaining product coverage
  and historical references accurately.

## Questions

None. Removal authorized; completed product history and outside archives survive.
