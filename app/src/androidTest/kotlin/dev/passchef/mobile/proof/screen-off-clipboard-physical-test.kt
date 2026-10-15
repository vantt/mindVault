package dev.passchef.mobile.proof

import android.app.Activity
import android.app.Application
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.os.SystemClock
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/** Owner physically sleeps/wakes the phone. Clipboard is inspected only in legal own foreground. */
@RunWith(AndroidJUnit4::class)
class ScreenOffClipboardPhysicalTest {
    @get:Rule val ui = createEmptyComposeRule()
    private lateinit var state: ColdCacheObservation
    private var phase = "screen-off-arguments"
    private var offStartedAt = -1L
    private var copyStartedAt = -1L
    private fun report(name: String, failed: Boolean = false) {
        phase = name
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefScreenOffPhase", name); putString("passchefScreenOffFixtureId", state.activity.proofId)
            putBoolean("passchefScreenOffFailed", failed); putInt("passchefScreenOffPid", Process.myPid())
            putLong("passchefScreenOffElapsedSinceCopy", if (copyStartedAt < 0) -1 else SystemClock.elapsedRealtime() - copyStartedAt)
            putLong("passchefScreenOffElapsedOff", if (offStartedAt < 0) -1 else SystemClock.elapsedRealtime() - offStartedAt)
            state.flags().forEach { (key, value) -> putBoolean("passchefScreenOff_$key", value) }
        })
    }
    private fun cleared(): Boolean = state.flags().let { flags ->
        listOf("sessionAbsent", "tokenAbsent", "selectedAbsent", "metadataAbsent", "draftAbsent").all { flags[it] == true } &&
            listOf("unlocked", "ready", "busy", "hasDish", "readyDish", "lifetimePresent", "revealed", "dishNonempty", "cooking", "sourceBusy").all { flags[it] == false }
    }
    @Test fun authenticatedCopyScreenOffAndLegalForegroundCleanupPreserveCache() {
        val id = requireNotNull(InstrumentationRegistry.getArguments().getString("passchefScreenOffFixtureId"))
        require(UUID.fromString(id).toString() == id)
        val scenario = ActivityScenario.launch<ColdStartProofActivity>(Intent(
            InstrumentationRegistry.getInstrumentation().targetContext, ColdStartProofActivity::class.java)
            .putExtra("passchefColdFixtureId", id).putExtra("passchefColdStage", "warm")
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        lateinit var activity: ColdStartProofActivity
        scenario.onActivity { activity = it }; state = ColdCacheObservation(activity)
        val stopped = AtomicInteger(); val destroyed = AtomicInteger(); val firstStop = AtomicLong(-1)
        val monitor = object : Application.ActivityLifecycleCallbacks {
            override fun onActivityStopped(value: Activity) {
                if (value === activity) { stopped.incrementAndGet(); firstStop.compareAndSet(-1, SystemClock.elapsedRealtime()) }
            }
            override fun onActivityDestroyed(value: Activity) { if (value === activity) destroyed.incrementAndGet() }
            override fun onActivityCreated(value: Activity, saved: Bundle?) = Unit
            override fun onActivityStarted(value: Activity) = Unit
            override fun onActivityResumed(value: Activity) = Unit
            override fun onActivityPaused(value: Activity) = Unit
            override fun onActivitySaveInstanceState(value: Activity, saved: Bundle) = Unit
        }
        var marker: String? = null
        var monitoring = false
        try {
            report("owner-screen-off-setup-biometric")
            val fixture = ColdCacheUiFixture(ui, state)
            fixture.setup(); fixture.prepareCache()
            val baseline = ColdCacheBaseline.create(activity, fixture.snapshot())
            activity.application.registerActivityLifecycleCallbacks(monitor); monitoring = true
            state.requireReady(); copyStartedAt = SystemClock.elapsedRealtime()
            ui.onNodeWithTag("dish-copy").performScrollTo().performClick()
            assertTrue("Actual authenticated UI Copy confirms success", timerMain { state.book().clipboardStatus == "COPIED" })
            assertTrue("Legal focused clipboard read immediately after own Copy", state.foreground())
            timerMain {
                val clip = activity.getSystemService(ClipboardManager::class.java).primaryClip
                marker = clip?.description?.label?.toString()
                assertTrue("Own exact synthetic Dish and sensitive marker were actually copied", marker?.startsWith("PassChef Dish ") == true &&
                    clip != null && clip.itemCount == 1 && clip.getItemAt(0).text?.toString() == "aB!*abcd" &&
                    clip.description.extras?.getBoolean("android.content.extra.IS_SENSITIVE", false) == true)
            }
            report("owner-power-off-now")
            while (SystemClock.elapsedRealtime() < copyStartedAt + 25_000) {
                if (stopped.get() > 0 && state.flags()["awake"] == false) break
                Thread.sleep(100)
            }
            offStartedAt = SystemClock.elapsedRealtime()
            assertTrue("Actual stop and noninteractive screen precede the original clipboard deadline", firstStop.get() >= copyStartedAt &&
                firstStop.get() < copyStartedAt + 25_000 && offStartedAt < copyStartedAt + 25_000 &&
                stopped.get() > 0 && state.flags()["awake"] == false)
            report("screen-off-observed-hold")
            while (SystemClock.elapsedRealtime() - offStartedAt < 35_000) {
                assertTrue("Continuous actual screen-off keeps the same Activity locked with exact cache/jars", destroyed.get() == 0 &&
                    state.flags()["awake"] == false && state.flags()["resumed"] == false && cleared() && baseline.matches(activity.proofDirectory))
                Thread.sleep(100) // Real elapsed time; no clipboard access, clock or lifecycle injection.
            }
            assertTrue("The completed 35s observation still ends noninteractive and locked before wake permission", destroyed.get() == 0 &&
                state.flags()["awake"] == false && state.flags()["resumed"] == false && cleared() && baseline.matches(activity.proofDirectory))
            report("owner-wake-unlock-return-now")
            state.await(state::foreground); ui.waitForIdle()
            assertTrue("Same original Activity returns locked with session/Dish absent", destroyed.get() == 0 && stopped.get() > 0 && cleared())
            assertTrue("Exact cache/jars survive screen-off/background and legal return", baseline.matches(activity.proofDirectory) &&
                timerMain { state.book().sources.snapshots == listOf(coldExpected(baseline.refreshedAt)) })
            assertTrue("Actual foreground read is legal after original cleanup eligibility", state.foreground() &&
                SystemClock.elapsedRealtime() > copyStartedAt + 30_000)
            assertTrue("Normal lifecycle/focus reconciliation leaves no original Dish clipboard", timerMain {
                !activity.getSystemService(ClipboardManager::class.java).hasPrimaryClip()
            })
            ui.onNodeWithTag("dish-display").assertDoesNotExist(); ui.onNodeWithTag("dish-copy").assertDoesNotExist()
            report("screen-off-clipboard-passed")
        } catch (failure: Throwable) {
            runCatching { report("$phase-failed", failed = true) }
            throw AssertionError("Screen-off clipboard checkpoint failed; safe phase recorded")
        } finally {
            // If still offscreen, legal clipboard cleanup is unavailable; never read or clear blindly.
            runCatching {
                if (marker != null && state.foreground()) timerMain {
                    val manager = activity.getSystemService(ClipboardManager::class.java)
                    val clip = manager.primaryClip
                    if (clip != null && clip.description.label?.toString() == marker && clip.itemCount == 1 &&
                        clip.getItemAt(0).text?.toString() == "aB!*abcd") manager.clearPrimaryClip()
                }
            }
            try { if (monitoring) activity.application.unregisterActivityLifecycleCallbacks(monitor) } finally {
                try { scenario.close() } finally { cleanupColdFixture(activity) }
            }
        }
    }
}
