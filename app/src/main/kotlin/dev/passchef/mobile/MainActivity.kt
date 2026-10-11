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
import dev.passchef.mobile.vault.BiometricPromptFailure

/** No session state is restored across Activity/process recreation. */
open class MainActivity : FragmentActivity() {
    private lateinit var controller: VaultController
    private lateinit var cookbook: dev.passchef.mobile.cookbook.CookbookController
    private lateinit var clipboard: dev.passchef.mobile.cookbook.OwnedClipboard
    protected open fun storageContext(): android.content.Context = this
    private var prompt: BiometricPrompt? = null
    private var promptEpoch = 0L
    private var clearInputs: () -> Unit = {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        controller = VaultController(storageContext(), ::authenticate) { promptEpoch++; prompt?.cancelAuthentication(); prompt = null }
        clipboard = dev.passchef.mobile.cookbook.OwnedClipboard.obtain(this)
        cookbook = dev.passchef.mobile.cookbook.CookbookController(controller, clipboard,
            dev.passchef.mobile.cookbook.AndroidSheetsAuthorization(this), java.io.File(storageContext().noBackupFilesDir,"menus"))
        controller.onLocked = cookbook::lock
        setContent { dev.passchef.mobile.cookbook.MainCookbookScreen(controller,cookbook) { clearInputs = it } }
    }
    private fun authenticate(cipher: Cipher, success: (Cipher) -> Unit, failure: (BiometricPromptFailure) -> Unit) {
        val token = ++promptEpoch
        var failedAttempts = 0
        prompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this), object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                if (token != promptEpoch) return
                prompt = null
                val authorized = result.cryptoObject?.cipher
                if (authorized === cipher) success(authorized) else failure(BiometricPromptFailure.OTHER)
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                if (token == promptEpoch) {
                    prompt = null
                    WrapFailureProbe.biometricError(errorCode)
                    failure(BiometricPromptFailure.fromAndroidError(errorCode))
                }
            }
            override fun onAuthenticationFailed() {
                if (token == promptEpoch && ++failedAttempts >= 3) { prompt?.cancelAuthentication() }
            }
        })
        val info = BiometricPrompt.PromptInfo.Builder().setTitle("Open your Cookbook")
            .setSubtitle("Authorize this device key operation")
            .setAllowedAuthenticators(BIOMETRIC_STRONG).setNegativeButtonText("Use master password").build()
        try { prompt!!.authenticate(info, BiometricPrompt.CryptoObject(cipher)) }
        catch (_: Exception) { prompt = null; failure(BiometricPromptFailure.OTHER) }
    }
    override fun onStart() { super.onStart(); controller.start(); clipboard.setForeground(true) }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && ::clipboard.isInitialized) clipboard.reconcile()
    }
    override fun onStop() { clearInputs(); controller.lock(); clipboard.setForeground(false); super.onStop() }
    override fun onDestroy() { cookbook.destroy(); controller.destroy(); super.onDestroy() }
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (::controller.isInitialized && event.action == MotionEvent.ACTION_DOWN) controller.touch()
        return super.dispatchTouchEvent(event)
    }
}
