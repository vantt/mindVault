package dev.passchef.mobile.proof

import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.passchef.mobile.ExpiredWrapFailure
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import javax.crypto.IllegalBlockSizeException

/** Numeric classifier tests only; never authorize or simulate a biometric operation. */
@RunWith(AndroidJUnit4::class)
class ExpiredWrapFailureTest {
    @Test fun textMatchingAndGenericExceptionNeverTriggerRetry() {
        assertNull(ExpiredWrapFailure.code(IllegalBlockSizeException("ERROR_KEY_OPERATION_EXPIRED code15")))
        assertNull(ExpiredWrapFailure.code(IllegalStateException("15")))
    }
    @Test fun nestedGenericCausesNeverBecomeNumericProviderFailures() {
        var error: Throwable = IllegalStateException("15")
        repeat(5) { error = IllegalStateException("SYNTHETIC_UNREAD_MESSAGE", error) }
        assertNull(ExpiredWrapFailure.code(error))
    }
}
