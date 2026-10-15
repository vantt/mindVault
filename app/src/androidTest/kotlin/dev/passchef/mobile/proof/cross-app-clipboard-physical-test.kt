package dev.passchef.mobile.proof

import android.app.Activity
import android.app.Application
import android.app.KeyguardManager
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.os.Process
import android.os.SystemClock
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/** Actual Chrome ACTION_COPY, then normal expired-owner reconciliation in PassChef. */
@RunWith(AndroidJUnit4::class)
class CrossAppClipboardPhysicalTest {
    @get:Rule val ui = createEmptyComposeRule()
    private lateinit var state: ColdCacheObservation
    private var phase = "replacement-arguments"
    private var copyAt = -1L
    private var foreignUid = -1
    private fun report(name: String, failed: Boolean = false) {
        phase = name
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefReplacementPhase", name); putString("passchefReplacementFixtureId", state.activity.proofId)
            putBoolean("passchefReplacementFailed", failed); putInt("passchefReplacementPid", Process.myPid())
            putInt("passchefReplacementForeignUid", foreignUid)
            putLong("passchefReplacementElapsedSinceCopy", if (copyAt < 0) -1 else SystemClock.elapsedRealtime() - copyAt)
            state.flags().forEach { (key, value) -> putBoolean("passchefReplacement_$key", value) }
        })
    }
    private fun cleared() = state.flags().let { bits ->
        listOf("sessionAbsent", "tokenAbsent", "selectedAbsent", "metadataAbsent", "draftAbsent").all { bits[it] == true } &&
            listOf("unlocked", "ready", "busy", "hasDish", "readyDish", "lifetimePresent", "revealed", "dishNonempty", "cooking", "sourceBusy").all { bits[it] == false }
    }
    private fun replacementPresent(marker: String, oldLabel: String): Boolean = timerMain {
        val activity = state.activity
        assertTrue("Only a legal own focused foreground may read the replacement clipboard", activity.lifecycle.currentState == Lifecycle.State.RESUMED &&
            activity.window.decorView.hasWindowFocus() && activity.window.decorView.isShown &&
            activity.getSystemService(PowerManager::class.java).isInteractive &&
            !activity.getSystemService(KeyguardManager::class.java).isKeyguardLocked &&
            !activity.getSystemService(KeyguardManager::class.java).isDeviceLocked)
        val clip = activity.getSystemService(ClipboardManager::class.java).primaryClip
        clip != null && clip.itemCount == 1 && clip.description.label?.toString() != oldLabel &&
            clip.getItemAt(0).text?.toString() == marker
    }
    @Test fun actualChromeCopySurvivesExpiredDishCleanupOnReturn() {
        val id = requireNotNull(InstrumentationRegistry.getArguments().getString("passchefReplacementFixtureId"))
        require(UUID.fromString(id).toString() == id)
        val marker = "PASSCHEF_CLIPBOARD_$id"
        val scenario = ActivityScenario.launch<ColdStartProofActivity>(Intent(
            InstrumentationRegistry.getInstrumentation().targetContext, ColdStartProofActivity::class.java)
            .putExtra("passchefColdFixtureId", id).putExtra("passchefColdStage", "warm")
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        lateinit var activity: ColdStartProofActivity
        scenario.onActivity { activity = it }; state = ColdCacheObservation(activity)
        val stopped = AtomicInteger(); val destroyed = AtomicInteger(); val firstStop = AtomicLong(-1)
        val allowReturn = AtomicBoolean(false); val earlyReturn = AtomicBoolean(false)
        val monitor = object : Application.ActivityLifecycleCallbacks {
            override fun onActivityStopped(value: Activity) {
                if (value === activity) { stopped.incrementAndGet(); firstStop.compareAndSet(-1, SystemClock.elapsedRealtime()) }
            }
            override fun onActivityStarted(value: Activity) {
                if (value === activity && stopped.get() > 0 && !allowReturn.get()) earlyReturn.set(true)
            }
            override fun onActivityDestroyed(value: Activity) { if (value === activity) destroyed.incrementAndGet() }
            override fun onActivityCreated(value: Activity, saved: Bundle?) = Unit
            override fun onActivityResumed(value: Activity) = Unit
            override fun onActivityPaused(value: Activity) = Unit
            override fun onActivitySaveInstanceState(value: Activity, saved: Bundle) = Unit
        }
        var copy: OwnerCopyObservation? = null
        var monitoring = false
        try {
            val chrome = ChromeClipboardAction(marker); foreignUid = chrome.uid
            report("owner-replacement-setup-biometric")
            val fixture = ColdCacheUiFixture(ui, state)
            fixture.setup(); fixture.prepareCache()
            state.requireReady()
            ui.onNode(hasClickAction() and hasText("Back")).performScrollTo().performClick()
            ui.onNode(hasClickAction() and hasText(coldEntry.label)).performScrollTo().performClick()
            state.await { state.foreground() && timerMain { state.book().hasDish() && !state.book().sources.busy } }
            ui.waitForIdle(); fixture.hideIme()
            assertTrue("Actual Dish is masked before the owner's Copy", ui.onNodeWithTag("dish-display").fetchSemanticsNode()
                .config[SemanticsProperties.Text].joinToString("") { it.text } == "••••••••")
            ui.onNodeWithTag("dish-copy").performScrollTo().assertIsEnabled().assertIsDisplayed()
            val baseline = ColdCacheBaseline.create(activity, fixture.snapshot())
            activity.application.registerActivityLifecycleCallbacks(monitor); monitoring = true
            val ownerCopy = OwnerCopyObservation(state); copy = ownerCopy
            report("owner-copy-then-open-empty-chrome")
            val copyDeadline = SystemClock.elapsedRealtime() + 180_000
            while (ownerCopy.copiedAt < 0 && SystemClock.elapsedRealtime() < copyDeadline) {
                ownerCopy.checkForFailure()
                if (ownerCopy.copiedAt < 0 && state.foreground()) ui.waitForIdle()
                Thread.sleep(50)
            }
            ownerCopy.requireSingleCopy(); copyAt = ownerCopy.copiedAt
            val claim = PendingClipboardClaim(state, copyAt)
            report("own-copy-observed-await-chrome")
            val externalDeadline = SystemClock.elapsedRealtime() + 180_000
            var remoteCopied = false
            fun stoppedAndPreserved() {
                assertTrue("Same stopped Activity stays locked without early return and preserves exact files", stopped.get() > 0 &&
                    firstStop.get() >= copyAt && destroyed.get() == 0 && !earlyReturn.get() && !state.foreground() &&
                    cleared() && baseline.matches(activity.proofDirectory))
                ownerCopy.requireSingleCopy(); claim.requirePending()
            }
            while (!remoteCopied && SystemClock.elapsedRealtime() < externalDeadline) {
                ownerCopy.checkForFailure()
                assertTrue("No unplanned Activity recreation/return precedes external Copy", destroyed.get() == 0 && !earlyReturn.get())
                if (stopped.get() > 0) {
                    stoppedAndPreserved()
                    remoteCopied = chrome.copyFromEmptyEditor(::stoppedAndPreserved)
                }
                if (!remoteCopied) Thread.sleep(100)
            }
            assertTrue("Actual selected different-UID Chrome Copy action completed", remoteCopied)
            report("chrome-copy-action-observed")
            while (SystemClock.elapsedRealtime() < claim.deadline + 1_000) { stoppedAndPreserved(); Thread.sleep(100) }
            stoppedAndPreserved(); allowReturn.set(true)
            report("owner-return-to-passchef-now")
            state.await(state::foreground); ui.waitForIdle()
            assertTrue("Return is still the same locked Activity with exact cache/jars", destroyed.get() == 0 && !earlyReturn.get() &&
                cleared() && baseline.matches(activity.proofDirectory) && timerMain {
                    state.book().sources.snapshots == listOf(coldExpected(baseline.refreshedAt))
                })
            assertTrue("Actual legal return is beyond the original Dish deadline", SystemClock.elapsedRealtime() >= claim.deadline + 1_000)
            assertTrue("Expired Dish cleanup preserves the exact Chrome replacement", replacementPresent(marker, claim.label))
            claim.requireForgotten(); ownerCopy.requireSingleCopy()
            Thread.sleep(1_000)
            assertTrue("Chrome replacement remains intact after normal foreground reconciliation", state.foreground() && replacementPresent(marker, claim.label))
            ui.onNodeWithTag("dish-display").assertDoesNotExist(); ui.onNodeWithTag("dish-copy").assertDoesNotExist()
            report("cross-app-replacement-passed")
        } catch (failure: Throwable) {
            runCatching { report("$phase-failed", failed = true) }
            throw AssertionError("Cross-app clipboard checkpoint failed; safe phase recorded")
        } finally {
            runCatching { copy?.cleanupIfOwned() } // Never clear the replacement from Chrome.
            try { copy?.close() } finally {
                try { if (monitoring) activity.application.unregisterActivityLifecycleCallbacks(monitor) } finally {
                    try { scenario.close() } finally { cleanupColdFixture(activity) }
                }
            }
        }
    }
}
