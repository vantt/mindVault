# Mobile Core recovery — independent product review

Date: 2026-10-11. Base: main `b097b9c`; reviewed frozen working-tree product files.
Scope: accepted Track B plan `plans/261011-mobile-core-recovery/plan.md`.
No Bee/tooling WIP reviewed or changed. Initial review made no product source/test
edits; its author was later assigned the repairs documented below.

## Current verdict — P2 resolved by authentic browser verification

The original P2 is **resolved** after a separate independent pass against actual
headed Chromium **143.0.7499.4**, including the unchanged modules loaded in the
extension. No remaining P1/P2 finding in accepted Core scope. Final evidence:
**26 JVM tests**, **56 browser recipe/tag vectors**, **22 original JS tests**,
all **1,112,064 scalar inputs**, and rebuilt Android AAR/JVM JAR. Commit/merge pending.

A first Node repair was insufficient: actual browser evidence found 28 different
mappings (1,552 browser versus 1,580 Node). Its historical **25 tests/35 Node
vectors** below are superseded for browser parity. Final verification is recorded
at the end; it does not promise identical casing in every other Chrome version.

## Original P2 — platform Unicode uppercase tables change the cooked password

Location: `shared/src/commonMain/kotlin/dev/passchef/mobile/recipe/GeneratePassword.kt:19`.
`spice.uppercase()` uses the host runtime's Unicode case table. Its JVM bytecode
calls `java.lang.String.toUpperCase(Locale.ROOT)`; Android's table also depends on
the OS version. The JS oracle and the required JDK17 do not have identical tables.

Concrete valid input: recipe `mix#1!`, seasoning consisting of U+10597 (Vithkuqi
small letter A). Actual extension `GeneratePassword` on the pinned Node runtime
cooks code points `10570,6d,69,78`; JDK17 uppercasing preserves `10597` and therefore
cooks `10597,6d,69,78`. This is an actual password mismatch, despite existing tests.

Evidence: independently invoked the real JS use case; independently ran the same
case through JDK17 `String.toUpperCase(Locale.ROOT)` using jshell; used javap on
the built `GeneratePassword` to confirm the exact platform call.

Required repair: deterministic uppercase behavior matching the JS oracle across
JVM and Android, with regression coverage for newer Unicode mappings and existing
supplementary/isolated-surrogate cases. Preserve modifier order and golden tests.
Do not silently restrict previously valid seasoning characters or call parity proven.

## Initial independent commands and results (before repair)

From this worktree root:

```sh
JAVA_HOME=/home/vantt/dev-tools/jdk-17.0.20+8 \
ANDROID_HOME=/home/vantt/dev-tools/android-sdk \
PATH=/home/vantt/dev-tools/jdk-17.0.20+8/bin:$PATH \
./gradlew --no-daemon clean :shared:jvmTest :shared:assemble
PATH=/home/vantt/projects/passMan/.toolchain/node/bin:$PATH \
pnpm --dir chrome-extension exec vitest run test/unit/generate_password.spec.mjs
```

- Gradle exit0, BUILD SUCCESSFUL in33s;26 tasks,24 executed. Android AAR and JVM JAR built.
- Fresh JVM XML:21 tests,0 failures/errors/skips:12 literal goldens,5 recipe interop,
  3 bundle decrypt/validation tests,1 Argon2 parity test.
- Original Vitest suite:22 passed, including all12 approved cases.
- Original JS source and approved tests compared byte-for-byte to `b097b9c`: unchanged.
- Independently reran the real JS adapter against stored synthetic fixture without
  rewriting files; plaintext round trip passed and both fixture copies are identical.
- Independently recalculated all14 JS password/HMAC vectors: identical to resources.
- Independently reran real `argon2-browser` WASM with stored params: exact reference
  `d2dba0254ed99b7f209639a23f2edc31a49c8c2956883caa67b5b56898c7d40a` matched.
- BouncyCastle1.85 dependency and implementation occur only in `jvmTest`, not production.
- Type/version, byte ranges, IV/salt length, ciphertext tampering and wrong password
  rejection verified; PBKDF2 UTF-8 fixture includes Vietnamese/emoji seasoning/password.
