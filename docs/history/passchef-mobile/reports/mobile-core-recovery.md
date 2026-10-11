# Mobile Core recovery — 2026-10-11

Status: implementation and final browser-derived repair passed independent
review; commit, merge and worktree closure pending. Base at start: main `b097b9c`.

## Delivered

- One root Gradle build, `:shared` in `shared/`, Android+JVM targets and explicit
  shared JVM/Android crypto source set. It matches Keystore's existing module
  layout; standalone `mobile/sheets-client/` is unchanged.
- Pure recipe parser/cooking, caller-supplied seasoning/tag ports, HMAC verification,
  profile/context mismatch refusal and legacy warning. UTF-16 transformations
  and Web TextEncoder's isolated-surrogate handling match the JS oracle.
- Full-access v1.0 PBKDF2-SHA256/AES-GCM bundle decrypt with type/version/byte-field
  validation, tamper/wrong-password refusal and temporary byte-buffer cleanup.
- Test-only Argon2id backend proof; BouncyCastle is a `jvmTest` dependency only.
  No production Argon2 wrapper or provider registration was introduced.

## Initial verification before Unicode repairs

`JAVA_HOME=/home/vantt/dev-tools/jdk-17.0.20+8`
`ANDROID_HOME=/home/vantt/dev-tools/android-sdk`

- `./gradlew :shared:jvmTest :shared:assemble --no-daemon`: **passed**. JVM JAR
  and Android AAR compiled; 21 tests, zero failures/errors/skips. Breakdown:
  12 literal approved recipe assertions,5 recipe/JS-oracle integration tests,
  3 bundle tests,1 Argon2 test. The integration test consumes14 real JS vectors.
- `pnpm --dir chrome-extension exec vitest run test/unit/generate_password.spec.mjs`:
  **22 passed**, including the original12 approved assertions; originals unchanged.
- `node chrome-extension/scripts/generate-mobile-fixture.mjs`: **passed**.
  The real `ExportImportAdapter` created the synthetic fixture and verified its
  own decrypt round trip BEFORE Kotlin decrypt implementation. It preserves the
  stored fixture on rerun and synchronizes the identical test-resource copy.
- `node chrome-extension/scripts/recipe-reference-vectors.mjs`: **passed**;
 14 independent vectors include Vietnamese,emoji and an isolated UTF-16 surrogate.
- `node chrome-extension/scripts/argon2-reference-vector.mjs`: **passed**.
  The installed extension dependency `argon2-browser`1.18.0 provides the oracle;
  the temporary Node fetch workaround is restored when the script finishes.
- `git diff --check`: **passed**. Kotlin source files are each under200 lines.
  No extension source/approved test, unrelated tooling, or lockfile change made.
- `./gradlew :shared:lint`: unavailable (exit1: no `lint` task in this Android
  KMP module). Compile, tests and whitespace checks pass; no lint pass is claimed.

An initial test compile failed because the fixture test lacked the serialization
extension import; corrected before the passing run. Two Kotlin compiler warnings
about per-test Json construction remain informational; no production warning.

## Argon2 proof

Synthetic password: `SYNTHETIC_ARGON2_VECTOR_🍳`.
Synthetic salt: `SYNTHETIC_SALT16`.
Argon2id v0x13,32 bytes,time3,memory65536KiB,parallelism4; UTF-8,empty secret/AD.
Real WASM result and BouncyCastle1.85 result both:
`d2dba0254ed99b7f209639a23f2edc31a49c8c2956883caa67b5b56898c7d40a`.
Verdict: **YES — byte-for-byte parity**. These values are synthetic test inputs,
not hardcoded production defaults.

## Integration and limits

### Unicode uppercase parity repair

Independent review found a real mismatch: recipe `mix#1!` with U+10597 seasoning
produced U+10570 in the real JS oracle but remained U+10597 on JDK17. The added
regression failed before repair (one test, one failure); original golden tests were untouched.

Cooking now uses a common Kotlin UTF-16 scanner and a deterministic generated table
independent of Android/JDK case tables. The compatibility baseline is the repo's
actual headed Playwright Chromium **143.0.7499.4**, not Node's Unicode table.
A first Node24.21.0 repair produced1580 mappings; actual browser evidence found
28 differences and invalidated that oracle choice. No Unicode-version inference
is needed: the exact browser engine and mapping checksum identify this baseline.

`chrome-extension/scripts/chromium-oracle.mjs` launches the installed E2E browser
using a temporary profile, `headless:false`, `DISPLAY=:0`, and refuses any browser
version other than143.0.7499.4. Profiles are closed and removed after each run.
For recipe vectors it loads the unchanged extension and imports its actual
GeneratePassword, RegexParserAdapter and tag codec into the extension page.
It does not read an existing browser profile or use real Cookbook credentials.

`unicode-uppercase-table.mjs --write` enumerates all1112064 Unicode scalar inputs
with real browser `String.toUpperCase()` and records1552 changed mappings,
including expansions/supplementary scripts. Table checksum (ordered hex rows):
`c8ae1f25f65551713e9dff9b77f723a4507171e584dc9f56a721215f5b4ae90a`.
Generated common data is88 lines; the handwritten scanner is34 lines and preserves
isolated surrogate code units. U+10597 maps to U+10570; U+A7CF and U+16EC0 stay
unchanged in this browser, despite Node uppercasing them.

Both scripts support `--check` without rewriting expectations. From worktree root:

```sh
export PATH=/home/vantt/projects/passMan/.toolchain/node/bin:$PATH
DISPLAY=:0 node chrome-extension/scripts/unicode-uppercase-table.mjs --check
DISPLAY=:0 node chrome-extension/scripts/recipe-reference-vectors.mjs --check
```

The browser recipe/tag oracle contains56 vectors: Vietnamese,emoji,Vithkuqi,Garay,
default special casing, browser-unknown letters, isolated surrogates, Greek
expansions and U+0345 with surrounding combining accents. Complete recipe inputs
verify context and UTF-16 reverse-before-uppercase behavior, beyond scalar checks.
ECMAScript uses locale-insensitive default conversion; see the [primary specification](https://tc39.es/ecma262/multipage/text-processing.html#sec-string.prototype.touppercase).
Node remains only the launcher and the unchanged standard-crypto/Argon2 fixture
runtime. Refreshing Chromium requires explicit regeneration and independent review;
this does not guarantee identical casing in all older/future Chrome versions.

Browser repair verification: Gradle `:shared:jvmTest :shared:assemble` passed in25s;
26 JVM tests,0 failures/errors/skips; JVM JAR and Android AAR rebuilt. Original
JS suite passed22 tests. Both actualbrowser oracle checks and whitespace checks
pass. A separate reviewer repeated the actual browser oracles, clean build and
golden tests successfully; the P2 is closed in the independent review report.
The repair's author does not self-approve the earlier independent finding.

Root layout intentionally adapts the older `mobile/shared/` file projection;
CONTEXT grants naming/layout discretion. Locked historical plans remain unchanged.
The common cooking class cannot depend on downstream JVM/Android classes, so its
minimal tag-verifier port preserves source-set boundaries without duplicated crypto.

Only the original full-access bundle is supported; QR compact decoding/reassembly
remains separate from this accepted slice. Keystore/biometric behavior, real-device
performance, iOS, persistence and full mobile screens are not implemented here.
Managed immutable strings are not guaranteed erased by byte-buffer cleanup.

Next: integration by the orchestrator after passing independent review, while
preserving deferred tooling WIP. No commit, merge, remote push or worktree closure
performed by this builder. Dependent Keystore work follows Core completion;
QR transport remains independent. No unresolved Core scope question.
