package dev.passchef.mobile.proof

import android.app.KeyguardManager
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.os.Process
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.cookbook.OwnedClipboard
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

/** Classifies only a prior public UUID marker while the fresh owned Cookbook stays locked. */
@RunWith(AndroidJUnit4::class)
class ClipboardLabelNullProbePhysicalTest {
    private lateinit var fixtureId: String
    private lateinit var observedFixtureId: String
    private var phase = "label-probe-arguments"
    private fun report(name: String, bits: Map<String, Boolean> = emptyMap()) {
        phase = name
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefLabelProbePhase", name)
            putString("passchefLabelProbeFixtureId", fixtureId)
            putString("passchefObservedClipboardFixtureId", observedFixtureId)
            putInt("passchefLabelProbePid", Process.myPid())
            bits.forEach { (key, value) -> putBoolean("passchefLabelProbe_$key", value) }
        })
    }
    private fun legalBits(state: ColdCacheObservation): Map<String, Boolean> = state.activity.let { activity ->
        val decor = activity.window.decorView
        val keyguard = activity.getSystemService(KeyguardManager::class.java)
        mapOf("resumed" to (activity.lifecycle.currentState == Lifecycle.State.RESUMED),
            "focused" to decor.hasWindowFocus(), "shown" to decor.isShown, "attached" to decor.isAttachedToWindow,
            "awake" to activity.getSystemService(PowerManager::class.java).isInteractive,
            "keyguardAbsent" to !keyguard.isKeyguardLocked, "deviceUnlocked" to !keyguard.isDeviceLocked,
            "secure" to (activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0),
            "cookbookLocked" to (!state.vault().unlocked && !state.vault().readyForCooking()))
    }
    @Test fun actualPriorChromeMarkerLabelClassification() {
        val arguments = InstrumentationRegistry.getArguments()
        fixtureId = requireNotNull(arguments.getString("passchefLabelProbeFixtureId"))
        observedFixtureId = requireNotNull(arguments.getString("passchefObservedClipboardFixtureId"))
        require(UUID.fromString(fixtureId).toString() == fixtureId &&
            UUID.fromString(observedFixtureId).toString() == observedFixtureId && fixtureId != observedFixtureId)
        val expectedMarker = "PASSCHEF_CLIPBOARD_$observedFixtureId"
        val scenario = ActivityScenario.launch<ColdStartProofActivity>(Intent(
            InstrumentationRegistry.getInstrumentation().targetContext, ColdStartProofActivity::class.java)
            .putExtra("passchefColdFixtureId", fixtureId).putExtra("passchefColdStage", "warm")
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        lateinit var activity: ColdStartProofActivity
        scenario.onActivity { activity = it }
        val state = ColdCacheObservation(activity)
        try {
            report("label-probe-await-foreground")
            state.await(state::foreground)
            state.initialLocked()
            report("label-probe-clipboard-gate")
            val sample = timerMain {
                val bits = legalBits(state)
                assertTrue("Only a legal secure own locked foreground may inspect the known public marker", bits.values.all { it })
                val adapter = state.book().clipboard
                val manager = OwnedClipboard::class.java.getDeclaredField("clipboard").apply { isAccessible = true }
                    .get(adapter) as ClipboardManager
                val clip = manager.primaryClip
                assertTrue("The prior public clipboard marker exists as one item", clip != null && clip.itemCount == 1)
                requireNotNull(clip)
                assertTrue("Only the exact prior public UUID marker may be classified", clip.getItemAt(0).text?.toString() == expectedMarker)
                val expression = runCatching { clip.description.label.toString() }
                bits + mapOf("labelNull" to (clip.description.label == null),
                    "directExpressionSucceeded" to expression.isSuccess,
                    "exceptionIsNpe" to (expression.exceptionOrNull() is NullPointerException),
                    "nullsafeLabelNull" to (clip.description.label?.toString() == null))
            }
            report("clipboard-label-sampled", sample)
        } catch (failure: Throwable) {
            runCatching { report("$phase-failed") }
            throw AssertionError("Clipboard label probe failed; safe phase recorded")
        } finally {
            try { scenario.close() } finally { cleanupColdFixture(activity) }
        }
    }
}
