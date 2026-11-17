package dev.passchef.mobile.vault

import kotlin.test.*

/** Vault format v2 (D17): v1 caches still open as one "Default" set; sets round-trip; limits hold. */
class SecretPartSetsV2Test {
    private fun bytes(text: String) = text.encodeToByteArray()

    @Test fun v1PlaintextDecodesAsDefaultSet() {
        val v1 = SeasoningCache.encode(mapOf(1 to bytes("Basic*"), 3 to bytes("three")))
        val sets = SeasoningCache.decodeSets(v1)
        assertEquals(1, sets.size)
        assertEquals(SecretPartSet.DEFAULT_ID, sets[0].id)
        assertContentEquals(bytes("Basic*"), sets[0].parts[1])
        assertEquals(listOf(1, 3), sets[0].parts.keys.sorted())
    }

    @Test fun v2RoundTripKeepsIdsNamesAndParts() {
        val sets = listOf(SecretPartSet.default(mapOf(1 to bytes("a1"))),
            SecretPartSet("family", "Gia đình", mapOf(1 to bytes("f1"), 2 to bytes("f2"))))
        val decoded = SeasoningCache.decodeSets(SeasoningCache.encodeSets(sets))
        assertEquals(listOf("default", "family"), decoded.map { it.id })
        assertEquals("Gia đình", decoded[1].name)
        assertContentEquals(bytes("f2"), decoded[1].parts[2])
    }

    @Test fun rejectsBadShapes() {
        val one = SecretPartSet.default(mapOf(1 to bytes("x")))
        assertFails { SeasoningCache.encodeSets(emptyList()) }
        assertFails { SeasoningCache.encodeSets(listOf(one, SecretPartSet.default(mapOf(1 to bytes("y"))))) }
        assertFails { SeasoningCache.encodeSets(List(9) { SecretPartSet("s$it", "S$it", mapOf(1 to bytes("x"))) }) }
        assertFails { SeasoningCache.encodeSets(List(8) { i -> SecretPartSet("s$i", "S$i", (1..5).associateWith { ByteArray(4096) }) }) }
        assertFails { SecretPartSet("Bad Id", "n", mapOf(1 to bytes("x"))) }
        val encoded = SeasoningCache.encodeSets(listOf(one))
        assertFails { SeasoningCache.decodeSets(encoded + byteArrayOf(0)) }
        assertFails { SeasoningCache.decodeSets(encoded.copyOf(encoded.size - 1)) }
    }

    @Test fun updateSetsNeedsMasterPasswordAndKeepsKey() {
        val store = object : RecordStore { var bytes: ByteArray? = null
            override fun read() = bytes?.copyOf(); override fun write(bytes: ByteArray) { this.bytes = bytes.copyOf() } }
        val repository = VaultRepository(store)
        val password = bytes("correct horse")
        repository.setup(password.copyOf(), mapOf(1 to bytes("Basic*"))).use { repository.commitPasswordOnly(it) }
        assertFails { repository.updateSets(bytes("wrong")) { it } }
        val key = repository.recover(password.copyOf()).use { it.session.key.copyOf() }
        repository.updateSets(password.copyOf()) { it + SecretPartSet("family", "Family", mapOf(1 to bytes("Fam1"))) }.use { session ->
            assertContentEquals(key, session.key)
            assertEquals(2, session.sets.size)
        }
        repository.recover(password.copyOf()).use { opened ->
            assertEquals(listOf("default", "family"), opened.session.sets.map { it.id })
            assertContentEquals(bytes("Basic*"), opened.session.spices[1])
            assertContentEquals(bytes("Fam1"), opened.session.set("family")!!.parts[1])
        }
    }
}
