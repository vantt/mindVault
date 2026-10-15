# Selected mapping and wrong-Menu UI —15October2026

Status: **Run01 Passed1/1; Mobile Partial**. Xiaomi14TPro/API36, transport30.
Production, all previous app sources/Golden/assertions and historical Failed verdicts
remain unchanged. This is actual selected offline UI acceptance, not Google mutation.

## Accepted selected boundary

New139-line `MenuMappingPhysicalTest#wrongMenuTagAndIrregularColumnsKeepLastGoodCache`
uses the existing unexported `ColdStartProofActivity`, canonical UUID storage and
genuine owner biometric setup. Existing UI helper creates a synthetic offline last-good
Menu with favorite/recent/Cook. All new Menu input/mapping/Cook actions use normal UI.

- Actual preview retains duplicate `Recipe` headers, a blank header and ragged rows.
  Duplicate role assignments and a blank required Recipe column both produce
  INVALID_MAPPING while prior exact cache/jars, RAM and durable inventory remain.
- Explicit valid remapping selects Label1/Recipe2/Username5, first data row2.
  Accepted physical rows2/3/7/8 retain literal00123/Unicode, missing usernames as empty
  and fallback label `Row 8`. Empty/missing-Recipe/invalid-Recipe rows are omitted.
  The second Recipe column and blank column are ignored by explicit choice.
- A tagged synthetic Recipe first Cooks/reveals the exact expected Dish with its
  correct saved Menu. Switching to the other saved Menu clears old Dish/Copy;
  explicit editable-text equality proves the tagged input did not change.
  Cooking then returns RECIPE_MISMATCH with the visible matching error and no Dish/
  usable Copy. Both accepted cache files and encrypted jars remain unchanged.

This samples duplicate/blank headers with explicit mapping; it does not certify every
ambiguous confidence/proposal case, every abnormal table, or all platform layouts.
No Google grant/read, clipboard write, timer, capture or process-death replay occurred.

## Local preparation and exact candidate

Build01 Passed14s,10executed/84UP-TO-DATE. Review added an explicit unchanged-input
assertion after Menu switch; prechange source is archived, no prior assertion removed.
Final build02 Passed12s,9executed/85UP-TO-DATE; lint0errors/11warnings/1hint.
No fresh JVM/browser suites: production and earlier assertions retain prior evidence.

Frozen01 binds71 app source/manifest files; all70 previous files unchanged, including
the two Google cancellation files. All27 previous host helpers, protected208 and
unrelated23 hashes match. New selected runner169lines was independently reviewed.
Debug APK unchanged `1f42a2c4b369492b0ed804fab3ec282882e831336cf4ebfec48dbdba05bfd772`;
new test APK `3e5df260279758fa03effa7092d73a31d37564c1175c232533c1278160e9a2f3`;
release unchanged `efd3b38644db2404e42faca714c963ef99467c644524833455dff32c11f6b653`.
Install-r updated only the test APK; installed debug/test hashes equal frozen01.

## Actual Run01

Owner had reported ready/unlocked; fresh native preflight confirmed connected exact
serial, awake, absent keyguard/API36. Own secure foreground guarded normal UI actions;
genuine strong-biometric round trip created the isolated jars before acceptance.
Fixture UUID `b7244e58-14f3-4947-a9fb-8c6397003ebe` was cleaned after terminal.

**Passed1/1,36.892s**, selected JUnit[1,0],terminal[-1],anchoredOK1,native0,
0failures/errors/skips,no watchdog or orchestration error. Final phase reports no
Dish/reveal/lifetime/readyDish and still-ready authenticated session; mismatch did not
destroy the usable local jars. Post-run source/APK/helper/protected audits match.
Owner Cookbook digest before/after installation and runtime remains
`894f73e51e316b6a369368ad4cebbc901609ccfe7eabe855aa73c2cbf4e7a4d6`.

Evidence outside repo:
`/tmp/passchef-cookbook-device-acceptance/menu-mapping-` prefix for baseline01,
build01/02 logs, build01-source snapshot, frozen01, candidate01, runner, install01,
run01 raw log/JSON/preflight/immediate-prelaunch JSON. Prior Google frozen/runtime
evidence remains historical and untouched; its Run01 is NotExercised, not Passed.

Unresolved: actual Google cancellation/stale callbacks/401/403; screen-off/background/
LMK/task restoration; authenticated/cross-app clipboard, broader accessibility and
historical UI/setup/wrap/cold/Run06 causes. Mobile remains Partial.
