package com.passchef.sheets

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.system.exitProcess

internal fun unauthorizedReAuthMessage(): String =
    "Google rejected the access token. Repeat sign-in to obtain fresh permission. See the runbook Step 6."

internal fun forbiddenReConsentMessage(): String =
    "Google denied Menu access. Check the account's Menu permission, API enablement, " +
        "requested scope, and consent test-user list. See the runbook Steps 2, 3 and 6."

/** Testable command boundary: zero only after one successful Menu read. */
internal fun runConnectionCheck(
    configFile: File = File("smoketest.local.properties"),
    engineFactory: () -> HttpClientEngine = { CIO.create() },
    authorize: suspend (SmokeTestConfig, HttpClient) -> String = ::authorizeWithLoopback,
    report: (String) -> Unit = ::println
): Int {
    val result = loadSmokeTestConfig(configFile)
    if (result is ConfigLoadResult.Missing) {
        report(result.message)
        return 1
    }
    val config = (result as ConfigLoadResult.Loaded).config
    return runBlocking {
        try {
            val oauthEngine = engineFactory()
            try {
                val oauthClient = HttpClient(oauthEngine) {
                    expectSuccess = false
                    install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
                }
                try {
                    val token = authorize(config, oauthClient)
                    val sheetsClient = SheetsClient(engineFactory(), token)
                    try {
                        val values = sheetsClient.getValues(config.spreadsheetId, config.range)
                        report("Read ${values.values.size} row(s) from the Menu.")
                        0
                    } finally {
                        sheetsClient.close()
                    }
                } finally {
                    oauthClient.close()
                }
            } finally {
                oauthEngine.close()
            }
        } catch (_: SheetsUnauthorizedException) {
            report(unauthorizedReAuthMessage())
            1
        } catch (_: SheetsForbiddenException) {
            report(forbiddenReConsentMessage())
            1
        } catch (error: ConnectionCheckFailure) {
            report(error.message ?: "Connection check failed. Repeat sign-in.")
            1
        } catch (_: Exception) {
            report("Connection check failed. Check the local configuration and network, then try again.")
            1
        }
    }
}

/** Manual only: private configuration and a real Google account are required. */
fun main() {
    exitProcess(runConnectionCheck())
}
