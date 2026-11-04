package dev.passchef.mobile.proof

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.view.WindowManager
import dev.passchef.mobile.MainActivity
import java.io.File
import java.util.UUID

/** Intent selects only a canonical owned storage locator; no authentication state is restored. */
class ColdStartProofActivity : MainActivity() {
    lateinit var proofDirectory: File
        private set
    lateinit var proofId: String
        private set
    var googleResultProof: GoogleResultProofWitness? = null
        private set
    override fun onCreate(savedInstanceState: Bundle?) {
        require(savedInstanceState == null) { "Fresh proof launch required" }
        val supplied = requireNotNull(intent.getStringExtra("passchefColdFixtureId"))
        require(UUID.fromString(supplied).toString() == supplied) { "Canonical proof locator required" }
        proofId = supplied
        val base = super.getNoBackupFilesDir().canonicalFile
        proofDirectory = File(base, "cookbook-cold-proof-$proofId").canonicalFile
        require(proofDirectory.parentFile == base && proofDirectory.name == "cookbook-cold-proof-$proofId")
        when (intent.getStringExtra("passchefColdStage")) {
            "warm" -> require(!proofDirectory.exists() && proofDirectory.mkdir()) { "New proof location required" }
            "cold" -> require(proofDirectory.isDirectory && File(proofDirectory, "cold-baseline.json").isFile) { "Owned baseline required" }
            else -> error("Explicit proof stage required")
        }
        super.onCreate(savedInstanceState)
        if (intent.getBooleanExtra("passchefGoogleResultProof", false)) googleResultProof = GoogleResultProofWitness(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
    @Suppress("DEPRECATION")
    override fun startIntentSenderForResult(sender: IntentSender, requestCode: Int, fillInIntent: Intent?,
        flagsMask: Int, flagsValues: Int, extraFlags: Int, options: Bundle?) {
        runCatching { googleResultProof?.launched(requestCode) }
        super.startIntentSenderForResult(sender, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options)
    }
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        runCatching { googleResultProof?.beforeResult(requestCode, resultCode) }
        var returnedNormally = false
        try { super.onActivityResult(requestCode, resultCode, data); returnedNormally = true }
        finally { runCatching { googleResultProof?.afterResult(requestCode, returnedNormally) } }
    }
    override fun storageContext(): Context = object : ContextWrapper(this) {
        override fun getNoBackupFilesDir(): File = proofDirectory
    }
}
