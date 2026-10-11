package dev.passchef.mobile

import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import javax.crypto.Cipher

/** No session state is restored across Activity/process recreation. */
class MainActivity : FragmentActivity() {
    private lateinit var controller: VaultController
    private var prompt: BiometricPrompt? = null
    private var promptEpoch = 0L
    private var clearInputs: () -> Unit = {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        controller = VaultController(this, ::authenticate) { promptEpoch++; prompt?.cancelAuthentication(); prompt = null }
        setContent { VaultScreen(controller) { clearInputs = it } }
    }
    private fun authenticate(cipher: Cipher, success: (Cipher) -> Unit, failure: () -> Unit) {
        val token = ++promptEpoch
        var failedAttempts = 0
        prompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this), object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                if (token != promptEpoch) return
                prompt = null
                val authorized = result.cryptoObject?.cipher
                if (authorized === cipher) success(authorized) else failure()
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                if (token == promptEpoch) { prompt = null; failure() }
            }
            override fun onAuthenticationFailed() {
                if (token == promptEpoch && ++failedAttempts >= 3) { prompt?.cancelAuthentication() }
            }
        })
        val info = BiometricPrompt.PromptInfo.Builder().setTitle("Open your Cookbook")
            .setSubtitle("Authorize this device key operation")
            .setAllowedAuthenticators(BIOMETRIC_STRONG).setNegativeButtonText("Use master password").build()
        try { prompt!!.authenticate(info, BiometricPrompt.CryptoObject(cipher)) }
        catch (_: Exception) { prompt = null; failure() }
    }
    override fun onStart() { super.onStart(); controller.start() }
    override fun onStop() { clearInputs(); controller.lock(); super.onStop() }
    override fun onDestroy() { controller.destroy(); super.onDestroy() }
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (::controller.isInitialized && event.action == MotionEvent.ACTION_DOWN) controller.touch()
        return super.dispatchTouchEvent(event)
    }
}
