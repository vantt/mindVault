package dev.passchef.mobile.proof

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.SystemClock
import android.view.InputDevice
import android.view.MotionEvent
import android.view.ViewTreeObserver
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.MainActivity
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.KeystoreCrypto
import dev.passchef.mobile.cookbook.CookbookController
import dev.passchef.mobile.vault.*
import org.junit.Assert.assertTrue
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

data class TimerClockObservation(val opened: Long?, val lastUsed: Long, val generation: Long)
/** Actual monotonic deadlines/window input; all reflection is read-only, storage is owned UUID-only. */
class CookbookTimerFixture(private val ui: AndroidComposeTestRule<ActivityScenarioRule<CookbookProofActivity>, CookbookProofActivity>) {
    private val activity = ui.activity
    private val directory = activity.proofDirectory
    private val file = File(directory, "cookbook-v1.json")
    private val interrupted = AtomicBoolean(false)
    @Volatile private var armed = false
    private var baseline: TimerClockObservation? = null
    private var jars = byteArrayOf()
    private var phase = "timer-start"
    private val focusMonitor = ViewTreeObserver.OnWindowFocusChangeListener { focused -> if (armed && !focused) interrupted.set(true) }
    private val monitor = object : Application.ActivityLifecycleCallbacks {
        private fun event(value: Activity) { if (armed && value === activity) interrupted.set(true) }
        override fun onActivityPaused(value: Activity) = event(value)
        override fun onActivityStopped(value: Activity) = event(value)
        override fun onActivityDestroyed(value: Activity) = event(value)
        override fun onActivityCreated(value: Activity, state: Bundle?) = Unit
        override fun onActivityStarted(value: Activity) = Unit
        override fun onActivityResumed(value: Activity) = Unit
        override fun onActivitySaveInstanceState(value: Activity, state: Bundle) = Unit
    }
    init {
        activity.application.registerActivityLifecycleCallbacks(monitor)
        activity.window.decorView.viewTreeObserver.addOnWindowFocusChangeListener(focusMonitor)
    }
    private fun <T> main(action: () -> T): T = timerMain(action)
    private fun owned(name: String) = MainActivity::class.java.getDeclaredField(name).apply { isAccessible = true }.get(activity)
    private fun vault() = owned("controller") as VaultController
    private fun book() = owned("cookbook") as CookbookController
    private fun clockNow(): TimerClockObservation {
        val clock = VaultController::class.java.getDeclaredField("clock").apply { isAccessible = true }.get(vault()) as SessionClock
        fun field(name: String) = SessionClock::class.java.getDeclaredField(name).apply { isAccessible = true }.get(clock)
        return TimerClockObservation(field("opened") as Long?, field("lastUsed") as Long, field("generation") as Long)
    }
    fun clock() = main(::clockNow)
    private fun foreground(): Boolean {
        val flags = CookbookUiDiagnostics.environmentFlags(activity)
        return activity.lifecycle.currentState == Lifecycle.State.RESUMED && flags["powerInteractive"] == true &&
            flags["keyguardLocked"] == false && flags["deviceLocked"] == false && flags["proofWindowFocused"] == true &&
            flags["proofWindowAttached"] == true && flags["proofWindowShown"] == true
    }
    fun environment() = assertTrue("Real unchanged interactive unlocked proof foreground required", main { foreground() && !interrupted.get() })
    private fun await(condition: () -> Boolean) {
        val until = SystemClock.elapsedRealtime() + 180_000
        while (!condition() && SystemClock.elapsedRealtime() < until) Thread.sleep(100)
        assertTrue("Bounded actual timer setup became ready", condition())
    }
    private fun click(text: String) = ui.onNode(hasClickAction() and hasText(text)).performScrollTo().performClick()
    fun setup(name: String) {
        assertTrue("Unique timer proof starts without jars", !file.exists())
        await { main(::foreground) }
        if (ui.onAllNodesWithText("EN").fetchSemanticsNodes().isNotEmpty()) click("EN")
        ui.onNode(hasSetTextAction() and hasText("Master password")).performScrollTo().performTextReplacement("SYNTHETIC_TIMER_Kitchen_2026")
        ui.onNode(hasSetTextAction() and hasText("Spice jar 1 (setup only)")).performScrollTo().performTextReplacement("aB!*")
        report("$name-owner-biometric")
        click("Set up / recover with password") // Genuine owner operation; never an injected session.
        await { main { foreground() && vault().readyForCooking() && !vault().busy } }
        ui.waitForIdle()
        assertTrue("Actual timer setup completed the owner biometric round trip", main { vault().status.startsWith("Round trip ready;") } &&
            VaultRepository(AtomicRecordStore(file)).record()?.alias?.isNotEmpty() == true)
        jars = file.readBytes()
        cook()
    }
    fun cook() {
        environment(); click("Menu"); click("Cook")
        ui.onNodeWithTag("manual-recipe").performScrollTo().performTextReplacement("abcd#1")
        ui.onNodeWithTag("manual-cook").performScrollTo().performClick()
        await { main { foreground() && vault().readyForCooking() && book().hasDish() } }
        report("$phase-ime-hide"); hideTimerIme(activity, ::environment); ui.waitForIdle()
        ui.onNodeWithTag("dish-display").performScrollTo().assertTextEquals("••••••••")
        ui.onNodeWithTag("dish-copy").assertIsEnabled()
    }
    fun arm(value: TimerClockObservation) { baseline = value; armed = true; environment() }
    fun revealTouch(value: TimerClockObservation, expectedLastUsed: Long): Long {
        environment()
        val touchPhase = phase
        report("$touchPhase-pointer-scroll")
        ui.onNodeWithTag("dish-reveal").performScrollTo(); ui.waitForIdle()
        val node = ui.onNodeWithTag("dish-reveal").fetchSemanticsNode()
        report("$touchPhase-pointer-bounds")
        val point = timerWindowPoint(activity, node)
        report("$touchPhase-pointer-window", extraFlags = point.checks)
        assertTrue("Actual timer pointer targets finite visible own-window coordinates with IME hidden", point.checks.values.all { it })
        val before = clock()
        assertTrue("No unowned touch/renewal precedes real pointer", before == value.copy(lastUsed = expectedLastUsed))
        val started = SystemClock.elapsedRealtime()
        val downAt = SystemClock.uptimeMillis()
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        for (action in listOf(MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP)) {
            report("$touchPhase-pointer-event-$action")
            val event = MotionEvent.obtain(downAt, SystemClock.uptimeMillis(), action, point.x, point.y, 0)
            event.source = InputDevice.SOURCE_TOUCHSCREEN
            report("$touchPhase-pointer-send-$action")
            try { instrumentation.sendPointerSync(event) } finally { event.recycle() }
        }
        val finished = SystemClock.elapsedRealtime()
        val after = clock()
        assertTrue("Actual Activity pointer updates only idle time inside real monotonic bracket",
            after.opened == value.opened && after.generation == value.generation && after.lastUsed > before.lastUsed &&
                after.lastUsed in started..finished)
        ui.onNodeWithTag("dish-display").assertTextEquals("aB!*abcd")
        assertTrue("Real window target reveals exact authenticated synthetic Dish", main { book().revealed && book().dish == "aB!*abcd" })
        return after.lastUsed
    }
    fun ready(value: TimerClockObservation, lastUsed: Long, before: Long = Long.MAX_VALUE) {
        environment()
        val observation = main {
            val state = clockNow() to (vault().readyForCooking() && vault().unlocked && book().hasDish())
            if (SystemClock.elapsedRealtime() >= before) null else state
        } ?: return // The passive polling dispatch may cross the real expiry; never call that an early lock.
        assertTrue("Original authenticated window/idle baseline remains unchanged", observation.first == value.copy(lastUsed = lastUsed))
        assertTrue("No early lock or lost ready Dish", observation.second)
    }
    fun waitUntil(target: Long, expiry: Long, value: TimerClockObservation, lastUsed: Long) {
        while (SystemClock.elapsedRealtime() < target) {
            val now = SystemClock.elapsedRealtime()
            if (now < expiry) ready(value, lastUsed, expiry) else environment()
            val remaining = target - SystemClock.elapsedRealtime()
            if (remaining > 0) Thread.sleep(minOf(200, remaining)) // Real time; no test-clock advancement.
        }
    }
    fun beforeExpiry(value: TimerClockObservation, lastUsed: Long, expiry: Long) {
        ready(value, lastUsed)
        ui.onNodeWithTag("dish-display").assertTextEquals("••••••••")
        ui.onNodeWithTag("dish-copy").assertIsEnabled()
        val now = SystemClock.elapsedRealtime()
        assertTrue("Real pre-expiry UI observation lies inside final five-second window", now >= expiry - 5_000 && now < expiry)
    }
    fun expired(value: TimerClockObservation, expiry: Long) {
        environment(); ui.waitForIdle()
        val observed = clock()
        val flags = main { CookbookUiDiagnostics.stateFlags(activity) }
        val sessionAbsent = main { VaultController::class.java.getDeclaredField("session").apply { isAccessible = true }.get(vault()) == null }
        assertTrue("Actual expiry locks original session and clears all Dish state", observed.opened == null &&
            observed.generation == value.generation + 1 && sessionAbsent && flags["vaultUnlocked"] == false &&
            flags["vaultReady"] == false && flags["hasDish"] == false && flags["readyDish"] == false &&
            flags["lifetimePresent"] == false && flags["revealed"] == false && flags["dishNonempty"] == false)
        ui.onNodeWithTag("dish-display").assertDoesNotExist(); ui.onNodeWithTag("dish-copy").assertDoesNotExist()
        ui.onNode(hasClickAction() and hasText("Open with biometric")).assertExists()
        assertTrue("Real timer expiry preserves encrypted owned jars", jars.contentEquals(file.readBytes()))
        assertTrue("Actual expiry UI observed within bounded Handler margin", SystemClock.elapsedRealtime().let { it in (expiry + 1_500)..(expiry + 5_000) })
    }
    fun report(name: String, passed: Boolean = false, failure: Throwable? = null, extraFlags: Map<String, Boolean> = emptyMap()) {
        phase = name
        val now = SystemClock.elapsedRealtime()
        val observed = clock()
        val flags = main { CookbookUiDiagnostics.environmentFlags(activity) + CookbookUiDiagnostics.stateFlags(activity) } + extraFlags
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefTimerPhase", name); putBoolean("passchefTimerPassed", passed)
            failure?.let(::putSafeTimerFailure)
            putLong("passchefTimerElapsedSinceOpened", baseline?.opened?.let { now - it } ?: -1)
            putLong("passchefTimerElapsedSinceLastUsed", now - observed.lastUsed)
            putBoolean("passchefTimerLifecycleInterrupted", interrupted.get())
            flags.forEach { (key, state) -> putBoolean("passchefTimer_$key", state) }
        })
    }
    fun run(name: String, work: () -> Unit) {
        try { setup(name); work() }
        catch (failure: Throwable) {
            runCatching { report("$phase-failed", failure = failure) }
            throw AssertionError("Cookbook timer acceptance failed at $phase; safe flags recorded")
        } finally {
            armed = false; activity.application.unregisterActivityLifecycleCallbacks(monitor)
            activity.window.decorView.viewTreeObserver.takeIf { it.isAlive }?.removeOnWindowFocusChangeListener(focusMonitor)
            runCatching { ui.activityRule.scenario.close() }
            runCatching { VaultRepository(AtomicRecordStore(file)).record()?.let { record ->
                (listOf(record.alias) + record.retiredAliases).filter { it.isNotEmpty() }.forEach(KeystoreCrypto()::deleteKey)
            } }
            directory.deleteRecursively()
        }
    }
}
