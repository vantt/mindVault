package dev.passchef.mobile.settings

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.passchef.mobile.ui.ThemeChoice
import java.util.Locale

/**
 * Non-sensitive UI preferences (D10 language, D13 theme, explainer seen). App has allowBackup=false,
 * so these never leave the phone. Never store secrets, tokens or sheet contents here.
 */
class AppPreferences(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences("passchef-ui", Context.MODE_PRIVATE)

    var vietnamese by mutableStateOf(prefs.getString(KEY_LANGUAGE, null)?.let { it == "vi" }
        ?: (Locale.getDefault().language == "vi")); private set
    var theme by mutableStateOf(runCatching { ThemeChoice.valueOf(prefs.getString(KEY_THEME, null) ?: "SYSTEM") }
        .getOrDefault(ThemeChoice.SYSTEM)); private set
    var explainerSeen by mutableStateOf(prefs.getBoolean(KEY_EXPLAINER, false)); private set
    /** Sheet origin key → secret-part set id (D17). Ids only. */
    var sheetSets by mutableStateOf(readSheetSets()); private set

    fun chooseLanguage(vi: Boolean) { vietnamese = vi; prefs.edit().putString(KEY_LANGUAGE, if (vi) "vi" else "en").apply() }
    fun chooseTheme(choice: ThemeChoice) { theme = choice; prefs.edit().putString(KEY_THEME, choice.name).apply() }
    fun markExplainerSeen() { explainerSeen = true; prefs.edit().putBoolean(KEY_EXPLAINER, true).apply() }

    fun bindSheet(originKey: String, setId: String?) {
        sheetSets = if (setId == null) sheetSets - originKey else sheetSets + (originKey to setId)
        prefs.edit().putString(KEY_SHEET_SETS, sheetSets.entries.joinToString("\n") { "${it.key}\t${it.value}" }).apply()
    }
    private fun readSheetSets(): Map<String, String> = prefs.getString(KEY_SHEET_SETS, "").orEmpty()
        .lineSequence().mapNotNull { line -> line.split('\t').takeIf { it.size == 2 }?.let { it[0] to it[1] } }.toMap()

    private companion object {
        const val KEY_LANGUAGE = "language"
        const val KEY_THEME = "theme"
        const val KEY_EXPLAINER = "explainer-seen"
        const val KEY_SHEET_SETS = "sheet-sets"
    }
}
