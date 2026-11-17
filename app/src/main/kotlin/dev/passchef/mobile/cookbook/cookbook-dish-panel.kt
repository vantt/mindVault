package dev.passchef.mobile.cookbook

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.ui.*

/** Password slab: masked by default, reveal 10s, copy (30s owned clipboard cleanup). Tags kept for proofs. */
@Composable fun DishPanel(model: CookbookController) {
    val vi = LocalCookbookVietnamese.current
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (model.cooking) CookbookStatus(words(vi,"Getting password…","Đang lấy mật khẩu…"), tone = CookbookTone.Warning)
        if (model.code != null) CookbookStatus(cookbookMessage(model.code,vi),
            modifier = Modifier.testTag("cook-error"), tone = CookbookTone.Error)
        if (model.warning) CookbookStatus(words(vi,"Older code without a check tag: PassChef can't confirm it belongs to this sheet.",
            "Mã kiểu cũ không có tag kiểm tra: PassChef không xác nhận được mã thuộc bảng tính này."), tone = CookbookTone.Warning)
        if (model.hasDish()) {
            PassChefCaption(words(vi, "Password", "Mật khẩu"))
            Surface(Modifier.fillMaxWidth(), color = PassChefPalette.Raised, contentColor = PassChefPalette.Text,
                shape = MaterialTheme.shapes.medium, border = BorderStroke(1.dp, PassChefPalette.StrongLine)) {
                Text(if(model.revealed) model.dish else "••••••••",style = PassChefDish,
                    modifier = Modifier.padding(16.dp).testTag("dish-display"))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CookbookSecondaryAction(words(vi,"Show 10 s","Hiện 10 giây"), onClick = model::reveal,
                    modifier = Modifier.weight(1f).testTag("dish-reveal"))
                CookbookPrimaryAction(words(vi,"Copy password","Chép mật khẩu"), onClick = model::copyDish,
                    modifier = Modifier.weight(1.4f).testTag("dish-copy"))
            }
        }
        model.clipboardStatus?.let { CookbookStatus(cookbookMessage(it,vi), tone = if (isProblem(it)) CookbookTone.Error else CookbookTone.Ready) }
    }
}
