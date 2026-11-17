package dev.passchef.mobile.connect

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.cookbook.*
import dev.passchef.mobile.navigation.AppRouteState
import dev.passchef.mobile.ui.*

/**
 * CM1: Google Drive Picker first (drive.file: PassChef sees only the sheet picked). Other ways:
 * Share from the Sheets app, paste a link (opens the Picker filtered to that sheet), offline table.
 * Clipboard is read only when the user taps Paste.
 */
@Composable fun ConnectChooser(cookbook: CookbookController, routes: AppRouteState) {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String, vn: String) = words(vi, en, vn)
    val sources = cookbook.sources
    val clipboard = LocalClipboardManager.current
    var open by remember { mutableStateOf<String?>(null) }
    var link by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var table by remember { mutableStateOf("") }
    Text(t("Connect a sheet", "Kết nối bảng tính"), style = PassChefDisplay)
    Text(t("Pick the Google Sheet that holds your written codes.", "Chọn bảng tính Google chứa các mã đã ghi."), color = PassChefPalette.Secondary)
    val resume = routes.connect.fileId
    val shared = routes.sharedSheetId
    if (resume != null) CookbookSection(t("Continue connecting", "Tiếp tục kết nối")) {
        Text(t("Your vault locked while connecting. Access granted? Continue where you were.", "Két đã khóa giữa chừng. Đã cấp quyền? Tiếp tục từ chỗ cũ."), color = PassChefPalette.Secondary)
        CookbookPrimaryAction(t("Continue", "Tiếp tục"), onClick = { cookbook.continueWith(resume) }, enabled = !sources.busy,
            modifier = Modifier.fillMaxWidth().testTag("connect-continue"))
    } else if (shared != null) CookbookSection(t("Use this shared sheet?", "Dùng bảng tính vừa chia sẻ?")) {
        Text(t("Google will ask you to confirm this one sheet.", "Google sẽ hỏi bạn xác nhận đúng bảng tính này."), color = PassChefPalette.Secondary)
        CookbookPrimaryAction(t("Confirm in Google", "Xác nhận với Google"), onClick = { cookbook.pick(shared) }, enabled = !sources.busy,
            modifier = Modifier.fillMaxWidth().testTag("connect-shared"))
    }
    if (sources.busy || sources.status in setOf("NOT_CONNECTED", "CONNECT_REQUIRED", "ACCESS_DENIED", "NETWORK_FAILED", "INVALID_TABLE", "CACHE_FAILED") || cookbook.code != null)
        CookbookStatus(cookbookMessage(cookbook.code ?: sources.status, vi),
            tone = if (sources.busy || sources.status == "NOT_CONNECTED") CookbookTone.Warning else CookbookTone.Error)
    Surface(Modifier.fillMaxWidth(), color = PassChefPalette.Surface, shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, PassChefPalette.AccentInk)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(PassChefIcons.Drive, contentDescription = null, tint = PassChefPalette.AccentInk); Spacer(Modifier.width(10.dp))
                Text(t("Choose from Google Drive", "Chọn từ Google Drive"), style = MaterialTheme.typography.titleMedium)
            }
            Text(t("PassChef sees only the sheet you pick. Google will show your Drive.", "PassChef chỉ thấy bảng tính bạn chọn. Google sẽ hiện Drive của bạn."),
                style = MaterialTheme.typography.bodySmall, color = PassChefPalette.Secondary)
            CookbookPrimaryAction(t("Open Google Drive", "Mở Google Drive"), onClick = { routes.sharedSheetId = null; cookbook.pick(null) },
                enabled = !sources.busy, modifier = Modifier.fillMaxWidth().testTag("connect-pick-drive"))
        }
    }
    PassChefCaption(t("Other ways", "Cách khác"))
    Way(t("Share from the Sheets app", "Chia sẻ từ ứng dụng Trang tính"), PassChefIcons.Share, "connect-share-help", open == "share") { open = if (open == "share") null else "share" }
    if (open == "share") Text(t("In Google Sheets: open the sheet › ⋮ › Share & export › Send a copy / Copy link › choose PassChef. You'll confirm the sheet in Google.",
        "Trong Google Trang tính: mở bảng › ⋮ › Chia sẻ và xuất › Gửi/Sao chép đường liên kết › chọn PassChef. Bạn sẽ xác nhận bảng tính với Google."),
        style = MaterialTheme.typography.bodySmall, color = PassChefPalette.Secondary)
    Way(t("Paste a link", "Dán đường liên kết"), PassChefIcons.Link, "connect-paste", open == "paste") { open = if (open == "paste") null else "paste" }
    if (open == "paste") Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(link, { if (it.length <= 300) { cookbook.edit(); link = it } }, singleLine = true,
            label = { Text(t("Google Sheets link", "Link Google Trang tính")) }, enabled = !sources.busy,
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontFamily = PassChefMonoFamily), modifier = Modifier.fillMaxWidth().testTag("source-id"))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CookbookSecondaryAction(t("Paste", "Dán"), onClick = { clipboard.getText()?.text?.take(300)?.let { link = it.trim() } })
            CookbookSecondaryAction(t("Continue", "Tiếp tục"), onClick = { cookbook.connect(link) }, enabled = link.isNotBlank() && !sources.busy)
        }
    }
    Way(t("Offline table (no Google)", "Bảng ngoại tuyến (không cần Google)"), PassChefIcons.Table, "connect-offline", open == "offline") { open = if (open == "offline") null else "offline" }
    if (open == "offline") Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(t("Copy cells from any spreadsheet, paste here. Columns separated by tabs.", "Sao chép các ô từ bảng tính bất kỳ rồi dán vào đây. Cột cách nhau bằng tab."),
            style = MaterialTheme.typography.bodySmall, color = PassChefPalette.Secondary)
        OutlinedTextField(title, { if (it.length <= 200) title = it }, singleLine = true, label = { Text(t("Name for this table", "Tên cho bảng này")) },
            modifier = Modifier.fillMaxWidth())
        OutlinedTextField(table, { if (it.length <= 120_000) table = it }, minLines = 3, label = { Text(t("Rows", "Các hàng")) },
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = PassChefMonoFamily), modifier = Modifier.fillMaxWidth().testTag("source-table"))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CookbookSecondaryAction(t("Paste", "Dán"), onClick = { clipboard.getText()?.text?.take(120_000)?.let { table = it } })
            CookbookSecondaryAction(t("Preview columns", "Xem trước cột"), onClick = { cookbook.offlineSource(title.trim(), table) },
                enabled = title.isNotBlank() && table.isNotBlank() && !sources.busy)
        }
    }
    Text(t("Sheet copies on this phone are not encrypted (they hold only codes). Secret parts are encrypted.",
        "Bản lưu bảng tính trên điện thoại không mã hóa (chỉ chứa mã). Phần bí mật thì được mã hóa."),
        style = MaterialTheme.typography.bodySmall, color = PassChefPalette.Muted)
}

