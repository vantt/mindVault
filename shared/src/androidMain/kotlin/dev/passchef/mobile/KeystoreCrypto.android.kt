package dev.passchef.mobile

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyInfo
import android.security.keystore.KeyProperties
import android.security.keystore.StrongBoxUnavailableException
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec

/** Android-only Ciphers must be authorized by BiometricPrompt.CryptoObject per operation. */
actual class KeystoreCrypto actual constructor() {
    private val store = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

    actual fun createKey(alias: String, preferStrongBox: Boolean): Boolean {
        require(alias.startsWith("passchef-"))
        check(!store.containsAlias(alias))
        if (Build.VERSION.SDK_INT >= 28 && preferStrongBox) {
            try { generate(alias, true); return hardwareBacked(alias) }
            catch (_: StrongBoxUnavailableException) { store.deleteEntry(alias) }
        }
        generate(alias, false)
        return hardwareBacked(alias)
    }

    private fun generate(alias: String, strongBox: Boolean) {
        val spec = KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setKeySize(256).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setRandomizedEncryptionRequired(true).setUserAuthenticationRequired(true)
            .setInvalidatedByBiometricEnrollment(true)
        if (Build.VERSION.SDK_INT >= 30) spec.setUserAuthenticationParameters(0, KeyProperties.AUTH_BIOMETRIC_STRONG)
        else spec.setUserAuthenticationValidityDurationSeconds(-1)
        if (Build.VERSION.SDK_INT >= 28) spec.setIsStrongBoxBacked(strongBox)
        KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore").apply {
            init(spec.build()); generateKey()
        }
    }

    private fun key(alias: String): SecretKey = store.getKey(alias, null) as? SecretKey
        ?: throw IllegalStateException("Device key unavailable; use your master password")
    fun wrapCipher(alias: String): Cipher = Cipher.getInstance("AES/GCM/NoPadding").apply {
        init(Cipher.ENCRYPT_MODE, key(alias))
    }
    fun unwrapCipher(alias: String, iv: ByteArray): Cipher = Cipher.getInstance("AES/GCM/NoPadding").apply {
        init(Cipher.DECRYPT_MODE, key(alias), GCMParameterSpec(128, iv))
    }
    fun hardwareBacked(alias: String): Boolean = info(alias).isInsideSecureHardware
    fun exportable(alias: String): Boolean = key(alias).encoded != null
    fun info(alias: String): KeyInfo = SecretKeyFactory.getInstance("AES", "AndroidKeyStore")
        .getKeySpec(key(alias), KeyInfo::class.java) as KeyInfo
    actual fun deleteKey(alias: String) { if (alias.isNotEmpty()) store.deleteEntry(alias) }
}
