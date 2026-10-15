package dev.passchef.mobile.proof

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.cookbook.menuIdFromInput
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.AssumptionViolatedException
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

/** Natural external lock/stale-result proof. Owner cancellation observation is recorded separately. */
@RunWith(AndroidJUnit4::class)
class GoogleCancellationPhysicalTest {
    @get:Rule val ui = createEmptyComposeRule()
    private var phase = "google-cancel-arguments"
    private fun report(name: String, state: ColdCacheObservation, proof: GoogleCancellationObservation? = null) {
        phase = name
        if (proof != null) proof.report(name) else {
            InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
                putString("passchefGoogleCancelPhase", name)
                putString("passchefGoogleCancelFixtureId", state.activity.proofId)
            })
        }
    }
    @Test fun realExternalGoogleReplyAfterNaturalLockPreservesOfflineCache() {
        val args = InstrumentationRegistry.getArguments()
        val id = requireNotNull(args.getString("passchefGoogleCancelFixtureId"))
        require(UUID.fromString(id).toString() == id)
        val url = args.getString("passchefGoogleMenuURL").orEmpty()
        require(url.length <= 300 && menuIdFromInput(url) != null)
        val intent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, ColdStartProofActivity::class.java)
            .putExtra("passchefColdFixtureId", id).putExtra("passchefColdStage", "warm")
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val scenario = ActivityScenario.launch<ColdStartProofActivity>(intent)
        lateinit var activity: ColdStartProofActivity
        scenario.onActivity { activity = it }
        val state = ColdCacheObservation(activity)
        var activeProof: GoogleCancellationObservation? = null
        try {
            report("owner-setup-biometric", state)
            val fixture = ColdCacheUiFixture(ui, state)
            fixture.setup(); fixture.prepareCache()
            val baseline = ColdCacheBaseline.create(activity, fixture.snapshot())
            val proof = GoogleCancellationObservation(state, baseline)
            activeProof = proof
            assertTrue("Genuine synthetic Cook precedes the new Google Connect", state.flags()["hasDish"] == true)
            state.requireReady()
            ui.onNode(hasClickAction() and hasText("Sources")).performScrollTo().performClick()
            ui.onNodeWithTag("source-id").performScrollTo().performTextReplacement(url)
            fixture.hideIme()
            report("connect-dispatch", state, proof)
            state.requireReady()
            ui.onNode(hasClickAction() and hasText("Connect / load tabs")).performScrollTo().performClick()
            val resolutionDeadline = SystemClock.elapsedRealtime() + 30_000
            var external = false
            while (SystemClock.elapsedRealtime() < resolutionDeadline) {
                if (proof.observeProvider()) { external = true; break }
                val terminal = state.foreground() && timerMain {
                    !state.book().sources.busy && !proof.pending()
                }
                if (terminal) break
                Thread.sleep(50)
            }
            if (!external) {
                proof.assertPreserved()
                assertTrue("An observed request/stop or unresolved request must not be skipped", proof.noResolutionObserved() && timerMain {
                    !state.book().sources.busy
                })
                report("not-exercised-no-observed-external-resolution", state, proof)
                assumeTrue("Pending: no actual Google external resolution observed; cached grant/error is not cancellation acceptance", false)
            }
            report("owner-cancel-google-now", state, proof)
            val returnDeadline = SystemClock.elapsedRealtime() + 180_000
            while (SystemClock.elapsedRealtime() < returnDeadline && (!state.foreground() || proof.pending())) {
                proof.assertPreserved()
                Thread.sleep(100)
            }
            assertTrue("Actual Google result returned within bounded owner wait", state.foreground() && !proof.pending())
            ui.waitForIdle()
            proof.assertReturned()
            ui.onNodeWithTag("dish-display").assertDoesNotExist()
            ui.onNodeWithTag("dish-copy").assertDoesNotExist()
            report("stale-reply-preservation-passed", state, proof)
        } catch (skipped: AssumptionViolatedException) {
            throw skipped
        } catch (failure: Throwable) {
            runCatching { activeProof?.report("$phase-failed", failed = true) }
            throw AssertionError("Google cancellation/stale-result checkpoint failed; safe phase recorded")
        } finally {
            try { activeProof?.close() } finally {
                try { scenario.close() } finally { cleanupColdFixture(activity) }
            }
        }
    }
}
