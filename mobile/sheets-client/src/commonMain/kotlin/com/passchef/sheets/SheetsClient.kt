package com.passchef.sheets

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val SHEETS_API_BASE = "https://sheets.googleapis.com/v4/spreadsheets"

/**
 * Google Sheets API v4 client (D2/D3, oauth-sheets-3): read via
 * `spreadsheets.values.get`, write via `.append` / `.update` only -- no
 * delete/removal operation is implemented, per the locked Q10 decision
 * (docs/mobile-app-brainstorm-qa.md line 257-260).
 *
 * Accepts an already-obtained OAuth access token; it never performs the OAuth
 * dance itself (that is oauth-sheets-4's job, see CONTEXT.md D1/D4).
 *
 * The caller supplies the [HttpClientEngine] so this class stays testable in
 * `commonTest` with a fake/mocked engine (e.g. Ktor's `MockEngine`) -- no real
 * network call is ever required to exercise it.
 */
class SheetsClient(
    engine: HttpClientEngine,
    private val accessToken: String
) {
    private val httpClient = HttpClient(engine) {
        expectSuccess = false
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    /** Reads [range] (A1 notation, e.g. `"Sheet1!A1:B2"`) from [spreadsheetId]. */
    suspend fun getValues(spreadsheetId: String, range: String): ValueRange {
        val response = httpClient.get(valuesUrl(spreadsheetId, range)) {
            bearerAuth()
        }
        return response.decodeOrThrow()
    }

    /**
     * Appends [values] as new rows after the last row of data in [range]
     * (Sheets API `spreadsheets.values.append`).
     */
    suspend fun appendValues(
        spreadsheetId: String,
        range: String,
        values: List<List<String>>,
        valueInputOption: String = "USER_ENTERED"
    ): AppendValuesResponse {
        val response = httpClient.post(
            "${valuesUrl(spreadsheetId, range)}:append?valueInputOption=$valueInputOption"
        ) {
            bearerAuth()
            contentType(ContentType.Application.Json)
            setBody(ValueRange(range = range, values = values))
        }
        return response.decodeOrThrow()
    }

    /**
     * Overwrites cells starting at [range] with [values] (Sheets API
     * `spreadsheets.values.update`) -- never clears/deletes cells outside it.
     */
    suspend fun updateValues(
        spreadsheetId: String,
        range: String,
        values: List<List<String>>,
        valueInputOption: String = "USER_ENTERED"
    ): UpdateValuesResponse {
        val response = httpClient.put(
            "${valuesUrl(spreadsheetId, range)}?valueInputOption=$valueInputOption"
        ) {
            bearerAuth()
            contentType(ContentType.Application.Json)
            setBody(ValueRange(range = range, values = values))
        }
        return response.decodeOrThrow()
    }

    private fun HttpRequestBuilder.bearerAuth() {
        header(HttpHeaders.Authorization, "Bearer $accessToken")
    }

    private fun valuesUrl(spreadsheetId: String, range: String): String =
        "$SHEETS_API_BASE/$spreadsheetId/values/${range.encodeAsPathSegment()}"

    /**
     * Decodes a 2xx body as [T]; a non-2xx status is surfaced as a distinct,
     * catchable [SheetsClientException] subtype rather than a generic
     * exception (plan.md test matrix item 7/8) -- 401 and 403 each get their
     * own type so callers can tell "re-auth" apart from "re-consent".
     */
    private suspend inline fun <reified T> HttpResponse.decodeOrThrow(): T {
        val httpStatus = status.value
        if (httpStatus in 200..299) {
            return body()
        }
        val message = runCatching { body<GoogleApiErrorBody>().error.message }
            .getOrElse { "Sheets API request failed with status $httpStatus" }
        throw when (httpStatus) {
            401 -> SheetsUnauthorizedException(message)
            403 -> SheetsForbiddenException(message)
            else -> SheetsApiException(httpStatus, message)
        }
    }
}

/**
 * Minimal, dependency-free percent-encoding for a single URL path segment.
 * Sheets A1-notation ranges (e.g. `"Sheet 1!A1:B2"`) contain spaces, `!`, and
 * `:` that are not safe unencoded in a path segment; this avoids pulling in a
 * JVM-only encoder so the client stays usable from `commonMain`.
 */
private fun String.encodeAsPathSegment(): String {
    val safe = ('a'..'z') + ('A'..'Z') + ('0'..'9') + listOf('-', '_', '.', '~')
    val builder = StringBuilder()
    for (ch in this) {
        if (ch in safe) {
            builder.append(ch)
        } else {
            for (byte in ch.toString().encodeToByteArray()) {
                val hex = (byte.toInt() and 0xFF).toString(16).uppercase()
                builder.append('%').append(if (hex.length == 1) "0$hex" else hex)
            }
        }
    }
    return builder.toString()
}
