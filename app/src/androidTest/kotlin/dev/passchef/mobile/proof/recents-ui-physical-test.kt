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
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/** Owner performs hardware capture/Recents. This fixture asserts actual stop/lock/reopen only. */
@RunWith(AndroidJUnit4::class)
class RecentsUiPhysicalTest {
    @get:Rule val ui = createAndroidComposeRule<CookbookProofActivity>()
    private var phase = "recents-ui-setup"
    private val paused = AtomicInteger()
    private val stopped = AtomicInteger()
    private val stoppedRead = AtomicBoolean()
    private val stoppedLocked = AtomicBoolean()
    private var pausedBaseline = 0
    private var stoppedBaseline = 0
    private fun sessionAbsent(activity: CookbookProofActivity): Boolean {
        val vault = MainActivity::class.java.getDeclaredField("controller").apply { isAccessible = true }.get(activity) as VaultController
        return VaultController::class.java.getDeclaredField("session").apply { isAccessible = true }.get(vault) == null
    }
    private fun foreground(activity: CookbookProofActivity): Boolean {
        val flags = CookbookUiDiagnostics.environmentFlags(activity)
        return activity.lifecycle.currentState == Lifecycle.State.RESUMED && flags["powerInteractive"] == true &&
            flags["keyguardLocked"] == false && flags["deviceLocked"] == false && flags["proofWindowFocused"] == true &&
            flags["proofWindowAttached"] == true && flags["proofWindowShown"] == true
    }
    private fun report(name: String, activity: CookbookProofActivity, failed: Boolean = false) {
        phase = name
        val flags = runCatching { timerMain {
            CookbookUiDiagnostics.environmentFlags(activity) + CookbookUiDiagnostics.stateFlags(activity) + mapOf(
                "resumed" to (activity.lifecycle.currentState == Lifecycle.State.RESUMED),
                "sessionAbsent" to sessionAbsent(activity),
                "secureFlag" to (activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0))
        } }.getOrDefault(emptyMap())
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefRecentsPhase", name); putBoolean("passchefRecentsFailed", failed)
            putBoolean("passchefRecents_passed", name == "recents-ui-passed" && !failed)
            putInt("passchefRecentsPaused", paused.get()); putInt("passchefRecentsStopped", stopped.get())
            putInt("passchefRecentsPausedSinceCheckpoint", paused.get() - pausedBaseline)
            putInt("passchefRecentsStoppedSinceCheckpoint", stopped.get() - stoppedBaseline)
            putBoolean("passchefRecentsStoppedStateRead", stoppedRead.get())
            putBoolean("passchefRecentsStoppedLocked", stoppedLocked.get())
            flags.forEach { (key, value) -> putBoolean("passchefRecents_$key", value) }
        })
    }
    private fun await(condition: () -> Boolean) {
        val deadline = SystemClock.elapsedRealtime() + 180_000
        while (!condition() && SystemClock.elapsedRealtime() < deadline) Thread.sleep(100)
        assertTrue("Bounded genuine Recents continuation reached the expected state", condition())
    }
    @Test fun genuineOwnerRecentsStopLockAndBiometricReopen() {
        val activity = ui.activity
        val proof = CookbookTimerFixture(ui)
        try {
            proof.run("recents-ui") {
                proof.environment()
                val file = File(activity.proofDirectory, "cookbook-v1.json")
                val original = file.readBytes()
                ui.onNodeWithTag("dish-display").assertTextEquals("••••••••")
                ui.onNodeWithTag("dish-copy").assertIsEnabled()
                assertTrue("Actual ready secure own Activity starts the owner check", timerMain {
                    foreground(activity) && CookbookUiDiagnostics.stateFlags(activity)["vaultReady"] == true &&
                        activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0
                })
                val monitor = object : Application.ActivityLifecycleCallbacks {
                    override fun onActivityPaused(value: Activity) { if (value === activity) paused.incrementAndGet() }
                    override fun onActivityStopped(value: Activity) {
                        if (value !== activity) return
                        stopped.incrementAndGet()
                        stoppedRead.set(false); stoppedLocked.set(false)
                        runCatching {
                            val flags = CookbookUiDiagnostics.stateFlags(activity)
                            stoppedLocked.set(flags["vaultUnlocked"] == false && flags["vaultReady"] == false &&
                                flags["hasDish"] == false && flags["dishNonempty"] == false && sessionAbsent(activity))
                            stoppedRead.set(true)
                        }
                    }
                    override fun onActivityCreated(value: Activity, state: Bundle?) = Unit
                    override fun onActivityStarted(value: Activity) = Unit
                    override fun onActivityResumed(value: Activity) = Unit
                    override fun onActivityDestroyed(value: Activity) = Unit
                    override fun onActivitySaveInstanceState(value: Activity, state: Bundle) = Unit
                }
                activity.application.registerActivityLifecycleCallbacks(monitor)
                try {
                    hideTimerIme(activity, proof::environment)
                    ui.onNodeWithTag("dish-reveal").performScrollTo().performClick()
                    ui.onNodeWithTag("dish-display").assertTextEquals("aB!*abcd")
                    assertTrue("Exact synthetic Dish is genuinely revealed before the owner checkpoint", timerMain {
                        val flags = CookbookUiDiagnostics.stateFlags(activity)
                        foreground(activity) && flags["vaultReady"] == true && flags["revealed"] == true && flags["dishNonempty"] == true
                    })
                    // Original ten-second remask remains. Root records screenshot/card observations separately.
                    pausedBaseline = paused.get(); stoppedBaseline = stopped.get()
                    stoppedRead.set(false); stoppedLocked.set(false)
                    report("owner-screenshot-recents-check", activity)
                    await { stopped.get() > stoppedBaseline && timerMain { foreground(activity) } }
                    ui.waitForIdle()
                    val flags = timerMain { CookbookUiDiagnostics.stateFlags(activity) }
                    assertTrue("Actual same Activity stop/resume clears authenticated session and every Dish state", timerMain {
                        ui.activity === activity && foreground(activity) && sessionAbsent(activity)
                    } && paused.get() > pausedBaseline && stopped.get() > stoppedBaseline && stoppedRead.get() && stoppedLocked.get() &&
                        flags["vaultUnlocked"] == false && flags["vaultReady"] == false && flags["hasDish"] == false &&
                        flags["readyDish"] == false && flags["lifetimePresent"] == false && flags["revealed"] == false &&
                        flags["dishNonempty"] == false && flags["cooking"] == false)
                    ui.onNodeWithTag("dish-display").assertDoesNotExist()
                    ui.onNodeWithTag("dish-copy").assertDoesNotExist()
                    assertTrue("Owner Recents round trip preserves encrypted owned jars", original.contentEquals(file.readBytes()))
                    report("recents-owner-reopen-biometric", activity)
                    ui.onNode(hasClickAction() and hasText("Open with biometric")).performScrollTo().performClick()
                    await { timerMain { foreground(activity) && CookbookUiDiagnostics.stateFlags(activity)["vaultReady"] == true } }
                    ui.waitForIdle()
                    proof.cook()
                    ui.onNodeWithTag("dish-display").assertTextEquals("••••••••")
                    ui.onNodeWithTag("dish-copy").assertIsEnabled()
                    assertTrue("Genuine reopened cooking preserves encrypted owned jars", original.contentEquals(file.readBytes()))
                    report("recents-ui-passed", activity)
                } finally { activity.application.unregisterActivityLifecycleCallbacks(monitor) }
            }
        } catch (error: Throwable) {
            runCatching { report("$phase-failed", activity, failed = true) }
            throw AssertionError("Recents UI acceptance failed at $phase; safe metadata recorded")
        }
    }
}
