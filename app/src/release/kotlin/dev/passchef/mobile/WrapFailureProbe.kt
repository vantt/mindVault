package dev.passchef.mobile

/** Release builds never emit physical debug diagnostics or inspect the key. */
object WrapFailureProbe {
    @Suppress("UNUSED_PARAMETER")
    fun record(phase: String, error: Throwable, securityLevel: () -> Int) = Unit
}
