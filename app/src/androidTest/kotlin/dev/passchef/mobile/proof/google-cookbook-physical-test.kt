package dev.passchef.mobile.proof

import android.os.Bundle
import android.os.SystemClock
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.KeystoreCrypto
import dev.passchef.mobile.MainActivity
import dev.passchef.mobile.cookbook.*
import dev.passchef.mobile.vault.AtomicRecordStore
import dev.passchef.mobile.vault.VaultRepository
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/** Owner-authorized live Google read only; compiling never grants permission or proves a read. */
@RunWith(AndroidJUnit4::class)
class GoogleCookbookPhysicalTest {
    @get:Rule val ui = createAndroidComposeRule<CookbookProofActivity>()
    private var phase = "arguments"
    private val expectedFlags = linkedMapOf<String, Boolean>()
    private fun <T> main(action: (CookbookProofActivity) -> T): T {
        var outcome: Result<T>? = null
        InstrumentationRegistry.getInstrumentation().runOnMainSync { outcome = runCatching { action(ui.activity) } }
        return requireNotNull(outcome).getOrThrow()
    }
    private fun sources(activity: CookbookProofActivity): CookbookSources =
        (MainActivity::class.java.getDeclaredField("cookbook").apply { isAccessible = true }.get(activity) as CookbookController).sources
    private fun ready(activity: CookbookProofActivity) = CookbookUiDiagnostics.stateFlags(activity)["vaultReady"] == true
    private fun ownUi(activity: CookbookProofActivity): Boolean {
        val flags = CookbookUiDiagnostics.environmentFlags(activity)
        return activity.lifecycle.currentState == Lifecycle.State.RESUMED && flags["powerInteractive"] == true &&
            flags["keyguardLocked"] == false && flags["deviceLocked"] == false && flags["proofWindowFocused"] == true &&
            flags["proofWindowShown"] == true && flags["proofWindowAttached"] == true
    }
    private fun await(condition: () -> Boolean) {
        val deadline = SystemClock.elapsedRealtime() + 180_000
        while (!condition() && SystemClock.elapsedRealtime() < deadline) Thread.sleep(100)
        assertTrue("Bounded real Google proof phase became ready", condition())
    }
    private fun click(text: String) {
        assertTrue("Actual own foreground required before UI action", main(::ownUi))
        ui.onNode(hasClickAction() and hasText(text)).performScrollTo().performClick()
    }
    private fun fill(label: String, text: String) {
        assertTrue("Actual own foreground required before input", main(::ownUi))
        ui.onNode(hasSetTextAction() and hasText(label)).performScrollTo().performTextReplacement(text)
    }
    private fun tag(name: String): SemanticsNodeInteraction {
        assertTrue("Actual own foreground required before tagged UI action", main(::ownUi))
        return ui.onNodeWithTag(name).performScrollTo()
    }
    private fun report(passed: Boolean, failure: Throwable? = null) {
        val flags = runCatching { main { activity ->
            val source = sources(activity)
            mapOf("diagnosticReadSucceeded" to true, "ownForeground" to ownUi(activity), "sessionReady" to ready(activity),
                "busy" to source.busy, "metadataPresent" to (source.metadata != null), "draftPresent" to (source.draft != null),
                "cachePresent" to source.snapshots.isNotEmpty(), "connectRequired" to (source.status == "CONNECT_REQUIRED"),
                "accessDenied" to (source.status == "ACCESS_DENIED"), "networkFailed" to (source.status == "NETWORK_FAILED"))
        } }.getOrElse { mapOf("diagnosticReadSucceeded" to false) }
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefGooglePhase", phase); putBoolean("passchefGooglePassed", passed)
            failure?.let { putString("passchefGoogleFailureClass", it.javaClass.name) }
            flags.forEach { (name, value) -> putBoolean("passchefGoogle_$name", value) }
            expectedFlags.forEach { (name, value) -> putBoolean("passchefGoogle_$name", value) }
        })
    }

    @Test fun realGoogleAuthorizationMetadataTabRowsAndConfirmedCache() {
        var directory: File? = null
        var recordFile: File? = null
        try {
            directory = main { it.proofDirectory }; recordFile = File(requireNotNull(directory), "cookbook-v1.json")
            val args = InstrumentationRegistry.getArguments()
            val url = args.getString("passchefGoogleMenuURL").orEmpty()
            val tabTitle = args.getString("passchefGoogleTab").orEmpty().ifEmpty { "Test" }
            val menuId = menuIdFromInput(url)
            assertTrue("Valid runtime synthetic Menu argument required", url.length <= 300 && menuId != null && tabTitle.length in 1..200)
            assertTrue("Proof starts without an encrypted Cookbook", !recordFile.exists())
            assertTrue("Proof starts with no cached Menu", main { sources(it).snapshots.isEmpty() })
            phase = "setup"; await { main(::ownUi) }
            if (ui.onAllNodesWithText("EN").fetchSemanticsNodes().isNotEmpty()) click("EN")
            fill("Master password", "SYNTHETIC_GOOGLE_Kitchen_2026")
            fill("Spice jar 1 (setup only)", "aB!*")
            click("Set up / recover with password") // Owner authorizes the actual per-operation biometric.
            await { main { ownUi(it) && ready(it) && !sources(it).busy } }
            val encryptedJars = recordFile.readBytes()
            var connected = false
            for (attempt in 0..1) {
                phase = "connect-${attempt + 1}"
                click("Sources")
                tag("source-id").performTextReplacement(url)
                click("Connect / load tabs") // Real AuthorizationClient and Android Sheets engine only.
                await { main { activity ->
                    val source = sources(activity)
                    ownUi(activity) && (!ready(activity) || (!source.busy &&
                        (source.metadata != null || source.status in setOf("CONNECT_REQUIRED", "ACCESS_DENIED", "NETWORK_FAILED"))))
                } }
                connected = main { ready(it) && sources(it).metadata != null && !sources(it).busy }
                if (connected) break
                assertTrue("Only one actual background-lock reconnect is allowed", attempt == 0 && main { ownUi(it) && !ready(it) })
                phase = "owner-reunlock"
                click("Open with biometric") // Normal UI; owner must authorize, never a synthetic session.
                await { main { ownUi(it) && ready(it) && !sources(it).busy } }
            }
            assertTrue("Real usable Google authorization and metadata read required", connected)
            phase = "metadata-origin"
            val document = main { requireNotNull(sources(it).metadata) }
            expectedFlags["metadataMatchesExpected"] = document.spreadsheetId == menuId
            assertTrue("Actual metadata matches requested Menu", document.spreadsheetId == menuId)
            phase = "metadata-tab"
            val tab = document.sheets.singleOrNull { it.properties.title == tabTitle }
            expectedFlags["selectedTabUnique"] = tab != null
            assertTrue("Actual selected tab exists uniquely", tab != null)
            phase = "tab-click"
            click(tabTitle)
            phase = "values-origin"
            await { main { ownUi(it) && ready(it) && !sources(it).busy } }
            val draft = main { requireNotNull(sources(it).draft) }
            expectedFlags["draftOriginMatches"] = draft.origin.menuId == menuId &&
                draft.origin.tabId == requireNotNull(tab).properties.sheetId && draft.origin.title == tabTitle
            assertTrue("Actual tab origin matches provider metadata", draft.origin.menuId == menuId &&
                draft.origin.tabId == requireNotNull(tab).properties.sheetId && draft.origin.title == tabTitle)
            phase = "values-schema"
            val header = draft.rows.firstOrNull().orEmpty()
            val data = draft.rows.getOrNull(1).orEmpty()
            expectedFlags.putAll(mapOf(
                "headerWidthMatches" to (header.size == 3), "headerLabelMatches" to (header.getOrNull(0) == "Label"),
                "headerRecipeMatches" to (header.getOrNull(1) == "Recipe"), "headerUsernameMatches" to (header.getOrNull(2) == "Username"),
                "dataWidthMatches" to (data.size == 3), "dataLabelMatches" to (data.getOrNull(0) == "Mail-test"),
                "dataRecipeMatches" to (data.getOrNull(1) == "abcd#1"), "dataUsernameMatches" to (data.getOrNull(2) == "00123")))
            assertTrue("Actual dedicated synthetic header and row remain literal",
                draft.rows.firstOrNull() == listOf("Label", "Recipe", "Username") &&
                    draft.rows.getOrNull(1) == listOf("Mail-test", "abcd#1", "00123"))
            phase = "mapping"
            fill("First data row", "2")
            for ((role, column) in listOf("LABEL" to "1", "RECIPE" to "2", "USERNAME" to "3"))
                tag("column-$role").performTextReplacement(column)
            tag("confirm-columns").performClick()
            await { main { ownUi(it) && ready(it) && !sources(it).busy } }
            phase = "cache"
            val saved = main { sources(it).snapshots.single() }
            assertTrue("Confirmed mapping retains actual provider origin and explicit roles", saved.mapping.origin == draft.origin &&
                saved.mapping.firstDataRow == 2 && saved.mapping.columns == mapOf(ColumnRole.LABEL to 0, ColumnRole.RECIPE to 1, ColumnRole.USERNAME to 2))
            val row = saved.entries.single { it.row == 2 }
            assertTrue("Confirmed cache retains literal synthetic row", row.origin == draft.origin &&
                row.label == "Mail-test" && row.recipe == "abcd#1" && row.username == "00123")
            assertTrue("Actual private cache reload matches committed snapshot", MenuSnapshotStore(File(directory, "menus")).load(draft.origin) == saved)
            assertTrue("Google read/cache preserves encrypted synthetic jars", encryptedJars.contentEquals(recordFile.readBytes()))
            if (args.getString("passchefGoogleNetworkContinuation") == "true")
                GoogleNetworkUiContinuation(ui) { phase = it }.run(requireNotNull(directory), saved, encryptedJars)
            else if (args.getString("passchefGoogleCacheContinuation") == "true")
                GoogleCacheUiContinuation(ui) { phase = it }.run(requireNotNull(directory), saved, encryptedJars)
            report(true)
        } catch (failure: Throwable) {
            runCatching { report(false, failure) }
            // Preserve a real failed verdict without printing a Compose tree, Intent, URL or underlying message/cause.
            throw AssertionError("Google physical acceptance failed at $phase; sanitized flags recorded")
        } finally {
            runCatching { ui.activityRule.scenario.close() }
            runCatching {
                recordFile?.let { file ->
                    VaultRepository(AtomicRecordStore(file)).record()?.let { record ->
                        val crypto = KeystoreCrypto()
                        (listOf(record.alias) + record.retiredAliases).filter { it.isNotEmpty() }.forEach(crypto::deleteKey)
                    }
                }
            }
            directory?.deleteRecursively()
        }
    }
}
