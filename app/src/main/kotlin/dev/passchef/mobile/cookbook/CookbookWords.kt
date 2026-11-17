package dev.passchef.mobile.cookbook

import androidx.compose.runtime.compositionLocalOf

val LocalCookbookVietnamese = compositionLocalOf { false }
fun words(vi: Boolean, english: String, vietnamese: String) = if (vi) vietnamese else english

/**
 * Every controller/source `code` maps to on-screen words (audit #4). Plain terms (D14): sheet,
 * written code, secret part, password, private vault. Codes stay internal; never interpolates
 * provider errors or stored values.
 */
fun cookbookMessage(code: String?, vi: Boolean): String = when (code) {
    "RECIPE_MISMATCH" -> words(vi,"This code belongs to another sheet or secret-part set. Pick the right sheet.","Mã này thuộc bảng tính hoặc bộ phần bí mật khác. Chọn đúng bảng tính.")
    "INVALID_RECIPE" -> words(vi,"That doesn't look like a written code. Example: rand0m\$1","Mã chưa đúng dạng. Ví dụ: rand0m\$1")
    "MISSING_SPICE" -> words(vi,"This code needs a secret part that isn't in your vault yet. Add it in Settings › Secret parts.","Mã này cần một phần bí mật chưa có trong két. Thêm ở Cài đặt › Phần bí mật.")
    "LOCKED" -> words(vi,"Your vault locked. Unlock to continue.","Két đã khóa. Mở khóa để tiếp tục.")
    "INVALID_MENU" -> words(vi,"That isn't a Google Sheets link. It looks like docs.google.com/spreadsheets/d/…","Đây không phải link Google Trang tính. Link có dạng docs.google.com/spreadsheets/d/…")
    "OFFLINE" -> words(vi,"Showing the saved copy on this phone.","Đang dùng bản lưu trên điện thoại.")
    "CORRUPT_CACHE" -> words(vi,"A saved sheet copy is damaged. Refresh or remove it; good copies are kept.","Một bản lưu bị hỏng. Làm mới hoặc xóa nó; các bản tốt được giữ nguyên.")
    "CONNECT_REQUIRED" -> words(vi,"Google sign-in expired or was not granted. Tap Connect again.","Phiên Google hết hạn hoặc chưa được cấp quyền. Bấm Kết nối lại.")
    "NOT_CONNECTED" -> words(vi,"Not connected. Nothing was picked. Try again when ready.","Chưa kết nối, chưa chọn bảng tính nào. Thử lại khi sẵn sàng.")
    "ACCESS_DENIED" -> words(vi,"PassChef can't open this sheet. Choose it again in Google Drive (or check the Google account).","PassChef không mở được bảng tính này. Chọn lại trong Google Drive (hoặc kiểm tra tài khoản Google).")
    "NETWORK_FAILED" -> words(vi,"Couldn't reach Google. Your saved copy still works. Try again later.","Không kết nối được Google. Bản lưu vẫn dùng được. Thử lại sau.")
    "LOADING" -> words(vi,"Opening the sheet…","Đang mở bảng tính…")
    "CHOOSE_TAB" -> words(vi,"Choose the tab that holds your codes.","Chọn tab chứa các mã.")
    "CONFIRM", "RECONFIRM" -> words(vi,"Check the columns before saving.","Kiểm tra các cột trước khi lưu.")
    "SYNCED" -> words(vi,"Sheet saved on this phone. Up to date.","Đã lưu bảng tính trên điện thoại. Mới nhất.")
    "INVALID_MAPPING" -> words(vi,"Pick one column for the written code, use each column once, and a valid first data row.","Chọn một cột cho mã đã ghi, mỗi cột dùng một lần, và hàng dữ liệu đầu hợp lệ.")
    "INVALID_TABLE" -> words(vi,"This table is empty or too big. Limits: 1000 rows, 32 columns.","Bảng trống hoặc quá lớn. Giới hạn: 1000 hàng, 32 cột.")
    "CACHE_FAILED" -> words(vi,"Couldn't save on this phone (max 20 sheets). Earlier copy kept.","Không lưu được trên điện thoại (tối đa 20 bảng). Bản trước được giữ.")
    "COPIED" -> words(vi,"Password copied. Clipboard clears in 30 seconds.","Đã chép mật khẩu. Bộ nhớ tạm tự xóa sau 30 giây.")
    "USERNAME_COPIED" -> words(vi,"Username copied (not cleared automatically).","Đã chép tên đăng nhập (không tự xóa).")
    "COPY_FAILED" -> words(vi,"Couldn't copy. Open PassChef and try again.","Chưa chép được. Mở PassChef rồi thử lại.")
    else -> ""
}

/** Which statuses read as trouble (coral) vs calm. */
fun isProblem(code: String?): Boolean = code in setOf("CORRUPT_CACHE", "CONNECT_REQUIRED", "ACCESS_DENIED",
    "NETWORK_FAILED", "INVALID_MAPPING", "INVALID_TABLE", "CACHE_FAILED", "COPY_FAILED", "INVALID_MENU",
    "RECIPE_MISMATCH", "INVALID_RECIPE", "MISSING_SPICE", "LOCKED")
