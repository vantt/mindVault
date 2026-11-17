package dev.passchef.mobile.vault

/** Unlocked key + secret-part sets (D17). [spices] = the first ("Default") set, kept for older callers. */
class VaultSession(val key: ByteArray, val sets: List<SecretPartSet>) : AutoCloseable {
    constructor(key: ByteArray, spices: Map<Int, ByteArray>) : this(key, listOf(SecretPartSet.default(spices)))
    init { require(sets.isNotEmpty()) }
    val spices: Map<Int, ByteArray> get() = sets.first().parts
    fun set(id: String?): SecretPartSet? = if (id == null) sets.first() else sets.find { it.id == id }
    fun summaries(): List<SecretPartSetSummary> = sets.map { SecretPartSetSummary(it.id, it.name, it.parts.keys.sorted()) }
    override fun close() { key.fill(0); sets.forEach { it.close() } }
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

    /**
     * Post-setup change of secret parts (D8 add part / D17 add set). Re-auth: the master password is
     * derived again and must open the current cache. The key is unchanged, so the biometric wrap stays valid.
     * [change] receives copies it may keep; the caller owns and closes the returned session.
     */
    fun updateSets(password: ByteArray, change: (List<SecretPartSet>) -> List<SecretPartSet>): VaultSession {
        val prepared = recover(password)
        var updated: List<SecretPartSet>? = null
        try {
            updated = change(prepared.session.sets.map { it.copy() })
            val (iv, cache) = SeasoningCache.encryptSets(prepared.session.key, updated)
            store.write(prepared.record.copy(cacheIv = iv, cache = cache).encode())
            return VaultSession(prepared.session.key.copyOf(), updated)
        } catch (error: Exception) { updated?.forEach { it.close() }; throw error }
        finally { prepared.close() }
    }

    /** Caller invokes only after cache authentication; deletion retries survive interruptions. */
    fun retireKeys(delete: (String) -> Unit): Boolean {
        val record = record() ?: return true
        val remaining = record.retiredAliases.filter { alias -> runCatching { delete(alias) }.isFailure }
        if (remaining != record.retiredAliases) store.write(record.copy(retiredAliases = remaining).encode())
        return remaining.isEmpty()
    }
}
