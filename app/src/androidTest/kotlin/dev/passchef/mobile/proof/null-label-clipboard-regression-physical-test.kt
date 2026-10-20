package dev.passchef.mobile.proof

import android.app.KeyguardManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.os.Process
import android.os.SystemClock
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.cookbook.ClipboardOwnership
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

/** Same-UID adapter regression: real Handler expiry preserves an actual public null-label replacement. */
@RunWith(AndroidJUnit4::class)
class NullLabelClipboardRegressionPhysicalTest {
    private fun read(owner: Any, name: String): Any? = owner.javaClass.getDeclaredField(name)
        .apply { isAccessible = true }.get(owner)
    private fun legal(activity: ColdStartProofActivity): Boolean {
        val decor = activity.window.decorView
        val keyguard = activity.getSystemService(KeyguardManager::class.java)
        return activity.lifecycle.currentState == Lifecycle.State.RESUMED && decor.hasWindowFocus() && decor.isShown &&
            decor.isAttachedToWindow && activity.getSystemService(PowerManager::class.java).isInteractive &&
            !keyguard.isKeyguardLocked && !keyguard.isDeviceLocked &&
            (activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0)
    }
    private fun exact(manager: ClipboardManager, text: String, label: String?): Boolean {
        val clip = manager.primaryClip
        return clip != null && clip.itemCount == 1 && clip.description.label?.toString() == label &&
            clip.getItemAt(0).text?.toString() == text
    }
    private fun report(phase: String, id: String) {
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefNullLabelPhase", phase); putString("passchefNullLabelFixtureId", id)
            putInt("passchefNullLabelPid", Process.myPid())
        })
    }
    @Test fun realExpiryForgetsOwnershipAndPreservesNullLabelReplacement() {
        val id = requireNotNull(InstrumentationRegistry.getArguments().getString("passchefNullLabelFixtureId"))
        require(UUID.fromString(id).toString() == id)
        val dish = "PASSCHEF_DISH_$id"
        val replacement = "PASSCHEF_NULL_LABEL_$id"
        val scenario = ActivityScenario.launch<ColdStartProofActivity>(Intent(
            InstrumentationRegistry.getInstrumentation().targetContext, ColdStartProofActivity::class.java)
            .putExtra("passchefColdFixtureId", id).putExtra("passchefColdStage", "warm")
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        lateinit var activity: ColdStartProofActivity
        scenario.onActivity { activity = it }
        val state = ColdCacheObservation(activity)
        var ownLabel: String? = null
        var manager: ClipboardManager? = null
        var phase = "null-label-await-foreground"
        try {
            report(phase, id)
            state.await(state::foreground); state.initialLocked()
            val original = timerMain {
                assertTrue("Only a secure legal own locked foreground may prepare synthetic clipboard data",
                    legal(activity) && !state.vault().unlocked)
                val adapter = state.book().clipboard
                val clipboard = read(adapter, "clipboard") as ClipboardManager
                manager = clipboard
                val policy = read(adapter, "policy") as ClipboardOwnership
                val priorGeneration = policy.token()
                val before = SystemClock.elapsedRealtime()
                assertTrue("Actual production adapter copies this run's synthetic Dish", adapter.copyDish(dish))
                val after = SystemClock.elapsedRealtime()
                ownLabel = read(adapter, "label") as String?
                val label = requireNotNull(ownLabel)
                assertTrue("Actual unique Dish ownership label exists", label.startsWith("PassChef Dish ") &&
                    UUID.fromString(label.removePrefix("PassChef Dish ")).toString() == label.removePrefix("PassChef Dish "))
                assertTrue("Actual own clipboard item matches the synthetic Dish", exact(clipboard, dish, label))
                val generation = policy.token()
                val deadline = read(policy, "deadline") as Long
                assertTrue("One real Copy establishes its original thirty-second deadline", generation == priorGeneration + 1 &&
                    deadline >= before + 30_000 && deadline <= after + 30_000 && read(policy, "value") != null)
                assertTrue("Replacement is prepared only while legal own foreground remains", legal(activity))
                clipboard.setPrimaryClip(ClipData.newPlainText(null, replacement))
                assertTrue("Actual replacement is exactly this public marker with a null label", exact(clipboard, replacement, null))
                assertTrue("Replacement does not manually alter the adapter's original ownership", policy.token() == generation &&
                    read(policy, "deadline") == deadline && read(policy, "value") != null && read(adapter, "label") == label)
                generation to deadline
            }
            phase = "null-label-await-production-cleanup"; report(phase, id)
            val fixedEnd = SystemClock.elapsedRealtime() + 30_500
            while (SystemClock.elapsedRealtime() < fixedEnd) {
                assertTrue("Fixed real-time observation stays in legal own locked foreground", timerMain {
                    legal(activity) && !state.vault().unlocked
                })
                Thread.sleep(100)
            }
            phase = "null-label-check-production-cleanup"; report(phase, id)
            timerMain {
                assertTrue("Final clipboard inspection requires legal own locked foreground", legal(activity) && !state.vault().unlocked)
                val adapter = state.book().clipboard
                val policy = read(adapter, "policy") as ClipboardOwnership
                assertTrue("The original production deadline elapsed in real time", SystemClock.elapsedRealtime() >= original.second)
                assertTrue("Normal production expiry preserves the exact public null-label replacement",
                    exact(requireNotNull(manager), replacement, null))
                assertTrue("Normal expiry forgets ownership without another Copy or pending retry", policy.token() == original.first &&
                    read(policy, "value") == null && read(adapter, "label") == null && read(policy, "deadline") == 0L &&
                    read(adapter, "retryQueued") == false)
            }
            report("null-label-regression-passed", id)
        } catch (failure: Throwable) {
            runCatching { report("$phase-failed", id) }
            throw AssertionError("Null-label clipboard regression failed; safe phase recorded")
        } finally {
            runCatching { timerMain {
                val clipboard = manager
                val label = ownLabel
                if (clipboard != null && legal(activity) && (exact(clipboard, replacement, null) ||
                        (label != null && exact(clipboard, dish, label)))) {
                    if (Build.VERSION.SDK_INT >= 28) clipboard.clearPrimaryClip()
                    else clipboard.setPrimaryClip(ClipData.newPlainText("", ""))
                }
            } }
            try { scenario.close() } finally { cleanupColdFixture(activity) }
        }
    }
}
