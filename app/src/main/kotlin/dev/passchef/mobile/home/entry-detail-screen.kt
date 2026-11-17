package dev.passchef.mobile.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.cookbook.*
import dev.passchef.mobile.navigation.AppRouteState
import dev.passchef.mobile.recipe.RecipeParser
import dev.passchef.mobile.settings.AppPreferences
import dev.passchef.mobile.ui.*

/**
 * RD1: username → password (auto-got on open, masked) → website → notes → written code + gloss.
 * Restores after unlock: the route keeps the entry id; the password is got again, never restored.
 */
@Composable fun EntryDetailScreen(cookbook: CookbookController, routes: AppRouteState, entryId: String, preferences: AppPreferences) {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String, vn: String) = words(vi, en, vn)
    val snapshots = cookbook.sources.snapshots
    val saved = snapshots.find { s -> s.entries.any { it.id == entryId } }
    val entry = saved?.entries?.find { it.id == entryId }
    LaunchedEffect(entryId, entry != null) { if (entry != null && cookbook.selected?.id != entryId) cookbook.open(entry) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { cookbook.back(); routes.pop() }, modifier = Modifier.semantics { contentDescription = t("Back", "Quay lại") }) {
                Icon(PassChefIcons.Back, contentDescription = null, tint = PassChefPalette.Text)
            }
            Text(entry?.label ?: t("Not found", "Không tìm thấy"), Modifier.weight(1f), style = MaterialTheme.typography.titleLarge,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (saved != null && entry != null) {
                val starred = entry.id in saved.favorites
                IconButton(onClick = { cookbook.sources.favorite(entry) }, enabled = !cookbook.sources.busy,
                    modifier = Modifier.semantics { contentDescription = if (starred) t("Remove favorite", "Bỏ yêu thích") else t("Add favorite", "Yêu thích") }) {
                    Icon(PassChefIcons.Star, contentDescription = null, tint = if (starred) PassChefPalette.AccentInk else PassChefPalette.Muted)
                }
            }
        }
        if (entry == null || saved == null) {
            Text(t("This password is no longer in the saved sheet. It may have been removed or refreshed.",
                "Mật khẩu này không còn trong bảng tính đã lưu. Có thể đã bị xóa hoặc làm mới."), color = PassChefPalette.Secondary)
            return@Column
        }
        TextButton(onClick = { cookbook.back(); routes.listScope = entry.origin.key; routes.pop() }, modifier = Modifier.testTag("detail-sheet-chip")) {
            PassChefCaption(t("From sheet: ", "Từ bảng tính: ") + entry.origin.title + " · " + t("row ", "hàng ") + entry.row,
                color = PassChefPalette.AccentInk)
        }
        if (entry.username.isNotEmpty()) {
            PassChefCaption(t("Username", "Tên đăng nhập"))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(entry.username, Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
                CookbookGhostAction(t("Copy", "Chép"), onClick = cookbook::copyUsername)
            }
        }
        DishPanel(cookbook)
        if (!cookbook.hasDish() && !cookbook.cooking) CookbookSecondaryAction(t("Get password", "Lấy mật khẩu"),
            onClick = { cookbook.open(entry) }, modifier = Modifier.fillMaxWidth())
        if (entry.url.isNotEmpty()) { PassChefCaption(t("Website", "Trang web")); Text(entry.url) }
        if (entry.notes.isNotEmpty()) { PassChefCaption(t("Notes", "Ghi chú")); Text(entry.notes) }
        PassChefCaption(t("Written code", "Mã đã ghi"))
        WrittenCode(entry.recipe, LocalSheetSetNames.current[entry.origin.key])
        if (RecipeParser.parse(entry.recipe)?.tag != null) Text(t("Has a check tag: PassChef confirms it belongs to this sheet when getting the password.",
            "Có tag kiểm tra: PassChef xác nhận mã thuộc bảng tính này khi lấy mật khẩu."), style = MaterialTheme.typography.bodySmall, color = PassChefPalette.Muted)
        Spacer(Modifier.height(16.dp))
    }
}
