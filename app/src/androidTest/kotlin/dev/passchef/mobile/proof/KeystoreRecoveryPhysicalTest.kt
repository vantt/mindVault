package dev.passchef.mobile.proof

import android.content.Intent
import android.os.Bundle
import android.os.Build
import android.security.KeyStoreException
import androidx.biometric.BiometricManager
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.KeystoreCrypto
import dev.passchef.mobile.vault.*
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.security.MessageDigest
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import javax.crypto.Cipher

/** Real recovery on a unique synthetic Cookbook, with four owner biometric authorizations. */
@RunWith(AndroidJUnit4::class)
class KeystoreRecoveryPhysicalTest {
    @Test fun authenticatesOldCacheRewrapsFreshKeyAndUnlocksSameCache() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val context = instrumentation.targetContext
        val file = File(context.noBackupFilesDir, "recovery-proof-${UUID.randomUUID()}.json")
        val oldAlias = "passchef-${UUID.randomUUID()}"
        val newAlias = "passchef-${UUID.randomUUID()}"
        val crypto = KeystoreCrypto()
        val repository = VaultRepository(AtomicRecordStore(file))
        val password = Argon2MasterKey.passwordBytes("SYNTHETIC_RECOVERY_PASSWORD_🍳")
        val spice = "SYNTHETIC_RECOVERY_SPICE".encodeToByteArray()
        val owned = mutableListOf<AutoCloseable>()
        var expected: ByteArray? = null
        var phase = "biometric-availability"
        fun stage(value: String) {
            phase = value
            instrumentation.sendStatus(0, Bundle().apply { putString("passchefRecoveryPhase", value) })
        }
        val intent = Intent(context, BiometricProofActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            assertTrue("Strong biometric must be enrolled", BiometricManager.from(context)
                .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS)
            stage("setup-derive-and-encrypt-cache")
            val setup = repository.setup(password, mapOf(1 to spice)).also { owned += it }
            expected = setup.session.key.copyOf()
            stage("setup-key-generation")
            val preferStrongBox = context.packageManager.hasSystemFeature("android.hardware.strongbox_keystore")
            crypto.createKey(oldAlias, preferStrongBox)
            if (Build.VERSION.SDK_INT >= 31) instrumentation.sendStatus(0, Bundle().apply {
                putInt("passchefSetupKeySecurityLevel", crypto.info(oldAlias).securityLevel)
            })
            val wrap = crypto.wrapCipher(oldAlias)
            stage("setup-wrap-prompt-1-of-4")
            val wrapped = authorizedOperation(intent, wrap, "setup wrap 1/4", setup.session.key) { stage("setup-wrap-doFinal") }
            stage("setup-record-first-commit")
            repository.commitWrap(setup, oldAlias, wrap.iv, wrapped)
            setup.close()
            val old = repository.record()!!
            val firstUnwrap = crypto.unwrapCipher(oldAlias, old.wrapIv)
            stage("baseline-unlock-prompt-2-of-4")
            val initial = repository.unwrap(authorizedOperation(intent, firstUnwrap, "baseline unlock 2/4", old.wrappedKey)
                { stage("baseline-unwrap-doFinal") }).also { owned += it }
            assertTrue("Baseline master key equality", MessageDigest.isEqual(expected, initial.key))
            initial.close()
            stage("password-recovery-cache-authentication")
            val recovered = repository.recover(password).also { owned += it }
            assertTrue("Recovered master key equality", MessageDigest.isEqual(expected, recovered.session.key))
            assertTrue("Recovered cache equality", MessageDigest.isEqual(spice, recovered.session.spices[1]))
            stage("fresh-recovery-key-generation")
            crypto.createKey(newAlias, preferStrongBox)
            if (Build.VERSION.SDK_INT >= 31) instrumentation.sendStatus(0, Bundle().apply {
                putInt("passchefRecoveryKeySecurityLevel", crypto.info(newAlias).securityLevel)
            })
            val rewrap = crypto.wrapCipher(newAlias)
            stage("recovery-wrap-prompt-3-of-4")
            val newWrapped = authorizedOperation(intent, rewrap, "recovery wrap 3/4", recovered.session.key) { stage("recovery-wrap-doFinal") }
            stage("existing-record-recovery-commit")
            assertTrue("Retirement must identify prior alias", repository.commitWrap(recovered, newAlias, rewrap.iv, newWrapped) == oldAlias)
            stage("prior-key-retirement")
            assertTrue("Prior alias retirement completed", repository.retireKeys(crypto::deleteKey))
            recovered.close()
            val replacement = VaultRepository(AtomicRecordStore(file)).record()!!
            assertTrue("New alias committed", replacement.alias == newAlias)
            assertTrue("Derivation salt unchanged", MessageDigest.isEqual(old.salt, replacement.salt))
            assertTrue("Encrypted cache unchanged", MessageDigest.isEqual(old.cache, replacement.cache))
            assertTrue("Cache IV unchanged", MessageDigest.isEqual(old.cacheIv, replacement.cacheIv))
            assertTrue("Retirement queue empty", replacement.retiredAliases.isEmpty())
            val finalCipher = crypto.unwrapCipher(newAlias, replacement.wrapIv)
            stage("recovered-unlock-prompt-4-of-4")
            val final = repository.unwrap(authorizedOperation(intent, finalCipher, "recovered unlock 4/4", replacement.wrappedKey)
                { stage("final-unwrap-doFinal") }).also { owned += it }
            assertTrue("Final master key equality", MessageDigest.isEqual(expected, final.key))
            assertTrue("Final spice cache equality", MessageDigest.isEqual(spice, final.spices[1]))
            stage("recovery-complete")
        } catch (error: Throwable) {
            val causes = generateSequence(error) { it.cause }.take(5).toList()
            instrumentation.sendStatus(0, Bundle().apply {
                putString("passchefRecoveryFailurePhase", phase)
                putString("passchefExceptionClasses", causes.joinToString(" -> ") { it.javaClass.name })
                if (Build.VERSION.SDK_INT >= 33) {
                    val keyErrors = causes.filterIsInstance<KeyStoreException>()
                    putString("passchefKeyStoreDiagnostics", keyErrors.joinToString("; ") {
                        "code=${it.numericErrorCode}; system=${it.isSystemError}; transient=${it.isTransientFailure}; " +
                            "authRequired=${it.requiresUserAuthentication()}; retry=" +
                            if (it.isTransientFailure) it.retryPolicy.toString() else "not-transient"
                    })
                }
                putString("passchefSanitizedStack", causes.flatMap { it.stackTrace.take(12).toList() }
                    .joinToString("\n") { "${it.className}.${it.methodName}:${it.lineNumber}" })
            })
            throw AssertionError("Physical recovery failed; sanitized phase/class/stack recorded")
        } finally {
            owned.forEach { it.close() }; expected?.fill(0); password.fill(0); spice.fill(0)
            runCatching { crypto.deleteKey(oldAlias) }; runCatching { crypto.deleteKey(newAlias) }
            File(file.parentFile, file.name + ".pending").delete(); file.delete()
        }
    }
    private fun authorizedOperation(intent: Intent, cipher: Cipher, phase: String, input: ByteArray, authorizedStage: () -> Unit): ByteArray =
        ActivityScenario.launch<BiometricProofActivity>(intent).use { scenario ->
            val done = CountDownLatch(1); val authorized = AtomicReference<Cipher?>()
            scenario.onActivity { it.authorize(cipher, phase, authorized, done) }
            assertTrue("Owner biometric deadline", done.await(120, TimeUnit.SECONDS))
            val ready = authorized.get() ?: throw AssertionError("Owner biometric did not authorize the operation")
            authorizedStage()
            ready.doFinal(input)
        }
}
