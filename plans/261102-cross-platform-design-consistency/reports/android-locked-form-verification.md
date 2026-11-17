# M02 — independent affected locked-form verification

Date:2026-11-02, Asia/Saigon. **Source/build verification Passed;
independent review Pending. Phone/rendering acceptance Pending.**
Parent:[Android phase](../phase-03-android.md),[M01 verification](android-theme-verification.md).
Root alone ran Gradle;tester read source,hashes and preserved actual build results.

## Actual scope and sensitive contracts

Bounded verifier passed4/4groups. Exactly VaultScreen.kt and cookbook-components.kt
changed against the reviewed110-file M01 baseline;108other files remain byte-identical.
Controllers,MainActivity/FLAG_SECURE,lifecycle,session/clipboard/provider/cache paths,
existing tests,resources/fonts/manifest and dependencies remain unchanged. Existing
component bodies remain byte-identical;only the ghost action was appended. No new
source files or canonical test/mirror assertions added. Source files remain<200lines.

The original2remember containers still back6masked input slots:one master password
and5Spice entries. Both1024-character guards,PasswordVisualTransformation and
password keyboard types remain. No rememberSaveable route introduced. All10checked
sensitive state/clear/branch units remain,and password-byte/jar conversion through
clear-before-controller.password is equivalent after whitespace/separator formatting.
Clear-before-biometric and clear-before-explicitLock remain;SideEffect registers
the same clear callback. Password busy+nonempty gate,field/biometric busy gates and
always-available lock retain their original meaning. No first-setup status guessing.

All8existing EN/VI text pairs—including instructions,field/action labels and footer
limits—remain exact and occur once each. Static pairs remain NFC;no dynamic user
normalization added. Surface groups introduce no duplicate field-label headings.
StaticNFC Cookbook title uses the reviewed display role;forms/helpers/actions inherit
Geist. New ghost action declares4dpcorners and≥48dpheight;root remains scrollable.
These are source facts,not verified touch/fontScale/IME or rendered masking claims.

## Actual root build and preserved artifacts

Root ran the4affected tasks with JDK17/SDK36,maxworkers2:
`:app:testDebugUnitTest`,`:app:assembleDebug`,`:app:assembleDebugAndroidTest`,
`:app:lintDebug`. Actual run01 log says **BUILD SUCCESSFUL in13s**.
[Root receipt](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-locked-form/run01-result.json)
records buildPassed:true,sourceStableDuringBuild:true and110source bindings.
Tester independently matched every current source and all8preserved artifact hashes.

Four actual JUnitXMLs total10tests,0failures/errors/skips. Lint0errors,3existing
warnings(OldTargetApi and2GradleDependency)plus1existing AutoboxingStateCreation hint.
TwoAPKs compile;device instrumentation was **not executed**. No full font/asset
re-audit:all M01 resources remain identical,so its scoped packaging evidence is reused.
AppAPK SHA256`2e47e38399a018460f60aa7e3dc46789517e2a2c980c7ae6ef3f98abbef374c5`;
unchanged testAPK`a5bdc551b621df13a24b3f377962efd99a52b89a54d45ba737f1fdf5ea8c8951`.

## Verifier command, preserved setup mismatch and receipt

```bash
/home/vantt/.local/bin/uv run --offline --no-project --python /usr/bin/python3 python chrome-extension/build/cross-platform-design-consistency-20261102/android-locked-form/verify-tester-02.py
git diff --check
```

Both exit0. Initial checker expected an older root receipt field named`exit`;
M02's reusable recorder uses`buildPassed`. Initial3Passed/1Failed receipt remains
immutable. Corrected reader checks actual buildPassed/source stability,exact log
SHA and successful log result;all4groupsPassed. No product implementation or test
expectation changed to resolve that checker schema mismatch.

Reproducible [verifier](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-locked-form/verify-tester-02.py)
and [final verification](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-locked-form/verification-tester-02.json)
retain each assertion,source hashes,actualJVM/lint results and limits.
Verification SHA256`966bacb8f285cf08b757e08b644f7f7ca92c617e6ded8eed94d720bff48d6333`.
No tester Gradle/ADB/GUI/network/native input,secret preview or phone acceptance.

## Unresolved

Independent review/plan update;actual narrow/large-font/IME/keyboard/scroll/contrast/
masking/native auth acceptance. JVM/source/build checks do not replace physical
security cases or authorize a phone slot. Broader M03/M04 design work remains.
