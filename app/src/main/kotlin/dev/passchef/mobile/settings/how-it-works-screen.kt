package dev.passchef.mobile.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.cookbook.LocalCookbookVietnamese
import dev.passchef.mobile.cookbook.words
import dev.passchef.mobile.gate.TearExplainer
import dev.passchef.mobile.ui.*

/** "How it works": the E1 tear again, code markers, and honest technical notes (crypto words live here only). */
@Composable fun HowItWorksScreen(onBack: (() -> Unit)? = null) {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String, vn: String) = words(vi, en, vn)
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(t("How it works", "Cách hoạt động"), style = PassChefDisplay)
        TearExplainer()
        CookbookSection(t("Reading a written code", "Đọc mã đã ghi")) {
            for ((code, en, vn) in listOf(
                Triple("abc#1", "secret part 1 in front", "phần bí mật số 1 ở đầu"),
                Triple("abc\$1", "secret part 1 at the end", "phần bí mật số 1 ở cuối"),
                Triple("abc@2", "secret part 2 in the middle", "phần bí mật số 2 ở giữa"),
                Triple("abc%1 / abc^1", "mixed in, 1 or 2 characters at a time", "xen kẽ từng 1 hoặc 2 ký tự"),
                Triple("_ ! ? ~", "swap front/end · CAPITALS · reversed · no symbols", "đổi đầu/cuối · viết HOA · đảo ngược · bỏ ký hiệu"),
                Triple(".k3dq", "check tag: confirms the code belongs to its sheet", "tag kiểm tra: xác nhận mã thuộc bảng tính"))) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(code, Modifier.width(120.dp), style = MaterialTheme.typography.bodyMedium.copy(fontFamily = PassChefMonoFamily), color = PassChefPalette.AccentInk)
                    Text(t(en, vn), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        CookbookSection(t("What is protected, and how", "Cái gì được bảo vệ, và bằng cách nào")) {
            for ((en, vn) in listOf(
                "Secret parts: encrypted on this phone (AES-GCM) with a key from your master password (Argon2id). Fingerprint unlock uses a hardware key that needs a strong biometric." to
                    "Phần bí mật: mã hóa trên điện thoại (AES-GCM) bằng khóa sinh từ mật khẩu chính (Argon2id). Mở bằng vân tay dùng khóa phần cứng, cần sinh trắc học mạnh.",
                "Saved sheet copies: codes, names, usernames — kept privately on this phone, not encrypted, never backed up." to
                    "Bản lưu bảng tính: mã, tên, tài khoản — giữ riêng trên điện thoại, không mã hóa, không sao lưu.",
                "Passwords: made only when you ask, hidden by default, wiped when you leave the screen or the vault locks." to
                    "Mật khẩu: chỉ tạo khi bạn cần, mặc định che, xóa khi rời màn hình hoặc két khóa.",
                "Google: access only to sheets you pick (drive.file). PassChef never writes to your sheet." to
                    "Google: chỉ có quyền với bảng tính bạn chọn (drive.file). PassChef không ghi vào bảng tính.",
                "Limits: the sheet shows account names; a weak secret part makes weak passwords; if a site leaks a password and your sheet leaks too, that one account is at risk." to
                    "Giới hạn: bảng tính lộ tên tài khoản; phần bí mật yếu cho mật khẩu yếu; nếu một trang web lộ mật khẩu và bảng tính cũng lộ, tài khoản đó gặp rủi ro.")) {
                Text(t(en, vn), style = MaterialTheme.typography.bodyMedium, color = PassChefPalette.Secondary)
            }
        }
    }
}
