package dev.passchef.mobile.vault

/** Controls ownership and one expired-operation retry; does not simulate hardware. */
class WrapOperationState(private val owned: AutoCloseable, private val retireDisposable: () -> Unit) : AutoCloseable {
    private var cancelled = false
    private var working = false
    private var committed = false
    private var ownsResource = true
    private var cleaned = false
    private var prompt = 0L
    private var accepted = false
    private var cipher: Any? = null
    private var previousCipher: Any? = null
    private var retries = 0
    private var cancelDelay: (() -> Unit)? = null

    @Synchronized fun active() = !cancelled
    @Synchronized fun beginWork(): Boolean {
        if (cancelled) return false
        check(!working); working = true; return true
    }
    @Synchronized fun endWork() { working = false; cleanup() }
    @Synchronized fun newPrompt(value: Any): Long {
        check(!cancelled && value !== previousCipher)
        previousCipher = value; cipher = value; accepted = false; return ++prompt
    }
    @Synchronized fun authorize(id: Long, value: Any): Boolean {
        if (cancelled || accepted || id != prompt || cipher !== value) return false
        accepted = true; cipher = null; return true
    }
    @Synchronized fun retry(api: Int, code: Int?, phase: String): Boolean {
        if (cancelled || api < 33 || code != 15 || phase != "wrap-doFinal" || retries != 0) return false
        retries++; prompt++; accepted = true; cipher = null; return true
    }
    @Synchronized fun delayCancellation(cancel: () -> Unit) {
        if (cancelled) cancel() else cancelDelay = cancel
    }
    @Synchronized fun clearDelay() { cancelDelay = null }
    fun postOwned(post: (() -> Unit) -> Boolean, action: () -> Unit) {
        if (!post(action)) close()
    }
    fun delayOwned(schedule: (Runnable, Long) -> Boolean, remove: (Runnable) -> Unit, action: () -> Unit) {
        val task = Runnable { clearDelay(); if (active()) action() }
        delayCancellation { remove(task) }
        if (!active() || !schedule(task, 5000)) close()
    }
    @Synchronized fun markCommitted() { committed = true }
    /** Caller takes responsibility for session delivery or password-only fallback. */
    @Synchronized fun transferOwned(): Boolean {
        if (cancelled) return false
        ownsResource = false; return true
    }
    @Synchronized override fun close() {
        cancelled = true; prompt++; cipher = null; previousCipher = null
        cancelDelay?.invoke(); cancelDelay = null; cleanup()
    }
    private fun cleanup() {
        if (!cancelled || working || cleaned) return
        cleaned = true
        if (ownsResource) owned.close()
        if (!committed) retireDisposable()
    }
}
