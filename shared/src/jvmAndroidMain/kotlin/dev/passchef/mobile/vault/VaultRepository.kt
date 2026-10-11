package dev.passchef.mobile.vault

class VaultSession(val key: ByteArray, val spices: Map<Int, ByteArray>) : AutoCloseable {
    override fun close() { key.fill(0); spices.values.forEach { it.fill(0) } }
}

class PreparedVault internal constructor(val record: VaultRecord, val session: VaultSession) : AutoCloseable {
    override fun close() = session.close()
}

/** Storage-only transactions: hardware keys are created/retired by the Android caller. */
class VaultRepository(private val store: RecordStore) {
    fun record(): VaultRecord? = store.read()?.let(VaultRecord::decode)

    fun setup(password: ByteArray, spices: Map<Int, ByteArray>): PreparedVault {
        check(record() == null) { "A Cookbook already exists; unlock it first" }
        val salt = SeasoningCache.random(16)
        val key = Argon2MasterKey.derive(password, salt)
        try {
            val (iv, cache) = SeasoningCache.encrypt(key, spices)
            return PreparedVault(VaultRecord(salt = salt, alias = "", wrapIv = byteArrayOf(),
                wrappedKey = byteArrayOf(), cacheIv = iv, cache = cache),
                VaultSession(key, spices.mapValues { it.value.copyOf() }))
        } catch (error: Exception) { key.fill(0); throw error }
    }

    /** Cache GCM authentication completes before the caller is allowed to rotate an alias. */
    fun recover(password: ByteArray): PreparedVault {
        val record = record() ?: throw IllegalStateException("No Cookbook stored")
        val key = Argon2MasterKey.derive(password, record.salt)
        try { return PreparedVault(record, VaultSession(key, SeasoningCache.decrypt(key, record.cacheIv, record.cache))) }
        catch (error: Exception) { key.fill(0); throw error }
    }

    fun unwrap(key: ByteArray): VaultSession {
        try {
            val record = record() ?: throw IllegalStateException("No Cookbook stored")
            return VaultSession(key, SeasoningCache.decrypt(key, record.cacheIv, record.cache))
        } catch (error: Exception) { key.fill(0); throw error }
    }

    /** Returns the prior alias only after atomic commit succeeds. */
    fun commitWrap(prepared: PreparedVault, alias: String, iv: ByteArray, wrapped: ByteArray): String {
        val old = prepared.record.alias
        val retired = (prepared.record.retiredAliases + listOf(old).filter { it.isNotEmpty() && it != alias }).distinct()
        val updated = prepared.record.copy(alias = alias, wrapIv = iv, wrappedKey = wrapped, retiredAliases = retired)
        store.write(updated.encode())
        return prepared.record.alias
    }
    fun commitPasswordOnly(prepared: PreparedVault) { store.write(prepared.record.encode()) }

    /** Caller invokes only after cache authentication; deletion retries survive interruptions. */
    fun retireKeys(delete: (String) -> Unit): Boolean {
        val record = record() ?: return true
        val remaining = record.retiredAliases.filter { alias -> runCatching { delete(alias) }.isFailure }
        if (remaining != record.retiredAliases) store.write(record.copy(retiredAliases = remaining).encode())
        return remaining.isEmpty()
    }
}
