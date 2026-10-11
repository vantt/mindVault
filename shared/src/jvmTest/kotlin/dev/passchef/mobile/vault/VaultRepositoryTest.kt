package dev.passchef.mobile.vault

import dev.passchef.mobile.KeystoreCrypto
import java.io.File
import java.nio.file.Files
import kotlin.test.*

class VaultRepositoryTest {
    private val password = Argon2MasterKey.passwordBytes("SYNTHETIC_Mật🍳")
    private val spice = "SYNTHETIC_SPICE".encodeToByteArray()
    private class FaultStore : RecordStore {
        var bytes: ByteArray? = null
        var fail = false
        override fun read() = bytes?.copyOf()
        override fun write(bytes: ByteArray) { if (fail) error("injected write fault"); this.bytes = bytes.copyOf() }
    }

    @Test fun passwordRecoveryAuthenticatesSameCacheAndWrongPasswordNeverWrites() {
        val store = FaultStore(); val repository = VaultRepository(store)
        repository.setup(password, mapOf(1 to spice)).use { repository.commitPasswordOnly(it) }
        val original = store.bytes!!.copyOf()
        assertFalse(original.decodeToString().contains("SYNTHETIC_SPICE"))
        assertFails { repository.recover("incorrect".encodeToByteArray()) }
        assertContentEquals(original, store.bytes)
        repository.recover(password).use { assertContentEquals(spice, it.session.spices[1]) }
        assertFails { repository.setup(password, mapOf(1 to spice)) }
    }

    @Test fun failedAtomicRewrapKeepsPriorSaltAliasAndEncryptedCache() {
        val store = FaultStore(); val repository = VaultRepository(store)
        val old = "passchef-00000000-0000-0000-0000-000000000001"
        repository.setup(password, mapOf(1 to spice)).use { repository.commitWrap(it, old, ByteArray(12), ByteArray(48)) }
        val original = store.bytes!!.copyOf()
        repository.recover(password).use { prepared ->
            store.fail = true
            assertFails { repository.commitWrap(prepared, "passchef-00000000-0000-0000-0000-000000000002", ByteArray(12), ByteArray(48)) }
            assertContentEquals(original, store.bytes)
            store.fail = false
            val retired = repository.commitWrap(prepared, "passchef-00000000-0000-0000-0000-000000000002", ByteArray(12), ByteArray(48))
            assertEquals(old, retired)
        }
        repository.recover(password).use { assertContentEquals(spice, it.session.spices[1]) }
    }

    @Test fun corruptCacheFailsAuthenticationWithoutMutation() {
        val store = FaultStore(); val repository = VaultRepository(store)
        repository.setup(password, mapOf(1 to spice)).use { repository.commitPasswordOnly(it) }
        val record = repository.record()!!
        record.cache[0] = (record.cache[0].toInt() xor 1).toByte()
        store.bytes = record.encode()
        val corrupt = store.bytes!!.copyOf()
        assertFails { repository.recover(password) }
        assertContentEquals(corrupt, store.bytes)
    }

    @Test fun physicalFileReplacementIgnoresInterruptedTemporaryFile() {
        val directory = Files.createTempDirectory("passchef-atomic-test").toFile()
        try {
            val file = File(directory, "record.json"); val store = AtomicRecordStore(file)
            store.write("original".encodeToByteArray())
            File(directory, "record.json.pending").writeText("partial")
            assertContentEquals("original".encodeToByteArray(), AtomicRecordStore(file).read())
            store.write("replacement".encodeToByteArray())
            assertContentEquals("replacement".encodeToByteArray(), AtomicRecordStore(file).read())
            assertFalse(File(directory, "record.json.pending").exists())
        } finally { directory.deleteRecursively() }
    }

    @Test fun recordAndCacheRejectBoundsVersionsDuplicateJarsAndTrailingData() {
        val valid = VaultRecord(salt = ByteArray(16), alias = "", wrapIv = byteArrayOf(), wrappedKey = byteArrayOf(),
            cacheIv = ByteArray(12), cache = ByteArray(32))
        for (bad in listOf(valid.copy(version = 2), valid.copy(salt = ByteArray(15)), valid.copy(alias = "foreign"),
            valid.copy(wrapIv = ByteArray(12)), valid.copy(cache = ByteArray(32769)))) assertFails { bad.encode() }
        assertFails { VaultRecord.decode(ByteArray(200001)) }
        assertFails { VaultRecord.decode("{}".encodeToByteArray()) }
        assertFails { SeasoningCache.encode(emptyMap()) }
        assertFails { SeasoningCache.encode(mapOf(6 to spice)) }
        assertFails { SeasoningCache.encode(mapOf(1 to ByteArray(4097))) }
        val encoded = SeasoningCache.encode(mapOf(1 to spice))
        assertFails { SeasoningCache.decode(encoded + byteArrayOf(0)) }
        assertFails { SeasoningCache.decode(encoded.copyOf(7)) }
    }

