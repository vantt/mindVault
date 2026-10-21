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

/** Presence bits only; no token, reply, HTTP body, account or Menu content is read. */
class GoogleProviderDeniedObservation(private val state: ColdCacheObservation,
    private val baseline: GoogleColdBaseline) : AutoCloseable {
    private val activity = state.activity
    private val expected = baseline.snapshot(activity.proofDirectory)
    private val jarBytes = coldJarFile(activity.proofDirectory).readBytes()
    private fun files() = File(activity.proofDirectory, "menus").walkTopDown().filter { it.isFile }
        .associate { it.relativeTo(activity.proofDirectory).path to it.readBytes() }
    private val cacheBytes = files()
    private fun exactBytes(): Boolean {
        val actual = files()
        return jarBytes.contentEquals(coldJarFile(activity.proofDirectory).readBytes()) &&
            actual.keys == cacheBytes.keys && cacheBytes.all { (key, bytes) -> bytes.contentEquals(actual.getValue(key)) }
    }
    private val pendingField = AndroidSheetsAuthorization::class.java.getDeclaredField("pending").apply { isAccessible = true }
    private var stopped = 0
    private var stopsClean = true
    private fun <T> main(action: () -> T): T = if (Looper.myLooper() == Looper.getMainLooper()) action() else timerMain(action)
    fun pending(): Boolean = main { pendingField.get(state.book().authorization) != null }
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
    private val monitor = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityStopped(value: Activity) {
            if (value !== activity) return
            stopped++
            stopsClean = stopsClean && runCatching { cleared() && (baseline.matches(activity.proofDirectory) && exactBytes()) }.getOrDefault(false)
        }
        override fun onActivityCreated(value: Activity, saved: Bundle?) = Unit
        override fun onActivityStarted(value: Activity) = Unit
        override fun onActivityResumed(value: Activity) = Unit
        override fun onActivityPaused(value: Activity) = Unit
        override fun onActivityDestroyed(value: Activity) = Unit
        override fun onActivitySaveInstanceState(value: Activity, saved: Bundle) = Unit
    }
    init { activity.application.registerActivityLifecycleCallbacks(monitor) }
    fun assertPreserved() {
        assertTrue("Every natural stop clears session/token/Dish and preserves exact fixture bytes", main { stopsClean })
        assertTrue("Exact provider-loaded cache and encrypted jars remain unchanged", (baseline.matches(activity.proofDirectory) && exactBytes()))
        val inventory = MenuSnapshotStore(File(activity.proofDirectory, "menus")).inventory()
        assertTrue("Exact provider-loaded mapping/cache/favorites/recent stay in RAM and inventory", inventory.corruptFiles == 0 &&
            inventory.snapshots == listOf(expected) && main {
                state.book().sources.snapshots == inventory.snapshots
            })
    }
    fun assertDishCleared() {
        val bits = state.flags()
        assertTrue("Old Dish/reveal/Copy lifetime cannot survive explicit Connect", listOf("hasDish", "readyDish",
            "lifetimePresent", "revealed", "dishNonempty", "cooking").all { bits[it] == false })
        if (main { stopped > 0 }) assertTrue("External stopped result remains locked with no usable token/session", cleared())
    }
    fun assertDenied() {
        state.requireReady(); assertPreserved(); assertDishCleared()
        // Production SheetsClient.decodeOrThrow creates SheetsForbiddenException only for response status403;
        // CookbookSources.failure maps only that type to ACCESS_DENIED. SDK/401 map CONNECT_REQUIRED.
        assertTrue("Real current provider refusal uniquely maps HTTP403 to ACCESS_DENIED", main {
            val source = state.book().sources
            stopped == 0 && !pending() && !source.busy && source.status == "ACCESS_DENIED" &&
                source.metadata == null && source.draft == null
        })
        assertTrue("403 retains foreground authorized session and RAM grant; no401 clearing or stale callback credit",
            state.flags().let { it["ready"] == true && it["sessionAbsent"] == false && it["tokenAbsent"] == false })
    }
    fun report(phase: String, failed: Boolean = false) {
        val bits = state.flags() + main {
            val status = state.book().sources.status
            mapOf("pending" to pending(), "stopped" to (stopped > 0), "allStopsClean" to stopsClean,
                "actual403" to (status == "ACCESS_DENIED"), "stoppedReturnLocked" to (stopped > 0 && cleared()),
                "sdkOrAuthorizationOr401" to (status == "CONNECT_REQUIRED"),
                "networkOrOtherHttp" to (status == "NETWORK_FAILED"),
                "metadataReturned" to (state.book().sources.metadata != null), "offlineAfterLock" to (status == "OFFLINE"))
        }
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefProviderDeniedPhase", phase)
            putString("passchefProviderDeniedFixtureId", activity.proofId)
            putString("passchefProviderDeniedJarSha256", baseline.jarSha)
            putString("passchefProviderDeniedMenuSha256", baseline.menuSha)
            putBoolean("passchefProviderDeniedFailed", failed)
            bits.forEach { (key, value) -> putBoolean("passchefProviderDenied_$key", value) }
        })
    }
    fun assertFreshCook() {
        state.requireReady()
        assertTrue("Fresh cached Cook preserves encrypted jars and complete expected snapshot", baseline.jarsMatch(activity.proofDirectory) &&
            jarBytes.contentEquals(coldJarFile(activity.proofDirectory).readBytes()) &&
            baseline.snapshot(activity.proofDirectory) == expected && main { state.book().sources.snapshots == listOf(expected) })
        assertTrue("Fresh Cook selects the provider entry with a new usable Dish", main {
            state.book().selected == googleColdEntry(expected.mapping.origin) && state.book().hasDish()
        })
    }
    override fun close() { activity.application.unregisterActivityLifecycleCallbacks(monitor) }
}
