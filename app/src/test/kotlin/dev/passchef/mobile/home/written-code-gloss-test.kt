package dev.passchef.mobile.home

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WrittenCodeGlossTest {
    @Test fun positionsMatchExtensionCombine() {
        assertEquals("= add secret part 1 at the end", writtenCodeGloss("rand0m\$1", false))
        assertEquals("= add secret part 2 in front", writtenCodeGloss("x9Lk2m#2", false))
        assertEquals("= put secret part 3 in the middle", writtenCodeGloss("abc@3", false))
        assertEquals("= thêm phần bí mật số 1 vào cuối", writtenCodeGloss("rand0m\$1", true))
    }
    @Test fun underscoreSwapsFrontAndEndAndModifiersAreNamed() {
        assertEquals("= add secret part 1 at the end", writtenCodeGloss("abc#1_", false))
        assertEquals("= add secret part 1 in front, secret part in CAPITALS", writtenCodeGloss("abc$1_!", false))
        assertEquals("= add secret part 2 at the end · Family set", writtenCodeGloss("abc$2", false, "Family"))
    }
    @Test fun notACode() { assertNull(writtenCodeGloss("hello world", false)) }
}
