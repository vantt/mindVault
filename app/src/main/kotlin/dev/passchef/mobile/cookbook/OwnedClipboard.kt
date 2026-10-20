package dev.passchef.mobile.cookbook

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.os.SystemClock
import java.util.UUID

/** Owned by the process with application context, independent of the closing screen; never blindly clears. */
class OwnedClipboard private constructor(context: Context) {
    companion object {
        private var instance: OwnedClipboard? = null
        fun obtain(context: Context): OwnedClipboard = instance ?: OwnedClipboard(context.applicationContext).also { instance = it }
    }
    private var foreground = false
    fun setForeground(value: Boolean) {
        foreground = value
        if (value) reconcile() else cancelRetry()
    }
    private fun digest(text: String) = java.security.MessageDigest.getInstance("SHA-256").digest(text.toByteArray(Charsets.UTF_8)).joinToString("") { "%02x".format(it) }
    private val clipboard = context.getSystemService(ClipboardManager::class.java)
    private val main = Handler(Looper.getMainLooper())
    private val policy = ClipboardOwnership()
    private var label: String? = null
    private val cleanup = Runnable { reconcile() }
    private var retryGeneration = -1L
    private var retryAttempts = 0
    private var retryQueued = false
    private val retryCleanup = Runnable {
        retryQueued = false
        if (foreground && policy.token() == retryGeneration && policy.expired(SystemClock.elapsedRealtime())) reconcile()
    }
    private fun cancelRetry() {
        main.removeCallbacks(retryCleanup)
        retryQueued = false; retryGeneration = -1L; retryAttempts = 0
    }
    private fun retryUnavailableRead() {
        if (!foreground || !policy.expired(SystemClock.elapsedRealtime())) return
        val generation = policy.token()
        if (retryGeneration != generation) { retryGeneration = generation; retryAttempts = 0 }
        if (retryQueued || retryAttempts >= 11) return
        val immediate = retryAttempts++ == 0
        retryQueued = true
        if (immediate) main.post(retryCleanup) else main.postDelayed(retryCleanup, 100)
    }
    fun copyDish(text: String): Boolean {
        if (!foreground) return false
        return runCatching {
            val marker = "PassChef Dish ${UUID.randomUUID()}"
            val clip = ClipData.newPlainText(marker, text)
            clip.description.extras = PersistableBundle().apply { putBoolean("android.content.extra.IS_SENSITIVE", true) }
            clipboard.setPrimaryClip(clip)
            cancelRetry()
            label = marker; policy.copied(digest(text), SystemClock.elapsedRealtime())
            main.removeCallbacks(cleanup); main.postDelayed(cleanup, 30_000)
            true
        }.getOrDefault(false)
    }
    fun copyUsername(text: String): Boolean = if (!foreground) false else runCatching {
        clipboard.setPrimaryClip(ClipData.newPlainText("PassChef username", text))
        cancelRetry()
        policy.forget(); label = null; main.removeCallbacks(cleanup); true
    }.getOrDefault(false)
    fun reconcile() {
        val now = SystemClock.elapsedRealtime()
        if (!foreground || !policy.expired(now)) return
        runCatching {
            val clip = clipboard.primaryClip ?: run { retryUnavailableRead(); return@runCatching }
            if (clip.description.label?.toString() != label) { policy.forget(); label = null; cancelRetry(); return@runCatching }
            val text = clip.getItemAt(0).text?.toString() ?: run { retryUnavailableRead(); return@runCatching }
            if (policy.shouldClear(policy.token(), now, digest(text))) {
                if (android.os.Build.VERSION.SDK_INT >= 28) clipboard.clearPrimaryClip()
                else clipboard.setPrimaryClip(ClipData.newPlainText("", ""))
                policy.forget(); label = null; cancelRetry()
            }
        }.onFailure { retryUnavailableRead() }
    }
    // Process-owned cleanup intentionally survives Activity destruction; no plaintext Dish retained.
}
