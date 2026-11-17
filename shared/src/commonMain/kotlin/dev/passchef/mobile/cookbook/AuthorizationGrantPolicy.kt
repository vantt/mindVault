package dev.passchef.mobile.cookbook

/** D1: the only Google scope is `drive.file` (files the user picks in the Google Picker). */
object AuthorizationGrantPolicy {
    const val DRIVE_FILE_SCOPE = "https://www.googleapis.com/auth/drive.file"
    fun usable(accessToken: String?, scopes: List<String>): Boolean =
        !accessToken.isNullOrBlank() && accessToken.length <= 16384 && DRIVE_FILE_SCOPE in scopes
}
