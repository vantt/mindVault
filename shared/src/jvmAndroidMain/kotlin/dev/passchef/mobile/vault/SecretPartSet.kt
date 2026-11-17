package dev.passchef.mobile.vault

/**
 * One named group of 1–5 secret parts (D15/D17). A sheet is bound to one set; a vault holds 1–8 sets.
 * Part bytes are secret: callers zero them through [close]. Id and name are non-secret labels.
 */
class SecretPartSet(val id: String, val name: String, val parts: Map<Int, ByteArray>) : AutoCloseable {
    init {
        require(id.matches(ID_PATTERN)) { "Bad set id" }
        require(name.isNotBlank() && name.length <= MAX_NAME) { "Bad set name" }
        require(parts.size in 1..5 && parts.keys.all { it in 1..5 } && parts.values.all { it.size in 1..4096 })
    }
    fun copy(): SecretPartSet = SecretPartSet(id, name, parts.mapValues { it.value.copyOf() })
    override fun close() { parts.values.forEach { it.fill(0) } }

    companion object {
        const val DEFAULT_ID = "default"
        const val DEFAULT_NAME = "Default"
        const val MAX_SETS = 8
        const val MAX_NAME = 60
        val ID_PATTERN = Regex("[a-z0-9-]{1,40}")
        fun default(parts: Map<Int, ByteArray>) = SecretPartSet(DEFAULT_ID, DEFAULT_NAME, parts)
    }
}

/** Non-secret view of a set for UI: id, name and which part numbers exist. Never part values. */
data class SecretPartSetSummary(val id: String, val name: String, val partNumbers: List<Int>)
