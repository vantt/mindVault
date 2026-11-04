package dev.passchef.mobile.cookbook

/** Main-thread ordering only; rejected access tokens stay in RAM until local eviction succeeds. */
internal class RejectedTokenCacheGate(private val clear: (String, (Boolean) -> Unit) -> Unit) {
    private val rejected = linkedSetOf<String>()
    private val waiting = mutableListOf<(Boolean) -> Unit>()
    private var attempt: Any? = null

    fun reject(token: String) {
        rejected += token
        start()
    }

    fun beforeAuthorization(complete: (Boolean) -> Unit) {
        if (rejected.isEmpty()) complete(true)
        else { waiting += complete; start() }
    }

    private fun start() {
        if (attempt != null) return
        val token = rejected.firstOrNull()
        if (token == null) { finishWaiting(true); return }
        val current = Any()
        attempt = current
        fun finished(success: Boolean) {
            if (attempt !== current) return
            attempt = null
            if (success) { rejected -= token; start() }
            else finishWaiting(false) // Retain the rejected token; only an explicit Connect retries.
        }
        try { clear(token, ::finished) } catch (_: Exception) { finished(false) }
    }

    private fun finishWaiting(success: Boolean) {
        val callbacks = waiting.toList()
        waiting.clear()
        callbacks.forEach { it(success) }
    }
}
