# Bee removal verification

Date: 2026-10-11. Baseline main: `fe3bac1`.

## Removed and retained

- Removed the complete `.bee/` runtime, 18 skills in each runtime mirror,
  Bee installer metadata, three Claude agent definitions, Codex hook files,
  managed instructions and ignore rules.
- Removed11 Bee Claude hook commands; retained5 non-Bee commands, matchers,
  status line and unrelated settings. Local Claude settings/skills are ignored
  by Git but were cleaned in this checkout as well.
- Preserved all10 product decision records,15 original backlog items and4
  dated criteria amendments as maintained Markdown. Current statuses reflect
  accepted scope; Google live checks, full phone UI/reader and coverage remain pending.
- Historical product plans/reports and external recovery archives remain intact.
  Former runtime references are explicitly retrospective, not runnable guidance.

## Checks

- Compared306 unchanged tracked product/history files against pre-removal
  SHA256/mode snapshots, excluding6 intentionally maintained documents: no drift.
- Compared23 unrelated Caveman/Cavecrew/lock files: no changes.
- All77 existing extension unit tests passed across10 files. Invocation:
  `pnpm test:unit -- test/unit/recipe.spec.mjs test/unit/generate_password.spec.mjs test/unit/qr-serialize.spec.mjs test/unit/qr-chunking.spec.mjs test/unit/qr-display.spec.mjs test/unit/qr-wizard-lifetime.spec.mjs`.
  Vitest treated the argument separator as ending options, so this ran the full
  unit suite. A corrected `pnpm exec vitest run` with generate-password and the
  four QR paths passed the focused37 subset too. No tests were modified.
- Linux Node24.18.0 and pnpm10.24.0; no dependency installation needed.
- Retained JSON parses; original non-Bee hook fields match exactly. No active
  Bee hooks, skills, instruction blocks or application dependencies survive.
- Local links and `git diff --check` pass. Product source/build inputs are
  unchanged, so no new Android physical or E2E proof is claimed by this cleanup.

Unrelated skill WIP and external recovery archives remain outside this commit.
