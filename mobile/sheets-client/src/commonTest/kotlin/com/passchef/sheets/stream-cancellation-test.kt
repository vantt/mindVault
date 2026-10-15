package com.passchef.sheets

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteChannel
import io.ktor.utils.io.writeFully
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** Incomplete synthetic HTTP streams test client cancellation, never Google consent/provider acceptance. */
class StreamCancellationTest {
    private suspend fun cancelDuringBody(status: HttpStatusCode, metadata: Boolean) = withContext(Dispatchers.Default) {
        val channel = ByteChannel(autoFlush = true)
        channel.writeFully(byteArrayOf('{'.code.toByte())) // No EOF: cancellation must interrupt a real body read.
        val headersReturned = CompletableDeferred<Unit>()
        val outcome = CompletableDeferred<Throwable?>()
        val client = SheetsClient(MockEngine {
            headersReturned.complete(Unit)
            respond(channel, status, headersOf(HttpHeaders.ContentType, "application/json"))
        }, "synthetic-cancellation-grant")
        val request = launch {
            outcome.complete(runCatching {
                if (metadata) client.getSpreadsheet("Menu_0123456789")
                else client.getValues("Menu_0123456789", "Test!A1:C2")
            }.exceptionOrNull())
        }
        try {
            withTimeout(5_000) {
                headersReturned.await()
                while (channel.availableForRead != 0) yield() // Consumer has entered the incomplete body.
            }
            assertTrue(request.isActive, "Request remains suspended before owner cancellation")
            assertFalse(outcome.isCompleted, "No result before the incomplete body is cancelled")
            request.cancel(CancellationException("owned request cancelled"))
            withTimeout(5_000) { request.join() }
            val failure = withTimeout(5_000) { outcome.await() }
            assertIs<CancellationException>(failure, "Cancellation during status ${status.value} body must propagate")
            assertTrue(channel.isClosedForRead, "Cancelled response body must be released")
        } finally {
            request.cancel(); channel.cancel(null); client.close()
            withTimeout(5_000) { request.join() }
        }
    }
    @Test fun cancelledValuesSuccessBodyStaysCancelled() = runTest { cancelDuringBody(HttpStatusCode.OK, false) }
    @Test fun cancelledMetadataSuccessBodyStaysCancelled() = runTest { cancelDuringBody(HttpStatusCode.OK, true) }
    @Test fun cancelledUnauthorizedBodyDoesNotBecomeCredentialFailure() = runTest { cancelDuringBody(HttpStatusCode.Unauthorized, false) }
    @Test fun cancelledUnauthorizedMetadataBodyStaysCancelled() = runTest { cancelDuringBody(HttpStatusCode.Unauthorized, true) }
    @Test fun cancelledForbiddenValuesBodyStaysCancelled() = runTest { cancelDuringBody(HttpStatusCode.Forbidden, false) }
    @Test fun cancelledForbiddenBodyDoesNotBecomeAccessFailure() = runTest { cancelDuringBody(HttpStatusCode.Forbidden, true) }
}
