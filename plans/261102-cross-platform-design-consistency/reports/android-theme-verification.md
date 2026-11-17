# M01 — independent affected verification

Date:2026-11-02, Asia/Saigon. **Source/resource/build verification Passed;
independent review Pending. Device/rendering acceptance Pending.**
Parent:[Android phase](../phase-03-android.md),[author delivery](android-theme.md).
Root alone executed Gradle;tester performed read-only source/artifact/result analysis.

## Actual source and resource checks

The103line [static verifier](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-theme/verify-tester-final.py)
passed4/4bounded check groups. Of95pre-edit files,onlyManifest/rootCookbook screen
changed;93protected files remain byte-identical,including controllers,lifecycle,
MainActivity/FLAG_SECURE,clipboard/session/cache/provider paths,tests and dependency
build files. Fifteennew files are exactly the reviewed theme/primitives/fonts/OFL/
provenance/windowstyles/icon scope;newKotlin files each<200lines. Extension74/tests46/
heldZIP unchanged. Six root callback/state units remain intact;safeDrawing+IME
insets and weighted content box are declared. Actual inset/keyboard behavior Pending.

FournativeTTFs and3OFLs match pinned reviewed provenance/source bytes. Kotlin font/
icon references and4XMLfiles parse/resolve;Manifest changes only theme/icon.
Source review confirms explicit FontVariation.weight for each requested face:
FrauncesNormal400(normal+italic),Geist400/500/600,GeistMono400/500. Material default
styles/dynamic section labels useGeist;only static ASCII Pass/Chef usesFraunces.
No user Unicode normalization added. Previously reviewed cmap/NFD limits remain;
metadata and declarations do not establish native shaping or fallback rendering.

Final header uses FlowRow with an unweighted brand group and separate≥48dp language
button. New primary/secondary primitives declare≥48dpheight and4dpcorners;theme
square surfaces use compatible0dpCornerBasedShape. The header change addresses a
source constraint concern;320dp/large-font appearance remains unaccepted. M02–M04
screen composition and application of new Dish/status primitives remain later slices.

## Root build history and actual final outputs

| Root run | Actual outcome |
|---|---|
|01|Failed atlint:commonstyles.xml windowLightNavigationBar requiresAPI27,min26. Kotlin/JVM/APK tasks completed;10tests0failures. Failure log/artifacts retained.|
|02|Passed all4tasks,16s after commonbase+v27override and headerwrap repair. Introduced ModifierParameter warning remained;unused status-function optional parameters reordered,without behavior/caller change.|
|03|Passed all4tasks,10s on final source. Four actual JUnitXMLs:10tests,0failures/errors/skips. Lint0errors,3existingwarnings and1hint;NewApi/ModifierParameter absent.|

Actual final task set:`:app:testDebugUnitTest`,`:app:assembleDebug`,
`:app:assembleDebugAndroidTest`,`:app:lintDebug`;root used JDK17/SDK36,maxworkers2.
DebugAndroidTest assembly was up-to-date in run03;**no device instrumentation ran**.
Retained3warnings are OldTargetApi and2GradleDependency;hint is existing
AutoboxingStateCreation. No dependency/minSDK change or lint suppression introduced.
[Run03 receipt](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-theme/run03-result.json)
binds110actual source identities,2APKs,JUnit/lint artifacts and exit0.

Tester analyzed the preserved run03 APK,not an unbound rebuilt candidate:

- FourTTFs,3OFLs and provenance asset match source bytes insideAPK.
- Launcher source and packaged128×128PNG both match original extension artwork byte-for-byte;no re-encoding/pixel-equivalence substitution needed.
- CompiledManifest resolves Theme.PassChef andpasscheficon;compiled resources containcommonbase+v27override. minSDK26/targetSDK34 retained.
- MainAPK SHA256`afaa6d5d9f4a9936353011de6437dc4ce06113eebe1515724467eec856f94114`;testAPK`a5bdc551b621df13a24b3f377962efd99a52b89a54d45ba737f1fdf5ea8c8951`.

[Built verification](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-theme/verification-tester-built.json)
checks all110current files against final root receipt. Static receipt preceded only
the unused optional-parameter reorder;final root compile/tests/source binding cover
that delta. Both earlier static receipts remain intact. Initial tester icon lookup
used nonexistent`src/icons/icon128.png`;that checker-path failure was preserved,
corrected to`src/assets/icon-128.png`,then static groupsPassed. It was not a product
asset failure or ignored failing test. No canonical tests added/modified for styling.

## Reproducible verifier commands and identities

From repository root,existing uv/systemPython;no installs:

```bash
/home/vantt/.local/bin/uv run --offline --no-project --python /usr/bin/python3 python chrome-extension/build/cross-platform-design-consistency-20261102/android-theme/verify-tester-final.py
/home/vantt/.local/bin/uv run --offline --no-project --python /usr/bin/python3 python chrome-extension/build/cross-platform-design-consistency-20261102/android-theme/verify-built-apk.py
```

Each exited0. Existing cachedaapt2 performed read-only xmltree/resources dumps;
[manifest](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-theme/tester-apk-manifest.txt)
and [resource dump](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-theme/tester-apk-resources.txt)
were tied to preservedAPK by equalSHA before analysis. No Gradle/ADB/GUI/network
or native input/clipboard/account operation by tester.

Static receipt SHA256`af9cc1d40fb37ff8d0190ced1bc47afc50764c3a85db79935ccb3fa45447eee5`;
built receiptSHA`b751acf9bcfff7a39f01b0e6a21745496a5897d7516814223134c09fbe1ed9a6`.
Both reproducible verifier scripts remain<200lines;exclusive-create rawJSON retains
assertions,source/artifact hashes and actual result scopes.

## Unresolved

Independent review/plan update;actual fonts/axis/NFD shaping,smallwidth/fontScale,
IME/insets/native lock/reveal/Copy and phone visual acceptance. Build/source checks
do not replace existing physical assertions or authorize a new phone/desktop slot.
