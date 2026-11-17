package dev.passchef.mobile.proof

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.WindowManager
import dev.passchef.mobile.MainActivity
import java.io.File
import java.util.UUID

/** Same real controller/authentication, unique owned storage; never the owner's Cookbook. */
class CookbookProofActivity : MainActivity() {
    var retainProofStorage = false
    private var proofId = UUID.randomUUID().toString()
    private var savedTaskJournal: SavedTaskProofJournal? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        savedInstanceState?.getString("passchefOwnedProofId")?.let { saved ->
            val parsed = runCatching { UUID.fromString(saved).toString() }.getOrNull()
            require(parsed == saved) { "Invalid owned proof identity" }
            proofId = saved; retainProofStorage = true
        }
        // Legacy proofs start at the setup/unlock gate; the first-run explainer has its own checks (extra opts in).
        if (!intent.getBooleanExtra("passchefShowExplainer", false))
            getSharedPreferences("passchef-ui", MODE_PRIVATE).edit().putBoolean("explainer-seen", true).commit()
        super.onCreate(savedInstanceState)
        // Legacy proofs drive unlock explicitly; the D6 auto fingerprint prompt would cover their UI.
        dev.passchef.mobile.navigation.AppRouteStateHolder.state.autoBiometricEnabled = intent.getBooleanExtra("passchefAutoBiometric", false)
        if (intent.getBooleanExtra("passchefSavedTaskProof", false)) {
            retainProofStorage = true
            savedTaskJournal = SavedTaskProofJournal(this).also { it.created(savedInstanceState) }
        }
        // Only the isolated runtime proof stays awake; production screen/lock policy is unchanged.
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        if (retainProofStorage) outState.putString("passchefOwnedProofId", proofId)
        super.onSaveInstanceState(outState)
        savedTaskJournal?.saved(outState)
    }
    override fun onStop() {
        super.onStop()
        savedTaskJournal?.stopped()
    }
    val proofDirectory: File by lazy { File(super.getNoBackupFilesDir(),"cookbook-ui-proof-$proofId").also { it.mkdirs() } }
    override fun storageContext(): Context = object : ContextWrapper(this) {
        override fun getNoBackupFilesDir(): File = proofDirectory
    }
}
