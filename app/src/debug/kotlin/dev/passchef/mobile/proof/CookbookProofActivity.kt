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
    override fun onCreate(savedInstanceState: Bundle?) {
        savedInstanceState?.getString("passchefOwnedProofId")?.let { saved ->
            val parsed = runCatching { UUID.fromString(saved).toString() }.getOrNull()
            require(parsed == saved) { "Invalid owned proof identity" }
            proofId = saved; retainProofStorage = true
        }
        super.onCreate(savedInstanceState)
        // Only the isolated runtime proof stays awake; production screen/lock policy is unchanged.
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        if (retainProofStorage) outState.putString("passchefOwnedProofId", proofId)
        super.onSaveInstanceState(outState)
    }
    val proofDirectory: File by lazy { File(super.getNoBackupFilesDir(),"cookbook-ui-proof-$proofId").also { it.mkdirs() } }
    override fun storageContext(): Context = object : ContextWrapper(this) {
        override fun getNoBackupFilesDir(): File = proofDirectory
    }
}
