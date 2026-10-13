package dev.passchef.mobile.proof

import android.os.Bundle
import android.os.SystemClock
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.MainActivity
import dev.passchef.mobile.cookbook.*
import org.junit.Assert.assertTrue
import java.io.File

/** Optional live-provider continuation; no session/grant restoration or device network changes. */
class GoogleCacheUiContinuation(
    private val ui: AndroidComposeTestRule<ActivityScenarioRule<CookbookProofActivity>, CookbookProofActivity>,
    private val phase: (String) -> Unit
) {
    private fun <T> main(action: (CookbookProofActivity) -> T): T {
        var result: Result<T>? = null
        InstrumentationRegistry.getInstrumentation().runOnMainSync { result = runCatching { action(ui.activity) } }
        return requireNotNull(result).getOrThrow()
    }
    private fun controller(activity: CookbookProofActivity) =
        MainActivity::class.java.getDeclaredField("cookbook").apply { isAccessible = true }.get(activity) as CookbookController
    private fun noToken(activity: CookbookProofActivity) = CookbookSources::class.java.getDeclaredField("accessToken")
        .apply { isAccessible = true }.get(controller(activity).sources) == null
    private fun ready(activity: CookbookProofActivity) = CookbookUiDiagnostics.stateFlags(activity)["vaultReady"] == true
    private fun foreground(activity: CookbookProofActivity): Boolean {
        val flags = CookbookUiDiagnostics.environmentFlags(activity)
        return activity.lifecycle.currentState == Lifecycle.State.RESUMED && flags["powerInteractive"] == true &&
            flags["keyguardLocked"] == false && flags["deviceLocked"] == false && flags["proofWindowFocused"] == true &&
            flags["proofWindowShown"] == true && flags["proofWindowAttached"] == true
    }
    private fun await(condition: () -> Boolean) {
        val deadline = SystemClock.elapsedRealtime() + 180_000
        while (!condition() && SystemClock.elapsedRealtime() < deadline) Thread.sleep(100)
        assertTrue("Bounded real cache continuation reached its expected state", condition())
    }
    private fun mark(name: String) {
        phase(name)
        val flags = main { activity -> mapOf("ownForeground" to foreground(activity), "sessionReady" to ready(activity),
            "cachePresent" to controller(activity).sources.snapshots.isNotEmpty(), "tokenAbsent" to noToken(activity)) }
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefGoogleCachePhase", name)
            flags.forEach { (key, value) -> putBoolean("passchefGoogleCache_$key", value) }
        })
    }
    private fun click(text: String) {
        assertTrue("Actual own foreground required for cache UI", main(::foreground))
        ui.onNode(hasClickAction() and hasText(text)).performScrollTo().performClick()
    }
    private fun tag(name: String): SemanticsNodeInteraction {
        assertTrue("Actual own foreground required for cache UI", main(::foreground))
        return ui.onNodeWithTag(name).performScrollTo()
    }
    private fun idleReady() = await { main { foreground(it) && ready(it) && !controller(it).sources.busy } }
    private fun locked() {
        val clean = main { activity ->
            val flags = CookbookUiDiagnostics.stateFlags(activity)
            val source = controller(activity).sources
            foreground(activity) && flags["vaultUnlocked"] == false && flags["vaultReady"] == false &&
                flags["hasDish"] == false && flags["readyDish"] == false && flags["lifetimePresent"] == false &&
                flags["revealed"] == false && flags["dishNonempty"] == false && controller(activity).selected == null &&
                noToken(activity) && source.metadata == null && source.draft == null && !source.busy && source.status == "OFFLINE"
        }
        assertTrue("Actual lock clears session, Dish, selected entry and provider grant", clean)
        ui.onNodeWithTag("dish-display").assertDoesNotExist()
    }
    private fun reopen(expected: MenuSnapshot, directory: File, unlockPhase: String) {
        locked()
        if (ui.onAllNodesWithText("EN").fetchSemanticsNodes().isNotEmpty()) click("EN")
        mark(unlockPhase)
        click("Open with biometric") // Owner must authorize the genuine device operation.
        await { main { foreground(it) && ready(it) } }
        mark("$unlockPhase-authenticated")
        ui.waitForIdle() // Normal Compose synchronization publishes the unlock-triggered cache reload.
        mark("$unlockPhase-cache-reload")
        await { main { activity ->
            val source = controller(activity).sources
            foreground(activity) && ready(activity) && !source.busy && source.snapshots == listOf(expected) &&
                source.status == "OFFLINE" && noToken(activity) && activity.proofDirectory == directory
        } }
        assertTrue("Normal unlock reloads the same durable provider cache without Connect", main { activity ->
            val source = controller(activity).sources
            activity.proofDirectory == directory && source.snapshots == listOf(expected) && source.status == "OFFLINE" &&
                noToken(activity) && source.metadata == null && source.draft == null
        })
        ui.onNodeWithText("Offline cache; Connect to refresh.").assertExists()
        tag("menu-search").performTextReplacement("00123")
        ui.onNode(hasClickAction() and hasText("Mail-test")).assertExists()
        click("Favorites")
        ui.onNode(hasClickAction() and hasText("Mail-test")).assertExists()
        click("Recent")
        ui.onNode(hasClickAction() and hasText("Mail-test")).assertExists()
        click("All")
    }
    private fun cachedCook(entry: MenuEntry) {
        click("Mail-test") // Actual cached row opens and cooks through the guarded production kitchen.
        await { main { foreground(it) && ready(it) && !controller(it).sources.busy && controller(it).hasDish() } }
        assertTrue("Cached Cook retains the exact provider row and context", main { controller(it).selected == entry })
        ui.onNodeWithText(entry.username).assertExists()
        ui.onNodeWithText(entry.recipe).assertExists()
        ui.onNodeWithText(entry.origin.title + " · " + entry.row).assertExists()
        tag("dish-display").assertTextEquals("••••••••")
        tag("dish-copy").assertIsEnabled()
        tag("dish-reveal").performClick()
        tag("dish-display").assertTextEquals("aB!*abcd")
        assertTrue("Exact synthetic cached Dish is ready only under genuine unlock", main {
            ready(it) && controller(it).hasDish() && controller(it).revealed && controller(it).dish == "aB!*abcd"
        })
    }
    fun run(directory: File, saved: MenuSnapshot, encryptedJars: ByteArray) {
        val entry = saved.entries.single { it.row == 2 }
        val record = File(directory, "cookbook-v1.json")
        val store = MenuSnapshotStore(File(directory, "menus"))
        mark("cache-ui-favorite-cook")
        click("Menu")
        click("Favorite")
        idleReady()
        cachedCook(entry)
        val expected = saved.copy(favorites = saved.favorites + entry.id,
            recent = (listOf(entry.id) + saved.recent.filterNot { it == entry.id }).take(20))
        assertTrue("Favorite and recent retain only the exact provider entry identities",
            main { controller(it).sources.snapshots == listOf(expected) } && store.load(entry.origin) == expected)
        mark("cache-ui-lock-reopen")
        click("Lock")
        reopen(expected, directory, "cache-ui-owner-reunlock")
        cachedCook(entry)
        mark("cache-ui-authenticated-recreation")
        val original = main { it.retainProofStorage = true; it }
        val oldController = main(::controller)
        ui.activityRule.scenario.recreate()
        await { main(::foreground) }
        assertTrue("Recreation destroys original Activity and creates a new locked controller", main {
            it !== original && original.lifecycle.currentState == Lifecycle.State.DESTROYED &&
                controller(it) !== oldController && !oldController.hasDish() && oldController.dish.isEmpty() &&
                it.proofDirectory == directory && controller(it).sources.snapshots.isEmpty()
        })
        locked()
        assertTrue("Recreation preserves exact encrypted jars and cache on the same owned disk",
            encryptedJars.contentEquals(record.readBytes()) && store.load(entry.origin) == expected)
        reopen(expected, directory, "cache-ui-recreated-owner-reunlock")
        cachedCook(entry)
        assertTrue("Reopened cached cooking preserves the encrypted Cookbook and durable metadata",
            encryptedJars.contentEquals(record.readBytes()) && store.load(entry.origin) == expected)
        mark("cache-ui-complete")
    }
}
