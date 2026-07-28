# oauth-sheets-4 — Report

**Status:** [DONE]

**Outcome:** Implemented the JVM OAuth loopback smoke-test tool in `mobile/sheets-client`'s jvmMain: `SheetsSmokeTest.kt` runs the OAuth 2.0 Desktop-app loopback flow (local ephemeral-port HTTP server, printed consent URL, code-for-token exchange), reads `clientId`/`clientSecret`/`spreadsheetId`/`range` from a local gitignored properties file, then calls oauth-sheets-3's `SheetsClient.getValues` once. A 401/403 from the client prints a distinct re-auth/re-consent message sourced from the OAuth runbook's Step 6/3 instead of a raw stack trace; no token value is ever printed or logged. Code-only per D4 — the one jvmTest (config-loading with no file present) is the only behavior verified; the real run against Google credentials was never attempted.

**Files touched:**
- `mobile/sheets-client/build.gradle.kts` (added jvmMain-only `ktor-client-cio` + `kotlinx-coroutines-core`, and a manual `runSheetsSmokeTest` JavaExec task — not wired into `test`/`check`)
- `mobile/sheets-client/src/jvmMain/kotlin/com/passchef/sheets/SheetsSmokeTest.kt`
- `mobile/sheets-client/src/jvmTest/kotlin/com/passchef/sheets/SheetsSmokeTestConfigTest.kt`
- `mobile/sheets-client/.gitignore` (new — ignores `smoketest.local.properties`)

**Full trace/evidence:** `.bee/cells/oauth-sheets-4.json`
