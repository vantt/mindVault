package dev.passchef.mobile.vault

import kotlin.test.*

/** Production KDF/cache/record checks plus control tokens, never simulated biometric proof. */
class PasswordLockoutSessionTest {
    private val password = "SYNTHETIC_LOCKOUT_PASSWORD_🍳".encodeToByteArray()
    private val spice = "SYNTHETIC_LOCKOUT_SPICE".encodeToByteArray()
    private val oldAlias = "passchef-00000000-0000-0000-0000-000000000001"
    private val queuedAlias = "passchef-00000000-0000-0000-0000-000000000002"
    private class Store : RecordStore {
        var bytes: ByteArray? = null
        var writes = 0
        var fail = false
        override fun read() = bytes?.copyOf()
        override fun write(bytes: ByteArray) {
            if (fail) error("SYNTHETIC_STORAGE_FAILURE")
            writes++; this.bytes = bytes.copyOf()
        }
    }
    private fun existing(wrapped: Boolean = true): Pair<Store, VaultRepository> {
        val store = Store(); val repository = VaultRepository(store)
        repository.setup(password, mapOf(1 to spice)).use {
            if (wrapped) repository.commitWrap(it, oldAlias, ByteArray(12), ByteArray(48))
            else repository.commitPasswordOnly(it)
        }
        if (wrapped) store.bytes = repository.record()!!.copy(retiredAliases = listOf(queuedAlias)).encode()
        return store to repository
    }
    private fun consumeFailure(op: WrapOperationState) {
        val controlToken = Any(); val id = op.newPrompt(controlToken)
        assertTrue(op.authorize(id, controlToken))
    }
    @Test fun classifierOnlyAcceptsBiometricLockoutNamespace() {
        assertEquals(BiometricPromptFailure.LOCKOUT, BiometricPromptFailure.fromAndroidError(7))
        assertEquals(BiometricPromptFailure.PERMANENT_LOCKOUT, BiometricPromptFailure.fromAndroidError(9))
        for (code in listOf(2, 5, 10, 13, 15)) assertEquals(BiometricPromptFailure.OTHER, BiometricPromptFailure.fromAndroidError(code))
        for (reason in listOf(BiometricPromptFailure.LOCKOUT, BiometricPromptFailure.PERMANENT_LOCKOUT))
            WrapOperationState(AutoCloseable {}) {}.use {
                assertFalse(it.transferForLockout(reason)) // No consumed prompt failure.
                consumeFailure(it); assertTrue(it.transferForLockout(reason)); assertFalse(it.transferForLockout(reason))
            }
    }
    @Test fun existingAuthenticatedCacheOpensWithoutWriteOrOriginalAliasRetirement() {
        val (store, repository) = existing(); val original = store.bytes!!.copyOf(); val writes = store.writes
        val deleted = mutableListOf<String>(); val prepared = repository.recover(password)
        val op = WrapOperationState(prepared) { deleted += "disposable-new-alias" }
        consumeFailure(op); assertTrue(op.transferForLockout(BiometricPromptFailure.LOCKOUT)); op.close()
        PasswordLockoutSession.open(repository, prepared, false).use { assertContentEquals(spice, it.spices[1]) }
        assertEquals(writes, store.writes); assertContentEquals(original, store.bytes)
        assertEquals(listOf("disposable-new-alias"), deleted)
        val reopened = VaultRepository(store).record()!!
        assertEquals(oldAlias, reopened.alias); assertEquals(listOf(queuedAlias), reopened.retiredAliases)
        repository.recover(password).use { assertContentEquals(spice, it.session.spices[1]) }
    }
    @Test fun existingPasswordOnlyCookbookStillGetsZeroWritePath() {
        val (store, repository) = existing(false); val original = store.bytes!!.copyOf(); val writes = store.writes
        repository.recover(password).use { PasswordLockoutSession.open(repository, it, false).use { session ->
            assertContentEquals(spice, session.spices[1])
        } }
        assertEquals(writes, store.writes); assertContentEquals(original, store.bytes)
        assertTrue(repository.record()!!.alias.isEmpty())
    }
    @Test fun firstSetupLockoutPersistsOnlyEncryptedCacheBeforeSessionPublication() {
        val store = Store(); val repository = VaultRepository(store)
        val prepared = repository.setup(password, mapOf(1 to spice)); var deleted = 0
        val op = WrapOperationState(prepared) { deleted++ }
        consumeFailure(op); assertTrue(op.transferForLockout(BiometricPromptFailure.PERMANENT_LOCKOUT)); op.close()
        PasswordLockoutSession.open(repository, prepared, true).use { assertContentEquals(spice, it.spices[1]) }
        assertEquals(1, deleted); assertEquals(1, store.writes)
        val record = repository.record()!!
        assertTrue(record.alias.isEmpty()); assertTrue(record.wrappedKey.isEmpty()); assertTrue(record.wrapIv.isEmpty())
        assertFalse(store.bytes!!.decodeToString().contains("SYNTHETIC_LOCKOUT_SPICE"))
        repository.recover(password).use { assertContentEquals(spice, it.session.spices[1]) }
    }
    @Test fun failedFirstSetupCommitWipesSessionAndCannotOpen() {
        val store = Store().also { it.fail = true }; val repository = VaultRepository(store)
        val prepared = repository.setup(password, mapOf(1 to spice))
        assertFails { PasswordLockoutSession.open(repository, prepared, true) }
        assertNull(store.bytes); assertEquals(0, store.writes)
        assertTrue(prepared.session.key.all { it == 0.toByte() }); assertTrue(prepared.session.spices[1]!!.all { it == 0.toByte() })
    }
    @Test fun expiredRetryLockoutAndOrdinaryCancelStayClosedAndPreserveOriginalRecord() {
        val (store, repository) = existing(); val original = store.bytes!!.copyOf(); val writes = store.writes
        for (reason in BiometricPromptFailure.entries) {
            val prepared = repository.recover(password); var deleted = 0
            val op = WrapOperationState(prepared) { deleted++ }
            consumeFailure(op)
            if (reason.isLockout()) {
                assertTrue(op.retry(33, 15, "wrap-doFinal")); consumeFailure(op)
            }
            assertFalse(op.transferForLockout(reason)); op.close()
            assertEquals(1, deleted); assertTrue(prepared.session.key.all { it == 0.toByte() })
            assertEquals(writes, store.writes); assertContentEquals(original, store.bytes)
        }
    }
    @Test fun wrongPasswordFailsBeforeFallbackAndStaleFailureCannotTransfer() {
        val (store, repository) = existing(); val original = store.bytes!!.copyOf(); val writes = store.writes
        assertFails { repository.recover("SYNTHETIC_WRONG_PASSWORD".encodeToByteArray()) }
        val prepared = repository.recover(password); val op = WrapOperationState(prepared) {}
        consumeFailure(op); op.close(); assertFalse(op.transferForLockout(BiometricPromptFailure.LOCKOUT))
        assertTrue(prepared.session.key.all { it == 0.toByte() })
        assertEquals(writes, store.writes); assertContentEquals(original, store.bytes)
    }
    @Test fun newExplicitOperationCanPromptAfterPriorLockoutWithoutRememberedBypass() {
        val first = WrapOperationState(AutoCloseable {}) {}
        consumeFailure(first); assertTrue(first.transferForLockout(BiometricPromptFailure.LOCKOUT)); first.close()
        WrapOperationState(AutoCloseable {}) {}.use {
            val restoredControlToken = Any(); val id = it.newPrompt(restoredControlToken)
            assertTrue(it.authorize(id, restoredControlToken))
            assertFalse(it.transferForLockout(BiometricPromptFailure.OTHER))
        }
    }
}
