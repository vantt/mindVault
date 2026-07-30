package dev.passchef.mobile

/**
 * Trivial placeholder actual — no Keystore, no BiometricPrompt, no crypto.
 * Proves the expect/actual boundary compiles on the Android target only.
 * Real implementation lands in a future slice (E3), not this cell.
 */
actual class KeystoreCrypto actual constructor() {
    actual fun ping(): String = "pong"
}
