package dev.passchef.mobile.recipe

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

@Serializable private data class OracleRow(
    val recipeText: String, val spice: String, val menuId: String, val tag: String, val password: String
)
@Serializable private data class OracleFixture(val browserVersion: String, val rows: List<OracleRow>)

class RecipeInteropTest {
    @Test fun matchesRealJsUnicodeAndTagVectors() {
        val text = javaClass.getResource("/recipe-reference-vectors.json")!!.readText()
        val vectors = Json { ignoreUnknownKeys = true }.decodeFromString<OracleFixture>(text)
        assertEquals("143.0.7499.4", vectors.browserVersion)
        assertEquals(56, vectors.rows.size)
        for (row in vectors.rows) {
            val recipe = RecipeParser.parse(row.recipeText)!!
            val codec = TagCodec()
            assertEquals(row.tag, codec.compute(recipe, row.spice, row.menuId))
            val kitchen = GeneratePassword(MapSeasoningProvider(mapOf(1 to row.spice)), codec)
            assertEquals(row.password, kitchen.execute(row.recipeText + "." + row.tag, row.menuId).password)
        }
    }

    @Test fun rejectsInvalidGrammarAndIndices() {
        for (text in listOf(null, "", "gmáil#1", "gmаil#1", "mail#0", "mail#6", "mail#1_v2", "mail#1.oooo")) {
            assertNull(RecipeParser.parse(text))
        }
    }

    @Test fun duplicateModifiersApplyOnceAndCanonicalOrderMatches() {
        val kitchen = GeneratePassword(MapSeasoningProvider(mapOf(1 to "Basic*")), TagCodec())
        assertEquals(kitchen.execute("mail#1?!_~").password, kitchen.execute("mail#1~~??!!__").password)
        assertEquals(RecipeParser.parse("mail#1!_")!!.canonical(), RecipeParser.parse("mail#1_!")!!.canonical())
    }

    @Test fun tagsRejectMissingAndWrongContext() {
        val kitchen = GeneratePassword(MapSeasoningProvider(mapOf(1 to "Basic*")), TagCodec())
        val recipe = RecipeParser.parse("mail#1")!!
        val tag = TagCodec().compute(recipe, "Basic*", "menu-A")
        for (menu in listOf(null, "menu-B")) {
            assertEquals("RECIPE_MISMATCH", assertFailsWith<RecipeProfileMismatchException> {
                kitchen.execute("mail#1.$tag", menu)
            }.code)
        }
        assertFailsWith<RecipeProfileMismatchException> { kitchen.execute("mail#1.zzzz", "menu-A") }
        assertEquals("Basic*mail", kitchen.execute("mail#1.$tag", "menu-A").password)
    }

    @Test fun legacyWarningAndSharedEffectiveSpicePreserveJsBehavior() {
        val own = GeneratePassword(MapSeasoningProvider(mapOf(1 to "Basic*")), TagCodec())
        assertEquals("legacy_no_tag", own.execute("mail#1", "menu-A").warning)
        assertNull(own.execute("mail#1").warning)
        val provider = SeasoningProvider { _, menu -> Seasoning("RAW", "BOUND", "Team", true, menuId = menu) }
        val shared = GeneratePassword(provider, TagCodec())
        val tag = TagCodec().compute(RecipeParser.parse("mail#1")!!, "RAW", "menu-A")
        assertEquals("BOUNDmail", shared.execute("mail#1.$tag", "menu-A").password)
        assertEquals(true, shared.execute("mail#1.$tag", "menu-A").isShared)
    }
}
