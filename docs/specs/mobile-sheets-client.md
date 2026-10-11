---
area: mobile-sheets-client
updated: 2026-10-12
sources: [passchef-mobile-oauth-sheets]
decisions: [D1, D2, D3, D4]
coverage: partial
---

# Spec: Mobile Sheets Connection

Lets the mobile app read and update the account owner's Google Sheet that stores
their password recipes — the same sheet the browser extension already reads —
without scraping the page like the extension does. Today this area is a
foundational connection layer plus a one-time manual setup and a one-time
verification tool. The [Android Cookbook](mobile-cookbook.md) now integrates its
canonical common sources through `:sheets-android`; independent local verification
passed. Real Android Google acceptance remains Pending.

## Entry Points & Triggers

- **Google account setup runbook** (followed once, by hand, by the account
  owner, outside this product, in their Google account's own settings pages) →
  grants Sheets read/write permission on the owner's behalf; the app explicitly
  selects the Menu/tab to read. The scope is not limited to that selected Menu.
- **One-time connection check** (a command-line tool run once by the person who
  just completed the setup) → proves the granted permission actually works by
  performing one real read of the sheet.
- **Android Cookbook Connect/tab selection** → canonical metadata/read client
  through an Android engine. Code/local checks Done; live acceptance Pending. Update
  authoring screens remain deferred.

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
| Android Cookbook (code complete) | Requests explicit Google authorization and reads a selected Menu/tab; real owner grant and phone acceptance Pending. |

## Business Rules

- **R1** — The permission requested covers both reading AND updating the sheet, not read-only, so the connection never has to ask the account owner to re-grant a broader permission later. (D3)
- **R2** — Updates through this connection are limited to appending new rows and updating existing ones; deleting a row is never done through this connection. (prior locked decision, carried into D3's scope)
- **R3** — Setting up the permission grant (creating the connection, choosing what it can access) is always a manual, one-time action the account owner takes themselves in their own Google account settings — this product never automates that setup. (D1)
- **R4** — Proving the connection actually works (the one-time connection check) is verified against simulated responses as part of building this area; running it for real, with the account owner's real permission grant, is the account owner's own follow-up action, separate from building the connection itself. (D4)

- **R5** — Each standalone desktop sign-in uses fresh state and PKCE S256; only a matching, valid callback
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

- Android metadata/read screens and the genuine authorization/engine adapter are
  implemented and locally verified in the Cookbook slice. Append/update authoring UI remains later work.
- The account owner has not yet actually completed the one-time setup, and the one-time connection check has not yet been run against a real permission grant — everything proven so far uses simulated responses, not a live connection.
- Android401 reconnect/403 access feedback is implemented but still needs the
  live owner/device acceptance; the local build passed. The desktop tool retains
  its existing distinct sanitized outcomes.

## Pointers (implementation)

- `mobile/sheets-client/` — Kotlin Multiplatform module (Gradle build: `settings.gradle.kts`, `build.gradle.kts`)
- `mobile/sheets-android/` — root Android/JVM bridge, compiling canonical common sources/tests;
  Android app's `cookbook/` supplies genuine authorization and engine adapters.
- `src/commonMain/kotlin/com/passchef/sheets/SheetsClient.kt`, `SheetsModels.kt`, `SheetsClientExceptions.kt` — the connection client (read/update, distinct expired/insufficient error types)
- `src/commonTest/kotlin/com/passchef/sheets/SheetsClientTest.kt` — unit tests against simulated responses
- `src/jvmMain/kotlin/com/passchef/sheets/SheetsSmokeTest.kt` — the one-time connection check tool
- `LoopbackOAuth.kt`, `OAuthTokenExchange.kt`, `SmokeTestConfig.kt` in the same directory —
  state/PKCE, safe token exchange, and UTF-8 local configuration
- `src/jvmTest/kotlin/com/passchef/sheets/SheetsSmokeTestConfigTest.kt`
- `docs/runbooks/google-oauth-sheets-setup.md` — the one-time setup runbook
- `docs/mobile-kmp-toolchain-setup.md` — local toolchain install notes for building this module
- `archive/plans/passchef-mobile-oauth-sheets/{CONTEXT.md,approach.md,plan.md}` — design history and locked decisions D1-D4
