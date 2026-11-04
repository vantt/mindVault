package dev.passchef.mobile.proof

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.passchef.mobile.KeystoreCrypto
import dev.passchef.mobile.vault.AtomicRecordStore
import dev.passchef.mobile.vault.VaultRepository
import java.io.File
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.AssumptionViolatedException
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Select one owner-held case. JUnit never certifies speech quality or focus order. */
@RunWith(AndroidJUnit4::class)
class TalkBackOwnerPhysicalTest {
    @get:Rule val ui = createAndroidComposeRule<CookbookProofActivity>()
    private fun english() {
        if (ui.onAllNodesWithText("EN").fetchSemanticsNodes().isNotEmpty()) ui.onNodeWithText("EN").performClick()
    }
    private fun locked(activity: CookbookProofActivity): Boolean = timerMain {
        val state = CookbookUiDiagnostics.stateFlags(activity)
        !File(activity.proofDirectory, "cookbook-v1.json").exists() &&
            listOf("vaultUnlocked", "vaultReady", "vaultBusy", "hasDish", "revealed", "dishNonempty")
                .all { state[it] == false }
    }
    private fun rememberAliases(directory: File, aliases: MutableSet<String>) {
        VaultRepository(AtomicRecordStore(File(directory, "cookbook-v1.json"))).record()?.let { record ->
            aliases += (listOf(record.alias) + record.retiredAliases).filter(String::isNotEmpty)
        }
    }
    private fun owned(work: (CookbookProofActivity, File, MutableSet<String>) -> Unit) {
        val activity = ui.activity
        val directory = activity.proofDirectory
        val aliases = mutableSetOf<String>()
        var primaryFailure: Throwable? = null
        try { work(activity, directory, aliases) }
        catch (failure: Throwable) { primaryFailure = failure; throw failure }
        finally {
            val failures = mutableListOf<Throwable>()
            fun clean(action: () -> Unit) { try { action() } catch (failure: Throwable) { failures += failure } }
            clean { ui.activityRule.scenario.close() }
            clean { rememberAliases(directory, aliases) }
            aliases.forEach { alias -> clean { KeystoreCrypto().deleteKey(alias) } }
            clean {
                assertTrue("Only the exact owned TalkBack fixture is removed", !directory.exists() || directory.deleteRecursively())
                assertTrue("Owned TalkBack fixture removal is verified", !directory.exists())
            }
            if (failures.isNotEmpty()) {
                val primary = primaryFailure
                if (primary != null) failures.forEach(primary::addSuppressed)
                else throw failures.first().also { first -> failures.drop(1).forEach(first::addSuppressed) }
            }
        }
    }
    @Test fun ownerObservesLockedEnglishVietnameseTalkBack() = owned { activity, _, _ ->
        var checkpoint: TalkBackOwnerCheckpoint? = null
        try {
            val current = TalkBackOwnerCheckpoint(activity)
            checkpoint = current
            current.preflight(); ui.waitForIdle(); english()
            for (vietnamese in listOf(false, true)) {
                if (vietnamese) ui.onNodeWithText("VI").performClick()
                val label = if (vietnamese) "Mật khẩu chính" else "Master password"
                ui.onNode(hasSetTextAction() and hasText(label)).performScrollTo().assertIsDisplayed()
                    .assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.Password))
                ui.waitForIdle()
                assumeTrue("Owner declined this bilingual spoken/focus observation", current.awaitOwner(
                    if (vietnamese) "locked-vi" else "locked-en") { locked(activity) })
            }
            assertTrue("Locked spoken checkpoint creates no encrypted Cookbook", locked(activity))
            current.report("locked-bilingual-native-checkpoints-complete", "locked-vi", 0)
        } catch (skipped: AssumptionViolatedException) {
            throw skipped
        } catch (failure: Throwable) {
            runCatching { checkpoint?.let { it.report("${it.phase}-failed", "locked", 0) } }
            throw failure
        }
    }
    @Test fun ownerObservesGenuinelyAuthenticatedMaskedDishTalkBack() = owned { activity, directory, aliases ->
        val checkpoint = TalkBackOwnerCheckpoint(activity)
        checkpoint.preflight()
        val proof = CookbookTimerFixture(ui)
        var observed = false
        proof.run("talkback-owner") {
            rememberAliases(directory, aliases) // Retain owned aliases even if the legacy fixture removes its file.
            val jars = File(directory, "cookbook-v1.json")
            val original = jars.readBytes()
            hideTimerIme(activity, proof::environment)
            ui.onNodeWithTag("dish-display").performScrollTo().assertIsDisplayed().assertTextEquals("••••••••")
            ui.onNodeWithTag("dish-reveal").assertIsEnabled()
            ui.onNodeWithTag("dish-copy").assertIsEnabled()
            ui.waitForIdle()
            observed = checkpoint.awaitOwner("authenticated-masked") {
                ui.waitForIdle()
                val flags = manualVisualSnapshot(ui, activity) {
                    CookbookUiDiagnostics.environmentFlags(activity) + CookbookUiDiagnostics.stateFlags(activity)
                }
                original.contentEquals(jars.readBytes()) &&
                    listOf("vaultUnlocked", "vaultReady", "hasDish", "visualDishUnique", "visualTextMatchesMask")
                        .all { flags[it] == true } && flags["revealed"] == false && flags["dishNonempty"] == false
            }
            assertTrue("Native owner checkpoint preserves exact encrypted jars", original.contentEquals(jars.readBytes()))
        }
        // The existing fixture wraps failures, so classify an explicit owner decline after its cleanup.
        assumeTrue("Owner declined this masked-Dish spoken/focus observation", observed)
    }
}
