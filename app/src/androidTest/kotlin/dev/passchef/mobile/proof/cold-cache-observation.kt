package dev.passchef.mobile.proof

import android.app.KeyguardManager
import android.os.Bundle
import android.os.PowerManager
import android.os.Process
import android.os.SystemClock
import android.view.WindowManager
import androidx.compose.runtime.State
import androidx.lifecycle.Lifecycle
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.MainActivity
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.cookbook.*
import org.junit.Assert.assertTrue

/** Reflection reads only presence/permission bits of this canonical UUID-owned real controller. */
class ColdCacheObservation(val activity: ColdStartProofActivity) {
    var phase = "cold-proof-start"
        private set
    private fun owned(name: String) = MainActivity::class.java.getDeclaredField(name).apply { isAccessible = true }.get(activity)
    fun vault() = owned("controller") as VaultController
    fun book() = owned("cookbook") as CookbookController
    fun flags(): Map<String, Boolean> = timerMain {
        val vault = vault(); val book = book(); val window = activity.window; val decor = window.decorView
        val power = activity.getSystemService(PowerManager::class.java)
        val keyguard = activity.getSystemService(KeyguardManager::class.java)
        val lifetime = CookbookController::class.java.getDeclaredField("lifetime").apply { isAccessible = true }.get(book) as DishLifetime
        val ready = CookbookController::class.java.getDeclaredField("readyDish\$delegate").apply { isAccessible = true }.get(book) as State<*>
        mapOf("resumed" to (activity.lifecycle.currentState == Lifecycle.State.RESUMED), "awake" to power.isInteractive,
            "keyguardAbsent" to (!keyguard.isKeyguardLocked && !keyguard.isDeviceLocked), "focused" to decor.hasWindowFocus(),
            "attached" to decor.isAttachedToWindow, "shown" to decor.isShown,
            "secure" to (window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0),
            "unlocked" to vault.unlocked, "ready" to vault.readyForCooking(), "busy" to vault.busy,
            "sessionAbsent" to (VaultController::class.java.getDeclaredField("session").apply { isAccessible = true }.get(vault) == null),
            "tokenAbsent" to (CookbookSources::class.java.getDeclaredField("accessToken").apply { isAccessible = true }.get(book.sources) == null),
            "hasDish" to book.hasDish(), "readyDish" to (ready.value == true), "lifetimePresent" to lifetime.ready(true),
            "revealed" to book.revealed, "dishNonempty" to book.dish.isNotEmpty(), "cooking" to book.cooking,
            "selectedAbsent" to (book.selected == null), "metadataAbsent" to (book.sources.metadata == null),
            "draftAbsent" to (book.sources.draft == null), "sourceBusy" to book.sources.busy,
            "cacheMemoryEmpty" to book.sources.snapshots.isEmpty())
    }
    fun foreground(): Boolean = flags().let { state ->
        listOf("resumed", "awake", "keyguardAbsent", "focused", "attached", "shown", "secure").all { state[it] == true }
    }
    fun requireReady() = assertTrue("Actual secure ready own foreground required", foreground() && flags()["ready"] == true)
    fun await(condition: () -> Boolean) {
        val deadline = SystemClock.elapsedRealtime() + 180_000
        var nextReport = SystemClock.elapsedRealtime() + 10_000
        while (!condition() && SystemClock.elapsedRealtime() < deadline) {
            if (SystemClock.elapsedRealtime() >= nextReport) { report(phase); nextReport += 10_000 }
            Thread.sleep(100)
        }
        assertTrue("Bounded actual cold proof state became ready", condition())
    }
    fun report(name: String, baseline: ColdCacheBaseline? = null, failed: Boolean = false) {
        phase = name
        val state = runCatching(::flags).getOrDefault(emptyMap())
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefColdPhase", name); putString("passchefColdFixtureId", activity.proofId)
            putInt("passchefColdPid", Process.myPid()); putInt("passchefColdUid", Process.myUid())
            putBoolean("passchefColdFailed", failed)
            baseline?.let { putInt("passchefColdWarmPid", it.warmPid); putString("passchefColdJarSha256", it.jarSha)
                putString("passchefColdMenuSha256", it.menuSha); putLong("passchefColdRefreshedAt", it.refreshedAt) }
            state.forEach { (key, value) -> putBoolean("passchefCold_$key", value) }
        })
    }
    fun initialLocked() {
        val state = flags()
        assertTrue("Fresh process starts locked without any usable session, grant or Dish", foreground() &&
            listOf("sessionAbsent", "tokenAbsent", "selectedAbsent", "metadataAbsent", "draftAbsent", "cacheMemoryEmpty").all { state[it] == true } &&
            listOf("unlocked", "ready", "busy", "hasDish", "readyDish", "lifetimePresent", "revealed", "dishNonempty", "cooking", "sourceBusy").all { state[it] == false })
    }
}
