package dev.passchef.mobile.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.cookbook.CookbookController
import dev.passchef.mobile.cookbook.LocalCookbookVietnamese
import dev.passchef.mobile.cookbook.cookbookMessage
import dev.passchef.mobile.cookbook.isProblem
import dev.passchef.mobile.cookbook.words
import dev.passchef.mobile.connect.ConnectWizardScreen
import dev.passchef.mobile.home.EnterCodeScreen
import dev.passchef.mobile.home.EntryDetailScreen
import dev.passchef.mobile.home.PasswordsHomeScreen
import dev.passchef.mobile.settings.AppPreferences
import dev.passchef.mobile.settings.HowItWorksScreen
import dev.passchef.mobile.settings.SecretPartsScreen
import dev.passchef.mobile.settings.SettingsScreen
import dev.passchef.mobile.settings.SheetDetailScreen
import dev.passchef.mobile.ui.*

/**
 * Unlocked shell (spec §2): header (wordmark · status dot · Lock now), current tab/route, bottom nav.
 * Back: pushed screen pops, other tab root → Passwords, Passwords root → system (finish = lock).
 * Leaving a screen always wipes the password on display (cookbook.back()).
 */
@Composable fun ColumnScope.AppShell(vault: VaultController, cookbook: CookbookController, preferences: AppPreferences,
    routes: AppRouteState, wipers: InputWipers) {
    val vi = LocalCookbookVietnamese.current
    BackHandler(enabled = routes.canGoBack) {
        if (routes.route == AppRoute.Connect) routes.resetConnect()
        cookbook.back(); routes.back()
    }
    val stale = cookbook.sources.status in setOf("NETWORK_FAILED", "ACCESS_DENIED", "CONNECT_REQUIRED", "CORRUPT_CACHE")
    Row(Modifier.fillMaxWidth().padding(start = 20.dp, end = 8.dp, top = 6.dp, bottom = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        PassChefWordmark(22)
        Spacer(Modifier.width(10.dp))
        Box(Modifier.size(8.dp).background(if (stale) PassChefPalette.Honey else PassChefPalette.Moss, CircleShape)
            .semantics { contentDescription = if (stale) words(vi, "Some sheets need attention", "Có bảng tính cần xem lại") else words(vi, "Unlocked", "Đã mở khóa") })
        Spacer(Modifier.weight(1f))
        IconButton(onClick = { cookbook.back(); vault.explicitLock() }, modifier = Modifier.testTag("lock-now")
            .semantics { contentDescription = words(vi, "Lock now", "Khóa ngay") }) {
            Icon(PassChefIcons.Lock, contentDescription = null, tint = PassChefPalette.Text)
        }
    }
    HorizontalDivider(color = PassChefPalette.Hairline)
    // D17: show a sheet's set name in code glosses only when 2+ sets exist.
    val summaries = vault.setSummaries
    val setNames = if (summaries.size < 2) emptyMap() else cookbook.sources.snapshots.associate { saved ->
        val key = saved.mapping.origin.key
        key to (summaries.find { it.id == preferences.sheetSets[key] } ?: summaries.first()).name
    }
    Box(Modifier.fillMaxWidth().weight(1f)) { androidx.compose.runtime.CompositionLocalProvider(dev.passchef.mobile.home.LocalSheetSetNames provides setNames) {
        when (val route = routes.route) {
            is AppRoute.EntryDetail -> EntryDetailScreen(cookbook, routes, route.entryId, preferences)
            AppRoute.Connect -> ConnectWizardScreen(cookbook, routes)
            is AppRoute.SheetDetail -> SheetDetailScreen(vault, cookbook, routes, preferences, route.originKey)
            AppRoute.SecretParts -> SecretPartsScreen(vault, routes, wipers)
            AppRoute.HowItWorks -> HowItWorksScreen()
            null -> when (routes.tab) {
                AppTab.PASSWORDS -> PasswordsHomeScreen(cookbook, routes, preferences)
                AppTab.ENTER_CODE -> EnterCodeScreen(cookbook)
                AppTab.SETTINGS -> SettingsScreen(vault, cookbook, routes, preferences)
            }
        }
    } }
    // Copy confirmation from a list row (D12): label + 30s clipboard note; the password is never shown.
    val copied = cookbook.copiedLabel
    if (routes.route == null && routes.tab == AppTab.PASSWORDS && (copied != null || cookbook.code != null)) {
        val message = if (cookbook.code != null) cookbookMessage(cookbook.code, vi)
            else "$copied · " + cookbookMessage(cookbook.clipboardStatus, vi)
        CookbookStatus(message, Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            tone = if (cookbook.code != null || isProblem(cookbook.clipboardStatus)) CookbookTone.Error else CookbookTone.Ready)
    }
    PassChefBottomNav(routes.tab) { tab -> cookbook.back(); routes.select(tab) }
}
