---
area: mobile-sheets-client
updated: 2026-10-11
sources: [passchef-mobile-oauth-sheets]
decisions: [D1, D2, D3, D4]
coverage: partial
---

# Spec: Mobile Sheets Connection

Lets the mobile app read and update the account owner's Google Sheet that stores
their password recipes — the same sheet the browser extension already reads —
without scraping the page like the extension does. Today this area is a
foundational connection layer plus a one-time manual setup and a one-time
verification tool; no end-user mobile screens consume it yet (see Open Gaps).

## Entry Points & Triggers

- **Google account setup runbook** (followed once, by hand, by the account
  owner, outside this product, in their Google account's own settings pages) →
  grants this connection permission to read and update one Google Sheet on the
  owner's behalf.
- **One-time connection check** (a command-line tool run once by the person who
  just completed the setup) → proves the granted permission actually works by
  performing one real read of the sheet.
- *(Not yet built: the mobile app's own screens invoking a read or an update
  during normal use — see Open Gaps.)*

## Data Dictionary

| # | Element | Meaning | Values | Required | Default |
|---|-------|---------|--------|----------|---------|
| 1 | Sheet range | Which rows/columns of the sheet a read or write applies to | a specific area of the sheet (e.g. one tab, a row range) | yes | — |
| 2 | Row values | The cell contents returned by a read, or written by an update | list of rows, each a list of cell values | — | — |
| 3 | Permission grant | The account owner's one-time authorization letting this connection act on their sheet | valid · expired · insufficient (wrong level) | yes | — |
| 4 | Connection outcome | The result of a read or write attempt | success (with row values) · permission expired · permission insufficient · other failure | yes | — |

## Behaviors & Operations

**Read a sheet range.** Given a valid permission grant and a target range,
returns the row values found in that range. A range with no rows in it returns
an empty result — this is a normal outcome, not a failure (see Edge Cases
Settled).

**Update a sheet range (append or overwrite existing rows).** Given a valid
permission grant, a target range, and row values, writes those values into
the configured range. Recipe and account values are sent as literal text by default,
so leading zeroes and formula-like text are preserved. An append searches the
configured table range and adds rows after its last data row; an update writes
from the specified range. The caller must choose a suitable destination. There
is no operation to delete rows through this connection at all — removing a row
is something the account owner does by hand, directly in the sheet (R2).

**Permission expired.** A read or write attempted with an expired grant is
reported as a distinct "permission expired" outcome (never a generic failure),
so whatever calls this connection can prompt the account owner to redo the
one-time setup's sign-in step.

**Permission insufficient.** A read or write attempted with a grant that does
not cover the needed access level is reported as a distinct "permission
insufficient" outcome, separate from "expired."

## Actors & Access

| Actor | Access |
|---|---|
| Account owner | The only actor. Performs the one-time setup and, currently, the one-time connection check. Owns the sheet and the permission grant. |
| Mobile app (future) | Will act on the account owner's behalf using the granted permission, once the app itself exists (Open Gap — not built yet). |

## Business Rules

- **R1** — The permission requested covers both reading AND updating the sheet, not read-only, so the connection never has to ask the account owner to re-grant a broader permission later. (D3)
- **R2** — Updates through this connection are limited to appending new rows and updating existing ones; deleting a row is never done through this connection. (prior locked decision, carried into D3's scope)
- **R3** — Setting up the permission grant (creating the connection, choosing what it can access) is always a manual, one-time action the account owner takes themselves in their own Google account settings — this product never automates that setup. (D1)
- **R4** — Proving the connection actually works (the one-time connection check) is verified against simulated responses as part of building this area; running it for real, with the account owner's real permission grant, is the account owner's own follow-up action, separate from building the connection itself. (D4)

- **R5** — Each sign-in uses fresh state and PKCE S256; only a matching, valid callback
  completes it. Codes and tokens are never printed. Connection-check failures exit nonzero.
- **R6** — Local connection configuration uses UTF-8; Vietnamese and supplementary
  characters in tab names are preserved. Owned HTTP clients and engines are closed
  after success or failure.

## Edge Cases Settled

- An empty target range returns zero rows, not an error.
- An expired permission grant surfaces as its own distinct outcome (never a crash or a generic error), with guidance to redo the setup's sign-in step.
- An insufficient permission grant surfaces as its own distinct outcome, separate from "expired," so the account owner knows which problem they're looking at.
- The permission grant naturally stops working after about a week while the account owner has not yet formally published/verified the connection with Google — this is expected, ordinary behavior, resolved by repeating the setup's sign-in step, not a defect to fix in code.
- Updates start at the configured range; appends add rows after the table found in the
  configured search range. Account values remain literal text by default. No deletion
  operation exists; callers choose a suitable destination and account for table growth.
- The permission-expired and permission-insufficient outcomes never reveal the actual permission value itself (no token/credential value is ever surfaced, logged, or printed anywhere, including in these error paths).

## Open Gaps

- No mobile app screens exist yet that surface a read or an update to an end user during normal use — today this area is only the connection layer, the setup runbook, and the one-time connection check; the real mobile app (a separate, not-yet-built effort) will be the first consumer.
- The account owner has not yet actually completed the one-time setup, and the one-time connection check has not yet been run against a real permission grant — everything proven so far uses simulated responses, not a live connection.
- No screen exists yet to show the account owner an in-app prompt when their permission grant expires; today that guidance only appears in the written setup instructions and in the connection check's own console output.

## Pointers (implementation)

- `mobile/sheets-client/` — Kotlin Multiplatform module (Gradle build: `settings.gradle.kts`, `build.gradle.kts`)
- `src/commonMain/kotlin/com/passchef/sheets/SheetsClient.kt`, `SheetsModels.kt`, `SheetsClientExceptions.kt` — the connection client (read/update, distinct expired/insufficient error types)
- `src/commonTest/kotlin/com/passchef/sheets/SheetsClientTest.kt` — unit tests against simulated responses
- `src/jvmMain/kotlin/com/passchef/sheets/SheetsSmokeTest.kt` — the one-time connection check tool
- `LoopbackOAuth.kt`, `OAuthTokenExchange.kt`, `SmokeTestConfig.kt` in the same directory —
  state/PKCE, safe token exchange, and UTF-8 local configuration
- `src/jvmTest/kotlin/com/passchef/sheets/SheetsSmokeTestConfigTest.kt`
- `docs/runbooks/google-oauth-sheets-setup.md` — the one-time setup runbook
- `docs/mobile-kmp-toolchain-setup.md` — local toolchain install notes for building this module
- `archive/plans/passchef-mobile-oauth-sheets/{CONTEXT.md,approach.md,plan.md}` — design history and locked decisions D1-D4
