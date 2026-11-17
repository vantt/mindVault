package dev.passchef.mobile.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.VaultController
import dev.passchef.mobile.cookbook.LocalCookbookVietnamese
import dev.passchef.mobile.cookbook.words
import dev.passchef.mobile.gate.SecretField
import dev.passchef.mobile.navigation.AppRouteState
import dev.passchef.mobile.navigation.InputWipers
import dev.passchef.mobile.navigation.RegisterWipe
import dev.passchef.mobile.ui.*
import dev.passchef.mobile.vault.Argon2MasterKey
import dev.passchef.mobile.vault.SecretPartSet
import java.security.SecureRandom

/**
 * KT3: sets and part numbers only — values are never shown after setup. Add a part to a set (D8) or a
 * new set (D17); each change asks for the master password first. Inputs remember-only and wiped on background.
 */
@Composable fun SecretPartsScreen(vault: VaultController, routes: AppRouteState, wipers: InputWipers) {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String, vn: String) = words(vi, en, vn)
    var target by remember { mutableStateOf<String?>(null) } // set id, or "new"
    var name by remember { mutableStateOf("") }
    var part by remember { mutableStateOf("") }
    var master by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<VaultController.UpdateOutcome?>(null) }
    val wipe = { part = ""; master = "" }
    RegisterWipe(wipers, "secret-parts", wipe)
    val sets = vault.setSummaries
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { wipe(); routes.pop() }) { Icon(PassChefIcons.Back, contentDescription = t("Back", "Quay lại"), tint = PassChefPalette.Text) }
            Text(t("Secret parts", "Phần bí mật"), style = MaterialTheme.typography.titleLarge)
        }
        Text(t("Values stay encrypted in your vault and are never shown again. Keep your own copy apart from the sheet.",
            "Giá trị được mã hóa trong két và không bao giờ hiện lại. Hãy tự cất một bản xa bảng tính."), color = PassChefPalette.Secondary)
        for (set in sets) CookbookSection(if (sets.size > 1) set.name else t("Your secret parts", "Phần bí mật của bạn")) {
            Text(set.partNumbers.joinToString("  ") { "#$it ••••" }, style = MaterialTheme.typography.bodyLarge.copy(fontFamily = PassChefMonoFamily))
            if (set.partNumbers.size < 5) CookbookGhostAction(t("+ Add part #${nextPart(set.partNumbers)}", "+ Thêm phần số ${nextPart(set.partNumbers)}"),
                onClick = { wipe(); result = null; target = set.id }, modifier = Modifier.testTag("add-part-${set.id}"))
        }
        if (sets.size < SecretPartSet.MAX_SETS) CookbookSecondaryAction(t("+ New set (e.g. Family, Work)", "+ Bộ mới (ví dụ Gia đình, Công việc)"),
            onClick = { wipe(); name = ""; result = null; target = "new" }, modifier = Modifier.fillMaxWidth().testTag("add-set"))
        Text(t("Different sets let different sheets use different secret parts, like the browser extension's profiles.",
            "Nhiều bộ cho phép mỗi bảng tính dùng phần bí mật riêng, giống hồ sơ trong tiện ích trình duyệt."),
            style = MaterialTheme.typography.bodySmall, color = PassChefPalette.Muted)
        val editing = target
        if (editing != null) CookbookSection(if (editing == "new") t("New set", "Bộ mới") else t("Add a part", "Thêm phần")) {
            if (editing == "new") OutlinedTextField(name, { if (it.length <= SecretPartSet.MAX_NAME) name = it }, singleLine = true,
                label = { Text(t("Set name", "Tên bộ")) }, modifier = Modifier.fillMaxWidth())
            val number = if (editing == "new") 1 else nextPart(sets.find { it.id == editing }?.partNumbers.orEmpty())
            SecretField(part, { part = it }, t("Secret part #$number", "Phần bí mật số $number"), "new-part")
            SecretField(master, { master = it }, t("Master password (to confirm)", "Mật khẩu chính (để xác nhận)"), "confirm-master")
            CookbookPrimaryAction(t("Save to vault", "Lưu vào két"), enabled = !vault.busy && part.isNotEmpty() && master.isNotEmpty() &&
                (editing != "new" || name.isNotBlank()), modifier = Modifier.fillMaxWidth().testTag("save-part"), onClick = {
                val password = Argon2MasterKey.passwordBytes(master)
                val value = Argon2MasterKey.passwordBytes(part)
                val setName = name.trim()
                wipe()
                vault.updateSets(password, { current ->
                    if (editing == "new") current + SecretPartSet(newSetId(), setName, mapOf(1 to value))
                    else { require(current.any { it.id == editing }) { "Set missing" }; current.map { set ->
                        if (set.id != editing) set
                        else SecretPartSet(set.id, set.name, set.parts + (nextPart(set.parts.keys.toList()) to value))
                    } }
                }) { outcome -> // on success the vault session owns `value`
                    if (outcome != VaultController.UpdateOutcome.SAVED) value.fill(0)
                    result = outcome; if (outcome == VaultController.UpdateOutcome.SAVED) target = null }
            })
        }
        when (result) {
            VaultController.UpdateOutcome.SAVED -> CookbookStatus(t("Saved in your vault.", "Đã lưu vào két."), tone = CookbookTone.Ready)
            VaultController.UpdateOutcome.TOO_LARGE -> CookbookStatus(t("Too much to store: shorten secret parts or remove a set. Nothing changed.",
                "Quá dung lượng: rút ngắn phần bí mật hoặc bớt một bộ. Không có gì thay đổi."), tone = CookbookTone.Error)
            VaultController.UpdateOutcome.REJECTED -> CookbookStatus(t("Not saved. Check the master password; nothing changed.", "Chưa lưu. Kiểm tra mật khẩu chính; không có gì thay đổi."), tone = CookbookTone.Error)
            null -> if (vault.busy) CookbookStatus(t("Saving…", "Đang lưu…"), tone = CookbookTone.Warning)
        }
    }
}

private fun nextPart(existing: List<Int>): Int = (1..5).firstOrNull { it !in existing } ?: 5
private fun newSetId(): String = "set-" + ByteArray(4).also(SecureRandom()::nextBytes).joinToString("") { "%02x".format(it) }
