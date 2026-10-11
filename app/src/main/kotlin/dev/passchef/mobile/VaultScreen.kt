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
    val vi = dev.passchef.mobile.cookbook.LocalCookbookVietnamese.current
    fun t(en:String,vn:String)=dev.passchef.mobile.cookbook.words(vi,en,vn)
    // Inputs deliberately use remember, never rememberSaveable, and are cleared on background/submission.
    var password by remember { mutableStateOf("") }
    var spices by remember { mutableStateOf(List(5) { "" }) }
    val clear = { password = ""; spices = List(5) { "" }; Unit }
    SideEffect { registerClear(clear) }
    MaterialTheme {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(t("PassChef — private Cookbook","PassChef — Cookbook riêng"), style = MaterialTheme.typography.headlineSmall)
            Text(vaultStatus(controller.status, vi))
            if (!controller.unlocked) {
                Text(t("First setup: enter your master password and one to five spice jars. Later, enter only the same password to recover. Losing it loses access to your spices.","Lần đầu: đặt mật khẩu chính và 1–5 lọ Spices. Sau đó dùng chính mật khẩu này để phục hồi. Mất mật khẩu sẽ mất quyền vào Spices."))
                OutlinedTextField(password, { if (it.length <= 1024) password = it }, label = { Text(t("Master password","Mật khẩu chính")) },
                    enabled = !controller.busy, visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())
                for (index in 0..4) OutlinedTextField(spices[index], { text ->
                    if (text.length <= 1024) spices = spices.toMutableList().also { it[index] = text }
                }, label = { Text(t("Spice jar ${index + 1} (setup only)","Lọ Spices ${index + 1} (chỉ lần đầu)")) }, enabled = !controller.busy,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())
                Button(enabled = !controller.busy && password.isNotEmpty(), onClick = {
                    val bytes = Argon2MasterKey.passwordBytes(password)
                    val jars = spices.mapIndexedNotNull { index, text ->
                        if (text.isEmpty()) null else index + 1 to Argon2MasterKey.passwordBytes(text)
                    }.toMap()
                    clear(); controller.password(bytes, jars)
                }) { Text(t("Set up / recover with password","Thiết lập / phục hồi bằng mật khẩu")) }
                Button(enabled = !controller.busy, onClick = { clear(); controller.biometric() }) { Text(t("Open with biometric","Mở bằng sinh trắc học")) }
            }
            Button(onClick = { clear(); controller.explicitLock() }) { Text(t("Lock Cookbook","Khóa Cookbook")) }
            Text(t("Locks on background, after 5 minutes idle, or 10 minutes total. Spices are never displayed after unlocking; cooked Dishes start masked.","Khóa khi ra nền, sau 5 phút không hoạt động hoặc 10 phút tổng. Spices không hiện sau mở khóa; Dish được che mặc định."))
        }
    }
}
