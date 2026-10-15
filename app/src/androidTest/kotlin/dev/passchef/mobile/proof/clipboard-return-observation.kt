package dev.passchef.mobile.proof

import android.os.Bundle
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.cookbook.ClipboardOwnership
import dev.passchef.mobile.cookbook.OwnedClipboard

/** Diagnostics only: reads own RAM without clipboard access, reconciliation or policy mutation. */
object ClipboardReturnObservation {
    fun report(state: ColdCacheObservation, claim: PendingClipboardClaim) = timerMain {
        val adapter = state.book().clipboard
        val policy = OwnedClipboard::class.java.getDeclaredField("policy").apply { isAccessible = true }
            .get(adapter) as ClipboardOwnership
        val value = ClipboardOwnership::class.java.getDeclaredField("value").apply { isAccessible = true }.get(policy)
        val deadline = ClipboardOwnership::class.java.getDeclaredField("deadline").apply { isAccessible = true }.getLong(policy)
        val label = OwnedClipboard::class.java.getDeclaredField("label").apply { isAccessible = true }.get(adapter)
        val foreground = OwnedClipboard::class.java.getDeclaredField("foreground").apply { isAccessible = true }.getBoolean(adapter)
        val generation = PendingClipboardClaim::class.java.getDeclaredField("generation").apply { isAccessible = true }.getLong(claim)
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefReplacementPhase", "return-ownership-sampled")
            putBoolean("passchefReplacement_adapterForeground", foreground)
            putBoolean("passchefReplacement_valueAbsent", value == null)
            putBoolean("passchefReplacement_labelAbsent", label == null)
            putBoolean("passchefReplacement_generationSame", policy.token() == generation)
            putBoolean("passchefReplacement_deadlineOriginal", deadline == claim.deadline)
            putBoolean("passchefReplacement_deadlineZero", deadline == 0L)
            putBoolean("passchefReplacement_labelStillOriginal", label == claim.label)
        })
    }
}
