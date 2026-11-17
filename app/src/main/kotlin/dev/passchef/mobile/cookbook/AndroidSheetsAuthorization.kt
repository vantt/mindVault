package dev.passchef.mobile.cookbook

import android.os.SystemClock
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.identity.ClearTokenRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Scope

/** Result of one Google authorization. Token is RAM-only and never logged. */
data class GoogleGrant(val token: String, val pickedIds: List<String>)
enum class GrantFailure { CANCELLED, FAILED }

/**
 * D1: `drive.file` only, via Play services AuthorizationClient + Google Picker (phase-01 spike).
 * [pick] opens Google's Picker (optionally filtered to one file id: Paste / Share-in / re-pick).
 * [silent] re-authorizes an already granted file with no UI (refresh). A result that returns after
 * lock or after another request started is discarded by the ownership ticket.
 */
class AndroidSheetsAuthorization(activity: FragmentActivity) {
    private val client = Identity.getAuthorizationClient(activity)
    private val ownership = SourceOwnership()
    private val rejectedTokens = RejectedTokenCacheGate { token, complete ->
        client.clearToken(ClearTokenRequest.builder().setToken(token).build())
            .addOnCompleteListener { complete(it.isSuccessful) }
    }
    private var pending: Pair<SourceTicket, (GoogleGrant?, GrantFailure?) -> Unit>? = null
    /** elapsedRealtime when our own Google screen was launched; drives the D2 consent grace. */
    private var pendingSince = 0L
    private var current: () -> Boolean = { false }
    private val launcher = activity.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { response ->
        val request = pending; pending = null; pendingSince = 0L
        if (request != null && ownership.accepts(request.first, current())) {
            val outcome = runCatching { client.getAuthorizationResultFromIntent(response.data) }
            val failure = outcome.exceptionOrNull()
            val cancelled = response.resultCode == android.app.Activity.RESULT_CANCELED ||
                (failure as? ApiException)?.statusCode == CommonStatusCodes.CANCELED
            val grant = outcome.getOrNull()?.let(::accepted)
            request.second(grant, if (grant != null) null else if (cancelled) GrantFailure.CANCELLED else GrantFailure.FAILED)
        }
    }
    private fun accepted(result: AuthorizationResult): GoogleGrant? {
        val token = result.accessToken?.takeIf { AuthorizationGrantPolicy.usable(it, result.grantedScopes) } ?: return null
        val picked = result.tokenResponseParams?.getString(PICKED_FILE_IDS).orEmpty()
            .split(',').map { it.trim() }.filter { it.matches(Regex("[A-Za-z0-9_-]{10,200}")) }
        return GoogleGrant(token, picked)
    }
    /** Lock/new request: later results are discarded (ticket) and the D2 grace no longer applies. */
    fun invalidate() { ownership.invalidate(); current = { false }; pending = null; pendingSince = 0L }
    fun clearRejectedToken(token: String) { rejectedTokens.reject(token) }
    /** D2: true while our own Picker/consent screen is open, at most [windowMs] after launch. */
    fun ownScreenPending(now: Long = SystemClock.elapsedRealtime(), windowMs: Long = GRACE_MS): Boolean =
        pending != null && pendingSince > 0 && now - pendingSince in 0..windowMs
    fun graceRemaining(now: Long = SystemClock.elapsedRealtime()): Long = (GRACE_MS - (now - pendingSince)).coerceAtLeast(0)

    fun pick(ready: () -> Boolean, fileId: String?, complete: (GoogleGrant?, GrantFailure?) -> Unit) =
        authorize(ready, picker = true, fileId = fileId, complete = complete)
    fun silent(ready: () -> Boolean, complete: (GoogleGrant?, GrantFailure?) -> Unit) =
        authorize(ready, picker = false, fileId = null, complete = complete)

    private fun request(picker: Boolean, fileId: String?): AuthorizationRequest {
        val builder = AuthorizationRequest.builder()
            .setRequestedScopes(listOf(Scope(AuthorizationGrantPolicy.DRIVE_FILE_SCOPE)))
            .setOptOutIncludingGrantedScopes(true)
        if (picker) {
            builder.setPrompt(AuthorizationRequest.Prompt.CONSENT)
                .addResourceParameter(AuthorizationRequest.ResourceParameter.PICKER_OAUTH_TRIGGER, "true")
                .addResourceParameter(AuthorizationRequest.ResourceParameter.PICKER_MIMETYPES, SPREADSHEET_MIME)
            fileId?.let { builder.addResourceParameter(AuthorizationRequest.ResourceParameter.PICKER_FILE_IDS, it) }
        }
        return builder.build()
    }

    private fun authorize(ready: () -> Boolean, picker: Boolean, fileId: String?, complete: (GoogleGrant?, GrantFailure?) -> Unit) {
        if (pending != null) { complete(null, GrantFailure.FAILED); return }
        invalidate(); current = ready
        val token = ownership.begin("authorization")
        val request = runCatching { request(picker, fileId) }.getOrElse { complete(null, GrantFailure.FAILED); return }
        rejectedTokens.beforeAuthorization { cleared ->
            if (!ownership.accepts(token, ready())) return@beforeAuthorization
            if (!cleared) { complete(null, GrantFailure.FAILED); return@beforeAuthorization }
            runCatching { client.authorize(request).addOnSuccessListener { result ->
                if (!ownership.accepts(token, ready())) return@addOnSuccessListener
                if (result.hasResolution()) {
                    pending = token to complete; pendingSince = SystemClock.elapsedRealtime()
                    runCatching { launcher.launch(IntentSenderRequest.Builder(result.pendingIntent!!.intentSender).build()) }
                        .onFailure { pending = null; pendingSince = 0L; complete(null, GrantFailure.FAILED) }
                } else {
                    val grant = accepted(result)
                    complete(grant, if (grant == null) GrantFailure.FAILED else null)
                }
            }.addOnFailureListener { if (ownership.accepts(token, ready())) complete(null, GrantFailure.FAILED) } }
                .onFailure { if (ownership.accepts(token, ready())) complete(null, GrantFailure.FAILED) }
        }
    }

    private companion object {
        const val SPREADSHEET_MIME = "application/vnd.google-apps.spreadsheet"
        const val PICKED_FILE_IDS = "picked_file_ids"
        const val GRACE_MS = 120_000L
    }
}
