# M03 — Android Cookbook journeys and Dish presentation

Date:2026-11-02, Asia/Saigon. **Implemented;root local build/tests Passed.
[Tester verification](android-cookbook-journeys-verification.md)/[independent source review Passed](android-cookbook-journeys-independent-review.md);phone/UI acceptance NotRun.**
Parent:[Android phase](../phase-03-android.md),[M02](android-locked-form.md).

## Exact scope and state preservation

Two existing files modified:CookbookScreen.kt and ui/cookbook-components.kt;
three focused helpers added: cookbook-list.kt,cookbook-manual-detail.kt,
cookbook-dish-panel.kt. Actual filenames are kebab-case;all files34–100lines.
Parent keeps six remembered data values(page/recipe/menu/query/filter/pageIndex),
original scroll state,exact LaunchedEffect(query,filter)reset and navigate
model.back→recipe/menu clear→page path. Children are stateless presentation.
The real single vertical-scroll ancestor includes navigation/filter/pagination/
Lock/actions;no sticky outside-scroll navigation or zero-range scroll proxy.

Static Cookbook heading uses Fraunces;selected Menu/Cook/Sources and filters use
wrapping4dp/48dp choices with native selected semantics. Separate Lock unchanged.
List keeps exact query limit/filter/favorites/recent ordering/search/100pagination,
count text,entry identity/origin-row data,open/recent/favorite callbacks and busy
guards. Quiet square Surface rows use dynamic Geist labels;entry button explicitly4dp.

Manual Recipe is Mono,with parent4096/edit invalidation and unchanged saved Menu/
No Menu context semantics/menuId/Cook-enabled predicate. Detail keeps label/username/
Recipe/URL/notes/tag hint,Copy username(enabled only for nonempty username)and Back.
Recipe/Dish use Mono;freeform entry labels remain Geist,not Fraunces.

DishPanel remains its public API and preserves cook-error/dish-display/dish-reveal/
dish-copy tags,exact EN/VI strings and eight-bullet mask vs model.dish. Raised
outlined slab wraps full values without fixed height,ellipsis,selection/normalization
or new Copy behavior. Existing reveal/copy actions remain outside Raised so ink500
secondary outline is on canvas/surface. Cooking/error/warning/clipboard messages
are explicitly grouped with semantic colors and unchanged words/model sources.
No controller/lifetime/10second reveal/owned clipboard policy change.

## Actual receipts and verification

[Immutable110file before](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-cookbook-journeys/before.json),
[after/source receipt](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-cookbook-journeys/after.json),
[exact patch](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-cookbook-journeys/cookbook-journeys.patch).
108old sources/configs/tests unchanged;2modified+3new. Combined string-literal
multiset equals old CookbookScreen+primitive source:labels,tags and content retained.
Patch SHA256`f62090cae44ea882c2bda3b68a49930e700f5f724de8ad427afc0ddaf0e4e319`.
No controller/MainActivity/theme/font/manifest/resource/dependency/test/web/extension/
workflow change. No fake examples,preview,GUI/ADB/Gradle by builder;whitespace Passed.

Root alone ran JDK17/SDK36:

```bash
./gradlew :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug
```

Run01Passed15s;then root requested explicit4dp entry-button shape instead of M3
CornerFull default. Its source draft and run01 evidence remain immutable.
[Final run02](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-cookbook-journeys/run02-result.json)
Passed11s,116tasks(16executed/100up-to-date),113source identities stable during build.
Actual10JUnit tests:0failure/error/skip. Lint0errors,3existing warnings/1existing hint.
Debug/test APKs,JUnit XML/lint reports preserved;test-source/assertions untouched.
DebugAPK SHA256`b18629f3ea0e6d541e963c9e5d340cc858f58bd161867b2c86e1fa63d9923981`;
unchanged testAPK SHA256`a5bdc551b621df13a24b3f377962efd99a52b89a54d45ba737f1fdf5ea8c8951`.
Instrumentation compilation is not execution;no phone/Main rendering accepted.

Unresolved:actual scroll-to navigation,
selected-state/TalkBack/EN-VI/large-text/IME,Recipe/Dish wrapping and affected
list/manual/detail/reveal/copy/lock flows on a fresh scoped phone run. M04 source/
mapping presentation and broader design/deployment gates remain open.

Final bookkeeping2026-11-02:independent source/build and dispatch review Passed;
M03 source Done,M04 Ready. Evidence binds frozen Run02;actual phone acceptance Pending.
