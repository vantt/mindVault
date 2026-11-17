# M01 — Android Precision theme, fonts and root shell

Date:2026-11-02, Asia/Saigon. **Implemented;root local build/tests and
[independent source review Passed](android-theme-independent-review.md). No phone/rendering acceptance.**
Parent:[Android phase](../phase-03-android.md),
[contract](../../../docs/specs/cross-platform-design.md).
M02–M04 screen hierarchy/polish remain Pending;M01 is a working theme/shell slice.

## Changed source and exact assets

[Root immutable95file before snapshot](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-theme/before.json)
unchanged. [Author final source receipt](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-theme/after.json):
**93old files preserved;2modified+15new**. No controller/MainActivity/security/storage/
test/dependency/Gradle configuration change.

- Modified `MainCookbookScreen.kt`:real existing language/reload/registerClear/
  unlocked callbacks retained;Precision theme/opaque Surface,PassChef header,
  safeDrawing+IME insets and remaining-content weighted Box.
- Modified Manifest only application theme/icon attributes;permissions,activities,
  backup/data rules unchanged.
- New `ui/passchef-palette.kt`,`passchef-type.kt`,`passchef-theme.kt`,
  `cookbook-components.kt`:opaque semantic scheme,all15Material typography defaults
  Geist,explicit variable-axis400/500/600families,4dp/square shape roles,header/
  section/status/48dp primary+secondary primitives for following real screens.
- Four original TTFs copied to `res/font/{fraunces_upright,fraunces_italic,geist,
  geist_mono}.ttf`;three original OFL notices+filtered provenance in `assets/fonts/`.
  Every font/license byte matches reviewed website/font-stage SHA;no provider fetch,
  conversion,subsetting or app dependency. Fraunces native wght400 is explicit despite
  raw900default;staticNFC brand/display only. Freeform/dynamic forms/headlines use
  Geist;Recipe/Dish Mono style available for M02/M03. NFD limitations remain recorded.
- New `res/values/styles.xml` common dark native theme and `values-v27/styles.xml`
  API-qualified light-navigation-bar flag. System biometric/provider surfaces stay
  native;app window colors follow Precision,not a custom authentication screen.
- New `mipmap-nodpi/passchef.png` is an exact unmodified copy of extension
  `src/assets/icon-128.png`;documented legacy mark exception,no redraw/resampling.

Header FlowRow lets the language action wrap as a whole at constrained font sizes;
brand row is unweighted,language target≥48dp. Actual font-scale/IME/window behavior
still requires device checks. Existing screen labels/testTags and language action
are unchanged;controller first-setup logic is not exposed/inferred by composition.
FLAG_SECURE,background clearing/lock,remember inputs,5/10minute deadlines,reveal/
clipboard ownership unchanged. Existing screen buttons are not all migrated to
new primitives yet;do not call every control shape/hierarchy aligned.

## Actual verification and retained failures

Root alone ran JDK17/SDK36 Gradle:

```bash
./gradlew :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug
```

| Attempt | Actual result / repair |
|---|---|
|Run01|Exit1:Kotlin/unit/APK/testAPK completed,lint rejected windowLightNavigationBar in unqualified values because API27/min26. Original XML/header drafts and raw log retained.|
|Run02|Exit0,16s:qualified API27 theme fixed lint error;header FlowRow revision compiled. One introduced optional-modifier-order warning remained.|
|Run03|Exit0,10s,116tasks(20executed/96up-to-date):all4tasks Passed;10JUnit tests/0failures/errors/skips;lint0errors,3existing target/dependency warnings. CookbookStatus modifier-order warning removed without dependency/target upgrades.|

[Run03 actual identities/tests/APKs](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-theme/run03-result.json)
bind110source files,debug/test APKs,lint and actual test XML.
Debug APK SHA256`afaa6d5d9f4a9936353011de6437dc4ce06113eebe1515724467eec856f94114`;
test APK SHA256`a5bdc551b621df13a24b3f377962efd99a52b89a54d45ba737f1fdf5ea8c8951`.
[Run03 log](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-theme/build-run03.log),
[badging](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-theme/run03-badging.txt),
[manifest dump](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-theme/run03-manifest.txt)
retained by root. Existing APKs preserved separately before builds;no installed
candidate or phone runtime implied. Test-APK compilation is not instrumentation execution.

[XML/source receipt](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-theme/xml-source-verification.json)
confirms manifest attribute allowlist and API27 item only in qualified resources.
[Tester final static checks](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-theme/verification-tester-final-static.json)
Pass4/4;initial tester icon lookup error is retained separately,not a source defect.
Author checks confirm17website files,extension74/tests46/heldZIP unchanged;all
new Kotlin files23–81lines. `git diff --check` Passed. No Gradle/ADB/GUI by builder.

[Tester verification](android-theme-verification.md) confirms final packaged assets and source/build bindings.

Unresolved:M02–M04 existing screen treatment;
actual bundled-font shaping/weight,large-text/header/IME/insets/window/icon,
TalkBack/touch and affected security-flow acceptance on a fresh scoped phone run.
No mockup/compile substituted for physical acceptance. Deployment remains held.

Final bookkeeping2026-11-02:independent source/build and plan review Passed;M01
source Done,M02 Ready. Evidence binds Run03;actual native acceptance remains Pending.
