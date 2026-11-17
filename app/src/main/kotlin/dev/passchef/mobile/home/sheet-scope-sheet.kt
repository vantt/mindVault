package dev.passchef.mobile.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.cookbook.CookbookController
import dev.passchef.mobile.cookbook.LocalCookbookVietnamese
import dev.passchef.mobile.cookbook.isLocalMenuId
import dev.passchef.mobile.cookbook.words
import dev.passchef.mobile.navigation.AppRouteState
import dev.passchef.mobile.navigation.AppTab
import dev.passchef.mobile.ui.*

/** MN2 "Show passwords from": All, or one sheet (colour, name, count, age/status, refresh). Choice remembered (D5). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun SheetScopeSheet(cookbook: CookbookController, routes: AppRouteState, onDismiss: () -> Unit) {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String, vn: String) = words(vi, en, vn)
    val snapshots = cookbook.sources.snapshots
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = PassChefPalette.Surface) {
        Column(Modifier.padding(horizontal = 16.dp).padding(bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(t("Show passwords from", "Hiện mật khẩu từ"), style = MaterialTheme.typography.titleMedium)
            ScopeRow(t("All sheets (${snapshots.size})", "Tất cả bảng tính (${snapshots.size})"), null,
                snapshots.sumOf { it.entries.size }.toString(), routes.listScope == null, "scope-option-all") {
                routes.listScope = null; onDismiss()
            }
            snapshots.forEachIndexed { index, saved ->
                val key = saved.mapping.origin.key
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.weight(1f)) {
                        ScopeRow(saved.mapping.origin.title, sheetColor(index), "${saved.entries.size} · " + ageWords(saved.refreshedAt, vi),
                            routes.listScope == key, "scope-option-$key") { routes.listScope = key; onDismiss() }
                    }
                    if (!isLocalMenuId(saved.mapping.origin.menuId)) TextButton(onClick = { cookbook.refresh(saved.mapping.origin) },
                        enabled = !cookbook.sources.busy) { Icon(PassChefIcons.Refresh, contentDescription = t("Refresh", "Làm mới")) }
                }
            }
            HorizontalDivider(color = PassChefPalette.Hairline)
            CookbookGhostAction(t("Manage in Settings", "Quản lý trong Cài đặt"), onClick = { onDismiss(); routes.select(AppTab.SETTINGS) })
            Text(t("Your choice is remembered next time.", "Lựa chọn được nhớ cho lần sau."), style = MaterialTheme.typography.bodySmall,
                color = PassChefPalette.Muted)
        }
    }
}

@Composable private fun ScopeRow(title: String, color: androidx.compose.ui.graphics.Color?, detail: String, selected: Boolean,
    tag: String, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = 56.dp).clickable(onClick = onClick).testTag(tag).padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected, onClick)
        if (color != null) Box(Modifier.size(10.dp).background(color, CircleShape))
        Spacer(Modifier.width(10.dp))
        Column {
            Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(detail, style = MaterialTheme.typography.bodySmall, color = PassChefPalette.Muted)
        }
    }
}
