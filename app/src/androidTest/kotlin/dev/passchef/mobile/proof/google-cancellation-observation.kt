package dev.passchef.mobile.proof

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Looper
import androidx.compose.runtime.State
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.cookbook.AndroidSheetsAuthorization
import dev.passchef.mobile.cookbook.CookbookController
import dev.passchef.mobile.cookbook.CookbookSources
import dev.passchef.mobile.cookbook.DishLifetime
import dev.passchef.mobile.cookbook.MenuSnapshotStore
import org.junit.Assert.assertTrue
import java.io.File

/** Reads only request presence/identity and safe state bits; never reads the Google reply/token. */
class GoogleCancellationObservation(val state: ColdCacheObservation, private val baseline: ColdCacheBaseline) : AutoCloseable {
    private val activity = state.activity
    private val pendingField = AndroidSheetsAuthorization::class.java.getDeclaredField("pending").apply { isAccessible = true }
    private var request: Any? = null
    private var stopped = 0
    private var stopsClean = true
    private var pendingAtStop = false
    private var providerSeen = false
    private fun <T> onMain(action: () -> T): T = if (Looper.myLooper() == Looper.getMainLooper()) action() else timerMain(action)
    private val monitor = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityStopped(value: Activity) {
            if (value !== activity) return
            stopped++
            runCatching {
                val pending = pendingField.get(state.book().authorization)
                if (request == null) request = pending
                pendingAtStop = pendingAtStop || (pending != null && pending === request)
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
    init { activity.application.registerActivityLifecycleCallbacks(monitor) }
    fun pending(): Boolean = onMain {
        val pending = pendingField.get(state.book().authorization)
        if (pending != null) {
            if (request == null) request = pending
            assertTrue("Only the originally observed real authorization request is pending", pending === request)
        }
        pending != null
    }
    private fun cleared(): Boolean = onMain {
        val vault = state.vault(); val book = state.book(); val source = book.sources
        val session = VaultController::class.java.getDeclaredField("session").apply { isAccessible = true }.get(vault)
        val token = CookbookSources::class.java.getDeclaredField("accessToken").apply { isAccessible = true }.get(source)
        val lifetime = CookbookController::class.java.getDeclaredField("lifetime").apply { isAccessible = true }.get(book) as DishLifetime
        val ready = CookbookController::class.java.getDeclaredField("readyDish\$delegate").apply { isAccessible = true }.get(book) as State<*>
        session == null && token == null && !vault.unlocked && !vault.readyForCooking() && !vault.busy &&
            book.selected == null && source.metadata == null && source.draft == null && !source.busy &&
            !book.hasDish() && ready.value == false && !lifetime.ready(true) && !book.revealed && book.dish.isEmpty() && !book.cooking
    }
    fun noResolutionObserved(): Boolean = onMain { stopped == 0 && request == null && !pending() }
    fun observeProvider(): Boolean {
        assertTrue("Every observed natural stop clears all sensitive state", onMain { stopsClean })
        if (!pending() || timerMain { stopped == 0 || !pendingAtStop || !stopsClean }) return false
        val root = InstrumentationRegistry.getInstrumentation().uiAutomation.rootInActiveWindow
        val google = try { root?.packageName?.toString() == "com.google.android.gms" } finally { root?.recycle() }
        if (google && !state.foreground()) providerSeen = true
        return providerSeen
    }
    fun assertPreserved() {
        assertTrue("Observed stop failures must never become skipped cancellation proof", onMain { stopsClean })
        assertTrue("Exact encrypted jars and last-good offline cache bytes remain unchanged", baseline.matches(activity.proofDirectory))
        val inventory = MenuSnapshotStore(File(activity.proofDirectory, "menus")).inventory()
        assertTrue("Exact last-good cache and jars survive the external Google flow", inventory.corruptFiles == 0 &&
            inventory.snapshots == listOf(coldExpected(baseline.refreshedAt)) && timerMain {
                state.book().sources.snapshots == inventory.snapshots
            })
    }
    fun assertReturned() {
        assertTrue("Actual Google external UI, pending request and natural stop were observed", providerSeen && timerMain {
            stopped > 0 && pendingAtStop && stopsClean && request != null
        })
        assertTrue("The real stale callback consumed its pending request after lock", !pending())
        assertTrue("Returned secure foreground remains locked with no session, token, Dish or usable Copy", state.foreground() && cleared())
        assertTrue("Stale callback cannot replace the disconnected source state", timerMain { state.book().sources.status == "OFFLINE" })
        assertPreserved()
    }
    fun report(phase: String, failed: Boolean = false) {
        val bits = state.flags() + onMain { mapOf("pending" to pending(), "pendingAtStop" to pendingAtStop,
            "stopped" to (stopped > 0), "allStopsClean" to stopsClean, "providerObserved" to providerSeen,
            "metadataPresent" to (state.book().sources.metadata != null),
            "connectRequired" to (state.book().sources.status == "CONNECT_REQUIRED"),
            "accessDenied" to (state.book().sources.status == "ACCESS_DENIED"),
            "networkFailed" to (state.book().sources.status == "NETWORK_FAILED")) }
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefGoogleCancelPhase", phase)
            putString("passchefGoogleCancelFixtureId", activity.proofId)
            putBoolean("passchefGoogleCancelFailed", failed)
            bits.forEach { (key, value) -> putBoolean("passchefGoogleCancel_$key", value) }
        })
    }
    override fun close() { activity.application.unregisterActivityLifecycleCallbacks(monitor) }
}
