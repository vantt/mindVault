package dev.passchef.mobile.proof

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import dev.passchef.mobile.cookbook.*
import org.junit.Assert.assertTrue
import java.io.File

/** Cache actions retain provider provenance and use normal guarded production UI only. */
class GoogleColdCacheUi(private val ui: ComposeTestRule, private val observation: ColdCacheObservation,
    private val phase: (String) -> Unit) {
    private val activity = observation.activity
    private fun click(text: String) {
        observation.requireReady()
        ui.onNode(hasClickAction() and hasText(text)).performScrollTo().performClick()
    }
    private fun idle() = observation.await { observation.foreground() && timerMain {
        observation.vault().readyForCooking() && !observation.book().sources.busy
    } }
    fun favoriteCook(saved: MenuSnapshot): MenuSnapshot {
        assertTrue("Dedicated provider cache contains only the expected synthetic row", saved.entries == listOf(googleColdEntry(saved.mapping.origin)))
        click("Menu"); click("Favorite"); idle(); cachedCook(saved.mapping.origin)
        val expected = googleColdExpected(saved.mapping.origin, saved.refreshedAt)
        val actual = timerMain { observation.book().sources.snapshots.single() }
        assertTrue("Actual provider cache favorite/recent IDs and disk snapshot are exact", actual == expected &&
            MenuSnapshotStore(File(activity.proofDirectory, "menus")).load(saved.mapping.origin) == expected)
        return actual
    }
    /** Reuse exact cached-Cook assertions without toggling favorite or reopening authentication. */
    fun cookAgain(origin: MenuOrigin) {
        click("Menu")
        cachedCook(origin)
    }
    private fun cachedCook(origin: MenuOrigin) {
        val entry = googleColdEntry(origin)
        click(entry.label)
        observation.await { observation.foreground() && timerMain { observation.vault().readyForCooking() &&
            !observation.book().sources.busy && observation.book().hasDish() } }
        ui.waitForIdle()
        assertTrue("Actual cached Cook retains exact provider row/origin", timerMain { observation.book().selected == entry })
        ui.onNodeWithText(entry.username).assertExists(); ui.onNodeWithText(entry.recipe).assertExists()
        ui.onNodeWithText(origin.title + " · 2").assertExists()
        ui.onNodeWithTag("dish-display").performScrollTo().assertTextEquals("••••••••")
        ui.onNodeWithTag("dish-copy").assertIsEnabled()
        ColdCacheUiFixture(ui, observation).hideIme()
        ui.onNodeWithTag("dish-reveal").performScrollTo().performClick()
        ui.onNodeWithTag("dish-display").assertTextEquals("aB!*abcd")
        assertTrue("Genuine provider cached Dish is exact and revealed only while ready", observation.foreground() && timerMain {
            observation.vault().readyForCooking() && observation.book().hasDish() && observation.book().revealed &&
                observation.book().dish == "aB!*abcd"
        })
    }
    fun reopen(baseline: GoogleColdBaseline, expected: MenuSnapshot) {
        ColdCacheUiFixture(ui, observation).english()
        ui.onNodeWithTag("dish-display").assertDoesNotExist(); ui.onNodeWithTag("dish-copy").assertDoesNotExist()
        phase("google-cold-owner-reopen-biometric")
        assertTrue("Actual own locked foreground required before genuine biometric reopen", observation.foreground())
        ui.onNode(hasClickAction() and hasText("Open with biometric")).performScrollTo().performClick()
        phase("google-cold-biometric-request-dispatched")
        observation.await { observation.foreground() && observation.flags()["ready"] == true }
        ui.waitForIdle()
        observation.await { observation.foreground() && timerMain {
            val source = observation.book().sources
            observation.vault().readyForCooking() && !source.busy && source.snapshots == listOf(expected) && source.status == "OFFLINE"
        } }
        assertTrue("Genuine reload preserves frozen provider cache/jars before interactions", baseline.matches(activity.proofDirectory))
        assertTrue("Fresh process has no app-usable token or active provider metadata/draft", observation.flags().let {
            it["tokenAbsent"] == true && it["metadataAbsent"] == true && it["draftAbsent"] == true
        })
        ui.onNodeWithText("Offline cache; Connect to refresh.").assertExists()
        ui.onNodeWithTag("menu-search").performScrollTo().performTextReplacement("00123")
        ui.onNode(hasClickAction() and hasText("Mail-test")).assertExists()
        click("Favorites"); ui.onNode(hasClickAction() and hasText("Mail-test")).assertExists()
        click("Recent"); ui.onNode(hasClickAction() and hasText("Mail-test")).assertExists()
        click("All"); cachedCook(expected.mapping.origin)
        assertTrue("Provider cache/favorite/recent semantics survive fresh-process cooking", timerMain {
            observation.book().sources.snapshots == listOf(expected)
        } && MenuSnapshotStore(File(activity.proofDirectory, "menus")).load(expected.mapping.origin) == expected)
    }
}
