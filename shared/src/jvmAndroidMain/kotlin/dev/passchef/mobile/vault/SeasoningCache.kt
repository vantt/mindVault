package dev.passchef.mobile.vault

import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.nio.ByteBuffer
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

internal object SeasoningCache {
    fun encrypt(key: ByteArray, spices: Map<Int, ByteArray>): Pair<ByteArray, ByteArray> =
        encryptSets(key, listOf(SecretPartSet.default(spices)))
    /** Always writes format v2 (sets). Reading accepts v1 (one implicit "Default" set) and v2. */
    fun encryptSets(key: ByteArray, sets: List<SecretPartSet>): Pair<ByteArray, ByteArray> {
        val plain = encodeSets(sets)
        try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), GCMParameterSpec(128, random(12)))
            cipher.updateAAD("PassChef-cache-v1".encodeToByteArray())
            return cipher.iv to cipher.doFinal(plain)
        } finally { plain.fill(0) }
    }
    fun decrypt(key: ByteArray, iv: ByteArray, encrypted: ByteArray): List<SecretPartSet> {
        require(key.size == 32 && iv.size == 12 && encrypted.size in 17..32768)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), GCMParameterSpec(128, iv))
        cipher.updateAAD("PassChef-cache-v1".encodeToByteArray())
        val plain = cipher.doFinal(encrypted)
        return try { decodeSets(plain) } finally { plain.fill(0) }
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
    /** v2: version, set count, then per set id, name and parts. Total kept under the record cache limit. */
    fun encodeSets(sets: List<SecretPartSet>): ByteArray {
        require(sets.size in 1..SecretPartSet.MAX_SETS && sets.map { it.id }.distinct().size == sets.size)
        val labels = sets.map { it.id.encodeToByteArray() to it.name.encodeToByteArray() }
        val size = 8 + sets.indices.sumOf { i -> 12 + labels[i].first.size + labels[i].second.size +
            sets[i].parts.values.sumOf { 8 + it.size } }
        require(size <= MAX_PLAIN) { "Secret parts too large" }
        val bytes = ByteArray(size)
        try {
            val output = ByteBuffer.wrap(bytes).putInt(2).putInt(sets.size)
            sets.forEachIndexed { i, set ->
                output.putInt(labels[i].first.size).put(labels[i].first).putInt(labels[i].second.size).put(labels[i].second)
                output.putInt(set.parts.size)
                for ((index, part) in set.parts.toSortedMap()) output.putInt(index).putInt(part.size).put(part)
            }
            return bytes
        } catch (error: Exception) { bytes.fill(0); throw error }
    }
    fun decodeSets(bytes: ByteArray): List<SecretPartSet> {
        if (bytes.size >= 4 && ByteBuffer.wrap(bytes).getInt(0) == 1) return listOf(SecretPartSet.default(decode(bytes)))
        val result = mutableListOf<SecretPartSet>()
        try {
            DataInputStream(ByteArrayInputStream(bytes)).use { input ->
                require(input.readInt() == 2)
                val count = input.readInt(); require(count in 1..SecretPartSet.MAX_SETS)
                repeat(count) {
                    val id = readLabel(input, 40); val name = readLabel(input, SecretPartSet.MAX_NAME * 4)
                    val parts = linkedMapOf<Int, ByteArray>()
                    try {
                        val partCount = input.readInt(); require(partCount in 1..5)
                        repeat(partCount) {
                            val index = input.readInt(); val size = input.readInt()
                            require(index in 1..5 && index !in parts && size in 1..4096 && size <= input.available())
                            parts[index] = ByteArray(size).also { input.readFully(it) }
                        }
                        require(result.none { it.id == id })
                        result += SecretPartSet(id, name, parts)
                    } catch (error: Exception) { parts.values.forEach { it.fill(0) }; throw error }
                }
                require(input.available() == 0)
            }
            return result
        } catch (error: Exception) { result.forEach { it.close() }; throw error }
    }
    private fun readLabel(input: DataInputStream, max: Int): String {
        val size = input.readInt(); require(size in 1..max && size <= input.available())
        return ByteArray(size).also { input.readFully(it) }.decodeToString(throwOnInvalidSequence = true)
    }
    const val MAX_PLAIN = 32_000
    fun random(size: Int): ByteArray = ByteArray(size).also { SecureRandom().nextBytes(it) }
}
