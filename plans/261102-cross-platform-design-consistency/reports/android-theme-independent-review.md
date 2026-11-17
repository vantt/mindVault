# M01 — independent Android theme and root shell review

Date:2026-11-02, Asia/Saigon. **Bounded source/build-evidence review Passed;
final bookkeeping Passed;M01 source Done,M02 Ready. No phone/rendering/security-flow acceptance.**
Parent:[Android phase](../phase-03-android.md),[author/tester report](android-theme.md),
[reviewed contract](../../../docs/specs/cross-platform-design.md).

## Source and resource assessment

Read actual MainCookbookScreen/Manifest before/current delta,four new Kotlin files,
common/API27 XML,font/license provenance,root build log/identities,JUnit/lint,APK/
manifest/resource evidence and tester scripts/receipts. No actionable source finding.

Explicit variable-font settings bind native wght400 despite Fraunces raw900default;
Geist400/500/600 and Mono400/500 likewise explicit. Static brand alone uses Fraunces;
all15Material typography roles use Geist,including dynamic headings. No user Recipe/
Spice/Dish normalization. Mono Dish style available for following screens;not yet
proof every existing Dish is migrated. Original4font/3OFL byte identity retained.

Opaque palette/theme follows reviewed roles. 4dp small controls/square larger shapes,
48dp action minima and section/status primitives are real source for following
screens. Source contains no fabricated product capability or new navigation state.
Outlined secondary primitive uses Ink500 on its intended canvas/surface;future use
on raised backgrounds must follow contract's stronger outline restriction. This is
an integration constraint,not a failure in current header use.

Actual root retains locale toggle+edit,reload on unlock,registerClear/cookbook lock,
explicit lock and locked/unlocked branching. Opaque Surface,weighted remaining body,
safeDrawing/IME padding and wrapping header are coherent source-level choices.
No claim actual large-text/header/IME behavior is accepted. Existing scroll/form
screens remain;controllers/MainActivity/session/remember-input/testTags unchanged.
FLAG_SECURE and lifecycle/background/clipboard ownership code not weakened.

Manifest delta is only application theme/icon. Shared dark window theme uses
API26-safe properties;light-navigation-bar flag lives only in values-v27. Run01's
real NewApi error is resolved without minSdk/dependency/permission changes. Actual
legacy extension128PNG is copied unchanged;no invented mark or image transformation.
No account/device/instrumentation activation by this task.

## Independently verified build and preservation evidence

Actual frozen95files match before hashes. Current old93 unchanged,only Manifest/
root shell modified;15new files. Rehashed all110Run03 source identities and4artifact
identities including immutable debug/testAPKs and lint XML/text. Debug APK SHA256
`afaa6d5d9f4a9936353011de6437dc4ce06113eebe1515724467eec856f94114`;
testAPK`a5bdc551b621df13a24b3f377962efd99a52b89a54d45ba737f1fdf5ea8c8951`.
Independent actual APK extraction confirms8font/license/provenance entries byteexact
and legacy icon equals extension source. This checks delivery bytes,not rendering.

Independently parsed actual4JUnit XML:10tests,0failures/errors/skips. Actual lintXML:
0errors/3warnings/1hint;existing target/dependency and boxing findings retained.
Run03 log exit0/BUILD SUCCESSFUL10s/116tasks matches receipt. Root alone ran Gradle;
reviewer did not repeat builds/tests or run ADB/GUI. TestAPK assembly is not actual
instrumentation. Run01 NewApi failure,Run02 introduced modifier warning and repaired
Run03 identities remain separate;no failed evidence erased.

Tester static4/4 and built-APK analysis agree:resources/manifest resolved,assets
match,callback/insets source unchanged in scope. Author/tester protected web17,
extension74/tests46/heldZIP/workflow checks remain time-bound actual receipts.
No claim those binaries/platforms were newly executed for M01.

Independent raw evidence:[source identity](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-theme/independent-source-identity.json),
[built artifacts/JUnit/lint](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-theme/independent-built-evidence.json).

## Final bookkeeping

Current child/Android phase,parent mobile/master,index and roadmap/changelog agree:
M01 source/build reviewed,M02 Ready,actual phone acceptance Pending. Tester report
links resolve. Actual10documents/346local links checked;original46IDs/46owner anchors
retained,master79lines. Four immutable Run03 artifacts still match. M01 source proof
binds frozen110identities and independent review-time receipt,not mutable sources
that subsequent M02 may legitimately change.
[Final receipt](../../../chrome-extension/build/cross-platform-design-consistency-20261102/android-theme/independent-final-bookkeeping.json).

## Unresolved

M02–M04 screen composition and per-control treatment.
Fresh phone checks of bundled-font loading/shaping/axes/fallback,EN/VI large text,
TalkBack,touch,header/IME/insets/system bars/icon,and affected lock/reveal/clipboard
flows remain Pending. No full Android/UI/security/owner visual acceptance. Original
broader phone acceptance gaps remain;deployment owner-held.
