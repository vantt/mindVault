package dev.passchef.mobile

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.biometric.BiometricManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.passchef.mobile.vault.*
import java.io.File
import java.util.concurrent.Executors
import javax.crypto.Cipher

/** One serial worker owns derivation/storage; stale results are always wiped. */
class VaultController(
    private val context: Context,
    private val authenticate: (Cipher, (Cipher) -> Unit, (BiometricPromptFailure) -> Unit) -> Unit,
    private val cancelPrompt: () -> Unit
) {
    var status by mutableStateOf("Cookbook locked")
        private set
    var busy by mutableStateOf(false)
        private set
    var unlocked by mutableStateOf(false)
        private set
    /** D3: gate phase computed on the worker, never by reading storage in composition. */
    var phase by mutableStateOf(VaultPhase.CHECKING)
        private set
    /** Increments on every lock; the unlock screen auto-prompts fingerprint once per event (D6). */
    var lockEvent by mutableStateOf(0L)
        private set
    /** Non-secret set labels + part numbers for Settings (D17). Empty while locked. */
    var setSummaries by mutableStateOf<List<SecretPartSetSummary>>(emptyList())
        private set
    private val worker = Executors.newSingleThreadExecutor()
    private val main = Handler(Looper.getMainLooper())
    private val repository = VaultRepository(AtomicRecordStore(File(context.noBackupFilesDir, "cookbook-v1.json")))
    private val crypto = KeystoreCrypto()
    private val clock = SessionClock()
    private var session: VaultSession? = null
    private val epoch = OperationEpoch()
    private var active = false
    var onLocked: () -> Unit = {}
    private val cooking = VaultCooking(worker, main, ::readyForCooking)
    fun readyForCooking(): Boolean = active && unlocked && !clock.expired(SystemClock.elapsedRealtime())
    fun invalidateDish() { cooking.invalidate() }
    fun cook(text: String, menuId: String?, callback: (dev.passchef.mobile.recipe.CookingResult?, String?) -> Unit) =
        cook(text, menuId, null, callback)
    /** [setId] = the sheet's secret-part set (D17); null = first ("Default") set. */
    fun cook(text: String, menuId: String?, setId: String?, callback: (dev.passchef.mobile.recipe.CookingResult?, String?) -> Unit) {
        cooking.invalidate()
        if (!readyForCooking()) { callback(null, "LOCKED"); return }
        val recipe = dev.passchef.mobile.recipe.RecipeParser.parse(text)
        if (recipe == null) { callback(null, "INVALID_RECIPE"); return }
        val jar = session?.set(setId)?.parts?.get(recipe.spiceIndex)?.copyOf()
        cooking.cook(text, menuId, jar, callback)
    }
    private val tickCallback = Runnable { tick() }
    private val wrapping = VaultWrapping(context, crypto, repository, worker, main, authenticate,
        { active && epoch.accepts(it) }, ::retire, ::passwordOnly, ::passwordLockout, ::finish, ::fail, { status = it })

    fun start() { active = true; main.removeCallbacks(tickCallback); tick(); refreshPhase() }
    /** Reads only whether a record exists and is wrapped; posts the result if still locked. */
    private fun refreshPhase() {
        if (unlocked) return
        runCatching { worker.execute {
            val next = runCatching {
                val record = repository.record()
                when {
                    record == null -> VaultPhase.NEW
                    record.alias.isNotEmpty() && biometricAvailable() -> VaultPhase.LOCKED_BIOMETRIC
                    else -> VaultPhase.LOCKED_PASSWORD
                }
            }.getOrDefault(VaultPhase.LOCKED_PASSWORD)
            main.post { if (!unlocked) phase = next }
        } }
    }
    fun touch() { clock.touch(SystemClock.elapsedRealtime()) }
    fun lock() {
        active = false; cooking.invalidate(); epoch.invalidate(); cancelPrompt(); main.removeCallbacks(tickCallback)
        wrapping.cancel()
        session?.close(); session = null; clock.lock()
        unlocked = false; busy = false; status = "Cookbook locked"; setSummaries = emptyList()
        if (phase == VaultPhase.UNLOCKED) phase = VaultPhase.CHECKING
        lockEvent++
        runCatching { onLocked() }
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

    /** First setup when no record exists, else recovery. [useBiometric]=false is SU3 "Not now". */
    fun password(password: ByteArray, spices: Map<Int, ByteArray>, useBiometric: Boolean = true) {
        if (!active || busy || unlocked) { password.fill(0); spices.values.forEach { it.fill(0) }; return }
        busy = true; status = "Opening the Cookbook…"
        val token = epoch.begin()
        worker.execute {
            try {
                val existing = repository.record()
                val firstSetup = existing == null
                // Re-wrap with fingerprint only for setup (user chose it) or a vault that already used it; a
                // password-only vault ("Not now") is never re-prompted on every password unlock.
                val wrap = useBiometric && (firstSetup || existing?.alias?.isNotEmpty() == true)
                val prepared = if (firstSetup) repository.setup(password, spices) else repository.recover(password)
                if (!main.post {
                    epoch.deliver(token, {
                        if (!active) prepared.close()
                        else if (!wrap || !biometricAvailable()) passwordOnly(prepared, token)
                        else wrapping.start(prepared, token, firstSetup)
                    }, { prepared.close() })
                }) prepared.close()
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
    private fun passwordLockout(prepared: PreparedVault, token: Long, firstSetup: Boolean) {
        if (!active || !epoch.accepts(token)) { prepared.close(); return }
        OwnedWork.submit(worker, prepared) {
            try {
                if (!epoch.accepts(token)) { prepared.close(); return@submit }
                val opened = PasswordLockoutSession.open(repository, prepared, firstSetup)
                finish(token, opened, "Cookbook open by password; biometrics are locked. Try them again later.")
            } catch (_: Exception) { prepared.close(); fail(token, "Could not save Cookbook; prior data preserved") }
        }
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
                            var phase = "unwrap-doFinal"
                            try {
                                if (!epoch.accepts(token)) return@execute
                                val unwrapped = authorized.doFinal(record.wrappedKey)
                                phase = "unwrap-cache"
                                val opened = repository.unwrap(unwrapped)
                                phase = "key-retirement"
                                runCatching { repository.retireKeys(crypto::deleteKey) }
                                phase = "session-delivery"
                                finish(token, opened, "Biometric round trip succeeded; ${opened.spices.size} spice jars authenticated")
                            } catch (error: Exception) {
                                WrapFailureProbe.record(phase, error) {
                                    if (android.os.Build.VERSION.SDK_INT >= 31) crypto.info(record.alias).securityLevel else -1
                                }
                                fail(token, "Device key unavailable. Use your original master password.")
                            }
                        }
                    }, { fail(token, "Biometric failed or cancelled. Use your master password.") })
                }
            } catch (_: Exception) { fail(token, "Device key invalidated or missing. Use your original master password.") }
        }
    }
    private fun finish(token: Long, opened: VaultSession, message: String) {
        if (!main.post {
            epoch.deliver(token, {
                if (!active) opened.close()
                else { session?.close(); session = opened; clock.begin(SystemClock.elapsedRealtime())
                    unlocked = true; busy = false; status = message
                    phase = VaultPhase.UNLOCKED; setSummaries = opened.summaries() }
            }, { opened.close() })
        }) opened.close()
    }
    /**
     * D8/D17 add a part or a set after setup. Re-auth with the master password on the worker;
     * the key does not change, so the fingerprint wrap stays valid. Password bytes are zeroed.
     */
    enum class UpdateOutcome { SAVED, REJECTED, TOO_LARGE }
    fun updateSets(password: ByteArray, change: (List<SecretPartSet>) -> List<SecretPartSet>, done: (UpdateOutcome) -> Unit) {
        if (!readyForCooking() || busy) { password.fill(0); done(UpdateOutcome.REJECTED); return }
        busy = true
        runCatching { worker.execute {
            var tooLarge = false
            val updated = try { repository.updateSets(password, change) }
                catch (error: Exception) { tooLarge = error is IllegalArgumentException && error.message == "Secret parts too large"; null }
                finally { password.fill(0) }
            if (!main.post {
                busy = false
                if (updated != null && readyForCooking()) { session?.close(); session = updated; setSummaries = updated.summaries(); done(UpdateOutcome.SAVED) }
                else { updated?.close(); done(if (tooLarge) UpdateOutcome.TOO_LARGE else UpdateOutcome.REJECTED) }
            }) updated?.close()
        } }.onFailure { password.fill(0); busy = false; done(UpdateOutcome.REJECTED) }
    }
    private fun fail(token: Long, message: String) {
        main.post { if (active && epoch.accepts(token)) { busy = false; status = message } }
    }
}
