package dev.passchef.mobile.proof

import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.passchef.mobile.WrapFailureProbe
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WrapFailureProbeTest {
    @Test fun summaryIncludesClassesButNeverThrowableMessages() {
        val marker = "SYNTHETIC_MESSAGE_MUST_NOT_LEAK"
        val error = IllegalStateException(marker, IllegalArgumentException(marker))
        val result = WrapFailureProbe.summary("wrap-doFinal", error) { 2 }
        assertTrue("Known phase recorded", result.contains("phase=wrap-doFinal"))
        assertTrue("Exception classes recorded", result.contains("java.lang.IllegalStateException->java.lang.IllegalArgumentException"))
        assertTrue("Exception messages excluded", !result.contains(marker))
    }
    @Test fun unknownPhaseAndFailedInspectionAreSanitized() {
        val marker = "SYNTHETIC_PHASE_MUST_NOT_LEAK"
        val result = WrapFailureProbe.summary(marker, IllegalStateException(marker)) { throw IllegalArgumentException(marker) }
        assertTrue("Unknown phase replaced", result.contains("phase=unknown"))
        assertTrue("No phase or exception value exposed", !result.contains(marker))
    }
}