- Fixture generation script calls the real adapter, preserves fixture on rerun, and
  verifies its round trip. Recorded file times place fixture before Kotlin decrypt;
  builder report states original generation preceded implementation. No immutable
  intermediate commit exists, so chronological claim is corroborated, not independently audited.
- `git diff --check` passes. No lint pass claimed: this plugin exposes no `:shared:lint` task.

## Limits and next action

Compile and JVM tests do not prove execution on physical Android hardware. Real
device Argon2 performance and Keystore/biometric acceptance remain the other plan's
scope; no emulator or JVM result is presented as hardware evidence.

The original P2 required repair before integration. The Node-only post-repair
check below did not close it: actual browser evidence invalidated that oracle.
The final browser check now closes the finding. Approved tests stay immutable.

## Repair follow-up — historical author handoff

The reviewer was subsequently assigned to implement this repair and therefore
did not independently approve its own code. The first `JsUppercase.kt` repair used
Node24.21.0/Unicode17.0 mappings for all changed scalars rather than platform casing.
The new regression failed before repair. Repair runs passed25 JVM tests, Android
AAR/JVM JAR builds and all1112064-scalar JS oracle regeneration checks; actual
recipe/tag vectors expanded14→35. Original source/goldens remain unchanged.
See `mobile-core-recovery.md` for provenance and explicit Unicode-version limits.
The original Core builder will perform the independent post-repair verification.

## Historical Node-only independent verification — 2026-10-11

The original Core builder reviewed the uppercase repair authored by the first
reviewer. This pass made no product source/test edits and did not regenerate
fixtures. The verdict is limited to the reviewed repair and its pinned JS baseline.

```sh
export PATH=/home/vantt/projects/passMan/.toolchain/node/bin:$PATH
node chrome-extension/scripts/unicode-uppercase-table.mjs --check
pnpm --dir chrome-extension exec vitest run test/unit/generate_password.spec.mjs
JAVA_HOME=/home/vantt/dev-tools/jdk-17.0.20+8 \
ANDROID_HOME=/home/vantt/dev-tools/android-sdk \
./gradlew --no-daemon --console=plain clean :shared:jvmTest :shared:assemble
git diff --check
```

- Oracle check exit 0: actual Node **v24.21.0**, Unicode **17.0**, ICU **78.3**;
  all **1,112,064 scalars** enumerated with real `String.toUpperCase()`, **1,580**
  changed mappings. Both production table and fixture match complete regeneration.
- Separately invoked the actual extension recipe engine and HMAC tag codec for
  every stored row, without writes: **35/35 password and tag vectors** match.
  Vithkuqi U+10597→U+10570 and Garay U+10D70→U+10D50 also checked directly.
- Clean Gradle exit 0, **BUILD SUCCESSFUL in 24s**, 26 tasks/24 executed; rebuilt
  JVM JAR and Android AAR. Fresh five JUnit XML reports: **25 tests**, zero
  failures/errors/skips (12 goldens,5 interop,4 Unicode,3 bundle,1 Argon2).
- Original JS suite exit 0: **22/22**, including all approved assertions.
  Extension source, original golden test and lockfile have no diff from `b097b9c`.
- `JsUppercase.kt` combines only valid high/low surrogate pairs, maps full
  scalars including one-to-many expansions, and preserves isolated surrogates.
  Common implementation avoids Android/JDK case tables. Cooking retains UTF-16
  code-unit reversal before uppercase; dedicated regressions cover that order.
- Generator pins actual Node/Unicode and refuses unexpected runtimes. Hex-only
  generated chunks decode deterministically; absent mappings preserve input.
  All-scalar JVM test checks the scanner as well as table contents. Production
  source/dependencies contain no BouncyCastle; it remains a test-only spike.
- Whitespace exit 0. Three compiler warnings about test Json instance creation
  are informational; no production warning or lint pass is claimed.

**Historical verdict: Node verified, browser P2 still open at that point.** Root's
actual Chromium comparison found 28 mismatches and withdrew the resolved claim.
This required browser-derived expectations; final evidence below supersedes that
baseline. No source, golden test, workflow state, commit or merge changed by this pass.

