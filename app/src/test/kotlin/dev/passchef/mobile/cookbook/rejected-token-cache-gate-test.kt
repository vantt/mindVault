package dev.passchef.mobile.cookbook

import org.junit.Assert.*
import org.junit.Test

/** Pure ordering checks; these do not claim Google SDK or real-device authorization acceptance. */
class RejectedTokenCacheGateTest {
    @Test fun normalConnectDoesNotClearAndRejectedTokenBlocksUntilSuccess() {
        val callbacks = mutableListOf<(Boolean) -> Unit>()
        val tokens = mutableListOf<String>()
        val gate = RejectedTokenCacheGate { token, done -> tokens += token; callbacks += done }
        val results = mutableListOf<Boolean>()
        gate.beforeAuthorization(results::add)
        assertEquals(listOf(true), results)
        assertTrue(tokens.isEmpty())
        gate.reject("rejected")
        gate.beforeAuthorization(results::add)
        assertEquals(listOf("rejected"), tokens)
        assertEquals(listOf(true), results)
        callbacks.single()(true)
        assertEquals(listOf(true, true), results)
        gate.beforeAuthorization(results::add)
        assertEquals(listOf(true, true, true), results)
        assertEquals(1, callbacks.size)
    }

    @Test fun failedClearFailsClosedAndOnlyNextExplicitConnectRetries() {
        val callbacks = mutableListOf<(Boolean) -> Unit>()
        val gate = RejectedTokenCacheGate { _, done -> callbacks += done }
        val results = mutableListOf<Boolean>()
        gate.reject("rejected")
        gate.beforeAuthorization(results::add)
        callbacks[0](false)
        assertEquals(listOf(false), results)
        assertEquals(1, callbacks.size)
        gate.beforeAuthorization(results::add)
        assertEquals(2, callbacks.size)
        assertEquals(listOf(false), results)
        callbacks[1](true)
        assertEquals(listOf(false, true), results)
    }

    @Test fun synchronousFailureRetainsInvalidationAndDuplicateCompletionCannotReleaseWaiter() {
        var calls = 0
        var callback: ((Boolean) -> Unit)? = null
        val gate = RejectedTokenCacheGate { _, done ->
            calls++
            if (calls == 1) throw IllegalStateException("unavailable")
            callback = done
        }
        val results = mutableListOf<Boolean>()
        gate.reject("rejected")
        gate.beforeAuthorization(results::add)
        assertTrue(results.isEmpty())
        val failed = callback!!
        failed(false)
        gate.beforeAuthorization(results::add)
        val newer = callback!!
        failed(true)
        assertEquals(listOf(false), results)
        newer(true)
        newer(false)
        assertEquals(listOf(false, true), results)
        assertEquals(3, calls)
    }

    @Test fun allRejectedTokensMustBeClearedBeforeAnyAuthorization() {
        val tokens = mutableListOf<String>()
        val callbacks = mutableListOf<(Boolean) -> Unit>()
        val gate = RejectedTokenCacheGate { token, done -> tokens += token; callbacks += done }
        val results = mutableListOf<Boolean>()
        gate.reject("first")
        gate.reject("second")
        gate.beforeAuthorization(results::add)
        assertEquals(listOf("first"), tokens)
        callbacks[0](true)
        assertEquals(listOf("first", "second"), tokens)
        assertTrue(results.isEmpty())
        callbacks[1](true)
        assertEquals(listOf(true), results)
    }

    @Test fun lateClearCannotAuthorizeAnInvalidatedOrLockedRequest() {
        lateinit var finish: (Boolean) -> Unit
        val gate = RejectedTokenCacheGate { _, done -> finish = done }
        val ownership = SourceOwnership()
        var ready = true
        var authorized = 0
        gate.reject("rejected")
        val request = ownership.begin("authorization")
        gate.beforeAuthorization { cleared -> if (cleared && ownership.accepts(request, ready)) authorized++ }
        ownership.invalidate(); ready = false
        finish(true)
        assertEquals(0, authorized)
        ready = true
        val fresh = ownership.begin("authorization")
        gate.beforeAuthorization { cleared -> if (cleared && ownership.accepts(fresh, ready)) authorized++ }
        assertEquals(1, authorized)
    }
}
