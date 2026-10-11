# passchef-mobile-oauth-sheets — Approach

## Discovery (L3 via bee-xia)

### Q1 — HTTP/JSON stack for a KMP Sheets API client

- **Local:** repo has no Kotlin/Gradle/Android project of any kind yet; no reusable client exists.
- **Docs/Upstream:** Ktor's official multiplatform guide (ktor.io/docs/client-create-multiplatform-application.html) is the standard way to make HTTP calls from `commonMain` in a KMP module, paired with `kotlinx.serialization` for JSON — this pairing shows up consistently across current KMP tutorials and the Ktor docs themselves.
- **Inference:** no ready-made, true-multiplatform Google Sheets client library was found. Google's own `google-api-services-sheets` / `google-api-client` are JVM/Android-oriented Java libraries, not published for KMP `commonMain` — using them would break D2's "build/test độc lập" goal across source sets.
- **Chosen (ladder rung 3 — adapt a proven upstream pattern):** Ktor client (`ktor-client-core` + `ktor-client-content-negotiation` + `ktor-serialization-kotlinx-json`, engine `CIO` for `jvmMain`) calling the Sheets API v4 REST endpoints directly (`spreadsheets.values.get` / `.append` / `.update`) with hand-written `@Serializable` request/response models. Rung 1 (reuse) rejected: no fitting multiplatform library exists. Rung 2 (built-in): N/A, no stdlib HTTP client. Rung 4 (build from scratch) rejected: Ktor itself is reused, not reinvented — only the thin Sheets-specific wrapper is new, against a stable, well-documented REST API.

### Q2 — OAuth flow + Google Cloud Console client type

- **Docs:** `developers.google.com/identity/protocols/oauth2/native-app` and the loopback-migration guide confirm the **"Desktop app" client type** uses the Loopback IP Address redirect flow, and — unlike Android/iOS/Chrome client types — this flow is **not** part of Google's loopback deprecation; it continues to be supported.
- **Chosen:** create the OAuth client as **Desktop app** type for this track. No SHA-1 fingerprint or package name needed (those only apply to an "Android" client type). This directly fits D4's JVM/CLI smoke-test — no Android app exists yet to host a real "Android" OAuth client.
- **Deferred (explicitly out of this track's scope, forward pointer for p-c46b217c):** when the real Android app is built, add a **separate "Android" OAuth client** (SHA-1 + package name) in the *same* GCP project, using Credential Manager + the `AuthorizationClient` API (Docs: Android's own guidance distinguishes Sign-In, handled by Credential Manager, from *authorization* for sensitive-scope data access like Sheets, which needs `AuthorizationClient`). Not built now — noted so the runbook doesn't paint the project into a one-client corner.

### Q3 — Testing-mode gotchas

- **Docs/Upstream:** an OAuth consent screen left in **Testing** status is treated as unverified — refresh tokens it issues **expire after 7 days**, and External apps are capped at **100 test users** (irrelevant here, single developer use). Sensitive-scope verification (needed only to publish/remove the 7-day limit) takes **2–6 weeks** including a security assessment. Setting the consent screen's user type to **Internal** removes both limits, but only exists for a Google Workspace-domain account — not available to a plain personal Google account.
- **Consequence for the runbook (D1):** document the 7-day refresh-token expiry plainly as an expected dev-time inconvenience (re-run the OAuth consent to get a fresh token every ~7 days) rather than a bug — and note the Internal-app shortcut as an option *if* the user has a Workspace domain to register the project under.

## Chosen Path (summary)

1. Install a local JDK + Gradle toolchain (none present in this sandbox today — `java`/`gradle`/`kotlin` are all absent; no passwordless sudo, so use a user-writable install, no root needed).
2. Scaffold a minimal Gradle KMP module (`mobile/sheets-client/`) with `commonMain` (client interface + Sheets v4 models) and `jvmMain` (CIO engine + a smoke-test CLI entrypoint) source sets — buildable and testable stand-alone (D2).
3. Write the OAuth Console runbook (D1) — Desktop app client type, Testing mode + test user, `.../auth/spreadsheets` scope, 7-day refresh-token caveat, Internal-app note, numbered steps with a checkable "done" per step (per fresh-eyes review fix).
4. Implement the Sheets API client wrapper against the REST v4 endpoints with unit tests using a fake/mocked HTTP response (D4 code-only item 2) — no real Google credentials needed to pass.
5. Implement the JVM smoke-test entrypoint (loopback OAuth exchange + one real `spreadsheets.values.get` call), reading Client ID/Secret from a local, gitignored config file — code-complete and cap-eligible without ever running it against real credentials (per amended D4); running it for real is the user's own follow-up step once they've completed the Console runbook.

## Rejected Alternatives

- **google-api-client (Java)** for the Sheets client — JVM/Android-only, breaks the KMP `commonMain` goal (D2).
- **"Android" OAuth client type now** — no Android app exists yet to receive the redirect; would also block the JVM-only smoke-test D4 needs today.
- **AppAuth-Android / Credential Manager now** — Android-only libraries, not usable before p-c46b217c scaffolds a real Android app; deferred, not rejected outright.

## Risk Map

| Component | Risk | Proof needed |
|---|---|---|
| Ktor + Sheets REST wrapper (commonMain) | LOW — stable, well-documented REST API, standard KMP HTTP stack | Unit tests against fixed/mocked JSON responses |
| Local JDK/Gradle toolchain absent in sandbox | MEDIUM (operational, not architectural) | `java -version` + `./gradlew tasks` succeed after install |
| OAuth Desktop loopback flow (never done in this repo) | MEDIUM — new integration surface | Manual smoke-test run by the user with real credentials (D4 item 4, out-of-band) |
| 7-day refresh-token expiry in Testing mode | LOW code risk, real dev-time friction | Documented in runbook, not a code fix |

## Open Questions For Validating

- Confirm the sandbox can actually install a JDK without root (portable tarball or user-scoped package manager) before cells assume it's possible.
- Confirm exact Gradle/Kotlin/Ktor version pins available today (validating should pull real current stable versions rather than this approach doc guessing numbers).
