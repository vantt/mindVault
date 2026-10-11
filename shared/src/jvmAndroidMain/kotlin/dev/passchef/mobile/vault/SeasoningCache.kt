package dev.passchef.mobile.vault

import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.nio.ByteBuffer
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

internal object SeasoningCache {
    fun encrypt(key: ByteArray, spices: Map<Int, ByteArray>): Pair<ByteArray, ByteArray> {
        val plain = encode(spices)
        try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), GCMParameterSpec(128, random(12)))
            cipher.updateAAD("PassChef-cache-v1".encodeToByteArray())
            return cipher.iv to cipher.doFinal(plain)
        } finally { plain.fill(0) }
    }
    fun decrypt(key: ByteArray, iv: ByteArray, encrypted: ByteArray): Map<Int, ByteArray> {
        require(key.size == 32 && iv.size == 12 && encrypted.size in 17..32768)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), GCMParameterSpec(128, iv))
        cipher.updateAAD("PassChef-cache-v1".encodeToByteArray())
        val plain = cipher.doFinal(encrypted)
        return try { decode(plain) } finally { plain.fill(0) }
    }
    fun encode(spices: Map<Int, ByteArray>): ByteArray {
        require(spices.size in 1..5 && spices.keys.all { it in 1..5 })
        require(spices.values.all { it.size in 1..4096 })
        val bytes = ByteArray(8 + spices.values.sumOf { 8 + it.size })
        try {
            val output = ByteBuffer.wrap(bytes).putInt(1).putInt(spices.size)
            for ((index, spice) in spices.toSortedMap()) output.putInt(index).putInt(spice.size).put(spice)
            return bytes
        } catch (error: Exception) { bytes.fill(0); throw error }
    }
    fun decode(bytes: ByteArray): Map<Int, ByteArray> {
        val result = linkedMapOf<Int, ByteArray>()
        try {
            DataInputStream(ByteArrayInputStream(bytes)).use { input ->
                require(input.readInt() == 1)
                val count = input.readInt(); require(count in 1..5)
                repeat(count) {
                    val index = input.readInt(); val size = input.readInt()
                    require(index in 1..5 && index !in result && size in 1..4096 && size <= input.available())
                    result[index] = ByteArray(size).also { input.readFully(it) }
                }
                require(input.available() == 0)
            }
            return result
        } catch (error: Exception) { result.values.forEach { it.fill(0) }; throw error }
    }
    fun random(size: Int): ByteArray = ByteArray(size).also { SecureRandom().nextBytes(it) }
}
