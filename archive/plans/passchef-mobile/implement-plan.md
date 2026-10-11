---
artifact_contract: bee-implement-plan/v1
feature: passchef-mobile
lane: high-risk
status: Done
accepted_scope: Android/JVM Core
historical_status: Approved
updated: 2026-07-28
sources: [archive/plans/passchef-mobile/CONTEXT.md, archive/plans/passchef-mobile/approach.md, plan.md]
decisions: [D1, D2, D3, D4, D5, D6, D7, D8]
---

# Implementation Plan: Track B — Mobile Core (KMP, no OAuth)

**Status audit — 2026-10-11: Done.** All five implementation steps completed
in root `shared/` (superseding the projected `mobile/shared/` layout), independently
verified and integrated at `7d8808c`. See [completion evidence](reports/mobile-core-recovery.md)
and [audited epic scope](plan.md). Original narrative below describes the planning
baseline; Native/iOS and phone UI/QR reader remain outside this accepted slice.

> Human-layer projection of the truth artifacts. Truth lives in CONTEXT.md
> (decisions), plan.md + cells (work), and the validating report (evidence).
> Feedback on this document flows back to those artifacts, then this re-renders.

## 1. Goal

Give the future mobile app a working, on-device password-generation engine and a way to read what the Chrome extension exports — so a phone can cook the exact same password as the browser, and can unlock a full-access bundle handed to it via QR — without touching Sheets, OAuth, or any UI screen yet.

**Success looks like**
- The KMP core generates byte-identical passwords to the extension for any recipe/secret pair (D8).
- The KMP core decrypts a real `createFullAccessBundle` bundle produced by the extension (D6).
- The Argon2id implementation chosen for the later unlock-key work is proven correct against the real `argon2-browser` package the extension depends on (D7).

## 2. Current State

The repo has no mobile code today — no Gradle project, no Kotlin directories anywhere (verified by directory scan). All the logic this slice ports lives in the Chrome extension: `regex_parser_adapter.js`, `tag-codec.js`, and `generate_password.js` compute passwords from a recipe string and a secret via pure string/regex operations (no crypto). Separately, `export-import-adapter.js` encrypts/decrypts a "full-access bundle" with PBKDF2 (100000 iterations) + AES-GCM, and `argon2_adapter.js` derives an unlock key with Argon2id — a completely separate code path from password generation.

## 3. Scope

**In scope**
- Bootstrapping a KMP shared module, Android + JVM targets only (D5).
- Porting the recipe engine (tag-codec, parser, generate_password) with a byte-for-byte acceptance bar against the extension's 12 golden tests (D8).
- Implementing bundle decrypt (PBKDF2 + AES-GCM) proven against a real extension-produced fixture (D6).
- A spike proving BouncyCastle's Argon2id output matches argon2-browser's WASM output for a fixed test vector (D7).

**Out of scope**
- Sheets API, OAuth, Google Cloud project setup (D2-adjacent, separate backlog items).
- Any UI screen — onboarding, connect-sheet, add-account, unlock (D4-adjacent; prototype screens already exist separately in `docs/designs/`).
- Android Keystore storage / biometric unlock (p-8b12c072) — this slice only proves the Argon2 primitive works, not where the derived key is stored.
- iOS / Kotlin-Native target and its Argon2 backend (deferred per D1; tracked as PBI p-23837c9e).
- Drive appData transport (p-e117d9aa) — QR/`createFullAccessBundle` only, per D3.

## 4. Proposed Approach

