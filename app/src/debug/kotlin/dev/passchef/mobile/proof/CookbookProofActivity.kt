package dev.passchef.mobile.proof

import android.content.Context
import android.content.ContextWrapper
import dev.passchef.mobile.MainActivity
import java.io.File
import java.util.UUID

/** Same real controller/authentication, unique owned storage; never the owner's Cookbook. */
class CookbookProofActivity : MainActivity() {
    val proofDirectory: File by lazy { File(super.getNoBackupFilesDir(),"cookbook-ui-proof-${UUID.randomUUID()}").also { it.mkdirs() } }
    override fun storageContext(): Context = object : ContextWrapper(this) {
        override fun getNoBackupFilesDir(): File = proofDirectory
    }
}
