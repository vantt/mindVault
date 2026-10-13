package dev.passchef.mobile.cookbook

import dev.passchef.mobile.recipe.GeneratePassword
import dev.passchef.mobile.recipe.MapSeasoningProvider
import dev.passchef.mobile.recipe.TagCodec
import dev.passchef.mobile.vault.SessionClock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Real shared policies with caller-supplied monotonic time; no Android/auth/UI proof. */
class DishSessionRegressionTest {
    private fun cook(): String = GeneratePassword(
        MapSeasoningProvider(mapOf(1 to "aB!*")), TagCodec()
    ).execute("abcd#1").password

    @Test fun revealExpiryKeepsMaskedDishAvailableInFreshSession() {
        val session = SessionClock().apply { begin(1_000) }
        val dish = DishLifetime()
        assertTrue(dish.publish(dish.invalidate(), cook(), !session.expired(1_000)))
        assertTrue(dish.ready(!session.expired(1_000)))
        assertNull(dish.visible(1_000, !session.expired(1_000)))
        dish.reveal(2_000, !session.expired(2_000))
        assertEquals("aB!*abcd", dish.visible(11_999, !session.expired(11_999)))

        for (now in listOf(12_000L, 12_500L)) {
            assertFalse(session.expired(now))
            assertNull(dish.visible(now, !session.expired(now)))
            assertTrue(dish.ready(!session.expired(now)))
            assertEquals("aB!*abcd", dish.forCopy(!session.expired(now)))
        }
    }

    @Test fun repeatedRevealExtendsVisibilityWithoutDiscardingMaskedDish() {
        val session = SessionClock().apply { begin(1_000) }
        val dish = DishLifetime()
        assertTrue(dish.publish(dish.invalidate(), cook(), !session.expired(1_000)))
        dish.reveal(2_000, !session.expired(2_000))
        dish.reveal(11_000, !session.expired(11_000))
        assertEquals("aB!*abcd", dish.visible(12_000, !session.expired(12_000)))
        assertEquals("aB!*abcd", dish.visible(20_999, !session.expired(20_999)))
        assertNull(dish.visible(21_000, !session.expired(21_000)))
        assertTrue(dish.ready(!session.expired(21_000)))
        assertEquals("aB!*abcd", dish.forCopy(!session.expired(21_000)))
    }

    @Test fun idleDeadlineOverridesActiveRevealAndDoesNotAcceptLateCooking() {
        val session = SessionClock().apply { begin(1_000) }
        val dish = DishLifetime()
        val token = dish.invalidate()
        assertTrue(dish.publish(token, cook(), !session.expired(1_000)))
        // A policy reveal is not a session touch; actual UI touch handling is Android-owned.
        dish.reveal(300_999, !session.expired(300_999))
        assertEquals("aB!*abcd", dish.visible(300_999, !session.expired(300_999)))
        assertTrue(session.expired(301_000))
        assertNull(dish.visible(301_000, !session.expired(301_000)))
        assertFalse(dish.ready(!session.expired(301_000)))
        assertNull(dish.forCopy(!session.expired(301_000)))
        assertFalse(dish.publish(token, "LateDish", !session.expired(301_000)))
    }

    @Test fun totalDeadlineOverridesRevealDespiteContinuedSessionTouches() {
        val session = SessionClock().apply {
            begin(1_000)
            touch(250_000)
            touch(500_000)
            touch(600_999)
        }
        val dish = DishLifetime()
        val token = dish.invalidate()
        assertTrue(dish.publish(token, cook(), !session.expired(600_999)))
        dish.reveal(600_999, !session.expired(600_999))
        assertEquals("aB!*abcd", dish.visible(600_999, !session.expired(600_999)))
        assertTrue(session.expired(601_000))
        assertNull(dish.visible(601_000, !session.expired(601_000)))
        assertFalse(dish.ready(!session.expired(601_000)))
        assertNull(dish.forCopy(!session.expired(601_000)))
        assertFalse(dish.publish(token, "LateDish", !session.expired(601_000)))
    }
}
