package com.passchef.sheets

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.readRemaining
import io.ktor.utils.io.core.readBytes
import kotlinx.serialization.decodeFromString
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.prepareGet
import io.ktor.client.request.header
import io.ktor.client.request.preparePost
import io.ktor.client.request.preparePut
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
 * Takes ownership of the supplied [HttpClientEngine]; callers must call [close].
 * The caller supplies the engine so this class stays testable in
 * `commonTest` with a fake/mocked engine (e.g. Ktor's `MockEngine`) -- no real
 * network call is ever required to exercise it.
 */
class SheetsClient(
    private val engine: HttpClientEngine,
    private val accessToken: String
) {
    private val httpClient = HttpClient(engine) {
        expectSuccess = false
        install(HttpTimeout) { requestTimeoutMillis = 20_000; connectTimeoutMillis = 10_000; socketTimeoutMillis = 20_000 }
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    /** Only requested metadata; the caller confirms a tab before reading rows. */
    suspend fun getSpreadsheet(spreadsheetId: String): SpreadsheetMetadata {
        val statement = httpClient.prepareGet("$SHEETS_API_BASE/${spreadsheetId.encodeAsPathSegment()}") {
            bearerAuth()
            url.parameters.append("fields", "spreadsheetId,properties(title),sheets(properties(sheetId,title,gridProperties))")
        }
        return statement.execute { it.decodeOrThrow<SpreadsheetMetadata>() }.also {
            require(it.spreadsheetId == spreadsheetId && it.properties.title.length in 1..200 && it.sheets.size <= 50)
            require(it.sheets.all { tab -> tab.properties.title.length in 1..200 && tab.properties.sheetId >= 0 })
        }
    }

    /** Reads [range] (A1 notation, e.g. `"Sheet1!A1:B2"`) from [spreadsheetId]. */
    suspend fun getValues(spreadsheetId: String, range: String): ValueRange {
        val statement = httpClient.prepareGet(valuesUrl(spreadsheetId, range)) {
            bearerAuth()
        }
        return statement.execute { it.decodeOrThrow<ValueRange>() }
    }

    /**
     * Appends [values] as new rows after the last row of data in [range]
     * (Sheets API `spreadsheets.values.append`).
     */
    suspend fun appendValues(
        spreadsheetId: String,
        range: String,
        values: List<List<String>>,
        valueInputOption: String = "RAW"
    ): AppendValuesResponse {
        validateInputOption(valueInputOption)
        val statement = httpClient.preparePost(
            "${valuesUrl(spreadsheetId, range)}:append?valueInputOption=$valueInputOption"
        ) {
            bearerAuth()
            contentType(ContentType.Application.Json)
            setBody(ValueRange(range = range, values = values))
        }
        return statement.execute { it.decodeOrThrow<AppendValuesResponse>() }
    }

    /**
     * Overwrites cells starting at [range] with [values] (Sheets API
     * `spreadsheets.values.update`) -- never clears/deletes cells outside it.
     */
    suspend fun updateValues(
        spreadsheetId: String,
        range: String,
        values: List<List<String>>,
        valueInputOption: String = "RAW"
    ): UpdateValuesResponse {
        validateInputOption(valueInputOption)
        val statement = httpClient.preparePut(
            "${valuesUrl(spreadsheetId, range)}?valueInputOption=$valueInputOption"
        ) {
            bearerAuth()
            contentType(ContentType.Application.Json)
            setBody(ValueRange(range = range, values = values))
        }
        return statement.execute { it.decodeOrThrow<UpdateValuesResponse>() }
    }

    fun close() {
        httpClient.close()
        engine.close()
    }

    private fun validateInputOption(option: String) {
        require(option == "RAW" || option == "USER_ENTERED") {
            "Input option must be RAW or USER_ENTERED"
        }
    }

    private fun HttpRequestBuilder.bearerAuth() {
        header(HttpHeaders.Authorization, "Bearer $accessToken")
    }

    private fun valuesUrl(spreadsheetId: String, range: String): String =
        "$SHEETS_API_BASE/${spreadsheetId.encodeAsPathSegment()}/values/${range.encodeAsPathSegment()}"

    /**
     * Decodes a 2xx body as [T]; a non-2xx status is surfaced as a distinct,
     * catchable [SheetsClientException] subtype rather than a generic
     * exception (plan.md test matrix item 7/8) -- 401 and 403 each get their
     * own type so callers can tell "re-auth" apart from "re-consent".
     */
    private suspend fun HttpResponse.boundedText(): String {
        val channel = bodyAsChannel()
        try {
            require((headers[HttpHeaders.ContentLength]?.toLongOrNull() ?: 0) <= 200_000)
            val bytes = channel.readRemaining(200_001).readBytes()
            require(bytes.size <= 200_000)
            return bytes.decodeToString(throwOnInvalidSequence = true)
        } finally { channel.cancel(null) }
    }

    private suspend inline fun <reified T> HttpResponse.decodeOrThrow(): T {
        val httpStatus = status.value
        if (httpStatus in 200..299) {
            return Json { ignoreUnknownKeys = true }.decodeFromString<T>(boundedText())
        }
        val message = runCatching { Json { ignoreUnknownKeys = true }.decodeFromString<GoogleApiErrorBody>(boundedText()).error.message }
            .getOrElse { "Sheets API request failed with status $httpStatus" }
        throw when (httpStatus) {
            401 -> SheetsUnauthorizedException(message)
            403 -> SheetsForbiddenException(message)
            else -> SheetsApiException(httpStatus, message)
        }
    }
}
