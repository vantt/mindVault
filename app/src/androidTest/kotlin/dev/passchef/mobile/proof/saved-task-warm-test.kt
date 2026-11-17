package dev.passchef.mobile.proof

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Process
import android.os.SystemClock
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.KeystoreCrypto
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/** Prepared is not saved-task acceptance. Host kills only after this no-restart stage is terminal. */
@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 36)
class SavedTaskWarmTest {
    @Test fun prepareAuthenticatedStoppedTaskWithoutFinishingActivity() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val arguments = InstrumentationRegistry.getArguments()
        require(arguments.getString("passchefSavedTaskStage") == "warm" &&
            arguments.getString("waitForActivitiesToComplete") == "false")
        val scenario = ActivityScenario.launch<CookbookProofActivity>(Intent(instrumentation.targetContext,
            CookbookProofActivity::class.java).putExtra("passchefSavedTaskProof", true)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        val fixture = SavedTaskNativeFixture(scenario)
        fixture.attach()
        var prepared = false
        var primaryFailure: Throwable? = null
        try {
            val snapshot = fixture.setupCacheAndCook()
            val activity = fixture.activity
            val journal = SavedTaskProofJournal.read(fixture.directory)
            assertTrue("Warm task starts in this process with real initial creation", journal.getInt("originalPid") == Process.myPid() &&
                journal.getInt("originalTaskId") == activity.taskId && !journal.getBoolean("lastCreatedWithSavedState"))
            val baseline = SavedTaskBaseline(fixture.directory, Process.myPid(), activity.taskId, snapshot.refreshedAt,
                SavedTaskBaseline.productionHashes(fixture.directory), SavedTaskBaseline.payload(requireNotNull(fixture.record())),
                journal.getInt("createCount"))
            assertTrue("Warm proof has the actual encrypted Cookbook and confirmed Menu", baseline.hashes.size == 2)
            baseline.write(); baseline.report("warm-before-system-home")
            assertTrue("Actual Android Home action succeeds", instrumentation.uiAutomation.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME))
            val deadline = SystemClock.elapsedRealtime() + 20_000
            var observed = false
            while (!observed && SystemClock.elapsedRealtime() < deadline) {
                val state = SavedTaskProofJournal.read(fixture.directory)
                observed = state.optInt("warmSavedPid") == baseline.pid && state.optInt("warmStoppedPid") == baseline.pid &&
                    state.optInt("warmSavedTaskId", -1) == baseline.taskId && state.optInt("warmStoppedTaskId", -1) == baseline.taskId &&
                    state.optBoolean("warmSavedLocatorMatches") && state.optBoolean("warmStoppedCleared")
                if (!observed) Thread.sleep(100)
            }
            assertTrue("Real Home delivered saved-state/stop/cleared callbacks on the exact task", observed)
            fixture.requireCleared(activity)
            assertTrue("Warm stop preserves exact encrypted jars/cache", baseline.matches())
            prepared = true
            baseline.report("warm-prepared-await-host-process-death")
            // Explicit no-restart + waitForActivitiesToComplete=false leaves this stopped Activity to the host.
        } catch (failure: Throwable) {
            primaryFailure = failure
            throw failure
        } finally {
            if (!prepared) {
                val failures = mutableListOf<Throwable>()
                fun clean(action: () -> Unit) { try { action() } catch (failure: Throwable) { failures += failure } }
                clean { scenario.close() }
                var aliases = emptyList<String>()
                clean { fixture.record()?.let { aliases = (listOf(it.alias) + it.retiredAliases).filter(String::isNotEmpty).distinct() } }
                aliases.forEach { alias -> clean { KeystoreCrypto().deleteKey(alias) } }
                clean { assertTrue("Failed warm fixture alone is removed", fixture.directory.deleteRecursively()) }
                if (failures.isNotEmpty()) {
                    val primary = primaryFailure
                    if (primary != null) failures.forEach(primary::addSuppressed)
                    else throw failures.first().also { first -> failures.drop(1).forEach(first::addSuppressed) }
                }
            }
        }
    }
}
