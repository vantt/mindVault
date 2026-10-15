package dev.passchef.mobile.proof

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.SystemClock
import android.view.WindowManager
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.MainActivity
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.cookbook.CookbookController
import org.json.JSONObject
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/** JUnit certifies the owned checkpoint only; actual hardware-shortcut verdict is owner evidence. */
@RunWith(AndroidJUnit4::class)
class ManualVisualPhysicalTest {
    @get:Rule val ui = createAndroidComposeRule<CookbookProofActivity>()
    private val stopped = AtomicInteger()
    private val stopRead = AtomicBoolean()
    private val stopCleared = AtomicBoolean()
    private val anyStopFailure = AtomicBoolean()
    private val firstStop = AtomicLong(-1)
    private var readyAt = 0L
    private var firstReveal = -1L
    private var lastReveal = -1L
    private var firstFocusLoss = -1L
    private var phase = "manual-shortcut-setup"
    private fun sessionAbsent(activity: CookbookProofActivity): Boolean {
        val vault = MainActivity::class.java.getDeclaredField("controller").apply { isAccessible = true }.get(activity) as VaultController
        return VaultController::class.java.getDeclaredField("session").apply { isAccessible = true }.get(vault) == null
    }
    private fun stateOnMain(activity: CookbookProofActivity): Map<String, Boolean> {
        val flags = CookbookUiDiagnostics.environmentFlags(activity) + CookbookUiDiagnostics.stateFlags(activity)
        val book = MainActivity::class.java.getDeclaredField("cookbook").apply { isAccessible = true }.get(activity) as CookbookController
        return flags + mapOf("resumed" to (activity.lifecycle.currentState == Lifecycle.State.RESUMED),
            "sessionAbsent" to sessionAbsent(activity),
            "secureFlag" to (activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0),
            "exactSyntheticRevealed" to (book.revealed && book.dish == "aB!*abcd"))
    }
    private fun snapshot(activity: CookbookProofActivity): Map<String, Boolean> {
        ui.waitForIdle() // New diagnostic variant: normal Compose frame synchronization, never a production clock change.
        return manualVisualSnapshot(ui, activity) { stateOnMain(activity) }
    }
    private fun cleared(flags: Map<String, Boolean>) = flags["sessionAbsent"] == true &&
        listOf("vaultUnlocked", "vaultReady", "hasDish", "readyDish", "lifetimePresent", "revealed", "dishNonempty", "cooking").all { flags[it] == false }
    private fun report(name: String, id: String, flags: Map<String, Boolean>) {
        phase = name
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefManualShortcutPhase", name); putString("passchefManualShortcutFixtureId", id)
            putLong("passchefManualShortcutElapsedSinceReady", if (readyAt == 0L) -1 else SystemClock.elapsedRealtime() - readyAt)
            putLong("passchefManualShortcutFirstReveal", firstReveal); putLong("passchefManualShortcutLastReveal", lastReveal)
            putLong("passchefManualShortcutFirstFocusLoss", firstFocusLoss)
            putLong("passchefManualShortcutFirstStop", firstStop.get().let { if (it < 0) -1 else it - readyAt })
            putInt("passchefManualShortcutStops", stopped.get())
            putBoolean("passchefManualShortcutStopRead", stopRead.get()); putBoolean("passchefManualShortcutStopCleared", stopCleared.get())
            putBoolean("passchefManualShortcutAnyStopFailure", anyStopFailure.get())
            flags.forEach { (key, value) -> putBoolean("passchefManualShortcut_$key", value) }
        })
    }
    @Test fun genuineOwnerHardwareShortcutCheckpoint() {
        val activity = ui.activity
        val directory = activity.proofDirectory
        val id = directory.name.removePrefix("cookbook-ui-proof-")
        require(UUID.fromString(id).toString() == id)
        val proof = CookbookTimerFixture(ui)
        try {
            proof.run("manual-shortcut") {
                val jars = File(directory, "cookbook-v1.json")
                val original = jars.readBytes()
                val marker = File(directory, "manual-shortcut-observed.json")
                assertTrue("Fresh UUID checkpoint has no completion marker", !marker.exists())
                hideTimerIme(activity, proof::environment)
                ui.onNodeWithTag("dish-reveal").performScrollTo().assertIsDisplayed()
                ui.onNodeWithTag("dish-display").assertIsDisplayed().assertTextEquals("••••••••")
                ui.onNodeWithTag("dish-copy").assertIsEnabled()
                proof.environment()
                val initial = snapshot(activity)
                assertTrue("Actual secure foreground starts ready and masked", initial["secureFlag"] == true &&
                    initial["resumed"] == true && initial["vaultReady"] == true && initial["hasDish"] == true &&
                    initial["revealed"] == false && initial["dishNonempty"] == false)
                val monitor = object : Application.ActivityLifecycleCallbacks {
                    override fun onActivityStopped(value: Activity) {
                        if (value !== activity) return
                        stopped.incrementAndGet(); firstStop.compareAndSet(-1, SystemClock.elapsedRealtime())
                        stopRead.set(false); stopCleared.set(false)
                        runCatching {
                            val clean = cleared(stateOnMain(activity))
                            stopCleared.set(clean); stopRead.set(true)
                            if (!clean) anyStopFailure.set(true)
                        }.onFailure { anyStopFailure.set(true) }
                    }
                    override fun onActivityCreated(value: Activity, state: Bundle?) = Unit
                    override fun onActivityStarted(value: Activity) = Unit
                    override fun onActivityResumed(value: Activity) = Unit
                    override fun onActivityPaused(value: Activity) = Unit
                    override fun onActivityDestroyed(value: Activity) = Unit
                    override fun onActivitySaveInstanceState(value: Activity, state: Bundle) = Unit
                }
                activity.application.registerActivityLifecycleCallbacks(monitor)
                try {
                    readyAt = SystemClock.elapsedRealtime()
                    report("owner-shortcut-ready", id, initial)
                    var previous = initial
                    while (!marker.exists() && SystemClock.elapsedRealtime() - readyAt < 180_000) {
                        val flags = snapshot(activity)
                        val elapsed = SystemClock.elapsedRealtime() - readyAt
                        assertTrue("Secure own checkpoint and encrypted jars stay intact", flags["secureFlag"] == true && original.contentEquals(jars.readBytes()))
                        if (flags["exactSyntheticRevealed"] == true) {
                            if (firstReveal < 0) firstReveal = elapsed
                            lastReveal = elapsed
                        }
                        if (flags["proofWindowFocused"] != true && firstFocusLoss < 0) firstFocusLoss = elapsed
                        if (flags != previous) { report("owner-shortcut-observation", id, flags); previous = flags }
                        Thread.sleep(200) // Passive observation; production reveal/session deadlines are untouched.
                    }
                    assertTrue("Actual owner-response completion marker arrived within bounded wait",
                        SystemClock.elapsedRealtime() - readyAt < 180_000 && marker.exists() && marker.length() in 1..256)
                    val observed = JSONObject(marker.readText())
                    assertTrue("Completion signal belongs only to this UUID, never a screenshot verdict", observed.keys().asSequence().toSet() == setOf("completed", "fixtureId") &&
                        observed.get("completed") == true && observed.getString("fixtureId") == id)
                    val final = snapshot(activity)
                    assertTrue("Own secure checkpoint preserves encrypted jars", final["secureFlag"] == true && original.contentEquals(jars.readBytes()))
                    if (stopped.get() > 0) {
                        assertTrue("Every observed stop clears session and every Dish state",
                            !anyStopFailure.get() && stopRead.get() && stopCleared.get() && cleared(final))
                        if (final["resumed"] == true && final["proofWindowFocused"] == true) {
                            ui.onNodeWithTag("dish-display").assertDoesNotExist(); ui.onNodeWithTag("dish-copy").assertDoesNotExist()
                        }
                    }
                    report("manual-shortcut-checkpoint-observed", id, final + mapOf("checkpointObserved" to true,
                        "revealObserved" to (firstReveal >= 0)))
                } finally { activity.application.unregisterActivityLifecycleCallbacks(monitor) }
            }
        } catch (failure: Throwable) {
            runCatching { report("$phase-failed", id, mapOf("failed" to true)) }
            throw AssertionError("Manual shortcut checkpoint failed; owner verdict remains separate")
        }
    }
}
