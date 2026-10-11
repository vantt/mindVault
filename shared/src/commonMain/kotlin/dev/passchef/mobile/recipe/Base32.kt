package dev.passchef.mobile.recipe

internal fun base32(bytes: ByteArray): String {
    var bits = 0
    var value = 0
    return buildString {
        for (byte in bytes) {
            value = (value shl 8) or (byte.toInt() and 255)
            bits += 8
            while (bits >= 5) {
                append(TAG_ALPHABET[(value ushr (bits - 5)) and 31])
                bits -= 5
            }
        }
        if (bits > 0) append(TAG_ALPHABET[(value shl (5 - bits)) and 31])
    }
}

/** Web TextEncoder replaces isolated UTF-16 surrogates with U+FFFD. */
internal fun webUnicode(text: String): String = buildString {
    var index = 0
    while (index < text.length) {
        val ch = text[index++]
        if (ch in '\uD800'..'\uDBFF') {
            if (index < text.length && text[index] in '\uDC00'..'\uDFFF') {
                append(ch); append(text[index++])
            } else append('\uFFFD')
        } else if (ch in '\uDC00'..'\uDFFF') append('\uFFFD')
        else append(ch)
    }
}
