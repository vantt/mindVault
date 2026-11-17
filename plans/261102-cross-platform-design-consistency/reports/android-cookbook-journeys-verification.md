# M03 — independent affected Cookbook-journey verification

Date:2026-11-02, Asia/Saigon. **Source/build verification Passed;
independent review Pending. Phone/rendering acceptance Pending.**
Parent:[Android phase](../phase-03-android.md).
Root alone executed Gradle;tester read frozen source/build/artifact evidence.

## Exact scope and preserved ownership

Bounded verification passed5/5groups. Against110reviewed M02 files,only
CookbookScreen.kt and cookbook-components.kt changed;3focused presentation files
were added:CookbookList,ManualCook/Detail and publicDishPanel. All108other files
remain byte-identical,including controllers,MainActivity/lifecycle,locked form,
tests,theme/font/resources and dependencies. Existing primitive bodies remain
byte-identical;CookbookChoice was appended. All affectedKotlin files<200lines.

The parent retains all6remember states(page,Recipe,Menu,query,filter,pageIndex),
query/filter reset effect and navigate's model.back/Recipe/Menu clearing. New helpers
are stateless;callbacks route edits back to the same parent. The single original
verticalScroll ancestor encloses navigation/lock and content,retaining the physical
helper's scroll-to precondition at source level. FlowRow selected navigation/filter
choices declare48dpheight/4dpcorners and selected semantics. Actual scroll/selection/
large-font behavior needs the later native run.

## Meaningful affected contracts checked

- Exact derived favorites/recent ordering,search and100-entry pagination formulas;previous/next bounds and parent page setter transport.
- 4096Recipe/query guards;model.edit before Recipe/Menu replacement;NoMenu and savedorigin callbacks;Cook uses actual MenuID and original nonempty/not-cooking gate.
- Entry opening/favorite source-busy gates;username-copy gate;detailorigin+row/Recipe/optionalURL/notes/tag boundary and Back callback.
- PublicDishPanel cooking/error/warning/clipboard statuses;8bullet mask,raw revealedDish expression,reveal/copy callbacks and original labels. No Dish truncation added;Recipe/Dish use reviewed Mono roles.
- All83original string-literal occurrences across extracted presentation source are exact;all7test tags occur once with their original names.

These are comparative source checks,not mocked cooking/provider/clipboard or new
cosmetic unit tests. Existing controller/timer/security acceptance is not widened.
Dynamic user labels remain Geist;no normalization or new persistent state introduced.

## Actual root builds and result analysis

Run01 passed all4tasks in15s. Source review then requested explicit4dp shape on the
entry-opening TextButton;its draft was preserved. No callback/label/busy gate changed.
Final run02 passed all4tasks in11s:
`:app:testDebugUnitTest`,`:app:assembleDebug`,`:app:assembleDebugAndroidTest`,
`:app:lintDebug`(root JDK17/SDK36,maxworkers2).
[Final root receipt](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-cookbook-journeys/run02-result.json)
records sourceStableDuringBuild:true and113source identities. Tester independently
matched current files,build log SHA and all8preserved APK/JUnit/lint artifacts.

Four actual JUnitXMLs total10tests,0failures/errors/skips. Lint0errors,3existing
warnings(OldTargetApi,2GradleDependency)and1existing AutoboxingStateCreation hint.
AndroidTest APK assembly does not execute instrumentation. Unchanged M01 fonts/
resources retain their dated packaging evidence;no redundant full asset audit.
No actual native layout/auth/lock/clipboard/provider case or owner visual verdict.

## Actual verification command and receipt

```bash
/home/vantt/.local/bin/uv run --offline --no-project --python /usr/bin/python3 python chrome-extension/build/cross-platform-design-consistency-20261102/android-cookbook-journeys/verify-tester.py
git diff --check
```

Verifier exit0;5Passed/0Failed. Reusable [verifier](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-cookbook-journeys/verify-tester.py)
and immutable [actual receipt](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-cookbook-journeys/verification-tester.json)
retain each source/behavioral-preservation assertion,all affected identities and
actual root results. No tester Gradle/ADB/GUI/network/account/native input action.
Receipt SHA256`a3694003360d9cedf6f01746ffdb9999c9691e4b41d133f5ef00b3934db81f44`;
final appAPK`b18629f3ea0e6d541e963c9e5d340cc858f58bd161867b2c86e1fa63d9923981`.

## Unresolved

Independent review/plan update;native narrow/large-text/IME/scroll/selection,actual
long Recipe/Dish layout and masking/reveal/Copy regressions. Source/JVM/build evidence
does not replace physical assertions or authorize a phone slot. M04 remains later.
