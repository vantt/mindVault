package dev.passchef.mobile.cookbook

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import java.util.Locale
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.VaultScreen

@Composable fun MainCookbookScreen(vault: VaultController, cookbook: CookbookController,registerClear: (() -> Unit) -> Unit) {
    var vi by remember { mutableStateOf(Locale.getDefault().language=="vi") }
    LaunchedEffect(vault.unlocked) { if(vault.unlocked) cookbook.sources.reload() }
    CompositionLocalProvider(LocalCookbookVietnamese provides vi) {
        MaterialTheme {
            Column {
                TextButton(onClick={vi=!vi; cookbook.edit()}) { Text(if(vi) "EN" else "VI") }
                if(vault.unlocked) {
                    SideEffect { registerClear { cookbook.lock() } }
                    CookbookScreen(cookbook,vault::explicitLock)
                } else VaultScreen(vault,registerClear)
            }
        }
    }
}
