package dev.passchef.mobile.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.cookbook.*
import dev.passchef.mobile.home.ageWords
import dev.passchef.mobile.navigation.AppRoute
import dev.passchef.mobile.navigation.AppRouteState
import dev.passchef.mobile.navigation.ConnectProgress
import dev.passchef.mobile.ui.*

/** KT2: refresh, check columns, choose again (lost access), secret-part set (D17, ≥2 sets), remove. */
@OptIn(ExperimentalLayoutApi::class)
@Composable fun SheetDetailScreen(vault: VaultController, cookbook: CookbookController, routes: AppRouteState,
    preferences: AppPreferences, originKey: String) {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String, vn: String) = words(vi, en, vn)
    val sources = cookbook.sources
    val saved = sources.snapshots.find { it.mapping.origin.key == originKey }
    var confirmRemove by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { routes.pop() }) { Icon(PassChefIcons.Back, contentDescription = t("Back", "Quay lại"), tint = PassChefPalette.Text) }
            Text(saved?.mapping?.origin?.title ?: t("Sheet removed", "Đã xóa bảng tính"), style = MaterialTheme.typography.titleLarge)
        }
        if (saved == null) return@Column
        val origin = saved.mapping.origin
        val local = isLocalMenuId(origin.menuId)
        PassChefCaption("${saved.entries.size} · " + ageWords(saved.refreshedAt, vi) + if (local) t(" · offline table", " · bảng ngoại tuyến") else "")
        if (sources.busy || isProblem(sources.status)) CookbookStatus(cookbookMessage(sources.status, vi),
            tone = if (isProblem(sources.status)) CookbookTone.Error else CookbookTone.Warning)
        if (!local) {
            CookbookSecondaryAction(t("Refresh now", "Làm mới ngay"), onClick = { cookbook.refresh(origin) }, enabled = !sources.busy,
                modifier = Modifier.fillMaxWidth().testTag("kitchen-refresh-$originKey"))
            CookbookSecondaryAction(t("Check columns", "Kiểm tra cột"), enabled = !sources.busy, modifier = Modifier.fillMaxWidth(), onClick = {
                routes.connect = ConnectProgress(fileId = origin.menuId); routes.push(AppRoute.Connect); cookbook.continueWith(origin.menuId)
            })
            CookbookSecondaryAction(t("Choose this sheet again in Google", "Chọn lại bảng tính này với Google"), enabled = !sources.busy,
                modifier = Modifier.fillMaxWidth(), onClick = { cookbook.pick(origin.menuId) })
            Text(t("Use this if PassChef lost access, or after the update that narrowed Google access to picked sheets only.",
                "Dùng khi PassChef mất quyền, hoặc sau bản cập nhật chỉ xin quyền với bảng tính bạn chọn."),
                style = MaterialTheme.typography.bodySmall, color = PassChefPalette.Muted)
        }
        val sets = vault.setSummaries
        if (sets.size > 1) {
            PassChefCaption(t("Secret-part set for this sheet", "Bộ phần bí mật cho bảng tính này"))
            val bound = preferences.sheetSets[originKey] ?: sets.first().id
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                for (set in sets) CookbookChoice(set.name, isSelected = bound == set.id, modifier = Modifier.testTag("sheet-set-${set.id}"),
                    onClick = { cookbook.edit(); preferences.bindSheet(originKey, set.id.takeIf { it != sets.first().id }) })
            }
        }
        CookbookGhostAction(t("Remove from this phone", "Xóa khỏi điện thoại"), onClick = { confirmRemove = true },
            enabled = !sources.busy, modifier = Modifier.testTag("kitchen-remove-$originKey"))
    }
    if (confirmRemove && saved != null) AlertDialog(onDismissRequest = { confirmRemove = false },
        title = { Text(t("Remove this sheet from the phone?", "Xóa bảng tính này khỏi điện thoại?")) },
        text = { Text(t("Only the saved copy here is removed. Your Google Sheet and secret parts are not touched.",
            "Chỉ xóa bản lưu ở đây. Bảng tính Google và phần bí mật không bị đụng tới.")) },
        confirmButton = { TextButton(onClick = {
            confirmRemove = false; cookbook.removeSource(saved.mapping.origin); preferences.bindSheet(originKey, null)
            routes.removeWhere { it == AppRoute.SheetDetail(originKey) || (it is AppRoute.EntryDetail && it.entryId.startsWith("$originKey:")) }
            if (routes.listScope == originKey) routes.listScope = null
        }) { Text(t("Remove", "Xóa"), color = PassChefPalette.Coral) } },
        dismissButton = { CookbookGhostAction(t("Cancel", "Hủy"), onClick = { confirmRemove = false }) })
}
