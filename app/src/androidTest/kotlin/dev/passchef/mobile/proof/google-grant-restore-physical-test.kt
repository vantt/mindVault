package dev.passchef.mobile.proof

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.KeystoreCrypto
import dev.passchef.mobile.cookbook.AndroidSheetsAuthorization
import dev.passchef.mobile.cookbook.CookbookSources
import dev.passchef.mobile.cookbook.MenuSnapshotStore
import dev.passchef.mobile.cookbook.SourceOwnership
import dev.passchef.mobile.cookbook.menuIdFromInput
import dev.passchef.mobile.vault.AtomicRecordStore
import dev.passchef.mobile.vault.VaultRepository
import java.io.File
import java.util.UUID
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Separately approved restoration only: genuine existing-scope SDK authorization and real Menu reads. */
@RunWith(AndroidJUnit4::class)
class GoogleGrantRestorePhysicalTest {
    @get:Rule val ui = createEmptyComposeRule()
    @Test fun approvedGrantRestorationReadsAndCachesOriginalProviderMenu() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val args = InstrumentationRegistry.getArguments()
        require(args.getString("passchefGoogleRestoreApproved") == "true" &&
            args.getString("passchefGoogleRevocationPerformed") == "true")
        val approval = requireNotNull(args.getString("passchefGoogleRestoreApprovalSha256"))
        val action = requireNotNull(args.getString("passchefGoogleRestoreActionSha256"))
        require(listOf(approval, action).all { it.matches(Regex("[a-f0-9]{64}")) })
        val id = requireNotNull(args.getString("passchefGoogleRestoreFixtureId"))
        require(UUID.fromString(id).toString() == id)
        val url = args.getString("passchefGoogleMenuURL").orEmpty()
        require(url.length <= 300 && menuIdFromInput(url) != null)
        val directory = File(instrumentation.targetContext.noBackupFilesDir.canonicalFile, "cookbook-cold-proof-$id")
        require(!directory.exists())
        var scenario: ActivityScenario<ColdStartProofActivity>? = null
        var diagnosticState: ColdCacheObservation? = null
        var primaryFailure: Throwable? = null
        val aliases = mutableSetOf<String>()
        var phase = "restore-preflight"
        fun report(name: String) {
            phase = name
            instrumentation.sendStatus(2, Bundle().apply {
                putString("passchefGoogleRestorePhase", name); putString("passchefGoogleRestoreFixtureId", id)
                putString("passchefGoogleRestoreApprovalSha256", approval)
                putString("passchefGoogleRestoreActionSha256", action)
            })
        }
        try {
            report(phase)
            val launched = ActivityScenario.launch<ColdStartProofActivity>(Intent(instrumentation.targetContext, ColdStartProofActivity::class.java)
                .putExtra("passchefColdFixtureId", id).putExtra("passchefColdStage", "warm")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            scenario = launched
            lateinit var activity: ColdStartProofActivity
            launched.onActivity { activity = it }
            val state = ColdCacheObservation(activity)
            diagnosticState = state
            // Existing helper uses genuine setup/biometrics, normal Connect and at most one natural-lock reconnect.
            // Production authorization keeps its existing Sheets scope; no account selection or grant is injected.
            val saved = GoogleColdProviderUi(ui, state, ::report).load(url, "Test")
            state.requireReady()
            val flags = state.flags()
            assertTrue("Real restoration retains usable authorization and provider metadata after mapping", flags["tokenAbsent"] == false &&
                flags["metadataAbsent"] == false && flags["sourceBusy"] == false && flags["draftAbsent"] == true)
            assertTrue("Restored metadata/tab/cache are bound to the original permitted provider Menu", timerMain {
                val source = state.book().sources
                source.metadata?.spreadsheetId == saved.mapping.origin.menuId &&
                    source.metadata?.sheets?.singleOrNull { it.properties.title == "Test" }?.properties?.sheetId == saved.mapping.origin.tabId &&
                    source.snapshots == listOf(saved) && AndroidSheetsAuthorization::class.java.getDeclaredField("pending")
                        .apply { isAccessible = true }.get(state.book().authorization) == null
            })
            val inventory = MenuSnapshotStore(File(directory, "menus")).inventory()
            assertTrue("Actual restored provider cache reloads exactly without corruption", inventory.corruptFiles == 0 && inventory.snapshots == listOf(saved))
            report("approved-restoration-provider-read-cache-passed")
            // Usable SDK grant/read is proved; fresh consent still needs the owner's separate observation.
        } catch (failure: Throwable) {
            primaryFailure = failure
            runCatching {
                val state = diagnosticState
                val flags = state?.let { runCatching(it::flags).getOrNull() }
                val status = state?.let { runCatching { timerMain { it.book().sources.status } }.getOrNull() }
                val ownership = state?.let { runCatching { timerMain {
                    val owner = CookbookSources::class.java.getDeclaredField("ownership").apply { isAccessible = true }
                        .get(it.book().sources) as SourceOwnership
                    val context = SourceOwnership::class.java.getDeclaredField("source").apply { isAccessible = true }.get(owner)
                    Triple(SourceOwnership::class.java.getDeclaredField("generation").apply { isAccessible = true }.getLong(owner),
                        context == null, context == menuIdFromInput(url))
                } }.getOrNull() }
                fun localClearState(): Pair<Boolean, Boolean>? = state?.let { runCatching { timerMain {
                    val gate = AndroidSheetsAuthorization::class.java.getDeclaredField("rejectedTokens").apply { isAccessible = true }
                        .get(it.book().authorization)
                    Pair((gate.javaClass.getDeclaredField("rejected").apply { isAccessible = true }.get(gate) as Collection<*>).isEmpty(),
                        gate.javaClass.getDeclaredField("attempt").apply { isAccessible = true }.get(gate) != null)
                } }.getOrNull() }
                var localClear = localClearState()
                val clearDeadline = SystemClock.elapsedRealtime() + 30_000
                while (localClear?.second == true && SystemClock.elapsedRealtime() < clearDeadline) {
                    Thread.sleep(50); localClear = localClearState()
                }
                val known = setOf("OFFLINE", "LOADING", "CHOOSE_TAB", "CONFIRM", "RECONFIRM", "SYNCED",
                    "CONNECT_REQUIRED", "ACCESS_DENIED", "NETWORK_FAILED", "CACHE_FAILED", "INVALID_MAPPING", "INVALID_TABLE", "CORRUPT_CACHE")
                instrumentation.sendStatus(2, Bundle().apply {
                    putString("passchefRestoreDiagnosticPhase", phase); putString("passchefRestoreDiagnosticFixtureId", id)
                    putBoolean("passchefRestoreDiagnosticStateReadSucceeded", flags != null)
                    putBoolean("passchefRestoreDiagnosticStatusReadSucceeded", status != null)
                    putBoolean("passchefRestoreDiagnosticOwnershipReadSucceeded", ownership != null)
                    putLong("passchefRestoreDiagnosticSourceGeneration", ownership?.first ?: -1L)
                    putBoolean("passchefRestoreDiagnosticContextAbsent", ownership?.second == true)
                    putBoolean("passchefRestoreDiagnosticContextMatchesRequestedMenu", ownership?.third == true)
                    putBoolean("passchefRestoreDiagnosticLocalClearReadSucceeded", localClear != null)
                    putBoolean("passchefRestoreDiagnosticRejectedTokensAbsent", localClear?.first == true)
                    putBoolean("passchefRestoreDiagnosticLocalClearInFlight", localClear?.second == true)
                    putString("passchefRestoreDiagnosticStatus", status?.takeIf { it in known } ?: "UNAVAILABLE")
                    putBoolean("passchefRestoreDiagnosticReady", flags?.get("ready") == true)
                    putBoolean("passchefRestoreDiagnosticTokenAbsent", flags?.get("tokenAbsent") == true)
                    putBoolean("passchefRestoreDiagnosticSourceBusy", flags?.get("sourceBusy") == true)
                    putBoolean("passchefRestoreDiagnosticMetadataAbsent", flags?.get("metadataAbsent") == true)
                    putBoolean("passchefRestoreDiagnosticForeground", listOf("resumed", "awake", "keyguardAbsent", "focused", "secure")
                        .all { flags?.get(it) == true })
                })
            }
            runCatching { report("$phase-terminal-${failure.javaClass.simpleName}") }
            throw failure
        } finally {
            val failures = mutableListOf<Throwable>()
            fun clean(work: () -> Unit) { try { work() } catch (failure: Throwable) { failures += failure } }
            clean { scenario?.close() }
            clean { VaultRepository(AtomicRecordStore(File(directory, "cookbook-v1.json"))).record()?.let {
                aliases += (listOf(it.alias) + it.retiredAliases).filter(String::isNotEmpty)
            } }
            aliases.forEach { alias -> clean { KeystoreCrypto().deleteKey(alias) } }
            clean { assertTrue("Only the exact restoration UUID is removed", !directory.exists() || directory.deleteRecursively()) }
            clean { assertTrue("Restoration UUID removal is verified", !directory.exists()) }
            if (failures.isNotEmpty()) {
                val primary = primaryFailure
                if (primary != null) failures.forEach(primary::addSuppressed)
                else throw failures.first().also { first -> failures.drop(1).forEach(first::addSuppressed) }
            }
        }
    }
}
