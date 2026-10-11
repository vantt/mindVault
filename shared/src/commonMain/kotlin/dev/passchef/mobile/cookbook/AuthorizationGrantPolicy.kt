package dev.passchef.mobile.cookbook

object AuthorizationGrantPolicy {
    const val SHEETS_SCOPE = "https://www.googleapis.com/auth/spreadsheets"
    fun usable(accessToken: String?, scopes: List<String>): Boolean =
        !accessToken.isNullOrBlank() && accessToken.length <= 16384 && SHEETS_SCOPE in scopes
}
