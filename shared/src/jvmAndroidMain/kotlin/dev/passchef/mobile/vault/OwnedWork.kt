package dev.passchef.mobile.vault

import java.util.concurrent.Executor
import java.util.concurrent.RejectedExecutionException

/** Submission transfers ownership; a shutting-down executor must not strand key buffers. */
object OwnedWork {
    fun submit(executor: Executor, owned: AutoCloseable, action: () -> Unit): Boolean {
        return try { executor.execute(action); true }
        catch (_: RejectedExecutionException) { owned.close(); false }
    }
}