Stand up one KMP module and build three pieces on it: the recipe engine (pure logic, no crypto), the bundle decrypt (PBKDF2+AES-GCM via `javax.crypto`, identical on JVM and Android), and the Argon2id spike (BouncyCastle's `Argon2BytesGenerator`, called directly as a class rather than registered as a JCA provider).

**Why this approach** — `javax.crypto` is built into both target runtimes with zero extra dependency and known-identical behavior; BouncyCastle is the only pure-Java Argon2id implementation mature enough to trust, and calling it directly (never via `Security.addProvider`) sidesteps Android's well-known JCA-provider registration conflict.
**Alternatives considered** — `argon2kt` (Android-only, no JVM target — breaks the spike's own jvmTest requirement); `ionspin/kotlin-multiplatform-crypto` (genuinely cross-platform including Kotlin/Native, but too thin/immature to justify displacing BouncyCastle for a target — iOS — that's already deferred).

## 5. Technical Design

```text
recipe string + secret --(RecipeParser.parse)--> Recipe
Recipe + secret --(GeneratePassword.execute -> _combine/_interleave)--> password (matches D8's golden tests byte-for-byte)

extension createFullAccessBundle() --(QR, D3, out of scope this slice)--> bundle JSON
bundle JSON + sharingPassword --(PBKDF2-SHA256, 100000 iter, javax.crypto)--> AES key
AES key + bundle.iv + bundle.encryptedData --(AES-GCM decrypt)--> plaintext secrets JSON (matches D6's real fixture)

fixed (password, salt) test vector --(BouncyCastle Argon2BytesGenerator, Argon2id v0x13, t=3/m=65536KiB/p=4/hashLen=32, jvmTest-only)--> KMP hash
same test vector --(the real argon2-browser npm package, plain Node, same params)--> reference hash
KMP hash == reference hash  (D7's pass criterion)
```

Data flows through one new intermediate Kotlin source set, `jvmAndroidMain` (validating finding): `androidTarget()+jvm()` gives no automatic shared-JVM source set, so `TagCodec.kt` (HMAC-SHA256) and `BundleDecryptor.kt` (PBKDF2+AES-GCM) — both needing `javax.crypto`, invisible from `commonMain` — live there as one plain implementation shared by both real targets, no `expect`/`actual` split needed.

**Data model** — No persistent storage in this slice. The decrypted bundle's plaintext secrets are held in memory only, for the duration of a test/verification run; where they'd actually live long-term (Keystore) is p-8b12c072, out of scope here.

**Security / Permissions** *(mandatory, high-risk)* — This slice handles vault-shaped secrets in two places, and neither ever touches a REAL user's data (validating hardened this after finding the original cell wording didn't require it): (1) the D6 fixture (cell `passchef-mobile-3`) is a real encrypted bundle, but its inputs — profile name, secrets, sharing password — must be obviously synthetic placeholder values, carry a `"_warning": "SYNTHETIC TEST DATA ONLY"` field, and are prohibited from resembling real data; (2) the bundle-decrypt test (cell `passchef-mobile-4`) asserts the decrypted plaintext via one direct `assertEquals` only — never a separate print/log statement — so a test failure can't leak the value into build logs; (3) the Argon2 spike (cell `passchef-mobile-5`) only ever touches a synthetic fixed test vector, is prohibited from that vector ever becoming a hardcoded production default, and its BouncyCastle wrapper is confined to the `jvmTest` source set only — it can never become a shipped production API by construction. All three cells additionally prohibit any modification to `chrome-extension/src/**` (read-only oracle). Neither path exposes any new attack surface beyond what already exists in the extension (this slice reimplements existing crypto, it does not design new crypto). Memory hygiene (zeroing key material after use) is explicitly **not** solved in this slice — flagged as an Open Question below, to be addressed when the Keystore slice (p-8b12c072) actually holds long-lived key material.

## 6. Affected Files

Projected from cells `passchef-mobile-1..5` (authoritative — current-slice cells created after Gate 2):

