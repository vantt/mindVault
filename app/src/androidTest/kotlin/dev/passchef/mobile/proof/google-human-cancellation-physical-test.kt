package dev.passchef.mobile.proof

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.KeystoreCrypto
import dev.passchef.mobile.cookbook.menuIdFromInput
import dev.passchef.mobile.vault.AtomicRecordStore
import dev.passchef.mobile.vault.VaultRepository
import java.io.File
import java.util.UUID
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Native cancellation/ownership proof only; the owner's hand action is separate evidence. */
@RunWith(AndroidJUnit4::class)
class GoogleHumanCancellationPhysicalTest {
    @get:Rule val ui = createEmptyComposeRule()
    @Test fun actualCanceledResultAfterOwnerGoogleCancellationPreservesOfflineCookbook() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val args = InstrumentationRegistry.getArguments()
        val id = requireNotNull(args.getString("passchefGoogleCancelFixtureId"))
        require(UUID.fromString(id).toString() == id)
        val url = args.getString("passchefGoogleMenuURL").orEmpty()
        require(url.length <= 300 && menuIdFromInput(url) != null)
        val directory = File(instrumentation.targetContext.noBackupFilesDir.canonicalFile, "cookbook-cold-proof-$id")
        require(!directory.exists())
        var scenario: ActivityScenario<ColdStartProofActivity>? = null
        var proof: GoogleCancellationObservation? = null
        var primaryFailure: Throwable? = null
        val aliases = mutableSetOf<String>()
        var phase = "google-human-launch"
        fun report(name: String, snapshot: GoogleResultProofSnapshot? = null) {
            phase = name
            instrumentation.sendStatus(2, Bundle().apply {
                putString("passchefGoogleHumanPhase", name); putString("passchefGoogleHumanFixtureId", id)
                snapshot?.let {
                    putInt("passchefGoogleHumanLaunches", it.launches); putInt("passchefGoogleHumanResults", it.results)
                    putInt("passchefGoogleHumanForwarded", it.forwarded)
                    it.requestCode?.let { code -> putInt("passchefGoogleHumanRequestCode", code) }
                    it.resultCode?.let { code -> putInt("passchefGoogleHumanResultCode", code) }
                    putBoolean("passchefGoogleHumanMatchedOriginal", it.matchedOriginal)
                    putBoolean("passchefGoogleHumanOriginalStillPending", it.originalStillPending)
                    putBoolean("passchefGoogleHumanConsumed", it.consumed)
                    putBoolean("passchefGoogleHumanWitnessFailed", it.failed)
                }
            })
        }
        try {
            val launched = ActivityScenario.launch<ColdStartProofActivity>(Intent(instrumentation.targetContext, ColdStartProofActivity::class.java)
                .putExtra("passchefColdFixtureId", id).putExtra("passchefColdStage", "warm")
                .putExtra("passchefGoogleResultProof", true)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            scenario = launched
            lateinit var activity: ColdStartProofActivity
            launched.onActivity { activity = it }
            val state = ColdCacheObservation(activity)
            val witness = requireNotNull(activity.googleResultProof)
            val fixture = ColdCacheUiFixture(ui, state)
            report("owner-setup-biometric")
            fixture.setup()
            VaultRepository(AtomicRecordStore(File(directory, "cookbook-v1.json"))).record()?.let {
                aliases += (listOf(it.alias) + it.retiredAliases).filter(String::isNotEmpty)
            }
            fixture.prepareCache()
            val baseline = ColdCacheBaseline.create(activity, fixture.snapshot())
            val observed = GoogleCancellationObservation(state, baseline)
            proof = observed
            assertTrue("Real cached synthetic Cook precedes human cancellation", state.flags()["hasDish"] == true)
            state.requireReady()
            ui.onNode(hasClickAction() and hasText("Sources")).performScrollTo().performClick()
            ui.onNodeWithTag("source-id").performScrollTo().performTextReplacement(url)
            fixture.hideIme(); report("connect-dispatch"); state.requireReady()
            ui.onNode(hasClickAction() and hasText("Connect / load tabs")).performScrollTo().performClick()
            val externalDeadline = SystemClock.elapsedRealtime() + 30_000
            var external = false
            while (SystemClock.elapsedRealtime() < externalDeadline) {
                if (observed.observeProvider()) { external = true; break }
                if (state.foreground() && timerMain { !state.book().sources.busy && !observed.pending() }) break
                Thread.sleep(50)
            }
            if (!external) {
                observed.assertPreserved()
                assertTrue("Observed pending/stop cannot become skipped human proof", observed.noResolutionObserved() &&
                    timerMain { !state.book().sources.busy })
                val idle = timerMain(witness::snapshot)
                assertTrue("Only silent grant reuse without a launch, result or witness fault can be skipped",
                    idle.launches == 0 && idle.results == 0 && idle.forwarded == 0 &&
                        idle.requestCode == null && idle.resultCode == null && !idle.matchedOriginal &&
                        !idle.originalStillPending && !idle.consumed && !idle.failed)
                report("not-exercised-no-real-external-google", idle)
                assumeTrue("No actual Google resolution; unchanged grant reuse is not human cancellation", false)
            }
            val pending = timerMain(witness::snapshot)
            assertTrue("Real IntentSender binds the original pending request and actual registry code",
                pending.launches == 1 && pending.requestCode != null && pending.results == 0 &&
                    pending.forwarded == 0 && pending.originalStillPending && !pending.failed)
            report("owner-cancel-google-now", pending)
            val deadline = SystemClock.elapsedRealtime() + 180_000
            while (SystemClock.elapsedRealtime() < deadline && (!state.foreground() || observed.pending())) {
                observed.assertPreserved(); Thread.sleep(100)
            }
            assertTrue("Actual owner Google result returns within bounded wait", state.foreground() && !observed.pending())
            ui.waitForIdle(); observed.assertReturned()
            val result = timerMain(witness::snapshot)
            report("real-result-returned", result)
            assertTrue("Original real request receives one canceled result through unchanged normal dispatch",
                result.launches == 1 && result.results == 1 && result.forwarded == 1 &&
                    result.matchedOriginal && result.resultCode == Activity.RESULT_CANCELED && result.consumed &&
                    !result.originalStillPending && !result.failed)
            ui.onNodeWithTag("dish-display").assertDoesNotExist()
            ui.onNodeWithTag("dish-copy").assertDoesNotExist()
            report("canceled-result-native-checkpoint-complete", result)
        } catch (failure: Throwable) {
            primaryFailure = failure
            runCatching { report("$phase-terminal-${failure.javaClass.simpleName}") }
            throw failure
        } finally {
            val failures = mutableListOf<Throwable>()
            fun clean(action: () -> Unit) { try { action() } catch (failure: Throwable) { failures += failure } }
            clean { proof?.close() }; clean { scenario?.close() }
            clean { VaultRepository(AtomicRecordStore(File(directory, "cookbook-v1.json"))).record()?.let {
                aliases += (listOf(it.alias) + it.retiredAliases).filter(String::isNotEmpty)
            } }
            aliases.forEach { alias -> clean { KeystoreCrypto().deleteKey(alias) } }
            clean { assertTrue("Exact human-cancellation fixture alone is removed", !directory.exists() || directory.deleteRecursively()) }
            clean { assertTrue("Human-cancellation UUID removal is verified", !directory.exists()) }
            if (failures.isNotEmpty()) {
                val primary = primaryFailure
                if (primary != null) failures.forEach(primary::addSuppressed)
                else throw failures.first().also { first -> failures.drop(1).forEach(first::addSuppressed) }
            }
        }
    }
}
