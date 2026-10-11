package dev.passchef.mobile

import android.content.Context
import android.os.Handler
import android.os.Build
import android.os.Looper
import android.os.SystemClock
import androidx.biometric.BiometricManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.passchef.mobile.vault.*
import java.io.File
import java.util.UUID
import java.util.concurrent.Executors
import javax.crypto.Cipher

/** One serial worker owns derivation/storage; stale results are always wiped. */
class VaultController(
    private val context: Context,
    private val authenticate: (Cipher, (Cipher) -> Unit, () -> Unit) -> Unit,
    private val cancelPrompt: () -> Unit
) {
    var status by mutableStateOf("Cookbook locked")
        private set
    var busy by mutableStateOf(false)
        private set
    var unlocked by mutableStateOf(false)
        private set
    private val worker = Executors.newSingleThreadExecutor()
    private val main = Handler(Looper.getMainLooper())
    private val repository = VaultRepository(AtomicRecordStore(File(context.noBackupFilesDir, "cookbook-v1.json")))
    private val crypto = KeystoreCrypto()
    private val clock = SessionClock()
    private var session: VaultSession? = null
    private var pending: PreparedVault? = null
    private var pendingAlias = ""
    private val epoch = OperationEpoch()
    private var active = false
    private val tickCallback = Runnable { tick() }

    fun start() { active = true; tick() }
    fun touch() { clock.touch(SystemClock.elapsedRealtime()) }
    fun lock() {
        active = false; epoch.invalidate(); cancelPrompt(); main.removeCallbacks(tickCallback)
        pending?.close(); pending = null
        val alias = pendingAlias; pendingAlias = ""
        if (alias.isNotEmpty()) retire(alias)
        session?.close(); session = null; clock.lock()
        unlocked = false; busy = false; status = "Cookbook locked"
    }
    fun explicitLock() { lock(); start() }
    fun destroy() { lock(); worker.shutdown() }
    private fun retire(alias: String) {
        if (alias.isEmpty()) return
        val cleanup = Runnable { runCatching { crypto.deleteKey(alias) } }
        runCatching { worker.execute(cleanup) }.onFailure { Thread(cleanup, "passchef-key-cleanup").start() }
    }
    private fun tick() {
        if (!active) return
        if (unlocked && clock.expired(SystemClock.elapsedRealtime())) { explicitLock(); return }
        main.postDelayed(tickCallback, 1000)
    }
    private fun biometricAvailable(): Boolean = BiometricManager.from(context)
        .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS

    fun password(password: ByteArray, spices: Map<Int, ByteArray>) {
        if (!active || busy || unlocked) { password.fill(0); spices.values.forEach { it.fill(0) }; return }
        busy = true; status = "Opening the Cookbook…"
        val token = epoch.begin()
        worker.execute {
            try {
                val prepared = if (repository.record() == null) repository.setup(password, spices) else repository.recover(password)
                main.post {
                    epoch.deliver(token, {
                        if (!active) prepared.close()
                        else if (!biometricAvailable()) passwordOnly(prepared, token)
                        else prepareWrap(prepared, token)
                    }, { prepared.close() })
                }
            } catch (_: Exception) { fail(token, "Could not open Cookbook. Check your master password; stored data was preserved.") }
            finally { password.fill(0); spices.values.forEach { it.fill(0) } }
        }
    }
    private fun passwordOnly(prepared: PreparedVault, token: Long) {
        if (!epoch.accepts(token)) { prepared.close(); return }
        OwnedWork.submit(worker, prepared) {
            try {
                if (!epoch.accepts(token)) { prepared.close(); return@submit }
                repository.commitPasswordOnly(prepared)
                runCatching { repository.retireKeys(crypto::deleteKey) }
                finish(token, prepared.session, "Cookbook open by password; strong biometric unavailable")
            } catch (_: Exception) { prepared.close(); fail(token, "Could not save Cookbook; prior data preserved") }
        }
    }
    private fun prepareWrap(prepared: PreparedVault, token: Long) {
        worker.execute {
            val alias = "passchef-${UUID.randomUUID()}"
            try {
                val hardware = crypto.createKey(alias, context.packageManager.hasSystemFeature("android.hardware.strongbox_keystore"))
                val cipher = crypto.wrapCipher(alias)
                main.post {
                    if (!active || !epoch.accepts(token)) { prepared.close(); retire(alias) }
                    else {
                        pending = prepared; pendingAlias = alias
                        authenticate(cipher, { authorized ->
                            if (!active || !epoch.accepts(token)) return@authenticate
                            pending = null; pendingAlias = ""
                            worker.execute {
                                var committed = false
                                var phase = "wrap-doFinal"
                                try {
                                    if (!epoch.accepts(token)) return@execute
                                    val wrapped = authorized.doFinal(prepared.session.key)
                                    phase = "record-commit"
                                    repository.commitWrap(prepared, alias, authorized.iv, wrapped)
                                    committed = true
                                    phase = "key-retirement"
                                    val retired = runCatching { repository.retireKeys(crypto::deleteKey) }.getOrDefault(false)
                                    val cleanup = if (retired) "" else "; prior device key cleanup pending"
                                    phase = "session-delivery"
                                    finish(token, prepared.session, "Round trip ready; device key hardware protected: $hardware$cleanup")
                                } catch (error: Exception) {
                                    WrapFailureProbe.record(phase, error) {
                                        if (Build.VERSION.SDK_INT >= 31) crypto.info(alias).securityLevel else -1
                                    }
                                    fail(token, "Could not save biometric wrap; prior data preserved")
                                }
                                finally {
                                    if (!committed) { prepared.close(); runCatching { crypto.deleteKey(alias) } }
                                }
                            }
                        }, { cancelPending(token) })
                    }
                }
            } catch (_: Exception) {
                runCatching { crypto.deleteKey(alias) }
                // Password authentication already succeeded; retain a usable password-only Cookbook.
                passwordOnly(prepared, token)
            }
        }
    }
    private fun cancelPending(token: Long) {
        if (!epoch.accepts(token)) return
        pending?.close(); pending = null
        val alias = pendingAlias; pendingAlias = ""
        retire(alias)
        fail(token, "Biometric cancelled or unavailable. Use your master password.")
    }
    fun biometric() {
        if (!active || busy || unlocked) return
        if (!biometricAvailable()) { status = "Strong biometric unavailable. Use your master password."; return }
        busy = true; val token = epoch.begin()
        worker.execute {
            try {
                val record = repository.record() ?: throw IllegalStateException()
                check(record.alias.isNotEmpty())
                val cipher = crypto.unwrapCipher(record.alias, record.wrapIv)
                main.post {
                    if (active && epoch.accepts(token)) authenticate(cipher, { authorized ->
                        if (!active || !epoch.accepts(token)) return@authenticate
                        worker.execute {
                            try {
                                if (!epoch.accepts(token)) return@execute
                                val opened = repository.unwrap(authorized.doFinal(record.wrappedKey))
                                runCatching { repository.retireKeys(crypto::deleteKey) }
                                finish(token, opened, "Biometric round trip succeeded; ${opened.spices.size} spice jars authenticated")
                            } catch (_: Exception) { fail(token, "Device key unavailable. Use your original master password.") }
                        }
                    }, { fail(token, "Biometric failed or cancelled. Use your master password.") })
                }
            } catch (_: Exception) { fail(token, "Device key invalidated or missing. Use your original master password.") }
        }
    }
    private fun finish(token: Long, opened: VaultSession, message: String) {
        main.post {
            epoch.deliver(token, {
                if (!active) opened.close()
                else { session?.close(); session = opened; clock.begin(SystemClock.elapsedRealtime())
                    unlocked = true; busy = false; status = message }
            }, { opened.close() })
        }
    }
    private fun fail(token: Long, message: String) {
        main.post { if (active && epoch.accepts(token)) { busy = false; status = message } }
    }
}
