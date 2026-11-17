# Phase03 — Android Cookbook theme and existing journeys

Context:[contract](phase-01-contract.md),[audit/screen map](reports/design-baseline-audit.md),
[Cookbook behavior](../../docs/specs/mobile-cookbook.md),
[existing acceptance](../261012-mobile-cookbook-phase1/phase-05-verification-delivery.md).
Priority:3. Status:M01–M04 source/build independently reviewed;actual native acceptance Pending. No runtime authorization here.

## Requirements and architecture

Theme/color/type/shape+meaningful hierarchy,not just repainting plain controls.
Keep Material3 native semantics/IME/font scaling and≥48dp touch;use bundled licensed
reviewed font assets,with explicit offline/Vietnamese fallback and pending actual shaping checks.
A small theme/composable layer wraps existing controllers;no controller/storage/
auth rewrite,new dependencies or toolkit upgrade. New code files each<200lines.

## Files and bounded tasks

M01 — create focused `app/.../ui/passchef-theme.kt`/token mapping and reusable
header/status/section primitives;apply at `cookbook/MainCookbookScreen.kt` root.
Use existing app/res conventions and keep source filenames kebab-case for new files
(e.g.`passchef-theme.kt`,`cookbook-components.kt`);no parallel enhanced screen copies.
Include application window colors/launcher resource coherence using the existing
unmodified extension mark;system biometric/provider surfaces remain native.
M02 — `VaultScreen.kt`:wordmark/status/password/Spice grouping/actions/help,existing
password/biometric routes. No guessed first-setup flag or auto-read in composition;
controller privately chooses setup/recovery and UI currently exposes a combined form.
M03 — `CookbookScreen.kt`:selected Menu/Cook/Sources navigation;group search/filter/
entry pagination/list cards;detail and masked Dish slab with status/action hierarchy.
Extract real presentation functions when needed;keep recipe/context/edit/back logic.
M04 — `SourceScreen.kt`,`MappingScreen.kt`:readable connection/offline-source/status/
preview/confirmation/cache-clear sections;preserve exact consent/confirmation scope.
No QR/scanner/Autofill/Drive/new-profile/account-write screen or fake data path.

## Implementation and TODOs

- [x] M01 theme/root shell:[delivery](reports/android-theme.md)→[tester verification](reports/android-theme-verification.md)→[independent review Passed](reports/android-theme-independent-review.md). Root10JUnit/2APKs/lint0errors;actual native rendering Pending.
- [x] M02 locked form hierarchy:[delivery](reports/android-locked-form.md)/[verification](reports/android-locked-form-verification.md)/[independent review Passed](reports/android-locked-form-independent-review.md). Sensitive state/clear/auth/labels preserved;root10JUnit/2APKs/lint Passed. Native acceptance Pending.
- [x] M03 list/manual/detail/Dish:[delivery](reports/android-cookbook-journeys.md)/[verification](reports/android-cookbook-journeys-verification.md)/[independent review Passed](reports/android-cookbook-journeys-independent-review.md). State/scroll/data/actions/labels/tags retained;root10JUnit/2APKs/lint Passed. Native acceptance Pending.
- [x] M04 source/mapping:[delivery](reports/android-sources-mapping.md)/[verification](reports/android-sources-mapping-verification.md)/[independent review Passed](reports/android-sources-mapping-independent-review.md). Input/consent/cache/mapping/labels retained;root10JUnit/2APKs/lint Passed. Native/provider acceptance Pending.
- [ ] Each slice gets its own compile/affected tests/review/update before next slice.
- [ ] Freeze APK/source/cases;fresh owner/phone readiness for visual/physical acceptance.

## Success criteria and verification

At a glance:family type/color/header/actions match website/extension while phone
remains readable/native. Existing EN/VI and long content remain usable with IME,
rotation and larger text;selected navigation/status/busy/error states unambiguous.
Root alone runs JDK17/SDK36 Gradle tasks:affected `:app:testDebugUnitTest`,
`:app:assembleDebug`,`:app:assembleDebugAndroidTest`,`:app:lintDebug`;extend to
shared/bridge tasks only if touched behavior/dependencies justify it.
Compilation is not execution. Existing tagged physical assertions remain unchanged;
if labels must change,review test protocol first and never edit Golden to suit UI.
Fresh selected phone run covers setup/password/biometric when owner available,
Menu/manual/detail/source/mapping/lock and affected masking/reveal/copy boundaries.

## Risks and security

FLAG_SECURE stays;no ordinary screenshots of real Dishes/Spices. Preserve background
lock,5/10minute deadlines,10second reveal,30second owned clipboard and nonpersistent
inputs. No rememberSaveable for sensitive state or caching Dish in view models.
Material default colors/shapes must not silently override intended theme roles.
System biometric/provider surfaces remain native;no imitation login screen.

Next:Phase04 extension delta and Phase05 integrated acceptance.
Unresolved:actual native font/large-text/IME/touch/flow behavior,phone readiness/visual owner observations. Website composition/artwork proceeds before integrated acceptance.
