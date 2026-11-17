package dev.passchef.mobile.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.cookbook.LocalCookbookVietnamese
import dev.passchef.mobile.cookbook.MenuEntry
import dev.passchef.mobile.cookbook.MenuSnapshot
import dev.passchef.mobile.cookbook.isLocalMenuId
import dev.passchef.mobile.cookbook.words
import dev.passchef.mobile.ui.*

/** Sheet origin key → secret-part set name; empty while only one set exists (D17 hides sets). */
val LocalSheetSetNames = compositionLocalOf<Map<String, String>> { emptyMap() }

/** Identity strip colours (extension profile palette). Identity only, never an action. Q11: auto-assigned. */
private val SheetColors = listOf(Color(0xFF58A6FF), Color(0xFFE3B341), Color(0xFF3FB950), Color(0xFFA371F7))
fun sheetColor(index: Int): Color = SheetColors[index.mod(SheetColors.size)]

fun ageWords(refreshedAt: Long, vi: Boolean, now: Long = System.currentTimeMillis()): String {
    val minutes = ((now - refreshedAt).coerceAtLeast(0) / 60_000)
    return when {
        minutes < 1 -> words(vi, "updated just now", "vừa cập nhật")
        minutes < 60 -> words(vi, "updated $minutes min ago", "cập nhật $minutes phút trước")
        minutes < 48 * 60 -> words(vi, "updated ${minutes / 60} h ago", "cập nhật ${minutes / 60} giờ trước")
        else -> words(vi, "saved copy, ${minutes / 1440} days old", "bản lưu, ${minutes / 1440} ngày trước")
    }
}
fun isStale(snapshot: MenuSnapshot, now: Long = System.currentTimeMillis()) =
    !isLocalMenuId(snapshot.mapping.origin.menuId) && now - snapshot.refreshedAt > 7 * 24 * 3_600_000L

private fun domain(url: String) = url.substringAfter("://").substringBefore('/').removePrefix("www.").take(40)

/** One password row: initial tile, label, username · site, written code + gloss, copy button (D12). */
@Composable fun PasswordListRow(entry: MenuEntry, color: Color, badge: String?, busy: Boolean,
    onOpen: () -> Unit, onCopy: () -> Unit) {
    val vi = LocalCookbookVietnamese.current
    Row(Modifier.fillMaxWidth().heightIn(min = 64.dp).clickable(onClick = onOpen).testTag("menu-row-${entry.id}")
        .padding(start = 12.dp, end = 4.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.width(3.dp).height(44.dp).background(color))
        Spacer(Modifier.width(10.dp))
        Box(Modifier.size(40.dp).background(PassChefPalette.Raised, MaterialTheme.shapes.small), contentAlignment = Alignment.Center) {
            Text(entry.label.filter { it.isLetterOrDigit() }.take(2).uppercase().ifEmpty { "•" }, fontWeight = FontWeight.SemiBold,
                color = PassChefPalette.Secondary)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(entry.label, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            val meta = listOf(entry.username, domain(entry.url)).filter { it.isNotBlank() }.joinToString(" · ")
            if (meta.isNotEmpty()) Text(meta, style = MaterialTheme.typography.bodySmall, color = PassChefPalette.Secondary,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            WrittenCode(entry.recipe, LocalSheetSetNames.current[entry.origin.key])
            // Identity colours fail contrast as text on light; colour goes in a dot, the name stays text colour.
            if (badge != null) Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.testTag("sheet-badge-${entry.origin.key}")) {
                Box(Modifier.size(8.dp).background(color, androidx.compose.foundation.shape.CircleShape))
                Spacer(Modifier.width(6.dp))
                Text(badge, style = MaterialTheme.typography.labelSmall, color = PassChefPalette.Secondary)
            }
        }
        IconButton(onClick = onCopy, enabled = !busy, modifier = Modifier.size(48.dp).testTag("menu-row-copy-${entry.id}")
            .semantics { contentDescription = words(vi, "Copy password for ${entry.label}", "Chép mật khẩu ${entry.label}") }) {
            Icon(PassChefIcons.Copy, contentDescription = null, tint = PassChefPalette.Text)
        }
    }
}

/** Sticky section header in "All sheets": strip = legend for row colours; name, count, status, refresh, collapse. */
@Composable fun SheetSectionHeader(snapshot: MenuSnapshot, color: Color, collapsed: Boolean, busy: Boolean,
    onToggle: () -> Unit, onRefresh: () -> Unit) {
    val vi = LocalCookbookVietnamese.current
    val key = snapshot.mapping.origin.key
    Row(Modifier.fillMaxWidth().background(PassChefPalette.Canvas).clickable(onClick = onToggle).testTag("sheet-section-$key")
        .padding(start = 12.dp, top = 10.dp, bottom = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.width(4.dp).height(28.dp).background(color))
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(snapshot.mapping.origin.title, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            PassChefCaption("${snapshot.entries.size} · " + ageWords(snapshot.refreshedAt, vi),
                color = if (isStale(snapshot)) PassChefPalette.Honey else PassChefPalette.Muted)
        }
        if (!isLocalMenuId(snapshot.mapping.origin.menuId)) IconButton(onClick = onRefresh, enabled = !busy,
            modifier = Modifier.testTag("sheet-refresh-$key").semantics { contentDescription = words(vi, "Refresh", "Làm mới") }) {
            Icon(PassChefIcons.Refresh, contentDescription = null, tint = PassChefPalette.Secondary)
        }
        Text(if (collapsed) "▸" else "▾", Modifier.padding(horizontal = 12.dp), color = PassChefPalette.Muted)
    }
}
