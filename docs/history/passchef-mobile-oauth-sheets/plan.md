---
artifact_contract: bee-plan/v1
mode: high-risk
approved_gate2: 2026-07-28
---

# Plan: passchef-mobile-oauth-sheets

Mode: `high-risk` — 1 hard-gate flag: **external systems/provider** (Google Cloud OAuth + Sheets API). Also touches a brand-new toolchain (Kotlin Multiplatform) with zero repo precedent.
Why this is the least workflow that protects the work: a single hard-gate flag alone forces high-risk regardless of file count (per the mode gate) — external-provider integrations get this scrutiny because a wrong scope/client-type choice costs a real re-consent cycle with Google, not just a code revert.

## Requirements (from CONTEXT.md)

- D1: OAuth/GCP Console setup is manual (agent writes a runbook); no gcloud-CLI automation; client-side code reads Client ID/Secret from config/env; consent screen in Testing mode with the user added as a test user.
- D2: Sheets API client scaffolds as a new, minimal KMP/Gradle module, buildable/testable stand-alone — no full app UI.
- D3: Request OAuth scope `https://www.googleapis.com/auth/spreadsheets` (read+write), not readonly.
- D4: DoD splits into a code portion (runbook + client code + fake-token unit tests — cap-eligible by the agent alone) and a user portion (real OAuth client creation + one real smoke-test call — tracked separately, does not block capping the code portion). Google app-verification review is out-of-band, never blocking.

## Approach

See `docs/history/passchef-mobile-oauth-sheets/approach.md` (high-risk lane — full research, risk map, and rejected alternatives live there). Summary: Ktor client + kotlinx.serialization calling the Sheets v4 REST endpoints directly from a new KMP module (no fitting ready-made multiplatform Sheets library exists); OAuth Console client type is **Desktop app** using the Loopback IP redirect flow (fits a JVM/CLI smoke-test, no Android app exists yet); the 7-day refresh-token expiry that comes with an unverified Testing-mode app is documented in the runbook as an expected dev-time inconvenience, not a bug.

## Shape — Epic Map

Feature outcome: an OAuth Console runbook + a buildable, unit-tested KMP module that can read/write a Google Sheet via the Sheets API v4 — ready for the real Android app (p-c46b217c) to consume later, and independently runnable today as a JVM smoke-test.

Repo-reality basis: no Kotlin/Gradle/Android tooling exists anywhere in this repo or sandbox today (`java`/`gradle`/`kotlin` all absent, confirmed via `which`); no passwordless sudo. This is the first Kotlin code point-of-entry the whole passchef-mobile initiative will ever build on.

| Epic | Capability/Risk Area | Why It Exists | Slices | Proof Needed |
|---|---|---|---|---|
| E1 | Toolchain bring-up | Nothing in this sandbox can build Kotlin today; every later epic depends on it | 1 slice (this track) | `java -version`, `./gradlew :sheets-client:build` succeed |
| E2 | KMP module scaffold | D2 — buildable/testable independent of any app UI | 1 slice (this track) | `./gradlew :sheets-client:test` runs (even with zero tests) |
| E3 | OAuth Console runbook | D1 — external, manual, cannot be code-automated | 1 slice (this track) | Runbook reviewed for numbered, checkable steps (fresh-eyes already applied once to CONTEXT.md; this is the runbook's own content) |
| E4 | Sheets client + unit tests | D2/D3 — the actual API wrapper, testable without real credentials | 1 slice (this track) | `./gradlew :sheets-client:test` green with fake/mocked HTTP responses |
| E5 | JVM smoke-test tool | D4 item 4 — code-complete, real run is the user's own follow-up | 1 slice (this track) | Code compiles + a documented manual run instruction; NOT run with real credentials by the agent |

All 5 epics are a single slice each and all belong to the current slice — this track has no future-slice deferral beyond what's already out of scope below.

Slice queue (deps): E1 → E2 → {E3 (independent of E2's internals, can run in parallel) , E4 (needs E2's module skeleton)} → E5 (needs E4's client + E3's scope/client-type decisions documented, though not the user's real credentials). Feasibility: E1 is the only open feasibility question (can this sandbox actually install a JDK without root) — flagged for validating.

Current slice to prepare: all of E1–E5 (this entire track is one slice; p-38074811's real end-to-end integration with p-7df5bb05's live OAuth client is explicitly NOT in this slice — that's the user's own out-of-band step per D4).

## Test matrix (high-risk — probe per dimension)

1. **User types** — single-developer/test-user only (Testing-mode cap of 100 is far above need); no anonymous/admin path exists. N/A beyond noting it.
2. **Input extremes** — Sheets client must not choke on an empty sheet (0 rows) or a sheet with the maximum single-`values.get` range Google allows; unit tests cover an empty-response JSON fixture.
3. **Timing** — the 7-day Testing-mode refresh-token expiry (approach.md Q3) is a timing edge documented in the runbook, not code; smoke-test tool should surface a clear "token expired, re-run the OAuth step" message rather than a raw stack trace.
4. **Scale** — out of scope for this track (single small sheet); no pagination/batching required yet.
5. **State transitions** — N/A, this track has no persistent state machine of its own.
6. **Environment** — the dominant risk here: `java`/`gradle`/`kotlin` absent in the dev sandbox today (E1); module must build reproducibly via the Gradle wrapper (`./gradlew`) so environment drift is caught by CI later, not assumed.
7. **Error cascades** — Sheets API 401 (expired/revoked token) and 403 (scope/consent mismatch) must surface as distinct, actionable client-side errors, not a generic exception; unit-tested with mocked error responses.
8. **Authorization** — a revoked/expired token must fail the smoke-test loudly with a re-auth instruction, never silently retry into a lockout.
9. **Data integrity** — `append`/`update` calls must target the exact configured range/tab (no accidental overwrite of unrelated rows); unit test asserts the request body's range parameter.
10. **Integration** — Sheets API v4 contract drift (a renamed/removed field) should fail the client's deserialization loudly (fail-fast `@Serializable` model), not silently corrupt data; noted as an accepted risk (Google's v4 API is stable/mature).
11. **Compliance** — Client ID/Secret and any token must never be logged or committed; runbook + code both call this out explicitly (gitignored config, no console.log of tokens).
12. **Business logic** — N/A, no domain business rule in this track beyond the scope/CoS already locked in D3.

## Out of scope

- The real Android app UI (6 screens, p-c46b217c) — separate PBI, builds on this module later.
- Actually running the smoke-test with real Google credentials, and any Google app-verification/publish process — user's own follow-up per D4, tracked outside this slice.
- Drive appData transport, QR handoff, write-conflict/merge policy for concurrent sheet edits — untouched, other PBIs.
- iOS target for the KMP module — deferred with the whole iOS track (docs/mobile-app-brainstorm-qa.md Q8), Android/JVM only for now.
