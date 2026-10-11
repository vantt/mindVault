package dev.passchef.mobile.crypto

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@Serializable private data class BundleFixture(
    val _warning: String, val bundle: FullAccessBundle,
    val expectedSpices: Map<String, SpiceValue>, val sharingPassword: String
)

class BundleDecryptorTest {
    private val json = Json { ignoreUnknownKeys = true }
    private fun fixture(): BundleFixture = json.decodeFromString(
        javaClass.getResource("/full-access-bundle.fixture.json")!!.readText())
    private fun encoded(bundle: FullAccessBundle): String = json.encodeToString(bundle)

    @Test fun decryptsRealExtensionSyntheticFixture() {
        val fixture = fixture()
        assertEquals("SYNTHETIC TEST DATA ONLY", fixture._warning)
        assertEquals(fixture.expectedSpices, BundleDecryptor.decrypt(encoded(fixture.bundle), fixture.sharingPassword))
    }

    @Test fun wrongPasswordAndDamagedCiphertextFailClosed() {
        val fixture = fixture()
        assertFailsWith<IllegalArgumentException> { BundleDecryptor.decrypt(encoded(fixture.bundle), "SYNTHETIC_WRONG") }
        val bytes = fixture.bundle.encryptedData.toMutableList().also { it[0] = it[0] xor 1 }
        assertFailsWith<IllegalArgumentException> {
            BundleDecryptor.decrypt(encoded(fixture.bundle.copy(encryptedData = bytes)), fixture.sharingPassword)
        }
    }

    @Test fun rejectsWrongVersionTypeAndMalformedByteArrays() {
        val fixture = fixture()
        for (bundle in listOf(
            fixture.bundle.copy(version = "future"), fixture.bundle.copy(type = "wrong"),
            fixture.bundle.copy(iv = emptyList()), fixture.bundle.copy(exportSalt = listOf(1)),
            fixture.bundle.copy(encryptedData = listOf(1)), fixture.bundle.copy(iv = List(12) { 256 })
        )) assertFailsWith<IllegalArgumentException> { BundleDecryptor.decrypt(encoded(bundle), fixture.sharingPassword) }
        assertFailsWith<IllegalArgumentException> { BundleDecryptor.decrypt("{}", fixture.sharingPassword) }
        assertFailsWith<IllegalArgumentException> { BundleDecryptor.decrypt("not json", fixture.sharingPassword) }
    }
}
