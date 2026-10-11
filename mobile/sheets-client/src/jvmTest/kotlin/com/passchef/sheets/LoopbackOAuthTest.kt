package com.passchef.sheets

import java.net.HttpURLConnection
import java.net.URI
import java.net.URLEncoder
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class LoopbackOAuthTest {
    private fun request(url: String, method: String = "GET"): Int {
        val connection = URI(url).toURL().openConnection() as HttpURLConnection
        connection.requestMethod = method
        connection.connectTimeout = 2000
        connection.readTimeout = 2000
        return try { connection.responseCode } finally { connection.disconnect() }
    }

    @Test
    fun pkceMatchesRfc7636KnownVector() {
        assertEquals("E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM",
            pkceChallenge("dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk"))
    }

    @Test
    fun attemptsHaveIndependentRandomStateAndVerifier() {
        LoopbackOAuthSession().use { first -> LoopbackOAuthSession().use { second ->
            assertNotEquals(first.state, second.state)
            assertNotEquals(first.verifier, second.verifier)
            assertEquals(43, first.verifier.length)
            val url = first.authorizationUrl("synthetic-client")
            assertTrue(url.contains("state=${first.state}"))
            assertTrue(url.contains("code_challenge=${pkceChallenge(first.verifier)}"))
            assertTrue(url.contains("code_challenge_method=S256"))
        } }
    }

    @Test
    fun invalidCallbacksCannotConsumeTheLegitimateAttempt() {
        LoopbackOAuthSession(state = "synthetic-state").use { session ->
            val uri = session.redirectUri
            for (query in listOf(
                "code=forged", "state=wrong&code=forged", "state=synthetic-state",
                "state=synthetic-state&state=synthetic-state&code=one",
                "state=synthetic-state&code=one&code=two",
                "state=synthetic-state&code=one&error=denied", "state=synthetic-state&code=one&error=",
                "state=synthetic-state&code=", "state=synthetic-state&bad"
            )) assertEquals(400, request("$uri?$query"))
            assertEquals(400, request("$uri?state=synthetic-state&code=forged", "POST"))
            assertEquals(400, request("${uri}extra?state=synthetic-state&code=forged"))
            val code = "synthetic+code%2F&=value"
            assertEquals(200, request("$uri?state=synthetic-state&code=${URLEncoder.encode(code, "UTF-8")}"))
            assertEquals(code, session.awaitCode(1, TimeUnit.SECONDS))
            assertEquals(400, request("$uri?state=synthetic-state&code=replay"))
        }
    }

    @Test
    fun deniedConsentAndTimeoutGiveSanitizedFailures() {
        LoopbackOAuthSession(state = "synthetic-state").use { session ->
            assertEquals(200, request("${session.redirectUri}?state=synthetic-state&error=private-value"))
            val failure = assertFailsWith<ConnectionCheckFailure> { session.awaitCode(1, TimeUnit.SECONDS) }
            assertTrue(failure.message!!.contains("denied") && !failure.message!!.contains("private-value"))
        }
        LoopbackOAuthSession().use { session ->
            assertFailsWith<ConnectionCheckFailure> { session.awaitCode(1, TimeUnit.MILLISECONDS) }
        }
    }
}
