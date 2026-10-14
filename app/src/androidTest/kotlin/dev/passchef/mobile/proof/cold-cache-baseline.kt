package dev.passchef.mobile.proof

import android.os.Process
import dev.passchef.mobile.KeystoreCrypto
import dev.passchef.mobile.cookbook.*
import dev.passchef.mobile.vault.AtomicRecordStore
import dev.passchef.mobile.vault.VaultRepository
import org.json.JSONObject
import org.junit.Assert.assertTrue
import java.io.File
import java.security.MessageDigest

val coldOrigin = MenuOrigin("Menu_Cold_0123456789", 0, "Cold Menu")
val coldEntry = MenuEntry(coldOrigin, 2, "abcd#1", "Mail-cold", "00123")
fun coldExpected(refreshedAt: Long) = MenuSnapshot(mapping = MenuMapping(coldOrigin,
    mapOf(ColumnRole.RECIPE to 1, ColumnRole.LABEL to 0, ColumnRole.USERNAME to 2), 2,
    listOf("Label", "Recipe", "Username")), entries = listOf(coldEntry), refreshedAt = refreshedAt,
    favorites = setOf(coldEntry.id), recent = listOf(coldEntry.id))
private fun coldHash(bytes: ByteArray) = MessageDigest.getInstance("SHA-256").digest(bytes)
    .joinToString("") { "%02x".format(it) }
fun coldJarFile(directory: File) = File(directory, "cookbook-v1.json")
private fun coldMenuFile(directory: File) = File(File(directory, "menus"),
    coldHash(coldOrigin.key.toByteArray(Charsets.UTF_8)) + ".menu.json")
data class ColdCacheBaseline(val fixtureId: String, val warmPid: Int, val jarSha: String, val menuSha: String, val refreshedAt: Long) {
    fun jarsMatch(directory: File): Boolean = jarSha == coldHash(coldJarFile(directory).readBytes())
    fun matches(directory: File): Boolean = jarsMatch(directory) &&
        menuSha == coldHash(coldMenuFile(directory).readBytes())
    fun write(directory: File) {
        val file = File(directory, "cold-baseline.json")
        require(!file.exists())
        AtomicRecordStore(file).write(JSONObject().put("version", 1).put("fixtureId", fixtureId).put("warmPid", warmPid)
            .put("jarSha256", jarSha).put("menuSha256", menuSha).put("refreshedAt", refreshedAt).toString().toByteArray())
    }
    companion object {
        fun create(activity: ColdStartProofActivity, snapshot: MenuSnapshot): ColdCacheBaseline {
            assertTrue("Warm exact synthetic offline cache includes favorite and recent", snapshot == coldExpected(snapshot.refreshedAt))
            return ColdCacheBaseline(activity.proofId, Process.myPid(), coldHash(coldJarFile(activity.proofDirectory).readBytes()),
                coldHash(coldMenuFile(activity.proofDirectory).readBytes()), snapshot.refreshedAt)
        }
        fun read(activity: ColdStartProofActivity): ColdCacheBaseline {
            val file = File(activity.proofDirectory, "cold-baseline.json")
            require(file.length() in 1..4096)
            val data = JSONObject(file.readText())
            require(data.getInt("version") == 1 && data.getString("fixtureId") == activity.proofId)
            return ColdCacheBaseline(data.getString("fixtureId"), data.getInt("warmPid"), data.getString("jarSha256"),
                data.getString("menuSha256"), data.getLong("refreshedAt")).also {
                require(it.warmPid > 0 && it.refreshedAt >= 0 && listOf(it.jarSha, it.menuSha).all { sha -> sha.matches(Regex("[0-9a-f]{64}")) })
            }
        }
    }
}
/** Called only for the exact validated fixture; SIGKILL cannot execute this cleanup. */
fun cleanupColdFixture(activity: ColdStartProofActivity) {
    val directory = activity.proofDirectory
    VaultRepository(AtomicRecordStore(coldJarFile(directory))).record()?.let { record ->
        (listOf(record.alias) + record.retiredAliases).filter { it.isNotEmpty() }.forEach(KeystoreCrypto()::deleteKey)
    }
    require(directory.deleteRecursively())
}
