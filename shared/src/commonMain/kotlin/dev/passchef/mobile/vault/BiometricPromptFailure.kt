package dev.passchef.mobile.vault

/** Android BiometricPrompt namespace, separate from public Keystore error codes. */
enum class BiometricPromptFailure {
    LOCKOUT, PERMANENT_LOCKOUT, OTHER;
    fun isLockout() = this == LOCKOUT || this == PERMANENT_LOCKOUT
    companion object {
        fun fromAndroidError(code: Int) = when (code) {
            7 -> LOCKOUT
            9 -> PERMANENT_LOCKOUT
            else -> OTHER
        }
    }
}
