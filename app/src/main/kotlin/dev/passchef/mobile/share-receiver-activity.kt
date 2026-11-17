package dev.passchef.mobile

import android.app.Activity
import android.content.Intent
import android.os.Bundle

/**
 * D11 Share-in trampoline: "Share › PassChef" from the Sheets app. Forwards only the shared text
 * (max 2048 chars) to the single PassChef task, then closes. No parsing of secrets; the Sheet id is
 * confirmed by the user in Google's Picker after unlock.
 */
class ShareReceiverActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val text = runCatching { intent?.getCharSequenceExtra(Intent.EXTRA_TEXT)?.toString() }.getOrNull()?.take(2048)
        if (!text.isNullOrBlank()) runCatching {
            startActivity(Intent(this, MainActivity::class.java).setAction(Intent.ACTION_SEND).setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, text)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }
        finish()
    }
}
