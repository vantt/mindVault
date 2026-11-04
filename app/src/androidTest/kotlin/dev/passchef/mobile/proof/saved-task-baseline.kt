package dev.passchef.mobile.proof

import android.os.Bundle
import android.os.Process
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.vault.AtomicRecordStore
import dev.passchef.mobile.vault.VaultRecord
import org.json.JSONObject
import java.io.File
import java.security.MessageDigest
import java.util.UUID

/** Metadata-only continuity witness for one actual warm task; no authentication is serialized. */
class SavedTaskBaseline(val directory: File, val pid: Int, val taskId: Int, val refreshedAt: Long,
    val hashes: Map<String, String>, val payloadSha: String, val createCount: Int) {
    companion object {
        const val FILE_NAME = "saved-task-baseline.json"
        fun hash(bytes: ByteArray) = MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
        fun payload(record: VaultRecord) = hash(record.salt + record.cacheIv + record.cache)
        fun productionHashes(directory: File) = directory.walkTopDown().filter { it.isFile }
            .filter { it.name == "cookbook-v1.json" || it.parentFile == File(directory, "menus") }
            .associate { it.relativeTo(directory).path to hash(it.readBytes()) }
        fun read(base: File, locator: String): SavedTaskBaseline {
            require(UUID.fromString(locator).toString() == locator)
            val requestedName = "cookbook-ui-proof-$locator"
            val directory = File(base, requestedName).canonicalFile
            require(directory.parentFile == base.canonicalFile && directory.name == requestedName && directory.isDirectory)
            val file = File(directory, FILE_NAME)
            require(file.canonicalFile == file)
            require(file.length() in 1..16_384)
            val data = JSONObject(file.readText())
            require(data.getInt("version") == 1 && data.getString("fixture") == directory.name)
            val stored = data.getJSONObject("hashes")
            val hashes = stored.keys().asSequence().associateWith { stored.getString(it) }
            require(hashes.size == 2 && hashes.keys.all { it == "cookbook-v1.json" || Regex("menus/[a-f0-9]{64}\\.menu\\.json").matches(it) })
            require(hashes.values.all { Regex("[a-f0-9]{64}").matches(it) })
            return SavedTaskBaseline(directory, data.getInt("pid"), data.getInt("taskId"), data.getLong("refreshedAt"),
                hashes, data.getString("payloadSha"), data.getInt("createCount")).also {
                require(it.pid > 0 && it.taskId >= 0 && it.createCount > 0 && Regex("[a-f0-9]{64}").matches(it.payloadSha))
            }
        }
    }
    fun matches() = productionHashes(directory) == hashes
    fun write() {
        val file = File(directory, FILE_NAME)
        require(!file.exists())
        val data = JSONObject().put("version", 1).put("fixture", directory.name).put("pid", pid).put("taskId", taskId)
            .put("refreshedAt", refreshedAt).put("payloadSha", payloadSha).put("createCount", createCount)
            .put("hashes", JSONObject(hashes))
        AtomicRecordStore(file).write(data.toString().toByteArray())
    }
    fun report(phase: String) = InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
        putString("passchefTaskPhase", phase); putString("passchefTaskFixture", directory.name)
        putInt("passchefTaskPid", Process.myPid()); putInt("passchefTaskId", taskId)
        putInt("passchefTaskWarmPid", pid)
    })
}
