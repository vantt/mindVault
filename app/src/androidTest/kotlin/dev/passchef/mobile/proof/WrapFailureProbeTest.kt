package dev.passchef.mobile.proof

import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.passchef.mobile.WrapFailureProbe
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import javax.crypto.AEADBadTagException
import javax.crypto.IllegalBlockSizeException

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
    @Test fun unwrapDoFinalIncludesCipherClassWithoutMessage() {
        val marker = "SYNTHETIC_UNWRAP_MESSAGE_MUST_NOT_LEAK"
        val result = WrapFailureProbe.summary("unwrap-doFinal", IllegalBlockSizeException(marker)) { 2 }
        assertTrue("Unwrap operation phase recorded", result.contains("phase=unwrap-doFinal "))
        assertTrue("Cipher exception class recorded", result.contains("classes=javax.crypto.IllegalBlockSizeException"))
        assertTrue("Cipher exception message excluded", !result.contains(marker))
    }
    @Test fun unwrapCacheIncludesAuthenticationClassWithoutMessage() {
        val marker = "SYNTHETIC_CACHE_MESSAGE_MUST_NOT_LEAK"
        val result = WrapFailureProbe.summary("unwrap-cache", AEADBadTagException(marker)) { 2 }
        assertTrue("Repository/cache phase recorded", result.contains("phase=unwrap-cache "))
        assertTrue("Authentication exception class recorded", result.contains("classes=javax.crypto.AEADBadTagException"))
        assertTrue("Authentication exception message excluded", !result.contains(marker))
    }
    @Test fun unwrapPhaseInjectionIsRejected() {
        val marker = "SYNTHETIC_PHASE_INJECTION_MUST_NOT_LEAK"
        val result = WrapFailureProbe.summary("unwrap-cache $marker", AEADBadTagException(marker)) { 2 }
        assertTrue("Only exact phase is accepted", result.contains("phase=unknown "))
        assertTrue("Injected phase and message excluded", !result.contains(marker))
    }
}
