package dev.passchef.mobile

import android.os.Build
import android.security.KeyStoreException
import android.util.Log

/** Debug physical diagnosis only: no exception messages, aliases or owned bytes. */
object WrapFailureProbe {
    fun biometricError(code: Int) {
        runCatching { Log.i("PassChefProof", "source=BiometricPrompt.onAuthenticationError code=$code") }
    }
    fun record(phase: String, error: Throwable, securityLevel: () -> Int) {
        runCatching { Log.i("PassChefProof", summary(phase, error, securityLevel)) }
    }
    fun summary(phase: String, error: Throwable, securityLevel: () -> Int): String {
        val safePhase = phase.takeIf { it in setOf("wrap-doFinal", "unwrap-doFinal", "unwrap-cache",
            "record-commit", "key-retirement", "session-delivery") } ?: "unknown"
        val causes = generateSequence(error) { it.cause }.take(5).toList()
        val level = if (Build.VERSION.SDK_INT >= 31) runCatching { securityLevel().toString() }.getOrDefault("unavailable") else "unsupported"
        val numeric = if (Build.VERSION.SDK_INT >= 33) causes.filterIsInstance<KeyStoreException>().joinToString(";") {
            "code=${it.numericErrorCode},system=${it.isSystemError},transient=${it.isTransientFailure}," +
                "authRequired=${it.requiresUserAuthentication()},retry=" +
                if (it.isTransientFailure) it.retryPolicy.toString() else "not-transient"
        } else "unsupported"
        return "phase=$safePhase level=$level classes=${causes.joinToString("->") { it.javaClass.name }} numeric=$numeric"
    }
}
