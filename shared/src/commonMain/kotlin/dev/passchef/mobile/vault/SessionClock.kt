package dev.passchef.mobile.vault

/** Monotonic timestamps supplied by the platform; no persisted unlocked state. */
class SessionClock {
    private var opened: Long? = null
    private var lastUsed = 0L
    private var generation = 0L
    fun begin(now: Long): Long { opened = now; lastUsed = now; return ++generation }
    fun touch(now: Long) { if (!expired(now)) lastUsed = now }
    fun expired(now: Long): Boolean = opened?.let {
        now < it || now - it >= 600_000 || now - lastUsed >= 300_000
    } ?: true
    fun lock() { opened = null; generation++ }
    fun accepts(token: Long): Boolean = token == generation && opened != null
}
