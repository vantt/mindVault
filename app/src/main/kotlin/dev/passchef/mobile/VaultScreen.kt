package dev.passchef.mobile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.vault.Argon2MasterKey

@Composable fun VaultScreen(controller: VaultController, registerClear: (() -> Unit) -> Unit) {
    // Inputs deliberately use remember, never rememberSaveable, and are cleared on background/submission.
    var password by remember { mutableStateOf("") }
    var spices by remember { mutableStateOf(List(5) { "" }) }
    val clear = { password = ""; spices = List(5) { "" }; Unit }
    SideEffect { registerClear(clear) }
    MaterialTheme {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("PassChef — private Cookbook", style = MaterialTheme.typography.headlineSmall)
            Text(controller.status)
            if (!controller.unlocked) {
                Text("First setup: enter your master password and one to five spice jars. Later, enter only the same password to recover. Losing it loses access to your spices.")
                OutlinedTextField(password, { if (it.length <= 1024) password = it }, label = { Text("Master password") },
                    enabled = !controller.busy, visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())
                for (index in 0..4) OutlinedTextField(spices[index], { text ->
                    if (text.length <= 1024) spices = spices.toMutableList().also { it[index] = text }
                }, label = { Text("Spice jar ${index + 1} (setup only)") }, enabled = !controller.busy,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())
                Button(enabled = !controller.busy && password.isNotEmpty(), onClick = {
                    val bytes = Argon2MasterKey.passwordBytes(password)
                    val jars = spices.mapIndexedNotNull { index, text ->
                        if (text.isEmpty()) null else index + 1 to Argon2MasterKey.passwordBytes(text)
                    }.toMap()
                    clear(); controller.password(bytes, jars)
                }) { Text("Set up / recover with password") }
                Button(enabled = !controller.busy, onClick = { clear(); controller.biometric() }) { Text("Open with biometric") }
            }
            Button(onClick = { clear(); controller.explicitLock() }) { Text("Lock Cookbook") }
            Text("Locks on background, after 5 minutes idle, or 10 minutes total. Dishes and spice values are never displayed here.")
        }
    }
}
