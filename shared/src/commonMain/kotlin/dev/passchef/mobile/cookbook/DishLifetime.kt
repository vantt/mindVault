package dev.passchef.mobile.cookbook

/** Caller supplies monotonic time and the actual Cookbook session guard. */
class DishLifetime {
    private var generation = 0L
    private var value: String? = null
    private var revealedUntil = 0L
    fun invalidate(): Long { value = null; revealedUntil = 0; return ++generation }
    fun publish(token: Long, dish: String, sessionReady: Boolean): Boolean {
        if (token != generation || !sessionReady) return false
        value = dish; return true
    }
    fun reveal(now: Long, sessionReady: Boolean) { if (sessionReady && value != null) revealedUntil = now + 10_000 }
    fun visible(now: Long, sessionReady: Boolean): String? = if (sessionReady && now < revealedUntil) value else null
    fun forCopy(sessionReady: Boolean): String? = if (sessionReady) value else null
    fun ready(sessionReady: Boolean) = sessionReady && value != null
}

/** A denied read never authorizes a clipboard overwrite. Cleanup survives screen closure. */
class ClipboardOwnership {
    private var value: String? = null
    private var deadline = 0L
    private var generation = 0L
    fun copied(text: String, now: Long): Long { value = text; deadline = now + 30_000; return ++generation }
    fun expired(now: Long) = value != null && now >= deadline
    fun shouldClear(token: Long, now: Long, observed: String?): Boolean =
        token == generation && expired(now) && observed != null && value == observed
    fun token() = generation
    fun forget() { value = null; deadline = 0 }
}
