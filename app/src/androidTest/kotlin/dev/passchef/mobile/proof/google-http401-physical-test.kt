package dev.passchef.mobile.proof

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

/** Conditional on separate final approval; native code never performs the external grant transition. */
@RunWith(AndroidJUnit4::class)
class GoogleHttp401PhysicalTest {
    @get:Rule val ui = createEmptyComposeRule()
    @Test fun approvedExternalTransitionThenRetainedTab401PreservesProviderCookbook() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val args = InstrumentationRegistry.getArguments()
        val id = requireNotNull(args.getString("passchefGoogle401FixtureId"))
        require(UUID.fromString(id).toString() == id)
        val url = args.getString("passchefGoogleMenuURL").orEmpty()
        require(url.length <= 300 && menuIdFromInput(url) != null)
        val approval = requireNotNull(args.getString("passchefGoogle401ApprovalSha256"))
        require(approval.matches(Regex("[a-f0-9]{64}")))
        val directory = File(instrumentation.targetContext.noBackupFilesDir.canonicalFile, "cookbook-cold-proof-$id")
        require(!directory.exists())
        var scenario: ActivityScenario<ColdStartProofActivity>? = null
        var proof: GoogleHttp401Observation? = null
        var primaryFailure: Throwable? = null
        val aliases = mutableSetOf<String>()
        var phase = "provider-setup"
        fun report(name: String) {
            phase = name
            proof?.report(name) ?: instrumentation.sendStatus(2, Bundle().apply {
                putString("passchefHttp401Phase", name); putString("passchefHttp401FixtureId", id)
                putString("passchefHttp401ApprovalSha256", approval)
            })
        }
        try {
            val launched = ActivityScenario.launch<ColdStartProofActivity>(Intent(instrumentation.targetContext, ColdStartProofActivity::class.java)
                .putExtra("passchefColdFixtureId", id).putExtra("passchefColdStage", "warm")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            scenario = launched
            lateinit var activity: ColdStartProofActivity
            launched.onActivity { activity = it }
            val state = ColdCacheObservation(activity)
            val saved = GoogleColdProviderUi(ui, state, ::report).load(url, "Test")
            val cacheUi = GoogleColdCacheUi(ui, state, ::report)
            val cached = cacheUi.favoriteCook(saved)
            ui.waitForIdle()
            assertTrue("Real provider/cache writes finish before exact full baseline freeze", state.flags()["sourceBusy"] == false)
            val baseline = GoogleColdBaseline.create(activity, cached)
            val observed = GoogleHttp401Observation(state, baseline, approval)
            proof = observed
            assertTrue("Real cached Cook precedes Sources navigation", state.flags()["hasDish"] == true)
            report("provider-baseline-frozen")
            ui.onNode(hasClickAction() and hasText("Sources")).performScrollTo().performClick()
            ColdCacheUiFixture(ui, state).hideIme()
            observed.checkpoint()
            ui.onNodeWithTag("dish-display").assertDoesNotExist(); ui.onNodeWithTag("dish-copy").assertDoesNotExist()
            observed.awaitApprovedAction() // Root publishes marker only after actual separately approved external action.
            report("retained-tab-read-dispatch")
            observed.checkpoint()
            ui.onNode(hasClickAction() and hasText(observed.tab.title)).performScrollTo().performClick()
            val deadline = SystemClock.elapsedRealtime() + 60_000
            while (SystemClock.elapsedRealtime() < deadline && state.flags()["sourceBusy"] == true) {
                observed.assertReadActive(); Thread.sleep(100)
            }
            assertTrue("One actual provider tab read completes within bounded deadline", state.flags()["sourceBusy"] == false)
            ui.waitForIdle()
            val status = observed.completedStatus()
            if (status == "SYNCED") {
                observed.assertSuccessfulRead(); report("not-exercised-successful-provider-refresh")
                assumeTrue("Actual provider read succeeded; old cache bytes may legitimately refresh, no401 credit", false)
            }
            if (status in setOf("ACCESS_DENIED", "NETWORK_FAILED")) {
                observed.assertOther(status); report("not-exercised-other-current-provider-outcome")
                assumeTrue("Current refusal/network outcome is not actual401", false)
            }
            observed.assertUnauthorized()
            ui.onNodeWithText("Connect again after unlocking; Google setup/permission may be needed.").assertExists()
            ui.onNodeWithTag("dish-display").assertDoesNotExist(); ui.onNodeWithTag("dish-copy").assertDoesNotExist()
            report("actual401-preserved-before-new-cook")
            cacheUi.cookAgain(cached.mapping.origin)
            observed.assertFreshCook(); report("actual401-and-fresh-cached-cook-passed")
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
            clean { assertTrue("Only the exact conditional401 fixture is removed", !directory.exists() || directory.deleteRecursively()) }
            clean { assertTrue("Conditional401 UUID removal is verified", !directory.exists()) }
            if (failures.isNotEmpty()) {
                val primary = primaryFailure
                if (primary != null) failures.forEach(primary::addSuppressed)
                else throw failures.first().also { first -> failures.drop(1).forEach(first::addSuppressed) }
            }
        }
    }
}
