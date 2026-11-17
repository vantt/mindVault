package dev.passchef.mobile.proof

import android.os.Bundle
import android.os.SystemClock
import android.text.InputType
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import com.passchef.sheets.SheetsApiException
import com.passchef.sheets.SheetsClient
import com.passchef.sheets.SheetsForbiddenException
import com.passchef.sheets.SheetsUnauthorizedException
import io.ktor.client.engine.android.Android
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Phase-01 spike (debug only): can PassChef pick a spreadsheet with the Google Picker
 * (Play services AuthorizationClient + `drive.file`) and then read/append it through the
 * existing Sheets client?
 *
 * Safety: the access token lives only in this Activity's memory, is never logged, written
 * to disk or shown. The on-screen log shows HTTP outcomes, counts and file IDs only — never
 * cell contents. Append writes one synthetic marker row; use a throwaway test spreadsheet.
 *
 * Launch: adb shell am start -n dev.passchef.mobile.demo/dev.passchef.mobile.proof.DrivePickerSpikeActivity
 */
class DrivePickerSpikeActivity : ComponentActivity() {
    private val client by lazy { Identity.getAuthorizationClient(this) }
    private var token: String? = null
    private var pickedIds: List<String> = emptyList()
    private var startedAt = 0L
    private lateinit var log: TextView
    private lateinit var fileIdFilter: EditText
    private lateinit var otherId: EditText

    private val launcher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { response ->
        val result = runCatching { client.getAuthorizationResultFromIntent(response.data) }
        result.onSuccess { accept(it, "resolution") }
            .onFailure { line("RESULT FAIL resultCode=${response.resultCode} ${it.javaClass.simpleName}: ${it.message}") }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val column = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(32, 48, 32, 32) }
        fun button(label: String, action: () -> Unit) = column.addView(Button(this).apply { text = label; setOnClickListener { action() } })
        fun field(hint: String) = EditText(this).apply {
            this.hint = hint; inputType = InputType.TYPE_CLASS_TEXT; isSingleLine = true
        }.also { column.addView(it) }

