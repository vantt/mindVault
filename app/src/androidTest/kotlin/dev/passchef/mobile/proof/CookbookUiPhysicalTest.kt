package dev.passchef.mobile.proof

import android.content.ClipboardManager
import android.content.Context
import android.view.WindowManager
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.KeystoreCrypto
import dev.passchef.mobile.recipe.RecipeParser
import dev.passchef.mobile.recipe.TagCodec
import dev.passchef.mobile.vault.AtomicRecordStore
import dev.passchef.mobile.vault.VaultRepository
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/** Owner-authorized runtime only: real local setup/biometric operation, no auth bypass.
 * Compiling this test APK alone proves none of these physical/UI outcomes. */
@RunWith(AndroidJUnit4::class)
class CookbookUiPhysicalTest {
    @get:Rule val ui = createAndroidComposeRule<CookbookProofActivity>()
    private fun click(text: String) = ui.onNodeWithText(text).performScrollTo().performClick()
    private fun fill(label: String, text: String) = ui.onNode(hasSetTextAction() and hasText(label)).performScrollTo().performTextReplacement(text)
    private fun present(tag: String) = ui.onAllNodesWithTag(tag).fetchSemanticsNodes().isNotEmpty()
    private fun shown() = ui.onNodeWithTag("dish-display").fetchSemanticsNode().config[SemanticsProperties.Text].joinToString("") { it.text }
    private fun clip(): String? = (ui.activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
        .primaryClip?.takeIf { it.itemCount == 1 }?.getItemAt(0)?.text?.toString()
    private var diagnostics: CookbookUiDiagnostics? = null
    private fun report(phase:String) {
        val flags = listOf("manual-recipe","menu-search","dish-display","dish-copy","cook-error")
            .associateWith { runCatching { present(it) }.getOrDefault(false) }.toMutableMap()
        flags["recipeMatchesSynthetic"] = runCatching {
            val config=ui.onNodeWithTag("manual-recipe").fetchSemanticsNode().config
            config.contains(SemanticsProperties.EditableText) && config[SemanticsProperties.EditableText].text=="abcd#1"
        }.getOrDefault(false)
        var lifecycle="UNAVAILABLE"
        flags["diagnosticLifecycleReadSucceeded"] = false
        flags["diagnosticStateReadSucceeded"] = false
        flags["diagnosticEnvironmentReadSucceeded"] = false
        runCatching { ui.runOnIdle {
            lifecycle=ui.activity.lifecycle.currentState.name
            flags["diagnosticLifecycleReadSucceeded"] = true
            runCatching {
                flags.putAll(CookbookUiDiagnostics.environmentFlags(ui.activity))
                flags["diagnosticEnvironmentReadSucceeded"] = true
            }
            flags.putAll(CookbookUiDiagnostics.stateFlags(ui.activity))
            flags["diagnosticStateReadSucceeded"] = true
        } }
        diagnostics?.report(phase,lifecycle,flags)
    }
    private fun exact(expected: String) = assertTrue("Synthetic Dish equality", shown() == expected)
    @Test fun realSetupOfflineMappingCookingRevealClipboardAndBackgroundLock() {
        val directory = ui.activity.proofDirectory
        val file = File(directory,"cookbook-v1.json")
        diagnostics=CookbookUiDiagnostics(ui.activity.application)
        try {
            // Explicit controlled experiment only: retain the production screen policy.
            if (InstrumentationRegistry.getArguments().getString("passchefProofKeepScreenOn") == "false") {
                ui.runOnIdle { ui.activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }
            }
            if(ui.onAllNodesWithText("EN").fetchSemanticsNodes().isNotEmpty()) ui.onNodeWithText("EN").performClick()
            fill("Master password","SYNTHETIC_UI_Kitchen_2026")
            fill("Spice jar 1 (setup only)","aB!*")
            click("Set up / recover with password")
            // Actual per-operation biometric prompt when available: owner must authenticate.
            ui.waitUntil(120_000) { present("menu-search") }
            val original = file.readBytes()
            click("Cook")
            ui.onNodeWithTag("manual-recipe").performTextInput("abcd#1")
            ui.onNodeWithTag("manual-cook").performScrollTo().performClick()
            ui.waitUntil(10_000) { present("dish-display") }
            exact("••••••••")
            ui.onNodeWithTag("dish-reveal").performScrollTo().performClick(); exact("aB!*abcd")
            report("before-reveal-delay")
            Thread.sleep(10_500) // real elapsed time/Handler, not a synthetic Compose clock
            report("after-reveal-delay")
            exact("••••••••")
            ui.onNodeWithTag("dish-copy").performScrollTo().performClick()
            assertTrue("Owned Dish clipboard equality", clip() == "aB!*abcd")
            ui.onNodeWithTag("manual-recipe").performTextReplacement("invalid Recipe")
            assertTrue("Editing hides Dish immediately", !present("dish-display"))
            ui.onNodeWithTag("manual-cook").performScrollTo().performClick()
            ui.waitUntil(10_000) { present("cook-error") }
            assertTrue("Invalid Recipe has no Copy", !present("dish-copy"))
            val parsed = RecipeParser.parse("abcd#1")!!
            val tag = TagCodec().compute(parsed,"aB!*","Menu_0123456789")
            ui.onNodeWithTag("manual-recipe").performTextReplacement("abcd#1.$tag")
            ui.onNodeWithTag("manual-cook").performScrollTo().performClick()
            ui.waitUntil(10_000) { present("cook-error") }
            assertTrue("Missing Menu refuses tagged Dish", !present("dish-display"))
            click("Sources")
            ui.waitUntil(10_000) { ui.onNodeWithTag("source-id").fetchSemanticsNode().config.contains(SemanticsProperties.Disabled).not() }
            ui.onNodeWithTag("source-id").performTextInput("Menu_0123456789")
            fill("Local tab label","Synthetic Menu")
            ui.onNodeWithTag("source-table").performTextInput("Label\tRecipe\tUsername\nMail\tabcd#1\t00123\nBank\tdef#1\té\nWork\tghi#1\tユーザー")
            click("Preview columns")
            ui.waitUntil(10_000) { present("confirm-columns") }
            ui.onNodeWithTag("confirm-columns").performScrollTo().performClick()
            ui.waitUntil(10_000) { !present("confirm-columns") }
            // A masked, successfully tagged Dish has no legacy warning to trigger redraw.
            click("Cook"); click("Synthetic Menu")
            ui.onNodeWithTag("manual-recipe").performTextInput("abcd#1.$tag")
            ui.onNodeWithTag("manual-cook").performScrollTo().performClick()
            ui.waitUntil(10_000) { present("dish-display") }; exact("••••••••")
            ui.onNodeWithTag("manual-recipe").performTextReplacement("abcd#1.aaaa")
            assertTrue("Tagged masked edit clears Dish/Copy immediately", !present("dish-display") && !present("dish-copy"))
            click("Menu")
            ui.onNodeWithTag("menu-search").performTextInput("0012")
            click("Mail")
            ui.waitUntil(10_000) { present("dish-display") }; exact("••••••••")
            ui.onNodeWithTag("dish-reveal").performScrollTo().performClick(); exact("aB!*abcd")
            ui.onNodeWithTag("dish-copy").performScrollTo().performClick()
            assertTrue("Fresh Dish copy starts owned timer", clip() == "aB!*abcd")
            click("Copy username")
            assertTrue("Username remains literal", clip() == "00123")
            // A previous Dish timer must never erase a separately copied username.
            Thread.sleep(30_500)
            assertTrue("Dish timer preserves username copy", clip() == "00123")
            click("Back")
            assertTrue("Back clears old Dish", !present("dish-display"))
            assertTrue("Cooking preserves encrypted jars", original.contentEquals(file.readBytes()))
            ui.activityRule.scenario.moveToState(Lifecycle.State.CREATED)
            ui.activityRule.scenario.moveToState(Lifecycle.State.RESUMED)
            ui.waitUntil(10_000) { ui.onAllNodesWithText("Open with biometric").fetchSemanticsNodes().isNotEmpty() }
            assertTrue("Background clears Cookbook detail", !present("dish-display") && !present("menu-search"))
        } finally {
            diagnostics?.close();diagnostics=null
            ui.activityRule.scenario.close()
            // Only aliases referenced by this unique test-owned record, never an owner file.
            runCatching {
                val record = VaultRepository(AtomicRecordStore(file)).record()
                val crypto = KeystoreCrypto()
                record?.let { (listOf(it.alias) + it.retiredAliases).filter { alias -> alias.isNotEmpty() }.forEach(crypto::deleteKey) }
            }
            directory.deleteRecursively()
        }
    }
}
