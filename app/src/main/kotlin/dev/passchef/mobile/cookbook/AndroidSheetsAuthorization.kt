package dev.passchef.mobile.cookbook

import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.IntentSenderRequest
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope

/** External consent can background/lock us; such a result is discarded. Connect again after unlock. */
class AndroidSheetsAuthorization(activity: FragmentActivity) {
    private val client = Identity.getAuthorizationClient(activity)
    private val ownership = SourceOwnership()
    private var pending: Pair<SourceTicket, (String?) -> Unit>? = null
    private var current: () -> Boolean = { false }
    private val launcher = activity.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { response ->
        val request = pending; pending = null
        if (request != null && ownership.accepts(request.first, current())) {
            val token = runCatching { accepted(client.getAuthorizationResultFromIntent(response.data)) }.getOrNull()
            request.second(token?.takeIf { it.isNotBlank() })
        }
    }
    private fun accepted(result: com.google.android.gms.auth.api.identity.AuthorizationResult): String? =
        result.accessToken.takeIf { AuthorizationGrantPolicy.usable(it,result.grantedScopes) }
    fun invalidate() { ownership.invalidate(); current = { false } }
    fun connect(ready: () -> Boolean, complete: (String?) -> Unit) {
        if (pending != null) { complete(null); return }
        invalidate(); current = ready
        val token = ownership.begin("authorization")
        val request = AuthorizationRequest.builder().setRequestedScopes(listOf(Scope(AuthorizationGrantPolicy.SHEETS_SCOPE))).build()
        runCatching { client.authorize(request).addOnSuccessListener { result ->
            if (!ownership.accepts(token, ready())) return@addOnSuccessListener
            if (result.hasResolution()) {
                pending = token to complete
                runCatching { launcher.launch(IntentSenderRequest.Builder(result.pendingIntent!!.intentSender).build()) }
                    .onFailure { pending = null; complete(null) }
            } else complete(accepted(result))
        }.addOnFailureListener { if (ownership.accepts(token, ready())) complete(null) } }
            .onFailure { if(ownership.accepts(token,ready())) complete(null) }
    }
}
