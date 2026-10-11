package dev.passchef.mobile.recipe

/** Default JS case mapping pinned to repo E2E Chromium, independent of Android/JDK tables. */
internal fun jsUppercase(text: String): String = buildString {
    var index = 0
    while (index < text.length) {
        val first = text[index++]
        val scalar = if (first in '\uD800'..'\uDBFF' && index < text.length &&
            text[index] in '\uDC00'..'\uDFFF') {
            0x10000 + ((first.code - 0xD800) shl 10) + (text[index++].code - 0xDC00)
        } else first.code
        val mapped = uppercaseMappings[scalar]
        if (mapped == null) appendScalar(scalar) else append(mapped)
    }
}

private val uppercaseMappings: Map<Int, String> by lazy {
    UnicodeUppercaseData.chunks.joinToString("").split(';').filter { it.isNotEmpty() }.associate { row ->
        val parts = row.split('=')
        parts[0].toInt(16) to buildString {
            for (hex in parts[1].split(',')) appendScalar(hex.toInt(16))
        }
    }
}

/** Preserve isolated UTF-16 surrogates; only valid supplementary pairs are combined. */
private fun StringBuilder.appendScalar(scalar: Int) {
    if (scalar <= 0xFFFF) append(scalar.toChar())
    else {
        val supplementary = scalar - 0x10000
        append((0xD800 + (supplementary ushr 10)).toChar())
        append((0xDC00 + (supplementary and 0x3FF)).toChar())
    }
}
