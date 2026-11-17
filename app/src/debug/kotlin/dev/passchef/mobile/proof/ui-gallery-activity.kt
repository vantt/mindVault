package dev.passchef.mobile.proof

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.cookbook.LocalCookbookVietnamese
import dev.passchef.mobile.cookbook.MenuEntry
import dev.passchef.mobile.cookbook.MenuOrigin
import dev.passchef.mobile.gate.FirstRunExplainer
import dev.passchef.mobile.home.PasswordListRow
import dev.passchef.mobile.home.sheetColor
import dev.passchef.mobile.settings.HowItWorksScreen
import dev.passchef.mobile.ui.*

/**
 * Debug-only visual QA gallery (phase 06). Fake, labelled data; no vault, no Google, no secrets, so it
 * deliberately has no FLAG_SECURE and can be screenshotted. Release builds do not contain it.
 * adb shell am start -n dev.passchef.mobile.demo/dev.passchef.mobile.proof.UiGalleryActivity --es theme light --es page explainer
 */
class UiGalleryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val theme = if (intent.getStringExtra("theme") == "light") ThemeChoice.LIGHT else ThemeChoice.DARK
        val page = intent.getStringExtra("page") ?: "explainer"
        val vi = intent.getStringExtra("lang") == "vi"
        setContent {
            CompositionLocalProvider(LocalCookbookVietnamese provides vi) {
                PassChefTheme(theme) {
                    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                        Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {
                            PassChefHeader(if (vi) "EN" else "VI") {}
                            when (page) {
                                "how" -> HowItWorksScreen()
                                "components" -> Components()
                                else -> FirstRunExplainer {}
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable private fun Components() {
    val origin = MenuOrigin("local_example0001", 0, "Example sheet")
    Column(Modifier.verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Components · example data", style = PassChefDisplay)
        PasswordListRow(MenuEntry(origin, 2, "rand0m\$1", "Facebook", "me@example.com", "https://facebook.com"), sheetColor(0), null, false, {}, {})
        PasswordListRow(MenuEntry(origin, 3, "x9Lk2m#2", "Gmail", "me@example.com"), sheetColor(1), "Work sheet", false, {}, {})
        CookbookPrimaryAction("Get password", {}, Modifier.fillMaxWidth())
        CookbookSecondaryAction("Show 10 s", {}, Modifier.fillMaxWidth())
        CookbookGhostAction("Choose this sheet again", {})
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { CookbookChoice("All", true, {}); CookbookChoice("Recent", false, {}) }
        for (tone in CookbookTone.entries) CookbookStatus("Status · ${tone.name}", tone = tone)
    }
}
