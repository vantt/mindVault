package dev.passchef.mobile.cookbook

import dev.passchef.mobile.recipe.RecipeParser

data class RecipeColumn(val index: Int, val valid: Int, val nonempty: Int) {
    val score: Double get() = if (nonempty == 0) 0.0 else valid.toDouble() / nonempty
}
object MenuTable {
    fun validate(rows: List<List<String>>) {
        require(rows.size <= MAX_ROWS && rows.all { it.size <= MAX_COLUMNS && it.all { cell -> cell.length <= MAX_CELL } })
        require(rows.sumOf { row -> row.sumOf { it.length.toLong() } } <= 120_000)
    }
    fun candidates(rows: List<List<String>>, firstDataRow: Int = 1): List<RecipeColumn> {
        validate(rows); require(firstDataRow in 1..MAX_ROWS)
        val data = rows.drop(firstDataRow - 1)
        return (0 until (rows.maxOfOrNull { it.size } ?: 0)).map { col ->
            val cells = data.mapNotNull { it.getOrNull(col) }.filter { it.isNotBlank() }
            RecipeColumn(col, cells.count { RecipeParser.parse(it) != null }, cells.size)
        }.filter { it.valid >= 3 && it.score >= 0.6 }.sortedByDescending { it.score }
    }
    fun proposal(rows: List<List<String>>, firstDataRow: Int = 1): Int? {
        val candidates = candidates(rows, firstDataRow)
        return candidates.firstOrNull()?.takeIf { first -> candidates.drop(1).none { it.score == first.score } }?.index
    }
    fun headerRole(text: String): ColumnRole? = when (text.trim().lowercase()) {
        "recipe", "công thức", "cong thuc" -> ColumnRole.RECIPE
        "label", "name", "tên", "ten" -> ColumnRole.LABEL
        "username", "user", "tài khoản", "tai khoan" -> ColumnRole.USERNAME
        "url", "website", "trang web" -> ColumnRole.URL
        "notes", "note", "ghi chú", "ghi chu" -> ColumnRole.NOTES
        else -> null
    }
    fun project(rows: List<List<String>>, mapping: MenuMapping): List<MenuEntry> {
        validate(rows)
        return rows.mapIndexedNotNull { offset, row ->
            if (offset + 1 < mapping.firstDataRow) return@mapIndexedNotNull null
            fun cell(role: ColumnRole) = mapping.columns[role]?.let { row.getOrNull(it) } ?: ""
            val recipe = cell(ColumnRole.RECIPE)
            if (RecipeParser.parse(recipe) == null) return@mapIndexedNotNull null
            MenuEntry(mapping.origin, offset + 1, recipe,
                mapping.manualLabels[offset + 1] ?: cell(ColumnRole.LABEL).ifBlank { "Row ${offset + 1}" },
                cell(ColumnRole.USERNAME), cell(ColumnRole.URL), cell(ColumnRole.NOTES))
        }
    }
    fun refresh(rows: List<List<String>>, previous: MenuSnapshot, now: Long): MenuSnapshot {
        validate(rows)
        previous.mapping.confirmedHeader?.let { require(rows.getOrNull(previous.mapping.firstDataRow - 2) == it) { "Mapping drift" } }
        val data = rows.drop(previous.mapping.firstDataRow - 1)
        val cells = data
            .mapNotNull { it.getOrNull(previous.mapping.columns.getValue(ColumnRole.RECIPE)) }.filter { it.isNotBlank() }
        require(cells.isNotEmpty() || data.all { row -> row.all { it.isBlank() } }) { "Ambiguous empty column" }
        require(cells.isEmpty() || cells.count { RecipeParser.parse(it) != null }.toDouble() / cells.size >= 0.6) { "Mapping drift" }
        val entries = project(rows, previous.mapping)
        val ids = entries.map { it.id }.toSet()
        return previous.copy(entries = entries, refreshedAt = now, favorites = previous.favorites.intersect(ids), recent = previous.recent.filter { it in ids })
    }
}
