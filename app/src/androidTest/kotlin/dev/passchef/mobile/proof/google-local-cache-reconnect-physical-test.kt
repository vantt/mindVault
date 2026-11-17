package dev.passchef.mobile.proof

import android.content.Intent
import android.os.Bundle
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.KeystoreCrypto
import dev.passchef.mobile.cookbook.AndroidSheetsAuthorization
import dev.passchef.mobile.cookbook.MenuSnapshotStore
import dev.passchef.mobile.cookbook.menuIdFromInput
import dev.passchef.mobile.vault.AtomicRecordStore
import dev.passchef.mobile.vault.VaultRepository
import java.io.File
import java.util.UUID
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Normal reconnect after a local SDK cache operation; no remote revocation is claimed or required. */
@RunWith(AndroidJUnit4::class)
class GoogleLocalCacheReconnectPhysicalTest {
    @get:Rule val ui = createEmptyComposeRule()
    @Test fun normalSdkReconnectAfterLocalCacheClearReadsOriginalProviderMenu() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val args = InstrumentationRegistry.getArguments()
        require(args.getString("passchefLocalSdkReconnectApproved") == "true" &&
            args.getString("passchefLocalSdkRemoteRevocationPerformed") == "false")
        val approval = requireNotNull(args.getString("passchefLocalCacheApprovalSha256"))
        val operation = requireNotNull(args.getString("passchefLocalSdkOperationSha256"))
        require(listOf(approval, operation).all { it.matches(Regex("[a-f0-9]{64}")) })
        val id = requireNotNull(args.getString("passchefLocalReconnectFixtureId"))
        require(UUID.fromString(id).toString() == id)
        val url = args.getString("passchefGoogleMenuURL").orEmpty()
        require(url.length <= 300 && menuIdFromInput(url) != null)
        val directory = File(instrumentation.targetContext.noBackupFilesDir.canonicalFile, "cookbook-cold-proof-$id")
        require(!directory.exists())
        var scenario: ActivityScenario<ColdStartProofActivity>? = null
        var primaryFailure: Throwable? = null
        val aliases = mutableSetOf<String>()
        var phase = "local-reconnect-preflight"
        fun report(name: String) {
            phase = name
            instrumentation.sendStatus(2, Bundle().apply {
                putString("passchefLocalReconnectPhase", name); putString("passchefLocalReconnectFixtureId", id)
                putString("passchefLocalReconnectApprovalSha256", approval)
                putString("passchefLocalReconnectOperationSha256", operation)
                putBoolean("passchefLocalReconnectRemoteRevocationPerformed", false)
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
            report("normal-sdk-local-reconnect-provider-read-cache-passed")
            // Usable SDK grant/read is proved; fresh consent still needs the owner's separate observation.
        } catch (failure: Throwable) {
            primaryFailure = failure
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
