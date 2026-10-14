package dev.passchef.mobile.proof

import android.os.Process
import dev.passchef.mobile.cookbook.*
import dev.passchef.mobile.vault.AtomicRecordStore
import org.json.JSONObject
import org.junit.Assert.assertTrue
import java.io.File
import java.security.MessageDigest

fun googleColdHash(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256").digest(bytes)
    .joinToString("") { "%02x".format(it) }
fun googleColdEntry(origin: MenuOrigin) = MenuEntry(origin, 2, "abcd#1", "Mail-test", "00123")
fun googleColdExpected(origin: MenuOrigin, refreshedAt: Long): MenuSnapshot {
    val entry = googleColdEntry(origin)
    return MenuSnapshot(mapping = MenuMapping(origin,
        mapOf(ColumnRole.LABEL to 0, ColumnRole.RECIPE to 1, ColumnRole.USERNAME to 2), 2,
        listOf("Label", "Recipe", "Username")), entries = listOf(entry), refreshedAt = refreshedAt,
        favorites = setOf(entry.id), recent = listOf(entry.id))
}
/** Provider schema is disjoint from offline baseline; only identity hashes/numeric metadata are stored. */
data class GoogleColdBaseline(val fixtureId: String, val warmPid: Int, val jarSha: String, val menuSha: String,
    val menuFileName: String, val menuIdSha: String, val tabId: Int, val refreshedAt: Long) {
    private fun menuFile(directory: File) = File(File(directory, "menus"), menuFileName)
    fun jarsMatch(directory: File) = jarSha == googleColdHash(coldJarFile(directory).readBytes())
    fun matches(directory: File) = jarsMatch(directory) && menuSha == googleColdHash(menuFile(directory).readBytes())
    fun write(directory: File) {
        val file = File(directory, "cold-baseline.json")
        require(!file.exists())
        AtomicRecordStore(file).write(JSONObject().put("version", 2).put("kind", "google-provider-cache")
            .put("fixtureId", fixtureId).put("warmPid", warmPid).put("jarSha256", jarSha).put("menuSha256", menuSha)
            .put("menuFileName", menuFileName).put("menuIdSha256", menuIdSha).put("tabId", tabId)
            .put("refreshedAt", refreshedAt).toString().toByteArray())
    }
    fun snapshot(directory: File): MenuSnapshot {
        val inventory = MenuSnapshotStore(File(directory, "menus")).inventory()
        assertTrue("Provider-owned inventory contains one uncorrupted cache", inventory.corruptFiles == 0 && inventory.snapshots.size == 1)
        return inventory.snapshots.single().also { saved ->
            val origin = saved.mapping.origin
            assertTrue("Frozen actual provider origin/tab and synthetic schema remain exact",
                googleColdHash(origin.menuId.toByteArray()) == menuIdSha && origin.tabId == tabId && origin.title == "Test" &&
                    menuFileName == googleColdHash(origin.key.toByteArray()) + ".menu.json" &&
                    saved == googleColdExpected(origin, refreshedAt))
        }
    }
    companion object {
        fun create(activity: ColdStartProofActivity, saved: MenuSnapshot): GoogleColdBaseline {
            val origin = saved.mapping.origin
            assertTrue("Genuine provider cache/favorite/recent use the exact dedicated synthetic schema",
                origin.title == "Test" && saved == googleColdExpected(origin, saved.refreshedAt))
            val fileName = googleColdHash(origin.key.toByteArray()) + ".menu.json"
            return GoogleColdBaseline(activity.proofId, Process.myPid(), googleColdHash(coldJarFile(activity.proofDirectory).readBytes()),
                googleColdHash(File(File(activity.proofDirectory, "menus"), fileName).readBytes()), fileName,
                googleColdHash(origin.menuId.toByteArray()), origin.tabId, saved.refreshedAt)
        }
        fun read(activity: ColdStartProofActivity): GoogleColdBaseline {
            val file = File(activity.proofDirectory, "cold-baseline.json")
            require(file.length() in 1..4096)
            val data = JSONObject(file.readText())
            require(data.getInt("version") == 2 && data.getString("kind") == "google-provider-cache" &&
                data.getString("fixtureId") == activity.proofId)
            return GoogleColdBaseline(data.getString("fixtureId"), data.getInt("warmPid"), data.getString("jarSha256"),
                data.getString("menuSha256"), data.getString("menuFileName"), data.getString("menuIdSha256"),
                data.getInt("tabId"), data.getLong("refreshedAt")).also { baseline ->
                require(baseline.warmPid > 0 && baseline.tabId >= 0 && baseline.refreshedAt >= 0 &&
                    baseline.menuFileName.matches(Regex("[0-9a-f]{64}\\.menu\\.json")) &&
                    listOf(baseline.jarSha, baseline.menuSha, baseline.menuIdSha).all { it.matches(Regex("[0-9a-f]{64}")) })
            }
        }
    }
}
