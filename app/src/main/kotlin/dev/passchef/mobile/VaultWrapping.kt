package dev.passchef.mobile

import android.content.Context
import android.os.Build
import android.os.Handler
import dev.passchef.mobile.vault.*
import java.util.UUID
import java.util.concurrent.Executor
import javax.crypto.Cipher

/** Owns the prepared Cookbook and disposable alias through both real prompt attempts. */
class VaultWrapping(
    private val context: Context, private val crypto: KeystoreCrypto,
    private val repository: VaultRepository, private val worker: Executor, private val main: Handler,
    private val authenticate: (Cipher, (Cipher) -> Unit, (BiometricPromptFailure) -> Unit) -> Unit,
    private val current: (Long) -> Boolean, private val retire: (String) -> Unit,
    private val passwordOnly: (PreparedVault, Long) -> Unit,
    private val passwordLockout: (PreparedVault, Long, Boolean) -> Unit,
    private val finish: (Long, VaultSession, String) -> Unit,
    private val fail: (Long, String) -> Unit, private val status: (String) -> Unit
) {
    @Volatile private var pending: WrapOperationState? = null
    fun cancel() { pending?.close(); pending = null }
    private fun live(op: WrapOperationState, token: Long) = current(token) && op.active()
    private fun submit(op: WrapOperationState, action: () -> Unit) {
        OwnedWork.submit(worker, op) {
                if (!op.beginWork()) return@submit
                try { action() } finally { op.endWork() }
        }
    }
    private fun post(op: WrapOperationState, action: () -> Unit) {
        op.postOwned({ main.post(it) }, action)
    }
    fun start(prepared: PreparedVault, token: Long, firstSetup: Boolean) {
        val alias = "passchef-${UUID.randomUUID()}"
        val op = WrapOperationState(prepared) { retire(alias) }
        pending = op
        submit(op) {
            if (!live(op, token)) { op.close(); return@submit }
            try {
                val hardware = crypto.createKey(alias, context.packageManager.hasSystemFeature("android.hardware.strongbox_keystore"))
                val cipher = crypto.wrapCipher(alias)
                prompt(op, prepared, alias, token, hardware, cipher, firstSetup)
            } catch (_: Exception) {
                // Preserve the established initial hardware-unavailable password fallback.
                if (live(op, token) && op.transferOwned()) { op.close(); passwordOnly(prepared, token) }
                else op.close()
            }
        }
    }
    private fun prompt(op: WrapOperationState, prepared: PreparedVault, alias: String, token: Long, hardware: Boolean, cipher: Cipher, firstSetup: Boolean) {
        post(op) {
            if (!live(op, token)) { op.close(); return@post }
            try {
                val id = op.newPrompt(cipher)
                authenticate(cipher, { authorized ->
                    if (!live(op, token) || !op.authorize(id, authorized)) return@authenticate
                    submit(op) { complete(op, prepared, alias, token, hardware, authorized) }
                }, { reason ->
                    if (live(op, token) && op.authorize(id, cipher)) {
                        if (op.transferForLockout(reason)) { op.close(); passwordLockout(prepared, token, firstSetup) }
                        else { op.close(); fail(token, "Biometric cancelled or unavailable. Use your master password.") }
                    }
                })
            } catch (_: Exception) { op.close(); fail(token, "Biometric unavailable. Use your master password.") }
        }
    }
    private fun complete(op: WrapOperationState, prepared: PreparedVault, alias: String, token: Long, hardware: Boolean, cipher: Cipher) {
        var phase = "wrap-doFinal"
        try {
            if (!live(op, token)) { op.close(); return }
            val wrapped = cipher.doFinal(prepared.session.key)
            phase = "record-commit"
            if (!live(op, token)) { op.close(); return }
            repository.commitWrap(prepared, alias, cipher.iv, wrapped)
            op.markCommitted()
            phase = "key-retirement"
            val retired = runCatching { repository.retireKeys(crypto::deleteKey) }.getOrDefault(false)
            val cleanup = if (retired) "" else "; prior device key cleanup pending"
            phase = "session-delivery"
            if (op.transferOwned()) finish(token, prepared.session, "Round trip ready; device key hardware protected: $hardware$cleanup")
            op.close()
        } catch (error: Exception) {
            WrapFailureProbe.record(phase, error) { if (Build.VERSION.SDK_INT >= 31) crypto.info(alias).securityLevel else -1 }
            if (live(op, token) && op.retry(Build.VERSION.SDK_INT, ExpiredWrapFailure.code(error), phase))
                scheduleRetry(op, prepared, alias, token, hardware)
            else { op.close(); fail(token, "Could not save biometric wrap; prior data preserved. Use your master password.") }
        }
    }
    private fun scheduleRetry(op: WrapOperationState, prepared: PreparedVault, alias: String, token: Long, hardware: Boolean) {
        post(op) {
            if (!live(op, token)) { op.close(); return@post }
            status("Please authorize the Cookbook again")
            op.delayOwned({ task, delay -> main.postDelayed(task, delay) }, { main.removeCallbacks(it) }) {
                if (!live(op, token)) { op.close(); return@delayOwned }
                submit(op) {
                    if (!live(op, token)) { op.close(); return@submit }
                    try { prompt(op, prepared, alias, token, hardware, crypto.wrapCipher(alias), false) }
                    catch (_: Exception) { op.close(); fail(token, "Device operation unavailable. Use your master password; prior data preserved.") }
                }
            }
        }
    }
}