## Browser-derived repair — author handoff

The repair author replaced the Node oracle with the actual repo E2E Chromium
143.0.7499.4, launched headed with an isolated temporary profile. Generated data
and fixtures now contain1552 changed mappings; fullscalar browser `--check`
passes1112064 inputs, checksum
`c8ae1f25f65551713e9dff9b77f723a4507171e584dc9f56a721215f5b4ae90a`.
Recipe oracle imports the unchanged real cooking/parser/tag modules in a loaded
extension page. Its56 browser-generated dish/tag vectors include U+A7CF/U+16EC0
identity, Vithkuqi/Garay, Greek expansions, U+0345/combining-accent sequences and
isolated UTF-16 surrogates, with reverse-before-uppercase coverage. Recipe
`--check` passes without writes. Golden source/tests remain immutable.

Author Gradle run passed in25s:26 JVM tests,0 failures/errors/skips; Android AAR
and JVM JAR rebuilt. Original JS suite22/22; diff whitespace check green. The author
requested the separate independent pass recorded next.
The historical Node-only check above is superseded for browser parity, not erased.
No commit, merge or Bee state change made.

## Final independent browser repair verification — 2026-10-11

The original Core builder independently reviewed the other agent's frozen repair.
Only this report was edited; no oracle was regenerated. From this checkout:

```sh
export PATH=/home/vantt/projects/passMan/.toolchain/node/bin:$PATH
DISPLAY=:0 node chrome-extension/scripts/unicode-uppercase-table.mjs --check
DISPLAY=:0 node chrome-extension/scripts/recipe-reference-vectors.mjs --check
pnpm --dir chrome-extension exec vitest run test/unit/generate_password.spec.mjs
JAVA_HOME=/home/vantt/dev-tools/jdk-17.0.20+8 \
ANDROID_HOME=/home/vantt/dev-tools/android-sdk \
./gradlew --no-daemon --console=plain clean :shared:jvmTest :shared:assemble
git diff --check
```

- Both browser checks exit 0. Exact **Chromium 143.0.7499.4**; **1,552 mappings**,
  **1,112,064 scalars**, checksum
  `c8ae1f25f65551713e9dff9b77f723a4507171e584dc9f56a721215f5b4ae90a`.
  **56/56 dish/tag vectors** execute the unchanged extension modules inside the
  loaded extension page, not Node. Kotlin tests consume those browser expectations.
- Separate read-only actual-browser probe exit 0: nine critical sequences and
  all **2,048 isolated surrogate code units** agree with the table. Confirmed
  U+A7CF/U+16EC0 identity, Vithkuqi/Garay changes, U+0345 with combining accents,
  Greek/special expansions and literal `mix#1!?` reverse-before-uppercase dish.
- Clean Gradle exit 0, **BUILD SUCCESSFUL in 25s**, 26 tasks/24 executed; rebuilt
  Android AAR/JVM JAR. Fresh five XML reports: **26 tests**, zero failures/errors/
  skips (12 golden,5 interop,5 Unicode,3 bundle,1 Argon2).
- Original JS suite exit 0, **22/22**. Original extension source, golden test and
  lockfile have no diff against `b097b9c`; production source has no platform
  uppercase or BouncyCastle implementation. BC remains only a `jvmTest` dependency.
- Reviewed common scalar scanner/expansion table and helper cleanup/version guard.
  Helper uses an isolated transient profile, closes its context in `finally`,
  removes that profile, and rejects a changed browser version before evaluation.
  Golden inputs remain immutable. Whitespace exit 0; no lint pass claimed.

**Final independent verdict: P2 resolved; no remaining P1/P2 in accepted scope.**
Browser-version refresh needs explicit oracle regeneration and independent review.
Physical-device Keystore/biometrics remain pending in their separate plan; these
build/tests do not certify hardware behavior. No unresolved Core scope question.

## Integration follow-up (2026-10-11)

Core `7d8808c` integrated into main; independent fresh-main JVM/build and
actual-browser parity checks passed. Complete archive verified; checkout closed.
Branch and recovery refs retained; no remote push.
