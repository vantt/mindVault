package dev.passchef.mobile.vault

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable data class VaultRecord(
    val version: Int = 1, val salt: ByteArray, val alias: String,
    val wrapIv: ByteArray, val wrappedKey: ByteArray,
    val cacheIv: ByteArray, val cache: ByteArray, val retiredAliases: List<String> = emptyList()
) {
    fun validate() {
        require(version == 1 && salt.size == 16 && cacheIv.size == 12 && cache.size in 17..32768)
        val aliasPattern = Regex("passchef-[0-9a-f-]{36}")
        require(alias.isEmpty() || alias.matches(aliasPattern))
        require(retiredAliases.size <= 8 && retiredAliases.distinct().size == retiredAliases.size)
        require(retiredAliases.all { it != alias && it.matches(aliasPattern) })
        require(if (alias.isEmpty()) wrapIv.isEmpty() && wrappedKey.isEmpty()
            else wrapIv.size == 12 && wrappedKey.size == 48)
    }
    fun encode(): ByteArray { validate(); return Json.encodeToString(this).encodeToByteArray() }
    companion object {
        fun decode(bytes: ByteArray): VaultRecord {
            require(bytes.size in 1..200000)
            return Json.decodeFromString<VaultRecord>(bytes.decodeToString(throwOnInvalidSequence = true)).also { it.validate() }
        }
    }
}

interface RecordStore {
    fun read(): ByteArray?
    /** On failure the old complete record must remain readable. */
    fun write(bytes: ByteArray)
}
