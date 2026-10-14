package dev.passchef.mobile.proof

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.os.SystemClock
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

/** Select stages separately. Root verifies/executes abrupt PID death; warm must never earn Passed. */
@RunWith(AndroidJUnit4::class)
class ColdStartCachePhysicalTest {
    @get:Rule val ui = createEmptyComposeRule()
    private fun launch(stage: String): ActivityScenario<ColdStartProofActivity> {
        val arguments = InstrumentationRegistry.getArguments()
        require(arguments.getString("passchefColdStage") == stage) { "Explicit matching stage required" }
        val id = requireNotNull(arguments.getString("passchefColdFixtureId"))
        require(UUID.fromString(id).toString() == id) { "Canonical fixture UUID required" }
        val intent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, ColdStartProofActivity::class.java)
            .putExtra("passchefColdFixtureId", id).putExtra("passchefColdStage", stage)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        return ActivityScenario.launch(intent)
    }
    @Test fun prepareOfflineCacheForAbruptProcessDeath() {
        val scenario = launch("warm")
        lateinit var activity: ColdStartProofActivity
        scenario.onActivity { activity = it }
        val observation = ColdCacheObservation(activity)
        val fixture = ColdCacheUiFixture(ui, observation)
        val paused = AtomicInteger(); val stopped = AtomicInteger(); val destroyed = AtomicInteger()
        val monitor = object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(value: Activity) { if (value === activity) paused.incrementAndGet() }
            override fun onActivityStopped(value: Activity) { if (value === activity) stopped.incrementAndGet() }
            override fun onActivityDestroyed(value: Activity) { if (value === activity) destroyed.incrementAndGet() }
            override fun onActivityCreated(value: Activity, state: Bundle?) = Unit
            override fun onActivityStarted(value: Activity) = Unit
            override fun onActivityResumed(value: Activity) = Unit
            override fun onActivitySaveInstanceState(value: Activity, state: Bundle) = Unit
        }
        var monitoring = false
        try {
            fixture.setup(); fixture.prepareCache()
            val baseline = ColdCacheBaseline.create(activity, fixture.snapshot())
            baseline.write(activity.proofDirectory)
            assertTrue("Warm baseline hashes match actual committed owned jars/cache", baseline.matches(activity.proofDirectory))
            activity.application.registerActivityLifecycleCallbacks(monitor); monitoring = true
            observation.requireReady()
            assertTrue("Root kill checkpoint has an actual revealed exact synthetic Dish", observation.flags()["revealed"] == true)
            observation.report("kill-ready", baseline)
            val deadline = SystemClock.elapsedRealtime() + 90_000
            while (SystemClock.elapsedRealtime() < deadline) {
                assertTrue("No graceful pause/stop/destroy precedes root's abrupt process death",
                    paused.get() == 0 && stopped.get() == 0 && destroyed.get() == 0)
                observation.requireReady()
                Thread.sleep(100)
            }
            throw AssertionError("Root abrupt process death was not observed; warm stage cannot pass")
        } catch (error: Throwable) {
            runCatching { observation.report("${observation.phase}-failed", failed = true) }
            throw AssertionError("Warm process proof failed before abrupt death; safe metadata recorded")
        } finally {
            // Actual SIGKILL cannot execute this block. Live failures clean only the exact fixture.
            if (monitoring) activity.application.unregisterActivityLifecycleCallbacks(monitor)
            scenario.close()
            cleanupColdFixture(activity)
        }
    }
    @Test fun reopenOfflineCacheInFreshProcess() {
        val scenario = launch("cold")
        lateinit var activity: ColdStartProofActivity
        scenario.onActivity { activity = it }
        val observation = ColdCacheObservation(activity)
        val fixture = ColdCacheUiFixture(ui, observation)
        try {
            observation.await(observation::foreground); ui.waitForIdle()
            val baseline = ColdCacheBaseline.read(activity)
            observation.report("cold-initial-locked", baseline)
            assertTrue("Actual fresh process PID differs from warm process", Process.myPid() != baseline.warmPid)
            observation.initialLocked()
            assertTrue("Abrupt death preserves exact encrypted jars and confirmed offline cache", baseline.matches(activity.proofDirectory))
            fixture.reopen(baseline)
            assertTrue("Genuine cold reopen/cached cooking preserve encrypted jar bytes", baseline.jarsMatch(activity.proofDirectory))
            observation.report("cold-cache-passed", baseline)
        } catch (error: Throwable) {
            runCatching { observation.report("${observation.phase}-failed", failed = true) }
            throw AssertionError("Cold process proof failed; safe metadata recorded")
        } finally {
            scenario.close()
            cleanupColdFixture(activity)
        }
    }
}
