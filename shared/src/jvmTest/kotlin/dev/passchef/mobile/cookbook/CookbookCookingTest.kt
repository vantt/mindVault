package dev.passchef.mobile.cookbook

import dev.passchef.mobile.recipe.*
import kotlin.test.*

class CookbookCookingTest {
    private val kitchen = GeneratePassword(MapSeasoningProvider(mapOf(1 to "aB!*")), TagCodec())
    @Test fun actualKitchenKeepsLiteralRecipeAndAllFivePositions() {
        val expected = mapOf('#' to "aB!*abcd", '$' to "abcdaB!*", '@' to "abaB!*cd", '%' to "aabBc!d*", '^' to "abaBcd!*")
        expected.forEach { (position, dish) -> assertEquals(dish, kitchen.execute("abcd${position}1").password) }
        assertEquals("BAabcd", kitchen.execute("abcd#1~?!").password)
    }
    @Test fun actualTagRequiresMatchingMenuAndRawSpice() {
        val recipe = RecipeParser.parse("abcd#1")!!
        val tag = TagCodec().compute(recipe, "aB!*", "Menu_0123456789")
        assertEquals("aB!*abcd", kitchen.execute("abcd#1.$tag", "Menu_0123456789").password)
        assertFailsWith<RecipeProfileMismatchException> { kitchen.execute("abcd#1.$tag", "Other_0123456789") }
        assertFailsWith<RecipeProfileMismatchException> { kitchen.execute("abcd#1.$tag") }
        assertFails { kitchen.execute("abcd#2") }
        assertFails { kitchen.execute("not a Recipe") }
        assertEquals("legacy_no_tag", kitchen.execute("abcd#1", "Menu_0123456789").warning)
    }
    @Test fun authorizationRequiresActualGrantedSheetsScopeAndBoundedToken() {
        assertTrue(AuthorizationGrantPolicy.usable("synthetic-grant", listOf(AuthorizationGrantPolicy.SHEETS_SCOPE)))
        assertFalse(AuthorizationGrantPolicy.usable("synthetic-grant", listOf("email")))
        assertFalse(AuthorizationGrantPolicy.usable(null, listOf(AuthorizationGrantPolicy.SHEETS_SCOPE)))
        assertFalse(AuthorizationGrantPolicy.usable("x".repeat(16385), listOf(AuthorizationGrantPolicy.SHEETS_SCOPE)))
    }
    @Test fun confidenceIsOnlyProposalAndProjectionRequiresExplicitMapping() {
        val rows = listOf(listOf("Tên", "Công thức", "Tài khoản"), listOf("Mail", "abc#1", "00123"),
            listOf("Bank", "def@1", "é"), listOf("Work", "ghi$1", "ユーザー"))
        assertEquals(1, MenuTable.proposal(rows, 2))
        assertEquals(ColumnRole.RECIPE, MenuTable.headerRole("Công thức"))
        val origin = MenuOrigin("Menu_0123456789", 7, "Accounts")
        val mapping = MenuMapping(origin, mapOf(ColumnRole.RECIPE to 1, ColumnRole.LABEL to 0, ColumnRole.USERNAME to 2), 2, rows.first())
        val entries = MenuTable.project(rows, mapping)
        assertEquals(listOf(2,3,4), entries.map { it.row })
        assertEquals(listOf("00123", "é", "ユーザー"), entries.map { it.username })
        assertEquals(origin, entries.first().origin)
    }
    @Test fun emptyHeaderlessRangeDoesNotInventRecipesOrDeleteUnrelatedMetadata() {
        val origin = MenuOrigin("Menu_0123456789", 0, "Empty")
        val mapping = MenuMapping(origin, mapOf(ColumnRole.RECIPE to 0))
        val prior = MenuSnapshot(mapping = mapping, entries = MenuTable.project(listOf(listOf("abc#1")), mapping), refreshedAt = 1)
        val empty = MenuTable.refresh(emptyList(), prior, 2)
        assertTrue(empty.entries.isEmpty()); assertEquals(2L, empty.refreshedAt)
        assertEquals(mapping, empty.mapping)
    }
}
