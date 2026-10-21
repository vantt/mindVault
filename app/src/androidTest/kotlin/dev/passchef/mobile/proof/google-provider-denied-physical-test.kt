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

/** Actual provider-loaded baseline, exact pre-Cook refusal preservation, then a fresh cached Cook. */
@RunWith(AndroidJUnit4::class)
class GoogleProviderDeniedPhysicalTest {
    @get:Rule val ui = createEmptyComposeRule()
    @Test fun actualGoogle403PreservesProviderCacheAndCooksAgain() {
        val args = InstrumentationRegistry.getArguments()
        val id = requireNotNull(args.getString("passchefProviderDeniedFixtureId"))
        require(UUID.fromString(id).toString() == id)
        val url = args.getString("passchefProviderDeniedMenuURL").orEmpty()
        require(url.length <= 300 && menuIdFromInput(url) != null)
        val readable = args.getString("passchefGoogleMenuURL").orEmpty()
        require(readable.length <= 300 && menuIdFromInput(readable) != null)
        require(menuIdFromInput(url) != menuIdFromInput(readable))
        val intent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, ColdStartProofActivity::class.java)
            .putExtra("passchefColdFixtureId", id).putExtra("passchefColdStage", "warm")
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val scenario = ActivityScenario.launch<ColdStartProofActivity>(intent)
        lateinit var activity: ColdStartProofActivity
        scenario.onActivity { activity = it }
        val state = ColdCacheObservation(activity)
        var proof: GoogleProviderDeniedObservation? = null
        var phase = "owner-setup-biometric"
        fun report(name: String, failed: Boolean = false) {
            phase = name
            if (proof != null) proof!!.report(name, failed) else {
                InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
                    putString("passchefProviderDeniedPhase", name)
                    putString("passchefProviderDeniedFixtureId", id)
                    putBoolean("passchefProviderDeniedFailed", failed)
                })
            }
        }
        try {
            report(phase)
            val fixture = ColdCacheUiFixture(ui, state)
            val saved = GoogleColdProviderUi(ui, state) { report(it) }.load(readable, "Test")
            val cacheUi = GoogleColdCacheUi(ui, state) { report(it) }
            val cached = cacheUi.favoriteCook(saved)
            ui.waitForIdle()
            assertTrue("Provider cache writes finish before exact baseline freeze", state.flags()["sourceBusy"] == false)
            val baseline = GoogleColdBaseline.create(activity, cached)
            proof = GoogleProviderDeniedObservation(state, baseline)
            assertTrue("Genuine provider cached Cook precedes refusal", state.flags()["hasDish"] == true)
            report("provider-baseline-frozen")
            state.requireReady()
            ui.onNode(hasClickAction() and hasText("Sources")).performScrollTo().performClick()
            ui.onNodeWithTag("source-id").performScrollTo().performTextReplacement(url)
            fixture.hideIme(); state.requireReady(); report("connect-dispatch")
            ui.onNode(hasClickAction() and hasText("Connect / load tabs")).performScrollTo().performClick()
            val deadline = SystemClock.elapsedRealtime() + 180_000
            var terminal = false
            while (SystemClock.elapsedRealtime() < deadline) {
                proof!!.assertPreserved()
                terminal = state.foreground() && timerMain { !state.book().sources.busy } && !proof!!.pending()
                if (terminal) break
                Thread.sleep(100)
            }
            assertTrue("Real production authorization/provider attempt completes within bounded wait", terminal)
            ui.waitForIdle(); proof!!.assertPreserved(); proof!!.assertDishCleared()
            ui.onNodeWithTag("dish-display").assertDoesNotExist()
            ui.onNodeWithTag("dish-copy").assertDoesNotExist()
            if (timerMain { state.book().sources.status != "ACCESS_DENIED" }) {
                report("not-exercised-other-production-outcome")
                assumeTrue("Actual HTTP403 remains Pending; SDK/cancel/stale/other provider outcomes are distinct", false)
            }
            proof!!.assertDenied()
            ui.onNodeWithText("This Google account cannot access this Menu.").assertExists()
            report("provider-http403-preservation-passed")
            cacheUi.cookAgain(cached.mapping.origin)
            proof!!.assertFreshCook()
            // recent may be rewritten by the fresh Cook; exact-byte preservation was asserted before it.
            report("provider-http403-and-fresh-cache-cook-passed")
        } catch (skipped: AssumptionViolatedException) {
            throw skipped
        } catch (failure: Throwable) {
            runCatching { report("$phase-failed", true) }
            throw AssertionError("Actual Google refusal checkpoint failed; safe phase recorded")
        } finally {
            try { proof?.close() } finally {
                try { scenario.close() } finally { cleanupColdFixture(activity) }
            }
        }
    }
}
