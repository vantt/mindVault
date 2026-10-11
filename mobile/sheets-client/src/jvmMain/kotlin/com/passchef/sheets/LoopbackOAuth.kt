package com.passchef.sheets

import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import java.net.URLDecoder
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

internal class ConnectionCheckFailure(message: String) : Exception(message)

internal fun randomOAuthValue(): String {
    val bytes = ByteArray(32).also { SecureRandom().nextBytes(it) }
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
}

internal fun pkceChallenge(verifier: String): String = Base64.getUrlEncoder().withoutPadding()
    .encodeToString(MessageDigest.getInstance("SHA-256").digest(verifier.toByteArray(Charsets.US_ASCII)))

/** One authorization attempt. Only a valid state-bound callback completes it. */
internal class LoopbackOAuthSession(
    internal val state: String = randomOAuthValue(),
    internal val verifier: String = randomOAuthValue()
) : AutoCloseable {
    private val server = HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0)
    private val received = CountDownLatch(1)
    @Volatile private var code: String? = null
    @Volatile private var denied = false
    private val callbackPath = "/oauth2callback"
    val redirectUri = "http://127.0.0.1:${server.address.port}$callbackPath"

    init {
        server.createContext("/") { exchange ->
            val params = parseCallback(exchange.requestURI.rawQuery)
            val valid = exchange.requestMethod == "GET" && exchange.requestURI.path == callbackPath &&
                params != null && MessageDigest.isEqual(
                    state.toByteArray(Charsets.UTF_8), params["state"].orEmpty().toByteArray(Charsets.UTF_8)
                ) && ((params.containsKey("code") && !params.containsKey("error") && params["code"]!!.isNotBlank()) ||
                    (params.containsKey("error") && !params.containsKey("code") && params["error"]!!.isNotBlank()))
            var accepted = false
            if (valid) {
                synchronized(this) {
                    if (received.count > 0) {
                        code = params!!["code"]
                        denied = params["error"] != null
                        received.countDown()
                        accepted = true
                    }
                }
            }
            val body = if (accepted) "Sign-in response received. Return to the connection check."
                else "Invalid sign-in callback. Continue the original sign-in request."
            val bytes = body.toByteArray(Charsets.UTF_8)
            try {
                exchange.sendResponseHeaders(if (accepted) 200 else 400, bytes.size.toLong())
                exchange.responseBody.use { it.write(bytes) }
            } finally {
                exchange.close()
            }
        }
        server.start()
    }

    fun authorizationUrl(clientId: String): String {
        val parameters = linkedMapOf(
            "client_id" to clientId, "redirect_uri" to redirectUri, "response_type" to "code",
            "scope" to "https://www.googleapis.com/auth/spreadsheets", "state" to state,
            "code_challenge" to pkceChallenge(verifier), "code_challenge_method" to "S256",
            "prompt" to "consent"
        )
        return "https://accounts.google.com/o/oauth2/v2/auth?" + parameters.entries.joinToString("&") {
            "${it.key}=${URLEncoder.encode(it.value, "UTF-8")}"
        }
    }

    fun awaitCode(timeout: Long = 5, unit: TimeUnit = TimeUnit.MINUTES): String {
        if (!received.await(timeout, unit)) {
            throw ConnectionCheckFailure("Sign-in timed out. Re-run the connection check and complete consent.")
        }
        if (denied) throw ConnectionCheckFailure("Sign-in was denied. Re-run and grant the requested permission.")
        return code ?: throw ConnectionCheckFailure("Sign-in returned no authorization code. Re-run the check.")
    }

    override fun close() = server.stop(0)
}

private fun parseCallback(rawQuery: String?): Map<String, String>? {
    if (rawQuery.isNullOrEmpty()) return null
    return try {
        val values = linkedMapOf<String, String>()
        for (pair in rawQuery.split("&")) {
            val parts = pair.split("=", limit = 2)
            if (parts.size != 2) return null
            val key = URLDecoder.decode(parts[0], "UTF-8")
            if (values.containsKey(key)) return null
            values[key] = URLDecoder.decode(parts[1], "UTF-8")
        }
        values
    } catch (_: IllegalArgumentException) {
        null
    }
}
