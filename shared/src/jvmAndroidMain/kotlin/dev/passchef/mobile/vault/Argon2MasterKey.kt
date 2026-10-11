package dev.passchef.mobile.vault

import dev.passchef.mobile.recipe.webUnicode
import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters

/** Same UTF-8 and Argon2id v19 parameters as the proven extension oracle. */
object Argon2MasterKey {
    fun passwordBytes(text: String): ByteArray = webUnicode(text).encodeToByteArray()
    fun derive(password: ByteArray, salt: ByteArray): ByteArray {
        require(password.isNotEmpty() && password.size <= 4096 && salt.size == 16)
        val params = Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withVersion(19).withSalt(salt).withIterations(3).withMemoryAsKB(65536)
            .withParallelism(4).build()
        try {
            val generator = Argon2BytesGenerator().apply { init(params) }
            return ByteArray(32).also { generator.generateBytes(password, it) }
        } finally { params.clear() }
    }
}
