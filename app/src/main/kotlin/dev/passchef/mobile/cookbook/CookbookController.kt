package dev.passchef.mobile.cookbook

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.compose.runtime.*
import dev.passchef.mobile.VaultController
import java.io.File

class CookbookController(private val vault: VaultController, val clipboard: OwnedClipboard,
    val authorization: AndroidSheetsAuthorization, directory: File) {
    val sources = CookbookSources(directory, vault::readyForCooking)
    private val lifetime = DishLifetime()
    private val main = Handler(Looper.getMainLooper())
    private var readyDish by mutableStateOf(false)
    var dish by mutableStateOf(""); private set
    var revealed by mutableStateOf(false); private set
    var cooking by mutableStateOf(false); private set
    var code by mutableStateOf<String?>(null); private set
    var warning by mutableStateOf(false); private set
    var selected by mutableStateOf<MenuEntry?>(null); private set
    private val hide = Runnable { revealed = false; dish = "" }
    var clipboardStatus by mutableStateOf<String?>(null); private set
    fun edit() { readyDish = false; lifetime.invalidate(); vault.invalidateDish(); main.removeCallbacks(hide); dish = ""; revealed = false; cooking = false; code = null; warning = false; clipboardStatus = null }
    fun lock() { edit(); selected = null; authorization.invalidate(); sources.invalidate() }
    fun destroy() { lock(); sources.destroy() }
    fun open(entry: MenuEntry) { edit(); selected = entry; sources.recent(entry); cook(entry.recipe, entry.origin.menuId) }
    fun back() { edit(); selected = null }
    fun cook(text: String, menuId: String?) {
        edit(); val token = lifetime.invalidate(); cooking = true
        vault.cook(text, menuId) { result, failure ->
            if (failure != null) { cooking = false; code = failure; return@cook }
            if (result != null && lifetime.publish(token, result.password, vault.readyForCooking())) {
                readyDish = true; cooking = false; warning = result.warning != null || menuId == null
            }
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
    fun copyUsername() { if (vault.readyForCooking()) selected?.let { clipboardStatus = if (clipboard.copyUsername(it.username)) "COPIED" else "COPY_FAILED" } }
    fun removeSource(origin: MenuOrigin) {
        if(selected?.origin?.key == origin.key) back()
        authorization.invalidate(); sources.remove(origin)
    }
    fun offlineSource(input:String,title:String,text:String) {
        if(sources.busy) return
        edit(); authorization.invalidate()
        menuIdFromInput(input)?.let { sources.offline(it,title,text) } ?: run { code="INVALID_MENU" }
    }
    fun connect(input: String) {
        if(sources.busy) return
        edit(); authorization.invalidate()
        val id = menuIdFromInput(input)
        if (!vault.readyForCooking() || id == null) { code = "INVALID_MENU"; return }
        if(!sources.awaitGrant()) return
        authorization.connect(vault::readyForCooking) { token ->
            if (token == null) sources.disconnected() else sources.granted(token, id)
        }
    }
}