    @Test fun lockWipesOwnedKeyAndAllSpiceBuffers() {
        val key = ByteArray(32) { 7 }; val jars = mapOf(1 to spice.copyOf(), 5 to spice.copyOf())
        VaultSession(key, jars).close()
        assertTrue(key.all { it == 0.toByte() }); assertTrue(jars.values.all { it.all { byte -> byte == 0.toByte() } })
    }

    @Test fun idleTotalBackgroundAndStaleCallbacksCannotReopenSessions() {
        val clock = SessionClock()
        assertTrue(clock.expired(0))
        val token = clock.begin(1000); assertFalse(clock.expired(300999)); assertTrue(clock.expired(301000))
        clock.begin(1000); clock.touch(250000); clock.touch(500000)
        assertFalse(clock.expired(600999)); assertTrue(clock.expired(601000))
        clock.lock(); assertFalse(clock.accepts(token))
        val epoch = OperationEpoch(); val first = epoch.begin(); epoch.invalidate()
        assertFalse(epoch.accepts(first)); val next = epoch.begin(); assertTrue(epoch.accepts(next)); assertFalse(epoch.accepts(first))
    }

    @Test fun productionArgon2MatchesProvenWasmAndWebUtf8Encoding() {
        val derived = Argon2MasterKey.derive(Argon2MasterKey.passwordBytes("SYNTHETIC_ARGON2_VECTOR_🍳"), "SYNTHETIC_SALT16".encodeToByteArray())
        try { assertEquals("d2dba0254ed99b7f209639a23f2edc31a49c8c2956883caa67b5b56898c7d40a",
            derived.joinToString("") { (it.toInt() and 255).toString(16).padStart(2, '0') }) }
        finally { derived.fill(0) }
        assertContentEquals("a�b".encodeToByteArray(), Argon2MasterKey.passwordBytes("a\uD800b"))
        assertFails { Argon2MasterKey.derive(byteArrayOf(), ByteArray(16)) }
    }

    @Test fun missingCorruptAndUnreadableRecordWipeUnwrappedKey() {
        for (store in listOf<RecordStore>(FaultStore(), FaultStore().also { it.bytes = "{}".encodeToByteArray() },
            object : RecordStore {
                override fun read(): ByteArray? = error("injected read fault")
                override fun write(bytes: ByteArray) = Unit
            })) {
            val key = ByteArray(32) { 9 }
            assertFails { VaultRepository(store).unwrap(key) }
            assertTrue(key.all { it == 0.toByte() })
        }
    }

    @Test fun queuedOwnedResultsAreWipedAfterBackgroundOrNewUnlock() {
        val epoch = OperationEpoch()
        for (replacement in listOf(false, true)) {
            val opened = VaultSession(ByteArray(32) { 5 }, mapOf(1 to spice.copyOf()))
            val token = epoch.begin()
            val queued = { epoch.deliver(token, { fail("Stale callback reopened session") }, { opened.close() }) }
            if (replacement) epoch.begin() else epoch.invalidate()
            queued()
            assertTrue(opened.key.all { it == 0.toByte() })
            assertTrue(opened.spices[1]!!.all { it == 0.toByte() })
        }
    }

    @Test fun failedAliasRetirementRemainsRetryableAfterRestart() {
        val store = FaultStore(); val repository = VaultRepository(store)
        val old = "passchef-00000000-0000-0000-0000-000000000001"
        val new = "passchef-00000000-0000-0000-0000-000000000002"
        repository.setup(password, mapOf(1 to spice)).use { repository.commitWrap(it, old, ByteArray(12), ByteArray(48)) }
        repository.recover(password).use { repository.commitWrap(it, new, ByteArray(12), ByteArray(48)) }
        assertFalse(repository.retireKeys { error("injected device-key deletion failure") })
        val restarted = VaultRepository(store)
        assertEquals(listOf(old), restarted.record()!!.retiredAliases)
        val deleted = mutableListOf<String>()
        assertTrue(restarted.retireKeys { deleted += it })
        assertEquals(listOf(old), deleted); assertEquals(new, restarted.record()!!.alias)
        assertTrue(restarted.record()!!.retiredAliases.isEmpty())
    }

    @Test fun rejectedFallbackAfterWorkerShutdownClosesOwnedPreparedKeyAndSpices() {
        val session = VaultSession(ByteArray(32) { 8 }, mapOf(1 to spice.copyOf()))
        val prepared = PreparedVault(VaultRecord(salt = ByteArray(16), alias = "", wrapIv = byteArrayOf(),
            wrappedKey = byteArrayOf(), cacheIv = ByteArray(12), cache = ByteArray(32)), session)
        val executor = java.util.concurrent.Executors.newSingleThreadExecutor()
        executor.shutdown()
        assertFalse(OwnedWork.submit(executor, prepared) { fail("Rejected fallback executed") })
        assertTrue(session.key.all { it == 0.toByte() }); assertTrue(session.spices[1]!!.all { it == 0.toByte() })
    }

    @Test fun jvmNeverClaimsAndroidHardwareSupport() {
        assertFailsWith<UnsupportedOperationException> { KeystoreCrypto().createKey("passchef-test", false) }
    }
}
