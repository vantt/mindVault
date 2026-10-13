package dev.passchef.mobile.proof

import android.os.Bundle

/** Code metadata only; never exception messages, causes, UI trees or field contents. */
fun Bundle.putSafeTimerFailure(failure: Throwable) {
    putString("passchefTimerFailureClass", failure.javaClass.name)
    putString("passchefTimerAssertionKind", when (failure.message) {
        "Original authenticated window/idle baseline remains unchanged" -> "clock-state-changed"
        "No early lock or lost ready Dish" -> "early-lock-or-dish-loss"
        else -> "unclassified"
    })
    failure.stackTrace.take(5).forEachIndexed { index, frame ->
        putString("passchefTimerFailureFrame${index}Class", frame.className)
        putString("passchefTimerFailureFrame${index}Method", frame.methodName)
        putString("passchefTimerFailureFrame${index}File", frame.fileName.orEmpty())
        putInt("passchefTimerFailureFrame${index}Line", frame.lineNumber)
    }
}
