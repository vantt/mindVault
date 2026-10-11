package dev.passchef.mobile.proof

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.biometric.BiometricManager
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.KeystoreCrypto
import dev.passchef.mobile.vault.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import javax.crypto.Cipher

/** Run only on an owner-authorized physical device with enrolled strong biometrics. */
@RunWith(AndroidJUnit4::class)
class KeystorePhysicalTest {
    @Test fun realWrapLockDiskReopenBiometricUnwrapAndUnauthenticatedRefusal() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val context = instrumentation.targetContext
        assertEquals(BiometricManager.BIOMETRIC_SUCCESS, BiometricManager.from(context)
            .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG))
        val file = File(context.noBackupFilesDir, "physical-proof-${UUID.randomUUID()}.json")
        val alias = "passchef-${UUID.randomUUID()}"
        val crypto = KeystoreCrypto()
        val repository = VaultRepository(AtomicRecordStore(file))
        val password = Argon2MasterKey.passwordBytes("SYNTHETIC_Physical_🍳")
        val spice = "SYNTHETIC_PHYSICAL_SPICE".encodeToByteArray()
        val intent = Intent(context, BiometricProofActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        var prepared: PreparedVault? = null
        var opened: VaultSession? = null
        var expected: ByteArray? = null
        try {
            prepared = repository.setup(password, mapOf(1 to spice))
            val hardware = crypto.createKey(alias, context.packageManager.hasSystemFeature("android.hardware.strongbox_keystore"))
            assertFalse("Keystore key must not export", crypto.exportable(alias))
            assertTrue(crypto.info(alias).isUserAuthenticationRequired)
            val wrap = crypto.wrapCipher(alias)
            val wrapped = ActivityScenario.launch<BiometricProofActivity>(intent).use { scenario ->
                authorize(scenario, wrap, "wrap").doFinal(prepared!!.session.key)
            }
            repository.commitWrap(prepared, alias, wrap.iv, wrapped)
            expected = prepared.session.key.copyOf()
            prepared.close(); assertTrue(prepared.session.key.all { it == 0.toByte() })
            // Reopen the on-disk record in a new repository/session; no unlocked process state reused.
            val restarted = VaultRepository(AtomicRecordStore(file))
            val record = restarted.record()!!
            var refused = false
            try { crypto.unwrapCipher(alias, record.wrapIv).doFinal(record.wrappedKey) }
            catch (_: java.security.GeneralSecurityException) { refused = true }
            catch (_: java.security.ProviderException) { refused = true }
            assertTrue("Unwrap must refuse absent per-operation authentication", refused)
            val unwrap = crypto.unwrapCipher(alias, record.wrapIv)
            val raw = ActivityScenario.launch<BiometricProofActivity>(intent).use { scenario ->
                authorize(scenario, unwrap, "unwrap").doFinal(record.wrappedKey)
            }
            opened = restarted.unwrap(raw)
            assertTrue("Authenticated master key equality", java.security.MessageDigest.isEqual(expected, opened.key))
            assertTrue("Authenticated spice cache equality", java.security.MessageDigest.isEqual(spice, opened.spices[1]))
            val before = file.readBytes()
            try { restarted.recover("wrong".encodeToByteArray()); fail("Wrong password accepted") }
            catch (_: javax.crypto.AEADBadTagException) { }
            assertArrayEquals(before, file.readBytes())
            instrumentation.sendStatus(0, Bundle().apply {
                putString("passchefDevice", "${Build.MANUFACTURER} ${Build.MODEL}; API${Build.VERSION.SDK_INT}")
                putString("passchefProof", "Real CryptoObject wrap/unwrap; hardwareProtected=$hardware; nonexportable=true; cache=true")
            })
        } finally {
            prepared?.close(); opened?.close(); expected?.fill(0); password.fill(0); spice.fill(0)
            crypto.deleteKey(alias); file.delete()
        }
    }
    private fun authorize(scenario: ActivityScenario<BiometricProofActivity>, cipher: Cipher, phase: String): Cipher {
        val done = CountDownLatch(1); val authorized = AtomicReference<Cipher?>()
        scenario.onActivity { it.authorize(cipher, phase, authorized, done) }
        assertTrue("Owner biometric timed out during $phase", done.await(120, TimeUnit.SECONDS))
        return authorized.get() ?: throw AssertionError("Owner biometric did not authorize $phase")
    }
}
