# M03 — independent Cookbook journeys and Dish review

Date:2026-11-02, Asia/Saigon. **Bounded source/build-evidence review Passed;
final bookkeeping Passed;M03 source Done,M04 Ready. No phone/UI/security-flow acceptance.**
Parent:[Android phase](../phase-03-android.md),[author/tester evidence](android-cookbook-journeys.md).

## Actual source assessment

Read exact five-file patch,current and frozen parent/primitives,new list/manual-
detail/Dish helpers,controller/message context and tester/build evidence. No
actionable source finding. Existing cookbook handlers/state remain authoritative.

Six parent remembered values and scroll state retained. Same query/filter effect
resets pagination;same navigate calls model.back then clears Recipe/Menu before
changing page. Children stateless. Navigation/filters/pagination/actions remain
within real existing vertical scroll ancestor;no sticky proxy or synthetic range.
Selected choices carry explicit semantics and4dp/48dp;actual TalkBack/large-text
behavior still requires device evidence.

List preserves snapshot flatMap/filter/favorites/recent order,search semantics,
lastPage/shownPage clamp and drop/take100. Entry identity/origin/row/status/count
text,open→recent/cook controller path and favorite busy guards unchanged. Explicit
4dp entry button is in finalRun02;dynamic labels remain Geist. Wrapping text has
no added ellipsis/fixed height;actual long-label usability remains untested.

Manual parent4096/edit invalidation preserved. No Menu/saved origin choices retain
exact context and model.cook(recipe,menu?.menuId),with unchanged cooking/nonempty
gate. Detail retains label/username/origin/row/Recipe/URL/notes/tag hint and Back/
Copy username handlers. Recipe uses Mono;freeform title remains Geist.

Dish retains hasDish/revealed/model.dish/eight-bullet mask,exact error/display/
reveal/copy tags and controller callbacks. No selection container,normalization,
new cache/state,deadline or clipboard policy. Slab wraps without maxLines/ellipsis;
secondary action outside Raised avoids weaker outline contrast. Existing statuses
are grouped without inventing a successful Copy;clipboard message source unchanged.
These source properties do not establish actual revealed-Dish geometry or clipboard
runtime. Controller/MainActivity/lifecycle/tests/fonts/dependencies untouched.

## Actual independent source/build checks

Frozen110originals match hashes;108old unchanged,2modified+3new. Pre/postbuild113
source inventories identical and current hashes matched at review. Eight immutable
artifacts,log and final patch hashes match. Combined literal multiset across parent/
primitives/helpers exact85occurrences:83CookbookScreen literals plus2existing header
brand literals. Tester83refers specifically to old screen;counts are compatible.
All7testTags retained once. Final patch SHA256
`f62090cae44ea882c2bda3b68a49930e700f5f724de8ad427afc0ddaf0e4e319`.

Parsed4actualJUnit XML:10tests/0failures/errors/skips. Actual lint:0errors/3existing
warnings/1existing hint. RootRun02Passed11s and bound APK identities retained:
debug`b18629f3ea0e6d541e963c9e5d340cc858f58bd161867b2c86e1fa63d9923981`,
test`a5bdc551b621df13a24b3f377962efd99a52b89a54d45ba737f1fdf5ea8c8951`.
Run01Passed15s and pre-entry-shape draft retained as historical,not final candidate.
[Independent receipt](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-cookbook-journeys/independent-source-build-review.json).

Tester5/5source/preservation/build checks agree. Root alone ran Gradle;reviewer did
no Gradle/ADB/GUI/new product tests or broad replay. TestAPK assembly is not device
instrumentation. No source test/assertion changed to accommodate presentation.

## Final bookkeeping

Child/Android phase,author/tester links,parent mobile/master,index and roadmap/
changelog agree M03 source/build reviewed,M04 Ready,phone acceptance Pending.
Actual10documents/359local links resolve;original46IDs/46owner anchors retained,
master79lines. Eight immutable Run02 artifacts still match. M03 proof binds frozen
pre/post113source and review-time receipt;future M04 mutable sources excluded.
[Final receipt](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-cookbook-journeys/independent-final-bookkeeping.json).

## Unresolved

M04 sources/mapping. Fresh actual Main phone checks
for scroll-to navigation,selected states/TalkBack,EN/VI/large-text/IME,long Recipe/
Dish wrapping and affected list/manual/detail/reveal/Copy/lock flows remain Pending.
No full Android/visual/security acceptance;deployment owner-held.
