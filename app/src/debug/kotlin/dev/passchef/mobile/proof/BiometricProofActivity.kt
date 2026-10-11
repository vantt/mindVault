package dev.passchef.mobile.proof

import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicReference
import javax.crypto.Cipher

/** Test harness always waits for the owner's real biometric; no injected success. */
class BiometricProofActivity : FragmentActivity() {
    private var prompt: BiometricPrompt? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(TextView(this).apply { text = "PassChef physical proof: authorize two biometric prompts. Synthetic Cookbook only." })
    }
    fun authorize(cipher: Cipher, phase: String, output: AtomicReference<Cipher?>, done: CountDownLatch) {
        prompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this), object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                if (result.cryptoObject?.cipher === cipher) output.set(cipher)
                done.countDown()
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) { done.countDown() }
        })
        val info = BiometricPrompt.PromptInfo.Builder().setTitle("PassChef physical proof: $phase")
            .setAllowedAuthenticators(BIOMETRIC_STRONG).setNegativeButtonText("Cancel proof").build()
        prompt!!.authenticate(info, BiometricPrompt.CryptoObject(cipher))
    }
    override fun onDestroy() { prompt?.cancelAuthentication(); super.onDestroy() }
}
