package dev.passchef.mobile.proof

import android.content.ClipData
import android.content.ClipboardManager
import android.os.SystemClock
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import dev.passchef.mobile.cookbook.OwnedClipboard
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/** Real locked Activity recreation and clipboard timer only; no Cook/auth/process-death proof. */
@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 28)
class ClipboardRecreationPhysicalTest {
    private fun environmentReady(activity: CookbookProofActivity): Boolean {
        val flags = CookbookUiDiagnostics.environmentFlags(activity)
        return activity.lifecycle.currentState == Lifecycle.State.RESUMED &&
            flags["powerInteractive"] == true && flags["keyguardLocked"] == false &&
            flags["deviceLocked"] == false && flags["proofWindowFocused"] == true &&
            flags["proofWindowShown"] == true && flags["proofWindowAttached"] == true
    }

    private fun ready(activity: CookbookProofActivity) {
        assertTrue("Actual interactive unlocked focused proof window is required", environmentReady(activity))
        val flags = CookbookUiDiagnostics.stateFlags(activity)
        assertTrue("Actual proof Cookbook remains locked without retained Dish",
            flags["vaultUnlocked"] == false && flags["vaultReady"] == false && flags["vaultBusy"] == false &&
                flags["readyDish"] == false && flags["hasDish"] == false && flags["lifetimePresent"] == false &&
                flags["revealed"] == false && flags["dishNonempty"] == false)
        assertTrue("No encrypted proof Cookbook is created", !File(activity.proofDirectory, "cookbook-v1.json").exists())
    }

    private fun awaitReady(scenario: ActivityScenario<CookbookProofActivity>, directories: MutableSet<File>) {
        val deadline = SystemClock.elapsedRealtime() + 10_000
        var available = false
        do {
            scenario.onActivity { activity ->
                directories += activity.proofDirectory
                available = environmentReady(activity)
                if (available) ready(activity)
            }
            if (!available) Thread.sleep(100)
        } while (!available && SystemClock.elapsedRealtime() < deadline)
        assertTrue("Actual foreground proof window became available", available)
    }

    private fun read(activity: CookbookProofActivity, manager: ClipboardManager): ClipData? {
        ready(activity)
        return manager.primaryClip
    }

    private fun matches(activity: CookbookProofActivity, manager: ClipboardManager, marker: String, dish: String) {
        val clip = read(activity, manager)
        assertTrue("Expected unique owned synthetic marker", marker.isNotEmpty() && clip?.description?.label?.toString() == marker)
        // Never expose an observed clipboard value in a JUnit failure.
        assertTrue("Owned synthetic clipboard equality", clip != null && clip.itemCount == 1 && clip.getItemAt(0).text?.toString() == dish)
    }

    private fun waitFrom(start: Long, duration: Long) {
        val remaining = start + duration - SystemClock.elapsedRealtime()
        if (remaining > 0) Thread.sleep(remaining) // Actual monotonic time; production Handler remains untouched.
    }

    @Test fun realActivityRecreationPreservesOriginalClipboardDeadline() {
        val scenario = ActivityScenario.launch(CookbookProofActivity::class.java)
        val directories = mutableSetOf<File>()
        val dish = "SYNTHETIC_RECREATED_CLIPBOARD_DISH_2026"
        var marker = ""
        var original: CookbookProofActivity? = null
        var copyStartedAt = 0L
        var copiedAt = 0L
        try {
            scenario.onActivity { activity -> original = activity; directories += activity.proofDirectory }
            awaitReady(scenario, directories)
            scenario.onActivity { activity ->
                ready(activity)
                val manager = activity.getSystemService(ClipboardManager::class.java)
                copyStartedAt = SystemClock.elapsedRealtime()
                assertTrue("Actual foreground synthetic Dish copy succeeds", OwnedClipboard.obtain(activity).copyDish(dish))
                copiedAt = SystemClock.elapsedRealtime()
                val clip = read(activity, manager)
                val label = clip?.description?.label?.toString()
                assertTrue("Actual adapter supplies a unique Dish marker", label?.startsWith("PassChef Dish ") == true)
                marker = requireNotNull(label)
                assertTrue("Actual synthetic Dish is marked sensitive",
                    clip?.description?.extras?.getBoolean("android.content.extra.IS_SENSITIVE", false) == true)
                matches(activity, manager, marker, dish)
            }

            waitFrom(copiedAt, 5_000)
            scenario.recreate() // Actual production stop/destroy/start/focus callbacks own clipboard foreground.
            awaitReady(scenario, directories)
            scenario.onActivity { activity ->
                assertTrue("Actual replacement Activity has a different identity", activity !== original)
                assertTrue("Actual original Activity was destroyed", original?.lifecycle?.currentState == Lifecycle.State.DESTROYED)
                assertTrue("Recreation owns two distinct isolated proof directories", directories.size == 2)
                assertTrue("Recreation observation occurs before earliest cleanup deadline",
                    SystemClock.elapsedRealtime() < copyStartedAt + 30_000)
                matches(activity, activity.getSystemService(ClipboardManager::class.java), marker, dish)
            }

            waitFrom(copiedAt, 29_000)
            scenario.onActivity { activity ->
                val now = SystemClock.elapsedRealtime()
                assertTrue("Original Dish survives until a real observation before its earliest deadline",
                    now >= copiedAt + 29_000 && now < copyStartedAt + 30_000)
                matches(activity, activity.getSystemService(ClipboardManager::class.java), marker, dish)
            }
            waitFrom(copiedAt, 30_500)
            scenario.onActivity { activity ->
                ready(activity)
                assertTrue("Original timer clears the actual clipboard after recreation",
                    !activity.getSystemService(ClipboardManager::class.java).hasPrimaryClip())
                assertTrue("Both isolated Cookbooks remain absent", directories.all { !File(it, "cookbook-v1.json").exists() })
            }
        } finally {
            runCatching { scenario.onActivity { activity ->
                directories += activity.proofDirectory
                // Preserve unknown/newer clipboard data; only inspect content behind this run's exact marker.
                val manager = activity.getSystemService(ClipboardManager::class.java)
                val clip = read(activity, manager)
                if (marker.isNotEmpty() && clip != null && clip.description.label?.toString() == marker &&
                    clip.itemCount == 1 && clip.getItemAt(0).text?.toString() == dish) manager.clearPrimaryClip()
            } }
            try { scenario.close() } finally { directories.forEach { it.deleteRecursively() } }
        }
    }
}
