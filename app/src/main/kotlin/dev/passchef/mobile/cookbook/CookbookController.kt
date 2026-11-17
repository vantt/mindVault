package dev.passchef.mobile.cookbook

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.compose.runtime.*
import dev.passchef.mobile.VaultController
import java.io.File
import java.security.SecureRandom

/**
 * Cooking + sheet sources for the unlocked app. Dish (password) lives in [DishLifetime] only,
 * masked by default, wiped on edit/navigation/lock. [setFor] maps a sheet to its secret-part set (D17).
 */
class CookbookController(private val vault: VaultController, val clipboard: OwnedClipboard,
    val authorization: AndroidSheetsAuthorization, directory: File,
    private val setFor: (MenuOrigin?) -> String? = { null }) {
    val sources = CookbookSources(directory, vault::readyForCooking, onRejectedToken = authorization::clearRejectedToken)
    private val lifetime = DishLifetime()
    private val main = Handler(Looper.getMainLooper())
    private var readyDish by mutableStateOf(false)
    var dish by mutableStateOf(""); private set
    var revealed by mutableStateOf(false); private set
    var cooking by mutableStateOf(false); private set
    var code by mutableStateOf<String?>(null); private set
    var warning by mutableStateOf(false); private set
    var selected by mutableStateOf<MenuEntry?>(null); private set
    /** Label of the entry whose password was just copied from the list (D12); non-secret. */
    var copiedLabel by mutableStateOf<String?>(null); private set
    /** Sheet of the last refresh, so "Choose this sheet again" can open the filtered Picker. Non-secret. */
    var lastRefresh by mutableStateOf<MenuOrigin?>(null); private set
    private val hide = Runnable { revealed = false; dish = "" }
    var clipboardStatus by mutableStateOf<String?>(null); private set
    fun edit() { readyDish = false; lifetime.invalidate(); vault.invalidateDish(); main.removeCallbacks(hide); dish = ""; revealed = false; cooking = false; code = null; warning = false; clipboardStatus = null; copiedLabel = null }
    fun lock() { edit(); selected = null; authorization.invalidate(); sources.invalidate() }
    fun destroy() { lock(); sources.destroy() }
    fun open(entry: MenuEntry) { edit(); selected = entry; sources.recent(entry); cook(entry.recipe, entry.origin.menuId, entry.origin) }
    fun back() { edit(); selected = null }
    fun cook(text: String, menuId: String?) =
        cook(text, menuId, sources.snapshots.firstOrNull { it.mapping.origin.menuId == menuId }?.mapping?.origin)
    private fun cook(text: String, menuId: String?, origin: MenuOrigin?, then: (() -> Unit)? = null) {
        edit(); val token = lifetime.invalidate(); cooking = true
        vault.cook(text, menuId, setFor(origin)) { result, failure ->
            if (failure != null) { cooking = false; code = failure; return@cook }
            if (result != null && lifetime.publish(token, result.password, vault.readyForCooking())) {
                readyDish = true; cooking = false; warning = result.warning != null || menuId == null
                then?.invoke()
            }
        }
    }
    /** D12: cook and copy straight from a list row. The password is never shown, then wiped. */
    fun cookAndCopy(entry: MenuEntry) {
        selected = null
        cook(entry.recipe, entry.origin.menuId, entry.origin) {
            copyDish(); val status = clipboardStatus
            readyDish = false; lifetime.invalidate(); vault.invalidateDish()
            clipboardStatus = status; copiedLabel = entry.label
            sources.recent(entry)
        }
    }
    fun hasDish() = readyDish && lifetime.ready(vault.readyForCooking())
    fun reveal() {
        if (!hasDish()) return
        lifetime.reveal(SystemClock.elapsedRealtime(), true)
        dish = lifetime.visible(SystemClock.elapsedRealtime(), true).orEmpty(); revealed = true
        main.removeCallbacks(hide); main.postDelayed(hide, 10_000)
    }
    fun copyDish() {
        val text = lifetime.forCopy(vault.readyForCooking()) ?: return
        clipboardStatus = if (clipboard.copyDish(text)) "COPIED" else "COPY_FAILED"
    }
    fun copyUsername() { if (vault.readyForCooking()) selected?.let { clipboardStatus = if (clipboard.copyUsername(it.username)) "USERNAME_COPIED" else "COPY_FAILED" } }
    fun removeSource(origin: MenuOrigin) {
        if(selected?.origin?.key == origin.key) back()
        authorization.invalidate(); sources.remove(origin)
    }
    /** D9: offline table needs no Google id; a local id is generated. */
    fun offlineSource(title: String, text: String) {
        if(sources.busy) return
        edit(); authorization.invalidate()
        sources.offline(localMenuId { ByteArray(it).also(SecureRandom()::nextBytes) }, title, text)
    }
    /** Older entry point kept for proofs: a Sheet id/URL is now confirmed through the filtered Picker. */
    fun offlineSource(input: String, title: String, text: String) =
        if (input.isBlank()) offlineSource(title, text) else menuIdFromInput(input)?.let { sources.offline(it, title, text) } ?: run { code = "INVALID_MENU" }
    fun connect(input: String) {
        val id = menuIdFromInput(input)
        if (id == null) { edit(); code = "INVALID_MENU"; return }
        pick(id)
    }
    /** CM1 "Choose from Google Drive" (fileId null) or Paste/Share-in/re-pick (Picker filtered to fileId). */
    fun pick(fileId: String?) {
        if(sources.busy) return
        edit(); authorization.invalidate()
        if (!vault.readyForCooking()) { code = "LOCKED"; return }
        if(!sources.awaitGrant()) return
        authorization.pick(vault::readyForCooking, fileId) { grant, failure ->
            val id = grant?.pickedIds?.firstOrNull() ?: fileId?.takeIf { grant != null }
            if (grant == null || id == null) sources.disconnected(cancelled = failure == GrantFailure.CANCELLED)
            else sources.granted(grant.token, id)
        }
    }
    /** Resume the wizard after a lock: the file grant persists, so a silent grant reloads its tabs. */
    fun continueWith(fileId: String) {
        if (sources.busy) return
        edit(); authorization.invalidate()
        if(!sources.awaitGrant()) return
        authorization.silent(vault::readyForCooking) { grant, failure ->
            if (grant == null) sources.disconnected(cancelled = failure == GrantFailure.CANCELLED) else sources.granted(grant.token, fileId)
        }
    }
    /** Refresh a saved sheet: silent grant (no UI) then same tab + stored columns. */
    fun refresh(origin: MenuOrigin) {
        if (sources.busy || isLocalMenuId(origin.menuId)) return
        edit(); authorization.invalidate(); lastRefresh = origin
        if(!sources.awaitGrant()) return
        authorization.silent(vault::readyForCooking) { grant, _ ->
            if (grant == null) sources.disconnected() else sources.refresh(grant.token, origin)
        }
    }
}
