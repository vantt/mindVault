package dev.passchef.mobile.gate

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.cookbook.LocalCookbookVietnamese
import dev.passchef.mobile.cookbook.words
import dev.passchef.mobile.ui.*
import dev.passchef.mobile.vault.Argon2MasterKey
import dev.passchef.mobile.vaultStatus
import dev.passchef.mobile.vaultStatusIsProblem

/**
 * SU1 master password → SU2 secret parts (1–5, user picks the count, D15) → SU3 fingerprint.
 * Inputs use remember only (never saveable), are capped at 1024 chars, and are wiped on submit
 * and when the app goes to the background ([registerClear]).
 */
@Composable fun SetupVaultScreen(controller: VaultController, registerClear: (() -> Unit) -> Unit) {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String, vn: String) = words(vi, en, vn)
    var step by remember { mutableStateOf(1) }
    var master by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var ack by remember { mutableStateOf(false) }
    var parts by remember { mutableStateOf(listOf("")) }
    val clear = { master = ""; confirm = ""; parts = listOf(""); ack = false; step = 1 }
    SideEffect { registerClear(clear) }
    BackHandler(enabled = step > 1 && !controller.busy) { step-- }
    fun create(useBiometric: Boolean) {
        val bytes = Argon2MasterKey.passwordBytes(master)
        val jars = parts.mapIndexed { i, text -> i + 1 to Argon2MasterKey.passwordBytes(text) }.toMap()
        clear(); controller.password(bytes, jars, useBiometric)
    }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PassChefCaption(t("Step $step of 3", "Bước $step / 3"))
        if (controller.busy) CookbookStatus(t("Preparing your vault…", "Đang chuẩn bị két…"), tone = CookbookTone.Warning)
        else if (vaultStatusIsProblem(controller.status)) CookbookStatus(vaultStatus(controller.status, vi), tone = CookbookTone.Error)
        when (step) {
            1 -> {
                Text(t("Create your master password", "Tạo mật khẩu chính"), style = PassChefDisplay)
                Text(t("It opens your private vault on this phone. Nobody can reset it.", "Nó mở két riêng trên điện thoại này. Không ai đặt lại được."),
                    color = PassChefPalette.Secondary)
                SecretField(master, { master = it }, t("Master password", "Mật khẩu chính"), "setup-master")
                SecretField(confirm, { confirm = it }, t("Type it again", "Nhập lại"), "setup-master-confirm")
                if (confirm.isNotEmpty() && confirm != master) Text(t("Passwords don't match", "Mật khẩu chưa khớp"), color = PassChefPalette.Coral)
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
                    .toggleable(ack, role = androidx.compose.ui.semantics.Role.Checkbox) { ack = it }.testTag("setup-ack")) {
                    Checkbox(ack, null)
                    Text(t("I understand: if I forget it, this vault can't be opened. My accounts still work with my sheet + secret parts.",
                        "Tôi hiểu: quên mật khẩu này thì không mở được két. Tài khoản vẫn dùng được nhờ bảng tính + phần bí mật."),
                        style = MaterialTheme.typography.bodySmall)
                }
                CookbookPrimaryAction(t("Next", "Tiếp"), onClick = { step = 2 }, enabled = master.isNotEmpty() && master == confirm && ack,
                    modifier = Modifier.fillMaxWidth())
            }
            2 -> {
                Text(t("Add your secret parts", "Thêm phần bí mật"), style = PassChefDisplay)
                Text(t("Short phrases only you know. The app adds them to each code. 1 is enough; up to 5. Each code says which one it uses (#1, \$2…).",
                    "Cụm ngắn chỉ bạn biết. App ghép chúng vào từng mã. 1 là đủ; tối đa 5. Mỗi mã ghi rõ dùng phần nào (#1, \$2…)."),
                    color = PassChefPalette.Secondary)
                parts.forEachIndexed { i, value ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.weight(1f)) {
                            SecretField(value, { text -> parts = parts.toMutableList().also { it[i] = text } },
                                t("Secret part ${i + 1}", "Phần bí mật số ${i + 1}"), "setup-spice-${i + 1}")
                        }
                        if (i > 0 && i == parts.lastIndex) CookbookGhostAction("✕", onClick = { parts = parts.dropLast(1) })
                    }
                }
                if (parts.size < 5) CookbookSecondaryAction(t("+ Add another (${parts.size}/5)", "+ Thêm phần nữa (${parts.size}/5)"),
                    onClick = { parts = parts + "" }, modifier = Modifier.testTag("setup-add-spice"))
                parts.indexOfFirst { it.isEmpty() }.takeIf { it >= 0 }?.let {
                    Text(if (parts.size == 1) t("Fill in secret part 1", "Điền phần bí mật số 1") else t("Secret part ${it + 1} is empty — fill or remove it", "Phần bí mật số ${it + 1} đang trống — điền hoặc xóa"),
                        style = MaterialTheme.typography.bodySmall, color = PassChefPalette.Muted)
                }
                Text(t("Weak secret part = weak passwords. Keep a copy apart from the sheet.", "Phần bí mật yếu = mật khẩu yếu. Cất một bản xa bảng tính."),
                    style = MaterialTheme.typography.bodySmall, color = PassChefPalette.Honey)
                CookbookPrimaryAction(t("Next", "Tiếp"), onClick = { step = 3 }, enabled = parts.all { it.isNotEmpty() },
                    modifier = Modifier.fillMaxWidth())
            }
            else -> {
                Text(t("Open with your fingerprint next time?", "Lần sau mở bằng vân tay?"), style = PassChefDisplay)
                Text(t("Your phone asks for your fingerprint once now. The master password always works too. If no fingerprint is set up, the vault opens with the password only.",
                    "Điện thoại hỏi vân tay một lần bây giờ. Mật khẩu chính luôn dùng được. Nếu chưa cài vân tay, két chỉ mở bằng mật khẩu."),
                    color = PassChefPalette.Secondary)
                CookbookPrimaryAction(t("Use fingerprint & create vault", "Dùng vân tay và tạo két"), onClick = { create(true) },
                    enabled = !controller.busy, modifier = Modifier.fillMaxWidth().testTag("setup-biometric"))
                CookbookSecondaryAction(t("Not now — password only", "Để sau — chỉ mật khẩu"), onClick = { create(false) },
                    enabled = !controller.busy, modifier = Modifier.fillMaxWidth().testTag("setup-create"))
            }
        }
    }
}

/** Masked field for master password / secret parts; 1024-char cap, no autofill keyboard learning. */
@Composable fun SecretField(value: String, onChange: (String) -> Unit, label: String, tag: String, enabled: Boolean = true,
    imeDone: (() -> Unit)? = null) {
    OutlinedTextField(value, { if (it.length <= 1024) onChange(it) }, label = { Text(label) }, enabled = enabled,
        singleLine = true, visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, autoCorrectEnabled = false,
            imeAction = if (imeDone != null) androidx.compose.ui.text.input.ImeAction.Go else androidx.compose.ui.text.input.ImeAction.Next),
        keyboardActions = androidx.compose.foundation.text.KeyboardActions(onGo = { imeDone?.invoke() }),
        modifier = Modifier.fillMaxWidth().testTag(tag))
}
