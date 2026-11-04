package dev.passchef.mobile.proof

import android.os.Bundle
import android.os.Process
import android.os.SystemClock
import android.util.AtomicFile
import dev.passchef.mobile.MainActivity
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.cookbook.CookbookController
import dev.passchef.mobile.cookbook.CookbookSources
import java.io.File
import java.util.UUID
import org.json.JSONObject

/** Passive debug-only lifecycle metadata; never stores or restores authentication or Dishes. */
class SavedTaskProofJournal(private val activity: CookbookProofActivity) {
    companion object {
        const val FILE_NAME = "saved-task-journal.json"
        private const val MAX_BYTES = 16 * 1024
        private const val PREFIX = "cookbook-ui-proof-"
        private const val LOCATOR_KEY = "passchefOwnedProofId"
        private val allowedKeys = setOf("version", "fixture", "originalPid", "originalTaskId", "createCount",
            "lastCreatedPid", "lastCreatedTaskId", "lastCreatedWithSavedState", "lastCreatedLocatorMatches",
            "lastCreatedCleared", "warmSavedPid", "warmSavedTaskId", "warmSavedLocatorMatches", "warmSavedAt",
            "warmStoppedPid", "warmStoppedTaskId", "warmStoppedCleared", "warmStoppedAt",
            "latestStoppedPid", "latestStoppedTaskId", "latestStoppedCleared")

        private fun ownedDirectory(directory: File): File {
            val canonical = directory.canonicalFile
            require(canonical.isDirectory && canonical.name == directory.name &&
                canonical.parentFile == directory.absoluteFile.parentFile.canonicalFile) { "Exact owned proof directory required" }
            require(canonical.name.startsWith(PREFIX)) { "Owned proof prefix required" }
            val locator = canonical.name.removePrefix(PREFIX)
            require(UUID.fromString(locator).toString() == locator) { "Canonical owned proof UUID required" }
            return canonical
        }
        private fun store(directory: File): AtomicFile {
            val path = File(directory, FILE_NAME)
            require(path.canonicalFile == path) { "Exact owned journal path required" }
            return AtomicFile(path)
        }
        fun read(directory: File): JSONObject {
            val owned = ownedDirectory(directory)
            val bytes = ByteArray(MAX_BYTES + 1)
            var size = 0
            store(owned).openRead().use { input ->
                while (size < bytes.size) {
                    val count = input.read(bytes, size, bytes.size - size)
                    if (count < 0) break
                    if (count == 0) continue
                    size += count
                }
            }
            require(size in 1..MAX_BYTES) { "Bounded proof journal required" }
            val data = JSONObject(String(bytes, 0, size, Charsets.UTF_8))
            require(data.get("version") == 1 && data.getString("fixture") == owned.name) { "Matching proof journal required" }
            val keys = data.keys()
            while (keys.hasNext()) require(keys.next() in allowedKeys) { "Metadata-only proof journal required" }
            return data
        }
    }

    private val directory = ownedDirectory(activity.proofDirectory)
    private val locator = directory.name.removePrefix(PREFIX)
    private fun locatorMatches(state: Bundle?) = state?.getString(LOCATOR_KEY) == locator
    private fun cleared(): Boolean = runCatching {
        val vault = MainActivity::class.java.getDeclaredField("controller").apply { isAccessible = true }
            .get(activity) as VaultController
        val cookbook = MainActivity::class.java.getDeclaredField("cookbook").apply { isAccessible = true }
            .get(activity) as CookbookController
        val sessionAbsent = VaultController::class.java.getDeclaredField("session").apply { isAccessible = true }.get(vault) == null
        val tokenAbsent = CookbookSources::class.java.getDeclaredField("accessToken").apply { isAccessible = true }
            .get(cookbook.sources) == null
        sessionAbsent && !vault.unlocked && !vault.readyForCooking() && !cookbook.hasDish() &&
            cookbook.dish.isEmpty() && !cookbook.revealed && cookbook.selected == null && tokenAbsent
    }.getOrDefault(false)
    private fun update(change: (JSONObject) -> Unit) {
        val atomic = store(directory)
        val exists = File(directory, FILE_NAME).exists() || File(directory, "$FILE_NAME.bak").exists()
        val data = if (exists) read(directory) else JSONObject()
            .put("version", 1).put("fixture", directory.name)
            .put("originalPid", Process.myPid()).put("originalTaskId", activity.taskId).put("createCount", 0)
        change(data)
        val bytes = data.toString().toByteArray(Charsets.UTF_8)
        require(bytes.size <= MAX_BYTES) { "Bounded proof journal required" }
        val output = atomic.startWrite()
        try {
            output.write(bytes)
            atomic.finishWrite(output)
        } catch (error: Throwable) {
            atomic.failWrite(output)
            throw error
        }
    }
    fun created(state: Bundle?) = update { data ->
        data.put("createCount", data.getInt("createCount") + 1)
            .put("lastCreatedPid", Process.myPid()).put("lastCreatedTaskId", activity.taskId)
            .put("lastCreatedWithSavedState", state != null).put("lastCreatedLocatorMatches", locatorMatches(state))
            .put("lastCreatedCleared", cleared())
    }
    fun saved(state: Bundle) = update { data ->
        if (data.getInt("originalPid") == Process.myPid()) data
            .put("warmSavedPid", Process.myPid()).put("warmSavedTaskId", activity.taskId)
            .put("warmSavedLocatorMatches", locatorMatches(state)).put("warmSavedAt", SystemClock.elapsedRealtime())
    }
    fun stopped() = update { data ->
        val clean = cleared()
        data.put("latestStoppedPid", Process.myPid()).put("latestStoppedTaskId", activity.taskId)
            .put("latestStoppedCleared", clean)
        if (data.getInt("originalPid") == Process.myPid()) data
            .put("warmStoppedPid", Process.myPid()).put("warmStoppedTaskId", activity.taskId)
            .put("warmStoppedCleared", clean).put("warmStoppedAt", SystemClock.elapsedRealtime())
    }
}
