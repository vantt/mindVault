package dev.passchef.mobile.proof

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import dev.passchef.mobile.cookbook.*
import org.junit.Assert.assertTrue
import java.io.File

/** Network presence is observed, never assumed from old radio commands or used instead of real API reads. */
fun googleColdNetwork(activity: ColdStartProofActivity): Map<String, Boolean> = runCatching {
    val connectivity = activity.getSystemService(ConnectivityManager::class.java)
    val default = connectivity.activeNetwork
    val bound = connectivity.boundNetworkForProcess
    val defaultCaps = default?.let(connectivity::getNetworkCapabilities)
    val boundCaps = bound?.let(connectivity::getNetworkCapabilities)
    mapOf("networkReadSucceeded" to true, "defaultNetworkPresent" to (default != null), "boundNetworkPresent" to (bound != null),
        "defaultInternet" to (defaultCaps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true),
        "defaultValidated" to (defaultCaps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true),
        "boundInternet" to (boundCaps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true))
}.getOrDefault(mapOf("networkReadSucceeded" to false))

/** New host adapter preserves every original Google argument/UI/provider/mapping/cache predicate. */
class GoogleColdProviderUi(private val ui: ComposeTestRule, private val observation: ColdCacheObservation,
    private val phase: (String) -> Unit) {
    private val activity = observation.activity
    private fun click(text: String) {
        assertTrue("Actual own foreground required before UI action", observation.foreground())
        ui.onNode(hasClickAction() and hasText(text)).performScrollTo().performClick()
    }
    private fun fill(label: String, text: String) {
        assertTrue("Actual own foreground required before input", observation.foreground())
        ui.onNode(hasSetTextAction() and hasText(label)).performScrollTo().performTextReplacement(text)
    }
    private fun tag(name: String): SemanticsNodeInteraction {
        assertTrue("Actual own foreground required before tagged UI action", observation.foreground())
        return ui.onNodeWithTag(name).performScrollTo()
    }
    fun load(url: String, tabTitle: String): MenuSnapshot {
        val menuId = menuIdFromInput(url)
        assertTrue("Valid runtime synthetic Menu argument required", url.length <= 300 && menuId != null && tabTitle.length in 1..200)
        val record = coldJarFile(activity.proofDirectory)
        assertTrue("Proof starts without an encrypted Cookbook", !record.exists())
        assertTrue("Proof starts with no cached Menu", timerMain { observation.book().sources.snapshots.isEmpty() })
        phase("google-warm-owner-setup-biometric")
        ColdCacheUiFixture(ui, observation).setup()
        observation.await { observation.foreground() && timerMain { observation.vault().readyForCooking() && !observation.book().sources.busy } }
        val encryptedJars = record.readBytes()
        var connected = false
        for (attempt in 0..1) {
            phase("google-connect-${attempt + 1}")
            assertTrue("Actual network observation is available before Google Connect", googleColdNetwork(activity)["networkReadSucceeded"] == true)
            click("Sources"); tag("source-id").performTextReplacement(url); click("Connect / load tabs")
            observation.await { observation.foreground() && timerMain {
                val source = observation.book().sources
                !observation.vault().readyForCooking() || (!source.busy &&
                    (source.metadata != null || source.status in setOf("CONNECT_REQUIRED", "ACCESS_DENIED", "NETWORK_FAILED")))
            } }
            connected = timerMain { observation.vault().readyForCooking() && observation.book().sources.metadata != null && !observation.book().sources.busy }
            if (connected) break
            assertTrue("Only one actual background-lock reconnect is allowed", attempt == 0 && observation.foreground() && timerMain { !observation.vault().readyForCooking() })
            phase("google-owner-reunlock")
            click("Open with biometric")
            observation.await { observation.foreground() && timerMain { observation.vault().readyForCooking() && !observation.book().sources.busy } }
            ui.waitForIdle()
        }
        assertTrue("Real usable Google authorization and metadata read required", connected)
        phase("google-metadata-origin")
        val document = timerMain { requireNotNull(observation.book().sources.metadata) }
        assertTrue("Actual metadata matches requested Menu", document.spreadsheetId == menuId)
        phase("google-metadata-tab")
        val tab = document.sheets.singleOrNull { it.properties.title == tabTitle }
        assertTrue("Actual selected tab exists uniquely", tab != null)
        phase("google-values-origin"); click(tabTitle)
        observation.await { observation.foreground() && timerMain { observation.vault().readyForCooking() && !observation.book().sources.busy } }
        val draft = timerMain { requireNotNull(observation.book().sources.draft) }
        assertTrue("Actual tab origin matches provider metadata", draft.origin.menuId == menuId &&
            draft.origin.tabId == requireNotNull(tab).properties.sheetId && draft.origin.title == tabTitle)
        phase("google-values-schema")
        assertTrue("Actual dedicated synthetic header and row remain literal",
            draft.rows.firstOrNull() == listOf("Label", "Recipe", "Username") &&
                draft.rows.getOrNull(1) == listOf("Mail-test", "abcd#1", "00123"))
        phase("google-mapping")
        fill("First data row", "2")
        for ((role, column) in listOf("LABEL" to "1", "RECIPE" to "2", "USERNAME" to "3"))
            tag("column-$role").performTextReplacement(column)
        tag("confirm-columns").performClick()
        observation.await { observation.foreground() && timerMain { observation.vault().readyForCooking() && !observation.book().sources.busy } }
        ui.waitForIdle(); phase("google-cache-committed")
        val saved = timerMain { observation.book().sources.snapshots.single() }
        assertTrue("Confirmed mapping retains actual provider origin and explicit roles", saved.mapping.origin == draft.origin &&
            saved.mapping.firstDataRow == 2 && saved.mapping.columns == mapOf(ColumnRole.LABEL to 0, ColumnRole.RECIPE to 1, ColumnRole.USERNAME to 2))
        val row = saved.entries.single { it.row == 2 }
        assertTrue("Confirmed cache retains literal synthetic row", row.origin == draft.origin &&
            row.label == "Mail-test" && row.recipe == "abcd#1" && row.username == "00123")
        assertTrue("Actual private cache reload matches committed snapshot", MenuSnapshotStore(File(activity.proofDirectory, "menus")).load(draft.origin) == saved)
        assertTrue("Google read/cache preserves encrypted synthetic jars", encryptedJars.contentEquals(record.readBytes()))
        return saved
    }
}
