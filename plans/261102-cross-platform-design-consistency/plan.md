# PassChef — cross-platform design consistency

Date:2026-11-02, Asia/Saigon. **D01/D02/W01–W03/M01–M04/E01–E04 source/preparation and V01 local consolidation Done;visual/native acceptance Pending.**
Owner asks one coherent style across website,phone and extension before deployment.
Owner confirms domain control;Cloudflare service choice remains unspecified.
**Deployment owner-deferred until UI alignment**;no hosting/registration/push action.
Parent:[master queue](../261022-project-work-queue/plan.md),
[website/C05](../261022-project-work-queue/phase-03-website.md),
[Android Phase1](../261012-mobile-cookbook-phase1/plan.md).

## Direction and scope

Default baseline:the current extension's Precision design,not the older neon/blue
prototypes. Warm ink,amber actions,Fraunces/Geist typography roles,4px rhythm,
hairlines,quiet status colors. Platform-native readable sizes/touch targets remain.
[Source inventory/contract draft](reports/design-baseline-audit.md) is read-only;
no new browser/phone visual acceptance is claimed. W01 now changes the bounded web foundation;older source snapshots stay historical.

Existing website sections and Android Cookbook capabilities only. Preserve Cooking
wording,EN/VI,accepted extension journeys,C01/UI01 temporary Display-default deferral,
security/session/clipboard semantics and Golden tests. QR reader,Autofill,Drive,
account authoring,new profiles/service work,iOS and optional light mode remain Later.

## Sequence and task status

| Order | Phase / stable child IDs | State / exit |
|---|---|---|
|1|[Design contract](phase-01-contract.md),D01–D02|D01/D02 Done for source contract/staging;[D02 review Passed](reports/design-contract-independent-review.md). Runtime font/visual proof Pending.|
|2|[Existing website](phase-02-website.md),W01–W03|W01–W03 source/assets reviewed;[W03 brand](reports/website-brand-assets-independent-review.md) and actual OG asset inspection Passed. Actual browser acceptance Pending.|
|3|[Android Cookbook](phase-03-android.md),M01–M04|M01–M04 source/build reviewed;[M04 final slice](reports/android-sources-mapping-independent-review.md). Source delivery complete;actual phone acceptance Pending.|
|4|[Extension coherence](phase-04-extension.md),E01–E04|[E03 final02 reviewed](reports/extension-role-alignment-independent-review.md),151unit/67package/83file ZIP c11e…ae9 Passed locally;[E04 preparation reviewed](reports/extension-ui-qa-preparation-independent-review.md),32independent offline groups Passed. Actual browser/owner acceptance Pending.|
|5|[Integrated acceptance](phase-05-verification.md),V01–V02|[V01 local matrix](reports/verification.md)/[review](reports/verification-preparation-independent-review.md) Done;fresh browser/phone/owner acceptance and V02 remain Pending.|

Every task:freeze scope/source→implement→compile/meaningful affected verification→
independent review→resolve findings→update report/phase/master handoff→next task.
Do not postpone all verification until Phase05 or call compiled UI visually accepted.

## Current local handoff

W01 foundation and W02 Privacy correction reviewed;M01 bundles reviewed native
fonts/theme/header/window/icon with root build/tests and independent review.
M01–M04 native source/build reviewed;existing theme/locked form/list/manual/detail/
sources/mapping treated. Controllers/security/labels/testTags remain constraints.
W01–W03 website source/assets reviewed;E01 found demonstrated extension font/role
gaps. E02/E03 local-font/role/shape83file candidate verified;E04 runtime packet reviewed.

## Constraints and resources

Root alone runs Gradle/ADB;at most2subagents;pnpm JS/uv Python;no dependency mixing.
No GUI/phone action without fresh scoped resource readiness. FLAG_SECURE stays;
no private Dish/Spice capture. Source design boards are proposals,not runtime proof.
Golden/approved tests immutable;existing source/artifact acceptance stays dated.
Sites skill is local-only/presentation guidance:existing checkout,no registration,
source sync,preview launch or publication. No deploy until alignment and later
specific owner-approved action;C05/C04/C11/broad release gates remain open.

## Unresolved

Actual font loading/shaping on devices;extension coherence verdict;
fresh desktop/phone slots and visual acceptance. Service/project choice
can wait;do not re-ask deployment details during UI alignment.

## Update 2026-11-10 — Android superseded by UX redesign
Owner decisions in [Android UX redesign](../261110-android-ux-flow-redesign/plan.md) replace three constraints above **for Android**: plain words instead of Cooking wording in UI (D14), light theme first-class (Palette D, D13), Google Picker/Drive file grant allowed (D1). Wordmark drops the amber dot (D16, extension + website follow in a separate plan). M01–M04 Android source stays historical; new Android visuals come from the redesign phases 03–06.
