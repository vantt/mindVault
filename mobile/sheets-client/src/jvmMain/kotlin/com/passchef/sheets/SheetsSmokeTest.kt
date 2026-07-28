package com.passchef.sheets

import com.sun.net.httpserver.HttpServer
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.net.InetSocketAddress
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.Properties
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * JVM OAuth loopback smoke-test tool (oauth-sheets-4, D4 item 4 / approach.md
 * Epic E5). Runs the OAuth 2.0 Desktop-app loopback flow (approach.md Q2),
 * exchanges the resulting code for an access token, then calls
 * [SheetsClient] once to read a configured sheet range.
 *
 * This is CODE-ONLY per D4: capping this cell never involves running `main()`
 * against real Google credentials -- that real run is the user's own
 * follow-up step (see docs/runbooks/google-oauth-sheets-setup.md). The only
 * behavior verified here is [loadSmokeTestConfig]'s missing-config path (see
 * SheetsSmokeTestConfigTest.kt).
 *
 * Never logs or prints a real access/refresh token value, including in error
 * paths (this cell's prohibitions) -- only status messages are printed.
 */

/** Default location of the local, gitignored smoke-test config file (see
 * mobile/sheets-client/.gitignore) -- never committed. */
private val DEFAULT_CONFIG_FILE = File("smoketest.local.properties")

private const val REQUIRED_KEYS_HELP = "clientId, clientSecret, spreadsheetId, range"

private const val AUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth"
private const val TOKEN_ENDPOINT = "https://oauth2.googleapis.com/token"
private const val SHEETS_SCOPE = "https://www.googleapis.com/auth/spreadsheets"

/**
 * The four values the smoke-test tool needs to run the real loopback OAuth +
 * one Sheets API read. Loaded from a local, gitignored properties file --
 * never hardcoded, never committed (D1).
 */
data class SmokeTestConfig(
    val clientId: String,
    val clientSecret: String,
    val spreadsheetId: String,
    val range: String
)

/**
 * Outcome of [loadSmokeTestConfig] -- deliberately not an exception, so the
 * "no config file present" case (this cell's one testable behavior per its
 * must_haves truth) can be asserted on directly instead of caught.
 */
sealed class ConfigLoadResult {
    data class Loaded(val config: SmokeTestConfig) : ConfigLoadResult()
    data class Missing(val message: String) : ConfigLoadResult()
}

/**
 * Loads [SmokeTestConfig] from [configFile] (default:
 * `smoketest.local.properties`, resolved relative to the current working
 * directory when the tool is run). Never throws for a missing file or
 * missing keys -- returns [ConfigLoadResult.Missing] with a clear, actionable
 * message instead.
 */
fun loadSmokeTestConfig(configFile: File = DEFAULT_CONFIG_FILE): ConfigLoadResult {
    if (!configFile.exists()) {
        return ConfigLoadResult.Missing(
            "No smoke-test config found at '${configFile.path}'. Create it (this file " +
                "is gitignored -- see mobile/sheets-client/.gitignore) with the keys: " +
                "$REQUIRED_KEYS_HELP. Get a Client ID/Secret by following " +
                "docs/runbooks/google-oauth-sheets-setup.md (Step 5 creates the " +
                "Desktop-app OAuth client; Step 7 explains where to place the values)."
        )
    }
    val props = Properties()
    configFile.inputStream().use { props.load(it) }
    val clientId = props.getProperty("clientId").orEmpty()
    val clientSecret = props.getProperty("clientSecret").orEmpty()
    val spreadsheetId = props.getProperty("spreadsheetId").orEmpty()
    val range = props.getProperty("range").orEmpty()
    val missingKeys = listOfNotNull(
        "clientId".takeIf { clientId.isBlank() },
        "clientSecret".takeIf { clientSecret.isBlank() },
        "spreadsheetId".takeIf { spreadsheetId.isBlank() },
        "range".takeIf { range.isBlank() }
    )
    if (missingKeys.isNotEmpty()) {
        return ConfigLoadResult.Missing(
            "Smoke-test config at '${configFile.path}' is missing required key(s): " +
                "${missingKeys.joinToString(", ")}. Required keys: $REQUIRED_KEYS_HELP."
        )
    }
    return ConfigLoadResult.Loaded(SmokeTestConfig(clientId, clientSecret, spreadsheetId, range))
}

/**
 * The re-auth message printed on a 401 ([SheetsUnauthorizedException]) --
 * text sourced from the runbook's Step 6 (docs/runbooks/google-oauth-sheets-setup.md),
 * which documents the Testing-mode 7-day refresh-token expiry as expected
 * dev-time friction, not a bug.
 */
internal fun unauthorizedReAuthMessage(): String =
    "Your Google access token was rejected (expired or revoked). Because this " +
        "app's OAuth consent screen is in Testing mode, refresh tokens expire " +
        "after 7 days -- this is expected, not a bug. Re-run this smoke-test tool " +
        "to go through the sign-in/consent screen again and get a fresh token. " +
        "See docs/runbooks/google-oauth-sheets-setup.md Step 6 for details."

/**
 * The re-consent message printed on a 403 ([SheetsForbiddenException]) --
 * scope/consent mismatch, distinct from an expired token (401).
 */
internal fun forbiddenReConsentMessage(): String =
    "Google rejected this request as forbidden -- your account likely has not " +
        "granted the 'spreadsheets' scope, or is not listed as a test user for " +
        "this OAuth consent screen. Re-run this smoke-test tool to go through " +
        "consent again, and confirm your account appears under Test users. See " +
        "docs/runbooks/google-oauth-sheets-setup.md Step 3 (test users) and " +
        "Step 6 (scope) for details."

@Serializable
private data class TokenResponse(
    val access_token: String,
    val expires_in: Int? = null,
    val token_type: String? = null,
    val scope: String? = null
)

/**
 * Runs the OAuth 2.0 Desktop-app loopback flow (approach.md Q2): starts a
 * loopback HTTP server on an ephemeral port, prints the consent URL for the
 * user to open by hand, waits for Google's redirect carrying the
 * authorization code, then exchanges it for an access token. The returned
 * token is never logged or printed by this function or its callers.
 */
private suspend fun runLoopbackOAuth(config: SmokeTestConfig, httpClient: HttpClient): String {
    val server = HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0)
    val port = server.address.port
    val redirectUri = "http://127.0.0.1:$port"
    var authorizationCode: String? = null
    var callbackError: String? = null
    val received = CountDownLatch(1)

    server.createContext("/") { exchange ->
        val query = exchange.requestURI.query.orEmpty()
        val params = query.split("&")
            .filter { it.isNotBlank() }
            .associate { pair ->
                val parts = pair.split("=", limit = 2)
                val key = parts[0]
                val value = parts.getOrElse(1) { "" }
                key to URLDecoder.decode(value, "UTF-8")
            }
        authorizationCode = params["code"]
        callbackError = params["error"]
        val responseBody = (
            if (authorizationCode != null) {
                "Login successful -- you can close this tab and return to the smoke-test tool."
            } else {
                "Login failed (${callbackError ?: "unknown error"}) -- you can close this tab."
            }
            ).toByteArray()
        exchange.sendResponseHeaders(200, responseBody.size.toLong())
        exchange.responseBody.use { it.write(responseBody) }
        received.countDown()
    }
    server.start()
    try {
        val authUrl = buildString {
            append(AUTH_ENDPOINT)
            append("?client_id=").append(URLEncoder.encode(config.clientId, "UTF-8"))
            append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8"))
            append("&response_type=code")
            append("&scope=").append(URLEncoder.encode(SHEETS_SCOPE, "UTF-8"))
            append("&access_type=offline")
            append("&prompt=consent")
        }
        println("Open this URL in your browser to sign in and grant access:")
        println(authUrl)
        println("Waiting for sign-in to complete (loopback redirect on $redirectUri)...")

        received.await(5, TimeUnit.MINUTES)
        val code = authorizationCode
            ?: error(
                "OAuth loopback did not receive an authorization code" +
                    (callbackError?.let { " (Google reported: $it)" } ?: " (timed out after 5 minutes)")
            )

        val tokenResponse: TokenResponse = httpClient.post(TOKEN_ENDPOINT) {
            setBody(
                FormDataContent(
                    Parameters.build {
                        append("code", code)
                        append("client_id", config.clientId)
                        append("client_secret", config.clientSecret)
                        append("redirect_uri", redirectUri)
                        append("grant_type", "authorization_code")
                    }
                )
            )
        }.body()
        return tokenResponse.access_token
    } finally {
        server.stop(0)
    }
}

