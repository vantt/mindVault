package dev.passchef.mobile.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.cookbook.*
import dev.passchef.mobile.recipe.RecipeParser
import dev.passchef.mobile.ui.*

/** CK1 "Enter code": type a written code, choose which sheet it is from ("Sheet › tab"), get the password. */
@OptIn(ExperimentalLayoutApi::class)
@Composable fun EnterCodeScreen(model: CookbookController) {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String, vn: String) = words(vi, en, vn)
    // The written code is not secret (it is in the sheet); still kept only while this tab is shown.
    var code by remember { mutableStateOf("") }
    var menu by remember { mutableStateOf<MenuOrigin?>(null) }
    val tagged = RecipeParser.parse(code)?.tag != null
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(t("Enter a code", "Nhập mã"), style = PassChefDisplay)
        OutlinedTextField(code, { if (it.length <= 4096) { model.edit(); code = it.trim() } }, singleLine = true,
            label = { Text(t("Written code, e.g. rand0m\$1", "Mã đã ghi, ví dụ rand0m\$1")) },
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontFamily = PassChefMonoFamily),
            modifier = Modifier.fillMaxWidth().testTag("manual-recipe"))
        writtenCodeGloss(code, vi)?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = PassChefPalette.Muted) }
        if (model.sources.snapshots.isNotEmpty()) {
            PassChefCaption(t("Which sheet is it from?", "Mã thuộc bảng tính nào?"))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CookbookChoice(t("None", "Không"), isSelected = menu == null, onClick = { model.edit(); menu = null })
                model.sources.snapshots.forEach { saved ->
                    CookbookChoice(saved.mapping.origin.title, isSelected = menu == saved.mapping.origin,
                        onClick = { model.edit(); menu = saved.mapping.origin })
                }
            }
            if (tagged && menu == null) Text(t("This code has a check tag. Choose its sheet.", "Mã này có tag kiểm tra. Hãy chọn bảng tính của nó."),
                style = MaterialTheme.typography.bodySmall, color = PassChefPalette.Honey)
        }
        // One amber action per screen: once a password is shown, Copy is the main action.
        if (model.hasDish()) CookbookSecondaryAction(t("Get password", "Lấy mật khẩu"), onClick = { model.cook(code, menu?.menuId) },
            enabled = !model.cooking && code.isNotEmpty(), modifier = Modifier.fillMaxWidth().testTag("manual-cook"))
        else CookbookPrimaryAction(t("Get password", "Lấy mật khẩu"), onClick = { model.cook(code, menu?.menuId) },
            enabled = !model.cooking && code.isNotEmpty(), modifier = Modifier.fillMaxWidth().testTag("manual-cook"))
        DishPanel(model)
    }
}
