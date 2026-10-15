package dev.passchef.mobile.proof

import android.app.KeyguardManager
import android.content.ClipboardManager
import android.os.PowerManager
import android.os.SystemClock
import androidx.lifecycle.Lifecycle
import dev.passchef.mobile.cookbook.ClipboardOwnership
import dev.passchef.mobile.cookbook.OwnedClipboard
import org.junit.Assert.assertTrue

/** Passive foreground observer; binds owner Copy to its unchanged production policy deadline. */
class OwnerCopyObservation(private val state: ColdCacheObservation) : AutoCloseable {
    private val activity = state.activity
    private val manager = activity.getSystemService(ClipboardManager::class.java)
    private val adapter = timerMain { state.book().clipboard }
    private val policy = OwnedClipboard::class.java.getDeclaredField("policy").apply { isAccessible = true }.get(adapter) as ClipboardOwnership
    private val deadlineField = ClipboardOwnership::class.java.getDeclaredField("deadline").apply { isAccessible = true }
    private val labelField = OwnedClipboard::class.java.getDeclaredField("label").apply { isAccessible = true }
    private val initialGeneration = timerMain { policy.token() }
    private val preparedAt = SystemClock.elapsedRealtime()
    private var marker: String? = null
    private var generation = -1L
    private var extraCopy = false
    private var readFailed = false
    @Volatile var copiedAt = -1L; private set
    private val listener = ClipboardManager.OnPrimaryClipChangedListener { observe() }
    init { timerMain { manager.addPrimaryClipChangedListener(listener) } }
    private fun foreground(): Boolean = activity.lifecycle.currentState == Lifecycle.State.RESUMED &&
        activity.window.decorView.hasWindowFocus() && activity.window.decorView.isShown &&
        activity.getSystemService(PowerManager::class.java).isInteractive &&
        !activity.getSystemService(KeyguardManager::class.java).isKeyguardLocked &&
        !activity.getSystemService(KeyguardManager::class.java).isDeviceLocked
    private fun observe() {
        if (copiedAt >= 0) { if (policy.token() != generation) extraCopy = true; return }
        if (!foreground()) return // Never inspect clipboard after focus/foreground access is lost.
        runCatching {
            val current = policy.token()
            if (current <= initialGeneration) return
            val clip = manager.primaryClip ?: return
            val label = labelField.get(adapter) as String? ?: return
            val deadline = deadlineField.getLong(policy)
            val now = SystemClock.elapsedRealtime()
            val actualCopyAt = deadline - 30_000
            assertTrue("Exactly one new actual owner Copy is observed before its original deadline", current == initialGeneration + 1 &&
                actualCopyAt >= preparedAt && actualCopyAt <= now && now < deadline && state.book().clipboardStatus == "COPIED")
            assertTrue("Actual own sensitive marker and synthetic Dish match the policy owner", label.startsWith("PassChef Dish ") &&
                clip.description.label?.toString() == label && clip.itemCount == 1 &&
                clip.getItemAt(0).text?.toString() == "aB!*abcd" &&
                clip.description.extras?.getBoolean("android.content.extra.IS_SENSITIVE", false) == true)
            marker = label; generation = current; copiedAt = actualCopyAt
        }.onFailure { readFailed = true }
    }
    fun requireSingleCopy() = timerMain {
        assertTrue("Copy observation is successful and never renews its original generation", !readFailed && !extraCopy &&
            copiedAt >= 0 && policy.token() == generation && generation == initialGeneration + 1)
    }
    fun checkForFailure() = timerMain {
        assertTrue("No clipboard observation error or extra Copy is hidden by the owner wait", !readFailed && !extraCopy &&
            (copiedAt < 0 || policy.token() == generation))
    }
    fun cleanupIfOwned() = timerMain {
        if (marker != null && foreground()) {
            val clip = manager.primaryClip
            if (clip != null && clip.description.label?.toString() == marker && clip.itemCount == 1 &&
                clip.getItemAt(0).text?.toString() == "aB!*abcd") manager.clearPrimaryClip()
        }
    }
    override fun close() = timerMain { manager.removePrimaryClipChangedListener(listener) }
}
