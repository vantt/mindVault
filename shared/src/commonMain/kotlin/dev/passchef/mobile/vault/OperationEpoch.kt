package dev.passchef.mobile.vault

/** Prevents a prior unlock/prompt from reopening a locked or newly requested session. */
class OperationEpoch {
    @kotlin.concurrent.Volatile private var generation = 0L
    fun begin(): Long = ++generation
    fun invalidate() { generation++ }
    fun accepts(token: Long): Boolean = token == generation
    fun deliver(token: Long, current: () -> Unit, stale: () -> Unit) {
        if (accepts(token)) current() else stale()
    }
}