| Action | File / Component | Purpose |
|--------|------------------|---------|
| Create | `mobile/settings.gradle.kts`, `mobile/build.gradle.kts`, `mobile/shared/build.gradle.kts` | KMP module scaffold, android+jvm targets, BouncyCastle dependency, `jvmAndroidMain` intermediate source set (`passchef-mobile-1`) |
| Create | `mobile/shared/src/commonMain/kotlin/*/recipe/RecipeParser.kt`, `GeneratePassword.kt`, `Base32.kt` | Pure-logic recipe engine port (`passchef-mobile-2`) |
| Create | `mobile/shared/src/jvmAndroidMain/kotlin/*/recipe/TagCodec.kt` | HMAC-SHA256 tag computation, needs `javax.crypto` (`passchef-mobile-2`) |
| Create | `mobile/shared/src/jvmTest/kotlin/*/recipe/GeneratePasswordGoldenTest.kt` | The 12 ported golden tests (`passchef-mobile-2`) |
| Create | `chrome-extension/scripts/generate-mobile-fixture.mjs` | Script calling the real `ExportImportAdapter.createFullAccessBundle` method to produce the fixture, synthetic inputs only (`passchef-mobile-3`) |
| Create | `chrome-extension/test/fixtures/full-access-bundle.fixture.json` | Real bundle + known **synthetic** plaintext + sharingPassword + `_warning` marker (`passchef-mobile-3`) |
| Create | `mobile/shared/src/jvmAndroidMain/kotlin/*/crypto/BundleDecryptor.kt` | PBKDF2+AES-GCM decrypt, plain implementation, no expect/actual (`passchef-mobile-4`) |
| Create | `mobile/shared/src/jvmTest/kotlin/*/crypto/BundleDecryptorTest.kt`, `mobile/shared/src/jvmTest/resources/full-access-bundle.fixture.json` | Decrypt test against the real fixture, single `assertEquals`, no plaintext logging (`passchef-mobile-4`) |
| Create | `chrome-extension/scripts/argon2-reference-vector.mjs` | Plain-Node reference hash via the real `argon2-browser` package (`delete globalThis.fetch` workaround) (`passchef-mobile-5`) |
| Create | `.bee/spikes/passchef-mobile/argon2-parity/*`, `mobile/shared/src/jvmTest/kotlin/*/crypto/Argon2ParityTest.kt` | Argon2 parity spike — BouncyCastle wrapper confined to `jvmTest`, never a production source set (`passchef-mobile-5`) |

## 7. Implementation Steps

Projected from the 5 current-slice cells (titles + deps, authoritative):

- [x] Bootstrap KMP shared module (android+jvm) (`passchef-mobile-1`)
- [x] Port recipe engine (tag-codec, parser, generate_password) with golden-test parity (`passchef-mobile-2`) — deps: `passchef-mobile-1`
- [x] Generate real extension-produced bundle fixture for decrypt verification (`passchef-mobile-3`) — deps: `passchef-mobile-1`
- [x] Implement PBKDF2+AES-GCM bundle decrypt, verified against the real fixture (`passchef-mobile-4`) — deps: `passchef-mobile-1`, `passchef-mobile-3`
- [x] Spike: Argon2id KMP (BouncyCastle) parity with argon2-browser WASM (`passchef-mobile-5`) — deps: `passchef-mobile-1`

Cells 2, 3, and 5 have no dependency on each other and can proceed in parallel once cell 1 caps; cell 4 waits on both 1 and 3.

## 8. Validation Plan

