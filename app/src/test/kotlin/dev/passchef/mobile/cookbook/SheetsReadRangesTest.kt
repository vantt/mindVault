package dev.passchef.mobile.cookbook

import org.junit.Test
import org.junit.Assert.*

class SheetsReadRangesTest {
    @Test fun quotedUnicodeTabsAndActualColumnsStayInsideGrid() {
        for((columns,last) in mapOf(1 to "A",26 to "Z",27 to "AA",32 to "AF")) {
            assertEquals(listOf("'Việt ''帳簿'''!A1:${last}100","'Việt ''帳簿'''!A101:${last}101"),
                SheetsReadRanges.ranges("Việt '帳簿'",101,columns))
        }
        assertTrue(SheetsReadRanges.ranges("Empty",0,0).isEmpty())
        try { SheetsReadRanges.ranges("Bad",1,0);fail("Nonempty grid has no columns") } catch(_:IllegalArgumentException) { }
        try { SheetsReadRanges.ranges("Bad",1001,1);fail("Oversized rows") } catch(_:IllegalArgumentException) { }
    }
}
