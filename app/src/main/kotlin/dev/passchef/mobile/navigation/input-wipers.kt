package dev.passchef.mobile.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

/**
 * Screens with sensitive inputs (master password, secret parts) register a wipe here. The Activity
 * runs [wipeAll] on background/stop, before any lock. Only the screens on display are registered.
 */
class InputWipers {
    private val wipers = linkedMapOf<String, () -> Unit>()
    fun wipeAll() { wipers.values.toList().forEach { runCatching { it() } } }
    fun set(key: String, wipe: () -> Unit) { wipers[key] = wipe }
    fun remove(key: String) { wipers.remove(key) }
}

/** Registers [wipe] while this composable is on screen. */
@Composable fun RegisterWipe(wipers: InputWipers, key: String, wipe: () -> Unit) {
    DisposableEffect(wipers, key) {
        wipers.set(key, wipe)
        onDispose { wipers.remove(key) }
    }
}
