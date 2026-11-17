# Android UX flow redesign — plan

Date 2026-11-10 · Sub-project: Android app (`app/`) · Status: **Phases 01–06 implemented (2026-11-10); 07 verification: device proofs + real Picker pass; 11 legacy Google* proofs triaged 2026-11-12 (run 3 / skip 8, awaiting owner); 2 UX notes awaiting fix choice** ([report 08](reports/08-implementation-verification.md)). All decisions D1–D18 settled (D7 deferred). Uncommitted.
Owner ask: "Android UI too plain; flow bad (picking Sheet by pasting URL). Redesign UX flow."
Parent: [master queue](../261022-project-work-queue/plan.md) Track02 Mobile · follows [Cookbook phase 1](../261012-mobile-cookbook-phase1/plan.md).

## Docs
- Spec (principles, IA, flows, screens, decisions D1–D18): [ux-flow-redesign-spec.md](ux-flow-redesign-spec.md)
- Wireframes: [visuals/android-ux-wireframes.html](visuals/android-ux-wireframes.html) (artifact v8) · First-screen options: [visuals/e1-onboarding-options.html](visuals/e1-onboarding-options.html) · Palette: [visuals/palette-study.html](visuals/palette-study.html)
- Reports: [00 insight/value](reports/00-product-insight-and-value.md) · [01 audit](reports/01-current-flow-audit.md) · [02 picker](reports/02-sheet-picker-research.md) · [03 patterns](reports/03-password-manager-ux-patterns.md) · [04 design language](reports/04-extension-design-language.md) · [05 plain terms](reports/05-plain-language-terminology.md) · [06 palette](reports/06-palette-study.md) · [07 picker spike results](reports/07-picker-spike-results.md) · [08 implementation + verification](reports/08-implementation-verification.md)

## Decisions (owner, 2026-11-10)
| ID | Decision |
|---|---|
| D1 | GO: Google Picker + `drive.file` is the only Sheets scope; **drop `spreadsheets`**. Paste/Share-in route through Picker filtered by ID |
| D13 | Palette D: Ink+Amber dark + Linen light, follow system, both first-class |
| D14 | Plain words in UI; cooking metaphor = tagline only |
| D15 | Secret parts 1–5, user picks count |
| D16 | Align extension + landing (plain terms, dot-less wordmark) — separate plan |
| D17 | Multiple secret-part sets on Android, one per sheet (vault format v2) |
| D18 | First run = 4 screens: E1 interactive tear, E2 stranger view, E3 by hand, E4 secret parts |
| D2–D12 | Approved as recommended (spec §9); D7 quick-return grace = Deferred |

## Gate
Open gate: none. Order 02 → (03, 04, 05 parallel-ish) → 06 → 07. 06 also needs revised dot-less wordmark.

## Phases

| # | Phase | Status | Depends on |
|---|---|---|---|
| 01 | [Picker spike: drive.file + Sheets API on device](phase-01-picker-spike-drive-file-sheets-api.md) | **Done — GO** (M1–M10 pass; M11 → 04) | — |
| 02 | [Navigation shell + state preservation](phase-02-navigation-shell-state-preservation.md) | **Done** | — |
| 03 | [Onboarding / unlock split](phase-03-onboarding-unlock-split.md) | **Done** | 02 |
| 04 | [Connect-Menu wizard + mapping](phase-04-connect-menu-wizard-mapping.md) | **Done** (real Picker flow needs owner on device) | 02 |
| 05 | [Home list / detail / Cook / Kitchen](phase-05-home-list-detail-kitchen.md) | **Done** (incl. D17 vault v2) | 02 |
| 06 | [Visual polish + brand assets](phase-06-visual-polish-brand-assets.md) | **Done** (light/dark, icons, launcher) | 03–05 |
| 07 | [Verification](phase-07-verification.md) | **In progress**: automated, V1–V13, re-baselined proofs, real Picker pass; open: 3 Google* runs (owner), 2 UX fixes | 01–06 |

## Key dependencies
- Google Cloud (done for spike): Picker API, `drive.file` on Data Access, Android client `dev.passchef.mobile.demo` + debug SHA-1. Release client/SHA-1 + verification later.
- Brand kit `docs/designs/passchef-brand-v2/` still has amber dot in wordmark → revise before 06 ([brand plan follow-up](../261106-passchef-chef-keyhole-brand/plan.md)).
- Existing androidTest proofs + golden tests immutable; testTags kept (spec §4).
- No new Gradle deps (play-services-auth 22.0.0 already has Picker params).

## Supersedes (other plans)
- [Cross-platform design consistency](../261102-cross-platform-design-consistency/plan.md): Android "preserve Cooking wording", "light mode Later", "Drive Later" replaced by D14/D13/D1.
- [Cookbook phase 1](../261012-mobile-cookbook-phase1/plan.md): `SourceScreen` paste flow + `spreadsheets` scope replaced by 04; its Google grant evidence (G1/G2) is for the old scope.
- [Master A19/A20](../261022-project-work-queue/phase-02-mobile.md): Picker file grant (not Drive-sync) and per-sheet secret-part sets pulled in here; Drive-sync/Autofill stay Later.

## Docs to update after build
`docs/specs/mobile-cookbook.md`, `docs/specs/cross-platform-design.md` (light theme, amber/accent-ink rule, dot-less wordmark, Drive allowed), `AGENTS.md` (Cooking Protocol → docs/marketing only, D14), `docs/specs/mobile-sheets-client.md` (scope → drive.file), `docs/runbooks/google-oauth-sheets-setup.md` (Picker API, drive.file, Android client), `docs/commercialization/legal/permissions-review.md` (narrower scope), `docs/project-changelog.md`, `docs/development-roadmap.md`.

## Unresolved
- Re-baseline 33 non-golden androidTest proof files that drive old UI text (list: report 08).
- Owner device checks: real Picker pick → save, refresh, re-pick after scope drop, Share-in.
- Spec Q3 reset path, Q5 website link vs lock, Q10 import sets from extension, Q11 sheet colours, M11 multi-account.
- Existing users after scope drop: one filtered re-pick per sheet — confirm copy in 04.
