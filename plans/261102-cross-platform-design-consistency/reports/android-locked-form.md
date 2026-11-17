# M02 — Android locked-form hierarchy

Date:2026-11-02, Asia/Saigon. **Implemented;root local build/tests Passed.
[Tester verification](android-locked-form-verification.md)/[independent source review Passed](android-locked-form-independent-review.md);phone acceptance NotRun.**
Parent:[Android phase](../phase-03-android.md),[M01 theme](android-theme.md).

Only `VaultScreen.kt`82lines and `ui/cookbook-components.kt`88lines changed.
No new files. Static NFC title uses explicit Fraunces400;status uses the existing
`vaultStatus`message with Neutral or busy-Warning tone,not inferred setup/error
state. Surface/hairline groups separate existing intro/master-password field from
all five always-visible setup-only Spice fields. Existing actions become full-width
4dp/48dp primary password/recovery,secondary biometric and ghost Lock,followed by
the unchanged helper text. No extra/duplicated visible labels or new capability.

Remember(notSaveable)inputs,1024limits,password transformations,keyboard options,
existing busy/unlocked/enabled predicates,Argon2 byte/jar conversion,SideEffect
clear registration,and clear-before-controller password/biometric/explicitLock
paths retained. No collapse,storage lookup,new setup flag/auth state or handler.
Exact string-literal multiset matches M01 source,including all EN/VI labels/helpers.
Input groups use inherited Surface(Precision ink100 in actual Main),so enabled
field/secondary outlines are not placed on raised ink150. Default MaterialTheme
wrapper inherits actual Main;standalone proof hosts remain separate contexts,
not evidence of actual Main/header/window/font acceptance.

[Immutable110file before](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-locked-form/before.json),
[after/source identities](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-locked-form/after.json),
[exact two-file patch](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-locked-form/locked-form.patch)
retain108unchanged old sources/configs/tests and two changes. PatchSHA256
`7f4c36e161a2ab1ce36e02ad98f8650927209b6d7f1739c6e09ea287fc5042ce`.
No controller/MainActivity/security/resources/Gradle/dependency/test/web/extension/
workflow mutation. FLAG_SECURE,session/mask/reveal/clipboard policies untouched.
`git diff --check` Passed. Builder ran no Gradle/ADB/GUI/server/network/device action.

Root alone ran JDK17/SDK36:

```bash
./gradlew :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug
```

[Actual run01](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-locked-form/run01-result.json)
Passed13s,116tasks(19executed/97up-to-date),110source identities stable throughout.
Actual10JUnit tests have0failure/error/skip;lint0errors,3existing warnings/1existing
hint,no introduced warning. Debug/test APKs,4JUnit XMLs and lint reports frozen.
DebugAPK SHA256`2e47e38399a018460f60aa7e3dc46789517e2a2c980c7ae6ef3f98abbef374c5`;
unchanged testAPK SHA256`a5bdc551b621df13a24b3f377962efd99a52b89a54d45ba737f1fdf5ea8c8951`.
Compilation of instrumentation is not executed UI/biometric acceptance. No old
failed evidence overwritten or assertions edited;no visual-assignment tests added.

Unresolved:actual six-field accessibility,
large-text/IME/scroll/EN-VI/readability and affected password/biometric/lock behavior
in the real themed Main Activity on a fresh scoped phone run. M03/M04 and deployment
hold remain unchanged. No full Android/theme/security reacceptance claimed.

Final bookkeeping2026-11-02:independent source/build and dispatch review Passed;
M02 source Done,M03 Ready. Evidence remains frozen Run01;actual phone acceptance Pending.
