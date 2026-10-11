package dev.passchef.mobile

/** Fixed local categories only; never interpolates provider errors or stored values. */
fun vaultStatus(status: String, vietnamese: Boolean): String {
    if (!vietnamese) return status
    return when {
        status == "Cookbook locked" -> "Cookbook đã khóa. Dùng mật khẩu chính hoặc sinh trắc học."
        status.startsWith("Opening") -> "Đang mở Cookbook…"
        status.contains("Check your master password") -> "Không mở được Cookbook. Kiểm tra mật khẩu chính; dữ liệu đã lưu được giữ nguyên."
        status.contains("Could not save") -> "Không lưu được Cookbook; dữ liệu trước đó được giữ nguyên. Hãy thử lại."
        status.startsWith("Device key") -> "Khóa thiết bị không dùng được. Dùng mật khẩu chính ban đầu để phục hồi."
        status.startsWith("Biometric failed") -> "Sinh trắc học thất bại hoặc đã hủy. Dùng mật khẩu chính."
        status.contains("locked. Try") -> "Cookbook mở bằng mật khẩu; sinh trắc học đang bị khóa. Thử lại sau."
        status.contains("strong biometric unavailable") -> "Cookbook mở bằng mật khẩu; sinh trắc học mạnh không khả dụng."
        status.startsWith("Strong biometric") -> "Sinh trắc học mạnh không khả dụng. Dùng mật khẩu chính."
        status.contains("again", ignoreCase = true) -> "Phiên cấp quyền thiết bị đã hết hạn. Xác nhận sinh trắc học mới để tiếp tục; dữ liệu được giữ nguyên."
        status.startsWith("Biometric round trip") -> "Cookbook đã mở bằng sinh trắc học; các lọ Spices đã được xác thực."
        else -> "Đang xác nhận quyền mở Cookbook; dữ liệu trước đó được giữ nguyên."
    }
}
