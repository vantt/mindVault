package dev.passchef.mobile.cookbook

/** Bounded, quoted A1 requests never extend beyond the declared grid. */
object SheetsReadRanges {
    fun ranges(title: String, rowCount: Int, columns: Int): List<String> {
        require(title.length in 1..200 && rowCount in 0..MAX_ROWS && columns in 0..MAX_COLUMNS)
        if(rowCount == 0) return emptyList()
        require(columns >= 1)
        val last=if(columns<=26) ('A'.code+columns-1).toChar().toString() else "A"+('A'.code+columns-27).toChar()
        val tab="'"+title.replace("'","''")+"'"
        return (1..rowCount step 100).map { start -> "$tab!A$start:$last${minOf(start+99,rowCount)}" }
    }
}