/**
 * Entrypoint: loads the local config, runs the loopback OAuth flow, then
 * performs one [SheetsClient.getValues] call and prints a row count -- never
 * the sheet's actual cell contents or the access token. On a 401/403 from
 * [SheetsClient], prints the specific re-auth/re-consent instruction instead
 * of a raw stack trace.
 *
 * NEVER invoked by this cell's verify step -- the only jvmTest exercises
 * [loadSmokeTestConfig] directly. Running this against real Google
 * credentials is the user's own follow-up step (D4 item 4).
 */
fun main() {
    when (val result = loadSmokeTestConfig()) {
        is ConfigLoadResult.Missing -> println(result.message)
        is ConfigLoadResult.Loaded -> runBlocking {
            val oauthHttpClient = HttpClient(CIO) {
                expectSuccess = false
                install(ContentNegotiation) {
                    json(Json { ignoreUnknownKeys = true })
                }
            }
            try {
                println("Starting OAuth loopback flow...")
                val accessToken = runLoopbackOAuth(result.config, oauthHttpClient)
                println(
                    "Token exchange complete. Reading '${result.config.range}' from " +
                        "spreadsheet '${result.config.spreadsheetId}'..."
                )
                val sheetsClient = SheetsClient(CIO.create(), accessToken)
                val values = sheetsClient.getValues(result.config.spreadsheetId, result.config.range)
                println("Read ${values.values.size} row(s) from the sheet.")
            } catch (e: SheetsUnauthorizedException) {
                println(unauthorizedReAuthMessage())
            } catch (e: SheetsForbiddenException) {
                println(forbiddenReConsentMessage())
            } finally {
                oauthHttpClient.close()
            }
        }
    }
}
