package dev.passchef.mobile

/**
 * Fixed local categories only; never interpolates provider errors or stored values.
 * Internal controller strings stay stable (proofs read them); UI shows plain words (D14).
 */
fun vaultStatus(status: String, vietnamese: Boolean): String {
    fun w(en: String, vi: String) = if (vietnamese) vi else en
    return when {
        status == "Cookbook locked" -> w("Your vault is locked.", "Két riêng đang khóa.")
        status.startsWith("Opening") -> w("Opening your vault…", "Đang mở két…")
        status.contains("Check your master password") -> w("That password didn't open the vault. Saved data is unchanged.", "Mật khẩu này không mở được két. Dữ liệu đã lưu không đổi.")
        status.contains("Could not save") -> w("Couldn't save. Earlier data kept. Try again.", "Không lưu được. Dữ liệu trước được giữ. Thử lại.")
        status.startsWith("Device key") -> w("Fingerprint key no longer works. Use your master password.", "Khóa vân tay không còn dùng được. Dùng mật khẩu chính.")
        status.startsWith("Biometric failed") || status.startsWith("Biometric cancelled") || status.startsWith("Biometric unavailable") ->
            w("Fingerprint cancelled or failed. Use your master password.", "Vân tay bị hủy hoặc thất bại. Dùng mật khẩu chính.")
        status.contains("locked. Try") -> w("Opened with password. Fingerprint is paused; try later.", "Đã mở bằng mật khẩu. Vân tay tạm khóa; thử lại sau.")
        status.contains("strong biometric unavailable") -> w("Opened with password.", "Đã mở bằng mật khẩu.")
        status.startsWith("Strong biometric") -> w("Fingerprint isn't set up on this phone. Use your master password.", "Điện thoại chưa cài vân tay. Dùng mật khẩu chính.")
        status.contains("again", ignoreCase = true) -> w("Please confirm your fingerprint again. Data unchanged.", "Hãy xác nhận vân tay lần nữa. Dữ liệu không đổi.")
        status.startsWith("Biometric round trip") || status.startsWith("Round trip") -> w("Vault open.", "Két đã mở.")
        else -> w("Checking access… earlier data kept.", "Đang kiểm tra quyền mở… dữ liệu trước được giữ.")
    }
}

/** True for statuses that should read as an error on the unlock screen. */
fun vaultStatusIsProblem(status: String): Boolean = status != "Cookbook locked" && !status.startsWith("Opening") &&
    (status.contains("Check your") || status.contains("Could not") || status.startsWith("Device") ||
        status.startsWith("Biometric") || status.startsWith("Strong"))
