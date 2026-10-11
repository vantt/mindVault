# Validation Report — Track B: Mobile Core (KMP, no OAuth)

Feature: passchef-mobile · Lane: high-risk · Slice: cells `passchef-mobile-1..5`

## Reality Gate Report

```text
REALITY GATE REPORT
Mode: high-risk
Current work: bootstrap a KMP shared module and port the recipe engine, bundle decrypt, and an Argon2id spike (3 backlog PBIs)
MODE FIT: PASS       — audit/security (crypto/secrets) + cross-platform flags; high-risk is correct, not overkill
REPO FIT: PASS       — all cited extension files/lines verified to exist (regex_parser_adapter.js, tag-codec.js, generate_password.js, export-import-adapter.js, argon2_adapter.js, generate_password.spec.mjs); confirmed 12 (not 13) @approved golden tests
ASSUMPTIONS: PASS    — every blocking assumption is in the feasibility matrix below
SMALLER PATH: FAIL->N/A — no smaller path delivers 3 already-scoped PBIs the user named directly; lane/ceremony match the crypto hard-gate, not oversized
PROOF SURFACE: FAIL (partial) — cell 3's verify and cell 5's Node-half run today; cells 1/2/4/5's Gradle-based verify commands cannot run in THIS sandbox (no JDK/Gradle/Android SDK) — see feasibility matrix
Decision: proceed with constraints — cell content is now sound after repair; the open gap is environmental, not a plan/cell defect
Evidence: see Feasibility Matrix below
```

## Feasibility Matrix

| Assumption | Risk | Proof Required | Evidence | Result |
|---|---|---|---|---|
| No pre-existing gradle/kotlin scaffold to collide with | Low | Repo-wide file scan | `find . -maxdepth 4 -iname "*.gradle*" -o -iname gradlew` → no hits (checked twice, independently) | **PASS** |
| `ExportImportAdapter.createFullAccessBundle`/`decryptBundle` are runnable standalone under plain Node (cell 3) | Medium | Actual execution | Ran `new ExportImportAdapter().createFullAccessBundle('p',{s:'x'},'sheet1','pw123')` then `.decryptBundle(...)` under Node v24.18.0 — round-tripped to `{"s":"x"}` | **PASS** |
| BouncyCastle (`bcprov-jdk18on`) is reachable for a future Gradle build | Low | Network probe | `curl -sI https://repo1.maven.org/maven2/org/bouncycastle/bcprov-jdk18on/maven-metadata.xml` → HTTP 200, latest release 1.85 | **PASS** |
| The extension's own WASM wrapper (`src/wasm/wrapper.js`) can serve as the Node-side Argon2 reference oracle | Medium | Actual execution attempt | Static analysis + the sibling jsdom idea both show it hits browser-only globals (`self`, `__dirname` under ENVIRONMENT_IS_NODE in an ESM context) — **rejected**, never attempted to force further | **FAIL → redesigned** (see below) |
| The real `argon2-browser` npm package can serve as the Node-side Argon2 reference oracle instead | Medium | Actual execution | First attempt (`import`/`require`, no workaround): `TypeError: Failed to parse URL from .../dist/argon2.wasm` — root cause: 2021-era Emscripten glue feature-detects `typeof fetch==='function'`, and Node 18+ ships global `fetch`, so it wrongly takes a browser fetch-path. Second attempt with `delete globalThis.fetch` before `require`: produced `HEX: 6efcf2c18cd36e16d5c1df765fc61a2e158a8169400ca5dbc97c1042c799bad7` (real 32-byte Argon2id hash, correct length) | **PASS (with workaround, now written into cell 5)** |
| A JDK + Gradle + Android SDK toolchain exists to run cells 1/2/4/5's `./gradlew` verify | **High** | Command probe | `java -version` → `command not found`; `which gradle` → empty; `/usr/lib/jvm` absent; `ANDROID_HOME`/`ANDROID_SDK_ROOT`/`JAVA_HOME` unset; `apt`/`apt-get` present but `sudo -n true` → password required (agent has no passwordless install path) | **FAIL — unresolved, escalated to the user (see Gate 3 message)** |
| KMP `commonMain` can host `javax.crypto`/BouncyCastle code directly on an `androidTarget()+jvm()`-only module | Medium | Kotlin Multiplatform architecture fact | `commonMain` has no visibility into JVM-only APIs without an explicit intermediate source set or `expect`/`actual`; confirmed via KMP source-set model, not guessed | **FAIL → redesigned**: cell 1 now creates a `jvmAndroidMain` intermediate source set; cells 2/4/5 moved their JVM-API-using code there |
| Cell dependency graph is acyclic and matches the plan's stated parallelism | Low | `bee.mjs cells schedule` | `Wave 1: passchef-mobile-1` / `Wave 2: -2, -3, -5` / `Wave 3: -4` — matches `plan.md`'s Epic 0 → {1,2a,3} → 2b structure | **PASS** |

