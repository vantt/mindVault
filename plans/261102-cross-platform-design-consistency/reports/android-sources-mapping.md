# M04 — Android sources and mapping presentation

Date:2026-11-02, Asia/Saigon. **Implemented;root local build/tests Passed.
[Tester verification](android-sources-mapping-verification.md)/[independent source review Passed](android-sources-mapping-independent-review.md);phone/provider/UI acceptance NotRun.**
Parent:[Android phase](../phase-03-android.md),[M03](android-cookbook-journeys.md).

Only SourceScreen.kt78lines and MappingScreen.kt46lines changed;no new file.
A stateless Surface/hairline group helper in SourceScreen organizes existing
Google connection,offline TSV and saved-cache controls. Shared ID remains shared;
permission/reunlock,unencrypted metadata/limits and offline-no-Google disclosures
stay visible with exact words. Dynamic document/tab/cache titles use Geist;
raw ID/TSV/numeric/preview text uses bundled Mono without normalization. Existing
4dp/48dp actions wrap/full-width as appropriate. No nested scrolling:the real
CookbookScreen ancestor still owns scroll-to behavior.

Source id300/title200/table120000limits,remember inputs,changed()edit+authorization+
source invalidation,enabled/busy/connect/offline/tab/removal callbacks retained.
Draft key(draft)and remember(saved.mapping.origin.key)removal confirmation remain.
AlertDialog title,Google rows/Spices preservation text,Clear/Cancel/dismiss wording
and guards unchanged;no consent bypass,automatic deletion or hidden disclosure.

Mapping retains header recognition/Recipe proposal,first-row1or2defaults,first-row
length4/column length2/MAX_CELL labels,unique-role/backend validation,original-row
preview/drop/coerce/take5 and existing100character cell truncation. Blank columns/
labels stay ignored;exact chosen-column conversion and firstRow fallback feed the
same explicit confirm callback. No new persisted/auth/controller/storage state.

[Immutable113file before](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-sources-mapping/before.json),
[after source receipt](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-sources-mapping/after.json),
[exact patch](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-sources-mapping/sources-mapping.patch)
prove111old sources/configs/tests unchanged and exact literal multiset preserved
per edited file,including every EN/VI label/tag/disclosure. Patch SHA256
`233a425fb7f5e9fc67bd2eeddecf2784f9ec9e8c32233c7679ee92a2b79d2179`.
No controllers/MainActivity/tests/Gradle/dependencies/fonts/theme/web/extension/
workflow changes. Builder ran no Gradle/ADB/GUI/network/device action;whitespace Passed.

Root alone ran JDK17/SDK36:

```bash
./gradlew :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug
```

[Actual run01](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-sources-mapping/run01-result.json)
Passed10s,116tasks(19executed/97up-to-date),113source identities stable during build.
Actual10JUnit tests:0failure/error/skip;lint0errors,3existing warnings/1existing hint.
APKs,JUnit XML and lint reports retained. DebugAPK SHA256
`f2dd45440d0fbe0011cef0951bd4642db4e2cb2e797e51cfb833a2aa1df495f8`;
unchanged testAPK SHA256`a5bdc551b621df13a24b3f377962efd99a52b89a54d45ba737f1fdf5ea8c8951`.
Instrumented UI compiled,not executed;no selected account/cache or provider case
reaccepted from these build/static checks. Existing failed/historical cases retained.

Unresolved:actual source/mapping/preview/
cache-clear dialog readability,EN-VI/font scaling/IME/scroll/TalkBack and affected
consent/error preservation on a fresh scoped phone run. Broader UI/website alignment
and owner deployment hold remain;no new feature or final mobile acceptance claimed.

Final bookkeeping2026-11-02:independent source/build and dispatch review Passed;
M01–M04 source delivery Done,W02 website composition Ready. Frozen Run01 evidence
does not establish actual phone/provider/visual acceptance.
