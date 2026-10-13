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
import java.util.UUID

/** Actual clipboard adapter in a locked proof Activity; no Cook/auth/session acceptance. */
@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 28)
class OwnedClipboardPhysicalTest {
    private fun locked(activity: CookbookProofActivity) {
        val flags = CookbookUiDiagnostics.stateFlags(activity)
        assertTrue("Proof Activity is actually resumed", activity.lifecycle.currentState == Lifecycle.State.RESUMED)
        assertTrue("Proof Cookbook stays locked without retained Dish",
            flags["vaultUnlocked"] == false && flags["vaultReady"] == false && flags["vaultBusy"] == false &&
                flags["hasDish"] == false && flags["lifetimePresent"] == false)
        assertTrue("No encrypted proof Cookbook is created", !File(activity.proofDirectory, "cookbook-v1.json").exists())
    }

    private fun foreground(activity: CookbookProofActivity) {
        locked(activity)
        val flags = CookbookUiDiagnostics.environmentFlags(activity)
        assertTrue("Clipboard observation requires actual interactive unlocked focused proof window",
            flags["powerInteractive"] == true && flags["keyguardLocked"] == false &&
                flags["deviceLocked"] == false && flags["proofWindowFocused"] == true &&
                flags["proofWindowShown"] == true && flags["proofWindowAttached"] == true)
    }

    private fun awaitForeground(scenario: ActivityScenario<CookbookProofActivity>) {
        val deadline = SystemClock.elapsedRealtime() + 10_000
        var ready = false
        do {
            scenario.onActivity { activity ->
                val flags = CookbookUiDiagnostics.environmentFlags(activity)
                ready = activity.lifecycle.currentState == Lifecycle.State.RESUMED &&
                    flags["powerInteractive"] == true && flags["keyguardLocked"] == false &&
                    flags["deviceLocked"] == false && flags["proofWindowFocused"] == true &&
                    flags["proofWindowShown"] == true && flags["proofWindowAttached"] == true
            }
            if (!ready) Thread.sleep(100)
        } while (!ready && SystemClock.elapsedRealtime() < deadline)
        assertTrue("Actual interactive unlocked focused proof window became available", ready)
    }

    private fun read(activity: CookbookProofActivity, manager: ClipboardManager): ClipData? {
        foreground(activity)
        return manager.primaryClip
    }

    private fun empty(activity: CookbookProofActivity, manager: ClipboardManager) {
        foreground(activity)
        assertTrue("Actual owned Dish clipboard is empty after deadline", !manager.hasPrimaryClip())
    }

    private fun onLocked(scenario: ActivityScenario<CookbookProofActivity>, action: (CookbookProofActivity, ClipboardManager) -> Unit) {
        scenario.onActivity { activity ->
            foreground(activity)
            action(activity, activity.getSystemService(ClipboardManager::class.java))
            locked(activity)
        }
    }

    private fun proof(action: (ActivityScenario<CookbookProofActivity>, MutableMap<String, String>) -> Unit) {
        val scenario = ActivityScenario.launch(CookbookProofActivity::class.java)
        var directory: File? = null
        val owned = mutableMapOf<String, String>()
        try {
            scenario.onActivity { directory = it.proofDirectory }
            awaitForeground(scenario)
            onLocked(scenario) { _, _ -> }
            action(scenario, owned)
            onLocked(scenario) { _, _ -> }
        } finally {
            // Read text only behind this run's unique marker; never inspect an unrelated clipboard.
            runCatching { scenario.onActivity { activity ->
                val manager = activity.getSystemService(ClipboardManager::class.java)
                val clip = read(activity, manager)
                val expected = owned[clip?.description?.label?.toString()]
                if (expected != null && clip != null && clip.itemCount == 1 && clip.getItemAt(0).text?.toString() == expected) {
                    manager.clearPrimaryClip()
                }
            } }
            try { scenario.close() } finally { directory?.deleteRecursively() }
        }
    }

