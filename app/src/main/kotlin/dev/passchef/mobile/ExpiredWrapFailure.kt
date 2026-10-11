package dev.passchef.mobile

import android.os.Build
import android.security.KeyStoreException
import dev.passchef.mobile.vault.WrapFailureCauses

/** Public numeric classification only; older Android keeps manual password guidance. */
object ExpiredWrapFailure {
    fun code(error: Throwable): Int? {
        if (Build.VERSION.SDK_INT < 33) return null
        return WrapFailureCauses.code(error) { (it as? KeyStoreException)?.numericErrorCode }
    }
}
