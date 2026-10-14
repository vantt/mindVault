package dev.passchef.mobile.proof

import android.view.inputmethod.InputMethodManager
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.passchef.mobile.cookbook.MenuSnapshotStore
import dev.passchef.mobile.vault.AtomicRecordStore
import dev.passchef.mobile.vault.VaultRepository
import org.junit.Assert.assertTrue
import java.io.File

/** All data/session creation uses real production Compose actions and owner authentication. */
class ColdCacheUiFixture(private val ui: ComposeTestRule, val observation: ColdCacheObservation) {
    private val activity = observation.activity
    private fun click(label: String) {
        assertTrue("Actual own foreground required for UI action", observation.foreground())
        ui.onNode(hasClickAction() and hasText(label)).performScrollTo().performClick()
    }
    private fun fill(label: String, value: String) = ui.onNode(hasSetTextAction() and hasText(label))
        .performScrollTo().performTextReplacement(value)
    fun english() {
        if (ui.onAllNodesWithText("EN").fetchSemanticsNodes().isNotEmpty()) ui.onNodeWithText("EN").performClick()
    }
    fun setup() {
        observation.await(observation::foreground); ui.waitForIdle(); english()
        assertTrue("Warm unique fixture starts without encrypted jars", !coldJarFile(activity.proofDirectory).exists())
        fill("Master password", "SYNTHETIC_COLD_Kitchen_2026")
        fill("Spice jar 1 (setup only)", "aB!*")
        observation.report("warm-owner-setup-biometric")
        click("Set up / recover with password")
        observation.await { observation.foreground() && observation.flags()["ready"] == true && observation.flags()["busy"] == false }
        ui.waitForIdle()
        assertTrue("Genuine strong-biometric setup completed, no password fallback substituted", timerMain {
            observation.vault().status.startsWith("Round trip ready;")
        } && VaultRepository(AtomicRecordStore(coldJarFile(activity.proofDirectory))).record()?.alias?.isNotEmpty() == true)
    }
    fun prepareCache() {
        observation.requireReady(); click("Sources")
        observation.await { ui.onNodeWithTag("source-id").fetchSemanticsNode().config.contains(androidx.compose.ui.semantics.SemanticsProperties.Disabled).not() }
        ui.onNodeWithTag("source-id").performScrollTo().performTextReplacement(coldOrigin.menuId)
        fill("Local tab label", coldOrigin.title)
        ui.onNodeWithTag("source-table").performScrollTo().performTextReplacement("Label\tRecipe\tUsername\nMail-cold\tabcd#1\t00123")
        click("Preview columns")
        observation.await { ui.onAllNodesWithTag("confirm-columns").fetchSemanticsNodes().isNotEmpty() }
        fill("First data row", "2")
        ui.onNodeWithTag("column-RECIPE").performScrollTo().performTextReplacement("2")
        ui.onNodeWithTag("column-LABEL").performScrollTo().performTextReplacement("1")
        ui.onNodeWithTag("column-USERNAME").performScrollTo().performTextReplacement("3")
        ui.onNodeWithTag("confirm-columns").performScrollTo().performClick()
        observation.await { timerMain { !observation.book().sources.busy && observation.book().sources.snapshots.size == 1 } }
        ui.waitForIdle(); click("Menu"); click("Favorite")
        observation.await { timerMain { !observation.book().sources.busy } }
        cachedCook()
    }
    fun hideIme() {
        timerMain { activity.getSystemService(InputMethodManager::class.java)
            .hideSoftInputFromWindow(activity.window.decorView.windowToken, 0) }
        observation.await { observation.foreground() && timerMain {
            ViewCompat.getRootWindowInsets(activity.window.decorView)?.isVisible(WindowInsetsCompat.Type.ime()) == false
        } }
        ui.waitForIdle()
    }
    fun cachedCook() {
        observation.requireReady(); click(coldEntry.label)
        observation.await { observation.foreground() && timerMain {
            !observation.book().sources.busy && observation.book().hasDish()
        } }
        ui.waitForIdle()
        assertTrue("Actual cached Cook retains the literal synthetic row/origin", timerMain { observation.book().selected == coldEntry })
        ui.onNodeWithText(coldEntry.username).assertExists()
        ui.onNodeWithText(coldEntry.recipe).assertExists()
        ui.onNodeWithText(coldOrigin.title + " · 2").assertExists()
        ui.onNodeWithTag("dish-display").performScrollTo().assertTextEquals("••••••••")
        ui.onNodeWithTag("dish-copy").assertIsEnabled()
        hideIme()
        ui.onNodeWithTag("dish-reveal").performScrollTo().performClick()
        ui.onNodeWithTag("dish-display").assertTextEquals("aB!*abcd")
        assertTrue("Exact cached synthetic Dish is genuinely ready/revealed", observation.foreground() && timerMain {
            observation.book().hasDish() && observation.book().revealed && observation.book().dish == "aB!*abcd"
        })
    }
    fun snapshot() = timerMain { observation.book().sources.snapshots.single() }.also { saved ->
        assertTrue("Exact owned cache disk equals actual production snapshot",
            MenuSnapshotStore(File(activity.proofDirectory, "menus")).load(coldOrigin) == saved)
    }
    fun reopen(baseline: ColdCacheBaseline) {
        english(); ui.onNodeWithTag("dish-display").assertDoesNotExist(); ui.onNodeWithTag("dish-copy").assertDoesNotExist()
        observation.report("cold-owner-reopen-biometric", baseline)
        click("Open with biometric")
        observation.await { observation.foreground() && observation.flags()["ready"] == true }
        ui.waitForIdle() // Normal unlock LaunchedEffect starts durable inventory loading.
        observation.await { observation.foreground() && timerMain {
            val source = observation.book().sources
            !source.busy && source.snapshots == listOf(coldExpected(baseline.refreshedAt)) && source.status == "OFFLINE"
        } }
        assertTrue("Unlock/reload preserves exact disk bytes before any cache interaction", baseline.matches(activity.proofDirectory))
        assertTrue("Genuine biometric reopen retains no app-usable Google grant", observation.flags()["tokenAbsent"] == true)
        ui.onNodeWithText("Offline cache; Connect to refresh.").assertExists()
        ui.onNodeWithTag("menu-search").performScrollTo().performTextReplacement("00123")
        ui.onNode(hasClickAction() and hasText(coldEntry.label)).assertExists()
        click("Favorites"); ui.onNode(hasClickAction() and hasText(coldEntry.label)).assertExists()
        click("Recent"); ui.onNode(hasClickAction() and hasText(coldEntry.label)).assertExists()
        click("All"); cachedCook()
        assertTrue("Fresh-process cache including favorite/recent is exact", snapshot() == coldExpected(baseline.refreshedAt))
    }
}
