package dev.passchef.mobile.proof

import android.app.Activity
import android.app.Application
import android.app.UiAutomation
import android.os.Bundle
import android.os.Process
import android.os.SystemClock
import android.view.inputmethod.InputMethodManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.MainActivity
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.cookbook.CookbookController
import dev.passchef.mobile.cookbook.MenuSnapshotStore
import dev.passchef.mobile.vault.AtomicRecordStore
import dev.passchef.mobile.vault.VaultRepository
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.concurrent.atomic.AtomicReference

/** Attach --no-restart after real system-task restoration; no ActivityScenario or Compose launch. */
@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 36)
class SavedTaskColdTest {
    private fun waitFor(condition: () -> Boolean) {
        val deadline = SystemClock.elapsedRealtime() + 120_000
        while (!condition() && SystemClock.elapsedRealtime() < deadline) Thread.sleep(100)
        assertTrue("Bounded restored-task state reached", condition())
    }
    @Test fun restoredExistingTaskRemainsLockedThenCooksAfterRealPassword() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val args = InstrumentationRegistry.getArguments()
        require(args.getString("passchefSavedTaskStage") == "cold")
        val baseline = SavedTaskBaseline.read(instrumentation.targetContext.noBackupFilesDir,
            requireNotNull(args.getString("passchefSavedTaskFixtureId")))
        assertTrue("No-restart cold attachment preserves the actual restored process", Process.myPid() ==
            requireNotNull(args.getString("passchefSavedTaskExpectedPid")).toInt() && Process.myPid() != baseline.pid)
        val app = instrumentation.targetContext.applicationContext as Application
        val resumed = AtomicReference<CookbookProofActivity?>()
        val callback = object : Application.ActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: Activity) {
                if (activity is CookbookProofActivity && activity.proofDirectory.canonicalFile == baseline.directory && activity.taskId == baseline.taskId) resumed.set(activity)
            }
            override fun onActivityCreated(activity: Activity, saved: Bundle?) = Unit
            override fun onActivityStarted(activity: Activity) = Unit
            override fun onActivityPaused(activity: Activity) = Unit
            override fun onActivityStopped(activity: Activity) = Unit
            override fun onActivitySaveInstanceState(activity: Activity, saved: Bundle) = Unit
            override fun onActivityDestroyed(activity: Activity) = Unit
        }
        app.registerActivityLifecycleCallbacks(callback)
        try {
            val journal = SavedTaskProofJournal.read(baseline.directory)
            assertTrue("Real saved-state journal proves original stopped task and new-process restoration",
                journal.getInt("originalPid") == baseline.pid && journal.getInt("originalTaskId") == baseline.taskId &&
                    journal.getInt("lastCreatedPid") == Process.myPid() && journal.getInt("lastCreatedTaskId") == baseline.taskId &&
                    journal.getBoolean("lastCreatedWithSavedState") && journal.getBoolean("lastCreatedLocatorMatches") &&
                    journal.getBoolean("lastCreatedCleared") && journal.getBoolean("warmStoppedCleared") &&
                    journal.getBoolean("warmSavedLocatorMatches") && journal.getInt("createCount") == baseline.createCount + 1)
            assertTrue("Restored task preserves exact encrypted jars and private Menu before unlock", baseline.matches())
            baseline.report("cold-await-existing-task-resume")
            // Host performs real Home/task-focus after this registration; no synthetic lifecycle dispatch.
            waitFor { resumed.get() != null }
            val activity = requireNotNull(resumed.get())
            fun book() = MainActivity::class.java.getDeclaredField("cookbook").apply { isAccessible = true }.get(activity) as CookbookController
            fun foreground() = timerMain { CookbookUiDiagnostics.environmentFlags(activity).let { flags ->
                listOf("powerInteractive", "proofWindowFocused", "proofWindowAttached", "proofWindowShown").all { flags[it] == true } &&
                    flags["keyguardLocked"] == false && flags["deviceLocked"] == false &&
                    activity.window.attributes.flags and android.view.WindowManager.LayoutParams.FLAG_SECURE != 0
            } }
            waitFor(::foreground)
            assertTrue("Actual restored Activity keeps the saved locator without a new launch", activity.retainProofStorage &&
                activity.proofDirectory.canonicalFile == baseline.directory && activity.taskId == baseline.taskId && baseline.matches())
            assertTrue("Actual restored controller is locked with no session, grant or Dish", timerMain {
                val vault = MainActivity::class.java.getDeclaredField("controller").apply { isAccessible = true }.get(activity)
                val state = CookbookUiDiagnostics.stateFlags(activity)
                VaultController::class.java.getDeclaredField("session").apply { isAccessible = true }.get(vault) == null &&
                    listOf("vaultUnlocked", "vaultBusy", "vaultReady", "readyDish", "hasDish", "lifetimePresent", "revealed", "dishNonempty")
                        .all { state[it] == false } && book().selected == null &&
                    dev.passchef.mobile.cookbook.CookbookSources::class.java.getDeclaredField("accessToken")
                        .apply { isAccessible = true }.get(book().sources) == null
            })
            val ui = SavedTaskUiActions(instrumentation.getUiAutomation(UiAutomation.FLAG_DONT_SUPPRESS_ACCESSIBILITY_SERVICES),
                activity.packageName, ::foreground)
            if (ui.hasText("PassChef — Cookbook riêng")) ui.click("EN")
            assertTrue("Restored Cookbook exposes real password recovery", ui.hasText("Master password"))
            ui.fill("Master password", AuthenticatedCookbookFixture.password)
            timerMain { activity.getSystemService(InputMethodManager::class.java).hideSoftInputFromWindow(activity.window.decorView.windowToken, 0) }
            baseline.report("cold-password-await-authentication")
            ui.click("Set up / recover with password")
            waitFor { foreground() && timerMain { CookbookUiDiagnostics.stateFlags(activity)["vaultReady"] == true &&
                !book().sources.busy && book().sources.snapshots == listOf(coldExpected(baseline.refreshedAt)) } }
            assertTrue("Real password recovery reloads exact favorite/recent Menu", MenuSnapshotStore(File(baseline.directory, "menus"))
                .load(coldOrigin) == coldExpected(baseline.refreshedAt))
            ui.fill("Search label / username", "00123"); ui.click("Favorites"); ui.click("Recent"); ui.click(coldEntry.label)
            waitFor { foreground() && timerMain { book().hasDish() && !book().sources.busy } }
            assertTrue("Restored task cooks the literal original Recipe/context", timerMain { book().selected == coldEntry })
            ui.click("Reveal for 10 seconds")
            assertTrue("Actual restored task reveals exact synthetic Dish", timerMain { book().revealed && book().dish == "aB!*abcd" })
            val record = requireNotNull(VaultRepository(AtomicRecordStore(File(baseline.directory, "cookbook-v1.json"))).record())
            assertTrue("Genuine password reopening preserves encrypted Spice payload", SavedTaskBaseline.payload(record) == baseline.payloadSha)
            assertTrue("Saved-task proof never performs an implicit clipboard Copy", timerMain { book().clipboardStatus == null })
            baseline.report("saved-task-password-cache-passed")
        } finally { app.unregisterActivityLifecycleCallbacks(callback) }
        // Host closes the exact restored task and removes only this UUID after reviewing the native terminal.
    }
}