        column.addView(TextView(this).apply { text = "Drive Picker spike · drive.file · debug only"; textSize = 18f })
        fileIdFilter = field("Optional: restrict Picker to file ID (share-in test)")
        button("1. Pick spreadsheet (Picker)") { authorize(withPicker = true) }
        button("2. Silent re-authorize (no Picker)") { authorize(withPicker = false) }
        button("3. Read picked: metadata + first rows") { readPicked() }
        button("4. Append marker row to picked (TEST SHEET ONLY)") { appendPicked() }
        otherId = field("Unpicked spreadsheet ID (expect 403/404)")
        button("5. Read unpicked ID") { readOther() }
        button("Clear log") { log.text = "" }
        log = TextView(this).apply { setTextIsSelectable(true); textSize = 12f; typeface = android.graphics.Typeface.MONOSPACE }
        column.addView(log)
        setContentView(ScrollView(this).apply { addView(column, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) })
        line("ready · package=$packageName")
    }

    private fun authorize(withPicker: Boolean) {
        val builder = AuthorizationRequest.builder()
            .setRequestedScopes(listOf(Scope(DRIVE_FILE_SCOPE)))
            .setOptOutIncludingGrantedScopes(true)
        if (withPicker) {
            builder.setPrompt(AuthorizationRequest.Prompt.CONSENT)
                .addResourceParameter(AuthorizationRequest.ResourceParameter.PICKER_OAUTH_TRIGGER, "true")
                .addResourceParameter(AuthorizationRequest.ResourceParameter.PICKER_MIMETYPES, SPREADSHEET_MIME)
            fileIdFilter.text.toString().trim().takeIf { it.isNotEmpty() }?.let {
                builder.addResourceParameter(AuthorizationRequest.ResourceParameter.PICKER_FILE_IDS, it)
                line("picker filtered to file ID")
            }
        }
        startedAt = SystemClock.elapsedRealtime()
        line(if (withPicker) "authorize(picker)…" else "authorize(silent)…")
        runCatching {
            client.authorize(builder.build())
                .addOnSuccessListener { result ->
                    if (result.hasResolution()) {
                        line("needs resolution → launching")
                        runCatching { launcher.launch(IntentSenderRequest.Builder(result.pendingIntent!!.intentSender).build()) }
                            .onFailure { line("LAUNCH FAIL ${it.javaClass.simpleName}: ${it.message}") }
                    } else accept(result, "no-resolution")
                }
                .addOnFailureListener { line("AUTHORIZE FAIL ${it.javaClass.simpleName}: ${it.message}") }
        }.onFailure { line("AUTHORIZE THREW ${it.javaClass.simpleName}: ${it.message}") }
    }

    private fun accept(result: AuthorizationResult, path: String) {
        val elapsed = SystemClock.elapsedRealtime() - startedAt
        token = result.accessToken?.takeIf { it.isNotBlank() }
        val params = result.tokenResponseParams
        val ids = params?.getString(PICKED_FILE_IDS)
        if (ids != null) pickedIds = ids.split(',').map { it.trim() }.filter { it.isNotEmpty() }
        line("OK via $path in ${elapsed}ms · token=${if (token != null) "present" else "MISSING"}")
        line("  grantedScopes=${result.grantedScopes}")
        line("  tokenResponseParams keys=${params?.keySet()?.sorted()}")
        line("  picked_file_ids=${ids ?: "(absent)"} · kept=${pickedIds.size}")
    }

    private fun readPicked() {
        val id = pickedIds.firstOrNull() ?: return line("no picked ID yet")
        read(id, "picked")
    }

    private fun readOther() {
        val id = otherId.text.toString().trim().ifEmpty { return line("enter an unpicked ID") }
        read(id, "unpicked")
    }

    private fun read(id: String, label: String) {
        val t = token ?: return line("no token; authorize first")
        lifecycleScope.launch {
            val sheets = SheetsClient(Android.create(), t)
            try {
                val meta = sheets.getSpreadsheet(id)
                line("$label metadata 200 · tabs=${meta.sheets.size} · titleLength=${meta.properties.title.length}")
                val first = meta.sheets.firstOrNull()?.properties ?: return@launch line("$label has no tabs")
                val values = sheets.getValues(id, "'${first.title.replace("'", "''")}'!A1:C5")
                line("$label values.get 200 · rows=${values.values.size} · grid=${first.gridProperties.rowCount}x${first.gridProperties.columnCount}")
            } catch (e: Exception) {
                line("$label READ ${outcome(e)}")
            } finally { sheets.close() }
        }
    }

    private fun appendPicked() {
        val id = pickedIds.firstOrNull() ?: return line("no picked ID yet")
        val t = token ?: return line("no token; authorize first")
        lifecycleScope.launch {
            val sheets = SheetsClient(Android.create(), t)
            try {
                val tab = sheets.getSpreadsheet(id).sheets.firstOrNull()?.properties?.title
                    ?: return@launch line("picked has no tabs")
                val stamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(Date())
                val response = sheets.appendValues(id, "'${tab.replace("'", "''")}'!A1", listOf(listOf("passchef-picker-spike", stamp)))
                line("picked append 200 · updatedRange present=${response.updates?.updatedRange != null}")
            } catch (e: Exception) {
                line("picked APPEND ${outcome(e)}")
            } finally { sheets.close() }
        }
    }

    /** Status only; exception messages from the client never contain the token. */
    private fun outcome(e: Exception): String = when (e) {
        is SheetsUnauthorizedException -> "401 unauthorized"
        is SheetsForbiddenException -> "403 forbidden"
        is SheetsApiException -> "HTTP ${e.statusCode}"
        else -> "FAIL ${e.javaClass.simpleName}: ${e.message?.take(120)}"
    }

    private fun line(text: String) {
        val time = SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())
        runOnUiThread { log.append("$time $text\n") }
    }

    override fun onDestroy() {
        token = null
        super.onDestroy()
    }

    private companion object {
        const val DRIVE_FILE_SCOPE = "https://www.googleapis.com/auth/drive.file"
        const val SPREADSHEET_MIME = "application/vnd.google-apps.spreadsheet"
        const val PICKED_FILE_IDS = "picked_file_ids"
    }
}
