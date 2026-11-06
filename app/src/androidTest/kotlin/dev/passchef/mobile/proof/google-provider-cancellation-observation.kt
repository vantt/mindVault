package dev.passchef.mobile.proof

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Looper
import android.os.Process
import androidx.compose.runtime.State
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.cookbook.*
import java.io.File
import org.junit.Assert.assertTrue

/** Actual provider-A bytes and original request identity; no grant or reply contents are read. */
class GoogleProviderCancellationObservation(val state: ColdCacheObservation,
    private val baseline: GoogleColdBaseline, private val approval: String, val action: String) : AutoCloseable {
    val activity = state.activity
    val expected = baseline.snapshot(activity.proofDirectory)
    private val pendingField = AndroidSheetsAuthorization::class.java.getDeclaredField("pending").apply { isAccessible = true }
    private var request: Any? = null
    private var stopped = 0
    private var pendingAtStop = false
    private var stopsClean = true
    private var providerSeen = false
    var phase = "provider-cancellation-observer-start"
        private set
    var differentSourceExercised = false
        private set
    private fun <T> main(block: () -> T): T = if (Looper.myLooper() == Looper.getMainLooper()) block() else timerMain(block)
    private fun files(): Map<String, ByteArray> {
        val root = activity.proofDirectory.canonicalFile
        val controls = setOf("google-provider-cancel-route.json", "google-provider-cancel-route.json.pending")
        return root.walkTopDown().onEnter { directory ->
            require(directory.canonicalFile == directory && directory.toPath().startsWith(root.toPath())); true
        }.filter { it.isFile }.mapNotNull { file ->
            require(file.canonicalFile == file && file.toPath().startsWith(root.toPath()))
            val relative = file.relativeTo(root).invariantSeparatorsPath
            if (relative in controls) null else relative to file.readBytes()
        }.toMap()
    }
    private val frozen = files()
    private val inventory = MenuSnapshotStore(File(activity.proofDirectory, "menus")).inventory()
    private val monitor = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityStopped(value: Activity) {
            if (value !== activity) return
            stopped++
            runCatching {
                val current = pendingField.get(state.book().authorization)
                if (request == null) request = current
                pendingAtStop = pendingAtStop || (current != null && current === request)
                stopsClean = stopsClean && cleared() && baseline.matches(activity.proofDirectory)
            }.onFailure { stopsClean = false }
        }
        override fun onActivityCreated(value: Activity, saved: Bundle?) = Unit
        override fun onActivityStarted(value: Activity) = Unit
        override fun onActivityResumed(value: Activity) = Unit
        override fun onActivityPaused(value: Activity) = Unit
        override fun onActivityDestroyed(value: Activity) = Unit
        override fun onActivitySaveInstanceState(value: Activity, saved: Bundle) = Unit
    }
    init {
        require(listOf(approval, action).all { it.matches(Regex("[a-f0-9]{64}")) })
        assertTrue("Actual provider-A baseline is exact before arming cancellation", baseline.matches(activity.proofDirectory) &&
            inventory.corruptFiles == 0 && inventory.snapshots == listOf(expected) &&
            main { state.book().sources.snapshots == listOf(expected) && pendingField.get(state.book().authorization) == null })
        activity.application.registerActivityLifecycleCallbacks(monitor)
    }
    fun armWitness(): GoogleResultProofWitness = main {
        assertTrue("Diagnostic witness is armed once only after provider load/401 and with no pending request",
            activity.googleResultProof == null && pendingField.get(state.book().authorization) == null)
        GoogleResultProofWitness(activity).also { witness ->
            ColdStartProofActivity::class.java.getDeclaredField("googleResultProof").apply { isAccessible = true }.set(activity, witness)
            assertTrue("Only the existing diagnostic field was initialized", activity.googleResultProof === witness)
        }
    }
    fun pending(): Boolean = main {
        val current = pendingField.get(state.book().authorization)
        if (current != null) {
            if (request == null) request = current
            assertTrue("Only the originally observed SDK request remains pending", current === request)
        }
        current != null
    }
    private fun cleared(): Boolean = main {
        val vault = state.vault(); val book = state.book(); val source = book.sources
        val session = VaultController::class.java.getDeclaredField("session").apply { isAccessible = true }.get(vault)
        val token = CookbookSources::class.java.getDeclaredField("accessToken").apply { isAccessible = true }.get(source)
        val lifetime = CookbookController::class.java.getDeclaredField("lifetime").apply { isAccessible = true }.get(book) as DishLifetime
        val ready = CookbookController::class.java.getDeclaredField("readyDish\$delegate").apply { isAccessible = true }.get(book) as State<*>
        session == null && token == null && !vault.unlocked && !vault.readyForCooking() && !vault.busy &&
            book.selected == null && source.metadata == null && source.draft == null && !source.busy &&
            !book.hasDish() && ready.value == false && !lifetime.ready(true) && !book.revealed && book.dish.isEmpty() && !book.cooking
    }
    fun observeProvider(): Boolean {
        assertTrue("Every natural stop clears session/grant/metadata/Dish", main { stopsClean })
        if (!pending() || main { stopped == 0 || !pendingAtStop }) return false
        val root = InstrumentationRegistry.getInstrumentation().uiAutomation.rootInActiveWindow
        val google = try { root?.packageName?.toString() == "com.google.android.gms" } finally { root?.recycle() }
        if (google && !state.foreground()) providerSeen = true
        return google && !state.foreground()
    }
    fun assertPreserved() {
        val actual = files()
        assertTrue("Actual provider-A full regular-file bytes, jars, cache/inventory and RAM remain exact",
            main { stopsClean && state.book().sources.snapshots == listOf(expected) } && baseline.matches(activity.proofDirectory) &&
                actual.keys == frozen.keys && frozen.all { (key, bytes) -> bytes.contentEquals(actual.getValue(key)) } &&
                MenuSnapshotStore(File(activity.proofDirectory, "menus")).inventory() == inventory)
    }
    fun noResolutionObserved() = main { stopped == 0 && request == null && !pending() }
    fun assertDifferentDraft(id: String, title: String) {
        assertTrue("Different-source preview keeps original pending request and saves no B cache", pending() && main {
            val source = state.book().sources
            val draft = source.draft
            val ownership = CookbookSources::class.java.getDeclaredField("ownership").apply { isAccessible = true }.get(source)
            val context = SourceOwnership::class.java.getDeclaredField("source").apply { isAccessible = true }.get(ownership)
            !source.busy && source.status == "CONFIRM" && source.metadata == null && draft != null && draft.origin == MenuOrigin(id, 0, title) &&
                draft.origin.key != expected.mapping.origin.key && context == draft.origin.key &&
                MenuSnapshotStore(File(activity.proofDirectory, "menus")).load(draft.origin) == null
        })
        assertPreserved(); differentSourceExercised = true
    }
    fun assertReturned() {
        assertTrue("Actual external Google, original request and clean natural stop were observed", providerSeen && main {
            stopped > 0 && pendingAtStop && stopsClean && request != null
        })
        assertTrue("Real stale callback is consumed while secure returned Cookbook remains locked", !pending() && state.foreground() && cleared())
        assertTrue("Callback cannot publish metadata/draft into the disconnected source", main { state.book().sources.status == "OFFLINE" })
        assertPreserved()
    }
    fun report(phase: String, witness: GoogleResultProofWitness? = null) {
        this.phase = phase
        val snapshot = witness?.let { main(it::snapshot) }
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefProviderCancelPhase", phase); putString("passchefProviderCancelFixtureId", activity.proofId)
            putString("passchefProviderCancelApprovalSha256", approval); putString("passchefProviderCancelActionSha256", action)
            putInt("passchefProviderCancelPid", Process.myPid()); putInt("passchefProviderCancelTaskId", activity.taskId)
            putBoolean("passchefProviderCancelDifferentSourceExercised", differentSourceExercised)
            putBoolean("passchefProviderCancelPending", pending()); putBoolean("passchefProviderCancelAllStopsClean", main { stopsClean })
            snapshot?.let {
                putInt("passchefProviderCancelLaunches", it.launches); putInt("passchefProviderCancelResults", it.results)
                putInt("passchefProviderCancelForwarded", it.forwarded)
                it.requestCode?.let { code -> putInt("passchefProviderCancelRequestCode", code) }
                it.resultCode?.let { code -> putInt("passchefProviderCancelResultCode", code) }
                putBoolean("passchefProviderCancelMatchedOriginal", it.matchedOriginal)
                putBoolean("passchefProviderCancelOriginalStillPending", it.originalStillPending)
                putBoolean("passchefProviderCancelConsumed", it.consumed); putBoolean("passchefProviderCancelWitnessFailed", it.failed)
            }
        })
    }
    override fun close() { activity.application.unregisterActivityLifecycleCallbacks(monitor) }
}