## Plan-Checker (high-risk persona panel: coherence, feasibility, security, + cold-pickup cell review)

Four independent reviewers dispatched in parallel, each with fresh context (no shared session history), against `CONTEXT.md`/`plan.md`/`approach.md`/`implement-plan.md` and the 5 live cells.

**Iteration 1 findings (all four lenses), and the fix applied to each:**

| # | Lens | Finding | Severity | Fix applied |
|---|---|---|---|---|
| 1 | Coherence | Cell 3's `key_links` claimed importable free functions (`createFullAccessBundle`/`decryptBundle`); the module only exports a class | BLOCKER | Cell 3 `key_links`/`action` rewritten to instantiate `ExportImportAdapter` and call its methods |
| 2 | Coherence | Cell 5 placed spike output (`Argon2Hasher.kt`) in `commonMain` — a production path — contradicting `approach.md`'s "scratch space" framing | BLOCKER | Cell 5 rewritten: BouncyCastle wrapper confined to `jvmTest` only, no production file created |
| 3 | Coherence | `implement-plan.md`/cells disagreed on file names (`.mjs` vs `.spec.mjs`) and Gradle root (`:mobile:shared:jvmTest` vs `cd mobile && ./gradlew :shared:jvmTest`) | WARNING | `implement-plan.md` re-projected from the cells (authoritative); `plan.md`'s own mention is left as-is (frozen, D1) — cells are the source of truth per D2 |
| 4 | Coherence/Security | Cells 2/4 prohibited editing `chrome-extension/**`; cells 3/5 (the only two allowed inside it) had no such guard at all | WARNING | All of cells 2/3/4/5 now explicitly prohibit modifying `chrome-extension/src/**` (read-only oracle) |
| 5 | Security | Cell 3's fixture had no requirement that its inputs be synthetic — a real vault export + real password could satisfy the cell as written, permanently in git history | **BLOCKER** | Cell 3 now requires obviously-fake profile/secrets/password, a `_warning: SYNTHETIC TEST DATA ONLY` field, and prohibits any real user data |
| 6 | Security | `implement-plan.md`'s Rollback Plan claimed "delete `mobile/`" but cells 3/5 write into `chrome-extension/` too, including **two copies** of the fixture | **BLOCKER** | Rollback Plan rewritten to enumerate both fixture copies and the `chrome-extension/scripts/*` additions |
| 7 | Security | Cell 4's "no plaintext logged" prohibition was unverifiable/self-contradictory (fixture plaintext is itself on disk; a failed assertion could print it) | WARNING | Cell 4 now requires a single direct `assertEquals`, never a custom message or separate print, to keep the prohibition checkable |
| 8 | Feasibility | **No JDK/Gradle/Android SDK in this sandbox** — every `./gradlew` verify (cells 1,2,4,5) is unrunnable here | **BLOCKER** | Not a cell defect — see Feasibility Matrix; escalated to the user, not silently worked around |
| 9 | Feasibility | Cell 1 had no version-pinning discipline (Gradle/Kotlin/AGP), no `namespace`/`compileSdk`/`minSdk` requirement — a cold worker would guess | BLOCKER | Cell 1 now requires recording chosen versions (via the official Kotlin/AGP/Gradle compatibility matrix) in `libs.versions.toml`, and prohibits alpha/beta/RC versions |
| 10 | Feasibility | Cell 5's first repair (route Argon2 reference generation through vitest/jsdom) was itself unproven and, on closer static analysis, likely still broken (`ENVIRONMENT_IS_NODE`/`__dirname` path fires under jsdom too) | BLOCKER (self-correction) | Redesigned again, this time against real execution evidence: plain Node + the actual `argon2-browser` package + `delete globalThis.fetch` — proven working (see matrix) |
| 11 | Feasibility | Cell 3's `node_modules` absence in this worktree was flagged; re-checked: cell 3 has zero external dependencies (only Node builtins + the extension's own adapter file), so this does not block it | WARNING (resolved, no fix needed) | N/A — confirmed cell 3 needs no `node_modules` |
| 12 | Cold-pickup | `commonMain`/`jvmMain`/`androidMain` file placement for `TagCodec.kt`/`BundleDecryptor.kt`/`Argon2Hasher.kt` contradicted itself (javax.crypto/BouncyCastle unreachable from `commonMain`, but cells put production code there) | CRITICAL | Resolved by the same `jvmAndroidMain` intermediate source set fix as #2/#9 above |
| 13 | Cold-pickup | Cell 5's fixed Argon2 test vector is still unresolved (no existing suite to draw one from) | MINOR (already an Open Question) | Left as an explicit Open Question for execution time — not a planning gap, genuinely decided at implementation |

