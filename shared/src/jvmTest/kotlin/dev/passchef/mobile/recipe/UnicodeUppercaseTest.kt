package dev.passchef.mobile.recipe

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

@Serializable private data class UppercaseMapping(val codePoint: Int, val uppercase: String)
@Serializable private data class UppercaseFixture(
    val browserVersion: String, val mappingSha256: String, val scalarCount: Int,
    val mappings: List<UppercaseMapping>
)

class UnicodeUppercaseTest {
    @Test fun newerSupplementaryScriptsMatchRealJsOracle() {
        for ((lower, upper) in listOf(0x10597 to 0x10570, 0x10D70 to 0x10D50)) {
            val spice = String(Character.toChars(lower))
            val kitchen = GeneratePassword(MapSeasoningProvider(mapOf(1 to spice)), TagCodec())
            assertEquals(String(Character.toChars(upper)) + "mix", kitchen.execute("mix#1!").password)
        }
    }

    @Test fun allScalarCodePointsMatchPinnedJsOracle() {
        val fixture = Json { ignoreUnknownKeys = true }.decodeFromString<UppercaseFixture>(
            javaClass.getResource("/unicode-uppercase-reference.json")!!.readText())
        assertEquals("143.0.7499.4", fixture.browserVersion)
        assertEquals(UnicodeUppercaseData.browserVersion, fixture.browserVersion)
        assertEquals(UnicodeUppercaseData.mappingSha256, fixture.mappingSha256)
        val mappings = fixture.mappings.associate { it.codePoint to it.uppercase }
        var checked = 0
        for (codePoint in 0..0x10FFFF) {
            if (codePoint in 0xD800..0xDFFF) continue
            val input = String(Character.toChars(codePoint))
            val expected = mappings[codePoint] ?: input
            val actual = jsUppercase(input)
            if (expected != actual) assertEquals(expected, actual, "Scalar U+${codePoint.toString(16)}")
            checked++
        }
        assertEquals(fixture.scalarCount, checked)
    }

    @Test fun defaultSpecialCasingExpandsWithoutLocaleRules() {
        assertEquals("SSFFIΙ\u0308\u0301ʼNJ\u030CΣII", jsUppercase("ßﬃΐŉǰςıi"))
    }

    @Test fun isolatedSurrogatesAndModifierOrderPreserveJsBehavior() {
        val spice = "a\uD800ß\uDC00z"
        assertEquals("A\uD800SS\uDC00Z", jsUppercase(spice))
        val kitchen = GeneratePassword(MapSeasoningProvider(mapOf(1 to spice)), TagCodec())
        assertEquals("Z\uDC00SS\uD800Amix", kitchen.execute("mix#1!?").password)
    }

    @Test fun lettersUnknownToCurrentChromiumRemainUnchanged() {
        // Node Unicode17 uppercases these; actual repo Chromium leaves them unchanged.
        for (codePoint in listOf(0xA7CF, 0x16EC0)) {
            val spice = String(Character.toChars(codePoint))
            val kitchen = GeneratePassword(MapSeasoningProvider(mapOf(1 to spice)), TagCodec())
            assertEquals(spice + "mix", kitchen.execute("mix#1!").password)
        }
    }
}
