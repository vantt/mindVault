package dev.passchef.mobile

/** JVM has no Android hardware authentication; never simulate successful key operations. */
actual class KeystoreCrypto actual constructor() {
    actual fun createKey(alias: String, preferStrongBox: Boolean): Boolean =
        throw UnsupportedOperationException("Android Keystore requires an Android device")
    actual fun deleteKey(alias: String): Unit =
        throw UnsupportedOperationException("Android Keystore requires an Android device")
}
