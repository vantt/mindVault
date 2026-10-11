package com.passchef.sheets

/**
 * Minimal, dependency-free percent-encoding for a single URL path segment.
 * Sheets A1-notation ranges (e.g. `"Sheet 1!A1:B2"`) contain spaces, `!`, and
 * `:` that are not safe unencoded in a path segment; this avoids pulling in a
 * JVM-only encoder so the client stays usable from `commonMain`.
 */
internal fun String.encodeAsPathSegment(): String {
    val safe = ('a'..'z') + ('A'..'Z') + ('0'..'9') + listOf('-', '_', '.', '~')
    val builder = StringBuilder()
    for (byte in encodeToByteArray()) {
        val value = byte.toInt() and 0xFF
        if (value.toChar() in safe) {
            builder.append(value.toChar())
        } else {
            val hex = value.toString(16).uppercase()
            builder.append('%').append(if (hex.length == 1) "0$hex" else hex)
        }
    }
    return builder.toString()
}
