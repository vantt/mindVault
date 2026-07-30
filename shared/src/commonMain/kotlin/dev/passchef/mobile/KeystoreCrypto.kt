package dev.passchef.mobile

/**
 * expect/actual crypto boundary for the Keystore-backed master-key wrap/unwrap
 * mechanism (CONTEXT.md D5/D6). This cell scaffolds ONLY the boundary itself —
 * a trivial placeholder actual body proves the module wiring compiles and the
 * :app -> :shared dependency resolves at build time.
 *
 * Real Keystore/BiometricPrompt/Argon2id wrap-unwrap logic is explicitly out of
 * scope here (future slice E3, per the dispatch prohibition) — do not add it.
 */
expect class KeystoreCrypto() {
    fun ping(): String
}
