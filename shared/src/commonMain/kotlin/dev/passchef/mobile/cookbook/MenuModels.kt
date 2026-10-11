package dev.passchef.mobile.cookbook

import kotlinx.serialization.Serializable
import dev.passchef.mobile.recipe.RecipeParser

const val MAX_ROWS = 1000
const val MAX_COLUMNS = 32
const val MAX_CELL = 4096
@Serializable data class MenuOrigin(val menuId: String, val tabId: Int, val title: String) {
    init { require(menuId.matches(Regex("[A-Za-z0-9_-]{10,200}")) && tabId >= 0 && title.length in 1..200) }
    val key: String get() = "$menuId:$tabId"
}
@Serializable enum class ColumnRole { RECIPE, LABEL, USERNAME, URL, NOTES }
@Serializable data class MenuMapping(val origin: MenuOrigin, val columns: Map<ColumnRole, Int>,
    val firstDataRow: Int = 1, val confirmedHeader: List<String>? = null, val manualLabels: Map<Int, String> = emptyMap()) {
    init {
        require(ColumnRole.RECIPE in columns && columns.values.all { it in 0 until MAX_COLUMNS })
        require(columns.values.distinct().size == columns.size && firstDataRow in 1..MAX_ROWS)
        require(confirmedHeader == null || (firstDataRow > 1 && confirmedHeader.size <= MAX_COLUMNS && confirmedHeader.all { it.length <= MAX_CELL }))
        require(manualLabels.size <= MAX_ROWS && manualLabels.all { it.key in 1..MAX_ROWS && it.value.length <= MAX_CELL })
    }
}
@Serializable data class MenuEntry(val origin: MenuOrigin, val row: Int, val recipe: String,
    val label: String, val username: String = "", val url: String = "", val notes: String = "") {
    val id: String get() = "${origin.key}:$row"
    init { require(row in 1..MAX_ROWS && listOf(recipe,label,username,url,notes).all { it.length <= MAX_CELL }) }
}
@Serializable data class MenuSnapshot(val version: Int = 1, val mapping: MenuMapping,
    val entries: List<MenuEntry>, val refreshedAt: Long, val favorites: Set<String> = emptySet(),
    val recent: List<String> = emptyList()) {
    init {
        require(version == 1 && entries.size <= MAX_ROWS && refreshedAt >= 0)
        require(entries.all { it.origin == mapping.origin && it.row >= mapping.firstDataRow && RecipeParser.parse(it.recipe) != null })
        require(entries.map { it.id }.distinct().size == entries.size)
        val ids = entries.map { it.id }.toSet()
        require(favorites.all { it in ids } && recent.size <= 20 && recent.distinct().size == recent.size && recent.all { it in ids })
    }
}
fun menuIdFromInput(input: String): String? {
    val value = input.trim()
    val id = Regex("^https://docs\\.google\\.com/spreadsheets/d/([A-Za-z0-9_-]{10,200})(?:/[^?#]*)?(?:[?#].*)?$")
        .matchEntire(value)?.groupValues?.get(1) ?: value
    return id.takeIf { it.matches(Regex("[A-Za-z0-9_-]{10,200}")) }
}
fun searchEntries(entries: List<MenuEntry>, query: String): List<MenuEntry> = entries.filter {
    query.isBlank() || it.label.contains(query, true) || it.username.contains(query, true)
}
