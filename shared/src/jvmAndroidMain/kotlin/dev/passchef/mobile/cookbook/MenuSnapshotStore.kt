package dev.passchef.mobile.cookbook

import dev.passchef.mobile.vault.AtomicRecordStore
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.security.MessageDigest

/** Separate private metadata files, never the encrypted jar record. */
data class CacheInventory(val snapshots: List<MenuSnapshot>, val corruptFiles: Int)
class MenuSnapshotStore(private val directory: File) {
    private val json = Json { ignoreUnknownKeys = false }
    private fun file(origin: MenuOrigin): File {
        val name = MessageDigest.getInstance("SHA-256").digest(origin.key.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
        return File(directory, "$name.menu.json")
    }
    @Synchronized fun save(snapshot: MenuSnapshot) {
        require(file(snapshot.mapping.origin).exists() || (directory.listFiles()?.count { it.name.endsWith(".menu.json") } ?: 0) < 20)
        val bytes = json.encodeToString(snapshot).toByteArray(Charsets.UTF_8)
        require(bytes.size <= 180_000)
        AtomicRecordStore(file(snapshot.mapping.origin)).write(bytes)
    }
    @Synchronized fun load(origin: MenuOrigin): MenuSnapshot? {
        val bytes = AtomicRecordStore(file(origin)).read() ?: return null
        require(bytes.size <= 180_000)
        return json.decodeFromString<MenuSnapshot>(bytes.decodeToString(throwOnInvalidSequence = true)).also { require(it.mapping.origin == origin) }
    }
    @Synchronized fun inventory(): CacheInventory {
        var corrupt = 0
        val snapshots = directory.listFiles()?.filter { it.name.matches(Regex("[0-9a-f]{64}\\.menu\\.json")) }?.mapNotNull { file ->
            runCatching {
                val bytes = AtomicRecordStore(file).read() ?: error("Missing cache")
                require(bytes.size <= 180_000)
                json.decodeFromString<MenuSnapshot>(bytes.decodeToString(throwOnInvalidSequence = true)).also { require(this.file(it.mapping.origin).name == file.name) }
            }.getOrElse { corrupt++; null }
        } ?: emptyList()
        return CacheInventory(snapshots, corrupt)
    }
    @Synchronized fun remove(origin: MenuOrigin) { file(origin).let { require(!it.exists() || it.delete()) } }
}
