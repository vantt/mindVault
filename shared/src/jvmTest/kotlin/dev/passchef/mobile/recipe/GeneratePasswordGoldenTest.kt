package dev.passchef.mobile.recipe

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/** Literal copies of the 12 approved JS assertions; JS tests remain immutable. */
class GeneratePasswordGoldenTest {
    private val kitchen = GeneratePassword(MapSeasoningProvider(mapOf(
        1 to "Basic*", 2 to "Secure#", 3 to "Trade&", 4 to "Ultra$")), TagCodec())
    @Test fun prefix() { assertEquals("Basic*r4nd0m", kitchen.execute("r4nd0m#1").password) }
    @Test fun suffix() { assertEquals("r4nd0mBasic*", kitchen.execute("r4nd0m$1").password) }
    @Test fun middle() { assertEquals("r4nSecure#d0m", kitchen.execute("r4nd0m@2").password) }
    @Test fun interleave() { assertEquals("rT4rnadd0em&", kitchen.execute("r4nd0m%3").password) }
    @Test fun pairs() { assertEquals("r4Trndad0me&", kitchen.execute("r4nd0m^3").password) }
    @Test fun flipPosition() { assertEquals("r4nd0mBasic*", kitchen.execute("r4nd0m#1_").password) }
    @Test fun uppercase() { assertEquals("BASIC*r4nd0m", kitchen.execute("r4nd0m#1!").password) }
    @Test fun stripSpecial() { assertEquals("Trader4nd0m", kitchen.execute("r4nd0m#3~").password) }
    @Test fun reverseSpice() { assertEquals("*cisaBr4nd0m", kitchen.execute("r4nd0m#1?").password) }
    @Test fun multipleModifiers() { assertEquals("r4nd0mBASIC*", kitchen.execute("r4nd0m#1_!").password) }
    @Test fun invalidRecipe() {
        assertEquals("Invalid recipe format", assertFailsWith<IllegalArgumentException> { kitchen.execute("invalid") }.message)
    }
    @Test fun missingSpice() {
        assertEquals("Secret #5 not found", assertFailsWith<IllegalArgumentException> { kitchen.execute("r4nd0m#5") }.message)
    }
}