No iteration 2 was needed: every BLOCKER above was resolved by a concrete cell/doc edit (not a cited "should be fine") except **#8, the missing JDK/toolchain**, which is an environment fact no cell edit can fix — it is carried forward to Gate 3 as an explicit constraint, per validating's own "never silently work around a NOT READY" rule.

## Cell Review (cold pickup, post-repair)

```text
CELL REVIEW REPORT
Work: Track B current slice (5 cells)
Cells reviewed: 5
CRITICAL FLAGS: none open (all fixed — see table above, rows 1, 2, 5, 6, 9, 10, 12)
MINOR FLAGS: passchef-mobile-5 — the fixed Argon2 test vector itself is chosen at execution time (recorded as an Open Question, not a defect)
CLEAN CELLS: passchef-mobile-3 (confirmed genuinely runnable end-to-end today)
REVISIONS MADE: passchef-mobile-1 (jvmAndroidMain source set, version-pin discipline), passchef-mobile-2 (TagCodec moved to jvmAndroidMain, Base32 split out), passchef-mobile-3 (synthetic-data requirement, class-based wording), passchef-mobile-4 (BundleDecryptor moved to jvmAndroidMain, checkable no-log prohibition), passchef-mobile-5 (real argon2-browser + fetch workaround, BouncyCastle confined to jvmTest)
SUMMARY: All 5 cells are now internally consistent and cold-pickup-ready. Cell 3 and cell 5's Node-half are proven executable in this environment today; cells 1/2/4/5's Kotlin/Gradle-side verify cannot run until a JDK+Android SDK toolchain exists.
```

## Approval Block

```text
VALIDATION COMPLETE - APPROVAL REQUIRED BEFORE EXECUTION
Mode: high-risk
Work: Track B — Mobile Core current slice (cells passchef-mobile-1..5)
Reality gate: PASS (with the toolchain constraint noted)
Feasibility: READY WITH CONSTRAINTS
Structure: PASS after 1 iteration (all BLOCKERs fixed except the environment gap, which is not a structural defect)
Spikes: none run as separate .bee/spikes/ probes during validating itself; cell 5 IS a spike cell, its Node-half proven, Kotlin-half pending toolchain
Cell review: PASS (5 cells, 0 CRITICAL open)
Unresolved concerns: no JDK/Gradle/Android SDK toolchain in this execution sandbox — blocks cells 1, 2, 4, and cell 5's Kotlin half; requires a human decision (install here, needs sudo password the agent does not have — vs. execute in a different environment)
```
