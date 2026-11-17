package dev.passchef.mobile.cookbook

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.VaultPhase
import dev.passchef.mobile.gate.FirstRunExplainer
import dev.passchef.mobile.gate.SetupVaultScreen
import dev.passchef.mobile.gate.UnlockVaultScreen
import dev.passchef.mobile.navigation.AppRouteState
import dev.passchef.mobile.navigation.AppShell
import dev.passchef.mobile.navigation.InputWipers
import dev.passchef.mobile.settings.AppPreferences
import dev.passchef.mobile.ui.PassChefHeader
import dev.passchef.mobile.ui.PassChefTheme

/**
 * Root: gate (first run / setup / unlock, by controller phase, D3) outside navigation, then the
 * 3-tab shell. Language + theme come from non-sensitive preferences (D10/D13).
 */
@Composable fun MainCookbookScreen(vault: VaultController, cookbook: CookbookController, preferences: AppPreferences,
    routes: AppRouteState, registerClear: (() -> Unit) -> Unit) {
    val wipers = remember { InputWipers() }
    SideEffect { registerClear { cookbook.edit(); wipers.wipeAll() } }
    LaunchedEffect(vault.unlocked) { if (vault.unlocked) cookbook.sources.reload() }
    val vi = preferences.vietnamese
    CompositionLocalProvider(LocalCookbookVietnamese provides vi) {
        PassChefTheme(preferences.theme) {
            Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).imePadding()) {
                    if (vault.unlocked) AppShell(vault, cookbook, preferences, routes, wipers)
                    else {
                        PassChefHeader(if (vi) "EN" else "VI", onLanguage = { preferences.chooseLanguage(!vi) })
                        Box(Modifier.fillMaxWidth().weight(1f)) {
                            val register: (() -> Unit) -> Unit = { wipe -> wipers.set("gate", wipe) }
                            when (vault.phase) {
                                VaultPhase.NEW -> if (!preferences.explainerSeen) FirstRunExplainer(preferences::markExplainerSeen)
                                    else SetupVaultScreen(vault, register)
                                VaultPhase.LOCKED_BIOMETRIC, VaultPhase.LOCKED_PASSWORD -> UnlockVaultScreen(vault, routes, register)
                                VaultPhase.CHECKING, VaultPhase.UNLOCKED -> Unit
                            }
                        }
                    }
                }
            }
        }
    }
}
