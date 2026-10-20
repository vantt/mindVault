package dev.passchef.mobile.proof

import android.app.KeyguardManager
import android.content.ClipboardManager
import android.os.Bundle
import android.os.Looper
import android.os.PowerManager
import android.os.SystemClock
import androidx.lifecycle.Lifecycle
import androidx.test.platform.app.InstrumentationRegistry

/** Fixed observations after an existing failure; never reconciles, mutates ownership or changes its result. */
object ClipboardFailureTailObservation {
    private fun read(owner: Any, name: String): Any? = owner.javaClass.getDeclaredField(name)
        .apply { isAccessible = true }.get(owner)
    fun observe(state: ColdCacheObservation, ownerCopy: OwnerCopyObservation) {
        check(Looper.myLooper() != Looper.getMainLooper())
        sample(state, ownerCopy, 0, false)
        Thread.sleep(150)
        sample(state, ownerCopy, 1, false)
        Thread.sleep(1_000)
        sample(state, ownerCopy, 2, true)
    }
    private fun legalForeground(state: ColdCacheObservation): Boolean = state.activity.let { activity ->
        activity.lifecycle.currentState == Lifecycle.State.RESUMED && activity.window.decorView.hasWindowFocus() &&
            activity.window.decorView.isShown && activity.getSystemService(PowerManager::class.java).isInteractive &&
            !activity.getSystemService(KeyguardManager::class.java).isKeyguardLocked &&
            !activity.getSystemService(KeyguardManager::class.java).isDeviceLocked
    }
    private fun clipboardSample(state: ColdCacheObservation, bundle: Bundle, prefix: String,
                                manager: ClipboardManager, marker: String, oldLabel: String?) {
        val available = legalForeground(state)
        bundle.putBoolean("passchefReplacement_${prefix}ReadAvailable", available)
        if (!available) return
        val success = runCatching {
            val clip = manager.primaryClip
            bundle.putBoolean("passchefReplacement_${prefix}ClipPresent", clip != null)
            bundle.putBoolean("passchefReplacement_${prefix}OneItem", clip?.itemCount == 1)
            bundle.putBoolean("passchefReplacement_${prefix}LabelDiffers", oldLabel != null && clip != null &&
                clip.description.label?.toString() != oldLabel)
            bundle.putBoolean("passchefReplacement_${prefix}MarkerExact", clip != null && clip.itemCount == 1 &&
                clip.getItemAt(0).text?.toString() == marker)
        }.isSuccess
        bundle.putBoolean("passchefReplacement_${prefix}ReadSucceeded", success)
    }
    private fun sample(state: ColdCacheObservation, ownerCopy: OwnerCopyObservation, index: Int, readClipboard: Boolean) = timerMain {
        val adapter = state.book().clipboard
        val policy = requireNotNull(read(adapter, "policy"))
        val value = read(policy, "value")
        val label = read(adapter, "label")
        val deadline = read(policy, "deadline") as Long
        val generation = read(policy, "generation") as Long
        val originalGeneration = read(ownerCopy, "generation") as Long
        val oldLabel = read(ownerCopy, "marker") as String?
        val activity = state.activity
        val bundle = Bundle().apply {
            putString("passchefReplacementPhase", "failure-tail-observed")
            putString("passchefReplacementFixtureId", activity.proofId)
            putInt("passchefReplacementTailSample", index)
            putLong("passchefReplacementElapsedSinceCopy", SystemClock.elapsedRealtime() - ownerCopy.copiedAt)
            putBoolean("passchefReplacement_adapterForeground", read(adapter, "foreground") as Boolean)
            putBoolean("passchefReplacement_metadataAbsent", value == null && label == null)
            putBoolean("passchefReplacement_valueAbsent", value == null)
            putBoolean("passchefReplacement_labelAbsent", label == null)
            putBoolean("passchefReplacement_generationSame", generation == originalGeneration)
            putBoolean("passchefReplacement_deadlineOriginal", deadline == ownerCopy.copiedAt + 30_000)
            putBoolean("passchefReplacement_deadlineZero", deadline == 0L)
            putBoolean("passchefReplacement_labelStillOriginal", oldLabel != null && label == oldLabel)
            putInt("passchefReplacement_retryAttempts", read(adapter, "retryAttempts") as Int)
            putBoolean("passchefReplacement_retryQueued", read(adapter, "retryQueued") as Boolean)
            putBoolean("passchefReplacement_retryGenerationSame", read(adapter, "retryGeneration") as Long == originalGeneration)
            putBoolean("passchefReplacement_lifecycleResumed", activity.lifecycle.currentState == Lifecycle.State.RESUMED)
            putBoolean("passchefReplacement_windowFocused", activity.window.decorView.hasWindowFocus())
            putBoolean("passchefReplacement_windowShown", activity.window.decorView.isShown)
            putBoolean("passchefReplacement_interactive", activity.getSystemService(PowerManager::class.java).isInteractive)
            putBoolean("passchefReplacement_keyguardClear", !activity.getSystemService(KeyguardManager::class.java).isKeyguardLocked)
            putBoolean("passchefReplacement_deviceUnlocked", !activity.getSystemService(KeyguardManager::class.java).isDeviceLocked)
            putBoolean("passchefReplacement_legalForeground", legalForeground(state))
        }
        if (readClipboard) {
            val marker = "PASSCHEF_CLIPBOARD_${activity.proofId}"
            clipboardSample(state, bundle, "adapter", read(adapter, "clipboard") as ClipboardManager, marker, oldLabel)
            clipboardSample(state, bundle, "activity", activity.getSystemService(ClipboardManager::class.java), marker, oldLabel)
        }
        InstrumentationRegistry.getInstrumentation().sendStatus(2, bundle)
    }
}
