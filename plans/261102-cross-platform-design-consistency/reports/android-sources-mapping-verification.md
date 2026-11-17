# M04 — independent affected Sources/mapping verification

Date:2026-11-02, Asia/Saigon. **Source/build verification Passed;
independent review Pending. Device/rendering acceptance Pending.**
Parent:[Android phase](../phase-03-android.md),[author delivery](android-sources-mapping.md).
Root alone ran Gradle;tester read frozen source and preserved actual build outputs.

## Scope and affected contracts

Bounded verifier passed5/5groups. Only SourceScreen.kt and MappingScreen.kt changed
against113reviewed files;111others remain byte-identical,including controllers,
lifecycle,tests,theme/fonts/resources and dependencies. No new file,extra scroll,
Saveable state or canonical test added. The SourceFormGroup wrapper is stateless;
existing Cookbook ancestor still owns scrolling. Source files each<200lines.

Seventeen Source contracts retained:3blank remember inputs with ID300/title200/
TSV120000 limits;valid edits call model.edit,authorization.invalidate and sources
invalidate before replacement. Connect/offline/tab choosing callbacks and all8busy
gates are unchanged. Draft stays keyed by draft;removal confirmation stays remembered
by origin key. Only explicit dialog confirmation removes the chosen local source;
dismiss/cancel only close the dialog. Permission/reconnect,unencrypted metadata,
backup exclusion,20source/1000row/32column limits and offline/noGoogle disclosures
remain visible at source level,with all original wording retained.

Fourteen mapping contracts retained:header-role/proposal logic,remembered firstrow
1/2 and role columns,manual-label map,4character firstrow/2character role limits,
MAX_CELL labels,original i+1row numbers,5row preview and100character cell excerpt.
Preview's invalid firstrow fallback remains1;confirmation's remains0. Confirmation
still ignores blank columns,converts chosen columns to zero-based indices and
passes nonblank manual labels. Busy gating remains;one sources.confirm call stays inside
an explicit action callback;no automatic confirmation/effect added.

All59original string-literal occurrences and4test tags match. No existing label or
warning duplicated/removed. New action wrappers reuse4dp/≥48dp primitives;fields/
previews use reviewed body/Mono roles without new user normalization. Actual width,
IME,fontScale,scroll,dialog focus and touch behavior remain Pending.

## Actual root build evidence

Root run01 passed the4affected tasks in10s(JDK17/SDK36,maxworkers2):
`:app:testDebugUnitTest`,`:app:assembleDebug`,`:app:assembleDebugAndroidTest`,
`:app:lintDebug`. [Root receipt](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-sources-mapping/run01-result.json)
records sourceStableDuringBuild:true with113source bindings. Tester independently
matched every current source,build-log SHA and all8preserved artifact hashes.

Four actual JUnitXMLs total10tests,0failures/errors/skips. Lint0errors,3existing
warnings(OldTargetApi,2GradleDependency)and1existing AutoboxingStateCreation hint.
Compiling the AndroidTest APK did not execute device instrumentation. M01 resource/
font identities remain protected;no redundant asset or physical-case replay.

## Actual command and immutable verification

```bash
/home/vantt/.local/bin/uv run --offline --no-project --python /usr/bin/python3 python chrome-extension/build/cross-platform-design-consistency-20261102/android-sources-mapping/verify-tester.py
git diff --check
```

Verifier exit0;5Passed/0Failed. Reusable [verifier](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-sources-mapping/verify-tester.py)
and immutable [receipt](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-sources-mapping/verification-tester.json)
retain source identities,individual assertions and actual root result counts.
No tester Gradle/ADB/GUI/network,Google consent,cache removal or native action.
Receipt SHA256`f13ab36d1a694c66f67f1ba0210c358a931277900f5258c7d94fdd4a000d201c`;
appAPK`f2dd45440d0fbe0011cef0951bd4642db4e2cb2e797e51cfb833a2aa1df495f8`.

## Unresolved

Independent review/plan update;actual native Sources/mapping layout,large text,IME,
scroll/focus and affected consent/cache-preservation acceptance. Build/source checks
do not authorize phone/account work or replace physical assertions.
