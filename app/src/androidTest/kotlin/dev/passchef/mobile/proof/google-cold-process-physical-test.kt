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

/** Warm is interrupted only by verified root SIGKILL; cold earns its own actual JUnit verdict. */
@RunWith(AndroidJUnit4::class)
class GoogleColdProcessPhysicalTest {
    @get:Rule val ui = createEmptyComposeRule()
    private var phase = "google-cold-arguments"
    private fun vaultDiagnostic(observation: ColdCacheObservation): String = timerMain {
        when (val status = observation.vault().status) {
            "Cookbook locked" -> "locked"
            "Strong biometric unavailable. Use your master password." -> "strong-biometric-unavailable"
            "Device key invalidated or missing. Use your original master password." -> "pre-prompt-key-unavailable"
            "Device key unavailable. Use your original master password." -> "post-prompt-unwrap-failed"
            "Biometric failed or cancelled. Use your master password." -> "biometric-callback-failed"
            "Opening the Cookbook…" -> "password-opening"
            else -> when {
                status.startsWith("Biometric round trip succeeded;") -> "biometric-session-ready"
                status.startsWith("Round trip ready;") -> "setup-round-trip-ready"
                else -> "other"
            }
        }
    }
    private fun launch(stage: String): ActivityScenario<ColdStartProofActivity> {
        val args = InstrumentationRegistry.getArguments()
        require(args.getString("passchefColdStage") == stage)
        val id = requireNotNull(args.getString("passchefColdFixtureId"))
        require(UUID.fromString(id).toString() == id)
        return ActivityScenario.launch(Intent(InstrumentationRegistry.getInstrumentation().targetContext, ColdStartProofActivity::class.java)
            .putExtra("passchefColdFixtureId", id).putExtra("passchefColdStage", stage)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
    }
    private fun report(name: String, observation: ColdCacheObservation, baseline: GoogleColdBaseline? = null,
        failed: Boolean = false, counts: Map<String, Int> = emptyMap()) {
        phase = name
        val flags = runCatching { observation.flags() + googleColdNetwork(observation.activity) + timerMain {
            val source = observation.book().sources
            mapOf("connectRequired" to (source.status == "CONNECT_REQUIRED"), "accessDenied" to (source.status == "ACCESS_DENIED"),
                "networkFailed" to (source.status == "NETWORK_FAILED"),
                "vaultActive" to (dev.passchef.mobile.VaultController::class.java.getDeclaredField("active")
                    .apply { isAccessible = true }.get(observation.vault()) == true))
        } }.getOrDefault(emptyMap())
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefGoogleColdPhase", name); putString("passchefGoogleColdFixtureId", observation.activity.proofId)
            putInt("passchefGoogleColdPid", Process.myPid()); putInt("passchefGoogleColdUid", Process.myUid())
            putBoolean("passchefGoogleColdFailed", failed)
            putString("passchefGoogleColdVaultStatus", runCatching { vaultDiagnostic(observation) }.getOrDefault("read-failed"))
            baseline?.let { putInt("passchefGoogleColdWarmPid", it.warmPid); putString("passchefGoogleColdJarSha256", it.jarSha)
                putString("passchefGoogleColdMenuSha256", it.menuSha); putLong("passchefGoogleColdRefreshedAt", it.refreshedAt) }
            flags.forEach { (key, value) -> putBoolean("passchefGoogleCold_$key", value) }
            counts.forEach { (key, value) -> putInt("passchefGoogleCold_$key", value) }
        })
    }
    @Test fun prepareProviderCacheForAbruptProcessDeath() {
        val scenario = launch("warm")
        lateinit var activity: ColdStartProofActivity
        scenario.onActivity { activity = it }
        val observation = ColdCacheObservation(activity)
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
            val args = InstrumentationRegistry.getArguments()
            val url = args.getString("passchefGoogleMenuURL").orEmpty()
            val tab = args.getString("passchefGoogleTab").orEmpty().ifEmpty { "Test" }
            val provider = GoogleColdProviderUi(ui, observation) { report(it, observation) }
            val saved = provider.load(url, tab)
            val cached = GoogleColdCacheUi(ui, observation) { report(it, observation) }.favoriteCook(saved)
            val baseline = GoogleColdBaseline.create(activity, cached)
            baseline.write(activity.proofDirectory)
            assertTrue("Actual provider cache/jars reread equals frozen warm digests", baseline.matches(activity.proofDirectory))
            assertTrue("All actual provider/cache/favorite/recent IO completed", observation.flags()["sourceBusy"] == false)
            activity.application.registerActivityLifecycleCallbacks(monitor); monitoring = true
            observation.requireReady()
            assertTrue("Actual root kill checkpoint contains a genuinely revealed synthetic Dish", observation.flags()["revealed"] == true)
            report("kill-ready", observation, baseline, counts = mapOf("paused" to paused.get(), "stopped" to stopped.get(), "destroyed" to destroyed.get()))
            val deadline = SystemClock.elapsedRealtime() + 90_000
            while (SystemClock.elapsedRealtime() < deadline) {
                assertTrue("No graceful lifecycle transition precedes root's provider process kill",
                    paused.get() == 0 && stopped.get() == 0 && destroyed.get() == 0)
                observation.requireReady(); Thread.sleep(100)
            }
            throw AssertionError("Root abrupt process death not observed; provider warm cannot pass")
        } catch (failure: Throwable) {
            runCatching { report("$phase-failed", observation, failed = true) }
            throw AssertionError("Google provider warm failed; safe metadata recorded")
        } finally {
            // Verified SIGKILL cannot run cleanup; ordinary live failures clean only this exact UUID.
            if (monitoring) activity.application.unregisterActivityLifecycleCallbacks(monitor)
            scenario.close(); cleanupColdFixture(activity)
        }
    }
    @Test fun reopenProviderCacheInFreshProcess() {
        val scenario = launch("cold")
        lateinit var activity: ColdStartProofActivity
        scenario.onActivity { activity = it }
        val observation = ColdCacheObservation(activity)
        try {
            observation.await(observation::foreground); ui.waitForIdle()
            val baseline = GoogleColdBaseline.read(activity)
            report("google-cold-initial-locked", observation, baseline)
            assertTrue("Actual cold provider process PID differs from warm", Process.myPid() != baseline.warmPid)
            observation.initialLocked()
            assertTrue("Abrupt death preserves exact provider cache/jars before unlock", baseline.matches(activity.proofDirectory))
            val expected = baseline.snapshot(activity.proofDirectory)
            GoogleColdCacheUi(ui, observation) { report(it, observation, baseline) }.reopen(baseline, expected)
            assertTrue("Genuine provider cold Cook preserves encrypted jar bytes", baseline.jarsMatch(activity.proofDirectory))
            report("provider-cache-passed", observation, baseline)
        } catch (failure: Throwable) {
            runCatching { report("$phase-failed", observation, failed = true) }
            throw AssertionError("Google provider cold failed; safe metadata recorded")
        } finally { scenario.close(); cleanupColdFixture(activity) }
    }
}
