package dev.passchef.mobile.cookbook

import androidx.compose.runtime.*
import com.passchef.sheets.*
import kotlinx.coroutines.*
import java.io.File

data class MappingDraft(val origin: MenuOrigin, val rows: List<List<String>>)
class CookbookSources(directory: File, private val ready: () -> Boolean, private val io: CoroutineDispatcher = Dispatchers.IO) {
    private val store = MenuSnapshotStore(directory)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val ownership = SourceOwnership()
    private var accessToken: String? = null
    var snapshots by mutableStateOf<List<MenuSnapshot>>(emptyList()); private set
    var draft by mutableStateOf<MappingDraft?>(null); private set
    var metadata by mutableStateOf<SpreadsheetMetadata?>(null); private set
    var status by mutableStateOf("OFFLINE"); private set
    var busy by mutableStateOf(false); private set
    private fun current(token: SourceTicket) = ownership.accepts(token, ready())
    fun invalidate() { ownership.invalidate(); scope.coroutineContext.cancelChildren(); accessToken = null; draft = null; metadata = null; busy = false; status = "OFFLINE" }
    fun destroy() { invalidate(); scope.cancel() }
    fun reload() {
        if (!ready() || busy) return
        busy=true; status="LOADING"
        val token = ownership.begin("inventory")
        scope.launch {
            val inventory = runCatching { withContext(io) { store.inventory() } }.getOrNull()
            if (current(token)) {
                if(inventory != null) snapshots = inventory.snapshots
                status = if (inventory == null || inventory.corruptFiles > 0) "CORRUPT_CACHE" else "OFFLINE"
                busy=false
            }
        }
    }
    fun awaitGrant(): Boolean {
        if(!ready() || busy) return false
        invalidate(); busy=true; status="LOADING"; return true
    }
    fun disconnected() { if (ready()) { busy = false; status = "CONNECT_REQUIRED" } }
    fun granted(token: String, id: String) {
        if (!ready()) return
        invalidate(); accessToken = token; busy = true; status = "LOADING"
        val request = ownership.begin(id)
        scope.launch {
            try {
                val data = withContext(io) { SheetsMenus.metadata(token, id) }
                if (current(request)) { metadata = data; status = "CHOOSE_TAB" }
            } catch (error: Exception) { if (current(request)) failure(error) }
            finally { if (current(request)) busy = false }
        }
    }
    fun choose(tab: TabProperties) {
        val document = metadata ?: return
        val token = accessToken ?: return
        if (!ready() || busy || document.sheets.none { it.properties == tab }) return
        val request = ownership.begin("${document.spreadsheetId}:${tab.sheetId}"); busy = true; draft = null; status = "LOADING"
        scope.launch {
            try {
                val rows = withContext(io) { SheetsMenus.rows(token, document.spreadsheetId, tab) }
                if (current(request)) stage(MenuOrigin(document.spreadsheetId, tab.sheetId, tab.title), rows, request)
            } catch (error: Exception) { if (current(request)) failure(error) }
            finally { if (current(request)) busy = false }
        }
    }
    private suspend fun stage(origin: MenuOrigin, rows: List<List<String>>, request: SourceTicket) {
        MenuTable.validate(rows)
        val previous = snapshots.find { it.mapping.origin.key == origin.key }
        if (previous != null) {
            val refreshed = runCatching { MenuTable.refresh(rows, previous, System.currentTimeMillis()) }.getOrNull()
            if (refreshed != null) { if(save(refreshed,request)) status = "SYNCED"; return }
            status = "RECONFIRM"
        } else status = "CONFIRM"
        draft = MappingDraft(origin, rows)
    }
    fun offline(id: String, title: String, text: String) {
        if (!ready() || busy) return
        invalidate()
        val request = ownership.begin("$id:0"); busy=true; status="LOADING"
        scope.launch {
            try {
                require(text.length <= 120_000)
                val rows = if (text.isEmpty()) emptyList() else text.lineSequence().take(MAX_ROWS + 1).map { it.split('\t') }.toList()
                if(current(request)) stage(MenuOrigin(id, 0, title), rows, request)
            } catch (_: Exception) { if(current(request)) status = "INVALID_TABLE" }
            finally { if(current(request)) busy=false }
        }
    }
    fun confirm(columns: Map<ColumnRole, Int>, firstRow: Int, labels: Map<Int, String>) {
        if (!ready() || busy) return
        val staged = draft ?: return
        val request = ownership.begin(staged.origin.key); busy = true
        scope.launch {
            try {
                val mapping = MenuMapping(staged.origin, columns, firstRow,
                    if (firstRow > 1) staged.rows.getOrNull(firstRow - 2) else null, labels)
                val entries = MenuTable.project(staged.rows, mapping)
                if(save(MenuSnapshot(mapping = mapping, entries = entries, refreshedAt = System.currentTimeMillis()),request)) {
                    draft = null; status = "SYNCED"
                }
            } catch (_: Exception) { if(current(request)) status = "INVALID_MAPPING" }
            finally { if(current(request)) busy = false }
        }
    }
    private suspend fun save(snapshot: MenuSnapshot, request: SourceTicket): Boolean {
        val committed = withContext(io) { ownership.commit(request, ready) { store.save(snapshot) } }
        if (!committed || !current(request)) return false
        snapshots = snapshots.filterNot { it.mapping.origin.key == snapshot.mapping.origin.key } + snapshot
        return true
    }
    fun favorite(entry: MenuEntry) {
        if (!ready() || busy) return
        val saved = snapshots.find { it.mapping.origin == entry.origin } ?: return
        val ids = if (entry.id in saved.favorites) saved.favorites - entry.id else saved.favorites + entry.id
        val request=ownership.begin(entry.origin.key); busy=true
        scope.launch {
            try { save(saved.copy(favorites = ids),request) }
            catch (_:Exception) { if(current(request)) status="CACHE_FAILED" }
            finally { if(current(request)) busy=false }
        }
    }
    fun recent(entry: MenuEntry) {
        if (!ready() || busy) return
        snapshots.find { it.mapping.origin == entry.origin }?.let { saved ->
            val request=ownership.begin(entry.origin.key); busy=true
            scope.launch {
                try { save(saved.copy(recent = (listOf(entry.id) + saved.recent.filterNot { it == entry.id }).take(20)),request) }
                catch (_:Exception) { if(current(request)) status="CACHE_FAILED" }
                finally { if(current(request)) busy=false }
            }
        }
    }
    fun remove(origin: MenuOrigin) {
        if (!ready() || busy) return
        ownership.invalidate(); scope.coroutineContext.cancelChildren(); draft = null; metadata = null; busy=false
        val request = ownership.begin(origin.key); busy=true
        scope.launch {
            try {
                val removed = withContext(io) { ownership.commit(request, ready) { store.remove(origin) } }
                if(removed && current(request)) snapshots = snapshots.filterNot { it.mapping.origin.key == origin.key }
            } catch (_: Exception) { if(current(request)) status = "CACHE_FAILED" }
            finally { if(current(request)) busy=false }
        }
    }
    private fun failure(error: Exception) {
        status = when (error) { is SheetsUnauthorizedException -> "CONNECT_REQUIRED"; is SheetsForbiddenException -> "ACCESS_DENIED"; else -> "NETWORK_FAILED" }
        if (error is SheetsUnauthorizedException) accessToken = null
    }
}
