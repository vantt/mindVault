package dev.passchef.mobile.vault

/** Android supplies a typed public-code reader; control tests need no provider constructors. */
object WrapFailureCauses {
    fun code(error: Throwable, typedCode: (Throwable) -> Int?): Int? =
        generateSequence(error) { it.cause }.take(5).mapNotNull(typedCode).firstOrNull()
}
