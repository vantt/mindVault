package dev.passchef.mobile

/** Device key management. Cipher/BiometricPrompt operations are Android-only. */
expect class KeystoreCrypto() {
    fun createKey(alias: String, preferStrongBox: Boolean): Boolean
    fun deleteKey(alias: String)
}
