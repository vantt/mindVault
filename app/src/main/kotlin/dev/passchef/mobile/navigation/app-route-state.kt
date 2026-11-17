package dev.passchef.mobile.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.passchef.mobile.cookbook.ColumnRole

/** Bottom navigation destinations. Internal names stay Menu/Cook/Kitchen in testTags. */
enum class AppTab(val tag: String) { PASSWORDS("nav-menu"), ENTER_CODE("nav-cook"), SETTINGS("nav-kitchen") }

/** Pushed screens. Each holds ids only, never a password, secret part, token or reveal flag. */
sealed interface AppRoute {
    data class EntryDetail(val entryId: String) : AppRoute
    data object Connect : AppRoute
    data class SheetDetail(val originKey: String) : AppRoute
    data object SecretParts : AppRoute
    data object HowItWorks : AppRoute
}

/** Connect wizard step. The rows themselves live in CookbookSources and are wiped on lock. */
enum class ConnectStep { CHOOSE, TAB, MAP, REVIEW }

/** Non-secret mapping choices (column numbers as typed, first data row) kept across lock. */
data class MappingChoices(val columns: Map<ColumnRole, String>, val firstRow: String, val originKey: String? = null)

/** Where the user was in the wizard: which sheet, which tab, which columns. Ids/titles only. */
data class ConnectProgress(
    val step: ConnectStep = ConnectStep.CHOOSE,
    val fileId: String? = null,
    val tabTitle: String? = null,
    val mapping: MappingChoices? = null,
    val resumeAfterUnlock: Boolean = false
)

/**
 * Process-scope UI place that survives lock (spec §8, D5). Whitelist by type: tab, route ids,
 * wizard progress, list search/filter/scope, shared sheet id. Cleared on a fresh launch.
 */
class AppRouteState {
    var tab by mutableStateOf(AppTab.PASSWORDS); private set
    private val stacks = mutableStateMapOf<AppTab, List<AppRoute>>()
    var listQuery by mutableStateOf("")
    var listFilter by mutableStateOf("all")
    /** Origin key of the one sheet shown, or null for all sheets. */
    var listScope by mutableStateOf<String?>(null)
    var collapsedSheets by mutableStateOf<Set<String>>(emptySet())
    var connect by mutableStateOf(ConnectProgress())
    /** Sheet id from a Share-in or pasted link, waiting for the user to confirm in the wizard. */
    var sharedSheetId by mutableStateOf<String?>(null)
    /** Lock event already used for the one automatic fingerprint prompt (D6). */
    var autoBiometricEvent = -1L
    /** D6 auto-prompt switch; only debug proof hosts turn it off (their scripts unlock explicitly). */
    var autoBiometricEnabled = true

    val route: AppRoute? get() = stacks[tab]?.lastOrNull()
    val canGoBack: Boolean get() = route != null || tab != AppTab.PASSWORDS

    /** Reselecting the current tab pops it to its root. */
    fun select(next: AppTab) {
        if (next == tab) stacks[tab] = emptyList() else tab = next
    }
    fun push(next: AppRoute) {
        val stack = stacks[tab].orEmpty()
        if (stack.lastOrNull() != next) stacks[tab] = stack + next
    }
    /** Switch tab and push in one move (e.g. empty-state "Connect a sheet"). */
    fun open(target: AppTab, next: AppRoute) { tab = target; push(next) }
    fun pop(): Boolean {
        val stack = stacks[tab].orEmpty()
        if (stack.isEmpty()) return false
        stacks[tab] = stack.dropLast(1); return true
    }
    /** Spec §2: pushed screen pops; other tab root goes to Passwords; Passwords root returns false (finish). */
    fun back(): Boolean = when {
        pop() -> true
        tab != AppTab.PASSWORDS -> { tab = AppTab.PASSWORDS; true }
        else -> false
    }
    /** Drop routes that point at removed data, e.g. a deleted sheet. */
    fun removeWhere(predicate: (AppRoute) -> Boolean) {
        for ((key, stack) in stacks.toMap()) stacks[key] = stack.filterNot(predicate)
    }
    fun resetConnect() { connect = ConnectProgress() }
}

/** Survives Activity stop/lock in the same process; a fresh launch starts clean. */
object AppRouteStateHolder {
    var state = AppRouteState(); private set
    fun fresh() { state = AppRouteState() }
}
