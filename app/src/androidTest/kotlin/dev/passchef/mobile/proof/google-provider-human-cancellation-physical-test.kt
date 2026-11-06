package dev.passchef.mobile.proof

import android.content.Intent
import android.os.Bundle
import android.os.Process
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

/** One separately approved owner transition; all provider/cache/UI work follows normal production paths. */
@RunWith(AndroidJUnit4::class)
class GoogleProviderHumanCancellationPhysicalTest {
    @get:Rule val ui = createEmptyComposeRule()
    @Test fun approvedRevocationThenActualHumanCancellationPreservesProviderLoadedCookbook() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val args = InstrumentationRegistry.getArguments()
        val id = requireNotNull(args.getString("passchefProviderCancelFixtureId"))
        require(UUID.fromString(id).toString() == id)
        val url = args.getString("passchefGoogleMenuURL").orEmpty()
        require(url.length <= 300 && menuIdFromInput(url) != null)
        val approval = requireNotNull(args.getString("passchefProviderCancelApprovalSha256"))
        require(approval.matches(Regex("[a-f0-9]{64}")))
        val directory = File(instrumentation.targetContext.noBackupFilesDir.canonicalFile, "cookbook-cold-proof-$id")
        require(!directory.exists())
        var scenario: ActivityScenario<ColdStartProofActivity>? = null
        var retained: GoogleHttp401Observation? = null
        var cancellation: GoogleProviderCancellationObservation? = null
        var primary: Throwable? = null
        val aliases = mutableSetOf<String>()
        var phase = "provider-setup"
        var taskId: Int? = null
        fun report(name: String) {
            phase = name
            cancellation?.report(name) ?: instrumentation.sendStatus(2, Bundle().apply {
                putString("passchefProviderCancelPhase", name); putString("passchefProviderCancelFixtureId", id)
                putString("passchefProviderCancelApprovalSha256", approval); putInt("passchefProviderCancelPid", Process.myPid())
                taskId?.let { putInt("passchefProviderCancelTaskId", it) }
            })
        }
        try {
            val launched = ActivityScenario.launch<ColdStartProofActivity>(Intent(instrumentation.targetContext, ColdStartProofActivity::class.java)
                .putExtra("passchefColdFixtureId", id).putExtra("passchefColdStage", "warm")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            scenario = launched
            lateinit var activity: ColdStartProofActivity
            launched.onActivity { activity = it; taskId = it.taskId }
            val state = ColdCacheObservation(activity)
            assertTrue("No callback witness observes initial setup/provider authorization", activity.googleResultProof == null)
            val saved = GoogleColdProviderUi(ui, state, ::report).load(url, "Test")
            val cached = GoogleColdCacheUi(ui, state, ::report).favoriteCook(saved)
            ui.waitForIdle()
            assertTrue("Real provider cache/favorite/recent writes complete before freezing A", state.flags()["sourceBusy"] == false)
            val baseline = GoogleColdBaseline.create(activity, cached)
            val observed = GoogleHttp401Observation(state, baseline, approval)
            retained = observed
            report("actual-provider-A-baseline-frozen")
            ui.onNode(hasClickAction() and hasText("Sources")).performScrollTo().performClick()
            ColdCacheUiFixture(ui, state).hideIme()
            observed.checkpoint()
            observed.awaitApprovedAction() // Existing bounded marker; root alone publishes after actual owner revocation.
            report("approved-action-observed-retained-Test-read")
            observed.checkpoint()
            ui.onNode(hasClickAction() and hasText(observed.tab.title)).performScrollTo().performClick()
            val deadline = SystemClock.elapsedRealtime() + 60_000
            while (SystemClock.elapsedRealtime() < deadline && state.flags()["sourceBusy"] == true) {
                observed.assertReadActive(); Thread.sleep(100)
            }
            assertTrue("One real retained Test-tab read finishes within bounded wait", state.flags()["sourceBusy"] == false)
            ui.waitForIdle()
            when (val status = observed.completedStatus()) {
                "SYNCED" -> {
                    observed.assertSuccessfulRead(); report("not-exercised-provider-refresh-not401")
                    assumeTrue("Successful refresh is not401 and cancellation prelude was not exercised", false)
                }
                "ACCESS_DENIED", "NETWORK_FAILED" -> {
                    observed.assertOther(status); report("not-exercised-other-provider-outcome-not401")
                    assumeTrue("Actual403/network outcome is not401 and cancellation prelude was not exercised", false)
                }
                else -> observed.assertUnauthorized()
            }
            report("actual401-provider-A-preserved-before-cancellation")
            observed.close(); retained = null
            val marker = providerCancelControl(File(directory, "google401-approved-action.json"))
            val action = marker.getString("actionRecordSha256")
            require(marker.keys().asSequence().toSet() == setOf("completed", "fixtureId", "approvalRecordSha256", "actionRecordSha256") &&
                marker.get("completed") == true && marker.getString("fixtureId") == id && marker.getString("approvalRecordSha256") == approval &&
                action.matches(Regex("[a-f0-9]{64}")))
            val cancel = GoogleProviderCancellationObservation(state, baseline, approval, action)
            cancellation = cancel
            val witness = cancel.armWitness()
            state.requireReady(); cancel.assertPreserved()
            ui.onNodeWithTag("source-id").performScrollTo().performTextReplacement(url)
            ColdCacheUiFixture(ui, state).hideIme()
            cancel.report("normal-connect-dispatch-after-real401", witness)
            state.requireReady()
            ui.onNode(hasClickAction() and hasText("Connect / load tabs")).performScrollTo().performClick()
            GoogleProviderCancellationRoute(ui, cancel, approval, witness).run()
        } catch (failure: Throwable) {
            primary = failure
            runCatching { report("${cancellation?.phase ?: phase}-terminal-${failure.javaClass.simpleName}") }
            throw failure
        } finally {
            val failures = mutableListOf<Throwable>()
            fun clean(action: () -> Unit) { try { action() } catch (failure: Throwable) { failures += failure } }
            clean { retained?.close() }; clean { cancellation?.close() }; clean { scenario?.close() }
            clean { VaultRepository(AtomicRecordStore(File(directory, "cookbook-v1.json"))).record()?.let {
                aliases += (listOf(it.alias) + it.retiredAliases).filter(String::isNotEmpty)
            } }
            aliases.forEach { alias -> clean { KeystoreCrypto().deleteKey(alias) } }
            clean { assertTrue("Remove only this canonical fresh provider-cancellation fixture", !directory.exists() || directory.deleteRecursively()) }
            clean { assertTrue("Selected provider-cancellation UUID removal is verified", !directory.exists()) }
            if (failures.isNotEmpty()) {
                val first = primary
                if (first != null) failures.forEach(first::addSuppressed)
                else throw failures.first().also { result -> failures.drop(1).forEach(result::addSuppressed) }
            }
        }
    }
}
