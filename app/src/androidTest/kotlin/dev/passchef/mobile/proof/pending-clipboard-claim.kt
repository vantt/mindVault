package dev.passchef.mobile.proof

import dev.passchef.mobile.cookbook.ClipboardOwnership
import dev.passchef.mobile.cookbook.OwnedClipboard
import org.junit.Assert.assertTrue

/** Read-only adapter ownership; never accesses the system clipboard or changes its deadline. */
class PendingClipboardClaim(private val state: ColdCacheObservation, copiedAt: Long) {
    private val adapter = timerMain { state.book().clipboard }
    private val policy = OwnedClipboard::class.java.getDeclaredField("policy").apply { isAccessible = true }.get(adapter) as ClipboardOwnership
    private val deadlineField = ClipboardOwnership::class.java.getDeclaredField("deadline").apply { isAccessible = true }
    private val valueField = ClipboardOwnership::class.java.getDeclaredField("value").apply { isAccessible = true }
    private val labelField = OwnedClipboard::class.java.getDeclaredField("label").apply { isAccessible = true }
    private val foregroundField = OwnedClipboard::class.java.getDeclaredField("foreground").apply { isAccessible = true }
    val deadline = copiedAt + 30_000
    private val generation = timerMain { policy.token() }
    val label = timerMain { requireNotNull(labelField.get(adapter) as String?) }
    init { assertTrue("Captured original Dish label is uniquely owned", label.startsWith("PassChef Dish ")) }
    fun requirePending() = timerMain {
        assertTrue("Stopped adapter retains exactly the original pending ownership/deadline", !foregroundField.getBoolean(adapter) &&
            policy.token() == generation && deadlineField.getLong(policy) == deadline && valueField.get(policy) != null &&
            labelField.get(adapter) == label)
    }
    fun requireForgotten() = timerMain {
        assertTrue("Normal focused return forgets stale ownership without another PassChef Copy", foregroundField.getBoolean(adapter) &&
            policy.token() == generation && valueField.get(policy) == null && labelField.get(adapter) == null)
    }
}
