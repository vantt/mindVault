package dev.passchef.mobile.gate

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.cookbook.LocalCookbookVietnamese
import dev.passchef.mobile.cookbook.words
import dev.passchef.mobile.ui.*

/** D18 first run: E1 tear, E2 stranger view, E3 by hand, E4 secret parts. Skip never blocks setup. */
@Composable fun FirstRunExplainer(onDone: () -> Unit) {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String, vn: String) = words(vi, en, vn)
    var page by rememberSaveable { mutableStateOf(0) }
    BackHandler(enabled = page > 0) { page-- }
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp), horizontalArrangement = Arrangement.End) {
            CookbookGhostAction(t("Skip", "Bỏ qua"), onClick = onDone, modifier = Modifier.testTag("onboarding-skip"))
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            when (page) {
                0 -> TearExplainer()
                1 -> StrangerView()
                2 -> ByHand()
                else -> SecretPartsIntro()
            }
        }
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            StepDots(page, 4)
            if (page < 3) CookbookPrimaryAction(t("Next", "Tiếp"), onClick = { page++ },
                modifier = Modifier.fillMaxWidth().testTag("onboarding-next"))
            else {
                CookbookPrimaryAction(t("Set up my vault", "Lập két riêng"), onClick = onDone, modifier = Modifier.fillMaxWidth().testTag("onboarding-next"))
                CookbookGhostAction(t("Not now", "Để sau"), onClick = onDone, modifier = Modifier.testTag("onboarding-later"))
            }
        }
    }
}

@Composable fun StepDots(current: Int, count: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(count) { i ->
            Box(Modifier.size(8.dp).background(if (i == current) PassChefPalette.Text else PassChefPalette.StrongLine, CircleShape))
        }
    }
}

@Composable private fun Title(plain: String, italic: String) {
    Column {
        Text(plain, style = PassChefDisplay)
        Text(italic, style = PassChefDisplay.copy(fontStyle = FontStyle.Italic), color = PassChefPalette.AccentInk)
    }
}

@Composable private fun Mono(text: String, color: androidx.compose.ui.graphics.Color = PassChefPalette.Text) =
    Text(text, style = MaterialTheme.typography.bodyLarge.copy(fontFamily = PassChefMonoFamily), color = color)

@Composable private fun StrangerView() {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String, vn: String) = words(vi, en, vn)
    Title(t("Sheet leaked?", "Lộ bảng tính?"), t("Strangers see only codes.", "Người lạ chỉ thấy mã."))
    Column(Modifier.fillMaxWidth().border(1.dp, PassChefPalette.Hairline, MaterialTheme.shapes.small).padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)) {
        PassChefCaption(t("Your sheet · example", "Bảng tính của bạn · ví dụ"))
        for ((site, code) in listOf("Facebook" to "rand0m\$1", "Gmail" to "x9Lk2m\$1", "Bank" to "p3Zn7w\$1")) Row {
            Text(site, Modifier.weight(1f)); Mono(code, PassChefPalette.AccentInk)
        }
    }
    CookbookSection(t("A stranger tries Facebook", "Người lạ thử đăng nhập Facebook")) {
        Mono("rand0m\$1")
        CookbookStatus(t("Wrong password. That's only a code.", "Sai mật khẩu. Đó chỉ là mã."), tone = CookbookTone.Error)
    }
    Text(t("The full password = code + a secret part only you know. The secret part is never in the sheet.",
        "Mật khẩu đầy đủ = mã + phần bí mật chỉ bạn biết. Phần bí mật không bao giờ nằm trong bảng tính."),
        color = PassChefPalette.Secondary)
}

@Composable private fun ByHand() {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String, vn: String) = words(vi, en, vn)
    Title(t("No app?", "Không có app?"), t("Do it by hand.", "Tự tính tay vẫn được."))
    CookbookSection(t("Example", "Ví dụ")) {
        PassChefCaption(t("Written code", "Mã đã ghi")); Mono("rand0m\$1", PassChefPalette.AccentInk)
        Text(t("\$1 = add secret part 1 at the end", "\$1 = thêm phần bí mật số 1 vào cuối"), color = PassChefPalette.Muted,
            style = MaterialTheme.typography.bodySmall)
        PassChefCaption("+ " + t("Secret part 1", "Phần bí mật số 1")); Mono("Basic*", PassChefPalette.Moss)
        PassChefCaption("= " + t("Password", "Mật khẩu")); Mono("rand0mBasic*")
    }
    CookbookStatus(t("Your sheet + your secret parts are enough. Forgot the app password? You don't lose your accounts. The app is just faster.",
        "Bảng tính + các phần bí mật là đủ. Quên mật khẩu app? Bạn không mất tài khoản. App chỉ giúp nhanh hơn."))
}

@Composable private fun SecretPartsIntro() {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String, vn: String) = words(vi, en, vn)
    Title(t("Pick 1 to 5 secret parts.", "Chọn 1–5 phần bí mật."), t("Keep a copy apart.", "Cất một bản riêng."))
    Text(t("Keep a copy away from the sheet, for example a sealed envelope for your family. On this phone the app keeps them encrypted in a private vault.",
        "Cất một bản xa bảng tính, ví dụ phong bì dán kín cho người nhà. Trên điện thoại này, app mã hóa chúng trong két riêng."),
        color = PassChefPalette.Secondary)
}
