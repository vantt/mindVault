package dev.passchef.mobile.proof

import android.app.UiAutomation
import android.os.Bundle
import android.os.Process
import android.os.SystemClock
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.MainActivity
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.cookbook.CookbookController
import dev.passchef.mobile.cookbook.CookbookSources
import dev.passchef.mobile.cookbook.MenuSnapshot
import dev.passchef.mobile.cookbook.MenuSnapshotStore
import dev.passchef.mobile.vault.AtomicRecordStore
import dev.passchef.mobile.vault.VaultRepository
import java.io.File
import org.junit.Assert.assertTrue

/** Genuine native UI actions for no-restart instrumentation; no Compose test/coroutine scope. */
/** SU1 acknowledgement row text (toggleable row, merged semantics). */
const val SETUP_ACK = "I understand: if I forget it, this vault can't be opened. My accounts still work with my sheet + secret parts."

class SavedTaskNativeFixture(private val scenario: ActivityScenario<CookbookProofActivity>) {
    lateinit var activity: CookbookProofActivity
        private set
    val directory get() = activity.proofDirectory
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val ui by lazy { SavedTaskUiActions(instrumentation.getUiAutomation(
        UiAutomation.FLAG_DONT_SUPPRESS_ACCESSIBILITY_SERVICES), activity.packageName, ::foreground) }
    fun attach() { scenario.onActivity { activity = it; it.retainProofStorage = true } }
    fun book(target: CookbookProofActivity = activity) = MainActivity::class.java
        .getDeclaredField("cookbook").apply { isAccessible = true }.get(target) as CookbookController
    fun record() = VaultRepository(AtomicRecordStore(File(directory, "cookbook-v1.json"))).record()
    private fun foreground(): Boolean = timerMain {
        CookbookUiDiagnostics.environmentFlags(activity).let { flags ->
            activity.lifecycle.currentState == Lifecycle.State.RESUMED &&
                activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0 &&
                listOf("powerInteractive", "proofWindowFocused", "proofWindowAttached", "proofWindowShown")
                    .all { flags[it] == true } && flags["keyguardLocked"] == false && flags["deviceLocked"] == false
        }
    }
    private fun await(condition: () -> Boolean) {
        val deadline = SystemClock.elapsedRealtime() + 120_000
        while (!condition() && SystemClock.elapsedRealtime() < deadline) Thread.sleep(100)
        assertTrue("Bounded native Cookbook state reached", condition())
    }
    private fun report(phase: String) = instrumentation.sendStatus(2, Bundle().apply {
        putString("passchefSavedTaskNativePhase", phase)
        putString("passchefSavedTaskNativeFixture", directory.name)
        putInt("passchefSavedTaskNativePid", Process.myPid())
    })
    private fun reportFavoriteRow(phase: String) {
        val favoriteContainsEntry = timerMain { book().sources.snapshots.any { saved ->
            saved.mapping.origin == coldOrigin && coldEntry in saved.entries && coldEntry.id in saved.favorites
        } }
        val rowPresent = ui.hasText(coldEntry.label)
        instrumentation.sendStatus(2, Bundle().apply {
            putString("passchefSavedTaskNativePhase", phase)
            putString("passchefSavedTaskNativeFixture", directory.name)
            putInt("passchefSavedTaskNativePid", Process.myPid())
            putBoolean("passchefSavedTaskModelFavoriteContainsColdEntry", favoriteContainsEntry)
            putBoolean("passchefSavedTaskUiRowPresent", rowPresent)
            putBoolean("passchefSavedTaskFavoritesTapCompleted", true)
        })
    }
    private fun reportDetailVisibility(phase: String) {
        val selectionMatches = timerMain { book().selected == coldEntry }
        val usernamePresent = ui.hasText("00123")
        val recipePresent = ui.hasText("abcd#1")
        instrumentation.sendStatus(2, Bundle().apply {
            putString("passchefSavedTaskNativePhase", phase)
            putString("passchefSavedTaskNativeFixture", directory.name)
            putInt("passchefSavedTaskNativePid", Process.myPid())
            putBoolean("passchefSavedTaskModelSelectionMatchesColdEntry", selectionMatches)
            putBoolean("passchefSavedTaskUiUsernamePresent", usernamePresent)
            putBoolean("passchefSavedTaskUiRecipePresent", recipePresent)
        })
    }
    private fun idleReady() = await {
        foreground() && timerMain { CookbookUiDiagnostics.stateFlags(activity)["vaultReady"] == true &&
            !book().sources.busy }
    }
    private fun hideIme() = timerMain {
        activity.getSystemService(InputMethodManager::class.java)
            .hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
    }
    fun setupCacheAndCook(): MenuSnapshot {
        await(::foreground)
        if (ui.hasText("Tạo mật khẩu chính")) ui.click("EN")
        assertTrue("Unique native Cookbook starts without encrypted jars", !File(directory, "cookbook-v1.json").exists())
        // Redesign SU1–SU3 through real native accessibility actions.
        ui.fill("Master password", AuthenticatedCookbookFixture.password)
        ui.fill("Type it again", AuthenticatedCookbookFixture.password)
        hideIme(); ui.click(SETUP_ACK); ui.click("Next")
        ui.fill("Secret part 1", "aB!*")
        hideIme(); ui.click("Next")
        report("setup-await-authentication"); ui.click("Use fingerprint & create vault")
        idleReady()
        assertTrue("Real password setup committed encrypted Spice payload", record() != null)
        report("setup-authenticated")
        ui.click("Settings"); ui.click("+ Connect a sheet"); ui.click("Offline table (no Google)")
        // D9: the offline table UI no longer takes a Sheet id; stage with the id-keeping controller entry point.
        timerMain { book().offlineSource(coldOrigin.menuId, coldOrigin.title, "Label\tRecipe\tUsername\nMail-cold\tabcd#1\t00123") }
        await { foreground() && timerMain { !book().sources.busy && book().sources.draft != null } }
        ui.fill("Advanced: data starts at row", "2")
        for ((label, value) in listOf("Written code" to "2", "Name" to "1", "Username" to "3", "Website" to "", "Notes" to "")) {
            ui.fill(label, value)
        }
        hideIme(); ui.click("Looks right — Review"); ui.click("Save to my phone")
        await { foreground() && timerMain { !book().sources.busy && book().sources.snapshots.size == 1 } }
        assertTrue("Actual mapping explicitly confirms Recipe and account columns", timerMain {
            val saved = book().sources.snapshots.single()
            saved.mapping == coldExpected(saved.refreshedAt).mapping && saved.entries == listOf(coldEntry)
        })
        // Favorite now lives on the detail star; open the row, star it, go back to the list.
        // Save lands on Passwords already; a selected Tab is not clickable to accessibility.
        ui.click(coldEntry.label); idleReady(); ui.click("Add favorite"); idleReady(); ui.click("Back"); idleReady()
        ui.click("Favorites")
        reportFavoriteRow("favorites-before-show")
        ui.show(coldEntry.label)
        reportFavoriteRow("favorites-after-show")
        assertTrue("Actual Favorites list contains the confirmed cached row", ui.hasText(coldEntry.label))
        ui.click(coldEntry.label)
        await { foreground() && timerMain { book().hasDish() && !book().sources.busy } }
        assertTrue("Cached native Cook uses exact original Recipe and Menu", timerMain { book().selected == coldEntry })
        reportDetailVisibility("detail-before-show")
        ui.showText("••••••••")
        assertTrue("Actual native cached Dish starts masked", ui.hasText("••••••••"))
        ui.showText("00123"); ui.showText("abcd#1")
        reportDetailVisibility("detail-after-show")
        assertTrue("Actual native detail preserves literal username and Recipe",
            ui.hasText("00123") && ui.hasText("abcd#1"))
        ui.click("Show 10 s")
        await { foreground() && timerMain { book().revealed && book().dish == "aB!*abcd" } }
        ui.showText("aB!*abcd")
        assertTrue("Actual native cached Cook reveals the exact synthetic Dish", ui.hasText("aB!*abcd"))
        val saved = timerMain { book().sources.snapshots.single() }
        assertTrue("Actual native cache retains favorite and recent row identities", saved == coldExpected(saved.refreshedAt))
        assertTrue("Private native cache reload equals committed RAM", MenuSnapshotStore(File(directory, "menus")).load(coldOrigin) == saved)
        report("cache-cook-revealed")
        return saved
    }
    fun requireCleared(target: CookbookProofActivity) {
        val flags = timerMain { CookbookUiDiagnostics.stateFlags(target) }
        assertTrue("Stopped native Cookbook has no usable session or Dish", listOf("vaultUnlocked", "vaultBusy", "vaultReady",
            "readyDish", "lifetimePresent", "hasDish", "revealed", "cooking", "dishNonempty").all { flags[it] == false })
        assertTrue("Stopped native controller has no session or selection", timerMain {
            val vault = MainActivity::class.java.getDeclaredField("controller").apply { isAccessible = true }.get(target)
            VaultController::class.java.getDeclaredField("session").apply { isAccessible = true }.get(vault) == null &&
                flags["vaultClockExpired"] == true && book(target).selected == null
        })
        assertTrue("Stopped native controller retains no Google grant", timerMain {
            CookbookSources::class.java.getDeclaredField("accessToken").apply { isAccessible = true }.get(book(target).sources) == null
        })
    }
}
