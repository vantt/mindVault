package dev.passchef.mobile.gate

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.R
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.VaultPhase
import dev.passchef.mobile.cookbook.LocalCookbookVietnamese
import dev.passchef.mobile.cookbook.words
import dev.passchef.mobile.navigation.AppRouteState
import dev.passchef.mobile.ui.*
import dev.passchef.mobile.vault.Argon2MasterKey
import dev.passchef.mobile.vaultStatus
import dev.passchef.mobile.vaultStatusIsProblem

/**
 * UL1/UL2. Fingerprint first (auto-prompt once per lock event, D6; no relaunch loop after cancel),
 * master password always one tap away. No "Lock" button here (#20). Password field remember-only.
 */
@Composable fun UnlockVaultScreen(controller: VaultController, routes: AppRouteState, registerClear: (() -> Unit) -> Unit) {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String, vn: String) = words(vi, en, vn)
    val biometric = controller.phase == VaultPhase.LOCKED_BIOMETRIC
    var showPassword by remember(controller.phase) { mutableStateOf(!biometric) }
    var password by remember { mutableStateOf("") }
    var forgot by remember { mutableStateOf(false) }
    SideEffect { registerClear { password = "" } }
    LaunchedEffect(controller.lockEvent, controller.phase) {
        if (biometric && routes.autoBiometricEnabled && !controller.busy && routes.autoBiometricEvent != controller.lockEvent) {
            routes.autoBiometricEvent = controller.lockEvent
            controller.biometric()
        }
    }
    fun submit() {
        if (password.isEmpty() || controller.busy) return
        val bytes = Argon2MasterKey.passwordBytes(password); password = ""
        controller.password(bytes, emptyMap())
    }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(24.dp))
        Image(painterResource(R.drawable.passchef_mark), contentDescription = null, modifier = Modifier.size(72.dp))
        Row {
            Text(t("Your vault is ", "Két riêng đang "), style = PassChefDisplay)
            Text(t("locked", "khóa"), style = PassChefDisplay.copy(fontStyle = FontStyle.Italic), color = PassChefPalette.AccentInk)
        }
        val problem = vaultStatusIsProblem(controller.status)
        if (controller.busy) CookbookStatus(vaultStatus(controller.status, vi), tone = CookbookTone.Warning)
        else if (problem) CookbookStatus(vaultStatus(controller.status, vi), tone = CookbookTone.Error)
        if (biometric) {
            val action = { password = ""; controller.biometric() }
            if (showPassword) CookbookSecondaryAction(t("Unlock with fingerprint", "Mở bằng vân tay"), onClick = action,
                enabled = !controller.busy, modifier = Modifier.fillMaxWidth().testTag("unlock-biometric"))
            else CookbookPrimaryAction(t("Unlock with fingerprint", "Mở bằng vân tay"), onClick = action,
                enabled = !controller.busy, modifier = Modifier.fillMaxWidth().testTag("unlock-biometric"))
        }
        if (!showPassword) CookbookSecondaryAction(t("Use master password", "Dùng mật khẩu chính"), onClick = { showPassword = true },
            modifier = Modifier.fillMaxWidth().testTag("unlock-password"))
        else {
            SecretField(password, { password = it }, t("Master password", "Mật khẩu chính"), "unlock-password-field",
                enabled = !controller.busy, imeDone = ::submit)
            CookbookPrimaryAction(t("Unlock", "Mở khóa"), onClick = ::submit, enabled = password.isNotEmpty() && !controller.busy,
                modifier = Modifier.fillMaxWidth().testTag("unlock-submit"))
            CookbookGhostAction(t("Forgot password?", "Quên mật khẩu?"), onClick = { forgot = true })
        }
        Text(t("Locks after 5 min idle, 10 min max, or when you leave the app.", "Tự khóa sau 5 phút không dùng, tối đa 10 phút, hoặc khi rời app."),
            style = MaterialTheme.typography.bodySmall, color = PassChefPalette.Muted)
    }
    if (forgot) AlertDialog(onDismissRequest = { forgot = false },
        title = { Text(t("No reset, by design", "Không có đặt lại, có chủ đích")) },
        text = { Text(t("Nobody, not even PassChef, can open this vault without the master password. Your accounts are not lost: your sheet + the copy of your secret parts you kept apart still give every password, by hand or in a new install.",
            "Không ai, kể cả PassChef, mở được két này nếu thiếu mật khẩu chính. Tài khoản không mất: bảng tính + bản phần bí mật bạn cất riêng vẫn cho ra mọi mật khẩu, tính tay hoặc cài lại app.")) },
        confirmButton = { CookbookGhostAction(t("OK", "Đã hiểu"), onClick = { forgot = false }) })
}