    private fun copy(activity: CookbookProofActivity, manager: ClipboardManager, owned: MutableMap<String, String>, text: String): String {
        assertTrue("Actual foreground Dish copy succeeds", OwnedClipboard.obtain(activity).copyDish(text))
        val clip = read(activity, manager)
        val label = clip?.description?.label?.toString()
        assertTrue("Fresh clipboard uses the adapter's unique Dish marker", label?.startsWith("PassChef Dish ") == true)
        val marker = requireNotNull(label)
        owned[marker] = text
        assertTrue("Actual synthetic Dish is marked sensitive",
            clip?.description?.extras?.getBoolean("android.content.extra.IS_SENSITIVE", false) == true)
        matches(activity, manager, marker, text)
        return marker
    }

    private fun matches(activity: CookbookProofActivity, manager: ClipboardManager, label: String, expected: String) {
        val clip = read(activity, manager)
        assertTrue("Expected owned synthetic clipboard marker", clip?.description?.label?.toString() == label)
        assertTrue("Owned synthetic clipboard equality", clip != null && clip.itemCount == 1 && clip.getItemAt(0).text?.toString() == expected)
    }

    private fun waitFrom(start: Long, duration: Long) {
        val remaining = start + duration - SystemClock.elapsedRealtime()
        if (remaining > 0) Thread.sleep(remaining) // Real elapsed time and production Handler only.
    }

    @Test fun untouchedSyntheticDishClearsAfterActualThirtySeconds() = proof { scenario, owned ->
        var copyStartedAt = 0L
        var copiedAt = 0L
        var label = ""
        val dish = "SYNTHETIC_UNTOUCHED_DISH_2026"
        onLocked(scenario) { activity, manager ->
            copyStartedAt = SystemClock.elapsedRealtime()
            label = copy(activity, manager, owned, dish)
            copiedAt = SystemClock.elapsedRealtime()
        }
        waitFrom(copiedAt, 29_000)
        onLocked(scenario) { activity, manager ->
            val now = SystemClock.elapsedRealtime()
            assertTrue("Untouched Dish is observed before actual thirty-second deadline",
                now >= copiedAt + 29_000 && now < copyStartedAt + 30_000)
            matches(activity, manager, label, dish)
        }
        waitFrom(copiedAt, 30_500)
        onLocked(scenario) { activity, manager -> empty(activity, manager) }
    }

    @Test fun externallyReplacedSyntheticClipboardSurvivesOldDishDeadline() = proof { scenario, owned ->
        val replacementLabel = "PassChef clipboard proof ${UUID.randomUUID()}"
        val replacement = "SYNTHETIC_EXTERNAL_REPLACEMENT_2026"
        var copiedAt = 0L
        onLocked(scenario) { activity, manager ->
            copy(activity, manager, owned, "SYNTHETIC_REPLACED_DISH_2026")
            copiedAt = SystemClock.elapsedRealtime()
            owned[replacementLabel] = replacement
            manager.setPrimaryClip(ClipData.newPlainText(replacementLabel, replacement))
            matches(activity, manager, replacementLabel, replacement)
        }
        waitFrom(copiedAt, 30_500)
        onLocked(scenario) { activity, manager -> matches(activity, manager, replacementLabel, replacement) }
    }

    @Test fun newerDishSurvivesOldDeadlineAndClearsAtItsOwnDeadline() = proof { scenario, owned ->
        var firstAt = 0L
        var secondAt = 0L
        var secondLabel = ""
        val second = "SYNTHETIC_NEWER_DISH_2026"
        onLocked(scenario) { activity, manager ->
            copy(activity, manager, owned, "SYNTHETIC_OLDER_DISH_2026")
            firstAt = SystemClock.elapsedRealtime()
        }
        waitFrom(firstAt, 5_000)
        onLocked(scenario) { activity, manager ->
            secondLabel = copy(activity, manager, owned, second)
            secondAt = SystemClock.elapsedRealtime()
        }
        waitFrom(firstAt, 30_500)
        onLocked(scenario) { activity, manager ->
            val now = SystemClock.elapsedRealtime()
            assertTrue("Survival is observed after old deadline and before new deadline",
                now >= firstAt + 30_000 && now < secondAt + 30_000)
            matches(activity, manager, secondLabel, second)
        }
        waitFrom(secondAt, 30_500)
        onLocked(scenario) { activity, manager -> empty(activity, manager) }
    }
}
