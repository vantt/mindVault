package com.passchef.sheets

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ConnectionCheckTest {
    private class OwnedEngine(val delegate: HttpClientEngine) : HttpClientEngine by delegate {
        var closed = false
        override fun close() { closed = true; delegate.close() }
    }

    private fun withConfig(body: (java.io.File) -> Unit) {
        val directory = Files.createTempDirectory("synthetic-menu-check").toFile()
        try {
            val file = directory.resolve("config.properties")
            file.writeText("clientId=synthetic-client\nclientSecret=synthetic-seasoning\nspreadsheetId=id\nrange=Món 🍳!A1:B2\n")
            body(file)
        } finally { directory.deleteRecursively() }
    }

    @Test
    fun utf8ConfigPreservesVietnameseAndEmoji() = withConfig { file ->
        assertEquals("Món 🍳!A1:B2", assertIs<ConfigLoadResult.Loaded>(loadSmokeTestConfig(file)).config.range)
    }

    @Test
    fun malformedAndUnreadableConfigHaveSafeErrors() = withConfig { file ->
        file.writeText("clientId=synthetic\nrange=" + "\\uZZZZ")
        assertIs<ConfigLoadResult.Missing>(loadSmokeTestConfig(file))
        assertIs<ConfigLoadResult.Missing>(loadSmokeTestConfig(file.parentFile))
    }

    @Test
    fun missingConfigFailsWithoutStartingOAuth() {
        val directory = Files.createTempDirectory("missing-menu-check").toFile()
        try {
            val messages = mutableListOf<String>()
            assertEquals(1, runConnectionCheck(directory.resolve("absent"), { error("no engine") }, report = messages::add))
            assertTrue(messages.single().contains("clientId"))
        } finally { directory.deleteRecursively() }
    }

    @Test
    fun successfulReadClosesBothOwnedEngines() = withConfig { file ->
        val engines = mutableListOf<OwnedEngine>()
        val messages = mutableListOf<String>()
        assertEquals(0, runConnectionCheck(file, {
            OwnedEngine(MockEngine { respond("{\"values\":[[\"synthetic-recipe\"]]}", HttpStatusCode.OK,
                headersOf("Content-Type", "application/json")) }).also { engines.add(it) }
        }, { _, _ -> "synthetic-token" }, messages::add))
        assertEquals(2, engines.size)
        assertTrue(engines.all { it.closed })
        assertEquals(listOf("Read 1 row(s) from the Menu."), messages)
    }

    @Test
    fun unauthorizedAndForbiddenFailWithoutLeakingBodies() = withConfig { file ->
        for (status in listOf(HttpStatusCode.Unauthorized, HttpStatusCode.Forbidden)) {
            val engines = mutableListOf<OwnedEngine>()
            val messages = mutableListOf<String>()
            assertEquals(1, runConnectionCheck(file, {
                OwnedEngine(MockEngine { respond("{\"error\":{\"code\":${status.value},\"message\":\"synthetic-seasoning\"}}",
                    status, headersOf("Content-Type", "application/json")) }).also { engines.add(it) }
            }, { _, _ -> "synthetic-token" }, messages::add))
            assertTrue(engines.all { it.closed })
            assertTrue(messages.none { it.contains("synthetic-seasoning") || it.contains("synthetic-token") })
        }
    }

    @Test
    fun tokenRequestUsesPkceAndErrorsAreSanitized() = withConfig { file ->
        for ((status, json) in listOf(
            HttpStatusCode.BadRequest to "{\"error\":\"synthetic-seasoning\"}",
            HttpStatusCode.OK to "{\"access_token\":\"\"}",
            HttpStatusCode.OK to "{\"access_token\":\"synthetic-token\",\"token_type\":\"invalid\"}",
            HttpStatusCode.OK to "invalid synthetic-token"
        )) {
            val engines = mutableListOf<OwnedEngine>()
            val messages = mutableListOf<String>()
            assertEquals(1, runConnectionCheck(file, {
                OwnedEngine(MockEngine { request ->
                    val body = request.body.toByteArray().decodeToString()
                    assertTrue(body.contains("code_verifier=synthetic-verifier"))
                    assertTrue(body.contains("grant_type=authorization_code"))
                    respond(json, status, headersOf("Content-Type", "application/json"))
                }).also { engines.add(it) }
            }, { config, client -> exchangeOAuthCode(config, client, "synthetic-code",
                "http://127.0.0.1:1/callback", "synthetic-verifier") }, messages::add))
            assertEquals(1, engines.size)
            assertTrue(engines.single().closed)
            assertTrue(messages.none { it.contains("synthetic-seasoning") || it.contains("synthetic-token") })
        }
    }

    @Test
    fun tokenExchangeSuccessWiresBearerTokenToMenuRead() = withConfig { file ->
        val engines = mutableListOf<OwnedEngine>()
        val messages = mutableListOf<String>()
        val result = runConnectionCheck(file, {
            val tokenExchange = engines.isEmpty()
            OwnedEngine(MockEngine { request ->
                val json = if (tokenExchange) {
                    assertEquals("oauth2.googleapis.com", request.url.host)
                    assertTrue(request.body.toByteArray().decodeToString().contains("code_verifier=synthetic-verifier"))
                    "{\"access_token\":\"synthetic-token\",\"token_type\":\"Bearer\"}"
                } else {
                    assertEquals("sheets.googleapis.com", request.url.host)
                    assertEquals("Bearer synthetic-token", request.headers["Authorization"])
                    "{\"values\":[[\"synthetic-recipe\"]]}"
                }
                respond(json, HttpStatusCode.OK, headersOf("Content-Type", "application/json"))
            }).also { engines.add(it) }
        }, { config, client -> exchangeOAuthCode(config, client, "synthetic-code",
            "http://127.0.0.1:1/callback", "synthetic-verifier") }, messages::add)
        assertEquals(0, result, messages.joinToString())
        assertEquals(2, engines.size)
        assertTrue(engines.all { it.closed })
        assertEquals(listOf("Read 1 row(s) from the Menu."), messages)
    }

    @Test
    fun unexpectedErrorsCloseTheEngineWithoutLeakingExceptionText() = withConfig { file ->
        val engine = OwnedEngine(MockEngine { error("not called") })
        val messages = mutableListOf<String>()
        assertEquals(1, runConnectionCheck(file, { engine }, { _, _ -> error("synthetic-seasoning") }, messages::add))
        assertTrue(engine.closed)
        assertTrue(!messages.single().contains("synthetic-seasoning"))
    }
}
