package com.passchef.sheets

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SheetsClientRegressionTest {
    @Test
    fun unicodeRangeAndSpreadsheetIdAreSinglePathSegments() = runTest {
        val client = SheetsClient(MockEngine { request ->
            assertEquals(
                "/v4/spreadsheets/sheet%2Fid/values/%27M%C3%B3n%20%F0%9F%8D%B3%20%2B%25%23%21A1%3AB2",
                request.url.encodedPath
            )
            assertEquals("Bearer synthetic-token", request.headers[HttpHeaders.Authorization])
            respond("{}", HttpStatusCode.OK, headersOf("Content-Type", "application/json"))
        }, "synthetic-token")
        try { client.getValues("sheet/id", "'Món 🍳 +%#!A1:B2") } finally { client.close() }
    }

    @Test
    fun appendAndUpdatePreserveLiteralTextAndAllowExplicitUserEntered() = runTest {
        val options = mutableListOf<String?>()
        val methods = mutableListOf<String>()
        val client = SheetsClient(MockEngine { request ->
            options.add(request.url.parameters["valueInputOption"])
            methods.add(request.method.value)
            assertEquals("Bearer synthetic-token", request.headers[HttpHeaders.Authorization])
            val body = (request.body as TextContent).text
            assertEquals(listOf("00123", "=A1", "mail#1"),
                Json.parseToJsonElement(body).jsonObject["values"]!!.jsonArray.single().jsonArray.map { it.jsonPrimitive.content })
            respond("{}", HttpStatusCode.OK, headersOf("Content-Type", "application/json"))
        }, "synthetic-token")
        try {
            val values = listOf(listOf("00123", "=A1", "mail#1"))
            client.appendValues("id", "Menu!A1:C2", values)
            client.updateValues("id", "Menu!A1:C2", values)
            client.updateValues("id", "Menu!A1:C2", values, "USER_ENTERED")
            assertEquals(listOf<String?>("RAW", "RAW", "USER_ENTERED"), options)
            assertEquals(listOf("POST", "PUT", "PUT"), methods)
        } finally { client.close() }
    }

    @Test
    fun invalidInputOptionNeverSendsARequest() = runTest {
        val client = SheetsClient(MockEngine { error("must not send") }, "synthetic-token")
        try {
            assertFailsWith<IllegalArgumentException> { client.appendValues("id", "A1", emptyList(), "RAW&range=other") }
            assertFailsWith<IllegalArgumentException> { client.updateValues("id", "A1", emptyList(), "UNKNOWN") }
        } finally { client.close() }
    }
}
