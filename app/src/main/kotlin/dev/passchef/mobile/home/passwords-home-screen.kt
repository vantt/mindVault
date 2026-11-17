package dev.passchef.mobile.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.cookbook.*
import dev.passchef.mobile.navigation.AppRoute
import dev.passchef.mobile.navigation.AppRouteState
import dev.passchef.mobile.navigation.AppTab
import dev.passchef.mobile.settings.AppPreferences
import dev.passchef.mobile.ui.*

/**
 * MN1–MN3 (v3): scope selector (all sheets / one), filter chips (All · Favorites · Recent), search
 * across label/username/site/notes. "All" scope groups rows in collapsible per-sheet sections; search
 * results are flat with a sheet badge. Search/filter/scope/collapse survive lock (D5, non-secret).
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable fun PasswordsHomeScreen(cookbook: CookbookController, routes: AppRouteState, preferences: AppPreferences) {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String, vn: String) = words(vi, en, vn)
    val sources = cookbook.sources
    val snapshots = sources.snapshots
    var scopeOpen by remember { mutableStateOf(false) }
    val scoped = snapshots.filter { routes.listScope == null || it.mapping.origin.key == routes.listScope }
    LaunchedEffect(snapshots, routes.listScope) {
        if (routes.listScope != null && snapshots.none { it.mapping.origin.key == routes.listScope }) routes.listScope = null
    }
    Column(Modifier.fillMaxSize()) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (snapshots.isNotEmpty()) TextButton(onClick = { scopeOpen = true }, modifier = Modifier.testTag("scope-selector")) {
                val name = scoped.singleOrNull()?.takeIf { routes.listScope != null || snapshots.size == 1 }?.mapping?.origin?.title
                Text((name ?: t("All sheets (${snapshots.size})", "Tất cả bảng tính (${snapshots.size})")) + if (snapshots.size > 1) "  ▾" else "",
                    style = MaterialTheme.typography.titleMedium, color = PassChefPalette.Text, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            if (sources.status !in setOf("OFFLINE", "SYNCED", "LOADING") || sources.busy)
                CookbookStatus(cookbookMessage(sources.status, vi), tone = if (isProblem(sources.status)) CookbookTone.Warning else CookbookTone.Neutral)
            if (sources.status == "ACCESS_DENIED") cookbook.lastRefresh?.let { origin ->
                CookbookSecondaryAction(t("Choose this sheet again", "Chọn lại bảng tính này"), onClick = { cookbook.pick(origin.menuId) })
            }
            if (snapshots.isNotEmpty()) {
                OutlinedTextField(routes.listQuery, { if (it.length <= 200) routes.listQuery = it }, singleLine = true,
                    label = { Text(t("Search name, username, site, notes", "Tìm tên, tài khoản, trang web, ghi chú")) },
                    leadingIcon = { Icon(PassChefIcons.Search, contentDescription = null) },
                    trailingIcon = { if (routes.listQuery.isNotEmpty()) TextButton(onClick = { routes.listQuery = "" }) { Text("✕") } },
                    modifier = Modifier.fillMaxWidth().testTag("menu-search"))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for ((id, en, vn) in listOf(Triple("all", "All", "Tất cả"), Triple("favorites", "Favorites", "Yêu thích"), Triple("recent", "Recent", "Gần đây")))
                        CookbookChoice(t(en, vn), isSelected = routes.listFilter == id, onClick = { routes.listFilter = id },
                            modifier = Modifier.testTag("menu-chip-$id"))
                }
            }
        }
        if (snapshots.isEmpty()) EmptyPasswords(sources.busy, onConnect = { routes.open(AppTab.SETTINGS, AppRoute.Connect) },
            onByHand = { routes.select(AppTab.ENTER_CODE) })
        else PasswordList(cookbook, routes, scoped, snapshots)
    }
    if (scopeOpen) SheetScopeSheet(cookbook, routes, onDismiss = { scopeOpen = false })
}

@Composable private fun PasswordList(cookbook: CookbookController, routes: AppRouteState, scoped: List<MenuSnapshot>, all: List<MenuSnapshot>) {
    val vi = LocalCookbookVietnamese.current
    val busy = cookbook.sources.busy
    fun filtered(saved: MenuSnapshot) = when (routes.listFilter) {
        "favorites" -> saved.entries.filter { it.id in saved.favorites }
        "recent" -> saved.recent.mapNotNull { id -> saved.entries.find { it.id == id } }
        else -> saved.entries
    }
    fun colorOf(saved: MenuSnapshot) = sheetColor(all.indexOf(saved))
    val searching = routes.listQuery.isNotBlank()
    val grouped = !searching && routes.listScope == null && all.size > 1
    val open: (MenuEntry) -> Unit = { routes.push(AppRoute.EntryDetail(it.id)) }
    LazyColumn(Modifier.fillMaxSize()) {
        if (grouped) for (saved in scoped) {
            val key = saved.mapping.origin.key
            val collapsed = key in routes.collapsedSheets
            item(key = "h:$key") {
                SheetSectionHeader(saved, colorOf(saved), collapsed, busy,
                    onToggle = { routes.collapsedSheets = if (collapsed) routes.collapsedSheets - key else routes.collapsedSheets + key },
                    onRefresh = { cookbook.refresh(saved.mapping.origin) })
            }
            if (!collapsed) items(filtered(saved), key = { it.id }) { entry ->
                PasswordListRow(entry, colorOf(saved), null, busy, { open(entry) }, { cookbook.cookAndCopy(entry) })
            }
        } else {
            val rows = scoped.flatMap { saved -> searchEntries(filtered(saved), routes.listQuery).map { saved to it } }
            item(key = "count") {
                PassChefCaption(if (searching) words(vi, "${rows.size} results · ${rows.map { it.first }.distinct().size} sheets",
                    "${rows.size} kết quả · ${rows.map { it.first }.distinct().size} bảng tính") else words(vi, "${rows.size} passwords", "${rows.size} mật khẩu"),
                    Modifier.padding(horizontal = 16.dp, vertical = 6.dp))
            }
            if (rows.isEmpty()) item(key = "none") {
                Text(words(vi, if (searching) "Nothing matches “${routes.listQuery.take(40)}”." else "Nothing here yet.",
                    if (searching) "Không có kết quả cho “${routes.listQuery.take(40)}”." else "Chưa có gì ở đây."),
                    Modifier.padding(16.dp), color = PassChefPalette.Muted)
            }
            items(rows, key = { it.second.id }) { (saved, entry) ->
                PasswordListRow(entry, colorOf(saved), if (searching && all.size > 1) saved.mapping.origin.title else null, busy,
                    { open(entry) }, { cookbook.cookAndCopy(entry) })
            }
        }
        item(key = "end") { Spacer(Modifier.height(24.dp)) }
    }
}

@Composable private fun EmptyPasswords(busy: Boolean, onConnect: () -> Unit, onByHand: () -> Unit) {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String, vn: String) = words(vi, en, vn)
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(t("No passwords yet", "Chưa có mật khẩu"), style = PassChefDisplay)
        Text(t("Connect the Google Sheet that holds your written codes. PassChef saves a copy on this phone and turns each code into its password.",
            "Kết nối bảng tính Google chứa các mã đã ghi. PassChef lưu một bản trên điện thoại và biến mỗi mã thành mật khẩu."),
            color = PassChefPalette.Secondary)
        CookbookPrimaryAction(t("Connect a sheet", "Kết nối bảng tính"), onClick = onConnect, enabled = !busy,
            modifier = Modifier.fillMaxWidth().testTag("empty-connect"))
        CookbookGhostAction(t("Enter a code by hand", "Nhập mã bằng tay"), onClick = onByHand)
    }
}
