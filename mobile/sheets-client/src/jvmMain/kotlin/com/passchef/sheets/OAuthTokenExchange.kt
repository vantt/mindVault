package com.passchef.sheets

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Parameters
import kotlinx.serialization.Serializable

@Serializable
private data class TokenResponse(val access_token: String, val token_type: String = "Bearer")

internal suspend fun exchangeOAuthCode(
    config: SmokeTestConfig, httpClient: HttpClient, code: String, redirectUri: String, verifier: String
): String {
    val response = httpClient.post("https://oauth2.googleapis.com/token") {
        setBody(FormDataContent(Parameters.build {
            append("code", code)
            append("client_id", config.clientId)
            append("client_secret", config.clientSecret)
            append("redirect_uri", redirectUri)
            append("grant_type", "authorization_code")
            append("code_verifier", verifier)
        }))
    }
    if (response.status.value !in 200..299) {
        throw ConnectionCheckFailure(
            "Google token exchange failed (HTTP ${response.status.value}). " +
                "Check the Desktop client configuration and repeat sign-in."
        )
    }
    val token = try {
        response.body<TokenResponse>()
    } catch (_: Exception) {
        throw ConnectionCheckFailure("Google returned an invalid token response. Repeat sign-in.")
    }
    if (token.access_token.isBlank() || !token.token_type.equals("Bearer", ignoreCase = true)) {
        throw ConnectionCheckFailure("Google returned an invalid token response. Repeat sign-in.")
    }
    return token.access_token
}

internal suspend fun authorizeWithLoopback(config: SmokeTestConfig, client: HttpClient): String =
    LoopbackOAuthSession().use { session ->
        println("Open this URL in your browser to sign in and grant access:")
        println(session.authorizationUrl(config.clientId))
        exchangeOAuthCode(config, client, session.awaitCode(), session.redirectUri, session.verifier)
    }