**Automated** — `cd mobile && ./gradlew :shared:jvmTest` → expected: all 12 ported golden tests pass (`passchef-mobile-2`), the fixture decrypt test passes (`passchef-mobile-4`), the Argon2 parity test passes (`passchef-mobile-5`). `node chrome-extension/scripts/generate-mobile-fixture.mjs` (`passchef-mobile-3`) and `node chrome-extension/scripts/argon2-reference-vector.mjs` (`passchef-mobile-5`, Node half) are proven runnable NOW (see Evidence).
**Manual** — [ ] Install a JDK + Android SDK toolchain in the execution environment (this validating pass ran in a sandbox with none — see Risks below) before any Gradle-based cell (1, 2, 4, 5's Kotlin half) can actually cap.
**Evidence** — `archive/plans/passchef-mobile/reports/validation-current-slice.md` (this validating pass): confirmed via real execution — `ExportImportAdapter.createFullAccessBundle`/`decryptBundle` round-trip under plain Node v24 (cell 3's approach); `argon2-browser` v1.18.0 produces a real 32-byte Argon2id hash under plain Node once `globalThis.fetch` is deleted first (cell 5's Node half); Maven Central reachable (BouncyCastle resolvable in principle); no JDK/Gradle/Android SDK present in this sandbox (cells' Kotlin-side verify cannot run here).

## 9. Risks & Mitigation

Projected from `approach.md`'s risk map, updated with validating's findings:

| Risk | Impact | Mitigation |
|------|--------|------------|
| **No JDK/Gradle/Android SDK toolchain in this execution environment** | **Blocks all Kotlin-side verify (cells 1, 2, 4, 5)** | Not fixable by editing cells/plan — needs a human decision: install a JDK+Android SDK here (requires sudo, not available password-less to the agent), or execute these cells in an environment that already has one |
| PBKDF2+AES-GCM wire-format mismatch (wrong IV/salt length, GCM tag handling) | Medium | Verify against a REAL extension-produced fixture (D6), never a hand-authored one; fixture generation proven runnable now |
| Argon2id cross-implementation mismatch (version/encoding/salt) | Medium-High | Spike pins every param explicitly (D7); Node-side reference proven runnable now against the real `argon2-browser` package |
| BouncyCastle / Android JCA provider conflict | Low | Call `Argon2BytesGenerator` directly as a class, never via `Security.addProvider` |
| `commonMain` cannot see `javax.crypto`/BouncyCastle on an android+jvm-only KMP module | Was a design gap, now fixed | `jvmAndroidMain` intermediate source set (cell `passchef-mobile-1`), no expect/actual needed |
| A checked-in fixture (cell 3) could resemble real vault data | Medium (secrets-repo blast radius) | Cell now requires obviously-synthetic inputs + a `_warning` marker, never real user data |

## 10. Rollback Plan

**Corrected during validating** (the original version of this section incorrectly scoped the change to `mobile/` only). This slice creates new, additive files in two places, neither deployed nor user-facing yet:
- The entire new `mobile/` directory (Kotlin/Gradle scaffold + all ported/implemented code).
- Inside the existing `chrome-extension/` tree: `chrome-extension/scripts/generate-mobile-fixture.mjs`, `chrome-extension/scripts/argon2-reference-vector.mjs`, and `chrome-extension/test/fixtures/full-access-bundle.fixture.json` — this last one is a **checked-in fixture containing a (synthetic) password + encrypted bundle**, and a second copy of it lives at `mobile/shared/src/jvmTest/resources/full-access-bundle.fixture.json`.

Rollback is a plain `git revert` of the cells' commits (or, before any commit, deleting `mobile/` plus the three `chrome-extension/` additions above — **both fixture copies**, not just one). No existing `chrome-extension/src/**` file is ever modified by any cell (each cell explicitly prohibits it), so nothing there needs reverting. No data migration, no flag, no other subsystem is touched.

## 11. Open Questions

- Exact path/mechanism for checking in the D6 fixture — **resolved**: `chrome-extension/test/fixtures/full-access-bundle.fixture.json`, generated by `chrome-extension/scripts/generate-mobile-fixture.mjs`, synthetic inputs only.
- The fixed Argon2 test vector (password + salt) — still authored fresh at execution time (no existing suite to draw one from); the Node-side generation mechanism around it is now proven (real `argon2-browser` + `delete globalThis.fetch`).
- Memory hygiene for decrypted secrets / derived keys (zeroing after use) is not addressed in this slice — deferred to the Keystore slice (p-8b12c072), noted here so it is not silently forgotten.
- **New, blocking:** this sandbox has no JDK/Gradle/Android SDK — needs a human decision (install here vs. execute elsewhere) before cells 1/2/4/5's Kotlin-side verify can run. Cell 3 and cell 5's Node-side half are unaffected and proven runnable now.
