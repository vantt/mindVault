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
        if (ui.hasText("PassChef — Cookbook riêng")) ui.click("EN")
        assertTrue("Unique native Cookbook starts without encrypted jars", !File(directory, "cookbook-v1.json").exists())
        ui.fill("Master password", AuthenticatedCookbookFixture.password)
        ui.fill("Spice jar 1 (setup only)", "aB!*")
        hideIme(); report("setup-await-authentication"); ui.click("Set up / recover with password")
        idleReady()
        assertTrue("Real password setup committed encrypted Spice payload", record() != null)
        report("setup-authenticated")
        ui.click("Sources")
        ui.fill("Menu URL or ID", coldOrigin.menuId)
        ui.fill("Local tab label", coldOrigin.title)
        ui.fill("Tab-separated rows", "Label\tRecipe\tUsername\nMail-cold\tabcd#1\t00123")
        hideIme(); ui.click("Preview columns")
        await { foreground() && timerMain { !book().sources.busy && book().sources.draft != null } }
        ui.fill("First data row", "2")
        for ((label, value) in listOf("Recipe" to "2", "Label" to "1", "Username" to "3", "URL" to "", "Notes" to "")) {
            ui.fill(label, value)
        }
        hideIme(); ui.click("Confirm mapping and save cache")
        await { foreground() && timerMain { !book().sources.busy && book().sources.snapshots.size == 1 } }
        assertTrue("Actual mapping explicitly confirms Recipe and account columns", timerMain {
            val saved = book().sources.snapshots.single()
            saved.mapping == coldExpected(saved.refreshedAt).mapping && saved.entries == listOf(coldEntry)
        })
        ui.click("Menu"); ui.click("Favorite"); idleReady()
        ui.click("Favorites")
        assertTrue("Actual Favorites list contains the confirmed cached row", ui.hasText(coldEntry.label))
        ui.click(coldEntry.label)
        await { foreground() && timerMain { book().hasDish() && !book().sources.busy } }
        assertTrue("Cached native Cook uses exact original Recipe and Menu", timerMain { book().selected == coldEntry })
        assertTrue("Actual native cached Dish starts masked", ui.hasText("••••••••"))
        assertTrue("Actual native detail preserves literal username and Recipe",
            ui.hasText("00123") && ui.hasText("abcd#1"))
        ui.click("Reveal for 10 seconds")
        await { foreground() && timerMain { book().revealed && book().dish == "aB!*abcd" } }
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
