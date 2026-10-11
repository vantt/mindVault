package dev.passchef.mobile.vault

import java.util.concurrent.Executors
import kotlin.test.*

/** Ownership/control tokens only; no mocked biometric success or hardware proof. */
class WrapOperationStateTest {
    private class Owned : AutoCloseable {
        val key = ByteArray(32) { 7 }
        val spice = ByteArray(16) { 8 }
        var closes = 0
        override fun close() { closes++; key.fill(0); spice.fill(0) }
    }
    @Test fun onlyFirstApi33ExpiredDoFinalRetries() {
        for ((api, code, phase) in listOf(Triple(32, 15, "wrap-doFinal"), Triple(33, 2, "wrap-doFinal"),
            Triple(33, null, "wrap-doFinal"), Triple(33, 15, "record-commit"), Triple(33, 15, "key-retirement"))) {
            WrapOperationState(Owned()) {}.use { assertFalse(it.retry(api, code, phase)) }
        }
        WrapOperationState(Owned()) {}.use {
            assertTrue(it.retry(33, 15, "wrap-doFinal"))
            assertFalse(it.retry(33, 15, "wrap-doFinal"))
        }
    }
    @Test fun oldOrDuplicateCallbackCannotAuthorizeFreshAttempt() {
        WrapOperationState(Owned()) {}.use {
            val oldCipher = Any(); val first = it.newPrompt(oldCipher)
            assertTrue(it.authorize(first, oldCipher)); assertFalse(it.authorize(first, oldCipher))
            assertTrue(it.retry(33, 15, "wrap-doFinal"))
            assertFailsWith<IllegalStateException> { it.newPrompt(oldCipher) }
            val freshCipher = Any(); val second = it.newPrompt(freshCipher)
            assertFalse(it.authorize(first, oldCipher)); assertFalse(it.authorize(second, oldCipher))
            assertTrue(it.authorize(second, freshCipher)); assertFalse(it.authorize(second, freshCipher))
        }
    }
    @Test fun retainedDataSurvivesDelayThenCancelWipesAndRetiresOnlyDisposable() {
        val owned = Owned(); val deleted = mutableListOf<String>()
        val op = WrapOperationState(owned) { deleted += "new-disposable" }
        assertTrue(op.retry(33, 15, "wrap-doFinal"))
        var task: Runnable? = null; var removed = 0
        op.delayOwned({ runnable, delay -> assertEquals(5000L, delay); task = runnable; true }, { removed++ }) { fail("Cancelled delay ran") }
        assertTrue(owned.key.any { it != 0.toByte() }); assertEquals(0, owned.closes)
        op.close(); op.close(); task!!.run()
        assertEquals(1, removed); assertEquals(1, owned.closes); assertEquals(listOf("new-disposable"), deleted)
        assertTrue(owned.key.all { it == 0.toByte() }); assertTrue(owned.spice.all { it == 0.toByte() })
    }
    @Test fun lockDuringWorkerDefersWipingUntilWorkerReleasesInput() {
        val owned = Owned(); var deleted = 0; val op = WrapOperationState(owned) { deleted++ }
        assertTrue(op.beginWork()); op.close()
        assertFalse(op.active()); assertEquals(0, owned.closes); assertEquals(0, deleted)
        assertFalse(op.transferOwned()); op.endWork()
        assertEquals(1, owned.closes); assertEquals(1, deleted)
    }
    @Test fun atomicCommitAlreadyInProgressKeepsCommittedAliasOnBackground() {
        val owned = Owned(); var deleted = 0; val op = WrapOperationState(owned) { deleted++ }
        assertTrue(op.beginWork()); op.close(); op.markCommitted(); op.endWork()
        assertEquals(1, owned.closes); assertEquals(0, deleted)
    }
    @Test fun successfulCommitTransfersSessionAndNeverDeletesCommittedAlias() {
        val owned = Owned(); var deleted = 0; val op = WrapOperationState(owned) { deleted++ }
        assertTrue(op.beginWork()); op.markCommitted(); assertTrue(op.transferOwned()); op.close(); op.endWork()
        assertEquals(0, owned.closes); assertEquals(0, deleted)
        owned.close(); assertEquals(1, owned.closes)
    }
    @Test fun rejectedExecutorAndMainPostAndDelayCannotStrandResources() {
        for (kind in 0..2) {
            val owned = Owned(); var deleted = 0; val op = WrapOperationState(owned) { deleted++ }
            when (kind) {
                0 -> {
                    val executor = Executors.newSingleThreadExecutor().also { it.shutdown() }
                    assertFalse(OwnedWork.submit(executor, op) { fail("Rejected work ran") })
                }
                1 -> op.postOwned({ false }) { fail("Rejected post ran") }
                2 -> op.delayOwned({ _, _ -> false }, {}) { fail("Rejected delay ran") }
            }
            assertEquals(1, owned.closes); assertEquals(1, deleted)
        }
    }
    @Test fun staleEpochDeliveryCancelsPendingOwnershipBeforeNewWork() {
        val epoch = OperationEpoch(); val token = epoch.begin()
        val owned = Owned(); var deleted = 0; val op = WrapOperationState(owned) { deleted++ }
        var queued: (() -> Unit)? = null
        op.postOwned({ queued = it; true }) {
            if (!epoch.accepts(token)) op.close() else fail("Stale prompt delivered")
        }
        epoch.begin(); queued!!()
        assertFalse(op.beginWork()); assertEquals(1, owned.closes); assertEquals(1, deleted)
    }
    @Test fun publicCodeCauseInspectionIsTypedBoundedAndNeverTextMatched() {
        class NumericFailure(val code: Int) : Exception("SYNTHETIC_UNREAD_MESSAGE")
        fun inspect(error: Throwable) = WrapFailureCauses.code(error) { (it as? NumericFailure)?.code }
        assertEquals(15, inspect(IllegalStateException("SYNTHETIC_UNREAD_MESSAGE", NumericFailure(15))))
        assertEquals(2, inspect(NumericFailure(2)))
        assertNull(inspect(IllegalStateException("ERROR_KEY_OPERATION_EXPIRED code15")))
        var error: Throwable = NumericFailure(15)
        repeat(5) { error = IllegalStateException("SYNTHETIC_UNREAD_MESSAGE", error) }
        assertNull(inspect(error))
    }
}
