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

/**
 * No secret session state is restored across Activity/process recreation. Only the non-secret
 * place (tab, route ids, wizard progress) survives a lock in the same process (D5).
 */
open class MainActivity : FragmentActivity() {
    private lateinit var controller: VaultController
    private lateinit var cookbook: dev.passchef.mobile.cookbook.CookbookController
    private lateinit var clipboard: dev.passchef.mobile.cookbook.OwnedClipboard
    protected open fun storageContext(): android.content.Context = this
    private var prompt: BiometricPrompt? = null
    private var promptEpoch = 0L
    private var clearInputs: () -> Unit = {}
    private lateinit var preferences: dev.passchef.mobile.settings.AppPreferences
    private val main = android.os.Handler(android.os.Looper.getMainLooper())
    private var stopped = false
    /** elapsedRealtime end of the D2 grace; checked again on return in case the handler stalled in sleep. */
    private var graceDeadline = 0L
    /** D2 grace ends: still in the background behind our own Google screen → lock now. */
    private val graceExpiry = Runnable { if (stopped) { clearInputs(); controller.lock() } }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        if (savedInstanceState == null) dev.passchef.mobile.navigation.AppRouteStateHolder.fresh()
        preferences = dev.passchef.mobile.settings.AppPreferences(this)
        dev.passchef.mobile.navigation.AppRouteStateHolder.state.autoBiometricEvent = -1L // new controller, new lock events
        if (savedInstanceState == null) acceptShared(intent) // never replay the share on recreate
        controller = VaultController(storageContext(), ::authenticate) { promptEpoch++; prompt?.cancelAuthentication(); prompt = null }
        clipboard = dev.passchef.mobile.cookbook.OwnedClipboard.obtain(this)
        cookbook = dev.passchef.mobile.cookbook.CookbookController(controller, clipboard,
            dev.passchef.mobile.cookbook.AndroidSheetsAuthorization(this), java.io.File(storageContext().noBackupFilesDir,"menus"),
            setFor = { origin -> origin?.let { preferences.sheetSets[it.key] } })
        controller.onLocked = cookbook::lock
        setContent { dev.passchef.mobile.cookbook.MainCookbookScreen(controller, cookbook, preferences,
            dev.passchef.mobile.navigation.AppRouteStateHolder.state) { clearInputs = it } }
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
        val vi = ::preferences.isInitialized && preferences.vietnamese
        val info = BiometricPrompt.PromptInfo.Builder().setTitle(if (vi) "Mở két riêng" else "Open your private vault")
            .setSubtitle(if (vi) "Xác nhận bằng vân tay" else "Confirm with your fingerprint")
            .setAllowedAuthenticators(BIOMETRIC_STRONG).setNegativeButtonText(if (vi) "Dùng mật khẩu chính" else "Use master password").build()
        try { prompt!!.authenticate(info, BiometricPrompt.CryptoObject(cipher)) }
        catch (_: Exception) { prompt = null; failure(BiometricPromptFailure.OTHER) }
    }
    override fun onStart() {
        super.onStart(); stopped = false; main.removeCallbacks(graceExpiry)
        if (graceDeadline > 0 && android.os.SystemClock.elapsedRealtime() > graceDeadline) { clearInputs(); controller.lock() }
        graceDeadline = 0L
        controller.start(); clipboard.setForeground(window.decorView.hasWindowFocus())
    }
    override fun onNewIntent(intent: android.content.Intent) { super.onNewIntent(intent); setIntent(intent); acceptShared(intent) }
    /** D11 Share-in: keep only a parsed Sheet id (non-secret); the user confirms it in the Picker. */
    private fun acceptShared(intent: android.content.Intent?) {
        if (intent?.action != android.content.Intent.ACTION_SEND) return
        val text = runCatching { intent.getStringExtra(android.content.Intent.EXTRA_TEXT) }.getOrNull()?.take(2048) ?: return
        val link = Regex("https://docs\\.google\\.com/spreadsheets/d/[A-Za-z0-9_-]{10,200}").find(text)?.value ?: text.trim()
        dev.passchef.mobile.cookbook.menuIdFromInput(link)?.let { id ->
            val routes = dev.passchef.mobile.navigation.AppRouteStateHolder.state
            routes.sharedSheetId = id
            routes.open(dev.passchef.mobile.navigation.AppTab.SETTINGS, dev.passchef.mobile.navigation.AppRoute.Connect)
        }
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (::clipboard.isInitialized) clipboard.setForeground(hasFocus)
    }
    /**
     * Leaving the app locks it. D2 exception: our own Google Picker/consent screen is open (ticket-bound,
     * at most 120s). Then the password on screen and typed inputs are wiped, timers keep running, and
     * [graceExpiry] locks if we are still in the background when the window ends.
     */
    override fun onStop() {
        stopped = true; clearInputs()
        if (cookbook.authorization.ownScreenPending()) {
            cookbook.edit()
            val remaining = cookbook.authorization.graceRemaining()
            graceDeadline = android.os.SystemClock.elapsedRealtime() + remaining
            main.postDelayed(graceExpiry, remaining)
        } else controller.lock()
        clipboard.setForeground(false); super.onStop()
    }
    override fun onDestroy() { main.removeCallbacks(graceExpiry); cookbook.destroy(); controller.destroy(); super.onDestroy() }
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (::controller.isInitialized && event.action == MotionEvent.ACTION_DOWN) controller.touch()
        return super.dispatchTouchEvent(event)
    }
}
