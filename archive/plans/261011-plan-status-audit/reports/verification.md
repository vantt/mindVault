# Plan status, relevance and archive verification

Date: 2026-10-11. Base main `b5be3b0`. Documentation-only change.

## Result

All eight previously flagged plans retain relevant unfinished outcomes; none
qualifies as wholly obsolete. Current phase tables distinguish implementation,
missing verification, superseded mechanisms and accepted deferred scope.
The [active order](../../../../plans/README.md) prioritizes next slices and their
dependencies; a high-priority MVP security phase does not bring release forward.

Three incomplete extension dossiers returned to active `plans/`. Five closed
recovery/removal folders and four accepted foundation dossiers moved whole to
`archive/plans/`, preserving contexts and reports. Historical report collection
and this finished audit also use the canonical archive. Broader mobile, real
Google and residual device coverage remain tracked separately.

## Independent verification

- `pnpm test:unit` in `chrome-extension`, Linux Node24.18.0: exit0;
  77/77 tests across10 files,1.69s. No product test edits or installs.
- All39 baseline plan destinations,18 separate phase files and66 mapped files
  present. The18 phase files now contain10 Done and8 Partial statuses; monolithic
  plans/embedded roadmaps have their own per-phase tables. No `plans/archive/` remains.
- Pre-closure link check:270 local targets across131 Markdown files resolve;
  no new README anchor defect. Eleven unchanged legacy design-document TOC anchor
  issues remain outside this path migration; they are not newly introduced defects.
- All192 product/source/test/build/tooling baseline paths retain exact bytes,
  size and mode, including Golden tests and22 unrelated skills. Untracked
  `skills-lock.json` retains its earlier preserved hash and stays untracked.
- All47 references to the external `passMan-recovery` archives remain unchanged.
  Archives/device data themselves are untouched.
- Independent reviewer: no P1/P2; one P3 index inconsistency corrected to builder
  phase6 QA (phase1 contract is Done). No unsupported live API/device/full-app pass.
- Whitespace checks pass, including new/moved Markdown; final staged links and
  paths checked again before commit:132 tracked Markdown files,302 local link/image
  targets exist; all plan overviews stay below80 lines. There is no lint script in the
  extension package; source/build inputs are unchanged.

## Limits and next action

No new full E2E/manual Google/UI/device proof is claimed; those are relevant open
outcomes in the active plans. Typed7/9, exact kill/retry windows and the original
held-unwrap cause remain unexecuted/unclassified within accepted coverage limits.
Missing warnings, demo Vietnamese, toolbar clipboard clearing and failed-unlock
handling are not discarded as obsolete. Optional light mode/catchall ideas do not
block release. Start next work from main with the MVP security slice.

Unresolved design question: whether the original PBKDF2-SHA256600k vault fallback
should remain a requirement. Validate current Argon2/error/format behavior before
making a separate implementation decision; this audit does not waive or add it.
