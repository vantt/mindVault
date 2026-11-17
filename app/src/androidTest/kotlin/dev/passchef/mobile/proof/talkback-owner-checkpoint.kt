package dev.passchef.mobile.proof

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.UiAutomation
import android.content.ComponentName
import android.os.Bundle
import android.os.SystemClock
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import androidx.lifecycle.Lifecycle
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import java.util.UUID
import org.json.JSONObject
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue

/** Native eligibility only: the owner's spoken/focus verdict is recorded outside instrumentation. */
class TalkBackOwnerCheckpoint(private val activity: CookbookProofActivity) {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    // Acquire before every owner wait without suppressing the actual installed screen reader.
    private val automation = instrumentation.getUiAutomation(UiAutomation.FLAG_DONT_SUPPRESS_ACCESSIBILITY_SERVICES)
    private val manager = activity.getSystemService(AccessibilityManager::class.java)
    private val expected = requireNotNull(ComponentName.unflattenFromString(requireNotNull(
        InstrumentationRegistry.getArguments().getString("passchefTalkBackComponent"))))
    private val directory = activity.proofDirectory.canonicalFile
    val fixtureId = directory.name.removePrefix("cookbook-ui-proof-")
    var phase = "talkback-preflight"
        private set
    init {
        require(directory.name == "cookbook-ui-proof-${UUID.fromString(fixtureId)}")
        require(expected.flattenToString().length <= 300 &&
            expected.flattenToString().contains("talkback", ignoreCase = true))
    }
    private fun enabled(): Boolean = manager.isEnabled && manager
        .getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_SPOKEN).any { service ->
            val info = service.resolveInfo.serviceInfo
            ComponentName(info.packageName, info.name) == expected &&
                service.feedbackType and AccessibilityServiceInfo.FEEDBACK_SPOKEN != 0
        }
    fun preflight() {
        val available = enabled() && manager.isTouchExplorationEnabled
        report(if (available) "talkback-service-ready" else "not-exercised-talkback-unavailable", "preflight", 0)
        assumeTrue("Expected owner-enabled TalkBack with touch exploration is unavailable", available)
    }
    private fun guard(eligible: () -> Boolean) {
        assertTrue("Actual expected TalkBack remains enabled during owner observation", enabled())
        assertTrue("Actual TalkBack touch exploration remains enabled during gesture observation", manager.isTouchExplorationEnabled)
        assertTrue("Owner checkpoint stays in the real secure own foreground", timerMain {
            val flags = CookbookUiDiagnostics.environmentFlags(activity)
            activity.lifecycle.currentState == Lifecycle.State.RESUMED &&
                activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0 &&
                listOf("powerInteractive", "proofWindowFocused", "proofWindowAttached", "proofWindowShown")
                    .all { flags[it] == true } && flags["keyguardLocked"] == false && flags["deviceLocked"] == false
        })
        assertTrue("Actual locked or masked authenticated stage remains eligible", eligible())
    }
    fun report(name: String, checkpoint: String, elapsed: Long) {
        phase = name
        val configuration = timerMain {
            val resources = activity.resources
            intArrayOf((resources.configuration.fontScale * 1000).toInt(), resources.configuration.orientation,
                resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels,
                (resources.displayMetrics.density * 1000).toInt())
        }
        instrumentation.sendStatus(2, Bundle().apply {
            putString("passchefTalkBackPhase", name); putString("passchefTalkBackCheckpoint", checkpoint)
            putString("passchefTalkBackFixtureId", fixtureId)
            putString("passchefTalkBackComponent", expected.flattenToString())
            putBoolean("passchefTalkBackExpectedServiceEnabled", enabled())
            putBoolean("passchefTalkBackTouchExplorationEnabled", manager.isTouchExplorationEnabled)
            putLong("passchefTalkBackElapsedSinceReady", elapsed)
            putInt("passchefTalkBackFontScaleX1000", configuration[0]); putInt("passchefTalkBackOrientation", configuration[1])
            putInt("passchefTalkBackDisplayWidth", configuration[2]); putInt("passchefTalkBackDisplayHeight", configuration[3])
            putInt("passchefTalkBackDensityX1000", configuration[4])
        })
    }
    fun awaitOwner(checkpoint: String, eligible: () -> Boolean): Boolean {
        require(checkpoint in setOf("locked-en", "locked-vi", "authenticated-masked"))
        val marker = File(directory, "talkback-owner-$checkpoint.json")
        assertTrue("Fresh owned checkpoint has no completion signal", !marker.exists() && marker.canonicalFile.parentFile == directory)
        guard(eligible)
        val started = SystemClock.elapsedRealtime()
        report("owner-ready", checkpoint, 0)
        while (!marker.exists() && SystemClock.elapsedRealtime() - started < 120_000) {
            guard(eligible)
            Thread.sleep(200) // Actual observation only; production reveal/session clocks remain untouched.
        }
        if (!marker.exists() || SystemClock.elapsedRealtime() - started >= 120_000) {
            report("owner-completion-timed-out", checkpoint, SystemClock.elapsedRealtime() - started)
            throw AssertionError("Owner TalkBack completion signal timed out")
        }
        guard(eligible)
        assertTrue("Only the exact bounded owned completion file is accepted", marker.canonicalFile == File(directory, marker.name) &&
            marker.length() in 1..256)
        val bytes = ByteArray(257)
        var size = 0
        marker.inputStream().use { input ->
            while (size < bytes.size) {
                val count = input.read(bytes, size, bytes.size - size)
                if (count < 0) break
                if (count > 0) size += count
            }
        }
        assertTrue("Owner completion metadata is bounded", size in 1..256)
        val completed = JSONObject(String(bytes, 0, size, Charsets.UTF_8))
        assertTrue("Completion signal binds UUID and stage, never a spoken verdict",
            completed.keys().asSequence().toSet() == setOf("completed", "fixtureId", "checkpoint") &&
                completed.getString("fixtureId") == fixtureId && completed.getString("checkpoint") == checkpoint &&
                completed.get("completed") is Boolean)
        val observed = completed.get("completed") == true
        report(if (observed) "owner-checkpoint-observed" else "owner-checkpoint-skipped", checkpoint,
            SystemClock.elapsedRealtime() - started)
        return observed
    }
}
