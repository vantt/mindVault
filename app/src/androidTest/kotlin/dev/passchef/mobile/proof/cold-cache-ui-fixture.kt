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
        ui.onNode(hasClickAction() and hasText(label)).scrollIfAble().performClick()
    }
    private fun fill(label: String, value: String) = ui.onNode(hasSetTextAction() and hasText(label))
        .scrollIfAble().performTextReplacement(value)
    fun english() {
        if (ui.onAllNodesWithText("EN").fetchSemanticsNodes().isNotEmpty()) ui.onNodeWithText("EN").performClick()
    }
    fun setup() {
        observation.await(observation::foreground); ui.waitForIdle(); english()
        assertTrue("Warm unique fixture starts without encrypted jars", !coldJarFile(activity.proofDirectory).exists())
        observation.report("warm-owner-setup-biometric")
        RedesignUi.setupVault(ui, "SYNTHETIC_COLD_Kitchen_2026", listOf("aB!*"), fingerprint = true)
        observation.await { observation.foreground() && observation.flags()["ready"] == true && observation.flags()["busy"] == false }
        ui.waitForIdle()
        assertTrue("Genuine strong-biometric setup completed, no password fallback substituted", timerMain {
            observation.vault().status.startsWith("Round trip ready;")
        } && VaultRepository(AtomicRecordStore(coldJarFile(activity.proofDirectory))).record()?.alias?.isNotEmpty() == true)
    }
    fun prepareCache() {
        observation.requireReady()
        RedesignUi.offlineDraft(ui, { observation.book() }, coldOrigin, "Label\tRecipe\tUsername\nMail-cold\tabcd#1\t00123")
        fill("Advanced: data starts at row", "2")
        ui.onNodeWithTag("column-RECIPE").scrollIfAble().performTextReplacement("2")
        ui.onNodeWithTag("column-LABEL").scrollIfAble().performTextReplacement("1")
        ui.onNodeWithTag("column-USERNAME").scrollIfAble().performTextReplacement("3")
        RedesignUi.confirmAndSave(ui)
        observation.await { timerMain { !observation.book().sources.busy && observation.book().sources.snapshots.size == 1 } }
        ui.waitForIdle(); RedesignUi.passwords(ui)
        cachedCook(); RedesignUi.favoriteFromDetail(ui) // Favorite moved to the detail screen.
        observation.await { timerMain { !observation.book().sources.busy } }
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
        RedesignUi.passwordsRoot(ui)
        observation.requireReady()
        // Redesign list row: the merged row node carries the label text and the open action.
        ui.onNodeWithText(coldEntry.label).scrollIfAble().performClick()
        ui.waitForIdle() // let the list settle before awaiting the detail cook
        observation.await { observation.foreground() && timerMain {
            !observation.book().sources.busy && observation.book().hasDish()
        } }
        ui.waitForIdle()
        assertTrue("Actual cached Cook retains the literal synthetic row/origin", timerMain { observation.book().selected == coldEntry })
        ui.onNodeWithText(coldEntry.username).assertExists()
        ui.onNodeWithText(coldEntry.recipe).assertExists()
        ui.onNode(hasText(coldOrigin.title + " · row 2", substring = true, ignoreCase = true)).assertExists()
        ui.onNodeWithTag("dish-display").scrollIfAble().assertTextEquals("••••••••")
        ui.onNodeWithTag("dish-copy").assertIsEnabled()
        hideIme()
        ui.onNodeWithTag("dish-reveal").scrollIfAble().performClick()
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
        RedesignUi.unlockWithFingerprint(ui)
        observation.await { observation.foreground() && observation.flags()["ready"] == true }
        ui.waitForIdle() // Normal unlock LaunchedEffect starts durable inventory loading.
        observation.await { observation.foreground() && timerMain {
            val source = observation.book().sources
            !source.busy && source.snapshots == listOf(coldExpected(baseline.refreshedAt)) && source.status == "OFFLINE"
        } }
        assertTrue("Unlock/reload preserves exact disk bytes before any cache interaction", baseline.matches(activity.proofDirectory))
        assertTrue("Genuine biometric reopen retains no app-usable Google grant", observation.flags()["tokenAbsent"] == true)
        // Redesign: OFFLINE is the calm default and is not shown as a banner; assert the state itself.
        assertTrue("Saved copy is the offline cache", timerMain { observation.book().sources.status == "OFFLINE" })
        RedesignUi.passwordsRoot(ui); ui.onNodeWithTag("menu-search").scrollIfAble().performTextReplacement("00123")
        ui.onNode(hasClickAction() and hasText(coldEntry.label)).assertExists()
        click("Favorites"); ui.onNode(hasClickAction() and hasText(coldEntry.label)).assertExists()
        click("Recent"); ui.onNode(hasClickAction() and hasText(coldEntry.label)).assertExists()
        click("All"); cachedCook()
        assertTrue("Fresh-process cache including favorite/recent is exact", snapshot() == coldExpected(baseline.refreshedAt))
    }
}
