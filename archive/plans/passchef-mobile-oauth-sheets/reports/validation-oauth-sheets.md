# Validation Report — passchef-mobile-oauth-sheets (current slice: oauth-sheets-1..4)

## Reality Gate

| Check | Verdict | Evidence |
|---|---|---|
| MODE FIT | PASS | High-risk correctly assigned (external-provider hard-gate flag: Google OAuth + Sheets API), unrelated to file count. |
| REPO FIT | PASS | No conflicting existing mobile code; this is the repo's first Kotlin/KMP entry point, confirmed via `find`/`ls` (no `.gradle*`, no `gradlew` anywhere pre-existing). |
| ASSUMPTIONS | PASS (after spike) | The one blocking assumption — "a JDK/Gradle toolchain can be installed here without root" — was proven via spike (below), not left as plausibility. |
| SMALLER PATH | PASS | Track already narrowed to a minimal module + JVM smoke-test, explicitly deferring the full Android app (p-c46b217c) and iOS. No further shrink available without dropping a locked decision. |
| PROOF SURFACE | PASS | Every cell has a self-contained, runnable `verify` (fixed after plan-checker pass, see below). |

## Feasibility Matrix

| Assumption | Risk | Proof Required | Evidence | Result |
|---|---|---|---|---|
| A portable JDK can be installed without root/sudo | HIGH (blocks all 4 cells if false) | Runtime probe: download + run `java -version` | Downloaded Temurin 21.0.12+8 tarball via Adoptium API, extracted with `tar xzf`, ran `./bin/java -version` successfully in `.bee/spikes/passchef-mobile-oauth-sheets/`. Output: `openjdk version "21.0.12" ... Temurin-21.0.12+8`. | PASS |
| A portable Gradle can run against that JDK without root | HIGH | Runtime probe: download + `gradle -v` | Downloaded Gradle 8.10 zip from services.gradle.org. No `unzip` binary present (`which unzip` empty) — extracted instead with `python3 -m zipfile -e`. Required `chmod +x` (archives don't preserve exec bits over this extraction path). `./gradle-8.10/bin/gradle -v` with `JAVA_HOME` pointed at the extracted JDK printed `Welcome to Gradle 8.10!` with correct JVM/OS info. | PASS |
| No cycles / sane wave shape across the 4 cells | MEDIUM | `bee cells schedule` | `node .bee/bin/bee.mjs cells schedule --feature passchef-mobile-oauth-sheets --json` → 3 waves `[[1,2],[3],[4]]`, zero cycles, zero unsatisfiable deps (re-checked after the dependency fix below). | PASS |
| Ktor + kotlinx.serialization is a viable, idiomatic KMP HTTP/JSON stack | MEDIUM | Docs/Upstream evidence (bee-xia) | Ktor's own multiplatform docs recommend exactly this pairing; confirmed during bee-xia discovery (approach.md Q1). No ready-made multiplatform Google Sheets client exists, so a thin wrapper over the stable v4 REST API is the correct (rung-3) choice. | PASS (Inference-labeled in approach.md, accepted) |
| Google's OAuth "Desktop app" client type still supports the loopback flow (not deprecated like Android/iOS/Chrome) | MEDIUM | Docs evidence | `developers.google.com/identity/protocols/oauth2/resources/loopback-migration` confirms Desktop-app-type loopback usage "will continue to be supported." | PASS |

## Plan-Checker + Cold-Pickup Review (bee-review, opus, review tier)

First pass returned **NOT READY** with 5 BLOCKER/CRITICAL findings and 5 WARNINGs. All BLOCKER/CRITICAL findings were fixed before this report was written:

1. **Fixed** — all 3 Gradle `verify` commands were not self-contained (missing `JAVA_HOME`/`PATH`, since env vars don't persist across separate command invocations). Now every verify command inlines `JAVA_HOME="$(pwd)/.toolchain/jdk" PATH="$JAVA_HOME/bin:$PATH"` against a fixed toolchain path established by oauth-sheets-1.
2. **Fixed** — oauth-sheets-4 was missing a dependency on oauth-sheets-2 despite needing its runbook's re-auth instruction text; added `oauth-sheets-2` to `deps`.
3. **Fixed** — oauth-sheets-4 was missing `build.gradle.kts` from its `files` list despite needing to add a jvmMain-only Ktor engine dependency; added.
4. **Fixed** — oauth-sheets-1's action told the worker to gitignore a toolchain download dir but never listed the root `.gitignore` in its `files`, and separately told it not to reuse `.bee/spikes/` (already-ignored) as the download location — a self-contradiction. Standardized on a fixed `.toolchain/` dir, added `.gitignore` to oauth-sheets-1's files, and instructed adding `.toolchain/` to it.
5. **Fixed** — oauth-sheets-3's `key_links` falsely claimed oauth-sheets-1 configures the Ktor HttpClient (oauth-sheets-1 is explicitly left skeletal); corrected to say oauth-sheets-3 adds its own Ktor deps.

WARNINGs also addressed: added a data-integrity truth (append/update targets the exact configured range, asserted against the mock engine's captured request) to oauth-sheets-3; added the D4 dry-run self-check requirement to oauth-sheets-2; replaced oauth-sheets-4's unverifiable runtime-behavior truth with one provable by an actual `jvmTest`, and added an explicit "never log/print a real token" prohibition (test matrix item 11).

Scope-sanity and decision-coverage dimensions passed clean on the first pass: every locked decision (D1-D4) maps to at least one cell, and no cell reaches into the out-of-scope Android app UI.

## Advisor Consult (AO2b/AO3, high-risk precondition)

`.bee/config.json` has no `advisor` key configured — `resolveAdvisor` returns null for this repo. Per the routing contract, this is recorded as a fact, not treated as a blocker: no advisor is configured, so none was consulted. This report itself is the recorded artifact referenced by the `advisor_ref` staleness anchor.

## Verdict

**READY WITH CONSTRAINTS.** The one real constraint: oauth-sheets-1's toolchain-install step is a one-time, ~330MB download that must succeed again in whatever environment actually executes the cells (this validation's spike proved it works in *this* sandbox; a different execution environment should re-verify network access to `api.adoptium.net` and `services.gradle.org`). Everything else — dependency graph, cell verify commands, decision coverage, scope — is proven or fixed.
