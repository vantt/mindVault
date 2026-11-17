package dev.passchef.mobile.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.cookbook.CookbookController
import dev.passchef.mobile.cookbook.LocalCookbookVietnamese
import dev.passchef.mobile.cookbook.words
import dev.passchef.mobile.home.ageWords
import dev.passchef.mobile.home.isStale
import dev.passchef.mobile.home.sheetColor
import dev.passchef.mobile.navigation.AppRoute
import dev.passchef.mobile.navigation.AppRouteState
import dev.passchef.mobile.ui.*

/** KT1 Settings: sheets, secret parts, security rules, language (D10), theme (D13), how it works, lock now. */
@OptIn(ExperimentalLayoutApi::class)
@Composable fun SettingsScreen(vault: VaultController, cookbook: CookbookController, routes: AppRouteState, preferences: AppPreferences) {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String, vn: String) = words(vi, en, vn)
    val snapshots = cookbook.sources.snapshots
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(t("Settings", "Cài đặt"), style = PassChefDisplay)
        PassChefCaption(t("Sheets", "Bảng tính"))
        snapshots.forEachIndexed { index, saved ->
            val key = saved.mapping.origin.key
            SettingsRow(saved.mapping.origin.title, "${saved.entries.size} · " + ageWords(saved.refreshedAt, vi), "kitchen-menu-$key",
                stripe = sheetColor(index), warn = isStale(saved)) { routes.push(AppRoute.SheetDetail(key)) }
        }
        CookbookSecondaryAction(t("+ Connect a sheet", "+ Kết nối bảng tính"), onClick = { routes.push(AppRoute.Connect) },
            modifier = Modifier.fillMaxWidth().testTag("kitchen-connect"))
        PassChefCaption(t("Secret parts", "Phần bí mật"))
        val sets = vault.setSummaries
        val parts = sets.sumOf { it.partNumbers.size }
        SettingsRow(if (sets.size > 1) t("$parts parts in ${sets.size} sets", "$parts phần trong ${sets.size} bộ") else t("$parts secret parts in your vault", "$parts phần bí mật trong két"),
            t("Values are never shown", "Không bao giờ hiện giá trị"), "kitchen-spices") { routes.push(AppRoute.SecretParts) }
        PassChefCaption(t("Security", "Bảo mật"))
        Text(t("Auto-lock: 5 min idle · 10 min max · when you leave the app. Copied passwords clear from the clipboard after 30 s.",
            "Tự khóa: 5 phút không dùng · tối đa 10 phút · khi rời app. Mật khẩu đã chép tự xóa khỏi bộ nhớ tạm sau 30 giây."),
            style = MaterialTheme.typography.bodyMedium, color = PassChefPalette.Secondary)
        PassChefCaption(t("Language", "Ngôn ngữ"))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CookbookChoice("English", isSelected = !vi, onClick = { cookbook.edit(); preferences.chooseLanguage(false) }, modifier = Modifier.testTag("kitchen-lang-en"))
            CookbookChoice("Tiếng Việt", isSelected = vi, onClick = { cookbook.edit(); preferences.chooseLanguage(true) }, modifier = Modifier.testTag("kitchen-lang-vi"))
        }
        PassChefCaption(t("Theme", "Giao diện"))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for ((choice, en, vn) in listOf(Triple(ThemeChoice.SYSTEM, "System", "Theo máy"), Triple(ThemeChoice.DARK, "Dark", "Tối"), Triple(ThemeChoice.LIGHT, "Light", "Sáng")))
                CookbookChoice(t(en, vn), isSelected = preferences.theme == choice, onClick = { preferences.chooseTheme(choice) },
                    modifier = Modifier.testTag("kitchen-theme-${choice.name.lowercase()}"))
        }
        SettingsRow(t("How it works", "Cách hoạt động"), t("Written codes, secret parts, what is encrypted", "Mã đã ghi, phần bí mật, cái gì được mã hóa"),
            "kitchen-how") { routes.push(AppRoute.HowItWorks) }
        CookbookSecondaryAction(t("Lock now", "Khóa ngay"), onClick = { cookbook.back(); vault.explicitLock() },
            modifier = Modifier.fillMaxWidth().testTag("kitchen-lock"))
        Text(t("Don't keep the password. Keep the recipe.", "Đừng giữ mật khẩu. Giữ công thức."),
            style = PassChefDisplay.copy(fontSize = androidx.compose.ui.unit.TextUnit(16f, androidx.compose.ui.unit.TextUnitType.Sp)),
            color = PassChefPalette.Muted, modifier = Modifier.padding(top = 12.dp))
    }
}

@Composable fun SettingsRow(title: String, detail: String, tag: String, stripe: androidx.compose.ui.graphics.Color? = null,
    warn: Boolean = false, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = 56.dp).background(PassChefPalette.Surface, MaterialTheme.shapes.medium)
        .clickable(onClick = onClick).testTag(tag).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
        if (stripe != null) { Box(Modifier.width(3.dp).height(32.dp).background(stripe)); Spacer(Modifier.width(10.dp)) }
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(detail, style = MaterialTheme.typography.bodySmall, color = if (warn) PassChefPalette.Honey else PassChefPalette.Muted)
        }
        Icon(PassChefIcons.Chevron, contentDescription = null, tint = PassChefPalette.Muted)
    }
}
