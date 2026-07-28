package com.passchef.sheets

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * Unit tests for [SheetsClient] against a fake/mocked HTTP engine (Ktor's
 * `MockEngine`) -- no real network call and no real Google credentials
 * needed, per D4's code-only DoD item 2 and plan.md's E4 verify ("green with
 * fake/mocked HTTP responses").
 */
class SheetsClientTest {

    private fun engineReturning(status: HttpStatusCode, json: String) = MockEngine { _ ->
        respond(
            content = ByteReadChannel(json),
            status = status,
            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        )
    }

    @Test
    fun getValues_success_parsesRowsFromFixture() = runTest {
        val fixture = """{"range":"Sheet1!A1:B2","majorDimension":"ROWS","values":[["a","1"],["b","2"]]}"""
        val client = SheetsClient(engineReturning(HttpStatusCode.OK, fixture), "fake-token")

        val result = client.getValues("sheet-id", "Sheet1!A1:B2")

        assertEquals(listOf(listOf("a", "1"), listOf("b", "2")), result.values)
    }

    @Test
    fun getValues_emptySheet_returnsNoRows() = runTest {
        // Google omits the "values" key entirely for an empty range -- must
        // not throw, must parse as an empty list (plan.md test matrix item 2).
        val fixture = """{"range":"Sheet1!A1:B2","majorDimension":"ROWS"}"""
        val client = SheetsClient(engineReturning(HttpStatusCode.OK, fixture), "fake-token")

        val result = client.getValues("sheet-id", "Sheet1!A1:B2")

        assertTrue(result.values.isEmpty())
    }

    @Test
    fun getValues_unauthorized_throwsDistinctUnauthorizedError() = runTest {
        val fixture = """{"error":{"code":401,"message":"Request had invalid authentication credentials.","status":"UNAUTHENTICATED"}}"""
        val client = SheetsClient(engineReturning(HttpStatusCode.Unauthorized, fixture), "expired-token")

        val error = assertFailsWith<SheetsUnauthorizedException> {
            client.getValues("sheet-id", "Sheet1!A1:B2")
        }
        assertTrue(error.message!!.contains("invalid authentication"))
    }

    @Test
    fun getValues_forbidden_throwsDistinctForbiddenError() = runTest {
        val fixture = """{"error":{"code":403,"message":"The caller does not have permission.","status":"PERMISSION_DENIED"}}"""
        val client = SheetsClient(engineReturning(HttpStatusCode.Forbidden, fixture), "wrong-scope-token")

        val error = assertFailsWith<SheetsForbiddenException> {
            client.getValues("sheet-id", "Sheet1!A1:B2")
        }
        assertTrue(error.message!!.contains("does not have permission"))
    }

    @Test
    fun appendValues_targetsExactlyTheConfiguredRange() = runTest {
        var capturedUrl: String? = null
        val engine = MockEngine { request ->
            capturedUrl = request.url.toString()
            respond(
                content = ByteReadChannel("""{"spreadsheetId":"sheet-id","tableRange":"Sheet1!A1:B2"}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val client = SheetsClient(engine, "fake-token")

        client.appendValues("sheet-id", "Sheet1!A1:B2", listOf(listOf("x", "y")))

        // Range is percent-encoded into the request path -- "!" -> %21, ":" -> %3A.
        assertTrue(
            capturedUrl!!.contains("Sheet1%21A1%3AB2"),
            "expected the encoded configured range in the request URL, got $capturedUrl"
        )
    }

    @Test
    fun updateValues_targetsExactlyTheConfiguredRange() = runTest {
        var capturedUrl: String? = null
        val engine = MockEngine { request ->
            capturedUrl = request.url.toString()
            respond(
                content = ByteReadChannel("""{"spreadsheetId":"sheet-id","updatedRange":"Sheet1!A1:B2","updatedCells":2}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val client = SheetsClient(engine, "fake-token")

        client.updateValues("sheet-id", "Sheet1!A1:B2", listOf(listOf("x", "y")))

        assertTrue(
            capturedUrl!!.contains("Sheet1%21A1%3AB2"),
            "expected the encoded configured range in the request URL, got $capturedUrl"
        )
    }
}
