package dev.passchef.mobile.crypto

import dev.passchef.mobile.recipe.webUnicode
import kotlinx.serialization.json.Json
import javax.crypto.AEADBadTagException
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/** Accepts the extension's original full-access JSON, not compact QR frames. */
object BundleDecryptor {
    private val json = Json { ignoreUnknownKeys = true }

    fun decrypt(bundleJson: String, sharingPassword: String): Map<String, SpiceValue> {
        val bundle = try { json.decodeFromString<FullAccessBundle>(bundleJson) }
        catch (_: Exception) { throw IllegalArgumentException("Invalid full-access bundle") }
        require(bundle.type == "passchef-fullaccess-share" && bundle.version == "1.0") {
            "Unsupported bundle type or version"
        }
        require(bundle.iv.size == 12 && bundle.exportSalt.size == 16 && bundle.encryptedData.size >= 16) {
            "Invalid bundle byte fields"
        }
        require((bundle.iv + bundle.exportSalt + bundle.encryptedData).all { it in 0..255 }) {
            "Invalid bundle byte fields"
        }
        val password = webUnicode(sharingPassword).toCharArray()
        val spec = PBEKeySpec(password, bundle.exportSalt.bytes(), 100000, 256)
        val key = try { SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).encoded }
        finally { password.fill('\u0000'); spec.clearPassword() }
        val plaintext = try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), GCMParameterSpec(128, bundle.iv.bytes()))
            cipher.doFinal(bundle.encryptedData.bytes())
        } catch (_: AEADBadTagException) {
            throw IllegalArgumentException("Incorrect sharing password or damaged bundle")
        } finally { key.fill(0) }
        return try { json.decodeFromString<Map<String, SpiceValue>>(plaintext.decodeToString(throwOnInvalidSequence = true)) }
        catch (_: Exception) { throw IllegalArgumentException("Invalid decrypted bundle") }
        finally { plaintext.fill(0) }
    }

    private fun List<Int>.bytes() = map { it.toByte() }.toByteArray()
}
