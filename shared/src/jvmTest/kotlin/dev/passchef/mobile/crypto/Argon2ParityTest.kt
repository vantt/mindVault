package dev.passchef.mobile.crypto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters
import kotlin.test.Test
import kotlin.test.assertEquals

@Serializable private data class ArgonVector(
    val password: String, val salt: String, val version: Int, val hashLength: Int,
    val time: Int, val memoryKiB: Int, val parallelism: Int, val expectedHex: String
)

/** Entire BC implementation lives in jvmTest; it is not a shipped API. */
class Argon2ParityTest {
    @Test fun matchesRealWasmReferenceWithUtf8SupplementaryCharacter() {
        val vector = Json { ignoreUnknownKeys = true }.decodeFromString<ArgonVector>(
            javaClass.getResource("/argon2-reference-vector.json")!!.readText())
        assertEquals(19, vector.version)
        assertEquals(32, vector.hashLength)
        assertEquals(3, vector.time)
        assertEquals(65536, vector.memoryKiB)
        assertEquals(4, vector.parallelism)
        val generator = Argon2BytesGenerator()
        generator.init(Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withVersion(vector.version).withSalt(vector.salt.encodeToByteArray())
            .withIterations(vector.time).withMemoryAsKB(vector.memoryKiB)
            .withParallelism(vector.parallelism).build())
        val output = ByteArray(vector.hashLength)
        generator.generateBytes(vector.password.encodeToByteArray(), output)
        assertEquals(vector.expectedHex, output.joinToString("") { (it.toInt() and 255).toString(16).padStart(2, '0') })
    }
}
