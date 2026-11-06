package dev.passchef.mobile.proof

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.UiAutomation
import android.content.ComponentName
import android.content.res.Configuration
import android.os.Bundle
import android.os.Process
import android.os.SystemClock
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import java.util.UUID
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/** Preparation only: leaves one locked empty owned Activity for untimed external owner observation. */
@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 36)
class TalkBackVietnamesePreparationTest {
    @Test fun prepareFreshVietnameseOsLabelForSeparateOwnerObservation() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val args = InstrumentationRegistry.getArguments()
        require(args.getString("waitForActivitiesToComplete") == "false")
        val expected = requireNotNull(ComponentName.unflattenFromString(requireNotNull(args.getString("passchefTalkBackComponent"))))
        require(expected.flattenToString().contains("talkback", ignoreCase = true))
        val automation = instrumentation.getUiAutomation(UiAutomation.FLAG_DONT_SUPPRESS_ACCESSIBILITY_SERVICES)
        val manager = instrumentation.targetContext.getSystemService(AccessibilityManager::class.java)
        fun spoken() = manager.isEnabled && manager.isTouchExplorationEnabled && manager
            .getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_SPOKEN).any {
                val info = it.resolveInfo.serviceInfo
                ComponentName(info.packageName, info.name) == expected &&
                    it.feedbackType and AccessibilityServiceInfo.FEEDBACK_SPOKEN != 0
            }
        var scenario: ActivityScenario<CookbookProofActivity>? = null
        var directory: File? = null
        var activity: CookbookProofActivity? = null
        var retained = false
        var primary: Throwable? = null
        var phase = "preflight"
        val parent = instrumentation.targetContext.noBackupFilesDir.canonicalFile
        fun owned(file: File) {
            val id = file.name.removePrefix("cookbook-ui-proof-")
            assertTrue("Only the recorded canonical UUID is owned", file.parentFile == parent && file.canonicalFile == file &&
                file.name == "cookbook-ui-proof-$id" && UUID.fromString(id).toString() == id)
        }
        fun report(name: String, flags: Map<String, Boolean> = emptyMap(), counts: Map<String, Int> = emptyMap()) {
            phase = name
            instrumentation.sendStatus(2, Bundle().apply {
                putString("passchefVietnameseManualPhase", name)
                directory?.let { putString("passchefVietnameseManualFixtureId", it.name.removePrefix("cookbook-ui-proof-")) }
                putInt("passchefVietnameseManualPid", Process.myPid())
                activity?.let { current ->
                    val configuration = timerMain {
                        val resources = current.resources
                        intArrayOf(current.taskId, (resources.configuration.fontScale * 1000).toInt(), resources.configuration.orientation,
                            resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
                    }
                    putInt("passchefVietnameseManualTaskId", configuration[0])
                    putInt("passchefVietnameseManualFontScaleX1000", configuration[1])
                    putInt("passchefVietnameseManualOrientation", configuration[2])
                    putInt("passchefVietnameseManualDisplayWidth", configuration[3]); putInt("passchefVietnameseManualDisplayHeight", configuration[4])
                }
                flags.forEach { (key, value) -> putBoolean("passchefVietnameseManual_$key", value) }
                counts.forEach { (key, value) -> putInt("passchefVietnameseManual_$key", value) }
            })
        }
        fun guarded(): Map<String, Boolean> = timerMain {
            val current = requireNotNull(activity)
            val file = requireNotNull(directory)
            val state = CookbookUiDiagnostics.stateFlags(current)
            val environment = CookbookUiDiagnostics.environmentFlags(current)
            environment + mapOf("resumed" to (current.lifecycle.currentState == Lifecycle.State.RESUMED),
                "secure" to (current.window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0),
                "retained" to current.retainProofStorage,
                "emptyOwnedDirectory" to (file.listFiles()?.isEmpty() == true),
                "lockedAndCleared" to listOf("vaultUnlocked", "vaultReady", "vaultBusy", "hasDish", "revealed", "dishNonempty")
                    .all { state[it] == false },
                "font200percent" to ((current.resources.configuration.fontScale * 1000).toInt() == 2000),
                "landscape" to (current.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE &&
                    current.resources.displayMetrics.widthPixels > current.resources.displayMetrics.heightPixels))
        }
        fun requireGuard(): Map<String, Boolean> {
            val flags = guarded() + mapOf("spokenServiceAndTouch" to spoken())
            assertTrue("Actual eligible locked200percent landscape foreground required", listOf("powerInteractive", "proofWindowFocused",
                "proofWindowAttached", "proofWindowShown", "resumed", "secure", "retained", "emptyOwnedDirectory", "lockedAndCleared",
                "font200percent", "landscape", "spokenServiceAndTouch").all { flags[it] == true } &&
                flags["keyguardLocked"] == false && flags["deviceLocked"] == false)
            return flags
        }
        fun await(condition: () -> Boolean) {
            val end = SystemClock.elapsedRealtime() + 15_000
            while (SystemClock.elapsedRealtime() < end) {
                if (condition()) return
                Thread.sleep(100)
            }
            assertTrue("Bounded actual native UI readiness required", condition())
        }
        try {
            report("service-preflight", mapOf("spokenServiceAndTouch" to spoken()))
            assertTrue("Owner-enabled expected spoken service and touch exploration required", spoken())
            val launched = ActivityScenario.launch(CookbookProofActivity::class.java)
            scenario = launched
            launched.onActivity { activity = it; it.retainProofStorage = true }
            val current = requireNotNull(activity)
            val file = current.proofDirectory.canonicalFile
            owned(file); directory = file
            await { runCatching { requireGuard(); true }.getOrDefault(false) }
            report("locked-original-language", requireGuard())
            val actions = SavedTaskUiActions(automation, current.packageName, foreground = {
                runCatching { requireGuard(); true }.getOrDefault(false)
            }, staticLabelsOnly = true)
            if (!actions.hasText("PassChef — Cookbook riêng")) actions.click("VI")
            await { actions.hasText("PassChef — Cookbook riêng") && !actions.hasText("PassChef — private Cookbook") }
            assertTrue("Actual native VI headline is present", actions.hasText("PassChef — Cookbook riêng"))
            assertTrue("Actual native EN headline is absent", !actions.hasText("PassChef — private Cookbook"))
            actions.showEditable("Mật khẩu chính")
            val os = readVietnameseManualOs(automation, current.packageName)
            report("fresh-vietnamese-os-observation", requireGuard() + os.flags, os.counts)
            assertTrue("Fresh OS password field is uniquely visible editable protected and VI-labelled without EN label",
                listOf("cacheCleared", "ownWindow", "complete", "singleField", "fieldVisible", "fieldEditable", "fieldPassword", "fieldLabelVi",
                    "fieldSemanticsIdPresent").all { os.flags[it] == true } && os.flags["fieldLabelEn"] == false &&
                    os.counts["englishPasswordFields"] == 0)
            report("preparation-only-retained-locked-empty", requireGuard() + os.flags, os.counts)
            retained = true // No owner speech verdict, waiting marker or native120s deadline.
        } catch (failure: Throwable) {
            primary = failure
            runCatching { report("$phase-failed", mapOf("failed" to true)) }
            throw failure
        } finally {
            if (!retained) {
                val failures = mutableListOf<Throwable>()
                fun clean(work: () -> Unit) { try { work() } catch (failure: Throwable) { failures += failure } }
                clean { scenario?.close() }
                clean { directory?.let { owned(it); assertTrue("Failed prep leaves only an empty UUID", !it.exists() || it.listFiles()?.isEmpty() == true)
                    assertTrue("Exact empty UUID removal succeeds", !it.exists() || it.delete()) } }
                clean { directory?.let { owned(it); assertTrue("Failed prep UUID absence verified", !it.exists()) } }
                if (failures.isNotEmpty()) {
                    val original = primary
                    if (original != null) failures.forEach(original::addSuppressed)
                    else throw failures.first().also { first -> failures.drop(1).forEach(first::addSuppressed) }
                }
            }
        }
    }
}