@Composable private fun Way(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, tag: String, expanded: Boolean, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = 52.dp).clickable(onClick = onClick).testTag(tag), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = PassChefPalette.Secondary); Spacer(Modifier.width(12.dp))
        Text(label, Modifier.weight(1f), maxLines = 2, overflow = TextOverflow.Ellipsis)
        Text(if (expanded) "▾" else "›", color = PassChefPalette.Muted)
    }
}

fun mappingColumns(choices: dev.passchef.mobile.navigation.MappingChoices): Map<ColumnRole, Int> =
    choices.columns.filterValues { it.isNotBlank() }.mapValues { (_, v) -> columnIndex(v) ?: -1 }
/** Same projection the save will do; null = the mapping would be refused (shown on CM3). */
fun mappingPreview(draft: MappingDraft, choices: dev.passchef.mobile.navigation.MappingChoices): List<MenuEntry>? = runCatching {
    val firstRow = choices.firstRow.toIntOrNull() ?: 0
    MenuTable.project(draft.rows, MenuMapping(draft.origin, mappingColumns(choices), firstRow,
        if (firstRow > 1) draft.rows.getOrNull(firstRow - 2) else null))
}.getOrNull()

/** CM4: sample rows exactly as list rows + count; saving replaces the cache only after this tap. */
@Composable fun ReviewMapping(sources: CookbookSources, draft: MappingDraft, choices: dev.passchef.mobile.navigation.MappingChoices) {
    val vi = LocalCookbookVietnamese.current
    val columns = mappingColumns(choices)
    val firstRow = choices.firstRow.toIntOrNull() ?: 0
    val preview = mappingPreview(draft, choices)
    Text(words(vi, "Review", "Xem lại"), style = PassChefDisplay)
    if (preview == null) { CookbookStatus(cookbookMessage("INVALID_MAPPING", vi), tone = CookbookTone.Error); return }
    Text(words(vi, "${preview.size} passwords will be saved on this phone (codes only, not encrypted).",
        "${preview.size} mật khẩu sẽ lưu trên điện thoại (chỉ mã, không mã hóa)."), color = PassChefPalette.Secondary)
    preview.take(3).forEach { entry ->
        dev.passchef.mobile.home.PasswordListRow(entry, PassChefPalette.Outline, null, busy = true, onOpen = {}, onCopy = {})
    }
    if (sources.status in setOf("INVALID_MAPPING", "CACHE_FAILED")) CookbookStatus(cookbookMessage(sources.status, vi), tone = CookbookTone.Error)
    CookbookPrimaryAction(words(vi, "Save to my phone", "Lưu vào điện thoại"), onClick = { sources.confirm(columns, firstRow, emptyMap()) },
        enabled = preview.isNotEmpty() && !sources.busy, modifier = Modifier.fillMaxWidth().testTag("connect-save"))
}
