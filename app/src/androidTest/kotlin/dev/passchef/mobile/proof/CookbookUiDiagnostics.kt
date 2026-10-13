package dev.passchef.mobile.proof

import android.app.Activity
import android.app.Application
import android.app.KeyguardManager
import android.os.Bundle
import android.os.PowerManager
import android.os.SystemClock
import android.view.WindowManager
import androidx.compose.runtime.State
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.MainActivity
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.cookbook.CookbookController
import dev.passchef.mobile.cookbook.DishLifetime
import dev.passchef.mobile.vault.SessionClock
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/** Unique proof Activity only; never emits field contents, keys, Dishes or exceptions. */
class CookbookUiDiagnostics(private val application: Application) : Application.ActivityLifecycleCallbacks, AutoCloseable {
    private val resumed = AtomicInteger()
    private val paused = AtomicInteger()
    private val stopped = AtomicInteger()
    private val destroyed = AtomicInteger()
    private val lastLifecycleAt = AtomicLong()
    private data class Snapshot(val elapsed: Long, val lifecycle: String, val flags: Map<String,Boolean>)
    @Volatile private var lastPause: Snapshot? = null
    @Volatile private var lastStop: Snapshot? = null
    init { application.registerActivityLifecycleCallbacks(this) }
    private fun event(activity: Activity, count: AtomicInteger) {
        if(activity is CookbookProofActivity) { count.incrementAndGet(); lastLifecycleAt.set(SystemClock.elapsedRealtime()) }
    }
    override fun onActivityResumed(activity: Activity) = event(activity,resumed)
    override fun onActivityPaused(activity: Activity) {
        event(activity,paused)
        if(activity is CookbookProofActivity) {
            val captured=snapshot(activity);lastPause=captured
            runCatching { report("lifecycle-paused",captured.lifecycle,captured.flags) }
        }
    }
    override fun onActivityStopped(activity: Activity) {
        event(activity,stopped)
        if(activity is CookbookProofActivity) {
            val captured=snapshot(activity);lastStop=captured
            runCatching { report("lifecycle-stopped",captured.lifecycle,captured.flags) }
        }
    }
    override fun onActivityDestroyed(activity: Activity) = event(activity,destroyed)
    override fun onActivityCreated(activity: Activity, state: Bundle?) = Unit
    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, state: Bundle) = Unit
    /** Framework callbacks run on main; snapshots contain only bounded lifecycle/boolean data. */
    private fun snapshot(activity: CookbookProofActivity): Snapshot {
        val elapsed=SystemClock.elapsedRealtime()
        var lifecycle="UNAVAILABLE"
        val flags=mutableMapOf("lifecycleReadSucceeded" to false,"stateReadSucceeded" to false,
            "environmentReadSucceeded" to false)
        runCatching { lifecycle=activity.lifecycle.currentState.name;flags["lifecycleReadSucceeded"]=true }
        runCatching { flags.putAll(environmentFlags(activity));flags["environmentReadSucceeded"]=true }
        runCatching { flags.putAll(stateFlags(activity));flags["stateReadSucceeded"]=true }
        return Snapshot(elapsed,lifecycle,flags.toMap())
    }
    private fun Bundle.putSnapshot(name:String,snapshot:Snapshot?) {
        putBoolean("passchef${name}Captured",snapshot!=null)
        snapshot?.let {
            putLong("passchef${name}Elapsed",it.elapsed);putString("passchef${name}Lifecycle",it.lifecycle)
            it.flags.forEach { (flag,value) -> putBoolean("passchef_${name.lowercase()}_$flag",value) }
        }
    }
    fun report(phase: String, lifecycle: String, flags: Map<String,Boolean>) {
        InstrumentationRegistry.getInstrumentation().sendStatus(0,Bundle().apply {
            putString("passchefUiPhase",phase);putString("passchefLifecycle",lifecycle)
            putLong("passchefElapsed",SystemClock.elapsedRealtime());putLong("passchefLastLifecycle",lastLifecycleAt.get())
            putInt("passchefResumed",resumed.get());putInt("passchefPaused",paused.get())
            putInt("passchefStopped",stopped.get());putInt("passchefDestroyed",destroyed.get())
            putSnapshot("Pause",lastPause);putSnapshot("Stop",lastStop)
            flags.forEach { (name,value) -> putBoolean("passchef_$name",value) }
        })
    }
    override fun close() { application.unregisterActivityLifecycleCallbacks(this) }
    companion object {
        /** Main-thread, unique proof window only; no device/settings changes or field contents. */
        fun environmentFlags(activity: CookbookProofActivity): Map<String,Boolean> {
            val power=activity.getSystemService(PowerManager::class.java)
            val keyguard=activity.getSystemService(KeyguardManager::class.java)
            val window=activity.window
            val decor=window.decorView
            return mapOf("powerInteractive" to power.isInteractive,"keyguardLocked" to keyguard.isKeyguardLocked,
                "deviceLocked" to keyguard.isDeviceLocked,
                "proofWindowKeepScreenOn" to (window.attributes.flags and WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON != 0),
                "proofWindowFocused" to decor.hasWindowFocus(),"proofWindowAttached" to decor.isAttachedToWindow,
                "proofWindowShown" to decor.isShown)
        }
        /** Read-only test reflection observes permission/presence bits; no production API/auth bypass. */
        fun stateFlags(activity: CookbookProofActivity): Map<String,Boolean> {
            fun owned(name:String)=MainActivity::class.java.getDeclaredField(name).apply { isAccessible=true }.get(activity)
            val vault=owned("controller") as VaultController
            val cookbook=owned("cookbook") as CookbookController
            val lifetime=CookbookController::class.java.getDeclaredField("lifetime").apply { isAccessible=true }.get(cookbook) as DishLifetime
            val ready=CookbookController::class.java.getDeclaredField("readyDish\$delegate").apply { isAccessible=true }.get(cookbook) as State<*>
            val active=VaultController::class.java.getDeclaredField("active").apply { isAccessible=true }.getBoolean(vault)
            val clock=VaultController::class.java.getDeclaredField("clock").apply { isAccessible=true }.get(vault) as SessionClock
            return mapOf("vaultUnlocked" to vault.unlocked,"vaultBusy" to vault.busy,"vaultReady" to vault.readyForCooking(),
                "vaultActive" to active,"vaultClockExpired" to clock.expired(SystemClock.elapsedRealtime()),
                "readyDish" to (ready.value==true),"lifetimePresent" to lifetime.ready(true),"hasDish" to cookbook.hasDish(),
                "revealed" to cookbook.revealed,"cooking" to cookbook.cooking,"dishNonempty" to cookbook.dish.isNotEmpty())
        }
    }
}
