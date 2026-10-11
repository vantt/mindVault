package dev.passchef.mobile.cookbook

import androidx.compose.runtime.compositionLocalOf
val LocalCookbookVietnamese = compositionLocalOf { false }
fun words(vi: Boolean, english: String, vietnamese: String) = if (vi) vietnamese else english
fun cookbookMessage(code: String?, vi: Boolean): String = when (code) {
    "RECIPE_MISMATCH" -> words(vi,"Recipe does not match this Menu or profile.","Recipe không khớp Menu hoặc hồ sơ này.")
    "INVALID_RECIPE" -> words(vi,"Check the Recipe format.","Kiểm tra định dạng Recipe.")
    "MISSING_SPICE" -> words(vi,"This spice jar is not configured.","Chưa thiết lập lọ Spices này.")
    "LOCKED" -> words(vi,"Unlock your Cookbook again.","Mở khóa Cookbook lại.")
    "INVALID_MENU" -> words(vi,"Enter a valid Menu URL or ID.","Nhập URL hoặc ID Menu hợp lệ.")
    "OFFLINE" -> words(vi,"Offline cache; Connect to refresh.","Cache ngoại tuyến; Kết nối để làm mới.")
    "CORRUPT_CACHE" -> words(vi,"Some local cache files could not be read; good caches are preserved.","Không đọc được một số cache; cache tốt được giữ nguyên.")
    "CONNECT_REQUIRED" -> words(vi,"Connect again after unlocking; Google setup/permission may be needed.","Mở khóa rồi Kết nối lại; có thể cần thiết lập hoặc cấp quyền Google.")
    "ACCESS_DENIED" -> words(vi,"This Google account cannot access this Menu.","Tài khoản Google này chưa có quyền vào Menu.")
    "NETWORK_FAILED" -> words(vi,"Could not refresh. Last good cache preserved. Maximum 1000 rows / 32 columns.","Không làm mới được. Giữ cache tốt; tối đa 1000 hàng / 32 cột.")
    "LOADING" -> words(vi,"Loading Menu…","Đang tải Menu…")
    "CHOOSE_TAB" -> words(vi,"Choose a tab to read.","Chọn tab để đọc.")
    "CONFIRM", "RECONFIRM" -> words(vi,"Confirm columns before replacing the cache.","Xác nhận cột trước khi thay cache.")
    "SYNCED" -> words(vi,"Local Menu cache ready.","Cache Menu đã sẵn sàng.")
    "INVALID_MAPPING" -> words(vi,"Choose distinct valid columns and a data row.","Chọn các cột riêng biệt và hàng dữ liệu hợp lệ.")
    "INVALID_TABLE" -> words(vi,"Table is invalid or exceeds local limits.","Bảng không hợp lệ hoặc vượt giới hạn.")
    "CACHE_FAILED" -> words(vi,"Could not save local cache; prior data preserved.","Không lưu được cache; giữ dữ liệu trước đó.")
    "COPIED" -> words(vi,"Copied.","Đã Copy.")
    "COPY_FAILED" -> words(vi,"Clipboard unavailable; no Copy confirmed.","Clipboard không khả dụng; chưa Copy.")
    else -> ""
}
