package dev.passchef.mobile.cookbook

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.navigation.MappingChoices
import dev.passchef.mobile.ui.*

fun columnLetter(index: Int): String = if (index < 26) ('A' + index).toString() else "A" + ('A' + index - 26)
/** Accepts "3" or "C" (case-insensitive); returns a 0-based index or null. */
fun columnIndex(text: String): Int? {
    val value = text.trim().uppercase()
    value.toIntOrNull()?.let { return (it - 1).takeIf { i -> i in 0 until MAX_COLUMNS } }
    return (0 until MAX_COLUMNS).firstOrNull { columnLetter(it) == value }
}
fun roleName(role: ColumnRole, vi: Boolean) = when (role) {
    ColumnRole.RECIPE -> words(vi, "Written code ★", "Mã đã ghi ★"); ColumnRole.LABEL -> words(vi, "Name", "Tên")
    ColumnRole.USERNAME -> words(vi, "Username", "Tên đăng nhập"); ColumnRole.URL -> words(vi, "Website", "Trang web")
    ColumnRole.NOTES -> words(vi, "Notes", "Ghi chú")
}
/** Suggestions from header names + code shape; proposed only, the user confirms (spec rule). */
fun suggestedChoices(draft: MappingDraft): MappingChoices {
    val header = draft.rows.firstOrNull().orEmpty()
    val hasHeader = header.any { MenuTable.headerRole(it) != null }
    val columns = ColumnRole.entries.associateWith { role ->
        val index = if (role == ColumnRole.RECIPE) MenuTable.proposal(draft.rows, if (hasHeader) 2 else 1)
            else header.indexOfFirst { MenuTable.headerRole(it) == role }.takeIf { it >= 0 }
        index?.let(::columnLetter).orEmpty()
    }
    return MappingChoices(columns, if (hasHeader) "2" else "1")
}

/** CM3: preview table with letters + headers, one field per role (tags `column-<ROLE>`), first data row. */
@Composable fun MappingScreen(sources: CookbookSources, draft: MappingDraft, initial: MappingChoices?, onReview: (MappingChoices) -> Unit) {
    val vi = LocalCookbookVietnamese.current
    var choices by remember(draft) { mutableStateOf(initial ?: suggestedChoices(draft)) }
    val width = (draft.rows.maxOfOrNull { it.size } ?: 0).coerceAtMost(MAX_COLUMNS)
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(words(vi, "Which column is which?", "Cột nào là gì?"), style = PassChefDisplay)
        Text(words(vi, "We guessed — check them. Only the written-code column is required.", "Đã đoán sẵn — hãy kiểm tra. Chỉ cột mã đã ghi là bắt buộc."),
            color = PassChefPalette.Secondary)
        if (sources.status == "RECONFIRM") CookbookStatus(words(vi, "Your sheet changed — check the columns.", "Bảng tính đã đổi — kiểm tra lại các cột."), tone = CookbookTone.Warning)
        Surface(Modifier.fillMaxWidth(), color = PassChefPalette.Surface, shape = MaterialTheme.shapes.medium,
            border = BorderStroke(1.dp, PassChefPalette.Hairline)) {
            Column(Modifier.horizontalScroll(rememberScrollState()).padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row { for (c in 0 until width) Cell(columnLetter(c), header = true) }
                draft.rows.take(6).forEachIndexed { i, row ->
                    Row { for (c in 0 until width) Cell(row.getOrNull(c).orEmpty(), header = i == 0 && choices.firstRow == "2") }
                }
            }
        }
        for (role in ColumnRole.entries) Row(verticalAlignment = Alignment.CenterVertically) {
            Text(roleName(role, vi), Modifier.weight(1f))
            OutlinedTextField(choices.columns[role].orEmpty(), { v -> if (v.length <= 2) choices = choices.copy(columns = choices.columns + (role to v.uppercase())) },
                singleLine = true, placeholder = { Text("—") },
                modifier = Modifier.width(96.dp).testTag("column-${role.name}").semantics { contentDescription = roleName(role, vi).removeSuffix(" ★") },
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontFamily = PassChefMonoFamily))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(words(vi, "First row is headings", "Hàng đầu là tiêu đề"), Modifier.weight(1f))
            Switch(choices.firstRow == "2", { choices = choices.copy(firstRow = if (it) "2" else "1") })
        }
        OutlinedTextField(choices.firstRow, { if (it.length <= 4) choices = choices.copy(firstRow = it) }, singleLine = true,
            label = { Text(words(vi, "Advanced: data starts at row", "Nâng cao: dữ liệu bắt đầu từ hàng")) }, modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontFamily = PassChefMonoFamily))
        val recipeSet = choices.columns[ColumnRole.RECIPE]?.let(::columnIndex) != null
        if (!recipeSet) Text(words(vi, "Pick the column with the written codes.", "Chọn cột chứa mã đã ghi."), color = PassChefPalette.Honey)
        if (sources.status == "INVALID_MAPPING") CookbookStatus(cookbookMessage("INVALID_MAPPING", vi), tone = CookbookTone.Error)
        CookbookPrimaryAction(words(vi, "Looks right — Review", "Đúng rồi — Xem lại"), onClick = { onReview(choices) },
            // Stays tappable without a code column: the tap is refused explicitly (INVALID_MAPPING) with the helper above.
            enabled = !sources.busy, modifier = Modifier.fillMaxWidth().testTag("confirm-columns"))
    }
}

@Composable private fun Cell(text: String, header: Boolean) {
    Text(text.take(14) + if (text.length > 14) "…" else "", Modifier.width(96.dp).padding(end = 6.dp), maxLines = 1,
        color = if (header) PassChefPalette.Muted else PassChefPalette.Text,
        style = MaterialTheme.typography.bodySmall.copy(fontFamily = PassChefMonoFamily))
}
