# Complete Track B: Mobile Core

Date: 2026-10-11. Worktree: `passMan--wt--passchef-mobile`.
Finish this existing plan, verify/review it, then merge and close this checkout.
Other remaining worktrees follow afterward; workflow-tooling work stays deferred.

## Accepted outcome and layout

Deliver Android+JVM KMP recipe parity, full-access bundle decrypt, and a proven
Argon2id parity spike. No UI, OAuth, Keystore implementation, iOS, or QR reader.
Use one repo-root Gradle build with `:shared` in `shared/`, matching the existing
Keystore scaffold so its later `:app` can consume the same module. This adapts
historical `mobile/shared/` paths under CONTEXT's directory-naming discretion;
locked product decisions stay unchanged. Keep `mobile/sheets-client/` independent.

Pin the proven Keystore versions: Gradle 9.1.0, AGP 9.0.0, Kotlin 2.4.10, JDK17,
compileSdk36/minSdk26; BouncyCastle `bcprov-jdk18on:1.85` stays test-only.
Use the Android KMP library plugin and `jvm()`, with explicit `jvmAndroidMain`
shared by Android/JVM. Disable automatic hierarchy when adding explicit edges.
Common recipe code uses an injected tag-verifier port; it cannot import the
downstream JVM/Android HMAC implementation directly. No production Argon2 wrapper.

## Implementation order and files

1. Preserve existing WIP; merge current main into this existing worktree first.
   Add root `settings.gradle.kts`, `build.gradle.kts`, `gradle.properties`, wrapper
   files, `.gitignore` rules, and `shared/build.gradle.kts`; no tracked machine-local
   properties. Match Keystore namespace `dev.passchef.mobile.shared` for later reuse.
2. Add common `Recipe`, `RecipeParser`, `GeneratePassword`, `Base32`, and minimal
   seasoning-provider/tag-verifier ports under `shared/src/commonMain/kotlin/`.
   Add `TagCodec` under `shared/src/jvmAndroidMain/kotlin/`. Preserve ASCII grammar,
   indices1–5, modifier deduplication/order, tag alphabet/canonicalization, mismatch
   rejection, and legacy warning. Preserve JS UTF-16 semantics for reverse/interleave.
3. Port all 12 approved cases into `shared/src/jvmTest/kotlin/` and add JS-oracle
   checks for tags, Unicode seasoning, malformed/null recipes, and missing seasoning.
   Existing extension source and approved tests remain immutable.
4. BEFORE decrypt implementation: create `chrome-extension/scripts/generate-mobile-fixture.mjs`
   importing the real `ExportImportAdapter` class and calling its instance methods.
   Write `chrome-extension/test/fixtures/full-access-bundle.fixture.json` using
   obviously synthetic profile/spices/sharingPassword plus `_warning` marker;
   prove the JS decrypt round trip. Subsequent verification reuses this fixture.
5. Implement `BundleDecryptor` in `shared/src/jvmAndroidMain/kotlin/`, plus DTOs
   and tests/resources under `shared/`. Match PBKDF2-SHA256/100000/AES256-GCM,
   16-byte salt/12-byte IV; reject unsupported type/version/malformed fields,
   wrong sharing password, and tampering. Assert synthetic plaintext; never log it.
6. Add `chrome-extension/scripts/argon2-reference-vector.mjs` using installed
   `argon2-browser`1.18.0 with the proven Node fetch workaround. Compare BC direct
   classes in `jvmTest` only: Argon2id v0x13,32 bytes,t3,m65536KiB,p4,UTF-8,
   empty secret/AD. Record vector/hash/verdict in versioned product reports.

## Commands and evidence

From this worktree root, set:
```sh
export PATH="/home/vantt/projects/passMan/.toolchain/node/bin:$PATH"
export JAVA_HOME=/home/vantt/dev-tools/jdk-17.0.20+8
export ANDROID_HOME=/home/vantt/dev-tools/android-sdk
export PATH="$JAVA_HOME/bin:$PATH"
pnpm --dir chrome-extension install --frozen-lockfile
node chrome-extension/scripts/generate-mobile-fixture.mjs
node chrome-extension/scripts/argon2-reference-vector.mjs
./gradlew :shared:jvmTest :shared:assemble --no-daemon
pnpm --dir chrome-extension exec vitest run test/unit/generate_password.spec.mjs
git diff --check
```
Keep focused source files under200 lines. Independently review, repair findings,
update product docs with observed evidence, and commit only completed product work.
Merge into main only after this complete slice passes; preserve/restore main WIP,
archive the checkout's recoverable contents, then close it. Leave live Android
biometric/hardware acceptance to the separate Keystore plan.

Feasibility proven outside repo in `/tmp/passchef-core-feasibility-03uvfl_6`:
Android AAR+JVM compile/test succeeded, BC resolves on both targets, and BC matches
the real WASM Unicode test vector. Real JS bundle round trip also passed. Current
Core complete: `7d8808c` integrated, fresh main checks passed, checkout archived
and closed. No scope answer needed.
