package dev.passchef.mobile.proof

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Looper
import android.os.SystemClock
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.cookbook.*
import java.io.File
import org.json.JSONObject
import org.junit.Assert.assertTrue

/** Read-only ownership/presence metadata. Never inspects a grant value, account or HTTP body. */
class GoogleHttp401Observation(private val state: ColdCacheObservation,
    private val baseline: GoogleColdBaseline, private val approvalSha: String) : AutoCloseable {
    private fun <T> main(action: () -> T): T = if (Looper.myLooper() == Looper.getMainLooper()) action() else timerMain(action)
    private val activity = state.activity
    private val expected = baseline.snapshot(activity.proofDirectory)
    private val ram = main { state.book().sources.snapshots.toList() }
    private val inventory = MenuSnapshotStore(File(activity.proofDirectory, "menus")).inventory()
    private val metadata = main { requireNotNull(state.book().sources.metadata) }
    val tab = metadata.sheets.single { it.properties.sheetId == baseline.tabId && it.properties.title == "Test" }.properties
    private val sessionField = VaultController::class.java.getDeclaredField("session").apply { isAccessible = true }
    private val session = main { requireNotNull(sessionField.get(state.vault())) }
    private val ownership = main { CookbookSources::class.java.getDeclaredField("ownership").apply { isAccessible = true }
        .get(state.book().sources) as SourceOwnership }
    private val generationField = SourceOwnership::class.java.getDeclaredField("generation").apply { isAccessible = true }
    private val contextField = SourceOwnership::class.java.getDeclaredField("source").apply { isAccessible = true }
    private val originalGeneration = main { generationField.getLong(ownership) }
    private val originalContext = main { contextField.get(ownership) }
    private fun files(): Map<String, ByteArray> {
        val root = activity.proofDirectory.canonicalFile
        val controls = setOf("google401-approved-action.json", "google401-approved-action.json.pending")
        return root.walkTopDown().onEnter { directory ->
            require(directory.canonicalFile == directory && directory.toPath().startsWith(root.toPath()))
            true
        }.filter { it.isFile }.mapNotNull { file ->
            require(file.canonicalFile == file && file.toPath().startsWith(root.toPath()))
            val relative = file.relativeTo(root).invariantSeparatorsPath
            if (relative in controls) null else relative to file.readBytes()
        }.toMap()
    }
    private val frozen = files()
    private var stops = 0
    private val monitor = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityStopped(value: Activity) { if (value === activity) stops++ }
        override fun onActivityCreated(value: Activity, saved: Bundle?) = Unit
        override fun onActivityStarted(value: Activity) = Unit
        override fun onActivityResumed(value: Activity) = Unit
        override fun onActivityPaused(value: Activity) = Unit
        override fun onActivityDestroyed(value: Activity) = Unit
        override fun onActivitySaveInstanceState(value: Activity, saved: Bundle) = Unit
    }
    init {
        require(approvalSha.matches(Regex("[a-f0-9]{64}")))
        assertTrue("Actual provider baseline has exact RAM/inventory", ram == listOf(expected) &&
            inventory.corruptFiles == 0 && inventory.snapshots == ram)
        activity.application.registerActivityLifecycleCallbacks(monitor)
    }
    private fun pending() = main { dev.passchef.mobile.cookbook.AndroidSheetsAuthorization::class.java
        .getDeclaredField("pending").apply { isAccessible = true }.get(state.book().authorization) != null }
    fun assertDishCleared() {
        val flags = state.flags()
        assertTrue("Sources navigation already clears the old Dish, selected row and usable Copy",
            listOf("hasDish", "readyDish", "lifetimePresent", "revealed", "dishNonempty", "cooking").all { flags[it] == false } &&
                flags["selectedAbsent"] == true)
    }
    private fun active(generation: Long) {
        state.requireReady(); assertDishCleared()
        assertTrue("Exclusive read retains the original session/metadata without stop, SDK callback or unrelated operation", main {
            stops == 0 && sessionField.get(state.vault()) === session && !pending() &&
                state.book().sources.metadata == metadata && generationField.getLong(ownership) == generation &&
                contextField.get(ownership) == if (generation == originalGeneration) originalContext else expected.mapping.origin.key
        })
    }
    fun assertPreserved() {
        val actual = files()
        assertTrue("All regular provider cache/jar bytes and inventory stay exact before fresh Cook",
            actual.keys == frozen.keys && frozen.all { (key, bytes) -> bytes.contentEquals(actual.getValue(key)) } &&
                baseline.matches(activity.proofDirectory) && MenuSnapshotStore(File(activity.proofDirectory, "menus")).inventory() == inventory &&
                main { state.book().sources.snapshots == ram })
    }
    fun checkpoint() {
        active(originalGeneration); assertPreserved()
        val tokenPresent = state.flags()["tokenAbsent"] == false
        assertTrue("Retained-grant checkpoint has metadata/Test tab, no busy/draft and actual RAM grant", main {
            val source = state.book().sources
            !source.busy && source.draft == null && source.metadata?.sheets?.any { it.properties == tab } == true &&
                tokenPresent
        })
    }
    fun awaitApprovedAction() {
        val marker = File(activity.proofDirectory, "google401-approved-action.json")
        assertTrue("Fresh UUID has no externally completed action marker", !marker.exists())
        checkpoint(); report("owner-approved-transition-now")
        val deadline = SystemClock.elapsedRealtime() + 120_000
        while (!marker.exists() && SystemClock.elapsedRealtime() < deadline) { checkpoint(); Thread.sleep(200) }
        assertTrue("Approved external action completed within the bounded retained-grant checkpoint",
            SystemClock.elapsedRealtime() < deadline && marker.exists() && marker.canonicalFile == marker)
        val bytes = ByteArray(513)
        var size = 0
        marker.inputStream().use { input -> while (size < bytes.size) {
            val count = input.read(bytes, size, bytes.size - size); if (count < 0) break; if (count > 0) size += count
        } }
        require(size in 1..512)
        val data = JSONObject(String(bytes, 0, size, Charsets.UTF_8))
        assertTrue("Only actual externally approved action metadata for this UUID is accepted",
            data.keys().asSequence().toSet() == setOf("completed", "fixtureId", "approvalRecordSha256", "actionRecordSha256") &&
                data.get("completed") == true && data.getString("fixtureId") == activity.proofId &&
                data.getString("approvalRecordSha256") == approvalSha && data.getString("actionRecordSha256").matches(Regex("[a-f0-9]{64}")))
        checkpoint(); report("approved-action-marker-observed")
    }
    fun assertReadActive() = active(originalGeneration + 1)
    fun completedStatus(): String {
        assertReadActive()
        return main {
            assertTrue("The one current ordinary tab request completed", !state.book().sources.busy)
            state.book().sources.status
        }
    }
    fun assertUnauthorized() {
        assertTrue("Exclusive current tab-read path uniquely proves actual typed HTTP401", completedStatus() == "CONNECT_REQUIRED" &&
            state.flags()["tokenAbsent"] == true && main { state.book().sources.draft == null })
        assertPreserved(); report("actual401-exact-preservation-passed")
    }
    fun assertOther(status: String) {
        assertTrue("Other typed refusal/network outcome remains separate from401", completedStatus() == status &&
            status in setOf("ACCESS_DENIED", "NETWORK_FAILED") && state.flags()["tokenAbsent"] == false)
        assertPreserved()
    }
    fun assertSuccessfulRead() {
        assertTrue("Successful provider refresh is not401", completedStatus() == "SYNCED" && state.flags()["tokenAbsent"] == false &&
            baseline.jarsMatch(activity.proofDirectory))
        // A successful read may legitimately replace cached bytes/refreshedAt; never claim old-byte preservation here.
    }
    fun assertFreshCook() {
        state.requireReady()
        assertTrue("Fresh cached Cook after401 preserves jars and expected provider snapshot", baseline.jarsMatch(activity.proofDirectory) &&
            baseline.snapshot(activity.proofDirectory) == expected && main {
                stops == 0 && sessionField.get(state.vault()) === session && state.book().sources.snapshots == ram &&
                    state.book().selected == googleColdEntry(expected.mapping.origin) && state.book().hasDish()
            })
    }
    fun report(phase: String) {
        val flags = state.flags()
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefHttp401Phase", phase); putString("passchefHttp401FixtureId", activity.proofId)
            putString("passchefHttp401ApprovalSha256", approvalSha)
            putBoolean("passchefHttp401NoStops", main { stops == 0 }); putBoolean("passchefHttp401NoPending", !pending())
            putLong("passchefHttp401SourceGeneration", main { generationField.getLong(ownership) })
            putLong("passchefHttp401ExpectedReadGeneration", originalGeneration + 1)
            putBoolean("passchefHttp401TokenAbsent", flags["tokenAbsent"] == true)
            putBoolean("passchefHttp401Ready", flags["ready"] == true)
        })
    }
    override fun close() { activity.application.unregisterActivityLifecycleCallbacks(monitor) }
}
