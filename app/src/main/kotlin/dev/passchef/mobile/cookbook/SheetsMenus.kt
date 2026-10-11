package dev.passchef.mobile.cookbook

import com.passchef.sheets.*
import io.ktor.client.engine.android.Android

/** Canonical portable client with an actual Android engine; no desktop OAuth code. */
object SheetsMenus {
    suspend fun metadata(token: String, id: String): SpreadsheetMetadata {
        val client = SheetsClient(Android.create(), token)
        return try { client.getSpreadsheet(id) } finally { client.close() }
    }
    suspend fun rows(token: String, id: String, tab: TabProperties): List<List<String>> {
        val ranges=SheetsReadRanges.ranges(tab.title,tab.gridProperties.rowCount,tab.gridProperties.columnCount)
        if(ranges.isEmpty()) return emptyList()
        val client = SheetsClient(Android.create(), token)
        return try {
            val rows = mutableListOf<List<String>>()
            for ((chunk,range) in ranges.withIndex()) {
                val start=chunk*100+1
                val end = minOf(start + 99, tab.gridProperties.rowCount)
                val page = client.getValues(id, range).values
                require(page.size <= end - start + 1)
                rows.addAll(page)
                repeat(end - start + 1 - page.size) { rows.add(emptyList()) }
                MenuTable.validate(rows)
            }
            rows
        } finally { client.close() }
    }
}
