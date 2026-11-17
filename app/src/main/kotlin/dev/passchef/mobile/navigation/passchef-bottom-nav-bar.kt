package dev.passchef.mobile.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import dev.passchef.mobile.cookbook.LocalCookbookVietnamese
import dev.passchef.mobile.cookbook.words
import dev.passchef.mobile.ui.PassChefIcons
import dev.passchef.mobile.ui.PassChefPalette

/** Three stable places (D4). Active state is neutral text colour, not amber (palette D rule). */
@Composable fun PassChefBottomNav(current: AppTab, onSelect: (AppTab) -> Unit) {
    val vi = LocalCookbookVietnamese.current
    NavigationBar(containerColor = PassChefPalette.Surface) {
        for (tab in AppTab.entries) {
            val label = when (tab) {
                AppTab.PASSWORDS -> words(vi, "Passwords", "Mật khẩu")
                AppTab.ENTER_CODE -> words(vi, "Enter code", "Nhập mã")
                AppTab.SETTINGS -> words(vi, "Settings", "Cài đặt")
            }
            val icon = when (tab) {
                AppTab.PASSWORDS -> PassChefIcons.Key
                AppTab.ENTER_CODE -> PassChefIcons.Keypad
                AppTab.SETTINGS -> PassChefIcons.Gear
            }
            NavigationBarItem(selected = current == tab, onClick = { onSelect(tab) },
                icon = { Icon(icon, contentDescription = null) }, label = { Text(label) },
                alwaysShowLabel = true, modifier = Modifier.testTag(tab.tag),
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PassChefPalette.Text, selectedTextColor = PassChefPalette.Text,
                    indicatorColor = PassChefPalette.Raised,
                    unselectedIconColor = PassChefPalette.Muted, unselectedTextColor = PassChefPalette.Muted))
        }
    }
}
