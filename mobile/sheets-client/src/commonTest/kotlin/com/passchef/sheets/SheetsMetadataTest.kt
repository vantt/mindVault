package com.passchef.sheets

import io.ktor.client.engine.mock.*
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.*
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteChannel
import io.ktor.utils.io.writeFully
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

/** Protocol/budget tests use synthetic HTTP responses, not Google authorization proof. */
class SheetsMetadataTest {
    private val metadata = """{"spreadsheetId":"Menu_0123456789","properties":{"title":"Cookbook"},"sheets":[{"properties":{"sheetId":7,"title":"Việt '帳簿'","gridProperties":{"rowCount":10,"columnCount":5}}}]}"""
    private fun engine(bytes: ByteArray, status: HttpStatusCode = HttpStatusCode.OK, length: String? = null) = MockEngine {
        respond(ByteReadChannel(bytes), status, if(length == null) headersOf(HttpHeaders.ContentType,"application/json")
            else headersOf(HttpHeaders.ContentType to listOf("application/json"),HttpHeaders.ContentLength to listOf(length)))
    }
    @Test fun metadataRequestsOnlyRelevantFieldsAndPreservesTabIdentity() = runTest {
        var url = ""; var bearer = ""
        val mock = MockEngine { request ->
            url = request.url.toString(); bearer = request.headers[HttpHeaders.Authorization].orEmpty()
            respond(metadata, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType,"application/json"))
        }
        val client = SheetsClient(mock,"synthetic-token")
        try {
            val result = client.getSpreadsheet("Menu_0123456789")
            assertEquals(7, result.sheets.single().properties.sheetId)
            assertEquals("Việt '帳簿'", result.sheets.single().properties.title)
            assertEquals("Bearer synthetic-token",bearer)
            assertTrue("fields=" in url); assertFalse("includeGridData" in url)
        } finally { client.close() }
    }
    @Test fun mismatchedMetadataCannotBindRecipesToAnotherMenu() = runTest {
        val client = SheetsClient(engine(metadata.toByteArray()),"synthetic")
        try { assertFails { client.getSpreadsheet("Other_0123456789") } } finally { client.close() }
    }
    @Test fun contentLengthAndStreamingBodyBudgetsRejectBeforeJsonDecode() = runTest {
        for (mock in listOf(engine(metadata.toByteArray(), length="200001"), engine(ByteArray(200001) { 32 }))) {
            val client = SheetsClient(mock,"synthetic")
            try { assertFailsWith<IllegalArgumentException> { client.getValues("Menu_0123456789","A1") } }
            finally { client.close() }
        }
    }
    @Test fun malformedUtf8AndInvalidJsonAreRejectedInsteadOfReplacementDecoded() = runTest {
        for (bytes in listOf(byteArrayOf(0xC3.toByte(),0x28), "{not JSON}".encodeToByteArray())) {
            val client = SheetsClient(engine(bytes),"synthetic")
            try { assertFails { client.getValues("Menu_0123456789","A1") } } finally { client.close() }
        }
    }
    @Test fun metadataRefusalsKeep401And403Distinct() = runTest {
        val unauthorized=SheetsClient(engine("{}".encodeToByteArray(),HttpStatusCode.Unauthorized),"synthetic")
        val forbidden=SheetsClient(engine("{}".encodeToByteArray(),HttpStatusCode.Forbidden),"synthetic")
        try {
            assertFailsWith<SheetsUnauthorizedException> { unauthorized.getSpreadsheet("Menu_0123456789") }
            assertFailsWith<SheetsForbiddenException> { forbidden.getSpreadsheet("Menu_0123456789") }
        } finally { unauthorized.close(); forbidden.close() }
    }
    @Test fun streamingBudgetRefusesWithoutWaitingForUnboundedResponseEof() = runTest {
        withContext(Dispatchers.Default) {
            val channel=ByteChannel(autoFlush=true)
            val producer=launch { channel.writeFully(ByteArray(200001) { 32 }); awaitCancellation() }
            val mock=MockEngine { respond(channel,HttpStatusCode.OK,headersOf(HttpHeaders.ContentType,"application/json")) }
            val client=SheetsClient(mock,"synthetic")
            try {
                withTimeout(5000) { assertFailsWith<IllegalArgumentException> { client.getValues("Menu_0123456789","A1") } }
            } finally { channel.cancel(null); producer.cancelAndJoin(); client.close() }
        }
    }
    @Test fun transportTimeoutStaysFailureAndCloseReleasesOwnedEngine() = runTest {
        val mock = MockEngine { throw HttpRequestTimeoutException(it) }
        var closed=false
        val owned=object:HttpClientEngine by mock { override fun close() { closed=true; mock.close() } }
        val client = SheetsClient(owned,"synthetic")
        try { assertFailsWith<HttpRequestTimeoutException> { client.getValues("Menu_0123456789","A1") } }
        finally { client.close() }
        assertTrue(closed)
    }
}
