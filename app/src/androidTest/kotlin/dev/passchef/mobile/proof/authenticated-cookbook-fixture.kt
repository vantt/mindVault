package dev.passchef.mobile.proof

import android.os.Bundle
import android.os.Looper
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.MainActivity
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.cookbook.CookbookController
import dev.passchef.mobile.cookbook.MenuSnapshot
import dev.passchef.mobile.cookbook.MenuSnapshotStore
import dev.passchef.mobile.vault.AtomicRecordStore
import dev.passchef.mobile.vault.VaultRepository
import org.junit.Assert.assertTrue
import java.io.File
import java.security.MessageDigest

/** Real password/Compose actions in one UUID-owned Cookbook; no session/authentication injection. */
class AuthenticatedCookbookFixture(private val ui: ComposeTestRule,
    private val scenario: ActivityScenario<CookbookProofActivity>) {
    private fun <T> main(action: () -> T): T = if (Looper.myLooper() == Looper.getMainLooper()) action() else timerMain(action)
    lateinit var activity: CookbookProofActivity; private set
    fun attach() { scenario.onActivity { activity = it; it.retainProofStorage = true } }
    val directory get() = activity.proofDirectory
    fun book(target: CookbookProofActivity = activity) = MainActivity::class.java.getDeclaredField("cookbook").apply { isAccessible = true }
        .get(target) as CookbookController
    fun present(tag: String) = ui.onAllNodesWithTag(tag).fetchSemanticsNodes().isNotEmpty()
    fun foreground(): Boolean = main {
        CookbookUiDiagnostics.environmentFlags(activity).let { flags ->
            listOf("powerInteractive", "proofWindowFocused", "proofWindowAttached", "proofWindowShown")
                .all { flags[it] == true } && flags["keyguardLocked"] == false && flags["deviceLocked"] == false
        }
    }
    fun report(phase: String, times: Map<String, Long> = emptyMap()) = InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
        putString("passchefAuthenticatedPhase", phase)
        putString("passchefAuthenticatedFixture", directory.name)
        putInt("passchefAuthenticatedPid", android.os.Process.myPid())
        times.forEach { (name, value) -> putLong("passchefAuthenticated_$name", value) }
        putBoolean("passchefAuthenticatedBiometricWrapPresent", record()?.alias?.isNotEmpty() == true)
    })
    fun click(label: String) {
        assertTrue("Only actual own foreground accepts a UI action", foreground())
        ui.onNode(hasClickAction() and hasText(label)).performScrollTo().performClick()
    }
    fun fill(label: String, value: String) = ui.onNode(hasSetTextAction() and hasText(label))
        .performScrollTo().performTextReplacement(value)
    fun ready() {
        ui.waitUntil(120_000) { present("menu-search") }
        ui.waitUntil(10_000) { timerMain { !book().sources.busy } }
        assertTrue("Real authenticated Cookbook is ready in own foreground", foreground() && timerMain {
            CookbookUiDiagnostics.stateFlags(activity)["vaultReady"] == true
        })
    }
    fun setupCacheAndCook(): MenuSnapshot {
        ui.waitUntil(10_000, ::foreground)
        if (ui.onAllNodesWithText("EN").fetchSemanticsNodes().isNotEmpty()) ui.onNodeWithText("EN").performClick()
        assertTrue("Unique Cookbook starts without encrypted jars", !File(directory, "cookbook-v1.json").exists())
        fill("Master password", password); fill("Spice jar 1 (setup only)", "aB!*")
        report("setup-await-authentication"); click("Set up / recover with password"); ready()
        report("setup-authenticated")
        click("Sources")
        ui.onNodeWithTag("source-id").performScrollTo().performTextReplacement(coldOrigin.menuId)
        fill("Local tab label", coldOrigin.title)
        ui.onNodeWithTag("source-table").performScrollTo()
            .performTextReplacement("Label\tRecipe\tUsername\nMail-cold\tabcd#1\t00123")
        click("Preview columns")
        ui.waitUntil(10_000) { present("confirm-columns") }
        fill("First data row", "2")
        for ((role, column) in listOf("RECIPE" to "2", "LABEL" to "1", "USERNAME" to "3", "URL" to "", "NOTES" to "")) {
            ui.onNodeWithTag("column-$role").performScrollTo().performTextReplacement(column)
        }
        ui.onNodeWithTag("confirm-columns").performScrollTo().performClick()
        ui.waitUntil(10_000) { timerMain { !book().sources.busy && book().sources.snapshots.size == 1 } }
        click("Menu"); click("Favorite")
        ui.waitUntil(10_000) { timerMain { !book().sources.busy } }
        cachedCook()
        return snapshot().also { assertTrue("Actual confirmed cache keeps favorite/recent identity", it == coldExpected(it.refreshedAt)) }
    }
    fun snapshot() = timerMain { book().sources.snapshots.single() }.also {
        assertTrue("Actual private cache reload equals committed RAM", MenuSnapshotStore(File(directory, "menus")).load(coldOrigin) == it)
    }
    fun cachedCook() {
        click(coldEntry.label)
        ui.waitUntil(10_000) { timerMain { book().hasDish() && !book().sources.busy } }
        assertTrue("Cached Cook uses exact Recipe and origin", timerMain { book().selected == coldEntry })
        ui.onNodeWithTag("dish-display").performScrollTo().assertTextEquals("••••••••")
        ui.onNodeWithTag("dish-reveal").performScrollTo().performClick()
        ui.onNodeWithTag("dish-display").assertTextEquals("aB!*abcd")
    }
    fun requireCleared(target: CookbookProofActivity) {
        val flags = timerMain { CookbookUiDiagnostics.stateFlags(target) }
        assertTrue("Recreated Cookbook has no usable session or Dish", listOf("vaultUnlocked", "vaultBusy", "vaultReady",
            "readyDish", "lifetimePresent", "hasDish", "revealed", "cooking", "dishNonempty").all { flags[it] == false })
        assertTrue("Retired or locked controller has no session or selection", timerMain {
            val vault = MainActivity::class.java.getDeclaredField("controller").apply { isAccessible = true }.get(target)
            VaultController::class.java.getDeclaredField("session").apply { isAccessible = true }.get(vault) == null &&
                flags["vaultClockExpired"] == true && book(target).selected == null
        })
        assertTrue("Locked controller retains no Google grant", timerMain {
            dev.passchef.mobile.cookbook.CookbookSources::class.java.getDeclaredField("accessToken")
                .apply { isAccessible = true }.get(book(target).sources) == null
        })
    }
    fun requireLocked() {
        ui.waitUntil(10_000, ::foreground)
        requireCleared(activity)
        ui.onNodeWithTag("dish-display").assertDoesNotExist(); ui.onNodeWithTag("dish-copy").assertDoesNotExist()
        ui.onNodeWithTag("menu-search").assertDoesNotExist()
    }
    fun reopen(expected: MenuSnapshot) {
        fill("Master password", password)
        report("reopen-await-authentication"); click("Set up / recover with password"); ready()
        assertTrue("Genuine reopen reloads the exact saved cache", snapshot() == expected)
        ui.onNodeWithTag("menu-search").performScrollTo().performTextReplacement("00123")
        click("Favorites"); ui.onNode(hasClickAction() and hasText(coldEntry.label)).assertExists()
        click("Recent"); ui.onNode(hasClickAction() and hasText(coldEntry.label)).assertExists()
        cachedCook()
        assertTrue("Cached Cook preserves favorite/recent mapping", snapshot() == expected)
    }
    fun hashes(): Map<String, String> = directory.walkTopDown().filter { it.isFile }.associate { file ->
        file.relativeTo(directory).path to MessageDigest.getInstance("SHA-256").digest(file.readBytes())
            .joinToString("") { "%02x".format(it) }
    }
    fun record() = VaultRepository(AtomicRecordStore(File(directory, "cookbook-v1.json"))).record()
    companion object { const val password = "SYNTHETIC_AUTHENTICATED_Kitchen_2026" }
}
